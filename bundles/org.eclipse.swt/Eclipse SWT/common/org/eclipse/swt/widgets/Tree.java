/*******************************************************************************
 * Copyright (c) 2000, 2022 IBM Corporation and others.
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
 *     Roland Oldenburg <r.oldenburg@hsp-software.de> - Bug 292199
 *     Conrad Groth - Bug 384906
 *******************************************************************************/
package org.eclipse.swt.widgets;

import java.util.*;
import java.util.List;

import org.eclipse.swt.*;
import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;

/**
 * Instances of this class implement a selecTree user interface object that
 * displays a list of images and strings and issues notification when selected.
 * <p>
 * The item children that may be added to instances of this class must be of
 * type <code>TreeItem</code>.
 * </p>
 * <p>
 * Style <code>VIRTUAL</code> is used to create a <code>Tree</code> whose
 * <code>TreeItem</code>s are to be populated by the client on an on-demand
 * basis instead of up-front. This can provide significant performance
 * improvements for Trees that are very large or for which <code>TreeItem</code>
 * population is expensive (for example, retrieving values from an external
 * source).
 * </p>
 * <p>
 * Here is an example of using a <code>Tree</code> with style
 * <code>VIRTUAL</code>:
 * </p>
 *
 * <pre>
 * <code>
 *  final Tree Tree = new Tree (parent, SWT.VIRTUAL | SWT.BORDER);
 *  Tree.setItemCount (1000000);
 *  Tree.addListener (SWT.SetData, new Listener () {
 *      public void handleEvent (Event event) {
 *          TreeItem item = (TreeItem) event.item;
 *          int index = Tree.indexOf (item);
 *          item.setText ("Item " + index);
 *          System.out.println (item.getText ());
 *      }
 *  });
 * </code>
 * </pre>
 * <p>
 * Note that although this class is a subclass of <code>Composite</code>, it
 * does not normally make sense to add <code>Control</code> children to it, or
 * set a layout on it, unless implementing something like a cell editor.
 * </p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SINGLE, MULTI, CHECK, FULL_SELECTION, HIDE_SELECTION, VIRTUAL,
 * NO_SCROLL</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection, DefaultSelection, SetData, MeasureItem, EraseItem,
 * PaintItem</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles SINGLE, and MULTI may be specified.
 * </p>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#Tree">Tree, TreeItem,
 *      TreeColumn snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example:
 *      ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Tree extends CustomComposite {

	// ------------------------------------------------------------

	static final boolean USE_CACHES = true; // default true

	static final boolean FILL_AREAS = false; // complete columns and items area:
												// default false
	static final boolean FILL_TEXT_AREAS = false; // fill areas where texts will
													// be written: default false
	static final boolean FILL_IMAGE_AREAS = false; // fill areas where images
													// will be drawn: default
													// false

	static final boolean DRAW_IMAGES = true; // draw the images of Tree items:
												// default true
	static final boolean DRAW_TEXTS = true; // draw the texts of Tree
											// items/colums: default true

	private static final boolean LOG_NOT_IMPLEMENTED = false; // write to console, if
														// method calls are not
														// implemented: default
														// false

	// ------------------------------------------------------------

	private static final int DRAW_FLAGS = SWT.DRAW_MNEMONIC | SWT.DRAW_TAB
			| SWT.DRAW_TRANSPARENT | SWT.DRAW_DELIMITER;

	// TODO this should be used with: Tree.setLinesVisible
	static final int Tree_GRID_LINE_SIZE = 1;

	private final java.util.List<TreeItem> itemsList = new ArrayList<>();
	private final TreeMap<Integer, TreeItem> virtualItemsList = new TreeMap<>();
	TreeSet<TreeItem> selectedTreeItems = new TreeSet<>((o1, o2) ->
			o1 == o2 ? 0 : Arrays.compare(logicalSelectionPath(o1), logicalSelectionPath(o2)));

	private int[] logicalSelectionPath(TreeItem item) {
		int depth = 1;
		for (TreeItem ancestor = item.getParentItem();
				ancestor != null; ancestor = ancestor.getParentItem()) {
			depth++;
		}
		int [] indices = new int [depth];
		TreeItem current = item;
		for (int position = depth - 1; position >= 0; position--) {
			TreeItem parentItem = current.getParentItem();
			indices [position] = parentItem == null ? indexOf(current) : parentItem.indexOf(current);
			current = parentItem;
		}
		return indices;
	}

	void removeSelectionSubtree(TreeItem root) {
		if (root == null) return;
		selectedTreeItems.removeIf(item -> {
			for (TreeItem ancestor = item; ancestor != null; ancestor = ancestor.getParentItem()) {
				if (ancestor == root) return true;
			}
			return false;
		});
	}
	// TODO implement focusHandling
	private TreeItem focusItem;
	Item mouseHoverElement;
	List<TreeItem> treeItemsArrangement = new ArrayList<>();
	private final java.util.List<TreeColumn> columnsList = new ArrayList<>();

	private final TreeColumnsHandler columnsHandler = new TreeColumnsHandler(this);
	private final TreeItemsHandler itemsHandler = new TreeItemsHandler(this);

	TreeItem currentItem;
	TreeColumn sortColumn;
	long headerToolTipHandle;
	boolean customDraw;
	int itemHeight;
	int sortDirection;
	private static final int GRID_WIDTH = 1;

	private Accessible acc;
	private long topIndex;

	private boolean headerVisible;

	private int[] columnOrder;

	private final TreeRenderer renderer;

	private Color headerBackgroundColor;

	// TODO implement
	private boolean linesVisible;

	private Color headerForegroundColor;

	private boolean ctrlPressed;
	private boolean hasMouseEntered;

	private int virtualItemCount;

	private TreeItem topItem;

	/**
	 * Constructs a new instance of this class given its parent and a style
	 * value describing its behavior and appearance.
	 * <p>
	 * The style value is either one of the style constants defined in class
	 * <code>SWT</code> which is applicable to instances of this class, or must
	 * be built by <em>bitwise OR</em>'ing together (that is, using the
	 * <code>int</code> "|" operator) two or more of those <code>SWT</code>
	 * style constants. The class description lists the style constants that are
	 * applicable to the class. Style bits are also inherited from superclasses.
	 * </p>
	 *
	 * @param parent
	 *            a composite control which will be the parent of the new
	 *            instance (cannot be null)
	 * @param style
	 *            the style of control to construct
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the parent</li>
	 *                <li>ERROR_INVALID_SUBCLASS - if this class is not an
	 *                allowed subclass</li>
	 *                </ul>
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
	public Tree(Composite parent, int style) {
		super(parent, checkStyle(style));

		renderer = new DefaultTreeRenderer(this);

		initialize();
	}

	private void initialize() {
		final Listener listener = event -> {
			switch (event.type) {
				case SWT.Dispose -> onDispose(event);
				case SWT.MouseDown -> onMouseDown(event);
				case SWT.MouseUp -> onMouseUp(event);
				case SWT.Paint -> onPaint(event);
				case SWT.Resize -> onResize();
				case SWT.ZoomChanged -> onZoomChange(event);
				case SWT.FocusIn -> onFocusIn();
				case SWT.FocusOut -> onFocusOut();
				case SWT.Traverse -> onTraverse(event);
				case SWT.Selection -> onSelection(event);
				case SWT.V_SCROLL -> onScrollBar(event);
				case SWT.H_SCROLL -> onScrollBar(event);
				case SWT.MouseWheel -> onScrollBar(event);
				case SWT.KeyDown -> onKeyDown(event);
				case SWT.KeyUp -> onKeyUp(event);
				case SWT.MouseMove -> onMouseMove(event);
				case SWT.MouseEnter -> onMouseEnter(event);
				case SWT.MouseExit -> onMouseExit(event);
				case SWT.MouseDoubleClick -> onDoubleClick(event);
			}
		};

		addListener(SWT.KeyDown, listener);
		addListener(SWT.KeyUp, listener);
		addListener(SWT.MouseMove, listener);
		addListener(SWT.Dispose, listener);
		addListener(SWT.MouseDown, listener);
		addListener(SWT.MouseUp, listener);
		addListener(SWT.Paint, listener);
		addListener(SWT.Resize, listener);
		addListener(SWT.KeyDown, listener);
		addListener(SWT.FocusIn, listener);
		addListener(SWT.FocusOut, listener);
		addListener(SWT.Traverse, listener);
		addListener(SWT.V_SCROLL, listener);
		addListener(SWT.H_SCROLL, listener);
		addListener(SWT.SCROLL_LINE, listener);
		addListener(SWT.MouseWheel, listener);
		addListener(SWT.MouseDoubleClick, listener);
		addListener(SWT.ZoomChanged, listener);

		if (verticalBar != null) {
			verticalBar.addListener(SWT.Selection, listener);
		}
		if (horizontalBar != null) {
			horizontalBar.addListener(SWT.Selection, listener);
		}

		initializeAccessible();
	}

	private void onDoubleClick(Event event) {
		itemsHandler.handleDoubleClick(event);
	}

	private void onMouseExit(Event event) {
		hasMouseEntered = false;
		redraw();
	}

	private void onMouseEnter(Event event) {
		if (!hasMouseEntered) {
			hasMouseEntered = true;
			redraw();
		}
	}

	private void onMouseMove(Event event) {
		columnsHandler.handleMouseMove(event);
		itemsHandler.handleMouseMove(event);
	}

	private void onScrollBar(Event event) {
		// Horizontal item and header coordinates share the existing scrollbar.
		// Do not walk or materialize offscreen TreeItems to invalidate geometry.
		columnsHandler.clearCache();
        ScrollBar bar = getVerticalBar();
        if (bar != null && !bar.isDisposed()) {
            if (isVirtual() && virtualScrollMetrics != null) {
                int selection = bar.getSelection();
                int maxSelection = Math.max(0, virtualScrollMetrics.maximum() - virtualScrollMetrics.thumb());
                virtualEndAffinity = virtualScrollMetrics.logicalRowCount() != 0
                        && (event.detail == SWT.END || (maxSelection > 0 && selection == maxSelection
                                && event.detail != SWT.HOME && event.detail != SWT.ARROW_UP
                                && event.detail != SWT.PAGE_UP));
                applyLogicalTopIndex(virtualScrollMetrics.topRowForSelection(selection));
            } else setTopIndex(bar.getSelection());
        }
        redraw();
    }

	void initializeAccessible() {
		acc = getAccessible();

		// TODO implement accessibility
		AccessibleAdapter accAdapter = new AccessibleAdapter() {
			@Override
			public void getName(AccessibleEvent e) {
				// TODO implement accessibility
				e.result = "text";
			}

			@Override
			public void getHelp(AccessibleEvent e) {
				e.result = getToolTipText();
			}

			@Override
			public void getKeyboardShortcut(AccessibleEvent e) {
			}
		};
		acc.addAccessibleListener(accAdapter);
		addListener(SWT.FocusIn, event -> acc.setFocus(ACC.CHILDID_SELF));
	}

	private void onSelection(Event event) {
		if (event.widget == verticalBar || event.widget == horizontalBar) {
			onScrollBar(event);
			return;
		}
		redraw();
	}

	private void onTraverse(Event event) {
	}

	private void onFocusIn() {
		redraw();
	}

	private void onFocusOut() {
		redraw();
	}

	private void onKeyDown(Event event) {
		// TODO implement all keyboard keys
		if (event.keyCode == SWT.CTRL) {
			this.ctrlPressed = true;
		}
	}

	private void onKeyUp(Event event) {
		if (event.keyCode == SWT.CTRL) {
			this.ctrlPressed = false;
		}
	}

	private void onResize() {
		updateScrollBarWithTextSize();
		redraw();
	}

	void updateScrollBarWithTextSize() {
        if (!isVisible()) return;
        ScrollBar vBar = verticalBar;
        if (vBar != null && !vBar.isDisposed()) vBar.setVisible(getItemCount() > 0);
        if (isDisposed()) return;
        ScrollBar hBar = horizontalBar;
        if (hBar != null && !hBar.isDisposed()) {
            Rectangle ca = getClientArea();
            Point treeSize = computeDefaultSize();
            if (isDisposed()) return;
            if (!hBar.isDisposed() && horizontalBar == hBar) {
                hBar.setVisible(treeSize.x > ca.width);
                if (isDisposed()) return;
                if (!hBar.isDisposed() && horizontalBar == hBar) {
                    ca = getClientArea();
                    int maximum = (int) Math.max(1L, Math.min(Integer.MAX_VALUE,
                            Math.max((long) getTotalColumnWidth(), (long) treeSize.x) + 10L));
                    int thumb = Math.min(maximum, Math.min(Integer.MAX_VALUE - 1,
                            Math.max(1, ca.width)));
                    hBar.setValues(hBar.getSelection(), 0, maximum, thumb,
                            1, Math.max(1, ca.width));
                }
            }
        }
        if (isDisposed()) return;
        vBar = verticalBar;
        if (vBar == null || vBar.isDisposed()) return;
        if (isVirtual()) {
            if (virtualScrollMetrics == null) virtualScrollMetrics = new VirtualScrollMetrics();
            VirtualRowExtents extents = virtualExtentFacts();
            if (isDisposed() || vBar.isDisposed() || verticalBar != vBar) return;
            Rectangle body = itemsHandler.getItemsClientArea();
            int pageIncrement = calculateThumb(body);
            if (isDisposed() || vBar.isDisposed() || verticalBar != vBar) return;
            long count = extents.rowCount();
            long lastTop = virtualLastTopRow(extents, body.height);
            // Positive receiving row extents bound this row page by the int body.
            int visibleRows = Math.toIntExact(count == 0 ? 0L : count - lastTop);
            virtualScrollMetrics.configure(count, 1, visibleRows);
            applyLogicalTopIndex(virtualEndAffinity ? lastTop : virtualScrollMetrics.clampTopRow(topIndex));
            int maximum = Math.max(1, virtualScrollMetrics.maximum());
            int thumb = Math.max(1, virtualScrollMetrics.thumb());
            pageIncrement = Math.min(maximum, pageIncrement);
            if (count > Integer.MAX_VALUE) {
                // Native page increments use the same projected units as selections.
                pageIncrement = virtualScrollMetrics.selectionForTopRow(pageIncrement);
            }
            if (isDisposed() || vBar.isDisposed() || verticalBar != vBar) return;
            vBar.setValues(virtualScrollMetrics.selectionForTopRow(topIndex), 0,
                    maximum, thumb, 1, Math.max(1, pageIncrement));
            return;
        }
        int count = Math.max(0, getOpenedItemCount());
        int maximum = Math.max(1, count);
        int thumb = Math.min(maximum, calculateThumb(itemsHandler.getItemsClientArea()));
        if (isDisposed() || vBar.isDisposed() || verticalBar != vBar) return;
        int selection = (int) Math.max(0L, Math.min(topIndex, (long) maximum - thumb));
        vBar.setValues(selection, 0, maximum, thumb, 1, thumb);
        if (isDisposed() || vBar.isDisposed() || verticalBar != vBar) return;
        setLogicalTopIndex(vBar.getSelection());
    }

	private int calculateThumb(Rectangle ca) {
        int rowHeight = Math.max(1, getItemHeight());
        int gridSize = TreeItemsHandler.getGridSize(this);
        return (int) Math.max(1L, Math.max(0L, (long) ca.height)
                / (rowHeight + (long) gridSize));
    }

	private int getColumnHeight() {
		return columnsHandler.getSize().y;
	}

	private void onDispose(Event event) {
		Set<TreeColumn> columnsSet = new HashSet<>(columnsList);
		Set<TreeItem> itemsSet = new HashSet<>();
		itemsSet.addAll(itemsList);
		itemsSet.addAll(virtualItemsList.values());

		itemsList.clear();
		columnsList.clear();
		virtualItemsList.clear();
		virtualItemCount = 0;
        invalidateVirtualTopology();

		for (TreeColumn c : columnsSet) {
			c.dispose();
		}

		for (TreeItem i : itemsSet) {
			i.dispose();
		}
	}

	private void onMouseDown(Event e) {
		Point p = new Point(e.x, e.y);
        if (isVirtual() && itemsHandler.getItemsClientArea().contains(p)) { handleVirtualMouseDown(e, p); return; }

		if (columnsHandler.getColumnsBounds().contains(e.x, e.y)) {
			columnsHandler.handleMouseDown(e);
		} else if (itemsHandler.getItemsClientArea().contains(e.x, e.y)) {
			if (e.button == 3) {
				// Right click handling:
				// -> if the item under the right click is selected, the
				// complete selection stays the same
				// -> if the item under the right click is not selected, the
				// selection will be cleared and the element will be selected
				for (TreeItem i : selectedTreeItems) {
					if (i.getBounds().contains(p)) {
						return;
					}
				}
			}

			final int max = (int) Math.min((long) treeItemsArrangement.size() - 1L,
					itemsHandler.getLastVisibleElementIndex());
			for (int i = Math.toIntExact(getTopIndex()); i <= max; i++) {
				TreeItem it = _getArrangementItem(i);

				Rectangle b = it.getBounds();
				if (it.isInArrowArea(p) && it.toggleExpand()) {
					synchronizeArrangements(true);
					break;
				}

				if (it.isInCheckArea(p)) {
					it.toggleCheck();
					break;
				}

				if (b.contains(p)) {
					if ((style & SWT.MULTI) == 0 || !ctrlPressed) {
						if (selectedTreeItems.size() != 1 || selectedTreeItems.iterator().next() != it) {
							selectedTreeItems.clear();
							selectedTreeItems.add(it);
						}
					} else {
						if (selectedTreeItems.contains(it)) {
							selectedTreeItems.remove(it);
						} else {
							selectedTreeItems.add(it);
						}
					}
				}
			}
			redraw();
		}
	}

	private void handleSelection() {
		sendSelectionEvent(SWT.Selection);
	}

	private void onMouseUp(Event e) {
		if (columnsHandler.getColumnsBounds().contains(e.x, e.y)) {
			columnsHandler.handleMouseUp(e);
		}

		if ((e.stateMask & SWT.BUTTON1) != 0) {
			handleSelection();
		} else {
			redraw();
		}
	}

	@Override
	void _addListener(int eventType, Listener listener) {
		super._addListener(eventType, listener);
		switch (eventType) {
			case SWT.MeasureItem :
			case SWT.EraseItem :
			case SWT.PaintItem :
				setCustomDraw(true);
				setBackgroundTransparent(true);
				break;
		}
	}

	private void onPaint(Event event) {
		Drawing.drawWithGC(this, event.gc, renderer::paint);
	}

	public boolean columnsExist() {
		return !columnsList.isEmpty();
	}

	private TreeItem _getItem(int index) {
		return _getItem(index, true);
	}

	private TreeItem _getItem(int index, boolean create) {
		return _getItem(index, create, -1);
	}

	private TreeItem _getItem(int index, boolean create, int count) {
		if (isVirtual()) {
			if (index >= 0 && index < virtualItemCount) {
				TreeItem e = virtualItemsList.get(index);
				if (e == null && create) {
					e = new TreeItem(this, SWT.None, index, false);
					virtualItemsList.put(index, e);
				}
				return e;
			}
			error(SWT.ERROR_INVALID_RANGE);
		}

		if (index >= getItemCount()) error(SWT.ERROR_INVALID_RANGE);
		if (index < 0) error(SWT.ERROR_INVALID_RANGE);

		return itemsList.get(index);
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when the user changes the receiver's selection, by sending it one of the
	 * messages defined in the <code>SelectionListener</code> interface.
	 * <p>
	 * When <code>widgetSelected</code> is called, the item field of the event
	 * object is valid. If the receiver has the <code>SWT.CHECK</code> style and
	 * the check selection changes, the event object detail field contains the
	 * value <code>SWT.CHECK</code>. <code>widgetDefaultSelected</code> is
	 * typically called when an item is double-clicked. The item field of the
	 * event object is valid for default selection, but the detail field is not
	 * used.
	 * </p>
	 *
	 * @param listener
	 *            the listener which should be notified when the user changes
	 *            the receiver's selection
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 *
	 * @see SelectionListener
	 * @see #removeSelectionListener
	 * @see SelectionEvent
	 */
	public void addSelectionListener(SelectionListener listener) {
		addTypedListener(listener, SWT.Selection, SWT.DefaultSelection);
	}

	static int checkStyle(int style) {
		/*
		 * Feature in Windows. Even when WS_HSCROLL or WS_VSCROLL is not
		 * specified, Windows creates trees and Trees with scroll bars. The fix
		 * is to set H_SCROLL and V_SCROLL.
		 *
		 * NOTE: This code appears on all platforms so that applications have
		 * consistent scroll bar behavior.
		 */
		if ((style & SWT.NO_SCROLL) == 0) {
			style |= SWT.H_SCROLL | SWT.V_SCROLL;
		}
		return checkBits(style, SWT.SINGLE, SWT.MULTI, 0, 0, 0, 0);
	}

	@Override
	void checkBuffered() {
		super.checkBuffered();
		style |= SWT.DOUBLE_BUFFERED;
	}

	boolean checkData(TreeItem item, boolean redraw) {
		if ((style & SWT.VIRTUAL) == 0) {
			return true;
		}
		if (!item.cached) {
			TreeItem parentItem = item.getParentItem();
			return checkData(item,
					parentItem == null
							? indexOf(item)
							: parentItem.indexOf(item),
					redraw);
		}
		return true;
	}

	boolean checkData(TreeItem item, int index, boolean redraw) {
		if ((style & SWT.VIRTUAL) == 0) {
			return true;
		}
		if (!item.cached) {
			item.cached = true;
			Event event = new Event();
			event.item = item;
			event.index = index;
			TreeItem oldItem = currentItem;
			currentItem = item;
			try {
				sendEvent(SWT.SetData, event);
			} finally {
				currentItem = oldItem;
			}
			// widget could be disposed at this point
			if (isDisposed() || item.isDisposed()) {
				return false;
			}
			if (redraw) {
				item.redraw();
			}
		}
		return true;
	}

	@Override
	protected void checkSubclass() {
		if (!isValidSubclass()) error(SWT.ERROR_INVALID_SUBCLASS);
	}

	/**
	 * Clears the item at the given zero-relative index in the receiver. The
	 * text, icon and other attributes of the item are set to the default value.
	 * If the Tree was created with the <code>SWT.VIRTUAL</code> style, these
	 * attributes are requested again as needed.
	 *
	 * @param index
	 *            the index of the item to clear
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_RANGE - if the index is not between 0
	 *                and the number of elements in the list minus 1
	 *                (inclusive)</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 *
	 * @see SWT#VIRTUAL
	 * @see SWT#SetData
	 *
	 * @since 3.0
	 */
	public void clear(int index) {
		clear(new int[]{index});
	}

	/**
	 * Removes the items from the receiver which are between the given
	 * zero-relative start and end indices (inclusive). The text, icon and other
	 * attributes of the items are set to their default values. If the Tree was
	 * created with the <code>SWT.VIRTUAL</code> style, these attributes are
	 * requested again as needed.
	 *
	 * @param start
	 *            the start index of the item to clear
	 * @param end
	 *            the end index of the item to clear
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_RANGE - if either the start or end are
	 *                not between 0 and the number of elements in the list minus
	 *                1 (inclusive)</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 *
	 * @see SWT#VIRTUAL
	 * @see SWT#SetData
	 *
	 * @since 3.0
	 */
	public void clear(int start, int end) {
		checkWidget();

		int count = getItemCount();

		if (start < 0 || start > count - 1) error(SWT.ERROR_INVALID_RANGE);
		if (end < 0 || end > count - 1) error(SWT.ERROR_INVALID_RANGE);
		if (end < start) error(SWT.ERROR_INVALID_RANGE);

		int[] indices = new int[end - start + 1];
		for (int i = start; i <= end; i++) {
			indices[i - start] = i;
		}

		clear(indices);
	}

	/**
	 * Clears the items at the given zero-relative indices in the receiver. The
	 * text, icon and other attributes of the items are set to their default
	 * values. If the Tree was created with the <code>SWT.VIRTUAL</code> style,
	 * these attributes are requested again as needed.
	 *
	 * @param indices
	 *            the array of indices of the items
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_RANGE - if the index is not between 0
	 *                and the number of elements in the list minus 1
	 *                (inclusive)</li>
	 *                <li>ERROR_NULL_ARGUMENT - if the indices array is
	 *                null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 *
	 * @see SWT#VIRTUAL
	 * @see SWT#SetData
	 *
	 * @since 3.0
	 */
	public void clear(int[] indices) {
		checkWidget();

		if (indices == null) error(SWT.ERROR_NULL_ARGUMENT);
		if (indices.length == 0) return;

		int count = getItemCount();
		for (int index : indices) {
			if (0 > index || index >= count) error(SWT.ERROR_INVALID_RANGE);

			TreeItem item = _getItem(index, false);
			if (item != null) {
				item.clear();
				redraw();
			}
		}
	}

	/**
	 * Clears all the items in the receiver. The text, icon and other attributes
	 * of the items are set to their default values. If the Tree was created
	 * with the <code>SWT.VIRTUAL</code> style, these attributes are requested
	 * again as needed.
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 *
	 * @see SWT#VIRTUAL
	 * @see SWT#SetData
	 *
	 * @since 3.0
	 */
	public void clearAll() {
		clearAll(false);
	}

	// TODO handle all true or false.

	/**
	 * public void clearAll(boolean all) Clears all the items in the receiver.
	 * The text, icon and other attributes of the items are set to their default
	 * values. If the tree was created with the SWT.VIRTUAL style, these
	 * attributes are requested again as needed. Parameters: all - true if all
	 * child items should be cleared recursively, and false otherwise Throws:
	 * SWTException - ERROR_WIDGET_DISPOSED - if the receiver has been disposed
	 * ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created
	 * the receiver Since: 3.2 See Also: SWT.VIRTUAL SWT.SetData
	 *
	 * @param all
	 */
	public void clearAll(boolean all) {
		checkWidget();
		java.util.List<TreeItem> roots = new ArrayList<>(isVirtual() ? virtualItemsList.values() : itemsList);
		for (TreeItem item : roots) {
			if (item != null && !item.isDisposed()) item.clear(all);
		}
		redraw();
	}

	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		checkWidget();

		Point defaultSize = computeDefaultSize();
		int width = wHint == SWT.DEFAULT ? defaultSize.x : wHint;
		int height = hHint == SWT.DEFAULT ? defaultSize.y : hHint;
		return new Point(width, height);
	}

	private Point computeDefaultSize() {
		Point columnsSize = columnsHandler.getSize();
		Point itemsArea = itemsHandler.getSize();

		final int scrollbarWidth = 0;
		int width = columnsSize.x + scrollbarWidth;
		if (!columnsExist()) {
			width = itemsArea.x + scrollbarWidth;
		}

		return new Point(width, columnsSize.y + itemsArea.y + scrollbarWidth);
	}

	void createHeaderToolTips() {
		logNotImplemented();
	}

	void destroyItem(TreeColumn column) {
		if (!columnsList.contains(column)) return;

		int index = columnsList.indexOf(column);
		columnsList.remove(column);
        invalidateVirtualMeasurements();
		moveTextsItemsToLeft(index);

		if (columnOrder != null) {
			int[] newColOrder = new int[columnsList.size()];

			boolean indexOccurred = false;
			for (int i = 0; i < columnOrder.length; i++) {
				int reduce = indexOccurred ? 1 : 0;

				if (columnOrder[i] < index) {
					newColOrder[i - reduce] = columnOrder[i];
				} else if (columnOrder[i] > index) {
					newColOrder[i - reduce] = columnOrder[i] - 1;
				} else {
					indexOccurred = true;
				}
			}

			this.columnOrder = newColOrder;
		}
	}

	void createItem(TreeColumn column, int index) {
		if (columnsList.isEmpty()) {
			headerVisible = true;
		}

		columnsList.add(index, column);
        invalidateVirtualMeasurements();

		if (sortColumn == null) {
			sortColumn = column;
		}

		moveTextsItemsToRight(index);

		final TreeItem[] items = isVirtual() ? virtualItemsList.values().toArray(new TreeItem[0]) : getItems();
		if (items != null) {
			for (TreeItem it : items) {
				it.clearCache();
			}
		}

		if (items != null) {
			for (TreeItem it : items) {
				it.clearCache();
			}
		}

		if (columnOrder != null) {
			int[] newOrder = new int[columnsList.size()];
			System.arraycopy(columnOrder, 0, newOrder, 0,
					columnOrder.length);

			for (int i = columnOrder.length; i < columnsList.size(); i++) {
				newOrder[i] = i;
			}

			this.columnOrder = newOrder;
		}

		updateScrollBarWithTextSize();
		redraw();
	}

	private void moveTextsItemsToRight(int index) {
        var pending = new ArrayDeque<TreeItem>(isVirtual() ? virtualItemsList.values() : itemsList);
        while (!pending.isEmpty()) {
            TreeItem item = pending.removeLast();
            if (item == null || item.isDisposed()) continue;
            item.moveTextToRightAt(index);
            item.clearCache();
            item.appendColumnRemapChildren(pending);
        }
    }

	private void moveTextsItemsToLeft(int index) {
        var pending = new ArrayDeque<TreeItem>(isVirtual() ? virtualItemsList.values() : itemsList);
        while (!pending.isEmpty()) {
            TreeItem item = pending.removeLast();
            if (item == null || item.isDisposed()) continue;
            item.moveTextsItemsToLeft(index);
            item.clearCache();
            item.appendColumnRemapChildren(pending);
        }
    }

	protected int getTotalColumnWidth() {
		return columnsHandler.getSize().x;
	}

	protected int getColumnWidth() {
		if (!columnsList.isEmpty()) {
			return columnsList.get(0).getWidth();
		}
		return 0;
	}

	void createItem(TreeItem item, int index) {
		if (isVirtual()) {
            long insertionRank = virtualInsertionRank(null, index);
			if (virtualItemCount == Integer.MAX_VALUE) error(SWT.ERROR_NO_HANDLES);
			int nextCount = virtualItemCount + 1;
			for (Integer at = virtualItemsList.isEmpty() ? null : virtualItemsList.lastKey();
					at != null && at >= index; at = virtualItemsList.lowerKey(at)) {
				TreeItem shifted = virtualItemsList.remove(at);
				virtualItemsList.put(at + 1, shifted);
			}
			virtualItemsList.put(index, item);
			virtualItemCount = nextCount;
            virtualRowsInserted(insertionRank, insertionRank < 0 ? 0 : 1);
		} else {
			if (index < itemsList.size()) {
				itemsList.add(index, item);
			} else {
				itemsList.add(item);
			}
		}

		synchronizeArrangements(true);

		if (!isVirtual()) {
			updateScrollBarWithTextSize();
		}
		if (index >= getTopIndex()
				&& index <= itemsHandler.getLastVisibleElementIndex()) {
			redraw();
		}
	}

	void synchronizeArrangements(boolean redraw) {
        if (isVirtual()) {
            if (virtualWindowBuilding) return;
            virtualWindowBuilding = true;
            try {
                updateScrollBarWithTextSize();
                if (isDisposed()) return;
                if (isVirtualWindowCurrent(treeItemsArrangement)) {
                    if (redraw) redraw();
                    return;
                }
                virtualGeometryValid = false;
                long count = refreshVirtualVisibleRows();
                long first = Math.max(0L, Math.min(topIndex, Math.max(0L, count - 1L)));
                Rectangle body = itemsHandler.getItemsClientArea();
                Point origin = getTopIndexItemPosition();
                int minimum = Math.max(1, TreeItemRenderer.guessItemHeight(this)
                        + TreeItemsHandler.getGridSize(this));
                long capacity = body.height <= 0 ? 0L : (long) body.height / minimum + 2L;
                int size = Math.toIntExact(Math.min(Math.max(0L, count - first), capacity));
                List<TreeItem> next = new ArrayList<>(size);
                for (int local = 0; local < size; local++) {
                    long rank = Math.addExact(first, local);
                    TreeItem item = virtualItemAt(rank);
                    if (item.isDisposed() || virtualVisibleIndexOf(item) != rank) { if (!isDisposed()) redraw(); return; }
                    next.add(item);
                }
                if (isDisposed() || topIndex != first || refreshVirtualVisibleRows() != count
                        || !body.equals(itemsHandler.getItemsClientArea())
                        || !origin.equals(getTopIndexItemPosition())) { if (!isDisposed()) redraw(); return; }
                boolean wasEmpty = virtualWasEmpty;
                virtualWasEmpty = count == 0;
                treeItemsArrangement = next;
                virtualWindowFirst = first;
                virtualWindowEnd = Math.addExact(first, size);
                virtualWindowCount = count;
                virtualWindowBody = body;
                virtualWindowOrigin = origin;
                virtualGeometryValid = true;
                virtualExtentFacts();
                if (wasEmpty && count != 0) notifyListeners(SWT.EmptinessChanged, new Event());
                else if (!wasEmpty && count == 0) { Event event = new Event(); event.detail = 1; notifyListeners(SWT.EmptinessChanged, event); }
                if (!isDisposed() && redraw) redraw();
                return;
            } finally { virtualWindowBuilding = false; }
        }
		boolean treeEmpty = treeItemsArrangement.isEmpty();

		for (TreeItem i : treeItemsArrangement) {
			i.clearCache();
		}
		treeItemsArrangement.clear();

		for (TreeItem i : getItems()) {
			addToArrangements(i, null);
		}

		if (treeEmpty && !treeItemsArrangement.isEmpty()) {
			notifyListeners(SWT.EmptinessChanged, new Event());
		}

		if (!treeEmpty && treeItemsArrangement.isEmpty()) {
			Event e = new Event();
			e.detail = 1;
			notifyListeners(SWT.EmptinessChanged, e);
		}
		if (redraw) {
			updateScrollBarWithTextSize();
			redraw();
		}
	}

	private TreeItem[] getItemsUpNumber(int visibleElements) {
		checkWidget();

		if (isVirtual()) {
			int count = Math.min(getItemCount(), visibleElements);
			TreeItem[] result = new TreeItem[count];
			for (int i = 0; i < count; i++) {
				result[i] = _getItem(i);
			}
			return result;
		}

		return itemsList.toArray(new TreeItem[0]);
	}

	private void addToArrangements(TreeItem i, Integer upToElements) {
		if (i.isDisposed()) return;
		i.clearCache();

		treeItemsArrangement.add(i);

		if (upToElements != null) {
			upToElements = upToElements - 1;

			if (upToElements <= 0) {
				return;
			}
		}

		if (i.getExpanded()) {
			for (TreeItem it : i.getItemsUpToNumber(upToElements)) {
				addToArrangements(it, upToElements);
			}
		}
	}

	private boolean customHeaderDrawing() {
		return headerBackgroundColor != null || headerForegroundColor != null;
	}

	/**
	 * Deselects the items at the given zero-relative indices in the receiver.
	 * If the item at the given zero-relative index in the receiver is selected,
	 * it is deselected. If the item at the index was not selected, it remains
	 * deselected. Indices that are out of range and duplicate indices are
	 * ignored.
	 *
	 * @param indices
	 *            the array of indices for the items to deselect
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the set of indices is
	 *                null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void deselect(int[] indices) {
		checkWidget();

		if (itemsList.isEmpty()) return;
		if (indices == null) error(SWT.ERROR_NULL_ARGUMENT);
		if (indices.length == 0) return;

		Set<TreeItem> s = new HashSet<>();
		for (int i : indices) {
			if (i >= 0 && i < itemsList.size()) {
				s.add(itemsList.get(i));
			}
		}
		selectedTreeItems.removeAll(s);
	}

	/**
	 * Deselects the item at the given zero-relative index in the receiver. If
	 * the item at the index was already deselected, it remains deselected.
	 * Indices that are out of range are ignored.
	 *
	 * @param index
	 *            the index of the item to deselect
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void deselect(int index) {
		checkWidget();

		deselect(new int[]{index});
	}

	public void deselect(TreeItem item) {
		final int index = indexOf(item);
		deselect(index);
	}

	/**
	 * Deselects the items at the given zero-relative indices in the receiver.
	 * If the item at the given zero-relative index in the receiver is selected,
	 * it is deselected. If the item at the index was not selected, it remains
	 * deselected. The range of the indices is inclusive. Indices that are out
	 * of range are ignored.
	 *
	 * @param start
	 *            the start index of the items to deselect
	 * @param end
	 *            the end index of the items to deselect
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void deselect(int start, int end) {
		checkWidget();

		if (start > end) return;

		if (start < 0) {
			start = 0;
		}
		if (end >= itemsList.size()) {
			end = itemsList.size() - 1;
		}

		int[] arr = new int[end - start + 1];
		for (int s = start; s <= end; s++) {
			arr[s - start] = s;
		}

		deselect(arr);
	}

	/**
	 * Deselects all selected items in the receiver.
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void deselectAll() {
		checkWidget();

		selectedTreeItems.clear();
		redraw();
	}

	void destroyItem(TreeItem item) {
		removeSelectionSubtree(item);

		if (mouseHoverElement == item) {
			mouseHoverElement = null;
		}

		if (isVirtual()) {
			int index = indexOf(item);
			if (index < 0) return;
			long removalRank = arrangementIndexOf(item);
            long removalRows = removalRank < 0 ? 0 : 1L + virtualDescendantRows(item);
            virtualItemsList.remove(index);
			for (Integer at = virtualItemsList.higherKey(index); at != null;) {
				Integer next = virtualItemsList.higherKey(at);
				TreeItem shifted = virtualItemsList.remove(at);
				virtualItemsList.put(at - 1, shifted);
				at = next;
			}
			virtualItemCount--;
            virtualRowsRemoved(removalRank, removalRows);
			synchronizeArrangements(true);
			return;
		}

		itemsList.remove(item);
		synchronizeArrangements(true);
	}

	/**
	 * Returns the column at the given, zero-relative index in the receiver.
	 * Throws an exception if the index is out of range. Columns are returned in
	 * the order that they were created. If no <code>TreeColumn</code>s were
	 * created by the programmer, this method will throw
	 * <code>ERROR_INVALID_RANGE</code> despite the fact that a single column of
	 * data may be visible in the Tree. This occurs when the programmer uses the
	 * Tree like a list, adding items but never creating a column.
	 *
	 * @param index
	 *            the index of the column to return
	 * @return the column at the given index
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_RANGE - if the index is not between 0
	 *                and the number of elements in the list minus 1
	 *                (inclusive)</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 *
	 * @see Tree#getColumnOrder()
	 * @see Tree#setColumnOrder(int[])
	 * @see TreeColumn#getMoveable()
	 * @see TreeColumn#setMoveable(boolean)
	 * @see SWT#Move
	 */
	public TreeColumn getColumn(int index) {
		checkWidget();
		if (0 > index || index >= columnsList.size()) error(SWT.ERROR_INVALID_RANGE);
		return columnsList.get(index);
	}

	/**
	 * Returns the number of columns contained in the receiver. If no
	 * <code>TreeColumn</code>s were created by the programmer, this value is
	 * zero, despite the fact that visually, one column of items may be visible.
	 * This occurs when the programmer uses the Tree like a list, adding items
	 * but never creating a column.
	 *
	 * @return the number of columns
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getColumnCount() {
		checkWidget();
		return columnsList.size();
	}

	/**
	 * Returns an array of zero-relative integers that map the creation order of
	 * the receiver's items to the order in which they are currently being
	 * displayed.
	 * <p>
	 * Specifically, the indices of the returned array represent the current
	 * visual order of the items, and the contents of the array represent the
	 * creation order of the items.
	 * </p>
	 * <p>
	 * Note: This is not the actual structure used by the receiver to maintain
	 * its list of items, so modifying the array will not affect the receiver.
	 * </p>
	 *
	 * @return the current visual order of the receiver's items
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 *
	 * @see Tree#setColumnOrder(int[])
	 * @see TreeColumn#getMoveable()
	 * @see TreeColumn#setMoveable(boolean)
	 * @see SWT#Move
	 *
	 * @since 3.1
	 */
	public int[] getColumnOrder() {
		checkWidget();

		if (columnsList.isEmpty()) {
			return new int[0];
		}

		if (columnOrder == null) {
			columnOrder = new int[columnsList.size()];

			for (int i = 0; i < columnsList.size(); i++) {
				columnOrder[i] = i;
			}
		}

		return columnOrder;
	}

	/**
	 * Returns an array of <code>TreeColumn</code>s which are the columns in the
	 * receiver. Columns are returned in the order that they were created. If no
	 * <code>TreeColumn</code>s were created by the programmer, the array is
	 * empty, despite the fact that visually, one column of items may be
	 * visible. This occurs when the programmer uses the Tree like a list,
	 * adding items but never creating a column.
	 * <p>
	 * Note: This is not the actual structure used by the receiver to maintain
	 * its list of items, so modifying the array will not affect the receiver.
	 * </p>
	 *
	 * @return the items in the receiver
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 *
	 * @see Tree#getColumnOrder()
	 * @see Tree#setColumnOrder(int[])
	 * @see TreeColumn#getMoveable()
	 * @see TreeColumn#setMoveable(boolean)
	 * @see SWT#Move
	 */
	public TreeColumn[] getColumns() {
		checkWidget();

		return columnsList.toArray(new TreeColumn[0]);
	}

	int getFocusIndex() {
		// checkWidget ();

		logNotImplemented();
		return 0;
	}

	/**
	 * Returns the width in points of a grid line.
	 *
	 * @return the width of a grid line in points
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getGridLineWidth() {
		checkWidget();
		return DPIUtil.scaleDown(getGridLineWidthInPixels(), 100);
	}

	int getGridLineWidthInPixels() {
		return GRID_WIDTH;
	}

	/**
	 * Returns the header background color.
	 *
	 * @return the receiver's header background color.
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * @since 3.106
	 */
	public Color getHeaderBackground() {
		checkWidget();

		if (headerBackgroundColor != null) {
			return headerBackgroundColor;
		}

		return Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
	}

	/**
	 * Returns the header foreground color.
	 *
	 * @return the receiver's header foreground color.
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * @since 3.106
	 */
	public Color getHeaderForeground() {
		checkWidget();

		if (headerForegroundColor != null) {
			return headerForegroundColor;
		}

		return Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
	}

	/**
	 * Returns the height of the receiver's header
	 *
	 * @return the height of the header or zero if the header is not visible
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 *
	 * @since 2.0
	 */
	public int getHeaderHeight() {
		checkWidget();

		return columnsHandler.getSize().y;
	}

	/**
	 * Returns <code>true</code> if the receiver's header is visible, and
	 * <code>false</code> otherwise.
	 * <p>
	 * If one of the receiver's ancestors is not visible or some other condition
	 * makes the receiver not visible, this method may still indicate that it is
	 * considered visible even though it may not actually be showing.
	 * </p>
	 *
	 * @return the receiver's header's visibility state
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public boolean getHeaderVisible() {
		checkWidget();
		return headerVisible;
	}

	/**
	 * Returns the item at the given, zero-relative index in the receiver.
	 * Throws an exception if the index is out of range.
	 *
	 * @param index
	 *            the index of the item to return
	 * @return the item at the given index
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_RANGE - if the index is not between 0
	 *                and the number of elements in the list minus 1
	 *                (inclusive)</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public TreeItem getItem(int index) {
		checkWidget();
		return _getItem(index);
	}

	/**
	 * Returns the item at the given point in the receiver or null if no such
	 * item exists. The point is in the coordinate system of the receiver.
	 * <p>
	 * The item that is returned represents an item that could be selected by
	 * the user. For example, if selection only occurs in items in the first
	 * column, then null is returned if the point is outside of the item. Note
	 * that the SWT.FULL_SELECTION style hint, which specifies the selection
	 * policy, determines the extent of the selection.
	 * </p>
	 *
	 * @param point
	 *            the point used to locate the item
	 * @return the item at the given point, or null if the point is not in a
	 *         selecTree item
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the point is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public TreeItem getItem(Point point) {
		checkWidget();
		if (point == null) error(SWT.ERROR_NULL_ARGUMENT);
		return getItemInPixels(DPIUtil.scaleUp(point, 100));
	}

	TreeItem getItemInPixels(Point point) {
        if (!isVirtual()) {
		for (int i = Math.toIntExact(getTopIndex()); i < Math.min(treeItemsArrangement.size(),
				itemsHandler.getLastVisibleElementIndex() + 5); i++) {
			TreeItem it = _getArrangementItem(i);
			if (it != null && it.getBounds().contains(point)) {
				return it;
			}
		}
		return null;
	}
        synchronizeArrangements(false);
        if (isDisposed()) return null;
        List<TreeItem> expected = treeItemsArrangement;
        if (!isVirtualWindowCurrent(expected)) { if (!isDisposed()) redraw(); return null; }
        Rectangle body = itemsHandler.getItemsClientArea();
        if (!body.contains(point)) return null;
        long bottom = (long) body.y + body.height;
        for (int local = 0; local < expected.size(); local++) {
            TreeItem item = expected.get(local);
            long rank = virtualWindowFirst + local;
            long y = virtualItemY(item);
            if (!isVirtualRowCurrent(expected, item, rank)) { if (!isDisposed()) redraw(); return null; }
            if (y >= bottom) break;
            if (!checkData(item, true) || !isVirtualRowCurrent(expected, item, rank)) { if (!isDisposed()) redraw(); return null; }
            Rectangle bounds = item.getBounds();
            if (!isVirtualRowCurrent(expected, item, rank)) { if (!isDisposed()) redraw(); return null; }
            if (bounds.contains(point)) return item;
        }
        return null;
    }

	/**
	 * Returns the number of items contained in the receiver.
	 *
	 * @return the number of items
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getItemCount() {
		checkWidget();

		if (isVirtual()) {
			return virtualItemCount;
		}

		return itemsList.size();
	}

	boolean isVirtual() {
		return (getStyle() & SWT.VIRTUAL) != 0;
	}

	/**
	 * Returns the height of the area which would be used to display
	 * <em>one</em> of the items in the receiver.
	 *
	 * @return the height of one item
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getItemHeight() {
		checkWidget();

		if (!itemsList.isEmpty() && !treeItemsArrangement.isEmpty()) {
			final int topIndex = Math.toIntExact(getTopIndex());
			if (treeItemsArrangement.size() > topIndex) {
				return treeItemsArrangement.get(topIndex)
						.getBounds().height;
			}
			return treeItemsArrangement
					.get(treeItemsArrangement.size() - 1)
					.getBounds().height;
		}

		return TreeItemRenderer.guessItemHeight(this);
	}

	/**
	 * Returns a (possibly empty) array of <code>TreeItem</code>s which are the
	 * items in the receiver.
	 * <p>
	 * Note: This is not the actual structure used by the receiver to maintain
	 * its list of items, so modifying the array will not affect the receiver.
	 * </p>
	 *
	 * @return the items in the receiver
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public TreeItem[] getItems() {
		checkWidget();

		if (isVirtual()) {
			TreeItem[] result = new TreeItem[getItemCount()];
			for (int i = 0; i < getItemCount(); i++) {
				result[i] = _getItem(i);
			}
			return result;
		}

		return itemsList.toArray(new TreeItem[0]);
	}

	/**
	 * Returns <code>true</code> if the receiver's lines are visible, and
	 * <code>false</code> otherwise. Note that some platforms draw grid lines
	 * while others may draw alternating row colors.
	 * <p>
	 * If one of the receiver's ancestors is not visible or some other condition
	 * makes the receiver not visible, this method may still indicate that it is
	 * considered visible even though it may not actually be showing.
	 * </p>
	 *
	 * @return the visibility state of the lines
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public boolean getLinesVisible() {
		checkWidget();
		return linesVisible;
	}

	/**
	 * Returns an array of <code>TreeItem</code>s that are currently selected in
	 * the receiver. The order of the items is unspecified. An empty array
	 * indicates that no items are selected.
	 * <p>
	 * Note: This is not the actual structure used by the receiver to maintain
	 * its selection, so modifying the array will not affect the receiver.
	 * </p>
	 *
	 * @return an array representing the selection
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public TreeItem[] getSelection() {
		checkWidget();
		return selectedTreeItems.toArray(new TreeItem[0]);
	}

	/**
	 * Returns the number of selected items contained in the receiver.
	 *
	 * @return the number of selected items
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getSelectionCount() {
		checkWidget();
		return selectedTreeItems.size();
	}

	/**
	 * Returns the zero-relative index of the item which is currently selected
	 * in the receiver, or -1 if no item is selected.
	 *
	 * @return the index of the selected item
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getSelectionIndex() {
		checkWidget();

		if (selectedTreeItems.isEmpty())
			return -1;
		return indexOf(selectedTreeItems.first());
	}

	/**
	 * @return rectangle which contains all visible columns of the Tree
	 */
	Rectangle getColumnsArea() {
		return columnsHandler.getColumnsBounds();
	}

	/**
	 * @return rectangle which contains all visible items of the Tree
	 */
	Rectangle getItemsArea() {
		return itemsHandler.getItemsClientArea();
	}

	/**
	 * Returns the zero-relative indices of the items which are currently
	 * selected in the receiver. The order of the indices is unspecified. The
	 * array is empty if no items are selected.
	 * <p>
	 * Note: This is not the actual structure used by the receiver to maintain
	 * its selection, so modifying the array will not affect the receiver.
	 * </p>
	 *
	 * @return the array of indices of the selected items
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int[] getSelectionIndices() {
		checkWidget();
		return indicesOf(selectedTreeItems.toArray(new TreeItem[0]));
	}

	/**
	 * Returns the column which shows the sort indicator for the receiver. The
	 * value may be null if no column shows the sort indicator.
	 *
	 * @return the sort indicator
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 *
	 * @see #setSortColumn(TreeColumn)
	 *
	 * @since 3.2
	 */
	public TreeColumn getSortColumn() {
		checkWidget();
		return sortColumn;
	}

	int getSortColumnPixel() {
		logNotImplemented();
		return 0;
	}

	/**
	 * Returns the direction of the sort indicator for the receiver. The value
	 * will be one of <code>UP</code>, <code>DOWN</code> or <code>NONE</code>.
	 *
	 * @return the sort direction
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 *
	 * @see #setSortDirection(int)
	 *
	 * @since 3.2
	 */
	public int getSortDirection() {
		checkWidget();
		return sortDirection;
	}

	/**
	 * Returns the zero-relative index of the item which is currently at the top
	 * of the receiver. This index can change when items are scrolled or new
	 * items are added or removed.
	 *
	 * @return the index of the top item
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	long getTopIndex() { checkWidget(); return topIndex; }

	public TreeItem getTopItem() {
		return topItem;
	}

	boolean hasChildren() {
		logNotImplemented();
		return false;
	}

	boolean hitTestSelection(int index, int x, int y) {
		logNotImplemented();
		return false;
	}

	/**
	 * Searches the receiver's list starting at the first column (index 0) until
	 * a column is found that is equal to the argument, and returns the index of
	 * that column. If no column is found, returns -1.
	 *
	 * @param column
	 *            the search column
	 * @return the index of the column
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the column is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int indexOf(TreeColumn column) {
		checkWidget();
		if (column == null)
			error(SWT.ERROR_NULL_ARGUMENT);
		return columnsList.indexOf(column);
	}

	/**
	 * Searches the receiver's list starting at the first item (index 0) until
	 * an item is found that is equal to the argument, and returns the index of
	 * that item. If no item is found, returns -1.
	 *
	 * @param item
	 *            the search item
	 * @return the index of the item
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the item is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int indexOf(TreeItem item) {
		checkWidget();
		if (item == null) error(SWT.ERROR_NULL_ARGUMENT);
		if (isVirtual()) {
			for (Map.Entry<Integer, TreeItem> e : virtualItemsList.entrySet()) {
				if (item.equals(e.getValue())) {
					return e.getKey();
				}
			}
			return -1;
		}
		return itemsList.indexOf(item);
	}

	long arrangementIndexOf(TreeItem item) { return isVirtual() ? virtualVisibleIndexOf(item) : treeItemsArrangement.indexOf(item); }


	private NavigableMap<Integer, TreeItem> materializedVirtualChildren(TreeItem parentItem) {
		return parentItem == null ? virtualItemsList : parentItem.materializedVirtualChildren();
	}

	/* Derived warm-node weights only; logical cold siblings remain implicit. */
    void invalidateVirtualTopology() {
        virtualTopologyRevision++;
        virtualRowsCacheValid = false;
        virtualGeometryValid = false;
    }

    private long publishVirtualVisibleRows(long rows, long revision) {
        if (!isDisposed() && revision == virtualTopologyRevision) {
            virtualVisibleRows = rows;
            virtualRowsRevision = revision;
            virtualRowsCacheValid = true;
        }
        return rows;
    }

	private long refreshVirtualVisibleRows() {
        if (getClass() != Tree.class) return refreshVirtualVisibleRowsUncached();
		long total = getItemCount();
        if (virtualRowsCacheValid && virtualRowsRevision == virtualTopologyRevision) return virtualVisibleRows;
        long revision = virtualTopologyRevision;

		var first = virtualItemsList.firstEntry();
		TreeItem current = first == null ? null : first.getValue();
        int coordinate = first == null ? -1 : first.getKey();
        var parentCoordinates = new java.util.ArrayDeque<Integer>();
		while (current != null) {
            if (current.getClass() != TreeItem.class) return refreshVirtualVisibleRowsUncached();
			current.virtualVisibleExtraRows = current.getExpanded() ? current.getItemCount() : 0;
			var child = current.getExpanded() ? current.materializedVirtualChildren().firstEntry() : null;
			if (child != null) {
				parentCoordinates.addLast(coordinate);
				current = child.getValue();
                coordinate = child.getKey();
				continue;
			}
			while (true) {
				TreeItem parentItem = current.getParentItem();
				if (parentItem == null) {
					total = Math.addExact(total, current.virtualVisibleExtraRows);
				} else {
					parentItem.virtualVisibleExtraRows = Math.addExact(
							parentItem.virtualVisibleExtraRows, current.virtualVisibleExtraRows);
				}
				var next = materializedVirtualChildren(parentItem).higherEntry(coordinate);
				if (next != null) {
					current = next.getValue();
                    coordinate = next.getKey();
					break;
				}
				if (parentItem == null) return publishVirtualVisibleRows(total, revision);
				current = parentItem;
                coordinate = parentCoordinates.removeLast();
			}
		}
		return publishVirtualVisibleRows(total, revision);
	}

	private long refreshVirtualVisibleRowsUncached() {
		long total = getItemCount();
		var first = virtualItemsList.firstEntry();
		TreeItem current = first == null ? null : first.getValue();
		while (current != null) {
			current.virtualVisibleExtraRows = current.getExpanded() ? current.getItemCount() : 0;
			var child = current.getExpanded() ? current.materializedVirtualChildren().firstEntry() : null;
			if (child != null) {
				current = child.getValue();
				continue;
			}
			while (true) {
				TreeItem parentItem = current.getParentItem();
				if (parentItem == null) {
					total = Math.addExact(total, current.virtualVisibleExtraRows);
				} else {
					parentItem.virtualVisibleExtraRows = Math.addExact(
							parentItem.virtualVisibleExtraRows, current.virtualVisibleExtraRows);
				}
				int coordinate = parentItem == null ? indexOf(current) : parentItem.indexOf(current);
				var next = materializedVirtualChildren(parentItem).higherEntry(coordinate);
				if (next != null) {
					current = next.getValue();
					break;
				}
				if (parentItem == null) return total;
				current = parentItem;
			}
		}
		return total;
	}

	/* Adapted from VirtualTreeVisibleProjection.rowAt: skip cold coordinate gaps arithmetically. */
	private TreeItem virtualItemAt(long visibleIndex) {
		long total = refreshVirtualVisibleRows();
		if (visibleIndex < 0 || visibleIndex >= total) throw new IndexOutOfBoundsException(
				"visible row outside projection: " + visibleIndex);
		TreeItem parentItem = null;
		long row = visibleIndex;
		seek: while (true) {
			int logicalCount = parentItem == null ? getItemCount() : parentItem.getItemCount();
			int coordinate = 0;
			for (var entry : materializedVirtualChildren(parentItem).entrySet()) {
				int index = entry.getKey();
				if (index >= logicalCount) break;
				int coldGap = index - coordinate;
				if (row < coldGap) {
					int target = Math.addExact(coordinate, (int) row);
					return parentItem == null ? _getItem(target) : parentItem._getItem(target);
				}
				row -= coldGap;
				TreeItem item = entry.getValue();
				if (row == 0) return item;
				row--;
				if (item.getExpanded()) {
					long descendants = item.virtualVisibleExtraRows;
					if (row < descendants) {
						parentItem = item;
						continue seek;
					}
					row -= descendants;
				}
				coordinate = index + 1;
			}
			int target = Math.toIntExact(Math.addExact((long) coordinate, row));
			if (target >= logicalCount) throw new IllegalStateException("projection overflow");
			return parentItem == null ? _getItem(target) : parentItem._getItem(target);
		}
	}

	/* Logical rank does not depend on current arrangement membership. */
	private long virtualVisibleIndexOf(TreeItem item) {
		if (item == null || item.isDisposed() || item.getParent() != this) return -1;
		for (TreeItem ancestor = item.getParentItem(); ancestor != null; ancestor = ancestor.getParentItem()) {
			if (ancestor.isDisposed() || !ancestor.getExpanded()) return -1;
		}
		refreshVirtualVisibleRows();
		long offset = 0;
		TreeItem current = item;
		while (true) {
			TreeItem parentItem = current.getParentItem();
			int coordinate = parentItem == null ? indexOf(current) : parentItem.indexOf(current);
			if (coordinate < 0) return -1;
			long withinParent = coordinate;
			for (TreeItem sibling : materializedVirtualChildren(parentItem).headMap(coordinate, false).values()) {
				if (sibling.getExpanded()) withinParent = Math.addExact(
						withinParent, sibling.virtualVisibleExtraRows);
			}
			offset = Math.addExact(offset, withinParent);
			if (parentItem == null) return offset;
			offset = Math.addExact(offset, 1);
			current = parentItem;
		}
	}

	public int[] indicesOf(TreeItem[] items) {
		checkWidget();

		if (items == null) {
			return null;
		}

		int[] indexes = new int[items.length];
		for (int currentIndex = 0; currentIndex < items.length; currentIndex++) {
			indexes[currentIndex] = indexOf(items[currentIndex]);
		}
		return indexes;
	}

	boolean isCustomToolTip() {
		return hooks(SWT.MeasureItem);
	}

	boolean isOptimizedRedraw() {
		if ((style & SWT.H_SCROLL) == 0 || (style & SWT.V_SCROLL) == 0) {
			return false;
		}
		return !hasChildren() && !hooks(SWT.Paint) && !filters(SWT.Paint)
				&& !customHeaderDrawing();
	}

	/**
	 * Returns <code>true</code> if the item is selected, and <code>false</code>
	 * otherwise. Indices out of range are ignored.
	 *
	 * @param index
	 *            the index of the item
	 * @return the selection state of the item at the index
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public boolean isSelected(int index) {
		for (TreeItem it : selectedTreeItems) {
			if (indexOf(it) == index) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Removes the items from the receiver's list at the given zero-relative
	 * indices.
	 *
	 * @param indices
	 *            the array of indices of the items
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_RANGE - if the index is not between 0
	 *                and the number of elements in the list minus 1
	 *                (inclusive)</li>
	 *                <li>ERROR_NULL_ARGUMENT - if the indices array is
	 *                null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void remove(int[] indices) {
		checkWidget();

		if (indices == null) error(SWT.ERROR_NULL_ARGUMENT);

		Set<Integer> set = new HashSet<>();
		Arrays.stream(indices).forEach(set::add);
		List<Integer> indicesList = new ArrayList<>(set);
		Collections.sort(indicesList);
		for (int i = indicesList.size() - 1; i >= 0; i--) {
			int index = indicesList.get(i);
			if (index >= 0 && index < getItemCount()) {
				remove(index);
			} else {
				error(SWT.ERROR_INVALID_RANGE);
			}
		}
	}

	/**
	 * Removes the item from the receiver at the given zero-relative index.
	 *
	 * @param index
	 *            the index for the item
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_RANGE - if the index is not between 0
	 *                and the number of elements in the list minus 1
	 *                (inclusive)</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void remove(int index) {
        checkWidget();
        if (index < 0 || index >= getItemCount()) error(SWT.ERROR_INVALID_RANGE);
        if (!isVirtual()) { itemsList.get(index).dispose(); return; }
        TreeItem item = _getItem(index, false);
        if (item != null) { item.dispose(); return; }
        long rank = virtualInsertionRank(null, index);
        for (Integer at = virtualItemsList.higherKey(index); at != null;) {
            Integer next = virtualItemsList.higherKey(at);
            TreeItem shifted = virtualItemsList.remove(at);
            virtualItemsList.put(at - 1, shifted);
            at = next;
        }
        virtualItemCount--;
        virtualRowsRemoved(rank, 1);
        synchronizeArrangements(true);
    }

	/**
	 * Removes the items from the receiver which are between the given
	 * zero-relative start and end indices (inclusive).
	 *
	 * @param start
	 *            the start of the range
	 * @param end
	 *            the end of the range
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_RANGE - if either the start or end are
	 *                not between 0 and the number of elements in the list minus
	 *                1 (inclusive)</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void remove(int start, int end) {
		checkWidget();

		if (start > end) return;
		if (start < 0 || end >= getItemCount()) error(SWT.ERROR_INVALID_RANGE);

		int[] indices = new int[end - start + 1];
		for (int i = start; i <= end; i++) {
			indices[i - start] = i;
		}
		remove(indices);
	}

	/**
	 * Removes all of the items from the receiver.
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void removeAll() {
		if (isVirtual()) {
			selectedTreeItems.clear();
			Set<TreeItem> s = new HashSet<>(virtualItemsList.values());
			virtualItemsList.clear();
			virtualItemCount = 0;
            virtualTopologyReset();
			try (var exceptions = new org.eclipse.swt.internal.ExceptionStash()) {
                for (TreeItem item : s) {
                    try { if (!item.isDisposed()) item.dispose(); }
                    catch (Error | RuntimeException exception) { exceptions.stash(exception); }
                }
            }
            if (!isDisposed()) synchronizeArrangements(true);
			return;
		}

		remove(0, getItemCount() - 1);
	}

	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when the user changes the receiver's selection.
	 *
	 * @param listener
	 *            the listener which should no longer be notified
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 *
	 * @see SelectionListener
	 * @see #addSelectionListener(SelectionListener)
	 */
	public void removeSelectionListener(SelectionListener listener) {
		checkWidget();
		if (listener == null) error(SWT.ERROR_NULL_ARGUMENT);
		if (eventTable == null) return;
		eventTable.unhook(SWT.Selection, listener);
		eventTable.unhook(SWT.DefaultSelection, listener);
	}

	/**
	 * Selects the items at the given zero-relative indices in the receiver. The
	 * current selection is not cleared before the new items are selected.
	 * <p>
	 * If the item at a given index is not selected, it is selected. If the item
	 * at a given index was already selected, it remains selected. Indices that
	 * are out of range and duplicate indices are ignored. If the receiver is
	 * single-select and multiple indices are specified, then all indices are
	 * ignored.
	 * </p>
	 *
	 * @param indices
	 *            the array of indices for the items to select
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the array of indices is
	 *                null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 *
	 * @see Tree#setSelection(int[])
	 */
	public void select(int[] indices) {
		checkWidget();

		if (indices == null) error(SWT.ERROR_NULL_ARGUMENT);

		if ((style & SWT.SINGLE) != 0) {
			if (indices.length > 1) {
				return;
			}
			selectedTreeItems.clear();
		}

		for (int index : indices) {
			if (index < 0 || index >= getItemCount()) {
				continue;
			}

			TreeItem it = getItem(index);
			if (it != null) {
				selectedTreeItems.add(it);

				if ((style & SWT.SINGLE) != 0) {
					break;
				}
			}
		}
	}

	boolean hasItems() {
		if (isVirtual()) {
			return virtualItemCount > 0;
		}
		return !itemsList.isEmpty();
	}

	@Override
	void reskinChildren(int flags) {
		if (isVirtual()) {
			for (Map.Entry<Integer, TreeItem> e : virtualItemsList.entrySet()) {
				TreeItem it = e.getValue();
				if (it != null) {
					it.reskin(flags);
				}
			}
		} else {
			if (hasItems()) {
				int itemCount = getItemCount();
				for (int i = 0; i < itemCount; i++) {
					TreeItem item = _getItem(i, false);
					if (item != null) {
						item.reskin(flags);
					}
				}
			}

		}
		if (columnsExist()) {
			for (int i = 0; i < getColumnCount(); i++) {
				TreeColumn column = getColumn(i);
				if (!column.isDisposed())
					column.reskin(flags);
			}
		}
		super.reskinChildren(flags);
	}

	/**
	 * Selects the item at the given zero-relative index in the receiver. If the
	 * item at the index was already selected, it remains selected. Indices that
	 * are out of range are ignored.
	 *
	 * @param index
	 *            the index of the item to select
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void select(int index) {
		checkWidget();

		if (index < 0 || index >= getItemCount()) return;

		if ((style & SWT.SINGLE) != 0) {
			selectedTreeItems.clear();
		}

		TreeItem selected = getItem(index);
		if (selectedTreeItems.contains(selected)) {
			return;
		}

		selectedTreeItems.add(getItem(index));
	}

	/**
	 * Selects the items in the range specified by the given zero-relative
	 * indices in the receiver. The range of indices is inclusive. The current
	 * selection is not cleared before the new items are selected.
	 * <p>
	 * If an item in the given range is not selected, it is selected. If an item
	 * in the given range was already selected, it remains selected. Indices
	 * that are out of range are ignored and no items will be selected if start
	 * is greater than end. If the receiver is single-select and there is more
	 * than one item in the given range, then all indices are ignored.
	 * </p>
	 *
	 * @param start
	 *            the start of the range
	 * @param end
	 *            the end of the range
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 *
	 * @see Tree#setSelection(int,int)
	 */
	public void select(int start, int end) {
		checkWidget();

		if (itemsList.isEmpty()) return;
		if ((SWT.SINGLE & style) != 0 && start != end) return;
		if (end < start) return;
		if (end < 0) return;

		if (start < 0) {
			start = 0;
		}

		if (start > itemsList.size() - 1) return;

		if (end > itemsList.size() - 1) {
			end = itemsList.size() - 1;
		}

		int[] indices = new int[end - start + 1];
		for (int i = start; i <= end; i++) {
			indices[i - start] = i;
		}

		if ((style & SWT.SINGLE) != 0) {
			selectedTreeItems.clear();
			selectedTreeItems.add(itemsList.get(indices[0]));
			return;
		}

		select(indices);
	}

	/**
	 * Selects all of the items in the receiver.
	 * <p>
	 * If the receiver is single-select, do nothing.
	 * </p>
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void selectAll() {
		checkWidget();

		if ((style & SWT.SINGLE) != 0) return;
		if (itemsList.isEmpty()) return;

		select(0, itemsList.size() - 1);
	}

	void setBackgroundTransparent(boolean transparent) {
		logNotImplemented();
	}

	/**
	 * Sets the order that the items in the receiver should be displayed in to
	 * the given argument which is described in terms of the zero-relative
	 * ordering of when the items were added.
	 *
	 * @param order
	 *            the new order to display the items
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the item order is null</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the item order is not the
	 *                same length as the number of items</li>
	 *                </ul>
	 *
	 * @see Tree#getColumnOrder()
	 * @see TreeColumn#getMoveable()
	 * @see TreeColumn#setMoveable(boolean)
	 * @see SWT#Move
	 *
	 * @since 3.1
	 */
	public void setColumnOrder(int[] order) {
		checkWidget();

		if (order == null) error(SWT.ERROR_NULL_ARGUMENT);
		// if (order.length == 0) {
		// this.columnOrder = null;
		// return;
		// }
		int columnCount = getColumnCount();
		if (columnCount == 0) {
			if (order.length != 0) error(SWT.ERROR_INVALID_ARGUMENT);
			return;
		}

		if (order.length != columnCount) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}

		Set<Integer> set = new HashSet<>();
		Arrays.stream(order).forEach(e -> {
			if (e < 0 || e >= columnCount) {
				error(SWT.ERROR_INVALID_ARGUMENT);
			}

			set.add(e);
		});

		if (order.length != set.size()) error(SWT.ERROR_INVALID_ARGUMENT);

		this.columnOrder = order;
        invalidateVirtualGeometry();
	}

	void setCustomDraw(boolean customDraw) {
		this.customDraw = customDraw;
	}

	void setDeferResize(boolean defer) {
		logNotImplemented();
	}

	void setCheckboxImageList(int width, int height, boolean fixScroll) {
		logNotImplemented();
	}

	void setFocusIndex(int index) {
		if (index < 0 || index >= getItemCount()) {
			focusItem = null;
			return;
		}

		focusItem = getItem(index);

		logNotImplemented();
	}

	/**
	 * Sets the header background color to the color specified by the argument,
	 * or to the default system color if the argument is null.
	 * <p>
	 * Note: This operation is a <em>HINT</em> and is not supported on all
	 * platforms. If the native header has a 3D look and feel (e.g. Windows 7),
	 * this method will cause the header to look FLAT irrespective of the state
	 * of the Tree style.
	 * </p>
	 *
	 * @param color
	 *            the new color (or null)
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the argument has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * @since 3.106
	 */
	public void setHeaderBackground(Color color) {
		checkWidget();

		if (color != null && color.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);

		if (headerBackgroundColor != color) {
			headerBackgroundColor = color;
		}
	}

	/**
	 * Sets the header foreground color to the color specified by the argument,
	 * or to the default system color if the argument is null.
	 * <p>
	 * Note: This operation is a <em>HINT</em> and is not supported on all
	 * platforms. If the native header has a 3D look and feel (e.g. Windows 7),
	 * this method will cause the header to look FLAT irrespective of the state
	 * of the Tree style.
	 * </p>
	 *
	 * @param color
	 *            the new color (or null)
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the argument has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * @since 3.106
	 */
	public void setHeaderForeground(Color color) {
		checkWidget();

		if (color != null && color.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);

		if (headerForegroundColor != color) {
			headerForegroundColor = color;
		}
	}

	/**
	 * Marks the receiver's header as visible if the argument is
	 * <code>true</code>, and marks it invisible otherwise.
	 * <p>
	 * If one of the receiver's ancestors is not visible or some other condition
	 * makes the receiver not visible, marking it visible may not actually cause
	 * it to be displayed.
	 * </p>
	 *
	 * @param show
	 *            the new visibility state
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setHeaderVisible(boolean show) {
		checkWidget();

		if (headerVisible == show) {
			return;
		}
		headerVisible = show;
        invalidateVirtualGeometry();

		if (isVirtual()) {
			for (TreeItem it : virtualItemsList.values()) {
				if (it != null) {
					it.clearCache();
				}
			}
		} else {
			for (TreeItem it : itemsList) {
				it.clearCache();
			}
		}

		columnsHandler.clearCache();
		itemsHandler.clearCache();

		redraw();
	}

	/**
	 * Sets the number of items contained in the receiver.
	 *
	 * @param count
	 *            the number of items
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 *
	 * @since 3.0
	 */
	public void setItemCount(int count) {
		checkWidget();

		count = Math.max(0, count);
        if (isVirtual()) {
            int oldCount = virtualItemCount;
            if (oldCount == count) return;
            long oldRows = logicalOpenedCount();
            long boundary = virtualInsertionRank(null, Math.min(oldCount, count));
            java.util.List<TreeItem> removed = new ArrayList<>();
            while (!virtualItemsList.isEmpty() && virtualItemsList.lastKey() >= count) {
                TreeItem detached = virtualItemsList.pollLastEntry().getValue();
                removeSelectionSubtree(detached);
                removed.add(detached);
            }
            virtualItemCount = count;
            invalidateVirtualTopology();
            long newRows = logicalOpenedCount();
            if (newRows > oldRows) virtualRowsInserted(boundary, newRows - oldRows);
            else if (newRows < oldRows) virtualRowsRemoved(boundary, oldRows - newRows);
            else invalidateVirtualGeometry();
            try (var exceptions = new org.eclipse.swt.internal.ExceptionStash()) {
                for (TreeItem detached : removed) {
                    try { if (!detached.isDisposed()) detached.dispose(); }
                    catch (Error | RuntimeException exception) { exceptions.stash(exception); }
                }
            }
            if (!isDisposed()) synchronizeArrangements(true);
            return;
        }

		if (count == itemsList.size()) {
			return;
		}

		if (count > itemsList.size()) {
			for (int i = itemsList.size(); i < count; i++) {
				new TreeItem(this, SWT.None);
			}
		} else {
			for (int i = itemsList.size() - 1; i >= count; i--) {
				itemsList.get(i).dispose();
			}
		}
	}

	void setItemHeight(boolean fixScroll) {
		long topIndex = getTopIndex();
		if (fixScroll && topIndex != 0) {
			setRedraw(false);
			applyLogicalTopIndex(0);
		}

		logNotImplemented();
		if (fixScroll && topIndex != 0) {
			applyLogicalTopIndex(topIndex);
			setRedraw(true);
		}
	}

	/**
	 * Sets the height of the area which would be used to display <em>one</em>
	 * of the items in the Tree.
	 *
	 * @param itemHeight
	 *            the height of one item
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 *
	 * @since 3.2
	 */
	/* public */ void setItemHeight(int itemHeight) {
		checkWidget();
		if (itemHeight < -1) error(SWT.ERROR_INVALID_ARGUMENT);

		this.itemHeight = itemHeight;
		logNotImplemented();
		setItemHeight(true);
		setScrollWidth(null, true);
	}

	/**
	 * Marks the receiver's lines as visible if the argument is
	 * <code>true</code>, and marks it invisible otherwise. Note that some
	 * platforms draw grid lines while others may draw alternating row colors.
	 * <p>
	 * If one of the receiver's ancestors is not visible or some other condition
	 * makes the receiver not visible, marking it visible may not actually cause
	 * it to be displayed.
	 * </p>
	 *
	 * @param show
	 *            the new visibility state
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setLinesVisible(boolean show) {
		checkWidget();
		this.linesVisible = show;
        invalidateVirtualMeasurements();
		logNotImplemented();
	}

	Point getTopIndexItemPosition() {
		Rectangle columns = getColumnsArea();
		int gridLineSize = TreeItemsHandler.getGridSize(this);
		int initialHeightPosition = columns.y + columns.height + 1;

		// TreeItem checks this live X origin in its own lazy cache epoch.
		ScrollBar bar = getHorizontalBar();
		return new Point(bar == null ? 0 : -bar.getSelection(),
				initialHeightPosition + gridLineSize);
	}

	@Override
	public void setRedraw(boolean redraw) {
		checkWidget();
		super.setRedraw(redraw);
	}

	void setScrollWidth(int width) {
		logNotImplemented();
	}

	boolean setScrollWidth(TreeItem item, boolean force) {
		logNotImplemented();
		return false;
	}

	/**
	 * Selects the items at the given zero-relative indices in the receiver. The
	 * current selection is cleared before the new items are selected, and if
	 * necessary the receiver is scrolled to make the new selection visible.
	 * <p>
	 * Indices that are out of range and duplicate indices are ignored. If the
	 * receiver is single-select and multiple indices are specified, then all
	 * indices are ignored.
	 * </p>
	 *
	 * @param indices
	 *            the indices of the items to select
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the array of indices is
	 *                null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 *
	 * @see Tree#deselectAll()
	 * @see Tree#select(int[])
	 */
	public void setSelection(int[] indices) {
		checkWidget();
		if (indices == null) error(SWT.ERROR_NULL_ARGUMENT);

		deselectAll();
		int length = indices.length;
		if (length == 0 || ((style & SWT.SINGLE) != 0 && length > 1)) {
			return;
		}

		TreeSet<Integer> set = new TreeSet<>((o1, o2) -> o2 - o1);

		Arrays.stream(indices).forEach(i -> {
			if (i >= 0 && i < itemsList.size()) {
				set.add(i);
			}
		});

		List<Integer> list = new ArrayList<>(set);

		int focusIndex = -1;
		for (int i = list.size() - 1; i >= 0; --i) {
			int index = list.get(i);
			if (index != -1) {
				select(focusIndex = index);
			}
		}
		if (focusIndex != -1) {
			setFocusIndex(focusIndex);
		}
		showSelection();
	}

	/**
	 * Sets the receiver's selection to the given item. The current selection is
	 * cleared before the new item is selected, and if necessary the receiver is
	 * scrolled to make the new selection visible.
	 * <p>
	 * If the item is not in the receiver, then it is ignored.
	 * </p>
	 *
	 * @param item
	 *            the item to select
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the item is null</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the item has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 *
	 * @since 3.2
	 */
	public void setSelection(TreeItem item) {
		checkWidget();

		if (item == null) error(SWT.ERROR_NULL_ARGUMENT);

		setSelection(new TreeItem[]{item});
	}

	/**
	 * Sets the receiver's selection to be the given array of items. The current
	 * selection is cleared before the new items are selected, and if necessary
	 * the receiver is scrolled to make the new selection visible.
	 * <p>
	 * Items that are not in the receiver are ignored. If the receiver is
	 * single-select and multiple items are specified, then all items are
	 * ignored.
	 * </p>
	 *
	 * @param items
	 *            the array of items
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the array of items is
	 *                null</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if one of the items has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 *
	 * @see Tree#deselectAll()
	 * @see Tree#select(int[])
	 * @see Tree#setSelection(int[])
	 */
	public void setSelection(TreeItem[] items) {
		checkWidget();
		if (items == null) error(SWT.ERROR_NULL_ARGUMENT);

		deselectAll();
		int length = items.length;
		if (length == 0 || ((style & SWT.SINGLE) != 0 && length > 1)) {
			return;
		}

		if (length == 1 && items[0] == null) {
			return;
		}

		int focusIndex = -1;
		for (int i = length - 1; i >= 0; --i) {
			int index = indexOf(items[i]);
			if (index != -1) {
				select(focusIndex = index);
			}
		}
		if (focusIndex != -1) {
			setFocusIndex(focusIndex);
		}
		showSelection();
	}

	/**
	 * Selects the item at the given zero-relative index in the receiver. The
	 * current selection is first cleared, then the new item is selected, and if
	 * necessary the receiver is scrolled to make the new selection visible.
	 *
	 * @param index
	 *            the index of the item to select
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 *
	 * @see Tree#deselectAll()
	 * @see Tree#select(int)
	 */
	public void setSelection(int index) {
		checkWidget();
		deselectAll();
		select(index);
		if (index != -1) {
			setFocusIndex(index);
		}
		showSelection();
	}

	/**
	 * Selects the items in the range specified by the given zero-relative
	 * indices in the receiver. The range of indices is inclusive. The current
	 * selection is cleared before the new items are selected, and if necessary
	 * the receiver is scrolled to make the new selection visible.
	 * <p>
	 * Indices that are out of range are ignored and no items will be selected
	 * if start is greater than end. If the receiver is single-select and there
	 * is more than one item in the given range, then all indices are ignored.
	 * </p>
	 *
	 * @param start
	 *            the start index of the items to select
	 * @param end
	 *            the end index of the items to select
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 *
	 * @see Tree#deselectAll()
	 * @see Tree#select(int,int)
	 */
	public void setSelection(int start, int end) {
		checkWidget();

		deselectAll();
		if (end < 0 || start > end
				|| ((style & SWT.SINGLE) != 0 && start != end)) {
			return;
		}
		int count = getItemCount();
		if (count == 0 || start >= count) {
			return;
		}
		start = Math.max(0, start);
		end = Math.min(end, count - 1);
		select(start, end);
		setFocusIndex(start);
		showSelection();
	}

	/**
	 * Sets the column used by the sort indicator for the receiver. A null value
	 * will clear the sort indicator. The current sort column is cleared before
	 * the new column is set.
	 *
	 * @param column
	 *            the column used by the sort indicator or <code>null</code>
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the column is
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 *
	 * @since 3.2
	 */
	public void setSortColumn(TreeColumn column) {
		checkWidget();

		logNotImplemented();
	}

	@Override
	public void pack() {
		super.pack();
		redraw();
	}

	/**
	 * Sets the direction of the sort indicator for the receiver. The value can
	 * be one of <code>UP</code>, <code>DOWN</code> or <code>NONE</code>.
	 *
	 * @param direction
	 *            the direction of the sort indicator
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 *
	 * @since 3.2
	 */
	public void setSortDirection(int direction) {
		checkWidget();
		logNotImplemented();
	}

	void setSubImagesVisible(boolean visible) {
		logNotImplemented();
	}

	void setTreeEmpty() {
		logNotImplemented();
	}

	/**
	 * Sets the zero-relative index of the item which is currently at the top of
	 * the receiver. This index can change when items are scrolled or new items
	 * are added and removed.
	 *
	 * @param index
	 *            the index of the top item
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setTopIndex(int index) {
		checkWidget();
		if (isVirtual()) {
			setLogicalTopIndex(index);
			return;
		}
		int count = Math.max(0, getOpenedItemCount());
		// Match updateScrollBarWithTextSize's existing exact visible projection.
		int thumb = Math.min(Math.max(1, count), calculateThumb(itemsHandler.getItemsClientArea()));
		int next = Math.max(0, Math.min(index, Math.max(0, count - thumb)));
		ScrollBar bar = getVerticalBar();
		if (bar != null && !bar.isDisposed() && bar.getSelection() != next) {
			bar.setSelection(next);
		}
		applyLogicalTopIndex(next);
	}

	/**
	 * Shows the column. If the column is already showing in the receiver, this
	 * method simply returns. Otherwise, the columns are scrolled until the
	 * column is visible.
	 *
	 * @param column
	 *            the column to be shown
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the column is null</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the column has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 *
	 * @since 3.0
	 */
	public void showColumn(TreeColumn column) {
		checkWidget();

		if (!isVisible()) return;

		if (column == null) error(SWT.ERROR_NULL_ARGUMENT);
		if (column.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
		if (column.getParent() != this) return;
		int index = indexOf(column);
		if (0 > index || index >= getColumnCount()) {
			return;
		}

		Rectangle ca = getClientArea();

		int horShift = column.getBounds().x;
		if (ca.x < horShift && ca.x + ca.width > horShift) {
			return;
		}

		if (getHorizontalBar() != null) {
			getHorizontalBar()
					.setSelection(getHorizontalBar().getSelection() + horShift);
			redraw();
		}
	}

	void showItem(long index) {
        if (index < 0) return;
        synchronizeArrangements(false);
        if (index < getTopIndex() || index > itemsHandler.getLastVisibleElementIndex()) setLogicalTopIndex(index);
    }

	long getLastVisibleIndex() { return itemsHandler.getLastVisibleElementIndex(); }

	/**
	 * Shows the item. If the item is already showing in the receiver, this
	 * method simply returns. Otherwise, the items are scrolled until the item
	 * is visible.
	 *
	 * @param item
	 *            the item to be shown
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the item is null</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the item has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 *
	 * @see Tree#showSelection()
	 */
	public void showItem(TreeItem item) {
		checkWidget();

		if (item == null) error(SWT.ERROR_NULL_ARGUMENT);
		if (item.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);

		long index = arrangementIndexOf(item);
		if (index != -1) {
			showItem(index);
		}
	}

	/**
	 * Shows the selection. If the selection is already showing in the receiver,
	 * this method simply returns. Otherwise, the items are scrolled until the
	 * selection is visible.
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 *
	 * @see Tree#showItem(TreeItem)
	 */
	public void showSelection() {
		checkWidget();

		if (selectedTreeItems.isEmpty()) return;
		// TODO: check whether it is always the first selected element, which
		// should be
		// visible.

		TreeItem first = selectedTreeItems.first();

		long index = arrangementIndexOf(first);
		showItem(index);
	}

	/* public */ void sort() {
		checkWidget();
		redraw();
	}

	void updateHeaderToolTips() {
		logNotImplemented();
	}

	void updateMoveable() {
		logNotImplemented();
	}

	@Override
	protected ControlRenderer getRenderer() {
		return renderer;
	}

	@Override
	public void dispose() {
		/*
		 * Note: It is valid to attempt to dispose a widget more than once. If
		 * this happens, fail silently.
		 */

		if (isDisposed()) return;
		if (!isValidThread()) error(SWT.ERROR_THREAD_INVALID_ACCESS);

		Set<TreeColumn> columnsSet = new HashSet<>();
		Set<TreeItem> itemsSet = new HashSet<>();

		columnsSet.addAll(columnsList);
		itemsSet.addAll(itemsList);
		itemsSet.addAll(virtualItemsList.values());

		itemsList.clear();
		columnsList.clear();
		virtualItemsList.clear();
		virtualItemCount = 0;
        invalidateVirtualTopology();

		for (TreeColumn c : columnsSet) {
			c.dispose();
		}

		for (TreeItem i : itemsSet) {
			i.dispose();
		}

		super.dispose();
	}

	static void logNotImplemented() {
		if (LOG_NOT_IMPLEMENTED) {
			System.out.println("WARN: Not implemented yet: "
					+ new Throwable().getStackTrace()[1]);
		}
	}

	// TODO move this heuristic somewhere else.
	static int guessTextHeight(Tree tree) {
		return Drawing.measure(tree, gc -> {
			gc.setFont(tree.getFont());
			return gc.getFontMetrics().getHeight();
		});
	}

	Point computeTextExtent(String str) {
		return Drawing.getTextExtent(this, str, DRAW_FLAGS);
	}

	TreeColumnsHandler getColumnsHandler() {
		return columnsHandler;
	}

	TreeItemsHandler getItemsHandler() {
		return itemsHandler;
	}

	public void addTreeListener(TreeListener l) {
		addTypedListener(l, SWT.Expand, SWT.Collapse);
	}

	public void removeTreeListener(TreeListener listener) {
		checkWidget();
		if (listener == null) error(SWT.ERROR_NULL_ARGUMENT);
		if (eventTable == null) return;
		eventTable.unhook(SWT.Expand, listener);
		eventTable.unhook(SWT.Collapse, listener);
	}

	public void setInsertMark(Object object, boolean b) {
		// TODO Auto-generated method stub
	}

	public void setTopItem(TreeItem item) {
		if (item == null) error(SWT.ERROR_NULL_ARGUMENT);
		if (item.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);

		long index = arrangementIndexOf(item);

		if (index != -1) {
			this.topItem = item;
			setLogicalTopIndex(index);
			return;
		}

		throw new IllegalStateException("Invalid range for item: " + item);
	}

	public TreeItem getParentItem() {
		if (itemsList.isEmpty()) return null;

		return itemsList.get(0);
	}

	public void select(TreeItem jItem) {
		selectedTreeItems.clear();
		selectedTreeItems.add(jItem);
	}

	public TreeItem _getArrangementItem(int i) {
		return arrangementItemAt(i);
	}

	public int getOpenedItemCount() {
		if (isVirtual()) return (int) Math.min(Integer.MAX_VALUE, refreshVirtualVisibleRows());
		int count;
		{
			count = getItemCount();

			for (TreeItem v : itemsList) {
				if (v != null) {
					count += v.computeAllExpandedChildCount();
				}
			}
		}

		return count;
	}

	/**
	 * for junit only
	 *
	 * @see #getColumnIndex
	 **/
	@SuppressWarnings("unused")
	private int getColumnIndexFromOS(int column) {
		if (column >= getColumnCount()) {
			return 0;
		}

		if (columnOrder == null) {
			return column;
		}
		return columnOrder[column];
	}

	/**
	 * for junit only
	 *
	 * @see #getColumnIndex
	 **/
	@SuppressWarnings("unused")
	private Rectangle getColumnRect(int order) {
		return columnsList.get(order).getBounds();
	}

	/**
	 * for junit only
	 *
	 * @see #getColumnIndex
	 **/
	@SuppressWarnings("unused")
	private int getColumnIndex(int index) {
		return getColumnIndexFromOS(index);
	}

	@Override
	public void setFont(Font font) {
		checkWidget();
		if (font != null && font.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
		invalidateVirtualFontMetrics();
		try {
			super.setFont(font);
		} finally {
			if (!isDisposed()) invalidateVirtualFontMetrics();
		}
	}

	@Override
	public void setOrientation(int orientation) {
		checkWidget();
		invalidateVirtualMeasurements();
		try {
			super.setOrientation(orientation);
		} finally {
			if (!isDisposed()) invalidateVirtualMeasurements();
		}
	}

	@Override
	public void setTextDirection(int textDirection) {
		checkWidget();
		invalidateVirtualMeasurements();
		try {
			super.setTextDirection(textDirection);
		} finally {
			if (!isDisposed()) invalidateVirtualMeasurements();
		}
	}

	private void invalidateVirtualFontMetrics() {
		invalidateVirtualMeasurements();
		if (columnsList != null) {
			for (TreeColumn column : columnsList) {
				if (column != null && !column.isDisposed()) column.clearCache();
			}
		}
		if (columnsHandler != null) columnsHandler.clearCache();
		if (itemsHandler != null) itemsHandler.clearCache();
	}

	private void onZoomChange(Event event) {
		if (isDisposed()) return;
		invalidateVirtualFontMetrics();
		if (!isDisposed()) redraw();
	}


    private long virtualTopologyRevision, virtualRowsRevision;
    private long virtualVisibleRows;
    private boolean virtualRowsCacheValid;
    private VirtualScrollMetrics virtualScrollMetrics;
    private VirtualRowExtents virtualRowExtents;
    private boolean virtualGeometryValid, virtualWindowBuilding;
    private boolean virtualWasEmpty = true;
    private boolean virtualEndAffinity;
    private long virtualWindowFirst, virtualWindowEnd, virtualWindowCount;
    private Rectangle virtualWindowBody;
    private Point virtualWindowOrigin;

    void invalidateVirtualGeometry() { if (!isDisposed() && isVirtual()) virtualGeometryValid = false; }

    void invalidateVirtualMeasurement(TreeItem item) {
        invalidateVirtualGeometry();
        if (virtualRowExtents == null || item == null || item.isDisposed() || isDisposed()) return;
        long rank = virtualVisibleIndexOf(item);
        if (rank >= 0 && rank < virtualRowExtents.rowCount()) virtualRowExtents.clearExtent(rank);
    }

    void invalidateVirtualMeasurements() {
        invalidateVirtualGeometry();
        if (virtualRowExtents != null) virtualRowExtents.clearFrom(0);
        if (isDisposed() || !isVirtual()) return;
        var pending = new ArrayDeque<TreeItem>(virtualItemsList.values());
        while (!pending.isEmpty()) {
            TreeItem item = pending.removeLast();
            if (item == null || item.isDisposed()) continue;
            item.clearCache();
            pending.addAll(item.materializedVirtualChildren().values());
        }
    }

    private VirtualRowExtents virtualExtentFacts() {
        long count = refreshVirtualVisibleRows();
        int estimate = Math.max(1, TreeItemRenderer.guessItemHeight(this) + TreeItemsHandler.getGridSize(this));
        if (virtualRowExtents == null) {
            virtualRowExtents = new VirtualRowExtents(estimate);
            virtualRowExtents.reset(count);
        } else {
            if (virtualRowExtents.rowCount() != count) virtualRowExtents.reset(count);
            if (virtualRowExtents.estimate() != estimate) virtualRowExtents.setEstimate(estimate);
        }
        return virtualRowExtents;
    }

    long virtualInsertionRank(TreeItem parentItem, int coordinate) {
        refreshVirtualVisibleRows();
        long first = 0;
        if (parentItem != null) {
            first = virtualVisibleIndexOf(parentItem);
            if (first < 0 || !parentItem.getExpanded()) return -1;
            first = Math.addExact(first, 1L);
        }
        first = Math.addExact(first, coordinate);
        for (TreeItem sibling : materializedVirtualChildren(parentItem).headMap(coordinate, false).values())
            if (sibling.getExpanded()) first = Math.addExact(first, sibling.virtualVisibleExtraRows);
        return first;
    }

    void virtualRowsInserted(long first, long rows) {
        invalidateVirtualTopology();
        virtualEndAffinity = false;
        invalidateVirtualGeometry();
        if (virtualRowExtents == null || rows == 0 || first < 0) return;
        long now = refreshVirtualVisibleRows();
        if (virtualRowExtents.rowCount() + rows == now && first <= virtualRowExtents.rowCount())
            virtualRowExtents.insert(first, rows);
        else virtualRowExtents.reset(now);
    }

    void virtualRowsRemoved(long first, long rows) {
        invalidateVirtualTopology();
        virtualEndAffinity = false;
        invalidateVirtualGeometry();
        if (virtualRowExtents == null || rows == 0 || first < 0) return;
        long now = refreshVirtualVisibleRows();
        if (virtualRowExtents.rowCount() - rows == now && first + rows <= virtualRowExtents.rowCount())
            virtualRowExtents.remove(first, rows);
        else virtualRowExtents.reset(now);
    }

    long virtualDescendantRows(TreeItem item) {
        if (isDisposed() || item == null || item.isDisposed()) return 0;

        if (!isVirtual() || virtualVisibleIndexOf(item) < 0 || !item.getExpanded()) return 0;
        refreshVirtualVisibleRows();
        return item.virtualVisibleExtraRows;
    }

    void virtualChildrenRemoved(TreeItem item, long rows) {
        invalidateVirtualTopology();
        long rank = virtualVisibleIndexOf(item);
        virtualRowsRemoved(rank < 0 ? -1 : rank + 1, rows);
    }

    void virtualTopologyReset() {
        invalidateVirtualTopology();
        virtualEndAffinity = false;
        invalidateVirtualGeometry();
        if (virtualRowExtents != null) virtualRowExtents.reset(refreshVirtualVisibleRows());
    }

    TreeItem arrangementItemAt(long index) {
        return isVirtual() ? virtualItemAt(index) : treeItemsArrangement.get(Math.toIntExact(index));
    }

    long logicalOpenedCount() {
        return isVirtual() ? refreshVirtualVisibleRows() : getOpenedItemCount();
    }

    private void setLogicalTopIndex(long index) {
        long count = logicalOpenedCount();
        index = Math.max(0L, Math.min(index, Math.max(0L, count - 1L)));
        // Exact caller intent precedes equality and native int projection.
        virtualEndAffinity = isVirtual() && count != 0 && index == count - 1L;
        applyLogicalTopIndex(index);
    }

    private void applyLogicalTopIndex(long index) {
        index = Math.max(0L, Math.min(index, Math.max(0L, logicalOpenedCount() - 1L)));
        if (index == topIndex) return;
        topIndex = index;
        invalidateVirtualGeometry();
        if (mouseHoverElement instanceof TreeItem) mouseHoverElement = null;
        redraw();
    }

    private long virtualLastTopRow(VirtualRowExtents extents, int bodyHeight) {
        long count = extents.rowCount();
        if (count == 0) return 0;
        long offset = extents.maxScrollOffset(Math.max(0, bodyHeight));
        long row = extents.rowAt(offset);
        if (extents.offsetOf(row) < offset) row = Math.addExact(row, 1L);
        return Math.max(0L, Math.min(count - 1L, row));
    }

    void completeVirtualMeasurements(List<TreeItem> expected) {
        if (!isVirtualWindowCurrent(expected) || virtualRowExtents == null
                || virtualScrollMetrics == null) return;
        long lastTop = virtualLastTopRow(virtualRowExtents, virtualWindowBody.height);
        if (lastTop != virtualScrollMetrics.maxTopRow()) {
            updateScrollBarWithTextSize();
            if (!isDisposed()) redraw();
        }
    }

    boolean isVirtualWindowCurrent(List<TreeItem> expected) {
        if (isDisposed() || !virtualGeometryValid || treeItemsArrangement != expected
                || topIndex != virtualWindowFirst || refreshVirtualVisibleRows() != virtualWindowCount) return false;
        return virtualWindowBody.equals(itemsHandler.getItemsClientArea())
                && virtualWindowOrigin.equals(getTopIndexItemPosition());
    }

    boolean isVirtualRowCurrent(List<TreeItem> expected, TreeItem item, long rank) {
        if (isDisposed() || item == null || item.isDisposed()) return false;

        if (!isVirtual()) return !isDisposed() && item != null && !item.isDisposed();
        if (!isVirtualWindowCurrent(expected) || item == null || item.isDisposed()
                || rank < virtualWindowFirst || rank >= virtualWindowEnd) return false;
        int local = Math.toIntExact(rank - virtualWindowFirst);
        return expected.get(local) == item && virtualVisibleIndexOf(item) == rank;
    }

    boolean isVirtualResident(TreeItem item) {
        if (isDisposed() || item == null || item.isDisposed()) return false;

        if (!isVirtual() || !isVirtualWindowCurrent(treeItemsArrangement)) return false;
        return isVirtualRowCurrent(treeItemsArrangement, item, virtualVisibleIndexOf(item));
    }

    long virtualItemY(TreeItem item) {
        List<TreeItem> expected = treeItemsArrangement;
        long rank = virtualVisibleIndexOf(item);
        VirtualRowExtents extents = virtualExtentFacts();
        int local = Math.toIntExact(rank - virtualWindowFirst);
        for (int i = 0; i < local; i++) {
            TreeItem previous = expected.get(i);
            int height = TreeItemsHandler.getItemsHeight(previous);
            if (!isVirtualRowCurrent(expected, item, rank)) return virtualWindowOrigin.y;
            extents.setExtent(virtualWindowFirst + i, height);
        }
        return Math.addExact((long) virtualWindowOrigin.y,
                Math.subtractExact(extents.offsetOf(rank), extents.offsetOf(virtualWindowFirst)));
    }

    void recordVirtualExtent(TreeItem item, int height) {
        if (!isVirtualResident(item)) return;
        virtualExtentFacts().setExtent(virtualVisibleIndexOf(item),
                Math.addExact(height, TreeItemsHandler.getGridSize(this)));
    }


    long lastVisibleVirtualIndex() {
        synchronizeArrangements(false);
        List<TreeItem> expected = treeItemsArrangement;
        if (!isVirtualWindowCurrent(expected)) return topIndex - 1;
        long last = topIndex - 1;
        long bottom = (long) virtualWindowBody.y + virtualWindowBody.height;
        for (int local = 0; local < expected.size(); local++) {
            long rank = virtualWindowFirst + local;
            TreeItem item = expected.get(local);
            long y = virtualItemY(item);
            if (!isVirtualRowCurrent(expected, item, rank) || y >= bottom) break;
            if (!checkData(item, true) || !isVirtualRowCurrent(expected, item, rank)) { redraw(); break; }
            int height = TreeItemsHandler.getItemsHeight(item);
            if (!isVirtualRowCurrent(expected, item, rank)) { redraw(); break; }
            virtualExtentFacts().setExtent(rank, height);
            last = rank;
            if (y + height >= bottom) break;
        }
        completeVirtualMeasurements(expected);
        return isVirtualWindowCurrent(expected) ? last : topIndex - 1L;
    }

    void redrawVirtualItem(TreeItem item) {
        if (!isVirtualResident(item)) { if (!isDisposed()) redraw(); return; }
        List<TreeItem> expected = treeItemsArrangement;
        long rank = virtualVisibleIndexOf(item);
        long y = virtualItemY(item);
        if (!isVirtualRowCurrent(expected, item, rank)) { if (!isDisposed()) redraw(); return; }
        long bottom = (long) virtualWindowBody.y + virtualWindowBody.height;
        if (y >= bottom) return;
        Point size = item.getSize();
        if (!isVirtualRowCurrent(expected, item, rank)) { if (!isDisposed()) redraw(); return; }
        long end = y + size.y;
        if (end <= virtualWindowBody.y) return;
        int visibleTop = Math.toIntExact(Math.max(y, virtualWindowBody.y));
        int visibleBottom = Math.toIntExact(Math.min(end, bottom));
        redraw(virtualWindowBody.x, visibleTop, virtualWindowBody.width,
                Math.max(0, visibleBottom - visibleTop), true);
    }

    private TreeItem virtualEventItemInPixels(Point point) {
        synchronizeArrangements(false);
        if (isDisposed()) return null;
        List<TreeItem> expected = treeItemsArrangement;
        if (!isVirtualWindowCurrent(expected)) { if (!isDisposed()) redraw(); return null; }
        Rectangle body = itemsHandler.getItemsClientArea();
        if (!body.contains(point)) return null;
        long bottom = (long) body.y + body.height;
        for (int local = 0; local < expected.size(); local++) {
            TreeItem item = expected.get(local);
            long rank = virtualWindowFirst + local;
            long y = virtualItemY(item);
            if (!isVirtualRowCurrent(expected, item, rank)) { if (!isDisposed()) redraw(); return null; }
            if (y >= bottom) break;
            if (!checkData(item, true) || !isVirtualRowCurrent(expected, item, rank)) { if (!isDisposed()) redraw(); return null; }
            Rectangle bounds = item.getBounds();
            if (!isVirtualRowCurrent(expected, item, rank)) { if (!isDisposed()) redraw(); return null; }
            if (item.isInArrowArea(point) || item.isInCheckArea(point)) return item;
            if (bounds.contains(point)) return item;
        }
        return null;
    }

    private void handleVirtualMouseDown(Event event, Point point) {
        TreeItem item = virtualEventItemInPixels(point);
        if (item == null || item.isDisposed()) return;
        List<TreeItem> expected = treeItemsArrangement;
        long rank = virtualVisibleIndexOf(item);
        if (!isVirtualRowCurrent(expected, item, rank)) { if (!isDisposed()) redraw(); return; }
        if (event.button == 3 && selectedTreeItems.contains(item)) return;
        if (item.isInArrowArea(point) && item.toggleExpand()) {
            if (!isDisposed()) synchronizeArrangements(true);
            return;
        }
        if (!isVirtualRowCurrent(expected, item, rank)) { if (!isDisposed()) redraw(); return; }
        if (item.isInCheckArea(point)) {
            item.toggleCheck();
            if (!isDisposed()) redraw();
            return;
        }
        if (!item.getBounds().contains(point)) return;
        if (!isVirtualRowCurrent(expected, item, rank)) { if (!isDisposed()) redraw(); return; }
        if ((style & SWT.MULTI) == 0 || !ctrlPressed) {
            selectedTreeItems.clear();
            selectedTreeItems.add(item);
        } else if (!selectedTreeItems.remove(item)) selectedTreeItems.add(item);
        redraw();
    }

}
