/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * Portion Copyright (c) 2009-2010 compeople AG (http://www.compeople.de).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Compeople AG	- QtJambi/Qt based implementation for Windows/Mac OS X/Linux
 *******************************************************************************/
package org.eclipse.swt.widgets;

import java.util.ArrayList;
import java.util.List;

import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt.DropActions;
import com.trolltech.qt.core.Qt.ScrollBarPolicy;
import com.trolltech.qt.core.Qt.SortOrder;
import com.trolltech.qt.gui.QAbstractItemDelegate;
import com.trolltech.qt.gui.QDragEnterEvent;
import com.trolltech.qt.gui.QDragLeaveEvent;
import com.trolltech.qt.gui.QDragMoveEvent;
import com.trolltech.qt.gui.QDropEvent;
import com.trolltech.qt.gui.QHeaderView;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QStyleOptionViewItem;
import com.trolltech.qt.gui.QTreeWidget;
import com.trolltech.qt.gui.QTreeWidgetItem;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QAbstractItemView.DragDropMode;
import com.trolltech.qt.gui.QAbstractItemView.ScrollHint;
import com.trolltech.qt.gui.QAbstractItemView.SelectionBehavior;
import com.trolltech.qt.gui.QAbstractItemView.SelectionMode;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;

/**
 * Instances of this class provide a selectable user interface object that
 * displays a hierarchy of items and issues notification when an item in the
 * hierarchy is selected.
 * <p>
 * The item children that may be added to instances of this class must be of
 * type <code>TreeItem</code>.
 * </p>
 * <p>
 * Style <code>VIRTUAL</code> is used to create a <code>Tree</code> whose
 * <code>TreeItem</code>s are to be populated by the client on an on-demand
 * basis instead of up-front. This can provide significant performance
 * improvements for trees that are very large or for which <code>TreeItem</code>
 * population is expensive (for example, retrieving values from an external
 * source).
 * </p>
 * <p>
 * Here is an example of using a <code>Tree</code> with style
 * <code>VIRTUAL</code>: <code><pre>
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
 * </pre></code>
 * </p>
 * <p>
 * Note that although this class is a subclass of <code>Composite</code>, it
 * does not normally make sense to add <code>Control</code> children to it, or
 * set a layout on it, unless implementing something like a cell editor.
 * </p>
 * <p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SINGLE, MULTI, CHECK, FULL_SELECTION, VIRTUAL, NO_SCROLL</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection, DefaultSelection, Collapse, Expand, SetData, MeasureItem,
 * EraseItem, PaintItem</dd>
 * </dl>
 * </p>
 * <p>
 * Note: Only one of the styles SINGLE and MULTI may be specified.
 * </p>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * 
 * @see <a href="http://www.eclipse.org/swt/snippets/#tree">Tree, TreeItem,
 *      TreeColumn snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example:
 *      ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Tree extends Composite {
	private List<TreeItem> items;
	private List<TreeColumn> columns;
	TreeColumn sortColumn;
	int sortDirection;
	boolean customDraw;
	private boolean linesVisible;
	private static final int GRID_WIDTH = 1;
	private TreeItemDelegate itemDelegate;

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
	 * @see SWT#VIRTUAL
	 * @see SWT#NO_SCROLL
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public Tree(Composite parent, int style) {
		super(parent, checkStyle(style));
	}

	@Override
	protected void connectSignals() {
		QTreeWidget tree = getQTreeWidget();

		tree.itemExpanded.connect(this, "qtTreeItemExpandedEvent(QTreeWidgetItem)"); //$NON-NLS-1$
		tree.itemCollapsed.connect(this, "qtTreeItemCollapsedEvent(QTreeWidgetItem)"); //$NON-NLS-1$
		tree.itemDoubleClicked.connect(this, "qtTreeItemDoubleClickedEvent(QTreeWidgetItem,Integer)"); //$NON-NLS-1$
		tree.itemSelectionChanged.connect(this, "qtTreeSelectionChanged()"); //$NON-NLS-1$
	}

	@Override
	QWidget createQWidget(int style) {
		state &= ~(CANVAS | THEME_BACKGROUND);
		items = new ArrayList<TreeItem>(4);
		columns = new ArrayList<TreeColumn>(4);

		QTreeWidget tree = new MyQTreeWidget();
		tree.setHeaderHidden(true);

		if ((style & SWT.NO_SCROLL) != 0) {
			tree.setHorizontalScrollBarPolicy(ScrollBarPolicy.ScrollBarAlwaysOff);
			tree.setVerticalScrollBarPolicy(ScrollBarPolicy.ScrollBarAlwaysOff);
		}

		// default value is singleSelection
		if ((style & SWT.MULTI) != 0) {
			tree.setSelectionMode(SelectionMode.ExtendedSelection);
		} else {
			tree.setSelectionMode(SelectionMode.SingleSelection);
		}

		if ((style & SWT.FULL_SELECTION) != 0) {
			tree.setSelectionBehavior(SelectionBehavior.SelectRows);
		} else {
			tree.setSelectionBehavior(SelectionBehavior.SelectItems);
		}

		QAbstractItemDelegate originalItemDelegate = tree.itemDelegate();
		itemDelegate = new TreeItemDelegate(originalItemDelegate);
		tree.setItemDelegate(itemDelegate);

		setQMasterWidget(tree);
		return tree.viewport();
	}

	QTreeWidget getQTreeWidget() {
		return (QTreeWidget) getQMasterWidget();
	}

	@Override
	protected void checkAndUpdateBorder() {
		checkAndUpdateBorder(getQMasterWidget());
		super.checkAndUpdateBorder();
	}

	@Override
	protected Color getDefaultBackgroundColor() {
		return display.getSystemColor(SWT.COLOR_LIST_BACKGROUND);
	}

	static int checkStyle(int style) {
		/*
		 * Feature in Windows. Even when WS_HSCROLL or WS_VSCROLL is not
		 * specified, Windows creates trees and tables with scroll bars. The fix
		 * is to set H_SCROLL and V_SCROLL.
		 * 
		 * NOTE: This code appears on all platforms so that applications have
		 * consistent scroll bar behavior.
		 */
		if ((style & SWT.NO_SCROLL) == 0) {
			style |= SWT.H_SCROLL | SWT.V_SCROLL;
		}
		/*
		 * Note: Windows only supports TVS_NOSCROLL and TVS_NOHSCROLL.
		 */
		if ((style & SWT.H_SCROLL) != 0 && (style & SWT.V_SCROLL) == 0) {
			style |= SWT.V_SCROLL;
		}
		return checkBits(style, SWT.SINGLE, SWT.MULTI, 0, 0, 0, 0);
	}

	@Override
	public void setDragEnabled(boolean enabled) {
		getQTreeWidget().setDragEnabled(enabled);
	}

	@Override
	public void setAcceptDrops(boolean accept) {
		super.setAcceptDrops(accept);
		getQTreeWidget().setDragDropMode(DragDropMode.DragDrop);
		getQTreeWidget().setDropIndicatorShown(true);
	}

	public void highlightItem(TreeItem item) {
		System.out.println("highlightItem: " + item);
		if (item != null) {
			getQTreeWidget().setCurrentItem(item.getQItem());
		} else {
			getQTreeWidget().setCurrentItem(null);
		}
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
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.Selection, typedListener);
		addListener(SWT.DefaultSelection, typedListener);
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when an item in the receiver is expanded or collapsed by sending it one
	 * of the messages defined in the <code>TreeListener</code> interface.
	 * 
	 * @param listener
	 *            the listener which should be notified
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
	 * @see TreeListener
	 * @see #removeTreeListener
	 */
	public void addTreeListener(TreeListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.Expand, typedListener);
		addListener(SWT.Collapse, typedListener);
	}

	@Override
	protected void checkSubclass() {
		if (!isValidSubclass()) {
			error(SWT.ERROR_INVALID_SUBCLASS);
		}
	}

	/**
	 * Clears the item at the given zero-relative index in the receiver. The
	 * text, icon and other attributes of the item are set to the default value.
	 * If the tree was created with the <code>SWT.VIRTUAL</code> style, these
	 * attributes are requested again as needed.
	 * 
	 * @param index
	 *            the index of the item to clear
	 * @param all
	 *            <code>true</code> if all child items of the indexed item
	 *            should be cleared recursively, and <code>false</code>
	 *            otherwise
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_RANGE - if the index is not between 0
	 *                and the number of elements in the list minus 1 (inclusive)
	 *                </li>
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
	 * @since 3.2
	 */
	public void clear(int index, boolean all) {
		checkWidget();
		if (_getItemCount() == 0) {
			return;
		}
		validateItemIndex(index);
		items.get(index).clear(0, all);
	}

	private void validateItemIndex(int index) {
		if (index < 0 || index >= _getItemCount()) {
			error(SWT.ERROR_INVALID_RANGE);
		}
	}

	/**
	 * Clears all the items in the receiver. The text, icon and other attributes
	 * of the items are set to their default values. If the tree was created
	 * with the <code>SWT.VIRTUAL</code> style, these attributes are requested
	 * again as needed.
	 * 
	 * @param all
	 *            <code>true</code> if all child items should be cleared
	 *            recursively, and <code>false</code> otherwise
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
	 * @since 3.2
	 */
	public void clearAll(boolean all) {
		checkWidget();
		for (TreeItem item : items) {
			item.clear(0, all);
		}
	}

	void addColumn(TreeColumn column, int index) {
		int columnCount = columns.size();
		if (!(0 <= index && index <= columnCount)) {
			error(SWT.ERROR_INVALID_RANGE);
		}

		if (index == columnCount) {
			columns.add(column);
		} else {
			columns.add(index, column);
		}
		//		QHeaderView header = getQTreeWidget().header();
		//		if (index >= columnCount) {
		//			header.addAction(column.getQAction());
		//		} else {
		//			QAction before = header.actions().get(index);
		//			header.insertAction(before, column.getQAction());
		//		}
	}

	void removeColumn(TreeColumn treeColumn) {
		columns.remove(treeColumn);
		//getQTreeWidget().headerItem().removeAction(treeColumn.getQAction());
	}

	void addItem(TreeItem item, int index) {
		if (index >= 0 && index < _getItemCount()) {
			items.add(index, item);
			getQTreeWidget().insertTopLevelItem(index, item.getQItem());
		} else {
			items.add(item);
			getQTreeWidget().addTopLevelItem(item.getQItem());
		}
	}

	void removeItem(TreeItem item) {
		items.remove(item);
		getQTreeWidget().invisibleRootItem().removeChild(item.getQItem());
	}

	/**
	 * Deselects an item in the receiver. If the item was already deselected, it
	 * remains deselected.
	 * 
	 * @param item
	 *            the item to be deselected
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the item is null</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the item has been disposed
	 *                </li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.4
	 */
	public void deselect(TreeItem item) {
		checkWidget();
		if (item == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (item.isDisposed()) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}

		item.getQItem().setSelected(false);
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
		getQTreeWidget().clearSelection();
	}

	/**
	 * Returns the width in pixels of a grid line.
	 * 
	 * @return the width of a grid line in pixels
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.1
	 */
	public int getGridLineWidth() {
		checkWidget();
		return GRID_WIDTH;
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
	 * @since 3.1
	 */
	public int getHeaderHeight() {
		checkWidget();
		return getQTreeWidget().header().height();
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
	 * 
	 * @since 3.1
	 */
	public boolean getHeaderVisible() {
		checkWidget();
		return !getQTreeWidget().isHeaderHidden();
	}

	/**
	 * Returns the column at the given, zero-relative index in the receiver.
	 * Throws an exception if the index is out of range. Columns are returned in
	 * the order that they were created. If no <code>TreeColumn</code>s were
	 * created by the programmer, this method will throw
	 * <code>ERROR_INVALID_RANGE</code> despite the fact that a single column of
	 * data may be visible in the tree. This occurs when the programmer uses the
	 * tree like a list, adding items but never creating a column.
	 * 
	 * @param index
	 *            the index of the column to return
	 * @return the column at the given index
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_RANGE - if the index is not between 0
	 *                and the number of elements in the list minus 1 (inclusive)
	 *                </li>
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
	 * 
	 * @since 3.1
	 */
	public TreeColumn getColumn(int index) {
		checkWidget();
		if (!(0 <= index && index < columns.size())) {
			error(SWT.ERROR_INVALID_RANGE);
		}
		return columns.get(index);
	}

	/**
	 * Returns the number of columns contained in the receiver. If no
	 * <code>TreeColumn</code>s were created by the programmer, this value is
	 * zero, despite the fact that visually, one column of items may be visible.
	 * This occurs when the programmer uses the tree like a list, adding items
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
	 * 
	 * @since 3.1
	 */
	public int getColumnCount() {
		checkWidget();
		return _getColumnCount();
	}

	int _getColumnCount() {
		return columns.size();
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
	 * @since 3.2
	 */
	public int[] getColumnOrder() {
		checkWidget();
		if (_getColumnCount() == 0) {
			return new int[0];
		}
		int[] order = new int[_getColumnCount()];
		QHeaderView header = getQTreeWidget().header();
		for (int i = 0; i < order.length; i++) {
			order[i] = header.visualIndex(i);
		}
		return order;
	}

	/**
	 * Returns an array of <code>TreeColumn</code>s which are the columns in the
	 * receiver. Columns are returned in the order that they were created. If no
	 * <code>TreeColumn</code>s were created by the programmer, the array is
	 * empty, despite the fact that visually, one column of items may be
	 * visible. This occurs when the programmer uses the tree like a list,
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
	 * 
	 * @since 3.1
	 */
	public TreeColumn[] getColumns() {
		checkWidget();
		TreeColumn[] result = new TreeColumn[_getColumnCount()];
		int i = 0;
		for (TreeColumn col : columns) {
			result[i] = col;
			i++;
		}
		return result;
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
	 *                and the number of elements in the list minus 1 (inclusive)
	 *                </li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.1
	 */
	public TreeItem getItem(int index) {
		checkWidget();
		if (index < 0 || index >= items.size()) {
			error(SWT.ERROR_INVALID_RANGE);
		}
		return items.get(index);
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
	 *         selectable item
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
		if (point == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		return _getItem(point);
	}

	private TreeItem _getItem(Point point) {
		QTreeWidgetItem qItem = getQTreeWidget().itemAt(point.x, point.y);
		for (TreeItem item : items) {
			if (item.getQItem() == qItem) {
				return item;
			}
			TreeItem ti = item.getItem(qItem);
			if (ti != null) {
				return ti;
			}
		}
		return null;

	}

	/**
	 * Returns the number of items contained in the receiver that are direct
	 * item children of the receiver. The number that is returned is the number
	 * of roots in the tree.
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
		return items.size();
	}

	int _getItemCount() {
		return items.size();
	}

	/**
	 * Returns the height of the area which would be used to display
	 * <em>one</em> of the items in the tree.
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
		int itemHeight = 0;
		if (items.size() > 0) {
			itemHeight = getQTreeWidget().sizeHintForRow(0);
		} else {
			// add dummy node and get the height
			getQTreeWidget().addTopLevelItem(new QTreeWidgetItem());
			itemHeight = getQTreeWidget().sizeHintForRow(0);
			getQTreeWidget().clear();
		}
		return itemHeight;
	}

	/**
	 * Returns a (possibly empty) array of items contained in the receiver that
	 * are direct item children of the receiver. These are the roots of the
	 * tree.
	 * <p>
	 * Note: This is not the actual structure used by the receiver to maintain
	 * its list of items, so modifying the array will not affect the receiver.
	 * </p>
	 * 
	 * @return the items
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
		TreeItem[] arr = new TreeItem[_getItemCount()];
		int i = 0;
		for (TreeItem item : items) {
			arr[i] = item;
			i++;
		}
		return arr;
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
	 * 
	 * @since 3.1
	 */
	public boolean getLinesVisible() {
		checkWidget();
		return linesVisible;
	}

	/**
	 * Returns the receiver's parent item, which must be a <code>TreeItem</code>
	 * or null when the receiver is a root.
	 * 
	 * @return the receiver's parent item
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public TreeItem getParentItem() {
		checkWidget();
		return null;
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
		List<QTreeWidgetItem> selectedQItems = getQTreeWidget().selectedItems();
		if (selectedQItems.isEmpty()) {
			return new TreeItem[0];
		}

		TreeItem[] items = new TreeItem[selectedQItems.size()];
		int i = 0;
		for (QTreeWidgetItem item : selectedQItems) {
			Widget widget = display.findControl(item);
			if (widget instanceof TreeItem) {
				items[i] = (TreeItem) widget;
			}
			i++;
		}
		return items;
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
		return getQTreeWidget().selectedItems().size();
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
	 * Returns the item which is currently at the top of the receiver. This item
	 * can change when items are expanded, collapsed, scrolled or new items are
	 * added or removed.
	 * 
	 * @return the item at the top of the receiver
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 2.1
	 */
	public TreeItem getTopItem() {
		checkWidget();
		return _getItem(new Point(1, 1));
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
	 * 
	 * @since 3.1
	 */
	public int indexOf(TreeColumn column) {
		checkWidget();
		if (column == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (column.isDisposed()) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		return columns.indexOf(column);
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
	 *                <li>ERROR_INVALID_ARGUMENT - if the item has been disposed
	 *                </li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.1
	 */
	public int indexOf(TreeItem item) {
		checkWidget();
		if (item == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (item.isDisposed()) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		return items.indexOf(item);
	}

	@Override
	void releaseChildren(boolean destroy) {
		if (items != null) {
			for (TreeItem item : items) {
				if (item != null && !item.isDisposed()) {
					item.release(false);
				}
			}
			items = null;
		}
		if (columns != null) {
			for (TreeColumn column : columns) {
				if (column != null && !column.isDisposed()) {
					column.release(false);
				}
			}
			columns = null;
		}
		super.releaseChildren(destroy);
	}

	@Override
	void releaseWidget() {
		super.releaseWidget();
		customDraw = false;
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
		checkWidget();
		List<TreeItem> copyOfItems = new ArrayList<TreeItem>(items);
		for (TreeItem item : copyOfItems) {
			item.dispose();
		}
		items.clear();
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
	 * @see #addSelectionListener
	 */
	public void removeSelectionListener(SelectionListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		eventTable.unhook(SWT.Selection, listener);
		eventTable.unhook(SWT.DefaultSelection, listener);
	}

	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when items in the receiver are expanded or collapsed.
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
	 * @see TreeListener
	 * @see #addTreeListener
	 */
	public void removeTreeListener(TreeListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (eventTable == null) {
			return;
		}
		eventTable.unhook(SWT.Expand, listener);
		eventTable.unhook(SWT.Collapse, listener);
	}

	/**
	 * Display a mark indicating the point at which an item will be inserted.
	 * The drop insert item has a visual hint to show where a dragged item will
	 * be inserted when dropped on the tree.
	 * 
	 * @param item
	 *            the insert item. Null will clear the insertion mark.
	 * @param before
	 *            true places the insert mark above 'item'. false places the
	 *            insert mark below 'item'.
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the item has been disposed
	 *                </li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setInsertMark(TreeItem item, boolean before) {
		checkWidget();
		//TODO d'n'd
	}

	/**
	 * Sets the number of root-level items contained in the receiver.
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
	 * @since 3.2
	 */
	public void setItemCount(int count) {
		checkWidget();
		count = Math.max(0, count);
		int itemCount = _getItemCount();
		if (count == itemCount) {
			return;
		}
		boolean isVirtual = (style & SWT.VIRTUAL) != 0;
		if (!isVirtual) {
			setRedraw(false);
		}

		if (count < itemCount) {
			for (int i = itemCount - 1; i >= count; i--) {
				removeItem(items.get(i));
			}
			return;
		}
		if (isVirtual) {
			//TODO
		} else {
			for (int i = itemCount; i < count; i++) {
				new TreeItem(this, SWT.NONE, i);
			}
		}
		if (!isVirtual) {
			setRedraw(true);
		}
	}

	/**
	 * Sets the height of the area which would be used to display <em>one</em>
	 * of the items in the tree.
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
	/* public */void setItemHeight(int itemHeight) {
		checkWidget();
		if (itemHeight < -1) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		itemDelegate.setHeight(itemHeight);
		update();
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
	 * 
	 * @since 3.1
	 */
	public void setLinesVisible(boolean show) {
		checkWidget();
		if (linesVisible == show) {
			return;
		}
		linesVisible = show;
		getQTreeWidget().setAlternatingRowColors(show);
	}

	/**
	 * Selects an item in the receiver. If the item was already selected, it
	 * remains selected.
	 * 
	 * @param item
	 *            the item to be selected
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the item is null</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the item has been disposed
	 *                </li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.4
	 */
	public void select(TreeItem item) {
		checkWidget();
		if (item == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (item.isDisposed()) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		item.getQItem().setSelected(true);
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
		getQTreeWidget().selectAll();
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
	 * @since 3.2
	 */
	public void setColumnOrder(int[] order) {
		checkWidget();
		if (order == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		int columnCount = _getColumnCount();
		if (columnCount == 0) {
			if (order.length != 0) {
				error(SWT.ERROR_INVALID_ARGUMENT);
			}
			return;
		}
		if (order.length != columnCount) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		QHeaderView header = getQTreeWidget().header();
		for (int i = 0; i < columnCount; i++) {
			int visualIndex = header.visualIndex(i);
			header.moveSection(visualIndex, order[i]);
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
	 * 
	 * @since 3.1
	 */
	public void setHeaderVisible(boolean show) {
		checkWidget();
		getQTreeWidget().setHeaderHidden(!show);
	}

	/**
	 * Sets the receiver's selection to the given item. The current selection is
	 * cleared before the new item is selected.
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
	 *                <li>ERROR_INVALID_ARGUMENT - if the item has been disposed
	 *                </li>
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
		if (item == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		setSelection(new TreeItem[] { item });
	}

	/**
	 * Sets the receiver's selection to be the given array of items. The current
	 * selection is cleared before the new items are selected.
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
	 *                <li>ERROR_NULL_ARGUMENT - if the array of items is null</li>
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
	 */
	public void setSelection(TreeItem[] items) {
		checkWidget();
		if (items == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		int length = items.length;
		if (length == 0 || (style & SWT.SINGLE) != 0 && length > 1) {
			deselectAll();
			return;
		}
		getQTreeWidget().clearSelection();
		for (TreeItem item : items) {
			QTreeWidgetItem treeWidgetItem = item.getQItem();
			showItem(treeWidgetItem);
			treeWidgetItem.setSelected(true);
		}
	}

	private void showItem(QTreeWidgetItem item) {
		if (item.parent() != null) {
			if (!item.parent().isExpanded()) {
				item.parent().setExpanded(true);
			}
			showItem(item.parent());
		}
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
	 *                <li>ERROR_INVALID_ARGUMENT - if the column is disposed</li>
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
		if (column != null && column.isDisposed()) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		sortColumn = column;
		getQTreeWidget().sortByColumn(indexOf(column), getQSortOrder());
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
		if ((direction & (SWT.UP | SWT.DOWN)) == 0 && direction != SWT.NONE) {
			return;
		}
		sortDirection = direction;
		if (sortColumn != null && !sortColumn.isDisposed()) {
			getQTreeWidget().sortByColumn(indexOf(sortColumn), getQSortOrder());
		}
	}

	/**
	 * @return
	 */
	private SortOrder getQSortOrder() {
		if ((sortDirection & SWT.UP) != 0) {
			return SortOrder.AscendingOrder;
		}
		return SortOrder.DescendingOrder;
	}

	/**
	 * Sets the item which is currently at the top of the receiver. This item
	 * can change when items are expanded, collapsed, scrolled or new items are
	 * added or removed.
	 * 
	 * @param item
	 *            the item to be shown
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the item is null</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the item has been disposed
	 *                </li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see Tree#getTopItem()
	 * 
	 * @since 2.1
	 */
	public void setTopItem(TreeItem item) {
		checkWidget();
		if (item == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (item.isDisposed()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		_showItem(item, ScrollHint.PositionAtTop);
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
	 *                <li>ERROR_NULL_ARGUMENT - if the item is null</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the item has been disposed
	 *                </li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.1
	 */
	public void showColumn(TreeColumn column) {
		checkWidget();
		if (column == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (column.isDisposed()) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		if (column.parent != this) {
			return;
		}
		int index = indexOf(column);
		if (index == -1) {
			return;
		}
		getQTreeWidget().scrollToItem(getQTreeWidget().headerItem().child(index), ScrollHint.EnsureVisible);
	}

	/**
	 * Shows the item. If the item is already showing in the receiver, this
	 * method simply returns. Otherwise, the items are scrolled and expanded
	 * until the item is visible.
	 * 
	 * @param item
	 *            the item to be shown
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the item is null</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the item has been disposed
	 *                </li>
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
		if (item == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (item.isDisposed()) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		_showItem(item, ScrollHint.EnsureVisible);
	}

	private void _showItem(TreeItem item, ScrollHint hint) {
		getQTreeWidget().scrollToItem(item.getQItem(), hint);
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
		List<QTreeWidgetItem> selection = getQTreeWidget().selectedItems();
		if (selection != null && selection.size() > 0) {
			getQTreeWidget().scrollToItem(selection.get(0), ScrollHint.EnsureVisible);
		}
	}

	boolean sendTreeEvent(int eventType, QTreeWidgetItem item) {
		Event event = new Event();
		event.item = display.findControl(item);
		sendEvent(eventType, event, true);
		return !event.doit;
	}

	public boolean qtTreeItemCollapsedEvent(QTreeWidgetItem item) {
		return sendTreeEvent(SWT.Collapse, item);
	}

	public boolean qtTreeItemExpandedEvent(QTreeWidgetItem item) {
		return sendTreeEvent(SWT.Expand, item);
	}

	public void qtTreeItemDoubleClickedEvent(QTreeWidgetItem item, Integer column) {
		sendEvent(SWT.DefaultSelection);
	}

	private final class TreeItemDelegate extends QAbstractItemDelegate {
		private final QAbstractItemDelegate wrappedDelegate;
		private int height = -1;

		TreeItemDelegate(QAbstractItemDelegate wrappedDelegate) {
			this.wrappedDelegate = wrappedDelegate;

		}

		public void setHeight(int height) {
			this.height = height;
		}

		@Override
		public void paint(QPainter painter, QStyleOptionViewItem item, QModelIndex index) {
			wrappedDelegate.paint(painter, item, index);
		}

		@Override
		public QSize sizeHint(QStyleOptionViewItem item, QModelIndex index) {
			QSize size = wrappedDelegate.sizeHint(item, index);
			if (height != -1) {
				size.setHeight(height);
			}
			return size;
		}

	}

	public boolean qtTreeSelectionChanged() {
		List<QTreeWidgetItem> selectedItems = getQTreeWidget().selectedItems();
		if (selectedItems.size() > 1) {
			System.out.println("more than one item selected in tree, selecting first"); //$NON-NLS-1$
		}
		if (selectedItems.size() == 0) {
			return sendTreeEvent(SWT.Selection, null);
		} else {
			// TODO what if more than one element is selected
			return sendTreeEvent(SWT.Selection, selectedItems.get(0));
		}
	}

	private final class MyQTreeWidget extends QTreeWidget {

		@Override
		protected void startDrag(DropActions supportedActions) {
			//			System.out.println("MyQTreeWidget.startDrag: " + supportedActions);
		}

		@Override
		protected void dropEvent(QDropEvent event) {
			sendDropEvent(event);
		}

		@Override
		protected void dragMoveEvent(QDragMoveEvent event) {
			sendDragMoveEvent(event);
		}

		@Override
		protected void dragEnterEvent(QDragEnterEvent event) {
			sendDragEnterEvent(event);
		}

		@Override
		protected void dragLeaveEvent(QDragLeaveEvent event) {
			sendDragLeaveEvent(event);
		}

	}

}
