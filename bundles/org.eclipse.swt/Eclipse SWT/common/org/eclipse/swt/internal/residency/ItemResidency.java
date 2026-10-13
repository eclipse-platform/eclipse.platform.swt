/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.internal.residency;

import java.util.Arrays;

/**
 * Shared M3 item-residency engine (design: docs/m3/M3_ITEM_RESIDENCY.md). An Item is a thin handle
 * of parent + item index + generation; every state lives here in always-resident {@code int[]} bit
 * lanes, resident payload slots, a spill tier and parent-owned data lanes. Residency follows the
 * viewport plus overscan and memory pressure, never the caller. Realizes Synexia invariant
 * M3-LAZY-OBJECT-PRESSURE-1 (#10889) for widgets: caller-owned objects are never discarded.
 *
 * <p>GWT-safe: {@code int} only, no reliance on int overflow, no {@code clone()}, no
 * {@code Objects.checkIndex}, no {@code java.nio}, no threads. Spill and pressure sources are
 * pluggable; their desktop implementations live in {@link ItemSpillStore} and
 * {@link ItemResidencyPressure}.</p>
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

	/** Creates and rebinds thin handles; item -1 means disposed. Generation 0 is a pinned handle. */
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
	DataSource source;
	Spill spill;
	Pressure pressure;
	HandleFactory factory;
	SiteMetadata metadata;
	DataCodec codec;
	int sampleEvery = 64, operations, permille = 1000, budget;
	int windowStart, windowEnd, prefetchRows, hotSize, hotRunCount;
	int [] hotRuns = new int [8], priority = new int [16];
	int filling = -1, fillingSlot = -1, hand, painting, materializedDuringPaint;
	final int [] counters = new int [COUNTERS];
	final Object [] ring = new Object [4];
	final int [] ringItem = new int [4], ringGeneration = new int [4];
	int ringNext, generation;
	int editorItem = -1, editorColumn, editorCaret, editorSelectionStart, editorSelectionEnd;
	String editorText;

	public ItemResidency (int config, int columns, int hardSlots, DataSource source, Spill spill, Pressure pressure) {
		if (columns < 1 || hardSlots < 1) throw new IllegalArgumentException ("columns and hardSlots must be positive");
		this.config = config;
		this.hardSlots = hardSlots;
		this.source = source;
		this.spill = spill;
		this.pressure = pressure;
		slots = new ItemSlots (columns, ITEM_INTS, CELL_INTS);
		budget = hardSlots;
	}

	public static int inc (int counter) {
		return counter == Integer.MAX_VALUE ? counter : counter + 1;
	}

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

	public int count () {
		return lanes.count;
	}

	private void checkItem (int item) {
		if (item < 0 || item >= lanes.count) throw new IndexOutOfBoundsException ("item " + item + " of " + lanes.count);
	}

	public void setCount (int items) {
		if (items < 0) throw new IllegalArgumentException ("negative count");
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
		lanes.setCount (items);
		invalidateRing ();
		clampWindow ();
	}

	public void insert (int index) {
		if (index < 0 || index > lanes.count) throw new IndexOutOfBoundsException ("insert " + index);
		requests.cancelAll (this, lanes, source);
		lanes.insert (index);
		directory.insert (index, lanes.count - 1);
		slots.shiftItems (index, 1);
		rebindFrom (pins.shift (index, 1));
		data.shift (index, 1);
		keyed.shift (index, 1);
		if (editorItem >= index) editorItem++;
		invalidateRing ();
	}

	public void remove (int index) {
		checkItem (index);
		requests.cancelAll (this, lanes, source);
		dropPayload (index);
		Object pinned = pins.put (index, null);
		if (pinned != null && factory != null) factory.rebind (pinned, -1, 0);
		data.put (index, null);
		keyed.put (index, null);
		lanes.remove (index);
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

	public boolean bit (int lane, int item) {
		checkItem (item);
		return lanes.get (lane, item);
	}

	public void setBit (int lane, int item, boolean on) {
		checkItem (item);
		checkUserLane (lane);
		lanes.set (lane, item, on);
	}

	public void setRange (int lane, int from, int to, boolean on) {
		checkUserLane (lane);
		if (from < 0 || to > lanes.count || from > to) throw new IndexOutOfBoundsException ("range");
		lanes.setRange (lane, from, to, on);
	}

	public int countRange (int lane, int from, int to) {
		if (from < 0 || to > lanes.count || from > to) throw new IndexOutOfBoundsException ("range");
		return lanes.countRange (lane, from, to);
	}

	public int nextSet (int lane, int from) {
		return from < 0 ? lanes.nextSet (lane, 0) : lanes.nextSet (lane, from);
	}

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
		if (start < 0 || end > lanes.count || start > end) throw new IndexOutOfBoundsException ("range");
		lanes.setPattern (lane, start, end, stride, repeatMask);
	}

	private static void checkUserLane (int lane) {
		if (lane < 0 || lane >= LANES || (ENGINE_LANES & (1 << lane)) != 0) throw new IllegalArgumentException ("lane " + lane);
	}

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
		if (filling >= 0) throw new IllegalStateException ("nested beginData");
		int d = directory.get (item);
		int s = d > 0 ? d - 1 : d < 0 ? swapIn (item, -d - 1) : allocate (item);
		filling = item;
		fillingSlot = s;
	}

	public void endData (int item) {
		if (filling != item) throw new IllegalStateException ("endData without beginData");
		filling = -1;
		fillingSlot = -1;
		lanes.set (LANE_DATA_REQUESTED, item, false);
		lanes.set (LANE_DATA_LOADED, item, true);
		lanes.set (LANE_DIRTY_DATA, item, false);
		count (lanes.get (LANE_MATERIALIZED, item) ? STAT_RECREATED : STAT_MATERIALIZED);
		lanes.set (LANE_MATERIALIZED, item, true);
		setTier (item, inWindow (item) ? TIER_HOT : TIER_WARM);
		trim ();
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
		if (d < 0) return swapIn (item, -d - 1);
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
		int s = d > 0 ? d - 1 : d < 0 ? swapIn (item, -d - 1) : allocate (item);
		slots.ref (s, true);
		freeRecord (s);
		if (filling != item && !derived) lanes.set (LANE_CALLER_SET, item, true);
		if (!lanes.get (LANE_DATA_LOADED, item)) {
			lanes.set (LANE_DATA_LOADED, item, true);
			lanes.set (LANE_MATERIALIZED, item, true);
			setTier (item, inWindow (item) ? TIER_HOT : TIER_WARM);
		}
		return s;
	}

	public String getText (int item, int column) {
		checkColumn (column);
		int s = readSlot (item);
		return s < 0 ? null : slots.slotText [s * slots.columns + column];
	}

	public void setText (int item, int column, String text) {
		checkColumn (column);
		int s = writeSlot (item, false);
		slots.slotText [s * slots.columns + column] = text;
		trim ();
	}

	public int getInt (int item, int field) {
		checkField (field, ITEM_INTS);
		int s = readSlot (item);
		return s < 0 ? 0 : slots.slotInts [s * slots.intStride + field];
	}

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

	public int getCellInt (int item, int column, int field) {
		checkColumn (column);
		checkField (field, CELL_INTS);
		int s = readSlot (item);
		return s < 0 ? 0 : slots.slotInts [s * slots.intStride + ITEM_INTS + column * CELL_INTS + field];
	}

	public void setCellInt (int item, int column, int field, int value) {
		checkColumn (column);
		checkField (field, CELL_INTS);
		slots.slotInts [writeSlot (item, false) * slots.intStride + ITEM_INTS + column * CELL_INTS + field] = value;
		trim ();
	}

	public byte [] getBlob (int item) {
		int s = readSlot (item);
		return s < 0 ? null : slots.slotBlob [s];
	}

	public void setBlob (int item, byte [] blob) {
		slots.slotBlob [writeSlot (item, false)] = blob;
		trim ();
	}

	private void checkColumn (int column) {
		if (column < 0 || column >= slots.columns) throw new IndexOutOfBoundsException ("column " + column);
	}

	private static void checkField (int field, int limit) {
		if (field < 0 || field >= limit) throw new IndexOutOfBoundsException ("field " + field);
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

	public void endPaint () {
		if (painting > 0) painting--;
	}

	/** Resident slot of {@code item}, or -1; no paging, no request, no recency update. */
	public int paintSlot (int item) {
		checkItem (item);
		int d = directory.get (item);
		return d > 0 ? d - 1 : -1;
	}

	public int paintLanes (int item) {
		checkItem (item);
		return lanes.gather (item);
	}

	public String slotText (int slot, int column) {
		checkColumn (column);
		return slots.slotText [slot * slots.columns + column];
	}

	public int slotInt (int slot, int field) {
		checkField (field, ITEM_INTS);
		return slots.slotInts [slot * slots.intStride + field];
	}

	public int slotCellInt (int slot, int column, int field) {
		checkColumn (column);
		checkField (field, CELL_INTS);
		return slots.slotInts [slot * slots.intStride + ITEM_INTS + column * CELL_INTS + field];
	}

	public byte [] slotBlob (int slot) {
		return slots.slotBlob [slot];
	}

	public int materializedDuringPaint () {
		return materializedDuringPaint;
	}

	// ---- item data lanes (parent-owned, pinned unless a codec accepts the value) ---------------

	public Object getData (int item) {
		checkItem (item);
		int d = directory.get (item);
		if (d < 0) swapIn (item, -d - 1);
		return data.get (item);
	}

	public void setData (int item, Object value) {
		checkItem (item);
		markDataChanged (item);
		data.put (item, value);
	}

	public Object getKeyedData (int item, String key) {
		checkItem (item);
		if (key == null) throw new IllegalArgumentException ("null key");
		int d = directory.get (item);
		if (d < 0) swapIn (item, -d - 1);
		Object [] pairs = (Object []) keyed.get (item);
		for (int i = 0; pairs != null && i < pairs.length; i += 2) {
			if (key.equals (pairs [i])) return pairs [i + 1];
		}
		return null;
	}

	public void setKeyedData (int item, String key, Object value) {
		checkItem (item);
		if (key == null) throw new IllegalArgumentException ("null key");
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

	public void setDataCodec (DataCodec codec) {
		if (counters [STAT_SWAP_OUT] != 0) throw new IllegalStateException ("codec must be set before the first swap-out");
		this.codec = codec;
	}

	// ---- editor (parent scalars) -----------------------------------------------------------------

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

	public void closeEditor () {
		editorItem = -1;
		editorText = null;
		editorCaret = editorSelectionStart = editorSelectionEnd = editorColumn = 0;
	}

	public int editorItem () {
		return editorItem;
	}

	public int editorColumn () {
		return editorColumn;
	}

	public String editorText () {
		return editorText;
	}

	public int editorCaret () {
		return editorCaret;
	}

	public int editorSelectionStart () {
		return editorSelectionStart;
	}

	public int editorSelectionEnd () {
		return editorSelectionEnd;
	}

	// ---- viewport, tiers, eviction -------------------------------------------------------------

	/**
	 * Flat residency driver: HOT window {@code [first - overscan, first + visible + overscan)};
	 * priority visible, then overscan in the scroll direction, then prefetch beyond it.
	 */
	public void setViewport (int first, int visible, int overscan) {
		if (first < 0 || visible < 0 || overscan < 0) throw new IllegalArgumentException ("negative viewport");
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
		if (hotRunCount < 0 || hotRunCount > runCount || 2 * runCount > runs.length) throw new IllegalArgumentException ("run counts");
		for (int k = 0; k < runCount; k++) {
			if (runs [2 * k] < 0 || runs [2 * k] > runs [2 * k + 1] || runs [2 * k + 1] > n) throw new IndexOutOfBoundsException ("run " + k);
		}
		int previousCount = this.hotRunCount;
		int [] previous = Arrays.copyOf (hotRuns, 2 * previousCount);
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

	public int windowStart () {
		return windowStart;
	}

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
		paintViolation ();
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
			if (item < 0 || s == fillingSlot || inWindow (item)) continue;
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

	public void setHandleFactory (HandleFactory factory) {
		this.factory = factory;
	}

	public void setSiteMetadata (SiteMetadata metadata) {
		this.metadata = metadata;
	}

	/** Pinned handle: the same object for the life of the item. */
	public Object handle (int item) {
		checkItem (item);
		if (factory == null) throw new IllegalStateException ("no handle factory");
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
				throw new IllegalArgumentException ("not an M3IRS01 sidecar");
			}
			int n = packed [2];
			if (n < 0 || packed.length != 3 + 2 * n) throw new IllegalArgumentException ("sidecar length");
			int [] keys = new int [n], kinds = new int [n];
			for (int i = 0; i < n; i++) {
				keys [i] = packed [3 + 2 * i];
				kinds [i] = packed [4 + 2 * i];
				if (i > 0 && keys [i] <= keys [i - 1]) throw new IllegalArgumentException ("sidecar keys not strictly sorted");
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
			default: throw new IllegalArgumentException ("stat " + which);
		}
	}

	public void dispose () {
		requests.cancelAll (this, lanes, source);
		if (spill != null) spill.close ();
		spill = null;
	}
}
