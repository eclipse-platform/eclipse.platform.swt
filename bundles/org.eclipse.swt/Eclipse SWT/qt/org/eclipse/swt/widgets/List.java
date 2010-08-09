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

import java.util.Arrays;

import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.Qt.DropActions;
import com.trolltech.qt.gui.QDragEnterEvent;
import com.trolltech.qt.gui.QDragLeaveEvent;
import com.trolltech.qt.gui.QDragMoveEvent;
import com.trolltech.qt.gui.QDropEvent;
import com.trolltech.qt.gui.QItemSelection;
import com.trolltech.qt.gui.QListWidget;
import com.trolltech.qt.gui.QListWidgetItem;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QAbstractItemView.DragDropMode;
import com.trolltech.qt.gui.QAbstractItemView.ScrollHint;
import com.trolltech.qt.gui.QAbstractItemView.SelectionMode;
import com.trolltech.qt.gui.QItemSelectionModel.SelectionFlag;
import com.trolltech.qt.gui.QItemSelectionModel.SelectionFlags;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

/**
 * Instances of this class represent a selectable user interface object that
 * displays a list of strings and issues notification when a string is selected.
 * A list may be single or multi select.
 * <p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SINGLE, MULTI</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection, DefaultSelection</dd>
 * </dl>
 * <p>
 * Note: Only one of SINGLE and MULTI may be specified.
 * </p>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * 
 * @see <a href="http://www.eclipse.org/swt/snippets/#list">List snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example:
 *      ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */

public class List extends Scrollable {
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
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public List(Composite parent, int style) {
		super(parent, checkStyle(style));
	}

	@Override
	protected QWidget createQWidget(int style) {
		QListWidget listWidget = new MyListWidget();
		if ((style & SWT.MULTI) != 0) {
			listWidget.setSelectionMode(SelectionMode.MultiSelection);
		} else {
			listWidget.setSelectionMode(SelectionMode.SingleSelection);
		}
		return listWidget;
	}

	@Override
	protected void connectSignals() {
		getQListWidget().selectionModel().selectionChanged.connect(this, "selectionEvent()");//$NON-NLS-1$
		getQListWidget().itemActivated.connect(this, "itemActivationEvent(QListWidgetItem)");//$NON-NLS-1$
	}

	private QListWidget getQListWidget() {
		return (QListWidget) getQWidget();
	}

	protected void selectionEvent() {
		sendEvent(SWT.Selection);
	}

	protected void itemActivationEvent(QListWidgetItem item) {
		sendEvent(SWT.DefaultSelection);
	}

	@Override
	public void setDragEnabled(boolean enabled) {
		getQListWidget().setDragEnabled(enabled);
	}

	@Override
	public void setAcceptDrops(boolean accept) {
		super.setAcceptDrops(accept);
		getQListWidget().setDragDropMode(DragDropMode.DragDrop);
		getQListWidget().setDropIndicatorShown(true);
	}

	/**
	 * Adds the argument to the end of the receiver's list.
	 * 
	 * @param string
	 *            the new item
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the string is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see #add(String,int)
	 */
	public void add(String string) {
		checkWidget();
		if (string == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		getQListWidget().addItem(string);
	}

	/**
	 * Adds the argument to the receiver's list at the given zero-relative
	 * index.
	 * <p>
	 * Note: To add an item at the end of the list, use the result of calling
	 * <code>getItemCount()</code> as the index or use <code>add(String)</code>.
	 * </p>
	 * 
	 * @param string
	 *            the new item
	 * @param index
	 *            the index for the item
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the string is null</li>
	 *                <li>ERROR_INVALID_RANGE - if the index is not between 0
	 *                and the number of elements in the list (inclusive)</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see #add(String)
	 */
	public void add(String string, int index) {
		checkWidget();
		if (string == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		validateIndex(index);
		getQListWidget().insertItem(index, string);
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when the user changes the receiver's selection, by sending it one of the
	 * messages defined in the <code>SelectionListener</code> interface.
	 * <p>
	 * <code>widgetSelected</code> is called when the selection changes.
	 * <code>widgetDefaultSelected</code> is typically called when an item is
	 * double-clicked.
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

	static int checkStyle(int style) {
		return checkBits(style, SWT.SINGLE, SWT.MULTI, 0, 0, 0, 0);
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

		updateSelection(indices, new SelectionFlags(SelectionFlag.Deselect));
	}

	private void updateSelection(int[] indices, SelectionFlags selectionFlags) {
		int count = getQListWidget().count();
		for (int i = 0; i < indices.length; i++) {
			if (indices[i] >= 0 && indices[i] < count) {
				updateSelectionRange(indices[i], indices[i], selectionFlags);
			}
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
		validateIndex(index);
		updateSelectionRange(index, index, new SelectionFlags(SelectionFlag.Deselect));
	}

	void validateIndex(int index) {
		if (index < 0 || index > getQListWidget().count()) {
			SWT.error(SWT.ERROR_INVALID_RANGE);
		}
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
		if (start > end || end < 0) {
			return;
		}
		int count = getQListWidget().count();
		if (start > count - 1) {
			return;
		}
		start = Math.max(0, start);
		end = Math.min(end, count - 1);
		updateSelectionRange(start, end, new SelectionFlags(SelectionFlag.Deselect));
	}

	void updateSelectionRange(int start, int end, SelectionFlags flags) {
		QModelIndex startIndex = getQListWidget().model().index(start, 0);
		QModelIndex endIndex = getQListWidget().model().index(end, 0);
		QItemSelection itemSelection = new QItemSelection(startIndex, endIndex);
		getQListWidget().selectionModel().select(itemSelection, flags);
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
		getQListWidget().clearSelection();
	}

	/**
	 * Returns the zero-relative index of the item which currently has the focus
	 * in the receiver, or -1 if no item has focus.
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
	public int getFocusIndex() {
		checkWidget();
		return getQListWidget().currentRow();
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
	public String getItem(int index) {
		checkWidget();
		validateIndex(index);
		return getQListWidget().item(index).text();
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
		return getQListWidget().count();
	}

	/**
	 * Returns the height of the area which would be used to display
	 * <em>one</em> of the items in the list.
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
		if (getQListWidget().count() > 0) {
			itemHeight = getQListWidget().sizeHintForRow(0);
		} else {
			// we add a dummy item to get the height
			getQListWidget().addItem("");//$NON-NLS-1$
			itemHeight = getQListWidget().sizeHintForRow(0);
			_remove(0);
		}
		return itemHeight;
	}

	/**
	 * Returns a (possibly empty) array of <code>String</code>s which are the
	 * items in the receiver.
	 * <p>
	 * Note: This is not the actual structure used by the receiver to maintain
	 * its list of items, so modifying the array will not affect the receiver.
	 * </p>
	 * 
	 * @return the items in the receiver's list
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public String[] getItems() {
		checkWidget();
		int count = getItemCount();
		String[] result = new String[count];
		for (int i = 0; i < count; i++) {
			result[i] = getItem(i);
		}
		return result;
	}

	/**
	 * Returns an array of <code>String</code>s that are currently selected in
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
	public String[] getSelection() {
		checkWidget();
		QListWidget listWidget = getQListWidget();
		java.util.List<QModelIndex> selectedIndices = listWidget.selectionModel().selectedRows();
		String[] sel = new String[selectedIndices.size()];
		int i = 0;
		for (QModelIndex index : selectedIndices) {
			sel[i] = listWidget.item(index.row()).text();
			i++;
		}
		return sel;
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
		return getQListWidget().selectionModel().selectedRows().size();
	}

	/**
	 * Returns the zero-relative index of the item which is currently selected
	 * in the receiver, or -1 if no item is selected.
	 * 
	 * @return the index of the selected item or -1
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
		java.util.List<QModelIndex> selectedIndices = getQListWidget().selectionModel().selectedRows();
		if (selectedIndices != null && selectedIndices.size() > 0) {
			return selectedIndices.get(0).row();
		}
		return 0;
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
		java.util.List<QModelIndex> selection = getQListWidget().selectionModel().selectedRows();
		int[] selIndex = new int[selection.size()];
		int i = 0;
		for (QModelIndex index : selection) {
			selIndex[i] = index.row();
			i++;
		}
		return selIndex;
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
		QModelIndex modellIndex = getQListWidget().indexAt(new QPoint(1, 1));
		return modellIndex == null ? -1 : modellIndex.row();
	}

	/**
	 * Gets the index of an item.
	 * <p>
	 * The list is searched starting at 0 until an item is found that is equal
	 * to the search item. If no item is found, -1 is returned. Indexing is zero
	 * based.
	 * 
	 * @param string
	 *            the search item
	 * @return the index of the item
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the string is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li> <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                called from the thread that created the receiver</li>
	 *                </ul>
	 */
	public int indexOf(String string) {
		checkWidget();
		if (string == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}

		return indexOf(string, 0);
	}

	/**
	 * Searches the receiver's list starting at the given, zero-relative index
	 * until an item is found that is equal to the argument, and returns the
	 * index of that item. If no item is found or the starting index is out of
	 * range, returns -1.
	 * 
	 * @param string
	 *            the search item
	 * @param start
	 *            the zero-relative index at which to start the search
	 * @return the index of the item
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the string is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int indexOf(String string, int start) {
		checkWidget();
		if (string == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		QListWidget listWidget = getQListWidget();
		int count = listWidget.count();
		if (start < 0 || start > count - 1) {
			return -1;
		}
		for (int i = start; i < count; i++) {
			if (listWidget.item(i).equals(string)) {
				return i;
			}
		}
		return -1;
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
		validateIndex(index);
		QListWidgetItem item = getQListWidget().item(index);
		if (item != null) {
			return item.isSelected();
		}
		return false;
	}

	/**
	 * Removes the items from the receiver at the given zero-relative indices.
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
		for (int index : indices) {
			_remove(index);
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
		_remove(index);
	}

	void _remove(int index) {
		validateIndex(index);
		getQListWidget().takeItem(index).dispose();
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
		if (end < 0 || start > end || (style & SWT.SINGLE) != 0 && start != end) {
			error(SWT.ERROR_INVALID_RANGE);
		}
		QListWidget listWidget = getQListWidget();
		int count = listWidget.count();
		if (count == 0 || start >= count) {
			error(SWT.ERROR_INVALID_RANGE);
		}
		start = Math.max(0, start);
		end = Math.min(end, count - 1);
		for (int i = end; i >= start; --i) {
			listWidget.takeItem(i).dispose();
		}
	}

	/**
	 * Searches the receiver's list starting at the first item until an item is
	 * found that is equal to the argument, and removes that item from the list.
	 * 
	 * @param string
	 *            the item to remove
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the string is null</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the string is not found in
	 *                the list</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void remove(String string) {
		checkWidget();
		if (string == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		int index = indexOf(string, 0);
		if (index == -1) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		remove(index);
	}

	/**
	 * Removes all of the items from the receiver.
	 * <p>
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li> <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                called from the thread that created the receiver</li>
	 *                </ul>
	 */
	public void removeAll() {
		checkWidget();
		getQListWidget().clear();
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
	 * 
	 * @param indices
	 *            the array of indices for the items to select
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the array of indices is null
	 *                </li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li> <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                called from the thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see List#setSelection(int[])
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
		select(indices, false);
	}

	void select(int[] indices, boolean scroll) {
		updateSelection(indices, new SelectionFlags(SelectionFlag.Select));
	}

	/**
	 * Selects the item at the given zero-relative index in the receiver's list.
	 * If the item at the index was already selected, it remains selected.
	 * Indices that are out of range are ignored.
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
		select(index, false);
	}

	void select(int index, boolean scroll) {
		if (index < 0) {
			return;
		}
		int count = getQListWidget().count();
		if (index >= count) {
			return;
		}
		updateSelectionRange(index, index, new SelectionFlags(SelectionFlag.Select));
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
	 * 
	 * @param start
	 *            the start of the range
	 * @param end
	 *            the end of the range
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li> <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                called from the thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see List#setSelection(int,int)
	 */
	public void select(int start, int end) {
		checkWidget();
		if (end < 0 || start > end || (style & SWT.SINGLE) != 0 && start != end) {
			return;
		}
		int count = getQListWidget().count();
		if (count == 0 || start >= count) {
			return;
		}
		start = Math.max(0, start);
		end = Math.min(end, count - 1);
		if ((style & SWT.SINGLE) != 0) {
			select(start, false);
		} else {
			select(start, end, false);
		}
	}

	void select(int start, int end, boolean scroll) {
		updateSelectionRange(start, end, new SelectionFlags(SelectionFlag.Select));
	}

	/**
	 * Selects all of the items in the receiver.
	 * <p>
	 * If the receiver is single-select, do nothing.
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li> <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                called from the thread that created the receiver</li>
	 *                </ul>
	 */
	public void selectAll() {
		checkWidget();
		int count = getQListWidget().count();
		if ((style & SWT.MULTI) != 0 && count > 0) {
			updateSelectionRange(0, count - 1, new SelectionFlags(SelectionFlag.Select));
		}
	}

	void setFocusIndex(int index) {
		QModelIndex modelIndex = getQListWidget().model().index(index, 0);
		getQListWidget().selectionModel().setCurrentIndex(modelIndex, SelectionFlag.Current);
	}

	/**
	 * Sets the text of the item in the receiver's list at the given
	 * zero-relative index to the string argument.
	 * 
	 * @param index
	 *            the index for the item
	 * @param string
	 *            the new text for the item
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_RANGE - if the index is not between 0
	 *                and the number of elements in the list minus 1 (inclusive)
	 *                </li>
	 *                <li>ERROR_NULL_ARGUMENT - if the string is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setItem(int index, String string) {
		checkWidget();
		if (string == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		validateIndex(index);
		int topIndex = getTopIndex();
		boolean isSelected = isSelected(index);
		remove(index);
		add(string, index);
		if (isSelected) {
			select(index, false);
		}
		setTopIndex(topIndex);
	}

	/**
	 * Sets the receiver's items to be the given array of items.
	 * 
	 * @param items
	 *            the array of items
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the items array is null</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if an item in the items array
	 *                is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setItems(String[] items) {
		checkWidget();
		if (items == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		for (String item : items) {
			if (item == null) {
				error(SWT.ERROR_INVALID_ARGUMENT);
			}
		}
		getQListWidget().clear();
		getQListWidget().addItems(Arrays.asList(items));
	}

	/**
	 * Selects the items at the given zero-relative indices in the receiver. The
	 * current selection is cleared before the new items are selected.
	 * <p>
	 * Indices that are out of range and duplicate indices are ignored. If the
	 * receiver is single-select and multiple indices are specified, then all
	 * indices are ignored.
	 * 
	 * @param indices
	 *            the indices of the items to select
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the array of indices is null
	 *                </li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li> <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                called from the thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see List#deselectAll()
	 * @see List#select(int[])
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
		select(indices, true);
		if ((style & SWT.MULTI) != 0) {
			int focusIndex = indices[0];
			if (focusIndex >= 0) {
				setFocusIndex(focusIndex);
			}
		}
	}

	/**
	 * Sets the receiver's selection to be the given array of items. The current
	 * selection is cleared before the new items are selected.
	 * <p>
	 * Items that are not in the receiver are ignored. If the receiver is
	 * single-select and multiple items are specified, then all items are
	 * ignored.
	 * 
	 * @param items
	 *            the array of items
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the array of items is null
	 *                </li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li> <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                called from the thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see List#deselectAll()
	 * @see List#select(int[])
	 * @see List#setSelection(int[])
	 */
	public void setSelection(String[] items) {
		checkWidget();
		if (items == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		deselectAll();
		int length = items.length;
		if (length == 0 || (style & SWT.SINGLE) != 0 && length > 1) {
			return;
		}
		int focusIndex = -1;
		for (int i = length - 1; i >= 0; --i) {
			String string = items[i];
			int index = 0;
			if (string != null) {
				int localFocus = -1;
				while ((index = indexOf(string, index)) != -1) {
					if (localFocus == -1) {
						localFocus = index;
					}
					select(index, false);
					if ((style & SWT.SINGLE) != 0 && isSelected(index)) {
						showSelection();
						return;
					}
					index++;
				}
				if (localFocus != -1) {
					focusIndex = localFocus;
				}
			}
		}
		if ((style & SWT.MULTI) != 0) {
			if (focusIndex >= 0) {
				setFocusIndex(focusIndex);
			}
		}
	}

	/**
	 * Selects the item at the given zero-relative index in the receiver. If the
	 * item at the index was already selected, it remains selected. The current
	 * selection is first cleared, then the new item is selected. Indices that
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
	 * @see List#deselectAll()
	 * @see List#select(int)
	 */
	public void setSelection(int index) {
		checkWidget();
		deselectAll();
		select(index, true);
		if ((style & SWT.MULTI) != 0) {
			if (index >= 0 && index < getQListWidget().count()) {
				setFocusIndex(index);
			}
		}
	}

	/**
	 * Selects the items in the range specified by the given zero-relative
	 * indices in the receiver. The range of indices is inclusive. The current
	 * selection is cleared before the new items are selected.
	 * <p>
	 * Indices that are out of range are ignored and no items will be selected
	 * if start is greater than end. If the receiver is single-select and there
	 * is more than one item in the given range, then all indices are ignored.
	 * 
	 * @param start
	 *            the start index of the items to select
	 * @param end
	 *            the end index of the items to select
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li> <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                called from the thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see List#deselectAll()
	 * @see List#select(int,int)
	 */
	public void setSelection(int start, int end) {
		checkWidget();
		deselectAll();
		if (end < 0 || start > end || (style & SWT.SINGLE) != 0 && start != end) {
			return;
		}
		int count = getQListWidget().count();
		if (count == 0 || start >= count) {
			return;
		}
		start = Math.max(0, start);
		end = Math.min(end, count - 1);
		updateSelectionRange(start, end, new SelectionFlags(SelectionFlag.Select));
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
		validateIndex(index);
		getQListWidget().scrollToItem(getQListWidget().item(index), ScrollHint.PositionAtTop);
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
	 */
	public void showSelection() {
		checkWidget();
		getQListWidget().scrollToItem(getQListWidget().currentItem(), ScrollHint.PositionAtTop);
	}

	private final class MyListWidget extends QListWidget {

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
