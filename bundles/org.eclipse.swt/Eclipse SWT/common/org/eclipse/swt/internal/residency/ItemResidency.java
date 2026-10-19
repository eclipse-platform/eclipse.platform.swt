/*
 * Copyright 2026 Hitesh Soliwal and contributors
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
 *
 * Dual-licensed by the rights holder's decision of 2026-10-13: you may use this file under the
 * Eclipse Public License 2.0 (https://www.eclipse.org/legal/epl-2.0/) or the Apache License 2.0
 * (https://www.apache.org/licenses/LICENSE-2.0).
 */
package org.eclipse.swt.internal.residency;

import java.util.Arrays;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.PackedHeapGrowth;

/**
 * Shared, toolkit-neutral M3 item-residency engine (design: docs/m3/M3_ITEM_RESIDENCY.md). State
 * lives here in sparse lanes and patterns, resident payload slots, a swap tier and parent-owned data
 * lanes. Residency follows the viewport plus overscan and memory pressure, never the caller. Realizes
 * Synexia invariant M3-LAZY-OBJECT-PRESSURE-1 (#10889): caller-owned objects are never discarded.
 *
 * <p><b>Proxy objects mapped to internal data structures.</b> Every public item, node or row object
 * (SWT {@code TableItem}/{@code TreeItem}, Nebula {@code GridItem}, a Swing row) is a <em>proxy</em>:
 * {@code (parent, item index, generation)} created by a {@link HandleFactory}. It stores no state;
 * each getter and setter forwards to this engine, which resolves the item index through the
 * directory to lanes and the current payload slot on every call. Consequences:</p>
 * <ul>
 * <li>A proxy keys the logical item index, never the payload slot: slots are recycled by eviction,
 * so DESTROY, recreate, swap-out and swap-in never stale a proxy. A pinned proxy (generation 0)
 * re-resolves through the directory and stays the same object for the life of its item.</li>
 * <li>Insert and remove re-key pinned proxies through {@code HandleFactory.rebind(proxy, newIndex, 0)}.
 * A removed item's proxy is rebound to index -1: it is stale, {@link #isCurrent} reports false, and
 * the adapter fails exactly as the toolkit's disposed-object contract says (SWT:
 * {@code SWT.ERROR_WIDGET_DISPOSED}).</li>
 * <li>A recyclable flyweight (generation &gt; 0, behaviour-only call sites only) is stale once rebound
 * to another item: {@link #isCurrent} reports false and the adapter fails the same way. It never
 * re-resolves silently.</li>
 * </ul>
 *
 * <p>GWT-safe: {@code int} only, no reliance on int overflow, no {@code clone()}, no
 * {@code Objects.checkIndex}, no {@code java.nio}, no threads. Swap and pressure sources are
 * pluggable; their desktop implementations are {@link ItemSwapSpill} and {@link ItemSwapPressure}.</p>
 */
public final class ItemResidency {
	// Construction flags: one engine-owned int (no Widget.state bit, no SWT style bit).
	public static final int RESIDENCY_SWAP = 1, RESIDENCY_DESTROY = 1 << 1, IDENTITY_PIN_ALL = 1 << 2,
			IDENTITY_BY_METADATA = 1 << 3, PRESSURE_SAMPLED = 1 << 4;

	// Always-resident bit lanes.
	public static final int LANE_EXPANDED = 0, LANE_HAS_CHILDREN = 1, LANE_CHILDREN_KNOWN = 2,
			LANE_CHILDREN_LOADED = 3, LANE_LOADING_PLACEHOLDER = 4, LANE_SELECTED = 5, LANE_FOCUSED = 6,
			LANE_HOVERED = 7, LANE_DISABLED = 8, LANE_FILTERED_OUT = 9, LANE_CHECKED = 10, LANE_GRAYED = 11,
			LANE_CHECKABLE = 12, LANE_DIRTY_PAINT = 13, LANE_DIRTY_MEASURE = 14, LANE_DIRTY_DATA = 15,
			LANE_PINNED = 16, LANE_TIER_LO = 17, LANE_TIER_HI = 18, LANE_CALLER_SET = 19,
			LANE_MATERIALIZED = 20, LANE_DATA_REQUESTED = 21, LANE_DATA_LOADED = 22, LANES = 23;
	/** Lanes only the engine writes. */
	public static final int ENGINE_LANES = 1 << LANE_PINNED | 1 << LANE_TIER_LO | 1 << LANE_TIER_HI
			| 1 << LANE_CALLER_SET | 1 << LANE_MATERIALIZED | 1 << LANE_DATA_REQUESTED | 1 << LANE_DATA_LOADED;

	public static final int TIER_DESTROYED = 0, TIER_HOT = 1, TIER_WARM = 2, TIER_COLD = 3;

	// Payload ints per item, then per cell.
	public static final int I_FOREGROUND = 0, I_BACKGROUND = 1, I_FONT = 2, I_IMAGE = 3, I_ROW_HEIGHT = 4,
			I_INDENT = 5, I_LEVEL = 6, I_TOOLTIP = 7, I_ACCESSIBLE = 8, I_USER = 9, ITEM_INTS = 10;
	public static final int C_FOREGROUND = 0, C_BACKGROUND = 1, C_FONT = 2, C_IMAGE = 3, CELL_INTS = 4;

	public static final int SITE_UNKNOWN = 0, SITE_BEHAVIOUR_ONLY = 1, SITE_RETAINS = 2;

	public static final int STAT_MATERIALIZED = 0, STAT_RECREATED = 1, STAT_SWAP_OUT = 2, STAT_SWAP_OUT_WRITES = 3,
			STAT_SWAP_IN = 4, STAT_DESTROYED = 5, STAT_UNEVICTABLE = 6, STAT_PRESSURE_SAMPLES = 7,
			STAT_TELEMETRY_FAILURES = 8, STAT_PINNED = 9, STAT_RECYCLED = 10, STAT_DATA_SWAPPED = 11,
			STAT_DATA_RESTORED = 12, COUNTERS = 13, STAT_RESIDENT = 20, STAT_BUDGET = 21, STAT_PERMILLE = 22,
			STAT_REQUEST_RANGES = 23, STAT_REQUEST_ITEMS = 24, STAT_CANCEL_RANGES = 25, STAT_CANCEL_ITEMS = 26,
			STAT_LANE_BYTES = 27, STAT_DIRECTORY_BYTES = 28, STAT_SLOT_BYTES = 29, STAT_SLOT_CAPACITY = 30,
			STAT_MATERIALIZED_DURING_PAINT = 31;

	public static final int SOFT_START = 700, HARD_STOP = 960, RECOVERY_STEP = 125;

	/** Supplies payload in ranges; writes between beginData/endData are source-backed. */
	public interface DataSource {
		void request (ItemResidency owner, int from, int to);
		default void cancel (ItemResidency owner, int from, int to) {
		}
	}

	/** COLD tier storage (desktop: {@link ItemSwapSpill} over the ported TieredSwapCache; web: none). */
	public interface Spill {
		int write (byte [] data, int length);
		/** The record's exact bytes; the engine only reads them. */
		byte [] read (int record);
		void free (int record);
		void close ();
	}

	/** Availability in permille (1000 = no pressure); throwing means telemetry failure. */
	public interface Pressure {
		int availablePermille ();
		default boolean urgent () {
			return false;
		}
	}

	/**
	 * Creates and rebinds proxies {@code (parent, item index, generation)}. Generation 0 is a pinned
	 * proxy; item -1 means the item was removed and the proxy is stale (disposed contract).
	 */
	public interface HandleFactory {
		Object create (int item, int generation);
		void rebind (Object handle, int item, int generation);
	}

	/** Compile-time site metadata: low 2 bits are the SITE_* class, higher bits the escape mask. */
	public interface SiteMetadata {
		int classify (int siteKey);
	}

	/** Immutable-value codec: registering it declares accepted values immutable and swappable. */
	public interface DataCodec {
		boolean accepts (Object value);
		byte [] encode (Object value);
		Object decode (byte [] bytes);
	}

	final int config;
	final ItemLanes lanes = new ItemLanes (LANES);
	final ItemDirectory directory = new ItemDirectory ();
	final ItemSlots slots;
	final ItemRequests requests = new ItemRequests ();
	final ItemSparseTable pins = new ItemSparseTable (), data = new ItemSparseTable (), keyed = new ItemSparseTable ();
	final int hardSlots;
	final ItemBitSet residencyPins = new ItemBitSet ();
	private final int [][] pinScratch = {new int [16]};
	DataSource source;
	Spill spill;
	Pressure pressure;
	HandleFactory factory;
	SiteMetadata metadata;
	DataCodec codec;
	int sampleEvery = 64, operations, permille = 1000, budget;
	int windowStart, windowEnd, prefetchRows, hotSize, hotRunCount;
	int [] hotRuns = new int [8], priority = new int [16], previousRuns = new int [8];
	int filling = -1, fillingSlot = -1, hand, painting, materializedDuringPaint;
	final int [] counters = new int [COUNTERS];
	final Object [] ring = new Object [4];
	final int [] ringItem = new int [4], ringGeneration = new int [4];
	int ringNext, generation;
	int editorItem = -1, editorColumn, editorCaret, editorSelectionStart, editorSelectionEnd;
	String editorText;

	/**
	 * Constructs a new engine for items with the given number of columns.
	 *
	 * @param config the construction flags (<code>RESIDENCY_SWAP</code>, <code>RESIDENCY_DESTROY</code>, <code>IDENTITY_PIN_ALL</code>, <code>IDENTITY_BY_METADATA</code>, <code>PRESSURE_SAMPLED</code>)
	 * @param columns the number of payload columns, at least 1
	 * @param hardSlots the hard ceiling of resident payload slots, at least 1
	 * @param source the data source that answers SetData requests, or <code>null</code>
	 * @param spill the COLD tier, or <code>null</code> (web: DESTROY only)
	 * @param pressure the pressure source, or <code>null</code>
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_ARGUMENT - if columns or hardSlots is less than 1</li>
	 * </ul>
	 */
	public ItemResidency (int config, int columns, int hardSlots, DataSource source, Spill spill, Pressure pressure) {
		if (columns < 1 || hardSlots < 1) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		this.config = config;
		this.hardSlots = hardSlots;
		this.source = source;
		this.spill = spill;
		this.pressure = pressure;
		slots = new ItemSlots (columns, ITEM_INTS, CELL_INTS);
		budget = hardSlots;
	}

	/**
	 * Returns the counter plus one, saturating at <code>Integer.MAX_VALUE</code>.
	 *
	 * @param counter the counter
	 * @return the incremented counter
	 */
	public static int inc (int counter) {
		return counter == Integer.MAX_VALUE ? counter : counter + 1;
	}

	/**
	 * Returns the counter plus a non-negative delta, saturating at <code>Integer.MAX_VALUE</code>.
	 *
	 * @param counter the counter
	 * @param delta the amount to add
	 * @return the new counter
	 */
	public static int add (int counter, int delta) {
		return counter > Integer.MAX_VALUE - delta ? Integer.MAX_VALUE : counter + delta;
	}

	private void count (int which) {
		counters [which] = inc (counters [which]);
	}

	/** FNV-1a 32 of {@code owner.name+desc@bci}; multiplication split so every step is exact in a double. */
	public static int siteKey (String owner, String name, String desc, int bci) {
		String s = owner + '.' + name + desc + '@' + bci;
		int h = 0x811C9DC5;
		for (int i = 0; i < s.length (); i++) {
			h ^= s.charAt (i);
			h = (h << 24) + (h << 8) + h * 0x93;
		}
		return h;
	}

	// ---- structure ---------------------------------------------------------------------------

	/**
	 * Returns the number of items.
	 *
	 * @return the number of items
	 */
	public int count () {
		return lanes.count;
	}

	private void checkItem (int item) {
		if (item < 0 || item >= lanes.count) SWT.error (SWT.ERROR_INVALID_RANGE);
	}

	/**
	 * Sets the number of items. Items at or above the new count lose their payload, lanes, data and pins.
	 *
	 * @param items the new number of items
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_ARGUMENT - if the count is negative</li>
	 * </ul>
	 */
	public void setCount (int items) {
		if (items < 0) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		requests.cancelAll (this, lanes, source);
		for (int i = items; i < lanes.count; i++) {
			dropPayload (i);
		}
		int first = pins.find (items);
		for (int j = first < 0 ? -first - 1 : first; factory != null && j < pins.size; j++) {
			factory.rebind (pins.values [j], -1, 0);
		}
		pins.truncate (items);
		data.truncate (items);
		keyed.truncate (items);
		if (editorItem >= items) editorItem = -1;
		if (items < lanes.count) residencyPins.setRange (items, lanes.count, false);
		lanes.setCount (items);
		invalidateRing ();
		clampWindow ();
	}

	/**
	 * Inserts a default item at the given index; later items, their lanes, payload, pins and data move up by one.
	 *
	 * @param index the index of the new item
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of items (inclusive)</li>
	 * </ul>
	 * @exception SWTError <ul>
	 *    <li>ERROR_ITEM_NOT_ADDED - if the number of items is already <code>Integer.MAX_VALUE</code></li>
	 * </ul>
	 */
	public void insert (int index) {
		if (index < 0 || index > lanes.count) SWT.error (SWT.ERROR_INVALID_RANGE);
		/* admission precedes every mutation: a full model is left untouched */
		if (lanes.count == Integer.MAX_VALUE) SWT.error (SWT.ERROR_ITEM_NOT_ADDED);
		requests.cancelAll (this, lanes, source);
		lanes.insert (index);
		residencyPins.shift (index, 1, lanes.count - 1, pinScratch);
		directory.insert (index, lanes.count - 1);
		slots.shiftItems (index, 1);
		rebindFrom (pins.shift (index, 1));
		data.shift (index, 1);
		keyed.shift (index, 1);
		if (editorItem >= index) editorItem++;
		invalidateRing ();
	}

	/**
	 * Removes the item at the given index; its pinned proxy is rebound to -1 (disposed).
	 *
	 * @param index the index of the item
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the item is not between 0 and the number of items minus 1 (inclusive)</li>
	 * </ul>
	 */
	public void remove (int index) {
		checkItem (index);
		requests.cancelAll (this, lanes, source);
		dropPayload (index);
		Object pinned = pins.put (index, null);
		if (pinned != null && factory != null) factory.rebind (pinned, -1, 0);
		data.put (index, null);
		keyed.put (index, null);
		lanes.remove (index);
		residencyPins.set (index, false);
		residencyPins.shift (index + 1, -1, lanes.count + 1, pinScratch);
		directory.remove (index, lanes.count + 1);
		slots.shiftItems (index + 1, -1);
		rebindFrom (pins.shift (index + 1, -1));
		data.shift (index + 1, -1);
		keyed.shift (index + 1, -1);
		if (editorItem == index) editorItem = -1;
		else if (editorItem > index) editorItem--;
		invalidateRing ();
		clampWindow ();
	}

	private void rebindFrom (int position) {
		for (int j = position; factory != null && j < pins.size; j++) {
			factory.rebind (pins.values [j], pins.keys [j], 0);
		}
	}

	/** Releases payload and spill record of one item (structural removal only). */
	private void dropPayload (int item) {
		int d = directory.get (item);
		if (d > 0) {
			freeRecord (d - 1);
			slots.release (d - 1);
		} else if (d < 0 && spill != null) {
			spill.free (-d - 1);
		}
		if (d != 0) directory.put (item, 0);
	}

	// ---- bit lanes -----------------------------------------------------------------------------

	/**
	 * Returns the state of one lane of an item.
	 *
	 * @param lane the lane (<code>LANE_*</code>)
	 * @param item the item index
	 * @return <code>true</code> when the bit is set
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the item is not between 0 and the number of items minus 1 (inclusive)</li>
	 * </ul>
	 */
	public boolean bit (int lane, int item) {
		checkItem (item);
		return lanes.get (lane, item);
	}

	/**
	 * Sets one user lane of an item. Inside a pattern this records an exception only when the value differs from the rule.
	 *
	 * @param lane the lane (<code>LANE_*</code>, not an engine-owned lane)
	 * @param item the item index
	 * @param on the new state
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the item is not between 0 and the number of items minus 1 (inclusive)</li>
	 * </ul>
	 */
	public void setBit (int lane, int item, boolean on) {
		checkItem (item);
		checkUserLane (lane);
		lanes.set (lane, item, on);
	}

	/**
	 * Sets or clears one user lane over a range of items, cutting any pattern there. Used for select-all and check-all.
	 *
	 * @param lane the lane
	 * @param from the first item (inclusive)
	 * @param to the last item (exclusive)
	 * @param on the new state
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the range is not within the items</li>
	 * </ul>
	 */
	public void setRange (int lane, int from, int to, boolean on) {
		checkUserLane (lane);
		if (from < 0 || to > lanes.count || from > to) SWT.error (SWT.ERROR_INVALID_RANGE);
		lanes.setRange (lane, from, to, on);
	}

	/**
	 * Returns the number of items whose lane is set in a range (patterns and exceptions included).
	 *
	 * @param lane the lane
	 * @param from the first item (inclusive)
	 * @param to the last item (exclusive)
	 * @return the count
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the range is not within the items</li>
	 * </ul>
	 */
	public int countRange (int lane, int from, int to) {
		if (from < 0 || to > lanes.count || from > to) SWT.error (SWT.ERROR_INVALID_RANGE);
		return lanes.countRange (lane, from, to);
	}

	/**
	 * Returns the first item at or after <code>from</code> whose lane is set.
	 *
	 * @param lane the lane
	 * @param from the first item to test
	 * @return the item index, or -1 when there is none
	 */
	public int nextSet (int lane, int from) {
		return from < 0 ? lanes.nextSet (lane, 0) : lanes.nextSet (lane, from);
	}

	/**
	 * Returns every lane of an item as a mask (bit = lane).
	 *
	 * @param item the item index
	 * @return the lane mask
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the item is not between 0 and the number of items minus 1 (inclusive)</li>
	 * </ul>
	 */
	public int gather (int item) {
		checkItem (item);
		return lanes.gather (item);
	}

	/**
	 * Stores state as a rule over {@code [start, end)}: item {@code i} is set when bit
	 * {@code (i - start) % stride} of {@code repeatMask} is set. O(1) memory for any range; explicit
	 * exceptions win over patterns, patterns win over the default.
	 */
	public void setPattern (int lane, int start, int end, int stride, int repeatMask) {
		checkUserLane (lane);
		if (start < 0 || end > lanes.count || start > end) SWT.error (SWT.ERROR_INVALID_RANGE);
		lanes.setPattern (lane, start, end, stride, repeatMask);
	}

	private static void checkUserLane (int lane) {
		if (lane < 0 || lane >= LANES || (ENGINE_LANES & (1 << lane)) != 0) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}

	/**
	 * Returns the residency tier of an item.
	 *
	 * @param item the item index
	 * @return <code>TIER_DESTROYED</code>, <code>TIER_HOT</code>, <code>TIER_WARM</code> or <code>TIER_COLD</code>
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the item is not between 0 and the number of items minus 1 (inclusive)</li>
	 * </ul>
	 */
	public int tier (int item) {
		checkItem (item);
		return (lanes.get (LANE_TIER_LO, item) ? 1 : 0) | (lanes.get (LANE_TIER_HI, item) ? 2 : 0);
	}

	private void setTier (int item, int tier) {
		lanes.set (LANE_TIER_LO, item, (tier & 1) != 0);
		lanes.set (LANE_TIER_HI, item, (tier & 2) != 0);
	}

	/** Membership in the HOT runs (visible plus overscan), binary search over sorted runs. */
	private boolean inWindow (int item) {
		int lo = 0, hi = hotRunCount - 1;
		while (lo <= hi) {
			int mid = (lo + hi) >>> 1;
			if (hotRuns [2 * mid + 1] <= item) lo = mid + 1;
			else if (hotRuns [2 * mid] > item) hi = mid - 1;
			else return true;
		}
		return false;
	}

	// ---- SetData negotiation ---------------------------------------------------------------------

	/** Opens a source-backed write scope for one item (SWT SetData). */
	public void beginData (int item) {
		checkItem (item);
		if (filling >= 0) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		int d = directory.get (item);
		int s = d > 0 ? d - 1 : d < 0 ? swapIn (item, -d - 1) : allocate (item);
		filling = item;
		fillingSlot = s;
	}

	/**
	 * Closes the source-backed write scope opened by <code>beginData</code> and marks the item loaded.
	 *
	 * @param item the item index
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_ARGUMENT - if no data scope is open for the item</li>
	 * </ul>
	 */
	public void endData (int item) {
		if (filling != item) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		filling = -1;
		fillingSlot = -1;
		lanes.set (LANE_DATA_REQUESTED, item, false);
		lanes.set (LANE_DATA_LOADED, item, true);
		count (lanes.get (LANE_MATERIALIZED, item) ? STAT_RECREATED : STAT_MATERIALIZED);
		lanes.set (LANE_MATERIALIZED, item, true);
		setTier (item, inWindow (item) ? TIER_HOT : TIER_WARM);
		// No trim here: a read that triggered this fill must still see the value (transient read at
		// zero residency); the next allocation, viewport change or pressure sample trims.
	}

	/** SWT {@code clear(index)}: drops payload (caller-set included) so the next access re-requests. */
	public void invalidate (int item) {
		checkItem (item);
		dropPayload (item);
		lanes.set (LANE_CALLER_SET, item, false);
		lanes.set (LANE_DATA_LOADED, item, false);
		lanes.set (LANE_DIRTY_DATA, item, true);
		setTier (item, TIER_DESTROYED);
	}

	/**
	 * Sets how many rows beyond the overscan the engine requests in the scroll direction.
	 *
	 * @param rows the number of rows, negative values mean 0
	 */
	public void setPrefetch (int rows) {
		prefetchRows = rows < 0 ? 0 : rows;
	}

	// ---- payload -----------------------------------------------------------------------------

	/** Resident slot for reading, paging in or requesting as needed; -1 = not available. */
	private int readSlot (int item) {
		checkItem (item);
		tick ();
		int d = directory.get (item);
		if (d > 0) {
			slots.ref (d - 1, true);
			return d - 1;
		}
		if (d < 0) {
			paintViolation ();
			return swapIn (item, -d - 1);
		}
		if (source == null || lanes.get (LANE_DATA_REQUESTED, item) || filling >= 0) return -1;
		paintViolation ();
		requests.request (this, lanes, source, item, item + 1);
		d = directory.get (item);
		return d > 0 ? d - 1 : -1;
	}

	/** Resident slot for writing; outside a fill the payload becomes caller-owned. */
	private int writeSlot (int item, boolean derived) {
		checkItem (item);
		tick ();
		int d = directory.get (item);
		if (d <= 0) paintViolation ();
		int s = d > 0 ? d - 1 : d < 0 ? swapIn (item, -d - 1) : allocate (item);
		slots.ref (s, true);
		freeRecord (s);
		if (filling != item && !derived) lanes.set (LANE_CALLER_SET, item, true);
		if (filling != item && !lanes.get (LANE_DATA_LOADED, item)) {
			lanes.set (LANE_DATA_LOADED, item, true);
			lanes.set (LANE_MATERIALIZED, item, true);
			setTier (item, inWindow (item) ? TIER_HOT : TIER_WARM);
		}
		return s;
	}

	/**
	 * Returns the text of a cell, paging the item in or requesting its data when needed.
	 *
	 * @param item the item index
	 * @param column the column index
	 * @return the text, or <code>null</code> when the item has no data yet
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the item or column is not in the valid range</li>
	 * </ul>
	 */
	public String getText (int item, int column) {
		checkColumn (column);
		int s = readSlot (item);
		return s < 0 ? null : slots.slotText [s * slots.columns + column];
	}

	/**
	 * Sets the text of a cell. Outside a data scope the payload becomes caller-owned and is never destroyed.
	 *
	 * @param item the item index
	 * @param column the column index
	 * @param text the new text
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the item or column is not in the valid range</li>
	 * </ul>
	 */
	public void setText (int item, int column, String text) {
		checkColumn (column);
		int s = writeSlot (item, false);
		slots.slotText [s * slots.columns + column] = text;
		trim ();
	}

	/**
	 * Returns an item payload field (<code>I_*</code>).
	 *
	 * @param item the item index
	 * @param field the field
	 * @return the value, or 0 when the item has no data yet
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the item or field is not in the valid range</li>
	 * </ul>
	 */
	public int getInt (int item, int field) {
		checkField (field, ITEM_INTS);
		int s = readSlot (item);
		return s < 0 ? 0 : slots.slotInts [s * slots.intStride + field];
	}

	/**
	 * Sets an item payload field (<code>I_*</code>).
	 *
	 * @param item the item index
	 * @param field the field
	 * @param value the new value
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the item or field is not in the valid range</li>
	 * </ul>
	 */
	public void setInt (int item, int field, int value) {
		checkField (field, ITEM_INTS);
		slots.slotInts [writeSlot (item, false) * slots.intStride + field] = value;
		trim ();
	}

	/** Derived write (measured height, indent): not caller-owned, recomputed by the adopter after DESTROY. */
	public void setDerivedInt (int item, int field, int value) {
		checkField (field, ITEM_INTS);
		slots.slotInts [writeSlot (item, true) * slots.intStride + field] = value;
		trim ();
	}

	/**
	 * Returns a cell payload field (<code>C_*</code>).
	 *
	 * @param item the item index
	 * @param column the column index
	 * @param field the field
	 * @return the value, or 0 when the item has no data yet
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the item, column or field is not in the valid range</li>
	 * </ul>
	 */
	public int getCellInt (int item, int column, int field) {
		checkColumn (column);
		checkField (field, CELL_INTS);
		int s = readSlot (item);
		return s < 0 ? 0 : slots.slotInts [s * slots.intStride + ITEM_INTS + column * CELL_INTS + field];
	}

	/**
	 * Sets a cell payload field (<code>C_*</code>).
	 *
	 * @param item the item index
	 * @param column the column index
	 * @param field the field
	 * @param value the new value
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the item, column or field is not in the valid range</li>
	 * </ul>
	 */
	public void setCellInt (int item, int column, int field, int value) {
		checkColumn (column);
		checkField (field, CELL_INTS);
		slots.slotInts [writeSlot (item, false) * slots.intStride + ITEM_INTS + column * CELL_INTS + field] = value;
		trim ();
	}

	/**
	 * Returns the opaque payload bytes of an item.
	 *
	 * @param item the item index
	 * @return the bytes, or <code>null</code>
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the item is not between 0 and the number of items minus 1 (inclusive)</li>
	 * </ul>
	 */
	public byte [] getBlob (int item) {
		int s = readSlot (item);
		return s < 0 ? null : slots.slotBlob [s];
	}

	/**
	 * Sets the opaque payload bytes of an item.
	 *
	 * @param item the item index
	 * @param blob the bytes, or <code>null</code>
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the item is not between 0 and the number of items minus 1 (inclusive)</li>
	 * </ul>
	 */
	public void setBlob (int item, byte [] blob) {
		slots.slotBlob [writeSlot (item, false)] = blob;
		trim ();
	}

	private void checkColumn (int column) {
		if (column < 0 || column >= slots.columns) SWT.error (SWT.ERROR_INVALID_RANGE);
	}

	private static void checkField (int field, int limit) {
		if (field < 0 || field >= limit) SWT.error (SWT.ERROR_INVALID_RANGE);
	}

	// ---- paint path: reads by slot, zero materialization -----------------------------------------

	/**
	 * Opens a paint scope. Widget paint code reads only through {@link #paintSlot}, the
	 * {@code slot*} readers and {@link #paintLanes}; any allocation, page-in, SetData request or
	 * handle creation inside the scope increments {@code materializedDuringPaint}, which must stay 0.
	 */
	public void beginPaint () {
		painting++;
	}

	/**
	 * Closes a paint scope opened by <code>beginPaint</code>.
	 */
	public void endPaint () {
		if (painting > 0) painting--;
	}

	/** Resident slot of {@code item}, or -1; no paging, no request, no recency update. */
	public int paintSlot (int item) {
		checkItem (item);
		int d = directory.get (item);
		return d > 0 ? d - 1 : -1;
	}

	/**
	 * Paint path: returns the lane mask of an item without paging, requesting or allocating.
	 *
	 * @param item the item index
	 * @return the lane mask
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the item is not between 0 and the number of items minus 1 (inclusive)</li>
	 * </ul>
	 */
	public int paintLanes (int item) {
		checkItem (item);
		return lanes.gather (item);
	}

	/**
	 * Bulk paint read (UI method): lane masks of {@code [from, to)} into the caller's reusable
	 * {@code out}, O(set bits) per plain lane. Returns the number of entries written.
	 */
	public int paintLanes (int from, int to, int [] out) {
		if (from < 0 || to > lanes.count || from > to || out.length < to - from) SWT.error (SWT.ERROR_INVALID_RANGE);
		lanes.gather (from, to, out);
		return to - from;
	}

	/** Bulk paint read (UI method): resident slot (or -1) of each item of {@code [from, to)}. */
	public int paintSlots (int from, int to, int [] out) {
		if (from < 0 || to > lanes.count || from > to || out.length < to - from) SWT.error (SWT.ERROR_INVALID_RANGE);
		for (int i = from; i < to; i++) {
			int d = directory.get (i);
			out [i - from] = d > 0 ? d - 1 : -1;
		}
		return to - from;
	}

	/**
	 * Paint path: returns the text of a resident slot.
	 *
	 * @param slot the slot from <code>paintSlot</code>
	 * @param column the column index
	 * @return the text
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the column is not in the valid range</li>
	 * </ul>
	 */
	public String slotText (int slot, int column) {
		checkColumn (column);
		return slots.slotText [slot * slots.columns + column];
	}

	/**
	 * Paint path: returns an item payload field of a resident slot.
	 *
	 * @param slot the slot from <code>paintSlot</code>
	 * @param field the field (<code>I_*</code>)
	 * @return the value
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the field is not in the valid range</li>
	 * </ul>
	 */
	public int slotInt (int slot, int field) {
		checkField (field, ITEM_INTS);
		return slots.slotInts [slot * slots.intStride + field];
	}

	/**
	 * Paint path: returns a cell payload field of a resident slot.
	 *
	 * @param slot the slot from <code>paintSlot</code>
	 * @param column the column index
	 * @param field the field (<code>C_*</code>)
	 * @return the value
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the column or field is not in the valid range</li>
	 * </ul>
	 */
	public int slotCellInt (int slot, int column, int field) {
		checkColumn (column);
		checkField (field, CELL_INTS);
		return slots.slotInts [slot * slots.intStride + ITEM_INTS + column * CELL_INTS + field];
	}

	/**
	 * Paint path: returns the opaque payload bytes of a resident slot.
	 *
	 * @param slot the slot from <code>paintSlot</code>
	 * @return the bytes, or <code>null</code>
	 */
	public byte [] slotBlob (int slot) {
		return slots.slotBlob [slot];
	}

	/**
	 * Returns how many materializations happened inside paint scopes; correct widgets keep it 0.
	 *
	 * @return the count
	 */
	public int materializedDuringPaint () {
		return materializedDuringPaint;
	}

	// ---- item data lanes (parent-owned, pinned unless a codec accepts the value) ---------------

	/**
	 * Returns the item data object. It is pinned in the parent's data lane and keeps its identity across DESTROY and recreate.
	 *
	 * @param item the item index
	 * @return the data, or <code>null</code>
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the item is not between 0 and the number of items minus 1 (inclusive)</li>
	 * </ul>
	 */
	public Object getData (int item) {
		checkItem (item);
		int d = directory.get (item);
		if (d < 0) swapIn (item, -d - 1);
		return data.get (item);
	}

	/**
	 * Sets the item data object in the parent's data lane.
	 *
	 * @param item the item index
	 * @param value the data, or <code>null</code>
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the item is not between 0 and the number of items minus 1 (inclusive)</li>
	 * </ul>
	 */
	public void setData (int item, Object value) {
		checkItem (item);
		markDataChanged (item);
		data.put (item, value);
	}

	/**
	 * Returns the keyed item data object.
	 *
	 * @param item the item index
	 * @param key the key
	 * @return the data, or <code>null</code>
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the item is not between 0 and the number of items minus 1 (inclusive)</li>
	 * </ul>
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the key is null</li>
	 * </ul>
	 */
	public Object getKeyedData (int item, String key) {
		checkItem (item);
		if (key == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		int d = directory.get (item);
		if (d < 0) swapIn (item, -d - 1);
		Object [] pairs = (Object []) keyed.get (item);
		for (int i = 0; pairs != null && i < pairs.length; i += 2) {
			if (key.equals (pairs [i])) return pairs [i + 1];
		}
		return null;
	}

	/**
	 * Sets keyed item data; a <code>null</code> value removes the key.
	 *
	 * @param item the item index
	 * @param key the key
	 * @param value the data, or <code>null</code>
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the item is not between 0 and the number of items minus 1 (inclusive)</li>
	 * </ul>
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the key is null</li>
	 * </ul>
	 */
	public void setKeyedData (int item, String key, Object value) {
		checkItem (item);
		if (key == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		markDataChanged (item);
		keyed.put (item, ItemResidency.withPair ((Object []) keyed.get (item), key, value));
	}

	/** Returns a new pair array with {@code key} set ({@code value} null removes); null when empty. */
	public static Object [] withPair (Object [] pairs, String key, Object value) {
		int n = pairs == null ? 0 : pairs.length, at = -1;
		for (int i = 0; i < n; i += 2) {
			if (key.equals (pairs [i])) at = i;
		}
		if (at < 0 && value == null) return pairs;
		int size = at < 0 ? n + 2 : value == null ? n - 2 : n;
		if (size == 0) return null;
		Object [] next = new Object [size];
		int j = 0;
		for (int i = 0; i < n; i += 2) {
			if (i == at && value == null) continue;
			next [j++] = pairs [i];
			next [j++] = i == at ? value : pairs [i + 1];
		}
		if (at < 0) {
			next [j++] = key;
			next [j] = value;
		}
		return next;
	}

	private void markDataChanged (int item) {
		int d = directory.get (item);
		if (d < 0) d = swapIn (item, -d - 1) + 1;
		if (d > 0) freeRecord (d - 1);
	}

	/**
	 * Registers the codec whose accepted values are immutable and may travel to the COLD tier.
	 *
	 * @param codec the codec, or <code>null</code>
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_ARGUMENT - if an item has already been swapped out</li>
	 * </ul>
	 */
	public void setDataCodec (DataCodec codec) {
		if (counters [STAT_SWAP_OUT] != 0) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		this.codec = codec;
	}

	// ---- editor (parent scalars) -----------------------------------------------------------------

	/**
	 * Records the in-place editor state in the parent so that it survives scrolling.
	 *
	 * @param item the edited item
	 * @param column the edited column
	 * @param text the edited text
	 * @param caret the caret offset
	 * @param selectionStart the selection start
	 * @param selectionEnd the selection end
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the item or column is not in the valid range</li>
	 * </ul>
	 */
	public void openEditor (int item, int column, String text, int caret, int selectionStart, int selectionEnd) {
		checkItem (item);
		checkColumn (column);
		editorItem = item;
		editorColumn = column;
		editorText = text;
		editorCaret = caret;
		editorSelectionStart = selectionStart;
		editorSelectionEnd = selectionEnd;
	}

	/**
	 * Clears the in-place editor state.
	 */
	public void closeEditor () {
		editorItem = -1;
		editorText = null;
		editorCaret = editorSelectionStart = editorSelectionEnd = editorColumn = 0;
	}

	/**
	 * Returns the item of the open editor.
	 *
	 * @return the item index, or -1 when no editor is open
	 */
	public int editorItem () {
		return editorItem;
	}

	/**
	 * Returns the column of the open editor.
	 *
	 * @return the column index
	 */
	public int editorColumn () {
		return editorColumn;
	}

	/**
	 * Returns the edited text of the open editor.
	 *
	 * @return the text, or <code>null</code>
	 */
	public String editorText () {
		return editorText;
	}

	/**
	 * Returns the caret offset of the open editor.
	 *
	 * @return the caret offset
	 */
	public int editorCaret () {
		return editorCaret;
	}

	/**
	 * Returns the selection start of the open editor.
	 *
	 * @return the selection start
	 */
	public int editorSelectionStart () {
		return editorSelectionStart;
	}

	/**
	 * Returns the selection end of the open editor.
	 *
	 * @return the selection end
	 */
	public int editorSelectionEnd () {
		return editorSelectionEnd;
	}

	// ---- viewport, tiers, eviction -------------------------------------------------------------

	/**
	 * Flat residency driver: HOT window {@code [first - overscan, first + visible + overscan)};
	 * priority visible, then overscan in the scroll direction, then prefetch beyond it.
	 */
	public void setViewport (int first, int visible, int overscan) {
		if (first < 0 || visible < 0 || overscan < 0) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		int n = lanes.count;
		first = Math.min (first, n);
		int visibleEnd = visible > n - first ? n : first + visible;
		int start = first - overscan < 0 ? 0 : first - overscan;
		int end = overscan > n - visibleEnd ? n : visibleEnd + overscan;
		boolean down = first >= lastFirst;
		lastFirst = first;
		int [] r = priority;
		r [0] = first; r [1] = visibleEnd;
		r [2] = down ? visibleEnd : start; r [3] = down ? end : first;
		r [4] = down ? start : visibleEnd; r [5] = down ? first : end;
		int room = hardSlots / 1000 * permille + hardSlots % 1000 * permille / 1000 - (end - start);
		int pf = Math.min (prefetchRows, room < 0 ? 0 : room);
		r [6] = down ? end : Math.max (0, start - pf); r [7] = down ? (pf > n - end ? n : end + pf) : start;
		setViewportRuns (r, 4, 3);
	}

	int lastFirst;

	/**
	 * General residency driver (trees, filtered views): {@code runs} are item ranges in priority order
	 * (visible, overscan, prefetch); the first {@code hotRunCount} form the HOT window, the rest are
	 * prefetch (requested, WARM, evictable). Outstanding requests outside every run are cancelled.
	 */
	public void setViewportRuns (int [] runs, int runCount, int hotRunCount) {
		int n = lanes.count;
		if (hotRunCount < 0 || hotRunCount > runCount || 2 * runCount > runs.length) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		for (int k = 0; k < runCount; k++) {
			if (runs [2 * k] < 0 || runs [2 * k] > runs [2 * k + 1] || runs [2 * k + 1] > n) SWT.error (SWT.ERROR_INVALID_RANGE);
		}
		int previousCount = this.hotRunCount;
		if (previousRuns.length < 2 * previousCount) previousRuns = new int [PackedHeapGrowth.capacity (previousRuns.length, 2 * previousCount, 4)];
		int [] previous = previousRuns;
		System.arraycopy (hotRuns, 0, previous, 0, 2 * previousCount);
		setHotRuns (runs, hotRunCount);
		for (int k = 0; k < previousCount; k++) {
			for (int i = previous [2 * k]; i < previous [2 * k + 1] && i < n; i++) {
				if (!inWindow (i) && tierOf (i) == TIER_HOT) setTier (i, TIER_WARM);
			}
		}
		updateBudget ();
		for (int k = 0; k < hotRunCount; k++) promote (runs [2 * k], runs [2 * k + 1]);
		requests.negotiate (this, lanes, source, runs, runCount);
		trim ();
	}

	private void setHotRuns (int [] runs, int count) {
		if (hotRuns.length < 2 * count) hotRuns = new int [2 * count];
		int size = 0, w = 0;
		for (int k = 0; k < count; k++) {
			if (runs [2 * k] >= runs [2 * k + 1]) continue;
			int s = runs [2 * k], e = runs [2 * k + 1], b = w - 1;
			while (b >= 0 && hotRuns [2 * b] > s) {
				hotRuns [2 * b + 2] = hotRuns [2 * b];
				hotRuns [2 * b + 3] = hotRuns [2 * b + 1];
				b--;
			}
			hotRuns [2 * b + 2] = s;
			hotRuns [2 * b + 3] = e;
			w++;
			size += e - s;
		}
		hotRunCount = w;
		hotSize = size;
		windowStart = w == 0 ? 0 : hotRuns [0];
		windowEnd = w == 0 ? 0 : hotRuns [2 * w - 1];
	}

	/**
	 * UI method: keeps {@code [from, to)} resident beyond the viewport (an open editor row, a drag
	 * source, a sticky header). Pinned items are never evicted; payload is paged in on first access.
	 */
	public void pinResidency (int from, int to) {
		if (from < 0 || to > lanes.count || from > to) SWT.error (SWT.ERROR_INVALID_RANGE);
		residencyPins.setRange (from, to, true);
	}

	/** UI method: releases {@link #pinResidency} and trims back to the budget. */
	public void unpinResidency (int from, int to) {
		if (from < 0 || to > lanes.count || from > to) SWT.error (SWT.ERROR_INVALID_RANGE);
		residencyPins.setRange (from, to, false);
		trim ();
	}

	/**
	 * Returns whether an item is pinned resident by <code>pinResidency</code>.
	 *
	 * @param item the item index
	 * @return <code>true</code> when pinned
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the item is not between 0 and the number of items minus 1 (inclusive)</li>
	 * </ul>
	 */
	public boolean isResidencyPinned (int item) {
		checkItem (item);
		return residencyPins.get (item);
	}

	/**
	 * Returns the first item of the HOT window.
	 *
	 * @return the item index
	 */
	public int windowStart () {
		return windowStart;
	}

	/**
	 * Returns the end (exclusive) of the HOT window.
	 *
	 * @return the item index
	 */
	public int windowEnd () {
		return windowEnd;
	}

	private int tierOf (int item) {
		return (lanes.get (LANE_TIER_LO, item) ? 1 : 0) | (lanes.get (LANE_TIER_HI, item) ? 2 : 0);
	}

	/** Loaded items in {@code [from, to)} become HOT; COLD ones are paged in for painting. */
	private void promote (int from, int to) {
		for (int i = from; i < to; i++) {
			int d = directory.get (i);
			if (d < 0) swapIn (i, -d - 1);
			else if (d > 0 && tierOf (i) != TIER_HOT) setTier (i, TIER_HOT);
		}
	}

	private void clampWindow () {
		int w = 0, n = lanes.count;
		for (int k = 0; k < hotRunCount; k++) {
			int s = Math.min (hotRuns [2 * k], n), e = Math.min (hotRuns [2 * k + 1], n);
			if (s < e) {
				hotRuns [2 * w] = s;
				hotRuns [2 * w + 1] = e;
				w++;
			}
		}
		setHotRuns (Arrays.copyOf (hotRuns, 2 * w), w);
	}

	private void paintViolation () {
		if (painting > 0) materializedDuringPaint = inc (materializedDuringPaint);
	}

	private int allocate (int item) {
		while (slots.live >= budget && evictOne ()) {
			// CLOCK sweep until under budget or nothing evictable
		}
		int s = slots.allocate (item);
		directory.put (item, s + 1);
		return s;
	}

	private int swapIn (int item, int record) {
		int s = allocate (item);
		byte [] saved = slots.buf, bytes = spill.read (record);
		int savedLength = slots.len;
		slots.buf = bytes;
		slots.len = bytes.length;
		try {
			slots.decode (s);
			restoreData (item);
		} finally {
			slots.buf = saved;
			slots.len = savedLength;
		}
		slots.slotRecord [s] = record;
		setTier (item, inWindow (item) ? TIER_HOT : TIER_WARM);
		count (STAT_SWAP_IN);
		return s;
	}

	/** Frees the clean spill copy of a slot whose payload or data is about to change. */
	private void freeRecord (int s) {
		int record = slots.slotRecord [s];
		if (record >= 0) {
			slots.slotRecord [s] = -1;
			if (spill != null) spill.free (record);
		}
	}

	/** One CLOCK step sweep; true when a slot was evicted. */
	private boolean evictOne () {
		int limit = 2 * slots.highWater;
		for (int step = 0; step < limit; step++) {
			int s = hand;
			hand = hand + 1 >= slots.highWater ? 0 : hand + 1;
			int item = slots.slotItem [s];
			if (item < 0 || s == fillingSlot || inWindow (item) || residencyPins.get (item)) continue;
			if (slots.ref (s)) {
				slots.ref (s, false);
				continue;
			}
			if (evict (s, item)) return true;
		}
		return false;
	}

	private boolean evict (int s, int item) {
		if (!lanes.get (LANE_CALLER_SET, item) && (config & RESIDENCY_DESTROY) != 0) {
			freeRecord (s);
			slots.release (s);
			directory.put (item, 0);
			lanes.set (LANE_DATA_LOADED, item, false);
			setTier (item, TIER_DESTROYED);
			count (STAT_DESTROYED);
			return true;
		}
		if ((config & RESIDENCY_SWAP) != 0 && spill != null) {
			int record = slots.slotRecord [s];
			if (record < 0) {
				slots.encode (s);
				encodeData (item);
				record = spill.write (slots.buf, slots.len);
				count (STAT_SWAP_OUT_WRITES);
			}
			dropSwappedData (item);
			slots.slotRecord [s] = -1;
			slots.release (s);
			directory.put (item, -(record + 1));
			setTier (item, TIER_COLD);
			count (STAT_SWAP_OUT);
			return true;
		}
		count (STAT_UNEVICTABLE);
		return false;
	}

	/** Appends codec-accepted data values to the record being encoded. */
	private void encodeData (int item) {
		Object value = data.get (item);
		boolean swap = codec != null && value != null && codec.accepts (value);
		slots.putBytes (swap ? codec.encode (value) : null);
		Object [] pairs = (Object []) keyed.get (item);
		int n = 0;
		for (int i = 0; codec != null && pairs != null && i < pairs.length; i += 2) {
			if (codec.accepts (pairs [i + 1])) n++;
		}
		slots.putInt (n);
		for (int i = 0; n > 0 && i < pairs.length; i += 2) {
			if (!codec.accepts (pairs [i + 1])) continue;
			slots.putString ((String) pairs [i]);
			slots.putBytes (codec.encode (pairs [i + 1]));
		}
	}

	/** Codec-accepted values leave the resident tables with the swapped item. */
	private void dropSwappedData (int item) {
		if (codec == null) return;
		Object value = data.get (item);
		if (value != null && codec.accepts (value)) {
			data.put (item, null);
			count (STAT_DATA_SWAPPED);
		}
		Object [] pairs = (Object []) keyed.get (item);
		for (int i = 0; pairs != null && i < pairs.length; i += 2) {
			if (!codec.accepts (pairs [i + 1])) continue;
			pairs = withPair (pairs, (String) pairs [i], null);
			i -= 2;
			count (STAT_DATA_SWAPPED);
		}
		keyed.put (item, pairs);
	}

	/** Restores the data section that follows the payload in {@code slots.buf}. */
	private void restoreData (int item) {
		byte [] value = slots.getBytes ();
		if (value != null) {
			data.put (item, codec.decode (value));
			count (STAT_DATA_RESTORED);
		}
		int n = slots.getInt ();
		for (int i = 0; i < n; i++) {
			String key = slots.getString ();
			Object decoded = codec.decode (slots.getBytes ());
			keyed.put (item, withPair ((Object []) keyed.get (item), key, decoded));
			count (STAT_DATA_RESTORED);
		}
	}

	// ---- pressure ------------------------------------------------------------------------------

	/**
	 * Sets the pressure sampling cadence in operations.
	 *
	 * @param operations the number of operations between samples, at least 1
	 */
	public void setSampleEvery (int operations) {
		sampleEvery = operations < 1 ? 1 : operations;
	}

	private void tick () {
		if ((config & PRESSURE_SAMPLED) == 0 || pressure == null) return;
		if (operations == 0 || operations >= sampleEvery || pressure.urgent ()) {
			operations = 0;
			samplePressure ();
		}
		operations++;
	}

	/** Samples the source; a telemetry exception zeroes optional residency. */
	public int samplePressure () {
		int raw;
		try {
			raw = pressure == null ? 1000 : pressure.availablePermille ();
		} catch (RuntimeException failure) {
			raw = 0;
			count (STAT_TELEMETRY_FAILURES);
		}
		count (STAT_PRESSURE_SAMPLES);
		applyPressure (raw);
		return permille;
	}

	/** Shrink is immediate; recovery is rate-limited to {@link #RECOVERY_STEP} per sample. */
	public void applyPressure (int rawPermille) {
		int raw = rawPermille < 0 ? 0 : rawPermille > 1000 ? 1000 : rawPermille;
		permille = raw < permille ? raw : Math.min (raw, permille + RECOVERY_STEP);
		updateBudget ();
		trim ();
	}

	/** Heap-ratio permille with {@link #SOFT_START} and {@link #HARD_STOP} (SwapBudgetPlane semantics). */
	public static int permilleOfUsed (int usedPermille) {
		if (usedPermille <= SOFT_START) return 1000;
		if (usedPermille >= HARD_STOP) return 0;
		return (HARD_STOP - usedPermille) * 1000 / (HARD_STOP - SOFT_START);
	}

	private void updateBudget () {
		int scaled = hardSlots / 1000 * permille + hardSlots % 1000 * permille / 1000;
		budget = Math.max (hotSize, scaled);
	}

	/** Evicts down to the budget (never the window, never caller-owned payload without a spill). */
	public void trim () {
		while (slots.live > budget && evictOne ()) {
			// CLOCK sweep
		}
	}

	// ---- identity ------------------------------------------------------------------------------

	/**
	 * Sets the factory that creates and rebinds proxies.
	 *
	 * @param factory the factory
	 */
	public void setHandleFactory (HandleFactory factory) {
		this.factory = factory;
	}

	/**
	 * Sets the compile-time site metadata that decides when proxies may be recycled.
	 *
	 * @param metadata the metadata, or <code>null</code> to pin every proxy
	 */
	public void setSiteMetadata (SiteMetadata metadata) {
		this.metadata = metadata;
	}

	/** Pinned handle: the same object for the life of the item. */
	public Object handle (int item) {
		checkItem (item);
		if (factory == null) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		Object h = lanes.get (LANE_PINNED, item) ? pins.get (item) : null;
		if (h == null) {
			paintViolation ();
			h = factory.create (item, 0);
			pins.put (item, h);
			lanes.set (LANE_PINNED, item, true);
			count (STAT_PINNED);
		}
		return h;
	}

	/** Recyclable flyweight only at sites proven behaviour-only; otherwise the pinned handle. */
	public Object transientHandle (int item, int siteKey) {
		checkItem (item);
		if ((config & IDENTITY_BY_METADATA) == 0 || (config & IDENTITY_PIN_ALL) != 0 || metadata == null
				|| lanes.get (LANE_PINNED, item) || (metadata.classify (siteKey) & 3) != SITE_BEHAVIOUR_ONLY) {
			return handle (item);
		}
		paintViolation ();
		int k = ringNext;
		ringNext = (k + 1) & 3;
		generation = generation == Integer.MAX_VALUE ? 1 : generation + 1;
		if (ring [k] == null) ring [k] = factory.create (item, generation);
		else factory.rebind (ring [k], item, generation);
		ringItem [k] = item;
		ringGeneration [k] = generation;
		count (STAT_RECYCLED);
		return ring [k];
	}

	/** True when {@code handle} still denotes {@code item} at {@code generation}. */
	public boolean isCurrent (Object handle, int item, int generation) {
		if (item < 0 || item >= lanes.count) return false;
		if (generation == 0) return lanes.get (LANE_PINNED, item) && pins.get (item) == handle;
		for (int k = 0; k < ring.length; k++) {
			if (ring [k] == handle) return ringItem [k] == item && ringGeneration [k] == generation;
		}
		return false;
	}

	private void invalidateRing () {
		for (int k = 0; k < ring.length; k++) {
			ringGeneration [k] = 0;
		}
	}

	/** Sorted (key, kind) sidecar of the M3 reference-metadata pass, wire form M3IRS01. */
	public static final class Sidecar implements SiteMetadata {
		static final int MAGIC = 0x4D334952, VERSION = 1;
		final int [] keys, kinds;

		private Sidecar (int [] keys, int [] kinds) {
			this.keys = keys;
			this.kinds = kinds;
		}

		public static Sidecar fromInts (int [] packed) {
			if (packed == null || packed.length < 3 || packed [0] != MAGIC || packed [1] != VERSION) {
				SWT.error (SWT.ERROR_INVALID_ARGUMENT);
			}
			int n = packed [2];
			if (n < 0 || packed.length != 3 + 2 * n) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
			int [] keys = new int [n], kinds = new int [n];
			for (int i = 0; i < n; i++) {
				keys [i] = packed [3 + 2 * i];
				kinds [i] = packed [4 + 2 * i];
				if (i > 0 && keys [i] <= keys [i - 1]) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
			}
			return new Sidecar (keys, kinds);
		}

		@Override
		public int classify (int siteKey) {
			int lo = 0, hi = keys.length - 1;
			while (lo <= hi) {
				int mid = (lo + hi) >>> 1;
				if (keys [mid] < siteKey) lo = mid + 1;
				else if (keys [mid] > siteKey) hi = mid - 1;
				else return kinds [mid];
			}
			return SITE_UNKNOWN;
		}
	}

	// ---- statistics ------------------------------------------------------------------------------

	/**
	 * Returns a diagnostic counter or measurement (<code>STAT_*</code>).
	 *
	 * @param which the statistic
	 * @return the value
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_ARGUMENT - if the statistic is unknown</li>
	 * </ul>
	 */
	public int stat (int which) {
		if (which >= 0 && which < COUNTERS) return counters [which];
		switch (which) {
			case STAT_RESIDENT: return slots.live;
			case STAT_BUDGET: return budget;
			case STAT_PERMILLE: return permille;
			case STAT_REQUEST_RANGES: return requests.rangesRequested;
			case STAT_REQUEST_ITEMS: return requests.itemsRequested;
			case STAT_CANCEL_RANGES: return requests.rangesCancelled;
			case STAT_CANCEL_ITEMS: return requests.itemsCancelled;
			case STAT_LANE_BYTES: return lanes.bytes ();
			case STAT_DIRECTORY_BYTES: return directory.bytes ();
			case STAT_SLOT_BYTES: return slots.bytes ();
			case STAT_SLOT_CAPACITY: return slots.capacity;
			case STAT_MATERIALIZED_DURING_PAINT: return materializedDuringPaint;
			default: SWT.error (SWT.ERROR_INVALID_ARGUMENT); return 0;
		}
	}

	/**
	 * Cancels outstanding requests and closes the COLD tier.
	 */
	public void dispose () {
		requests.cancelAll (this, lanes, source);
		if (spill != null) spill.close ();
		spill = null;
	}
}
