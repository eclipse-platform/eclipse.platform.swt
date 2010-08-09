/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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

import static com.trolltech.qt.core.Qt.ItemDataRole.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.core.Qt.DropActions;
import com.trolltech.qt.core.Qt.ItemFlag;
import com.trolltech.qt.core.Qt.ItemFlags;
import com.trolltech.qt.core.Qt.Orientation;
import com.trolltech.qt.core.Qt.ScrollBarPolicy;
import com.trolltech.qt.core.Qt.SortOrder;
import com.trolltech.qt.gui.QAbstractTableModel;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QDragEnterEvent;
import com.trolltech.qt.gui.QDragLeaveEvent;
import com.trolltech.qt.gui.QDragMoveEvent;
import com.trolltech.qt.gui.QDropEvent;
import com.trolltech.qt.gui.QHeaderView;
import com.trolltech.qt.gui.QItemSelection;
import com.trolltech.qt.gui.QTableView;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QAbstractItemView.DragDropMode;
import com.trolltech.qt.gui.QAbstractItemView.EditTrigger;
import com.trolltech.qt.gui.QAbstractItemView.SelectionBehavior;
import com.trolltech.qt.gui.QAbstractItemView.SelectionMode;
import com.trolltech.qt.gui.QHeaderView.ResizeMode;
import com.trolltech.qt.gui.QItemSelectionModel.SelectionFlag;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.qt.QtSWTConverter;
import org.eclipse.swt.internal.qt.SignalConnector;

/**
 * Instances of this class implement a selectable user interface object that
 * displays a list of images and strings and issues notification when selected.
 * <p>
 * The item children that may be added to instances of this class must be of
 * type <code>TableItem</code>.
 * </p>
 * <p>
 * Style <code>VIRTUAL</code> is used to create a <code>Table</code> whose
 * <code>TableItem</code>s are to be populated by the client on an on-demand
 * basis instead of up-front. This can provide significant performance
 * improvements for tables that are very large or for which
 * <code>TableItem</code> population is expensive (for example, retrieving
 * values from an external source).
 * </p>
 * <p>
 * Here is an example of using a <code>Table</code> with style
 * <code>VIRTUAL</code>: <code><pre>
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
 * <dd>SINGLE, MULTI, CHECK, FULL_SELECTION, HIDE_SELECTION, VIRTUAL, NO_SCROLL</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection, DefaultSelection, SetData, MeasureItem, EraseItem, PaintItem</dd>
 * </dl>
 * </p>
 * <p>
 * Note: Only one of the styles SINGLE, and MULTI may be specified.
 * </p>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * 
 * @see <a href="http://www.eclipse.org/swt/snippets/#table">Table, TableItem,
 *      TableColumn snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example:
 *      ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */

public class Table extends Composite {
	// aka rows, contains the cells/data
	TableColumn sortColumn;
	private int sortDirection;
	private TableModel model;

	private SignalConnector itemSelectionChanged;
	private static final int DEFAULT_ITEMHIGHT = 14;
	private int itemHeight = DEFAULT_ITEMHIGHT;

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
	public Table(Composite parent, int style) {
		super(parent, checkStyle(style));
	}

	@Override
	protected QWidget createQWidget(int style) {
		state &= ~(CANVAS | THEME_BACKGROUND);

		QTableView table = new MyQTableWidget();
		model = new TableModel(isVirtual());
		table.setModel(model);

		table.verticalHeader().hide();
		table.horizontalHeader().hide();
		table.setEditTriggers(EditTrigger.NoEditTriggers);
		table.setShowGrid(false);
		table.setWordWrap(false);
		table.setHorizontalScrollBarPolicy(ScrollBarPolicy.ScrollBarAsNeeded);
		table.setVerticalScrollBarPolicy(ScrollBarPolicy.ScrollBarAsNeeded);
		table.horizontalHeader().setResizeMode(ResizeMode.Interactive);
		table.horizontalHeader().setStretchLastSection(true);
		table.horizontalHeader().setDefaultAlignment(Qt.AlignmentFlag.AlignLeft);
		table.horizontalHeader().setContentsMargins(0, 0, 0, 0);
		if ((style & SWT.MULTI) != 0) {
			table.setSelectionMode(SelectionMode.ExtendedSelection);
		} else {
			table.setSelectionMode(SelectionMode.SingleSelection);
		}
		table.setSelectionBehavior(SelectionBehavior.SelectRows);
		setQMasterWidget(table);
		return table.viewport();
	}

	@Override
	protected void connectSignals() {
		itemSelectionChanged = new SignalConnector(getQTableWidget().selectionModel().selectionChanged, this,
				"qtItemSelectionChangedEvent()").connect(); //$NON-NLS-1$
	}

	protected void qtItemSelectionChangedEvent() {
		int selectionIndex = getSelectionIndex();
		Event event = new Event();
		event.type = SWT.Selection;
		if (selectionIndex != -1) {
			event.item = getItem(selectionIndex);
		}
		sendEvent(SWT.Selection, event, true);
	}

	protected void selectionEvent(QItemSelection selected, QItemSelection deselected) {
		sendEvent(SWT.Selection);
	}

	public QTableView getQTableWidget() {
		return (QTableView) getQMasterWidget();
	}

	static int checkStyle(int style) {
		if ((style & SWT.NO_SCROLL) == 0) {
			style |= SWT.H_SCROLL | SWT.V_SCROLL;
		}
		return checkBits(style, SWT.SINGLE, SWT.MULTI, 0, 0, 0, 0);
	}

	@Override
	protected void updateAutoFillBackground() {
		// nothing
	}

	@Override
	protected void updateBackground() {
		super.updateBackground();
	}

	@Override
	void updateBackgroundImage() {
		//super.updateBackgroundImage();
	}

	@Override
	protected Color getDefaultBackgroundColor() {
		return display.getSystemColor(SWT.COLOR_LIST_BACKGROUND);
	}

	@Override
	public void setDragEnabled(boolean enabled) {
		getQTableWidget().setDragEnabled(enabled);
	}

	@Override
	public void setAcceptDrops(boolean accept) {
		super.setAcceptDrops(accept);
		getQTableWidget().setDragDropMode(DragDropMode.DragDrop);
		getQTableWidget().setDropIndicatorShown(true);
	}

	void addItem(TableItem item, int row) {
		model.addItem(item, row, false);
	}

	void removeRow(TableItem row) {
		model.removeRow(row);
	}

	void addColumn(TableColumn column, int index) {
		model.addColumn(column, index);
	}

	void removeColumn(TableColumn column) {
		model.removeColumn(column);
	}

	void rowChanged(TableItem row) {
		model.rowChanged(row);
	}

	void cellChanged(TableItem row, int column) {
		model.cellChanged(row, column);
	}

	void columnChanged(TableColumn column) {
		model.columnChanged(column);
	}

	Rectangle visualRect(TableItem row, int column) {
		return QtSWTConverter.convert(getQTableWidget().visualRect(model.index(indexOf(row), column)));
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

	boolean checkData(TableItem item, boolean redraw) {
		if ((style & SWT.VIRTUAL) == 0) {
			return true;
		}

		if (!item.cached) {
			item.cached = true;
			Event event = new Event();
			event.item = item;
			event.index = indexOf(item);
			sendEvent(SWT.SetData, event);
			// widget could be disposed at this point
			if (isDisposed() || item.isDisposed()) {
				return false;
			}
		}
		return true;
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
	 * If the table was created with the <code>SWT.VIRTUAL</code> style, these
	 * attributes are requested again as needed.
	 * 
	 * @param index
	 *            the index of the item to clear
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
	 * @since 3.0
	 */
	public void clear(int index) {
		checkWidget();
		validateItemIndex(index);
		model.getRow(index).clear();
	}

	/**
	 * Removes the items from the receiver which are between the given
	 * zero-relative start and end indices (inclusive). The text, icon and other
	 * attributes of the items are set to their default values. If the table was
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
		if (start > end) {
			return;
		}
		int count = model.rowCount();
		if (!(0 <= start && start <= end && end < count)) {
			error(SWT.ERROR_INVALID_RANGE);
		}
		if (start == 0 && end == count - 1) {
			clearAll();
		}
		for (int index = start; index < end; index++) {
			model.getRow(index).clear();
		}
	}

	/**
	 * Clears the items at the given zero-relative indices in the receiver. The
	 * text, icon and other attributes of the items are set to their default
	 * values. If the table was created with the <code>SWT.VIRTUAL</code> style,
	 * these attributes are requested again as needed.
	 * 
	 * @param indices
	 *            the array of indices of the items
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_RANGE - if the index is not between 0
	 *                and the number of elements in the list minus 1 (inclusive)
	 *                </li>
	 *                <li>ERROR_NULL_ARGUMENT - if the indices array is null</li>
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
		if (indices == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (indices.length == 0) {
			return;
		}
		int count = model.rowCount();
		for (int i = 0; i < indices.length; i++) {
			if (!(0 <= indices[i] && indices[i] < count)) {
				error(SWT.ERROR_INVALID_RANGE);
			}
		}
		for (int index : indices) {
			model.getRow(index).clear();
		}
	}

	/**
	 * Clears all the items in the receiver. The text, icon and other attributes
	 * of the items are set to their default values. If the table was created
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
		model.clearAllItems();
		//getQTableWidget().clearContents();
	}

	@Override
	public void pack() {
		//		getQTableWidget().resizeColumnsToContents();
		//		getQTableWidget().resizeRowsToContents();
	}

	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		checkWidget();
		QSize size = getQTableWidget().sizeHint();

		if (getItemCount() > 0) {
			int width = 0;
			int height = 0;

			// Checkbox column
			if ((style & SWT.CHECK) != 0 || (style & SWT.RADIO) != 0) {
				width += getQTableWidget().columnWidth(0);
			}

			if (getColumnCount() > 0) {
				for (int i = 0; i < getColumnCount(); i++) {
					// if ( getColumn( i ).useFixedWidth ) {
					// width += getQTableWidget().columnWidth( i );
					// } else {
					width += getColumn(i).getPreferredColumnWidth(i);
					// }
				}
			} else { // list mode width +=
				getQTableWidget().sizeHintForColumn(1);
			}

			int borderWidth = getBorderWidth();
			width += 2 * borderWidth;
			height += 2 * borderWidth;
			height += getHeaderHeight();
			int items = getItemCount();
			height += items * _getItemHeight();
			size.setWidth(width);
			size.setHeight(height);
		}

		return QtSWTConverter.convert(size);
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
	 *                <li>ERROR_NULL_ARGUMENT - if the set of indices is null</li>
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
		if (indices == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (indices.length == 0) {
			return;
		}
		for (int index : indices) {
			_select(index, false);
		}
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
		_select(index, false);
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
		_select(start, end, false);
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
		itemSelectionChanged.runDisconnected(new Runnable() {
			public void run() {
				getQTableWidget().clearSelection();
			}
		});
	}

	/**
	 * Returns the column at the given, zero-relative index in the receiver.
	 * Throws an exception if the index is out of range. Columns are returned in
	 * the order that they were created. If no <code>TableColumn</code>s were
	 * created by the programmer, this method will throw
	 * <code>ERROR_INVALID_RANGE</code> despite the fact that a single column of
	 * data may be visible in the table. This occurs when the programmer uses
	 * the table like a list, adding items but never creating a column.
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
	 * @see Table#getColumnOrder()
	 * @see Table#setColumnOrder(int[])
	 * @see TableColumn#getMoveable()
	 * @see TableColumn#setMoveable(boolean)
	 * @see SWT#Move
	 */
	public TableColumn getColumn(int index) {
		checkWidget();
		return model.getColumn(index);
	}

	/**
	 * Returns the number of columns contained in the receiver. If no
	 * <code>TableColumn</code>s were created by the programmer, this value is
	 * zero, despite the fact that visually, one column of items may be visible.
	 * This occurs when the programmer uses the table like a list, adding items
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
		return model.columnCount();
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
	 * @see Table#setColumnOrder(int[])
	 * @see TableColumn#getMoveable()
	 * @see TableColumn#setMoveable(boolean)
	 * @see SWT#Move
	 * 
	 * @since 3.1
	 */
	public int[] getColumnOrder() {
		checkWidget();
		if (model.columnCount() == 0) {
			return new int[0];
		}
		return _getColumnOrder();
	}

	private int[] _getColumnOrder() {
		int columnCount = model.columnCount();
		int[] order = new int[columnCount];
		QHeaderView header = getQTableWidget().horizontalHeader();
		for (int i = 0; i < columnCount; i++) {
			order[i] = header.visualIndex(i);
		}
		return order;
	}

	/**
	 * Returns an array of <code>TableColumn</code>s which are the columns in
	 * the receiver. Columns are returned in the order that they were created.
	 * If no <code>TableColumn</code>s were created by the programmer, the array
	 * is empty, despite the fact that visually, one column of items may be
	 * visible. This occurs when the programmer uses the table like a list,
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
	 * @see Table#getColumnOrder()
	 * @see Table#setColumnOrder(int[])
	 * @see TableColumn#getMoveable()
	 * @see TableColumn#setMoveable(boolean)
	 * @see SWT#Move
	 */
	public TableColumn[] getColumns() {
		checkWidget();
		return model.getColumns();
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
	 */
	public int getGridLineWidth() {
		checkWidget();
		return getQTableWidget().showGrid() ? 1 : 0;
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
		if (getQTableWidget().horizontalHeader().isVisible()) {
			return getQTableWidget().horizontalHeader().height();
		}
		return 0;
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
		return !getQTableWidget().horizontalHeader().isHidden();
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
	 */
	public TableItem getItem(int index) {
		checkWidget();
		validateItemIndex(index);
		return model.getRow(index);
	}

	void validateItemIndex(int index) {
		if (!(0 <= index && index < model.rowCount())) {
			error(SWT.ERROR_INVALID_RANGE);
		}
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
	public TableItem getItem(Point point) {
		checkWidget();
		if (point == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		int row = getQTableWidget().rowAt(point.y);
		if (row < 0) {
			return null;
		}
		return model.getRow(row);
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
		return model.rowCount();
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
		return _getItemHeight();
	}

	int _getItemHeight() {
		int itemHeight = 0;
		if (model.rowCount() > 0) {
			itemHeight = getQTableWidget().rowHeight(0);
		} else {
			//			getQTableWidget().insertRow(0);
			//			itemHeight = getQTableWidget().rowHeight(0);
			//			getQTableWidget().removeRow(0);
		}
		return itemHeight;
	}

	/**
	 * Returns a (possibly empty) array of <code>TableItem</code>s which are the
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
	public TableItem[] getItems() {
		checkWidget();
		return model.getRows();
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
		return getQTableWidget().showGrid();
	}

	/**
	 * Returns an array of <code>TableItem</code>s that are currently selected
	 * in the receiver. The order of the items is unspecified. An empty array
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
	public TableItem[] getSelection() {
		checkWidget();
		List<QModelIndex> selection = getQTableWidget().selectionModel().selectedRows();

		/*
		 * This is an insert sort for sorting items according to their rows. In
		 * SWT/Win32 the selection is returned sorted that way.
		 */
		for (int i = 0; i < selection.size(); i++) {
			QModelIndex selectedItem = selection.get(i);
			int j = i;
			while (j > 0 && selection.get(j - 1).row() > selectedItem.row()) {
				selection.set(j, selection.get(j - 1));
				j = j - 1;
			}
			selection.set(j, selectedItem);
		}

		TableItem items[] = new TableItem[selection.size()];
		int i = 0;
		for (QModelIndex index : selection) {
			items[i++] = model.getRow(index.row());
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
		return getQTableWidget().selectionModel().selectedRows().size();
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
		if (model.rowCount() == 0) {
			return -1;
		}
		//		if (getQTableWidget().selectionModel().hasSelection()) {
		List<QModelIndex> selectedRows = getQTableWidget().selectionModel().selectedRows();
		if (selectedRows.size() > 0) {
			return selectedRows.get(0).row();
		}
		//		}
		return -1;
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
		List<QModelIndex> selectedIndices = getQTableWidget().selectionModel().selectedRows();
		int[] result = new int[selectedIndices.size()];
		int i = 0;
		for (QModelIndex index : selectedIndices) {
			result[i++] = index.row();
		}
		return result;
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
	 * @see #setSortColumn(TableColumn)
	 * 
	 * @since 3.2
	 */
	public TableColumn getSortColumn() {
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
	public int getTopIndex() {
		checkWidget();
		return getQTableWidget().rowAt(1);
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
	public int indexOf(TableColumn column) {
		checkWidget();
		if (column == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		return model.indexOf(column);
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
	public int indexOf(TableItem item) {
		checkWidget();
		if (item == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		return model.indexOf(item);
	}

	boolean isCustomToolTip() {
		return hooks(SWT.MeasureItem);
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
		checkWidget();
		validateItemIndex(index);
		return model.getRow(index).isSelected();
	}

	@Override
	void releaseChildren(boolean destroy) {
		model.release();
		super.releaseChildren(destroy);
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
	 *                and the number of elements in the list minus 1 (inclusive)
	 *                </li>
	 *                <li>ERROR_NULL_ARGUMENT - if the indices array is null</li>
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
		if (indices == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (indices.length == 0) {
			return;
		}
		model.removeItems(indices);
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
	 */
	public void remove(int index) {
		checkWidget();
		validateItemIndex(index);
		model.removeRowItem(index);
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
		if (start > end) {
			return;
		}
		int count = model.rowCount();
		if (!(0 <= start && start <= end && end < count)) {
			error(SWT.ERROR_INVALID_RANGE);
		}
		if (start == 0 && end == count - 1) {
			removeAll();
		} else {
			for (int index = end; index >= start; index--) {
				model.removeRowItem(index);
			}
		}
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
		model.releaseItems();
		//		getQTableWidget().clearContents();
		//		getQTableWidget().setRowCount(0);
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
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (eventTable == null) {
			return;
		}
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
	 *                <li>ERROR_NULL_ARGUMENT - if the array of indices is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see Table#setSelection(int[])
	 */
	public void select(int[] indices) {
		checkWidget();
		if (indices == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		int length = indices.length;
		if (length == 0 || (style & SWT.SINGLE) != 0 && length > 1) {
			return;
		}
		for (int index : indices) {
			_select(index, true);
		}
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
		_select(index, true);
	}

	void _select(int index, boolean selected) {
		if (index < 0 || index >= model.rowCount()) {
			return;
		}
		getQTableWidget().selectionModel().select(model.index(index, 0),
				SelectionFlag.createQFlags(SelectionFlag.Select, SelectionFlag.Rows));
		model.getRow(index).setSelected(selected);
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
	 * @see Table#setSelection(int,int)
	 */
	public void select(int start, int end) {
		checkWidget();
		_select(start, end, true);
	}

	void _select(int start, int end, boolean selected) {
		if (end < 0 || start > end || (style & SWT.SINGLE) != 0 && start != end) {
			return;
		}
		int count = model.rowCount();
		if (count == 0 || start >= count) {
			return;
		}
		start = Math.max(0, start);
		end = Math.min(end, count - 1);
		if (start == 0 && end == count - 1) {
			if (selected) {
				selectAll();
			} else {
				deselectAll();
			}
		} else {
			for (int index = start; index < end; index++) {
				_select(index, selected);
			}
		}
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
		if ((style & SWT.SINGLE) != 0) {
			return;
		}
		getQTableWidget().selectAll();
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
	 * @see Table#getColumnOrder()
	 * @see TableColumn#getMoveable()
	 * @see TableColumn#setMoveable(boolean)
	 * @see SWT#Move
	 * 
	 * @since 3.1
	 */
	public void setColumnOrder(int[] order) {
		checkWidget();
		if (validateColumnOrder(order)) {
			int columnCount = getColumnCount();
			QHeaderView header = getQTableWidget().horizontalHeader();
			for (int i = 0; i < columnCount; i++) {
				int visualIndex = header.visualIndex(i);
				header.moveSection(visualIndex, order[i]);
			}
		}
	}

	private boolean validateColumnOrder(int[] order) {
		if (order == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		int columnCount = model.columnCount();
		if (columnCount == 0) {
			if (order.length != 0) {
				error(SWT.ERROR_INVALID_ARGUMENT);
			}
			return false;
		}
		if (order.length != columnCount) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		int[] oldOrder = _getColumnOrder();

		boolean reorder = false;
		boolean[] seen = new boolean[columnCount];
		for (int i = 0; i < order.length; i++) {
			int index = order[i];
			if (index < 0 || index >= columnCount) {
				error(SWT.ERROR_INVALID_RANGE);
			}
			if (seen[index]) {
				error(SWT.ERROR_INVALID_ARGUMENT);
			}
			seen[index] = true;
			if (index != oldOrder[i]) {
				reorder = true;
			}
		}
		return reorder;
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
		if (show) {
			getQTableWidget().horizontalHeader().show();
		} else {
			getQTableWidget().horizontalHeader().hide();
		}
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
		model.setItemCount(count);
	}

	/**
	 * @return true if Table was created with SWT.VIRTUAL style
	 */
	boolean isVirtual() {
		return (style & SWT.VIRTUAL) != 0;
	}

	/**
	 * Sets the height of the area which would be used to display <em>one</em>
	 * of the items in the table.
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
		this.itemHeight = itemHeight;
		int rows = model.rowCount();
		for (int row = 0; row < rows; row++) {
			getQTableWidget().setRowHeight(row, itemHeight);
		}
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
		getQTableWidget().setShowGrid(show);
	}

	/**
	 * Selects the items at the given zero-relative indices in the receiver. The
	 * current selection is cleared before the new items are selected.
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
	 *                <li>ERROR_NULL_ARGUMENT - if the array of indices is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see Table#deselectAll()
	 * @see Table#select(int[])
	 */
	public void setSelection(int[] indices) {
		checkWidget();
		if (indices == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		deselectAll();
		int length = indices.length;
		if (length == 0 || (style & SWT.SINGLE) != 0 && length > 1) {
			return;
		}
		select(indices);
		showSelection();
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
	public void setSelection(TableItem item) {
		checkWidget();
		if (item == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		setSelection(new TableItem[] { item });
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
	 * @see Table#deselectAll()
	 * @see Table#select(int[])
	 * @see Table#setSelection(int[])
	 */
	public void setSelection(TableItem[] items) {
		checkWidget();
		if (items == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		deselectAll();
		int length = items.length;
		if (length == 0 || (style & SWT.SINGLE) != 0 && length > 1) {
			return;
		}
		for (int i = length - 1; i >= 0; --i) {
			int index = indexOf(items[i]);
			if (index != -1) {
				select(index);
			}
		}
		showSelection();
	}

	/**
	 * Selects the item at the given zero-relative index in the receiver. The
	 * current selection is first cleared, then the new item is selected.
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
	 * @see Table#deselectAll()
	 * @see Table#select(int)
	 */
	public void setSelection(int index) {
		checkWidget();
		deselectAll();
		select(index);
		showSelection();
	}

	/**
	 * Selects the items in the range specified by the given zero-relative
	 * indices in the receiver. The range of indices is inclusive. The current
	 * selection is cleared before the new items are selected.
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
	 * @see Table#deselectAll()
	 * @see Table#select(int,int)
	 */
	public void setSelection(int start, int end) {
		checkWidget();
		deselectAll();
		if (end < 0 || start > end || (style & SWT.SINGLE) != 0 && start != end) {
			return;
		}
		int count = model.rowCount();
		if (count == 0 || start >= count) {
			return;
		}
		start = Math.max(0, start);
		end = Math.min(end, count - 1);
		select(start, end);
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
	public void setSortColumn(TableColumn column) {
		checkWidget();
		if (column != null && column.isDisposed()) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		sortColumn = column;
		if (sortColumn != null && sortDirection != SWT.NONE) {
			getQTableWidget().sortByColumn(column.getColumn(), getSortOrder());
		}
	}

	private SortOrder getSortOrder() {
		if ((sortDirection & SWT.UP) != 0) {
			return SortOrder.AscendingOrder;
		}
		return SortOrder.DescendingOrder;
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
			getQTableWidget().sortByColumn(sortColumn.getColumn(), getSortOrder());
		}
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
		if (!(0 <= index && index < model.rowCount())) {
			return;
		}
		//		int currentColumn = getQTableWidget().currentColumn();
		//		QTableWidgetItem item = model.getItem(index).getCellItem(currentColumn);
		//		getQTableWidget().scrollToItem(item, ScrollHint.PositionAtTop);
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
	public void showColumn(TableColumn column) {
		checkWidget();
		if (column == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (column.isDisposed()) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		if (column.getParent() != this) {
			return;
		}
		if (model.rowCount() == 0) {
			return;
		}
		QModelIndex index = getQTableWidget().currentIndex();
		getQTableWidget().scrollTo(model.index(index.row(), model.indexOf(column)));
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
	 * @see Table#showSelection()
	 */
	public void showItem(TableItem item) {
		checkWidget();
		if (item == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (item.isDisposed()) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		int index = indexOf(item);
		if (index != -1) {
			showItem(index, 0);
		}
	}

	void showItem(int row, int col) {
		getQTableWidget().scrollTo(model.index(row, col));
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
	 * @see Table#showItem(TableItem)
	 */
	public void showSelection() {
		checkWidget();
		getQTableWidget().scrollTo(getQTableWidget().selectionModel().currentIndex());
	}

	void update(boolean all) {
		getQTableWidget().update();
	}

	private final class MyQTableWidget extends QTableView {

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

	final class TableModel extends QAbstractTableModel {
		private ArrayList<TableItem> rows;
		private List<TableColumn> columns;
		private final boolean virtual;

		TableModel(boolean virtual) {
			this.virtual = virtual;
			init();
		}

		public void init() {
			rows = new ArrayList<TableItem>(4);
			columns = new ArrayList<TableColumn>(4);
		}

		@Override
		public Object data(QModelIndex index, int role) {
			int row = index.row();
			int column = index.column();
			TableItem rowItem = getRow(row);
			switch (role) {
			case DisplayRole:
				return rowItem.getText(column);
			case CheckStateRole:
				if ((style & SWT.CHECK) != 0 && column == 0) {
					return rowItem.getChecked();
				}
				break;
			case DecorationRole:
				Image img = rowItem.getImage(column);
				if (img != null) {
					return img.getQPixmap();
				}
				break;
			case BackgroundRole: {
				Color color = rowItem.getBackground(column);
				if (color != null) {
					return new QBrush(color.getColor());
				}
				break;
			}
			case ForegroundRole: {
				Color color = rowItem.getForeground(column);
				if (color != null) {
					return new QBrush(color.getColor());
				}
				break;
			}
			}
			return null;
		}

		@Override
		public boolean setData(QModelIndex index, Object value, int role) {
			if (role == CheckStateRole) {
				getRow(index.row()).setChecked(((Boolean) value).booleanValue());
				dataChanged.emit(index, index);
				return true;
			}
			return super.setData(index, value, role);
		}

		@Override
		public ItemFlags flags(QModelIndex index) {
			int column = index.column();
			if ((style & SWT.CHECK) != 0 && column == 0) {
				return new ItemFlags(new ItemFlag[] { ItemFlag.ItemIsSelectable, ItemFlag.ItemIsEditable,
						ItemFlag.ItemIsEnabled, ItemFlag.ItemIsUserCheckable });
			}
			return super.flags(index);
		}

		@Override
		public int rowCount(QModelIndex index) {
			return rows.size();
		}

		void setItemCount(int count) {
			count = Math.max(0, count);
			if (count < rows.size()) {
				beginRemoveRows(null, rows.size(), count - 1);
				for (int i = rows.size() - 1; i >= count; i--) {
					TableItem row = rows.remove(i);
					row.release(false);
				}
				endRemoveRows();
			} else {
				beginInsertRows(null, rows.size(), count - 1);
				rows.ensureCapacity(count);
				boolean virtual = isVirtual();
				for (int i = rows.size(); i < count; i++) {
					if (virtual) {
						rows.add(null);
					} else {
						rows.add(new TableItem(Table.this, SWT.NONE, i));
					}
				}
				endInsertRows();
			}
		}

		TableItem getRow(int index) {
			TableItem item = rows.get(index);
			if (item != null) {
				return item;
			}
			if (!virtual) {
				return null;
			}
			item = new TableItem(Table.this, SWT.NONE, index, false);
			addItem(item, index, true);
			return item;
		}

		public TableItem[] getRows() {
			TableItem[] out = new TableItem[rows.size()];
			if (virtual) {
				int count = out.length;
				for (int i = 0; i < count; i++) {
					out[i] = getRow(i);
				}
			} else {
				rows.toArray(out);
			}
			return out;
		}

		int indexOf(TableItem row) {
			return rows.indexOf(row);
		}

		void addItem(TableItem rowItem, int row, boolean update) {
			beginInsertRows(null, row, row);
			// if we add items then we need an first column to hold them
			if (columns.size() == 0) {
				new TableColumn(Table.this, SWT.NONE);
			}
			if (update) {
				rows.set(row, rowItem);
			} else {
				if (row == rows.size()) {
					rows.add(rowItem);
				} else {
					rows.add(row, rowItem);
				}
			}
			endInsertRows();
			getQTableWidget().setRowHeight(row, itemHeight);
		}

		void cellChanged(TableItem rowItem, int column) {
			int row = indexOf(rowItem);
			QModelIndex index = index(row, column);
			dataChanged.emit(index, index);
		}

		void rowChanged(TableItem rowItem) {
			int row = indexOf(rowItem);
			dataChanged.emit(index(row, 0), index(row, columns.size() - 1));
		}

		void removeRow(TableItem row) {
			removeRowItem(rows.indexOf(row));
		}

		void removeRowItem(int index) {
			if (index != -1) {
				beginRemoveRows(null, index, index);
				TableItem item = rows.remove(index);
				if (item != null && !item.isDisposed()) {
					item.release(false);
				}
				endRemoveRows();
			}
		}

		void removeItems(int[] indices) {
			int[] newIndices = new int[indices.length];
			System.arraycopy(indices, 0, newIndices, 0, indices.length);
			Arrays.sort(newIndices);
			for (int i = indices.length - 1; i >= 0; i--) {
				validateItemIndex(indices[i]);
				removeRowItem(indices[i]);
			}
		}

		void clearAllItems() {
			for (TableItem item : rows) {
				if (item != null) {
					item.clear();
				}
			}
		}

		@Override
		public int columnCount(QModelIndex index) {
			return columns.size();
		}

		TableColumn getColumn(int index) {
			validateColumnIndex(index);
			return columns.get(index);
		}

		int indexOf(TableColumn column) {
			return columns.indexOf(column);
		}

		private void validateColumnIndex(int index) {
			if (!(0 <= index && index < columnCount())) {
				error(SWT.ERROR_INVALID_RANGE);
			}
		}

		void addColumn(TableColumn column, int index) {
			if (!(0 <= index && index <= columnCount())) {
				error(SWT.ERROR_INVALID_RANGE);
			}
			beginInsertColumns(null, index, index);
			if (index == columns.size()) {
				columns.add(column);
			} else {
				columns.add(index, column);
			}
			int columnCount = columns.size();
			for (TableItem item : rows) {
				if (item != null) {
					item.addColumn(index, columnCount);
				}
			}
			endInsertColumns();
		}

		void removeColumn(TableColumn column) {
			int index = columns.indexOf(column);
			if (index == -1) {
				return;
			}
			beginRemoveColumns(null, index, index);
			int columnCount = columnCount();
			columns.remove(column);
			for (TableItem item : rows) {
				if (item != null) {
					item.removeColumn(index, columnCount);
				}
			}
			endRemoveColumns();
		}

		TableColumn[] getColumns() {
			TableColumn[] out = new TableColumn[columns.size()];
			return columns.toArray(out);
		}

		void release() {
			releaseItems();
			releaseColumns();
		}

		private void releaseItems() {
			beginRemoveRows(null, 0, rows.size() - 1);
			for (TableItem item : rows) {
				if (item != null && !item.isDisposed()) {
					item.release(false);
				}
			}
			rows.clear();
			endRemoveRows();
		}

		private void releaseColumns() {
			beginRemoveColumns(null, 0, columns.size() - 1);
			for (TableColumn column : columns) {
				if (!column.isDisposed()) {
					column.release(false);
				}
			}
			columns.clear();
			endRemoveColumns();
		}

		@Override
		public Object headerData(int index, Orientation orientation, int role) {
			if (index >= columns.size()) {
				return null;
			}
			switch (role) {
			case DisplayRole:
				return columnText(index);
			case ToolTipRole:
				return columnTooltip(index);
			case TextAlignmentRole:
				return columnAlignment(index);
			}
			return null;
		}

		private Object columnAlignment(int index) {
			int alignment = columns.get(index).getAlignment();
			switch (alignment) {
			case SWT.LEFT:
				return AlignmentFlag.AlignLeft;
			case SWT.CENTER:
				return AlignmentFlag.AlignCenter;
			case SWT.RIGHT:
				return AlignmentFlag.AlignRight;
			}
			return null;
		}

		private Object columnText(int index) {
			return columns.get(index).getText();
		}

		private Object columnTooltip(int index) {
			return columns.get(index).getToolTipText();
		}

		void columnChanged(TableColumn column) {
			int index = indexOf(column);
			columnChanged(index, index);
		}

		void columnChanged(int from, int to) {
			headerDataChanged.emit(Orientation.Horizontal, from, to);
		}

	}

}
