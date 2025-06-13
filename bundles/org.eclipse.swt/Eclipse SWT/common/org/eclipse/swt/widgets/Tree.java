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
	TreeSet<TreeItem> selectedTreeItems = new TreeSet<>((o1, o2) -> Integer
			.compare(arrangementIndexOf(o1), arrangementIndexOf(o2)));
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
	private int topIndex;

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
		ScrollBar vBar = getVerticalBar();

		if (vBar != null) {
			int vs = vBar.getSelection();
			setTopIndex(vs);
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
		if (event.widget == verticalBar) {
			onScrollBar(event);
		}

		// TODO also the scrollbars will be handled here

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
		if (!isVisible()) {
			return;
		}

		Rectangle ca = getClientArea();

		// remove column height, columns must be ignored for the scrolling.
		int colHeight = getColumnHeight();
		ca.y += colHeight;
		ca.height -= colHeight;

		Point treeSize = computeDefaultSize();

		if (verticalBar != null && getItemCount() > 0) {
			int thumb = calculateThumb(ca);
			verticalBar.setThumb(thumb);

			int count = getOpenedItemCount();

			verticalBar.setMaximum(count + 1);
			verticalBar.setMinimum(0);
			verticalBar.setVisible(true);
			verticalBar.setIncrement(1);
			verticalBar.setPageIncrement(thumb);

			if (horizontalBar != null) {
				horizontalBar.setMaximum(getTotalColumnWidth() + 10);
				horizontalBar.setMinimum(0);
				horizontalBar.setThumb(ca.width);
				horizontalBar.setVisible(treeSize.x > ca.width);
			}
		}
	}

	private int calculateThumb(Rectangle ca) {
		return ca.height / getItemHeight() - 1;
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

		for (TreeColumn c : columnsSet) {
			c.dispose();
		}

		for (TreeItem i : itemsSet) {
			i.dispose();
		}
	}

	private void onMouseDown(Event e) {
		Point p = new Point(e.x, e.y);

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

			final int max = Math.min(treeItemsArrangement.size(),
					itemsHandler.getLastVisibleElementIndex());
			for (int i = getTopIndex(); i <= max; i++) {
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
		renderer.paint(event.gc);
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
			if (index < virtualItemCount) {
				TreeItem e = virtualItemsList.get(index);
				if (e == null && create) {
					e = new TreeItem(this, SWT.None, index);
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
			sendEvent(SWT.SetData, event);
			// widget could be disposed at this point
			currentItem = oldItem;
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
				item.redraw();
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
		checkWidget();

		if (getItemCount() == 0)
			return;

		if (isVirtual()) {
			for (Map.Entry<Integer, TreeItem> e : virtualItemsList.entrySet()) {
				final TreeItem value = e.getValue();
				if (value != null) {
					value.clear();
				}
			}
			return;
		}

		clear(0, getItemCount() - 1);
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
		if (getItemCount() == 0) return;

		if (isVirtual()) {
			for (Map.Entry<Integer, TreeItem> e : virtualItemsList.entrySet()) {
				final TreeItem value = e.getValue();
				if (value != null) {
					value.clear();
				}
			}
			return;
		}

		clear(0, getItemCount() - 1);
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

		if (sortColumn == null) {
			sortColumn = column;
		}

		moveTextsItemsToRight(index);

		final TreeItem[] items = getItems();
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
		for (TreeItem i : itemsList) {
			i.moveTextToRightAt(index);
		}
	}

	private void moveTextsItemsToLeft(int index) {
		for (TreeItem i : itemsList) {
			i.moveTextsItemsToLeft(index);
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
			TreeItem previous = virtualItemsList.get(index);
			int previousIndex = index;

			// move all other elements one up until an element was not yet set
			while (previous != null) {
				virtualItemsList.remove(previousIndex);

				TreeItem nextPrevious = virtualItemsList.get(previousIndex + 1);
				virtualItemsList.put(previousIndex + 1, previous);
				previous = nextPrevious;
				previousIndex = previousIndex + 1;
			}

			if (previousIndex >= virtualItemCount) {
				virtualItemCount = previousIndex + 1;
			}

			virtualItemsList.put(index, item);

			if (index >= virtualItemCount) {
				virtualItemCount = index + 1;
			}
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
			Rectangle ca = getClientArea();
			int itemHeight = TreeItemRenderer.guessItemHeight(this);
			int visibleElements = ca.height / itemHeight + 2;
			visibleElements += getTopIndex();

			boolean treeEmpty = treeItemsArrangement.isEmpty();

			for (TreeItem i : treeItemsArrangement) {
				i.clearCache();
			}
			treeItemsArrangement.clear();

			for (TreeItem i : getItemsUpNumber(visibleElements)) {
				addToArrangements(i, visibleElements);
			}

			if (treeEmpty && !treeItemsArrangement.isEmpty()) {
				notifyListeners(SWT.EmptinessChanged, new Event());
			}

			if (!treeEmpty && treeItemsArrangement.isEmpty()) {
				Event e = new Event();
				e.detail = 1;
				notifyListeners(SWT.EmptinessChanged, e);
			}

			updateScrollBarWithTextSize();

			if (redraw) {
				redraw();
			}

			return;
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

		treeItemsArrangement.add(i);

		if (upToElements != null) {
			upToElements = upToElements - 1;

			if (upToElements <= 0) {
				return;
			}
		}

		if (i.getExpanded()) {
			for (TreeItem it : i.getItemsUpToNumber(upToElements)) {
				it.clearCache();
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
		selectedTreeItems.remove(item);

		if (mouseHoverElement == item) {
			mouseHoverElement = null;
		}

		if (!isVirtual()) {
			itemsList.remove(item);
			selectedTreeItems.remove(item);

			synchronizeArrangements(true);
		}
		// for virtual items, we have to take care, that these are not in
		// virtualItemsList
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
		for (int i = getTopIndex(); i < Math.min(treeItemsArrangement.size(),
				itemsHandler.getLastVisibleElementIndex() + 5); i++) {
			TreeItem it = _getArrangementItem(i);
			if (it != null && it.getBounds().contains(point)) {
				return it;
			}
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
			final int topIndex = getTopIndex();
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
	int getTopIndex() {
		checkWidget();
		return topIndex;
	}

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

	int arrangementIndexOf(TreeItem item) {
		return treeItemsArrangement.indexOf(item);
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
				TreeItem item = _getItem(index, false);
				if (item != null) {
					item.dispose();
				}
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
			Set<TreeItem> s = new HashSet<>(virtualItemsList.values());
			virtualItemsList.clear();
			virtualItemCount = 0;
			s.forEach(Widget::dispose);
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
			boolean redraw = count > virtualItemCount;
			this.virtualItemCount = count;

			while (!virtualItemsList.isEmpty()) {
				int key = virtualItemsList.lastKey();
				if (key >= count) {
					virtualItemsList.remove(key);
				} else {
					break;
				}
			}

			if (redraw) {
				redraw();
			}

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
		int topIndex = getTopIndex();
		if (fixScroll && topIndex != 0) {
			setRedraw(false);
			setTopIndex(0);
		}

		logNotImplemented();
		if (fixScroll && topIndex != 0) {
			setTopIndex(topIndex);
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
		logNotImplemented();
	}

	Point getTopIndexItemPosition() {
		Rectangle columns = getColumnsArea();
		int gridLineSize = TreeItemsHandler.getGridSize(this);
		int initialHeightPosition = columns.y + columns.height + 1;

		return new Point(columns.x, initialHeightPosition + gridLineSize);
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

		if (index == getTopIndex())
			return;

		if (getItemCount() == 0) {
			topIndex = 0;
			return;
		}

		if (index > getOpenedItemCount()) {
			index = getItemCount() - 1;
		}

		topIndex = index;

		if (mouseHoverElement instanceof TreeItem) {
			mouseHoverElement = null;
		}

		redraw();
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

	void showItem(int index) {
		if (index < getTopIndex()
				|| index > itemsHandler.getLastVisibleElementIndex()) {
			setTopIndex(index);
			redraw();
		}
	}

	int getLastVisibleIndex() {
		return itemsHandler.getLastVisibleElementIndex();
	}

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

		int index = indexOf(item);
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

		int index = indexOf(first);
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
		final GC gc = new GC(tree);
		try {
			gc.setFont(tree.getFont());
			return gc.getFontMetrics().getHeight();
		} finally {
			gc.dispose();
		}
	}

	Point computeTextExtent(String str) {
		final GC gc = new GC(this);
		try {
			gc.setFont(getFont());
			return gc.textExtent(str, DRAW_FLAGS);
		} finally {
			gc.dispose();
		}
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

		int index = treeItemsArrangement.indexOf(item);

		if (index != -1) {
			this.topItem = item;
			setTopIndex(index);
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
		return treeItemsArrangement.get(i);
	}

	public int getOpenedItemCount() {
		int count;
		if (isVirtual()) {
			count = getItemCount();

			for (Map.Entry<Integer, TreeItem> e : virtualItemsList.entrySet()) {
				if (e.getValue() != null) {
					var v = e.getValue();
					count += v.computeAllExpandedChildCount();
				}
			}
		} else {
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

}
