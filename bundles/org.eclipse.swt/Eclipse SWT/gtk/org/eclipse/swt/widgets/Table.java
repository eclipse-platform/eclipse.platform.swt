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
 *     Lars Vogel <Lars.Vogel@vogella.com> - Bug 509648
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
 * Instances of this class implement a selectable user interface
 * object that displays a list of images and strings and issues
 * notification when selected.
 * <p>
 * The item children that may be added to instances of this class
 * must be of type <code>TableItem</code>.
 * </p><p>
 * Style <code>VIRTUAL</code> is used to create a <code>Table</code> whose
 * <code>TableItem</code>s are to be populated by the client on an on-demand basis
 * instead of up-front.  This can provide significant performance improvements for
 * tables that are very large or for which <code>TableItem</code> population is
 * expensive (for example, retrieving values from an external source).
 * </p><p>
 * Here is an example of using a <code>Table</code> with style <code>VIRTUAL</code>:</p>
 * <pre><code>
 *  final Table table = new Table (parent, SWT.VIRTUAL | SWT.BORDER);
 *  table.setItemCount (1000000);
 *  table.addListener (SWT.SetData, new Listener () {
 *      public void handleEvent (Event event) {
 *          TableItem item = (TableItem) event.item;
 *          int index = table.indexOf (item);
 *          item.setText ("Item " + index);
 *          System.out.println (item.getText ());
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
 * <dd>SINGLE, MULTI, CHECK, FULL_SELECTION, HIDE_SELECTION, VIRTUAL, NO_SCROLL, NO_SEARCH</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection, DefaultSelection, SetData, MeasureItem, EraseItem, PaintItem</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles SINGLE, and MULTI may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="https://eclipse.dev/eclipse/swt/snippets/#table">Table, TableItem, TableColumn snippets</a>
 * @see <a href="https://eclipse.dev/eclipse/swt/examples.html">SWT Example: ControlExample</a>
 * @see <a href="https://eclipse.dev/eclipse/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Table extends Composite {
	long modelHandle, checkRenderer;
	long virtualViewModel, virtualViewAdjustment, verticalAdjustment;
	long virtualHorizontalAdjustment, horizontalAdjustment;
	long virtualHorizontalValueSignal, virtualHorizontalRangeSignal, virtualPhysicalHorizontalSignal;
	boolean virtualHorizontalDirty = true, updatingVirtualHorizontal, virtualHorizontalRangeDirty;
	boolean virtualRenderersDirty;
	static final int VIRTUAL_HORIZONTAL_OVERSCAN = 64;
	TableColumn [] virtualHorizontalResidents = new TableColumn [0];
	long virtualImplicitResident, virtualColumnEventGeneration = -1;
	int virtualHorizontalLayoutWidth = -1;
	long virtualHorizontalColumnVisits;
	long virtualLogicalValueSignal, virtualViewRangeSignal, virtualViewValueSignal;
	Callback virtualAdjustmentCallback;
	Map<TableItem, Map<TableColumn, java.lang.ref.WeakReference<Accessible>>> virtualAccessibleCells;
	Map<TableColumn, Accessible> virtualAccessibleHeaders;
	Map<Accessible, Long> virtualAccessibleStates;
	long virtualAccessibleGeneration, virtualModelGeneration, virtualColumnsSignal;
	int virtualAccessibleFocusId;
	boolean virtualAccessibleColumnsDirty;
	Boolean virtualBoundedView, virtualBoundedHorizontal;
	int virtualViewStart, virtualViewCount, virtualRowExtent = 1, virtualBodyExtent;
	int virtualFocusIndex = -1, virtualAnchorIndex = -1, virtualCallbackDepth;
	double virtualPixelRemainder;
	boolean reconcilingVirtualView, virtualViewScheduled, virtualViewChanged;
	boolean updatingVirtualAdjustment, updatingVirtualGeometry, virtualViewRangeDirty;
	int itemCount, columnCount, lastIndexOf, sortDirection;
	int selectionCountOnPress,selectionCountOnRelease;
	long ignoreCell;
	TableItem [] items;
	VirtualItemStorage<TableItem> virtualItems;
	VirtualViewportPlanner virtualViewport;
	TableColumn [] columns;
	TableItem currentItem;
	TableColumn sortColumn;
	ImageList imageList, headerImageList;
	boolean firstCustomDraw;
	/** True iff computeSize has never been called on this Table */
	boolean firstCompute = true;
	int drawState, drawFlags;
	GdkRGBA background, foreground, drawForegroundRGBA;
	Color headerBackground, headerForeground;
	boolean ownerDraw, ignoreSize, pixbufSizeSet, hasChildren;
	int maxWidth = 0;
	int topIndex, pendingTopIndex = -1;
	double cachedAdjustment, currentAdjustment;
	int pixbufHeight, pixbufWidth;
	int headerHeight;
	boolean boundsChangedSinceLastDraw, headerVisible, wasScrolled;
	ViewportLayerState viewportLayers;
	boolean rowActivated;

	private long headerCSSProvider;

	static final int CHECKED_COLUMN = 0;
	static final int GRAYED_COLUMN = 1;
	static final int FOREGROUND_COLUMN = 2;
	static final int BACKGROUND_COLUMN = 3;
	static final int FONT_COLUMN = 4;
	static final int FIRST_COLUMN = FONT_COLUMN + 1;
	static final int CELL_PIXBUF = 0;
	static final int CELL_TEXT = 1;
	static final int CELL_FOREGROUND = 2;
	static final int CELL_BACKGROUND = 3;
	static final int CELL_FONT = 4;
	static final int CELL_SURFACE = 5;
	static final int CELL_TYPES = CELL_SURFACE + 1;
	static final byte [] VIRTUAL_MODEL_ITEM_COUNT =
			Converter.wcsToMbcs ("swt-item-count", true);
	static final String VIRTUAL_BOUNDED_VIEW_PROPERTY =
			"org.eclipse.swt.internal.gtk.virtualTableBoundedView";

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
 * @see SWT#HIDE_SELECTION
 * @see SWT#VIRTUAL
 * @see SWT#NO_SCROLL
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Table (Composite parent, int style) {
	super (parent, checkStyle (style));
}

@Override
void _addListener (int eventType, Listener listener) {
	super._addListener (eventType, listener);
	if (!ownerDraw) {
		switch (eventType) {
			case SWT.MeasureItem:
			case SWT.EraseItem:
			case SWT.PaintItem:
				ownerDraw = true;
				recreateRenderers ();
				break;
		}
	}
}

TableItem _getItem (int index) {
    if ((style & SWT.VIRTUAL) == 0) {
        return items [index];
    }
	TableItem item = virtualItems.get (index);
    if (item != null) {
        return item;
    }
	item = new TableItem (this, SWT.NONE, index, false);
	virtualItems.put (index, item);
	return item;
}

TableItem _getItem (int index, boolean create) {
    if ((style & SWT.VIRTUAL) == 0) {
        return items [index];
    }
	TableItem item = virtualItems.get (index);
	return item != null || !create ? item : _getItem (index);
}

int materializedItemCount () {
	return (style & SWT.VIRTUAL) != 0 ? (virtualItems == null ? 0 : virtualItems.size ()) : itemCount;
}

TableItem materializedItem (int position) {
	return (style & SWT.VIRTUAL) != 0 ? virtualItems.valueAt (position) : items [position];
}

int materializedIndex (int position) {
	return (style & SWT.VIRTUAL) != 0 ? virtualItems.indexAt (position) : position;
}

void virtualItemChanged (TableItem item) {
    if ((style & SWT.VIRTUAL) == 0 || handle == 0) {
        return;
    }
	notifyVirtualAccessibleItemStates (item);
	if (!isVirtualPaintCandidate (item)) {
		item.markVirtualDirty ();
		return;
	}
	GTK.gtk_widget_queue_draw (handle);
}

void updateVirtualViewport () {
    if (virtualViewport == null) {
        return;
    }
	if (usesBoundedVirtualView ()) {
		updateVirtualViewportGeometry ();
		return;
	}
	Rectangle client = getClientAreaInPixels ();
	int rowHeight = Math.max (1, getItemHeight ());
	int chromeHeight = getHeaderVisible () ? getHeaderHeight () : 0;
	int bodyHeight = isVisible () && client.width > 0 ? Math.max (0, client.height - chromeHeight) : 0;
	int first = itemCount == 0 ? 0 : Math.min (getTopIndex (), itemCount - 1);
	virtualViewport.setUniformGeometry (itemCount, rowHeight, bodyHeight, first);
}

boolean isVirtualPaintCandidate (TableItem item) {
    if (virtualViewport == null) {
        return true;
    }
	updateVirtualViewport ();
	int index = virtualItems.indexOfIdentity (item);
	return index >= 0 && virtualViewport.isPaintCandidate (index);
}

boolean usesVirtualNativeModel () {
	return (style & SWT.VIRTUAL) != 0 && !GTK.GTK4;
}

boolean boundedVirtualViewEnabled () {
	if (!usesVirtualNativeModel ()) return false;
	if (virtualBoundedView == null) {
		virtualBoundedView = Boolean.valueOf (System.getProperty (VIRTUAL_BOUNDED_VIEW_PROPERTY, "false"));
	}
	return virtualBoundedView.booleanValue ();
}

boolean usesBoundedVirtualView () {
	return virtualViewModel != 0;
}

long viewModel () {
	return usesBoundedVirtualView () ? virtualViewModel : modelHandle;
}

int modelIndex (long model, long iter) {
	long path = GTK.gtk_tree_model_get_path (model, iter);
	if (path == 0) return -1;
	try {
		long indices = GTK.gtk_tree_path_get_indices (path);
		if (indices == 0) return -1;
		int [] index = new int [1];
		C.memmove (index, indices, 4);
		long logical = index [0] + (model == virtualViewModel ? (long)virtualViewStart : 0);
		return logical >= 0 && logical < itemCount ? (int)logical : -1;
	} finally {
		GTK.gtk_tree_path_free (path);
	}
}

long virtualViewPath (int index) {
	if (index < virtualViewStart || index - virtualViewStart >= virtualViewCount) return 0;
	long path = GTK.gtk_tree_path_new ();
	GTK.gtk_tree_path_append_index (path, index - virtualViewStart);
	return path;
}

long viewPath (long iter) {
	if (iter == 0) return 0;
	if (!usesBoundedVirtualView ()) return GTK.gtk_tree_model_get_path (modelHandle, iter);
	int index = modelIndex (modelHandle, iter);
	if (index < 0) index = modelIndex (virtualViewModel, iter);
	return virtualViewPath (index);
}

void virtualCellArea (TableItem item, long path, long column, GdkRectangle rect) {
	GTK.gtk_tree_view_get_cell_area (handle, path, column, rect);
	if (usesBoundedHorizontalView ()) virtualLogicalCellArea (column, rect);
	if (!usesBoundedVirtualView () || path != 0) return;
	int index = virtualItems.indexOfIdentity (item);
	if (index < 0) return;
	int [] separator = new int [1];
	GTK3.gtk_widget_style_get (handle, Converter.wcsToMbcs ("vertical-separator", true), separator, 0);
	double y = (index - (double)virtualViewport.firstVisible ()) * virtualRowExtent
			- virtualPixelRemainder + separator [0] / 2;
	rect.y = (int)Math.max (Integer.MIN_VALUE, Math.min (Integer.MAX_VALUE, y));
	rect.height = Math.max (0, virtualRowExtent - separator [0]);
}

boolean boundedHorizontalViewEnabled () {
	if (virtualBoundedHorizontal == null) virtualBoundedHorizontal = usesBoundedVirtualView ()
			&& Boolean.getBoolean ("org.eclipse.swt.internal.gtk.virtualHorizontalViewport");
	return virtualBoundedHorizontal;
}

boolean usesBoundedHorizontalView () {
	return virtualHorizontalAdjustment != 0;
}

/** Logical columns remain present when their native paint slots are absent. */
int virtualColumnIndex (long column) {
	if (columnCount == 0) return 0;
	if (virtualHorizontalDirty && virtualViewport != null) virtualHorizontalWindow ();
	Widget widget = display.getWidget (column);
	return widget instanceof TableColumn value && value.parent == this ? value.virtualIndex : -1;
}

VirtualViewportPlanner.HorizontalWindow virtualHorizontalWindow () {
	VirtualViewportPlanner.HorizontalWindow window = virtualViewport.horizontal ();
	int width = Math.max (0, getClientWidth ());
	if (virtualHorizontalAdjustment != 0) {
		double page = GTK.gtk_adjustment_get_page_size (virtualHorizontalAdjustment);
		if (page > 0) width = (int)Math.min (Integer.MAX_VALUE, page);
	}
	if (virtualHorizontalDirty || virtualHorizontalLayoutWidth != width) {
		long [] widths = new long [Math.max (1, columnCount)];
		int [] order = columnCount == 0 ? new int [] { 0 } : getColumnOrder ();
		if (columnCount == 0) {
			widths [0] = Math.max (Math.max (1, maxWidth), width);
		} else {
			long requested = 0;
			for (int i = 0; i < columnCount; i++) {
				columns [i].virtualIndex = i;
				widths [i] = columns [i].virtualLogicalWidth;
				requested += widths [i];
			}
			// GtkTreeView gives unused viewport width to the last visible
			// column. Keep requested widths separately for a later resize.
			if (requested < width) {
				for (int slot = order.length - 1; slot >= 0; slot--) {
					if (widths [order [slot]] == 0) continue;
					widths [order [slot]] += width - requested;
					break;
				}
			}
		}
		window.setColumns (widths, order);
		virtualHorizontalDirty = false;
		virtualHorizontalLayoutWidth = width;
	}
	window.setViewport (window.origin (), width, VIRTUAL_HORIZONTAL_OVERSCAN, (style & SWT.MIRRORED) != 0);
	return window;
}

void virtualColumnsChanged () {
	if (!usesBoundedHorizontalView ()) return;
	virtualHorizontalDirty = virtualAccessibleColumnsDirty = true;
	scheduleVirtualView ();
}

boolean virtualColumnVisible (long column) {
	if (!usesBoundedHorizontalView () || virtualViewport == null) return GTK.gtk_tree_view_column_get_visible (column);
	int index = virtualColumnIndex (column);
	return index >= 0 && virtualHorizontalWindow ().columnWidth (index) != 0;
}

int virtualNativeX (int x) {
	if (!usesBoundedHorizontalView ()) return x;
	long physical = (style & SWT.MIRRORED) != 0 ? getClientWidth () - (long)x - 1 : x;
	return virtualCoordinate (physical + (long)GTK.gtk_adjustment_get_value (virtualHorizontalAdjustment));
}

static int virtualCoordinate (long value) {
	return (int)Math.max (Integer.MIN_VALUE, Math.min (Integer.MAX_VALUE, value));
}

void virtualLogicalCellArea (long column, GdkRectangle rect) {
	int index = virtualColumnIndex (column);
	if (index < 0 || virtualViewport == null) return;
	VirtualViewportPlanner.HorizontalWindow window = virtualHorizontalWindow ();
	int [] separator = new int [1];
	GTK3.gtk_widget_style_get (handle, Converter.wcsToMbcs ("horizontal-separator", true), separator, 0);
	// Match GTK physical-left coordinates; existing Item code applies SWT.MIRRORED.
	rect.x = virtualCoordinate (window.viewportX (index) + separator [0] / 2);
	rect.width = (int)Math.max (0, window.columnWidth (index) - separator [0]);
}

/** Keep the full coordinate until Item bounds apply their renderer offsets. */
long virtualItemCellX (long column, int cellWidth) {
	int index = virtualColumnIndex (column);
	VirtualViewportPlanner.HorizontalWindow window = virtualHorizontalWindow ();
	long x = window.viewportX (index) + (window.columnWidth (index) - cellWidth) / 2;
	return (style & SWT.MIRRORED) != 0 ? getClientWidth () - (long)cellWidth - x : x;
}

void virtualColumnPosition (long column, long renderer, int [] x, int [] width) {
	if (!usesBoundedHorizontalView () || virtualViewport == null) {
		gtk_tree_view_column_cell_get_position (column, renderer, x, width);
		return;
	}
	GdkRectangle allocation = new GdkRectangle ();
	// GTK cell_get_position allocates against the full background width.
	boolean allocated = virtualRendererAllocation (column, renderer, Math.max (1, virtualRowExtent), false, allocation);
	if (isDisposed () || virtualColumnIndex (column) < 0) error (SWT.ERROR_WIDGET_DISPOSED);
	if (!allocated) {
		if (x != null) x [0] = 0;
		if (width != null) width [0] = 0;
		return;
	}
	if (x != null) x [0] = allocation.x;
	if (width != null) width [0] = allocation.width;
}

boolean virtualRendererAllocation (long column, long renderer, int height, GdkRectangle allocation) {
	return virtualRendererAllocation (column, renderer, height, true, allocation);
}

boolean virtualRendererAllocation (long column, long renderer, int height, boolean content, GdkRectangle allocation) {
	int index = virtualColumnIndex (column);
	if (index < 0 || virtualViewport == null) return false;
	int [] separator = new int [1];
	GTK3.gtk_widget_style_get (handle, Converter.wcsToMbcs ("horizontal-separator", true), separator, 0);
	int width = (int)Math.max (0, virtualHorizontalWindow ().columnWidth (index) - (content ? separator [0] : 0));
	virtualCallbackDepth++;
	try {
		return OS.swt_tree_view_column_get_logical_cell_allocation (column, handle, renderer, width,
				Math.max (1, height), allocation);
	} finally {
		virtualCallbackDepth--;
		if (!isDisposed () && (virtualRenderersDirty || virtualHorizontalDirty)) scheduleVirtualView ();
	}
}

/** Reconcile the bounded native columns without changing logical widths or row ownership. */
void reconcileVirtualHorizontal () {
	if (!usesBoundedHorizontalView () || virtualViewport == null || updatingVirtualHorizontal || isDisposed ()) return;
	VirtualViewportPlanner.HorizontalWindow window = virtualHorizontalWindow ();
	updatingVirtualHorizontal = true;
	boolean previous = updatingVirtualAdjustment;
	updatingVirtualAdjustment = true;
	try {
		// Only the previous and next resident unions are visited during a seek.
		// Rebuilding the logical directory is reserved for column mutations.
		for (TableColumn old : virtualHorizontalResidents) {
			if (!old.isDisposed () && window.residentColumnWidth (old.virtualIndex) == 0) {
				GTK.gtk_tree_view_column_set_visible (old.handle, false);
			}
			virtualHorizontalColumnVisits++;
		}
		if (virtualImplicitResident != 0) {
			long first = GTK.gtk_tree_view_get_column (handle, 0);
			if (first == virtualImplicitResident) GTK.gtk_tree_view_column_set_visible (first, false);
			virtualImplicitResident = 0;
		}
		TableColumn [] next = new TableColumn [columnCount == 0 ? 0 : window.endSlot () - window.firstSlot ()];
		for (int slot = window.firstSlot (); slot < window.endSlot (); slot++) {
			int i = window.columnAtSlot (slot);
			TableColumn column = columnCount == 0 ? null : columns [i];
			long nativeColumn = column == null ? GTK.gtk_tree_view_get_column (handle, 0) : column.handle;
			int width = window.residentColumnWidth (i);
			virtualHorizontalColumnVisits++;
			if (column != null) {
				column.virtualPhysicalWidth = width;
				next [slot - window.firstSlot ()] = column;
			} else virtualImplicitResident = nativeColumn;
			if (GTK.gtk_tree_view_column_get_fixed_width (nativeColumn) != width) {
				GTK.gtk_tree_view_column_set_fixed_width (nativeColumn, width);
			}
			if (!GTK.gtk_tree_view_column_get_visible (nativeColumn)) {
				GTK.gtk_tree_view_column_set_visible (nativeColumn, true);
			}
			if (column != null) {
				boolean trailingEdge = window.rightToLeft () ? window.sourceOffset (i) == 0
						: window.sourceOffset (i) + width == window.columnWidth (i);
				GTK.gtk_tree_view_column_set_resizable (nativeColumn, column.virtualResizable && trailingEdge);
				column.virtualAllocateHeader ();
			}
		}
		if (window.firstSlot () == window.endSlot ()) {
			long sentinel = GTK.gtk_tree_view_get_column (handle, 0);
			if (sentinel != 0) {
				GTK.gtk_tree_view_column_set_fixed_width (sentinel, 1);
				GTK.gtk_tree_view_column_set_resizable (sentinel, false);
				GTK.gtk_tree_view_column_set_visible (sentinel, true);
				if (columnCount != 0) columns [0].virtualPhysicalWidth = 1;
				virtualImplicitResident = sentinel;
			}
		}
		virtualHorizontalResidents = next;
		if (virtualColumnEventGeneration != window.geometryGeneration ()) {
			virtualColumnEventGeneration = window.geometryGeneration ();
			for (int i = 0; i < columnCount; i++) {
				TableColumn column = columns [i];
				long x = window.rightToLeft () ? window.contentWidth () - window.columnStart (i) - window.columnWidth (i)
						: window.columnStart (i);
				if (column.virtualLogicalX != x) {
					column.virtualLogicalX = x;
					column.sendEvent (SWT.Move);
					if (isDisposed ()) return;
					if (virtualHorizontalDirty) { scheduleVirtualView (); return; }
				}
			}
		}
		double page = window.viewportWidth ();
		OS.g_signal_handlers_block_matched (horizontalAdjustment, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
		try {
			GTK.gtk_adjustment_configure (horizontalAdjustment, virtualHorizontalBarValue (window), 0,
					Math.max (page, window.contentWidth ()), 16, Math.max (1, page - 16), page);
		} finally {
			OS.g_signal_handlers_unblock_matched (horizontalAdjustment, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
		}
		GTK.gtk_adjustment_set_value (virtualHorizontalAdjustment, Math.max (0, -window.residentViewportX ()));
		virtualHorizontalRangeDirty = false;
		int dirty = viewportLayers.scrollTo (window.origin (), viewportLayers.originY ());
		if ((dirty & ViewportLayerState.HEADER) != 0) { wasScrolled = true; GTK.gtk_widget_queue_draw (handle); }
	} finally {
		updatingVirtualAdjustment = previous;
		updatingVirtualHorizontal = false;
	}
}

long virtualHorizontalBarValue (VirtualViewportPlanner.HorizontalWindow window) {
	return window.rightToLeft () ? Math.max (0, window.contentWidth () - window.viewportWidth ()) - window.origin () : window.origin ();
}

void virtualSetHorizontalOrigin (long origin) {
	VirtualViewportPlanner.HorizontalWindow window = virtualHorizontalWindow ();
	window.setViewport (origin, window.viewportWidth (), VIRTUAL_HORIZONTAL_OVERSCAN, window.rightToLeft ());
	boolean previous = updatingVirtualAdjustment;
	updatingVirtualAdjustment = true;
	try { GTK.gtk_adjustment_set_value (horizontalAdjustment, virtualHorizontalBarValue (window)); }
	finally { updatingVirtualAdjustment = previous; }
	scheduleVirtualView ();
}

void virtualShowColumn (int index) {
	VirtualViewportPlanner.HorizontalWindow window = virtualHorizontalWindow ();
	if (window.ensureColumnVisible (index)) {
		boolean previous = updatingVirtualAdjustment;
		updatingVirtualAdjustment = true;
		try { GTK.gtk_adjustment_set_value (horizontalAdjustment, virtualHorizontalBarValue (window)); }
		finally { updatingVirtualAdjustment = previous; }
	}
	reconcileVirtualView ();
}

void updateVirtualViewportGeometry () {
	if (virtualViewport == null || updatingVirtualGeometry) return;
	updatingVirtualGeometry = true;
	try {
		Rectangle client = getClientAreaInPixels ();
		int body = Math.max (0, client.height - getHeaderHeight ());
		if (virtualViewAdjustment != 0) {
			double page = GTK.gtk_adjustment_get_page_size (virtualViewAdjustment);
			if (page > 0) body = (int)Math.min (Integer.MAX_VALUE, page);
		}
		int extent = Math.max (1, getItemHeight ());
		if (isDisposed ()) return;
		if (virtualViewCount > 0 && GTK.gtk_widget_get_realized (handle)) {
			long path = virtualViewPath (virtualViewStart);
			try {
				GdkRectangle rect = new GdkRectangle ();
				GTK.gtk_tree_view_get_background_area (handle, path, 0, rect);
				if (rect.height > 0) extent = rect.height;
			} finally {
				GTK.gtk_tree_path_free (path);
			}
		}
		virtualRowExtent = extent;
		virtualPixelRemainder = Math.max (0, Math.min (virtualPixelRemainder, Math.nextDown ((double)extent)));
		virtualBodyExtent = body;
		virtualViewport.setUniformGeometry (itemCount, extent, body, virtualViewport.firstVisible ());
		topIndex = virtualViewport.firstVisible ();
	} finally {
		updatingVirtualGeometry = false;
	}
}

void scheduleVirtualView () {
	if (!usesBoundedVirtualView () || virtualViewScheduled || isDisposed ()) return;
	virtualViewScheduled = true;
	display.asyncExec (() -> {
		virtualViewScheduled = false;
		if (!isDisposed ()) reconcileVirtualView ();
	});
}

void updateVirtualLogicalAdjustment () {
	if (verticalAdjustment == 0 || virtualViewport == null) return;
	double page = Math.max (0, virtualBodyExtent);
	double upper = Math.max (page, itemCount * (double)virtualRowExtent);
	double value = virtualViewport.firstVisible () * (double)virtualRowExtent + virtualPixelRemainder;
	value = Math.max (0, Math.min (value, upper - page));
	virtualPixelRemainder = Math.max (0, value - virtualViewport.firstVisible () * (double)virtualRowExtent);
	boolean previous = updatingVirtualAdjustment;
	updatingVirtualAdjustment = true;
	OS.g_signal_handlers_block_matched (verticalAdjustment, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
	try {
		GTK.gtk_adjustment_configure (verticalAdjustment, value, 0, upper, virtualRowExtent,
				Math.max (1, virtualViewport.visibleCount () - 1) * (double)virtualRowExtent, page);
		long horizontal = GTK.gtk_scrolled_window_get_hadjustment (scrolledHandle);
		int dirty = viewportLayers.scrollTo (usesBoundedHorizontalView () ? virtualHorizontalWindow ().origin ()
				: horizontal != 0 ? GTK.gtk_adjustment_get_value (horizontal) : 0, value);
		if ((dirty & ViewportLayerState.HEADER) != 0) wasScrolled = true;
	} finally {
		OS.g_signal_handlers_unblock_matched (verticalAdjustment, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
		updatingVirtualAdjustment = previous;
	}
}

void alignVirtualViewAdjustment () {
	if (virtualViewAdjustment == 0) return;
	double value = (virtualViewport.firstVisible () - (double)virtualViewStart) * virtualRowExtent + virtualPixelRemainder;
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
		virtualHorizontalDirty = true;
		virtualAccessibleColumnsDirty = true;
		if (!isDisposed ()) scheduleVirtualView ();
		return 0;
	}
	if (isDisposed () || virtualViewport == null || updatingVirtualAdjustment || reconcilingVirtualView) return 0;
	if (kind >= 5) {
		if (kind == 5) {
			VirtualViewportPlanner.HorizontalWindow window = virtualHorizontalWindow ();
			long value = (long)Math.max (0, GTK.gtk_adjustment_get_value (horizontalAdjustment));
			long origin = window.rightToLeft () ? Math.max (0, window.contentWidth () - window.viewportWidth ()) - value : value;
			window.setViewport (Math.max (0, origin), window.viewportWidth (), VIRTUAL_HORIZONTAL_OVERSCAN, window.rightToLeft ());
		} else if (kind == 6) {
			virtualHorizontalRangeDirty = true;
		} else if (kind == 7 && !virtualHorizontalRangeDirty) {
			VirtualViewportPlanner.HorizontalWindow window = virtualHorizontalWindow ();
			double delta = GTK.gtk_adjustment_get_value (virtualHorizontalAdjustment)
					+ window.residentViewportX ();
			if (window.rightToLeft ()) delta = -delta;
			long shift = (long)delta;
			long origin = shift >= 0 ? window.origin () + Math.min (shift, Long.MAX_VALUE - window.origin ())
					: Math.max (0, window.origin () + shift);
			window.setViewport (origin, window.viewportWidth (), VIRTUAL_HORIZONTAL_OVERSCAN, window.rightToLeft ());
			boolean previous = updatingVirtualAdjustment;
			updatingVirtualAdjustment = true;
			try { GTK.gtk_adjustment_set_value (horizontalAdjustment, virtualHorizontalBarValue (window)); }
			finally { updatingVirtualAdjustment = previous; }
		}
		scheduleVirtualView ();
		return 0;
	}
	if (kind == 1) {
		double pixels = GTK.gtk_adjustment_get_value (verticalAdjustment);
		int requested = (int)Math.min (Integer.MAX_VALUE, Math.floor (pixels / virtualRowExtent));
		virtualViewport.setViewport (requested, virtualViewport.visibleCount ());
		virtualPixelRemainder = virtualViewport.firstVisible () == requested ? pixels - requested * (double)virtualRowExtent : 0;
		topIndex = virtualViewport.firstVisible ();
		pendingTopIndex = -1;
	} else if (kind == 2) {
		virtualViewRangeDirty = true;
	} else if (!virtualViewRangeDirty) {
		int physicalTop = nativeTopIndex ();
		if (physicalTop >= 0) {
			virtualPixelRemainder = GTK.gtk_adjustment_get_value (virtualViewAdjustment) % virtualRowExtent;
			GTK.gtk_adjustment_set_value (verticalAdjustment,
					(virtualViewStart + (double)physicalTop) * virtualRowExtent + virtualPixelRemainder);
		}
	}
	scheduleVirtualView ();
	return 0;
}

void restoreVirtualSelection () {
	if (!usesBoundedVirtualView () || virtualViewport == null || isDisposed ()) return;
	long selection = OS.g_object_ref (GTK.gtk_tree_view_get_selection (handle));
	boolean previousAdjustment = updatingVirtualAdjustment;
	updatingVirtualAdjustment = true;
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	try {
		long focus = virtualViewPath (virtualFocusIndex);
		if (focus != 0) {
			try { GTK.gtk_tree_view_set_cursor (handle, focus, 0, false); }
			finally { GTK.gtk_tree_path_free (focus); }
		}
		if (isDisposed ()) return;
		GTK.gtk_tree_selection_unselect_all (selection);
		for (int row = 0; row < virtualViewCount; row++) {
			int index = virtualViewStart + row;
			if (!virtualViewport.selection ().isSelected (index)) continue;
			long iter = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
			if (iter == 0) error (SWT.ERROR_NO_HANDLES);
			try {
				if (GTK.gtk_tree_model_iter_nth_child (virtualViewModel, iter, 0, row)) GTK.gtk_tree_selection_select_iter (selection, iter);
			} finally {
				OS.g_free (iter);
			}
			if (isDisposed ()) return;
		}
	} finally {
		OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
		OS.g_object_unref (selection);
		updatingVirtualAdjustment = previousAdjustment;
	}
	/* Setting GTK's cursor can scroll its private adjustment. Selection restores
	 * are projections of the logical state and must not move that viewport. */
	alignVirtualViewAdjustment ();
	notifyVirtualAccessibleSelection ();
	notifyVirtualAccessibleFocus ();
}

void reconcileVirtualView () {
	if (!usesBoundedVirtualView () || virtualViewport == null || reconcilingVirtualView || isDisposed ()) return;
	if (currentItem != null || virtualCallbackDepth != 0 || updatingVirtualGeometry) {
		scheduleVirtualView ();
		return;
	}
	if (virtualRenderersDirty) {
		virtualRenderersDirty = false;
		recreateRenderers ();
		if (isDisposed ()) return;
	}
	updateVirtualViewportGeometry ();
	if (isDisposed ()) return;
	reconcileVirtualHorizontal ();
	if (isDisposed ()) return;
	int start = virtualViewport.paintStart ();
	int count = virtualViewport.paintEndExclusive () - start;
	long generation = virtualModelGeneration;
	if (!virtualViewChanged && start == virtualViewStart && count == virtualViewCount) {
		updateVirtualLogicalAdjustment ();
		alignVirtualViewAdjustment ();
		reconcileVirtualHorizontal ();
		notifyVirtualAccessibleModel ();
		notifyVirtualAccessibleFocus ();
		notifyVirtualAccessibleStates ();
		return;
	}
	long selection = OS.g_object_ref (GTK.gtk_tree_view_get_selection (handle));
	reconcilingVirtualView = true;
	setRedraw (false);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	try {
		GTK.gtk_tree_view_set_model (handle, 0);
		if (virtualViewSnapshotChanged (generation, start, count)) return;
		OS.g_object_set (virtualViewModel, VIRTUAL_MODEL_ITEM_COUNT, count, 0);
		virtualViewStart = start;
		virtualViewCount = count;
		virtualViewChanged = false;
		GTK.gtk_tree_view_set_model (handle, virtualViewModel);
		if (virtualViewSnapshotChanged (generation, start, count)) return;
		restoreVirtualSelection ();
		if (virtualViewSnapshotChanged (generation, start, count)) return;
		long path = virtualViewPath (virtualViewport.firstVisible ());
		if (path != 0) {
			try { GTK.gtk_tree_view_scroll_to_cell (handle, path, 0, true, 0f, 0f); }
			finally { GTK.gtk_tree_path_free (path); }
		}
		if (virtualViewSnapshotChanged (generation, start, count)) return;
		updateVirtualLogicalAdjustment ();
		alignVirtualViewAdjustment ();
		reconcileVirtualHorizontal ();
	} finally {
		OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
		OS.g_object_unref (selection);
		reconcilingVirtualView = false;
		if (!isDisposed ()) {
			setRedraw (true);
			if (!isDisposed ()) {
				GTK.gtk_widget_queue_draw (handle);
				if (virtualViewChanged) scheduleVirtualView ();
			}
		}
	}
	if (!isDisposed ()) {
		notifyVirtualAccessibleModel ();
		notifyVirtualAccessibleFocus ();
		notifyVirtualAccessibleStates ();
	}
}

boolean virtualViewSnapshotChanged (long generation, int start, int count) {
	if (isDisposed ()) return true;
	if (generation == virtualModelGeneration && start == virtualViewport.paintStart ()
			&& count == virtualViewport.paintEndExclusive () - start) return false;
	virtualViewChanged = true;
	return true;
}

void revealVirtualIndex (int index, boolean top) {
	if (index < 0 || index >= itemCount) return;
	updateVirtualViewportGeometry ();
	if (isDisposed ()) return;
	int first = virtualViewport.firstVisible ();
	int rows = Math.max (1, virtualViewport.visibleCount ());
	if (top || index < first) first = index;
	else if ((long)index >= first + (long)rows) first = index - rows + 1;
	virtualViewport.setViewport (first, rows);
	virtualPixelRemainder = 0;
	topIndex = virtualViewport.firstVisible ();
	reconcileVirtualView ();
}

void captureVirtualSelection () {
	if (!usesBoundedVirtualView () || reconcilingVirtualView) return;
	int [] nativeState = new int [1];
	GTK3.gtk_get_current_event_state (nativeState);
	boolean control = (nativeState [0] & GDK.GDK_CONTROL_MASK) != 0 && (style & SWT.MULTI) != 0;
	boolean shift = (nativeState [0] & GDK.GDK_SHIFT_MASK) != 0 && (style & SWT.MULTI) != 0;
	if (!control) virtualViewport.selection ().clear ();
	int focus = nativeFocusedIndex ();
	if (focus >= 0) {
		virtualFocusIndex = focus;
		if (shift && virtualAnchorIndex >= 0) {
			virtualViewport.selection ().selectRange (Math.min (virtualAnchorIndex, focus), Math.max (virtualAnchorIndex, focus) + 1);
		} else {
			virtualAnchorIndex = focus;
		}
	}
	long selection = GTK.gtk_tree_view_get_selection (handle);
	for (int row = 0; row < virtualViewCount; row++) {
		int index = virtualViewStart + row;
		long path = virtualViewPath (index);
		try {
			boolean selected = GTK.gtk_tree_selection_path_is_selected (selection, path);
			if (!shift || selected) virtualViewport.selection ().setSelected (index, selected);
		} finally {
			GTK.gtk_tree_path_free (path);
		}
	}
}

int firstVirtualSelectionIndex () {
	VirtualSelectionModel selection = virtualViewport.selection ();
	if (selection.selectedCount () == 0) return -1;
	if (!selection.complementMode ()) return selection.rangeStart (0);
	int first = 0;
	for (int range = 0; range < selection.rangeCount () && selection.rangeStart (range) == first; range++) {
		first = selection.rangeEndExclusive (range);
	}
	return first < itemCount ? first : -1;
}

void virtualNavigate (int key, int mask) {
	if (itemCount == 0) return;
	int index = virtualFocusIndex >= 0 ? virtualFocusIndex : virtualViewport.firstVisible ();
	int page = Math.max (1, virtualViewport.visibleCount () - 1);
	int next = switch (key) {
		case GDK.GDK_Up, GDK.GDK_KP_Up -> Math.max (0, index - 1);
		case GDK.GDK_Down, GDK.GDK_KP_Down -> Math.min (itemCount - 1, index + 1);
		case GDK.GDK_Home, GDK.GDK_KP_Home -> 0;
		case GDK.GDK_End, GDK.GDK_KP_End -> itemCount - 1;
		case GDK.GDK_Page_Up, GDK.GDK_KP_Page_Up -> Math.max (0, index - page);
		case GDK.GDK_Page_Down, GDK.GDK_KP_Page_Down -> index + Math.min (itemCount - 1 - index, page);
		default -> index;
	};
	boolean control = (mask & GDK.GDK_CONTROL_MASK) != 0;
	boolean shift = (mask & GDK.GDK_SHIFT_MASK) != 0 && (style & SWT.MULTI) != 0;
	if (shift) {
		if (virtualAnchorIndex < 0) virtualAnchorIndex = index;
		if (!control) virtualViewport.selection ().clear ();
		virtualViewport.selection ().selectRange (Math.min (virtualAnchorIndex, next), Math.max (virtualAnchorIndex, next) + 1);
	} else {
		virtualAnchorIndex = next;
		if (!control) {
			virtualViewport.selection ().clear ();
			virtualViewport.selection ().setSelected (next, true);
		}
	}
	virtualFocusIndex = next;
	revealVirtualIndex (next, false);
	if (isDisposed ()) return;
	restoreVirtualSelection ();
	if (!control || shift) {
		Event event = new Event ();
		event.item = getItem (next);
		sendSelectionEvent (SWT.Selection, event, false);
	}
}

void disconnectVirtualAdjustments () {
	if (virtualHorizontalValueSignal != 0) OS.g_signal_handler_disconnect (horizontalAdjustment, virtualHorizontalValueSignal);
	if (virtualHorizontalRangeSignal != 0) OS.g_signal_handler_disconnect (virtualHorizontalAdjustment, virtualHorizontalRangeSignal);
	if (virtualPhysicalHorizontalSignal != 0) OS.g_signal_handler_disconnect (virtualHorizontalAdjustment, virtualPhysicalHorizontalSignal);
	virtualHorizontalValueSignal = virtualHorizontalRangeSignal = virtualPhysicalHorizontalSignal = 0;
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
	accessible.internal_setNativeRole (ATK.ATK_ROLE_TABLE);
	accessible.internal_setManagesDescendants (true);
	accessible.internal_setTableHeaderRows (1);
	accessible.internal_setSelectedRowHandler (this::virtualAccessibleSelectedRowAt);
	accessible.internal_setFocusHandler (this::setFocus);
	accessible.internal_setSelectionHandlers (() -> { deselectAll (); return true; }, () -> { selectAll (); return true; });
	accessible.addAccessibleControlListener (new AccessibleControlAdapter () {
		@Override public void getRole (AccessibleControlEvent event) { event.detail = ACC.ROLE_TABLE; }
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
			if (!GTK.gtk_tree_view_get_path_at_pos (handle, virtualNativeX (point.x), point.y - getHeaderHeight (), path, column, null, null)
					|| path [0] == 0) { event.childID = ACC.CHILDID_SELF; return; }
			try {
				int [] index = new int [1];
				C.memmove (index, GTK.gtk_tree_path_get_indices (path [0]), 4);
				int row = logicalIndexForNativeHit (index [0]);
				if (row >= 0) event.accessible = virtualAccessibleCell (getItem (row), virtualAccessibleColumnFromHandle (column [0]));
			} finally { GTK.gtk_tree_path_free (path [0]); }
		}
		@Override public void getFocus (AccessibleControlEvent event) {
			TableItem focus = getFocusItem ();
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
			event.isSelected = virtualViewport.selection ().isSelected (event.row);
		}
		@Override public void selectRow (AccessibleTableEvent event) {
			if (event.row < 0 || event.row >= virtualAccessibleRowCount ()) return;
			select (event.row);
			event.result = ACC.OK;
		}
		@Override public void deselectRow (AccessibleTableEvent event) {
			if (event.row < 0 || event.row >= virtualAccessibleRowCount ()) return;
			deselect (event.row);
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
		if (virtualColumnVisible (columns [column].handle)) visible++;
	}
	return columnCount == 0 ? 1 : visible;
}

int virtualAccessibleRowCount () {
	/* ATK uses signed int child/row indices. Keep its row/cell range consistent
	 * while the Java logical model and scrollbar retain the full domain. */
	int columns = virtualAccessibleColumnCount ();
	long maximum = columns == 0 ? Integer.MAX_VALUE : (Integer.MAX_VALUE - (long)columns) / columns;
	return (int)Math.min (itemCount, maximum);
}

int virtualAccessibleVisualColumn (TableColumn column) {
	if (column == null) return columnCount == 0 ? 0 : -1;
	if (column.isDisposed ()) return -1;
	int visual = 0;
	for (int index : getColumnOrder ()) {
		if (!virtualColumnVisible (columns [index].handle)) continue;
		if (columns [index] == column) return visual;
		visual++;
	}
	return -1;
}

int virtualAccessibleChildIndex (TableItem item, TableColumn column) {
	if (item.isDisposed ()) return -1;
	int visual = virtualAccessibleVisualColumn (column);
	long row = virtualItems.indexOfIdentity (item);
	if (visual < 0 || row < 0 || row >= virtualAccessibleRowCount ()) return -1;
	return Math.toIntExact ((row + 1) * virtualAccessibleColumnCount () + visual);
}

int virtualAccessibleModelColumn (int visualColumn) {
	if (columnCount == 0) return 0;
	int [] order = getColumnOrder ();
	int visible = 0;
	for (int index : order) {
		if (virtualColumnVisible (columns [index].handle)) {
			if (visible++ == visualColumn) return index;
		}
	}
	return 0;
}

int virtualAccessibleColumnFromHandle (long nativeColumn) {
	int visible = 0;
	for (int index : getColumnOrder ()) {
		if (!virtualColumnVisible (columns [index].handle)) continue;
		if (columns [index].handle == nativeColumn) return visible;
		visible++;
	}
	return 0;
}

boolean virtualAccessibleColumnShowing (TableColumn column) {
	if (!usesBoundedHorizontalView ()) return true;
	if (column != null && column.isDisposed ()) return false;
	VirtualViewportPlanner.HorizontalWindow window = virtualHorizontalWindow ();
	int index = column == null ? 0 : column.virtualIndex;
	long x = window.viewportX (index), width = window.columnWidth (index);
	return width > 0 && x < window.viewportWidth () && x + width > 0;
}

Accessible virtualAccessibleHeader (int visualColumn) {
	pruneVirtualAccessibleColumns ();
	int modelColumn = virtualAccessibleModelColumn (visualColumn);
	TableColumn column = columnCount == 0 ? null : columns [modelColumn];
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
			if (!virtualAccessibleColumnShowing (column)) event.detail |= ACC.STATE_OFFSCREEN;
		}
		@Override public void getLocation (AccessibleControlEvent event) {
			if (!getHeaderVisible () || virtualAccessibleVisualColumn (column) < 0 || !virtualAccessibleColumnShowing (column)) {
				event.x = event.y = Integer.MIN_VALUE;
				return;
			}
			GdkRectangle area = new GdkRectangle ();
			long nativeColumn = column != null ? column.handle : GTK.gtk_tree_view_get_column (handle, 0);
			GTK.gtk_tree_view_get_cell_area (handle, 0, nativeColumn, area);
			if (usesBoundedHorizontalView ()) virtualLogicalCellArea (nativeColumn, area);
			if (usesBoundedHorizontalView () && (style & SWT.MIRRORED) != 0) area.x = getClientWidth () - area.width - area.x;
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
	return row >= 0 && row < itemCount ? virtualAccessibleCell (getItem ((int)row), visualColumn) : null;
}

Accessible virtualAccessibleCell (TableItem item, int visualColumn) {
	if (item == null || item.isDisposed () || visualColumn < 0 || visualColumn >= virtualAccessibleColumnCount ()) return null;
	int modelColumn = virtualAccessibleModelColumn (visualColumn);
	TableColumn column = columnCount == 0 ? null : columns [modelColumn];
	Map<TableColumn, java.lang.ref.WeakReference<Accessible>> cells = virtualAccessibleCells.computeIfAbsent (item, ignored -> new HashMap<> ());
	java.lang.ref.WeakReference<Accessible> reference = cells.get (column);
	Accessible cell = reference != null ? reference.get () : null;
	if (cell != null) return cell;
	cell = Accessible.internal_new_AccessibleChild (accessible);
	cell.internal_setNativeRole (ATK.ATK_ROLE_TABLE_CELL);
	cell.internal_setNativeStates (() -> virtualAccessibleCheckStates (item));
	cell.internal_setFocusHandler (() -> {
		if (item.isDisposed ()) return false;
		virtualFocusIndex = virtualAnchorIndex = virtualItems.indexOfIdentity (item);
		revealVirtualIndex (virtualItems.indexOfIdentity (item), false);
		if (isDisposed () || item.isDisposed ()) return false;
		restoreVirtualSelection ();
		boolean focused = setFocus ();
		notifyVirtualAccessibleFocus ();
		return focused;
	});
	cell.internal_setScrollHandlers (type -> virtualAccessibleScrollTo (item, column, type),
			point -> virtualAccessibleScrollToPoint (item, column, point));
	cell.internal_setNodeParent (() -> item.isDisposed () ? null : accessible);
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
			int id = virtualItems.indexOfIdentity (item);
			if (virtualViewport.selection ().isSelected (id)) event.detail |= ACC.STATE_SELECTED;
			if (id == virtualFocusIndex && isFocusControl ()) event.detail |= ACC.STATE_FOCUSED;
			if ((style & SWT.CHECK) != 0 && item.isCheckedState ()) event.detail |= ACC.STATE_CHECKED;
			long row = id;
			if (row < 0) event.detail |= ACC.STATE_INVISIBLE | ACC.STATE_OFFSCREEN;
			else if (!virtualAccessibleRowShowing (row) || !virtualAccessibleColumnShowing (column)) event.detail |= ACC.STATE_OFFSCREEN;
			if (!getEnabled ()) event.detail |= ACC.STATE_DISABLED;
			if (virtualAccessibleVisualColumn (column) < 0) event.detail |= ACC.STATE_INVISIBLE | ACC.STATE_OFFSCREEN;
		}
		@Override public void getLocation (AccessibleControlEvent event) {
			if (item.isDisposed ()) return;
			Rectangle bounds = item.getBounds (virtualAccessibleItemColumn (column));
			long row = virtualItems.indexOfIdentity (item);
			event.width = bounds.width;
			event.height = bounds.height;
			if (!virtualAccessibleRowShowing (row) || !virtualAccessibleColumnShowing (column)) {
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
			long row = item.isDisposed () ? -1 : virtualItems.indexOfIdentity (item);
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
			int id = virtualItems.indexOfIdentity (item);
			event.isSelected = virtualViewport.selection ().isSelected (id);
		}
	});
	cell.addAccessibleActionListener (new AccessibleActionAdapter () {
		@Override public void getActionCount (AccessibleActionEvent event) {
			event.count = item.isDisposed () ? 0 : 1 + ((style & SWT.CHECK) != 0 ? 1 : 0);
		}
		@Override public void getName (AccessibleActionEvent event) {
			if (event.index == 0) event.result = "activate";
			else if (event.index == 1 && (style & SWT.CHECK) != 0) event.result = "toggle";
		}
		@Override public void doAction (AccessibleActionEvent event) {
			if (item.isDisposed () || !getEnabled () || !checkData (item)) return;
			if (event.index == 0) {
				Event selection = new Event ();
				selection.item = getItem (virtualItems.indexOfIdentity (item));
				sendSelectionEvent (SWT.DefaultSelection, selection, false);
			} else if (event.index == 1 && (style & SWT.CHECK) != 0) {
				item.setChecked (!item.getChecked ());
				Event selection = new Event ();
				selection.item = getItem (virtualItems.indexOfIdentity (item));
				selection.detail = SWT.CHECK;
				sendSelectionEvent (SWT.Selection, selection, false);

			} else return;
			event.result = ACC.OK;
		}
	});
	virtualAccessibleStates.put (cell, virtualAccessibleNativeStates (item, column));
	cells.put (column, new java.lang.ref.WeakReference<> (cell));
	return cell;
}

int virtualAccessibleItemColumn (TableColumn column) {
	if (column == null || column.isDisposed ()) return 0;
	for (int index = 0; index < columnCount; index++) if (columns [index] == column) return index;
	return 0;
}

int [] virtualAccessibleSelectedRows () {
	int limit = virtualAccessibleRowCount ();
	return Arrays.stream (virtualViewport.selection ().toArray ()).filter (row -> row < limit).toArray ();
}

int virtualAccessibleSelectionCount () {
	return virtualAccessibleSelectionCount (virtualAccessibleRowCount ());
}

int virtualAccessibleSelectionCount (long maximum) {
	VirtualSelectionModel selection = virtualViewport.selection ();
	int limit = (int)Math.min (itemCount, maximum);
	long spans = 0;
	for (int range = 0; range < selection.rangeCount (); range++) {
		spans += Math.max (0, Math.min (limit, selection.rangeEndExclusive (range)) - selection.rangeStart (range));
	}
	return (int)(selection.complementMode () ? limit - spans : spans);
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

boolean virtualAccessibleScrollTo (TableItem item, TableColumn column, int type) {
	if (item.isDisposed () || type < ATK.ATK_SCROLL_TOP_LEFT || type > ATK.ATK_SCROLL_ANYWHERE) return false;
	revealVirtualIndex (virtualItems.indexOfIdentity (item), false);
	if (isDisposed () || item.isDisposed ()) return false;
	long row = virtualItems.indexOfIdentity (item);
	if (row < 0) return false;
	if (type == ATK.ATK_SCROLL_TOP_LEFT || type == ATK.ATK_SCROLL_TOP_EDGE) {
		GTK.gtk_adjustment_set_value (verticalAdjustment, row * (double)virtualRowExtent);
	} else if (type == ATK.ATK_SCROLL_BOTTOM_RIGHT || type == ATK.ATK_SCROLL_BOTTOM_EDGE) {
		GTK.gtk_adjustment_set_value (verticalAdjustment, (row + 1) * (double)virtualRowExtent - virtualBodyExtent);
	}
	if (usesBoundedHorizontalView ()) {
		int index = virtualAccessibleItemColumn (column);
		VirtualViewportPlanner.HorizontalWindow window = virtualHorizontalWindow ();
		long leading = window.columnStart (index), trailing = leading + window.columnWidth (index) - window.viewportWidth ();
		if (type == ATK.ATK_SCROLL_LEFT_EDGE || type == ATK.ATK_SCROLL_TOP_LEFT) {
			virtualSetHorizontalOrigin (window.rightToLeft () ? trailing : leading);
		} else if (type == ATK.ATK_SCROLL_RIGHT_EDGE || type == ATK.ATK_SCROLL_BOTTOM_RIGHT) {
			virtualSetHorizontalOrigin (window.rightToLeft () ? leading : trailing);
		} else virtualShowColumn (index);
		return true;
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

boolean virtualAccessibleScrollToPoint (TableItem item, TableColumn column, AccessibleControlEvent point) {
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
	revealVirtualIndex (virtualItems.indexOfIdentity (item), false);
	if (isDisposed () || item.isDisposed ()) return false;
	long row = virtualItems.indexOfIdentity (item);
	if (row < 0) return false;
	GTK.gtk_adjustment_set_value (verticalAdjustment, row * (double)virtualRowExtent - (local.y - (double)getHeaderHeight ()));
	if (usesBoundedHorizontalView ()) {
		int index = virtualAccessibleItemColumn (column);
		VirtualViewportPlanner.HorizontalWindow window = virtualHorizontalWindow ();
		long origin = window.rightToLeft ()
				? window.columnStart (index) + window.columnWidth (index) - window.viewportWidth () + local.x
				: window.columnStart (index) - local.x;
		virtualSetHorizontalOrigin (origin);
		return true;
	}
	Rectangle bounds = item.getBounds (virtualAccessibleItemColumn (column));
	long horizontal = GTK.gtk_scrolled_window_get_hadjustment (scrolledHandle);
	GTK.gtk_adjustment_set_value (horizontal, GTK.gtk_adjustment_get_value (horizontal) + bounds.x - (double)local.x);
	return true;
}

void pruneVirtualAccessibleColumns () {
	if (virtualAccessibleHeaders == null) return;
	virtualAccessibleHeaders.entrySet ().removeIf (entry -> {
		TableColumn column = entry.getKey ();
		boolean obsolete = column == null ? columnCount != 0 : column.isDisposed () || column.parent != this;
		if (obsolete) entry.getValue ().dispose ();
		return obsolete;
	});
	for (var cells : virtualAccessibleCells.values ()) cells.entrySet ().removeIf (entry -> {
		TableColumn column = entry.getKey ();
		boolean obsolete = column == null ? columnCount != 0 : column.isDisposed () || column.parent != this;
		Accessible cell = entry.getValue ().get ();
		if (obsolete && cell != null) cell.dispose ();
		return obsolete || cell == null;
	});
}

void releaseVirtualAccessibleItem (TableItem item) {
	if (virtualAccessibleCells == null) return;
	Map<TableColumn, java.lang.ref.WeakReference<Accessible>> cells = virtualAccessibleCells.remove (item);
	if (cells == null) return;
	for (var reference : cells.values ()) {
		Accessible cell = reference.get ();
		if (cell != null) cell.dispose ();
	}
}

void notifyVirtualAccessibleFocus () {
	if (!usesBoundedVirtualView () || accessible == null) return;
	TableItem item = getFocusItem ();
	int id = item != null && isFocusControl () ? virtualItems.indexOfIdentity (item) : -1;
	if (virtualAccessibleFocusId == id) return;
	virtualAccessibleFocusId = id;
	accessible.internal_setActiveDescendant (id >= 0 ? virtualAccessibleCell (item, 0) : null);
}

long virtualAccessibleCheckStates (TableItem item) {
	if (item.isDisposed () || (style & SWT.CHECK) == 0) return 0;
	long states = 1L << ATK.ATK_STATE_CHECKABLE;
	if (item.isCheckedState ()) {
		states |= 1L << ATK.ATK_STATE_CHECKED;
		if (item.isGrayedState ()) states |= 1L << ATK.ATK_STATE_INDETERMINATE;
	}
	return states;
}

long virtualAccessibleNativeStates (TableItem item, TableColumn column) {
	if (item.isDisposed ()) return 1L << ATK.ATK_STATE_DEFUNCT;
	int id = virtualItems.indexOfIdentity (item);
	long states = (1L << ATK.ATK_STATE_FOCUSABLE) | (1L << ATK.ATK_STATE_SELECTABLE) | virtualAccessibleCheckStates (item);
	if (virtualViewport.selection ().isSelected (id)) states |= 1L << ATK.ATK_STATE_SELECTED;
	if (id == virtualFocusIndex && isFocusControl ()) states |= 1L << ATK.ATK_STATE_FOCUSED;

	long row = id;
	if (getVisible () && row >= 0 && virtualAccessibleVisualColumn (column) >= 0) {
		states |= 1L << ATK.ATK_STATE_VISIBLE;
		if (virtualAccessibleRowShowing (row) && virtualAccessibleColumnShowing (column)) states |= 1L << ATK.ATK_STATE_SHOWING;
	}
	if (getEnabled ()) states |= (1L << ATK.ATK_STATE_ENABLED) | (1L << ATK.ATK_STATE_SENSITIVE);
	return states;
}

boolean virtualAccessibleRowShowing (long row) {
	if (row < 0 || row >= itemCount || virtualBodyExtent <= 0) return false;
	double top = (row - (double)virtualViewport.firstVisible ()) * virtualRowExtent - virtualPixelRemainder;
	return top < virtualBodyExtent && top + virtualRowExtent > 0;
}

void notifyVirtualAccessibleItemStates (TableItem item) {
	if (virtualAccessibleCells == null || isDisposed () || item.isDisposed ()) return;
	Map<TableColumn, java.lang.ref.WeakReference<Accessible>> cells = virtualAccessibleCells.get (item);
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
	for (TableItem item : virtualAccessibleCells.keySet ()) notifyVirtualAccessibleItemStates (item);
}

void notifyVirtualAccessibleSelection () {
	if (!usesBoundedVirtualView () || accessible == null) return;
	accessible.selectionChanged ();
	notifyVirtualAccessibleStates ();
}

void notifyVirtualAccessibleModel () {
	if (!usesBoundedVirtualView () || accessible == null) return;
	long generation = virtualModelGeneration;
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

private int focusedIndex () {
	if (usesBoundedVirtualView ()) return virtualFocusIndex;
	return nativeFocusedIndex ();
}

private int nativeFocusedIndex () {
	long [] path = new long [1];
	GTK.gtk_tree_view_get_cursor (handle, path, null);
    if (path [0] == 0) {
        return -1;
    }
	int index = -1;
	long indices = GTK.gtk_tree_path_get_indices (path [0]);
	if (indices != 0) {
		int [] value = new int [1];
		C.memmove (value, indices, 4);
		index = value [0] + (usesBoundedVirtualView () ? virtualViewStart : 0);
	}
	GTK.gtk_tree_path_free (path [0]);
	return index;
}

private static int remapIndex (int index, int start, int removed, int inserted) {
    if (index < 0 || index < start) {
        return index;
    }
    if (index < start + removed) {
        return -1;
    }
	return index - removed + inserted;
}

private void rebindVirtualItemHandles () {
    if (!usesVirtualNativeModel()) {
        return;
    }
	virtualItems.forEachIndexed ((index, item) -> {
		if (item.handle == 0) {
			item.handle = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
            if (item.handle == 0) {
                error(SWT.ERROR_NO_HANDLES);
            }
		}
		if (!GTK.gtk_tree_model_iter_nth_child (modelHandle, item.handle, 0, index)) {
			throw new IllegalStateException ("virtual TableItem outside logical model: " + index);
		}
	});
}

private void restoreVirtualNativeState (int [] selected, int focus, int top) {
	long selection = GTK.gtk_tree_view_get_selection (handle);
	long iter = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
    if (iter == 0) {
        error(SWT.ERROR_NO_HANDLES);
    }
	try {
		if (focus >= 0 && focus < itemCount
				&& GTK.gtk_tree_model_iter_nth_child (modelHandle, iter, 0, focus)) {
			long path = GTK.gtk_tree_model_get_path (modelHandle, iter);
			if (path != 0) {
				GTK.gtk_tree_view_set_cursor (handle, path, 0, false);
				GTK.gtk_tree_path_free (path);
			}
		}
		GTK.gtk_tree_selection_unselect_all (selection);
		for (int index : selected) {
			if (index >= 0 && index < itemCount
					&& GTK.gtk_tree_model_iter_nth_child (modelHandle, iter, 0, index)) {
				GTK.gtk_tree_selection_select_iter (selection, iter);
			}
		}
		if (top >= 0 && top < itemCount
				&& GTK.gtk_tree_model_iter_nth_child (modelHandle, iter, 0, top)) {
			long path = GTK.gtk_tree_model_get_path (modelHandle, iter);
			if (path != 0) {
				GTK.gtk_tree_view_scroll_to_cell (handle, path, 0, true, 0f, 0f);
				GTK.gtk_tree_path_free (path);
			}
			topIndex = top;
			pendingTopIndex = top;
			long adjustment = GTK.gtk_scrollable_get_vadjustment (handle);
			cachedAdjustment = GTK.gtk_adjustment_get_value (adjustment);
		}
	} finally {
		OS.g_free (iter);
	}
}

void resetVirtualNativeModel (int newCount, int start, int removed, int inserted) {
	if (!usesVirtualNativeModel ()) {
		itemCount = newCount;
		return;
	}
	if (usesBoundedVirtualView ()) {
		if (removed > 0) virtualViewport.remove (start, removed);
		if (inserted > 0) virtualViewport.insert (start, inserted);
		virtualFocusIndex = remapIndex (virtualFocusIndex, start, removed, inserted);
		virtualAnchorIndex = remapIndex (virtualAnchorIndex, start, removed, inserted);
		if (virtualFocusIndex < 0 && newCount > 0 && removed > 0) virtualFocusIndex = Math.min (start, newCount - 1);
		itemCount = newCount;
		virtualViewport.setLogicalCount (newCount);
		OS.g_object_set (modelHandle, VIRTUAL_MODEL_ITEM_COUNT, newCount, 0);
		rebindVirtualItemHandles ();
		virtualViewChanged = true;
		virtualModelGeneration++;
		reconcileVirtualView ();
		return;
	}
	int [] selected = getSelectionIndices ();
	for (int i = 0; i < selected.length; i++) {
		selected [i] = remapIndex (selected [i], start, removed, inserted);
	}
	int focus = remapIndex (focusedIndex (), start, removed, inserted);
	int top = remapIndex (itemCount == 0 ? -1 : getTopIndex (), start, removed, inserted);
    if (focus < 0 && newCount > 0 && removed > 0) {
        focus = Math.min(start, newCount - 1);
    }
    if (top < 0 && newCount > 0) {
        top = Math.min(start, newCount - 1);
    }

	long selection = GTK.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	try {
		GTK.gtk_tree_view_set_model (handle, 0);
		OS.g_object_set (modelHandle, VIRTUAL_MODEL_ITEM_COUNT, newCount, 0);
		itemCount = newCount;
		rebindVirtualItemHandles ();
		GTK.gtk_tree_view_set_model (handle, modelHandle);
		restoreVirtualNativeState (selected, focus, top);
	} finally {
		OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	}
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
		if (usesBoundedVirtualView () && virtualViewport != null && (virtualViewChanged
				|| virtualViewport.paintStart () != virtualViewStart
				|| virtualViewport.paintEndExclusive () - virtualViewStart != virtualViewCount)) scheduleVirtualView ();
	}
}

long virtualCellDataProc (long tree_column, long cell, long tree_model, long iter, long data) {
    if (cell == ignoreCell) {
        return 0;
    }
	OS.g_object_set_qdata (cell, Display.SWT_OBJECT_INDEX2, 0);
	long path = GTK.gtk_tree_model_get_path (tree_model, iter);
    if (path == 0) {
        return 0;
    }
	int [] index = new int [1];
	long pathIndices = GTK.gtk_tree_path_get_indices (path);
	if (pathIndices == 0) {
		GTK.gtk_tree_path_free (path);
		return 0;
	}
	C.memmove (index, pathIndices, 4);
	if (usesBoundedVirtualView () && tree_model == virtualViewModel) index [0] += virtualViewStart;
	GTK.gtk_tree_path_free (path);
	if (index [0] < 0 || index [0] >= itemCount) return 0;
	TableItem item = _getItem (index[0]);
    if (item == null || item.isDisposed()) {
        return 0;
    }
	boolean isPixbuf = GTK.GTK_IS_CELL_RENDERER_PIXBUF (cell);
	boolean isText = GTK.GTK_IS_CELL_RENDERER_TEXT (cell);
	boolean isToggle = GTK.GTK_IS_CELL_RENDERER_TOGGLE (cell);
    if (isText) {
        GTK.gtk_cell_renderer_set_fixed_size(cell, -1, -1);
    }

	int columnIndex = 0;
	int modelIndex = -1;
	boolean customDraw = false;
	if (columnCount == 0) {
		modelIndex = Table.FIRST_COLUMN;
		customDraw = firstCustomDraw;
	} else {
		for (int i = 0; i < columnCount; i++) {
			if (columns [i] != null && columns [i].handle == tree_column) {
				columnIndex = i;
				modelIndex = columns [i].modelIndex;
				customDraw = columns [i].customDraw;
				break;
			}
		}
	}
    if (modelIndex == -1) {
        return 0;
    }

	boolean setData = false;
	if ((style & SWT.VIRTUAL) != 0 && !item.isCachedState ()) {
		lastIndexOf = index[0];
		setData = checkData (item);
        if (!setData || isDisposed() || item.isDisposed()) {
            return 0;
        }
	}
	OS.g_object_set_qdata (cell, Display.SWT_OBJECT_INDEX2, item.handle);
    if ((style & SWT.VIRTUAL) != 0) {
        item.markVirtualPainted();
    }

	if ((style & SWT.VIRTUAL) != 0 && isToggle) {
		OS.g_object_set (cell, OS.active, item.isCheckedState (), 0);
		OS.g_object_set (cell, OS.inconsistent,
				item.isCheckedState () && item.isGrayedState (), 0);
		return 0;
	}

	if (usesVirtualNativeModel ()) {
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
                if (imageList == null) {
                    imageList = new ImageList();
                }
				int imageIndex = imageList.indexOf (image);
                if (imageIndex == -1) {
                    imageIndex = imageList.add(image);
                }
				pixbuf = ImageList.createPixbuf (imageList.getSurface (imageIndex));
			}
			OS.g_object_set (cell, OS.pixbuf, pixbuf, 0);
            if (pixbuf != 0) {
                OS.g_object_unref(pixbuf);
            }
			return 0;
		}
		if (isText) {
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
		}
		if (setData) {
			ignoreCell = cell;
			setScrollWidth (tree_column, item);
			ignoreCell = 0;
		}
		return 0;
	}

    if (!(isPixbuf || isText)) {
        return 0;
    }
	long [] ptr = new long [1];
	if (setData) {
		ptr [0] = 0;
		if (isPixbuf) {
			GTK.gtk_tree_model_get (tree_model, iter, modelIndex + CELL_PIXBUF, ptr, -1);
			OS.g_object_set (cell, OS.gicon, ptr [0], 0);
            if (ptr [0] != 0) {
                OS.g_object_unref(ptr [0]);
            }
		} else {
			GTK.gtk_tree_model_get (tree_model, iter, modelIndex + CELL_TEXT, ptr, -1);
			if (ptr [0] != 0) {
				OS.g_object_set (cell, OS.text, ptr [0], 0);
				OS.g_free (ptr [0]);
			}
		}
	}
	if (customDraw) {
		if (!ownerDraw) {
			ptr [0] = 0;
			GTK.gtk_tree_model_get (tree_model, iter, modelIndex + CELL_BACKGROUND, ptr, -1);
			if (ptr [0] != 0) {
				OS.g_object_set (cell, OS.cell_background_rgba, ptr[0], 0);
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
	if (setData) {
		ignoreCell = cell;
		setScrollWidth (tree_column, item);
		ignoreCell = 0;
	}
	return 0;
}

void clearVirtualRendererItem (long iter) {
	if (!usesVirtualNativeModel () || handle == 0 || iter == 0) return;
	for (int columnIndex = 0; columnIndex < Math.max (1, columnCount); columnIndex++) {
		long column = GTK.gtk_tree_view_get_column (handle, columnIndex);
		long cells = GTK.gtk_cell_layout_get_cells (column);
		try {
			for (long next = cells; next != 0; next = OS.g_list_next (next)) {
				long cell = OS.g_list_data (next);
				if (OS.g_object_get_qdata (cell, Display.SWT_OBJECT_INDEX2) == iter) {
					OS.g_object_set_qdata (cell, Display.SWT_OBJECT_INDEX2, 0);
				}
			}
		} finally {
			if (cells != 0) OS.g_list_free (cells);
		}
	}
}

boolean checkData (TableItem item) {
    if (item.isCachedState()) {
        return true;
    }
	if ((style & SWT.VIRTUAL) != 0) {
		item.pinVirtualFacade ();
		item.setCachedState (true);
		Event event = new Event ();
		event.item = item;
		event.index = indexOf (item);
		int mask = OS.G_SIGNAL_MATCH_DATA | OS.G_SIGNAL_MATCH_ID;
		int signal_id = OS.g_signal_lookup (OS.row_changed, GTK.gtk_tree_model_get_type ());
		long signalModel = OS.g_object_ref (modelHandle);
		long signalData = handle;
		OS.g_signal_handlers_block_matched (signalModel, mask, signal_id, 0, 0, 0, signalData);
		TableItem previousItem = currentItem;
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
			if (usesBoundedVirtualView () && virtualViewChanged) scheduleVirtualView ();
		}
		// The provider may have disposed the widget.
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

int calculateWidth (long column, long iter) {
	GTK.gtk_tree_view_column_cell_set_cell_data(column, modelHandle, iter, false, false);

	/*
	* Bug in GTK.  The width calculated by gtk_tree_view_column_cell_get_size()
	* always grows in size regardless of the text or images in the table.
	* The fix is to determine the column width from the cell renderers.
	*/

	//This workaround is causing the problem Bug 459834 in GTK3. So reverting the workaround for GTK3
	int[] width = new int[1];
	if (GTK.GTK4) {
		GTK4.gtk_tree_view_column_cell_get_size(column, null, null, width, null);
	} else {
		GTK3.gtk_tree_view_column_cell_get_size(column, null, null, null, width, null);
	}

	long textRenderer = getTextRenderer(column);
	int[] xpad = new int[1];
    if (textRenderer != 0) {
        GTK.gtk_cell_renderer_get_padding(textRenderer, xpad, null);
    }

	return width[0] + xpad[0] * 2;
}

/**
 * Clears the item at the given zero-relative index in the receiver.
 * The text, icon and other attributes of the item are set to the default
 * value.  If the table was created with the <code>SWT.VIRTUAL</code> style,
 * these attributes are requested again as needed.
 *
 * @param index the index of the item to clear
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
 * @since 3.0
 */
public void clear (int index) {
	checkWidget ();
    if (!(0 <= index && index < itemCount)) {
        error(SWT.ERROR_INVALID_RANGE);
    }
	TableItem item = _getItem (index, false);
    if (item != null) {
        item.clear();
    }
}
/**
 * Removes the items from the receiver which are between the given
 * zero-relative start and end indices (inclusive).  The text, icon
 * and other attributes of the items are set to their default values.
 * If the table was created with the <code>SWT.VIRTUAL</code> style,
 * these attributes are requested again as needed.
 *
 * @param start the start index of the item to clear
 * @param end the end index of the item to clear
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if either the start or end are not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SWT#VIRTUAL
 * @see SWT#SetData
 *
 * @since 3.0
 */
public void clear (int start, int end) {
	checkWidget ();
    if (start > end) {
        return;
    }
    if (!(0 <= start && start <= end && end < itemCount)) {
        error(SWT.ERROR_INVALID_RANGE);
    }
	if (start == 0 && end == itemCount - 1) {
		clearAll();
	} else if ((style & SWT.VIRTUAL) != 0) {
		virtualItems.forEachIndexed ((index, item) -> {
            if (start <= index && index <= end) {
                item.clear();
            }
		});
	} else {
		for (int i=start; i<=end; i++) {
			TableItem item = items [i];
            if (item != null) {
                item.clear();
            }
		}
	}
}
/**
 * Clears the items at the given zero-relative indices in the receiver.
 * The text, icon and other attributes of the items are set to their default
 * values.  If the table was created with the <code>SWT.VIRTUAL</code> style,
 * these attributes are requested again as needed.
 *
 * @param indices the array of indices of the items
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 *    <li>ERROR_NULL_ARGUMENT - if the indices array is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SWT#VIRTUAL
 * @see SWT#SetData
 *
 * @since 3.0
 */
public void clear (int [] indices) {
	checkWidget ();
    if (indices == null) {
        error(SWT.ERROR_NULL_ARGUMENT);
    }
    if (indices.length == 0) {
        return;
    }
	for (int i=0; i<indices.length; i++) {
        if (!(0 <= indices [i] && indices [i] < itemCount)) {
            error(SWT.ERROR_INVALID_RANGE);
        }
	}
	for (int i=0; i<indices.length; i++) {
		TableItem item = _getItem (indices [i], false);
        if (item != null) {
            item.clear();
        }
	}
}
/**
 * Clears all the items in the receiver. The text, icon and other
 * attributes of the items are set to their default values. If the
 * table was created with the <code>SWT.VIRTUAL</code> style, these
 * attributes are requested again as needed.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SWT#VIRTUAL
 * @see SWT#SetData
 *
 * @since 3.0
 */
public void clearAll () {
	checkWidget ();
	for (int i=0; i<materializedItemCount (); i++) {
		TableItem item = materializedItem (i);
        if (item != null) {
            item.clear();
        }
	}
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
	 * Set all the TableColumn buttons visible otherwise
	 * gtk_widget_get_preferred_size() will not take their size
	 * into account.
	 */
	if (firstCompute) {
		for (int x = 0; x < columns.length; x++) {
			TableColumn column = columns[x];
            if (column != null) {
                GTK.gtk_widget_set_visible(column.buttonHandle, true);
            }
		}
		firstCompute = false;
	}
	Point size = computeNativeSize (handle, wHint, hHint, changed);
	if (usesBoundedHorizontalView () && wHint == SWT.DEFAULT) size.x = (int)Math.min (Integer.MAX_VALUE, virtualHorizontalWindow ().contentWidth ());
	/*
	 * Preferred content size describes the logical table even when GTK sees
	 * only its paint window. GTK 3 can also return just the header height.
	 */
	if (hHint == SWT.DEFAULT && (usesBoundedVirtualView () || size.y == getHeaderHeight ()) || size.y == 0) {
		long logicalHeight = (long) getItemCount () * getItemHeight () + getHeaderHeight ();
		size.y = (int) Math.min (Integer.MAX_VALUE, logicalHeight);
	}
	/*
	 * Bug 465056: single column Tables have a very small initial width.
	 * Fix: set the width of the Table to the width of the widest
	 * TableItem.
	 */
	if (wHint == SWT.DEFAULT && size.x == 0 && columnCount == 0) {
		size.x = maxWidth;
	}
	Rectangle trim = computeTrimInPixels (0, 0, size.x, size.y);
	size.x = trim.width;
	/*
	 * Feature in GTK: sometimes GtkScrolledWindow's with no scrollbars
	 * won't automatically adjust their size. This happens when a Table
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

void copyModel(long oldModel, int oldStart, long newModel, int newStart, int modelLength) {
	long value = OS.g_malloc (OS.GValue_sizeof ());
	// GValue needs to be initialized with G_VALUE_INIT, which is zeroes
	C.memset (value, 0, OS.GValue_sizeof ());

	for (int i=0; i<itemCount; i++) {
		long newIterator = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
        if (newIterator == 0) {
            error(SWT.ERROR_NO_HANDLES);
        }
		GTK.gtk_list_store_append (newModel, newIterator);

		TableItem item = _getItem (i, false);
		if (item == null) {
			/*
			 * In `SWT.VIRTUAL` mode only materialized rows are retained, and
			 * iterators are not remembered for untouched rows. SWT will use
			 * `gtk_tree_model_iter_nth_child()` when a row is requested.
			 */
			OS.g_free (newIterator);
			continue;
		}

		long oldIterator = item.handle;

		// Copy header fields
		for (int iColumn = 0; iColumn < FIRST_COLUMN; iColumn++) {
			GTK.gtk_tree_model_get_value (oldModel, oldIterator, iColumn, value);
			GTK.gtk_list_store_set_value (newModel, newIterator, iColumn, value);
			OS.g_value_unset (value);
		}

		// Copy requested columns
		for (int iOffset = 0; iOffset < modelLength - FIRST_COLUMN; iOffset++) {
			GTK.gtk_tree_model_get_value (oldModel, oldIterator, oldStart + iOffset, value);
			GTK.gtk_list_store_set_value (newModel, newIterator, newStart + iOffset, value);
			OS.g_value_unset (value);
		}

		GTK.gtk_list_store_remove (oldModel, oldIterator);
		OS.g_free (oldIterator);
		item.handle = newIterator;
	}

	OS.g_free (value);
}

void createColumn (TableColumn column, int index) {
	int modelIndex = FIRST_COLUMN;
	if (usesVirtualNativeModel ()) {
		modelIndex = FIRST_COLUMN + columnCount * CELL_TYPES;
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
            if (!usedColumns [modelIndex]) {
                break;
            }
			modelIndex++;
		}
		if (modelIndex == modelLength) {
			long oldModel = modelHandle;
			long [] types = getColumnTypes (columnCount + 4);
			long newModel = GTK.gtk_list_store_newv (types.length, types);
            if (newModel == 0) {
                error(SWT.ERROR_NO_HANDLES);
            }
			GTK.gtk_tree_view_set_model (handle, 0);
			copyModel (oldModel, FIRST_COLUMN, newModel, FIRST_COLUMN, modelLength);
			GTK.gtk_tree_view_set_model (handle, newModel);
			setModel (newModel);
		}
	}
	long columnHandle = boundedHorizontalViewEnabled () ? OS.swt_tree_view_column_new_for_viewport () : GTK.gtk_tree_view_column_new ();
    if (columnHandle == 0) {
        error(SWT.ERROR_NO_HANDLES);
    }
	if (index == 0 && columnCount > 0) {
		TableColumn checkColumn = columns [0];
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
	if (boundedHorizontalViewEnabled ()) {
		GTK.gtk_tree_view_column_set_fixed_width (columnHandle, 1);
		GTK.gtk_tree_view_column_set_visible (columnHandle, false);
	}
	GTK.gtk_tree_view_insert_column (handle, columnHandle, index);
    if (columnCount != 0) {
        GTK.gtk_tree_view_column_set_visible(columnHandle, false);
    }
	if (column != null) {
		column.handle = columnHandle;
		column.modelIndex = modelIndex;
	}
	if (!searchEnabled ()) {
		GTK.gtk_tree_view_set_search_column (handle, -1);
	} else {
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
		long modelType = OS.content_providers_create_gtype ("SwtVirtualTableModel");
		modelHandle = modelType != 0 ? OS.g_object_new (modelType, 0) : 0;
	} else {
		long [] types = getColumnTypes (1);
		modelHandle = GTK.gtk_list_store_newv (types.length, types);
	}
    if (modelHandle == 0) {
        error(SWT.ERROR_NO_HANDLES);
    }
	if (boundedVirtualViewEnabled ()) {
		long modelType = OS.content_providers_create_gtype ("SwtVirtualTableModel");
		virtualViewModel = modelType != 0 ? OS.g_object_new (modelType, 0) : 0;
		if (virtualViewModel == 0) error (SWT.ERROR_NO_HANDLES);
		if (boundedHorizontalViewEnabled ()) ownerDraw = true;
	}
	handle = GTK.gtk_tree_view_new_with_model (viewModel ());
    if (handle == 0) {
        error(SWT.ERROR_NO_HANDLES);
    }
	if ((style & SWT.CHECK) != 0) {
		long toggleType = ownerDraw ? display.gtk_cell_renderer_toggle_get_type () : 0;
		checkRenderer = toggleType != 0 ? OS.g_object_new (toggleType, 0) : GTK.gtk_cell_renderer_toggle_new ();
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
		GTK3.gtk_container_add(fixedHandle, scrolledHandle);
		GTK3.gtk_container_add(scrolledHandle, handle);
	}

	if (usesBoundedVirtualView ()) {
		verticalAdjustment = GTK.gtk_scrolled_window_get_vadjustment (scrolledHandle);
		virtualViewAdjustment = GTK.gtk_adjustment_new (0, 0, 0, 0, 0, 0);
		if (virtualViewAdjustment == 0) error (SWT.ERROR_NO_HANDLES);
		OS.g_object_ref_sink (virtualViewAdjustment);
		OS.g_object_set (handle, Converter.wcsToMbcs ("vadjustment", true), virtualViewAdjustment, 0);
		if (boundedHorizontalViewEnabled ()) {
			horizontalAdjustment = GTK.gtk_scrolled_window_get_hadjustment (scrolledHandle);
		virtualHorizontalAdjustment = GTK.gtk_adjustment_new (0, 0, 0, 0, 0, 0);
		if (virtualHorizontalAdjustment == 0) error (SWT.ERROR_NO_HANDLES);
		OS.g_object_ref_sink (virtualHorizontalAdjustment);
		OS.g_object_set (handle, Converter.wcsToMbcs ("hadjustment", true), virtualHorizontalAdjustment, 0);
		}
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
		// Required before TableDropTargetEffect may use
		// gtk_tree_view_set_drag_dest_row(), see Tree.createHandle() for details.
		long formats = GTK4.gdk_content_formats_builder_free_to_formats(GTK4.gdk_content_formats_builder_new());
		GTK4.gtk_tree_view_enable_model_drag_dest(handle, formats, 0);
		GTK4.gdk_content_formats_unref(formats);
	}
}

void createItem (TableColumn column, int index) {
    if (!(0 <= index && index <= columnCount)) {
        error(SWT.ERROR_INVALID_RANGE);
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

	long boxHandle = gtk_box_new(GTK.GTK_ORIENTATION_HORIZONTAL, false, 3);
    if (boxHandle == 0) {
        error(SWT.ERROR_NO_HANDLES);
    }
	GTK.gtk_tree_view_column_set_widget (column.handle, boxHandle);

	long labelHandle = GTK.gtk_label_new_with_mnemonic(null);
    if (labelHandle == 0) {
        error(SWT.ERROR_NO_HANDLES);
    }
	long imageHandle = GTK.gtk_image_new();
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
	column.buttonHandle = GTK.gtk_tree_view_column_get_button(column.handle);
	GTK.gtk_widget_set_focus_on_click(column.buttonHandle, false);

	if (columnCount == columns.length) {
		TableColumn [] newColumns = new TableColumn [columns.length + 4];
		System.arraycopy (columns, 0, newColumns, 0, columns.length);
		columns = newColumns;
	}
	System.arraycopy (columns, index, columns, index + 1, columnCount++ - index);
	columns [index] = column;
	virtualColumnsChanged ();
	if ((state & FONT) != 0) {
		long fontDesc = getFontDescription ();
		column.setFontDescription (fontDesc);
		OS.pango_font_description_free (fontDesc);
	}
	if (columnCount >= 1) {
		for (int i=0; i<materializedItemCount (); i++) {
			TableItem item = materializedItem (i);
            if (item == null) {
                continue;
            }
			if (usesVirtualNativeModel ()) {
                if (columnCount > 1) {
                    item.insertVirtualColumn(index, columnCount);
                }
				continue;
			}
			// Bug 545139: For consistency, do not wipe out content of first TableColumn created after TableItem
			boolean doNotModify;
			Font [] cellFont = item.cellFont;
			doNotModify = columnCount == 1 && cellFont != null && cellFont.length == columnCount;
			if (cellFont != null && !doNotModify) {
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
			doNotModify = columnCount == 1 && strings != null && strings.length == columnCount;
			if (strings != null && !doNotModify) {
				String [] temp = new String [columnCount];
				System.arraycopy (strings, 0, temp, 0, index);
				System.arraycopy (strings, index, temp, index+1, columnCount-index-1);
				temp [index] = "";
				item.strings = temp;
			}
		}
	}

	updateHeaderCSS();

	/*
	 * Feature in GTK. The tree view does not resize immediately if a table
	 * column is created when the table is not visible. If the width of the
	 * new column is queried, GTK returns an incorrect value. The fix is to
	 * ensure that the columns are resized before any queries.
	 */
	if(!isVisible ()) {
		forceResize();
	}
}

void createItem (TableItem item, int index) {
    if (!(0 <= index && index <= itemCount)) {
        error(SWT.ERROR_INVALID_RANGE);
    }
	if (usesVirtualNativeModel ()) {
		int oldCount = itemCount;
		virtualItems.insert (index, item);
		resetVirtualNativeModel (oldCount + 1, index, 0, 1);
		return;
	}
	if ((style & SWT.VIRTUAL) == 0 && itemCount == items.length) {
		int length = drawCount <= 0 ? items.length + 4 : Math.max (4, items.length * 3 / 2);
		TableItem [] newItems = new TableItem [length];
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
	}
	item.handle = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
    if (item.handle == 0) {
        error(SWT.ERROR_NO_HANDLES);
    }
	if (index == itemCount) {
		GTK.gtk_list_store_append (modelHandle, item.handle);
	} else {
		GTK.gtk_list_store_insert (modelHandle, item.handle, index);
	}
	if ((style & SWT.VIRTUAL) != 0) {
		virtualItems.insert (index, item);
		itemCount++;
	} else {
		System.arraycopy (items, index, items, index + 1, itemCount++ - index);
		items [index] = item;
	}
}

void createRenderers (long columnHandle, int modelIndex, boolean check, int columnStyle) {
	if (boundedHorizontalViewEnabled () && virtualCallbackDepth != 0) {
		// GtkCellArea may retain CellInfo pointers across MeasureItem. Preserve
		// logical mutation immediately; rebuild its physical packing after return.
		virtualRenderersDirty = virtualHorizontalDirty = true;
		scheduleVirtualView ();
		return;
	}
	GTK.gtk_tree_view_column_clear (columnHandle);
	boolean logicalVirtual = usesVirtualNativeModel ();
	if ((style & SWT.CHECK) != 0 && check) {
		GTK.gtk_tree_view_column_pack_start (columnHandle, checkRenderer, false);
		if (boundedHorizontalViewEnabled ()) OS.g_object_set (checkRenderer, OS.mode, 0 /* GTK_CELL_RENDERER_MODE_INERT */, 0);
		if (!logicalVirtual) {
			GTK.gtk_tree_view_column_add_attribute (columnHandle, checkRenderer, OS.active, CHECKED_COLUMN);
			GTK.gtk_tree_view_column_add_attribute (columnHandle, checkRenderer, OS.inconsistent, GRAYED_COLUMN);
			if (!ownerDraw) {
				GTK.gtk_tree_view_column_add_attribute (columnHandle, checkRenderer,
						OS.cell_background_rgba, BACKGROUND_COLUMN);
			}
		}
		if ((style & SWT.VIRTUAL) != 0 || ownerDraw) {
			GTK.gtk_tree_view_column_set_cell_data_func (columnHandle, checkRenderer,
					display.cellDataProc, handle, 0);
			OS.g_object_set_qdata (checkRenderer, Display.SWT_OBJECT_INDEX1, columnHandle);
		}
	}
	long pixbufType = display.gtk_cell_renderer_pixbuf_get_type ();
	long pixbufRenderer = ownerDraw && pixbufType != 0
			? OS.g_object_new (pixbufType, 0) : GTK.gtk_cell_renderer_pixbuf_new ();
	if (pixbufRenderer == 0) {
		error (SWT.ERROR_NO_HANDLES);
	} else if (!ownerDraw) {
		GTK.gtk_cell_renderer_set_fixed_size(pixbufRenderer, 0, 0);
	}
	long textRenderer = ownerDraw
			? OS.g_object_new (display.gtk_cell_renderer_text_get_type (), 0)
			: GTK.gtk_cell_renderer_text_new ();
    if (textRenderer == 0) {
        error(SWT.ERROR_NO_HANDLES);
    }
	if (ownerDraw) {
		OS.g_object_set_qdata (pixbufRenderer, Display.SWT_OBJECT_INDEX1, columnHandle);
		OS.g_object_set_qdata (textRenderer, Display.SWT_OBJECT_INDEX1, columnHandle);
	}
	if ((style & SWT.CHECK) != 0 && check) {
		OS.g_object_set (pixbufRenderer, OS.mode, GTK.GTK_CELL_RENDERER_MODE_ACTIVATABLE, 0);
	}
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
	if (!logicalVirtual) {
		GTK.gtk_tree_view_column_add_attribute (columnHandle, pixbufRenderer,
				OS.pixbuf, modelIndex + CELL_PIXBUF);
		if (!ownerDraw) {
			GTK.gtk_tree_view_column_add_attribute (columnHandle, pixbufRenderer,
					OS.cell_background_rgba, BACKGROUND_COLUMN);
			GTK.gtk_tree_view_column_add_attribute (columnHandle, textRenderer,
					OS.cell_background_rgba, BACKGROUND_COLUMN);
		}
		GTK.gtk_tree_view_column_add_attribute (columnHandle, textRenderer,
				OS.text, modelIndex + CELL_TEXT);
		GTK.gtk_tree_view_column_add_attribute (columnHandle, textRenderer,
				OS.foreground_rgba, FOREGROUND_COLUMN);
		GTK.gtk_tree_view_column_add_attribute (columnHandle, textRenderer,
				OS.font_desc, FONT_COLUMN);
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
	if (logicalVirtual || (style & SWT.VIRTUAL) != 0 || customDraw || ownerDraw) {
		GTK.gtk_tree_view_column_set_cell_data_func (columnHandle, textRenderer,
				display.cellDataProc, handle, 0);
		GTK.gtk_tree_view_column_set_cell_data_func (columnHandle, pixbufRenderer,
				display.cellDataProc, handle, 0);
	}
}

@Override
void createWidget (int index) {
	viewportLayers = new ViewportLayerState ();
	super.createWidget (index);
	items = new TableItem [4];
	if ((style & SWT.VIRTUAL) != 0) {
		virtualItems = new VirtualItemStorage<> ();
		virtualViewport = new VirtualViewportPlanner ();
	}
	columns = new TableColumn [4];
	itemCount = columnCount = 0;
	initializeViewportLayers ();
	initializeVirtualAccessible ();
	// In GTK 3 font description is inherited from parent widget which is not how SWT has always worked,
	// reset to default font to get the usual behavior
	setFontDescription(defaultFont().handle);
}

@Override
int applyThemeBackground () {
	return -1; /* No Change */
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
 * Deselects the item at the given zero-relative index in the receiver.
 * If the item at the index was already deselected, it remains
 * deselected. Indices that are out of range are ignored.
 *
 * @param index the index of the item to deselect
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void deselect (int index) {
	checkWidget();
	if (usesBoundedVirtualView ()) {
		if (index >= 0 && index < itemCount) virtualViewport.selection ().setSelected (index, false);
		restoreVirtualSelection ();
		return;
	}
    if (index < 0 || index >= itemCount) {
        return;
    }
	boolean fixColumn = showFirstColumn ();
	long selection = GTK.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	if (usesVirtualNativeModel ()) {
		long iter = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
        if (iter == 0) {
            error(SWT.ERROR_NO_HANDLES);
        }
        if (GTK.gtk_tree_model_iter_nth_child(modelHandle, iter, 0, index)) {
            GTK.gtk_tree_selection_unselect_iter(selection, iter);
        }
		OS.g_free (iter);
	} else {
		GTK.gtk_tree_selection_unselect_iter (selection, _getItem (index).handle);
	}
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
    if (fixColumn) {
        hideFirstColumn();
    }
}

/**
 * Deselects the items at the given zero-relative indices in the receiver.
 * If the item at the given zero-relative index in the receiver
 * is selected, it is deselected.  If the item at the index
 * was not selected, it remains deselected.  The range of the
 * indices is inclusive. Indices that are out of range are ignored.
 *
 * @param start the start index of the items to deselect
 * @param end the end index of the items to deselect
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void deselect (int start, int end) {
	checkWidget();
	if (usesBoundedVirtualView ()) {
		int first = Math.max (0, start), last = (int)Math.min (itemCount, (long)end + 1);
		if (first < last) virtualViewport.selection ().deselectRange (first, last);
		restoreVirtualSelection ();
		return;
	}
	boolean fixColumn = showFirstColumn ();
	long selection = GTK.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	long iter = usesVirtualNativeModel () ? OS.g_malloc (GTK.GtkTreeIter_sizeof ()) : 0;
    if (usesVirtualNativeModel() && iter == 0) {
        error(SWT.ERROR_NO_HANDLES);
    }
	try {
		for (int index=start; index<=end; index++) {
            if (index < 0 || index >= itemCount) {
                continue;
            }
			if (usesVirtualNativeModel ()) {
                if (GTK.gtk_tree_model_iter_nth_child(modelHandle, iter, 0, index)) {
                    GTK.gtk_tree_selection_unselect_iter(selection, iter);
                }
			} else {
				GTK.gtk_tree_selection_unselect_iter (selection, _getItem (index).handle);
			}
		}
	} finally {
        if (iter != 0) {
            OS.g_free(iter);
        }
	}
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
    if (fixColumn) {
        hideFirstColumn();
    }
}

/**
 * Deselects the items at the given zero-relative indices in the receiver.
 * If the item at the given zero-relative index in the receiver
 * is selected, it is deselected.  If the item at the index
 * was not selected, it remains deselected. Indices that are out
 * of range and duplicate indices are ignored.
 *
 * @param indices the array of indices for the items to deselect
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the set of indices is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void deselect (int [] indices) {
	checkWidget();
	if (usesBoundedVirtualView ()) {
		if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
		for (int index : indices) if (index >= 0 && index < itemCount) virtualViewport.selection ().setSelected (index, false);
		restoreVirtualSelection ();
		return;
	}
    if (indices == null) {
        error(SWT.ERROR_NULL_ARGUMENT);
    }
	boolean fixColumn = showFirstColumn ();
	long selection = GTK.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	long iter = usesVirtualNativeModel () ? OS.g_malloc (GTK.GtkTreeIter_sizeof ()) : 0;
    if (usesVirtualNativeModel() && iter == 0) {
        error(SWT.ERROR_NO_HANDLES);
    }
	try {
		for (int index : indices) {
            if (index < 0 || index >= itemCount) {
                continue;
            }
			if (usesVirtualNativeModel ()) {
                if (GTK.gtk_tree_model_iter_nth_child(modelHandle, iter, 0, index)) {
                    GTK.gtk_tree_selection_unselect_iter(selection, iter);
                }
			} else {
				GTK.gtk_tree_selection_unselect_iter (selection, _getItem (index).handle);
			}
		}
	} finally {
        if (iter != 0) {
            OS.g_free(iter);
        }
	}
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
public void deselectAll () {
	checkWidget();
	if (usesBoundedVirtualView ()) {
		virtualViewport.selection ().clear ();
		restoreVirtualSelection ();
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

void destroyItem (TableColumn column) {
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

	if (usesVirtualNativeModel ()) {
		if (columnCount == 0) {
			createColumn (null, 0);
		} else {
			for (int i=0; i<materializedItemCount (); i++) {
				TableItem item = materializedItem (i);
                if (item != null) {
                    item.removeVirtualColumn(index, columnCount);
                }
			}
			if (index == 0) {
				TableColumn checkColumn = columns [0];
				createRenderers (checkColumn.handle, checkColumn.modelIndex, true, checkColumn.style);
			}
		}
	} else if (columnCount == 0) {
		long oldModel = modelHandle;
		long [] types = getColumnTypes (1);
		long newModel = GTK.gtk_list_store_newv (types.length, types);
        if (newModel == 0) {
            error(SWT.ERROR_NO_HANDLES);
        }
		GTK.gtk_tree_view_set_model (handle, 0);
		copyModel (oldModel, column.modelIndex, newModel, FIRST_COLUMN, FIRST_COLUMN + CELL_TYPES);
		GTK.gtk_tree_view_set_model (handle, newModel);
		setModel (newModel);
		createColumn (null, 0);
	} else {
		for (int i=0; i<materializedItemCount (); i++) {
			TableItem item = materializedItem (i);
			if (item != null) {
				long iter = item.handle;
				int modelIndex = column.modelIndex;
				GTK.gtk_list_store_set (modelHandle, iter, modelIndex + CELL_PIXBUF, (long )0, -1);
				GTK.gtk_list_store_set (modelHandle, iter, modelIndex + CELL_TEXT, (long )0, -1);
				GTK.gtk_list_store_set (modelHandle, iter, modelIndex + CELL_FOREGROUND, (long )0, -1);
				GTK.gtk_list_store_set (modelHandle, iter, modelIndex + CELL_BACKGROUND, (long )0, -1);
				GTK.gtk_list_store_set (modelHandle, iter, modelIndex + CELL_FONT, (long )0, -1);
				Font [] cellFont = item.cellFont;
				if (cellFont != null) {
					Font [] temp = new Font [columnCount];
					System.arraycopy (cellFont, 0, temp, 0, index);
					System.arraycopy (cellFont, index + 1, temp, index, columnCount - index);
					item.cellFont = temp;
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
			TableColumn checkColumn = columns [0];
			createRenderers (checkColumn.handle, checkColumn.modelIndex, true, checkColumn.style);
		}
	}
	if (!searchEnabled ()) {
		GTK.gtk_tree_view_set_search_column (handle, -1);
	} else {
		int firstColumn = columnCount == 0 ? FIRST_COLUMN : columns [0].modelIndex;
		GTK.gtk_tree_view_set_search_column (handle, firstColumn + CELL_TEXT);
	}
}

void destroyItem (TableItem item) {
	int index;
	if ((style & SWT.VIRTUAL) != 0) {
		index = virtualItems.indexOfIdentity (item);
	} else {
		index = 0;
        while (index < itemCount && items [index] != item) {
            index++;
        }
	}
    if (index == itemCount || index < 0) {
        return;
    }
	if (usesVirtualNativeModel ()) {
		int oldCount = itemCount;
		virtualItems.remove (index);
		resetVirtualNativeModel (oldCount - 1, index, 1, 0);
        if (itemCount == 0) {
            resetCustomDraw();
        }
		return;
	}
	long selection = GTK.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	GTK.gtk_list_store_remove (modelHandle, item.handle);
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	if ((style & SWT.VIRTUAL) != 0) {
		virtualItems.remove (index);
		itemCount--;
	} else {
		System.arraycopy (items, index + 1, items, index, --itemCount - index);
		items [itemCount] = null;
	}
    if (itemCount == 0) {
        resetCustomDraw();
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
long eventSurface () {
	return paintSurface ();
}

@Override
Rectangle getClientAreaInPixels() {
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
 * If no <code>TableColumn</code>s were created by the programmer,
 * this method will throw <code>ERROR_INVALID_RANGE</code> despite
 * the fact that a single column of data may be visible in the table.
 * This occurs when the programmer uses the table like a list, adding
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
 * @see Table#getColumnOrder()
 * @see Table#setColumnOrder(int[])
 * @see TableColumn#getMoveable()
 * @see TableColumn#setMoveable(boolean)
 * @see SWT#Move
 */
public TableColumn getColumn (int index) {
	checkWidget();
    if (!(0 <= index && index < columnCount)) {
        error(SWT.ERROR_INVALID_RANGE);
    }
	return columns [index];
}

/**
 * Returns the number of columns contained in the receiver.
 * If no <code>TableColumn</code>s were created by the programmer,
 * this value is zero, despite the fact that visually, one column
 * of items may be visible. This occurs when the programmer uses
 * the table like a list, adding items but never creating a column.
 *
 * @return the number of columns
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getColumnCount () {
	checkWidget();
	return columnCount;
}

long [] getColumnTypes (int columnCount) {
	long [] types = new long [FIRST_COLUMN + (columnCount * CELL_TYPES)];
	// per row data
	types [CHECKED_COLUMN] = OS.G_TYPE_BOOLEAN ();
	types [GRAYED_COLUMN] = OS.G_TYPE_BOOLEAN ();
	types [FOREGROUND_COLUMN] = GDK.GDK_TYPE_RGBA();
	types [BACKGROUND_COLUMN] = GDK.GDK_TYPE_RGBA();
	types [FONT_COLUMN] = OS.PANGO_TYPE_FONT_DESCRIPTION ();
	// per cell data
	for (int i=FIRST_COLUMN; i<types.length; i+=CELL_TYPES) {
		types [i + CELL_PIXBUF] = GDK.GDK_TYPE_PIXBUF ();
		types [i + CELL_SURFACE] = OS.G_TYPE_LONG();
		types [i + CELL_TEXT] = OS.G_TYPE_STRING ();
		types [i + CELL_FOREGROUND] = GDK.GDK_TYPE_RGBA();
		types [i + CELL_BACKGROUND] = GDK.GDK_TYPE_RGBA();
		types [i + CELL_FONT] = OS.PANGO_TYPE_FONT_DESCRIPTION ();
	}
	return types;
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
 * @see Table#setColumnOrder(int[])
 * @see TableColumn#getMoveable()
 * @see TableColumn#setMoveable(boolean)
 * @see SWT#Move
 *
 * @since 3.1
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

/**
 * Returns an array of <code>TableColumn</code>s which are the
 * columns in the receiver.  Columns are returned in the order
 * that they were created.  If no <code>TableColumn</code>s were
 * created by the programmer, the array is empty, despite the fact
 * that visually, one column of items may be visible. This occurs
 * when the programmer uses the table like a list, adding items but
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
 * @see Table#getColumnOrder()
 * @see Table#setColumnOrder(int[])
 * @see TableColumn#getMoveable()
 * @see TableColumn#setMoveable(boolean)
 * @see SWT#Move
 */
public TableColumn [] getColumns () {
	checkWidget();
	TableColumn [] result = new TableColumn [columnCount];
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

TableItem getFocusItem () {
	if (usesBoundedVirtualView ()) {
		return virtualFocusIndex >= 0 && virtualFocusIndex < itemCount ? getItem (virtualFocusIndex) : null;
	}
	long [] path = new long [1];
	GTK.gtk_tree_view_get_cursor (handle, path, null);
    if (path [0] == 0) {
        return null;
    }
	TableItem item = null;
	long indices = GTK.gtk_tree_path_get_indices (path [0]);
	if (indices != 0) {
		int [] index = new int []{-1};
		C.memmove (index, indices, 4);
		item = _getItem (index [0]);
        if (item != null) {
            item.pinVirtualFacade();
        }
	}
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
 * @since 2.0
 */
public int getHeaderHeight () {
	checkWidget ();
    if (!GTK.gtk_tree_view_get_headers_visible(handle)) {
        return 0;
    }

	int height = 0;
	if (columnCount > 0) {
		GtkRequisition requisition = new GtkRequisition ();
		for (int i = 0; i < columnCount; i++) {
			long buttonHandle = columns[i].buttonHandle;
			if (buttonHandle != 0) {
				if (!GTK.gtk_widget_get_visible(buttonHandle))  {
					gtk_widget_show(buttonHandle);
					gtk_widget_get_preferred_size(buttonHandle, requisition);
					gtk_widget_hide(buttonHandle);
				} else {
					gtk_widget_get_preferred_size(buttonHandle, requisition);
				}
				height = Math.max(height, requisition.height);
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
 */
public TableItem getItem (int index) {
	checkWidget();
    if (!(0 <= index && index < itemCount)) {
        error(SWT.ERROR_INVALID_RANGE);
    }
	TableItem item = _getItem (index);
	item.pinVirtualFacade ();
	return item;
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
public TableItem getItem (Point point) {
	checkWidget();
    if (point == null) {
        error(SWT.ERROR_NULL_ARGUMENT);
    }
	long [] path = new long [1];
	GTK.gtk_widget_realize (handle);
	int y = point.y;
	/*
	 * On GTK4 the header is included in the entire widget's surface, so we must subtract
	 * its size from the y-coordinate. This does not apply on GTK3 as the header and
	 * "main-widget" have separate GdkWindows.
	 */
	if (getHeaderVisible() && GTK.GTK4) {
		y -= getHeaderHeight();
	}
    if (usesBoundedHorizontalView () && (point.x < 0 || point.x >= getClientWidth () || y < 0 || y >= virtualBodyExtent)) return null;
    if (!GTK.gtk_tree_view_get_path_at_pos(handle, virtualNativeX (point.x), y, path, null, null, null)) {
        return null;
    }
    if (path [0] == 0) {
        return null;
    }
	long indices = GTK.gtk_tree_path_get_indices (path [0]);
	TableItem item = null;
	if (indices != 0) {
		int [] index = new int [1];
		C.memmove (index, indices, 4);
		int logicalIndex = logicalIndexForNativeHit (index [0]);
        if (logicalIndex >= 0) {
            item = _getItem(logicalIndex);
        }
        if (item != null) {
            item.pinVirtualFacade();
        }
	}
	GTK.gtk_tree_path_free (path [0]);
	return item;
}

/* Translate a native hit while GTK is still applying a programmatic virtual scroll.
 * Native hit testing continues to own columns, clipping and row geometry. */
private int logicalIndexForNativeHit (int nativeIndex) {
	if (usesBoundedVirtualView ()) {
		long logical = virtualViewStart + (long)nativeIndex;
		return nativeIndex >= 0 && nativeIndex < virtualViewCount && logical < itemCount ? (int)logical : -1;
	}
    if ((style & SWT.VIRTUAL) == 0 || pendingTopIndex < 0) {
        return nativeIndex;
    }
	int nativeTop = nativeTopIndex ();
    if (nativeTop < 0) {
        return nativeIndex;
    }
    if (nativeTop == pendingTopIndex) {
        topIndex = pendingTopIndex;
        pendingTopIndex = -1;
        long adjustment = GTK.gtk_scrollable_get_vadjustment (handle);
        cachedAdjustment = GTK.gtk_adjustment_get_value (adjustment);
        return nativeIndex;
    }
	long logicalIndex = (long) nativeIndex + pendingTopIndex - nativeTop;
	return logicalIndex >= 0 && logicalIndex < itemCount ? (int) logicalIndex : -1;
}

private int nativeTopIndex () {
	long [] topPath = new long [1];
    if (!GTK.gtk_tree_view_get_path_at_pos(handle, 1, 1, topPath, null, null, null)
            || topPath [0] == 0) {
        return -1;
    }
	try {
		long indices = GTK.gtk_tree_path_get_indices (topPath [0]);
        if (indices == 0) {
            return -1;
        }
		int [] nativeTop = new int [1];
		C.memmove (nativeTop, indices, 4);
		return nativeTop [0];
	} finally {
		GTK.gtk_tree_path_free (topPath [0]);
	}
}

/**
 * Returns the number of items contained in the receiver.
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
	return itemCount;
}

/**
 * Returns the height of the area which would be used to
 * display <em>one</em> of the items in the receiver.
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
	if (usesBoundedVirtualView ()) {
		/* Geometry is independent of payload population. In particular, sizing a
		 * distant window must not ask the semantic model to populate row zero. */
		if (virtualViewCount > 0 && GTK.gtk_widget_get_realized (handle)) {
			long path = virtualViewPath (virtualViewStart);
			try {
				GdkRectangle area = new GdkRectangle ();
				GTK.gtk_tree_view_get_background_area (handle, path, 0, area);
				if (area.height > 0) return area.height;
			} finally {
				GTK.gtk_tree_path_free (path);
			}
		}
		int height = 0;
		boolean previousIgnoreSize = ignoreSize;
		ignoreSize = true;
		try {
			for (int index = 0; index < Math.max (1, columnCount); index++) {
				long column = GTK.gtk_tree_view_get_column (handle, index);
				int [] size = new int [1];
				GTK3.gtk_tree_view_column_cell_get_size (column, null, null, null, null, size);
				int cellHeight = size [0];
				long text = getTextRenderer (column);
				if (text != 0) {
					GTK.gtk_cell_renderer_get_preferred_height_for_width (text, handle, 0, size, null);
					cellHeight = Math.max (cellHeight, size [0]);
				}
				height = Math.max (height, cellHeight);
			}
		} finally {
			ignoreSize = previousIgnoreSize;
		}
		return height;
	}
	int height = 0;

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
		GTK.gtk_tree_model_get_iter_first(modelHandle, iter);

		int columnCount = Math.max(1, this.columnCount);
		for (int i = 0; i < columnCount; i++) {
			long column = GTK.gtk_tree_view_get_column(handle, i);
			GTK.gtk_tree_view_column_cell_set_cell_data(column, modelHandle, iter, false, false);
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
 * Returns a (possibly empty) array of <code>TableItem</code>s which
 * are the items in the receiver.
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
 */
public TableItem [] getItems () {
	checkWidget();
	TableItem [] result = new TableItem [itemCount];
	if ((style & SWT.VIRTUAL) != 0) {
		for (int i=0; i<itemCount; i++) {
			result [i] = _getItem (i);
			result [i].pinVirtualFacade ();
		}
	} else {
		System.arraycopy (items, 0, result, 0, itemCount);
	}
	return result;
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
 */
public boolean getLinesVisible() {
	checkWidget();
	return GTK.gtk_tree_view_get_grid_lines(handle) > GTK.GTK_TREE_VIEW_GRID_LINES_NONE;
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
 * Returns an array of <code>TableItem</code>s that are currently
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
public TableItem [] getSelection () {
	checkWidget();
	if (usesBoundedVirtualView ()) {
		int [] indices = virtualViewport.selection ().toArray ();
		TableItem [] selected = new TableItem [indices.length];
		for (int i = 0; i < indices.length; i++) selected [i] = getItem (indices [i]);
		return selected;
	}
	long selection = GTK.gtk_tree_view_get_selection (handle);
	long list = GTK.gtk_tree_selection_get_selected_rows (selection, null);
	long originalList = list;
	if (list != 0) {
		int count = OS.g_list_length (list);
		int [] treeSelection = new int [count];
		int length = 0;
		for (int i=0; i<count; i++) {
			long data = OS.g_list_data (list);
			long indices = GTK.gtk_tree_path_get_indices (data);
			if (indices != 0) {
				int [] index = new int [1];
				C.memmove (index, indices, 4);
				treeSelection [length] = index [0];
				length++;
			}
			GTK.gtk_tree_path_free (data);
			list = OS.g_list_next (list);
		}
		OS.g_list_free (originalList);
		TableItem [] result = new TableItem [length];
		for (int i=0; i<result.length; i++) {
			result [i] = _getItem (treeSelection [i]);
			result [i].pinVirtualFacade ();
		}
		return result;
	}
	return new TableItem [0];
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
	if (usesBoundedVirtualView ()) return virtualViewport.selection ().selectedCount ();
	long selection = GTK.gtk_tree_view_get_selection (handle);
	return GTK.gtk_tree_selection_count_selected_rows (selection);
}

/**
 * Returns the zero-relative index of the item which is currently
 * selected in the receiver, or -1 if no item is selected.
 *
 * @return the index of the selected item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getSelectionIndex () {
	checkWidget();
	if (usesBoundedVirtualView ()) return firstVirtualSelectionIndex ();
	long selection = GTK.gtk_tree_view_get_selection (handle);
	long list = GTK.gtk_tree_selection_get_selected_rows (selection, null);
	long originalList = list;
	if (list != 0) {
		int [] index = new int [1];
		boolean foundIndex = false;
		while (list != 0) {
			long data = OS.g_list_data (list);
			if (!foundIndex) {
				long indices = GTK.gtk_tree_path_get_indices (data);
				if (indices != 0) {
					C.memmove (index, indices, 4);
					foundIndex = true;
				}
			}
			list = OS.g_list_next (list);
			GTK.gtk_tree_path_free (data);
		}
		OS.g_list_free (originalList);
		return index [0];
	}
	return -1;
}

/**
 * Returns the zero-relative indices of the items which are currently
 * selected in the receiver. The order of the indices is unspecified.
 * The array is empty if no items are selected.
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its selection, so modifying the array will
 * not affect the receiver.
 * </p>
 * @return the array of indices of the selected items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int [] getSelectionIndices () {
	checkWidget();
	if (usesBoundedVirtualView ()) return virtualViewport.selection ().toArray ();
	long selection = GTK.gtk_tree_view_get_selection (handle);
	long list = GTK.gtk_tree_selection_get_selected_rows (selection, null);
	long originalList = list;
	if (list != 0) {
		int count = OS.g_list_length (list);
		int [] treeSelection = new int [count];
		int length = 0;
		for (int i=0; i<count; i++) {
			long data = OS.g_list_data (list);
			long indices = GTK.gtk_tree_path_get_indices (data);
			if (indices != 0) {
				int [] index = new int [1];
				C.memmove (index, indices, 4);
				treeSelection [length] = index [0];
				length++;
			}
			list = OS.g_list_next (list);
			GTK.gtk_tree_path_free (data);
		}
		OS.g_list_free (originalList);
		int [] result = new int [length];
		System.arraycopy (treeSelection, 0, result, 0, length);
		return result;
	}
	return new int [0];
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
 * @see #setSortColumn(TableColumn)
 *
 * @since 3.2
 */
public TableColumn getSortColumn () {
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
 * Returns the zero-relative index of the item which is currently
 * at the top of the receiver. This index can change when items are
 * scrolled or new items are added or removed.
 *
 * @return the index of the top item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getTopIndex () {
	checkWidget();
	if (usesBoundedVirtualView ()) return virtualViewport.firstVisible ();
	long vAdjustment = GTK.gtk_scrollable_get_vadjustment(handle);
	currentAdjustment = GTK.gtk_adjustment_get_value(vAdjustment);
	int nativeTop = nativeTopIndex ();
    if (pendingTopIndex >= 0) {
        if (nativeTop == pendingTopIndex) {
            topIndex = pendingTopIndex;
            pendingTopIndex = -1;
            cachedAdjustment = currentAdjustment;
        }
        return topIndex;
    }
    if (cachedAdjustment == currentAdjustment) {
        return topIndex;
    }
	return nativeTop >= 0 ? nativeTop : 0;
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
	if (usesBoundedHorizontalView () && (style & SWT.CHECK) != 0
			&& eventButton [0] == 1 && eventType == GDK.GDK_BUTTON_PRESS) {
		virtualToggleAt ((int)eventX [0], (int)eventY [0]);
		if (isDisposed ()) return 1;
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
	* Feature in GTK.  In a multi-select table view, when multiple items are already
	* selected, the selection state of the item is toggled and the previous selection
	* is cleared. This is not the desired behaviour when bringing up a popup menu.
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
	 * Bug 518414: Added rowActivated guard flag to only send a DefaultSelection when the
	 * double-click triggers a 'row-activated' signal. Note that this relies on the fact
	 * that 'row-activated' signal comes before double-click event. This prevents
	 * opening of the current highlighted item when double clicking on any expander arrow.
	 */
	if (eventType == GDK.GDK_2BUTTON_PRESS && rowActivated) {
		sendTreeDefaultSelection ();
		rowActivated = false;
	}

	return result;
}

@Override
int gtk_gesture_press_event (long gesture, int n_press, double x, double y, long event) {
    if (n_press == 1) {
        return GTK4.GTK_EVENT_SEQUENCE_NONE;
    }
	int result = super.gtk_gesture_press_event(gesture, n_press, x, y, event);

	// TODO: GTK4 replicate gtk_button_press_event functions

	if (n_press == 2 && rowActivated) {
		sendTreeDefaultSelection ();
		rowActivated = false;
	}

	return result;
}

@Override
long gtk_row_activated (long tree, long path, long column) {
	rowActivated = true;
	return 0;
}


@Override
long gtk3_key_press_event (long widget, long event) {
	int [] key = new int[1];
	GDK.gdk_event_get_keyval(event, key);
	if (usesBoundedVirtualView ()) {
		int mask = gdk3_event_get_state (event);
		boolean navigation = switch (key [0]) {
			case GDK.GDK_Up, GDK.GDK_KP_Up, GDK.GDK_Down, GDK.GDK_KP_Down,
					GDK.GDK_Home, GDK.GDK_KP_Home, GDK.GDK_End, GDK.GDK_KP_End,
					GDK.GDK_Page_Up, GDK.GDK_KP_Page_Up, GDK.GDK_Page_Down, GDK.GDK_KP_Page_Down -> true;
			default -> false;
		};
		boolean selectAll = (mask & GDK.GDK_CONTROL_MASK) != 0 && (key [0] == 'a' || key [0] == 'A');
		if (navigation || selectAll) {
			long result = super.gtk3_key_press_event (widget, event);
			if (result != 0 || isDisposed ()) return result;
			if (navigation) virtualNavigate (key [0], mask);
			else {
				if ((mask & GDK.GDK_SHIFT_MASK) != 0) deselectAll (); else selectAll ();
				TableItem item = getFocusItem ();
				if (item != null) { Event selected = new Event (); selected.item = item; sendSelectionEvent (SWT.Selection, selected, false); }
			}
			return 1;
		}
	}

	switch (key[0]) {
		case GDK.GDK_Return:
			// Send DefaultSelectionEvent when:
			// when    : Enter, Shift+Enter, Ctrl+Enter are pressed.
			// Not when: Alt+Enter, (Meta|Super|Hyper)+Enter, reason is stateMask is not provided on Gtk.
			// Note: alt+Enter creates a selection on GTK, but we filter it out to be a bit more consitent Win32 (521387)
			int keymask = gdk3_event_get_state (event);
			if ((keymask & (GDK.GDK_SUPER_MASK | GDK.GDK_META_MASK | GDK.GDK_HYPER_MASK | GDK.GDK_MOD1_MASK)) == 0) {
				sendTreeDefaultSelection ();
			}
			break;
		case GDK.GDK_space:
			if ((style & SWT.CHECK) != 0) {
				TableItem[] selected = getSelection();
				for (int i = 0; i < selected.length; i++) {
					toggleItemAndSendEvent(selected[i]);
				}

				// Maintain current selection by stopping additional handling to GDK_space event
				return 1;
			}
			break;
	}

	return super.gtk3_key_press_event (widget, event);
}

private void toggleItemAndSendEvent(TableItem item) {
	item.pinVirtualFacade ();
	item.setChecked (!item.getChecked ());

	Event event = new Event ();
	event.detail = SWT.CHECK;
	event.item = item;
	sendSelectionEvent (SWT.Selection, event, false);
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
	TableItem tableItem = getFocusItem ();
    if (tableItem == null) {
        return;
    }

	Event event = new Event ();
	event.item = tableItem;

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
	TableItem item = getFocusItem ();
	if (item != null) {
		Event event = new Event ();
		event.item = item;
		sendSelectionEvent (SWT.Selection, event, false);
	}
	return 0;
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
			int bottom = 0;
			if (usesBoundedVirtualView ()) {
				double remaining = (itemCount - (double)virtualViewport.firstVisible ()) * virtualRowExtent - virtualPixelRemainder;
				bottom = (int)Math.max (0, Math.min (height [0], remaining));
			} else if (itemCount != 0) {
				long iter = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
				GTK.gtk_tree_model_iter_nth_child (modelHandle, iter, 0, itemCount - 1);
				long path = GTK.gtk_tree_model_get_path (modelHandle, iter);
				GdkRectangle rect = new GdkRectangle ();
				GTK.gtk_tree_view_get_cell_area (handle, path, 0, rect);
				bottom = rect.y + rect.height;
				GTK.gtk_tree_path_free (path);
				OS.g_free (iter);
			}
			if (height [0] > bottom) {
				drawBackground (control, gdkResource, cairo, 0, bottom, width [0], height [0] - bottom);
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
	 * Bug 537960: JFace table viewers miss a repaint when resized by a SashForm
	 *
	 * If a listener of type SWT.MeasureItem, SWT.PaintItem and or SWT.EraseItem is registered,
	 * GTK will sometimes not invalidate the tree widget pixel cache when the tree is resized.
	 * As a result, a few of the bottom rows of JFace table viewers that use styled text are often not drawn on resize.
	 * If the table was resized since the last paint, we ignore this draw request
	 * and queue another draw request so that the pixel cache is properly invalidated.
	 */
	if (ownerDraw && haveBoundsChanged) {
		GTK.gtk_widget_queue_draw(handle);
		return 0;
	}
	drawInheritedBackground (cairo);
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

void initializeViewportLayers () {
	long horizontal = GTK.gtk_scrolled_window_get_hadjustment (scrolledHandle);
	long vertical = usesBoundedVirtualView () ? verticalAdjustment : GTK.gtk_scrollable_get_vadjustment (handle);
	viewportLayers.initialize (
			horizontal != 0 ? GTK.gtk_adjustment_get_value (horizontal) : 0,
			vertical != 0 ? GTK.gtk_adjustment_get_value (vertical) : 0);
}

@Override
long gtk_scroll_event (long widget, long eventPtr) {
	pendingTopIndex = -1;
	long result = super.gtk_scroll_event(widget, eventPtr);
	if (usesBoundedVirtualView ()) return result;
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
boolean virtualToggleColumnCurrent (long nativeColumn, TableColumn owner, boolean implicit, long generation) {
	if (isDisposed () || virtualModelGeneration != generation || virtualHorizontalDirty) return false;
	if (implicit) return columnCount == 0 && GTK.gtk_tree_view_get_column (handle, 0) == nativeColumn;
	return owner != null && !owner.isDisposed () && owner.parent == this && owner.handle == nativeColumn
			&& columnCount > 0 && columns [0] == owner;
}

void virtualToggleAt (int x, int y) {
	long [] path = new long [1], nativeColumn = new long [1];
	if (!GTK.gtk_tree_view_get_path_at_pos (handle, x, y, path, nativeColumn, null, null) || path [0] == 0) return;
	try {
		if (virtualColumnIndex (nativeColumn [0]) != 0) return;
		long indices = GTK.gtk_tree_path_get_indices (path [0]);
		if (indices == 0) return;
		int [] row = new int [1];
		C.memmove (row, indices, 4);
		int logical = logicalIndexForNativeHit (row [0]);
		if (logical < 0) return;
		// A SetData provider may replace the hit column while retaining this
		// table and item. Validate its Java owner before reusing any native pointer.
		boolean implicit = columnCount == 0;
		TableColumn owner = implicit ? null : columns [0];
		long generation = virtualModelGeneration;
		TableItem item = _getItem (logical);
		if (!checkData (item) || item.isDisposed ()
				|| !virtualToggleColumnCurrent (nativeColumn [0], owner, implicit, generation)) return;
		GTK.gtk_tree_view_column_cell_set_cell_data (nativeColumn [0], modelHandle, item.handle, false, false);
		if (item.isDisposed () || !virtualToggleColumnCurrent (nativeColumn [0], owner, implicit, generation)) return;
		GdkRectangle cell = new GdkRectangle (), checkbox = new GdkRectangle ();
		GTK.gtk_tree_view_get_cell_area (handle, path [0], nativeColumn [0], cell);
		if (!virtualRendererAllocation (nativeColumn [0], checkRenderer, cell.height, checkbox)
				|| item.isDisposed () || !virtualToggleColumnCurrent (nativeColumn [0], owner, implicit, generation)) return;
		int [] separator = new int [1];
		GTK3.gtk_widget_style_get (handle, Converter.wcsToMbcs ("horizontal-separator", true), separator, 0);
		long left = virtualHorizontalWindow ().viewportX (0)
				+ (long)GTK.gtk_adjustment_get_value (virtualHorizontalAdjustment) + separator [0] / 2 + checkbox.x;
		long top = cell.y + (long)checkbox.y;
		if (left <= x && x < left + checkbox.width && top <= y && y < top + checkbox.height) {
			toggleItemAndSendEvent (item);
		}
	} finally {
		GTK.gtk_tree_path_free (path [0]);
	}
}

@Override
long gtk_toggled (long renderer, long pathStr) {
	// The native allocation is a clipped slot. Pointer activation is resolved
	// against the logical checkbox in virtualToggleAt; keyboard handling above
	// retains the logical selection contract.
	if (usesBoundedHorizontalView ()) return 0;
	long path = GTK.gtk_tree_path_new_from_string (pathStr);
    if (path == 0) {
        return 0;
    }
	long indices = GTK.gtk_tree_path_get_indices (path);
	if (indices != 0) {
		int [] index = new int [1];
		C.memmove (index, indices, 4);
		int logical = logicalIndexForNativeHit (index [0]);
		if (logical < 0) { GTK.gtk_tree_path_free (path); return 0; }
		TableItem item = _getItem (logical);
		toggleItemAndSendEvent(item);
	}
	GTK.gtk_tree_path_free (path);
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
		virtualLogicalValueSignal = Integer.toUnsignedLong (OS.g_signal_connect (verticalAdjustment, OS.value_changed, proc, 1));
		virtualViewRangeSignal = Integer.toUnsignedLong (OS.g_signal_connect (virtualViewAdjustment, OS.changed, proc, 2));
		virtualViewValueSignal = Integer.toUnsignedLong (OS.g_signal_connect (virtualViewAdjustment, OS.value_changed, proc, 3));
		virtualColumnsSignal = Integer.toUnsignedLong (OS.g_signal_connect (handle, Converter.wcsToMbcs ("columns-changed", true), proc, 4));
		if (usesBoundedHorizontalView ()) {
		virtualHorizontalValueSignal = Integer.toUnsignedLong (OS.g_signal_connect (horizontalAdjustment, OS.value_changed, proc, 5));
		virtualHorizontalRangeSignal = Integer.toUnsignedLong (OS.g_signal_connect (virtualHorizontalAdjustment, OS.changed, proc, 6));
		virtualPhysicalHorizontalSignal = Integer.toUnsignedLong (OS.g_signal_connect (virtualHorizontalAdjustment, OS.value_changed, proc, 7));
		}
	}
	long selection = GTK.gtk_tree_view_get_selection(handle);
	OS.g_signal_connect_closure (selection, OS.changed, display.getClosure (CHANGED), false);
	OS.g_signal_connect_closure (handle, OS.row_activated, display.getClosure (ROW_ACTIVATED), false);
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
 */
public int indexOf (TableColumn column) {
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
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int indexOf (TableItem item) {
	checkWidget();
    if (item == null) {
        error(SWT.ERROR_NULL_ARGUMENT);
    }
    if ((style & SWT.VIRTUAL) != 0) {
        return virtualItems.indexOfIdentity(item);
    }
	if (1 <= lastIndexOf && lastIndexOf < itemCount - 1) {
        if (items [lastIndexOf] == item) {
            return lastIndexOf;
        }
        if (items [lastIndexOf + 1] == item) {
            return ++lastIndexOf;
        }
        if (items [lastIndexOf - 1] == item) {
            return --lastIndexOf;
        }
	}
	if (lastIndexOf < itemCount / 2) {
        for (int i = 0; i < itemCount; i++) {
            if (items [i] == item) {
                return lastIndexOf = i;
            }
        }
	} else {
        for (int i = itemCount - 1; i >= 0; --i) {
            if (items [i] == item) {
                return lastIndexOf = i;
            }
        }
	}
	return -1;
}

/**
 * Returns <code>true</code> if the item is selected,
 * and <code>false</code> otherwise.  Indices out of
 * range are ignored.
 *
 * @param index the index of the item
 * @return the selection state of the item at the index
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean isSelected (int index) {
	checkWidget();
	if (usesBoundedVirtualView ()) return virtualViewport.selection ().isSelected (index);
	long selection = GTK.gtk_tree_view_get_selection (handle);
	byte [] buffer = Converter.wcsToMbcs (Integer.toString (index), true);
	long path = GTK.gtk_tree_path_new_from_string (buffer);
	boolean answer = GTK.gtk_tree_selection_path_is_selected (selection, path);
	GTK.gtk_tree_path_free (path);
	return answer;
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
		for (TableColumn column : columns) {
			if (column != null) {
				GTK.gtk_widget_queue_draw(column.buttonHandle);
			}
		}
		wasScrolled = false;
	}
}

void recreateRenderers () {
	if (usesBoundedHorizontalView () && virtualCallbackDepth != 0) {
		virtualRenderersDirty = virtualHorizontalDirty = true;
		scheduleVirtualView ();
		return;
	}
	if (checkRenderer != 0) {
		display.removeWidget (checkRenderer);
		OS.g_object_unref (checkRenderer);
		long toggleType = display.gtk_cell_renderer_toggle_get_type ();
		checkRenderer = ownerDraw && toggleType != 0 ? OS.g_object_new (toggleType, 0) : GTK.gtk_cell_renderer_toggle_new ();
        if (checkRenderer == 0) {
            error(SWT.ERROR_NO_HANDLES);
        }
		OS.g_object_ref (checkRenderer);
		display.addWidget (checkRenderer, this);
		OS.g_signal_connect_closure (checkRenderer, OS.toggled, display.getClosure (TOGGLED), false);
	}
	if (columnCount == 0) {
		createRenderers (GTK.gtk_tree_view_get_column (handle, 0), Table.FIRST_COLUMN, true, 0);
	} else {
		for (int i = 0; i < columnCount; i++) {
			TableColumn column = columns [i];
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

@Override
void releaseChildren (boolean destroy) {
	if (usesBoundedVirtualView () && accessible != null) accessible.internal_dispose_Accessible ();
	for (int i=0; i<materializedItemCount (); i++) {
		TableItem item = materializedItem (i);
        if (item != null && !item.isDisposed()) {
            item.release(false);
        }
	}
    if (virtualItems != null) {
        virtualItems.clear(ignored -> {
        });
    }
	items = null;
	if (columns != null) {
		for (int i=0; i<columnCount; i++) {
			TableColumn column = columns [i];
            if (column != null && !column.isDisposed()) {
                column.release(false);
            }
		}
		columns = null;
	}
	super.releaseChildren (destroy);
}

@Override
void releaseWidget () {
	releaseVirtualAccessibleChildren ();
	disconnectVirtualAdjustments ();
	if (virtualViewModel != 0) OS.g_object_unref (virtualViewModel);
	if (virtualHorizontalAdjustment != 0) OS.g_object_unref (virtualHorizontalAdjustment);
	virtualHorizontalAdjustment = horizontalAdjustment = 0;
	if (virtualViewAdjustment != 0) OS.g_object_unref (virtualViewAdjustment);
	virtualViewModel = virtualViewAdjustment = verticalAdjustment = 0;
	super.releaseWidget ();
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

/**
 * Removes the item from the receiver at the given
 * zero-relative index.
 *
 * @param index the index for the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void remove (int index) {
	checkWidget();
    if (!(0 <= index && index < itemCount)) {
        error(SWT.ERROR_ITEM_NOT_REMOVED);
    }
	if (usesVirtualNativeModel ()) {
		int oldCount = itemCount;
		TableItem item = _getItem (index, false);
        if (item != null && !item.isDisposed()) {
            item.release(false);
        }
		virtualItems.remove (index);
		resetVirtualNativeModel (oldCount - 1, index, 1, 0);
		return;
	}
	long iter = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
	TableItem item = _getItem (index, false);
	boolean disposed = false;
	if (item != null) {
		disposed = item.isDisposed ();
		if (!disposed) {
			C.memmove (iter, item.handle, GTK.GtkTreeIter_sizeof ());
			item.release (false);
		}
	} else {
		GTK.gtk_tree_model_iter_nth_child (modelHandle, iter, 0, index);
	}
	if (!disposed) {
		long selection = GTK.gtk_tree_view_get_selection (handle);
		OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
		GTK.gtk_list_store_remove (modelHandle, iter);
		OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
		if ((style & SWT.VIRTUAL) != 0) {
			virtualItems.remove (index);
			itemCount--;
		} else {
			System.arraycopy (items, index + 1, items, index, --itemCount - index);
			items [itemCount] = null;
		}
	}
	OS.g_free (iter);
}

/**
 * Removes the items from the receiver which are
 * between the given zero-relative start and end
 * indices (inclusive).
 *
 * @param start the start of the range
 * @param end the end of the range
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if either the start or end are not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void remove (int start, int end) {
	checkWidget();
    if (start > end) {
        return;
    }
    if (!(0 <= start && start <= end && end < itemCount)) {
        error(SWT.ERROR_INVALID_RANGE);
    }
	if (start == 0 && end == itemCount - 1) {
		removeAll();
		return;
	}
	checkSetDataInProcessBeforeRemoval(start, end + 1);
	int removed = end - start + 1;
	if (usesVirtualNativeModel ()) {
		int oldCount = itemCount;
		virtualItems.removeRange (start, end + 1, item -> {
            if (!item.isDisposed()) {
                item.release(false);
            }
		});
		resetVirtualNativeModel (oldCount - removed, start, removed, 0);
		return;
	}
	long selection = GTK.gtk_tree_view_get_selection (handle);
	long iter = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
    if (iter == 0) {
        error(SWT.ERROR_NO_HANDLES);
    }
	for (int index = start; index <= end; index++) {
        if (index == start) {
            GTK.gtk_tree_model_iter_nth_child(modelHandle, iter, 0, index);
        }
		TableItem item = _getItem (index, false);
        if (item != null && !item.isDisposed()) {
            item.release(false);
        }
		OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
		GTK.gtk_list_store_remove (modelHandle, iter);
		OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	}
	OS.g_free (iter);
	if ((style & SWT.VIRTUAL) != 0) {
		virtualItems.removeRange (start, end + 1, ignored -> { });
	} else {
		System.arraycopy (items, end + 1, items, start, itemCount - end - 1);
        for (int i = itemCount - removed; i < itemCount; i++) {
            items [i] = null;
        }
	}
	itemCount -= removed;
}

/**
 * Removes the items from the receiver's list at the given
 * zero-relative indices.
 *
 * @param indices the array of indices of the items
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 *    <li>ERROR_NULL_ARGUMENT - if the indices array is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void remove (int [] indices) {
	checkWidget();
    if (indices == null) {
        error(SWT.ERROR_NULL_ARGUMENT);
    }
    if (indices.length == 0) {
        return;
    }
	int [] newIndices = new int [indices.length];
	System.arraycopy (indices, 0, newIndices, 0, indices.length);
	sort (newIndices);
	int start = newIndices [newIndices.length - 1], end = newIndices [0];
    if (!(0 <= start && start <= end && end < itemCount)) {
        error(SWT.ERROR_INVALID_RANGE);
    }
	if (usesVirtualNativeModel ()) {
		int last = -1;
		for (int i=0; i<newIndices.length; i++) {
			int index = newIndices [i];
            if (index == last) {
                continue;
            }
			int oldCount = itemCount;
			TableItem item = _getItem (index, false);
            if (item != null && !item.isDisposed()) {
                item.release(false);
            }
			virtualItems.remove (index);
			resetVirtualNativeModel (oldCount - 1, index, 1, 0);
			last = index;
		}
		return;
	}
	long selection = GTK.gtk_tree_view_get_selection (handle);
	int last = -1;
	long iter = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
    if (iter == 0) {
        error(SWT.ERROR_NO_HANDLES);
    }
	for (int i=0; i<newIndices.length; i++) {
		int index = newIndices [i];
		if (index != last) {
			TableItem item = _getItem (index, false);
			boolean disposed = false;
			if (item != null) {
				disposed = item.isDisposed ();
				if (!disposed) {
					C.memmove (iter, item.handle, GTK.GtkTreeIter_sizeof ());
					item.release (false);
				}
			} else {
				GTK.gtk_tree_model_iter_nth_child (modelHandle, iter, 0, index);
			}
			if (!disposed) {
				OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
				GTK.gtk_list_store_remove (modelHandle, iter);
				OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
				if ((style & SWT.VIRTUAL) != 0) {
					virtualItems.remove (index);
					itemCount--;
				} else {
					System.arraycopy (items, index + 1, items, index, --itemCount - index);
					items [itemCount] = null;
				}
			}
			last = index;
		}
	}
	OS.g_free (iter);
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
	checkWidget();
	checkSetDataInProcessBeforeRemoval(0, itemCount);
	if (usesVirtualNativeModel ()) {
		int oldCount = itemCount;
		for (int i=0; i<materializedItemCount (); i++) {
			TableItem item = materializedItem (i);
            if (item != null && !item.isDisposed()) {
                item.release(false);
            }
		}
		virtualItems = new VirtualItemStorage<> ();
		resetVirtualNativeModel (0, 0, oldCount, 0);
		resetCustomDraw ();
		return;
	}
	for (int i=0; i<materializedItemCount (); i++) {
		TableItem item = materializedItem (i);
        if (item != null && !item.isDisposed()) {
            item.release(false);
        }
	}
	items = new TableItem [4];
    if (virtualItems != null) {
        virtualItems = new VirtualItemStorage<>();
    }
	itemCount = 0;
	long selection = GTK.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	/*
	 * Bug 499850: On GTK3, calling gtk_list_store_clear with GtkSelectionMode GTK_SELECTION_MULTIPLE
	 * takes exponential time. Temporarily change the mode GTK_SELECTION_BROWSE before
	 * making the call to avoid performance hang.
	 */
	long selectionHandle = GTK.gtk_tree_view_get_selection(handle);
	boolean changeMode = (style & SWT.MULTI) != 0;
    if (changeMode) {
        GTK.gtk_tree_selection_set_mode(selectionHandle, GTK.GTK_SELECTION_BROWSE);
    }
	GTK.gtk_list_store_clear (modelHandle);
    if (changeMode) {
        GTK.gtk_tree_selection_set_mode(selectionHandle, GTK.GTK_SELECTION_MULTIPLE);
    }
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	resetCustomDraw ();
    if (!searchEnabled()) {
        GTK.gtk_tree_view_set_search_column(handle, -1);
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
 * @see #addSelectionListener(SelectionListener)
 */
public void removeSelectionListener(SelectionListener listener) {
	checkWidget();
    if (listener == null) {
        error(SWT.ERROR_NULL_ARGUMENT);
    }
    if (eventTable == null) {
        return;
    }
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);
}

void sendMeasureEvent (long cell, long width, long height) {
	if (!usesBoundedHorizontalView ()) { virtualSendMeasureEvent (cell, width, height); return; }
	virtualCallbackDepth++;
	try { virtualSendMeasureEvent (cell, width, height); }
	finally {
		virtualCallbackDepth--;
		if (!isDisposed () && (virtualRenderersDirty || virtualHorizontalDirty)) scheduleVirtualView ();
	}
}

void virtualSendMeasureEvent (long cell, long width, long height) {
	if (!ignoreSize && GTK.GTK_IS_CELL_RENDERER_TEXT (cell) && hooks (SWT.MeasureItem)) {
		long iter = OS.g_object_get_qdata (cell, Display.SWT_OBJECT_INDEX2);
		TableItem item = null;
		boolean isSelected = false;
		if (iter != 0) {
			int index = modelIndex (modelHandle, iter);
			if (index >= 0) {
				item = _getItem (index);
				long path = viewPath (iter);
				try {
					isSelected = usesBoundedVirtualView () ? virtualViewport.selection ().isSelected (index)
							: GTK.gtk_tree_selection_path_is_selected (GTK.gtk_tree_view_get_selection (handle), path);
				} finally {
					if (path != 0) GTK.gtk_tree_path_free (path);
				}
			}
		}
		if (item != null) {
			int columnIndex = 0;
			if (columnCount > 0) {
				long columnHandle = OS.g_object_get_qdata (cell, Display.SWT_OBJECT_INDEX1);
				if (usesBoundedHorizontalView ()) {
					columnIndex = virtualColumnIndex (columnHandle);
					if (columnIndex < 0) return;
				} else {
					for (int i = 0; i < columnCount; i++) {
						if (columns [i].handle == columnHandle) {
							columnIndex = i;
							break;
						}
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
			item.pinVirtualFacade ();
			event.item = item;
			event.index = columnIndex;
			event.gc = gc;
			Rectangle eventRect = new Rectangle (0, 0, contentWidth [0], contentHeight [0]);
			event.setBounds (eventRect);
            if (isSelected) {
                event.detail = SWT.SELECTED;
            }
			try {
				sendEvent (SWT.MeasureItem, event);
			} finally {
				gc.dispose ();
			}
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
		if (usesBoundedVirtualView () && virtualViewChanged) scheduleVirtualView ();
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
	virtualCallbackDepth++;
	try {
		if (usesBoundedHorizontalView ()) {
			virtualHorizontalRendererRender (cell, cr, snapshot, widget, background_area, cell_area, expose_area, flags);
		} else {
			virtualRendererRender (cell, cr, snapshot, widget, background_area, cell_area, expose_area, flags);
		}
	} finally {
		virtualCallbackDepth--;
		if (usesBoundedVirtualView () && virtualViewChanged) scheduleVirtualView ();
	}
}


/** Supply GTK's renderer its logical allocation while retaining a bounded Cairo clip. */
void virtualHorizontalRendererRender (long cell, long cr, long snapshot, long widget,
		long backgroundArea, long cellArea, long exposeArea, long flags) {
	long column = OS.g_object_get_qdata (cell, Display.SWT_OBJECT_INDEX1);
	int index = virtualColumnIndex (column);
	if (index < 0 || cr == 0) return;
	VirtualViewportPlanner.HorizontalWindow window = virtualHorizontalWindow ();
	if (window.residentColumnWidth (index) == 0) return;
	long generation = window.generation (), modelGeneration = virtualModelGeneration;
	long itemLease = OS.g_object_get_qdata (cell, Display.SWT_OBJECT_INDEX2);
	GdkRectangle originalCell = new GdkRectangle ();
	GdkRectangle originalBackground = new GdkRectangle ();
	OS.memmove (originalCell, cellArea, GdkRectangle.sizeof);
	OS.memmove (originalBackground, backgroundArea, GdkRectangle.sizeof);
	GdkRectangle logicalCell = new GdkRectangle ();
	if (!virtualRendererAllocation (column, cell, originalCell.height, logicalCell)
			|| isDisposed () || virtualHorizontalDirty || window.generation () != generation || modelGeneration != virtualModelGeneration
			|| virtualColumnIndex (column) != index || OS.g_object_get_qdata (cell, Display.SWT_OBJECT_INDEX2) != itemLease) {
		if (!isDisposed ()) scheduleVirtualView ();
		return;
	}
	int [] separator = new int [1];
	GTK3.gtk_widget_style_get (handle, Converter.wcsToMbcs ("horizontal-separator", true), separator, 0);
	long physicalOrigin = (long)GTK.gtk_adjustment_get_value (virtualHorizontalAdjustment);
	long fullX = window.viewportX (index) + physicalOrigin;
	logicalCell.x = virtualCoordinate (fullX + separator [0] / 2 + logicalCell.x);
	logicalCell.y = originalCell.y;
	logicalCell.height = originalCell.height;
	GdkRectangle logicalBackground = new GdkRectangle ();
	logicalBackground.x = logicalCell.x;
	logicalBackground.y = originalBackground.y;
	logicalBackground.width = logicalCell.width;
	logicalBackground.height = originalBackground.height;
	long cellPointer = OS.g_malloc (GdkRectangle.sizeof);
	long backgroundPointer = OS.g_malloc (GdkRectangle.sizeof);
	if (cellPointer == 0 || backgroundPointer == 0) {
		if (cellPointer != 0) OS.g_free (cellPointer);
		if (backgroundPointer != 0) OS.g_free (backgroundPointer);
		error (SWT.ERROR_NO_HANDLES);
	}
	Cairo.cairo_save (cr);
	try {
		long clippedX = window.residentViewportX () + (long)window.residentX (index) + physicalOrigin;
		Cairo.cairo_rectangle (cr, clippedX, originalBackground.y,
				window.residentColumnWidth (index), originalBackground.height);
		Cairo.cairo_clip (cr);
		C.memmove (cellPointer, new int [] {logicalCell.x, logicalCell.y, logicalCell.width, logicalCell.height}, GdkRectangle.sizeof);
		C.memmove (backgroundPointer, new int [] {logicalBackground.x, logicalBackground.y, logicalBackground.width, logicalBackground.height}, GdkRectangle.sizeof);
		virtualRendererRender (cell, cr, snapshot, widget, backgroundPointer, cellPointer, exposeArea, flags);
	} finally {
		Cairo.cairo_restore (cr);
		OS.g_free (backgroundPointer);
		OS.g_free (cellPointer);
	}
}

void virtualRendererRender (long cell, long cr, long snapshot, long widget, long background_area, long cell_area, long expose_area, long flags) {
	TableItem item = null;
	boolean wasSelected = false;
	long iter = OS.g_object_get_qdata (cell, Display.SWT_OBJECT_INDEX2);
	if (iter != 0) {
		int index = modelIndex (modelHandle, iter);
		if (index >= 0) item = _getItem (index);
	}
	long columnHandle = OS.g_object_get_qdata (cell, Display.SWT_OBJECT_INDEX1);
	int columnIndex = 0;
	if (columnCount > 0) {
		if (usesBoundedHorizontalView ()) {
			columnIndex = virtualColumnIndex (columnHandle);
			if (columnIndex < 0) return;
		} else {
			for (int i = 0; i < columnCount; i++) {
				if (columns [i].handle == columnHandle) {
					columnIndex = i;
					break;
				}
			}
		}
	}

	GdkRectangle rendererRect = new GdkRectangle ();
	GdkRectangle columnRect = new GdkRectangle ();
	int y_offset;
	double x_offset = usesBoundedHorizontalView () ? GTK.gtk_adjustment_get_value (virtualHorizontalAdjustment)
			* ((style & SWT.MIRRORED) != 0 ? -1 : 1) : 0;
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
		if (path != 0) GTK.gtk_tree_path_free (path);
		if (usesBoundedHorizontalView ()) {
			VirtualViewportPlanner.HorizontalWindow window = virtualHorizontalWindow ();
			int index = virtualColumnIndex (columnHandle);
			if (index >= 0) {
				columnRect.x = virtualCoordinate (window.viewportX (index)
						+ (long)GTK.gtk_adjustment_get_value (virtualHorizontalAdjustment));
				columnRect.width = (int)window.columnWidth (index);
			}
		}

		y_offset = columnRect.y - rendererRect.y;
		columnRect.y -= y_offset;
	}

	if (item != null) {
		if (GTK.GTK_IS_CELL_RENDERER_TOGGLE (cell) || (columnIndex != 0 || (style & SWT.CHECK) == 0)) {
			drawFlags = (int)flags;
			drawState = SWT.FOREGROUND;
			if (usesVirtualNativeModel ()) {
				Color cellBackground = item.virtualCellBackground != null
						&& columnIndex < item.virtualCellBackground.length
						? item.virtualCellBackground [columnIndex] : null;
				if (cellBackground != null || item.virtualBackground != null) {
					drawState |= SWT.BACKGROUND;
				}
			} else {
				long [] ptr = new long [1];
				GTK.gtk_tree_model_get (modelHandle, item.handle, Table.BACKGROUND_COLUMN, ptr, -1);
				if (ptr [0] == 0) {
					int modelIndex = columnCount == 0 ? Table.FIRST_COLUMN : columns [columnIndex].modelIndex;
					GTK.gtk_tree_model_get (modelHandle, item.handle, modelIndex + Table.CELL_BACKGROUND, ptr, -1);
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
					GdkRectangle r = new GdkRectangle();
					GDK.gdk_cairo_get_clip_rectangle(cr, r);
					gc.setClipping (virtualPaintClip (rect, columnIndex));
				} else {
					gc.setClipping (virtualPaintClip (rect, columnIndex));

				}

				// SWT.PaintItem/SWT.EraseItem often expect that event.y matches
				// what 'event.item.getBounds()' returns. The workaround is to
				// adjust coordinate system temporarily.
				Event event = new Event ();
				try {
					Rectangle eventRect = new Rectangle (rect.x, rect.y, rect.width, rect.height);

					eventRect.x = virtualCoordinate ((long)eventRect.x - (long)x_offset);
					eventRect.y += y_offset;
					Cairo.cairo_translate (cr, x_offset, -y_offset);

					item.pinVirtualFacade ();
					event.item = item;
					event.index = columnIndex;
					event.gc = gc;
					event.detail = drawState;
					event.setBounds (eventRect);
					sendEvent (SWT.EraseItem, event);
				} finally {
					Cairo.cairo_translate (cr, -x_offset, y_offset);
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
					Cairo.cairo_save (cr);
					if (!usesBoundedHorizontalView ()) Cairo.cairo_reset_clip (cr);
					long context = GTK.gtk_widget_get_style_context (widget);
					GTK.gtk_style_context_save (context);
					GTK.gtk_style_context_add_class (context, GTK.GTK_STYLE_CLASS_CELL);
					GTK.gtk_style_context_set_state (context, GTK.GTK_STATE_FLAG_SELECTED);
					GTK.gtk_render_background(context, cr, rect.x, rect.y, rect.width, rect.height);
					GTK.gtk_style_context_restore (context);
					Cairo.cairo_restore (cr);
				} else {
					if (wasSelected) {
						drawForegroundRGBA = gc.getForeground ().handle;
					}
				}
				gc.dispose();
				Cairo.cairo_restore(cr);
			}
		}
	}
	if ((drawState & SWT.BACKGROUND) != 0 && (drawState & SWT.SELECTED) == 0) {
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
			 * SWT.FOREGROUND means the Table is responsible for painting the default foreground
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
				virtualColumnPosition (columnHandle, textCell, contentX, null);
				ignoreSize = false;
				Image image = item.getImage (columnIndex);
				int imageWidth = 0;
				if (image != null && !image.isDisposed()) {
					imageWidth = image.getBounds ().width;
				}
				contentX [0] -= imageWidth;
				contentWidth [0] += imageWidth;
				GC gc = getGC(cr);
				if ((drawState & SWT.SELECTED) != 0) {
					Color background, foreground;
					if (GTK.gtk_widget_has_focus (handle)) {
						background = display.getSystemColor (SWT.COLOR_LIST_SELECTION);
						foreground = display.getSystemColor (SWT.COLOR_LIST_SELECTION_TEXT);
					} else {
						/*
						 * Feature in GTK. When the widget doesn't have focus, then
						 * gtk_paint_flat_box () changes the background color state_type
						 * to GTK_STATE_ACTIVE. The fix is to use the same values in the GC.
						 */
						background = Color.gtk_new (display, display.COLOR_LIST_SELECTION_INACTIVE_RGBA);
						foreground = Color.gtk_new (display, display.COLOR_LIST_SELECTION_TEXT_INACTIVE_RGBA);
					}
					gc.setBackground (background);
					gc.setForeground (foreground);
				} else {
					gc.setBackground (item.getBackground (columnIndex));
					Color foreground = drawForegroundRGBA != null ? Color.gtk_new (display, drawForegroundRGBA) : item.getForeground (columnIndex);
					gc.setForeground (foreground);
				}
				gc.setFont (item.getFont (columnIndex));
                if ((style & SWT.MIRRORED) != 0) {
                    rect.x = getClientWidth() - rect.width - rect.x;
                }

				gc.setClipping (virtualPaintClip (rect, columnIndex));

				// SWT.PaintItem/SWT.EraseItem often expect that event.y matches
				// what 'event.item.getBounds()' returns. The workaround is to
				// adjust coordinate system temporarily.
				Event event = new Event ();
				try {
					Rectangle eventRect = new Rectangle (rect.x + contentX [0], rect.y, contentWidth [0], rect.height);

					eventRect.x = virtualCoordinate ((long)eventRect.x - (long)x_offset);
					eventRect.y += y_offset;
					Cairo.cairo_translate (cr, x_offset, -y_offset);

					item.pinVirtualFacade ();
					event.item = item;
					event.index = columnIndex;
					event.gc = gc;
					event.detail = drawState;
					event.setBounds (eventRect);
					sendEvent (SWT.PaintItem, event);
				} finally {
					Cairo.cairo_translate (cr, -x_offset, y_offset);
				}

				gc.dispose();
			}
		}
	}
}

Rectangle virtualPaintClip (Rectangle bounds, int column) {
	if (!usesBoundedHorizontalView ()) return bounds;
	VirtualViewportPlanner.HorizontalWindow window = virtualHorizontalWindow ();
	int width = window.residentColumnWidth (column);
	long x = window.residentViewportX () + (long)window.residentX (column)
			+ (long)GTK.gtk_adjustment_get_value (virtualHorizontalAdjustment);
	if ((style & SWT.MIRRORED) != 0) x = getClientWidth () - width - x;
	long start = Math.max (x, bounds.x);
	long end = Math.min (x + width, bounds.x + (long)bounds.width);
	return new Rectangle (virtualCoordinate (start), bounds.y, (int)Math.max (0, end - start), bounds.height);
}

private GC getGC(long cr) {
	GC gc;
	GCData gcData = new GCData();
	gcData.cairo = cr;
	gc = GC.gtk_new(this, gcData );
	return gc;
}

void resetCustomDraw () {
    if ((style & SWT.VIRTUAL) != 0 || ownerDraw) {
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
	for (int i=0; i<materializedItemCount (); i++) {
		TableItem item = materializedItem (i);
        if (item != null) {
            item.reskin(flags);
        }
	}
	if (columns != null) {
		for (int i=0; i<columnCount; i++) {
			TableColumn column = columns [i];
            if (!column.isDisposed()) {
                column.reskin(flags);
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
 * Selects the item at the given zero-relative index in the receiver.
 * If the item at the index was already selected, it remains
 * selected. Indices that are out of range are ignored.
 *
 * @param index the index of the item to select
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void select (int index) {
	checkWidget();
	if (usesBoundedVirtualView ()) {
		if (index >= 0 && index < itemCount) {
			if ((style & SWT.SINGLE) != 0) virtualViewport.selection ().clear ();
			virtualViewport.selection ().setSelected (index, true);
			restoreVirtualSelection ();
		}
		return;
	}
    if (!(0 <= index && index < itemCount)) {
        return;
    }
	boolean fixColumn = showFirstColumn ();
	long selection = GTK.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	if (usesVirtualNativeModel ()) {
		long iter = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
        if (iter == 0) {
            error(SWT.ERROR_NO_HANDLES);
        }
        if (GTK.gtk_tree_model_iter_nth_child(modelHandle, iter, 0, index)) {
            GTK.gtk_tree_selection_select_iter(selection, iter);
        }
		OS.g_free (iter);
	} else {
		GTK.gtk_tree_selection_select_iter (selection, _getItem (index).handle);
	}
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
    if (fixColumn) {
        hideFirstColumn();
    }
}

/**
 * Selects the items in the range specified by the given zero-relative
 * indices in the receiver. The range of indices is inclusive.
 * The current selection is not cleared before the new items are selected.
 * <p>
 * If an item in the given range is not selected, it is selected.
 * If an item in the given range was already selected, it remains selected.
 * Indices that are out of range are ignored and no items will be selected
 * if start is greater than end.
 * If the receiver is single-select and there is more than one item in the
 * given range, then all indices are ignored.
 * </p>
 *
 * @param start the start of the range
 * @param end the end of the range
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Table#setSelection(int,int)
 */
public void select (int start, int end) {
	checkWidget ();
	if (usesBoundedVirtualView ()) {
		if (end < 0 || start > end || ((style & SWT.SINGLE) != 0 && start != end)) return;
		int first = Math.max (0, start), last = (int)Math.min (itemCount, (long)end + 1);
		if (first < last) {
			if ((style & SWT.SINGLE) != 0) virtualViewport.selection ().clear ();
			virtualViewport.selection ().selectRange (first, last);
			restoreVirtualSelection ();
		}
		return;
	}
    if (end < 0 || start > end || ((style & SWT.SINGLE) != 0 && start != end)) {
        return;
    }
    if (itemCount == 0 || start >= itemCount) {
        return;
    }
	start = Math.max (0, start);
	end = Math.min (end, itemCount - 1);
	boolean fixColumn = showFirstColumn ();
	long selection = GTK.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	long iter = usesVirtualNativeModel () ? OS.g_malloc (GTK.GtkTreeIter_sizeof ()) : 0;
    if (usesVirtualNativeModel() && iter == 0) {
        error(SWT.ERROR_NO_HANDLES);
    }
	try {
		for (int index=start; index<=end; index++) {
			if (usesVirtualNativeModel ()) {
                if (GTK.gtk_tree_model_iter_nth_child(modelHandle, iter, 0, index)) {
                    GTK.gtk_tree_selection_select_iter(selection, iter);
                }
			} else {
				GTK.gtk_tree_selection_select_iter (selection, _getItem (index).handle);
			}
		}
	} finally {
        if (iter != 0) {
            OS.g_free(iter);
        }
	}
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
    if (fixColumn) {
        hideFirstColumn();
    }
}

/**
 * Selects the items at the given zero-relative indices in the receiver.
 * The current selection is not cleared before the new items are selected.
 * <p>
 * If the item at a given index is not selected, it is selected.
 * If the item at a given index was already selected, it remains selected.
 * Indices that are out of range and duplicate indices are ignored.
 * If the receiver is single-select and multiple indices are specified,
 * then all indices are ignored.
 * </p>
 *
 * @param indices the array of indices for the items to select
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the array of indices is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Table#setSelection(int[])
 */
public void select (int [] indices) {
	checkWidget ();
	if (usesBoundedVirtualView ()) {
		if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
		if ((style & SWT.SINGLE) != 0 && indices.length > 1) return;
		for (int index : indices) {
			if (index < 0 || index >= itemCount) continue;
			if ((style & SWT.SINGLE) != 0) virtualViewport.selection ().clear ();
			virtualViewport.selection ().setSelected (index, true);
		}
		restoreVirtualSelection ();
		return;
	}
    if (indices == null) {
        error(SWT.ERROR_NULL_ARGUMENT);
    }
	int length = indices.length;
    if (length == 0 || ((style & SWT.SINGLE) != 0 && length > 1)) {
        return;
    }
	boolean fixColumn = showFirstColumn ();
	long selection = GTK.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	long iter = usesVirtualNativeModel () ? OS.g_malloc (GTK.GtkTreeIter_sizeof ()) : 0;
    if (usesVirtualNativeModel() && iter == 0) {
        error(SWT.ERROR_NO_HANDLES);
    }
	try {
		for (int index : indices) {
            if (!(0 <= index && index < itemCount)) {
                continue;
            }
			if (usesVirtualNativeModel ()) {
                if (GTK.gtk_tree_model_iter_nth_child(modelHandle, iter, 0, index)) {
                    GTK.gtk_tree_selection_select_iter(selection, iter);
                }
			} else {
				GTK.gtk_tree_selection_select_iter (selection, _getItem (index).handle);
			}
		}
	} finally {
        if (iter != 0) {
            OS.g_free(iter);
        }
	}
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
		if ((style & SWT.SINGLE) != 0) return;
		virtualViewport.selection ().selectAll ();
		restoreVirtualSelection ();
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

void selectFocusIndex (int index) {
	if (usesBoundedVirtualView ()) {
		if (index < 0 || index >= itemCount) return;
		virtualViewport.selection ().clear ();
		virtualViewport.selection ().setSelected (index, true);
		virtualFocusIndex = virtualAnchorIndex = index;
		restoreVirtualSelection ();
		return;
	}
    /*
     * Note that this method both selects and sets the focus to the specified
     * index, so any previous selection in the list will be lost.
     */
    if (!(0 <= index && index < itemCount)) {
        return;
    }
	long iter = 0;
	long itemHandle;
	if (usesVirtualNativeModel ()) {
		iter = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
        if (iter == 0) {
            error(SWT.ERROR_NO_HANDLES);
        }
		if (!GTK.gtk_tree_model_iter_nth_child (modelHandle, iter, 0, index)) {
			OS.g_free (iter);
			return;
		}
		itemHandle = iter;
	} else {
		itemHandle = _getItem (index).handle;
	}
	long path = GTK.gtk_tree_model_get_path (modelHandle, itemHandle);
	long selection = GTK.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	GTK.gtk_tree_view_set_cursor (handle, path, 0, false);
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	GTK.gtk_tree_path_free (path);
    if (iter != 0) {
        OS.g_free(iter);
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
	String css = "treeview {background-color: " + display.gtk_rgba_to_css_string(background) + ";}\n"+
			 "treeview:selected {background-color: " + display.gtk_rgba_to_css_string(selectedBackground) + ";}";

	// Cache background color
	cssBackground = css;

	// Apply background color and any foreground color
	String finalCss = display.gtk_css_create_css_color_string (cssBackground, cssForeground, SWT.BACKGROUND);
	gtk_css_provider_load_from_css(context, finalCss);
}

@Override
void setBackgroundSurface (Image image) {
	ownerDraw = true;
	recreateRenderers ();
}

@Override
int setBounds (int x, int y, int width, int height, boolean move, boolean resize) {
	int result = super.setBounds (x, y, width, height, move, resize);
	if (usesBoundedVirtualView ()) scheduleVirtualView ();
	if (result != 0) {
		boundsChangedSinceLastDraw = true;
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
 * @see Table#getColumnOrder()
 * @see TableColumn#getMoveable()
 * @see TableColumn#setMoveable(boolean)
 * @see SWT#Move
 *
 * @since 3.1
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
	for (int i=0; i<order.length; i++) {
		long column = columns [order [i]].handle;
		long baseColumn = i == 0 ? 0 : columns [order [i-1]].handle;
		GTK.gtk_tree_view_move_column_after (handle, column, baseColumn);
	}
	virtualColumnsChanged ();
}

@Override
void setFontDescription (long font) {
	super.setFontDescription (font);
	TableColumn[] columns = getColumns ();
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
 * will cause the header to look FLAT irrespective of the state of the table style.
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
		for (TableColumn column : columns) {
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
 * will cause the header to look FLAT irrespective of the state of the table style.
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
 */
public void setHeaderVisible (boolean show) {
	checkWidget ();
	GTK.gtk_tree_view_set_headers_visible (handle, show);
	this.headerHeight = this.getHeaderHeight();
	this.headerVisible = show;
}

/**
 * Sets the number of items contained in the receiver.
 *
 * @param count the number of items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.0
 */
public void setItemCount (int count) {
	checkWidget ();
	count = Math.max (0, count);
    if (count == itemCount) {
        return;
    }
	boolean isVirtual = (style & SWT.VIRTUAL) != 0;
	if (usesVirtualNativeModel ()) {
		int oldCount = itemCount;
		if (count < oldCount) {
			virtualItems.truncate (count, item -> {
                if (!item.isDisposed()) {
                    item.release(false);
                }
			});
			resetVirtualNativeModel (count, count, oldCount - count, 0);
		} else {
			resetVirtualNativeModel (count, oldCount, 0, count - oldCount);
		}
		return;
	}
    if (!isVirtual) {
        setRedraw(false);
    }
    if (count < itemCount) {
        remove(count, itemCount - 1);
    }
	if (isVirtual) {
		if (count > itemCount) {
			long iter = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
            if (iter == 0) {
                error(SWT.ERROR_NO_HANDLES);
            }
            for (int i = itemCount; i < count; i++) {
                GTK.gtk_list_store_append(modelHandle, iter);
            }
			OS.g_free (iter);
			itemCount = count;
		}
	} else {
		if (count > itemCount) {
			int length = Math.max (4, (count + 3) / 4 * 4);
			TableItem [] newItems = new TableItem [length];
			System.arraycopy (items, 0, newItems, 0, itemCount);
			items = newItems;
            for (int i = itemCount; i < count; i++) {
                new TableItem(this, SWT.NONE, i, true);
            }
		} else {
			int length = Math.max (4, (count + 3) / 4 * 4);
			TableItem [] newItems = new TableItem [length];
			System.arraycopy (items, 0, newItems, 0, itemCount);
			items = newItems;
		}
	}
    if (!isVirtual) {
        setRedraw(true);
    }
}

/**
 * Marks the receiver's lines as visible if the argument is <code>true</code>,
 * and marks it invisible otherwise. Note that some platforms draw grid lines
 * while others may draw alternating row colors.
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
 */
public void setLinesVisible (boolean show) {
	checkWidget();
	//Note: this is overridden by the active theme in GTK3.
	GTK.gtk_tree_view_set_grid_lines (handle, show ? GTK.GTK_TREE_VIEW_GRID_LINES_VERTICAL : GTK.GTK_TREE_VIEW_GRID_LINES_NONE);
}

void setModel (long newModel) {
	display.removeWidget (modelHandle);
	OS.g_object_unref (modelHandle);
	modelHandle = newModel;
	display.addWidget (modelHandle, this);
}

@Override
void setOrientation (boolean create) {
	super.setOrientation (create);
	for (int i=0; i<materializedItemCount (); i++) {
		TableItem item = materializedItem (i);
        if (item != null) {
            item.setOrientation(create);
        }
	}
	for (int i=0; i<columnCount; i++) {
        if (columns[i] != null) {
            columns[i].setOrientation(create);
        }
	}
}

@Override
void setParentBackground () {
	ownerDraw = true;
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

@Override
public void setRedraw (boolean redraw) {
	checkWidget();
	super.setRedraw (redraw);
	if (redraw && drawCount == 0) {
		/* Resize the item array to match the item count */
		if (items.length > 4 && items.length - itemCount > 3) {
			int length = Math.max (4, (itemCount + 3) / 4 * 4);
			TableItem [] newItems = new TableItem [length];
			System.arraycopy (items, 0, newItems, 0, itemCount);
			items = newItems;
		}
	}
}

void setScrollWidth (long column, TableItem item) {
    if (columnCount != 0 || currentItem == item) {
        return;
    }
	int itemWidth = calculateWidth (column, item.handle);
	if (usesBoundedHorizontalView ()) {
		if (maxWidth < itemWidth) { maxWidth = itemWidth; virtualColumnsChanged (); }
		return;
	}
	int width = GTK.gtk_tree_view_column_get_fixed_width (column);
	if (width < itemWidth) {
		GTK.gtk_tree_view_column_set_fixed_width (column, itemWidth);
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
public void setSortColumn (TableColumn column) {
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
 * Selects the item at the given zero-relative index in the receiver.
 * The current selection is first cleared, then the new item is selected,
 * and if necessary the receiver is scrolled to make the new selection visible.
 *
 * @param index the index of the item to select
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Table#deselectAll()
 * @see Table#select(int)
 */
public void setSelection (int index) {
	checkWidget ();
	boolean fixColumn = showFirstColumn ();
	deselectAll ();
	selectFocusIndex (index);
	showSelection ();
    if (fixColumn) {
        hideFirstColumn();
    }
}

/**
 * Selects the items in the range specified by the given zero-relative
 * indices in the receiver. The range of indices is inclusive.
 * The current selection is cleared before the new items are selected,
 * and if necessary the receiver is scrolled to make the new selection visible.
 * <p>
 * Indices that are out of range are ignored and no items will be selected
 * if start is greater than end.
 * If the receiver is single-select and there is more than one item in the
 * given range, then all indices are ignored.
 * </p>
 *
 * @param start the start index of the items to select
 * @param end the end index of the items to select
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Table#deselectAll()
 * @see Table#select(int,int)
 */
public void setSelection (int start, int end) {
	checkWidget ();
	deselectAll();
    if (end < 0 || start > end || ((style & SWT.SINGLE) != 0 && start != end)) {
        return;
    }
    if (itemCount == 0 || start >= itemCount) {
        return;
    }
	boolean fixColumn = showFirstColumn ();
	start = Math.max (0, start);
	end = Math.min (end, itemCount - 1);
	selectFocusIndex (start);
	if ((style & SWT.MULTI) != 0) {
		select (start, end);
	}
	showSelection ();
    if (fixColumn) {
        hideFirstColumn();
    }
}

/**
 * Selects the items at the given zero-relative indices in the receiver.
 * The current selection is cleared before the new items are selected,
 * and if necessary the receiver is scrolled to make the new selection visible.
 * <p>
 * Indices that are out of range and duplicate indices are ignored.
 * If the receiver is single-select and multiple indices are specified,
 * then all indices are ignored.
 * </p>
 *
 * @param indices the indices of the items to select
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the array of indices is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Table#deselectAll()
 * @see Table#select(int[])
 */
public void setSelection (int [] indices) {
	checkWidget ();
    if (indices == null) {
        error(SWT.ERROR_NULL_ARGUMENT);
    }
	deselectAll ();
	int length = indices.length;
    if (length == 0 || ((style & SWT.SINGLE) != 0 && length > 1)) {
        return;
    }
	boolean fixColumn = showFirstColumn ();
	selectFocusIndex (indices [0]);
	if ((style & SWT.MULTI) != 0) {
		select (indices);
	}
	showSelection ();
    if (fixColumn) {
        hideFirstColumn();
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
public void setSelection (TableItem item) {
    if (item == null) {
        error(SWT.ERROR_NULL_ARGUMENT);
    }
	setSelection (new TableItem [] {item});
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
 * @see Table#deselectAll()
 * @see Table#select(int[])
 * @see Table#setSelection(int[])
 */
public void setSelection (TableItem [] items) {
	checkWidget ();
    if (items == null) {
        error(SWT.ERROR_NULL_ARGUMENT);
    }
	boolean fixColumn = showFirstColumn ();
	deselectAll ();
	int length = items.length;
	if (!(length == 0 || ((style & SWT.SINGLE) != 0 && length > 1))) {
		boolean first = true;
		for (int i = 0; i < length; i++) {
			int index = indexOf (items [i]);
			if (index != -1) {
				if (first) {
					first = false;
					selectFocusIndex (index);
				} else {
					select (index);
				}
			}
		}
		showSelection ();
	}
    if (fixColumn) {
        hideFirstColumn();
    }
}

/**
 * Sets the zero-relative index of the item which is currently
 * at the top of the receiver. This index can change when items
 * are scrolled or new items are added and removed.
 *
 * @param index the index of the top item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setTopIndex (int index) {
	checkWidget();
	if (usesBoundedVirtualView ()) {
		if (index >= 0 && index < itemCount) revealVirtualIndex (index, true);
		return;
	}
    if (!(0 <= index && index < itemCount)) {
        return;
    }
	long vAdjustment = GTK.gtk_scrollable_get_vadjustment(handle);
	cachedAdjustment = GTK.gtk_adjustment_get_value(vAdjustment);
	topIndex = index;
	pendingTopIndex = index;
	long iter = 0;
	long itemHandle;
	if (usesVirtualNativeModel ()) {
		iter = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
        if (iter == 0) {
            error(SWT.ERROR_NO_HANDLES);
        }
		if (!GTK.gtk_tree_model_iter_nth_child (modelHandle, iter, 0, index)) {
			OS.g_free (iter);
			return;
		}
		itemHandle = iter;
	} else {
		itemHandle = _getItem (index).handle;
	}
	long path = GTK.gtk_tree_model_get_path (modelHandle, itemHandle);
	GTK.gtk_tree_view_scroll_to_cell (handle, path, 0, true, 0f, 0f);
	GTK.gtk_tree_path_free (path);
    if (iter != 0) {
        OS.g_free(iter);
    }
}

/**
 * Shows the column.  If the column is already showing in the receiver,
 * this method simply returns.  Otherwise, the columns are scrolled until
 * the column is visible.
 *
 * @param column the column to be shown
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the column is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the column has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.0
 */
public void showColumn (TableColumn column) {
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

	if (usesBoundedHorizontalView ()) {
		virtualShowColumn (indexOf (column));
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
 * Shows the item.  If the item is already showing in the receiver,
 * this method simply returns.  Otherwise, the items are scrolled until
 * the item is visible.
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
 * @see Table#showSelection()
 */
public void showItem (TableItem item) {
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
	showItem (item.handle);
}

void showItem (long iter) {
	if (usesBoundedVirtualView ()) {
		revealVirtualIndex (modelIndex (modelHandle, iter), false);
		return;
	}
	long path = GTK.gtk_tree_model_get_path (modelHandle, iter);

	GTK.gtk_tree_view_scroll_to_cell (handle, path, 0, false, 0, 0);
	GTK.gtk_tree_path_free (path);
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
 * @see Table#showItem(TableItem)
 */
public void showSelection () {
	checkWidget();
	if (usesBoundedVirtualView ()) {
		revealVirtualIndex (firstVirtualSelectionIndex (), false);
		return;
	}
	if (usesVirtualNativeModel ()) {
		int [] selection = getSelectionIndices ();
        if (selection.length == 0) {
            return;
        }
		long iter = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
        if (iter == 0) {
            error(SWT.ERROR_NO_HANDLES);
        }
		if (GTK.gtk_tree_model_iter_nth_child (modelHandle, iter, 0, selection [0])) {
			showItem (iter);
		}
		OS.g_free (iter);
		return;
	}
	TableItem [] selection = getSelection ();
    if (selection.length == 0) {
        return;
    }
	showItem (selection [0].handle);
}


@Override
void updateScrollBarValue (ScrollBar bar) {
	super.updateScrollBarValue (bar);

	if (!GTK.GTK4) {
		/*
		*  Bug in GTK. Scrolling changes the XWindow position
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
			 * If this Table has any child widgets, propagate the draw signal
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
			if (itemCount == 0 && (state & OBSCURED) == 0 && !GTK.GTK4) {
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
	 * In the error case, the SWT fixed which contains the table still resizes as expected,
	 * and the horizontal scrollbar is only partially visible so that it doesn't overlap with table headers.
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
 * Check the table item range [start, end) for items that are in process of
 * sending {@code SWT#SetData} event. If such items exist, throw an exception.
 *
 * Does nothing if the given range contains no indices.
 *
 * @param start index of first item to check
 * @param end index after the last item to check
 */
void checkSetDataInProcessBeforeRemoval(int start, int end) {
	if ((style & SWT.VIRTUAL) != 0) {
		virtualItems.forEachIndexed ((index, item) -> {
			if (start <= index && index < end && item.settingData) {
				String message = "Cannot remove a table item while its data is being set. "
						+ "At item " + index + " in range [" + start + ", " + end + ").";
				throw new SWTException(message);
			}
		});
		return;
	}
	for (int i = start; i < end; i++) {
		TableItem item = items[i];
		if (item != null && item.settingData) {
			String message = "Cannot remove a table item while its data is being set. "
					+ "At item " + i + " in range [" + start + ", " + end + ").";
			throw new SWTException(message);
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
	// Table renders via native GTK children (GtkScrolledWindow > GtkTreeView).
	// Like GTK3 where Table explicitly fires paint in EXPOSE_EVENT (after=true),
	// GTK4 must paint after children so SWT.Paint overlays appear on top.
}

@Override
void snapshotToDrawAfterChildren(long handle, long snapshot) {
	snapshotPaint(handle, snapshot);
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
