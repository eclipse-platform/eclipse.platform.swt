/*******************************************************************************
 * Copyright (c) 2000, 2026 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.accessibility.gtk.*;
import org.eclipse.swt.internal.cairo.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.internal.gtk3.*;
import org.eclipse.swt.internal.gtk4.*;

/**
 * Instances of this class provide a selectable user interface object
 * that displays a hierarchy of items and issues notification when an
 * item in the hierarchy is selected.
 * <p>
 * The item children that may be added to instances of this class
 * must be of type <code>TreeItem</code>.
 * </p><p>
 * Style <code>VIRTUAL</code> is used to create a <code>Tree</code> whose
 * <code>TreeItem</code>s are to be populated by the client on an on-demand basis
 * instead of up-front.  This can provide significant performance improvements for
 * trees that are very large or for which <code>TreeItem</code> population is
 * expensive (for example, retrieving values from an external source).
 * </p><p>
 * Here is an example of using a <code>Tree</code> with style <code>VIRTUAL</code>:</p>
 * <pre><code>
 *  final Tree tree = new Tree(parent, SWT.VIRTUAL | SWT.BORDER);
 *  tree.setItemCount(20);
 *  tree.addListener(SWT.SetData, new Listener() {
 *      public void handleEvent(Event event) {
 *          TreeItem item = (TreeItem)event.item;
 *          TreeItem parentItem = item.getParentItem();
 *          String text = null;
 *          if (parentItem == null) {
 *              text = "node " + tree.indexOf(item);
 *          } else {
 *              text = parentItem.getText() + " - " + parentItem.indexOf(item);
 *          }
 *          item.setText(text);
 *          System.out.println(text);
 *          item.setItemCount(10);
 *      }
 *  });
 * </code></pre>
 * <p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not normally make sense to add <code>Control</code> children to
 * it, or set a layout on it, unless implementing something like a cell
 * editor.
 * </p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SINGLE, MULTI, CHECK, FULL_SELECTION, VIRTUAL, NO_SCROLL, NO_SEARCH</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection, DefaultSelection, Collapse, Expand, SetData, MeasureItem, EraseItem, PaintItem, EmptinessChanged</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles SINGLE and MULTI may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="https://eclipse.dev/eclipse/swt/snippets/#tree">Tree, TreeItem, TreeColumn snippets</a>
 * @see <a href="https://eclipse.dev/eclipse/swt/examples.html">SWT Example: ControlExample</a>
 * @see <a href="https://eclipse.dev/eclipse/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Tree extends Composite {
	long modelHandle, checkRenderer;
	long virtualViewModel, virtualViewAdjustment, verticalAdjustment;
	Callback virtualAdjustmentCallback;
	long virtualLogicalValueSignal, virtualViewRangeSignal, virtualViewValueSignal, virtualColumnsSignal;
	Map<TreeItem, Map<TreeColumn, java.lang.ref.WeakReference<Accessible>>> virtualAccessibleCells;
	Map<TreeColumn, Accessible> virtualAccessibleHeaders;
	Map<Accessible, Long> virtualAccessibleStates;
	long virtualAccessibleGeneration;
	int virtualAccessibleFocusId;
	boolean virtualAccessibleColumnsDirty;
	boolean virtualViewRangeDirty;
	int virtualRowExtent = 1, virtualBodyExtent;
	double virtualPixelRemainder;
	VirtualTreeVisibleProjection.Residency virtualResidency = new VirtualTreeVisibleProjection.Residency ();
	VirtualTreeVisibleProjection.Residency nextVirtualResidency = new VirtualTreeVisibleProjection.Residency ();
	final Map<Integer, VirtualSelectionModel> virtualSelections = new HashMap<> ();
	long virtualResidencyGeneration = -1;
	boolean reconcilingVirtualResidency, virtualResidencyScheduled, updatingVirtualAdjustment, virtualShapeChanged;
	int virtualCallbackDepth, virtualFocusId = -1, virtualAnchorId = -1;
	int columnCount, sortDirection;
	int selectionCountOnPress,selectionCountOnRelease;
	long ignoreCell;
	TreeItem[] items;
	VirtualTreeTopology virtualTopology;
	final Set<Integer> virtualFrontierPending = new HashSet<> ();
	VirtualTreeVisibleProjection virtualProjection;
	VirtualTreeViewport virtualViewport;
	VirtualNativeViewState pendingVirtualNativeViewState;
	boolean virtualNativeViewResetScheduled;
	Boolean virtualLogicalNativeModel;
	int nextId;
	TreeColumn [] columns;
	TreeColumn sortColumn;
	TreeItem currentItem;
	ImageList imageList, headerImageList;
	boolean firstCustomDraw;
	/** True iff computeSize has never been called on this Tree */
	boolean firstCompute = true;
	boolean modelChanged;
	boolean expandAll;
	int drawState, drawFlags;
	GdkRGBA background, foreground, drawForegroundRGBA;
	/** The owner of the widget is responsible for drawing */
	boolean isOwnerDrawn;
	boolean ignoreSize, pixbufSizeSet, hasChildren;
	int pixbufHeight, pixbufWidth, headerHeight;
	boolean headerVisible;
	TreeItem topItem;
	double cachedAdjustment, currentAdjustment;
	Color headerBackground, headerForeground;
	boolean boundsChangedSinceLastDraw, wasScrolled;
	ViewportLayerState viewportLayers;
	boolean defaultSelectionPending;
	/**
	 * True while {@link #removeAll()} clears the GTK model. GTK may call back
	 * into SWT (cell data function, renderer callbacks) during the clear with
	 * iterators of rows that are being removed. Such callbacks must not touch
	 * the model or send events to the application.
	 */
	boolean clearingModel;

	private long headerCSSProvider;

	static final int ID_COLUMN = 0;
	static final int CHECKED_COLUMN = 1;
	static final int GRAYED_COLUMN = 2;
	static final int FOREGROUND_COLUMN = 3;
	static final int BACKGROUND_COLUMN = 4;
	static final int FONT_COLUMN = 5;
	static final int FIRST_COLUMN = FONT_COLUMN + 1;
	static final int CELL_PIXBUF = 0;
	static final int CELL_TEXT = 1;
	static final int CELL_FOREGROUND = 2;
	static final int CELL_BACKGROUND = 3;
	static final int CELL_FONT = 4;
	static final int CELL_SURFACE = 5;
	static final int CELL_TYPES = CELL_SURFACE + 1;
	static final int VIRTUAL_BACKSLASH_KEY = 0x5c; // GDK printable keyval
	static final int VIRTUAL_FRONTIER_CHUNK = 256;
	static final int VIRTUAL_FRONTIER_TRIGGER = 32;
	static final String VIRTUAL_LOGICAL_NATIVE_MODEL_PROPERTY =
			"org.eclipse.swt.internal.gtk.virtualTreeLogicalNativeModel";
	static final byte [] VIRTUAL_MODEL_RESIDENCY = Converter.wcsToMbcs ("swt-residency", true);
	static final byte [] VIRTUAL_MODEL_FACADE = Converter.wcsToMbcs ("swt-facade", true);
	static final byte [] VIRTUAL_MODEL_TOPOLOGY =
			Converter.wcsToMbcs ("swt-topology", true);

	record VirtualNativeViewState (TreeItem [] selection, TreeItem focus, TreeItem top) {
	}

/**
 * Constructs a new instance of this class given its parent
 * and a style value describing its behavior and appearance.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p>
 *
 * @param parent a composite control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#SINGLE
 * @see SWT#MULTI
 * @see SWT#CHECK
 * @see SWT#FULL_SELECTION
 * @see SWT#VIRTUAL
 * @see SWT#NO_SCROLL
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Tree (Composite parent, int style) {
	super (parent, checkStyle (style));
}

@Override
void _addListener (int eventType, Listener listener) {
	super._addListener (eventType, listener);
	if (!isOwnerDrawn) {
		switch (eventType) {
			case SWT.MeasureItem:
			case SWT.EraseItem:
			case SWT.PaintItem:
				isOwnerDrawn = true;
				recreateRenderers ();
				break;
		}
	}
}

TreeItem _getItem (long iter) {
	if (usesBoundedVirtualView ()) {
		long path = GTK.gtk_tree_model_get_path (virtualViewModel, iter);
		if (path != 0) {
			try { return residentItem (residentEntry (path)); }
			finally { GTK.gtk_tree_path_free (path); }
		}
	}
	int id = getId (iter, true);
	ensureVirtualTopology (iter, id);
    if (items [id] != null) {
        return items [id];
    }
	long path = GTK.gtk_tree_model_get_path (modelHandle, iter);
	int depth = GTK.gtk_tree_path_get_depth (path);
	int [] indices = new int [depth];
	C.memmove (indices, GTK.gtk_tree_path_get_indices (path), 4*depth);
	long parentIter = 0;
	if (depth > 1) {
		GTK.gtk_tree_path_up (path);
		parentIter = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
		GTK.gtk_tree_model_get_iter (modelHandle, parentIter, path);
	}
	bindVirtualTopology (id, parentIter, indices [indices.length - 1]);
	items [id] = new TreeItem (this, parentIter, SWT.NONE, indices [indices.length -1], iter);
	if (virtualTopology != null) items [id].virtualId = id;
	GTK.gtk_tree_path_free (path);
    if (parentIter != 0) {
        OS.g_free(parentIter);
    }
	return items [id];
}

TreeItem _getItem (long parentIter, long iter, int index) {
	int id = getId (iter, true);
	if (virtualTopology != null && !virtualTopology.contains (id)) {
		bindVirtualTopology (id, parentIter, index);
	}
    if (items [id] != null) {
        return items [id];
    }
	TreeItem item = items [id] = new TreeItem (this, parentIter, SWT.NONE, index, iter);
	if (virtualTopology != null) item.virtualId = id;
	return item;
}

void reallocateIds(int newSize) {
	TreeItem [] newItems = new TreeItem [newSize];
	System.arraycopy (items, 0, newItems, 0, items.length);
	items = newItems;
}

int findAvailableId() {
    // Adapt to cases where items[] array was resized since last search
    // This also fixes cases where +1 below went too far
    if (nextId >= items.length) {
        nextId = 0;
    }

	// Search from 'nextId' to end
	for (int id = nextId; id < items.length; id++) {
        if (items [id] == null && (virtualTopology == null || !virtualTopology.contains (id))) {
            return id;
        }
	}

	// Search from begin to nextId
	for (int id = 0; id < nextId; id++) {
        if (items [id] == null && (virtualTopology == null || !virtualTopology.contains (id))) {
            return id;
        }
	}

	// Still not found; no empty spots remaining
	int newId = items.length;
	if (drawCount <= 0) {
		reallocateIds (items.length + 4);
	} else {
		// '.setRedraw(false)' is typically used during bulk operations.
		// Reallocate to 1.5x the old size to avoid frequent reallocations.
		reallocateIds ((items.length + 1) * 3 / 2);
	}

	return newId;
}

int getId (long iter, boolean queryModel) {
	if (usesBoundedVirtualView ()) {
		long path = GTK.gtk_tree_model_get_path (virtualViewModel, iter);
		if (path != 0) {
			try {
				TreeItem item = residentItem (residentEntry (path));
				return item == null ? -1 : item.virtualId;
			} finally { GTK.gtk_tree_path_free (path); }
		}
	}
	if (queryModel) {
		int[] value = new int[1];
		GTK.gtk_tree_model_get (modelHandle, iter, ID_COLUMN, value, -1);
		if (value [0] != -1) return value [0];
		if (usesVirtualNativeModel ()) {
			int existing = virtualMaterializedId (iter);
			if (existing >= 0) return existing;
		}
	}

	int id = findAvailableId();
	nextId = id + 1;
	if (usesVirtualNativeModel ()) {
		long path = GTK.gtk_tree_model_get_path (modelHandle, iter);
		if (path == 0) return id;
		try {
			int depth = GTK.gtk_tree_path_get_depth (path);
			if (depth <= 0) return id;
			int [] indices = new int [depth];
			C.memmove (indices, GTK.gtk_tree_path_get_indices (path), 4L * depth);
			int parentId = VirtualTreeTopology.ROOT;
			for (int level = 0; level < depth - 1; level++) {
				int parent = virtualTopology.materializedChildId (parentId, indices [level]);
				if (parent < 0) {
					parent = findAvailableId ();
					nextId = parent + 1;
					virtualTopology.bind (parent, parentId, indices [level]);
				}
				parentId = parent;
			}
			virtualTopology.bind (id, parentId, indices [depth - 1]);
		} finally {
			GTK.gtk_tree_path_free (path);
		}
		return id;
	}

	GTK.gtk_tree_store_set (modelHandle, iter, ID_COLUMN, id, -1);
	return id;
}

int virtualParentId (long parentIter) {
    if (virtualTopology == null || parentIter == 0) {
        return VirtualTreeTopology.ROOT;
    }
	int id = getId (parentIter, true);
	ensureVirtualTopology (parentIter, id);
	return id;
}

void ensureVirtualTopology (long iter, int id) {
    if (virtualTopology == null || virtualTopology.contains(id)) {
        return;
    }
	long path = GTK.gtk_tree_model_get_path (modelHandle, iter);
    if (path == 0) {
        return;
    }
	try {
		int depth = GTK.gtk_tree_path_get_depth (path);
        if (depth <= 0) {
            return;
        }
		int [] indices = new int [depth];
		C.memmove (indices, GTK.gtk_tree_path_get_indices (path), 4 * depth);
		int parentId = VirtualTreeTopology.ROOT;
		if (depth > 1) {
			GTK.gtk_tree_path_up (path);
			long parentIter = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
            if (parentIter == 0) {
                error(SWT.ERROR_NO_HANDLES);
            }
			try {
				if (GTK.gtk_tree_model_get_iter (modelHandle, parentIter, path)) {
					parentId = getId (parentIter, true);
					ensureVirtualTopology (parentIter, parentId);
				}
			} finally {
				OS.g_free (parentIter);
			}
		}
		virtualTopology.bind (id, parentId, indices [depth - 1]);
	} finally {
		GTK.gtk_tree_path_free (path);
	}
}

void bindVirtualTopology (int id, long parentIter, int childIndex) {
    if (virtualTopology == null) {
        return;
    }
	virtualTopology.bind (id, virtualParentId (parentIter), childIndex);
}

int virtualChildCount (long parentIter) {
    if (virtualTopology == null) {
        return GTK.gtk_tree_model_iter_n_children(modelHandle, parentIter);
    }
	int parentId = virtualParentId (parentIter);
    if (virtualTopology.childCountKnown(parentId)) {
        return virtualTopology.childCount(parentId);
    }
	int count = GTK.gtk_tree_model_iter_n_children (modelHandle, parentIter);
	virtualTopology.setChildCount (parentId, count);
	return count;
}

int virtualChildCount (TreeItem parentItem) {
	if (virtualTopology != null && parentItem != null) {
		int parentId = virtualItemId (parentItem);
		if (virtualTopology.childCountKnown (parentId)) return virtualTopology.childCount (parentId);
	}
	return virtualChildCount (parentItem == null ? 0 : parentItem.handle);
}

long virtualVisibleRowCount () {
	return virtualProjection != null ? virtualProjection.visibleRowCount () : 0;
}

/* M3RowMath seam: no row lanes on this platform yet; M3RowMath walks the public API */
long m3VisibleRowCount () {
	return M3RowMath.UNKNOWN;
}

long m3RowOf (TreeItem item) {
	return M3RowMath.UNKNOWN;
}

TreeItem m3ItemAt (long row) {
	return null;
}

long m3SubtreeRowCount (TreeItem item) {
	return M3RowMath.UNKNOWN;
}

/* M3Residency seam: no residency lanes on this platform; every visible row is resident, facades unknown */
long [] m3ResidentRange () {
	return null;
}

long m3Facades (boolean held) {
	return -1;
}

VirtualTreeVisibleProjection.Row [] virtualVisibleWindow (long firstVisible, int rowCount) {
	return virtualProjection != null
			? virtualProjection.window (firstVisible, rowCount)
			: new VirtualTreeVisibleProjection.Row [0];
}

int virtualItemId (TreeItem item) {
    if (virtualTopology == null) {
        return -1;
    }
	if (item.virtualId >= 0) return item.virtualId;
	int id = getId (item.handle, true);
	ensureVirtualTopology (item.handle, id);
	item.virtualId = id;
	return id;
}

boolean virtualFlag (TreeItem item, long flag) {
    if (virtualTopology == null) {
        return false;
    }
	return virtualTopology.flag (virtualItemId (item), flag);
}

void virtualFlag (TreeItem item, long flag, boolean value) {
    if (virtualTopology == null) {
        return;
    }
	int id = virtualItemId (item);
	boolean changed = virtualTopology.flag (id, flag) != value;
	if (!changed) return; // Never invent listeners or redraw for an idempotent setter.
	boolean shape = flag == VirtualItemState.EXPANDED;
	virtualTopology.flag (id, flag, value);
	if (usesBoundedVirtualView ()) notifyVirtualAccessibleItemStates (item);
	if (shape && usesBoundedVirtualView ()) {
		virtualShapeChanged = true;
		if (!value) clearVirtualDescendantSelection (id);
		scheduleVirtualResidency ();
	}
}

void pinVirtualFacade (TreeItem item) {
	virtualFlag (item, VirtualItemState.PINNED, true);
}

TreeItem exposeVirtualItem (TreeItem item) {
    if (item != null && virtualTopology != null) {
        pinVirtualFacade(item);
    }
	return item;
}

void ensureVirtualNativeChildren (long parentIter, int requiredExclusive) {
	if (virtualTopology == null) return;
	if (usesVirtualNativeModel ()) return;
	int parentId = virtualParentId (parentIter);
	int logicalCount = virtualTopology.childCountKnown (parentId)
			? virtualTopology.childCount (parentId)
			: GTK.gtk_tree_model_iter_n_children (modelHandle, parentIter);
	int target = Math.min (logicalCount, Math.max (0, requiredExclusive));
	int resident = GTK.gtk_tree_model_iter_n_children (modelHandle, parentIter);
	if (target <= resident) return;
	long iter = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
	if (iter == 0) error (SWT.ERROR_NO_HANDLES);
	long anchor = 0;
	try {
		if (resident != 0) {
			anchor = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
			if (anchor == 0) error (SWT.ERROR_NO_HANDLES);
			GTK.gtk_tree_model_iter_nth_child (modelHandle, anchor, parentIter, resident - 1);
		}
		for (int i = resident; i < target; i++) {
			GTK.gtk_tree_store_insert_after (modelHandle, iter, parentIter, anchor);
			GTK.gtk_tree_store_set (modelHandle, iter, ID_COLUMN, -1, -1);
		}
	} finally {
		if (anchor != 0) OS.g_free (anchor);
		OS.g_free (iter);
	}
}

void ensureVirtualNativeItem (long parentIter, int index) {
	if (virtualTopology == null) return;
	int logicalCount = virtualChildCount (parentIter);
	if (!(0 <= index && index < logicalCount)) error (SWT.ERROR_INVALID_RANGE);
	if (!usesVirtualNativeModel ()) ensureVirtualNativeChildren (parentIter, index + 1);
}

void restoreVirtualChildren (TreeItem item) {
	if (virtualTopology == null || item == null || item.isDisposed ()) return;
	if (usesVirtualNativeModel ()) return;
	int id = virtualItemId (item);
	if (!virtualTopology.childCountKnown (id)) return;
	int logicalCount = virtualTopology.childCount (id);
	int resident = GTK.gtk_tree_model_iter_n_children (modelHandle, item.handle);
	int target = Math.min (logicalCount, Math.max (resident, VIRTUAL_FRONTIER_CHUNK));
	ensureVirtualNativeChildren (item.handle, target);
}

void requestVirtualFrontier (TreeItem item) {
	if (usesVirtualNativeModel ()) return;
	if (virtualTopology == null || item == null || item.isDisposed ()) return;
	int itemId = virtualItemId (item);
	int parentId = virtualTopology.parentId (itemId);
	if (!virtualTopology.childCountKnown (parentId)) return;
	int logicalCount = virtualTopology.childCount (parentId);
	long parentIter = 0;
	if (parentId != VirtualTreeTopology.ROOT) {
		if (parentId >= items.length) return;
		TreeItem parentItem = items [parentId];
		if (parentItem == null || parentItem.isDisposed ()) return;
		parentIter = parentItem.handle;
	}
	int resident = GTK.gtk_tree_model_iter_n_children (modelHandle, parentIter);
	if (resident >= logicalCount) return;
	if (virtualTopology.childIndex (itemId) < Math.max (0, resident - VIRTUAL_FRONTIER_TRIGGER)) return;
	int key = parentId + 1;
	if (!virtualFrontierPending.add (key)) return;
	display.asyncExec (() -> {
		virtualFrontierPending.remove (key);
		if (isDisposed () || item.isDisposed () || virtualTopology == null) return;
		if (!virtualTopology.childCountKnown (parentId)) return;
		long currentParent = 0;
		if (parentId != VirtualTreeTopology.ROOT) {
			if (parentId >= items.length) return;
			TreeItem parentItem = items [parentId];
			if (parentItem == null || parentItem.isDisposed () || !parentItem.getExpanded ()) return;
			currentParent = parentItem.handle;
		}
		int current = GTK.gtk_tree_model_iter_n_children (modelHandle, currentParent);
		ensureVirtualNativeChildren (
				currentParent, Math.min (virtualTopology.childCount (parentId), current + VIRTUAL_FRONTIER_CHUNK));
	});
}

int virtualResidentChildCount (long parentIter) {
	if (!usesVirtualNativeModel ()) return GTK.gtk_tree_model_iter_n_children (modelHandle, parentIter);
	int parentId = virtualParentId (parentIter);
	int count = 0;
	for (int id = virtualTopology.firstMaterializedChildId (parentId);
			id >= 0; id = virtualTopology.nextMaterializedSiblingId (id)) count++;
	return count;
}

int virtualResidentChildCount (TreeItem parentItem) {
	return virtualResidentChildCount (parentItem == null ? 0 : parentItem.handle);
}

void scheduleVirtualCollapseCompaction (TreeItem item) {
	if (usesVirtualNativeModel ()) return;
	if (virtualTopology == null || item == null || item.isDisposed ()) return;
	display.asyncExec (() -> {
		if (isDisposed () || item.isDisposed () || item.getExpanded ()) return;
		compactCollapsedVirtualChildren (item);
	});
}

void compactCollapsedVirtualChildren (TreeItem item) {
	if (usesVirtualNativeModel ()) return;
    if (virtualTopology == null || item == null || item.isDisposed() || item.getExpanded()) {
        return;
    }
	int parentId = virtualItemId (item);
    if (!virtualTopology.childCountKnown(parentId)) {
        return;
    }
	int logicalCount = virtualTopology.childCount (parentId);
	int resident = GTK.gtk_tree_model_iter_n_children (modelHandle, item.handle);
    if (resident == 0) {
        return;
    }

	int highestPinned = virtualTopology.highestChildIndexWithSubtreeFlag (
			parentId, VirtualItemState.PINNED);
	int keep = logicalCount == 0 ? 0 : Math.max (1, highestPinned + 1);
	keep = Math.min (keep, resident);
    if (keep >= resident) {
        return;
    }

	long selection = GTK.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (
			selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	long iter = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
    if (iter == 0) {
        error(SWT.ERROR_NO_HANDLES);
    }
	try {
		for (int index = resident - 1; index >= keep; index--) {
            if (!GTK.gtk_tree_model_iter_nth_child(modelHandle, iter, item.handle, index)) {
                continue;
            }
			int [] value = new int [1];
			GTK.gtk_tree_model_get (modelHandle, iter, ID_COLUMN, value, -1);
			int id = value [0];
			if (id >= 0 && virtualTopology.contains (id)) {
				releaseItems (iter);
				TreeItem child = id < items.length ? items [id] : null;
                if (child != null && !child.isDisposed()) {
                    releaseItem(child, true);
                }
				virtualTopology.forgetSubtree (id);
			}
			GTK.gtk_tree_store_remove (modelHandle, iter);
		}
	} finally {
		OS.g_free (iter);
		OS.g_signal_handlers_unblock_matched (
				selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	}
}

void updateVirtualLogicalAdjustment () {
	if (verticalAdjustment == 0 || virtualViewport == null) return;
	boolean scaled = virtualViewport.usesScaledNativeScrollbar ();
	double page = scaled ? virtualViewport.scrollbarThumb () : Math.max (0, virtualBodyExtent);
	double upper = scaled ? virtualViewport.scrollbarMaximum ()
			: Math.max (page, virtualViewport.visibleRowCount () * (double)virtualRowExtent);
	double value = scaled ? virtualViewport.scrollbarSelection ()
			: virtualViewport.topRow () * (double)virtualRowExtent + virtualPixelRemainder;
	value = Math.max (0, Math.min (value, upper - page));
	boolean previous = updatingVirtualAdjustment;
	updatingVirtualAdjustment = true;
	OS.g_signal_handlers_block_matched (verticalAdjustment, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
	try {
		GTK.gtk_adjustment_configure (verticalAdjustment, value, 0, upper,
				scaled ? 1 : virtualRowExtent,
				scaled ? Math.max (1, virtualViewport.scrollbarPageIncrement ())
					: virtualViewport.scrollbarPageIncrement () * (double)virtualRowExtent,
				page);
		long horizontal = GTK.gtk_scrolled_window_get_hadjustment (scrolledHandle);
		/* Paint planes track logical pixel origin, never scaled scrollbar units. */
		double logicalOrigin = virtualViewport.topRow () * (double)virtualRowExtent
				+ virtualPixelRemainder;
		int dirty = viewportLayers.scrollTo (
				horizontal != 0 ? GTK.gtk_adjustment_get_value (horizontal) : 0, logicalOrigin);
		if ((dirty & ViewportLayerState.HEADER) != 0) wasScrolled = true;
	} finally {
		OS.g_signal_handlers_unblock_matched (verticalAdjustment, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
		updatingVirtualAdjustment = previous;
	}
}

void alignVirtualViewAdjustment () {
	if (virtualViewAdjustment == 0 || virtualResidency.paintCount () == 0) return;
	int offset = Math.toIntExact (virtualViewport.topRow () - virtualResidency.firstPaintRow ());
	if (offset < 0 || offset >= virtualResidency.paintCount ()) return;
	/* Real residency entries are the physical visible preorder, including the
	 * ancestor carriers preceding paint. Expander hints follow the paint range. */
	int entry = virtualResidency.paintEntry (offset);
	double value = entry * (double)virtualRowExtent + virtualPixelRemainder;
	boolean previous = updatingVirtualAdjustment;
	updatingVirtualAdjustment = true;
	try {
		GTK.gtk_adjustment_set_value (virtualViewAdjustment, value);
		virtualViewRangeDirty = false;
	} finally {
		updatingVirtualAdjustment = previous;
	}
}

long virtualAdjustmentProc (long adjustment, long kind) {
	if (kind == 4) {
		virtualAccessibleColumnsDirty = true;
		if (!isDisposed ()) scheduleVirtualResidency ();
		return 0;
	}
	if (isDisposed () || virtualViewport == null || updatingVirtualAdjustment || reconcilingVirtualResidency) return 0;
	if (kind == 1) {
		double position = GTK.gtk_adjustment_get_value (verticalAdjustment);
		if (virtualViewport.usesScaledNativeScrollbar ()) {
			/* The native slider is a projection, never the owner of long row IDs. */
			int selection = (int)Math.max (0d, Math.min ((double)Integer.MAX_VALUE,
					Math.rint (position)));
			virtualViewport.setScrollbarSelection (selection);
			virtualPixelRemainder = 0;
		} else {
			long requested = (long)Math.floor (position / virtualRowExtent);
			virtualViewport.setTopRow (requested);
			virtualPixelRemainder = virtualViewport.topRow () == requested
					? position - requested * (double)virtualRowExtent : 0;
		}
		topItem = null;
		cachedAdjustment = Double.NaN;
	} else if (kind == 2) {
		virtualViewRangeDirty = true;
	} else if (!virtualViewRangeDirty) {
		/* Native DND/autoscroll changes the private physical adjustment. Translate
		 * its top row through the resident mapping before updating logical scroll. */
		long [] path = new long [1];
		if (GTK.gtk_tree_view_get_path_at_pos (handle, 1, 1, path, null, null, null) && path [0] != 0) {
			try {
				int entry = residentEntry (path [0]);
				if (entry >= 0 && !virtualResidency.isHint (entry)) {
					long row = virtualResidency.visibleRow (entry);
					if (row >= 0) {
						virtualPixelRemainder = virtualViewport.usesScaledNativeScrollbar () ? 0
							: GTK.gtk_adjustment_get_value (virtualViewAdjustment) % virtualRowExtent;
						if (virtualViewport.usesScaledNativeScrollbar ()) {
							virtualViewport.setTopRow (row);
							updateVirtualLogicalAdjustment ();
						} else {
							GTK.gtk_adjustment_set_value (verticalAdjustment,
									row * (double)virtualRowExtent + virtualPixelRemainder);
						}
					}
				}
			} finally {
				GTK.gtk_tree_path_free (path [0]);
			}
		}
	}
	scheduleVirtualResidency ();
	return 0;
}

void disconnectVirtualAdjustments () {
	if (virtualColumnsSignal != 0) OS.g_signal_handler_disconnect (handle, virtualColumnsSignal);
	virtualColumnsSignal = 0;
	if (virtualLogicalValueSignal != 0) OS.g_signal_handler_disconnect (verticalAdjustment, virtualLogicalValueSignal);
	if (virtualViewRangeSignal != 0) OS.g_signal_handler_disconnect (virtualViewAdjustment, virtualViewRangeSignal);
	if (virtualViewValueSignal != 0) OS.g_signal_handler_disconnect (virtualViewAdjustment, virtualViewValueSignal);
	virtualLogicalValueSignal = virtualViewRangeSignal = virtualViewValueSignal = 0;
	if (virtualAdjustmentCallback != null) virtualAdjustmentCallback.dispose ();
	virtualAdjustmentCallback = null;
}


void initializeVirtualAccessible () {
	if (!usesBoundedVirtualView () || accessible != null) return;
	virtualAccessibleCells = new WeakHashMap<> ();
	virtualAccessibleHeaders = new HashMap<> ();
	virtualAccessibleStates = new WeakHashMap<> ();
	virtualAccessibleGeneration = -1;
	virtualAccessibleFocusId = -1;
	accessible = Accessible.internal_new_Accessible (this, fixedHandle);
	accessible.internal_setNativeRole (ATK.ATK_ROLE_TREE_TABLE);
	accessible.internal_setManagesDescendants (true);
	accessible.internal_setTableHeaderRows (1);
	accessible.internal_setSelectedRowHandler (this::virtualAccessibleSelectedRowAt);
	accessible.internal_setFocusHandler (this::setFocus);
	accessible.internal_setSelectionHandlers (() -> { deselectAll (); return true; }, () -> { selectAll (); return true; });
	accessible.addAccessibleControlListener (new AccessibleControlAdapter () {
		@Override public void getRole (AccessibleControlEvent event) { event.detail = ACC.ROLE_TREE; }
		@Override public void getChildCount (AccessibleControlEvent event) {
			event.detail = Math.toIntExact ((virtualAccessibleRowCount () + 1L) * virtualAccessibleColumnCount ());
		}
		@Override public void getChild (AccessibleControlEvent event) {
			int index = event.childID == ACC.CHILDID_CHILD_AT_INDEX ? event.detail : event.childID;
			int columns = virtualAccessibleColumnCount ();
			if (index < 0 || columns == 0) return;
			if (index < columns) event.accessible = virtualAccessibleHeader (index);
			else {
				int row = index / columns - 1;
				if (row < virtualAccessibleRowCount ()) event.accessible = virtualAccessibleCell (row, index % columns);
			}
		}
		@Override public void getChildAtPoint (AccessibleControlEvent event) {
			Point point = toControl (event.x, event.y);
			long [] path = new long [1], column = new long [1];
			if (!GTK.gtk_tree_view_get_path_at_pos (handle, point.x, point.y - getHeaderHeight (), path, column, null, null)
					|| path [0] == 0) { event.childID = ACC.CHILDID_SELF; return; }
			try {
				TreeItem item = residentItem (residentEntry (path [0]));
				if (item != null) event.accessible = virtualAccessibleCell (item, virtualAccessibleColumnFromHandle (column [0]));
			} finally { GTK.gtk_tree_path_free (path [0]); }
		}
		@Override public void getFocus (AccessibleControlEvent event) {
			TreeItem focus = getFocusItem ();
			if (focus != null) event.accessible = virtualAccessibleCell (focus, 0);
			else event.childID = ACC.CHILDID_SELF;
		}
		@Override public void getState (AccessibleControlEvent event) {
			event.detail = ACC.STATE_READONLY | ACC.STATE_FOCUSABLE | ((style & SWT.MULTI) != 0 ? ACC.STATE_MULTISELECTABLE : 0);
			if (!getEnabled ()) event.detail |= ACC.STATE_DISABLED;
			if (!getVisible ()) event.detail |= ACC.STATE_INVISIBLE | ACC.STATE_OFFSCREEN;
			if (isFocusControl ()) event.detail |= ACC.STATE_FOCUSED;
		}
	});
	accessible.addAccessibleTableListener (new AccessibleTableAdapter () {
		@Override public void getRowCount (AccessibleTableEvent event) { event.count = virtualAccessibleRowCount (); }
		@Override public void getColumnCount (AccessibleTableEvent event) { event.count = virtualAccessibleColumnCount (); }
		@Override public void getCell (AccessibleTableEvent event) {
			if (event.row >= 0 && event.row < virtualAccessibleRowCount () && event.column >= 0
					&& event.column < virtualAccessibleColumnCount ()) event.accessible = virtualAccessibleCell (event.row, event.column);
		}
		@Override public void getColumnHeaderCells (AccessibleTableEvent event) {
			event.accessibles = new Accessible [virtualAccessibleColumnCount ()];
			for (int column = 0; column < event.accessibles.length; column++) event.accessibles [column] = virtualAccessibleHeader (column);
		}
		@Override public void getSelectedRowCount (AccessibleTableEvent event) { event.count = virtualAccessibleSelectionCount (); }
		@Override public void getSelectedRows (AccessibleTableEvent event) { event.selected = virtualAccessibleSelectedRows (); }
		@Override public void isRowSelected (AccessibleTableEvent event) {
			if (event.row < 0 || event.row >= virtualAccessibleRowCount ()) return;
			VirtualTreeVisibleProjection.Row row = virtualProjection.rowAt (event.row);
			event.isSelected = virtualSelected (row.parentId (), row.childIndex ());
		}
		@Override public void selectRow (AccessibleTableEvent event) {
			if (event.row < 0 || event.row >= virtualAccessibleRowCount ()) return;
			virtualSelectItem (virtualVisibleItem (event.row), true);
			event.result = ACC.OK;
		}
		@Override public void deselectRow (AccessibleTableEvent event) {
			if (event.row < 0 || event.row >= virtualAccessibleRowCount ()) return;
			VirtualTreeVisibleProjection.Row row = virtualProjection.rowAt (event.row);
			virtualSelection (row.parentId ()).setSelected (row.childIndex (), false);
			restoreVirtualSelection ();
			notifyVirtualAccessibleSelection ();
			event.result = ACC.OK;
		}
	});
}

@Override
Accessible _getAccessible () {
	if (usesBoundedVirtualView ()) initializeVirtualAccessible ();
	return super._getAccessible ();
}

int virtualAccessibleColumnCount () {
	int visible = 0;
	for (int column = 0; column < columnCount; column++) {
		if (GTK.gtk_tree_view_column_get_visible (columns [column].handle)) visible++;
	}
	return columnCount == 0 ? 1 : visible;
}

int virtualAccessibleRowCount () {
	/* ATK uses signed int child/row indices. Keep its row/cell range consistent
	 * while the Java visible preorder and scrollbar retain long coordinates. */
	int columns = virtualAccessibleColumnCount ();
	long maximum = columns == 0 ? Integer.MAX_VALUE : (Integer.MAX_VALUE - (long)columns) / columns;
	return (int)Math.min (virtualProjection.visibleRowCount (), maximum);
}

int virtualAccessibleVisualColumn (TreeColumn column) {
	if (column == null) return columnCount == 0 ? 0 : -1;
	if (column.isDisposed ()) return -1;
	int visual = 0;
	for (int index : getColumnOrder ()) {
		if (!GTK.gtk_tree_view_column_get_visible (columns [index].handle)) continue;
		if (columns [index] == column) return visual;
		visual++;
	}
	return -1;
}

int virtualAccessibleChildIndex (TreeItem item, TreeColumn column) {
	if (item.isDisposed ()) return -1;
	int visual = virtualAccessibleVisualColumn (column);
	long row = virtualProjection.visibleIndexOf (virtualItemId (item));
	if (visual < 0 || row < 0 || row >= virtualAccessibleRowCount ()) return -1;
	return Math.toIntExact ((row + 1) * virtualAccessibleColumnCount () + visual);
}

int virtualAccessibleModelColumn (int visualColumn) {
	if (columnCount == 0) return 0;
	int [] order = getColumnOrder ();
	int visible = 0;
	for (int index : order) {
		if (GTK.gtk_tree_view_column_get_visible (columns [index].handle)) {
			if (visible++ == visualColumn) return index;
		}
	}
	return 0;
}

int virtualAccessibleColumnFromHandle (long nativeColumn) {
	int visible = 0;
	for (int index : getColumnOrder ()) {
		if (!GTK.gtk_tree_view_column_get_visible (columns [index].handle)) continue;
		if (columns [index].handle == nativeColumn) return visible;
		visible++;
	}
	return 0;
}

Accessible virtualAccessibleHeader (int visualColumn) {
	pruneVirtualAccessibleColumns ();
	int modelColumn = virtualAccessibleModelColumn (visualColumn);
	TreeColumn column = columnCount == 0 ? null : columns [modelColumn];
	Accessible header = virtualAccessibleHeaders.get (column);
	if (header != null) return header;
	header = new Accessible (accessible);
	header.addAccessibleListener (new AccessibleAdapter () {
		@Override public void getName (AccessibleEvent event) {
			event.result = column != null && !column.isDisposed () ? column.getText () : "";
		}
	});
	header.addAccessibleControlListener (new AccessibleControlAdapter () {
		@Override public void getRole (AccessibleControlEvent event) { event.detail = ACC.ROLE_TABLECOLUMNHEADER; }
		@Override public void getChildCount (AccessibleControlEvent event) { event.detail = 0; }
		@Override public void getChild (AccessibleControlEvent event) {
			if (event.childID == ACC.CHILDID_CHILD_INDEX) event.detail = virtualAccessibleVisualColumn (column);
		}
		@Override public void getState (AccessibleControlEvent event) {
			event.detail = ACC.STATE_READONLY | (getHeaderVisible () && getVisible () ? 0 : ACC.STATE_INVISIBLE | ACC.STATE_OFFSCREEN);
		}
		@Override public void getLocation (AccessibleControlEvent event) {
			if (!getHeaderVisible () || virtualAccessibleVisualColumn (column) < 0) {
				event.x = event.y = Integer.MIN_VALUE;
				return;
			}
			GdkRectangle area = new GdkRectangle ();
			long nativeColumn = column != null ? column.handle : GTK.gtk_tree_view_get_column (handle, 0);
			GTK.gtk_tree_view_get_cell_area (handle, 0, nativeColumn, area);
			Point point = toDisplay (area.x, 0);
			event.x = point.x;
			event.y = point.y;
			event.width = column != null ? column.getWidth () : area.width;
			event.height = getHeaderHeight ();
		}
	});
	if (column != null) header.addAccessibleActionListener (new AccessibleActionAdapter () {
		@Override public void getActionCount (AccessibleActionEvent event) { event.count = column.isDisposed () ? 0 : 1; }
		@Override public void getName (AccessibleActionEvent event) { if (event.index == 0) event.result = "click"; }
		@Override public void doAction (AccessibleActionEvent event) {
			if (event.index == 0 && !column.isDisposed () && getEnabled ()) {
				column.sendSelectionEvent (SWT.Selection);
				event.result = ACC.OK;
			}
		}
	});
	virtualAccessibleHeaders.put (column, header);
	return header;
}

Accessible virtualAccessibleCell (long row, int visualColumn) {
	return virtualAccessibleCell (virtualVisibleItem (row), visualColumn);
}

Accessible virtualAccessibleCell (TreeItem item, int visualColumn) {
	if (item == null || item.isDisposed () || visualColumn < 0 || visualColumn >= virtualAccessibleColumnCount ()) return null;
	int modelColumn = virtualAccessibleModelColumn (visualColumn);
	TreeColumn column = columnCount == 0 ? null : columns [modelColumn];
	Map<TreeColumn, java.lang.ref.WeakReference<Accessible>> cells = virtualAccessibleCells.computeIfAbsent (item, ignored -> new HashMap<> ());
	java.lang.ref.WeakReference<Accessible> reference = cells.get (column);
	Accessible cell = reference != null ? reference.get () : null;
	if (cell != null) return cell;
	cell = Accessible.internal_new_AccessibleChild (accessible);
	cell.internal_setNativeRole (ATK.ATK_ROLE_TABLE_CELL);
	cell.internal_setNativeStates (() -> virtualAccessibleCheckStates (item));
	cell.internal_setFocusHandler (() -> {
		if (item.isDisposed ()) return false;
		virtualFocusId = virtualAnchorId = virtualItemId (item);
		revealVirtualItem (item, false);
		if (isDisposed () || item.isDisposed ()) return false;
		restoreVirtualFocus ();
		boolean focused = setFocus ();
		notifyVirtualAccessibleFocus ();
		return focused;
	});
	cell.internal_setScrollHandlers (type -> virtualAccessibleScrollTo (item, column, type),
			point -> virtualAccessibleScrollToPoint (item, column, point));
	cell.internal_setNodeParent (() -> {
		if (item.isDisposed ()) return null;
		int parentId = virtualTopology.parentId (virtualItemId (item));
		return parentId == VirtualTreeTopology.ROOT ? accessible :
				virtualAccessibleCell (virtualCoordinateItem (virtualTopology.parentId (parentId), virtualTopology.childIndex (parentId)), 0);
	});
	cell.addAccessibleListener (new AccessibleAdapter () {
		@Override public void getName (AccessibleEvent event) {
			int index = virtualAccessibleItemColumn (column);
			event.result = !item.isDisposed () && checkData (item) ? item._getText (index) : "";
		}
	});
	cell.addAccessibleControlListener (new AccessibleControlAdapter () {
		@Override public void getRole (AccessibleControlEvent event) { event.detail = ACC.ROLE_TABLECELL; }
		@Override public void getChildCount (AccessibleControlEvent event) { event.detail = 0; }
		@Override public void getChild (AccessibleControlEvent event) {
			if (event.childID == ACC.CHILDID_CHILD_INDEX) event.detail = virtualAccessibleChildIndex (item, column);
		}
		@Override public void getState (AccessibleControlEvent event) {
			event.detail = ACC.STATE_READONLY | ACC.STATE_SELECTABLE | ACC.STATE_FOCUSABLE;
			if (item.isDisposed ()) { event.detail |= ACC.STATE_INVISIBLE | ACC.STATE_OFFSCREEN | ACC.STATE_DISABLED; return; }
			int id = virtualItemId (item);
			if (virtualSelected (virtualTopology.parentId (id), virtualTopology.childIndex (id))) event.detail |= ACC.STATE_SELECTED;
			if (id == virtualFocusId && isFocusControl ()) event.detail |= ACC.STATE_FOCUSED;
			if (virtualChildCount (item) > 0) event.detail |= item.isExpandedState () ? ACC.STATE_EXPANDED : ACC.STATE_COLLAPSED;
			if ((style & SWT.CHECK) != 0 && item.isCheckedState ()) event.detail |= ACC.STATE_CHECKED;
			long row = virtualProjection.visibleIndexOf (id);
			if (row < 0) event.detail |= ACC.STATE_INVISIBLE | ACC.STATE_OFFSCREEN;
			else if (row < virtualViewport.topRow () || row >= virtualViewport.topRow () + virtualViewport.visibleRows ()) event.detail |= ACC.STATE_OFFSCREEN;
			if (!getEnabled ()) event.detail |= ACC.STATE_DISABLED;
			if (virtualAccessibleVisualColumn (column) < 0) event.detail |= ACC.STATE_INVISIBLE | ACC.STATE_OFFSCREEN;
		}
		@Override public void getLocation (AccessibleControlEvent event) {
			if (item.isDisposed ()) return;
			Rectangle bounds = item.getBounds (virtualAccessibleItemColumn (column));
			long row = virtualProjection.visibleIndexOf (virtualItemId (item));
			event.width = bounds.width;
			event.height = bounds.height;
			if (row < virtualViewport.topRow () || row >= virtualViewport.topRow () + virtualViewport.visibleRows ()) {
				event.x = event.y = Integer.MIN_VALUE;
			} else {
				Point screen = toDisplay (bounds.x, bounds.y + getHeaderHeight ());
				event.x = screen.x;
				event.y = screen.y;
			}
		}
	});
	cell.addAccessibleTableCellListener (new AccessibleTableCellAdapter () {
		@Override public void getColumnIndex (AccessibleTableCellEvent event) { event.index = virtualAccessibleVisualColumn (column); }
		@Override public void getRowIndex (AccessibleTableCellEvent event) {
			long row = item.isDisposed () ? -1 : virtualProjection.visibleIndexOf (virtualItemId (item));
			event.index = row >= 0 && row < virtualAccessibleRowCount () ? (int)row : -1;
		}
		@Override public void getColumnSpan (AccessibleTableCellEvent event) { event.count = 1; }
		@Override public void getRowSpan (AccessibleTableCellEvent event) { event.count = 1; }
		@Override public void getTable (AccessibleTableCellEvent event) { event.accessible = accessible; }
		@Override public void getColumnHeaders (AccessibleTableCellEvent event) {
			int index = virtualAccessibleVisualColumn (column);
			event.accessibles = index >= 0 ? new Accessible [] {virtualAccessibleHeader (index)} : new Accessible [0];
		}
		@Override public void getRowHeaders (AccessibleTableCellEvent event) { event.accessibles = new Accessible [0]; }
		@Override public void isSelected (AccessibleTableCellEvent event) {
			if (item.isDisposed ()) return;
			int id = virtualItemId (item);
			event.isSelected = virtualSelected (virtualTopology.parentId (id), virtualTopology.childIndex (id));
		}
	});
	cell.addAccessibleActionListener (new AccessibleActionAdapter () {
		@Override public void getActionCount (AccessibleActionEvent event) {
			event.count = item.isDisposed () ? 0 : 1 + ((style & SWT.CHECK) != 0 ? 1 : 0) + (virtualChildCount (item) > 0 ? 1 : 0);
		}
		@Override public void getName (AccessibleActionEvent event) {
			if (event.index == 0) event.result = "activate";
			else if (event.index == 1 && (style & SWT.CHECK) != 0) event.result = "toggle";
			else if (event.index == ((style & SWT.CHECK) != 0 ? 2 : 1)) event.result = "expand or contract";
		}
		@Override public void doAction (AccessibleActionEvent event) {
			if (item.isDisposed () || !getEnabled () || !checkData (item)) return;
			if (event.index == 0) {
				Event selection = new Event ();
				selection.item = exposeVirtualItem (item);
				sendSelectionEvent (SWT.DefaultSelection, selection, false);
			} else if (event.index == 1 && (style & SWT.CHECK) != 0) {
				item.setChecked (!item.getChecked ());
				Event selection = new Event ();
				selection.item = exposeVirtualItem (item);
				selection.detail = SWT.CHECK;
				sendSelectionEvent (SWT.Selection, selection, false);
			} else if (event.index == ((style & SWT.CHECK) != 0 ? 2 : 1) && virtualChildCount (item) > 0) {
				boolean expanded = item.isExpandedState ();
				Event expansion = new Event ();
				expansion.item = exposeVirtualItem (item);
				virtualCallbackDepth++;
				try {
					sendEvent (expanded ? SWT.Collapse : SWT.Expand, expansion);
					if (!isDisposed () && !item.isDisposed ()) item.setExpandedState (!expanded);
				} finally { virtualCallbackDepth--; }
				if (!isDisposed ()) scheduleVirtualResidency ();
			} else return;
			event.result = ACC.OK;
		}
	});
	virtualAccessibleStates.put (cell, virtualAccessibleNativeStates (item, column));
	cells.put (column, new java.lang.ref.WeakReference<> (cell));
	return cell;
}

int virtualAccessibleItemColumn (TreeColumn column) {
	if (column == null || column.isDisposed ()) return 0;
	for (int index = 0; index < columnCount; index++) if (columns [index] == column) return index;
	return 0;
}

int [] virtualAccessibleSelectedRows () {
	java.util.List<Integer> rows = new ArrayList<> ();
	for (var entry : virtualSelections.entrySet ()) {
		for (int index : entry.getValue ().toArray ()) {
			long row = virtualProjection.visibleIndexOfCoordinate (entry.getKey (), index);
			if (row >= 0 && row < virtualAccessibleRowCount ()) rows.add ((int)row);
		}
	}
	return rows.stream ().mapToInt (Integer::intValue).sorted ().toArray ();
}

int virtualAccessibleSelectionCount () {
	return virtualAccessibleSelectionCount (virtualAccessibleRowCount ());
}

int virtualAccessibleSelectionCount (long maximum) {
	refreshVirtualSelectionCounts ();
	long count = 0;
	for (var entry : virtualSelections.entrySet ()) {
		int parentId = entry.getKey ();
		if (parentId != VirtualTreeTopology.ROOT && (virtualProjection.visibleIndexOf (parentId) < 0
				|| !virtualTopology.flag (parentId, VirtualItemState.EXPANDED))) continue;
		VirtualSelectionModel selection = entry.getValue ();
		int limit = virtualChildLowerBound (parentId, maximum, selection.logicalCount ());
		long spans = 0;
		for (int range = 0; range < selection.rangeCount (); range++) {
			spans += Math.max (0, Math.min (limit, selection.rangeEndExclusive (range)) - selection.rangeStart (range));
		}
		count += selection.complementMode () ? limit - spans : spans;
	}
	return (int)Math.min (Integer.MAX_VALUE, count);
}

int virtualAccessibleSelectedRowAt (int rank) {
	if (rank < 0 || rank >= virtualAccessibleSelectionCount ()) return -1;
	long low = 0, high = virtualAccessibleRowCount () - 1L;
	while (low < high) {
		long middle = low + (high - low) / 2;
		if (virtualAccessibleSelectionCount (middle + 1) > rank) high = middle;
		else low = middle + 1;
	}
	return (int)low;
}

boolean virtualAccessibleScrollTo (TreeItem item, TreeColumn column, int type) {
	if (item.isDisposed () || type < ATK.ATK_SCROLL_TOP_LEFT || type > ATK.ATK_SCROLL_ANYWHERE) return false;
	revealVirtualItem (item, false);
	if (isDisposed () || item.isDisposed ()) return false;
	long row = virtualProjection.visibleIndexOf (virtualItemId (item));
	if (row < 0) return false;
	if (type == ATK.ATK_SCROLL_TOP_LEFT || type == ATK.ATK_SCROLL_TOP_EDGE) {
		GTK.gtk_adjustment_set_value (verticalAdjustment, row * (double)virtualRowExtent);
	} else if (type == ATK.ATK_SCROLL_BOTTOM_RIGHT || type == ATK.ATK_SCROLL_BOTTOM_EDGE) {
		GTK.gtk_adjustment_set_value (verticalAdjustment, (row + 1) * (double)virtualRowExtent - virtualBodyExtent);
	}
	Rectangle bounds = item.getBounds (virtualAccessibleItemColumn (column));
	long horizontal = GTK.gtk_scrolled_window_get_hadjustment (scrolledHandle);
	double value = GTK.gtk_adjustment_get_value (horizontal);
	double page = GTK.gtk_adjustment_get_page_size (horizontal);
	if (type == ATK.ATK_SCROLL_LEFT_EDGE || type == ATK.ATK_SCROLL_TOP_LEFT) value += bounds.x;
	else if (type == ATK.ATK_SCROLL_RIGHT_EDGE || type == ATK.ATK_SCROLL_BOTTOM_RIGHT) value += bounds.x + (double)bounds.width - page;
	else if (bounds.x < 0) value += bounds.x;
	else if (bounds.x + (double)bounds.width > page) value += bounds.x + (double)bounds.width - page;
	GTK.gtk_adjustment_set_value (horizontal, value);
	return true;
}

boolean virtualAccessibleScrollToPoint (TreeItem item, TreeColumn column, AccessibleControlEvent point) {
	if (item.isDisposed ()) return false;
	Point local;
	if (point.detail == ATK.ATK_XY_PARENT) local = new Point (point.x, point.y);
	else {
		long x = point.x, y = point.y;
		if (point.detail == ATK.ATK_XY_WINDOW) {
			int [] originX = new int [1], originY = new int [1];
			long window = GTK3.gtk_widget_get_window (GTK3.gtk_widget_get_toplevel (handle));
			if (window != 0) GDK.gdk_window_get_origin (window, originX, originY);
			x += originX [0];
			y += originY [0];
		} else if (point.detail != ATK.ATK_XY_SCREEN) return false;
		local = toControl ((int)Math.max (Integer.MIN_VALUE, Math.min (Integer.MAX_VALUE, x)),
				(int)Math.max (Integer.MIN_VALUE, Math.min (Integer.MAX_VALUE, y)));
	}
	revealVirtualItem (item, false);
	if (isDisposed () || item.isDisposed ()) return false;
	long row = virtualProjection.visibleIndexOf (virtualItemId (item));
	if (row < 0) return false;
	GTK.gtk_adjustment_set_value (verticalAdjustment, row * (double)virtualRowExtent - (local.y - (double)getHeaderHeight ()));
	Rectangle bounds = item.getBounds (virtualAccessibleItemColumn (column));
	long horizontal = GTK.gtk_scrolled_window_get_hadjustment (scrolledHandle);
	GTK.gtk_adjustment_set_value (horizontal, GTK.gtk_adjustment_get_value (horizontal) + bounds.x - (double)local.x);
	return true;
}

void pruneVirtualAccessibleColumns () {
	if (virtualAccessibleHeaders == null) return;
	virtualAccessibleHeaders.entrySet ().removeIf (entry -> {
		TreeColumn column = entry.getKey ();
		boolean obsolete = column == null ? columnCount != 0 : column.isDisposed () || column.parent != this;
		if (obsolete) entry.getValue ().dispose ();
		return obsolete;
	});
	for (var cells : virtualAccessibleCells.values ()) cells.entrySet ().removeIf (entry -> {
		TreeColumn column = entry.getKey ();
		boolean obsolete = column == null ? columnCount != 0 : column.isDisposed () || column.parent != this;
		Accessible cell = entry.getValue ().get ();
		if (obsolete && cell != null) cell.dispose ();
		return obsolete || cell == null;
	});
}

void releaseVirtualAccessibleItem (TreeItem item) {
	if (virtualAccessibleCells == null) return;
	Map<TreeColumn, java.lang.ref.WeakReference<Accessible>> cells = virtualAccessibleCells.remove (item);
	if (cells == null) return;
	for (var reference : cells.values ()) {
		Accessible cell = reference.get ();
		if (cell != null) cell.dispose ();
	}
}

void notifyVirtualAccessibleFocus () {
	if (!usesBoundedVirtualView () || accessible == null) return;
	TreeItem item = getFocusItem ();
	int id = item != null && isFocusControl () ? virtualItemId (item) : -1;
	if (virtualAccessibleFocusId == id) return;
	virtualAccessibleFocusId = id;
	accessible.internal_setActiveDescendant (id >= 0 ? virtualAccessibleCell (item, 0) : null);
}

long virtualAccessibleCheckStates (TreeItem item) {
	if (item.isDisposed () || (style & SWT.CHECK) == 0) return 0;
	long states = 1L << ATK.ATK_STATE_CHECKABLE;
	if (item.isCheckedState ()) {
		states |= 1L << ATK.ATK_STATE_CHECKED;
		if (item.isGrayedState ()) states |= 1L << ATK.ATK_STATE_INDETERMINATE;
	}
	return states;
}

long virtualAccessibleNativeStates (TreeItem item, TreeColumn column) {
	if (item.isDisposed ()) return 1L << ATK.ATK_STATE_DEFUNCT;
	int id = virtualItemId (item);
	long states = (1L << ATK.ATK_STATE_FOCUSABLE) | (1L << ATK.ATK_STATE_SELECTABLE) | virtualAccessibleCheckStates (item);
	if (virtualSelected (virtualTopology.parentId (id), virtualTopology.childIndex (id))) states |= 1L << ATK.ATK_STATE_SELECTED;
	if (id == virtualFocusId && isFocusControl ()) states |= 1L << ATK.ATK_STATE_FOCUSED;
	if (virtualChildCount (item) > 0) {
		states |= 1L << ATK.ATK_STATE_EXPANDABLE;
		if (item.isExpandedState ()) states |= 1L << ATK.ATK_STATE_EXPANDED;
	}
	long row = virtualProjection.visibleIndexOf (id);
	if (getVisible () && row >= 0 && virtualAccessibleVisualColumn (column) >= 0) {
		states |= 1L << ATK.ATK_STATE_VISIBLE;
		if (row >= virtualViewport.topRow () && row < virtualViewport.topRow () + virtualViewport.visibleRows ()) states |= 1L << ATK.ATK_STATE_SHOWING;
	}
	if (getEnabled ()) states |= (1L << ATK.ATK_STATE_ENABLED) | (1L << ATK.ATK_STATE_SENSITIVE);
	return states;
}

void notifyVirtualAccessibleItemStates (TreeItem item) {
	if (virtualAccessibleCells == null || isDisposed () || item.isDisposed ()) return;
	Map<TreeColumn, java.lang.ref.WeakReference<Accessible>> cells = virtualAccessibleCells.get (item);
	if (cells == null) return;
	for (var entry : cells.entrySet ()) {
		Accessible cell = entry.getValue ().get ();
		if (cell == null) continue;
		long states = virtualAccessibleNativeStates (item, entry.getKey ());
		Long previous = virtualAccessibleStates.put (cell, states);
		long changed = previous != null ? previous ^ states : 0;
		while (changed != 0) {
			int state = Long.numberOfTrailingZeros (changed);
			cell.internal_notifyNativeState (state, (states & 1L << state) != 0);
			changed &= changed - 1;
		}
	}
}

void notifyVirtualAccessibleStates () {
	if (virtualAccessibleCells == null) return;
	for (TreeItem item : virtualAccessibleCells.keySet ()) notifyVirtualAccessibleItemStates (item);
}

void notifyVirtualAccessibleSelection () {
	if (!usesBoundedVirtualView () || accessible == null) return;
	accessible.selectionChanged ();
	notifyVirtualAccessibleStates ();
}

void notifyVirtualAccessibleModel () {
	if (!usesBoundedVirtualView () || accessible == null) return;
	long generation = virtualTopology.generation ();
	if (!virtualAccessibleColumnsDirty && virtualAccessibleGeneration == generation) return;
	virtualAccessibleColumnsDirty = false;
	virtualAccessibleGeneration = generation;
	pruneVirtualAccessibleColumns ();
	OS.g_signal_emit_by_name (GTK3.gtk_widget_get_accessible (fixedHandle), Converter.wcsToMbcs ("model-changed", true));
	accessible.sendEvent (ACC.EVENT_LOCATION_CHANGED, null);
}

void releaseVirtualAccessibleChildren () {
	if (virtualAccessibleCells == null) return;
	for (var cells : virtualAccessibleCells.values ()) for (var reference : cells.values ()) {
		Accessible cell = reference.get ();
		if (cell != null) cell.dispose ();
	}
	virtualAccessibleCells.clear ();
	virtualAccessibleHeaders.clear ();
	virtualAccessibleStates.clear ();
}

void restoreVirtualFocus () {
	if (!usesBoundedVirtualView () || isDisposed ()) return;
	int entry = virtualResidency.entryForMaterializedId (virtualFocusId);
	if (entry < 0) return;
	long path = residentPath (entry);
	long selection = GTK.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	try {
		GTK.gtk_tree_view_set_cursor (handle, path, 0, false);
		restoreVirtualSelection ();
	} finally {
		GTK.gtk_tree_path_free (path);
		OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	}
	if (!isDisposed ()) notifyVirtualAccessibleFocus ();
}

int virtualLogicalKey (int key) {
	return switch (key) {
		case GDK.GDK_KP_Up -> GDK.GDK_Up;
		case GDK.GDK_KP_Down -> GDK.GDK_Down;
		case GDK.GDK_KP_Home -> GDK.GDK_Home;
		case GDK.GDK_KP_End -> GDK.GDK_End;
		case GDK.GDK_KP_Page_Up -> GDK.GDK_Page_Up;
		case GDK.GDK_KP_Page_Down -> GDK.GDK_Page_Down;
		case GDK.GDK_KP_Left -> GDK.GDK_Left;
		case GDK.GDK_KP_Right -> GDK.GDK_Right;
		default -> key;
	};
}

boolean virtualSelectionKey (int key, int mask) {
	if ((mask & GDK.GDK_CONTROL_MASK) == 0) return false;
	if (key == 'a' || key == 'A') return true;
	return (mask & GDK.GDK_SHIFT_MASK) == 0 && (key == '/' || key == VIRTUAL_BACKSLASH_KEY);
}

void virtualKeyboardSelection (int key, int mask) {
	boolean clear = key == VIRTUAL_BACKSLASH_KEY || (mask & GDK.GDK_SHIFT_MASK) != 0;
	boolean changed = clear ? virtualSelectionCount () != 0
			: (style & SWT.MULTI) != 0 && !virtualAllVisibleSelected ();
	if (!changed) return;
	if (clear) deselectAll ();
	else selectAll ();
	if (isDisposed ()) return;
	TreeItem item = getFocusItem ();
	if (item != null) {
		Event selection = new Event ();
		selection.item = exposeVirtualItem (item);
		sendSelectionEvent (SWT.Selection, selection, false);
	}
}

boolean virtualNavigationKey (int key) {
	return key == GDK.GDK_Up || key == GDK.GDK_Down || key == GDK.GDK_Home || key == GDK.GDK_End
			|| key == GDK.GDK_Page_Up || key == GDK.GDK_Page_Down || key == GDK.GDK_Left || key == GDK.GDK_Right;
}

void virtualNavigate (int key, int mask) {
	if ((style & SWT.MIRRORED) != 0) {
		if (key == GDK.GDK_Left) key = GDK.GDK_Right;
		else if (key == GDK.GDK_Right) key = GDK.GDK_Left;
	}
	long total = virtualProjection.visibleRowCount ();
	if (total == 0) return;
	TreeItem focus = getFocusItem ();
	long row = focus != null ? virtualProjection.visibleIndexOf (virtualItemId (focus)) : virtualViewport.topRow ();
	if (row < 0) row = virtualViewport.topRow ();
	long next = row;
	if (key == GDK.GDK_Up) next = Math.max (0, row - 1);
	if (key == GDK.GDK_Down) next = Math.min (total - 1, row + 1);
	if (key == GDK.GDK_Home) next = 0;
	if (key == GDK.GDK_End) next = total - 1;
	if (key == GDK.GDK_Page_Up) next = Math.max (0, row - Math.max (1, virtualViewport.visibleRows () - 1));
	if (key == GDK.GDK_Page_Down) next = row + Math.min (total - 1 - row, Math.max (1, virtualViewport.visibleRows () - 1));
	if (key == GDK.GDK_Left || key == GDK.GDK_Right) {
		if (focus == null) focus = virtualVisibleItem (row);
		if (focus == null || !checkData (focus)) return;
		int id = virtualItemId (focus);
		boolean expanded = virtualTopology.flag (id, VirtualItemState.EXPANDED);
		if (key == GDK.GDK_Left && expanded || key == GDK.GDK_Right && !expanded && virtualChildCount (focus) > 0) {
			Event event = new Event ();
			event.item = exposeVirtualItem (focus);
			virtualCallbackDepth++;
			try {
				sendEvent (key == GDK.GDK_Right ? SWT.Expand : SWT.Collapse, event);
				if (!isDisposed () && !focus.isDisposed ()) focus.setExpandedState (key == GDK.GDK_Right);
			} finally {
				virtualCallbackDepth--;
			}
			if (!isDisposed ()) reconcileVirtualResidency ();
			return;
		}
		if (key == GDK.GDK_Left) {
			int parentId = virtualTopology.parentId (id);
			if (parentId == VirtualTreeTopology.ROOT) return;
			next = virtualProjection.visibleIndexOf (parentId);
		} else {
			if (!expanded || virtualChildCount (focus) == 0) return;
			next = row + 1;
		}
	}
	TreeItem item = virtualVisibleItem (next);
	if (item == null) return;
	int id = virtualItemId (item);
	boolean control = (mask & GDK.GDK_CONTROL_MASK) != 0;
	boolean shift = (mask & GDK.GDK_SHIFT_MASK) != 0 && (style & SWT.MULTI) != 0;
	if (shift) {
		if (virtualAnchorId < 0) virtualAnchorId = focus != null ? virtualItemId (focus) : id;
		if (!control) virtualSelections.clear ();
		long anchor = virtualProjection.visibleIndexOf (virtualAnchorId);
		if (anchor >= 0) virtualSelectVisibleRange (Math.min (anchor, next), Math.max (anchor, next));
	} else {
		virtualAnchorId = id;
		if (!control) {
			virtualSelections.clear ();
			virtualSelectItem (item, true);
		}
	}
	virtualFocusId = id;
	revealVirtualItem (item, false);
	if (isDisposed () || item.isDisposed ()) return;
	long selection = GTK.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	long path = residentPath (virtualResidency.entryForMaterializedId (id));
	try {
		if (path != 0) GTK.gtk_tree_view_set_cursor (handle, path, 0, false);
		restoreVirtualSelection ();
	} finally {
		if (path != 0) GTK.gtk_tree_path_free (path);
		OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	}
	if (isDisposed ()) return;
	notifyVirtualAccessibleFocus ();
	notifyVirtualAccessibleSelection ();
	if (!control || shift) {
		Event event = new Event ();
		event.item = exposeVirtualItem (item);
		sendSelectionEvent (SWT.Selection, event, false);
	}
}


void virtualCellArea (TreeItem item, long path, long column, GdkRectangle rect) {
	GTK.gtk_tree_view_get_cell_area (handle, path, column, rect);
	if (!usesBoundedVirtualView () || path != 0) return;
	int id = virtualItemId (item);
	long row = virtualProjection.visibleIndexOf (id);
	if (row < 0) return;
	int [] horizontal = new int [1], vertical = new int [1], expander = new int [1];
	GTK3.gtk_widget_style_get (handle, OS.horizontal_separator, horizontal, 0);
	GTK3.gtk_widget_style_get (handle, Converter.wcsToMbcs ("vertical-separator", true), vertical, 0);
	GTK3.gtk_widget_style_get (handle, OS.expander_size, expander, 0);
	int extent = expander [0] + horizontal [0] / 2;
	if (column == GTK.gtk_tree_view_get_expander_column (handle)) {
		int depth = 1;
		for (int parentId = virtualTopology.parentId (id); parentId != VirtualTreeTopology.ROOT;
				parentId = virtualTopology.parentId (parentId)) depth++;
		int [] indentation = new int [1], expanders = new int [1];
		OS.g_object_get (handle, Converter.wcsToMbcs ("level-indentation", true), indentation, 0);
		OS.g_object_get (handle, Converter.wcsToMbcs ("show-expanders", true), expanders, 0);
		long inset = (depth - 1L) * indentation [0] + (expanders [0] != 0 ? depth * (long)extent : 0);
		int clipped = (int)Math.min (Integer.MAX_VALUE, inset);
		if ((style & SWT.MIRRORED) == 0) rect.x = (int)Math.min (Integer.MAX_VALUE, rect.x + (long)clipped);
		rect.width = Math.max (0, rect.width - clipped);
	}
	double y = (row - virtualViewport.topRow ()) * (double)virtualRowExtent
			- virtualPixelRemainder + vertical [0] / 2;
	rect.y = (int)Math.max (Integer.MIN_VALUE, Math.min (Integer.MAX_VALUE, y));
	rect.height = Math.max (0, Math.max (virtualRowExtent, extent) - vertical [0]);
}

void clearVirtualDescendantSelection (int collapsedId) {
	virtualSelections.entrySet ().removeIf (entry -> {
		for (int id = entry.getKey (); id != VirtualTreeTopology.ROOT && virtualTopology.contains (id);
				id = virtualTopology.parentId (id)) if (id == collapsedId) return true;
		return false;
	});
	for (int id = virtualFocusId; id >= 0 && virtualTopology.contains (id); id = virtualTopology.parentId (id)) {
		if (id == collapsedId) { virtualFocusId = collapsedId; break; }
	}
}

boolean usesBoundedVirtualView () {
	return usesVirtualNativeModel () && virtualViewModel != 0;
}

long viewModel () {
	return usesBoundedVirtualView () ? virtualViewModel : modelHandle;
}

long viewPath (long iter) {
	if (!usesBoundedVirtualView ()) return GTK.gtk_tree_model_get_path (modelHandle, iter);
	long path = GTK.gtk_tree_model_get_path (virtualViewModel, iter);
	if (path != 0) return path;
	int id = virtualMaterializedId (iter);
	if (id < 0 || virtualShapeChanged) return 0;
	int entry = virtualResidency.entryForMaterializedId (id);
	if (entry < 0) entry = virtualResidency.entryForCoordinate (virtualTopology.parentId (id), virtualTopology.childIndex (id));
	if (entry >= 0) residentItem (entry);
	return residentPath (entry);
}

long residentPath (int entry) {
	if (entry < 0) return 0;
	int [] indices = new int [virtualResidency.depth (entry) + 1];
	int length = virtualResidency.physicalPath (entry, indices);
	long path = GTK.gtk_tree_path_new ();
	for (int level = 0; level < length; level++) GTK.gtk_tree_path_append_index (path, indices [level]);
	return path;
}

int residentEntry (long path) {
	if (path == 0) return -1;
	int depth = GTK.gtk_tree_path_get_depth (path);
	if (depth <= 0) return -1;
	int [] indices = new int [depth];
	C.memmove (indices, GTK.gtk_tree_path_get_indices (path), 4L * depth);
	return virtualResidency.entryAtPhysicalPath (indices, depth);
}

TreeItem virtualCoordinateItem (int parentId, int index) {
	int id = virtualTopology.materializedChildId (parentId, index);
	if (id >= 0 && id < items.length && items [id] != null) return items [id];
	if (id < 0) {
		id = findAvailableId ();
		nextId = id + 1;
		virtualTopology.bind (id, parentId, index);
		updateVirtualNativeSnapshot ();
	}
	long path = virtualPath (id);
	long iter = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
	if (iter == 0) error (SWT.ERROR_NO_HANDLES);
	try {
		if (!GTK.gtk_tree_model_get_iter (modelHandle, iter, path)) return null;
		return _getItem (iter);
	} finally {
		OS.g_free (iter);
		GTK.gtk_tree_path_free (path);
	}
}

TreeItem virtualVisibleItem (long row) {
	if (row < 0 || row >= virtualProjection.visibleRowCount ()) return null;
	VirtualTreeVisibleProjection.Row coordinate = virtualProjection.rowAt (row);
	return virtualCoordinateItem (coordinate.parentId (), coordinate.childIndex ());
}

TreeItem residentItem (int entry) {
	if (entry < 0 || virtualResidency.isHint (entry)) return null;
	if (virtualShapeChanged) {
		int id = virtualResidency.materializedId (entry);
		return id >= 0 && id < items.length && virtualTopology.contains (id)
				&& items [id] != null && !items [id].isDisposed () ? items [id] : null;
	}
	TreeItem item = virtualCoordinateItem (virtualResidency.parentId (entry), virtualResidency.childIndex (entry));
	if (item == null || item.isDisposed ()) return null;
	int id = virtualItemId (item);
	if (virtualResidency.materializedId (entry) != id) {
		virtualResidency.bindFacade (entry, id);
		int [] binding = {entry, id};
		long address = OS.g_malloc (8);
		if (address == 0) error (SWT.ERROR_NO_HANDLES);
		try {
			C.memmove (address, binding, 8);
			OS.g_object_set (virtualViewModel, VIRTUAL_MODEL_FACADE, address, 0);
		} finally {
			OS.g_free (address);
		}
	}
	return item;
}

TreeItem virtualViewItem (long iter) {
	long path = GTK.gtk_tree_model_get_path (virtualViewModel, iter);
	if (path == 0) return null;
	try {
		return residentItem (residentEntry (path));
	} finally {
		GTK.gtk_tree_path_free (path);
	}
}

void scheduleVirtualResidency () {
	if (!usesBoundedVirtualView () || virtualResidencyScheduled || isDisposed ()) return;
	virtualResidencyScheduled = true;
	display.asyncExec (() -> {
		virtualResidencyScheduled = false;
		if (isDisposed () || !usesBoundedVirtualView ()) return;
		reconcileVirtualResidency ();
	});
}

void reconcileVirtualResidency () {
	if (!usesBoundedVirtualView () || virtualViewport == null || reconcilingVirtualResidency) return;
	if (currentItem != null || virtualCallbackDepth != 0) {
		scheduleVirtualResidency ();
		return;
	}
	updateVirtualViewportGeometry ();
	virtualViewport.refreshLogicalRange ();
	/* Overscan is part of this Tree's viewport, not an external viewer object.
	 * Reclaim only transient native rows when the JVM approaches effective Xmx. */
	virtualViewport.refreshOverscanForMemoryPressure ();
	refreshVirtualSelectionCounts ();
	if (virtualResidencyGeneration == virtualViewport.generation ()) {
		updateVirtualLogicalAdjustment ();
		alignVirtualViewAdjustment ();
		notifyVirtualAccessibleModel ();
		notifyVirtualAccessibleFocus ();
		return;
	}
	long plannedGeneration = virtualViewport.generation ();
	virtualViewport.paintResidency (nextVirtualResidency);
	nextVirtualResidency.addExpanderHints (virtualTopology);
	int [] packet = nextVirtualResidency.nativeModelSnapshot ();
	long bytes = Math.multiplyExact ((long)packet.length, Integer.BYTES);
	long address = OS.g_malloc (bytes);
	if (address == 0) error (SWT.ERROR_NO_HANDLES);
	C.memmove (address, packet, bytes);
	long selection = GTK.gtk_tree_view_get_selection (handle);
	boolean modelDetached = false;
	reconcilingVirtualResidency = true;
	setRedraw (false);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, TEST_EXPAND_ROW);
	OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, TEST_COLLAPSE_ROW);
	try {
		/* GtkTreeView must hold no old iters when the native snapshot stamp changes. */
		GTK.gtk_tree_view_set_model (handle, 0);
		modelDetached = true;
		OS.g_object_set (virtualViewModel, VIRTUAL_MODEL_RESIDENCY, address, 0);
		VirtualTreeVisibleProjection.Residency previous = virtualResidency;
		virtualResidency = nextVirtualResidency;
		nextVirtualResidency = previous;
		virtualShapeChanged = false;
		GTK.gtk_tree_view_set_model (handle, virtualViewModel);
		modelDetached = false;
		for (int entry = 0; entry < virtualResidency.size (); entry++) {
			int id = virtualResidency.materializedId (entry);
			if (id < 0 || !virtualTopology.flag (id, VirtualItemState.EXPANDED)) continue;
			long path = residentPath (entry);
			try {
				GTK.gtk_tree_view_expand_row (handle, path, false);
			} finally {
				GTK.gtk_tree_path_free (path);
			}
		}
		int focusEntry = virtualResidency.entryForMaterializedId (virtualFocusId);
		if (focusEntry >= 0) {
			long path = residentPath (focusEntry);
			try {
				GTK.gtk_tree_view_set_cursor (handle, path, 0, false);
			} finally {
				GTK.gtk_tree_path_free (path);
			}
		}
		restoreVirtualSelection ();
		/* Ancestor closure and overscan precede top. Align the logical top's
		 * physical row, rather than treating entry zero as the viewport origin. */
		long top = virtualViewport.topRow ();
		int offset = Math.toIntExact (top - virtualResidency.firstPaintRow ());
		if (offset >= 0 && offset < virtualResidency.paintCount ()) {
			long path = residentPath (virtualResidency.paintEntry (offset));
			try {
				GTK.gtk_tree_view_scroll_to_cell (handle, path, 0, true, 0f, 0f);
			} finally {
				GTK.gtk_tree_path_free (path);
			}
		}
		virtualResidencyGeneration = plannedGeneration;
		updateVirtualLogicalAdjustment ();
		alignVirtualViewAdjustment ();
	} finally {
		/* On allocation failure, reattach the previous native snapshot so the
		 * view is never left disconnected from its bounded owner. */
		if (!isDisposed ()) {
			if (modelDetached) GTK.gtk_tree_view_set_model (handle, virtualViewModel);
			OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, TEST_COLLAPSE_ROW);
			OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, TEST_EXPAND_ROW);
			OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
			setRedraw (true);
			GTK.gtk_widget_queue_draw (handle);
		}
		OS.g_free (address);
		reconcilingVirtualResidency = false;
	}
	if (!isDisposed ()) {
		notifyVirtualAccessibleModel ();
		notifyVirtualAccessibleFocus ();
		notifyVirtualAccessibleStates ();
	}
}

VirtualSelectionModel virtualSelection (int parentId) {
	VirtualSelectionModel selection = virtualSelections.computeIfAbsent (parentId, ignored -> new VirtualSelectionModel ());
	selection.setLogicalCount (virtualTopology.childCountKnown (parentId) ? virtualTopology.childCount (parentId) : 0);
	return selection;
}

void refreshVirtualSelectionCounts () {
	virtualSelections.entrySet ().removeIf (entry -> {
		int parentId = entry.getKey ();
		if (parentId != VirtualTreeTopology.ROOT && !virtualTopology.contains (parentId)) return true;
		entry.getValue ().setLogicalCount (virtualTopology.childCountKnown (parentId) ? virtualTopology.childCount (parentId) : 0);
		return entry.getValue ().selectedCount () == 0;
	});
	if (virtualFocusId >= 0 && !virtualTopology.contains (virtualFocusId)) virtualFocusId = -1;
	if (virtualAnchorId >= 0 && !virtualTopology.contains (virtualAnchorId)) virtualAnchorId = -1;
}

boolean virtualSelected (int parentId, int index) {
	VirtualSelectionModel selection = virtualSelections.get (parentId);
	return selection != null && index >= 0 && index < selection.logicalCount () && selection.isSelected (index);
}

void virtualSelectItem (TreeItem item, boolean selected) {
	if (item.parent != this) return;
	int id = virtualItemId (item);
	if (selected && (style & SWT.SINGLE) != 0) virtualSelections.clear ();
	virtualSelection (virtualTopology.parentId (id)).setSelected (virtualTopology.childIndex (id), selected);
	restoreVirtualSelection ();
	notifyVirtualAccessibleSelection ();
}

boolean virtualAllVisibleSelected () {
	refreshVirtualSelectionCounts ();
	long total = virtualProjection.visibleRowCount ();
	long selected = 0;
	for (var entry : virtualSelections.entrySet ()) {
		int parentId = entry.getKey ();
		if (parentId != VirtualTreeTopology.ROOT && (virtualProjection.visibleIndexOf (parentId) < 0
				|| !virtualTopology.flag (parentId, VirtualItemState.EXPANDED))) return false;
		long count = entry.getValue ().selectedCount ();
		if (count > total - selected) return false;
		selected += count;
	}
	return selected == total;
}

int virtualSelectionCount () {
	refreshVirtualSelectionCounts ();
	long count = 0;
	for (VirtualSelectionModel selection : virtualSelections.values ()) count += selection.selectedCount ();
	return (int)Math.min (Integer.MAX_VALUE, count);
}

TreeItem [] virtualSelectionItems () {
	refreshVirtualSelectionCounts ();
	java.util.List<TreeItem> selected = new ArrayList<> ();
	/* Explicit result enumeration may materialize its requested Items. Ordinary
	 * reconciliation uses the compressed algebra and never invokes this path. */
	for (var entry : virtualSelections.entrySet ()) {
		for (int index : entry.getValue ().toArray ()) {
			TreeItem item = virtualCoordinateItem (entry.getKey (), index);
			if (item != null) selected.add (exposeVirtualItem (item));
		}
	}
	return selected.toArray (TreeItem []::new);
}

void restoreVirtualSelection () {
	if (!usesBoundedVirtualView ()) return;
	long selection = GTK.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	try {
		GTK.gtk_tree_selection_unselect_all (selection);
		for (int entry = 0; entry < virtualResidency.size (); entry++) {
			if (virtualResidency.isHint (entry)) continue;
			if (!virtualSelected (virtualResidency.parentId (entry), virtualResidency.childIndex (entry))) continue;
			long path = residentPath (entry);
			try {
				long iter = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
				if (iter == 0) error (SWT.ERROR_NO_HANDLES);
				try {
					if (GTK.gtk_tree_model_get_iter (virtualViewModel, iter, path)) GTK.gtk_tree_selection_select_iter (selection, iter);
				} finally {
					OS.g_free (iter);
				}
			} finally {
				GTK.gtk_tree_path_free (path);
			}
		}
	} finally {
		OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	}
}

void captureVirtualSelection () {
	if (!usesBoundedVirtualView () || reconcilingVirtualResidency) return;
	long selection = GTK.gtk_tree_view_get_selection (handle);
	TreeItem focus = virtualNativeCursorItem ();
	int [] nativeState = new int [1];
	GTK3.gtk_get_current_event_state (nativeState);
	int modifiers = 0;
	if ((nativeState [0] & GDK.GDK_CONTROL_MASK) != 0) modifiers |= SWT.CTRL;
	if ((nativeState [0] & GDK.GDK_SHIFT_MASK) != 0) modifiers |= SWT.SHIFT;
	if ((modifiers & SWT.CTRL) == 0 || (style & SWT.SINGLE) != 0) virtualSelections.clear ();
	if (focus != null) {
		virtualFocusId = virtualItemId (focus);
		if ((modifiers & SWT.SHIFT) != 0 && virtualAnchorId >= 0) {
			long first = virtualProjection.visibleIndexOf (virtualAnchorId);
			long last = virtualProjection.visibleIndexOf (virtualFocusId);
			if (first >= 0 && last >= 0) virtualSelectVisibleRange (Math.min (first, last), Math.max (first, last));
		} else {
			virtualAnchorId = virtualFocusId;
		}
	}
	for (int entry = 0; entry < virtualResidency.size (); entry++) {
		if (virtualResidency.isHint (entry)) continue;
		long path = residentPath (entry);
		try {
			boolean selected = GTK.gtk_tree_selection_path_is_selected (selection, path);
			/* Shift ranges were evaluated against logical preorder; a clipped
			 * native range must not erase their off-window portion. */
			if ((modifiers & SWT.SHIFT) == 0 || selected) {
				virtualSelection (virtualResidency.parentId (entry)).setSelected (virtualResidency.childIndex (entry), selected);
			}
		} finally {
			GTK.gtk_tree_path_free (path);
		}
	}
	refreshVirtualSelectionCounts ();
}

TreeItem virtualNativeCursorItem () {
	long [] path = new long [1];
	GTK.gtk_tree_view_get_cursor (handle, path, null);
	if (path [0] == 0) return null;
	try {
		return residentItem (residentEntry (path [0]));
	} finally {
		GTK.gtk_tree_path_free (path [0]);
	}
}

void virtualSelectVisibleRange (long first, long last) {
	selectVirtualChildRange (VirtualTreeTopology.ROOT, first, last);
	for (int id = 0; id < virtualTopology.idCapacity (); id++) {
		if (virtualTopology.contains (id) && virtualTopology.flag (id, VirtualItemState.EXPANDED)
				&& virtualProjection.visibleIndexOf (id) >= 0) selectVirtualChildRange (id, first, last);
	}
}

void selectVirtualChildRange (int parentId, long first, long last) {
	if (!virtualTopology.childCountKnown (parentId)) return;
	int count = virtualTopology.childCount (parentId);
	int start = virtualChildLowerBound (parentId, first, count);
	int end = last == Long.MAX_VALUE ? count : virtualChildLowerBound (parentId, last + 1, count);
	if (start < end) virtualSelection (parentId).selectRange (start, end);
}

int virtualChildLowerBound (int parentId, long row, int count) {
	int low = 0, high = count;
	while (low < high) {
		int middle = low + (high - low) / 2;
		if (virtualProjection.visibleIndexOfCoordinate (parentId, middle) < row) low = middle + 1;
		else high = middle;
	}
	return low;
}

boolean expandVirtualAncestors (TreeItem item) {
	if (isDisposed () || item == null || item.isDisposed () || item.parent != this) return false;
	int id = virtualItemId (item);
	int depth = 0;
	for (int parentId = virtualTopology.parentId (id); parentId != VirtualTreeTopology.ROOT;
			parentId = virtualTopology.parentId (parentId)) depth++;
	int [] ancestors = new int [depth];
	for (int parentId = virtualTopology.parentId (id), position = depth - 1; position >= 0;
			parentId = virtualTopology.parentId (parentId)) ancestors [position--] = parentId;
	/* Preserve showItem's root-to-leaf expansion events without creating any
	 * sibling prefix. A listener may dispose the target or mutate the branch. */
	virtualCallbackDepth++;
	try {
		for (int parentId : ancestors) {
			if (isDisposed () || item.isDisposed () || !virtualTopology.contains (parentId)) return false;
			if (virtualTopology.flag (parentId, VirtualItemState.EXPANDED)) continue;
			TreeItem ancestor = virtualCoordinateItem (virtualTopology.parentId (parentId), virtualTopology.childIndex (parentId));
			if (ancestor == null || !checkData (ancestor)) return false;
			if (isDisposed () || item.isDisposed () || ancestor.isDisposed ()) return false;
			Event event = new Event ();
			event.item = exposeVirtualItem (ancestor);
			sendEvent (SWT.Expand, event);
			if (isDisposed () || item.isDisposed () || ancestor.isDisposed ()) return false;
			ancestor.setExpandedState (true);
		}
	} finally {
		virtualCallbackDepth--;
		if (!isDisposed ()) scheduleVirtualResidency ();
	}
	return !isDisposed () && !item.isDisposed ();
}

void revealVirtualItem (TreeItem item, boolean top) {
	if (!expandVirtualAncestors (item)) return;
	int id = virtualItemId (item);
	updateVirtualViewportGeometry ();
	virtualViewport.refreshLogicalRange ();
	long row = virtualProjection.visibleIndexOf (id);
	if (row < 0) return;
	if (top) virtualViewport.setTopRow (row);
	else virtualViewport.ensureVisible (row);
	reconcileVirtualResidency ();
}


boolean usesVirtualNativeModel () {
	if ((style & SWT.VIRTUAL) == 0 || GTK.GTK4) return false;
	if (virtualLogicalNativeModel == null) {
		virtualLogicalNativeModel = Boolean.getBoolean (VIRTUAL_LOGICAL_NATIVE_MODEL_PROPERTY);
	}
	return virtualLogicalNativeModel.booleanValue ();
}

long virtualPath (int id) {
	if (virtualTopology == null || !virtualTopology.contains (id)) return 0;
	int depth = 1;
	for (int parentId = virtualTopology.parentId (id);
			parentId != VirtualTreeTopology.ROOT; parentId = virtualTopology.parentId (parentId)) {
		depth++;
	}
	int [] indices = new int [depth];
	int current = id;
	for (int position = depth - 1; position >= 0; position--) {
		indices [position] = virtualTopology.childIndex (current);
		current = virtualTopology.parentId (current);
	}
	long path = GTK.gtk_tree_path_new ();
	for (int index : indices) GTK.gtk_tree_path_append_index (path, index);
	return path;
}

int virtualMaterializedId (long iter) {
	if (virtualTopology == null || iter == 0) return -1;
	long path = GTK.gtk_tree_model_get_path (modelHandle, iter);
	if (path == 0) return -1;
	try {
		int depth = GTK.gtk_tree_path_get_depth (path);
		if (depth <= 0) return -1;
		int [] indices = new int [depth];
		C.memmove (indices, GTK.gtk_tree_path_get_indices (path), 4L * depth);
		int parentId = VirtualTreeTopology.ROOT;
		for (int level = 0; level < depth; level++) {
			int id = virtualTopology.materializedChildId (parentId, indices [level]);
			if (level == depth - 1) return id;
			if (id < 0) return -1;
			parentId = id;
		}
		return -1;
	} finally {
		GTK.gtk_tree_path_free (path);
	}
}

void rebindVirtualItemHandles () {
	if (!usesVirtualNativeModel () || virtualTopology == null) return;
	for (int id = 0; id < items.length; id++) {
		TreeItem item = items [id];
		if (item == null || item.isDisposed () || !virtualTopology.contains (id)) continue;
		long path = virtualPath (id);
		if (path == 0) continue;
		try {
			if (item.handle == 0) {
				item.handle = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
				if (item.handle == 0) error (SWT.ERROR_NO_HANDLES);
			}
			if (!GTK.gtk_tree_model_get_iter (modelHandle, item.handle, path)) {
				throw new IllegalStateException ("virtual TreeItem outside logical model: " + id);
			}
		} finally {
			GTK.gtk_tree_path_free (path);
		}
	}
}

void updateVirtualNativeSnapshot () {
	if (!usesVirtualNativeModel () || virtualTopology == null || modelHandle == 0) return;
	int [] snapshot = virtualTopology.nativeModelSnapshot ();
	long bytes = Math.multiplyExact ((long)snapshot.length, Integer.BYTES);
	long address = OS.g_malloc (bytes);
	if (address == 0) error (SWT.ERROR_NO_HANDLES);
	try {
		C.memmove (address, snapshot, bytes);
		OS.g_object_set (modelHandle, VIRTUAL_MODEL_TOPOLOGY, address, 0);
	} finally {
		OS.g_free (address);
	}
	rebindVirtualItemHandles ();
}

TreeItem virtualFocusItem () {
	if (usesBoundedVirtualView ()) return getFocusItem ();
	if (!usesVirtualNativeModel () || handle == 0) return null;
	long [] path = new long [1];
	GTK.gtk_tree_view_get_cursor (handle, path, null);
	if (path [0] == 0) return null;
	long iter = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
	if (iter == 0) {
		GTK.gtk_tree_path_free (path [0]);
		error (SWT.ERROR_NO_HANDLES);
	}
	try {
		return GTK.gtk_tree_model_get_iter (viewModel (), iter, path [0]) ? _getItem (iter) : null;
	} finally {
		OS.g_free (iter);
		GTK.gtk_tree_path_free (path [0]);
	}
}

VirtualNativeViewState captureVirtualNativeViewState () {
	if (usesBoundedVirtualView ()) {
		return new VirtualNativeViewState (null, getFocusItem (), virtualVisibleItem (virtualViewport.topRow ()));
	}
	if (!usesVirtualNativeModel () || handle == 0) return null;
	return new VirtualNativeViewState (getSelection (), virtualFocusItem (), getTopItem ());
}

void restoreVirtualNativeViewState (VirtualNativeViewState state) {
	if (usesBoundedVirtualView ()) {
		if (state != null && state.top () != null && !state.top ().isDisposed ()) {
			virtualViewport.refreshLogicalRange ();
			virtualViewport.setTopMaterializedId (virtualItemId (state.top ()));
		}
		reconcileVirtualResidency ();
		return;
	}
	if (!usesVirtualNativeModel () || handle == 0) return;
	long selectionHandle = GTK.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (
			selectionHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	OS.g_signal_handlers_block_matched (
			handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, TEST_EXPAND_ROW);
	try {
		GTK.gtk_tree_view_set_model (handle, 0);
		GTK.gtk_tree_view_set_model (handle, modelHandle);
		for (int id = 0; id < items.length; id++) {
			TreeItem item = items [id];
			if (item == null || item.isDisposed () || !virtualTopology.contains (id)) continue;
			if (!virtualTopology.flag (id, VirtualItemState.EXPANDED)) continue;
			long path = virtualPath (id);
			if (path != 0) {
				GTK.gtk_tree_view_expand_row (handle, path, false);
				GTK.gtk_tree_path_free (path);
			}
		}
		GTK.gtk_tree_selection_unselect_all (selectionHandle);
		if (state != null && state.selection () != null) {
			for (TreeItem item : state.selection ()) {
				if (item != null && !item.isDisposed ()) {
					GTK.gtk_tree_selection_select_iter (selectionHandle, item.handle);
				}
			}
		}
		if (state != null && state.focus () != null && !state.focus ().isDisposed ()) {
			int id = virtualItemId (state.focus ());
			long path = virtualPath (id);
			if (path != 0) {
				GTK.gtk_tree_view_set_cursor (handle, path, 0, false);
				GTK.gtk_tree_path_free (path);
			}
		}
		if (state != null && state.top () != null && !state.top ().isDisposed ()) {
			int id = virtualItemId (state.top ());
			long path = virtualPath (id);
			if (path != 0) {
				GTK.gtk_tree_view_scroll_to_cell (handle, path, 0, true, 0f, 0f);
				GTK.gtk_tree_path_free (path);
			}
		}
	} finally {
		OS.g_signal_handlers_unblock_matched (
				handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, TEST_EXPAND_ROW);
		OS.g_signal_handlers_unblock_matched (
				selectionHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	}
}

void finishVirtualNativeMutation (VirtualNativeViewState state) {
	if (!usesVirtualNativeModel ()) return;
	updateVirtualNativeSnapshot ();
	if (usesBoundedVirtualView ()) {
		virtualShapeChanged = true;
		refreshVirtualSelectionCounts ();
		restoreVirtualNativeViewState (state);
		return;
	}
	if (currentItem == null) {
		restoreVirtualNativeViewState (state);
		return;
	}
	if (pendingVirtualNativeViewState == null) pendingVirtualNativeViewState = state;
	if (virtualNativeViewResetScheduled) return;
	virtualNativeViewResetScheduled = true;
	display.asyncExec (() -> {
		virtualNativeViewResetScheduled = false;
		if (isDisposed () || !usesVirtualNativeModel ()) return;
		VirtualNativeViewState pending = pendingVirtualNativeViewState;
		pendingVirtualNativeViewState = null;
		restoreVirtualNativeViewState (pending);
	});
}

static int checkStyle (int style) {
	return WidgetStylePolicy.TABLE_FULL_SELECTION.applyAsInt(style);
}

@Override
long cellDataProc (long tree_column, long cell, long tree_model, long iter, long data) {
	virtualCallbackDepth++;
	try {
		return virtualCellDataProc (tree_column, cell, tree_model, iter, data);
	} finally {
		virtualCallbackDepth--;
		if (usesBoundedVirtualView () && virtualViewport != null
				&& virtualResidencyGeneration != virtualViewport.generation ()) scheduleVirtualResidency ();
	}
}

long virtualCellDataProc (long tree_column, long cell, long tree_model, long iter, long data) {
	if (cell == ignoreCell) return 0;
	/*
	 * Don't re-enter SWT while removeAll() clears the model: _getItem() may call
	 * gtk_tree_store_set() (row-changed -> cellDataProc -> checkData -> SetData),
	 * and application code may then access items whose rows are already freed.
	 * See https://github.com/eclipse-platform/eclipse.platform.swt/issues/3329
	 */
	if (clearingModel) return 0;
	TreeItem item = _getItem (iter);
	if (usesBoundedVirtualView ()) OS.g_object_set (cell, Converter.wcsToMbcs ("visible", true), item != null, 0);
	if (item == null || item.isDisposed()) return 0;
	OS.g_object_set_qdata (cell, Display.SWT_OBJECT_INDEX2, item.handle);

	boolean isPixbuf = GTK.GTK_IS_CELL_RENDERER_PIXBUF (cell);
	boolean isText = GTK.GTK_IS_CELL_RENDERER_TEXT (cell);
	boolean isToggle = GTK.GTK_IS_CELL_RENDERER_TOGGLE (cell);
	if (isText) GTK.gtk_cell_renderer_set_fixed_size (cell, -1, -1);
	if (!(isPixbuf || isText || isToggle)) return 0;

	int columnIndex = 0;
	int modelIndex = -1;
	boolean customDraw = false;
	if (columnCount == 0) {
		modelIndex = Tree.FIRST_COLUMN;
		customDraw = firstCustomDraw;
	} else {
		for (int i = 0; i < columnCount; i++) {
			TreeColumn column = columns [i];
			if (column != null && column.handle == tree_column) {
				columnIndex = i;
				modelIndex = column.modelIndex;
				customDraw = column.customDraw;
				break;
			}
		}
	}
	if (modelIndex == -1) return 0;

	boolean setData = false;
	boolean updated = false;
	if ((style & SWT.VIRTUAL) != 0) {
		if (!item.isCachedState ()) setData = checkData (item);
		if (!setData && (isDisposed () || item.isDisposed ())) return 0;
		if (item.updated) {
			updated = true;
			item.updated = false;
		}
		virtualFlag (item, VirtualItemState.DIRTY, false);
		virtualFlag (item, VirtualItemState.PAINT_RESIDENT, true);

		if (isToggle) {
			OS.g_object_set (cell, OS.active, item.isCheckedState (), 0);
			OS.g_object_set (cell, OS.inconsistent,
					item.isCheckedState () && item.isGrayedState (), 0);
			requestVirtualFrontier (item);
			return 0;
		}

		Color itemBackground = item.virtualBackground;
		Color cellBackground = item.virtualCellBackground != null
				&& columnIndex < item.virtualCellBackground.length
				? item.virtualCellBackground [columnIndex] : null;
		GdkRGBA backgroundRGBA = cellBackground != null ? cellBackground.handle
				: itemBackground != null ? itemBackground.handle : null;
		OS.g_object_set (cell, OS.cell_background_rgba, backgroundRGBA, 0);

		if (isPixbuf) {
			Image image = item.virtualImages != null && columnIndex < item.virtualImages.length
					? item.virtualImages [columnIndex] : null;
			long pixbuf = 0;
			if (image != null) {
				if (imageList == null) imageList = new ImageList ();
				int imageIndex = imageList.indexOf (image);
				if (imageIndex == -1) imageIndex = imageList.add (image);
				pixbuf = ImageList.createPixbuf (imageList.getSurface (imageIndex));
			}
			OS.g_object_set (cell, OS.pixbuf, pixbuf, 0);
			if (pixbuf != 0) OS.g_object_unref (pixbuf);
			requestVirtualFrontier (item);
			return 0;
		}

		byte [] text = Converter.wcsToMbcs (item.virtualDisplayText (columnIndex), true);
		OS.g_object_set (cell, OS.text, text, 0);
		Color itemForeground = item.virtualForeground;
		Color cellForeground = item.virtualCellForeground != null
				&& columnIndex < item.virtualCellForeground.length
				? item.virtualCellForeground [columnIndex] : null;
		GdkRGBA foregroundRGBA = cellForeground != null ? cellForeground.handle
				: itemForeground != null ? itemForeground.handle : null;
		OS.g_object_set (cell, OS.foreground_rgba, foregroundRGBA, 0);
		Font cellFont = item.cellFont != null && columnIndex < item.cellFont.length
				? item.cellFont [columnIndex] : null;
		Font renderFont = cellFont != null ? cellFont : item.font;
		OS.g_object_set (cell, OS.font_desc, renderFont != null ? renderFont.handle : 0, 0);
		if (setData || updated) {
			ignoreCell = cell;
			setScrollWidth (tree_column, item);
			ignoreCell = 0;
		}
		requestVirtualFrontier (item);
		return 0;
	}

	long [] ptr = new long [1];
	if (setData) {
		if (isPixbuf) {
			ptr [0] = 0;
			GTK.gtk_tree_model_get (tree_model, iter, modelIndex + CELL_PIXBUF, ptr, -1);
			OS.g_object_set (cell, OS.gicon, ptr [0], 0);
			if (ptr [0] != 0) OS.g_object_unref (ptr [0]);
		} else {
			ptr [0] = 0;
			GTK.gtk_tree_model_get (tree_model, iter, modelIndex + CELL_TEXT, ptr, -1);
			if (ptr [0] != 0) {
				OS.g_object_set (cell, OS.text, ptr [0], 0);
				OS.g_free (ptr [0]);
			}
		}
	}
	if (customDraw) {
		if (!isOwnerDrawn) {
			ptr [0] = 0;
			GTK.gtk_tree_model_get (tree_model, iter, modelIndex + CELL_BACKGROUND, ptr, -1);
			if (ptr [0] != 0) {
				OS.g_object_set (cell, OS.cell_background_rgba, ptr [0], 0);
				GDK.gdk_rgba_free (ptr [0]);
			}
		}
		if (!isPixbuf) {
			ptr [0] = 0;
			GTK.gtk_tree_model_get (tree_model, iter, modelIndex + CELL_FOREGROUND, ptr, -1);
			if (ptr [0] != 0) {
				OS.g_object_set (cell, OS.foreground_rgba, ptr [0], 0);
				GDK.gdk_rgba_free (ptr [0]);
			}
			ptr [0] = 0;
			GTK.gtk_tree_model_get (tree_model, iter, modelIndex + CELL_FONT, ptr, -1);
			if (ptr [0] != 0) {
				OS.g_object_set (cell, OS.font_desc, ptr [0], 0);
				OS.pango_font_description_free (ptr [0]);
			}
		}
	}
	if (setData || updated) {
		ignoreCell = cell;
		setScrollWidth (tree_column, item);
		ignoreCell = 0;
	}
	return 0;
}

boolean checkData (TreeItem item) {
    if ((style & SWT.VIRTUAL) != 0 && virtualFlag(item, VirtualItemState.CACHED)) {
        return true;
    }
    if ((style & SWT.VIRTUAL) == 0 && item.cached) {
        return true;
    }
	if ((style & SWT.VIRTUAL) != 0) {
		pinVirtualFacade (item);
		virtualFlag (item, VirtualItemState.CACHED, true);
		TreeItem parentItem = item.getParentItem ();
		Event event = new Event ();
		event.item = item;
		event.index = parentItem == null ? indexOf (item) : parentItem.indexOf (item);
		int mask = OS.G_SIGNAL_MATCH_DATA | OS.G_SIGNAL_MATCH_ID;
		int signal_id = OS.g_signal_lookup (OS.row_changed, GTK.gtk_tree_model_get_type ());
		/* A provider can nest SetData, replace the model, dispose the Tree, or throw. */
		long signalModel = OS.g_object_ref (modelHandle);
		long signalData = handle;
		OS.g_signal_handlers_block_matched (signalModel, mask, signal_id, 0, 0, 0, signalData);
		TreeItem previousItem = currentItem;
		boolean previousSettingData = item.settingData;
		currentItem = item;
		item.settingData = true;
		try {
			sendEvent (SWT.SetData, event);
		} finally {
			item.settingData = previousSettingData;
			currentItem = previousItem;
			OS.g_signal_handlers_unblock_matched (signalModel, mask, signal_id, 0, 0, 0, signalData);
			OS.g_object_unref (signalModel);
		}
        //widget could be disposed at this point
        if (isDisposed()) {
            return false;
        }
        if (item.isDisposed()) {
            return false;
        }
	}
	return true;
}

@Override
protected void checkSubclass () {
    if (!isValidSubclass()) {
        error(SWT.ERROR_INVALID_SUBCLASS);
    }
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the user changes the receiver's selection, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * When <code>widgetSelected</code> is called, the item field of the event object is valid.
 * If the receiver has the <code>SWT.CHECK</code> style and the check selection changes,
 * the event object detail field contains the value <code>SWT.CHECK</code>.
 * <code>widgetDefaultSelected</code> is typically called when an item is double-clicked.
 * The item field of the event object is valid for default selection, but the detail field is not used.
 * </p>
 *
 * @param listener the listener which should be notified when the user changes the receiver's selection
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SelectionListener
 * @see #removeSelectionListener
 * @see SelectionEvent
 */
public void addSelectionListener (SelectionListener listener) {
	addTypedListener(listener, SWT.Selection, SWT.DefaultSelection);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when an item in the receiver is expanded or collapsed
 * by sending it one of the messages defined in the <code>TreeListener</code>
 * interface.
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see TreeListener
 * @see #removeTreeListener
 */
public void addTreeListener(TreeListener listener) {
	addTypedListener(listener, SWT.Expand, SWT.Collapse);
}


/**
 * Constant indicating that a bulk expansion or collapse operation applies to
 * every reachable level.
 *
 * @since 3.136
 */
public static final int ALL_LEVELS = TreeExpansionModel.ALL_LEVELS;

/**
 * Expands all reachable items in the receiver in one redraw-bounded operation.
 *
 * @since 3.136
 */
public void expandAll () {
	checkWidget ();
	TreeExpansionModel.expandAll (this);
}

/**
 * Collapses all currently projected items in the receiver in one
 * redraw-bounded operation.
 *
 * @since 3.136
 */
public void collapseAll () {
	checkWidget ();
	TreeExpansionModel.collapseAll (this);
}

/**
 * Expands the receiver to the given level. Levels are relative to the
 * receiver's implicit root, matching the long-standing JFace tree-viewer
 * convention. Use {@link #ALL_LEVELS} to expand every reachable level.
 *
 * @param level a non-negative level or {@link #ALL_LEVELS}
 * @since 3.136
 */
public void expandToLevel (int level) {
	checkWidget ();
	TreeExpansionModel.expandToLevel (this, level);
}

/**
 * Collapses the receiver to the given level. Levels are relative to the
 * receiver's implicit root. Use {@link #ALL_LEVELS} to collapse all levels.
 *
 * @param level a non-negative level or {@link #ALL_LEVELS}
 * @since 3.136
 */
public void collapseToLevel (int level) {
	checkWidget ();
	TreeExpansionModel.collapseToLevel (this, level);
}

/**
 * Expands the subtree rooted at {@code item} to the given level.
 *
 * @param item subtree root
 * @param level a non-negative level or {@link #ALL_LEVELS}
 * @since 3.136
 */
public void expandToLevel (TreeItem item, int level) {
	checkWidget ();
	TreeExpansionModel.expandToLevel (this, item, level);
}

/**
 * Collapses the subtree rooted at {@code item} to the given level.
 *
 * @param item subtree root
 * @param level a non-negative level or {@link #ALL_LEVELS}
 * @since 3.136
 */
public void collapseToLevel (TreeItem item, int level) {
	checkWidget ();
	TreeExpansionModel.collapseToLevel (this, item, level);
}

/**
 * Expands all selected subtrees.
 *
 * @since 3.136
 */
public void expandSelection () {
	expandSelectionToLevel (ALL_LEVELS);
}

/**
 * Expands selected subtrees to the given level.
 *
 * @param level a non-negative level or {@link #ALL_LEVELS}
 * @since 3.136
 */
public void expandSelectionToLevel (int level) {
	checkWidget ();
	TreeExpansionModel.expandSelection (this, level);
}

/**
 * Collapses all selected subtrees.
 *
 * @since 3.136
 */
public void collapseSelection () {
	collapseSelectionToLevel (ALL_LEVELS);
}

/**
 * Collapses selected subtrees to the given level.
 *
 * @param level a non-negative level or {@link #ALL_LEVELS}
 * @since 3.136
 */
public void collapseSelectionToLevel (int level) {
	checkWidget ();
	TreeExpansionModel.collapseSelection (this, level);
}


/**
 * Returns the items whose expanded state is currently set in the receiver,
 * including expanded items below a collapsed ancestor.
 * <p>
 * For a virtual receiver this method does not materialize cold logical items.
 * Returned virtual item facades are pinned and retain their identity.
 * </p>
 *
 * @return the expanded items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #setExpandedItems(TreeItem[])
 * @since 3.138
 */
public TreeItem [] getExpandedItems () {
	checkWidget ();
	return TreeExpansionModel.expandedItems (this);
}

/**
 * Replaces the receiver's represented expanded-item state with the given items.
 * Items in the receiver that are currently represented but are not in the array
 * are collapsed. The operation does not materialize cold virtual children.
 * <p>
 * Items that are not in the receiver, and null entries, are ignored.
 * </p>
 *
 * @param items the items that are to remain expanded
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the array of items is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if one of the items has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #getExpandedItems()
 * @since 3.138
 */
public void setExpandedItems (TreeItem [] items) {
	checkWidget ();
	TreeExpansionModel.setExpandedItems (this, items);
}

TreeItem [] modelChildren (TreeItem parentItem, boolean materialize) {
	if (virtualTopology == null || materialize) {
		return parentItem == null ? getItems () : parentItem.getItems ();
	}
	int parentId = parentItem == null ? VirtualTreeTopology.ROOT : virtualItemId (parentItem);
	if (parentId < VirtualTreeTopology.ROOT
			|| (parentId != VirtualTreeTopology.ROOT && !virtualTopology.contains (parentId))) {
		return new TreeItem [0];
	}
	int count = 0;
	for (int id = virtualTopology.firstMaterializedChildId (parentId);
			id >= 0; id = virtualTopology.nextMaterializedSiblingId (id)) {
		if (id < items.length && items [id] != null && !items [id].isDisposed ()) {
			count++;
		}
	}
	TreeItem [] result = new TreeItem [count];
	int index = 0;
	for (int id = virtualTopology.firstMaterializedChildId (parentId);
			id >= 0; id = virtualTopology.nextMaterializedSiblingId (id)) {
		if (id < items.length) {
			TreeItem item = items [id];
			if (item != null && !item.isDisposed ()) {
				result [index++] = item;
			}
		}
	}
	return result;
}


int calculateVirtualColumnWidth (long column) {
	int width = 0;
	/* Logical counts do not imply payloads. Packing must not request cold rows. */
	for (TreeItem item : items) {
		if (item != null && item.isCachedState ()) {
			width = Math.max (width, calculateWidth (column, item.handle, false));
		}
	}
	return width;
}

int calculateWidth (long column, long iter, boolean recurse) {
	GTK.gtk_tree_view_column_cell_set_cell_data (column, modelHandle, iter, false, false);
	/*
	* Bug in GTK.  The width calculated by gtk_tree_view_column_cell_get_size()
	* always grows in size regardless of the text or images in the table.
	* The fix is to determine the column width from the cell renderers.
	*/
	// Code intentionally commented
	//int [] width = new int [1];
	//OS.gtk_tree_view_column_cell_get_size (column, null, null, null, width, null);
	//return width [0];

	int width = 0;
	int [] w = new int [1];
	long path = 0;

	/*
	 * gtk_tree_view_get_expander_column() returns 0 if the expander column is not visible.
	 * When pack is called for the first time, the expander arrow indent is not added to
	 * the width for the expander column. The fix is to always get the expander column as if
	 * it is visible.
	 */
	long expander_column = GTK.gtk_tree_view_get_expander_column(handle);
	if (expander_column == 0 && !GTK.gtk_tree_view_column_get_visible(column)) {
		GTK.gtk_tree_view_column_set_visible(column, true);
		expander_column = GTK.gtk_tree_view_get_expander_column(handle);
		GTK.gtk_tree_view_column_set_visible(column, false);
	}
	if (expander_column == column) {
		/* indent */
		GdkRectangle rect = new GdkRectangle ();
		GTK.gtk_widget_realize (handle);
		path = viewPath (iter);
		if (usesBoundedVirtualView ()) {
			TreeItem item = _getItem (iter);
			if (item != null) virtualCellArea (item, path, column, rect);
		} else {
			GTK.gtk_tree_view_get_cell_area (handle, path, column, rect);
		}
		width += rect.x;
		/* expander */
		if (!GTK.gtk_tree_view_column_get_visible(column)) {
			if (GTK.GTK4) {
				long image = GTK4.gtk_image_new_from_icon_name(GTK.GTK_NAMED_ICON_PAN_DOWN);
				GtkRequisition requisition = new GtkRequisition ();
				GTK.gtk_widget_get_preferred_size(image, requisition, null);
				width += requisition.width + TreeItem.EXPANDER_EXTRA_PADDING;
			} else {
				GTK3.gtk_widget_style_get (handle, OS.expander_size, w, 0);
				width += w [0] + TreeItem.EXPANDER_EXTRA_PADDING;
			}
		}
	}
	/*
	 * Focus line width is done via CSS in GTK4, and does not contribute
	 * to the size of the widget.
	 */
	if (!GTK.GTK4) {
		GTK3.gtk_widget_style_get(handle, OS.focus_line_width, w, 0);
		width += 2 * w [0];
	}
	long list = GTK.gtk_cell_layout_get_cells(column);
    if (list == 0) {
		if (path != 0) GTK.gtk_tree_path_free (path);
        return 0;
    }
	long temp = list;
	while (temp != 0) {
		long renderer = OS.g_list_data (temp);
		if (renderer != 0) {
			gtk_cell_renderer_get_preferred_size (renderer, handle, w, null);
			width += w [0];
		}
		temp = OS.g_list_next (temp);
	}
	OS.g_list_free (list);

	if (recurse && (style & SWT.VIRTUAL) == 0) {
        if (path == 0) {
            path = GTK.gtk_tree_model_get_path(modelHandle, iter);
        }
		boolean expanded = GTK.gtk_tree_view_row_expanded (handle, path);
		if (expanded) {
			long childIter = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
			boolean valid = GTK.gtk_tree_model_iter_children (modelHandle, childIter, iter);
			while (valid) {
				width = Math.max (width, calculateWidth (column, childIter, true));
				valid = GTK.gtk_tree_model_iter_next (modelHandle, childIter);
			}
			OS.g_free (childIter);
		}
	}

    if (path != 0) {
        GTK.gtk_tree_path_free(path);
    }
	if (GTK.gtk_tree_view_get_grid_lines(handle) > GTK.GTK_TREE_VIEW_GRID_LINES_NONE) {
		/*
		 * Grid line width is handled via CSS in GTK4.
		 */
		if (!GTK.GTK4) {
			GTK3.gtk_widget_style_get (handle, OS.grid_line_width, w, 0) ;
			width += 2 * w [0];
		}
	}
	return width;
}

/**
 * Clears the item at the given zero-relative index in the receiver.
 * The text, icon and other attributes of the item are set to the default
 * value.  If the tree was created with the <code>SWT.VIRTUAL</code> style,
 * these attributes are requested again as needed.
 *
 * @param index the index of the item to clear
 * @param all <code>true</code> if all child items of the indexed item should be
 * cleared recursively, and <code>false</code> otherwise
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SWT#VIRTUAL
 * @see SWT#SetData
 *
 * @since 3.2
 */
public void clear(int index, boolean all) {
	checkWidget ();
	clear (0, index, all);
}

void clear (long parentIter, int index, boolean all) {
	long iter = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
	GTK.gtk_tree_model_iter_nth_child(modelHandle, iter, parentIter, index);
	int[] value = new int[1];
	GTK.gtk_tree_model_get (modelHandle, iter, ID_COLUMN, value, -1);
	if (value [0] != -1) {
		TreeItem item = items [value [0]];
		item.clear ();
	}
    if (all) {
        clearAll(all, iter);
    }
	OS.g_free (iter);
}

/**
 * Clears all the items in the receiver. The text, icon and other
 * attributes of the items are set to their default values. If the
 * tree was created with the <code>SWT.VIRTUAL</code> style, these
 * attributes are requested again as needed.
 *
 * @param all <code>true</code> if all child items should be cleared
 * recursively, and <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SWT#VIRTUAL
 * @see SWT#SetData
 *
 * @since 3.2
 */
public void clearAll (boolean all) {
	checkWidget ();
	clearAll (all, 0);
}
void clearAll (boolean all, long parentIter) {
	int length = GTK.gtk_tree_model_iter_n_children (modelHandle, parentIter);
    if (length == 0) {
        return;
    }
	long iter = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
	boolean valid = GTK.gtk_tree_model_iter_children (modelHandle, iter, parentIter);
	int[] value = new int[1];
	while (valid) {
		GTK.gtk_tree_model_get (modelHandle, iter, ID_COLUMN, value, -1);
		if (value [0] != -1) {
			TreeItem item = items [value [0]];
			item.clear ();
		}
        if (all) {
            clearAll(all, iter);
        }
		valid = GTK.gtk_tree_model_iter_next (modelHandle, iter);
	}
	OS.g_free (iter);
}

@Override
Point computeSizeInPixels (int wHint, int hHint, boolean changed) {
	checkWidget ();
    if (wHint != SWT.DEFAULT && wHint < 0) {
        wHint = 0;
    }
    if (hHint != SWT.DEFAULT && hHint < 0) {
        hHint = 0;
    }
	/*
	 * Bug 546490: Set all the TreeColumn buttons visible otherwise
	 * gtk_widget_get_preferred_size() will not take their size
	 * into account.
	 */
	if (!GTK.GTK4) {
		if (firstCompute) {
			for (TreeColumn column : columns) {
                if (column != null) {
                    GTK.gtk_widget_set_visible(column.buttonHandle, true);
                }
			}
			firstCompute = false;
		}
	}

	GTK.gtk_widget_realize(handle);
	Point size = computeNativeSize (handle, wHint, hHint, changed);

	/*
	 * In GTK 3, computeNativeSize(..) sometimes just returns the header
	 * height as height. In that case, calculate the tree height based on
	 * the number of items at the root of the tree.
	 */
	if (hHint == SWT.DEFAULT && size.y == getHeaderHeight()) {
		int itemHeight = getItemHeight();
		long visibleRows;
		if (virtualTopology != null && virtualTopology.childCountKnown (VirtualTreeTopology.ROOT)) {
			visibleRows = virtualTopology.visibleRowCount ();
		} else {
			visibleRows = getItemCount ();
		}
		long logicalHeight = Math.addExact (
				Math.multiplyExact (visibleRows, itemHeight), getHeaderHeight ());
		size.y = (int)Math.min (Integer.MAX_VALUE, logicalHeight);
	}

	/*
	 * Single column Trees may return a very small initial width if the tree has not
	 * yet been rendered. Measure populated virtual rows without traversing the
	 * logical domain; regular Trees walk their root items and expanded children.
	 */
	if (wHint == SWT.DEFAULT && size.x == 0 && columnCount == 0) {
		long col = GTK.gtk_tree_view_get_column(handle, 0);
		if (col != 0) {
			if ((style & SWT.VIRTUAL) != 0) {
				size.x = calculateVirtualColumnWidth (col);
			} else {
				long iter = OS.g_malloc(GTK.GtkTreeIter_sizeof());
				try {
					boolean valid = GTK.gtk_tree_model_iter_children(modelHandle, iter, 0);
					while (valid) {
						size.x = Math.max(size.x, calculateWidth(col, iter, true));
						valid = GTK.gtk_tree_model_iter_next(modelHandle, iter);
					}
				} finally {
					OS.g_free(iter);
				}
			}
		}
	}
	Rectangle trim = computeTrimInPixels (0, 0, size.x, size.y);
	size.x = trim.width;
	/*
	 * Feature in GTK: sometimes GtkScrolledWindow's with no scrollbars
	 * won't automatically adjust their size. This happens when a Tree
	 * has a header, and the initial computed height was the height of
	 * the of the header.
	 *
	 *  The fix is to increment the height by 1 in order to force a size
	 *  update for the parent GtkScrollWindow, otherwise the headers
	 *  will not be shown. This only happens once, see bug 546490.
	 */
	if (size.y == this.headerHeight && this.headerVisible && (style & SWT.NO_SCROLL) != 0) {
		trim.height = trim.height + 1;
	}
	size.y = trim.height;
	return size;
}

void copyModel (long oldModel, int oldStart, long newModel, int newStart, long oldParent, long newParent, int modelLength) {
	long iter = OS.g_malloc(GTK.GtkTreeIter_sizeof ());
	long value = OS.g_malloc (OS.GValue_sizeof ());
	// GValue needs to be initialized with G_VALUE_INIT, which is zeroes
	C.memset (value, 0, OS.GValue_sizeof ());

	if (GTK.gtk_tree_model_iter_children (oldModel, iter, oldParent))  {
		long [] oldItems = new long [GTK.gtk_tree_model_iter_n_children (oldModel, oldParent)];
		int oldIndex = 0;
		int [] intBuffer = new int [1];
		do {
			long newIterator = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
            if (newIterator == 0) {
                error(SWT.ERROR_NO_HANDLES);
            }
			GTK.gtk_tree_store_append (newModel, newIterator, newParent);
			GTK.gtk_tree_model_get (oldModel, iter, ID_COLUMN, intBuffer, -1);
			int index = intBuffer[0];
			TreeItem item = null;
			if (index != -1) {
				item = items [index];
				if (item != null) {
					long oldIterator = item.handle;
					oldItems[oldIndex++] = oldIterator;

					// Copy header fields
					for (int iColumn = 0; iColumn < FIRST_COLUMN; iColumn++) {
						GTK.gtk_tree_model_get_value (oldModel, oldIterator, iColumn, value);
						GTK.gtk_tree_store_set_value (newModel, newIterator, iColumn, value);
						OS.g_value_unset (value);
					}

					// Copy requested columns
					for (int iOffset = 0; iOffset < modelLength - FIRST_COLUMN; iOffset++) {
						GTK.gtk_tree_model_get_value (oldModel, oldIterator, oldStart + iOffset, value);
						GTK.gtk_tree_store_set_value (newModel, newIterator, newStart + iOffset, value);
						OS.g_value_unset (value);
					}
				}
			} else {
				GTK.gtk_tree_store_set (newModel, newIterator, ID_COLUMN, -1, -1);
			}
			// recurse through children
			copyModel(oldModel, oldStart, newModel, newStart, iter, newIterator, modelLength);

			if (item!= null) {
				item.handle = newIterator;
			} else {
				OS.g_free (newIterator);
			}
		} while (GTK.gtk_tree_model_iter_next(oldModel, iter));
		for (int i = 0; i < oldItems.length; i++) {
			long oldItem = oldItems [i];
			if (oldItem != 0) {
				GTK.gtk_tree_store_remove (oldModel, oldItem);
				OS.g_free (oldItem);
			}
		}
	}

	OS.g_free (value);
	OS.g_free (iter);
}

void createColumn (TreeColumn column, int index) {
/*
* Bug in ATK. For some reason, ATK segments fault if
* the GtkTreeView has a column and does not have items.
* The fix is to insert the column only when an item is
* created.
*/

	int modelIndex = FIRST_COLUMN;
	if (usesVirtualNativeModel ()) {
		for (int i = 0; i < columnCount; i++) {
			if (columns [i] != null) {
				modelIndex = Math.max (modelIndex, columns [i].modelIndex + CELL_TYPES);
			}
		}
	} else if (columnCount != 0) {
		int modelLength = GTK.gtk_tree_model_get_n_columns (modelHandle);
		boolean [] usedColumns = new boolean [modelLength];
		for (int i=0; i<columnCount; i++) {
			int columnIndex = columns [i].modelIndex;
			for (int j = 0; j < CELL_TYPES; j++) {
				usedColumns [columnIndex + j] = true;
			}
		}
		while (modelIndex < modelLength) {
			if (!usedColumns [modelIndex]) break;
			modelIndex++;
		}
		if (modelIndex == modelLength) {
			long oldModel = modelHandle;
			long [] types = getColumnTypes (columnCount + 4);
			long newModel = GTK.gtk_tree_store_newv (types.length, types);
			if (newModel == 0) error(SWT.ERROR_NO_HANDLES);
			copyModel (oldModel, FIRST_COLUMN, newModel, FIRST_COLUMN, (long )0, (long )0, modelLength);
			GTK.gtk_tree_view_set_model (handle, newModel);
			setModel (newModel);
		}
	}
	long columnHandle = GTK.gtk_tree_view_column_new ();
    if (columnHandle == 0) {
        error(SWT.ERROR_NO_HANDLES);
    }
	if (index == 0 && columnCount > 0) {
		TreeColumn checkColumn = columns [0];
		createRenderers (checkColumn.handle, checkColumn.modelIndex, false, checkColumn.style);
	}
	createRenderers (columnHandle, modelIndex, index == 0, column == null ? 0 : column.style);
	if ((style & SWT.VIRTUAL) == 0 && columnCount == 0) {
		GTK.gtk_tree_view_column_set_sizing (columnHandle, GTK.GTK_TREE_VIEW_COLUMN_GROW_ONLY);
	} else {
		GTK.gtk_tree_view_column_set_sizing (columnHandle, GTK.GTK_TREE_VIEW_COLUMN_FIXED);
	}
	GTK.gtk_tree_view_column_set_resizable (columnHandle, true);
	GTK.gtk_tree_view_column_set_clickable (columnHandle, true);
	GTK.gtk_tree_view_column_set_min_width (columnHandle, 0);
	GTK.gtk_tree_view_insert_column (handle, columnHandle, index);
    /*
    * Bug in GTK3.  The column header has the wrong CSS styling if it is hidden
    * when inserting to the tree widget.  The fix is to hide the column only
    * after it is inserted.
    */
    if (columnCount != 0) {
        GTK.gtk_tree_view_column_set_visible(columnHandle, false);
    }
	if (column != null) {
		column.handle = columnHandle;
		column.modelIndex = modelIndex;
	}
	if (!searchEnabled () || usesVirtualNativeModel ()) {
		GTK.gtk_tree_view_set_search_column (handle, -1);
	} else {
		/* Set the search column whenever the model changes */
		int firstColumn = columnCount == 0 ? FIRST_COLUMN : columns [0].modelIndex;
		GTK.gtk_tree_view_set_search_column (handle, firstColumn + CELL_TEXT);
	}
}

@Override
void createHandle (int index) {
	state |= HANDLE;
	fixedHandle = OS.g_object_new (display.gtk_fixed_get_type (), 0);
    if (fixedHandle == 0) {
        error(SWT.ERROR_NO_HANDLES);
    }
	if (GTK.GTK4) {
		scrolledHandle = GTK4.gtk_scrolled_window_new();
	} else {
		GTK3.gtk_widget_set_has_window(fixedHandle, true);
		scrolledHandle = GTK3.gtk_scrolled_window_new (0, 0);
	}
    if (scrolledHandle == 0) {
        error(SWT.ERROR_NO_HANDLES);
    }
	if (usesVirtualNativeModel ()) {
		long modelType = OS.content_providers_create_gtype ("SwtVirtualTreeModel");
		modelHandle = modelType != 0 ? OS.g_object_new (modelType, 0) : 0;
	} else {
		long [] types = getColumnTypes (1);
		modelHandle = GTK.gtk_tree_store_newv (types.length, types);
	}
    if (modelHandle == 0) {
        error(SWT.ERROR_NO_HANDLES);
    }
	if (usesVirtualNativeModel ()) {
		long modelType = OS.content_providers_create_gtype ("SwtVirtualTreeModel");
		virtualViewModel = modelType != 0 ? OS.g_object_new (modelType, 0) : 0;
		if (virtualViewModel == 0) error (SWT.ERROR_NO_HANDLES);
	}
	handle = GTK.gtk_tree_view_new_with_model (viewModel ());
    if (handle == 0) {
        error(SWT.ERROR_NO_HANDLES);
    }
	if ((style & SWT.CHECK) != 0) {
		checkRenderer = GTK.gtk_cell_renderer_toggle_new ();
        if (checkRenderer == 0) {
            error(SWT.ERROR_NO_HANDLES);
        }
		OS.g_object_ref (checkRenderer);
	}
	createColumn (null, 0);

	if (GTK.GTK4) {
		OS.swt_fixed_add(fixedHandle, scrolledHandle);
		GTK4.gtk_scrolled_window_set_child(scrolledHandle, handle);
	} else {
		GTK3.gtk_container_add (fixedHandle, scrolledHandle);
		GTK3.gtk_container_add (scrolledHandle, handle);
	}
	if (usesBoundedVirtualView ()) {
		verticalAdjustment = GTK.gtk_scrolled_window_get_vadjustment (scrolledHandle);
		virtualViewAdjustment = GTK.gtk_adjustment_new (0, 0, 0, 0, 0, 0);
		if (virtualViewAdjustment == 0) error (SWT.ERROR_NO_HANDLES);
		OS.g_object_ref_sink (virtualViewAdjustment);
		/* Parenting forwards the scrolled-window adjustment to its child. Split
		 * them afterwards. The owner keeps long logical rows; the native
		 * ScrollBar remains pixel-valued in normal models and switches to a
		 * bounded integer projection beyond its exact row range. */
		OS.g_object_set (handle, Converter.wcsToMbcs ("vadjustment", true), virtualViewAdjustment, 0);
	}

	int mode = (style & SWT.MULTI) != 0 ? GTK.GTK_SELECTION_MULTIPLE : GTK.GTK_SELECTION_BROWSE;
	long selectionHandle = GTK.gtk_tree_view_get_selection (handle);
	GTK.gtk_tree_selection_set_mode (selectionHandle, mode);
	GTK.gtk_tree_view_set_headers_visible (handle, false);
	int hsp = (style & SWT.H_SCROLL) != 0 ? GTK.GTK_POLICY_AUTOMATIC : GTK.GTK_POLICY_NEVER;
	int vsp = (style & SWT.V_SCROLL) != 0 ? GTK.GTK_POLICY_AUTOMATIC : GTK.GTK_POLICY_NEVER;
	GTK.gtk_scrolled_window_set_policy (scrolledHandle, hsp, vsp);
	if ((style & SWT.BORDER) != 0) {
		if (GTK.GTK4) {
			GTK4.gtk_scrolled_window_set_has_frame(scrolledHandle, true);
		} else {
			GTK3.gtk_scrolled_window_set_shadow_type (scrolledHandle, GTK.GTK_SHADOW_ETCHED_IN);
		}
	}
	/*
	 * We enable fixed-height-mode for performance reasons (see bug 490203).
	 */
	if ((style & SWT.VIRTUAL) != 0) {
		OS.g_object_set (handle, OS.fixed_height_mode, true, 0);
	}
	if (!searchEnabled ()) {
		GTK.gtk_tree_view_set_search_column (handle, -1);
	}

	if (GTK.GTK4) {
		/*
		 * GTK renders the drop highlight requested through
		 * gtk_tree_view_set_drag_dest_row() from the private TreeViewDragInfo struct,
		 * but only gtk_tree_view_enable_model_drag_dest() ever allocates it and the
		 * snapshot code dereferences it without a NULL check. Driving the highlight
		 * ourselves, as setInsertMark() and TreeDropTargetEffect do, would therefore
		 * crash on the next repaint, so allocate the struct up front. The drop target
		 * GTK installs alongside it gets an empty format list and no actions, which
		 * makes it reject every drag so that GTK's own tree view drag handlers never
		 * compete with SWT's DropTarget.
		 */
		long formats = GTK4.gdk_content_formats_builder_free_to_formats(GTK4.gdk_content_formats_builder_new());
		GTK4.gtk_tree_view_enable_model_drag_dest(handle, formats, 0);
		GTK4.gdk_content_formats_unref(formats);
	}
}

@Override
int applyThemeBackground () {
	return -1; /* No Change */
}

void createItem (TreeColumn column, int index) {
    if (!(0 <= index && index <= columnCount)) {
        error(SWT.ERROR_INVALID_RANGE);
    }
	if (index == 0) {
		// first column must be left aligned
		column.style &= ~(SWT.LEFT | SWT.RIGHT | SWT.CENTER);
		column.style |= SWT.LEFT;
	}
	if (columnCount == 0) {
		column.handle = GTK.gtk_tree_view_get_column (handle, 0);
		GTK.gtk_tree_view_column_set_sizing (column.handle, GTK.GTK_TREE_VIEW_COLUMN_FIXED);
		GTK.gtk_tree_view_column_set_visible (column.handle, false);
		column.modelIndex = FIRST_COLUMN;
		createRenderers (column.handle, column.modelIndex, true, column.style);
		column.customDraw = firstCustomDraw;
		firstCustomDraw = false;
	} else {
		createColumn (column, index);
	}
	long boxHandle = gtk_box_new (GTK.GTK_ORIENTATION_HORIZONTAL, false, 3);
    if (boxHandle == 0) {
        error(SWT.ERROR_NO_HANDLES);
    }
	long labelHandle = GTK.gtk_label_new_with_mnemonic (null);
    if (labelHandle == 0) {
        error(SWT.ERROR_NO_HANDLES);
    }
	long imageHandle = GTK.gtk_image_new ();
    if (imageHandle == 0) {
        error(SWT.ERROR_NO_HANDLES);
    }

	if (GTK.GTK4) {
		GTK4.gtk_box_append(boxHandle, imageHandle);
		GTK4.gtk_box_append(boxHandle, labelHandle);

		gtk_widget_hide(imageHandle);
	} else {
		GTK3.gtk_container_add (boxHandle, imageHandle);
		GTK3.gtk_container_add (boxHandle, labelHandle);

		gtk_widget_show (boxHandle);
		gtk_widget_show (labelHandle);
	}

	column.labelHandle = labelHandle;
	column.imageHandle = imageHandle;
	GTK.gtk_tree_view_column_set_widget (column.handle, boxHandle);
	column.buttonHandle = GTK.gtk_tree_view_column_get_button(column.handle);
	GTK.gtk_widget_set_focus_on_click(column.buttonHandle, false);
	if (columnCount == columns.length) {
		TreeColumn [] newColumns = new TreeColumn [columns.length + 4];
		System.arraycopy (columns, 0, newColumns, 0, columns.length);
		columns = newColumns;
	}
	System.arraycopy (columns, index, columns, index + 1, columnCount++ - index);
	columns [index] = column;
	if ((state & FONT) != 0) {
		long fontDesc = getFontDescription ();
		column.setFontDescription (fontDesc);
		OS.pango_font_description_free (fontDesc);
	}
	if (columnCount >= 1) {
		for (int i=0; i<items.length; i++) {
			TreeItem item = items [i];
			if (item != null) {
				if ((style & SWT.VIRTUAL) != 0) {
					item.insertVirtualColumn (index, columnCount);
				} else {
					Font [] cellFont = item.cellFont;
					if (cellFont != null) {
						Font [] temp = new Font [columnCount];
						System.arraycopy (cellFont, 0, temp, 0, index);
						System.arraycopy (cellFont, index, temp, index+1, columnCount-index-1);
						item.cellFont = temp;
					}
					Image [] cellImage = item.cellImage;
					boolean keepImages = columnCount == 1 && cellImage != null && cellImage.length == columnCount;
					if (cellImage != null && !keepImages) {
						Image [] temp = new Image [columnCount];
						System.arraycopy (cellImage, 0, temp, 0, index);
						System.arraycopy (cellImage, index, temp, index+1, columnCount-index-1);
						item.cellImage = temp;
					}
					String [] strings = item.strings;
					if (strings != null) {
						String [] temp = new String [columnCount];
						System.arraycopy (strings, 0, temp, 0, index);
						System.arraycopy (strings, index, temp, index+1, columnCount-index-1);
						temp [index] = "";
						item.strings = temp;
					}
				}
			}
		}
	}

	updateHeaderCSS();
}

/**
 * The fastest way to insert many items is documented in {@link TreeItem#TreeItem(org.eclipse.swt.widgets.Tree,int,int)}
 * and {@link TreeItem#setItemCount}
 */
void createItem (TreeItem item, long parentIter, int index) {
	int topologyParentId = VirtualTreeTopology.ROOT;
	int logicalIndex = index;
	if (virtualTopology != null) {
		topologyParentId = virtualParentId (parentIter);
		int logicalCount = virtualChildCount (parentIter);
		logicalIndex = index == -1 ? logicalCount : index;
		if (logicalIndex < 0 || logicalIndex > logicalCount) error (SWT.ERROR_INVALID_RANGE);
	}

	if (usesVirtualNativeModel ()) {
		VirtualNativeViewState state = captureVirtualNativeViewState ();
		int oldRootCount = virtualTopology.childCountKnown (VirtualTreeTopology.ROOT)
				? virtualTopology.childCount (VirtualTreeTopology.ROOT) : 0;
		int id = findAvailableId ();
		nextId = id + 1;
		item.handle = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
		if (item.handle == 0) error (SWT.ERROR_NO_HANDLES);
		virtualTopology.insertCoordinate (topologyParentId, logicalIndex, id);
		if (usesBoundedVirtualView () && virtualSelections.containsKey (topologyParentId)) virtualSelections.get (topologyParentId).insert (logicalIndex, 1);
		items [id] = item;
		virtualTopology.flag (id, VirtualItemState.PINNED, true);
		modelChanged = true;
		finishVirtualNativeMutation (state);
		if (topologyParentId == VirtualTreeTopology.ROOT && oldRootCount == 0) {
			Event event = new Event ();
			event.detail = 0;
			sendEvent (SWT.EmptinessChanged, event);
		}
		return;
	}

	if (virtualTopology != null) ensureVirtualNativeChildren (parentIter, logicalIndex);
	if (index == 0) {
		item.handle = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
		if (item.handle == 0) error (SWT.ERROR_NO_HANDLES);
		GTK.gtk_tree_store_prepend (modelHandle, item.handle, parentIter);
	} else if (index == -1) {
		item.handle = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
		if (item.handle == 0) error (SWT.ERROR_NO_HANDLES);
		GTK.gtk_tree_store_append (modelHandle, item.handle, parentIter);
	} else {
		int count = GTK.gtk_tree_model_iter_n_children (modelHandle, parentIter);
		if (!(0 <= index && index <= count)) error (SWT.ERROR_INVALID_RANGE);
		item.handle = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
		if (item.handle == 0) error (SWT.ERROR_NO_HANDLES);
		if (index == count) {
			GTK.gtk_tree_store_append (modelHandle, item.handle, parentIter);
		} else {
			GTK.gtk_tree_store_insert (modelHandle, item.handle, parentIter, index);
		}
	}

	int id = getId (item.handle, false);
	items [id] = item;
	if (virtualTopology != null) {
		item.virtualId = id;
		virtualTopology.insertCoordinate (topologyParentId, logicalIndex, id);
		if (usesBoundedVirtualView () && virtualSelections.containsKey (topologyParentId)) virtualSelections.get (topologyParentId).insert (logicalIndex, 1);
		pinVirtualFacade (item);
	}
	modelChanged = true;
	if (parentIter == 0 && GTK.gtk_tree_model_iter_n_children (modelHandle, 0) == 1) {
		Event event = new Event ();
		event.detail = 0;
		sendEvent (SWT.EmptinessChanged, event);
	}
}

void createRenderers (long columnHandle, int modelIndex, boolean check, int columnStyle) {
	GTK.gtk_tree_view_column_clear (columnHandle);
	boolean logicalVirtual = usesVirtualNativeModel ();
	if ((style & SWT.CHECK) != 0 && check) {
		GTK.gtk_tree_view_column_pack_start (columnHandle, checkRenderer, false);
		if (!logicalVirtual) {
			GTK.gtk_tree_view_column_add_attribute (columnHandle, checkRenderer, OS.active, CHECKED_COLUMN);
			GTK.gtk_tree_view_column_add_attribute (columnHandle, checkRenderer, OS.inconsistent, GRAYED_COLUMN);
	        if (!isOwnerDrawn) {
	            GTK.gtk_tree_view_column_add_attribute(columnHandle, checkRenderer, OS.cell_background_rgba, BACKGROUND_COLUMN);
	        }
		}
		if ((style & SWT.VIRTUAL) != 0 || isOwnerDrawn) {
			GTK.gtk_tree_view_column_set_cell_data_func (columnHandle, checkRenderer, display.cellDataProc, handle, 0);
			OS.g_object_set_qdata (checkRenderer, Display.SWT_OBJECT_INDEX1, columnHandle);
		}
	}

	long pixbufType = display.gtk_cell_renderer_pixbuf_get_type ();
	long pixbufRenderer = isOwnerDrawn && pixbufType != 0 ? OS.g_object_new (pixbufType, 0) : GTK.gtk_cell_renderer_pixbuf_new ();

	if (pixbufRenderer == 0) {
		error (SWT.ERROR_NO_HANDLES);
	} else {
		// set default size this size is used for calculating the icon and text positions in a tree
		if ((!isOwnerDrawn)) {
			/*
			 * When SWT.VIRTUAL is specified, size the pixbuf renderer
			 * according to the size of the first image set. If no image
			 * is set, specify a size of 0x0 like for all other Tree
			 * styles. Fix for bug 480261.
			 */
			if ((style & SWT.VIRTUAL) != 0 && pixbufSizeSet)  {
				GTK.gtk_cell_renderer_set_fixed_size(pixbufRenderer, pixbufHeight, pixbufWidth);
			} else {
				/*
				 * For all other styles, set render size to 0x0 until we
				 * actually add images, fix for bugs 469277 & 476419.
				 */
				GTK.gtk_cell_renderer_set_fixed_size(pixbufRenderer, 0, 0);
			}
		}
	}
	long textRenderer = isOwnerDrawn ? OS.g_object_new (display.gtk_cell_renderer_text_get_type (), 0) : GTK.gtk_cell_renderer_text_new ();
    if (textRenderer == 0) {
        error(SWT.ERROR_NO_HANDLES);
    }

	if (isOwnerDrawn) {
		OS.g_object_set_qdata (pixbufRenderer, Display.SWT_OBJECT_INDEX1, columnHandle);
		OS.g_object_set_qdata (textRenderer, Display.SWT_OBJECT_INDEX1, columnHandle);
	}

	/*
	* Feature in GTK.  When a tree view column contains only one activatable
	* cell renderer such as a toggle renderer, mouse clicks anywhere in a cell
	* activate that renderer. The workaround is to set a second  cell renderer
	* to be activatable.
	*/
	if ((style & SWT.CHECK) != 0 && check) {
		OS.g_object_set (pixbufRenderer, OS.mode, GTK.GTK_CELL_RENDERER_MODE_ACTIVATABLE, 0);
	}

	/* Set alignment */
	if ((columnStyle & SWT.RIGHT) != 0) {
		OS.g_object_set(textRenderer, OS.xalign, 1f, 0);
		GTK.gtk_tree_view_column_pack_end (columnHandle, textRenderer, true);
		GTK.gtk_tree_view_column_pack_end (columnHandle, pixbufRenderer, false);
		GTK.gtk_tree_view_column_set_alignment (columnHandle, 1f);
	} else if ((columnStyle & SWT.CENTER) != 0) {
		OS.g_object_set(textRenderer, OS.xalign, 0.5f, 0);
		GTK.gtk_tree_view_column_pack_start (columnHandle, pixbufRenderer, false);
		GTK.gtk_tree_view_column_pack_end (columnHandle, textRenderer, true);
		GTK.gtk_tree_view_column_set_alignment (columnHandle, 0.5f);
	} else {
		GTK.gtk_tree_view_column_pack_start (columnHandle, pixbufRenderer, false);
		GTK.gtk_tree_view_column_pack_start (columnHandle, textRenderer, true);
		GTK.gtk_tree_view_column_set_alignment (columnHandle, 0f);
	}

	/*
	 * Add attributes
	 *
	 * Formerly OS.gicon was set if on GTK3, but this is being removed do to spacing issues in Tables/Trees with
	 * no images. Fix for Bug 469277 & 476419. NOTE: this change has been ported to Tables since Tables/Trees both
	 * use the same underlying GTK structure.
	 */
	if (!logicalVirtual) {
		GTK.gtk_tree_view_column_add_attribute (columnHandle, pixbufRenderer, OS.pixbuf, modelIndex + CELL_PIXBUF);
		if (!isOwnerDrawn) {
			GTK.gtk_tree_view_column_add_attribute (columnHandle, pixbufRenderer, OS.cell_background_rgba, BACKGROUND_COLUMN);
			GTK.gtk_tree_view_column_add_attribute (columnHandle, textRenderer, OS.cell_background_rgba, BACKGROUND_COLUMN);
		}
		GTK.gtk_tree_view_column_add_attribute (columnHandle, textRenderer, OS.text, modelIndex + CELL_TEXT);
		GTK.gtk_tree_view_column_add_attribute (columnHandle, textRenderer, OS.foreground_rgba, FOREGROUND_COLUMN);
		GTK.gtk_tree_view_column_add_attribute (columnHandle, textRenderer, OS.font_desc, FONT_COLUMN);
	}

	boolean customDraw = firstCustomDraw;
	if (columnCount != 0) {
		for (int i=0; i<columnCount; i++) {
			if (columns [i].handle == columnHandle) {
				customDraw = columns [i].customDraw;
				break;
			}
		}
	}
	if (logicalVirtual || (style & SWT.VIRTUAL) != 0 || customDraw || isOwnerDrawn) {
		GTK.gtk_tree_view_column_set_cell_data_func (columnHandle, textRenderer, display.cellDataProc, handle, 0);
		GTK.gtk_tree_view_column_set_cell_data_func (columnHandle, pixbufRenderer, display.cellDataProc, handle, 0);
	}
}

@Override
void createWidget (int index) {
	viewportLayers = new ViewportLayerState ();
	super.createWidget (index);
	items = new TreeItem [4];
	if ((style & SWT.VIRTUAL) != 0) {
		virtualTopology = new VirtualTreeTopology ();
		virtualProjection = new VirtualTreeVisibleProjection (virtualTopology);
		virtualViewport = new VirtualTreeViewport (virtualProjection);
	}
	columns = new TreeColumn [4];
	columnCount = 0;
	initializeViewportLayers ();
	// In GTK 3 font description is inherited from parent widget which is not how SWT has always worked,
	// reset to default font to get the usual behavior
	setFontDescription(defaultFont().handle);
	initializeVirtualAccessible ();
	if (usesBoundedVirtualView ()) virtualColumnsSignal = OS.g_signal_connect (handle,
			Converter.wcsToMbcs ("columns-changed", true), virtualAdjustmentCallback.getAddress (), 4);
}

@Override
GdkRGBA defaultBackground () {
	return display.getSystemColor(SWT.COLOR_LIST_BACKGROUND).handle;
}

@Override
void deregister () {
	disconnectVirtualAdjustments ();
	super.deregister ();
	display.removeWidget (GTK.gtk_tree_view_get_selection (handle));
    if (checkRenderer != 0) {
        display.removeWidget(checkRenderer);
    }
	display.removeWidget (modelHandle);
	if (virtualViewModel != 0) display.removeWidget (virtualViewModel);
}

/**
 * Deselects an item in the receiver.  If the item was already
 * deselected, it remains deselected.
 *
 * @param item the item to be deselected
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the item is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the item has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.4
 */
public void deselect (TreeItem item) {
	checkWidget ();
    if (item == null) {
        error(SWT.ERROR_NULL_ARGUMENT);
    }
    if (item.isDisposed()) {
        error(SWT.ERROR_INVALID_ARGUMENT);
    }
	if (usesBoundedVirtualView ()) { virtualSelectItem (item, false); return; }
	boolean fixColumn = showFirstColumn ();
	long selection = GTK.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	GTK.gtk_tree_selection_unselect_iter (selection, item.handle);
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
    if (fixColumn) {
        hideFirstColumn();
    }
}

/**
 * Deselects all selected items in the receiver.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void deselectAll() {
	checkWidget();
	if (usesBoundedVirtualView ()) {
		if (virtualSelectionCount () == 0) return;
		virtualSelections.clear ();
		restoreVirtualSelection ();
		notifyVirtualAccessibleSelection ();
		return;
	}
	boolean fixColumn = showFirstColumn ();
	long selection = GTK.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	GTK.gtk_tree_selection_unselect_all (selection);
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
    if (fixColumn) {
        hideFirstColumn();
    }
}

void destroyItem (TreeColumn column) {
	int index = 0;
	while (index < columnCount) {
        if (columns [index] == column) {
            break;
        }
		index++;
	}
    if (index == columnCount) {
        return;
    }
	long columnHandle = column.handle;
	if (columnCount == 1) {
		firstCustomDraw = column.customDraw;
	}
	System.arraycopy (columns, index + 1, columns, index, --columnCount - index);
	columns [columnCount] = null;
	GTK.gtk_tree_view_remove_column (handle, columnHandle);
	if (columnCount == 0) {
		if (usesVirtualNativeModel ()) {
			createColumn (null, 0);
		} else {
			long oldModel = modelHandle;
			long [] types = getColumnTypes (1);
			long newModel = GTK.gtk_tree_store_newv (types.length, types);
			if (newModel == 0) error (SWT.ERROR_NO_HANDLES);
			copyModel(oldModel, column.modelIndex, newModel, FIRST_COLUMN, (long )0, (long )0, FIRST_COLUMN + CELL_TYPES);
			GTK.gtk_tree_view_set_model (handle, newModel);
			setModel (newModel);
			createColumn (null, 0);
		}
	} else {
		for (int i=0; i<items.length; i++) {
			TreeItem item = items [i];
			if (item != null) {
				if (!usesVirtualNativeModel ()) {
					long iter = item.handle;
					int modelIndex = column.modelIndex;
					GTK.gtk_tree_store_set (modelHandle, iter, modelIndex + CELL_PIXBUF, (long )0, -1);
					GTK.gtk_tree_store_set (modelHandle, iter, modelIndex + CELL_TEXT, (long )0, -1);
					GTK.gtk_tree_store_set (modelHandle, iter, modelIndex + CELL_FOREGROUND, (long )0, -1);
					GTK.gtk_tree_store_set (modelHandle, iter, modelIndex + CELL_BACKGROUND, (long )0, -1);
					GTK.gtk_tree_store_set (modelHandle, iter, modelIndex + CELL_FONT, (long )0, -1);
				}

				if ((style & SWT.VIRTUAL) != 0) {
					item.removeVirtualColumn (index, columnCount);
				} else {
					Font [] cellFont = item.cellFont;
					if (cellFont != null) {
						if (columnCount == 0) {
							item.cellFont = null;
						} else {
							Font [] temp = new Font [columnCount];
							System.arraycopy (cellFont, 0, temp, 0, index);
							System.arraycopy (cellFont, index + 1, temp, index, columnCount - index);
							item.cellFont = temp;
						}
					}
				}
				Image [] cellImage = item.cellImage;
				if (cellImage != null) {
					Image [] temp = new Image [columnCount];
					System.arraycopy (cellImage, 0, temp, 0, index);
					System.arraycopy (cellImage, index + 1, temp, index, columnCount - index);
					item.cellImage = temp;
				}
			}
		}
		if (index == 0) {
			// first column must be left aligned and must show check box
			TreeColumn firstColumn = columns [0];
			firstColumn.style &= ~(SWT.LEFT | SWT.RIGHT | SWT.CENTER);
			firstColumn.style |= SWT.LEFT;
			createRenderers (firstColumn.handle, firstColumn.modelIndex, true, firstColumn.style);
		}
	}
	if (!searchEnabled () || usesVirtualNativeModel ()) {
		GTK.gtk_tree_view_set_search_column (handle, -1);
	} else {
		/* Set the search column whenever the model changes */
		int firstColumn = columnCount == 0 ? FIRST_COLUMN : columns [0].modelIndex;
		GTK.gtk_tree_view_set_search_column (handle, firstColumn + CELL_TEXT);
	}
}


void destroyItem (TreeItem item) {
	if (usesVirtualNativeModel ()) {
		int topologyId = virtualItemId (item);
		int parentId = virtualTopology.parentId (topologyId);
		boolean lastRoot = parentId == VirtualTreeTopology.ROOT
				&& virtualTopology.childCount (VirtualTreeTopology.ROOT) == 1;
		int childIndex = virtualTopology.childIndex (topologyId);
		VirtualNativeViewState state = captureVirtualNativeViewState ();
		virtualTopology.releaseSubtree (topologyId);
		if (usesBoundedVirtualView () && virtualSelections.containsKey (parentId)) virtualSelections.get (parentId).remove (childIndex, 1);
		if (topologyId < items.length) items [topologyId] = null;
		modelChanged = true;
		finishVirtualNativeMutation (state);
		if (lastRoot) {
			Event event = new Event ();
			event.detail = 1;
			sendEvent (SWT.EmptinessChanged, event);
		}
		return;
	}

	int topologyId = -1;
	if (virtualTopology != null) {
		topologyId = virtualItemId (item);
	}
	long selection = GTK.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	GTK.gtk_tree_store_remove (modelHandle, item.handle);
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	if (virtualTopology != null && topologyId >= 0) virtualTopology.releaseSubtree (topologyId);
	modelChanged = true;
	if (GTK.gtk_tree_model_iter_n_children (modelHandle, 0) == 0) {
		Event event = new Event ();
		event.detail = 1;
		sendEvent (SWT.EmptinessChanged, event);
	}
}

@Override
boolean dragDetect (int x, int y, boolean filter, boolean dragOnTimeout, boolean [] consume) {
	boolean selected = false;
	if (OS.isX11()) {
		if (filter) {
			long [] path = new long [1];
			if (GTK.gtk_tree_view_get_path_at_pos (handle, x, y, path, null, null, null)) {
				if (path [0] != 0) {
					long selection = GTK.gtk_tree_view_get_selection (handle);
                    if (GTK.gtk_tree_selection_path_is_selected(selection, path [0])) {
                        selected = true;
                    }
					GTK.gtk_tree_path_free (path [0]);
				}
			} else {
				return false;
			}
		}
		boolean dragDetect = super.dragDetect (x, y, filter, false, consume);
        if (dragDetect && selected && consume != null) {
            consume [0] = true;
        }
		return dragDetect;
	} else {
		double [] startX = new double[1];
		double [] startY = new double [1];
		long [] path = new long [1];
		if (GTK.gtk_gesture_drag_get_start_point(dragGesture, startX, startY)) {
			if (getHeaderVisible()) {
				startY[0]-= getHeaderHeight();
			}
			if (GTK.gtk_tree_view_get_path_at_pos (handle, (int) startX[0], (int) startY[0], path, null, null, null)) {
				if (path [0] != 0) {
					boolean dragDetect = super.dragDetect (x, y, filter, false, consume);
                    if (dragDetect && selected && consume != null) {
                        consume [0] = true;
                    }
					return dragDetect;
				}
			} else {
				return false;
			}
		}
	}
	return false;
}


@Override
long eventWindow () {
	return paintWindow ();
}

@Override
Rectangle getClientAreaInPixels () {
	checkWidget();
	if(RESIZE_ON_GETCLIENTAREA) {
		forceResize();
	}

	long clientHandle = clientHandle();
	GtkAllocation allocation = new GtkAllocation();
	GTK.gtk_widget_get_allocation(clientHandle, allocation);
	int width = (state & ZERO_WIDTH) != 0 ? 0 : allocation.width;
	int height = (state & ZERO_HEIGHT) != 0 ? 0 : allocation.height;

	Rectangle rect;
	if (GTK.GTK4) {
		int[] headerHeight = new int[1], headerWidth = new int[1];
		GTK.gtk_tree_view_convert_bin_window_to_widget_coords(handle, 0, 0, headerWidth, headerHeight);
		rect = new Rectangle(headerWidth[0], headerHeight[0], width, height);
	} else {
		GTK.gtk_widget_realize(handle);
		long fixedWindow = gtk_widget_get_window(fixedHandle);
		long binWindow = GTK3.gtk_tree_view_get_bin_window(handle);
		int[] binX = new int[1], binY = new int[1];
		GDK.gdk_window_get_origin(binWindow, binX, binY);
		int[] fixedX = new int[1], fixedY = new int[1];
		GDK.gdk_window_get_origin(fixedWindow, fixedX, fixedY);
		rect = new Rectangle(fixedX[0] - binX[0], fixedY[0] - binY[0], width, height);
	}

	return rect;
}

@Override
int getClientWidth () {
	int [] w = new int [1], h = new int [1];
	if (GTK.GTK4) {
		long surface = gtk_widget_get_surface(handle);
		gdk_surface_get_size(surface, w, h);
	} else {
		GTK.gtk_widget_realize (handle);
		gdk_window_get_size(GTK3.gtk_tree_view_get_bin_window(handle), w, h);
	}
	return w[0];
}

/**
 * Returns the column at the given, zero-relative index in the
 * receiver. Throws an exception if the index is out of range.
 * Columns are returned in the order that they were created.
 * If no <code>TreeColumn</code>s were created by the programmer,
 * this method will throw <code>ERROR_INVALID_RANGE</code> despite
 * the fact that a single column of data may be visible in the tree.
 * This occurs when the programmer uses the tree like a list, adding
 * items but never creating a column.
 *
 * @param index the index of the column to return
 * @return the column at the given index
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Tree#getColumnOrder()
 * @see Tree#setColumnOrder(int[])
 * @see TreeColumn#getMoveable()
 * @see TreeColumn#setMoveable(boolean)
 * @see SWT#Move
 *
 * @since 3.1
 */
public TreeColumn getColumn (int index) {
	checkWidget();
    if (!(0 <= index && index < columnCount)) {
        error(SWT.ERROR_INVALID_RANGE);
    }
	return columns [index];
}

/**
 * Returns the number of columns contained in the receiver.
 * If no <code>TreeColumn</code>s were created by the programmer,
 * this value is zero, despite the fact that visually, one column
 * of items may be visible. This occurs when the programmer uses
 * the tree like a list, adding items but never creating a column.
 *
 * @return the number of columns
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.1
 */
public int getColumnCount () {
	checkWidget();
	return columnCount;
}

/**
 * Returns an array of zero-relative integers that map
 * the creation order of the receiver's items to the
 * order in which they are currently being displayed.
 * <p>
 * Specifically, the indices of the returned array represent
 * the current visual order of the items, and the contents
 * of the array represent the creation order of the items.
 * </p><p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of items, so modifying the array will
 * not affect the receiver.
 * </p>
 *
 * @return the current visual order of the receiver's items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Tree#setColumnOrder(int[])
 * @see TreeColumn#getMoveable()
 * @see TreeColumn#setMoveable(boolean)
 * @see SWT#Move
 *
 * @since 3.2
 */
public int [] getColumnOrder () {
	checkWidget ();
    if (columnCount == 0) {
        return new int [0];
    }
	long list = GTK.gtk_tree_view_get_columns (handle);
    if (list == 0) {
        return new int [0];
    }
	int  i = 0, count = OS.g_list_length (list);
	int [] order = new int [count];
	long temp = list;
	while (temp != 0) {
		long column = OS.g_list_data (temp);
		if (column != 0) {
			for (int j=0; j<columnCount; j++) {
				if (columns [j].handle == column) {
					order [i++] = j;
					break;
				}
			}
		}
		temp = OS.g_list_next (temp);
	}
	OS.g_list_free (list);
	return order;
}

long [] getColumnTypes (int columnCount) {
	long [] types = new long [FIRST_COLUMN + (columnCount * CELL_TYPES)];
	// per row data
	types [ID_COLUMN] = OS.G_TYPE_INT ();
	types [CHECKED_COLUMN] = OS.G_TYPE_BOOLEAN ();
	types [GRAYED_COLUMN] = OS.G_TYPE_BOOLEAN ();
	types [FOREGROUND_COLUMN] = GDK.GDK_TYPE_RGBA();
	types [BACKGROUND_COLUMN] = GDK.GDK_TYPE_RGBA();
	types [FONT_COLUMN] = OS.PANGO_TYPE_FONT_DESCRIPTION ();
	// per cell data
	for (int i=FIRST_COLUMN; i<types.length; i+=CELL_TYPES) {
		types [i + CELL_PIXBUF] = GDK.GDK_TYPE_PIXBUF ();
		types [i + CELL_TEXT] = OS.G_TYPE_STRING ();
		types [i + CELL_FOREGROUND] = GDK.GDK_TYPE_RGBA();
		types [i + CELL_BACKGROUND] = GDK.GDK_TYPE_RGBA();
		types [i + CELL_FONT] = OS.PANGO_TYPE_FONT_DESCRIPTION ();
		types [i + CELL_SURFACE] = OS.G_TYPE_LONG();
	}
	return types;
}

/**
 * Returns an array of <code>TreeColumn</code>s which are the
 * columns in the receiver. Columns are returned in the order
 * that they were created.  If no <code>TreeColumn</code>s were
 * created by the programmer, the array is empty, despite the fact
 * that visually, one column of items may be visible. This occurs
 * when the programmer uses the tree like a list, adding items but
 * never creating a column.
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of items, so modifying the array will
 * not affect the receiver.
 * </p>
 *
 * @return the items in the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Tree#getColumnOrder()
 * @see Tree#setColumnOrder(int[])
 * @see TreeColumn#getMoveable()
 * @see TreeColumn#setMoveable(boolean)
 * @see SWT#Move
 *
 * @since 3.1
 */
public TreeColumn [] getColumns () {
	checkWidget();
	TreeColumn [] result = new TreeColumn [columnCount];
	System.arraycopy (columns, 0, result, 0, columnCount);
	return result;
}

@Override
GdkRGBA getContextBackgroundGdkRGBA () {
	if (background != null) {
		return background;
	} else {
		// For Tables and Trees, the default background is
		// COLOR_LIST_BACKGROUND instead of COLOR_WIDGET_BACKGROUND.
		return defaultBackground();
	}
}

@Override
GdkRGBA getContextColorGdkRGBA () {
	if (foreground != null) {
		return foreground;
	} else {
		return display.COLOR_LIST_FOREGROUND_RGBA;
	}
}

TreeItem getFocusItem () {
	if (usesBoundedVirtualView ()) {
		if (virtualFocusId < 0 || !virtualTopology.contains (virtualFocusId)) {
			TreeItem nativeFocus = virtualNativeCursorItem ();
			virtualFocusId = nativeFocus != null ? virtualItemId (nativeFocus) : -1;
		}
		return virtualFocusId >= 0 && virtualFocusId < items.length ? items [virtualFocusId] : null;
	}
	long [] path = new long [1];
	GTK.gtk_tree_view_get_cursor (handle, path, null);
    if (path [0] == 0) {
        return null;
    }
	TreeItem item = null;
	long iter = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
	if (GTK.gtk_tree_model_get_iter (viewModel (), iter, path [0])) {
		int [] index = new int [1];
		GTK.gtk_tree_model_get (modelHandle, iter, ID_COLUMN, index, -1);
        if (index [0] != -1) {
            item = items [index [0]];
        } //TODO should we be creating this item when index is -1?
	}
	OS.g_free (iter);
	GTK.gtk_tree_path_free (path [0]);
	return item;
}

/**
 * Returns the width in points of a grid line.
 *
 * @return the width of a grid line in points
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.1
 */
public int getGridLineWidth () {
	checkWidget ();
	return 0;
}

/**
 * Returns the header background color.
 *
 * @return the receiver's header background color.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @since 3.106
 */
public Color getHeaderBackground () {
	checkWidget ();
	return headerBackground != null ? headerBackground : display.getSystemColor(SWT.COLOR_LIST_BACKGROUND);
}

/**
 * Returns the header foreground color.
 *
 * @return the receiver's header foreground color.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @since 3.106
 */
public Color getHeaderForeground () {
	checkWidget ();
	return headerForeground != null ? headerForeground : display.getSystemColor(SWT.COLOR_LIST_FOREGROUND);
}

/**
 * Returns the height of the receiver's header
 *
 * @return the height of the header or zero if the header is not visible
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.1
 */
public int getHeaderHeight () {
	checkWidget ();
    if (!GTK.gtk_tree_view_get_headers_visible(handle)) {
        return 0;
    }

	int height = 0;
	if (columnCount > 0) {
		GtkRequisition requisition = new GtkRequisition ();
		for (int i=0; i<columnCount; i++) {
			long buttonHandle = columns [i].buttonHandle;
			if (buttonHandle != 0) {
				gtk_widget_get_preferred_size (buttonHandle, requisition);
				height = Math.max (height, requisition.height);
			}
		}
	} else {
		if (GTK.GTK4) {
			int[] headerHeight = new int[1];
			GTK.gtk_tree_view_convert_bin_window_to_widget_coords(handle, 0, 0, null, headerHeight);
			height = headerHeight[0];
		} else {
			GTK.gtk_widget_realize (handle);
			long fixedWindow = gtk_widget_get_window (fixedHandle);
			long binWindow = GTK3.gtk_tree_view_get_bin_window (handle);
			int [] binY = new int [1];
			GDK.gdk_window_get_origin (binWindow, null, binY);
			int [] fixedY = new int [1];
			GDK.gdk_window_get_origin (fixedWindow, null, fixedY);
			height = binY [0] - fixedY [0];
		}
	}

	return height;
}

/**
 * Returns <code>true</code> if the receiver's header is visible,
 * and <code>false</code> otherwise.
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, this method
 * may still indicate that it is considered visible even though
 * it may not actually be showing.
 * </p>
 *
 * @return the receiver's header's visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.1
 */
public boolean getHeaderVisible () {
	checkWidget();
	return GTK.gtk_tree_view_get_headers_visible (handle);
}

/**
 * Returns the item at the given, zero-relative index in the
 * receiver. Throws an exception if the index is out of range.
 *
 * @param index the index of the item to return
 * @return the item at the given index
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.1
 */
public TreeItem getItem (int index) {
	checkWidget();
    if (index < 0) {
        error(SWT.ERROR_INVALID_RANGE);
    }
	ensureVirtualNativeItem (0, index);
	long iter = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
	try {
        if (!GTK.gtk_tree_model_iter_nth_child(modelHandle, iter, 0, index)) {
            error(SWT.ERROR_INVALID_RANGE);
        }
		return exposeVirtualItem (_getItem (0, iter, index));
	} finally {
		OS.g_free (iter);
	}
}

/**
 * Returns the item at the given point in the receiver
 * or null if no such item exists. The point is in the
 * coordinate system of the receiver.
 * <p>
 * The item that is returned represents an item that could be selected by the user.
 * For example, if selection only occurs in items in the first column, then null is
 * returned if the point is outside of the item.
 * Note that the SWT.FULL_SELECTION style hint, which specifies the selection policy,
 * determines the extent of the selection.
 * </p>
 *
 * @param point the point used to locate the item
 * @return the item at the given point, or null if the point is not in a selectable item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public TreeItem getItem (Point point) {
	checkWidget();
    if (point == null) {
        error(SWT.ERROR_NULL_ARGUMENT);
    }
	long [] path = new long [1];
	GTK.gtk_widget_realize (handle);
	int x = point.x;
	int y = point.y;
	/*
	 * On GTK4 the header is included in the entire widget's surface, so we must subtract
	 * its size from the y-coordinate. This does not apply on GTK3 as the header and
	 * "main-widget" have separate GdkWindows.
	 */
	if (getHeaderVisible() && GTK.GTK4) {
		y -= getHeaderHeight();
	}
    if ((style & SWT.MIRRORED) != 0) {
        x = getClientWidth() - x;
    }
	long [] columnHandle = new long [1];
    if (!GTK.gtk_tree_view_get_path_at_pos(handle, x, y, path, columnHandle, null, null)) {
        return null;
    }
    if (path [0] == 0) {
        return null;
    }
	TreeItem item = null;
	long iter = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
	if (GTK.gtk_tree_model_get_iter (viewModel (), iter, path [0])) {
		boolean overExpander = false;
		if (GTK.gtk_tree_view_get_expander_column (handle) == columnHandle [0]) {
			GdkRectangle rect = new GdkRectangle ();
			GTK.gtk_tree_view_get_cell_area (handle, path [0], columnHandle [0], rect);
			if ((style & SWT.MIRRORED) != 0) {
				overExpander = x > rect.x + rect.width;
			} else {
				overExpander = x < rect.x;
			}
		}
		if (!overExpander) {
			item = _getItem (iter);
		}
	}
	OS.g_free (iter);
	GTK.gtk_tree_path_free (path [0]);
	return exposeVirtualItem (item);
}

/**
 * Returns the number of items contained in the receiver
 * that are direct item children of the receiver.  The
 * number that is returned is the number of roots in the
 * tree.
 *
 * @return the number of items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getItemCount () {
	checkWidget ();
	return virtualChildCount (0);
}

/**
 * Returns the height of the area which would be used to
 * display <em>one</em> of the items in the tree.
 *
 * @return the height of one item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getItemHeight () {
	checkWidget ();
	int height = 0;
	long geometryModel = viewModel ();
	int itemCount = GTK.gtk_tree_model_iter_n_children(geometryModel, 0);

	if (itemCount == 0) {
		long column = GTK.gtk_tree_view_get_column(handle, 0);
		int[] h = new int[1];
		ignoreSize = true;
		if (GTK.GTK4) {
			GTK4.gtk_tree_view_column_cell_get_size(column, null, null, null, h);
		} else {
			GTK3.gtk_tree_view_column_cell_get_size(column, null, null, null, null, h);
		}

		height = h[0];
		long textRenderer = getTextRenderer(column);
        if (textRenderer != 0) {
            GTK.gtk_cell_renderer_get_preferred_height_for_width(textRenderer, handle, 0, h, null);
        }
		height += h[0];
		ignoreSize = false;
	} else {
		long iter = OS.g_malloc(GTK.GtkTreeIter_sizeof());
		GTK.gtk_tree_model_get_iter_first(geometryModel, iter);

		int columnCount = Math.max(1, this.columnCount);
		for (int i = 0; i < columnCount; i++) {
			long column = GTK.gtk_tree_view_get_column(handle, i);
			GTK.gtk_tree_view_column_cell_set_cell_data(column, geometryModel, iter, false, false);
			int[] h = new int[1];
			if (GTK.GTK4) {
				GTK4.gtk_tree_view_column_cell_get_size(column, null, null, null, h);
			} else {
				GTK3.gtk_tree_view_column_cell_get_size (column, null, null, null, null, h);
			}

			long textRenderer = getTextRenderer(column);
			int[] ypad = new int[1];
            if (textRenderer != 0) {
                GTK.gtk_cell_renderer_get_padding(textRenderer, null, ypad);
            }
			height = Math.max(height, h[0] + ypad[0]);
		}

		OS.g_free (iter);
	}

	return height;
}

/**
 * Returns a (possibly empty) array of items contained in the
 * receiver that are direct item children of the receiver.  These
 * are the roots of the tree.
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of items, so modifying the array will
 * not affect the receiver.
 * </p>
 *
 * @return the items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public TreeItem [] getItems () {
	checkWidget();
	return getItems (0);
}

TreeItem [] getItems (long parent) {
    if (virtualTopology != null) {
        ensureVirtualNativeChildren(parent, virtualChildCount(parent));
    }
	ArrayList<TreeItem> result = new ArrayList<> ();
	long iter = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
	try {
		boolean valid = GTK.gtk_tree_model_iter_children (modelHandle, iter, parent);
		while (valid) {
			result.add (exposeVirtualItem (_getItem (parent, iter, result.size ())));
			valid = GTK.gtk_tree_model_iter_next (modelHandle, iter);
		}
	} finally {
		OS.g_free (iter);
	}
	return result.toArray (new TreeItem [result.size ()]);
}

/**
 * Returns <code>true</code> if the receiver's lines are visible,
 * and <code>false</code> otherwise. Note that some platforms draw
 * grid lines while others may draw alternating row colors.
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, this method
 * may still indicate that it is considered visible even though
 * it may not actually be showing.
 * </p>
 *
 * @return the visibility state of the lines
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.1
 */
public boolean getLinesVisible() {
	checkWidget();
	return GTK.gtk_tree_view_get_grid_lines(handle) > GTK.GTK_TREE_VIEW_GRID_LINES_NONE;
}

/**
 * Returns the receiver's parent item, which must be a
 * <code>TreeItem</code> or null when the receiver is a
 * root.
 *
 * @return the receiver's parent item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public TreeItem getParentItem () {
	checkWidget ();
	return null;
}

long getPixbufRenderer (long column) {
	long list = GTK.gtk_cell_layout_get_cells(column);
    if (list == 0) {
        return 0;
    }
	long originalList = list;
	long pixbufRenderer = 0;
	while (list != 0) {
		long renderer = OS.g_list_data (list);
		if (GTK.GTK_IS_CELL_RENDERER_PIXBUF (renderer)) {
			pixbufRenderer = renderer;
			break;
		}
		list = OS.g_list_next (list);
	}
	OS.g_list_free (originalList);
	return pixbufRenderer;
}

/**
 * Returns an array of <code>TreeItem</code>s that are currently
 * selected in the receiver. The order of the items is unspecified.
 * An empty array indicates that no items are selected.
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its selection, so modifying the array will
 * not affect the receiver.
 * </p>
 * @return an array representing the selection
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public TreeItem[] getSelection () {
	checkWidget();
	if (usesBoundedVirtualView ()) return virtualSelectionItems ();
	long selection = GTK.gtk_tree_view_get_selection (handle);
	long list = GTK.gtk_tree_selection_get_selected_rows (selection, null);
    if (list == 0) {
        return new TreeItem [0];
    }
	int count = OS.g_list_length (list);
	TreeItem [] treeSelection = new TreeItem [count];
	int length = 0;
	// Paths come in tree order, so advance the previous iterators instead of the linear gtk_tree_model_get_iter()
	long [] iters = new long [4];
	int [] previous = new int [0];
	for (long l = list; l != 0; l = OS.g_list_next (l)) {
		long path = OS.g_list_data (l);
		int depth = GTK.gtk_tree_path_get_depth (path);
		int [] indices = new int [depth];
		C.memmove (indices, GTK.gtk_tree_path_get_indices (path), 4 * depth);
		GTK.gtk_tree_path_free (path);
        if (depth > iters.length) {
            iters = Arrays.copyOf(iters, depth);
        }
		int level = 0;
        while (level < depth && level < previous.length && indices [level] == previous [level]) {
            level++;
        }
		boolean found = true;
		for (int i = level; i < depth && found; i++) {
            if (iters [i] == 0) {
                iters [i] = OS.g_malloc(GTK.GtkTreeIter_sizeof());
            }
			if (i == level && i < previous.length && indices [i] > previous [i]) {
				for (int j = previous [i]; j < indices [i] && found; j++) {
					found = GTK.gtk_tree_model_iter_next (modelHandle, iters [i]);
				}
			} else {
				found = GTK.gtk_tree_model_iter_nth_child (modelHandle, iters [i], i == 0 ? 0 : iters [i - 1], indices [i]);
			}
		}
		if (found) {
			treeSelection [length++] = exposeVirtualItem (_getItem (iters [depth - 1]));
			previous = indices;
		} else {
			previous = new int [0];
		}
	}
	for (long iter : iters) {
        if (iter != 0) {
            OS.g_free(iter);
        }
	}
	OS.g_list_free (list);
	if (length < count) {
		TreeItem [] temp = new TreeItem [length];
		System.arraycopy(treeSelection, 0, temp, 0, length);
		treeSelection = temp;
	}
	return treeSelection;
}

/**
 * Returns the number of selected items contained in the receiver.
 *
 * @return the number of selected items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getSelectionCount () {
	checkWidget();
	if (usesBoundedVirtualView ()) return virtualSelectionCount ();
	long selection = GTK.gtk_tree_view_get_selection (handle);
	return GTK.gtk_tree_selection_count_selected_rows (selection);
}

/**
 * Returns the column which shows the sort indicator for
 * the receiver. The value may be null if no column shows
 * the sort indicator.
 *
 * @return the sort indicator
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #setSortColumn(TreeColumn)
 *
 * @since 3.2
 */
public TreeColumn getSortColumn () {
	checkWidget ();
	return sortColumn;
}

/**
 * Returns the direction of the sort indicator for the receiver.
 * The value will be one of <code>UP</code>, <code>DOWN</code>
 * or <code>NONE</code>.
 *
 * @return the sort direction
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #setSortDirection(int)
 *
 * @since 3.2
 */
public int getSortDirection () {
	checkWidget ();
	return sortDirection;
}

long getTextRenderer (long column) {
	long list = GTK.gtk_cell_layout_get_cells(column);
    if (list == 0) {
        return 0;
    }
	long originalList = list;
	long textRenderer = 0;
	while (list != 0) {
		long renderer = OS.g_list_data (list);
		if (GTK.GTK_IS_CELL_RENDERER_TEXT (renderer)) {
			textRenderer = renderer;
			break;
		}
		list = OS.g_list_next (list);
	}
	OS.g_list_free (originalList);
	return textRenderer;
}

/**
 * Returns the item which is currently at the top of the receiver.
 * This item can change when items are expanded, collapsed, scrolled
 * or new items are added or removed.
 *
 * @return the item at the top of the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 2.1
 */
public TreeItem getTopItem () {
	checkWidget ();
	if (usesBoundedVirtualView ()) return exposeVirtualItem (virtualVisibleItem (virtualViewport.topRow ()));
	/*
	 * Feature in GTK: fetch the topItem using the topItem global variable
	 * if setTopItem() has been called and the widget has not been scrolled
	 * using the UI. Otherwise, fetch topItem using GtkTreeView API.
	 */
	long vAdjustment;
	vAdjustment = GTK.gtk_scrollable_get_vadjustment(handle);
	currentAdjustment = GTK.gtk_adjustment_get_value(vAdjustment);
	TreeItem item = null;
	if (cachedAdjustment == currentAdjustment) {
		item = _getCachedTopItem();
	}
	/*
	 * Bug 501420: check to make sure the item is not disposed before returning
	 * it. If it is, find the topItem using GtkTreeView API.
	 */
	if(item != null && !item.isDisposed()){
		return exposeVirtualItem (item);
	}
	// Use GTK method to get topItem if there has been changes to the vAdjustment
	long [] path = new long [1];
	GTK.gtk_widget_realize (handle);
    if (!GTK.gtk_tree_view_get_path_at_pos(handle, 1, 1, path, null, null, null)) {
        return null;
    }
    if (path [0] == 0) {
        return null;
    }
	item = null;
	long iter = OS.g_malloc (GTK.GtkTreeIter_sizeof());
	if (GTK.gtk_tree_model_get_iter (viewModel (), iter, path [0])) {
		item = _getItem (iter);
	}
	OS.g_free (iter);
	GTK.gtk_tree_path_free (path [0]);
	topItem = item;
	if (virtualViewport != null && item != null) {
		updateVirtualViewportGeometry ();
		virtualViewport.setTopMaterializedId (virtualItemId (item));
	}
	return exposeVirtualItem (item);
}

TreeItem _getCachedTopItem() {
	/*
	 *  Check to see if the selected item is also the topItem. If it is, that means topItem is
	 *  in sync with the GTK view. If not, the real top item should be the last selected item, which is caused
	 *  by setSelection().
	 */
	long treeSelect = GTK.gtk_tree_view_get_selection(handle);
	long list = GTK.gtk_tree_selection_get_selected_rows(treeSelect, null);
	TreeItem treeSelection = null;
	if (list != 0) {
		long iter = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
		long data = OS.g_list_data (list);
		if (GTK.gtk_tree_model_get_iter (viewModel (), iter, data)) {
			treeSelection = _getItem (iter);
		}
		OS.g_free (iter);
		GTK.gtk_tree_path_free (data);
		if (topItem == treeSelection) {
			return topItem;
		}
		else {
			return treeSelection;
		}
	} else {
		if (topItem == null) {
			// if topItem isn't set and there is nothing selected, topItem is the first item on the Tree
			TreeItem item = null;
			long iter = OS.g_malloc (GTK.GtkTreeIter_sizeof());
			if (GTK.gtk_tree_model_get_iter_first (modelHandle, iter)) {
				item = _getItem (iter);
			}
			OS.g_free (iter);
			return item;
		} else {
			return topItem;
		}
	}
}

@Override
long gtk3_button_press_event (long widget, long event) {
	double [] eventX = new double [1];
	double [] eventY = new double [1];
	GDK.gdk_event_get_coords(event, eventX, eventY);

	int eventType = GDK.gdk_event_get_event_type(event);

	int [] eventButton = new int [1];
	int [] eventState = new int [1];
	GDK.gdk_event_get_button(event, eventButton);
	GDK.gdk_event_get_state(event, eventState);

	double [] eventRX = new double [1];
	double [] eventRY = new double [1];
	GDK.gdk_event_get_root_coords(event, eventRX, eventRY);

	long eventGdkResource = GDK.gdk_event_get_window(event);
    if (eventGdkResource != GTK3.gtk_tree_view_get_bin_window(handle)) {
        return 0;
    }

	long result = super.gtk3_button_press_event (widget, event);
    if (result != 0) {
        return result;
    }
	/*
	 * Feature in GTK. In multi-select tree view there is a problem with using DnD operations while also selecting multiple items.
	 * When doing a DnD, GTK de-selects all other items except for the widget being dragged from. By disabling the selection function
	 * in GTK in the case that additional items aren't being added (CTRL_MASK or SHIFT_MASK) and the item being dragged is already
	 * selected, we can give the DnD handling to MOTION-NOTIFY. Seee Bug 503431
	 */
	if ((state & DRAG_DETECT) != 0 && hooks (SWT.DragDetect) &&
			OS.isWayland() && eventType == GDK.GDK_BUTTON_PRESS) {
	// check to see if there is another event coming in that is not a double/triple click, this is to prevent Bug 514531
		long nextEvent = GDK.gdk_event_peek();
		if (nextEvent == 0) {
			long [] path = new long [1];
			long selection = GTK.gtk_tree_view_get_selection (handle);
			if (GTK.gtk_tree_view_get_path_at_pos (handle, (int)eventX[0], (int)eventY[0], path, null, null, null) &&
					path[0] != 0) {
				//  selection count is used in the case of clicking an already selected item while holding Control
				selectionCountOnPress = getSelectionCount();
				if (GTK.gtk_tree_selection_path_is_selected (selection, path[0])) {
					if (((eventState[0] & (GDK.GDK_CONTROL_MASK|GDK.GDK_SHIFT_MASK)) == 0) ||
							((eventState[0] & GDK.GDK_CONTROL_MASK) != 0)) {
						/**
						 * Disable selection on a mouse click if there are multiple items already selected. Also,
						 * if control is currently being held down, we will designate the selection logic over to release
						 * instead by first disabling the selection.
						 * E.g to reproduce: Open DNDExample, select "Tree", select multiple items, try dragging.
						 *   without line below, only one item is selected for drag.
						 */
						long gtk_false_funcPtr = GTK.GET_FUNCTION_POINTER_gtk_false();
						GTK.gtk_tree_selection_set_select_function(selection, gtk_false_funcPtr, 0, 0);
					}
				}
			}
		} else {
			gdk_event_free (nextEvent);
		}
	}
	/*
	* Feature in GTK.  In a multi-select tree view, when multiple items are already
	* selected, the selection state of the item is toggled and the previous selection
	* is cleared. This is not the desired behaviour when bringing up a popup menu
	* Also, when an item is reselected with the right button, the tree view issues
	* an unwanted selection event. The workaround is to detect that case and not
	* run the default handler when the item is already part of the current selection.
	*/
	int button = eventButton[0];
	if (button == 3 && eventType == GDK.GDK_BUTTON_PRESS) {
		long [] path = new long [1];
		if (GTK.gtk_tree_view_get_path_at_pos (handle, (int)eventX[0], (int)eventY[0], path, null, null, null)) {
			if (path [0] != 0) {
				long selection = GTK.gtk_tree_view_get_selection (handle);
                if (GTK.gtk_tree_selection_path_is_selected(selection, path [0])) {
                    result = 1;
                }
				GTK.gtk_tree_path_free (path [0]);
			}
		}
	}

	/*
	* Feature in GTK.  When the user clicks in a single selection GtkTreeView
	* and there are no selected items, the first item is selected automatically
	* before the click is processed, causing two selection events.  The is fix
	* is the set the cursor item to be same as the clicked item to stop the
	* widget from automatically selecting the first item.
	*/
	if ((style & SWT.SINGLE) != 0 && getSelectionCount () == 0) {
		long [] path = new long [1];
		if (GTK.gtk_tree_view_get_path_at_pos (handle, (int)eventX[0], (int)eventY[0], path, null, null, null)) {
			if (path [0] != 0) {
				long selection = GTK.gtk_tree_view_get_selection (handle);
				OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
				GTK.gtk_tree_view_set_cursor (handle, path [0], 0, false);
				OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
				GTK.gtk_tree_path_free (path [0]);
			}
		}
	}

	/*
	 * Bug 312568: If mouse double-click pressed, manually send a DefaultSelection.
	 * Bug 518414: Added defaultSelectionPending guard flag to only send a DefaultSelection when the
	 * double-click triggers a 'row-activated' signal. Note that this relies on the fact
	 * that 'row-activated' signal comes before double-click event. This prevents
	 * opening of the current highlighted item when double clicking on any expander arrow.
	 */
	if (eventType == GDK.GDK_2BUTTON_PRESS && defaultSelectionPending) {
		sendTreeDefaultSelection ();
		defaultSelectionPending = false;
	}

	return result;
}

@Override
int gtk_gesture_press_event (long gesture, int n_press, double x, double y, long event) {
	/*
	 * GtkTreeView activates the row for a double-click in its own click gesture, which runs
	 * after this one, and activates nothing for a double-click on an expander: send the
	 * DefaultSelection from gtk_row_activated.
	 */
	defaultSelectionPending = n_press == 2;
	return super.gtk_gesture_press_event(gesture, n_press, x, y, event);
}

@Override
boolean gtk4_key_press_event (long controller, int keyval, int keycode, int state, long event) {
	/* Space and Enter activate the row too, see gtk_gesture_press_event. */
	defaultSelectionPending = false;
	switch (keyval) {
		case GDK.GDK_Return:
		case GDK.GDK_KP_Enter:
			// Send DefaultSelection as gtk3_key_press_event does, for the keypad Enter too.
			if ((state & (GDK.GDK_SUPER_MASK | GDK.GDK_META_MASK | GDK.GDK_HYPER_MASK | GDK.GDK_MOD1_MASK)) == 0) {
				sendTreeDefaultSelection ();
				if (isDisposed ()) return true;
			}
			break;
	}
	boolean handled = super.gtk4_key_press_event(controller, keyval, keycode, state, event);
	if (handled || isDisposed ()) return handled;
	switch (keyval) {
		case GDK.GDK_Left:
		case GDK.GDK_Right:
			/*
			 * GtkTreeView moves between cells for Left and Right. Expand and collapse the
			 * cursor row instead when it has children, as on GTK3. Leave the keys of the
			 * search entry alone.
			 */
			if ((state & (GDK.GDK_SHIFT_MASK | GDK.GDK_CONTROL_MASK | GDK.GDK_MOD1_MASK | GDK.GDK_SUPER_MASK | GDK.GDK_META_MASK | GDK.GDK_HYPER_MASK)) == 0 && GTK.gtk_widget_has_focus (handle)) {
				boolean expand = (keyval == GDK.GDK_Right) != ((style & SWT.RIGHT_TO_LEFT) != 0);
				return expandCollapseCursorRow (expand);
			}
			break;
	}
	return false;
}

boolean expandCollapseCursorRow (boolean expand) {
	long [] path = new long [1];
	GTK.gtk_tree_view_get_cursor (handle, path, null);
	if (path [0] == 0) return false;
	long iter = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
	boolean parent = GTK.gtk_tree_model_get_iter (modelHandle, iter, path [0]) && GTK.gtk_tree_model_iter_n_children (modelHandle, iter) > 0;
	OS.g_free (iter);
	if (parent) {
		if (expand) {
			GTK.gtk_tree_view_expand_row (handle, path [0], false);
		} else {
			GTK.gtk_tree_view_collapse_row (handle, path [0]);
		}
	}
	GTK.gtk_tree_path_free (path [0]);
	return parent;
}


@Override
long gtk_row_activated (long tree, long path, long column) {
	if (GTK.GTK4) {
		if (defaultSelectionPending) sendTreeDefaultSelection ();
		defaultSelectionPending = false;
		return 0;
	}
	/*
	 * Enter, Space and accessibility tools activate the row too, but only the second press of a double-click is
	 * followed by the GDK_2BUTTON_PRESS that sends the DefaultSelection, see gtk3_button_press_event.
	 */
	defaultSelectionPending = false;
	long eventPtr = GTK3.gtk_get_current_event ();
	if (eventPtr != 0) {
		int eventType = GDK.gdk_event_get_event_type (eventPtr);
		defaultSelectionPending = eventType == GDK.GDK_BUTTON_PRESS || eventType == GDK.GDK_2BUTTON_PRESS;
		GDK.gdk_event_free (eventPtr);
	}
	return 0;
}

@Override
long gtk3_key_press_event (long widget, long event) {
	int [] key = new int[1];
	GDK.gdk_event_get_keyval(event, key);
	int mask = gdk3_event_get_state (event);
	int logicalKey = virtualLogicalKey (key [0]);
	if (usesBoundedVirtualView () && (mask & (GDK.GDK_MOD1_MASK | GDK.GDK_SUPER_MASK | GDK.GDK_META_MASK | GDK.GDK_HYPER_MASK)) == 0
			&& (virtualNavigationKey (logicalKey) || virtualSelectionKey (logicalKey, mask))) {
		long result = super.gtk3_key_press_event (widget, event);
		if (result == 0 && !isDisposed ()) {
			if (virtualSelectionKey (logicalKey, mask)) virtualKeyboardSelection (logicalKey, mask);
			else virtualNavigate (logicalKey, mask);
		}
		return 1;
	}


	switch (key[0]) {
		case GDK.GDK_Return:
			// Send DefaultSelectionEvent when:
			// When    : Enter, Shift+Enter, Ctrl+Enter are pressed.
			// Not when: Alt+Enter, (Meta|Super|Hyper)+Enter, reason is stateMask is not provided on Gtk.
			// Note: alt+Enter creates a selection on GTK, but we filter it out to be a bit more consistent Win32 (521387)
			int keymask = gdk3_event_get_state (event);
			if ((keymask & (GDK.GDK_SUPER_MASK | GDK.GDK_META_MASK | GDK.GDK_HYPER_MASK | GDK.GDK_MOD1_MASK)) == 0) {
				sendTreeDefaultSelection ();
			}
			break;
	}

	return super.gtk3_key_press_event (widget, event);
}

/**
 * Used to emulate DefaultSelection event. See Bug 312568.
 * Feature in GTK. 'row-activation' event comes before DoubleClick event.
 * This is causing the editor not to get focus after double-click.
 * The solution is to manually send the DefaultSelection event after a double-click,
 * and to emulate it for Space/Return.
 */
void sendTreeDefaultSelection() {

	//Note, similar DefaultSelectionHandling in SWT List/Table/Tree
	TreeItem treeItem = getFocusItem ();
    if (treeItem == null) {
        return;
    }
	pinVirtualFacade (treeItem);

	Event event = new Event ();
	event.item = treeItem;

	sendSelectionEvent (SWT.DefaultSelection, event, false);
}

@Override
long gtk3_button_release_event (long widget, long event) {
	double [] eventX = new double [1];
	double [] eventY = new double [1];
	GDK.gdk_event_get_coords(event, eventX, eventY);
	int [] eventButton = new int [1];
	int [] eventState = new int [1];
	GDK.gdk_event_get_button(event, eventButton);
	GDK.gdk_event_get_state(event, eventState);

	double [] eventRX = new double [1];
	double [] eventRY = new double [1];
	GDK.gdk_event_get_root_coords(event, eventRX, eventRY);

	long eventGdkResource = GDK.gdk_event_get_window(event);
    if (eventGdkResource != GTK3.gtk_tree_view_get_bin_window(handle)) {
        return 0;
    }
	// Check region since super.gtk_button_release_event() isn't called
	lastInput.x = (int) eventX[0];
	lastInput.y = (int) eventY[0];
    if (containedInRegion(lastInput.x, lastInput.y)) {
        return 0;
    }
	/*
	 * Feature in GTK. In multi-select tree view there is a problem with using DnD operations while also selecting multiple items.
	 * When doing a DnD, GTK de-selects all other items except for the widget being dragged from. By disabling the selection function
	 * in GTK in the case that additional items aren't being added (CTRL_MASK or SHIFT_MASK) and the item being dragged is already
	 * selected, we can give the DnD handling to MOTION-NOTIFY. On release, we can then re-enable the selection method
	 * and also select the item in the tree by moving the selection logic to release instead. See Bug 503431.
	 */
	if ((state & DRAG_DETECT) != 0 && hooks (SWT.DragDetect) && OS.isWayland()) {
		long [] path = new long [1];
		long selection = GTK.gtk_tree_view_get_selection (handle);
		// free up the selection function on release.
		GTK.gtk_tree_selection_set_select_function(selection,0,0,0);
		if (GTK.gtk_tree_view_get_path_at_pos (handle, (int)eventX[0], (int)eventY[0], path, null, null, null) &&
				path[0] != 0 && GTK.gtk_tree_selection_path_is_selected (selection, path[0])) {
			selectionCountOnRelease = getSelectionCount();
			if ((eventState[0] & (GDK.GDK_CONTROL_MASK|GDK.GDK_SHIFT_MASK)) == 0) {
				GTK.gtk_tree_view_set_cursor(handle, path[0], 0,  false);
			}
			// Check to see if there has been a new tree item selected when holding Control in Path.
			// If not, deselect the item.
			if ((eventState[0] & GDK.GDK_CONTROL_MASK) != 0 && selectionCountOnRelease == selectionCountOnPress) {
				GTK.gtk_tree_selection_unselect_path (selection,path[0]);
			}
		}
	}
	return super.gtk3_button_release_event (widget, event);
}

@Override
long gtk_changed (long widget) {
	captureVirtualSelection ();
	notifyVirtualAccessibleSelection ();
	notifyVirtualAccessibleFocus ();
	TreeItem item = getFocusItem ();
	if (item != null) {
		pinVirtualFacade (item);
		Event event = new Event ();
		event.item = item;
		sendSelectionEvent (SWT.Selection, event, false);
	}
	return 0;
}

@Override
long gtk_expand_collapse_cursor_row (long widget, long logical, long expand, long open_all) {
    // FIXME - this flag is never cleared.  It should be cleared when the expand all operation completes.
    if (expand != 0 && open_all != 0) {
        expandAll = true;
    }
	return 0;
}

@Override
long gtk_focus_in_event (long widget, long event) {
	long result = super.gtk_focus_in_event (widget, event);
	if (!isDisposed ()) notifyVirtualAccessibleFocus ();
	return result;
}

@Override
long gtk_focus_out_event (long widget, long event) {
	long result = super.gtk_focus_out_event (widget, event);
	if (!isDisposed ()) notifyVirtualAccessibleFocus ();
	return result;
}

void drawInheritedBackground (long cairo) {
	if ((state & PARENT_BACKGROUND) != 0 || backgroundImage != null) {
		Control control = findBackgroundControl ();
		if (control != null) {
			int [] width = new int [1], height = new int [1];
			long gdkResource;
			if (GTK.GTK4) {
				gdkResource = gtk_widget_get_surface(handle);
				gdk_surface_get_size (gdkResource, width, height);
			} else {
				gdkResource = GTK3.gtk_tree_view_get_bin_window (handle);
				gdk_window_get_size (gdkResource, width, height);
			}
			if (usesBoundedVirtualView ()) {
				/* The final logical row need not have a resident native path. Use
				 * the same row stride as the scroll owner and retain the SWT GC plane. */
				double bottom = (virtualProjection.visibleRowCount () - virtualViewport.topRow ())
						* (double)virtualRowExtent - virtualPixelRemainder;
				int y = (int)Math.max (0, Math.min (height [0], Math.ceil (bottom)));
				if (height [0] > y) drawBackground (control, gdkResource, cairo, 0, y, width [0], height [0] - y);
				return;
			}
			long parent = 0;
			int itemCount = GTK.gtk_tree_model_iter_n_children (modelHandle, parent);
			GdkRectangle rect = new GdkRectangle ();
			boolean expanded = true;
			while (itemCount != 0 && expanded && height [0] > (rect.y + rect.height)) {
				long iter = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
				GTK.gtk_tree_model_iter_nth_child (modelHandle, iter, parent, itemCount - 1);
				itemCount = GTK.gtk_tree_model_iter_n_children (modelHandle, iter);
				long path = viewPath (iter);
				GTK.gtk_tree_view_get_cell_area (handle, path, 0, rect);
				expanded = GTK.gtk_tree_view_row_expanded (handle, path);
				GTK.gtk_tree_path_free (path);
                if (parent != 0) {
                    OS.g_free(parent);
                }
				parent = iter;
			}
            if (parent != 0) {
                OS.g_free(parent);
            }
			if (height [0] > (rect.y + rect.height)) {
				drawBackground (control, gdkResource, cairo, 0, rect.y + rect.height, width [0], height [0] - (rect.y + rect.height));
			}
		}
	}
}

@Override
long gtk_draw (long widget, long cairo) {
	boolean haveBoundsChanged = boundsChangedSinceLastDraw;
	boundsChangedSinceLastDraw = false;
    if ((state & OBSCURED) != 0) {
        return 0;
    }
	/*
	 * Bug 537960: JFace tree viewers miss a repaint when resized by a SashForm
	 *
	 * If a listener of type SWT.MeasureItem, SWT.PaintItem and or SWT.EraseItem is registered,
	 * GTK will sometimes not invalidate the tree widget pixel cache when the tree is resized.
	 * As a result, a few of the bottom rows of JFace tree viewers that use styled text are often not drawn on resize.
	 * If the tree was resized since the last paint, we ignore this draw request
	 * and queue another draw request so that the pixel cache is properly invalidated.
	 */
	if (isOwnerDrawn && haveBoundsChanged) {
		GTK.gtk_widget_queue_draw(handle);
		return 0;
	}
	drawInheritedBackground	(cairo);
	if (GTK.GTK4) {
		return super.gtk_draw (widget, cairo);
	} else {
		// On GTK3 super.gtk_draw will be lost by items drawing thus handle explicitly in windowProc.
		return 0;
	}
}

@Override
long gtk3_motion_notify_event (long widget, long event) {
	long window = GDK.gdk_event_get_window (event);
    if (window != GTK3.gtk_tree_view_get_bin_window(handle)) {
        return 0;
    }
	return super.gtk3_motion_notify_event (widget, event);
}

@Override
long gtk_row_has_child_toggled (long model, long path, long iter) {
	if (usesBoundedVirtualView ()) { scheduleVirtualResidency (); return 0; }
	/*
	* Feature in GTK. The expanded state of a row that lost
	* its children is not persisted by GTK. So, the row
	* doesn't exhibit the expanded state after obtaining the
	* children. The fix is to preserve the expanded state
	* and use this callback, as it is invoked when a row has
	* gotten the first child row or lost its last child row.
	*/
	int [] index = new int [1];
	GTK.gtk_tree_model_get (modelHandle, iter, ID_COLUMN, index, -1);
    if (index [0] >= items.length) {
        return 0;
    }
	TreeItem item = items [index [0]];
    if (item == null) {
        return 0;
    }
	int childCount = GTK.gtk_tree_model_iter_n_children (modelHandle, item.handle);
	if (childCount != 0 && item.isExpanded) {
		OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, TEST_EXPAND_ROW);
		GTK.gtk_tree_view_expand_row (handle, path, false);
		OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, TEST_EXPAND_ROW);
	}
	return 0;
}

void initializeViewportLayers () {
	long horizontal = GTK.gtk_scrolled_window_get_hadjustment (scrolledHandle);
	long vertical = usesBoundedVirtualView () ? verticalAdjustment : GTK.gtk_scrollable_get_vadjustment (handle);
	viewportLayers.initialize (
			horizontal != 0 ? GTK.gtk_adjustment_get_value (horizontal) : 0,
			vertical != 0 ? GTK.gtk_adjustment_get_value (vertical) : 0);
}

void updateVirtualViewportGeometry () {
    if (virtualViewport == null) {
        return;
    }
	Rectangle client = getClientAreaInPixels ();
	int chromeHeight = getHeaderVisible () ? getHeaderHeight () : 0;
	int bodyHeight = isVisible () && client.width > 0 ? Math.max (0, client.height - chromeHeight) : 0;
	if (usesBoundedVirtualView ()) {
		int allocated = (int)GTK.gtk_adjustment_get_page_size (virtualViewAdjustment);
		if (bodyHeight > 0 && allocated > 0) bodyHeight = allocated;
		virtualRowExtent = Math.max (1, getItemHeight ());
		if (virtualResidency.paintCount () > 0 && GTK.gtk_widget_get_realized (handle)) {
			long path = residentPath (virtualResidency.paintEntry (0));
			GdkRectangle background = new GdkRectangle ();
			try { GTK.gtk_tree_view_get_background_area (handle, path, 0, background); }
			finally { GTK.gtk_tree_path_free (path); }
			if (background.height > 0) virtualRowExtent = background.height;
		}
		virtualBodyExtent = bodyHeight;
		virtualViewport.configureGeometry (virtualRowExtent, virtualBodyExtent);
	} else {
		virtualViewport.configureGeometry (Math.max (1, getItemHeight ()), bodyHeight);
	}
}

void syncVirtualTopRowFromNative () {
	if (usesBoundedVirtualView ()) return;
    if (virtualViewport == null) {
        return;
    }
	updateVirtualViewportGeometry ();
	long [] path = new long [1];
	GTK.gtk_widget_realize (handle);
    if (!GTK.gtk_tree_view_get_path_at_pos(handle, 1, 1, path, null, null, null)) {
        return;
    }
    if (path [0] == 0) {
        return;
    }
	long iter = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
    if (iter == 0) {
        error(SWT.ERROR_NO_HANDLES);
    }
	try {
        if (!GTK.gtk_tree_model_get_iter(viewModel (), iter, path [0])) {
            return;
        }
		TreeItem item = _getItem (iter);
        if (item == null || item.isDisposed()) {
            return;
        }
		virtualViewport.setTopMaterializedId (virtualItemId (item));
		topItem = item;
	} finally {
		OS.g_free (iter);
		GTK.gtk_tree_path_free (path [0]);
	}
}

long virtualViewportTopRow () {
    if (virtualViewport == null) {
        return 0;
    }
	updateVirtualViewportGeometry ();
	return virtualViewport.topRow ();
}

VirtualTreeVisibleProjection.Row [] virtualViewportVisibleWindow () {
    if (virtualViewport == null) {
        return new VirtualTreeVisibleProjection.Row [0];
    }
	updateVirtualViewportGeometry ();
	return virtualViewport.visibleWindow ();
}

VirtualTreeVisibleProjection.Row [] virtualViewportPaintWindow () {
    if (virtualViewport == null) {
        return new VirtualTreeVisibleProjection.Row [0];
    }
	updateVirtualViewportGeometry ();
	return virtualViewport.paintWindow ();
}

@Override
long gtk_scroll_event (long widget, long eventPtr) {
	long result = super.gtk_scroll_event(widget, eventPtr);
	if (usesBoundedVirtualView ()) return result;
	syncVirtualTopRowFromNative ();
	long horizontal = GTK.gtk_scrolled_window_get_hadjustment (scrolledHandle);
	long vertical = GTK.gtk_scrollable_get_vadjustment (handle);
	int dirtyLayers = viewportLayers.scrollTo (
			horizontal != 0 ? GTK.gtk_adjustment_get_value (horizontal) : 0,
			vertical != 0 ? GTK.gtk_adjustment_get_value (vertical) : 0);
    if ((dirtyLayers & ViewportLayerState.HEADER) != 0) {
        wasScrolled = true;
    }
	return result;
}

@Override
long gtk_start_interactive_search(long widget) {
	if (!searchEnabled()) {
		OS.g_signal_stop_emission_by_name(widget, OS.start_interactive_search);
		return 1;
	}
	return 0;
}

@Override
long gtk_test_collapse_row (long tree, long iter, long path) {
	if (usesBoundedVirtualView ()) {
		if (reconcilingVirtualResidency) return 0;
		TreeItem item = _getItem (iter);
		if (item == null) return 1;
		virtualCallbackDepth++;
		try {
			if (!checkData (item)) return 1;
			pinVirtualFacade (item);
			Event event = new Event ();
			event.item = item;
			sendEvent (SWT.Collapse, event);
			if (!isDisposed () && !item.isDisposed ()) item.setExpandedState (false);
		} finally {
			virtualCallbackDepth--;
			scheduleVirtualResidency ();
		}
		return 1;
	}
	int [] index = new int [1];
	GTK.gtk_tree_model_get (modelHandle, iter, ID_COLUMN, index, -1);
	TreeItem item = items [index [0]];
	pinVirtualFacade (item);
	Event event = new Event ();
	event.item = item;
	boolean oldModelChanged = modelChanged;
	modelChanged = false;
	sendEvent (SWT.Collapse, event);
	/*
	* Bug in GTK.  Collapsing the target row during the test_collapse_row
	* handler will cause a segmentation fault if the animation code is allowed
	* to run.  The fix is to block the animation if the row is already
	* collapsed.
	*/
	boolean changed = modelChanged || !GTK.gtk_tree_view_row_expanded (handle, path);
	modelChanged = oldModelChanged;
    if (isDisposed() || item.isDisposed()) {
        return 1;
    }
    if (virtualTopology != null) {
        virtualFlag(item, VirtualItemState.EXPANDED, false);
    }
	item.setExpandedState (false);
	/*
	* Bug in GTK.  Expanding or collapsing a row which has no more
	* children causes the model state to become invalid, causing
	* GTK to give warnings and behave strangely.  Other changes to
	* the model can cause expansion to fail when using the multiple
	* expansion keys (such as *).  The fix is to stop the expansion
	* if there are model changes.
	*
	* Note: This callback must return 0 for the collapsing
	* animation to occur.
	*/
	if (changed) {
		OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, TEST_COLLAPSE_ROW);
		GTK.gtk_tree_view_collapse_row (handle, path);
		OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, TEST_COLLAPSE_ROW);
		scheduleVirtualCollapseCompaction (item);
		return 1;
	}
	scheduleVirtualCollapseCompaction (item);
	return 0;
}

@Override
long gtk_test_expand_row (long tree, long iter, long path) {
	if (usesBoundedVirtualView ()) {
		if (reconcilingVirtualResidency) return 0;
		TreeItem item = _getItem (iter);
		if (item == null) return 1;
		virtualCallbackDepth++;
		try {
			if (!checkData (item)) return 1;
			pinVirtualFacade (item);
			Event event = new Event ();
			event.item = item;
			sendEvent (SWT.Expand, event);
			if (!isDisposed () && !item.isDisposed ()) item.setExpandedState (true);
		} finally {
			virtualCallbackDepth--;
			scheduleVirtualResidency ();
		}
		return 1;
	}
	int [] index = new int [1];
	GTK.gtk_tree_model_get (modelHandle, iter, ID_COLUMN, index, -1);
	TreeItem item = items [index [0]];
	pinVirtualFacade (item);
	restoreVirtualChildren (item);
	Event event = new Event ();
	event.item = item;
	boolean oldModelChanged = modelChanged;
	modelChanged = false;
	sendEvent (SWT.Expand, event);
	/*
	* Bug in GTK.  Expanding the target row during the test_expand_row
	* handler will cause a segmentation fault if the animation code is allowed
	* to run.  The fix is to block the animation if the row is already
	* expanded.
	*/
	boolean changed = modelChanged || GTK.gtk_tree_view_row_expanded (handle, path);
	modelChanged = oldModelChanged;
    if (isDisposed() || item.isDisposed()) {
        return 1;
    }
    if (virtualTopology != null) {
        virtualFlag(item, VirtualItemState.EXPANDED, true);
    }
	item.setExpandedState (true);
	/*
	* Bug in GTK.  Expanding or collapsing a row which has no more
	* children causes the model state to become invalid, causing
	* GTK to give warnings and behave strangely.  Other changes to
	* the model can cause expansion to fail when using the multiple
	* expansion keys (such as *).  The fix is to stop the expansion
	* if there are model changes.
	*
	* Bug in GTK.  test-expand-row does not get called for each row
	* in an expand all operation.  The fix is to block the initial
	* expansion and only expand a single level.
	*
	* Note: This callback must return 0 for the collapsing
	* animation to occur.
	*/
	if (changed || expandAll) {
		OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, TEST_EXPAND_ROW);
		GTK.gtk_tree_view_expand_row (handle, path, false);
		OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, TEST_EXPAND_ROW);
		return 1;
	}
	return 0;
}

@Override
long gtk_toggled (long renderer, long pathStr) {
	long path = GTK.gtk_tree_path_new_from_string (pathStr);
    if (path == 0) {
        return 0;
    }
	TreeItem item = null;
	long iter = OS.g_malloc (GTK.GtkTreeIter_sizeof());
	if (GTK.gtk_tree_model_get_iter (viewModel (), iter, path)) {
		item = _getItem (iter);
	}
	OS.g_free (iter);
	GTK.gtk_tree_path_free (path);
	if (item != null) {
		pinVirtualFacade (item);
		item.setChecked (!item.getChecked ());
		Event event = new Event ();
		event.detail = SWT.CHECK;
		event.item = item;
		sendSelectionEvent (SWT.Selection, event, false);
	}
	return 0;
}

@Override
void gtk_widget_get_preferred_size (long widget, GtkRequisition requisition) {
	/*
	 * Bug in GTK.  For some reason, gtk_widget_size_request() fails
	 * to include the height of the tree view items when there are
	 * no columns visible.  The fix is to temporarily make one column
	 * visible.
	 */
	if (columnCount == 0) {
		super.gtk_widget_get_preferred_size (widget, requisition);
		return;
	}
	long columns = GTK.gtk_tree_view_get_columns (handle), list = columns;
	boolean fixVisible = columns != 0;
	while (list != 0) {
		long column = OS.g_list_data (list);
		if (GTK.gtk_tree_view_column_get_visible (column)) {
			fixVisible = false;
			break;
		}
		list = OS.g_list_next (list);
	}
	long columnHandle = 0;
	if (fixVisible) {
		columnHandle = OS.g_list_data (columns);
		GTK.gtk_tree_view_column_set_visible (columnHandle, true);
	}
	super.gtk_widget_get_preferred_size (widget, requisition);
	if (fixVisible) {
		GTK.gtk_tree_view_column_set_visible (columnHandle, false);
	}
    if (columns != 0) {
        OS.g_list_free(columns);
    }
}

void hideFirstColumn () {
	long firstColumn = GTK.gtk_tree_view_get_column (handle, 0);
	GTK.gtk_tree_view_column_set_visible (firstColumn, false);
}

@Override
void hookEvents () {
	super.hookEvents ();
	if (virtualViewAdjustment != 0) {
		virtualAdjustmentCallback = new Callback (this, "virtualAdjustmentProc", 2);
		long proc = virtualAdjustmentCallback.getAddress ();
		if (proc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
		/* Distinct user data survives ScrollBar.setSelection's VALUE_CHANGED block,
		 * preserving programmatic scrolling without sending a Selection event. */
		virtualLogicalValueSignal = Integer.toUnsignedLong (OS.g_signal_connect (verticalAdjustment, OS.value_changed, proc, 1));
		virtualViewRangeSignal = Integer.toUnsignedLong (OS.g_signal_connect (virtualViewAdjustment, OS.changed, proc, 2));
		virtualViewValueSignal = Integer.toUnsignedLong (OS.g_signal_connect (virtualViewAdjustment, OS.value_changed, proc, 3));
	}
	long selection = GTK.gtk_tree_view_get_selection(handle);
	OS.g_signal_connect_closure (selection, OS.changed, display.getClosure (CHANGED), false);
	OS.g_signal_connect_closure (handle, OS.row_activated, display.getClosure (ROW_ACTIVATED), false);
	OS.g_signal_connect_closure (handle, OS.test_expand_row, display.getClosure (TEST_EXPAND_ROW), false);
	OS.g_signal_connect_closure (handle, OS.test_collapse_row, display.getClosure (TEST_COLLAPSE_ROW), false);
	OS.g_signal_connect_closure (handle, OS.expand_collapse_cursor_row, display.getClosure (EXPAND_COLLAPSE_CURSOR_ROW), false);
	OS.g_signal_connect_closure (modelHandle, OS.row_has_child_toggled, display.getClosure (ROW_HAS_CHILD_TOGGLED), false);
	if (checkRenderer != 0) {
		OS.g_signal_connect_closure (checkRenderer, OS.toggled, display.getClosure (TOGGLED), false);
	}
	OS.g_signal_connect_closure (handle, OS.start_interactive_search, display.getClosure (START_INTERACTIVE_SEARCH), false);
}

/**
 * Searches the receiver's list starting at the first column
 * (index 0) until a column is found that is equal to the
 * argument, and returns the index of that column. If no column
 * is found, returns -1.
 *
 * @param column the search column
 * @return the index of the column
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the column is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.1
 */
public int indexOf (TreeColumn column) {
	checkWidget();
    if (column == null) {
        error(SWT.ERROR_NULL_ARGUMENT);
    }
	for (int i=0; i<columnCount; i++) {
        if (columns [i] == column) {
            return i;
        }
	}
	return -1;
}

/**
 * Searches the receiver's list starting at the first item
 * (index 0) until an item is found that is equal to the
 * argument, and returns the index of that item. If no item
 * is found, returns -1.
 *
 * @param item the search item
 * @return the index of the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the item is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the item has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.1
 */
public int indexOf (TreeItem item) {
	checkWidget();
    if (item == null) {
        error(SWT.ERROR_NULL_ARGUMENT);
    }
    if (item.isDisposed()) {
        error(SWT.ERROR_INVALID_ARGUMENT);
    }
	if (virtualTopology != null) {
		if (item.parent != this) return -1;
		int id = virtualItemId (item);
		return virtualTopology.parentId (id) == VirtualTreeTopology.ROOT
				? virtualTopology.childIndex (id) : -1;
	}
	int index = -1;
	long path = GTK.gtk_tree_model_get_path (modelHandle, item.handle);
	int depth = GTK.gtk_tree_path_get_depth (path);
	if (depth == 1) {
		long indices = GTK.gtk_tree_path_get_indices (path);
		if (indices != 0) {
			int[] temp = new int[1];
			C.memmove (temp, indices, 4);
			index = temp[0];
		}
	}
	GTK.gtk_tree_path_free (path);
	return index;
}

@Override
boolean mnemonicHit (char key) {
	for (int i=0; i<columnCount; i++) {
		long labelHandle = columns [i].labelHandle;
        if (labelHandle != 0 && mnemonicHit(labelHandle, key)) {
            return true;
        }
	}
	return false;
}

@Override
boolean mnemonicMatch (char key) {
	for (int i=0; i<columnCount; i++) {
		long labelHandle = columns [i].labelHandle;
        if (labelHandle != 0 && mnemonicMatch(labelHandle, key)) {
            return true;
        }
	}
	return false;
}

@Override
long paintWindow () {
	GTK.gtk_widget_realize (handle);
	if (GTK.GTK4) {
		// gtk_tree_view_get_bin_window was removed in GTK4; fall back to the widget surface
		return gtk_widget_get_surface (handle);
	}
	return GTK3.gtk_tree_view_get_bin_window (handle);
}

@Override
void propagateDraw (long container, long cairo) {
	/*
	 * Sometimes Tree/Table headers need to be re-drawn, as some of the
	 * "noChildDrawing" widgets might still be partially drawn.
	 */
	super.propagateDraw(container, cairo);
	if (headerVisible && noChildDrawing && wasScrolled) {
		for (TreeColumn column : columns) {
			if (column != null) {
				GTK.gtk_widget_queue_draw(column.buttonHandle);
			}
		}
		wasScrolled = false;
	}
}

void recreateRenderers () {
	if (checkRenderer != 0) {
		display.removeWidget (checkRenderer);
		OS.g_object_unref (checkRenderer);
		long toggleType = display.gtk_cell_renderer_toggle_get_type ();
		checkRenderer = isOwnerDrawn && toggleType != 0 ? OS.g_object_new (toggleType, 0) : GTK.gtk_cell_renderer_toggle_new ();
        if (checkRenderer == 0) {
            error(SWT.ERROR_NO_HANDLES);
        }
		OS.g_object_ref (checkRenderer);
		display.addWidget (checkRenderer, this);
		OS.g_signal_connect_closure (checkRenderer, OS.toggled, display.getClosure (TOGGLED), false);
	}
	if (columnCount == 0) {
		createRenderers (GTK.gtk_tree_view_get_column (handle, 0), Tree.FIRST_COLUMN, true, 0);
	} else {
		for (int i = 0; i < columnCount; i++) {
			TreeColumn column = columns [i];
			createRenderers (column.handle, column.modelIndex, i == 0, column.style);
		}
	}
}

@Override
void redrawBackgroundImage () {
	Control control = findBackgroundControl ();
	if (control != null && control.backgroundImage != null) {
		redrawWidget (0, 0, 0, 0, true, false, false);
	}
}

@Override
void register () {
	super.register ();
	display.addWidget (GTK.gtk_tree_view_get_selection (handle), this);
    if (checkRenderer != 0) {
        display.addWidget(checkRenderer, this);
    }
	display.addWidget (modelHandle, this);
	if (virtualViewModel != 0) display.addWidget (virtualViewModel, this);
}

void releaseItem (TreeItem item, boolean release) {
	if (usesVirtualNativeModel ()) {
		int id = virtualItemId (item);
		if (id < 0) return;
		if (release) {
			item.release (false);
			if (id < items.length) items [id] = null;
		}
		return;
	}
	int [] index = new int [1];
	GTK.gtk_tree_model_get (modelHandle, item.handle, ID_COLUMN, index, -1);
	if (index [0] == -1) return;
	if (release) item.release (false);
	items [index [0]] = null;
}

// Bulk mutations may shift Java coordinates before the native snapshot is published.
// Release descendants by stable topology IDs, never by stale native iterators.
private void releaseVirtualItems (int parentId) {
	int child = virtualTopology.firstMaterializedChildId (parentId);
	while (child >= 0) {
		int next = virtualTopology.nextMaterializedSiblingId (child);
		releaseVirtualItems (child);
		TreeItem item = child < items.length ? items [child] : null;
		if (item != null && !item.isDisposed ()) releaseItem (item, true);
		child = next;
	}
}

void releaseItems (long parentIter) {
	if (usesVirtualNativeModel ()) {
		int parentId = parentIter == 0 ? VirtualTreeTopology.ROOT : virtualMaterializedId (parentIter);
		if (parentIter != 0 && parentId < 0) return;
		releaseVirtualItems (parentId);
		return;
	}
	int[] index = new int [1];
	long iter = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
	boolean valid = GTK.gtk_tree_model_iter_children (modelHandle, iter, parentIter);
	while (valid) {
		releaseItems (iter);
		if (!isDisposed ()) {
			GTK.gtk_tree_model_get (modelHandle, iter, ID_COLUMN, index, -1);
			if (index [0] != -1) {
				TreeItem item = items [index [0]];
				if (item != null) releaseItem (item, true);
			}
		}
		valid = GTK.gtk_tree_model_iter_next (modelHandle, iter);
	}
	OS.g_free (iter);
}

@Override
void releaseChildren (boolean destroy) {
	/* AT-held cells can call back while child disposal emits defunct states.
	 * Tombstone the logical root before its item and column arrays are cleared. */
	if (usesBoundedVirtualView () && accessible != null) accessible.internal_dispose_Accessible ();
	if (items != null) {
		for (int i=0; i<items.length; i++) {
			TreeItem item = items [i];
			if (item != null && !item.isDisposed ()) {
				item.release (false);
			}
		}
		items = null;
	}
    if (virtualTopology != null) {
        virtualTopology.clear();
    }
	if (columns != null) {
		for (int i=0; i<columnCount; i++) {
			TreeColumn column = columns [i];
			if (column != null && !column.isDisposed ()) {
				column.release (false);
			}
		}
		columns = null;
	}
	super.releaseChildren (destroy);
}

@Override
void releaseWidget () {
	releaseVirtualAccessibleChildren ();
	super.releaseWidget ();
	virtualSelections.clear ();
	if (virtualViewModel != 0) OS.g_object_unref (virtualViewModel);
	if (virtualViewAdjustment != 0) OS.g_object_unref (virtualViewAdjustment);
	virtualViewModel = virtualViewAdjustment = verticalAdjustment = 0;
	virtualResidency = nextVirtualResidency = null;
    if (modelHandle != 0) {
        OS.g_object_unref(modelHandle);
    }
	modelHandle = 0;
    if (checkRenderer != 0) {
        OS.g_object_unref(checkRenderer);
    }
	checkRenderer = 0;
    if (imageList != null) {
        imageList.dispose();
    }
    if (headerImageList != null) {
        headerImageList.dispose();
    }
	imageList = headerImageList = null;
	currentItem = null;
}

void remove (long parentIter, int start, int end) {
    if (start > end) {
        return;
    }
	if (usesVirtualNativeModel ()) {
		int parentId = virtualParentId (parentIter);
		int itemCount = virtualTopology.childCount (parentId);
		if (!(0 <= start && start <= end && end < itemCount)) error (SWT.ERROR_INVALID_RANGE);
		VirtualNativeViewState state = captureVirtualNativeViewState ();
		for (int i = start; i <= end; i++) {
			int id = virtualTopology.materializedChildId (parentId, start);
			if (id >= 0 && id < items.length) {
				TreeItem item = items [id];
				if (item != null && !item.isDisposed ()) {
					if (item.settingData) throwCannotRemoveItem (start);
					releaseVirtualItems (id);
					releaseItem (item, true);
				}
			}
			virtualTopology.removeCoordinate (parentId, start);
			if (usesBoundedVirtualView () && virtualSelections.containsKey (parentId)) virtualSelections.get (parentId).remove (start, 1);
		}
		modelChanged = true;
		finishVirtualNativeMutation (state);
		return;
	}
	int itemCount = GTK.gtk_tree_model_iter_n_children (modelHandle, parentIter);
	if (!(0 <= start && start <= end && end < itemCount)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	long selection = GTK.gtk_tree_view_get_selection (handle);
	long iter = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
    if (iter == 0) {
        error(SWT.ERROR_NO_HANDLES);
    }
	try {
		for (int i = start; i <= end; i++) {
			GTK.gtk_tree_model_iter_nth_child (modelHandle, iter, parentIter, start);
			int[] value = new int[1];
			GTK.gtk_tree_model_get (modelHandle, iter, ID_COLUMN, value, -1);
			TreeItem item = value [0] != -1 ? items [value [0]] : null;
			if (item != null && !item.isDisposed ()) {
				/*
				 * Bug 182598 - assertion failed in gtktreestore.c
				 * Removing an item while its data is being set will invalidate
				 * it, which will cause a crash in GTK.
				 */
				if(item.settingData) {
					throwCannotRemoveItem(i);
				}
				item.dispose ();
			} else {
				OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
				GTK.gtk_tree_store_remove (modelHandle, iter);
				OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
			}
		}
	} finally {
		OS.g_free (iter);
	}
}

/**
 * Removes all of the items from the receiver.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void removeAll () {
	checkWidget ();
	checkSetDataInProcessBeforeRemoval();

	if (usesVirtualNativeModel ()) {
		for (TreeItem item : items) {
			if (item != null && !item.isDisposed ()) item.release (false);
		}
		items = new TreeItem [4];
		virtualTopology.clear ();
		virtualTopology.setChildCount (VirtualTreeTopology.ROOT, 0);
		modelChanged = true;
		finishVirtualNativeMutation (null);
		return;
	}

	long selection = GTK.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);

	/*
	 * gtk_tree_store_clear() may call back into SWT (cell data function ->
	 * _getItem() -> gtk_tree_store_set() -> row-changed -> cellDataProc ->
	 * checkData() -> SetData) while rows are being removed. Application code
	 * running in SetData may then access items whose rows are already freed,
	 * causing a SIGSEGV (issue 3329). Suppress these callbacks during the clear.
	 *
	 * Note: don't detach the model via gtk_tree_view_set_model(handle, 0) here.
	 * That resets the internal GtkTreeView state (scroll offset "dy", top row,
	 * anchor, cursor, model signal handler order) without updating the
	 * vertical adjustment, which leads to wrong painting and lost selection
	 * when the tree is refreshed in a scrolled state.
	 */
	clearingModel = true;
	try {
		GTK.gtk_tree_store_clear (modelHandle);
	} finally {
		clearingModel = false;
	}

	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);

	for (int i=0; i<items.length; i++) {
		TreeItem item = items [i];
        if (item != null && !item.isDisposed()) {
            item.release(false);
        }
	}
	items = new TreeItem[4];
	if (virtualTopology != null) {
		virtualTopology.clear ();
		virtualTopology.setChildCount (VirtualTreeTopology.ROOT, 0);
	}

	if (!searchEnabled () || usesVirtualNativeModel ()) {
		GTK.gtk_tree_view_set_search_column (handle, -1);
	} else {
		/* Set the search column whenever the model changes */
		int firstColumn = columnCount == 0 ? FIRST_COLUMN : columns [0].modelIndex;
		GTK.gtk_tree_view_set_search_column (handle, firstColumn + CELL_TEXT);
	}
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the user changes the receiver's selection.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SelectionListener
 * @see #addSelectionListener
 */
public void removeSelectionListener (SelectionListener listener) {
	checkWidget ();
    if (listener == null) {
        error(SWT.ERROR_NULL_ARGUMENT);
    }
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when items in the receiver are expanded or collapsed.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see TreeListener
 * @see #addTreeListener
 */
public void removeTreeListener(TreeListener listener) {
	checkWidget ();
    if (listener == null) {
        error(SWT.ERROR_NULL_ARGUMENT);
    }
    if (eventTable == null) {
        return;
    }
	eventTable.unhook (SWT.Expand, listener);
	eventTable.unhook (SWT.Collapse, listener);
}

void sendMeasureEvent (long cell, long width, long height) {
	virtualCallbackDepth++;
	try {
	if (!ignoreSize && !clearingModel && GTK.GTK_IS_CELL_RENDERER_TEXT (cell) && hooks (SWT.MeasureItem)) {
		long iter = OS.g_object_get_qdata (cell, Display.SWT_OBJECT_INDEX2);
		TreeItem item = null;
        if (iter != 0) {
            item = _getItem(iter);
        }
		if (item != null && !item.isDisposed()) {
			pinVirtualFacade (item);
			int columnIndex = 0;
			if (columnCount > 0) {
				long columnHandle = OS.g_object_get_qdata (cell, Display.SWT_OBJECT_INDEX1);
				for (int i = 0; i < columnCount; i++) {
					if (columns [i].handle == columnHandle) {
						columnIndex = i;
						break;
					}
				}
			}
			int [] contentWidth = new int [1], contentHeight = new int  [1];
            if (width != 0) {
                C.memmove(contentWidth, width, 4);
            }
            if (height != 0) {
                C.memmove(contentHeight, height, 4);
            }
			GTK.gtk_cell_renderer_get_preferred_height_for_width (cell, handle, contentWidth[0], contentHeight, null);
			Image image = item.getImage (columnIndex);
			int imageWidth = 0;
			if (image != null && !image.isDisposed()) {
				imageWidth = image.getBounds ().width;
			}
			contentWidth [0] += imageWidth;
			GC gc = new GC (this);
			gc.setFont (item.getFont (columnIndex));
			Event event = new Event ();
			event.item = item;
			event.index = columnIndex;
			event.gc = gc;
			Rectangle eventRect = new Rectangle (0, 0, contentWidth [0], contentHeight [0]);
			event.setBounds (eventRect);
			long path = viewPath (iter);
			long selection = GTK.gtk_tree_view_get_selection (handle);
			if (usesBoundedVirtualView () ? virtualSelected (virtualTopology.parentId (virtualItemId (item)), virtualTopology.childIndex (virtualItemId (item))) : GTK.gtk_tree_selection_path_is_selected (selection, path)) {
				event.detail = SWT.SELECTED;
			}
			if (path != 0) GTK.gtk_tree_path_free (path);
			sendEvent (SWT.MeasureItem, event);
			gc.dispose ();
			Rectangle rect = event.getBounds ();
			contentWidth [0] = rect.width - imageWidth;
            if (contentHeight [0] < rect.height) {
                contentHeight [0] = rect.height;
            }
            if (width != 0) {
                C.memmove(width, contentWidth, 4);
            }
            if (height != 0) {
                C.memmove(height, contentHeight, 4);
            }
			GTK.gtk_cell_renderer_set_fixed_size (cell, -1, contentHeight [0]);
		}
	}
	} finally {
		virtualCallbackDepth--;
		if (usesBoundedVirtualView () && virtualViewport != null
				&& virtualResidencyGeneration != virtualViewport.generation ()) scheduleVirtualResidency ();
	}

}

@Override
long rendererGetPreferredWidthProc (long cell, long handle, long minimun_size, long natural_size) {
	virtualCallbackDepth++;
	try {
	long g_class = OS.g_type_class_peek_parent (OS.G_OBJECT_GET_CLASS (cell));
	GtkCellRendererClass klass = new GtkCellRendererClass ();
	OS.memmove (klass, g_class);
	OS.call (klass.get_preferred_width, cell, handle, minimun_size, natural_size);
	sendMeasureEvent (cell, minimun_size, 0);
	return 0;
	} finally {
		virtualCallbackDepth--;
		if (usesBoundedVirtualView () && virtualViewport != null
				&& virtualResidencyGeneration != virtualViewport.generation ()) scheduleVirtualResidency ();
	}

}

@Override
long rendererSnapshotProc (long cell, long snapshot, long widget, long background_area, long cell_area, long flags) {
	long rect = Graphene.graphene_rect_alloc();
	GdkRectangle gdkRectangle = new GdkRectangle ();
	OS.memmove(gdkRectangle, background_area, GdkRectangle.sizeof);
	Graphene.graphene_rect_init(rect, gdkRectangle.x, gdkRectangle.y, gdkRectangle.width, gdkRectangle.height);
	long cairo = GTK4.gtk_snapshot_append_cairo(snapshot, rect);
	try {
		rendererRender (cell, cairo, snapshot, widget, background_area, cell_area, 0, flags);
	} finally {
		Cairo.cairo_destroy(cairo);
		Graphene.graphene_rect_free(rect);
	}
	return 0;
}

@Override
long rendererRenderProc (long cell, long cr, long widget, long background_area, long cell_area, long flags) {
	rendererRender (cell, cr, 0, widget, background_area, cell_area, 0, flags);
	return 0;
}

void rendererRender (long cell, long cr, long snapshot, long widget, long background_area, long cell_area, long expose_area, long flags) {
	/*
	 * Don't use the cached iter while removeAll() clears the model: it may
	 * point to a row that is already freed. The rows are being removed anyway
	 * and the tree is redrawn after the clear, so there is nothing to render.
	 */
	if (clearingModel) return;
	virtualCallbackDepth++;
	try {
	TreeItem item = null;
	boolean wasSelected = false;
	long iter = OS.g_object_get_qdata (cell, Display.SWT_OBJECT_INDEX2);
    if (iter != 0) {
        item = _getItem(iter);
    }
	long columnHandle = OS.g_object_get_qdata (cell, Display.SWT_OBJECT_INDEX1);
	int columnIndex = 0;
	if (columnCount > 0) {
		for (int i = 0; i < columnCount; i++) {
			if (columns [i].handle == columnHandle) {
				columnIndex = i;
				break;
			}
		}
	}

	GdkRectangle rendererRect = new GdkRectangle ();
	GdkRectangle columnRect = new GdkRectangle ();
	int y_offset;
	{
		/*
		 * SWT creates multiple renderers (kind of sub-columns) per column.
		 * For example: one for checkbox, one for image, one for text.
		 * 'background_area' argument in this function is area of currently
		 * painted renderer. However, for SWT.EraseItem and SWT.PaintItem,
		 * SWT wants entire column's area along with the event. There's api
		 * 'gtk_tree_view_get_background_area()' but it calculates item's
		 * rect in control, which will have wrong Y if item is rendered
		 * separately (for example, for drag image).
		 * The workaround is to take X range from api and Y range from argument.
		 */
		OS.memmove (rendererRect, background_area, GdkRectangle.sizeof);

		long path = viewPath (iter);
		GTK.gtk_tree_view_get_background_area (handle, path, columnHandle, columnRect);
		GTK.gtk_tree_path_free (path);

		y_offset = columnRect.y - rendererRect.y;
		columnRect.y -= y_offset;
	}

	if (item != null) {
		if (GTK.GTK_IS_CELL_RENDERER_TOGGLE (cell) || ( columnIndex != 0 || (style & SWT.CHECK) == 0)) {
			drawFlags = (int)flags;
			drawState = SWT.FOREGROUND;
			if (usesVirtualNativeModel ()) {
				Color cellBackground = item.virtualCellBackground != null && columnIndex < item.virtualCellBackground.length
						? item.virtualCellBackground [columnIndex] : null;
				if (item.virtualBackground != null || cellBackground != null) drawState |= SWT.BACKGROUND;
			} else {
				long [] ptr = new long [1];
				GTK.gtk_tree_model_get (modelHandle, item.handle, Tree.BACKGROUND_COLUMN, ptr, -1);
				if (ptr [0] == 0) {
					int modelIndex = columnCount == 0 ? Tree.FIRST_COLUMN : columns [columnIndex].modelIndex;
				GTK.gtk_tree_model_get (modelHandle, item.handle, modelIndex + Tree.CELL_BACKGROUND, ptr, -1);
				}
				if (ptr [0] != 0) {
					drawState |= SWT.BACKGROUND;
					GDK.gdk_rgba_free (ptr [0]);
				}
			}
            if ((flags & GTK.GTK_CELL_RENDERER_SELECTED) != 0) {
                drawState |= SWT.SELECTED;
            }
			if ((flags & GTK.GTK_CELL_RENDERER_SELECTED) == 0) {
                if ((flags & GTK.GTK_CELL_RENDERER_FOCUSED) != 0) {
                    drawState |= SWT.FOCUSED;
                }
			}

			Rectangle rect = columnRect.toRectangle ();
			// Use the x and width information from the Cairo context. See bug 535124.
			if (cr != 0) {
				GdkRectangle r2 = new GdkRectangle ();
				if (GTK.GTK4) {
					/* gdk_cairo_get_clip_rectangle() does not exist, and cr is clipped to background_area */
					r2 = rendererRect;
				} else {
					GDK.gdk_cairo_get_clip_rectangle (cr, r2);
				}
				rect.x = r2.x;
				rect.width = r2.width;
			}
			if ((drawState & SWT.SELECTED) == 0) {
				if ((state & PARENT_BACKGROUND) != 0 || backgroundImage != null) {
					Control control = findBackgroundControl ();
					if (control != null) {
						if (cr != 0) {
							Cairo.cairo_save (cr);
						}
						drawBackground (control, 0, cr, rect.x, rect.y, rect.width, rect.height);
						if (cr != 0) {
							Cairo.cairo_restore (cr);
						}
					}
				}
			}

			//send out measure before erase
			long textRenderer =  getTextRenderer (columnHandle);
            if (textRenderer != 0) {
                gtk_cell_renderer_get_preferred_size(textRenderer, handle, null, null);
            }

			if (hooks (SWT.EraseItem)) {
				pinVirtualFacade (item);
				Cairo.cairo_save(cr);
				/*
				 * Cache the selection state so that it is not lost if a
				 * PaintListener wants to draw custom selection foregrounds.
				 * See bug 528155.
				 */
				wasSelected = (drawState & SWT.SELECTED) != 0;
				if (wasSelected) {
					Control control = findBackgroundControl ();
                    if (control == null) {
                        control = this;
                    }
				}
				GC gc = getGC(cr);
				if ((drawState & SWT.SELECTED) != 0) {
					gc.setBackground (display.getSystemColor (SWT.COLOR_LIST_SELECTION));
					gc.setForeground (display.getSystemColor (SWT.COLOR_LIST_SELECTION_TEXT));
				} else {
					gc.setBackground (item.getBackground (columnIndex));
					gc.setForeground (item.getForeground (columnIndex));
				}
				gc.setFont (item.getFont (columnIndex));
                if ((style & SWT.MIRRORED) != 0) {
                    rect.x = getClientWidth() - rect.width - rect.x;
                }

				if (cr != 0) {
					// Use the original rectangle, not the Cairo clipping for the y, width, and height values.
					// See bug 535124.
					gc.setClipping(rect.x, rect.y, rect.width, rect.height);
				} else {
					gc.setClipping(rect.x, rect.y, rect.width, rect.height);
				}

				// SWT.PaintItem/SWT.EraseItem often expect that event.y matches
				// what 'event.item.getBounds()' returns. The workaround is to
				// adjust coordinate system temporarily.
				Event event = new Event ();
				try {
					Rectangle eventRect = new Rectangle (rect.x, rect.y, rect.width, rect.height);

					eventRect.y += y_offset;
					Cairo.cairo_translate (cr, 0, -y_offset);

					event.item = item;
					event.index = columnIndex;
					event.gc = gc;
					event.detail = drawState;
					event.setBounds (eventRect);
					sendEvent (SWT.EraseItem, event);
				} finally {
					Cairo.cairo_translate (cr, 0, y_offset);
				}

				drawForegroundRGBA = null;
				drawState = event.doit ? event.detail : 0;
				drawFlags &= ~(GTK.GTK_CELL_RENDERER_FOCUSED | GTK.GTK_CELL_RENDERER_SELECTED);
                if ((drawState & SWT.SELECTED) != 0) {
                    drawFlags |= GTK.GTK_CELL_RENDERER_SELECTED;
                }
                if ((drawState & SWT.FOCUSED) != 0) {
                    drawFlags |= GTK.GTK_CELL_RENDERER_FOCUSED;
                }
				if ((drawState & SWT.SELECTED) != 0) {
				} else {
					if (wasSelected) {
						drawForegroundRGBA = gc.getForeground ().handle;
					}
				}
				gc.dispose();
				Cairo.cairo_restore (cr);
			}
		}
	}
	if (item != null && (drawState & SWT.BACKGROUND) != 0 && (drawState & SWT.SELECTED) == 0) {
		GC gc = getGC(cr);
		gc.setBackground (item.getBackground (columnIndex));
		gc.fillRectangle (rendererRect.toRectangle ());
		gc.dispose ();
	}
	if ((drawState & SWT.FOREGROUND) != 0 || GTK.GTK_IS_CELL_RENDERER_TOGGLE (cell)) {
		long g_class = OS.g_type_class_peek_parent (OS.G_OBJECT_GET_CLASS (cell));
		GtkCellRendererClass klass = new GtkCellRendererClass ();
		OS.memmove (klass, g_class);
		if (GTK.GTK_IS_CELL_RENDERER_TEXT (cell)) {
			/*
			 * SWT.FOREGROUND means the Tree is responsible for painting the default foreground
			 * color. This can be either the system default (COLOR_LIST_FOREGROUND), or the
			 * color set by setForeground(). See bug 294300.
			 */
			GdkRGBA rgba = foreground != null ? foreground : display.getSystemColor(SWT.COLOR_LIST_FOREGROUND).handle;
			OS.g_object_set (cell, OS.foreground_rgba, rgba, 0);
		}
		if (GTK.GTK4) {
			OS.call (klass.snapshot, cell, snapshot, widget, background_area, cell_area, drawFlags);
		} else {
			OS.call (klass.render, cell, cr, widget, background_area, cell_area, drawFlags);
		}
	}
	if (item != null) {
		/*
		 * GTK4 clips each renderer to its own cell, so the image cell gets the PaintItem of the
		 * text cell too, and each cell shows its part of what the listener draws.
		 */
		long textCell = GTK.GTK4 && GTK.GTK_IS_CELL_RENDERER_PIXBUF (cell) ? getTextRenderer (columnHandle) : cell;
		if (GTK.GTK_IS_CELL_RENDERER_TEXT (textCell)) {
			if (hooks (SWT.PaintItem)) {
                if (wasSelected) {
                    drawState |= SWT.SELECTED;
                }
				Rectangle rect = columnRect.toRectangle ();
				ignoreSize = true;
				int [] contentX = new int [1], contentWidth = new int [1];
				gtk_cell_renderer_get_preferred_size (textCell, handle, contentWidth, null);
				gtk_tree_view_column_cell_get_position (columnHandle, textCell, contentX, null);
				ignoreSize = false;
				Image image = item.getImage (columnIndex);
				int imageWidth = 0;
				if (image != null && !image.isDisposed()) {
					imageWidth = image.getBounds ().width;
				}
				// Account for the image width on GTK3, see bug 535124.
				if (cr != 0) {
					rect.x -= imageWidth;
					rect.width +=imageWidth;
				}
				contentX [0] -= imageWidth;
				contentWidth [0] += imageWidth;

				// Account for the expander arrow offset in x position
				if (GTK.gtk_tree_view_get_expander_column (handle) == columnHandle) {
					/* indent */
					GdkRectangle rect3 = new GdkRectangle ();
					GTK.gtk_widget_realize (handle);
					long path = viewPath (iter);
					GTK.gtk_tree_view_get_cell_area (handle, path, columnHandle, rect3);
					GTK.gtk_tree_path_free (path);
					contentX[0] += rect3.x;
				}
				GC gc = getGC(cr);
				if ((drawState & SWT.SELECTED) != 0) {
					Color background, foreground;
					background = display.getSystemColor (SWT.COLOR_LIST_SELECTION);
					foreground = display.getSystemColor (SWT.COLOR_LIST_SELECTION_TEXT);
					gc.setBackground (background);
					gc.setForeground (foreground);
				} else {
					gc.setBackground (item.getBackground (columnIndex));
					Color foreground;
					foreground = drawForegroundRGBA != null ? Color.gtk_new (display, drawForegroundRGBA) : item.getForeground (columnIndex);
					gc.setForeground (foreground);
				}
				gc.setFont (item.getFont (columnIndex));
				if ((style & SWT.MIRRORED) != 0) {
					rect.x = getClientWidth () - rect.width - rect.x;
				}

				gc.setClipping(rect.x, rect.y, rect.width, rect.height);

				// SWT.PaintItem/SWT.EraseItem often expect that event.y matches
				// what 'event.item.getBounds()' returns. The workaround is to
				// adjust coordinate system temporarily.
				Event event = new Event ();
				try {
					Rectangle eventRect = new Rectangle (rect.x + contentX [0], rect.y, contentWidth [0], rect.height);

					eventRect.y += y_offset;
					Cairo.cairo_translate (cr, 0, -y_offset);

					event.item = item;
					event.index = columnIndex;
					event.gc = gc;
					event.detail = drawState;
					event.setBounds (eventRect);
					sendEvent (SWT.PaintItem, event);
				} finally {
					Cairo.cairo_translate (cr, 0, y_offset);
				}

				gc.dispose();
			}
		}
	}
	} finally {
		virtualCallbackDepth--;
		if (usesBoundedVirtualView () && virtualViewport != null
				&& virtualResidencyGeneration != virtualViewport.generation ()) scheduleVirtualResidency ();
	}

}

private GC getGC(long cr) {
	GC gc;
	GCData gcData = new GCData();
	gcData.cairo = cr;
	gc = GC.gtk_new(this, gcData );
	return gc;
}

void resetCustomDraw () {
    if ((style & SWT.VIRTUAL) != 0 || isOwnerDrawn) {
        return;
    }
	int end = Math.max (1, columnCount);
	for (int i=0; i<end; i++) {
		boolean customDraw = columnCount != 0 ? columns [i].customDraw : firstCustomDraw;
		if (customDraw) {
			long column = GTK.gtk_tree_view_get_column (handle, i);
			long textRenderer = getTextRenderer (column);
			GTK.gtk_tree_view_column_set_cell_data_func (column, textRenderer, 0, 0, 0);
            if (columnCount != 0) {
                columns [i].customDraw = false;
            }
		}
	}
	firstCustomDraw = false;
}

@Override
void reskinChildren (int flags) {
	if (items != null) {
		for (int i=0; i<items.length; i++) {
			TreeItem item = items [i];
            if (item != null) {
                item.reskinChildren(flags);
            }
		}
	}
	if (columns != null) {
		for (int i=0; i<columns.length; i++) {
			TreeColumn column = columns [i];
            if (column != null) {
                column.reskinChildren(flags);
            }
		}
	}
	super.reskinChildren (flags);
}
boolean searchEnabled () {
    /* Disable searching when using VIRTUAL or NO_SEARCH */
    if ((style & SWT.VIRTUAL) != 0 || (style & SWT.NO_SEARCH) != 0) {
        return false;
    }
	return true;
}
/**
 * Display a mark indicating the point at which an item will be inserted.
 * The drop insert item has a visual hint to show where a dragged item
 * will be inserted when dropped on the tree.
 *
 * @param item the insert item.  Null will clear the insertion mark.
 * @param before true places the insert mark above 'item'. false places
 *	the insert mark below 'item'.
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the item has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setInsertMark (TreeItem item, boolean before) {
	checkWidget ();
	if (item == null) {
		GTK.gtk_tree_view_set_drag_dest_row(handle, 0, GTK.GTK_TREE_VIEW_DROP_BEFORE);
		return;
	}
    if (item.isDisposed()) {
        error(SWT.ERROR_INVALID_ARGUMENT);
    }
    if (item.parent != this) {
        return;
    }
	Rectangle rect = item.getBounds();
	long [] path = new long [1];
	GTK.gtk_widget_realize (handle);
    if (!GTK.gtk_tree_view_get_path_at_pos(handle, rect.x, rect.y, path, null, null, null)) {
        return;
    }
    if (path [0] == 0) {
        return;
    }
	int position = before ? GTK.GTK_TREE_VIEW_DROP_BEFORE : GTK.GTK_TREE_VIEW_DROP_AFTER;
	GTK.gtk_tree_view_set_drag_dest_row(handle, path[0], position);
	GTK.gtk_tree_path_free (path [0]);
}

void setItemCount (long parentIter, int count) {
	int residentCount = GTK.gtk_tree_model_iter_n_children (modelHandle, parentIter);
	int topologyParentId =
			virtualTopology != null ? virtualParentId (parentIter) : VirtualTreeTopology.ROOT;
	boolean isVirtual = (style & SWT.VIRTUAL) != 0;
	int logicalCount = isVirtual && virtualTopology.childCountKnown (topologyParentId)
			? virtualTopology.childCount (topologyParentId)
			: residentCount;
    if (count == logicalCount) {
        return;
    }

	VirtualNativeViewState nativeState = usesVirtualNativeModel () ? captureVirtualNativeViewState () : null;
    if (!isVirtual) {
        setRedraw(false);
    }
	try {
		if (parentIter == 0 && count == 0) {
			removeAll ();
			return;
		}

		if (count < residentCount) {
			remove (parentIter, count, residentCount - 1);
			residentCount = count;
		}

		if (isVirtual) {
			virtualTopology.setChildCount (topologyParentId, count);
			/*
			 * Root rows need a scrollable prefix. A collapsed child branch only
			 * needs one sentinel row for its expander. Expansion grows that
			 * prefix to one frontier chunk and rendering near the edge grows it
			 * asynchronously.
			 */
			int minimumResident = parentIter == 0 ? VIRTUAL_FRONTIER_CHUNK : 1;
			int initialTarget = Math.min (count, Math.max (residentCount, minimumResident));
			ensureVirtualNativeChildren (parentIter, initialTarget);
		} else {
			for (int i = residentCount; i < count; i++) {
				new TreeItem (this, parentIter, SWT.NONE, residentCount, 0);
			}
		}
		modelChanged = true;
		// Publish changed child counts before an immediate TreeItem.getItem() query.
		finishVirtualNativeMutation (nativeState);
	} finally {
        if (!isVirtual && !isDisposed()) {
            setRedraw(true);
        }
	}
}

/**
 * Sets the number of root-level items contained in the receiver.
 * <p>
 * The fastest way to insert many items is documented in {@link TreeItem#TreeItem(Tree,int,int)}
 * and {@link TreeItem#setItemCount}
 *
 * @param count the number of items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.2
 */
public void setItemCount (int count) {
	checkWidget ();
	count = Math.max (0, count);
	setItemCount (0, count);
}

/**
 * Selects an item in the receiver.  If the item was already
 * selected, it remains selected.
 *
 * @param item the item to be selected
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the item is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the item has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.4
 */
public void select (TreeItem item) {
	checkWidget ();
    if (item == null) {
        error(SWT.ERROR_NULL_ARGUMENT);
    }
    if (item.isDisposed()) {
        error(SWT.ERROR_INVALID_ARGUMENT);
    }
	if (usesBoundedVirtualView ()) { virtualSelectItem (item, true); return; }
	boolean fixColumn = showFirstColumn ();
	long selection = GTK.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	GTK.gtk_tree_selection_select_iter (selection, item.handle);
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
    if (fixColumn) {
        hideFirstColumn();
    }
}

/**
 * Selects all of the items in the receiver.
 * <p>
 * If the receiver is single-select, do nothing.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void selectAll () {
	checkWidget();
	if (usesBoundedVirtualView ()) {
		if ((style & SWT.SINGLE) != 0 || virtualAllVisibleSelected ()) return;
		virtualSelections.clear ();
		long total = virtualProjection.visibleRowCount ();
		if (total > 0) virtualSelectVisibleRange (0, total - 1);
		restoreVirtualSelection ();
		notifyVirtualAccessibleSelection ();
		return;
	}
    if ((style & SWT.SINGLE) != 0) {
        return;
    }
	boolean fixColumn = showFirstColumn ();
	long selection = GTK.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	GTK.gtk_tree_selection_select_all (selection);
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
    if (fixColumn) {
        hideFirstColumn();
    }
}

@Override
void setBackgroundGdkRGBA (long context, long handle, GdkRGBA rgba) {
	/* Setting the background color overrides the selected background color.
	 * To prevent this, we need to re-set the default. This can be done with CSS
	 * on GTK3.14+, or by using GtkStateFlags as an argument to
	 * gtk_widget_override_background_color() on versions of GTK3 less than 3.16.
	 */
	if (rgba == null) {
		background = defaultBackground();
	} else {
		background = rgba;
	}
	GdkRGBA selectedBackground = display.getSystemColor(SWT.COLOR_LIST_SELECTION).handle;
	String css = "treeview {background-color: " + display.gtk_rgba_to_css_string(background) + ";}\n"
			+ "treeview:selected {background-color: " + display.gtk_rgba_to_css_string(selectedBackground) + ";}";

	// Cache background color
	cssBackground = css;

	// Apply background color and any foreground color
	String finalCss = display.gtk_css_create_css_color_string (cssBackground, cssForeground, SWT.BACKGROUND);
	gtk_css_provider_load_from_css(context, finalCss);
}

@Override
void setBackgroundSurface (Image image) {
	isOwnerDrawn = true;
	recreateRenderers ();
}

@Override
int setBounds (int x, int y, int width, int height, boolean move, boolean resize) {
	int result = super.setBounds (x, y, width, height, move, resize);
	if (result != 0) {
		boundsChangedSinceLastDraw = true;
		scheduleVirtualResidency ();
	}
	/*
	* Bug on GTK.  The tree view sometimes does not get a paint
	* event or resizes to a one pixel square when resized in a new
	* shell that is not visible after any event loop has been run.  The
	* problem is intermittent. It doesn't seem to happen the first time
	* a new shell is created. The fix is to ensure the tree view is realized
	* after it has been resized.
	*/
	GTK.gtk_widget_realize (handle);
	return result;
}

/**
 * Sets the order that the items in the receiver should
 * be displayed in to the given argument which is described
 * in terms of the zero-relative ordering of when the items
 * were added.
 *
 * @param order the new order to display the items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the item order is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the item order is not the same length as the number of items</li>
 * </ul>
 *
 * @see Tree#getColumnOrder()
 * @see TreeColumn#getMoveable()
 * @see TreeColumn#setMoveable(boolean)
 * @see SWT#Move
 *
 * @since 3.2
 */
public void setColumnOrder (int [] order) {
	checkWidget ();
    if (order == null) {
        error(SWT.ERROR_NULL_ARGUMENT);
    }
	if (columnCount == 0) {
        if (order.length > 0) {
            error(SWT.ERROR_INVALID_ARGUMENT);
        }
		return;
	}
    if (order.length != columnCount) {
        error(SWT.ERROR_INVALID_ARGUMENT);
    }
	boolean [] seen = new boolean [columnCount];
	for (int i = 0; i<order.length; i++) {
		int index = order [i];
        if (index < 0 || index >= columnCount) {
            error(SWT.ERROR_INVALID_RANGE);
        }
        if (seen [index]) {
            error(SWT.ERROR_INVALID_ARGUMENT);
        }
		seen [index] = true;
	}
	long baseColumn = 0;
	for (int i=0; i<order.length; i++) {
		long column = columns [order [i]].handle;
		GTK.gtk_tree_view_move_column_after (handle, column, baseColumn);
		baseColumn = column;
	}
}

@Override
void setFontDescription (long font) {
	super.setFontDescription (font);
	TreeColumn[] columns = getColumns ();
	for (int i = 0; i < columns.length; i++) {
		if (columns[i] != null) {
			columns[i].setFontDescription (font);
		}
	}
}

@Override
void setForegroundGdkRGBA (GdkRGBA rgba) {
	foreground = rgba;
	GdkRGBA toSet = rgba == null ? display.COLOR_LIST_FOREGROUND_RGBA : rgba;
	setForegroundGdkRGBA (handle, toSet);
}

/**
 * Sets the header background color to the color specified
 * by the argument, or to the default system color if the argument is null.
 * <p>
 * Note: This operation is a <em>HINT</em> and is not supported on all platforms. If
 * the native header has a 3D look and feel (e.g. Windows 7), this method
 * will cause the header to look FLAT irrespective of the state of the tree style.
 * </p>
 * @param color the new color (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @since 3.106
 */
public void setHeaderBackground(Color color) {
	checkWidget();
	if (color != null) {
        if (color.isDisposed()) {
            error(SWT.ERROR_INVALID_ARGUMENT);
        }
        if (color.equals(headerBackground)) {
            return;
        }
	}
	headerBackground = color;

	updateHeaderCSS();
}

void updateHeaderCSS() {
	StringBuilder css = new StringBuilder("button {");
	if (headerBackground != null) {
		/*
		 * Bug 571466: On some platforms & themes, the 'background-image'
		 * css tag also needs to be set in order to change the
		 * background color. Using 'background' tag as it overrides both
		 * 'background-image' and 'background-color'.
		 */
		css.append("background: " + display.gtk_rgba_to_css_string(headerBackground.handle) + "; ");
	}
	if (headerForeground != null) {
		css.append("color: " + display.gtk_rgba_to_css_string(headerForeground.handle) + "; ");
	}
	css.append("}\n");

	if (columnCount == 0) {
		long buttonHandle = GTK.gtk_tree_view_column_get_button(GTK.gtk_tree_view_get_column(handle, 0));
		if (headerCSSProvider == 0) {
			headerCSSProvider = GTK.gtk_css_provider_new();
			GTK.gtk_style_context_add_provider(GTK.gtk_widget_get_style_context(buttonHandle), headerCSSProvider, GTK.GTK_STYLE_PROVIDER_PRIORITY_APPLICATION);
		}

		if (GTK.GTK4) {
			GTK4.gtk_css_provider_load_from_data(headerCSSProvider, Converter.javaStringToCString(css.toString()), -1);
		} else {
			GTK3.gtk_css_provider_load_from_data(headerCSSProvider, Converter.javaStringToCString(css.toString()), -1, null);
		}
	} else {
		for (TreeColumn column : columns) {
			if (column != null) {
				column.setHeaderCSS(css.toString());
			}
		}
	}
}

/**
 * Sets the header foreground color to the color specified
 * by the argument, or to the default system color if the argument is null.
 * <p>
 * Note: This operation is a <em>HINT</em> and is not supported on all platforms. If
 * the native header has a 3D look and feel (e.g. Windows 7), this method
 * will cause the header to look FLAT irrespective of the state of the tree style.
 * </p>
 * @param color the new color (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @since 3.106
 */
public void setHeaderForeground(Color color) {
	checkWidget();
	if (color != null) {
        if (color.isDisposed()) {
            error(SWT.ERROR_INVALID_ARGUMENT);
        }
        if (color.equals(headerForeground)) {
            return;
        }
	}
	headerForeground = color;

	updateHeaderCSS();
}

/**
 * Marks the receiver's header as visible if the argument is <code>true</code>,
 * and marks it invisible otherwise.
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, marking
 * it visible may not actually cause it to be displayed.
 * </p>
 *
 * @param show the new visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.1
 */
public void setHeaderVisible (boolean show) {
	checkWidget ();
	GTK.gtk_tree_view_set_headers_visible (handle, show);
	this.headerHeight = this.getHeaderHeight();
	this.headerVisible = show;
}

/**
 * Marks the receiver's lines as visible if the argument is <code>true</code>,
 * and marks it invisible otherwise. Note that some platforms draw
 * grid lines while others may draw alternating row colors.
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, marking
 * it visible may not actually cause it to be displayed.
 * </p>
 *
 * @param show the new visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.1
 */
public void setLinesVisible (boolean show) {
	checkWidget();
	//Note: this is overridden by the active theme in GTK3.
	GTK.gtk_tree_view_set_grid_lines (handle, show ? GTK.GTK_TREE_VIEW_GRID_LINES_VERTICAL : GTK.GTK_TREE_VIEW_GRID_LINES_NONE);
}

void setModel (long newModel) {
	display.removeWidget (modelHandle);
	if (virtualViewModel != 0) display.removeWidget (virtualViewModel);
	OS.g_object_unref (modelHandle);
	modelHandle = newModel;
	display.addWidget (modelHandle, this);
	if (virtualViewModel != 0) display.addWidget (virtualViewModel, this);
}

@Override
void setOrientation (boolean create) {
	super.setOrientation (create);
	if (items != null) {
		for (int i=0; i<items.length; i++) {
            if (items[i] != null) {
                items[i].setOrientation(create);
            }
		}
	}
	if (columns != null) {
		for (int i=0; i<columns.length; i++) {
            if (columns[i] != null) {
                columns[i].setOrientation(create);
            }
		}
	}
}

@Override
void setParentBackground () {
	isOwnerDrawn = true;
	recreateRenderers ();
}

@Override
void setParentGdkResource (Control child) {
	/*
	 * Feature in GTK3: non-native GdkWindows are not drawn implicitly
	 * as of GTK3.10+. It is the client's responsibility to propagate draw
	 * events to these windows in the "draw" signal handler.
	 *
	 * This change breaks table editing on GTK3.10+, as the table editor
	 * widgets no longer receive draw signals. The fix is to connect the
	 * Table's fixedHandle to the draw signal, and propagate the draw
	 * signal using gtk_container_propagate_draw(). See bug 531928.
	 */
	if (GTK.GTK4) {
		// long parentGdkSurface = eventSurface ();
		// TODO: GTK4 no gtk_widget_set_parent_surface
		// GTK.gtk_widget_set_parent_surface (child.topHandle(), parentGdkSurface);
		// TODO: implement connectFixedHandleDraw with the "snapshot" signal
	} else {
		long parentGdkWindow = eventWindow ();
		GTK3.gtk_widget_set_parent_window (child.topHandle(), parentGdkWindow);
		hasChildren = true;
		connectFixedHandleDraw();
	}
}

void setScrollWidth (long column, TreeItem item) {
    if (columnCount != 0 || currentItem == item) {
        return;
    }
	int width = GTK.gtk_tree_view_column_get_fixed_width (column);
	int itemWidth = calculateWidth (column, item.handle, true);
	if (width < itemWidth) {
		GTK.gtk_tree_view_column_set_fixed_width (column, itemWidth);
	}
}

/**
 * Sets the receiver's selection to the given item.
 * The current selection is cleared before the new item is selected,
 * and if necessary the receiver is scrolled to make the new selection visible.
 * <p>
 * If the item is not in the receiver, then it is ignored.
 * </p>
 *
 * @param item the item to select
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the item is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the item has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.2
 */
public void setSelection (TreeItem item) {
	checkWidget ();
    if (item == null) {
        error(SWT.ERROR_NULL_ARGUMENT);
    }
	setSelection (new TreeItem [] {item});
}

/**
 * Sets the receiver's selection to be the given array of items.
 * The current selection is cleared before the new items are selected,
 * and if necessary the receiver is scrolled to make the new selection visible.
 * <p>
 * Items that are not in the receiver are ignored.
 * If the receiver is single-select and multiple items are specified,
 * then all items are ignored.
 * </p>
 *
 * @param items the array of items
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the array of items is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if one of the items has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Tree#deselectAll()
 */
public void setSelection (TreeItem [] items) {
	checkWidget ();
    if (items == null) {
        error(SWT.ERROR_NULL_ARGUMENT);
    }
	int length = items.length;
	if (length == 0 || ((style & SWT.SINGLE) != 0 && length > 1)) {
		deselectAll ();
		return;
	}
	Set<TreeItem> wanted = Collections.newSetFromMap (new IdentityHashMap<> ());
	java.util.List<TreeItem> toSelect = new ArrayList<> (length);
	for (TreeItem item : items) {
        if (item == null) {
            continue;
        }
        if (item.isDisposed()) {
            break;
        }
        if (item.parent != this) {
            continue;
        }
        if (wanted.add(item)) {
            toSelect.add(item);
        }
	}
	if (toSelect.isEmpty ()) {
		deselectAll ();
		return;
	}
	if (usesBoundedVirtualView ()) {
		virtualSelections.clear ();
		TreeItem first = toSelect.get (0);
		virtualFocusId = virtualAnchorId = virtualItemId (first);
		revealVirtualItem (first, false);
		if (isDisposed () || first.isDisposed ()) return;
		for (TreeItem item : toSelect) {
			if (item.isDisposed ()) continue;
			if (!expandVirtualAncestors (item) || isDisposed ()) return;
			if (!item.isDisposed ()) virtualSelectItem (item, true);
		}
		/* Expansion listeners for another target may dispose the first Item.
		 * Select focus from the surviving explicit request, never its freed iter. */
		TreeItem focus = null;
		for (TreeItem item : toSelect) {
			if (!item.isDisposed ()) { focus = item; break; }
		}
		refreshVirtualSelectionCounts ();
		virtualFocusId = virtualAnchorId = focus == null ? -1 : virtualItemId (focus);
		if (focus != null) revealVirtualItem (focus, false);
		if (!isDisposed ()) {
			restoreVirtualFocus ();
			notifyVirtualAccessibleSelection ();
		}
		return;
	}
	boolean fixColumn = showFirstColumn ();
	long selection = GTK.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	// Each toggle is linear in the row index and set_cursor unselects all rows, so avoid both where possible
	long firstPath = GTK.gtk_tree_model_get_path (modelHandle, toSelect.get (0).handle);
	showItem (firstPath, false);
	long [] cursorPath = new long [1];
	GTK.gtk_tree_view_get_cursor (handle, cursorPath, null);
	boolean cursorOnFirst = cursorPath [0] != 0 && GTK.gtk_tree_path_compare (cursorPath [0], firstPath) == 0;
    if (cursorPath [0] != 0) {
        GTK.gtk_tree_path_free(cursorPath [0]);
    }
	if (cursorOnFirst) {
        if (GTK.gtk_widget_get_realized(handle)) {
            GTK.gtk_tree_view_scroll_to_cell(handle, firstPath, 0, false, 0, 0);
        }
	} else {
		deselectAll ();
		GTK.gtk_tree_view_set_cursor (handle, firstPath, 0, false);
	}
	GTK.gtk_tree_path_free (firstPath);
	Set<TreeItem> selected = Collections.newSetFromMap (new IdentityHashMap<> ());
	for (TreeItem item : getSelection ()) {
		if (wanted.contains (item)) {
			selected.add (item);
		} else {
			GTK.gtk_tree_selection_unselect_iter (selection, item.handle);
		}
	}
	long parentIter = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
	TreeItem expandedParent = null;
	for (TreeItem item : toSelect) {
        if (selected.contains(item)) {
            continue;
        }
		if (GTK.gtk_tree_model_iter_parent (modelHandle, parentIter, item.handle)) {
			TreeItem parentItem = _getItem (parentIter);
			if (parentItem != expandedParent) {
				long path = GTK.gtk_tree_model_get_path (modelHandle, parentIter);
				showItem (path, false);
				GTK.gtk_tree_view_expand_row (handle, path, false);
				GTK.gtk_tree_path_free (path);
				expandedParent = parentItem;
			}
		}
		GTK.gtk_tree_selection_select_iter (selection, item.handle);
	}
	OS.g_free (parentIter);
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
    if (fixColumn) {
        hideFirstColumn();
    }
}

/**
 * Sets the column used by the sort indicator for the receiver. A null
 * value will clear the sort indicator.  The current sort column is cleared
 * before the new column is set.
 *
 * @param column the column used by the sort indicator or <code>null</code>
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the column is disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.2
 */
public void setSortColumn (TreeColumn column) {
	checkWidget ();
    if (column != null && column.isDisposed()) {
        error(SWT.ERROR_INVALID_ARGUMENT);
    }
	if (sortColumn != null && !sortColumn.isDisposed()) {
		GTK.gtk_tree_view_column_set_sort_indicator (sortColumn.handle, false);
	}
	sortColumn = column;
	if (sortColumn != null && sortDirection != SWT.NONE) {
		GTK.gtk_tree_view_column_set_sort_indicator (sortColumn.handle, true);
		GTK.gtk_tree_view_column_set_sort_order (sortColumn.handle, sortDirection == SWT.DOWN ? 0 : 1);
	}
}

/**
 * Sets the direction of the sort indicator for the receiver. The value
 * can be one of <code>UP</code>, <code>DOWN</code> or <code>NONE</code>.
 *
 * @param direction the direction of the sort indicator
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.2
 */
public void setSortDirection  (int direction) {
	checkWidget ();
    if (direction != SWT.UP && direction != SWT.DOWN && direction != SWT.NONE) {
        return;
    }
	sortDirection = direction;
    if (sortColumn == null || sortColumn.isDisposed()) {
        return;
    }
	if (sortDirection == SWT.NONE) {
		GTK.gtk_tree_view_column_set_sort_indicator (sortColumn.handle, false);
	} else {
		GTK.gtk_tree_view_column_set_sort_indicator (sortColumn.handle, true);
		GTK.gtk_tree_view_column_set_sort_order (sortColumn.handle, sortDirection == SWT.DOWN ? 0 : 1);
	}
}

/**
 * Sets the item which is currently at the top of the receiver.
 * This item can change when items are expanded, collapsed, scrolled
 * or new items are added or removed.
 *
 * @param item the item to be shown
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the item is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the item has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Tree#getTopItem()
 *
 * @since 2.1
 */
public void setTopItem (TreeItem item) {
	checkWidget ();
	if (usesBoundedVirtualView ()) {
		if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
		if (item.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
		if (item.parent == this) revealVirtualItem (item, true);
		return;
	}

	/*
	 * Feature in GTK: cache the GtkAdjustment value for future use in
	 * getTopItem(). Set topItem to item.
	 */
	long vAdjustment;
	vAdjustment = GTK.gtk_scrollable_get_vadjustment(handle);
	cachedAdjustment = GTK.gtk_adjustment_get_value(vAdjustment);
	topItem = item;

    if (item == null) {
        error(SWT.ERROR_NULL_ARGUMENT);
    }
    if (item.isDisposed()) {
        error(SWT.ERROR_INVALID_ARGUMENT);
    }
    if (item.parent != this) {
        return;
    }
	long path = GTK.gtk_tree_model_get_path (modelHandle, item.handle);
	showItem (path, false);
	GTK.gtk_tree_view_scroll_to_cell (handle, path, 0, true, 0, 0);
	if (virtualViewport != null) {
		updateVirtualViewportGeometry ();
		virtualViewport.setTopMaterializedId (virtualItemId (item));
	}
	GTK.gtk_tree_path_free (path);
}

/**
 * Shows the column.  If the column is already showing in the receiver,
 * this method simply returns.  Otherwise, the columns are scrolled until
 * the column is visible.
 *
 * @param column the column to be shown
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the item is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the item has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.1
 */
public void showColumn (TreeColumn column) {
	checkWidget ();
    if (column == null) {
        error(SWT.ERROR_NULL_ARGUMENT);
    }
    if (column.isDisposed()) {
        error(SWT.ERROR_INVALID_ARGUMENT);
    }
    if (column.parent != this) {
        return;
    }

	GTK.gtk_tree_view_scroll_to_cell (handle, 0, column.handle, false, 0, 0);
}

boolean showFirstColumn () {
	/*
	* Bug in GTK.  If no columns are visible, changing the selection
	* will fail.  The fix is to temporarily make a column visible.
	*/
	int columnCount = Math.max (1, this.columnCount);
	for (int i=0; i<columnCount; i++) {
		long column = GTK.gtk_tree_view_get_column (handle, i);
        if (GTK.gtk_tree_view_column_get_visible(column)) {
            return false;
        }
	}
	long firstColumn = GTK.gtk_tree_view_get_column (handle, 0);
	GTK.gtk_tree_view_column_set_visible (firstColumn, true);
	return true;
}

/**
 * Shows the selection.  If the selection is already showing in the receiver,
 * this method simply returns.  Otherwise, the items are scrolled until
 * the selection is visible.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Tree#showItem(TreeItem)
 */
public void showSelection () {
	checkWidget();
	if (usesBoundedVirtualView ()) {
		refreshVirtualSelectionCounts ();
		for (var entry : virtualSelections.entrySet ()) {
			VirtualSelectionModel selection = entry.getValue ();
			if (selection.selectedCount () == 0) continue;
			int index = selection.complementMode () ? 0 : selection.rangeStart (0);
			while (!selection.isSelected (index)) {
				for (int range = 0; range < selection.rangeCount (); range++) {
					if (selection.rangeStart (range) <= index && index < selection.rangeEndExclusive (range)) {
						index = selection.rangeEndExclusive (range); break;
					}
				}
			}
			showItem (virtualCoordinateItem (entry.getKey (), index));
			break;
		}
		return;
	}
	TreeItem [] items = getSelection ();
    if (items.length != 0 && items [0] != null) {
        showItem(items [0]);
    }
}

void showItem (long path, boolean scroll) {
	int depth = GTK.gtk_tree_path_get_depth (path);
	if (depth > 1) {
		int [] indices = new int [depth - 1];
		long indicesPtr = GTK.gtk_tree_path_get_indices (path);
		C.memmove (indices, indicesPtr, indices.length * 4);
		long tempPath = GTK.gtk_tree_path_new ();
		for (int i=0; i<indices.length; i++) {
			GTK.gtk_tree_path_append_index (tempPath, indices [i]);
			GTK.gtk_tree_view_expand_row (handle, tempPath, false);
		}
		GTK.gtk_tree_path_free (tempPath);
	}
	if (scroll) {
		GTK.gtk_tree_view_scroll_to_cell (handle, path, 0, false, 0.5f, 0.0f);
	}
}

/**
 * Shows the item.  If the item is already showing in the receiver,
 * this method simply returns.  Otherwise, the items are scrolled
 * and expanded until the item is visible.
 *
 * @param item the item to be shown
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the item is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the item has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Tree#showSelection()
 */
public void showItem (TreeItem item) {
	checkWidget ();
    if (item == null) {
        error(SWT.ERROR_NULL_ARGUMENT);
    }
    if (item.isDisposed()) {
        error(SWT.ERROR_INVALID_ARGUMENT);
    }
    if (item.parent != this) {
        return;
    }
	if (usesBoundedVirtualView ()) { revealVirtualItem (item, false); return; }
	long path = GTK.gtk_tree_model_get_path (modelHandle, item.handle);
	showItem (path, true);
	GTK.gtk_tree_path_free (path);
}

@Override
void updateScrollBarValue (ScrollBar bar) {
	super.updateScrollBarValue (bar);

	if (!GTK.GTK4) {
		/*
		* Bug in GTK. Scrolling changes the XWindow position
		* and makes the child widgets appear to scroll even
		* though when queried their position is unchanged.
		* The fix is to queue a resize event for each child to
		* force the position to be corrected.
		*/
		long parentHandle = parentingHandle ();
		long list = GTK3.gtk_container_get_children (parentHandle);
        if (list == 0) {
            return;
        }
		long temp = list;
		while (temp != 0) {
			long widget = OS.g_list_data (temp);
            if (widget != 0) {
                GTK.gtk_widget_queue_resize(widget);
            }
			temp = OS.g_list_next (temp);
		}
		OS.g_list_free (list);
	}
}

@Override
long windowProc (long handle, long arg0, long user_data) {
	switch ((int)user_data) {
		case EXPOSE_EVENT: {
			/*
			 * If this Tree has any child widgets, propagate the draw signal
			 * to them using gtk_container_propagate_draw(). See bug 531928.
			 */
			if (hasChildren) {
				/*
				 * If headers are visible, set noChildDrawing to true
				 * this will prevent any child widgets from drawing
				 * over the header buttons. See bug 535978.
				 */
				if (headerVisible) {
					noChildDrawing = true;
				}
				propagateDraw(handle, arg0);
			}
			/*
			 * Ensure the paint listener's drawing appears on top of items rather than being
			 * overwritten by them.
			 */
			if (!GTK.GTK4) {
				gtk3_paintEvent(arg0);
			}
			break;
		}
		case EXPOSE_EVENT_INVERSE: {
			/*
			 * Feature in GTK. When the GtkTreeView has no items it does not propagate
			 * expose events. The fix is to fill the background in the inverse expose
			 * event.
			 */
			int itemCount = GTK.gtk_tree_model_iter_n_children (modelHandle, 0);
			if (itemCount == 0 && (state & OBSCURED) == 0) {
				if ((state & PARENT_BACKGROUND) != 0 || backgroundImage != null) {
					Control control = findBackgroundControl ();
					if (control != null) {
						long window = GTK3.gtk_tree_view_get_bin_window (handle);
						if (window == GTK3.gtk_widget_get_window(handle)) {
							GdkRectangle rect = new GdkRectangle ();
							GDK.gdk_cairo_get_clip_rectangle (arg0, rect);
							drawBackground (control, window, arg0, rect.x, rect.y, rect.width, rect.height);
						}
					}
				}
			}
			break;
		}
	}
	return super.windowProc (handle, arg0, user_data);
}

@Override
Point resizeCalculationsGTK3 (long widget, int width, int height) {
	Point sizes = super.resizeCalculationsGTK3(widget, width, height);
	/*
	 * Bug - Resizing Problems View can cause invalid rectangle errors on standard eror
	 *
	 * When resizing an SWT tree or table, its possible that the horizontal scrollbar overlaps with the column headers.
	 * This is possible due to SWT native resizing on the scrolled window of the tree or table.
	 * To avoid the error, we set a minimal size for the scrolled window.
	 * This height is equal to the header and scrollbar heights if both are visible,
	 * plus the total border height (bottom and top border height combined).
	 * In the error case, the SWT fixed which contains the tree still resizes as expected,
	 * and the horizontal scrollbar is only partially visible so that it doesn't overlap with tree headers.
	 */
	if (widget == scrolledHandle && getHeaderVisible()) {
		int hScrollBarHeight = hScrollBarWidth(); // this actually returns height
		if (hScrollBarHeight > 0) {
			sizes.y = Math.max(sizes.y, getHeaderHeight() + hScrollBarHeight + (getBorderWidth() * 2));
		}
	}
	return sizes;
}

/**
 * Check the tree for items that are in process of
 * sending {@code SWT#SetData} event. If such items exist, throw an exception.
 *
 * Does nothing if the given range contains no indices,
 * or if we are below GTK 3.22.0 or are using GTK 4.
 */
void checkSetDataInProcessBeforeRemoval() {
	/*
	 * Bug 182598 - assertion failed in gtktreestore.c
	 *
	 * To prevent a crash in GTK, we ensure we are not setting data on the tree items we are about to remove.
	 * Removing an item while its data is being set will invalidate it, which will cause a crash.
	 *
	 * We therefore throw an exception to prevent the crash.
	 */
	for (int i = 0; i < items.length; i++) {
		TreeItem item = items[i];
		if (item != null && item.settingData) {
			throwCannotRemoveItem(i);
		}
	}
}

/**
 * Fire the paint event explicitly, so the paint listener's drawing is not lost.
 */
private void gtk3_paintEvent(long cairo) {
    if ((state & OBSCURED) != 0) {
        return;
    }
	if (drawRegion) {
		cairoClipRegion(cairo);
	}
    if (!hooksPaint()) {
        return;
    }
	GdkRectangle rect = new GdkRectangle();
	GDK.gdk_cairo_get_clip_rectangle(cairo, rect);
	Event event = new Event();
	event.count = 1;
	Rectangle eventBounds = new Rectangle(rect.x, rect.y, rect.width, rect.height);
    if ((style & SWT.MIRRORED) != 0) {
        eventBounds.x = getClientWidth() - eventBounds.width - eventBounds.x;
    }
	event.setBounds(eventBounds);
	GCData data = new GCData();
    if (drawRegion) {
        data.regionSet = eventRegion;
    }
	data.cairo = cairo;
	GC gc = event.gc = GC.gtk_new(this, data);
	gc.setClipping(eventBounds.x, eventBounds.y, eventBounds.width, eventBounds.height);
	drawWidget(gc);
	sendEvent(SWT.Paint, event);
	gc.dispose();
	event.gc = null;
}

@Override
void snapshotToDraw(long handle, long snapshot) {
	// Tree renders via native GTK children (GtkScrolledWindow > GtkTreeView).
	// Like GTK3 where Tree explicitly fires paint in EXPOSE_EVENT (after=true),
	// GTK4 must paint after children so SWT.Paint overlays appear on top.
}

@Override
void snapshotToDrawAfterChildren(long handle, long snapshot) {
	snapshotPaint(handle, snapshot);
}

private void throwCannotRemoveItem(int i) {
	String message = "Cannot remove item with index " + i + ".";
	throw new SWTException(message);
}

@Override
public void dispose() {
	super.dispose();

	if (headerCSSProvider != 0) {
		OS.g_object_unref(headerCSSProvider);
		headerCSSProvider = 0;
	}
}
}
