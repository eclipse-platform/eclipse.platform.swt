// SPDX-License-Identifier: Apache-2.0
// Ported from hsoliwal/com.synexia 46e296ad537c6ecbd62b9783874e8f1714e278a9 synexia-mindex/runtime/src/main/java/com/synexia/mindex/swap/TieredSwapCache.java; package renamed only. Notice: docs/m3/item-residency/THIRD_PARTY_NOTICES.md
package org.eclipse.swt.internal.swap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Synchronous, deterministic HOT/WARM/COLD residency for immutable values.
 *
 * <p>No per-cold-key heap index, hidden threads, wall-clock eviction or weak-reference behavior.
 * All I/O and loaders are serialized per cache, giving single-flight successful first access.
 * Weight is codec-accounted, not JVM RSS.</p>
 *
 * <p>The configured {@link Budget} is the hard safety ceiling. {@link #readjust} may apply a
 * smaller active budget from a live signal ratio without mutating that hard ceiling. Explicit
 * demand remains available even when optional residency is zero: a decoded value is returned under
 * a transient lease and is not inserted into HOT/WARM state.</p>
 */
public final class TieredSwapCache<T> implements AutoCloseable {
    public enum Policy { LRU, MRU }
    public enum Tier { HOT, WARM, NONRESIDENT }

    public interface Codec<T> {
        byte[] encode(T value) throws IOException;
        T decode(byte[] bytes) throws IOException;
        /** Conservative retained payload estimate, including nested values; must be positive. */
        long weight(T value);
    }

    @FunctionalInterface
    public interface Loader<T> {
        T load(long id) throws IOException;
    }

    public record Budget(long bytes, int entries, long hotBytes) {
        public Budget {
            if (bytes <= 0 || entries <= 0 || hotBytes < 0 || hotBytes > bytes) {
                throw new IllegalArgumentException("invalid budget");
            }
        }
    }

    public record Stats(
            long hits,
            long misses,
            long loads,
            long evictions,
            long promotions,
            long demotions,
            long bytes,
            int entries,
            long pins) {}

    /** Additive diagnostics for the signal-driven active plane. */
    public record DynamicStats(
            double ratio,
            long activeBytes,
            int activeEntries,
            long activeHotBytes,
            int physicalShards,
            long transientLoads,
            long transientPins,
            long transientPinnedBytes) {
        public DynamicStats {
            if (!Double.isFinite(ratio)
                    || ratio < 0.0
                    || ratio > 1.0
                    || activeBytes < 0L
                    || activeEntries < 0
                    || activeHotBytes < 0L
                    || activeHotBytes > activeBytes
                    || physicalShards <= 0
                    || transientLoads < 0L
                    || transientPins < 0L
                    || transientPinnedBytes < 0L) {
                throw new IllegalArgumentException("invalid dynamic swap stats");
            }
        }
    }

    public record Pressure(
            long targetBytes,
            int targetEntries,
            long bytes,
            int entries,
            long pinnedBytes,
            int pinnedEntries) {
        public boolean satisfied() {
            return bytes <= targetBytes && entries <= targetEntries;
        }
    }

    public static final class CapacityException extends IOException {
        private static final long serialVersionUID = 1L;

        public CapacityException(String message) {
            super(message);
        }
    }

    private static final class Entry<T> {
        final long id;
        final long weight;
        final T value;
        int pins;

        Entry(long id, T value, long weight) {
            this.id = id;
            this.value = value;
            this.weight = weight;
        }
    }

    /** Holds only an ID and optional loader; creating it performs no database read. */
    public final class Ref {
        private final long id;
        private final Loader<T> loader;

        private Ref(long id, Loader<T> loader) {
            this.id = id;
            this.loader = loader;
        }

        public long id() {
            return id;
        }

        public Lease acquire() throws IOException {
            return TieredSwapCache.this.acquire(id, loader);
        }
    }

    /** A closed lease drops its strong reference. Do not retain its value beyond lease lifetime. */
    public final class Lease implements AutoCloseable {
        private Entry<T> entry;
        private boolean resident;

        private Lease(Entry<T> entry, boolean resident) {
            this.entry = entry;
            this.resident = resident;
        }

        private TieredSwapCache<T> owner() {
            return TieredSwapCache.this;
        }

        public T value() {
            synchronized (TieredSwapCache.this) {
                if (entry == null) throw new IllegalStateException("closed lease");
                return entry.value;
            }
        }

        public long id() {
            synchronized (TieredSwapCache.this) {
                if (entry == null) throw new IllegalStateException("closed lease");
                return entry.id;
            }
        }

        public boolean resident() {
            synchronized (TieredSwapCache.this) {
                if (entry == null) throw new IllegalStateException("closed lease");
                return resident;
            }
        }

        @Override
        public void close() {
            synchronized (TieredSwapCache.this) {
                if (entry == null) return;
                if (resident) {
                    if (entry.pins <= 0 || pins <= 0) {
                        throw new IllegalStateException("resident pin accounting underflow");
                    }
                    if (entry.pins == 1) {
                        if (pinnedResidentEntries <= 0 || pinnedResidentBytes < entry.weight) {
                            throw new IllegalStateException("pinned resident accounting underflow");
                        }
                        pinnedResidentEntries--;
                        pinnedResidentBytes -= entry.weight;
                    }
                    entry.pins--;
                    pins--;
                } else {
                    if (transientPins <= 0 || transientPinnedBytes < entry.weight) {
                        throw new IllegalStateException("transient pin accounting underflow");
                    }
                    transientPins--;
                    transientPinnedBytes -= entry.weight;
                }
                entry = null;
                // A pressure shrink blocked only by this lease becomes effective immediately.
                trimResidents(activeBytes, activeEntries);
            }
        }
    }

    private final SwapStore store;
    private final Codec<T> codec;
    private final boolean ownsStore;
    private final LinkedHashMap<Long, Entry<T>> warm = new LinkedHashMap<>(16, 0.75f, true);
    private final LinkedHashMap<Long, Entry<T>> hot = new LinkedHashMap<>(16, 0.75f, true);
    private final Set<Long> loading = new HashSet<>();

    private Budget budget;
    private Policy policy;

    private long activeBytes;
    private int activeEntries;
    private long activeHotBytes;
    private double activeRatio = 1.0;
    private int physicalShards = 1;

    private long bytes;
    private long hotBytes;
    private long pins;
    // Exact zero/one pin transitions make admission and pressure reporting O(1).
    private long pinnedResidentBytes;
    private int pinnedResidentEntries;
    private long hits;
    private long misses;
    private long loads;
    private long evictions;
    private long promotions;
    private long demotions;

    private long transientLoads;
    private long transientPins;
    private long transientPinnedBytes;

    private boolean closed;

    public TieredSwapCache(
            SwapStore store,
            Codec<T> codec,
            Budget budget,
            Policy policy,
            boolean ownsStore) {
        this.store = Objects.requireNonNull(store);
        this.codec = Objects.requireNonNull(codec);
        this.budget = Objects.requireNonNull(budget);
        this.policy = Objects.requireNonNull(policy);
        this.ownsStore = ownsStore;
        activeBytes = budget.bytes();
        activeEntries = budget.entries();
        activeHotBytes = budget.hotBytes();
    }

    public synchronized Ref ref(long id) {
        return deferred(id, null);
    }

    public synchronized Ref deferred(long id, Loader<T> loader) {
        checkOpen();
        SwapStore.requireId(id);
        return new Ref(id, loader);
    }

    public synchronized Lease acquire(long id) throws IOException {
        return acquire(id, null);
    }

    public synchronized Lease acquire(long id, Loader<T> loader) throws IOException {
        checkOpen();
        SwapStore.requireId(id);

        Entry<T> entry = hot.get(id);
        if (entry != null) {
            hits++;
            return pin(entry);
        }

        entry = warm.get(id);
        if (entry != null) {
            hits++;
            if (entry.weight <= activeHotBytes && activeHotBytes > 0L) {
                warm.remove(id);
                hot.put(id, entry);
                hotBytes += entry.weight;
                promotions++;
                demote();
            }
            return pin(entry);
        }

        if (!loading.add(id)) throw new IOException("recursive load of record " + id);
        misses++;
        try {
            byte[] encoded = store.read(id);
            boolean fresh = encoded == null;
            T value;

            if (fresh) {
                if (loader == null) throw new IOException("missing record " + id);
                value = Objects.requireNonNull(loader.load(id), "loader value");
                encoded = Objects.requireNonNull(codec.encode(value), "encoded value");
                validateEncodedRecord(encoded);
                // Decode the durable representation, not an externally mutable supplier instance.
                value = Objects.requireNonNull(codec.decode(encoded), "decoded value");
            } else {
                validateEncodedRecord(encoded);
                value = Objects.requireNonNull(codec.decode(encoded), "decoded value");
            }

            long weight = validateWeight(value);

            boolean retain = canFit(weight);
            if (!retain && !activeBudgetRestricted()) {
                throw new CapacityException("all available capacity is pinned");
            }

            // A failed write must leave resident entries and recency unchanged.
            if (fresh) store.write(Map.of(id, encoded));
            loads++;

            entry = new Entry<>(id, value, weight);
            if (!retain) {
                transientLoads++;
                return pinTransient(entry);
            }

            makeRoom(weight);
            warm.put(id, entry);
            bytes += weight;
            return pin(entry);
        } finally {
            loading.remove(id);
        }
    }

    /** Persist a value cold, without creating a residency entry or retaining the caller's object. */
    public synchronized void put(long id, T value) throws IOException {
        checkOpen();
        SwapStore.requireId(id);
        T checked = Objects.requireNonNull(value, "value");
        byte[] encoded = Objects.requireNonNull(codec.encode(checked), "encoded value");
        validateEncodedRecord(encoded);
        // Validate the durable representation and its reload weight before publication.
        T durable = Objects.requireNonNull(codec.decode(encoded), "decoded value");
        validateWeight(durable);
        store.write(Map.of(id, encoded));
    }

    /**
     * Apply a signal-derived active budget without changing the configured hard ceiling.
     *
     * <p>Pinned residents survive an unsatisfied shrink. Future explicit misses may be served
     * transiently while optional residency is constrained.</p>
     */
    public synchronized Pressure readjust(SwapBudgetPlane.Snapshot snapshot) {
        checkOpen();
        Objects.requireNonNull(snapshot, "snapshot");
        if (snapshot.bytes() > budget.bytes()
                || snapshot.entries() > budget.entries()
                || snapshot.hotBytes() > budget.hotBytes()) {
            throw new IllegalArgumentException("active budget exceeds hard budget");
        }

        activeRatio = snapshot.ratio();
        activeBytes = snapshot.bytes();
        activeEntries = snapshot.entries();
        activeHotBytes = snapshot.hotBytes();
        physicalShards = snapshot.physicalShards();

        demote();
        return trimTo(activeBytes, activeEntries);
    }

    private Lease pin(Entry<T> entry) {
        chargeResidentPin(entry);
        return new Lease(entry, true);
    }

    private Lease pinTransient(Entry<T> entry) {
        if (transientPins == Long.MAX_VALUE
                || transientPinnedBytes > Long.MAX_VALUE - entry.weight) {
            throw new IllegalStateException("transient pin counter exhausted");
        }
        transientPins++;
        transientPinnedBytes += entry.weight;
        return new Lease(entry, false);
    }

    /**
     * Attempt to convert this cache's live transient lease into retained WARM residency.
     *
     * <p>The caller must first adjust the active budget if it wants to lend this cache additional
     * capacity. Failure is non-destructive: the lease remains transient and valid.</p>
     */
    synchronized boolean retain(Lease lease) {
        checkOpen();
        Objects.requireNonNull(lease, "lease");
        if (lease.owner() != this) throw new IllegalArgumentException("lease belongs to another cache");
        if (lease.entry == null) throw new IllegalStateException("closed lease");
        if (lease.resident) return true;

        Entry<T> entry = lease.entry;
        // A transient load must never replace or redirect an already resident owner.
        // containsKey is observational and does not promote access-order LinkedHashMap entries.
        if (warm.containsKey(entry.id) || hot.containsKey(entry.id)) return false;
        if (!canFit(entry.weight)) return false;
        if (transientPins <= 0L || transientPinnedBytes < entry.weight) {
            throw new IllegalStateException("transient pin accounting underflow");
        }
        if (entry.pins != 0 || pins == Long.MAX_VALUE
                || pinnedResidentEntries == Integer.MAX_VALUE
                || pinnedResidentBytes > Long.MAX_VALUE - entry.weight) {
            throw new IllegalStateException("resident pin counter exhausted");
        }

        makeRoom(entry.weight);
        warm.put(entry.id, entry);
        bytes += entry.weight;
        chargeResidentPin(entry);
        transientPins--;
        transientPinnedBytes -= entry.weight;
        lease.resident = true;
        return true;
    }

    private void chargeResidentPin(Entry<T> entry) {
        if (entry.pins == Integer.MAX_VALUE || pins == Long.MAX_VALUE) {
            throw new IllegalStateException("pin counter exhausted");
        }
        long nextPinnedBytes = pinnedResidentBytes;
        int nextPinnedEntries = pinnedResidentEntries;
        if (entry.pins == 0) {
            nextPinnedBytes = Math.addExact(pinnedResidentBytes, entry.weight);
            nextPinnedEntries = Math.addExact(pinnedResidentEntries, 1);
        }
        entry.pins++;
        pins++;
        pinnedResidentBytes = nextPinnedBytes;
        pinnedResidentEntries = nextPinnedEntries;
    }

    private void validateEncodedRecord(byte[] encoded) throws IOException {
        int length = Objects.requireNonNull(encoded, "encoded").length;
        if (length > store.maxRecordBytes() || length > store.maxBatchBytes()) {
            throw new IOException("record limit");
        }
    }

    private long validateWeight(T value) throws CapacityException {
        long weight = codec.weight(Objects.requireNonNull(value, "value"));
        if (weight <= 0L || weight > budget.bytes()) {
            throw new CapacityException("record weight does not fit hard budget: " + weight);
        }
        return weight;
    }

    private boolean canFit(long extra) {
        if (activeBytes <= 0L || activeEntries <= 0 || extra > activeBytes) return false;
        if (pinnedResidentBytes > activeBytes || pinnedResidentEntries >= activeEntries) return false;
        return extra <= activeBytes - pinnedResidentBytes;
    }

    private boolean activeBudgetRestricted() {
        return activeBytes < budget.bytes()
                || activeEntries < budget.entries()
                || activeHotBytes < budget.hotBytes();
    }

    private int size() {
        return warm.size() + hot.size();
    }

    private void makeRoom(long extra) {
        while (bytes > activeBytes - extra || size() >= activeEntries) {
            if (!evict()) throw new AssertionError("active capacity preflight failed");
        }
    }

    private void demote() {
        while (hotBytes > activeHotBytes && !hot.isEmpty()) {
            Entry<T> first = hot.firstEntry().getValue();
            hot.remove(first.id);
            hotBytes -= first.weight;
            warm.put(first.id, first);
            demotions++;
        }
    }

    private Entry<T> victim(LinkedHashMap<Long, Entry<T>> segment) {
        var candidates =
                policy == Policy.LRU
                        ? segment.sequencedValues()
                        : segment.reversed().values();
        for (Entry<T> entry : candidates) {
            if (entry.pins == 0) return entry;
        }
        return null;
    }

    private boolean evict() {
        Entry<T> entry = victim(warm);
        if (entry != null) {
            warm.remove(entry.id);
        } else {
            entry = victim(hot);
            if (entry == null) return false;
            hot.remove(entry.id);
            hotBytes -= entry.weight;
        }
        bytes -= entry.weight;
        evictions++;
        return true;
    }

    /** Best-effort pressure release. Pins are reported rather than silently invalidated. */
    public synchronized Pressure trimTo(long targetBytes, int targetEntries) {
        checkOpen();
        if (targetBytes < 0L || targetEntries < 0) {
            throw new IllegalArgumentException("negative target");
        }
        trimResidents(targetBytes, targetEntries);
        return new Pressure(
                targetBytes,
                targetEntries,
                bytes,
                size(),
                pinnedResidentBytes,
                pinnedResidentEntries);
    }

    private void trimResidents(long targetBytes, int targetEntries) {
        while ((bytes > targetBytes || size() > targetEntries) && evict()) {
            // bounded by current residency; pinned values remain untouched
        }
    }

    /**
     * Change the hard budget.
     *
     * <p>Failed shrink keeps the old hard and active budgets; it may still have released unpinned
     * entries while proving that the replacement could not be satisfied.</p>
     */
    public synchronized boolean resize(Budget replacement) {
        checkOpen();
        Objects.requireNonNull(replacement);
        if (!trimTo(replacement.bytes(), replacement.entries()).satisfied()) return false;

        budget = replacement;
        activeRatio = 1.0;
        activeBytes = replacement.bytes();
        activeEntries = replacement.entries();
        activeHotBytes = replacement.hotBytes();
        physicalShards = 1;
        demote();
        return true;
    }

    public synchronized void policy(Policy replacement) {
        checkOpen();
        policy = Objects.requireNonNull(replacement);
    }

    public synchronized Tier tier(long id) {
        checkOpen();
        SwapStore.requireId(id);
        return hot.containsKey(id)
                ? Tier.HOT
                : warm.containsKey(id) ? Tier.WARM : Tier.NONRESIDENT;
    }

    public synchronized List<Long> residentIds() {
        checkOpen();
        List<Long> result = new ArrayList<>(warm.keySet());
        result.addAll(hot.keySet());
        return List.copyOf(result);
    }

    /**
     * Resident IDs ordered from hottest/most-recent to colder/older entries.
     *
     * <p>This is metadata only: it does not read the cold store, pin values, or alter recency.</p>
     */
    public synchronized List<Long> residentIdsByHeat(int limit) {
        checkOpen();
        if (limit < 0) throw new IllegalArgumentException("negative limit");
        if (limit == 0) return List.of();

        List<Long> result = new ArrayList<>(Math.min(limit, size()));
        for (Long id : hot.reversed().keySet()) {
            if (result.size() == limit) return List.copyOf(result);
            result.add(id);
        }
        for (Long id : warm.reversed().keySet()) {
            if (result.size() == limit) break;
            result.add(id);
        }
        return List.copyOf(result);
    }

    public synchronized Stats stats() {
        return new Stats(
                hits,
                misses,
                loads,
                evictions,
                promotions,
                demotions,
                bytes,
                size(),
                saturatingAdd(pins, transientPins));
    }

    public synchronized DynamicStats dynamicStats() {
        return new DynamicStats(
                activeRatio,
                activeBytes,
                activeEntries,
                activeHotBytes,
                physicalShards,
                transientLoads,
                transientPins,
                transientPinnedBytes);
    }

    private void checkOpen() {
        if (closed) throw new IllegalStateException("closed cache");
    }

    @Override
    public synchronized void close() throws IOException {
        if (closed) return;
        if (pins != 0L || transientPins != 0L || !loading.isEmpty()) {
            throw new IOException("outstanding leases or loaders");
        }
        if (ownsStore) store.close();
        warm.clear();
        hot.clear();
        bytes = 0L;
        hotBytes = 0L;
        closed = true;
    }

    private static long saturatingAdd(long left, long right) {
        if (right > 0L && left > Long.MAX_VALUE - right) return Long.MAX_VALUE;
        return left + right;
    }
}
