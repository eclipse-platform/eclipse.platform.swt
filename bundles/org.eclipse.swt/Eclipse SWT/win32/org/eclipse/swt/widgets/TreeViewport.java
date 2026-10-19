/*******************************************************************************
 * Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0, or (at your option) the Eclipse
 * Public License 2.0, which are available at
 * https://www.apache.org/licenses/LICENSE-2.0 and
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
 *
 * Contributors:
 *     Hitesh Soliwal (Synexia) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.win32.*;

/**
 * Viewport of a win32 <code>SWT.VIRTUAL</code> Tree.
 * <p>
 * The Tree keeps its logical rows in {@link VirtualTreeTopology} and answers its public API
 * from it. The native TreeView is an ordinary control that holds only the resident window:
 * the rows on screen, the overscan and their ancestors
 * ({@link VirtualTreeVisibleProjection.Residency}). It does not scroll itself
 * (<code>TVS_NOSCROLL</code>); it is a content plane inside <code>hwndParent</code>,
 * positioned so that the logical top row is at the top of the body. <code>hwndParent</code>
 * owns the vertical scroll bar, whose range is the logical row count
 * ({@link VirtualTreeViewport}).
 * </p><p>
 * While a row is resident, its selection and expansion are native; when it leaves the
 * window they are kept in the topology lanes, and a returning row gets them back. A
 * TreeItem whose row is not resident keeps its identity with a zero handle.
 * </p><p>
 * Only window-bounded work runs here: inserting, deleting and positioning at most one
 * window of native rows per change.
 * </p>
 */
final class TreeViewport {
	/** The viewport is the default Tree mode; <code>-Dswt.widget.mode.Tree=original</code> (or the global <code>swt.widget.mode</code>) selects the native Tree at startup. */
	static final boolean ENABLED = org.eclipse.swt.internal.WidgetMode.of ("Tree", org.eclipse.swt.internal.WidgetMode.VIEWPORT) == org.eclipse.swt.internal.WidgetMode.VIEWPORT;
	/**
	 * Test hook, read when a Tree is created: the recorded differential builds its native reference
	 * Tree (same style, same SetData) in the same display while the viewport is the default.
	 */
	static boolean nativeReference;
	/** Sentinel parent handle of {@link TreeItem}'s internal constructor: create a facade without a native row. */
	static final long DETACHED = Long.MIN_VALUE;
	private static final org.eclipse.swt.internal.SwtLog LOG = org.eclipse.swt.internal.SwtLog.getLog ("org.eclipse.swt.widgets.Tree");

	final Tree tree;
	private final VirtualTreeVisibleProjection.Residency residency = new VirtualTreeVisibleProjection.Residency ();
	private final VirtualTreeResidentPlan plan = new VirtualTreeResidentPlan ();
	private long committedGeneration = -1, committedFirstRow = -1;
	private int committedCount = -1;
	private int focusId = -1;
	/* the logical caret moved and the native caret has not followed yet: native rows must not overwrite it */
	private boolean focusPending;
	/*
	 * selectAll pattern, per parent (root = VirtualTreeTopology.ROOT): its cold children with a child index
	 * below the limit are selected. The limit is the child count at selectAll; an insert or a removal before it
	 * shifts it, a shrink clamps it, so rows added later are not selected (as natively). Stored as segments
	 * {limit0, epoch0, limit1, epoch1, ...} with ascending limits: a later selectAll that covers more children
	 * adds a segment, and the segment of a child names the selectAll (its selection clock value) that selected it.
	 */
	private final HashMap<Integer, long []> patterns = new HashMap<> ();
	private boolean anyChildrenSelected;
	/*
	 * Selection recency, the order in which rows became selected (the native TreeView deselects by it when it moves
	 * the caret off a deleted row, see deselectOnCaretMoves): per facade id the clock value at which its row last
	 * became selected; the epochs are the clock values of the selectAll calls still present in a pattern.
	 */
	private long [] selectedAt = new long [0];
	private long selectionClock;
	private final TreeSet<Long> patternEpochs = new TreeSet<> ();
	/*
	 * Per plan entry: the row's label was re-measured while it was inside the client area. A rebuild
	 * clears every bit, because rows it touches are measured while SWT declines their data.
	 */
	private boolean [] measured = new boolean [0];
	private boolean remeasurePending;
	/* the mediator itself is driving the native control (style toggle, TVM_EXPAND): no SetData for what is in view only in between */
	private boolean quiet;
	private boolean busy, rebuilding;
	/* the mediator is moving the native caret (restoreFocus, clearNativeCaret): native selection is transient, lanes are not captured */
	private int drivingCaret;
	/* counts window rebuilds, so a native call that rebuilt the window re-entrantly is noticed */
	private int rebuilds;

	TreeViewport (Tree tree) {
		this.tree = tree;
	}

	/* ---------------------------------------------------------------- layout */

	/** Wraps the TreeView in its viewport and makes it a content plane. */
	void install () {
		VirtualTreeTopology topology = tree.virtualTopology;
		if (!topology.childCountKnown (VirtualTreeTopology.ROOT)) topology.setChildCount (VirtualTreeTopology.ROOT, 0);
		if (tree.hwndParent == 0) tree.createParent ();
		long tv = tree.handle;
		int bits = OS.GetWindowLong (tv, OS.GWL_STYLE);
		OS.SetWindowLong (tv, OS.GWL_STYLE, (bits | OS.TVS_NOSCROLL | OS.WS_CLIPSIBLINGS) & ~(OS.WS_VSCROLL | OS.WS_HSCROLL));
		OS.SetWindowPos (tv, 0, 0, 0, 0, 0, OS.SWP_NOMOVE | OS.SWP_NOSIZE | OS.SWP_NOZORDER | OS.SWP_NOACTIVATE | OS.SWP_FRAMECHANGED);
		layout ();
	}

	/** Recomputes geometry, moves the resident window if needed, positions the plane and updates the bar. */
	void layout () {
		if (busy || tree.isDisposed () || tree.hwndParent == 0) return;
		busy = true;
		try {
			int itemHeight = Math.max (1, tree.getItemHeightInPixels ());
			int body = bodyHeight ();
			/* the native control counts fully visible rows; the partial last row comes from the overscan */
			tree.virtualViewport.configureGeometry (itemHeight, body / itemHeight * itemHeight);
			ensureWindow ();
			/* the bar first: showing or hiding it changes the client width the plane is laid out in */
			updateBar ();
			positionPlane ();
		} finally {
			busy = false;
		}
	}

	private int bodyHeight () {
		RECT client = new RECT ();
		OS.GetClientRect (tree.hwndParent, client);
		return Math.max (0, client.bottom - client.top - headerHeight ());
	}

	private int headerHeight () {
		if (tree.hwndHeader == 0 || !OS.IsWindowVisible (tree.hwndHeader)) return 0;
		RECT rect = new RECT ();
		OS.GetWindowRect (tree.hwndHeader, rect);
		return rect.bottom - rect.top;
	}

	private void ensureWindow () {
		VirtualTreeViewport viewport = tree.virtualViewport;
		long first = viewport.firstPaintRow ();
		int count = viewport.paintRowCount ();
		if (tree.virtualTopology.generation () == committedGeneration && first == committedFirstRow && count == committedCount) return;
		rebuild ();
		committedGeneration = tree.virtualTopology.generation ();
		committedFirstRow = first;
		committedCount = count;
		M3Prefetch.windowChanged (tree);
		M3VisibilityObserver.changed (tree);
	}

	/** Moves the native window to the current paint window: retire, insert, commit. */
	private void rebuild () {
		long tv = tree.handle;
		captureResidentState ();
		tree.virtualViewport.paintResidency (residency);
		NativeWindow window = nativeWindow ();
		plan.plan (window.count, window.parentIds, window.childIndices, window.physicalParents);
		int count = plan.plannedCount ();
		boolean redraw = tree.getDrawing () && OS.IsWindowVisible (tv);
		if (redraw) OS.DefWindowProc (tv, OS.WM_SETREDRAW, 0, 0);
		boolean oldShrink = tree.ignoreShrink, oldSelect = tree.ignoreSelect, oldDeselect = tree.ignoreDeselect;
		tree.ignoreShrink = tree.ignoreSelect = tree.ignoreDeselect = true;
		int retired = plan.retiredCount (), inserted = 0;
		boolean [] measuredNext = null;
		rebuilding = true;
		rebuilds++;
		try {
			long caret = OS.SendMessage (tv, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
			for (int i = 0; i < retired; i++) {
				long hItem = plan.retired (i);
				/* always detach: without a caret too, a retired row's facade must not keep a handle the TreeView reuses */
				boolean caretInside = detachSubtree (hItem, caret);
				if (caret != 0 && caretInside) {
					clearNativeCaret ();
					caret = 0;
				}
				OS.SendMessage (tv, OS.TVM_DELETEITEM, 0, hItem);
			}
			long [] handles = new long [count];
			measuredNext = new boolean [count];
			for (int entry = 0; entry < count; entry++) {
				long reused = plan.plannedHandle (entry);
				if (reused != 0) {
					handles [entry] = reused;
					continue;
				}
				int physicalParent = window.physicalParents [entry];
				long hParent = physicalParent == -1 ? OS.TVI_ROOT : handles [physicalParent];
				int previous = plan.previousSibling (entry);
				long hAfter = previous == -1 ? OS.TVI_FIRST : handles [previous];
				handles [entry] = insertRow (hParent, hAfter, window.ids [entry], window.parentIds [entry], window.childIndices [entry]);
				inserted++;
			}
			plan.commit (window.count, window.parentIds, window.childIndices, window.physicalParents, handles);
			measured = measuredNext;
			rebuilding = false;
			bindResidentFacades ();
			syncResidentShape ();
			resetPlaneTop ();
			restoreFocus ();
			tree.cachedFirstItem = tree.cachedIndexItem = 0;
			tree.cachedItemCount = -1;
			tree.pageCursorParent = Tree.PAGE_NO_CURSOR;
		} finally {
			rebuilding = false;
			tree.ignoreShrink = oldShrink;
			tree.ignoreSelect = oldSelect;
			tree.ignoreDeselect = oldDeselect;
			if (redraw) {
				OS.DefWindowProc (tv, OS.WM_SETREDRAW, 1, 0);
				OS.InvalidateRect (tv, null, true);
			}
		}
		/*
		 * Outside the rebuild (ignoreShrink restored) and after the top is set: SWT answers a virtual
		 * row's data when the row is inside the client area or its data is already cached.
		 */
		remeasurePending = true;
		final int ins = inserted;
		LOG.trace (() -> "Tree window: rows " + residency.firstPaintRow () + "+" + residency.paintCount ()
				+ ", native " + plan.size () + " (" + ins + " inserted, " + retired + " retired subtrees)");
	}

	/*
	 * A row kept across a window move keeps its native state; its expansion and expander must
	 * follow the lanes, or the TreeView would expand or collapse it on its own (for example when
	 * the caret moves onto a child of a row that is collapsed natively).
	 */
	private void syncResidentShape () {
		VirtualTreeTopology topology = tree.virtualTopology;
		TVITEM tvItem = new TVITEM ();
		for (int entry = 0; entry < plan.size (); entry++) {
			tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_PARAM | OS.TVIF_STATE | OS.TVIF_CHILDREN;
			tvItem.stateMask = OS.TVIS_EXPANDED;
			tvItem.hItem = plan.handle (entry);
			if (OS.SendMessage (tree.handle, OS.TVM_GETITEM, 0, tvItem) == 0) continue;
			int id = idOf (facadeOrNull ((int) tvItem.lParam));
			if (id < 0) continue;
			boolean expanded = topology.flag (id, VirtualItemState.EXPANDED);
			int children = topology.childCountKnown (id) && topology.childCount (id) > 0 ? 1 : 0;
			boolean nativeExpanded = (tvItem.state & OS.TVIS_EXPANDED) != 0;
			long hItem = tvItem.hItem;
			if (tvItem.cChildren != children) {
				tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_CHILDREN;
				tvItem.cChildren = children;
				OS.SendMessage (tree.handle, OS.TVM_SETITEM, 0, tvItem);
			}
			if (nativeExpanded != expanded || expanded && !childrenLaidOut (hItem)) setNativeExpanded (hItem, expanded);
		}
	}

	/* an expanded row whose first resident child has no rectangle shows the glyph but hides its children */
	private boolean childrenLaidOut (long hItem) {
		long child = OS.SendMessage (tree.handle, OS.TVM_GETNEXTITEM, OS.TVGN_CHILD, hItem);
		if (child == 0) return true;
		RECT rect = new RECT ();
		return OS.TreeView_GetItemRect (tree.handle, child, rect, false);
	}

	/**
	 * Expands or collapses a resident row natively with TVM_EXPAND: TVM_SETITEM with TVIS_EXPANDED only
	 * changes the glyph and leaves the children hidden. No SWT event is sent (the logical change already
	 * sent its events) and a caret moved by a native collapse is put back by restoreFocus.
	 */
	private void setNativeExpanded (long hItem, boolean expanded) {
		long tv = tree.handle;
		if (expanded) {
			/* a row that already carries the glyph treats TVE_EXPAND as done: clear the bit first */
			TVITEM tvItem = new TVITEM ();
			tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_STATE;
			tvItem.hItem = hItem;
			tvItem.stateMask = OS.TVIS_EXPANDED;
			tvItem.state = 0;
			OS.SendMessage (tv, OS.TVM_SETITEM, 0, tvItem);
		}
		boolean oldExpand = tree.ignoreExpand, oldSelect = tree.ignoreSelect, oldDeselect = tree.ignoreDeselect, oldQuiet = quiet;
		tree.ignoreExpand = tree.ignoreSelect = tree.ignoreDeselect = quiet = true;
		try {
			OS.SendMessage (tv, OS.TVM_EXPAND, expanded ? OS.TVE_EXPAND : OS.TVE_COLLAPSE, hItem);
		} finally {
			quiet = oldQuiet;
			tree.ignoreExpand = oldExpand;
			tree.ignoreSelect = oldSelect;
			tree.ignoreDeselect = oldDeselect;
		}
		LOG.trace (() -> "Tree native " + (expanded ? "expand" : "collapse") + " 0x" + Long.toHexString (hItem));
	}

	/* A facade made for a resident row while the window moved is bound once the window is committed. */
	/*
	 * A row inserted before its facade was bound was measured with an empty label, and the
	 * TreeView keeps that width for hit testing and bounds. Re-announcing the text callback, as
	 * TreeItem.setText does, makes it measure the label again.
	 */
	/**
	 * SetData is mediated for the resident rows (TVN_GETDISPINFO). A row whose data is cached is
	 * answered wherever it is in the window, so the TreeView measures its label right; a row inside
	 * the client area gets SetData now, as a plain virtual Tree's visible row does. During a rebuild
	 * only rows that already have cached data are answered (no application code runs mid-rebuild);
	 * every other row is declined and answered when it is painted in view.
	 */
	boolean answersDispInfo (long hItem) {
		if (hItem != 0 && hItem == probeHandle) return true;
		int entry = plan.entryOf (hItem);
		if (entry < 0) {
			LOG.trace (() -> "Tree dispinfo 0x" + Long.toHexString (hItem) + ": not in the plan");
			return false;
		}
		if (rebuilding || quiet) {
			TVITEM tvItem = new TVITEM ();
			tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_PARAM;
			tvItem.hItem = hItem;
			if (OS.SendMessage (tree.handle, OS.TVM_GETITEM, 0, tvItem) == 0) return false;
			TreeItem item = facadeOrNull ((int) tvItem.lParam);
			return item != null && item.isCachedState ();
		}
		long key = plan.keyOf (entry);
		TreeItem item = facade ((int) (key >> 32), (int) key);
		if (item.isCachedState ()) return true;
		RECT client = new RECT (), rect = new RECT ();
		OS.GetClientRect (tree.handle, client);
		boolean inClient = OS.TreeView_GetItemRect (tree.handle, hItem, rect, false) && rect.bottom > client.top && rect.top < client.bottom;
		if (LOG.isTraceEnabled ()) {
			final int y = rect.top, h = client.bottom;
			LOG.trace (() -> "Tree dispinfo key " + Long.toHexString (key) + " y " + y + " client " + h + (inClient ? ": SetData" : ": declined (out of view)"));
		}
		return inClient;
	}

	/*
	 * Labels are re-measured when the TreeView paints, as a plain virtual Tree asks for row data only when it
	 * paints: an intermediate layout (an expansion laid out before the scroll that follows it) must not fill
	 * rows that are in view only in between (recorded differential, stock seed 0 step 10).
	 */
	void beforePaint () {
		if (!remeasurePending || rebuilding) return;
		remeasurePending = false;
		remeasureClientRows ();
	}

	private void remeasureClientRows () {
		if (rebuilding || measured.length != plan.size ()) return;
		long tv = tree.handle;
		RECT client = new RECT (), rect = new RECT ();
		OS.GetClientRect (tv, client);
		TVITEM tvItem = new TVITEM ();
		tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_TEXT;
		tvItem.pszText = OS.LPSTR_TEXTCALLBACK;
		for (int entry = 0; entry < measured.length; entry++) {
			if (measured [entry]) continue;
			long hItem = plan.handle (entry);
			if (!OS.TreeView_GetItemRect (tv, hItem, rect, false) || rect.bottom <= client.top || rect.top >= client.bottom) continue;
			/* SWT answers TVN_GETDISPINFO only for a row with a facade (lParam), as a plain virtual Tree has for every painted row */
			long key = plan.keyOf (entry);
			facade ((int) (key >> 32), (int) key);
			tvItem.hItem = hItem;
			OS.SendMessage (tv, OS.TVM_SETITEM, 0, tvItem);
			measured [entry] = true;
			if (LOG.isTraceEnabled ()) {
				RECT label = new RECT ();
				OS.TreeView_GetItemRect (tv, hItem, label, true);
				final int width = label.right - label.left, top = rect.top;
				LOG.trace (() -> "Tree re-measured row at y " + top + " key " + Long.toHexString (key) + ": label width " + width);
			}
		}
	}

	private void bindResidentFacades () {
		TVITEM tvItem = new TVITEM ();
		tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_PARAM;
		for (int entry = 0; entry < plan.size (); entry++) {
			tvItem.hItem = plan.handle (entry);
			if (OS.SendMessage (tree.handle, OS.TVM_GETITEM, 0, tvItem) == 0) continue;
			TreeItem item = facadeOrNull ((int) tvItem.lParam);
			if (item != null && idOf (item) < 0 && item.virtualId >= 0) {
				long key = plan.keyOf (entry);
				bindCoordinate (item.virtualId, (int) (key >> 32), (int) key);
			}
		}
	}

	/** The native rows of one window move, in preorder: the paint window plus the focus anchor. */
	static final class NativeWindow {
		int count;
		int [] parentIds, childIndices, physicalParents, ids;
	}

	/**
	 * The paint window and, when the caret row lies outside it, the caret row with its ancestors
	 * as an anchor. The anchor stays native so the caret keeps its native row (focus, keyboard and
	 * WM_SETFOCUS behave natively); it lies above or below the paint rows in preorder, so the
	 * content plane clips it and it is never painted.
	 */
	private NativeWindow nativeWindow () {
		VirtualTreeTopology topology = tree.virtualTopology;
		int n = residency.size ();
		ArrayList<int []> paths = new ArrayList<> (n + 8);
		ArrayList<int []> entries = new ArrayList<> (n + 8); /* {parentId, childIndex, id} */
		HashMap<Long, Integer> byKey = new HashMap<> ();
		for (int entry = 0; entry < n; entry++) {
			int parentEntry = residency.physicalParent (entry);
			int [] parentPath = parentEntry == -1 ? new int [0] : paths.get (parentEntry);
			int [] path = Arrays.copyOf (parentPath, parentPath.length + 1);
			path [parentPath.length] = residency.childIndex (entry);
			paths.add (path);
			int id = residency.materializedId (entry);
			entries.add (new int [] {residency.parentId (entry), residency.childIndex (entry), id});
			byKey.put (VirtualTreeResidentPlan.key (residency.parentId (entry), residency.childIndex (entry)), entry);
		}
		if (focusId >= 0 && topology.contains (focusId) && tree.virtualProjection.visibleIndexOf (focusId) >= 0) {
			ArrayDeque<Integer> chain = new ArrayDeque<> ();
			for (int id = focusId; id != VirtualTreeTopology.ROOT; id = topology.parentId (id)) chain.push (id);
			int [] path = new int [0];
			for (int id : chain) {
				int parentId = topology.parentId (id), index = topology.childIndex (id);
				path = Arrays.copyOf (path, path.length + 1);
				path [path.length - 1] = index;
				long key = VirtualTreeResidentPlan.key (parentId, index);
				if (byKey.containsKey (key)) continue;
				byKey.put (key, entries.size ());
				paths.add (path);
				entries.add (new int [] {parentId, index, id});
			}
		}
		Integer [] order = new Integer [entries.size ()];
		for (int i = 0; i < order.length; i++) order [i] = i;
		Arrays.sort (order, (a, b) -> Arrays.compare (paths.get (a), paths.get (b)));
		NativeWindow window = new NativeWindow ();
		window.count = order.length;
		window.parentIds = new int [order.length];
		window.childIndices = new int [order.length];
		window.physicalParents = new int [order.length];
		window.ids = new int [order.length];
		HashMap<Integer, Integer> entryOfId = new HashMap<> ();
		for (int i = 0; i < order.length; i++) {
			int [] e = entries.get (order [i]);
			window.parentIds [i] = e [0];
			window.childIndices [i] = e [1];
			window.ids [i] = e [2];
			if (e [2] >= 0) entryOfId.put (e [2], i);
			Integer parentEntry = e [0] == VirtualTreeTopology.ROOT ? Integer.valueOf (-1) : entryOfId.get (e [0]);
			if (parentEntry == null) throw new IllegalStateException ("native window without its parent row");
			window.physicalParents [i] = parentEntry;
		}
		return window;
	}

	/* The visible row of a native row's coordinate, or -1 when it is hidden under a collapsed row. */
	private long rowOfKey (long key) {
		int parentId = (int) (key >> 32), index = (int) key;
		VirtualTreeVisibleProjection projection = tree.virtualProjection;
		if (parentId != VirtualTreeTopology.ROOT
				&& (projection.visibleIndexOf (parentId) < 0 || !tree.virtualTopology.flag (parentId, VirtualItemState.EXPANDED))) {
			return -1;
		}
		return projection.visibleIndexOfCoordinate (parentId, index);
	}

	private long insertRow (long hParent, long hAfter, int id, int parentId, int childIndex) {
		VirtualTreeTopology topology = tree.virtualTopology;
		TreeItem item = facadeOrNull (id);
		TVINSERTSTRUCT insert = new TVINSERTSTRUCT ();
		insert.hParent = hParent;
		insert.hInsertAfter = hAfter;
		insert.lParam = item != null ? id : -1;
		insert.pszText = OS.LPSTR_TEXTCALLBACK;
		insert.iImage = insert.iSelectedImage = OS.I_IMAGECALLBACK;
		insert.mask = OS.TVIF_TEXT | OS.TVIF_IMAGE | OS.TVIF_SELECTEDIMAGE | OS.TVIF_HANDLE | OS.TVIF_PARAM
				| OS.TVIF_CHILDREN | OS.TVIF_STATE;
		insert.cChildren = item != null && topology.childCountKnown (id) && topology.childCount (id) > 0 ? 1 : 0;
		int state = 0, mask = OS.TVIS_EXPANDED | OS.TVIS_SELECTED;
		if (item != null && topology.flag (id, VirtualItemState.EXPANDED)) state |= OS.TVIS_EXPANDED;
		if (id >= 0 ? item != null && topology.flag (id, VirtualItemState.SELECTED) : patternSelects (parentId, childIndex)) state |= OS.TVIS_SELECTED;
		if ((tree.style & SWT.CHECK) != 0) {
			state |= (item != null ? checkImage (id) : 1) << 12;
			mask |= OS.TVIS_STATEIMAGEMASK;
		}
		insert.state = state;
		insert.stateMask = mask;
		/* the TreeView measures a new row through NM_CUSTOMDRAW; as in createItem, no facade is made for it here */
		boolean oldIgnoreCustomDraw = tree.ignoreCustomDraw;
		tree.ignoreCustomDraw = true;
		long hItem;
		try {
			hItem = OS.SendMessage (tree.handle, OS.TVM_INSERTITEM, 0, insert);
		} finally {
			tree.ignoreCustomDraw = oldIgnoreCustomDraw;
		}
		if (hItem == 0) tree.error (SWT.ERROR_ITEM_NOT_ADDED);
		if (item != null) item.handle = hItem;
		return hItem;
	}

	/* the check-box state image of a facade's lanes: 1 unchecked, 2 checked, 3 grayed, 4 checked and grayed */
	private int checkImage (int id) {
		VirtualTreeTopology topology = tree.virtualTopology;
		int image = 1;
		if (topology.flag (id, VirtualItemState.CHECKED)) image++;
		if (topology.flag (id, VirtualItemState.GRAYED)) image += 2;
		return image;
	}

	/*
	 * Attaches a facade to the resident native row of its coordinate: the row names the facade (lParam) and the
	 * facade knows its row, so its setters write through. The row's check-box image follows the facade's lanes:
	 * rows and handles are reused, so the row may still show what a previous occupant had, and TreeItem's virtual
	 * setChecked writes natively only when the lane changes (check+columns seed 10 under a slow paint: a box
	 * painted checked where stock paints it unchecked).
	 */
	private void attachRow (TreeItem item, long hItem) {
		TVITEM tvItem = new TVITEM ();
		tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_PARAM;
		tvItem.hItem = hItem;
		tvItem.lParam = item.virtualId;
		if ((tree.style & SWT.CHECK) != 0) {
			tvItem.mask |= OS.TVIF_STATE;
			tvItem.stateMask = OS.TVIS_STATEIMAGEMASK;
			tvItem.state = checkImage (item.virtualId) << 12;
		}
		OS.SendMessage (tree.handle, OS.TVM_SETITEM, 0, tvItem);
		item.handle = hItem;
	}

	/**
	 * The facade's validated native row; when the facade lost its handle while its coordinate stayed resident
	 * (a reused row), the row is attached again. Zero when the row is not resident or the window is moving.
	 */
	long rowOf (TreeItem item) {
		long hItem = residentHandle (item);
		if (hItem != 0 || rebuilding) return hItem;
		int id = idOf (item);
		if (id < 0) return 0;
		VirtualTreeTopology topology = tree.virtualTopology;
		hItem = plan.handleOf (topology.parentId (id), topology.childIndex (id));
		if (hItem != 0) attachRow (item, hItem);
		return hItem;
	}

	/* ---------------------------------------------------------------- geometry of rows outside the window */

	/* TVM_GETINDENT (TV_FIRST + 6): the indentation of one tree level */
	private static final int TVM_GETINDENT = 0x1106;

	private long probeHandle, probeRow;
	private int probeDepth;

	/**
	 * Bounds of an item whose row is not resident. Stock SWT answers geometry for every row of an expanded
	 * branch, also far outside the client area (TVM_GETITEMRECT), and an empty rectangle for a row inside a
	 * collapsed branch. For the duration of the call the item gets a native probe row, so the unchanged stock
	 * code measures it (text, font, image, columns); the probe sits at the end of the root level with drawing
	 * off, and {@link #translateProbe} moves each native rectangle of it to the row's logical place.
	 */
	RECT offWindowBounds (TreeItem item, int index, boolean getText, boolean getImage, boolean fullText, boolean fullImage, boolean clip, long hDC) {
		int id = idOf (item);
		long row = id < 0 ? -1 : tree.virtualProjection.visibleIndexOf (id);
		if (row < 0 || probeHandle != 0) return new RECT ();
		VirtualTreeTopology topology = tree.virtualTopology;
		long tv = tree.handle;
		boolean redraw = tree.getDrawing () && OS.IsWindowVisible (tv);
		if (redraw) OS.DefWindowProc (tv, OS.WM_SETREDRAW, 0, 0);
		boolean oldShrink = tree.ignoreShrink, oldSelect = tree.ignoreSelect, oldDeselect = tree.ignoreDeselect;
		tree.ignoreShrink = tree.ignoreSelect = tree.ignoreDeselect = true;
		long hProbe = 0;
		try {
			hProbe = insertRow (OS.TVI_ROOT, OS.TVI_LAST, id, topology.parentId (id), topology.childIndex (id));
			probeHandle = hProbe;
			probeRow = row;
			probeDepth = depthOf (id);
			LOG.trace (() -> "Tree bounds of off-window row " + row + " through a probe row");
			return item.getBounds (index, getText, getImage, fullText, fullImage, clip, hDC);
		} finally {
			probeHandle = 0;
			item.handle = 0;
			if (hProbe != 0) OS.SendMessage (tv, OS.TVM_DELETEITEM, 0, hProbe);
			tree.ignoreShrink = oldShrink;
			tree.ignoreSelect = oldSelect;
			tree.ignoreDeselect = oldDeselect;
			if (redraw) OS.DefWindowProc (tv, OS.WM_SETREDRAW, 1, 0);
		}
	}

	/* the item has no valid resident row (a native handle may be stale: handles are reused), and is not being probed */
	boolean needsProbe (TreeItem item) {
		if (item.handle != 0 && item.handle == probeHandle) return false;
		return residentHandle (item) == 0;
	}

	/*
	 * A native rectangle of the probe row moves to the row's logical place: vertically to its distance from
	 * the logical top row (the native first visible row), horizontally by the indentation of its depth (the
	 * probe is a root row).
	 */
	void translateProbe (long hItem, RECT rect) {
		if (hItem == 0) return;
		if (hItem != probeHandle) {
			translateAnchor (hItem, rect);
			return;
		}
		long height = Math.max (1, tree.getItemHeightInPixels ());
		long top = (probeRow - tree.virtualViewport.topRow ()) * height;
		int dy = (int) Math.max (Integer.MIN_VALUE / 2, Math.min (Integer.MAX_VALUE / 2, top - rect.top));
		int dx = probeDepth * (int) OS.SendMessage (tree.handle, TVM_GETINDENT, 0, 0);
		rect.top += dy;
		rect.bottom += dy;
		rect.left += dx;
		rect.right += dx;
	}

	/*
	 * A resident row outside the paint window (the caret anchor, see nativeWindow) sits natively just above or
	 * below the window, so its native y is not its logical distance from the top. Stock answers real geometry
	 * for every row of an expanded branch, also far outside the client (presentation differential, stock seed 8):
	 * the rectangle moves vertically to the row's logical place. Its indentation is already real (unlike the
	 * root-level probe row), and rows inside the paint window are already in place.
	 */
	private void translateAnchor (long hItem, RECT rect) {
		int entry = plan.entryOf (hItem);
		if (entry < 0) return;
		long row = rowOfKey (plan.keyOf (entry));
		if (row < 0) return;
		long first = residency.firstPaintRow ();
		if (row >= first && row < first + residency.paintCount ()) return;
		long height = Math.max (1, tree.getItemHeightInPixels ());
		long top = (row - tree.virtualViewport.topRow ()) * height;
		int dy = (int) Math.max (Integer.MIN_VALUE / 2, Math.min (Integer.MAX_VALUE / 2, top - rect.top));
		rect.top += dy;
		rect.bottom += dy;
	}

	/* a column other than the first: only the vertical place comes from the row, the horizontal one from the header */
	void translateProbeRow (long hItem, RECT rect) {
		if (hItem == 0 || hItem != probeHandle) return;
		int left = rect.left, right = rect.right;
		translateProbe (hItem, rect);
		rect.left = left;
		rect.right = right;
	}

	private int depthOf (int id) {
		VirtualTreeTopology topology = tree.virtualTopology;
		int depth = 0;
		for (int parent = topology.parentId (id); parent != VirtualTreeTopology.ROOT; parent = topology.parentId (parent)) depth++;
		return depth;
	}

	/**
	 * Gives every facade of a retiring native subtree a zero handle (it keeps its identity
	 * and its lanes) and forgets native handles that point into the subtree.
	 * @return whether the native caret is inside the subtree
	 */
	private boolean detachSubtree (long hRoot, long caret) {
		long tv = tree.handle;
		boolean caretInside = false;
		TVITEM tvItem = new TVITEM ();
		tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_PARAM;
		ArrayDeque<Long> stack = new ArrayDeque<> ();
		stack.push (hRoot);
		while (!stack.isEmpty ()) {
			long hItem = stack.pop ();
			if (hItem == caret) caretInside = true;
			if (hItem == tree.hAnchor) tree.hAnchor = 0;
			if (hItem == tree.hInsert) tree.hInsert = 0;
			if (hItem == tree.hSelect) tree.hSelect = 0;
			tvItem.hItem = hItem;
			if (OS.SendMessage (tv, OS.TVM_GETITEM, 0, tvItem) != 0) {
				TreeItem item = facadeOrNull ((int) tvItem.lParam);
				if (item != null) item.handle = 0;
			}
			for (long hChild = OS.SendMessage (tv, OS.TVM_GETNEXTITEM, OS.TVGN_CHILD, hItem); hChild != 0;
					hChild = OS.SendMessage (tv, OS.TVM_GETNEXTITEM, OS.TVGN_NEXT, hChild)) {
				stack.push (hChild);
			}
		}
		return caretInside;
	}

	/**
	 * Makes the logical top row the native first visible row, so that every native coordinate
	 * (item bounds, hit testing, mouse events, custom draw) equals the coordinate of a plain Tree
	 * scrolled to the same row. A non-scrolling TreeView ignores TVGN_FIRSTVISIBLE, so scrolling is
	 * enabled for the call; expanding or collapsing a row above the top resets the native top,
	 * which is why every layout re-applies it (measured by FirstVisibleProbe, see the design).
	 */
	private void resetPlaneTop () {
		long tv = tree.handle;
		long hTop = handleOfRow (tree.virtualViewport.topRow ());
		if (hTop == 0) hTop = OS.SendMessage (tv, OS.TVM_GETNEXTITEM, OS.TVGN_ROOT, 0);
		if (hTop == 0 || OS.SendMessage (tv, OS.TVM_GETNEXTITEM, OS.TVGN_FIRSTVISIBLE, 0) == hTop) return;
		final long top = hTop;
		LOG.trace (() -> "Tree native first visible row -> 0x" + Long.toHexString (top) + " (logical row " + tree.virtualViewport.topRow () + ")");
		int bits = OS.GetWindowLong (tv, OS.GWL_STYLE);
		boolean oldQuiet = quiet, oldResize = tree.ignoreResize;
		/*
		 * No paint while the native top moves: the TreeView may paint synchronously while row data is held
		 * back, which would leave blank rows that a later scroll blits. The client is invalidated once, so
		 * the next paint fills exactly the rows in view, as a plain Tree does (a rebuild has redraw off already).
		 */
		boolean redraw = OS.IsWindowVisible (tv);
		if (redraw) OS.DefWindowProc (tv, OS.WM_SETREDRAW, 0, 0);
		quiet = tree.ignoreResize = true;
		try {
			OS.SetWindowLong (tv, OS.GWL_STYLE, bits & ~OS.TVS_NOSCROLL);
			OS.SendMessage (tv, OS.TVM_SELECTITEM, OS.TVGN_FIRSTVISIBLE, hTop);
			/* with scrolling enabled the TreeView may show its own bar; drop it and recompute the frame */
			OS.SetWindowLong (tv, OS.GWL_STYLE, bits & ~(OS.WS_VSCROLL | OS.WS_HSCROLL));
			OS.SetWindowPos (tv, 0, 0, 0, 0, 0, OS.SWP_NOMOVE | OS.SWP_NOSIZE | OS.SWP_NOZORDER | OS.SWP_NOACTIVATE | OS.SWP_FRAMECHANGED);
		} finally {
			quiet = oldQuiet;
			tree.ignoreResize = oldResize;
			if (redraw) {
				OS.DefWindowProc (tv, OS.WM_SETREDRAW, 1, 0);
				OS.InvalidateRect (tv, null, true);
			}
		}
	}

	private void positionPlane () {
		long tv = tree.handle;
		RECT client = new RECT ();
		OS.GetClientRect (tree.hwndParent, client);
		int width = client.right - client.left;
		int header = headerHeight ();
		int y = header;
		int left = 0, contentWidth = width;
		if (tree.columnCount != 0) {
			SCROLLINFO info = new SCROLLINFO ();
			info.cbSize = SCROLLINFO.sizeof;
			info.fMask = OS.SIF_POS;
			OS.GetScrollInfo (tree.hwndParent, OS.SB_HORZ, info);
			left = info.nPos;
			HDITEM hdItem = new HDITEM ();
			int columns = 0;
			for (int i = 0; i < tree.columnCount; i++) {
				hdItem.mask = OS.HDI_WIDTH;
				OS.SendMessage (tree.hwndHeader, OS.HDM_GETITEM, i, hdItem);
				columns += hdItem.cxy;
			}
			contentWidth = Math.max (width, columns);
		}
		int height = Math.max (0, client.bottom - client.top - header);
		boolean oldResize = tree.ignoreResize;
		tree.ignoreResize = true;
		try {
			OS.SetWindowPos (tv, 0, -left, y, contentWidth + left, height, OS.SWP_NOACTIVATE | OS.SWP_NOZORDER);
		} finally {
			tree.ignoreResize = oldResize;
		}
		resetPlaneTop ();
		remeasurePending = true;
	}

	/** The logical vertical bar on <code>hwndParent</code>, with the native TreeView's semantics. */
	void updateBar () {
		if (tree.hwndParent == 0) return;
		VirtualTreeViewport viewport = tree.virtualViewport;
		long rows = viewport.visibleRowCount ();
		int page = Math.max (0, viewport.visibleRows ());
		SCROLLINFO info = new SCROLLINFO ();
		info.cbSize = SCROLLINFO.sizeof;
		info.fMask = OS.SIF_RANGE | OS.SIF_PAGE | OS.SIF_POS;
		info.nMin = 0;
		if (rows <= Integer.MAX_VALUE) {
			info.nMax = (int) Math.max (0, rows - 1);
			info.nPage = rows <= page ? info.nMax + 1 : page;
			info.nPos = (int) viewport.topRow ();
		} else {
			info.nMax = viewport.scrollbarMaximum () - 1;
			info.nPage = viewport.scrollbarThumb ();
			info.nPos = viewport.scrollbarSelection ();
		}
		OS.SetScrollInfo (tree.hwndParent, OS.SB_VERT, info, true);
		if (tree.columnCount == 0 && tree.scrollWidth == 0) {
			/* without columns the content plane is as wide as the body: no horizontal range */
			SCROLLINFO horizontal = new SCROLLINFO ();
			horizontal.cbSize = SCROLLINFO.sizeof;
			horizontal.fMask = OS.SIF_RANGE | OS.SIF_PAGE;
			OS.GetScrollInfo (tree.hwndParent, OS.SB_HORZ, horizontal);
			if (horizontal.nPage != horizontal.nMax + 1) {
				horizontal.nPage = horizontal.nMax + 1;
				OS.SetScrollInfo (tree.hwndParent, OS.SB_HORZ, horizontal, true);
			}
		}
		/*
		 * The native TreeView pages by its visible rows without SWT's increment fields, and so does
		 * onVScroll: getIncrement/getPageIncrement keep their stock values (or the client's).
		 */
	}

	/* ---------------------------------------------------------------- scroll input */

	long onVScroll (long wParam, long lParam) {
		ScrollBar bar = tree.verticalBar;
		if (bar != null) {
			/* SWT's own bar semantics: increments, track position and the ScrollBar Selection event */
			tree.wmScroll (bar, true, tree.hwndParent, OS.WM_VSCROLL, wParam, lParam);
			SCROLLINFO info = new SCROLLINFO ();
			info.cbSize = SCROLLINFO.sizeof;
			info.fMask = OS.SIF_POS;
			OS.GetScrollInfo (tree.hwndParent, OS.SB_VERT, info);
			int selection = info.nPos;
			VirtualTreeViewport viewport = tree.virtualViewport;
			if (viewport.visibleRowCount () <= Integer.MAX_VALUE) {
				viewport.setTopRow (selection);
			} else {
				viewport.setScrollbarSelection (selection);
			}
			layout ();
		}
		return 0;
	}

	/** Replaces the TreeView's own wheel handling (which would scroll the content plane). */
	long onWheel (long wParam) {
		ScrollBar bar = tree.verticalBar;
		Widget.MouseWheelData wheel = tree.new MouseWheelData (true, bar, wParam, tree.display.scrollRemainderBar);
		if (wheel.count != 0) {
			long top = tree.virtualViewport.topRow ();
			setTopRow (top - wheel.count);
			if (tree.virtualViewport.topRow () != top) paintNow ();
		}
		return 0;
	}

	/** Page Up, Page Down, Home and End move the caret over logical rows; other keys stay native. */
	/*
	 * A navigation key the mediator handles itself: the key is consumed, and the caret row is painted now, as the
	 * native TreeView repaints when it scrolls for a key. Keyboard cues are not touched: CueOracle (official jar,
	 * plain and virtual trees) shows that a TreeView never changes them for a key it receives; SWT's traversal
	 * code changes them for real keyboard input, the same in both modes.
	 */
	private LRESULT navigationKeyUsed () {
		paintNow ();
		return LRESULT.ZERO;
	}

	LRESULT onKeyDown (long wParam) {
		int key = (int) wParam;
		if (OS.GetKeyState (OS.VK_SHIFT) < 0 || OS.GetKeyState (OS.VK_CONTROL) < 0) return null;
		if (OS.SendMessage (tree.handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0) == 0
				&& (key == OS.VK_UP || key == OS.VK_DOWN || key == OS.VK_PRIOR || key == OS.VK_NEXT || key == OS.VK_RIGHT)) {
			/*
			 * Without a caret the native TreeView puts it on the first item of the tree and scrolls it into
			 * view (stock seeds 9 and 10); the first native row of the plane is not the first logical row.
			 * Measured on stock SWT (NoCaretKeyOracle): Up, Down, Page Up, Page Down, Home and Right go to the
			 * first row, End to the last row, Left, plus, minus and space do nothing.
			 */
			VirtualTreeViewport viewport = tree.virtualViewport;
			if (viewport.visibleRowCount () == 0) return navigationKeyUsed ();
			LOG.debug (() -> "Tree navigation key without a caret: caret on the first row");
			viewport.ensureVisible (0);
			layout ();
			long hFirst = handleOfRow (0);
			if (hFirst != 0) OS.SendMessage (tree.handle, OS.TVM_SELECTITEM, OS.TVGN_CARET, hFirst);
			return navigationKeyUsed ();
		}
		if (key == OS.VK_UP || key == OS.VK_DOWN) {
			/*
			 * Natively the caret moves to its logical neighbour, which is then scrolled into view.
			 * When the caret row is an off-window anchor its native neighbours are not its logical
			 * ones, so the move is made here; inside the window the TreeView handles the key.
			 */
			VirtualTreeViewport viewport = tree.virtualViewport;
			long caret = caretRow (), top = viewport.topRow ();
			if (caret < 0 || caret >= top && caret < top + Math.max (1, viewport.visibleRows ())) return null;
			long target = Math.max (0, Math.min (viewport.visibleRowCount () - 1, caret + (key == OS.VK_DOWN ? 1 : -1)));
			if (target == caret) return LRESULT.ZERO;
			viewport.ensureVisible (target);
			layout ();
			long hTarget = handleOfRow (target);
			if (hTarget != 0) OS.SendMessage (tree.handle, OS.TVM_SELECTITEM, OS.TVGN_CARET, hTarget);
			return navigationKeyUsed ();
		}
		if (key != OS.VK_PRIOR && key != OS.VK_NEXT && key != OS.VK_HOME && key != OS.VK_END) return null;
		VirtualTreeViewport viewport = tree.virtualViewport;
		long rows = viewport.visibleRowCount ();
		if (rows == 0) return navigationKeyUsed ();
		long top = viewport.topRow (), page = Math.max (1, viewport.visibleRows ());
		long caret = caretRow ();
		boolean hasCaret = caret >= 0;
		if (caret < 0) caret = top;
		long target = switch (key) {
			case OS.VK_HOME -> 0;
			case OS.VK_END -> rows - 1;
			/* measured on stock SWT (PageKeyOracle): the caret moves by a page less one, then is scrolled into view */
			case OS.VK_NEXT -> caret + page - 1;
			default -> caret - page + 1;
		};
		target = Math.max (0, Math.min (rows - 1, target));
		/*
		 * Natively a key whose target is the caret itself does nothing: no selection, no scroll and no paint, also
		 * when the caret row is out of view (stock seed 5 step 138: Home with the caret on the first row, scrolled away).
		 */
		if (hasCaret && target == caret) return LRESULT.ZERO;
		viewport.ensureVisible (target);
		layout ();
		long hTarget = handleOfRow (target);
		if (hTarget != 0) OS.SendMessage (tree.handle, OS.TVM_SELECTITEM, OS.TVGN_CARET, hTarget);
		return navigationKeyUsed ();
	}

	/**
	 * WM_SETFOCUS without a caret: natively the TreeView (SINGLE) or SWT's workaround (MULTI)
	 * puts the caret on the first visible row, which in the content plane would be an overscan
	 * row. The caret goes to the logical top row instead, with the same selection semantics:
	 * SINGLE selects it with notifications, MULTI moves only the focus.
	 */
	void beforeSetFocus () {
		long tv = tree.handle;
		if (OS.SendMessage (tv, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0) != 0) return;
		LOG.debug (() -> "Tree focus without a caret: caret on logical top row " + tree.virtualViewport.topRow ());
		VirtualTreeViewport viewport = tree.virtualViewport;
		if (viewport.visibleRowCount () == 0) return;
		long hTop = handleOfRow (viewport.topRow ());
		if (hTop == 0) return;
		if ((tree.style & SWT.SINGLE) != 0) {
			OS.SendMessage (tv, OS.TVM_SELECTITEM, OS.TVGN_CARET, hTop);
		} else {
			TVITEM tvItem = new TVITEM ();
			tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_STATE;
			tvItem.hItem = hTop;
			OS.SendMessage (tv, OS.TVM_GETITEM, 0, tvItem);
			tree.hSelect = hTop;
			tree.ignoreDeselect = tree.ignoreSelect = tree.lockSelection = true;
			try {
				OS.SendMessage (tv, OS.TVM_SELECTITEM, OS.TVGN_CARET, hTop);
			} finally {
				tree.ignoreDeselect = tree.ignoreSelect = tree.lockSelection = false;
				tree.hSelect = 0;
			}
			if ((tvItem.state & OS.TVIS_SELECTED) == 0) OS.SendMessage (tv, OS.TVM_SETITEM, 0, tvItem);
		}
		int entry = plan.entryOf (hTop);
		if (entry >= 0) {
			long key = plan.keyOf (entry);
			focusId = idOf (facade ((int) (key >> 32), (int) key));
		}
	}

	/** After the native TreeView handled a message: keep the caret row visible. */
	/* the native caret before a key or a click: only a moved caret is kept in view afterwards */
	private long caretBefore;

	void beforeNative (int msg) {
		if (msg == OS.WM_KEYDOWN || msg == OS.WM_CHAR || msg == OS.WM_LBUTTONDOWN) {
			caretBefore = OS.SendMessage (tree.handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
		}
	}

	void afterNative (int msg) {
		if (busy) return;
		if (msg != OS.WM_KEYDOWN && msg != OS.WM_CHAR && msg != OS.WM_LBUTTONDOWN) return;
		/* as the native TreeView: a key that does not move the caret (for example + on a row without children) does not scroll */
		if (OS.SendMessage (tree.handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0) == caretBefore) return;
		long row = caretRow ();
		if (row < 0) return;
		VirtualTreeViewport viewport = tree.virtualViewport;
		long top = viewport.topRow ();
		if (row < top || row >= top + Math.max (1, viewport.visibleRows ())) {
			viewport.ensureVisible (row);
			layout ();
			paintNow ();
		}
	}

	/*
	 * The native TreeView repaints synchronously when it scrolls for a caret move (TVM_SELECTITEM, navigation
	 * keys, clicks) or for the wheel, so the rows that come into view ask for their data (SetData) inside that
	 * call, not at the next paint; TVM_ENSUREVISIBLE (showItem) and TVGN_FIRSTVISIBLE (setTopItem) do not.
	 * Measured on stock SWT (SetDataTimingOracle). A logical scroll repaints the plane at the same points.
	 */
	private void paintNow () {
		OS.UpdateWindow (tree.handle);
	}

	private long caretRow () {
		long caret = OS.SendMessage (tree.handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
		if (caret == 0) return -1;
		int entry = plan.entryOf (caret);
		return entry < 0 ? -1 : rowOfKey (plan.keyOf (entry));
	}

	private long handleOfRow (long row) {
		if (row < 0 || row >= tree.virtualViewport.visibleRowCount ()) return 0;
		VirtualTreeVisibleProjection.Row coordinate = tree.virtualProjection.rowAt (row);
		return plan.handleOf (coordinate.parentId (), coordinate.childIndex ());
	}

	void setTopRow (long row) {
		tree.virtualViewport.setTopRow (Math.max (0, row));
		layout ();
	}

	/* ---------------------------------------------------------------- facades and identity */

	int idOf (TreeItem item) {
		if (item == null || item.isDisposed ()) return -1;
		int id = item.virtualId;
		return id >= 0 && id < tree.items.length && tree.items [id] == item && tree.virtualTopology.contains (id) ? id : -1;
	}

	/**
	 * The facade's native row, or 0 when it is not resident. A handle counts only while that
	 * native row's lParam names this facade: the TreeView reuses handles of deleted rows.
	 */
	long residentHandle (TreeItem item) {
		if (item == null || item.handle == 0) return 0;
		TVITEM tvItem = new TVITEM ();
		tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_PARAM;
		tvItem.hItem = item.handle;
		if (plan.entryOf (item.handle) < 0 || OS.SendMessage (tree.handle, OS.TVM_GETITEM, 0, tvItem) == 0
				|| tvItem.lParam != item.virtualId) {
			item.handle = 0;
			return 0;
		}
		return item.handle;
	}

	private TreeItem facadeOrNull (int id) {
		TreeItem [] items = tree.items;
		return id >= 0 && items != null && id < items.length ? items [id] : null;
	}

	/** Registers a facade created with {@link #DETACHED} under a new id, without a native row. */
	void adopt (TreeItem item) {
		TreeItem [] items = tree.items;
		int id = tree.lastID < items.length ? tree.lastID : 0;
		while (id < items.length && items [id] != null) id++;
		if (id == items.length) {
			tree.itemsGrowArray (Math.max (4, items.length * 3 / 2));
		}
		tree.lastID = id + 1;
		tree.items [id] = item;
		item.virtualId = id;
		item.handle = 0;
		if (id < selectedAt.length) selectedAt [id] = 0;
	}

	/**
	 * TreeItem's insertion helpers (<code>findPrevious</code>, <code>findLast</code>) remember the
	 * logical target here and pass this sentinel as the native insert-after handle.
	 */
	static final long LOGICAL_INSERT = Long.MIN_VALUE + 1;
	private int pendingParentId = Integer.MIN_VALUE, pendingIndex = -1;

	/** Remembers where an application-created item goes; <code>index == -1</code> appends. */
	long insertAt (TreeItem parent, int index) {
		int parentId = parentIdOf (parent);
		int count = knownCount (parentId);
		if (index == -1) index = count;
		if (index < 0 || index > count) tree.error (SWT.ERROR_INVALID_RANGE);
		pendingParentId = parentId;
		pendingIndex = index;
		return LOGICAL_INSERT;
	}

	/* An application-created parent has as many children as were inserted (none yet): no SetData. */
	private int knownCount (int parentId) {
		VirtualTreeTopology topology = tree.virtualTopology;
		if (!topology.childCountKnown (parentId)) topology.setChildCount (parentId, 0);
		return topology.childCount (parentId);
	}

	/** <code>createItem</code> of an application-created item: a new logical row, native only when resident. */
	void insert (TreeItem item, long hParent, long hInsertAfter) {
		int parentId, index;
		if (hInsertAfter == LOGICAL_INSERT && pendingParentId != Integer.MIN_VALUE) {
			parentId = pendingParentId;
			index = pendingIndex;
		} else {
			/* TreeItem (Tree, int): appended to the roots (TVI_ROOT, TVI_LAST) */
			parentId = VirtualTreeTopology.ROOT;
			index = hInsertAfter == OS.TVI_FIRST ? 0 : knownCount (parentId);
		}
		pendingParentId = Integer.MIN_VALUE;
		adopt (item);
		VirtualTreeTopology topology = tree.virtualTopology;
		knownCount (parentId);
		captureResidentState ();
		boolean shifted = shiftsResidentRows (parentId, index);
		topology.insertCoordinate (parentId, index, item.virtualId);
		shiftPattern (parentId, index, 1);
		if (shifted) shiftResidentRows (parentId, index, 1);
		TreeItem parent = parentId == VirtualTreeTopology.ROOT ? null : facadeOrNull (parentId);
		long hParentRow = residentHandle (parent);
		if (hParentRow != 0) {
			TVITEM tvItem = new TVITEM ();
			tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_CHILDREN;
			tvItem.hItem = hParentRow;
			tvItem.cChildren = 1;
			OS.SendMessage (tree.handle, OS.TVM_SETITEM, 0, tvItem);
		}
		tree.virtualViewport.refreshLogicalRange ();
		layout ();
		/* stock: TVM_GETCOUNT == 1 after createItem, i.e. the first root of an empty tree (EmptyOracle) */
		if (parentId == VirtualTreeTopology.ROOT && topology.childCount (VirtualTreeTopology.ROOT) == 1) {
			Event event = new Event ();
			event.detail = 0;
			tree.sendEvent (SWT.EmptinessChanged, event);
		}
	}

	/*
	 * Gives an existing logical row a materialized slot. Under a selectAll pattern the row was selected
	 * while cold, so its new slot starts selected (a newly inserted row takes another path and does not).
	 */
	private void bindCoordinate (int virtualId, int parentId, int index) {
		VirtualTreeTopology topology = tree.virtualTopology;
		/* a facade bound again to its own row keeps its lanes: an explicit deselection under the pattern stays, and so does its recency */
		if (topology.contains (virtualId) && topology.parentId (virtualId) == parentId && topology.childIndex (virtualId) == index) return;
		topology.bind (virtualId, parentId, index);
		if (patternSelects (parentId, index)) {
			tree.virtualTopology.flag (virtualId, VirtualItemState.SELECTED, true);
			stampSelected (virtualId, epochOf (parentId, index));
		}
	}

	/** Binds a facade that adopted a resident native row to that row's logical coordinate. */
	void bind (TreeItem item) {
		/* while the window moves, native handles may be reused before the plan is committed */
		if (rebuilding) return;
		int entry = plan.entryOf (item.handle);
		if (entry < 0 || item.virtualId < 0) return;
		long key = plan.keyOf (entry);
		bindCoordinate (item.virtualId, (int) (key >> 32), (int) key);
	}

	/** The facade of a logical row, created on demand: from its resident native row, or without one. Never null. */
	TreeItem facade (int parentId, int index) {
		VirtualTreeTopology topology = tree.virtualTopology;
		TreeItem item = facadeOrNull (topology.materializedChildId (parentId, index));
		if (item != null) {
			rowOf (item);
			return item;
		}
		long hItem = plan.handleOf (parentId, index);
		if (hItem != 0) {
			/* the row's own facade first (its lParam), so a live facade is never orphaned by a second one */
			item = tree._getItem (hItem);
			if (item == null) item = tree._getItem (hItem, -1);
			if (item != null && !item.isDisposed () && item.virtualId >= 0) {
				if (idOf (item) < 0) bindCoordinate (item.virtualId, parentId, index);
				if (idOf (item) >= 0) return item;
			}
		}
		item = new TreeItem (tree, SWT.NONE, DETACHED, 0, 0);
		bindCoordinate (item.virtualId, parentId, index);
		if (hItem != 0) attachRow (item, hItem);
		return item;
	}

	private int parentIdOf (TreeItem parent) {
		if (parent == null) return VirtualTreeTopology.ROOT;
		int id = idOf (parent);
		if (id < 0) tree.error (SWT.ERROR_INVALID_ARGUMENT);
		return id;
	}

	/* ---------------------------------------------------------------- mediated API */

	int getItemCount (TreeItem parent) {
		int parentId = parentIdOf (parent);
		VirtualTreeTopology topology = tree.virtualTopology;
		if (parent != null && !topology.childCountKnown (parentId)) tree.checkData (parent, false);
		return topology.childCountKnown (parentId) ? topology.childCount (parentId) : 0;
	}

	TreeItem getItem (TreeItem parent, int index) {
		int count = getItemCount (parent);
		if (index < 0 || index >= count) tree.error (SWT.ERROR_INVALID_RANGE);
		TreeItem item = facade (parentIdOf (parent), index);
		tree.pinVirtualFacade (item);
		return item;
	}

	/* ---------------------------------------------------------------- M3RowMath seam (enhanced API) */

	/** Visible row of the facade from the projection (rank), -1 under a collapsed ancestor; UNKNOWN for a foreign facade. */
	long visibleRowOf (TreeItem item) {
		int id = idOf (item);
		return id < 0 ? M3RowMath.UNKNOWN : tree.virtualProjection.visibleIndexOf (id);
	}

	/** The facade at a visible row (select), made or reused and pinned like the result of getItem. */
	TreeItem visibleItem (long row) {
		VirtualTreeVisibleProjection.Row coordinate = tree.virtualProjection.rowAt (row);
		TreeItem item = facade (coordinate.parentId (), coordinate.childIndex ());
		tree.pinVirtualFacade (item);
		return item;
	}

	/** Rows of the facade's visible subtree: the facade plus the topology's visible extra rows below it. */
	long subtreeRows (TreeItem item) {
		int id = idOf (item);
		return id < 0 ? M3RowMath.UNKNOWN : Math.addExact (1L, tree.virtualTopology.visibleExtraRows (id));
	}

	TreeItem [] getItems (TreeItem parent) {
		int count = getItemCount (parent);
		int parentId = parentIdOf (parent);
		TreeItem [] result = new TreeItem [count];
		for (int i = 0; i < count; i++) {
			result [i] = facade (parentId, i);
			tree.pinVirtualFacade (result [i]);
		}
		return result;
	}

	int indexOf (TreeItem parent, TreeItem item) {
		int id = idOf (item);
		if (id < 0) return -1;
		VirtualTreeTopology topology = tree.virtualTopology;
		return topology.parentId (id) == parentIdOf (parent) ? topology.childIndex (id) : -1;
	}

	TreeItem getParentItem (TreeItem item) {
		int id = idOf (item);
		if (id < 0) return null;
		int parentId = tree.virtualTopology.parentId (id);
		TreeItem parent = parentId == VirtualTreeTopology.ROOT ? null : facadeOrNull (parentId);
		if (parent != null) tree.pinVirtualFacade (parent);
		return parent;
	}

	void setItemCount (TreeItem parent, int count) {
		final int requested = count;
		LOG.debug (() -> "Tree setItemCount " + requested + (parent == null ? " (roots)" : " (children of id " + idOf (parent) + ")"));
		count = Math.max (0, count);
		int parentId = parentIdOf (parent);
		VirtualTreeTopology topology = tree.virtualTopology;
		int rootsBefore = parentId == VirtualTreeTopology.ROOT ? getItemCount (null) : 1;
		captureResidentState ();
		boolean caretRemoved = removesCaret (parentId, count);
		if (caretRemoved) deselectOnShrink (parentId, count);
		/* the row that takes the caret keeps the selection state it has now (deselectOnCaretMoves) */
		long newCaret = !caretRemoved ? NO_ROW : count > 0 ? VirtualTreeResidentPlan.key (parentId, count - 1) : parentId == VirtualTreeTopology.ROOT ? NO_ROW : keyOf (parentId);
		boolean newCaretSelected = newCaret != NO_ROW && ((tree.style & SWT.SINGLE) != 0 || rowSelected (newCaret));
		for (int id = topology.firstMaterializedChildId (parentId); id >= 0;) {
			int next = topology.nextMaterializedSiblingId (id);
			if (topology.childIndex (id) >= count) releaseFacades (id);
			id = next;
		}
		topology.setChildCount (parentId, count);
		clampPattern (parentId, count);
		long hParent = residentHandle (parent);
		if (hParent != 0) {
			TVITEM tvItem = new TVITEM ();
			tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_CHILDREN;
			tvItem.hItem = hParent;
			tvItem.cChildren = count > 0 ? 1 : 0;
			OS.SendMessage (tree.handle, OS.TVM_SETITEM, 0, tvItem);
		}
		boolean emptied = parentId == VirtualTreeTopology.ROOT && count == 0 && rootsBefore > 0;
		tree.virtualViewport.refreshLogicalRange ();
		layout ();
		if (caretRemoved) caretAfterShrink (parentId, count, newCaretSelected);
		if (emptied) becameEmpty ();
	}

	/* whether the caret row is a child of parentId at an index >= count, or inside such a child's subtree */
	private boolean removesCaret (int parentId, int count) {
		VirtualTreeTopology topology = tree.virtualTopology;
		for (int id = focusId; id >= 0 && topology.contains (id); id = topology.parentId (id)) {
			if (topology.parentId (id) == parentId) return topology.childIndex (id) >= count;
		}
		return false;
	}

	/*
	 * The native TreeView deletes the removed rows in order; deleting the caret row moves the caret to the next
	 * removed row, and the last one to the new last child (or to the parent when no child is left), scrolled into
	 * view with TVM_ENSUREVISIBLE semantics, without events. The selection those moves leave is applied before the
	 * rows go (deselectOnShrink). In SINGLE the caret is the selection, so the new caret is selected silently, as when
	 * the caret row is disposed (CaretDisposeOracle).
	 */
	private void caretAfterShrink (int parentId, int count, boolean selected) {
		TreeItem target = count > 0 ? facade (parentId, count - 1) : parentId == VirtualTreeTopology.ROOT ? null : facadeOrNull (parentId);
		LOG.debug (() -> "Tree setItemCount removed the caret row: caret -> " + (target == null ? "none" : "id " + idOf (target)));
		if (target == null) {
			moveFocus (-1);
			return;
		}
		moveCaretAfterRemoval (target);
		layout ();
		if ((tree.style & SWT.SINGLE) != 0) selectSilently (target);
		restoreFocus ();
		keepSelection (target, selected);
	}

	/*
	 * A new caret after a deletion keeps the selection state it had before the deletion (deselectOnCaretMoves; SINGLE:
	 * selected). The mediator's own native caret move selects the row natively for a moment, and a window rebuild
	 * inside that move would copy it into the lanes, so the state decided before the deletion is applied to both.
	 */
	private void keepSelection (TreeItem item, boolean selected) {
		int id = idOf (item);
		if (id < 0) return;
		if (selected) markSelected (id);
		else tree.virtualTopology.flag (id, VirtualItemState.SELECTED, false);
		long hItem = residentHandle (item);
		if (hItem == 0) return;
		int state = (int) OS.SendMessage (tree.handle, OS.TVM_GETITEMSTATE, hItem, OS.TVIS_SELECTED);
		if (((state & OS.TVIS_SELECTED) != 0) != selected) setNativeSelected (hItem, selected);
	}

	/* whether a row is selected in the lanes: its facade's bit, or the selectAll pattern for a cold row */
	private boolean rowSelected (long key) {
		int parentId = (int) (key >> 32), index = (int) key;
		int id = tree.virtualTopology.materializedChildId (parentId, index);
		return id >= 0 ? tree.virtualTopology.flag (id, VirtualItemState.SELECTED) : patternSelects (parentId, index);
	}

	/* ---------------------------------------------------------------- selection left by native caret moves on deletion */

	private static final long NO_ROW = -1;

	/* which rows a deletion has removed: by a given caret move (its old caret row aside), and once it is done */
	private interface Deletion {
		boolean goneAt (int move, long key);

		boolean gone (long key);
	}

	/*
	 * Measured on stock SWT (ShrinkSelectionOracle and CaretMoveSelectionOracle, 60 cases, MULTI): when the caret row
	 * is deleted (setItemCount, dispose) the TreeView moves the caret natively, and on every such move it also deselects
	 * the most recently selected row other than the old caret, unless that row is the new caret. SWT's selection lock
	 * then restores only the old caret row and keeps a new caret row that was not selected unselected. Recency is the
	 * order in which rows became selected; selectAll selects in preorder and leaves rows that were selected in place. A
	 * caret inside a deleted subtree first moves to the subtree's root, then on as for that row.
	 *
	 * oldKeys and newKeys hold each move's old and new caret row (NO_ROW: no new caret, nothing is selected). The
	 * deselected rows that survive the deletion are deselected in the lanes and natively.
	 */
	private void deselectOnCaretMoves (long [] oldKeys, long [] newKeys, Deletion deletion) {
		if ((tree.style & SWT.SINGLE) != 0) return;
		RecencyCursor recent = new RecencyCursor ();
		ArrayList<Long> deselected = new ArrayList<> ();
		for (int move = 0; move < oldKeys.length; move++) {
			if (newKeys [move] == NO_ROW) continue;
			long row;
			while ((row = recent.peek ()) != NO_ROW && (row == oldKeys [move] || deletion.goneAt (move, row))) recent.advance ();
			if (row == NO_ROW) break;
			if (row == newKeys [move]) continue;
			recent.advance ();
			if (!deletion.gone (row)) deselected.add (row);
		}
		if (!deselected.isEmpty ()) LOG.debug (() -> "Tree caret moves on deletion deselect " + deselected.size () + " rows");
		for (long row : deselected) deselectRow (row);
	}

	/* the native caret moves of a shrink past the caret: from the caret along the removed children, then to the new last child */
	private void deselectOnShrink (int parentId, int count) {
		VirtualTreeTopology topology = tree.virtualTopology;
		int oldCount = topology.childCount (parentId);
		int child = focusId;
		while (topology.parentId (child) != parentId) child = topology.parentId (child);
		int first = topology.childIndex (child);
		boolean inside = child != focusId;
		int moves = oldCount - first + (inside ? 1 : 0), move = 0;
		long [] oldKeys = new long [moves], newKeys = new long [moves];
		int [] removedBelow = new int [moves];
		boolean [] subtreeGone = new boolean [moves];
		if (inside) {
			oldKeys [0] = keyOf (focusId);
			newKeys [0] = VirtualTreeResidentPlan.key (parentId, first);
			removedBelow [0] = first;
			move = 1;
		}
		long last = count > 0 ? VirtualTreeResidentPlan.key (parentId, count - 1) : parentId == VirtualTreeTopology.ROOT ? NO_ROW : keyOf (parentId);
		for (int index = first; index < oldCount; index++, move++) {
			oldKeys [move] = VirtualTreeResidentPlan.key (parentId, index);
			newKeys [move] = index + 1 < oldCount ? VirtualTreeResidentPlan.key (parentId, index + 1) : last;
			removedBelow [move] = index;
			subtreeGone [move] = true;
		}
		deselectOnCaretMoves (oldKeys, newKeys, new Deletion () {
			@Override
			public boolean goneAt (int at, long key) {
				long branch = branchUnder (parentId, key);
				int index = (int) (branch >> 1);
				return branch >= 0 && index >= count && (index < removedBelow [at] || subtreeGone [at] && index == removedBelow [at] && (branch & 1) != 0);
			}

			@Override
			public boolean gone (long key) {
				long branch = branchUnder (parentId, key);
				return branch >= 0 && (branch >> 1) >= count;
			}
		});
	}

	/* the native caret moves of disposing the row holding the caret (or an ancestor of it): to the row, then to the next caret */
	private void deselectOnDispose (int parentId, int index, int id, TreeItem nextCaret) {
		boolean inside = focusId != id;
		long removed = VirtualTreeResidentPlan.key (parentId, index);
		long [] oldKeys = inside ? new long [] {keyOf (focusId), removed} : new long [] {removed};
		long next = nextCaret == null || idOf (nextCaret) < 0 ? NO_ROW : keyOf (idOf (nextCaret));
		long [] newKeys = inside ? new long [] {removed, next} : new long [] {next};
		deselectOnCaretMoves (oldKeys, newKeys, new Deletion () {
			@Override
			public boolean goneAt (int at, long key) {
				return at == oldKeys.length - 1 && branchUnder (parentId, key) == (((long) index << 1) | 1);
			}

			@Override
			public boolean gone (long key) {
				long branch = branchUnder (parentId, key);
				return branch >= 0 && (branch >> 1) == index;
			}
		});
	}

	private long keyOf (int id) {
		VirtualTreeTopology topology = tree.virtualTopology;
		return VirtualTreeResidentPlan.key (topology.parentId (id), topology.childIndex (id));
	}

	/*
	 * The child index, under parentId, of the row's ancestor-or-self there, shifted left by one; bit 0 is set when the
	 * row is a strict descendant of that child. -1 when the row is not below parentId.
	 */
	private long branchUnder (int parentId, long key) {
		int rowParent = (int) (key >> 32), index = (int) key;
		if (rowParent == parentId) return (long) index << 1;
		VirtualTreeTopology topology = tree.virtualTopology;
		for (int id = rowParent; id != VirtualTreeTopology.ROOT && topology.contains (id); id = topology.parentId (id)) {
			if (topology.parentId (id) == parentId) return ((long) topology.childIndex (id) << 1) | 1;
		}
		return -1;
	}

	/* deselects one row by coordinate; a cold row leaves its pattern (the trailing one by a shorter limit) */
	private void deselectRow (long key) {
		int parentId = (int) (key >> 32), index = (int) key;
		VirtualTreeTopology topology = tree.virtualTopology;
		int id = topology.materializedChildId (parentId, index);
		if (id < 0) {
			long [] segments = patterns.get (parentId);
			if (segments != null && index == segments [segments.length - 2] - 1) {
				for (int s = 0; s < segments.length; s += 2) segments [s] = Math.min (segments [s], index);
				return;
			}
			id = idOf (facade (parentId, index));
			if (id < 0) return;
		}
		topology.flag (id, VirtualItemState.SELECTED, false);
		long hItem = residentHandle (facadeOrNull (id));
		if (hItem != 0) setNativeSelected (hItem, false);
	}

	/*
	 * The selected rows, most recently selected first: rows with their own selection stamp, and per selectAll epoch the
	 * rows it selected in reverse preorder (PatternWalk), merged by clock value. Lazy: a deletion reads only as many
	 * rows as it has caret moves (plus the rows it skips).
	 */
	private final class RecencyCursor {
		private final long [] stamps, keys;
		private int explicit;
		private final Iterator<Long> epochs = patternEpochs.descendingIterator ();
		private long epoch;
		private PatternWalk walk;
		private long head = NO_ROW;
		private boolean headValid;

		RecencyCursor () {
			VirtualTreeTopology topology = tree.virtualTopology;
			ArrayList<long []> rows = new ArrayList<> ();
			visitMaterialized (id -> {
				long stamp = selectedAt (id);
				if (topology.flag (id, VirtualItemState.SELECTED) && !patternEpochs.contains (stamp)) rows.add (new long [] {stamp, keyOf (id)});
			}, false);
			rows.sort ((a, b) -> Long.compare (b [0], a [0]));
			stamps = new long [rows.size ()];
			keys = new long [rows.size ()];
			for (int i = 0; i < stamps.length; i++) {
				stamps [i] = rows.get (i) [0];
				keys [i] = rows.get (i) [1];
			}
			nextEpoch ();
		}

		private void nextEpoch () {
			epoch = epochs.hasNext () ? epochs.next () : 0;
			walk = epoch == 0 ? null : new PatternWalk (epoch);
		}

		long peek () {
			if (!headValid) {
				head = pull ();
				headValid = true;
			}
			return head;
		}

		void advance () {
			peek ();
			headValid = false;
		}

		private long pull () {
			while (true) {
				if (explicit < stamps.length && (walk == null || stamps [explicit] > epoch)) return keys [explicit++];
				if (walk == null) return NO_ROW;
				long key = walk.next ();
				if (key != NO_ROW) return key;
				nextEpoch ();
			}
		}
	}

	/* the rows one selectAll epoch selected and that are still selected, in reverse preorder (most recent first) */
	private final class PatternWalk {
		private final long epoch;
		/* {parentId, next child index, the child's subtree was walked} */
		private final ArrayDeque<int []> stack = new ArrayDeque<> ();

		PatternWalk (long epoch) {
			this.epoch = epoch;
			stack.push (new int [] {VirtualTreeTopology.ROOT, patternLimit (VirtualTreeTopology.ROOT) - 1, 0});
		}

		long next () {
			VirtualTreeTopology topology = tree.virtualTopology;
			while (!stack.isEmpty ()) {
				int [] frame = stack.peek ();
				if (frame [1] < 0) {
					stack.pop ();
					continue;
				}
				int parentId = frame [0], index = frame [1];
				int id = topology.materializedChildId (parentId, index);
				if (frame [2] == 0 && id >= 0 && patterns.containsKey (id)) {
					frame [2] = 1;
					stack.push (new int [] {id, patternLimit (id) - 1, 0});
					continue;
				}
				frame [1]--;
				frame [2] = 0;
				boolean member = id >= 0 ? topology.flag (id, VirtualItemState.SELECTED) && selectedAt (id) == epoch : epochOf (parentId, index) == epoch;
				if (member) return VirtualTreeResidentPlan.key (parentId, index);
			}
			return NO_ROW;
		}
	}

	/** Releases the facades of a logical subtree whose rows are being removed (descendants first). */
	private void releaseFacades (int rootId) {
		VirtualTreeTopology topology = tree.virtualTopology;
		ArrayDeque<Integer> stack = new ArrayDeque<> ();
		ArrayList<Integer> order = new ArrayList<> ();
		stack.push (rootId);
		while (!stack.isEmpty ()) {
			int id = stack.pop ();
			order.add (id);
			for (int child = topology.firstMaterializedChildId (id); child >= 0; child = topology.nextMaterializedSiblingId (child)) {
				stack.push (child);
			}
		}
		for (int i = order.size () - 1; i >= 0; i--) {
			int id = order.get (i);
			TreeItem item = facadeOrNull (id);
			if (item != null && !item.isDisposed ()) item.release (false);
			if (id < tree.items.length) tree.items [id] = null;
			if (id < tree.lastID) tree.lastID = id;
			if (focusId == id) moveFocus (-1);
			patterns.remove (id);
		}
	}

	/** Mediated <code>clear (index, all)</code>: only a materialized, cached row has data to clear. */
	void clear (TreeItem parent, int index, boolean all) {
		if (index < 0 || index >= getItemCount (parent)) tree.error (SWT.ERROR_INVALID_RANGE);
		int id = tree.virtualTopology.materializedChildId (parentIdOf (parent), index);
		TreeItem item = facadeOrNull (id);
		if (item == null) return;
		clearItem (item);
		if (all) clearChildren (id, true);
	}

	/** Mediated <code>clearAll (all)</code>; with <code>all</code> on the Tree every facade but the one in SetData is cleared. */
	void clearAll (TreeItem parent, boolean all) {
		if (parent == null && all) {
			boolean redraw = false;
			for (TreeItem item : tree.items) {
				if (item != null && item != tree.currentItem) {
					item.clear ();
					redraw = true;
				}
			}
			if (redraw) OS.InvalidateRect (tree.handle, null, true);
			return;
		}
		clearChildren (parentIdOf (parent), all);
	}

	private void clearChildren (int parentId, boolean all) {
		VirtualTreeTopology topology = tree.virtualTopology;
		for (int id = topology.firstMaterializedChildId (parentId); id >= 0; id = topology.nextMaterializedSiblingId (id)) {
			TreeItem item = facadeOrNull (id);
			if (item != null) clearItem (item);
			if (all) clearChildren (id, true);
		}
	}

	/* the row is attached first, so TreeItem.clear resets the native check-box image through a valid handle */
	private void clearItem (TreeItem item) {
		if (!item.isCachedState ()) return;
		long hItem = rowOf (item);
		item.clear ();
		if (hItem != 0) item.redraw ();
	}

	TreeItem getTopItem () {
		VirtualTreeViewport viewport = tree.virtualViewport;
		if (viewport.visibleRowCount () == 0) return null;
		VirtualTreeVisibleProjection.Row row = tree.virtualProjection.rowAt (viewport.topRow ());
		TreeItem item = facade (row.parentId (), row.childIndex ());
		tree.pinVirtualFacade (item);
		return item;
	}

	void setTopItem (TreeItem item) {
		int id = idOf (item);
		if (id < 0) return;
		LOG.debug (() -> "Tree setTopItem id " + id);
		if (!ensureAncestorsExpanded (id, false)) return;
		long row = tree.virtualProjection.visibleIndexOf (id);
		if (row >= 0) setTopRow (row);
	}

	/**
	 * Mediated <code>showItem</code> with the native semantics: nothing happens when the row is
	 * fully visible; otherwise collapsed ancestors expand, nearest first, each scrolling its
	 * children into view (see {@link #scrollChildrenIntoView}), and then the row is scrolled
	 * just into view, to the top edge from above or the bottom edge from below.
	 */
	void showItem (TreeItem item) {
		int id = idOf (item);
		if (id < 0) return;
		LOG.debug (() -> "Tree showItem id " + id + " from top row " + tree.virtualViewport.topRow ());
		VirtualTreeViewport viewport = tree.virtualViewport;
		VirtualTreeTopology topology = tree.virtualTopology;
		int page = Math.max (0, viewport.visibleRows ());
		long row = tree.virtualProjection.visibleIndexOf (id);
		long top = viewport.topRow ();
		if (row >= 0 && row >= top && row < top + page) return;
		boolean wasHidden = row < 0;
		if (!ensureAncestorsExpanded (id, true)) return;
		row = tree.virtualProjection.visibleIndexOf (id);
		if (row < 0) return;
		top = viewport.topRow ();
		if (page == 0 || row < top) {
			viewport.setTopRow (row);
		} else if (row >= top + page) {
			viewport.setTopRow (row - page + 1);
		}
		layout ();
		if (wasHidden && measuresShownRow ()) tree.checkData (item, false);
	}

	/*
	 * Stock showItem reads the text bounds of the row before scrolling and, in a tree with a parent window,
	 * again after it to adjust the horizontal scroll. A row inside a collapsed branch has no bounds the first
	 * time, so the second read is its first measure: the TreeView asks for its data inside the call
	 * (SetDataTimingOracle, columns variant). Stock creates that parent window for the first column, a shown
	 * header or a MeasureItem listener; the viewport always has one, so the stock condition is checked here.
	 */
	private boolean measuresShownRow () {
		return tree.columnCount > 0 || tree.getHeaderVisible () || tree.hooks (SWT.MeasureItem);
	}

	/**
	 * Native TVM_ENSUREVISIBLE expansion of collapsed ancestors: nearest first, and, as the
	 * TreeView's TVN_ITEMEXPANDING does, the Expand event is sent before each expansion.
	 * @return false when a listener disposed the tree or the item
	 */
	private boolean ensureAncestorsExpanded (int id, boolean scroll) {
		VirtualTreeTopology topology = tree.virtualTopology;
		TreeItem item = facadeOrNull (id);
		for (int parent = topology.parentId (id); parent != VirtualTreeTopology.ROOT; parent = topology.parentId (parent)) {
			if (topology.flag (parent, VirtualItemState.EXPANDED)) continue;
			TreeItem parentItem = facadeOrNull (parent);
			if (parentItem != null) {
				Event event = new Event ();
				event.item = parentItem;
				tree.sendEvent (SWT.Expand, event);
				if (tree.isDisposed () || item == null || item.isDisposed () || idOf (item) < 0) return false;
			}
			long parentRow = tree.virtualProjection.visibleIndexOf (parent);
			long topBefore = tree.virtualViewport.topRow ();
			topology.flag (parent, VirtualItemState.EXPANDED, true);
			tree.virtualViewport.refreshLogicalRange ();
			if (scroll && parentRow >= 0) {
				/*
				 * Measured on the native control (EnsureVisibleOracle): when the parent is above the
				 * top and the old top row lies beyond its expanded subtree, the parent first becomes
				 * the top row; otherwise the top row index stays. Then its children scroll into view.
				 */
				long lastChild = topology.childCountKnown (parent) && topology.childCount (parent) > 0
						? tree.virtualProjection.visibleIndexOfCoordinate (parent, topology.childCount (parent) - 1) : parentRow;
				if (parentRow < topBefore && topBefore > lastChild + 1) tree.virtualViewport.setTopRow (parentRow);
				scrollChildrenIntoView (parent);
			}
		}
		return true;
	}

	/**
	 * The native TreeView's scroll after an item expands: as many of its children as possible
	 * become visible, but the item itself never scrolls above the top row.
	 */
	private void scrollChildrenIntoView (int id) {
		VirtualTreeTopology topology = tree.virtualTopology;
		VirtualTreeVisibleProjection projection = tree.virtualProjection;
		VirtualTreeViewport viewport = tree.virtualViewport;
		long row = projection.visibleIndexOf (id);
		if (row < 0 || !topology.childCountKnown (id) || topology.childCount (id) == 0) return;
		/*
		 * Measured on the native control (EnsureVisibleOracle, 15 cases): when the last child is
		 * below the view, it is scrolled to the bottom edge; an item at or below the top row is
		 * never scrolled above it. An item above the top still scrolls its last child into view.
		 */
		long lastChild = projection.visibleIndexOfCoordinate (id, topology.childCount (id) - 1);
		long top = viewport.topRow ();
		int rows = Math.max (1, viewport.visibleRows ());
		if (lastChild >= top + rows) {
			long newTop = lastChild - rows + 1;
			if (row >= top) newTop = Math.min (newTop, row);
			viewport.setTopRow (newTop);
		}
	}

	/* The logical coordinate of the top row, to keep the same item at the top across a shape change. */
	private int [] topCoordinate () {
		VirtualTreeViewport viewport = tree.virtualViewport;
		if (viewport.visibleRowCount () == 0) return null;
		VirtualTreeVisibleProjection.Row row = tree.virtualProjection.rowAt (viewport.topRow ());
		return new int [] {row.parentId (), row.childIndex ()};
	}

	/* As SWT's setExpanded does natively: the top item stays the top item, or its nearest visible ancestor. */
	private void restoreTop (int [] coordinate) {
		if (coordinate == null) return;
		VirtualTreeTopology topology = tree.virtualTopology;
		VirtualTreeVisibleProjection projection = tree.virtualProjection;
		int parentId = coordinate [0], index = coordinate [1];
		while (parentId != VirtualTreeTopology.ROOT && projection.visibleIndexOf (parentId) < 0
				|| parentId != VirtualTreeTopology.ROOT && !topology.flag (parentId, VirtualItemState.EXPANDED)) {
			index = topology.childIndex (parentId);
			parentId = topology.parentId (parentId);
		}
		tree.virtualViewport.setTopRow (projection.visibleIndexOfCoordinate (parentId, index));
	}

	/** Mediated <code>showSelection</code>: the first selected row in tree order, as the native walk from the root finds it. */
	void showSelection () {
		TreeItem [] selection = getSelection ();
		if (selection.length > 0) showItem (selection [0]);
	}

	/** Mediated <code>TreeItem.setExpanded</code>: expansion is a topology lane; the window follows. */
	void setExpanded (TreeItem item, boolean expanded) {
		int id = idOf (item);
		if (id < 0) return;
		LOG.debug (() -> "Tree setExpanded id " + id + " " + expanded);
		VirtualTreeTopology topology = tree.virtualTopology;
		if (expanded) {
			/*
			 * As the native Tree: a row whose data has not been set has no children yet, so it does not
			 * expand and no SetData is sent for it (stock TreeItem.setExpanded returns without native children).
			 */
			if (!topology.childCountKnown (id) || topology.childCount (id) == 0) return;
		}
		if (topology.flag (id, VirtualItemState.EXPANDED) == expanded) return;
		int [] top = topCoordinate ();
		/*
		 * Native collapse of a row whose subtree holds the caret: the caret moves to the collapsed
		 * row, which becomes the only selected row (every other row is deselected). SWT then sends the
		 * Selection event that Windows omits for a programmatic collapse.
		 */
		boolean caretMoved = !expanded && focusId != id && isDescendant (focusId, id);
		if (caretMoved) {
			/* the collapsed row becomes the whole selection (recorded differential seed 5 step 62) */
			LOG.debug (() -> "Tree collapse over the caret: caret and the whole selection move to id " + id);
			deselectAll ();
			markSelected (id);
			long hNew = residentHandle (item);
			if (hNew != 0) setNativeSelected (hNew, true);
			moveFocus (id);
		}
		topology.flag (id, VirtualItemState.EXPANDED, expanded);
		long hItem = residentHandle (item);
		if (hItem != 0) setNativeExpanded (hItem, expanded);
		tree.virtualViewport.refreshLogicalRange ();
		restoreTop (top);
		layout ();
		if (caretMoved) {
			restoreFocus ();
			Event event = new Event ();
			event.item = item;
			tree.hAnchor = residentHandle (item);
			tree.sendSelectionEvent (SWT.Selection, event, true);
		}
	}

	/* Whether a materialized row lies inside the subtree of another. */
	private boolean isDescendant (int id, int ancestor) {
		VirtualTreeTopology topology = tree.virtualTopology;
		if (id < 0 || !topology.contains (id)) return false;
		for (int parent = topology.parentId (id); parent != VirtualTreeTopology.ROOT; parent = topology.parentId (parent)) {
			if (parent == ancestor) return true;
		}
		return false;
	}

	/**
	 * A native expand starts (TVN_ITEMEXPANDING): the children must be resident before the
	 * TreeView finishes, or it removes the expander of a row that has no native children.
	 */
	void expanding (TreeItem item) {
		int id = idOf (item);
		if (id < 0) return;
		VirtualTreeTopology topology = tree.virtualTopology;
		boolean knownBefore = topology.childCountKnown (id);
		if (!knownBefore) tree.checkData (item, false);
		LOG.debug (() -> "Tree native expansion of id " + id + ": cached " + item.isCachedState () + ", child count known " + knownBefore + " -> "
				+ topology.childCountKnown (id) + (topology.childCountKnown (id) ? " (" + topology.childCount (id) + ")" : "") + ", expanded " + topology.flag (id, VirtualItemState.EXPANDED));
		if (!topology.childCountKnown (id) || topology.childCount (id) == 0) return;
		int [] top = topCoordinate ();
		topology.flag (id, VirtualItemState.EXPANDED, true);
		tree.virtualViewport.refreshLogicalRange ();
		restoreTop (top);
		/* a user expansion scrolls its children into view, as the native TreeView does */
		scrollChildrenIntoView (id);
		/* expanding the caret row (keyboard RIGHT, +, double click) also keeps the caret in view (stock seed 1 step 102) */
		if (focusId == id) keepRowVisible (tree.virtualProjection.visibleIndexOf (id));
		layout ();
	}

	/**
	 * A native collapse starts (TVN_ITEMEXPANDING with TVE_COLLAPSE from the user: VK_SUBTRACT, VK_LEFT, the
	 * expander; the mediator's own TVM_EXPAND runs with ignoreExpand and does not come here), after SWT sent Collapse.
	 * Measured on stock SWT (VkSubtractCollapseOracle, plain and columns, top at, inside and below the row): the row
	 * stays collapsed with its children, no Expand follows, the top item stays the top item or becomes its nearest
	 * visible ancestor, and the caret row is kept in view (TVM_ENSUREVISIBLE semantics). The lane follows here; the
	 * window follows when the native collapse finishes (TVN_ITEMEXPANDED, expansionChanged).
	 */
	void collapsing (TreeItem item) {
		if (tree.isDisposed ()) return;
		int id = idOf (item);
		VirtualTreeTopology topology = tree.virtualTopology;
		if (id < 0 || !topology.flag (id, VirtualItemState.EXPANDED)) return;
		LOG.debug (() -> "Tree native collapse of id " + id + (focusId == id ? " (the caret row)" : ""));
		int [] top = topCoordinate ();
		topology.flag (id, VirtualItemState.EXPANDED, false);
		tree.virtualViewport.refreshLogicalRange ();
		restoreTop (top);
		if (focusId == id) keepRowVisible (tree.virtualProjection.visibleIndexOf (id));
	}

	private void keepRowVisible (long row) {
		VirtualTreeViewport viewport = tree.virtualViewport;
		if (row < 0) return;
		long top = viewport.topRow ();
		int rows = Math.max (1, viewport.visibleRows ());
		if (row < top) viewport.setTopRow (row);
		else if (row >= top + rows) viewport.setTopRow (row - rows + 1);
	}

	/*
	 * TVN_ITEMEXPANDED (and SWT's fall-through from TVN_ITEMEXPANDING for a row without native children) would copy
	 * the native row's state into the lane; the lane is the authority (stock seed 1 step 102, key RIGHT on an anchor row).
	 */
	boolean logicalExpanded (TreeItem item) {
		int id = idOf (item);
		return id >= 0 && tree.virtualTopology.flag (id, VirtualItemState.EXPANDED);
	}

	/** A native expand or collapse finished (TVN_ITEMEXPANDED): the window follows the new shape. */
	void expansionChanged () {
		tree.virtualViewport.refreshLogicalRange ();
		layout ();
	}

	/** Mediated dispose of a facade: the logical row is removed; its native row too when resident. */
	void destroy (TreeItem item) {
		int id = idOf (item);
		if (id < 0) return;
		VirtualTreeTopology topology = tree.virtualTopology;
		int parentId = topology.parentId (id), index = topology.childIndex (id);
		long hRow = plan.handleOf (parentId, index);
		VirtualTreeViewport viewport = tree.virtualViewport;
		long row = tree.virtualProjection.visibleIndexOf (id), rowsBefore = viewport.visibleRowCount ();
		captureResidentState ();
		boolean caretInside = focusId >= 0 && inSubtree (focusId, id);
		TreeItem nextCaret = caretInside ? caretAfterRemoval (parentId, index, row) : null;
		if (caretInside) deselectOnDispose (parentId, index, id, nextCaret);
		boolean nextSelected = nextCaret != null && ((tree.style & SWT.SINGLE) != 0 || idOf (nextCaret) >= 0 && rowSelected (keyOf (idOf (nextCaret))));
		boolean shifted = shiftsResidentRows (parentId, index);
		/* the native row goes first, while the resident keys still match the topology (native notifications bind rows) */
		if (hRow != 0) deleteResidentRow (hRow);
		releaseFacades (id);
		topology.removeCoordinate (parentId, index);
		shiftPattern (parentId, index, -1);
		patterns.remove (id);
		if (shifted) shiftResidentRows (parentId, index + 1, -1);
		viewport.refreshLogicalRange ();
		keepRemovalVisible (row, rowsBefore - viewport.visibleRowCount ());
		if (nextCaret != null) moveCaretAfterRemoval (nextCaret);
		layout ();
		if (nextCaret != null && (tree.style & SWT.SINGLE) != 0) selectSilently (nextCaret);
		if (nextCaret != null) keepSelection (nextCaret, nextSelected);
		if (parentId == VirtualTreeTopology.ROOT && topology.childCount (VirtualTreeTopology.ROOT) == 0) becameEmpty ();
	}

	/*
	 * Stock destroyItem when the last item is gone (TVM_GETCOUNT == 0), measured by EmptyOracle: the image list is
	 * released (the next first image sets a new image size), custom draw is reset where stock resets it, the scroll
	 * width is cleared, and EmptinessChanged (detail 1) is sent. removeAll keeps its own stock code and sends no event.
	 */
	private void becameEmpty () {
		LOG.debug (() -> "Tree became empty: image list released, EmptinessChanged");
		if (tree.imageList != null) {
			OS.SendMessage (tree.handle, OS.TVM_SETIMAGELIST, 0, 0);
			tree.display.releaseImageList (tree.imageList);
		}
		tree.imageList = null;
		/* stock resets custom draw only without a parent window (no columns, header or MeasureItem) */
		if (!measuresShownRow () && !tree.linesVisible && !tree.hooks (SWT.EraseItem) && !tree.hooks (SWT.PaintItem)) {
			tree.customDraw = false;
		}
		tree.scrollWidth = 0;
		tree.setScrollWidth ();
		Event event = new Event ();
		event.detail = 1;
		tree.sendEvent (SWT.EmptinessChanged, event);
	}

	private boolean inSubtree (int candidate, int rootId) {
		VirtualTreeTopology topology = tree.virtualTopology;
		for (int id = candidate; id != VirtualTreeTopology.ROOT; id = topology.parentId (id)) {
			if (id == rootId) return true;
		}
		return false;
	}

	/*
	 * Measured on stock SWT (CaretDisposeOracle, MULTI and SINGLE): removing the caret row or one of its
	 * ancestors moves the caret to the removed item's next sibling, else its previous sibling, else its
	 * parent. The TreeView picks it during the delete (the removed row's children are already gone, the
	 * row itself not yet): a next sibling then on screen asks for its data before the indices shift, an
	 * off-screen one only when it is painted later. Answers that item or null.
	 */
	private TreeItem caretAfterRemoval (int parentId, int index, long row) {
		int count = getItemCount (parentId == VirtualTreeTopology.ROOT ? null : facadeOrNull (parentId));
		TreeItem next;
		if (index + 1 < count) {
			next = facade (parentId, index + 1);
			VirtualTreeViewport viewport = tree.virtualViewport;
			long top = viewport.topRow ();
			/* the next sibling's row follows the removed row's visible descendants (an expanded row is not one row) */
			long nextRow = row < 0 ? -1 : tree.virtualProjection.visibleIndexOfCoordinate (parentId, index + 1);
			boolean onScreen = nextRow >= 0 && nextRow >= top && nextRow < top + Math.max (1, viewport.visibleRows ());
			if (next != null && onScreen) tree.checkData (next, false);
			return next;
		} else if (index > 0) {
			next = facade (parentId, index - 1);
		} else if (parentId != VirtualTreeTopology.ROOT) {
			return facadeOrNull (parentId);
		} else {
			return null;
		}
		return next;
	}

	/* the new caret is scrolled into view; the selection is unchanged (MULTI) and no event is sent */
	private void moveCaretAfterRemoval (TreeItem item) {
		int nextId = idOf (item);
		if (nextId < 0) return;
		LOG.debug (() -> "Tree caret row removed: caret to id " + nextId);
		moveFocus (nextId);
		long row = tree.virtualProjection.visibleIndexOf (nextId);
		if (row >= 0) tree.virtualViewport.ensureVisible (row);
	}

	/* SINGLE: the caret is the selection, so the new caret is selected too (no event, as in stock) */
	private void selectSilently (TreeItem item) {
		int id = idOf (item);
		if (id < 0) return;
		markSelected (id);
		long hItem = residentHandle (item);
		if (hItem != 0) setNativeSelected (hItem, true);
	}

	/*
	 * Measured on stock SWT (DisposeAboveTopOracle, plain and columns variants): removing an item with
	 * visible descendants scrolls the row that takes its place into view, as TVM_ENSUREVISIBLE does (to the
	 * top from above, also when the top row was inside the removed subtree; to the bottom from below).
	 * Removing a single row keeps the top row index.
	 */
	private void keepRemovalVisible (long row, long removedRows) {
		VirtualTreeViewport viewport = tree.virtualViewport;
		long rows = viewport.visibleRowCount ();
		if (row < 0 || removedRows <= 1 || rows == 0) return;
		long target = Math.min (row, rows - 1);
		LOG.debug (() -> "Tree removed " + removedRows + " rows at row " + row + ": row " + target + " scrolled into view");
		viewport.ensureVisible (target);
	}

	/*
	 * The resident rows are keyed by (parent, child index). An insert or a removal at <code>index</code> of
	 * <code>parentId</code> shifts the index of every later sibling.
	 */
	private boolean shiftsResidentRows (int parentId, int index) {
		for (int entry = 0; entry < plan.size (); entry++) {
			long key = plan.keyOf (entry);
			if ((int) (key >> 32) == parentId && (int) key >= index) return true;
		}
		return false;
	}

	/*
	 * Resident siblings whose child index moved keep their native rows and only change keys, so the next
	 * layout inserts or retires just the rows entering or leaving the window (instead of rebuilding it).
	 */
	private void shiftResidentRows (int parentId, int fromIndex, int delta) {
		LOG.trace (() -> "Tree siblings of " + parentId + " from " + fromIndex + " moved by " + delta + ": resident keys shifted");
		plan.shiftSiblings (parentId, fromIndex, delta);
		committedGeneration = -1;
	}

	/*
	 * A resident row leaves: its native subtree is deleted and dropped from the plan, the other rows stay.
	 * As for a retired row, a caret inside the subtree is detached first, so the TreeView does not move it
	 * (the lanes decide the caret; the removed item's focus was already released).
	 */
	private void deleteResidentRow (long hRow) {
		long tv = tree.handle;
		int entry = plan.entryOf (hRow);
		boolean oldShrink = tree.ignoreShrink, oldSelect = tree.ignoreSelect, oldDeselect = tree.ignoreDeselect;
		tree.ignoreShrink = tree.ignoreSelect = tree.ignoreDeselect = true;
		try {
			long caret = OS.SendMessage (tv, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
			boolean caretInside = detachSubtree (hRow, caret);
			if (caret != 0 && caretInside) clearNativeCaret ();
			OS.SendMessage (tv, OS.TVM_DELETEITEM, 0, hRow);
		} finally {
			tree.ignoreShrink = oldShrink;
			tree.ignoreSelect = oldSelect;
			tree.ignoreDeselect = oldDeselect;
		}
		if (entry >= 0) plan.removeSubtree (entry);
		committedGeneration = -1;
	}

	/* ---------------------------------------------------------------- selection */

	/** Resident rows are the authority for their selection and the caret; copy them into the lanes. */
	private void captureResidentState () {
		/* while the mediator moves the native caret the native selection is transient (the TreeView deselects a row for the move): the lanes stay */
		if (drivingCaret > 0) return;
		int size = plan.size ();
		if (size == 0) return;
		long tv = tree.handle;
		long caret = OS.SendMessage (tv, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
		TVITEM tvItem = new TVITEM ();
		tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_PARAM | OS.TVIF_STATE;
		tvItem.stateMask = OS.TVIS_SELECTED;
		VirtualTreeTopology topology = tree.virtualTopology;
		for (int entry = 0; entry < size; entry++) {
			long hItem = plan.handle (entry);
			tvItem.hItem = hItem;
			if (OS.SendMessage (tv, OS.TVM_GETITEM, 0, tvItem) == 0) continue;
			boolean selected = (tvItem.state & OS.TVIS_SELECTED) != 0;
			TreeItem item = facadeOrNull ((int) tvItem.lParam);
			long key = plan.keyOf (entry);
			/* a row whose coordinate a structural change removed (for example setItemCount (0)) is stale: never capture it */
			if (!coordinateExists ((int) (key >> 32), (int) key)) continue;
			if (item == null && (selected || hItem == caret || patternSelects ((int) (key >> 32), (int) key))) {
				item = facade ((int) (key >> 32), (int) key);
			}
			int id = idOf (item);
			if (id < 0) continue;
			/* a row the user selected natively (keys, clicks) becomes the most recently selected one */
			if (selected) markSelected (id);
			else topology.flag (id, VirtualItemState.SELECTED, false);
			if (hItem == caret && !focusPending) focusId = id;
		}
	}

	private void restoreFocus () {
		TreeItem item = facadeOrNull (focusId);
		long hFocus = residentHandle (item);
		if (hFocus == 0) return;
		focusPending = false;
		long tv = tree.handle;
		if (OS.SendMessage (tv, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0) == hFocus) return;
		/*
		 * A programmatic caret move is silent and selection-neutral. Natively it selects the new
		 * caret row and may deselect another row, and in MULTI mode the TVN_SELCHANGING handler
		 * would deselect everything: the resident selection is put back afterwards.
		 */
		int size = plan.size ();
		boolean [] selected = new boolean [size];
		TVITEM tvItem = new TVITEM ();
		tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_STATE;
		tvItem.stateMask = OS.TVIS_SELECTED;
		for (int entry = 0; entry < size; entry++) {
			tvItem.hItem = plan.handle (entry);
			selected [entry] = OS.SendMessage (tv, OS.TVM_GETITEM, 0, tvItem) != 0 && (tvItem.state & OS.TVIS_SELECTED) != 0;
		}
		int focusEntry = plan.entryOf (hFocus);
		boolean focusSelected = tree.virtualTopology.flag (focusId, VirtualItemState.SELECTED)
				|| focusEntry >= 0 && selected [focusEntry];
		boolean oldSelect = tree.ignoreSelect, oldDeselect = tree.ignoreDeselect;
		int rebuildsBefore = rebuilds;
		tree.ignoreSelect = tree.ignoreDeselect = true;
		try {
			drivingCaret++;
			OS.SendMessage (tv, OS.TVM_SELECTITEM, OS.TVGN_CARET, hFocus);
		} finally {
			drivingCaret--;
			tree.ignoreSelect = oldSelect;
			tree.ignoreDeselect = oldDeselect;
		}
		if (rebuilds != rebuildsBefore) {
			/*
			 * The caret move rebuilt the window re-entrantly (SetData of a row coming into view changed a child count):
			 * the rows are new, so every resident row takes its selection from the lanes, which the move did not touch.
			 */
			LOG.debug (() -> "Tree caret move rebuilt the window re-entrantly");
			long hNewFocus = residentHandle (item);
			for (int entry = 0; entry < plan.size (); entry++) {
				long hItem = plan.handle (entry);
				tvItem.hItem = hItem;
				boolean now = OS.SendMessage (tv, OS.TVM_GETITEM, 0, tvItem) != 0 && (tvItem.state & OS.TVIS_SELECTED) != 0;
				boolean wanted = hItem == hNewFocus ? focusSelected : rowSelected (plan.keyOf (entry));
				if (now != wanted) setNativeSelected (hItem, wanted);
			}
			return;
		}
		for (int entry = 0; entry < size; entry++) {
			long hItem = plan.handle (entry);
			tvItem.hItem = hItem;
			boolean now = OS.SendMessage (tv, OS.TVM_GETITEM, 0, tvItem) != 0 && (tvItem.state & OS.TVIS_SELECTED) != 0;
			boolean wanted = hItem == hFocus ? focusSelected : selected [entry];
			if (now != wanted) setNativeSelected (hItem, wanted);
		}
	}

	/** Moves the logical caret; the native caret follows at the next {@link #restoreFocus()}. */
	private void moveFocus (int id) {
		LOG.trace (() -> "Tree logical caret -> id " + id + " (native caret follows at restoreFocus)");
		focusId = id;
		focusPending = id >= 0;
	}

	/*
	 * Takes the native caret off its row before the row is deleted (otherwise the TreeView picks a new caret
	 * itself). A native caret move also deselects the most recently selected row other than the old caret (the rule
	 * deselectOnCaretMoves mirrors for the public API), which here would be a resident row the lanes still select,
	 * picked from the native insertion order: every resident row keeps its selection across the call.
	 */
	private void clearNativeCaret () {
		long tv = tree.handle;
		int size = plan.size ();
		boolean [] selected = new boolean [size], known = new boolean [size];
		TVITEM tvItem = new TVITEM ();
		tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_STATE;
		tvItem.stateMask = OS.TVIS_SELECTED;
		for (int entry = 0; entry < size; entry++) {
			tvItem.hItem = plan.handle (entry);
			known [entry] = OS.SendMessage (tv, OS.TVM_GETITEM, 0, tvItem) != 0;
			selected [entry] = known [entry] && (tvItem.state & OS.TVIS_SELECTED) != 0;
		}
		drivingCaret++;
		try {
			OS.SendMessage (tv, OS.TVM_SELECTITEM, OS.TVGN_CARET, 0);
		} finally {
			drivingCaret--;
		}
		for (int entry = 0; entry < size; entry++) {
			if (!known [entry]) continue;
			tvItem.hItem = plan.handle (entry);
			if (OS.SendMessage (tv, OS.TVM_GETITEM, 0, tvItem) == 0) continue;
			if (((tvItem.state & OS.TVIS_SELECTED) != 0) != selected [entry]) setNativeSelected (tvItem.hItem, selected [entry]);
		}
	}

	private void setNativeSelected (long hItem, boolean selected) {
		TVITEM tvItem = new TVITEM ();
		tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_STATE;
		tvItem.stateMask = OS.TVIS_SELECTED;
		tvItem.state = selected ? OS.TVIS_SELECTED : 0;
		tvItem.hItem = hItem;
		OS.SendMessage (tree.handle, OS.TVM_SETITEM, 0, tvItem);
	}

	/** Selection in tree (preorder) order, from the lanes after copying the resident rows in. */
	TreeItem [] getSelection () {
		captureResidentState ();
		ArrayList<TreeItem> result = new ArrayList<> ();
		if (anyChildrenSelected) {
			selectedRowsInPreorder (result);
		} else {
			visitMaterialized (id -> {
				if (tree.virtualTopology.flag (id, VirtualItemState.SELECTED)) {
					TreeItem item = facadeOrNull (id);
					if (item != null) {
						tree.pinVirtualFacade (item);
						result.add (item);
					}
				}
			}, true);
		}
		return result.toArray (new TreeItem [0]);
	}

	/* after selectAll the cold rows are selected by pattern: walk the visible rows (the result is O(selection) anyway) */
	private void selectedRowsInPreorder (ArrayList<TreeItem> result) {
		VirtualTreeVisibleProjection projection = tree.virtualProjection;
		VirtualTreeTopology topology = tree.virtualTopology;
		long total = projection.visibleRowCount ();
		for (long first = 0; first < total; first += 4096) {
			for (VirtualTreeVisibleProjection.Row row : projection.window (first, (int) Math.min (4096, total - first))) {
				boolean selected = row.materialized () ? topology.flag (row.materializedId (), VirtualItemState.SELECTED) : patternSelects (row.parentId (), row.childIndex ());
				if (!selected) continue;
				TreeItem item = row.materialized () ? facadeOrNull (row.materializedId ()) : null;
				if (item == null) item = facade (row.parentId (), row.childIndex ());
				tree.pinVirtualFacade (item);
				result.add (item);
			}
		}
	}

	/* arithmetic: materialized selected rows plus, per pattern parent, its cold children */
	int getSelectionCount () {
		captureResidentState ();
		VirtualTreeTopology topology = tree.virtualTopology;
		long [] count = {0};
		count [0] += coldChildren (VirtualTreeTopology.ROOT);
		visitMaterialized (id -> {
			if (topology.flag (id, VirtualItemState.SELECTED)) count [0]++;
			if (topology.flag (id, VirtualItemState.EXPANDED)) count [0] += coldChildren (id);
		}, true);
		return (int) Math.min (Integer.MAX_VALUE, count [0]);
	}

	private long coldChildren (int parentId) {
		VirtualTreeTopology topology = tree.virtualTopology;
		if (!topology.childCountKnown (parentId)) return 0;
		int limit = Math.min (patternLimit (parentId), topology.childCount (parentId));
		long count = limit;
		for (int child = topology.firstMaterializedChildId (parentId); child >= 0 && count > 0; child = topology.nextMaterializedSiblingId (child)) {
			if (topology.childIndex (child) < limit) count--;
		}
		return count;
	}

	private boolean coordinateExists (int parentId, int index) {
		VirtualTreeTopology topology = tree.virtualTopology;
		if (parentId != VirtualTreeTopology.ROOT && !topology.contains (parentId)) return false;
		return topology.childCountKnown (parentId) && index >= 0 && index < topology.childCount (parentId);
	}

	private int patternLimit (int parentId) {
		if (!anyChildrenSelected) return 0;
		long [] segments = patterns.get (parentId);
		return segments == null ? 0 : (int) segments [segments.length - 2];
	}

	private boolean patternSelects (int parentId, int index) {
		return index >= 0 && index < patternLimit (parentId);
	}

	/* the selectAll (selection clock value) whose pattern selected a cold child, 0 outside the pattern */
	private long epochOf (int parentId, int index) {
		long [] segments = anyChildrenSelected ? patterns.get (parentId) : null;
		if (segments == null || index < 0) return 0;
		for (int s = 0; s < segments.length; s += 2) {
			if (index < segments [s]) return segments [s + 1];
		}
		return 0;
	}

	/* an insert (delta 1) or a removal (delta -1) at index shifts the pattern of its parent */
	private void shiftPattern (int parentId, int index, int delta) {
		long [] segments = patterns.get (parentId);
		if (segments == null) return;
		for (int s = 0; s < segments.length; s += 2) {
			if (index < segments [s]) segments [s] += delta;
		}
	}

	/* a shrink to count children clamps the pattern of the parent */
	private void clampPattern (int parentId, int count) {
		long [] segments = patterns.get (parentId);
		if (segments == null) return;
		for (int s = 0; s < segments.length; s += 2) segments [s] = Math.min (segments [s], count);
	}

	/* selectAll over a parent's children [0, count): children beyond its pattern join it under this selectAll's epoch */
	private void extendPattern (int parentId, int count, long epoch) {
		long [] segments = patterns.get (parentId);
		if (segments == null) {
			patterns.put (parentId, new long [] {count, epoch});
			return;
		}
		if (count <= segments [segments.length - 2]) return;
		long [] grown = Arrays.copyOf (segments, segments.length + 2);
		grown [segments.length] = count;
		grown [segments.length + 1] = epoch;
		patterns.put (parentId, grown);
	}

	/* the clock value at which a facade's row last became selected (0: none recorded) */
	private long selectedAt (int id) {
		return id >= 0 && id < selectedAt.length ? selectedAt [id] : 0;
	}

	private void stampSelected (int id, long stamp) {
		if (id >= selectedAt.length) selectedAt = Arrays.copyOf (selectedAt, Math.max (id + 1, selectedAt.length * 3 / 2 + 16));
		selectedAt [id] = stamp;
	}

	/*
	 * Selects a facade's row in the lanes. A row that was not selected becomes the most recently selected one; a row
	 * that already was keeps its place (natively, setting TVIS_SELECTED again changes nothing).
	 */
	private void markSelected (int id) {
		VirtualTreeTopology topology = tree.virtualTopology;
		if (!topology.flag (id, VirtualItemState.SELECTED)) stampSelected (id, ++selectionClock);
		topology.flag (id, VirtualItemState.SELECTED, true);
	}

	/**
	 * Native MULTI selectAll selects every row reachable through expanded parents, without events.
	 * Mediated as a pattern: materialized visible rows get their bit, every expanded visible node
	 * (and the root) marks its cold children selected; resident visible rows are selected natively.
	 * The rows it selects become the most recently selected ones in preorder (one epoch); rows that
	 * were selected already keep their place (CaretMoveSelectionOracle).
	 */
	void selectAll () {
		captureResidentState ();
		VirtualTreeTopology topology = tree.virtualTopology;
		anyChildrenSelected = true;
		long epoch = ++selectionClock;
		patternEpochs.add (epoch);
		extendPattern (VirtualTreeTopology.ROOT, topology.childCountKnown (VirtualTreeTopology.ROOT) ? topology.childCount (VirtualTreeTopology.ROOT) : 0, epoch);
		visitMaterialized (id -> {
			if (!topology.flag (id, VirtualItemState.SELECTED)) stampSelected (id, epoch);
			topology.flag (id, VirtualItemState.SELECTED, true);
			if (topology.flag (id, VirtualItemState.EXPANDED) && topology.childCountKnown (id)) extendPattern (id, topology.childCount (id), epoch);
		}, true);
		for (int entry = 0; entry < plan.size (); entry++) {
			if (rowOfKey (plan.keyOf (entry)) >= 0) setNativeSelected (plan.handle (entry), true);
		}
		LOG.debug (() -> "Tree selectAll: " + tree.virtualViewport.visibleRowCount () + " visible rows selected by pattern");
	}

	void setSelection (TreeItem [] items) {
		for (TreeItem item : items) {
			if (item != null && item.isDisposed ()) tree.error (SWT.ERROR_INVALID_ARGUMENT);
		}
		deselectAll ();
		boolean single = (tree.style & SWT.SINGLE) != 0;
		if (items.length == 0 || (single && items.length > 1)) return;
		TreeItem first = null;
		for (TreeItem item : items) {
			int id = idOf (item);
			if (id < 0) continue;
			if (first == null) first = item;
			markSelected (id);
			long hItem = residentHandle (item);
			if (hItem != 0) setNativeSelected (hItem, true);
		}
		if (first != null) {
			moveFocus (idOf (first));
			showItem (first);
			restoreFocus ();
			/* natively TVM_SELECTITEM repaints synchronously (see paintNow) */
			paintNow ();
			/* as SWT's MULTI setSelection: every selected item's ancestors expand (expandToItem) */
			if (!single) {
				for (TreeItem item : items) {
					if (item != null && !item.isDisposed ()) tree.expandToItem (item);
				}
			}
		}
	}

	void select (TreeItem item) {
		if ((tree.style & SWT.SINGLE) != 0) {
			setSelection (new TreeItem [] {item});
			return;
		}
		tree.expandToItem (item);
		int id = idOf (item);
		if (id < 0) return;
		markSelected (id);
		long hItem = residentHandle (item);
		if (hItem != 0) setNativeSelected (hItem, true);
	}

	void deselect (TreeItem item) {
		int id = idOf (item);
		if (id < 0) return;
		tree.virtualTopology.flag (id, VirtualItemState.SELECTED, false);
		long hItem = residentHandle (item);
		if (hItem != 0) setNativeSelected (hItem, false);
	}

	void deselectAll () {
		LOG.debug (() -> "Tree deselectAll" + (anyChildrenSelected ? " (clears the selectAll pattern)" : ""));
		VirtualTreeTopology topology = tree.virtualTopology;
		visitMaterialized (id -> topology.flag (id, VirtualItemState.SELECTED, false), false);
		patterns.clear ();
		patternEpochs.clear ();
		anyChildrenSelected = false;
		for (int entry = 0; entry < plan.size (); entry++) setNativeSelected (plan.handle (entry), false);
	}

	/* preorder over materialized coordinates; visibleOnly descends only into expanded nodes */
	private void visitMaterialized (java.util.function.IntConsumer visitor, boolean visibleOnly) {
		VirtualTreeTopology topology = tree.virtualTopology;
		ArrayDeque<Integer> stack = new ArrayDeque<> ();
		ArrayList<Integer> children = new ArrayList<> ();
		for (int id = topology.firstMaterializedChildId (VirtualTreeTopology.ROOT); id >= 0; id = topology.nextMaterializedSiblingId (id)) {
			children.add (id);
		}
		for (int i = children.size () - 1; i >= 0; i--) stack.push (children.get (i));
		while (!stack.isEmpty ()) {
			int id = stack.pop ();
			visitor.accept (id);
			children.clear ();
			if (visibleOnly && !topology.flag (id, VirtualItemState.EXPANDED)) continue;
			for (int child = topology.firstMaterializedChildId (id); child >= 0; child = topology.nextMaterializedSiblingId (child)) {
				children.add (child);
			}
			for (int i = children.size () - 1; i >= 0; i--) stack.push (children.get (i));
		}
	}
}
