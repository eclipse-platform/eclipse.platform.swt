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
package org.eclipse.swt.widgets;

import java.util.concurrent.*;
import java.util.function.*;

import org.eclipse.swt.*;

/**
 * An asynchronous, block-wise data source for a <code>Table</code> or <code>Tree</code>
 * created with {@link SWT#VIRTUAL}.
 * <p>
 * Instead of answering {@link SWT#SetData} synchronously, the widget's rows are fetched in
 * blocks of <code>blockSize</code> rows on worker threads and kept in a bounded block cache of
 * <code>maxBlocks</code> blocks. A row whose block is not loaded yet is shown as a placeholder
 * (blank, or the placeholder text with {@link #PLACEHOLDER_TEXT}) and is asked again as soon as
 * its block arrives. Each frame the visible blocks are requested first, then the overscan
 * blocks by distance; a block that leaves the overscan window is cancelled before or while it
 * is fetched. Results reach the UI thread through one {@link Display#asyncExec(Runnable)} per
 * frame, however many blocks finished.
 * </p><p>
 * With {@link #TREE} the rows are tree nodes: each row may carry a child-count hint and a node
 * key, and the children of a node whose count is {@link #UNKNOWN} are fetched when it is
 * expanded; until they arrive the node shows one "loading" child that is drawn from the
 * parent's state and is not a <code>TreeItem</code> on platforms that support it.
 * </p><p>
 * Once a block is loaded, the widget's API results are identical to a synchronous
 * <code>SWT.SetData</code> listener that sets the same strings and item counts.
 * </p>
 * <pre>
 * Table table = new Table (shell, SWT.VIRTUAL | SWT.BORDER);
 * table.setItemCount (10_000_000);
 * VirtualPageSource source = new VirtualPageSource (256, 64, 1, VirtualPageSource.NONE, request -&gt; {
 *     for (int i = request.getStart (), end = i + request.getCount (); i &lt; end; i++) {
 *         request.setText (i, 0, database.row (i));
 *     }
 * });
 * source.attach (table);
 * </pre>
 *
 * @since 3.137
 */
public final class VirtualPageSource {
	/** Construction flag: no options. */
	public static final int NONE = 0;
	/** Construction flag: rows are tree nodes; children are fetched when a node is expanded. */
	public static final int TREE = 1;
	/** Construction flag: a row that is not loaded shows the placeholder text instead of blank. */
	public static final int PLACEHOLDER_TEXT = 1 << 1;
	/** Construction flag: never release transient item facades (keeps every facade, as plain SetData does). */
	public static final int KEEP_FACADES = 1 << 2;
	/**
	 * Construction flag: a loaded block evicted from the cache is swapped out to a temporary swap
	 * store and swapped back in when it is needed again, without asking the fetcher.
	 */
	public static final int SWAP = 1 << 3;

	/** The parent key of top-level rows. */
	public static final int ROOT = -1;
	/** Child-count hint of a tree row that has children whose number is not known yet. */
	public static final int UNKNOWN = VirtualBlockStore.UNKNOWN;

	/** Counter: blocks reserved and queued. */
	public static final int REQUESTED = 0;
	/** Counter: blocks delivered to the cache. */
	public static final int FETCHED = 1;
	/** Counter: blocks cancelled before or while they were fetched. */
	public static final int CANCELLED = 2;
	/** Counter: fetches that threw. */
	public static final int FAILED = 3;
	/** Counter: loaded blocks evicted to make room. */
	public static final int EVICTED = 4;
	/** Counter: UI-thread frames that drained results or planned requests. */
	public static final int FRAMES = 5;
	/** Counter: blocks resident in the cache now. */
	public static final int RESIDENT = 6;
	/** Counter: blocks queued or being fetched now. */
	public static final int IN_FLIGHT = 7;
	/** Counter: transient item facades released by the widget. */
	public static final int RELEASED = 8;
	/** Counter: blocks swapped out ({@link #SWAP}). */
	public static final int SWAPPED_OUT = 9;
	/** Counter: blocks swapped back in instead of fetched ({@link #SWAP}). */
	public static final int SWAPPED_IN = 10;
	/** Counter: API reads that waited longer than 100 ms behind the busy indicator. */
	public static final int BUSY_WAITS = 11;
	/** Counter: fetched blocks dropped because {@link #invalidate()} bumped the generation while they ran. */
	public static final int STALE_DROPPED = 12;
	/** Counter: UI-thread drain turns that reposted because their time budget ran out. */
	public static final int REPOSTS = 13;

	/**
	 * Fetches one block. Called on a worker thread, never on the UI thread; it must not touch
	 * widgets. It fills the request for rows <code>[getStart (), getStart () + getCount ())</code>
	 * and may poll {@link Request#isCancelled()} to stop early.
	 */
	@FunctionalInterface
	public interface Fetcher {
		/**
		 * Fills <code>request</code>.
		 *
		 * @param request the block to fill
		 * @throws Exception when the block cannot be fetched; the rows stay placeholders
		 */
		void fetch (Request request) throws Exception;
	}

	/**
	 * One block request handed to a {@link Fetcher}. Each worker thread reuses one instance;
	 * it is valid only for the duration of the <code>fetch</code> call.
	 */
	public static final class Request {
		final VirtualPageSource source;
		int slot = -1;
		int parentKey;
		int start;
		int count;

		Request (VirtualPageSource source) {
			this.source = source;
		}

		/**
		 * Returns the key of the node whose children are requested, or {@link VirtualPageSource#ROOT}.
		 *
		 * @return the parent key
		 */
		public int getParentKey () {
			return parentKey;
		}

		/**
		 * Returns the index of the first row of the block.
		 *
		 * @return the first row index
		 */
		public int getStart () {
			return start;
		}

		/**
		 * Returns the number of rows to fill. For the children of a tree node whose count is not
		 * known yet this is the block size; call {@link #setTotal(int)} with the real count.
		 *
		 * @return the row count
		 */
		public int getCount () {
			return count;
		}

		/**
		 * Returns whether the block left the overscan window and its result will be dropped.
		 *
		 * @return <code>true</code> when the fetch can stop
		 */
		public boolean isCancelled () {
			return source.isCancelled (slot);
		}

		/**
		 * Sets the text of one cell.
		 *
		 * @param index the row index, between <code>getStart ()</code> and <code>getStart () + getCount () - 1</code>
		 * @param column the column index
		 * @param text the text, or <code>null</code> for an empty cell
		 */
		public void setText (int index, int column, String text) {
			source.cache.text (slot, rowOf (index), column, text);
		}

		/**
		 * Sets the data handle of a row; it is applied with <code>Item.setData (Object)</code>
		 * whenever the row's item is filled, so it is reproduced after the item is released.
		 *
		 * @param index the row index
		 * @param data the data, or <code>null</code>
		 */
		public void setData (int index, Object data) {
			source.cache.data (slot, rowOf (index), data);
		}

		/**
		 * Sets the child-count hint of a tree row: <code>0</code> for a leaf, a positive count, or
		 * {@link VirtualPageSource#UNKNOWN} when the node has children whose number is fetched on expand.
		 *
		 * @param index the row index
		 * @param count the hint
		 */
		public void setChildCount (int index, int count) {
			if (count < UNKNOWN) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
			source.cache.childHint (slot, rowOf (index), count);
		}

		/**
		 * Sets the key of a tree row; the key is passed back as {@link #getParentKey()} when the
		 * row's children are requested.
		 *
		 * @param index the row index
		 * @param key the node key
		 */
		public void setKey (int index, int key) {
			source.cache.key (slot, rowOf (index), key);
		}

		/**
		 * Sets the total number of children of the parent (tree), or of rows (flat, when the
		 * widget's item count is still zero).
		 *
		 * @param total the total row count, zero or more
		 */
		public void setTotal (int total) {
			if (total < 0) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
			source.setTotalFromWorker (slot, total);
		}

		private int rowOf (int index) {
			int row = index - start;
			if (row < 0 || row >= count) SWT.error (SWT.ERROR_INVALID_RANGE);
			return row;
		}
	}

	/* Set by the platform Table/Tree class initializers that implement the transient-facade hooks. */
	static Function<Table, VirtualPageHost> tableHost;
	static Function<Tree, VirtualPageHost> treeHost;

	private static final int NO_KEY = Integer.MIN_VALUE;
	private static final int REFRESH_ALL = Integer.MAX_VALUE;

	private final int flags;
	private final int blockSize;
	private final int threadCount;
	private final Fetcher fetcher;
	final VirtualBlockStore cache;
	private int overscanBlocks = 2;
	private String placeholderText = "\u2026";

	private Control host;
	private VirtualPageHost hook;
	private Display display;
	private Listener listener;
	private final Runnable frameRunnable = this::frame;
	private final Runnable debounceRunnable = () -> {
		debounceArmed = false;
		scheduleFrame ();
	};

	/* Guarded by lock: the queue, the completions, the cancel bits and the worker state. */
	private final Object lock = new Object ();
	private int [] queueSlot = new int [8];
	private int [] queueParent = new int [8];
	private int [] queueStart = new int [8];
	private int [] queueCount = new int [8];
	private int queueSize;
	private int [] doneSlot = new int [8];
	private int [] doneFlags = new int [8];
	private int [] doneTotal = new int [8];
	private int doneSize;
	private final boolean [] cancelBits;
	private final int [] workerTotals;
	private boolean frameScheduled;
	private boolean stopped;
	private Thread [] workers;

	/* UI thread only. */
	private int [] uiDoneSlot = new int [8];
	private int [] uiDoneFlags = new int [8];
	private int [] uiDoneTotal = new int [8];
	private int [] wantParent = new int [16];
	private int [] wantBlock = new int [16];
	private int wantSize;
	private int [] refreshParent = new int [8];
	private int [] refreshBlock = new int [8];
	private int refreshSize;
	private int [] orderBlocks = new int [16];
	private int orderSize;
	private int lastPlannedTop = -1;
	private int loadDebounce;
	private long lastJumpMillis;
	private boolean debounceArmed;
	/** A scroll of more than this many blocks per frame is a jump, not a step. */
	private static final int JUMP_BLOCKS = 8;
	private TreeItem [] nodeItems = new TreeItem [8];
	private int [] nodeKeys = new int [8];
	private int [] nodeHints = new int [8];
	private int nodeSize;
	private final int [] counters = new int [14];
	/*
	 * Generation tokens (UI thread): every request carries the generation of its slot; invalidate
	 * bumps the generation, so a result that was running meanwhile is dropped at hand-off.
	 */
	private int generation;
	private final int [] slotGen;
	/* Drain-turn time budget and the durations of the last turns, in microseconds (proof). */
	static final long TURN_BUDGET_NANOS = 4_000_000L;
	private final int [] turnMicros = new int [256];
	private int turnCount;
	/** A caller's API read waits plainly this long before the busy indicator shows. */
	static final int BUSY_AFTER_MILLIS = 100;
	/* Guarded by lock: slots a caller's API read waits for (never cancelled by the window), and their waiters. */
	private final boolean [] apiWanted;
	private final CompletableFuture<?> [] waiters;
	private final VirtualBlockSpill spill;
	/** Result bit of a block answered from the swap store. */
	private static final int RESTORED = 1 << 5;
	/** Set by the last SetData answer: <code>true</code> when the row got a placeholder. Read by the host. */
	boolean answeredPlaceholder;
	/** Set by the host around paint-time SetData; any other SetData is a caller's API read. */
	boolean paintRequest;

	/**
	 * Creates a source.
	 *
	 * @param blockSize rows per block, at least 1
	 * @param maxBlocks blocks kept in the cache, at least 1; also bounds the requests in flight
	 * @param columns text columns per row, at least 1
	 * @param flags construction flags: {@link #NONE} or a combination of {@link #TREE},
	 *        {@link #PLACEHOLDER_TEXT} and {@link #KEEP_FACADES}
	 * @param fetcher fetches blocks on worker threads
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the fetcher is null</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if a size is out of range</li>
	 * </ul>
	 */
	public VirtualPageSource (int blockSize, int maxBlocks, int columns, int flags, Fetcher fetcher) {
		if (fetcher == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		if (blockSize < 1 || maxBlocks < 1 || columns < 1) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		this.blockSize = blockSize;
		this.flags = flags;
		this.fetcher = fetcher;
		this.threadCount = 2;
		cache = newBlockStore (blockSize, maxBlocks, columns);
		if ((flags & SWAP) != 0) {
			VirtualBlockSpill cold = new VirtualBlockSpill ();
			VirtualBlockStore hot = cache;
			spill = cold;
			hot.evictor (slot -> cold.spill (VirtualBlockSpill.key (hot.parentKey (slot), hot.block (slot)),
					VirtualBlockSpill.encode (hot, slot)));
		} else {
			spill = null;
		}
		cancelBits = new boolean [maxBlocks];
		workerTotals = new int [maxBlocks];
		apiWanted = new boolean [maxBlocks];
		slotGen = new int [maxBlocks];
		waiters = new CompletableFuture<?> [maxBlocks];
	}

	/**
	 * Feeds <code>table</code>, which must have the {@link SWT#VIRTUAL} style.
	 *
	 * @param table the table
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the table is null</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if the table is disposed, not virtual, the source has the
	 *        {@link #TREE} flag, or the source is already attached</li>
	 * </ul>
	 */
	public void attach (Table table) {
		if (table == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		if ((flags & TREE) != 0) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		attachTo (table);
	}

	/**
	 * Feeds <code>tree</code>, which must have the {@link SWT#VIRTUAL} style; the source needs the
	 * {@link #TREE} flag.
	 *
	 * @param tree the tree
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the tree is null</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if the tree is disposed, not virtual, the source lacks the
	 *        {@link #TREE} flag, or the source is already attached</li>
	 * </ul>
	 */
	public void attach (Tree tree) {
		if (tree == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		if ((flags & TREE) == 0) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		attachTo (tree);
		if (tree.getItemCount () == 0) want (ROOT, 0, true);
	}

	/**
	 * Stops feeding the widget, cancels every request and stops the worker threads. The rows
	 * already shown keep their data.
	 */
	public void detach () {
		Control control = host;
		if (control == null) return;
		synchronized (lock) {
			stopped = true;
			lock.notifyAll ();
			for (int i = 0; i < waiters.length; i++) {
				if (waiters [i] != null) waiters [i].cancel (false);
				waiters [i] = null;
			}
		}
		if (!control.isDisposed ()) {
			control.removeListener (SWT.SetData, listener);
			control.removeListener (SWT.Expand, listener);
			control.removeListener (SWT.Collapse, listener);
			control.removeListener (SWT.Dispose, listener);
			if (hook != null) hook.pageSourceAttached (null);
		}
		host = null;
		hook = null;
		workers = null;
		nodeSize = 0;
		if (spill != null) spill.close ();
	}

	/**
	 * Drops every loaded block and asks the visible rows again.
	 */
	public void invalidate () {
		checkThread ();
		generation++;
		synchronized (lock) {
			for (int i = 0; i < queueSize; i++) {
				cache.free (queueSlot [i]);
				counters [CANCELLED]++;
			}
			queueSize = 0;
		}
		wantSize = 0;
		cache.invalidate (Integer.MIN_VALUE);
		if (spill != null) spill.invalidate (Integer.MIN_VALUE);
		addRefresh (ROOT, REFRESH_ALL);
		scheduleFrame ();
	}

	/**
	 * Sets how many blocks beyond the visible ones are prefetched on each side (default 2).
	 *
	 * @param blocks the overscan in blocks, zero or more
	 */
	public void setOverscanBlocks (int blocks) {
		if (blocks < 0) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		overscanBlocks = blocks;
	}

	/**
	 * Sets how long, after a jump (a thumb drag of more than a few blocks per frame), new blocks
	 * wait before they are requested (default 0: no wait). Rows show placeholders meanwhile; a
	 * drag across millions of rows then fetches only where it stops (AG Grid
	 * <code>blockLoadDebounceMillis</code>).
	 *
	 * @param millis the debounce in milliseconds, zero or more
	 */
	public void setLoadDebounce (int millis) {
		if (millis < 0) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		loadDebounce = millis;
	}

	/**
	 * Sets the text shown in rows that are not loaded when the source has {@link #PLACEHOLDER_TEXT}.
	 *
	 * @param text the placeholder text
	 */
	public void setPlaceholderText (String text) {
		if (text == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		placeholderText = text;
	}

	/**
	 * Returns whether the row <code>index</code> under <code>parentKey</code> is loaded.
	 *
	 * @param parentKey {@link #ROOT} or a node key
	 * @param index the row index
	 * @return <code>true</code> when the row's block is in the cache
	 */
	public boolean isLoaded (int parentKey, int index) {
		if (index < 0) return false;
		int slot = cache.find (parentKey, index / blockSize);
		return slot >= 0 && cache.state (slot) == VirtualBlockStore.LOADED;
	}

	/**
	 * Returns whether nothing is queued, being fetched, or waiting for the UI thread.
	 *
	 * @return <code>true</code> when the source is idle
	 */
	public boolean isIdle () {
		synchronized (lock) {
			if (queueSize != 0 || doneSize != 0 || frameScheduled) return false;
		}
		return cache.count (VirtualBlockStore.BUSY) == 0 && wantSize == 0 && !debounceArmed;
	}

	/**
	 * Returns one of the counters {@link #REQUESTED}, {@link #FETCHED}, {@link #CANCELLED},
	 * {@link #FAILED}, {@link #EVICTED}, {@link #FRAMES}, {@link #RESIDENT}, {@link #IN_FLIGHT}
	 * or {@link #RELEASED}.
	 *
	 * @param counter the counter
	 * @return its value
	 */
	public int getCount (int counter) {
		switch (counter) {
			case RESIDENT: return cache.count (VirtualBlockStore.LOADED | VirtualBlockStore.FAILED);
			case IN_FLIGHT: return cache.count (VirtualBlockStore.BUSY);
			case EVICTED: return cache.evictions ();
			case SWAPPED_OUT: return spill == null ? 0 : spill.spilled ();
			default:
				if (counter < 0 || counter >= counters.length) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
				return counters [counter];
		}
	}

	/**
	 * The one place that chooses the block store. The tiered swap store of SWT #122 replaces the
	 * in-memory cache here.
	 */
	static VirtualBlockStore newBlockStore (int blockSize, int maxBlocks, int columns) {
		return new VirtualBlockCache (blockSize, maxBlocks, columns);
	}

	/* ---------------------------------------------------------------- attach */

	private void attachTo (Control control) {
		if (control.isDisposed () || (control.getStyle () & SWT.VIRTUAL) == 0 || host != null) {
			SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		}
		host = control;
		display = control.getDisplay ();
		listener = this::handleEvent;
		synchronized (lock) {
			stopped = false;
		}
		control.addListener (SWT.SetData, listener);
		control.addListener (SWT.Dispose, listener);
		if ((flags & TREE) != 0) {
			control.addListener (SWT.Expand, listener);
			control.addListener (SWT.Collapse, listener);
		}
		hook = null;
		if (control instanceof Table table && tableHost != null) hook = tableHost.apply (table);
		else if (control instanceof Tree tree && treeHost != null) hook = treeHost.apply (tree);
		if (hook != null) hook.pageSourceAttached (this);
	}

	boolean keepsFacades () {
		return (flags & KEEP_FACADES) != 0;
	}

	String placeholder () {
		return (flags & PLACEHOLDER_TEXT) != 0 ? placeholderText : "";
	}

	String loadingText () {
		return placeholderText;
	}

	/**
	 * Text of a tree row for a paint callback without a facade: the loaded text, or null after
	 * wanting its block (or its parent's key).
	 */
	String treePaintText (TreeItem parent, int index, int column) {
		int parentKey = parent == null ? ROOT : keyOf (parent);
		if (parentKey == NO_KEY || index < 0) return null;
		int slot = cache.find (parentKey, index / blockSize);
		if (slot < 0 || cache.state (slot) != VirtualBlockStore.LOADED) {
			want (parentKey, index / blockSize, false);
			return null;
		}
		cache.touch (slot);
		if (column < 0 || column >= cache.columns ()) return "";
		String text = cache.text (slot, index - cache.firstRow (slot), column);
		return text == null ? "" : text;
	}

	/** Whether a loaded tree row has children (known or UNKNOWN). */
	boolean treePaintExpandable (TreeItem parent, int index) {
		int parentKey = parent == null ? ROOT : keyOf (parent);
		if (parentKey == NO_KEY || index < 0) return false;
		int slot = cache.find (parentKey, index / blockSize);
		return slot >= 0 && cache.state (slot) == VirtualBlockStore.LOADED && cache.childHint (slot, index - cache.firstRow (slot)) != 0;
	}

	/** Text of a top-level row if its block is loaded, else null; never requests anything. */
	String rowText (int index, int column) {
		if (index < 0 || column < 0 || column >= cache.columns ()) return null;
		int slot = cache.find (ROOT, index / blockSize);
		if (slot < 0 || cache.state (slot) != VirtualBlockStore.LOADED) return null;
		return cache.text (slot, index - cache.firstRow (slot), column);
	}

	/**
	 * Text of a top-level row for a paint callback without a facade: the loaded text (an empty
	 * cell reads as ""), or null after asking the next frame to plan the window.
	 */
	String paintText (int index, int column) {
		if (index < 0) return null;
		int slot = cache.find (ROOT, index / blockSize);
		if (slot < 0 || cache.state (slot) != VirtualBlockStore.LOADED) {
			scheduleFrame ();
			return null;
		}
		cache.touch (slot);
		if (column < 0 || column >= cache.columns ()) return "";
		String text = cache.text (slot, index - cache.firstRow (slot), column);
		return text == null ? "" : text;
	}

	private void handleEvent (Event event) {
		switch (event.type) {
			case SWT.SetData:
				if (event.item instanceof TableItem item) setTableData (item, event.index);
				else if (event.item instanceof TreeItem item) setTreeData (item, event.index);
				break;
			case SWT.Expand:
				if (event.item instanceof TreeItem item) expand (item);
				break;
			case SWT.Collapse:
				if (event.item instanceof TreeItem item) collapse (item);
				break;
			case SWT.Dispose:
				if (event.widget == host) detach ();
				break;
		}
	}

	/* ---------------------------------------------------------------- SetData */

	private void setTableData (TableItem item, int index) {
		int slot = cache.find (ROOT, index / blockSize);
		if (slot >= 0 && cache.state (slot) == VirtualBlockStore.LOADED) {
			answeredPlaceholder = false;
			cache.touch (slot);
			fillTableItem (item, slot, index - cache.firstRow (slot));
			return;
		}
		if (!paintRequest) {
			slot = loadNow (ROOT, index / blockSize);
			if (slot >= 0) {
				answeredPlaceholder = false;
				fillTableItem (item, slot, index - cache.firstRow (slot));
				return;
			}
		}
		answeredPlaceholder = true;
		if ((flags & PLACEHOLDER_TEXT) != 0) item.setText (placeholderText);
		scheduleFrame ();
	}

	private void fillTableItem (TableItem item, int slot, int row) {
		for (int column = 0; column < cache.columns (); column++) {
			String text = cache.text (slot, row, column);
			item.setText (column, text == null ? "" : text);
		}
		Object data = cache.data (slot, row);
		if (data != null) item.setData (data);
	}

	private void setTreeData (TreeItem item, int index) {
		TreeItem parent = item.getParentItem ();
		int parentKey = parent == null ? ROOT : keyOf (parent);
		if (parentKey == NO_KEY) {
			answeredPlaceholder = true;
			scheduleFrame ();
			return;
		}
		int slot = cache.find (parentKey, index / blockSize);
		if ((slot < 0 || cache.state (slot) != VirtualBlockStore.LOADED) && !paintRequest) {
			slot = loadNow (parentKey, index / blockSize);
		}
		if (slot < 0 || cache.state (slot) != VirtualBlockStore.LOADED) {
			answeredPlaceholder = true;
			if ((flags & PLACEHOLDER_TEXT) != 0) item.setText (placeholderText);
			want (parentKey, index / blockSize, false);
			return;
		}
		answeredPlaceholder = false;
		cache.touch (slot);
		int row = index - cache.firstRow (slot);
		for (int column = 0; column < cache.columns (); column++) {
			String text = cache.text (slot, row, column);
			item.setText (column, text == null ? "" : text);
		}
		Object data = cache.data (slot, row);
		if (data != null) item.setData (data);
		int hint = cache.childHint (slot, row);
		int key = cache.key (slot, row);
		int children = hint;
		if (hint == UNKNOWN) {
			int node = nodeIndex (item);
			int first = cache.find (key, 0);
			if (node >= 0 && nodeKeys [node] == key && nodeHints [node] != UNKNOWN) {
				children = nodeHints [node];
			} else if (first >= 0 && cache.state (first) == VirtualBlockStore.LOADED && cache.total (first) >= 0) {
				children = cache.total (first);
			}
		}
		if (hint != 0) remember (item, key, children);
		item.setItemCount (children == UNKNOWN ? 1 : children);
	}

	/* ---------------------------------------------------------------- tree */

	private void expand (TreeItem item) {
		/*
		 * A row painted without a facade has only its native expander; reading the count fills
		 * the new facade through SetData, which inserts the known children or the one
		 * placeholder child of an UNKNOWN node, as the classic listener would.
		 */
		item.getItemCount ();
		if (item.isDisposed ()) return;
		int key = keyOf (item);
		if (key == NO_KEY) return;
		int node = nodeIndex (item);
		if (node < 0) return;
		if (nodeHints [node] != UNKNOWN) {
			if (hook != null) hook.pageSourceChildrenLoading (item, false);
			return;
		}
		int slot = cache.find (key, 0);
		if (slot >= 0 && cache.state (slot) == VirtualBlockStore.LOADED && cache.total (slot) >= 0) {
			applyTotal (node, cache.total (slot));
			return;
		}
		if (hook != null) hook.pageSourceChildrenLoading (item, true);
		want (key, 0, true);
	}

	private void collapse (TreeItem item) {
		int key = keyOf (item);
		if (key != NO_KEY) cancelParent (key);
	}

	private int keyOf (TreeItem item) {
		int node = nodeIndex (item);
		if (node >= 0) return nodeKeys [node];
		TreeItem parent = item.getParentItem ();
		int parentKey = parent == null ? ROOT : keyOf (parent);
		if (parentKey == NO_KEY) return NO_KEY;
		int index = parent == null ? item.getParent ().indexOf (item) : parent.indexOf (item);
		if (index < 0) return NO_KEY;
		int slot = cache.find (parentKey, index / blockSize);
		if ((slot < 0 || cache.state (slot) != VirtualBlockStore.LOADED) && !paintRequest) {
			slot = loadNow (parentKey, index / blockSize);
		}
		if (slot < 0 || cache.state (slot) != VirtualBlockStore.LOADED) {
			want (parentKey, index / blockSize, false);
			return NO_KEY;
		}
		int row = index - cache.firstRow (slot);
		int key = cache.key (slot, row);
		remember (item, key, cache.childHint (slot, row));
		return key;
	}

	private int nodeIndex (TreeItem item) {
		for (int i = 0; i < nodeSize; i++) {
			if (nodeItems [i] == item) return i;
		}
		return -1;
	}

	private int nodeIndexOfKey (int key) {
		for (int i = 0; i < nodeSize; i++) {
			if (nodeKeys [i] == key && !nodeItems [i].isDisposed ()) return i;
		}
		return -1;
	}

	private void remember (TreeItem item, int key, int hint) {
		int node = nodeIndex (item);
		if (node < 0) {
			compactNodes ();
			if (nodeSize == nodeItems.length) {
				nodeItems = grow (nodeItems);
				nodeKeys = grow (nodeKeys);
				nodeHints = grow (nodeHints);
			}
			node = nodeSize++;
			nodeItems [node] = item;
		}
		nodeKeys [node] = key;
		nodeHints [node] = hint;
	}

	private void compactNodes () {
		int kept = 0;
		for (int i = 0; i < nodeSize; i++) {
			if (nodeItems [i].isDisposed ()) continue;
			nodeItems [kept] = nodeItems [i];
			nodeKeys [kept] = nodeKeys [i];
			nodeHints [kept] = nodeHints [i];
			kept++;
		}
		for (int i = kept; i < nodeSize; i++) nodeItems [i] = null;
		nodeSize = kept;
	}

	private void applyTotal (int node, int total) {
		TreeItem item = nodeItems [node];
		nodeHints [node] = total;
		if (item.isDisposed ()) return;
		if (hook != null) hook.pageSourceChildrenLoading (item, false);
		boolean batch = hook != null && total > hook.pageSourceBatchThreshold () && item.getExpanded ();
		if (batch) hook.pageSourceBatch (true, item);
		try {
			if (batch) org.eclipse.swt.custom.BusyIndicator.showWhile (item.getDisplay (), () -> item.setItemCount (total));
			else item.setItemCount (total);
		} finally {
			if (batch) hook.pageSourceBatch (false, item);
		}
	}

	/* ---------------------------------------------------------------- requests */

	private void want (int parentKey, int block, boolean urgent) {
		for (int i = 0; i < wantSize; i++) {
			if (wantParent [i] == parentKey && wantBlock [i] == block) {
				if (urgent && i != 0) {
					System.arraycopy (wantParent, 0, wantParent, 1, i);
					System.arraycopy (wantBlock, 0, wantBlock, 1, i);
					wantParent [0] = parentKey;
					wantBlock [0] = block;
				}
				scheduleFrame ();
				return;
			}
		}
		if (wantSize == wantParent.length) {
			wantParent = grow (wantParent);
			wantBlock = grow (wantBlock);
		}
		if (urgent) {
			System.arraycopy (wantParent, 0, wantParent, 1, wantSize);
			System.arraycopy (wantBlock, 0, wantBlock, 1, wantSize);
			wantParent [0] = parentKey;
			wantBlock [0] = block;
		} else {
			wantParent [wantSize] = parentKey;
			wantBlock [wantSize] = block;
		}
		wantSize++;
		scheduleFrame ();
	}

	/**
	 * A caller's API read of a row that is not loaded: fetch its block on the calling thread, as
	 * a synchronous SetData would. A block that is queued is taken out of the queue; one that a
	 * worker is fetching is awaited. Returns the loaded slot, or -1.
	 */
	private int loadNow (int parentKey, int block) {
		int slot = cache.find (parentKey, block);
		if (slot >= 0 && cache.state (slot) == VirtualBlockStore.LOADED) return slot;
		if (slot >= 0 && cache.state (slot) == VirtualBlockStore.FAILED) {
			cache.free (slot);
			slot = -1;
		}
		int start = block * blockSize;
		int rows = rowsFor (parentKey, start, -1);
		if (rows <= 0) return -1;
		if (slot < 0) {
			/* An API read starts a new access epoch so blocks read earlier may be evicted (LRU). */
			cache.nextFrame ();
			slot = cache.reserve (parentKey, block);
			if (slot < 0) return -1;
			counters [REQUESTED]++;
			slotGen [slot] = generation;
			synchronized (lock) {
				enqueueFront (slot, parentKey, start, rows);
				workerTotals [slot] = VirtualBlockStore.UNKNOWN;
			}
		} else {
			synchronized (lock) {
				if (removeQueued (slot)) enqueueFront (slot, parentKey, start, rows);
			}
		}
		synchronized (lock) {
			cancelBits [slot] = false;
			apiWanted [slot] = true;
			lock.notifyAll ();
		}
		ensureWorkers ();
		try {
			awaitSlot (slot);
		} finally {
			synchronized (lock) {
				apiWanted [slot] = false;
			}
		}
		if (host == null || host.isDisposed ()) return -1;
		drainNow ();
		slot = cache.find (parentKey, block);
		return slot >= 0 && cache.state (slot) == VirtualBlockStore.LOADED ? slot : -1;
	}

	/** Caller holds the lock. */
	private void enqueueFront (int slot, int parentKey, int start, int rows) {
		if (queueSize == queueSlot.length) {
			queueSlot = grow (queueSlot);
			queueParent = grow (queueParent);
			queueStart = grow (queueStart);
			queueCount = grow (queueCount);
		}
		System.arraycopy (queueSlot, 0, queueSlot, 1, queueSize);
		System.arraycopy (queueParent, 0, queueParent, 1, queueSize);
		System.arraycopy (queueStart, 0, queueStart, 1, queueSize);
		System.arraycopy (queueCount, 0, queueCount, 1, queueSize);
		queueSlot [0] = slot;
		queueParent [0] = parentKey;
		queueStart [0] = start;
		queueCount [0] = rows;
		queueSize++;
	}

	/**
	 * Waits for a block a caller's API read needs. Up to {@link #BUSY_AFTER_MILLIS} the UI thread
	 * waits plainly; a slower source is awaited through
	 * {@link org.eclipse.swt.custom.BusyIndicator#showWhile(java.util.concurrent.Future)}: the
	 * busy cursor shows and the event loop stays alive (scroll bars live, frames paint once).
	 */
	private void awaitSlot (int slot) {
		CompletableFuture<Void> future;
		synchronized (lock) {
			long deadline = System.currentTimeMillis () + BUSY_AFTER_MILLIS;
			while (!stopped && !isDone (slot)) {
				long left = deadline - System.currentTimeMillis ();
				if (left <= 0) break;
				try {
					lock.wait (left);
				} catch (InterruptedException e) {
					Thread.currentThread ().interrupt ();
					return;
				}
			}
			if (stopped || isDone (slot)) return;
			future = new CompletableFuture<> ();
			waiters [slot] = future;
		}
		counters [BUSY_WAITS]++;
		org.eclipse.swt.custom.BusyIndicator.showWhile (future);
	}

	/** Rows to request for a block; <code>flatCount</code> is the table row count or -1. */
	private int rowsFor (int parentKey, int start, int flatCount) {
		int rows = blockSize;
		if (host instanceof Table table) {
			int count = flatCount >= 0 ? flatCount : table.getItemCount ();
			rows = Math.min (blockSize, count - start);
		} else if (parentKey != ROOT) {
			int node = nodeIndexOfKey (parentKey);
			if (node >= 0 && nodeHints [node] > 0) rows = Math.min (blockSize, nodeHints [node] - start);
		} else if (host instanceof Tree tree && tree.getItemCount () > 0) {
			rows = Math.min (blockSize, tree.getItemCount () - start);
		}
		return rows;
	}

	/** Caller holds the lock. */
	private boolean removeQueued (int slot) {
		for (int i = 0; i < queueSize; i++) {
			if (queueSlot [i] != slot) continue;
			queueSize--;
			System.arraycopy (queueSlot, i + 1, queueSlot, i, queueSize - i);
			System.arraycopy (queueParent, i + 1, queueParent, i, queueSize - i);
			System.arraycopy (queueStart, i + 1, queueStart, i, queueSize - i);
			System.arraycopy (queueCount, i + 1, queueCount, i, queueSize - i);
			return true;
		}
		return false;
	}

	/** Caller holds the lock. */
	private boolean isDone (int slot) {
		for (int i = 0; i < doneSize; i++) {
			if (doneSlot [i] == slot) return true;
		}
		return false;
	}

	/** Settles every finished block now, on the UI thread, without planning. */
	private void drainNow () {
		int done;
		synchronized (lock) {
			done = takeDone ();
		}
		for (int i = 0; i < done; i++) settle (uiDoneSlot [i], uiDoneFlags [i], uiDoneTotal [i]);
		scheduleFrame ();
	}

	/** Moves the completions to the UI arrays. Caller holds the lock. */
	private int takeDone () {
		int done = doneSize;
		if (uiDoneSlot.length < done) {
			uiDoneSlot = new int [doneSlot.length];
			uiDoneFlags = new int [doneSlot.length];
			uiDoneTotal = new int [doneSlot.length];
		}
		System.arraycopy (doneSlot, 0, uiDoneSlot, 0, done);
		System.arraycopy (doneFlags, 0, uiDoneFlags, 0, done);
		System.arraycopy (doneTotal, 0, uiDoneTotal, 0, done);
		doneSize = 0;
		return done;
	}

	private void scheduleFrame () {
		synchronized (lock) {
			if (frameScheduled || stopped) return;
			frameScheduled = true;
		}
		display.asyncExec (frameRunnable);
	}

	/** Answers a request from the swap store when the block was swapped out; any thread. */
	private boolean restore (Request request) {
		if (spill == null) return false;
		byte [] encoded = spill.take (VirtualBlockSpill.key (request.parentKey, request.start / blockSize));
		if (encoded == null) return false;
		try {
			int total = VirtualBlockSpill.decode (encoded, cache, request.slot);
			if (total >= 0) setTotalFromWorker (request.slot, total);
			return true;
		} catch (java.io.IOException | RuntimeException e) {
			return false;
		}
	}

	boolean isCancelled (int slot) {
		synchronized (lock) {
			return stopped || cancelBits [slot];
		}
	}

	void setTotalFromWorker (int slot, int total) {
		synchronized (lock) {
			workerTotals [slot] = total;
		}
	}

	/** One UI-thread frame: drain finished blocks, plan the window, refresh, release. */
	private void frame () {
		long turnStart = System.nanoTime ();
		int done;
		synchronized (lock) {
			frameScheduled = false;
			done = takeDone ();
		}
		Control control = host;
		if (control == null || control.isDisposed ()) return;
		counters [FRAMES]++;
		cache.nextFrame ();
		int settled = 0;
		while (settled < done) {
			settle (uiDoneSlot [settled], uiDoneFlags [settled], uiDoneTotal [settled]);
			settled++;
			if (System.nanoTime () - turnStart > TURN_BUDGET_NANOS) break;
		}
		if (settled < done) {
			/* Budget spent: hand the rest back and repost, so input and paint get in between turns. */
			requeueDone (settled, done);
			counters [REPOSTS]++;
			recordTurn (turnStart);
			scheduleFrame ();
			return;
		}
		if (!lazyContentShowing (control)) {
			recordTurn (turnStart);
			return;
		}
		if ((flags & TREE) == 0) planTable ((Table) control);
		else planTree ();
		applyRefreshes (control);
		if (hook != null && (flags & (TREE | KEEP_FACADES)) == 0) {
			Table table = (Table) control;
			int top = table.getTopIndex ();
			int rows = visibleRows (table);
			int overscanRows = overscanBlocks * blockSize;
			counters [RELEASED] += hook.pageSourceRelease (Math.max (0, top - overscanRows), top + rows + overscanRows);
		}
		/* Settle gating: expensive fits run once the queue has drained; otherwise a later turn runs them. */
		if (hook != null && isIdle ()) hook.pageSourceSettled ();
		recordTurn (turnStart);
	}

	/** Puts completions [from, to) of this turn back at the head of the completion list. Caller is the UI thread. */
	private void requeueDone (int from, int to) {
		int count = to - from;
		synchronized (lock) {
			while (doneSize + count > doneSlot.length) {
				doneSlot = grow (doneSlot);
				doneFlags = grow (doneFlags);
				doneTotal = grow (doneTotal);
			}
			System.arraycopy (doneSlot, 0, doneSlot, count, doneSize);
			System.arraycopy (doneFlags, 0, doneFlags, count, doneSize);
			System.arraycopy (doneTotal, 0, doneTotal, count, doneSize);
			System.arraycopy (uiDoneSlot, from, doneSlot, 0, count);
			System.arraycopy (uiDoneFlags, from, doneFlags, 0, count);
			System.arraycopy (uiDoneTotal, from, doneTotal, 0, count);
			doneSize += count;
		}
	}

	private void recordTurn (long turnStart) {
		long micros = (System.nanoTime () - turnStart) / 1000;
		turnMicros [turnCount & (turnMicros.length - 1)] = (int) Math.min (Integer.MAX_VALUE, micros);
		turnCount++;
	}

	/** Durations of the last drain turns in microseconds, oldest first (package proof hook). */
	int [] recentTurnMicros () {
		int n = Math.min (turnCount, turnMicros.length);
		int [] result = new int [n];
		for (int i = 0; i < n; i++) result [i] = turnMicros [(turnCount - n + i) & (turnMicros.length - 1)];
		return result;
	}

	/** Until the effective-visibility bit of the visibility leaf lands, visibility is <code>isVisible</code>. */
	private static boolean lazyContentShowing (Control control) {
		return control.isVisible ();
	}

	private void settle (int slot, int resultFlags, int total) {
		boolean cancelled;
		synchronized (lock) {
			cancelled = cancelBits [slot];
			cancelBits [slot] = false;
			workerTotals [slot] = VirtualBlockStore.UNKNOWN;
		}
		if (cancelled) {
			cache.free (slot);
			counters [CANCELLED]++;
			return;
		}
		if (slotGen [slot] != generation) {
			cache.free (slot);
			counters [STALE_DROPPED]++;
			return;
		}
		if ((resultFlags & VirtualBlockStore.FAILED) != 0) {
			cache.state (slot, VirtualBlockStore.FAILED);
			counters [FAILED]++;
			return;
		}
		cache.state (slot, VirtualBlockStore.LOADED);
		cache.total (slot, total);
		cache.touch (slot);
		counters [(resultFlags & RESTORED) != 0 ? SWAPPED_IN : FETCHED]++;
		int parentKey = cache.parentKey (slot);
		if (total >= 0 && cache.block (slot) == 0) {
			if (parentKey == ROOT) {
				if (host instanceof Tree tree) {
					if (tree.getItemCount () == 0) tree.setItemCount (total);
				} else if (host instanceof Table table) {
					if (table.getItemCount () == 0) table.setItemCount (total);
				}
			} else {
				int node = nodeIndexOfKey (parentKey);
				if (node >= 0 && nodeHints [node] == UNKNOWN) applyTotal (node, total);
			}
		}
		addRefresh (parentKey, cache.block (slot));
	}

	private void planTable (Table table) {
		int count = table.getItemCount ();
		wantSize = 0;
		if (count == 0) return;
		int top = table.getTopIndex ();
		int rows = visibleRows (table);
		int lastRow = Math.min (count, top + rows) - 1;
		int first = top / blockSize;
		int last = Math.max (first, lastRow / blockSize);
		int lastBlock = (count - 1) / blockSize;
		/*
		 * Prefetch by direction and velocity: a step scroll widens the overscan ahead by the
		 * blocks moved per frame (capped) and narrows it behind; a jump (thumb drag beyond the
		 * jump threshold) keeps the symmetric overscan, like Paging's jumpThreshold.
		 */
		int delta = lastPlannedTop < 0 ? 0 : top - lastPlannedTop;
		lastPlannedTop = top;
		int ahead = overscanBlocks, behind = overscanBlocks;
		int distance = Math.abs (delta);
		if (overscanBlocks > 0 && distance > 0 && distance <= JUMP_BLOCKS * blockSize) {
			int velocity = Math.min (overscanBlocks * 2, distance / blockSize + 1);
			int narrow = Math.max (1, overscanBlocks / 2);
			if (delta > 0) {
				ahead = overscanBlocks + velocity;
				behind = narrow;
			} else {
				behind = overscanBlocks + velocity;
				ahead = narrow;
			}
		}
		orderSize = 0;
		for (int b = first; b <= last; b++) order (b);
		for (int d = 1, n = Math.max (ahead, behind); d <= n; d++) {
			if (d <= ahead && last + d <= lastBlock) order (last + d);
			if (d <= behind && first - d >= 0) order (first - d);
		}
		for (int i = 0; i < orderSize; i++) wantTableBlock (orderBlocks [i]);
		int lowBlock = Math.max (0, first - behind);
		int highBlock = Math.min (lastBlock, last + ahead);
		cancelOutside (lowBlock, highBlock);
		if (debouncing (distance)) {
			wantSize = 0;
			sortQueueByOrder ();
			return;
		}
		enqueueWanted (count);
		sortQueueByOrder ();
	}

	/** True while a jump happened less than the load debounce ago; re-plans once it expires. */
	private boolean debouncing (int distance) {
		if (loadDebounce <= 0) return false;
		long now = System.currentTimeMillis ();
		if (distance > JUMP_BLOCKS * blockSize) lastJumpMillis = now;
		long left = lastJumpMillis + loadDebounce - now;
		if (left <= 0) return false;
		if (!debounceArmed) {
			debounceArmed = true;
			display.timerExec ((int) left, debounceRunnable);
		}
		return true;
	}

	private void order (int block) {
		if (orderSize == orderBlocks.length) orderBlocks = grow (orderBlocks);
		orderBlocks [orderSize++] = block;
	}

	private int rank (int block) {
		for (int i = 0; i < orderSize; i++) {
			if (orderBlocks [i] == block) return i;
		}
		return orderSize;
	}

	/** Visible blocks first, then overscan by distance: insertion sort of the (bounded) queue by rank. */
	private void sortQueueByOrder () {
		synchronized (lock) {
			for (int i = 1; i < queueSize; i++) {
				int slot = queueSlot [i], parent = queueParent [i], start = queueStart [i], rows = queueCount [i];
				int r = rank (start / blockSize);
				int j = i - 1;
				while (j >= 0 && rank (queueStart [j] / blockSize) > r) {
					queueSlot [j + 1] = queueSlot [j];
					queueParent [j + 1] = queueParent [j];
					queueStart [j + 1] = queueStart [j];
					queueCount [j + 1] = queueCount [j];
					j--;
				}
				queueSlot [j + 1] = slot;
				queueParent [j + 1] = parent;
				queueStart [j + 1] = start;
				queueCount [j + 1] = rows;
			}
		}
	}

	private void wantTableBlock (int block) {
		int slot = cache.find (ROOT, block);
		if (slot >= 0) {
			if ((cache.state (slot) & VirtualBlockStore.BUSY) == 0) cache.touch (slot);
			if (cache.state (slot) != VirtualBlockStore.FAILED) return;
			cache.free (slot);
		}
		if (wantSize == wantParent.length) {
			wantParent = grow (wantParent);
			wantBlock = grow (wantBlock);
		}
		wantParent [wantSize] = ROOT;
		wantBlock [wantSize] = block;
		wantSize++;
	}

	private void planTree () {
		int count = wantSize;
		int kept = 0;
		for (int i = 0; i < count; i++) {
			int slot = cache.find (wantParent [i], wantBlock [i]);
			if (slot >= 0) {
				if (cache.state (slot) != VirtualBlockStore.FAILED) {
					if ((cache.state (slot) & VirtualBlockStore.BUSY) == 0) {
						cache.touch (slot);
						addRefresh (wantParent [i], wantBlock [i]);
					}
					continue;
				}
				cache.free (slot);
			}
			wantParent [kept] = wantParent [i];
			wantBlock [kept] = wantBlock [i];
			kept++;
		}
		wantSize = kept;
		enqueueWanted (-1);
	}

	/** Reserves and queues the wanted blocks in order; requests that find no free slot wait. */
	private void enqueueWanted (int flatCount) {
		int started = 0;
		int kept = 0;
		for (int i = 0; i < wantSize; i++) {
			int parentKey = wantParent [i];
			int block = wantBlock [i];
			if (cache.find (parentKey, block) >= 0) continue;
			int slot = cache.reserve (parentKey, block);
			if (slot < 0) {
				wantParent [kept] = parentKey;
				wantBlock [kept] = block;
				kept++;
				continue;
			}
			int start = block * blockSize;
			int rows = rowsFor (parentKey, start, flatCount);
			if (rows <= 0) {
				cache.free (slot);
				continue;
			}
			counters [REQUESTED]++;
			synchronized (lock) {
				if (queueSize == queueSlot.length) {
					queueSlot = grow (queueSlot);
					queueParent = grow (queueParent);
					queueStart = grow (queueStart);
					queueCount = grow (queueCount);
				}
				queueSlot [queueSize] = slot;
				queueParent [queueSize] = parentKey;
				queueStart [queueSize] = start;
				queueCount [queueSize] = rows;
				queueSize++;
				slotGen [slot] = generation;
				cancelBits [slot] = false;
				workerTotals [slot] = VirtualBlockStore.UNKNOWN;
				lock.notifyAll ();
			}
			started++;
		}
		wantSize = (flags & TREE) != 0 ? kept : 0;
		if (started > 0) ensureWorkers ();
	}

	/** Cancels flat requests outside <code>[lowBlock, highBlock]</code>, queued or running. */
	private void cancelOutside (int lowBlock, int highBlock) {
		synchronized (lock) {
			int kept = 0;
			for (int i = 0; i < queueSize; i++) {
				int slot = queueSlot [i];
				int block = queueStart [i] / blockSize;
				if (queueParent [i] == ROOT && !apiWanted [slot] && (block < lowBlock || block > highBlock)) {
					cache.free (slot);
					counters [CANCELLED]++;
					continue;
				}
				queueSlot [kept] = slot;
				queueParent [kept] = queueParent [i];
				queueStart [kept] = queueStart [i];
				queueCount [kept] = queueCount [i];
				kept++;
			}
			queueSize = kept;
			for (int slot = 0; slot < cache.capacity (); slot++) {
				if ((cache.state (slot) & VirtualBlockStore.BUSY) == 0 || cache.parentKey (slot) != ROOT) continue;
				int block = cache.block (slot);
				if (!apiWanted [slot] && (block < lowBlock || block > highBlock)) cancelBits [slot] = true;
			}
		}
	}

	private void cancelParent (int parentKey) {
		synchronized (lock) {
			int kept = 0;
			for (int i = 0; i < queueSize; i++) {
				if (queueParent [i] == parentKey) {
					cache.free (queueSlot [i]);
					counters [CANCELLED]++;
					continue;
				}
				queueSlot [kept] = queueSlot [i];
				queueParent [kept] = queueParent [i];
				queueStart [kept] = queueStart [i];
				queueCount [kept] = queueCount [i];
				kept++;
			}
			queueSize = kept;
			for (int slot = 0; slot < cache.capacity (); slot++) {
				if ((cache.state (slot) & VirtualBlockStore.BUSY) != 0 && cache.parentKey (slot) == parentKey) cancelBits [slot] = true;
			}
		}
		int kept = 0;
		for (int i = 0; i < wantSize; i++) {
			if (wantParent [i] == parentKey) continue;
			wantParent [kept] = wantParent [i];
			wantBlock [kept] = wantBlock [i];
			kept++;
		}
		wantSize = kept;
	}

	private void addRefresh (int parentKey, int block) {
		for (int i = 0; i < refreshSize; i++) {
			if (refreshParent [i] == parentKey && (refreshBlock [i] == block || refreshBlock [i] == REFRESH_ALL)) return;
		}
		if (refreshSize == refreshParent.length) {
			refreshParent = grow (refreshParent);
			refreshBlock = grow (refreshBlock);
		}
		refreshParent [refreshSize] = parentKey;
		refreshBlock [refreshSize] = block;
		refreshSize++;
	}

	private void applyRefreshes (Control control) {
		int count = refreshSize;
		refreshSize = 0;
		for (int i = 0; i < count; i++) {
			int parentKey = refreshParent [i];
			int block = refreshBlock [i];
			if (control instanceof Table table) refreshTable (table, block);
			else refreshTree ((Tree) control, parentKey, block);
		}
	}

	private void refreshTable (Table table, int block) {
		int count = table.getItemCount ();
		if (count == 0) return;
		int top = table.getTopIndex ();
		int rows = visibleRows (table);
		int overscanRows = overscanBlocks * blockSize;
		int low = Math.max (0, top - overscanRows);
		int high = Math.min (count - 1, top + rows + overscanRows);
		int start = block == REFRESH_ALL ? low : Math.max (low, block * blockSize);
		int end = block == REFRESH_ALL ? high : Math.min (high, block * blockSize + blockSize - 1);
		if (start > end) return;
		if (hook != null) hook.pageSourceRefresh (null, start, end);
		else table.clear (start, end);
	}

	private void refreshTree (Tree tree, int parentKey, int block) {
		TreeItem parent = null;
		int count;
		if (parentKey == ROOT) {
			count = tree.getItemCount ();
		} else {
			int node = nodeIndexOfKey (parentKey);
			if (node < 0) return;
			parent = nodeItems [node];
			if (!parent.getExpanded ()) return;
			count = parent.getItemCount ();
		}
		if (count == 0) return;
		int start = block == REFRESH_ALL ? 0 : block * blockSize;
		int end = block == REFRESH_ALL ? count - 1 : Math.min (count - 1, start + blockSize - 1);
		if (start > end) return;
		if (hook != null) {
			hook.pageSourceRefresh (parent, start, end);
		} else if (parent == null) {
			for (int i = start; i <= end; i++) tree.clear (i, false);
		} else {
			for (int i = start; i <= end; i++) parent.clear (i, false);
		}
	}

	private static int visibleRows (Table table) {
		int itemHeight = Math.max (1, table.getItemHeight ());
		return table.getClientArea ().height / itemHeight + 1;
	}

	private void checkThread () {
		if (display != null && display.getThread () != Thread.currentThread ()) SWT.error (SWT.ERROR_THREAD_INVALID_ACCESS);
	}

	/* ---------------------------------------------------------------- workers */

	private void ensureWorkers () {
		if (workers != null) return;
		workers = new Thread [threadCount];
		for (int i = 0; i < threadCount; i++) {
			Request request = new Request (this);
			Thread worker = new Thread (() -> work (request), "SWT VirtualPageSource worker " + i);
			worker.setDaemon (true);
			workers [i] = worker;
			worker.start ();
		}
	}

	private void work (Request request) {
		while (true) {
			synchronized (lock) {
				while (!stopped && queueSize == 0) {
					try {
						lock.wait ();
					} catch (InterruptedException e) {
						return;
					}
				}
				if (stopped) return;
				request.slot = queueSlot [0];
				request.parentKey = queueParent [0];
				request.start = queueStart [0];
				request.count = queueCount [0];
				queueSize--;
				System.arraycopy (queueSlot, 1, queueSlot, 0, queueSize);
				System.arraycopy (queueParent, 1, queueParent, 0, queueSize);
				System.arraycopy (queueStart, 1, queueStart, 0, queueSize);
				System.arraycopy (queueCount, 1, queueCount, 0, queueSize);
			}
			int resultFlags = VirtualBlockStore.LOADED;
			if (!isCancelled (request.slot)) {
				if (restore (request)) {
					resultFlags |= RESTORED;
				} else {
					try {
						fetcher.fetch (request);
					} catch (Exception | LinkageError e) {
						resultFlags = VirtualBlockStore.FAILED;
					}
				}
			}
			boolean schedule;
			CompletableFuture<?> waiter;
			synchronized (lock) {
				if (stopped) return;
				if (doneSize == doneSlot.length) {
					doneSlot = grow (doneSlot);
					doneFlags = grow (doneFlags);
					doneTotal = grow (doneTotal);
				}
				doneSlot [doneSize] = request.slot;
				doneFlags [doneSize] = resultFlags;
				doneTotal [doneSize] = workerTotals [request.slot];
				doneSize++;
				schedule = !frameScheduled;
				frameScheduled = true;
				lock.notifyAll ();
				waiter = waiters [request.slot];
				waiters [request.slot] = null;
			}
			if (waiter != null) waiter.complete (null);
			request.slot = -1;
			if (schedule) {
				Display d = display;
				if (d == null || d.isDisposed ()) return;
				d.asyncExec (frameRunnable);
			}
		}
	}

	/* The one growth path of the source's lanes: Xmx-aware (exact length under pressure). */
	private static int [] grow (int [] array) {
		int [] result = new int [VirtualGrowth.capacity (array.length, array.length + 1, 4)];
		System.arraycopy (array, 0, result, 0, array.length);
		return result;
	}

	private static TreeItem [] grow (TreeItem [] array) {
		TreeItem [] result = new TreeItem [VirtualGrowth.capacity (array.length, array.length + 1, 8)];
		System.arraycopy (array, 0, result, 0, array.length);
		return result;
	}
}
