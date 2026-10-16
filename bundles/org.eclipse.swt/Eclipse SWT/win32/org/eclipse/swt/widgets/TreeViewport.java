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
	/** Opt-in until the Tree suites pass with the viewport on: <code>-Dorg.eclipse.swt.internal.win32.Tree.viewport=true</code>. */
	static final boolean ENABLED = Boolean.getBoolean ("org.eclipse.swt.internal.win32.Tree.viewport");
	/** Sentinel parent handle of {@link TreeItem}'s internal constructor: create a facade without a native row. */
	static final long DETACHED = Long.MIN_VALUE;
	private static final System.Logger LOG = System.getLogger ("org.eclipse.swt.widgets.Tree");

	final Tree tree;
	private final VirtualTreeVisibleProjection.Residency residency = new VirtualTreeVisibleProjection.Residency ();
	private final VirtualTreeResidentPlan plan = new VirtualTreeResidentPlan ();
	private long committedGeneration = -1, committedFirstRow = -1;
	private int committedCount = -1;
	private int focusId = -1;
	private boolean busy;

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
			positionPlane ();
			updateBar ();
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
	}

	/** Moves the native window to the current paint window: retire, insert, commit. */
	private void rebuild () {
		long tv = tree.handle;
		captureResidentState ();
		tree.virtualViewport.paintResidency (residency);
		plan.plan (residency);
		int count = plan.plannedCount ();
		boolean redraw = tree.getDrawing () && OS.IsWindowVisible (tv);
		if (redraw) OS.DefWindowProc (tv, OS.WM_SETREDRAW, 0, 0);
		boolean oldShrink = tree.ignoreShrink, oldSelect = tree.ignoreSelect, oldDeselect = tree.ignoreDeselect;
		tree.ignoreShrink = tree.ignoreSelect = tree.ignoreDeselect = true;
		int retired = plan.retiredCount (), inserted = 0;
		try {
			long firstRoot = OS.SendMessage (tv, OS.TVM_GETNEXTITEM, OS.TVGN_ROOT, 0);
			long caret = OS.SendMessage (tv, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
			for (int i = 0; i < retired; i++) {
				long hItem = plan.retired (i);
				if (caret != 0 && detachSubtree (hItem, caret)) {
					OS.SendMessage (tv, OS.TVM_SELECTITEM, OS.TVGN_CARET, 0);
					caret = 0;
				}
				OS.SendMessage (tv, OS.TVM_DELETEITEM, 0, hItem);
			}
			long [] handles = new long [count];
			for (int entry = 0; entry < count; entry++) {
				long reused = plan.plannedHandle (entry);
				if (reused != 0) {
					handles [entry] = reused;
					continue;
				}
				int physicalParent = residency.physicalParent (entry);
				long hParent = physicalParent == -1 ? OS.TVI_ROOT : handles [physicalParent];
				int previous = plan.previousSibling (entry);
				long hAfter = previous == -1 ? OS.TVI_FIRST : handles [previous];
				handles [entry] = insertRow (hParent, hAfter, residency.materializedId (entry));
				inserted++;
			}
			plan.commit (handles);
			if (OS.SendMessage (tv, OS.TVM_GETNEXTITEM, OS.TVGN_ROOT, 0) != firstRoot) resetPlaneTop ();
			restoreFocus ();
			tree.cachedFirstItem = tree.cachedIndexItem = 0;
			tree.cachedItemCount = -1;
			tree.pageCursorParent = Tree.PAGE_NO_CURSOR;
		} finally {
			tree.ignoreShrink = oldShrink;
			tree.ignoreSelect = oldSelect;
			tree.ignoreDeselect = oldDeselect;
			if (redraw) {
				OS.DefWindowProc (tv, OS.WM_SETREDRAW, 1, 0);
				OS.InvalidateRect (tv, null, true);
			}
		}
		final int ins = inserted;
		LOG.log (System.Logger.Level.TRACE, () -> "Tree window: rows " + residency.firstPaintRow () + "+" + residency.paintCount ()
				+ ", native " + plan.size () + " (" + ins + " inserted, " + retired + " retired subtrees)");
	}

	private long insertRow (long hParent, long hAfter, int id) {
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
		if (item != null && topology.flag (id, VirtualItemState.SELECTED)) state |= OS.TVIS_SELECTED;
		if ((tree.style & SWT.CHECK) != 0) {
			int image = 1;
			if (item != null && topology.flag (id, VirtualItemState.CHECKED)) image++;
			if (item != null && topology.flag (id, VirtualItemState.GRAYED)) image += 2;
			state |= image << 12;
			mask |= OS.TVIS_STATEIMAGEMASK;
		}
		insert.state = state;
		insert.stateMask = mask;
		long hItem = OS.SendMessage (tree.handle, OS.TVM_INSERTITEM, 0, insert);
		if (hItem == 0) tree.error (SWT.ERROR_ITEM_NOT_ADDED);
		if (item != null) item.handle = hItem;
		return hItem;
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
	 * A non-scrolling TreeView keeps its internal top row when rows are inserted in front of
	 * it; scrolling is briefly enabled to put the first native row back at the top of the plane.
	 */
	private void resetPlaneTop () {
		long tv = tree.handle;
		int bits = OS.GetWindowLong (tv, OS.GWL_STYLE);
		OS.SetWindowLong (tv, OS.GWL_STYLE, bits & ~OS.TVS_NOSCROLL);
		long first = OS.SendMessage (tv, OS.TVM_GETNEXTITEM, OS.TVGN_ROOT, 0);
		if (first != 0) OS.SendMessage (tv, OS.TVM_SELECTITEM, OS.TVGN_FIRSTVISIBLE, first);
		OS.SetWindowLong (tv, OS.GWL_STYLE, bits);
	}

	private void positionPlane () {
		long tv = tree.handle;
		RECT client = new RECT ();
		OS.GetClientRect (tree.hwndParent, client);
		int width = client.right - client.left;
		int header = headerHeight ();
		int itemHeight = Math.max (1, tree.getItemHeightInPixels ());
		int y = header;
		if (residency.paintCount () > 0 && plan.size () == residency.size ()) {
			long offset = tree.virtualViewport.topRow () - residency.firstPaintRow ();
			int paintOffset = (int) Math.max (0, Math.min (residency.paintCount () - 1L, offset));
			long hTop = plan.handle (residency.paintEntry (paintOffset));
			RECT rect = new RECT ();
			if (OS.TreeView_GetItemRect (tv, hTop, rect, false)) y = header - rect.top;
		}
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
		int height = Math.max (client.bottom - client.top, (plan.size () + 2) * itemHeight);
		boolean oldResize = tree.ignoreResize;
		tree.ignoreResize = true;
		try {
			OS.SetWindowPos (tv, 0, -left, y, contentWidth + left, height, OS.SWP_NOACTIVATE | OS.SWP_NOZORDER);
		} finally {
			tree.ignoreResize = oldResize;
		}
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
		ScrollBar bar = tree.verticalBar;
		if (bar != null) {
			bar.setIncrement (1);
			bar.setPageIncrement (Math.max (1, page));
		}
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
		if (wheel.count != 0) setTopRow (tree.virtualViewport.topRow () - wheel.count);
		return 0;
	}

	/** Page Up, Page Down, Home and End move the caret over logical rows; other keys stay native. */
	LRESULT onKeyDown (long wParam) {
		int key = (int) wParam;
		if (key != OS.VK_PRIOR && key != OS.VK_NEXT && key != OS.VK_HOME && key != OS.VK_END) return null;
		if (OS.GetKeyState (OS.VK_SHIFT) < 0 || OS.GetKeyState (OS.VK_CONTROL) < 0) return null;
		VirtualTreeViewport viewport = tree.virtualViewport;
		long rows = viewport.visibleRowCount ();
		if (rows == 0) return LRESULT.ZERO;
		long top = viewport.topRow (), page = Math.max (1, viewport.visibleRows ());
		long caret = caretRow ();
		if (caret < 0) caret = top;
		long target = switch (key) {
			case OS.VK_HOME -> 0;
			case OS.VK_END -> rows - 1;
			case OS.VK_NEXT -> caret < top + page - 1 ? top + page - 1 : caret + page - 1;
			default -> caret > top ? top : caret - page + 1;
		};
		target = Math.max (0, Math.min (rows - 1, target));
		viewport.ensureVisible (target);
		layout ();
		long hTarget = handleOfRow (target);
		if (hTarget != 0) OS.SendMessage (tree.handle, OS.TVM_SELECTITEM, OS.TVGN_CARET, hTarget);
		return LRESULT.ZERO;
	}

	/** After the native TreeView handled a message: keep the caret row visible. */
	void afterNative (int msg) {
		if (busy) return;
		if (msg != OS.WM_KEYDOWN && msg != OS.WM_CHAR && msg != OS.WM_LBUTTONDOWN) return;
		long row = caretRow ();
		if (row < 0) return;
		VirtualTreeViewport viewport = tree.virtualViewport;
		long top = viewport.topRow ();
		if (row < top || row >= top + Math.max (1, viewport.visibleRows ())) {
			viewport.ensureVisible (row);
			layout ();
		}
	}

	private long caretRow () {
		long caret = OS.SendMessage (tree.handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
		if (caret == 0) return -1;
		int entry = plan.entryOf (caret);
		return entry < 0 || entry >= residency.size () ? -1 : residency.visibleRow (entry);
	}

	private long handleOfRow (long row) {
		long offset = row - residency.firstPaintRow ();
		if (offset < 0 || offset >= residency.paintCount () || plan.size () != residency.size ()) return 0;
		return plan.handle (residency.paintEntry ((int) offset));
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
	}

	/** Binds a facade that adopted a resident native row to that row's logical coordinate. */
	void bind (TreeItem item) {
		int entry = plan.entryOf (item.handle);
		if (entry < 0 || item.virtualId < 0) return;
		long key = plan.keyOf (entry);
		tree.virtualTopology.bind (item.virtualId, (int) (key >> 32), (int) key);
	}

	/** The facade of a logical row, created on demand: from its resident native row, or without one. */
	TreeItem facade (int parentId, int index) {
		VirtualTreeTopology topology = tree.virtualTopology;
		TreeItem item = facadeOrNull (topology.materializedChildId (parentId, index));
		if (item != null) return item;
		long hItem = plan.handleOf (parentId, index);
		if (hItem != 0) {
			item = tree._getItem (hItem, -1);
			if (item != null && idOf (item) >= 0) return item;
		}
		item = new TreeItem (tree, SWT.NONE, DETACHED, 0, 0);
		topology.bind (item.virtualId, parentId, index);
		if (hItem != 0) {
			TVITEM tvItem = new TVITEM ();
			tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_PARAM;
			tvItem.hItem = hItem;
			tvItem.lParam = item.virtualId;
			OS.SendMessage (tree.handle, OS.TVM_SETITEM, 0, tvItem);
			item.handle = hItem;
		}
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
		count = Math.max (0, count);
		int parentId = parentIdOf (parent);
		VirtualTreeTopology topology = tree.virtualTopology;
		for (int id = topology.firstMaterializedChildId (parentId); id >= 0;) {
			int next = topology.nextMaterializedSiblingId (id);
			if (topology.childIndex (id) >= count) releaseFacades (id);
			id = next;
		}
		topology.setChildCount (parentId, count);
		if (parent != null && parent.handle != 0) {
			TVITEM tvItem = new TVITEM ();
			tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_CHILDREN;
			tvItem.hItem = parent.handle;
			tvItem.cChildren = count > 0 ? 1 : 0;
			OS.SendMessage (tree.handle, OS.TVM_SETITEM, 0, tvItem);
		}
		tree.virtualViewport.refreshLogicalRange ();
		layout ();
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
			if (focusId == id) focusId = -1;
		}
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
		expandAncestors (id);
		long row = tree.virtualProjection.visibleIndexOf (id);
		if (row >= 0) setTopRow (row);
	}

	void showItem (TreeItem item) {
		int id = idOf (item);
		if (id < 0) return;
		expandAncestors (id);
		long row = tree.virtualProjection.visibleIndexOf (id);
		if (row < 0) return;
		tree.virtualViewport.ensureVisible (row);
		layout ();
	}

	private void expandAncestors (int id) {
		VirtualTreeTopology topology = tree.virtualTopology;
		boolean changed = false;
		for (int parent = topology.parentId (id); parent != VirtualTreeTopology.ROOT; parent = topology.parentId (parent)) {
			if (!topology.flag (parent, VirtualItemState.EXPANDED)) {
				topology.flag (parent, VirtualItemState.EXPANDED, true);
				changed = true;
			}
		}
		if (changed) tree.virtualViewport.refreshLogicalRange ();
	}

	/** Mediated <code>TreeItem.setExpanded</code>: expansion is a topology lane; the window follows. */
	void setExpanded (TreeItem item, boolean expanded) {
		int id = idOf (item);
		if (id < 0) return;
		VirtualTreeTopology topology = tree.virtualTopology;
		if (expanded) {
			if (!topology.childCountKnown (id)) tree.checkData (item, false);
			if (!topology.childCountKnown (id) || topology.childCount (id) == 0) return;
		}
		if (topology.flag (id, VirtualItemState.EXPANDED) == expanded) return;
		topology.flag (id, VirtualItemState.EXPANDED, expanded);
		if (item.handle != 0) {
			TVITEM tvItem = new TVITEM ();
			tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_STATE;
			tvItem.hItem = item.handle;
			tvItem.stateMask = OS.TVIS_EXPANDED;
			tvItem.state = expanded ? OS.TVIS_EXPANDED : 0;
			OS.SendMessage (tree.handle, OS.TVM_SETITEM, 0, tvItem);
		}
		tree.virtualViewport.refreshLogicalRange ();
		layout ();
	}

	/**
	 * A native expand starts (TVN_ITEMEXPANDING): the children must be resident before the
	 * TreeView finishes, or it removes the expander of a row that has no native children.
	 */
	void expanding (TreeItem item) {
		int id = idOf (item);
		if (id < 0) return;
		VirtualTreeTopology topology = tree.virtualTopology;
		if (!topology.childCountKnown (id)) tree.checkData (item, false);
		if (!topology.childCountKnown (id) || topology.childCount (id) == 0) return;
		topology.flag (id, VirtualItemState.EXPANDED, true);
		tree.virtualViewport.refreshLogicalRange ();
		layout ();
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
		long hItem = item.handle;
		releaseFacades (id);
		topology.removeCoordinate (parentId, index);
		if (hItem != 0) {
			boolean oldShrink = tree.ignoreShrink;
			tree.ignoreShrink = true;
			try {
				detachSubtree (hItem, 0);
				OS.SendMessage (tree.handle, OS.TVM_DELETEITEM, 0, hItem);
			} finally {
				tree.ignoreShrink = oldShrink;
			}
			plan.clear ();
			committedGeneration = -1;
			removeAllNative ();
		}
		tree.virtualViewport.refreshLogicalRange ();
		layout ();
	}

	/* The plan no longer describes the native rows: rebuild the window from scratch. */
	private void removeAllNative () {
		long tv = tree.handle;
		boolean oldShrink = tree.ignoreShrink, oldSelect = tree.ignoreSelect, oldDeselect = tree.ignoreDeselect;
		tree.ignoreShrink = tree.ignoreSelect = tree.ignoreDeselect = true;
		try {
			for (long hRoot = OS.SendMessage (tv, OS.TVM_GETNEXTITEM, OS.TVGN_ROOT, 0); hRoot != 0;
					hRoot = OS.SendMessage (tv, OS.TVM_GETNEXTITEM, OS.TVGN_ROOT, 0)) {
				detachSubtree (hRoot, 0);
				OS.SendMessage (tv, OS.TVM_DELETEITEM, 0, hRoot);
			}
		} finally {
			tree.ignoreShrink = oldShrink;
			tree.ignoreSelect = oldSelect;
			tree.ignoreDeselect = oldDeselect;
		}
	}

	/* ---------------------------------------------------------------- selection */

	/** Resident rows are the authority for their selection and the caret; copy them into the lanes. */
	private void captureResidentState () {
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
			if (item == null && (selected || hItem == caret)) {
				long key = plan.keyOf (entry);
				item = facade ((int) (key >> 32), (int) key);
			}
			int id = idOf (item);
			if (id < 0) continue;
			topology.flag (id, VirtualItemState.SELECTED, selected);
			if (hItem == caret) focusId = id;
		}
	}

	private void restoreFocus () {
		TreeItem item = facadeOrNull (focusId);
		if (item == null || item.handle == 0) return;
		long tv = tree.handle;
		if (OS.SendMessage (tv, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0) == item.handle) return;
		/* moving the caret selects natively in SINGLE mode: put the lane state back afterwards */
		boolean selected = tree.virtualTopology.flag (focusId, VirtualItemState.SELECTED);
		OS.SendMessage (tv, OS.TVM_SELECTITEM, OS.TVGN_CARET, item.handle);
		setNativeSelected (item.handle, selected);
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
		visitMaterialized (id -> {
			if (tree.virtualTopology.flag (id, VirtualItemState.SELECTED)) {
				TreeItem item = facadeOrNull (id);
				if (item != null) {
					tree.pinVirtualFacade (item);
					result.add (item);
				}
			}
		});
		return result.toArray (new TreeItem [0]);
	}

	int getSelectionCount () {
		captureResidentState ();
		int [] count = {0};
		visitMaterialized (id -> {
			if (tree.virtualTopology.flag (id, VirtualItemState.SELECTED)) count [0]++;
		});
		return count [0];
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
			tree.virtualTopology.flag (id, VirtualItemState.SELECTED, true);
			if (item.handle != 0) setNativeSelected (item.handle, true);
		}
		if (first != null) {
			focusId = idOf (first);
			showItem (first);
			restoreFocus ();
		}
	}

	void select (TreeItem item) {
		if ((tree.style & SWT.SINGLE) != 0) {
			setSelection (new TreeItem [] {item});
			return;
		}
		int id = idOf (item);
		if (id < 0) return;
		tree.virtualTopology.flag (id, VirtualItemState.SELECTED, true);
		if (item.handle != 0) setNativeSelected (item.handle, true);
	}

	void deselect (TreeItem item) {
		int id = idOf (item);
		if (id < 0) return;
		tree.virtualTopology.flag (id, VirtualItemState.SELECTED, false);
		if (item.handle != 0) setNativeSelected (item.handle, false);
	}

	void deselectAll () {
		visitMaterialized (id -> tree.virtualTopology.flag (id, VirtualItemState.SELECTED, false));
		for (int entry = 0; entry < plan.size (); entry++) setNativeSelected (plan.handle (entry), false);
	}

	private void visitMaterialized (java.util.function.IntConsumer visitor) {
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
			for (int child = topology.firstMaterializedChildId (id); child >= 0; child = topology.nextMaterializedSiblingId (child)) {
				children.add (child);
			}
			for (int i = children.size () - 1; i >= 0; i--) stack.push (children.get (i));
		}
	}
}
