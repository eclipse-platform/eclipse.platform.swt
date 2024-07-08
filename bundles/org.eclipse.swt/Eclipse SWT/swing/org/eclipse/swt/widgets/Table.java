/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

 
import java.awt.AWTEvent;
import java.awt.Container;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventObject;

import javax.swing.DefaultListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.swing.CTable;
import org.eclipse.swt.internal.swing.CTableItem;
import org.eclipse.swt.internal.swing.UIThreadUtils;
import org.eclipse.swt.internal.swing.CTable.CellPaintEvent;

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
 * Here is an example of using a <code>Table</code> with style <code>VIRTUAL</code>:
 * <code><pre>
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
 * </p><p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to add <code>Control</code> children to it,
 * or set a layout on it.
 * </p><p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SINGLE, MULTI, CHECK, FULL_SELECTION, HIDE_SELECTION, VIRTUAL</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection, DefaultSelection, SetData, MeasureItem, EraseItem, PaintItem</dd>
 * </dl>
 * </p><p>
 * Note: Only one of the styles SINGLE, and MULTI may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */

public class Table extends Composite {
  ArrayList itemList;
  ArrayList columnList;
//	ImageList imageList;
	TableItem currentItem;
	int lastIndexOf;
//  int lastWidth;
//	boolean customDraw, dragStarted, fixScrollWidth, mouseDown, tipRequested;
//	boolean ignoreActivate, ignoreSelect, ignoreShrink, ignoreResize;
	static final int INSET = 4;
	static final int GRID_WIDTH = 1;
	static final int HEADER_MARGIN = 10;


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
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Table (Composite parent, int style) {
	super (parent, checkStyle (style));
}

void adjustColumnWidth() {
  // Could be more efficient. Should post, with coalescing?
  if(getColumnCount() == 0) {
    CTable cTable = (CTable)handle;
    cTable.getColumnModel().getColumn(0).setPreferredWidth(cTable.getPreferredColumnWidth(0));
  }
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver's selection changes, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * When <code>widgetSelected</code> is called, the item field of the event object is valid.
 * If the receiver has <code>SWT.CHECK</code> style set and the check selection changes,
 * the event object detail field contains the value <code>SWT.CHECK</code>.
 * <code>widgetDefaultSelected</code> is typically called when an item is double-clicked.
 * The item field of the event object is valid for default selection, but the detail field is not used.
 * </p>
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
 * @see SelectionListener
 * @see #removeSelectionListener
 * @see SelectionEvent
 */
public void addSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

boolean checkData (TableItem item, boolean redraw) {
  if ((style & SWT.VIRTUAL) == 0) return true;
  return checkData (item, indexOf (item), redraw);
}

boolean checkData (TableItem item, int index, boolean redraw) {
  if ((style & SWT.VIRTUAL) == 0) return true;
  if (!item.cached) {
    item.cached = true;
    Event event = new Event ();
    event.item = item;
    event.index = index;
    currentItem = item;
    sendEvent (SWT.SetData, event);
    //widget could be disposed at this point
    currentItem = null;
    if (isDisposed () || item.isDisposed ()) return false;
//    if (redraw) {
//      if (!setScrollWidth (item, false)) {
//        item.redraw ();
//      }
//    }
  }
  return true;
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
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
  TableItem item = (TableItem)itemList.get(index);
  if(item != null) {
    item.clear();
  }
  handle.repaint();
//	int count = OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
//	if (!(0 <= index && index < count)) error (SWT.ERROR_INVALID_RANGE);
//	TableItem item = items [index];
//	if (item != null) {
//		if (item != currentItem) item.clear ();
//		/*
//		* Bug in Windows.  Despite the fact that every item in the
//		* table always has LPSTR_TEXTCALLBACK, Windows caches the
//		* bounds for the selected items.  This means that 
//		* when you change the string to be something else, Windows
//		* correctly asks you for the new string but when the item
//		* is selected, the selection draws using the bounds of the
//		* previous item.  The fix is to reset LPSTR_TEXTCALLBACK
//		* even though it has not changed, causing Windows to flush
//		* cached bounds.
//		*/
//		if ((style & SWT.VIRTUAL) == 0 && item.cached) {
//			LVITEM lvItem = new LVITEM ();
//			lvItem.mask = OS.LVIF_TEXT | OS.LVIF_INDENT;
//			lvItem.pszText = OS.LPSTR_TEXTCALLBACK;
//			lvItem.iItem = index;
//			OS.SendMessage (handle, OS.LVM_SETITEM, 0, lvItem);
//			item.cached = false;
//		}
//		if (currentItem == null && drawCount == 0 && OS.IsWindowVisible (handle)) {
//			OS.SendMessage (handle, OS.LVM_REDRAWITEMS, index, index);
//		}
//		setScrollWidth (item, false);
//	}
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
	if (start > end) return;
  for(int i=start; i<=end; i++) {
    TableItem item = (TableItem)itemList.get(i);
    if(item != null) {
      item.clear();
    }
  }
  handle.repaint();
//	int count = OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
//	if (!(0 <= start && start <= end && end < count)) {
//		error (SWT.ERROR_INVALID_RANGE);
//	}
//	if (start == 0 && end == count - 1) {
//		clearAll ();
//	} else {
//		LVITEM lvItem = null;
//		boolean cleared = false;
//		for (int i=start; i<=end; i++) {
//			TableItem item = items [i];
//			if (item != null) {
//				if (item != currentItem) {
//					cleared = true;
//					item.clear ();
//				}
//				/*
//				* Bug in Windows.  Despite the fact that every item in the
//				* table always has LPSTR_TEXTCALLBACK, Windows caches the
//				* bounds for the selected items.  This means that 
//				* when you change the string to be something else, Windows
//				* correctly asks you for the new string but when the item
//				* is selected, the selection draws using the bounds of the
//				* previous item.  The fix is to reset LPSTR_TEXTCALLBACK
//				* even though it has not changed, causing Windows to flush
//				* cached bounds.
//				*/
//				if ((style & SWT.VIRTUAL) == 0 && item.cached) {
//					if (lvItem == null) {
//						lvItem = new LVITEM ();
//						lvItem.mask = OS.LVIF_TEXT | OS.LVIF_INDENT;
//						lvItem.pszText = OS.LPSTR_TEXTCALLBACK;
//					}
//					lvItem.iItem = i;
//					OS.SendMessage (handle, OS.LVM_SETITEM, 0, lvItem);
//					item.cached = false;
//				}
//			}
//		}
//		if (cleared) {
//			if (currentItem == null && drawCount == 0 && OS.IsWindowVisible (handle)) {
//				OS.SendMessage (handle, OS.LVM_REDRAWITEMS, start, end);
//			}
//			TableItem item = start == end ? items [start] : null; 
//			setScrollWidth (item, false);
//		}
//	}
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
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (indices.length == 0) return;
	for(int i=0; i<indices.length; i++) {
	  TableItem item = (TableItem)itemList.get(i);
	  if(item != null) {
	    item.clear();
	  }
	}
  handle.repaint();
//	int count = OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
//	for (int i=0; i<indices.length; i++) {
//		if (!(0 <= indices [i] && indices [i] < count)) {
//			error (SWT.ERROR_INVALID_RANGE);
//		}
//	}
//	LVITEM lvItem = null;
//	boolean cleared = false;
//	for (int i=0; i<indices.length; i++) {
//		int index = indices [i];
//		TableItem item = items [index];
//		if (item != null) {
//			if (item != currentItem) {
//				cleared = true;
//				item.clear ();
//			}
//			/*
//			* Bug in Windows.  Despite the fact that every item in the
//			* table always has LPSTR_TEXTCALLBACK, Windows caches the
//			* bounds for the selected items.  This means that 
//			* when you change the string to be something else, Windows
//			* correctly asks you for the new string but when the item
//			* is selected, the selection draws using the bounds of the
//			* previous item.  The fix is to reset LPSTR_TEXTCALLBACK
//			* even though it has not changed, causing Windows to flush
//			* cached bounds.
//			*/
//			if ((style & SWT.VIRTUAL) == 0 && item.cached) {
//				if (lvItem == null) {
//					lvItem = new LVITEM ();
//					lvItem.mask = OS.LVIF_TEXT | OS.LVIF_INDENT;
//					lvItem.pszText = OS.LPSTR_TEXTCALLBACK;
//				}
//				lvItem.iItem = i;
//				OS.SendMessage (handle, OS.LVM_SETITEM, 0, lvItem);
//				item.cached = false;
//			}
//			if (currentItem == null && drawCount == 0 && OS.IsWindowVisible (handle)) {
//				OS.SendMessage (handle, OS.LVM_REDRAWITEMS, index, index);
//			}
//		}
//	}
//	if (cleared) setScrollWidth (null, false);
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
  for (int i=itemList.size()-1; i>=0; i--) {
    TableItem item = (TableItem)itemList.get(i);
    if(item != null) {
      item.clear();
    }
  }
  handle.repaint();
//	LVITEM lvItem = null;
//	boolean cleared = false;
//	int count = OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
//	for (int i=0; i<count; i++) {
//		TableItem item = items [i];
//		if (item != null) {
//			if (item != currentItem) {
//				cleared = true;
//				item.clear ();
//			}
//			/*
//			* Bug in Windows.  Despite the fact that every item in the
//			* table always has LPSTR_TEXTCALLBACK, Windows caches the
//			* bounds for the selected items.  This means that 
//			* when you change the string to be something else, Windows
//			* correctly asks you for the new string but when the item
//			* is selected, the selection draws using the bounds of the
//			* previous item.  The fix is to reset LPSTR_TEXTCALLBACK
//			* even though it has not changed, causing Windows to flush
//			* cached bounds.
//			*/
//			if ((style & SWT.VIRTUAL) == 0 && item.cached) {
//				if (lvItem == null) {
//					lvItem = new LVITEM ();
//					lvItem.mask = OS.LVIF_TEXT | OS.LVIF_INDENT;
//					lvItem.pszText = OS.LPSTR_TEXTCALLBACK;
//				}
//				lvItem.iItem = i;
//				OS.SendMessage (handle, OS.LVM_SETITEM, 0, lvItem);
//				item.cached = false;
//			}
//		}
//	}
//	if (cleared) {
//		if (currentItem == null && drawCount == 0 && OS.IsWindowVisible (handle)) {
//			OS.SendMessage (handle, OS.LVM_REDRAWITEMS, 0, count - 1);
//		}
//		setScrollWidth (null, false);
//	}
}

//public Point computeSize (int wHint, int hHint, boolean changed) {
//	checkWidget ();
//	if (fixScrollWidth) setScrollWidth (null, true);
//	int bits = 0;
//	if (wHint != SWT.DEFAULT) {
//		bits |= wHint & 0xFFFF;
//	} else {
//		int width = 0;
//		int hwndHeader = OS.SendMessage (handle, OS.LVM_GETHEADER, 0, 0);
//		int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
//		for (int i=0; i<count; i++) {
//			width += OS.SendMessage (handle, OS.LVM_GETCOLUMNWIDTH, i, 0);
//		}
//		bits |= width & 0xFFFF;
//	}
//	if (hHint != SWT.DEFAULT) bits |= hHint << 16;
//	int result = OS.SendMessage (handle, OS.LVM_APPROXIMATEVIEWRECT, -1, bits);
//	int width = result & 0xFFFF, height = result >> 16;
//	
//	/*
//	* Feature in Windows.  The height returned by LVM_APPROXIMATEVIEWRECT
//	* includes the trim plus the height of the items plus one extra row.
//	* The fix is to subtract the height of one row from the result height.
//	*/
//	int empty = OS.SendMessage (handle, OS.LVM_APPROXIMATEVIEWRECT, 0, 0);
//	int oneItem = OS.SendMessage (handle, OS.LVM_APPROXIMATEVIEWRECT, 1, 0);
//	height -= (oneItem >> 16) - (empty >> 16);
//	
//	if (width == 0) width = DEFAULT_WIDTH;
//	if (height == 0) height = DEFAULT_HEIGHT;
//	if (wHint != SWT.DEFAULT) width = wHint;
//	if (hHint != SWT.DEFAULT) height = hHint;
//	int border = getBorderWidth ();
//	width += border * 2;  height += border * 2;
//	if ((style & SWT.V_SCROLL) != 0) {
//		width += OS.GetSystemMetrics (OS.SM_CXVSCROLL);
//	}
//	if ((style & SWT.H_SCROLL) != 0) {
//		height += OS.GetSystemMetrics (OS.SM_CYHSCROLL);
//	}
//	return new Point (width, height);
//}

public Rectangle computeTrim(int x, int y, int width, int height) {
  CTable cTable = (CTable)handle;
  width += cTable.getVerticalScrollBar().getPreferredSize().width;
  height += cTable.getHorizontalScrollBar().getPreferredSize().height;
  return super.computeTrim(x, y, width, height);
}

void createHandleInit() {
  super.createHandleInit();
  state &= ~(CANVAS | THEME_BACKGROUND);
}

protected Container createHandle () {
  return (Container)CTable.Factory.newInstance(this, style);
}

void createItem (TableColumn column, int index) {
  for (int i=0; i<itemList.size(); i++) {
    TableItem item = (TableItem)itemList.get(i);
    if(item != null) {
      item.handle.insertColumn(index);
      if (index == 0) {
        item.text = "";
        item.image = null;
      }
    }
  }
  TableColumnModel columnModel = ((CTable)handle).getColumnModel();
  javax.swing.table.TableColumn tableColumn = (javax.swing.table.TableColumn)column.handle;
  if(columnList.isEmpty()) {
    // TODO: remove a first bogus column? (3)
    columnModel.removeColumn(columnModel.getColumn(0));
  }
  columnList.add(index, column);
  for(int i=columnModel.getColumnCount()-1; i>=0; i--) {
    javax.swing.table.TableColumn tColumn = columnModel.getColumn(i);
    int modelIndex = tColumn.getModelIndex();
    if(modelIndex >= index) {
      tColumn.setModelIndex(modelIndex + 1);
    }
  }
  columnModel.addColumn(tableColumn);
  tableColumn.setModelIndex(index);
  ((CTable)handle).moveColumn(getColumnCount()-1, index);
  // TODO: check it is enough
  handle.repaint();
}

void createItem (TableItem item, int index) {
	int count = getItemCount();
	if (!(0 <= index && index <= count)) error (SWT.ERROR_INVALID_RANGE);
  itemList.add(index, item);
  ((CTable)handle).addItem(index);
}

void createWidget () {
  itemList = new ArrayList();
  columnList = new ArrayList();
	super.createWidget ();
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
	checkWidget ();
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (indices.length == 0) return;
	DefaultListSelectionModel selectionModel = ((CTable)handle).getSelectionModel();
  isAdjustingSelection = true;
  for(int i=0; i<indices.length; i++) {
    int index = indices[i];
    selectionModel.removeSelectionInterval(index, index);
  }
  isAdjustingSelection = false;
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
	checkWidget ();
	if (index < 0) return;
  isAdjustingSelection = true;
  ((CTable)handle).getSelectionModel().removeSelectionInterval(index, index);
  isAdjustingSelection = false;
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
	checkWidget ();
  isAdjustingSelection = true;
  ((CTable)handle).getSelectionModel().removeSelectionInterval(start, end);
  isAdjustingSelection = false;
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
	checkWidget ();
  isAdjustingSelection = true;
  ((CTable)handle).getSelectionModel().clearSelection();
  isAdjustingSelection = false;
}

void destroyItem (TableColumn column) {
  int index = columnList.indexOf(column);
  for (int i=0; i<itemList.size(); i++) {
    TableItem item = (TableItem)itemList.get(i);
    if(item != null) {
      item.handle.removeColumn(index);
    }
  }
  columnList.remove(index);
  if(columnList.isEmpty()) {
    // TODO: add a first bogus column? (2)
    TableColumnModel columnModel = ((CTable)handle).getColumnModel();
    javax.swing.table.TableColumn tableColumn = new javax.swing.table.TableColumn(0);
    columnModel.addColumn(tableColumn);
  }
  if(sortColumn == column) {
    sortColumn = null;
    sortDirection = SWT.NONE;
  }
  handle.repaint();
}

void destroyItem (TableItem item) {
  int index = indexOf(item);
  itemList.remove(item);
  ((CTable)handle).removeItem(index);
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
	checkWidget ();
  if (columnList == null) error (SWT.ERROR_INVALID_RANGE);
  int count = getColumnCount();
  if (!(0 <= index && index < count)) error (SWT.ERROR_INVALID_RANGE);
  return (TableColumn)columnList.get(index);
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
	checkWidget ();
  return columnList == null? 0: columnList.size();
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
public int[] getColumnOrder () {
	checkWidget ();
	return ((CTable)handle).getColumnOrder();
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
	checkWidget ();
  if (columnList == null) return new TableColumn [0];
  return (TableColumn[])columnList.toArray(new TableColumn[0]);
}

///*
//* Not currently used.
//*/
//int getFocusIndex () {
////	checkWidget ();
//	return OS.SendMessage (handle, OS.LVM_GETNEXTITEM, -1, OS.LVNI_FOCUSED);
//}

/**
 * Returns the width in pixels of a grid line.
 *
 * @return the width of a grid line in pixels
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getGridLineWidth () {
	checkWidget ();
	return GRID_WIDTH;
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
  JTableHeader tableHeader = ((CTable)handle).getTableHeader();
  return tableHeader.isVisible()? tableHeader.getHeight(): 0;
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
	checkWidget ();
  return ((CTable)handle).getTableHeader().isVisible();
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
	checkWidget ();
	int count = getItemCount();
	if (!(0 <= index && index < count)) error (SWT.ERROR_INVALID_RANGE);
	return _getItem(index);
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
	checkWidget ();
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
  int row = ((CTable)handle).rowAtPoint(new java.awt.Point(point.x, point.y));
  if(row < 0) return null;
  return _getItem(row);
}

TableItem _getItem (int index) {
  TableItem tableItem = (TableItem)itemList.get(index);
  if ((style & SWT.VIRTUAL) == 0) return tableItem;
  if (tableItem != null) return tableItem;
  tableItem = new TableItem (this, SWT.NONE, -1, false);
  itemList.set(index, tableItem);
  return tableItem;
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
  return itemList == null? 0: itemList.size();
}

/**
 * Returns the height of the area which would be used to
 * display <em>one</em> of the items in the receiver's.
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
  return ((CTable)handle).getRowHeight();
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
	checkWidget ();
  if ((style & SWT.VIRTUAL) != 0) {
    TableItem[] items = new TableItem[getItemCount()];
    for(int i=0; i<items.length; i++) {
      items[i] = _getItem(i);
    }
    return items;
  }
  return (TableItem[])itemList.toArray(new TableItem[0]);
}

/**
 * Returns <code>true</code> if the receiver's lines are visible,
 * and <code>false</code> otherwise.
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
public boolean getLinesVisible () {
	checkWidget ();
  return ((CTable)handle).isGridVisible();
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
	checkWidget ();
  DefaultListSelectionModel selectionModel = ((CTable)handle).getSelectionModel();
  int minSelectionIndex = selectionModel.getMinSelectionIndex();
  int maxSelectionIndex = selectionModel.getMaxSelectionIndex();
  if(minSelectionIndex == -1 || maxSelectionIndex == -1) {
    return new TableItem[0];
  }
  TableItem[] selectedIndices_ = new TableItem[1 + maxSelectionIndex - minSelectionIndex];
  int count = 0;
  for(int i=minSelectionIndex; i<=maxSelectionIndex; i++) {
    if(selectionModel.isSelectedIndex(i)) {
      selectedIndices_[count++] = _getItem(i);
    }
  }
  TableItem[] selectedIndices = new TableItem[count];
  System.arraycopy(selectedIndices_, 0, selectedIndices, 0, count);
  return selectedIndices;
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
	checkWidget ();
  DefaultListSelectionModel selectionModel = ((CTable)handle).getSelectionModel();
  int minSelectionIndex = selectionModel.getMinSelectionIndex();
  int maxSelectionIndex = selectionModel.getMaxSelectionIndex();
  int count = 0;
  for(int i=minSelectionIndex; i<=maxSelectionIndex; i++) {
    if(selectionModel.isSelectedIndex(i)) {
      count++;
    }
  }
  return count;
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
	checkWidget ();
  DefaultListSelectionModel selectionModel = ((CTable)handle).getSelectionModel();
  return selectionModel.getMinSelectionIndex();
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
	checkWidget ();
  DefaultListSelectionModel selectionModel = ((CTable)handle).getSelectionModel();
  int minSelectionIndex = selectionModel.getMinSelectionIndex();
  int maxSelectionIndex = selectionModel.getMaxSelectionIndex();
  if(minSelectionIndex == -1 || maxSelectionIndex == -1) {
    return new int[0];
  }
  int[] selectedIndices_ = new int[1 + maxSelectionIndex - minSelectionIndex];
  int count = 0;
  for(int i=minSelectionIndex; i<=maxSelectionIndex; i++) {
    if(selectionModel.isSelectedIndex(i)) {
      selectedIndices_[count++] = i;
    }
  }
  int[] selectedIndices = new int[count];
  System.arraycopy(selectedIndices_, 0, selectedIndices, 0, count);
  return selectedIndices;
}

TableColumn sortColumn;
int sortDirection;

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
	checkWidget ();
  // Should we return -1 or 0 when there are no items?
  return Math.max(0, ((CTable)handle).getTopIndex());
}

//int imageIndex (Image image) {
//	if (image == null) return OS.I_IMAGENONE;
//	if (imageList == null) {
//		Rectangle bounds = image.getBounds ();
//		imageList = display.getImageList (new Point (bounds.width, bounds.height));
//		int index = imageList.indexOf (image);
//		if (index == -1) index = imageList.add (image);
//		int hImageList = imageList.getHandle ();
//		/*
//		* Bug in Windows.  Making any change to an item that
//		* changes the item height of a table while the table
//		* is scrolled can cause the lines to draw incorrectly.
//		* This happens even when the lines are not currently
//		* visible and are shown afterwards.  The fix is to
//		* save the top index, scroll to the top of the table
//		* and then restore the original top index.
//		*/
//		int topIndex = getTopIndex ();
//		setRedraw (false);
//		setTopIndex (0);
//		OS.SendMessage (handle, OS.LVM_SETIMAGELIST, OS.LVSIL_SMALL, hImageList);
//		setTopIndex (topIndex);
//		fixCheckboxImageList ();
//		setRedraw (true);
//		return index;
//	}
//	int index = imageList.indexOf (image);
//	if (index != -1) return index;
//	return imageList.add (image);
//}

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
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int indexOf (TableColumn column) {
	checkWidget ();
	if (column == null) error (SWT.ERROR_NULL_ARGUMENT);
  if (column.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
  return columnList.indexOf(column);
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
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int indexOf (TableItem item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	int count = getItemCount();
	if (1 <= lastIndexOf && lastIndexOf < count - 1) {
		if (itemList.get(lastIndexOf) == item) return lastIndexOf;
		if (itemList.get(lastIndexOf + 1) == item) return ++lastIndexOf;
		if (itemList.get(lastIndexOf - 1) == item) return --lastIndexOf;
	}
	if (lastIndexOf < count / 2) {
		for (int i=0; i<count; i++) {
			if (itemList.get(i) == item) return lastIndexOf = i;
		}
	} else {
		for (int i=count - 1; i>=0; --i) {
			if (itemList.get(i) == item) return lastIndexOf = i;
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
 * @return the visibility state of the item at the index
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean isSelected (int index) {
	checkWidget ();
  return ((CTable)handle).getSelectionModel().isSelectedIndex(index);
}

Point minimumSize (int wHint, int hHint, boolean changed) {
  java.awt.Dimension size = handle.getPreferredSize();
  return new Point(size.width, size.height);
}

void releaseChildren (boolean destroy) {
  if(itemList != null) {
    for (int i=itemList.size()-1; i>=0; i--) {
      TableItem item = (TableItem)itemList.get(i);
      if (item != null && !item.isDisposed ()) item.release (false);
    }
    itemList = null;
  }
  if(columnList != null) {
    for(int i=0; i<columnList.size(); i++) {
      TableColumn column = (TableColumn)columnList.get(i);
      if (!column.isDisposed ()) column.release (false);
    }
    columnList = null;
  }
//  
//  int hwndHeader =  OS.SendMessage (handle, OS.LVM_GETHEADER, 0, 0);
//	int columnCount = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
//	if (columnCount == 1 && columns [0] == null) columnCount = 0;
//	for (int i=0; i<columnCount; i++) {
//		TableColumn column = columns [i];
//		if (!column.isDisposed ()) column.releaseResources ();
//	}
//	columns = null;
//	int itemCount = OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
//
//	/*
//	* Feature in Windows 98.  When there are a large number
//	* of columns and items in a table (>1000) where each
//	* of the subitems in the table has a string, it is much
//	* faster to delete each item with LVM_DELETEITEM rather
//	* than using LVM_DELETEALLITEMS.  The fix is to detect
//	* this case and delete the items, one by one.  The fact
//	* that the fix is only necessary on Windows 98 was
//	* confirmed using version 5.81 of COMCTL32.DLL on both
//	* Windows 98 and NT.
//	*
//	* NOTE: LVM_DELETEALLITEMS is also sent by the table
//	* when the table is destroyed.
//	*/	
//	if (OS.IsWin95 && columnCount > 1) {
//		/* Turn off redraw and leave it off */
//		OS.SendMessage (handle, OS.WM_SETREDRAW, 0, 0);
//		for (int i=itemCount-1; i>=0; --i) {
//			TableItem item = items [i];
//			ignoreSelect = ignoreShrink = true;
//			OS.SendMessage (handle, OS.LVM_DELETEITEM, i, 0);
//			ignoreSelect = ignoreShrink = false;
//			if (item != null && !item.isDisposed ()) item.releaseResources ();
//		}
//	} else {	
//		for (int i=0; i<itemCount; i++) {
//			TableItem item = items [i];
//			if (item != null && !item.isDisposed ()) item.releaseResources ();
//		}
//	}
//	customDraw = false;
//	currentItem = null;
//	items = null;
//	if (imageList != null) {
//		OS.SendMessage (handle, OS.LVM_SETIMAGELIST, OS.LVSIL_SMALL, 0);
//		display.releaseImageList (imageList);
//	}
//	imageList = null;
//	int hOldList = OS.SendMessage (handle, OS.LVM_GETIMAGELIST, OS.LVSIL_STATE, 0);
//	OS.SendMessage (handle, OS.LVM_SETIMAGELIST, OS.LVSIL_STATE, 0);
//	if (hOldList != 0) OS.ImageList_Destroy (hOldList);
	super.releaseChildren(destroy);
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
	checkWidget ();
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (indices.length == 0) return;
	int [] newIndices = new int [indices.length];
	System.arraycopy (indices, 0, newIndices, 0, indices.length);
	// Sort puts the biggest indices first
	sort (newIndices);
	int start = newIndices [newIndices.length - 1], end = newIndices [0];
	int count = getItemCount();
	if (!(0 <= start && start <= end && end < count)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
  for(int i=0; i<newIndices.length; i++) {
    int index = newIndices[i];
    TableItem tableItem = (TableItem)itemList.get(index);
    if(tableItem != null) {
      tableItem.dispose();
    } else {
      itemList.remove(index);
      ((CTable)handle).removeItem(index);
    }
  }
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
	checkWidget ();
	int count = getItemCount();
	if (!(0 <= index && index < count)) error (SWT.ERROR_INVALID_RANGE);
  TableItem tableItem = (TableItem)itemList.get(index);
  if(tableItem != null) {
    tableItem.dispose();
  } else {
    itemList.remove(index);
    ((CTable)handle).removeItem(index);
  }
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
	checkWidget ();
	if (start > end) return;
	int count = getItemCount();
	if (!(0 <= start && start <= end && end < count)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
  for(int i=end; i>=start; i--) {
    TableItem tableItem = (TableItem)itemList.get(i);
    if(tableItem != null) {
      tableItem.dispose();
    } else {
      itemList.remove(i);
      ((CTable)handle).removeItem(i);
    }
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
  for(int i=itemList.size()-1; i>=0; i--) {
    TableItem tableItem = (TableItem)itemList.get(i);
    if(tableItem != null) {
      tableItem.dispose();
    } else {
      itemList.remove(i);
      ((CTable)handle).removeItem(i);
    }
  }
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the receiver's selection changes.
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
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

boolean isAdjustingSelection;

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
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	int length = indices.length;
	if (length == 0 || ((style & SWT.SINGLE) != 0 && length > 1)) return;
	DefaultListSelectionModel selectionModel = ((CTable)handle).getSelectionModel();
  isAdjustingSelection = true;
  for(int i=0; i<indices.length; i++) {
    int index = indices[i];
    selectionModel.addSelectionInterval(index, index);
  }
  isAdjustingSelection = false;
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
	checkWidget ();
  isAdjustingSelection = true;
  ((CTable)handle).getSelectionModel().addSelectionInterval(index, index);
  isAdjustingSelection = false;
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
	if (end < 0 || start > end || ((style & SWT.SINGLE) != 0 && start != end)) return;
  isAdjustingSelection = true;
  ((CTable)handle).getSelectionModel().addSelectionInterval(start, end);
  isAdjustingSelection = false;
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
	checkWidget ();
	if ((style & SWT.SINGLE) != 0) return;
  if(!itemList.isEmpty()) {
    isAdjustingSelection = true;
    ((CTable)handle).getSelectionModel().addSelectionInterval(0, itemList.size() - 1);
    isAdjustingSelection = false;
  }
}

//LRESULT sendMouseDownEvent (int type, int button, int msg, int wParam, int lParam) {
//	/*
//	* Feature in Windows.  Inside WM_LBUTTONDOWN and WM_RBUTTONDOWN,
//	* the widget starts a modal loop to determine if the user wants
//	* to begin a drag/drop operation or marque select.  Unfortunately,
//	* this modal loop eats the corresponding mouse up.  The fix is to
//	* detect the cases when the modal loop has eaten the mouse up and
//	* issue a fake mouse up.
//	*
//	* By observation, when the mouse is clicked anywhere but the check
//	* box, the widget eats the mouse up.  When the mouse is dragged,
//	* the widget does not eat the mouse up.
//	*/
//	LVHITTESTINFO pinfo = new LVHITTESTINFO ();
//	pinfo.x = (short) (lParam & 0xFFFF);
//	pinfo.y = (short) (lParam >> 16);
//	OS.SendMessage (handle, OS.LVM_HITTEST, 0, pinfo);
//	sendMouseEvent (type, button, handle, msg, wParam, lParam);
//
//	/*
//	* Force the table to have focus so that when the user
//	* reselects the focus item, the LVIS_FOCUSED state bits
//	* for the item will be set.  These bits are used when
//	* the table is multi-select to issue the selection
//	* event.  If the user did not click on an item, then
//	* set focus to the table so that it will come to the
//	* front and take focus in the work around below.
//	*/
//	OS.SetFocus (handle);
//		
//	/*
//	* Feature in Windows.  When the user selects outside of
//	* a table item, Windows deselects all the items, even
//	* when the table is multi-select.  While not strictly
//	* wrong, this is unexpected.  The fix is to detect the
//	* case and avoid calling the window proc.
//	*/
//	if (pinfo.iItem == -1) {
//		if (OS.GetCapture () != handle) OS.SetCapture (handle);
//		return LRESULT.ZERO;
//	}
//	
//	/*
//	* Feature in Windows.  When a table item is reselected
//	* in a single-select table, Windows does not issue a
//	* WM_NOTIFY because the item state has not changed.
//	* This is strictly correct but is inconsistent with the
//	* list widget and other widgets in Windows.  The fix is
//	* to detect the case when an item is reselected and issue
//	* the notification.
//	* 
//	* NOTE: This code runs for multi-select as well, ignoring
//	* the selection that is issed from WM_NOTIFY.
//	*/
//	boolean wasSelected = false;
//	int count = OS.SendMessage (handle, OS.LVM_GETSELECTEDCOUNT, 0, 0);
//	if (count == 1 && pinfo.iItem != -1) {
//		LVITEM lvItem = new LVITEM ();
//		lvItem.mask = OS.LVIF_STATE;
//		lvItem.stateMask = OS.LVIS_SELECTED;
//		lvItem.iItem = pinfo.iItem;
//		OS.SendMessage (handle, OS.LVM_GETITEM, 0, lvItem);
//		wasSelected = (lvItem.state & OS.LVIS_SELECTED) != 0;
//		if (wasSelected) ignoreSelect = true;
//	}
//	dragStarted = false;
//	int code = callWindowProc (handle, msg, wParam, lParam);
//	if (wasSelected) {
//		ignoreSelect = false;
//		Event event = new Event ();
//		event.item = _getItem (pinfo.iItem);
//		postEvent (SWT.Selection, event);
//	}
//	if (dragStarted) {
//		if (OS.GetCapture () != handle) OS.SetCapture (handle);
//	} else {
//		int flags = OS.LVHT_ONITEMLABEL | OS.LVHT_ONITEMICON;
//		boolean fakeMouseUp = (pinfo.flags & flags) != 0;
//		if (!fakeMouseUp && (style & SWT.MULTI) != 0) {
//			fakeMouseUp = (pinfo.flags & OS.LVHT_ONITEMSTATEICON) == 0;
//		}
//		if (fakeMouseUp) {
//			mouseDown = false;
//			sendMouseEvent (SWT.MouseUp, button, handle, msg, wParam, lParam);
//		}
//	}
//	dragStarted = false;
//	return new LRESULT (code);
//}

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
	if (order == null) error (SWT.ERROR_NULL_ARGUMENT);
  int columnCount = getColumnCount();
  if (order.length != columnCount) error (SWT.ERROR_INVALID_ARGUMENT);
  ((CTable)handle).setColumnOrder(order);
//	int hwndHeader = OS.SendMessage (handle, OS.LVM_GETHEADER, 0, 0);
//	int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0 );
//	if (count == 1 && columns [0] == null) {
//		if (order.length != 0) error (SWT.ERROR_INVALID_ARGUMENT);
//		return;
//	}
//	if (order.length != count) error (SWT.ERROR_INVALID_ARGUMENT);
//	int [] oldOrder = new int [count];
//	OS.SendMessage (handle, OS.LVM_GETCOLUMNORDERARRAY, count, oldOrder);
//	boolean reorder = false;
//	boolean [] seen = new boolean [count];
//	for (int i=0; i<order.length; i++) {
//		int index = order [i];
//		if (index < 0 || index >= count) error (SWT.ERROR_INVALID_RANGE);
//		if (seen [index]) error (SWT.ERROR_INVALID_ARGUMENT);
//		seen [index] = true;
//		if (index != oldOrder [i]) reorder = true;
//	}
//	if (reorder) {
//		RECT [] oldRects = new RECT [count];
//		for (int i=0; i<count; i++) {
//			oldRects [i] = new RECT ();
//			OS.SendMessage (hwndHeader, OS.HDM_GETITEMRECT, i, oldRects [i]);
//		}
//		OS.SendMessage (handle, OS.LVM_SETCOLUMNORDERARRAY, order.length, order);
//		/*
//		* Bug in Windows.  When LVM_SETCOLUMNORDERARRAY is used to change
//		* the column order, the header redraws correctly but the table does
//		* not.  The fix is to force a redraw.
//		*/
//		OS.InvalidateRect (handle, null, true);
//		TableColumn[] newColumns = new TableColumn [count];
//		System.arraycopy (columns, 0, newColumns, 0, count);
//		for (int i=0; i<count; i++) {
//			TableColumn column = newColumns [oldOrder [i]];
//			if (!column.isDisposed ()) {
//				if (order [i] != oldOrder [i]) {
//					column.sendEvent (SWT.Move);
//				}
//			}
//		}
//	}
}

//void setCheckboxImageListColor () {
//	if ((style & SWT.CHECK) == 0) return;
//	int hOldStateList = OS.SendMessage (handle, OS.LVM_GETIMAGELIST, OS.LVSIL_STATE, 0);
//	if (hOldStateList == 0) return;
//	int [] cx = new int [1], cy = new int [1];
//	OS.ImageList_GetIconSize (hOldStateList, cx, cy);
//	setCheckboxImageList (cx [0], cy [0]);
//}
//
//void setCheckboxImageList (int width, int height) {
//	if ((style & SWT.CHECK) == 0) return;
//	int count = 4;
//	int flags = ImageList.COLOR_FLAGS;
//	if ((style & SWT.RIGHT_TO_LEFT) != 0) flags |= OS.ILC_MIRROR;
//	int hImageList = OS.ImageList_Create (width, height, flags, count, count);
//	int hDC = OS.GetDC (handle);
//	int memDC = OS.CreateCompatibleDC (hDC);
//	int hBitmap = OS.CreateCompatibleBitmap (hDC, width * count, height);
//	int hOldBitmap = OS.SelectObject (memDC, hBitmap);
//	RECT rect = new RECT ();
//	OS.SetRect (rect, 0, 0, width * count, height);
//	int hBrush = OS.CreateSolidBrush (getBackgroundPixel ());
//	OS.FillRect (memDC, rect, hBrush);
//	OS.DeleteObject (hBrush);
//	int oldFont = OS.SelectObject (hDC, defaultFont ());
//	TEXTMETRIC tm = OS.IsUnicode ? (TEXTMETRIC) new TEXTMETRICW () : new TEXTMETRICA ();
//	OS.GetTextMetrics (hDC, tm);
//	OS.SelectObject (hDC, oldFont);
//	int itemWidth = Math.min (tm.tmHeight, width);
//	int itemHeight = Math.min (tm.tmHeight, height);
//	int left = (width - itemWidth) / 2, top = (height - itemHeight) / 2 + 1;
//	OS.SetRect (rect, left, top, left + itemWidth, top + itemHeight);
//	if (OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ()) {
//		int hTheme = OS.OpenThemeData (handle, BUTTON);
//		OS.DrawThemeBackground (hTheme, memDC, OS.BP_CHECKBOX, OS.CBS_UNCHECKEDNORMAL, rect, null);
//		rect.left += width;  rect.right += width;
//		OS.DrawThemeBackground (hTheme, memDC, OS.BP_CHECKBOX, OS.CBS_CHECKEDNORMAL, rect, null);
//		rect.left += width;  rect.right += width;
//		OS.DrawThemeBackground (hTheme, memDC, OS.BP_CHECKBOX, OS.CBS_UNCHECKEDNORMAL, rect, null);
//		rect.left += width;  rect.right += width;
//		OS.DrawThemeBackground (hTheme, memDC, OS.BP_CHECKBOX, OS.CBS_MIXEDNORMAL, rect, null);
//		OS.CloseThemeData (hTheme);
//	} else {
//		OS.DrawFrameControl (memDC, rect, OS.DFC_BUTTON, OS.DFCS_BUTTONCHECK | OS.DFCS_FLAT);
//		rect.left += width;  rect.right += width;
//		OS.DrawFrameControl (memDC, rect, OS.DFC_BUTTON, OS.DFCS_BUTTONCHECK | OS.DFCS_CHECKED | OS.DFCS_FLAT);
//		rect.left += width;  rect.right += width;
//		OS.DrawFrameControl (memDC, rect, OS.DFC_BUTTON, OS.DFCS_BUTTONCHECK | OS.DFCS_INACTIVE | OS.DFCS_FLAT);
//		rect.left += width;  rect.right += width;
//		OS.DrawFrameControl (memDC, rect, OS.DFC_BUTTON, OS.DFCS_BUTTONCHECK | OS.DFCS_CHECKED | OS.DFCS_INACTIVE | OS.DFCS_FLAT);
//	}
//	OS.SelectObject (memDC, hOldBitmap);
//	OS.DeleteDC (memDC);
//	OS.ReleaseDC (handle, hDC);
//	OS.ImageList_Add (hImageList, hBitmap, 0);
//	OS.DeleteObject (hBitmap);
//	int hOldStateList = OS.SendMessage (handle, OS.LVM_GETIMAGELIST, OS.LVSIL_STATE, 0);
//	OS.SendMessage (handle, OS.LVM_SETIMAGELIST, OS.LVSIL_STATE, hImageList);
//	if (hOldStateList != 0) OS.ImageList_Destroy (hOldStateList);
//}

void setFocusIndex (int index) {
//	checkWidget ();	
  if(index < 0 || index >= getItemCount()) return;
  DefaultListSelectionModel selectionModel = ((CTable)handle).getSelectionModel();
  selectionModel.addSelectionInterval(index, index);
  selectionModel.setAnchorSelectionIndex(index);
  selectionModel.setLeadSelectionIndex(index);
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
  ((CTable)handle).setHeaderVisible(show);
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
	int itemCount = getItemCount();
	if (count == itemCount) return;
	boolean isVirtual = (style & SWT.VIRTUAL) != 0;
//	if (!isVirtual) setRedraw (false);
	int index = count;
	int tmpItemCount = itemCount;
	while (index < tmpItemCount) {
		TableItem item = (TableItem)itemList.get(tmpItemCount - 1);
//		if (!isVirtual) {
//      ((CTable)handle).removeItem(index);
//		}
		if (item != null && !item.isDisposed ()) {
		  item.release (true);
		} else {
		  itemList.remove(index);
		  ((CTable)handle).removeItem(index);
		}
		tmpItemCount--;
	}
//	if (index < itemCount) error (SWT.ERROR_ITEM_NOT_REMOVED);
  itemList.ensureCapacity(count);
  for(int i=itemCount; i<count; i++) {
    if (isVirtual) {
      itemList.add(null);
      ((CTable)handle).addItem(i);
    } else {
      new TableItem (this, SWT.NONE, i, true);
    }
  }
//	if (isVirtual) {
////  TODO: notify item deleted?
//	} else {
//		for (int i=itemCount; i<count; i++) {
//			itemList.set(i, new TableItem (this, SWT.NONE, i, false));
//		}
//	}
  ((CTable)handle).getModel().fireTableDataChanged();
//	if (!isVirtual) setRedraw (true);
}

/**
 * Sets the height of the area which would be used to
 * display <em>one</em> of the items in the table.
 *
 * @return the height of one item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.2
 */
/*public*/ void setItemHeight (int itemHeight) {
  checkWidget ();
  if (itemHeight < 0) error (SWT.ERROR_INVALID_ARGUMENT);
  ((CTable)handle).setRowHeight(itemHeight);
}

/**
 * Marks the receiver's lines as visible if the argument is <code>true</code>,
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
public void setLinesVisible (boolean show) {
	checkWidget ();
  ((CTable)handle).setGridVisible(show);
}

//public void setRedraw (boolean redraw) {
//	checkWidget ();
//	/*
//	 * Feature in Windows.  When WM_SETREDRAW is used to turn
//	 * off drawing in a widget, it clears the WS_VISIBLE bits
//	 * and then sets them when redraw is turned back on.  This
//	 * means that WM_SETREDRAW will make a widget unexpectedly
//	 * visible.  The fix is to track the visibility state while
//	 * drawing is turned off and restore it when drawing is turned
//	 * back on.
//	 */
//	if (drawCount == 0) {
//		int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
//		if ((bits & OS.WS_VISIBLE) == 0) state |= HIDDEN;
//	}
//	if (redraw) {
//		if (--drawCount == 0) {
//			/*
//			* When many items are added to a table, it is faster to
//			* temporarily unsubclass the window proc so that messages
//			* are dispatched directly to the table.
//			*
//			* NOTE: This is optimization somewhat dangerous because any
//			* operation can occur when redraw is turned off, even operations
//			* where the table must be subclassed in order to have the correct
//			* behavior or work around a Windows bug.
//			* 
//			* This code is intentionally commented. 
//			*/
////			subclass ();
//			
//			/* Set the width of the horizontal scroll bar */
//			setScrollWidth (null, true);
//
//			OS.SendMessage (handle, OS.WM_SETREDRAW, 1, 0);
//			int hwndHeader = OS.SendMessage (handle, OS.LVM_GETHEADER, 0, 0);	
//			if (hwndHeader != 0) OS.SendMessage (hwndHeader, OS.WM_SETREDRAW, 1, 0);
//			if ((state & HIDDEN) != 0) {
//				state &= ~HIDDEN;
//				OS.ShowWindow (handle, OS.SW_HIDE);
//			} else {
//				if (OS.IsWinCE) {
//					OS.InvalidateRect (handle, null, false);
//					if (hwndHeader != 0) {
//						OS.InvalidateRect (hwndHeader, null, false);
//					}
//				} else {
//					int flags = OS.RDW_ERASE | OS.RDW_FRAME | OS.RDW_INVALIDATE | OS.RDW_ALLCHILDREN;
//					OS.RedrawWindow (handle, null, 0, flags);
//				}
//			}
//		}
//	} else {
//		if (drawCount++ == 0) {
//			OS.SendMessage (handle, OS.WM_SETREDRAW, 0, 0);
//			int hwndHeader = OS.SendMessage (handle, OS.LVM_GETHEADER, 0, 0);
//			if (hwndHeader != 0) OS.SendMessage (hwndHeader, OS.WM_SETREDRAW, 0, 0);
//			
//			/*
//			* When many items are added to a table, it is faster to
//			* temporarily unsubclass the window proc so that messages
//			* are dispatched directly to the table.
//			*
//			* NOTE: This is optimization somewhat dangerous because any
//			* operation can occur when redraw is turned off, even operations
//			* where the table must be subclassed in order to have the correct
//			* behavior or work around a Windows bug.
//			*
//			* This code is intentionally commented. 
//			*/
////			unsubclass ();
//		}
//	}
//}

//boolean setScrollWidth (TableItem item, boolean force) {
//	if (currentItem != null) {
//		if (currentItem != item) fixScrollWidth = true;
//		return false;
//	}
//	if (!force && (drawCount != 0 || !OS.IsWindowVisible (handle))) {
//		fixScrollWidth = true;
//		return false;
//	}
//	fixScrollWidth = false;
//	int hwndHeader = OS.SendMessage (handle, OS.LVM_GETHEADER, 0, 0);
//	int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
//	/*
//	* NOTE: It is much faster to measure the strings and compute the
//	* width of the scroll bar in non-virtual table rather than using
//	* LVM_SETCOLUMNWIDTH with LVSCW_AUTOSIZE.
//	*/
//	if (count == 1 && columns [0] == null) {
//		int newWidth = 0;
//		count = OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
//		int index = 0;
//		int imageIndent = 0;
//		while (index < count) {
//			String string = null;
//			int font = -1;
//			if (item != null) {
//				string = item.text;
//				imageIndent = Math.max (imageIndent, item.imageIndent);
//				if (item.cellFont != null) font = item.cellFont [0];
//				if (font == -1) font = item.font;
//			} else {
//				if (items [index] != null) {
//					TableItem tableItem = items [index];
//					string = tableItem.text;
//					imageIndent = Math.max (imageIndent, tableItem.imageIndent);
//					if (tableItem.cellFont != null) font = tableItem.cellFont [0];
//					if (font == -1) font = tableItem.font;
//				}
//			}
//			if (string != null && string.length () != 0) {
//				if (font != -1) {
//					int hDC = OS.GetDC (handle);
//					int oldFont = OS.SelectObject (hDC, font);
//					int flags = OS.DT_CALCRECT | OS.DT_SINGLELINE | OS.DT_NOPREFIX;
//					TCHAR buffer = new TCHAR (getCodePage (), string, false);
//					RECT rect = new RECT ();
//					OS.DrawText (hDC, buffer, buffer.length (), rect, flags);
//					OS.SelectObject (hDC, oldFont);
//					OS.ReleaseDC (handle, hDC);
//					newWidth = Math.max (newWidth, rect.right - rect.left);
//				} else {
//					TCHAR buffer = new TCHAR (getCodePage (), string, true);
//					newWidth = Math.max (newWidth, OS.SendMessage (handle, OS.LVM_GETSTRINGWIDTH, 0, buffer));
//				}
//			}
//			if (item != null) break;
//			index++;
//		}
//		int hStateList = OS.SendMessage (handle, OS.LVM_GETIMAGELIST, OS.LVSIL_STATE, 0);
//		if (hStateList != 0) {
//			int [] cx = new int [1], cy = new int [1];
//			OS.ImageList_GetIconSize (hStateList, cx, cy);
//			newWidth += cx [0] + INSET;
//		}
//		int hImageList = OS.SendMessage (handle, OS.LVM_GETIMAGELIST, OS.LVSIL_SMALL, 0);
//		if (hImageList != 0) {
//			int [] cx = new int [1], cy = new int [1];
//			OS.ImageList_GetIconSize (hImageList, cx, cy);
//			newWidth += (imageIndent + 1) * cx [0];
//		} else {
//			/*
//			* Bug in Windows.  When LVM_SETIMAGELIST is used to remove the
//			* image list by setting it to NULL, the item width and height
//			* is not changed and space is reserved for icons despite the
//			* fact that there are none.  The fix is to set the image list
//			* to be very small before setting it to NULL.  This causes
//			* Windows to reserve the smallest possible space when an image
//			* list is removed.  In this case, the scroll width must be one
//			* pixel larger.
//			*/
//			newWidth++;
//		}
//		newWidth += INSET * 2;
//		int oldWidth = OS.SendMessage (handle, OS.LVM_GETCOLUMNWIDTH, 0, 0);
//		if (newWidth > oldWidth) {
//			OS.SendMessage (handle, OS.LVM_SETCOLUMNWIDTH, 0, newWidth);
//			return true;
//		}
//	}
//	return false;
//}

/**
 * Selects the items at the given zero-relative indices in the receiver.
 * The current selection is cleared before the new items are selected.
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
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	deselectAll ();
	int length = indices.length;
	if (length == 0 || ((style & SWT.SINGLE) != 0 && length > 1)) return;
	select (indices);
	int focusIndex = indices [0];
	if (focusIndex != -1) setFocusIndex (focusIndex);
	showSelection ();
}

/**
 * Sets the receiver's selection to the given item.
 * The current selection is cleared before the new item is selected.
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
public void setSelection (TableItem  item) {
  checkWidget ();
  if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
  setSelection (new TableItem [] {item});
}

/**
 * Sets the receiver's selection to be the given array of items.
 * The current selection is cleared before the new items are selected.
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
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
  isAdjustingSelection = true;
	deselectAll ();
  isAdjustingSelection = false;
	int length = items.length;
	if (length == 0 || ((style & SWT.SINGLE) != 0 && length > 1)) return;
	int focusIndex = -1;
	for (int i=length-1; i>=0; --i) {
		int index = indexOf (items [i]);
		if (index != -1) {
			select (focusIndex = index);
		}
	}
	if (focusIndex != -1) setFocusIndex (focusIndex);
	showSelection ();
}

/**
 * Selects the item at the given zero-relative index in the receiver. 
 * The current selection is first cleared, then the new item is selected.
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
	deselectAll ();
	select (index);
	if (index != -1) setFocusIndex (index);
	showSelection ();
}

/**
 * Selects the items in the range specified by the given zero-relative
 * indices in the receiver. The range of indices is inclusive.
 * The current selection is cleared before the new items are selected.
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
	deselectAll ();
	if (end < 0 || start > end || ((style & SWT.SINGLE) != 0 && start != end)) return;
  ((CTable)handle).getSelectionModel().setSelectionInterval(start, end);
	showSelection ();
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
  if (column != null && column.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
  sortColumn = column;
  handle.repaint();
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
public void setSortDirection (int direction) {
  checkWidget ();
  if ((direction & (SWT.UP | SWT.DOWN)) == 0 && direction != SWT.NONE) return;
  sortDirection = direction;
  handle.repaint();
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
	checkWidget ();
  ((CTable)handle).setTopIndex(index);
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
	if (column == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (column.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	if (column.parent != this) return;
	int index = indexOf (column);
	if (index == -1) return;
  ((CTable)handle).ensureColumnVisible(index);
}

//void showItem (int index) {
//	/*
//	* Bug in Windows.  For some reason, when there is insufficient space
//	* to show an item, LVM_ENSUREVISIBLE causes blank lines to be
//	* inserted at the top of the widget.  A call to LVM_GETTOPINDEX will
//	* return a negative number (this is an impossible result).  The fix 
//	* is to use LVM_GETCOUNTPERPAGE to detect the case when the number 
//	* of visible items is zero and use LVM_ENSUREVISIBLE with the
//	* fPartialOK flag set to true to scroll the table.
//	*/
//	if (OS.SendMessage (handle, OS.LVM_GETCOUNTPERPAGE, 0, 0) <= 0) {
//		/*
//		* Bug in Windows.  For some reason, LVM_ENSUREVISIBLE can
//		* scroll one item more or one item less when there is not
//		* enough space to show a single table item.  The fix is
//		* to detect the case and call LVM_ENSUREVISIBLE again with
//		* the same arguments.  It seems that once LVM_ENSUREVISIBLE
//		* has scrolled into the general area, it is able to scroll
//		* to the exact item.
//		*/
//		OS.SendMessage (handle, OS.LVM_ENSUREVISIBLE, index, 1);
//		if (index != OS.SendMessage (handle, OS.LVM_GETTOPINDEX, 0, 0)) {
//			OS.SendMessage (handle, OS.LVM_ENSUREVISIBLE, index, 1);
//		}		
//	} else {
//		OS.SendMessage (handle, OS.LVM_ENSUREVISIBLE, index, 0);
//	}
//}

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
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	int index = indexOf (item);
	if (index != -1) {
    ((CTable)handle).ensureRowVisible(index);
  }
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
	checkWidget (); 
  int selectionIndex = getSelectionIndex();
  if(selectionIndex == -1) {
    return;
  }
  ((CTable)handle).ensureRowVisible(selectionIndex);
}

static int checkStyle (int style) {
  style |= SWT.H_SCROLL | SWT.V_SCROLL;
	return checkBits (style, SWT.SINGLE, SWT.MULTI, 0, 0, 0, 0);
}

//String toolTipText (NMTTDISPINFO hdr) {
//	int hwndToolTip = OS.SendMessage (handle, OS.LVM_GETTOOLTIPS, 0, 0);
//	if (hwndToolTip == hdr.hwndFrom && toolTipText != null) return ""; //$NON-NLS-1$
//	return super.toolTipText (hdr);
//}
//
//int widgetStyle () {
//	int bits = super.widgetStyle () | OS.LVS_SHAREIMAGELISTS;
//	if ((style & SWT.HIDE_SELECTION) == 0) bits |= OS.LVS_SHOWSELALWAYS;
//	if ((style & SWT.SINGLE) != 0) bits |= OS.LVS_SINGLESEL;
//	/*
//	* This code is intentionally commented.  In the future,
//	* the FLAT bit may be used to make the header flat and
//	* unresponsive to mouse clicks.
//	*/
////	if ((style & SWT.FLAT) != 0) bits |= OS.LVS_NOSORTHEADER;
//	bits |= OS.LVS_REPORT | OS.LVS_NOCOLUMNHEADER;
//	if ((style & SWT.VIRTUAL) != 0) bits |= OS.LVS_OWNERDATA;
//	return bits;
//}
//
//TCHAR windowClass () {
//	return TableClass;
//}
//
//int windowProc () {
//	return TableProc;
//}
//
//LRESULT WM_CHAR (int wParam, int lParam) {
//	LRESULT result = super.WM_CHAR (wParam, lParam);
//	if (result != null) return result;
//	switch (wParam) {
//		case SWT.CR:
//			/*
//			* Feature in Windows.  Windows sends LVN_ITEMACTIVATE from WM_KEYDOWN
//			* instead of WM_CHAR.  This means that application code that expects
//			* to consume the key press and therefore avoid a SWT.DefaultSelection
//			* event will fail.  The fix is to ignore LVN_ITEMACTIVATE when it is
//			* caused by WM_KEYDOWN and send SWT.DefaultSelection from WM_CHAR.
//			*/
//			int index = OS.SendMessage (handle, OS.LVM_GETNEXTITEM, -1, OS.LVNI_FOCUSED);
//			if (index != -1) {
//				Event event = new Event ();
//				event.item = _getItem (index);
//				postEvent (SWT.DefaultSelection, event);
//			}
//			return LRESULT.ZERO;
//	}
//	return result;
//}
//
//LRESULT WM_ERASEBKGND (int wParam, int lParam) {
//	LRESULT result = super.WM_ERASEBKGND (wParam, lParam);
//	if (result != null) return result;
//	/*
//	* This code is intentionally commented.  When a table contains
//	* images that are not in the first column, the work around causes
//	* pixel corruption.
//	*/
////	if (!OS.IsWindowEnabled (handle)) return result;
////	/*
////	* Feature in Windows.  When WM_ERASEBKGND is called,
////	* it clears the damaged area by filling it with the
////	* background color.  During WM_PAINT, when the table
////	* items are drawn, the background for each item is
////	* also drawn, causing flashing.  The fix is to adjust
////	* the damage by subtracting the bounds of each visible
////	* table item.
////	*/
////	int itemCount = getItemCount ();
////	if (itemCount == 0) return result;
////	GCData data = new GCData();
////	data.device = display;
////	GC gc = GC.win32_new (wParam, data);
////	Region region = new Region (display);
////	gc.getClipping (region);
////	int columnCount = Math.max (1, getColumnCount ());
////	Rectangle clientArea = getClientArea ();
////	int i = getTopIndex ();
////	int bottomIndex = i + OS.SendMessage (handle, OS.LVM_GETCOUNTPERPAGE, 0, 0);
////	bottomIndex = Math.min (itemCount, bottomIndex);
////	while (i < bottomIndex) {
////		int j = 0;
////		while (j < columnCount) {
////			if (j != 0 || (!isSelected (i) && i != getFocusIndex ())) {
////				RECT rect = new RECT ();
////				rect.top = j;
////				rect.left = OS.LVIR_LABEL;
////				OS.SendMessage (handle, OS. LVM_GETSUBITEMRECT, i, rect);
////				int width = Math.max (0, rect.right - rect.left);
////				int height = Math.max (0, rect.bottom - rect.top);
////				Rectangle rect2 = new Rectangle (rect.left, rect.top, width, height);
////				if (!rect2.intersects (clientArea)) break;
////				region.subtract (rect2);
////			}
////			j++;
////		}
////		i++;
////	}
////	gc.setClipping (region);
////	drawBackground (wParam);
////	gc.setClipping ((Region) null);
////	region.dispose ();
////	gc.dispose ();
////	return LRESULT.ONE;
//	return result;
//}
//
//LRESULT WM_GETOBJECT (int wParam, int lParam) {
//	/*
//	* Ensure that there is an accessible object created for this
//	* control because support for checked item accessibility is
//	* temporarily implemented in the accessibility package.
//	*/
//	if ((style & SWT.CHECK) != 0) {
//		if (accessible == null) accessible = new_Accessible (this);
//	}
//	return super.WM_GETOBJECT (wParam, lParam);
//}
//
//LRESULT WM_KEYDOWN (int wParam, int lParam) {
//	LRESULT result = super.WM_KEYDOWN (wParam, lParam);
//	if (result != null) return result;
//	if ((style & SWT.CHECK) != 0 && wParam == OS.VK_SPACE) {
//		int index = OS.SendMessage (handle, OS.LVM_GETNEXTITEM, -1, OS.LVNI_FOCUSED);
//		if (index != -1) {
//			TableItem item = _getItem (index);
//			item.setChecked (!item.getChecked (), true);
//			if (!OS.IsWinCE) {
//				OS.NotifyWinEvent (OS.EVENT_OBJECT_FOCUS, handle, OS.OBJID_CLIENT, index + 1);
//			}
//		}
//	}
//	return result;
//}
//
//LRESULT WM_LBUTTONDBLCLK (int wParam, int lParam) {
//	mouseDown = true;
//
//	/*
//	* Feature in Windows.  When the user selects outside of
//	* a table item, Windows deselects all the items, even
//	* when the table is multi-select.  While not strictly
//	* wrong, this is unexpected.  The fix is to detect the
//	* case and avoid calling the window proc.
//	*/
//	LVHITTESTINFO pinfo = new LVHITTESTINFO ();
//	pinfo.x = (short) (lParam & 0xFFFF);
//	pinfo.y = (short) (lParam >> 16);
//	int index = OS.SendMessage (handle, OS.LVM_HITTEST, 0, pinfo);
//	sendMouseEvent (SWT.MouseDown, 1, handle, OS.WM_LBUTTONDOWN, wParam, lParam);
//	sendMouseEvent (SWT.MouseDoubleClick, 1, handle, OS.WM_LBUTTONDBLCLK, wParam, lParam);
//	if (pinfo.iItem != -1) callWindowProc (handle, OS.WM_LBUTTONDBLCLK, wParam, lParam);
//	if (OS.GetCapture () != handle) OS.SetCapture (handle);
//	
//	/* Look for check/uncheck */
//	if ((style & SWT.CHECK) != 0) {
//		/*
//		* Note that when the table has LVS_EX_FULLROWSELECT and the
//		* user clicks anywhere on a row except on the check box, all
//		* of the bits are set.  The hit test flags are LVHT_ONITEM.
//		* This means that a bit test for LVHT_ONITEMSTATEICON is not
//		* the correct way to determine that the user has selected
//		* the check box, equality is needed.
//		*/
//		if (index != -1 && pinfo.flags == OS.LVHT_ONITEMSTATEICON) {
//			TableItem item = _getItem (index);
//			item.setChecked (!item.getChecked (), true);
//			if (!OS.IsWinCE) {
//				OS.NotifyWinEvent (OS.EVENT_OBJECT_FOCUS, handle, OS.OBJID_CLIENT, index + 1);
//			}
//		}	
//	}
//	return LRESULT.ZERO;
//}
//
//LRESULT WM_LBUTTONDOWN (int wParam, int lParam) {
//	mouseDown = true;
//	
//	/*
//	* Feature in Windows.  For some reason, capturing
//	* the mouse after processing the mouse event for the
//	* widget interferes with the normal mouse processing
//	* for the widget.  The fix is to avoid the automatic
//	* mouse capture.
//	*/
//	LRESULT result = sendMouseDownEvent (SWT.MouseDown, 1, OS.WM_LBUTTONDOWN, wParam, lParam);
//
//	/* Look for check/uncheck */
//	if ((style & SWT.CHECK) != 0) {
//		LVHITTESTINFO pinfo = new LVHITTESTINFO ();
//		pinfo.x = (short) (lParam & 0xFFFF);
//		pinfo.y = (short) (lParam >> 16);
//		/*
//		* Note that when the table has LVS_EX_FULLROWSELECT and the
//		* user clicks anywhere on a row except on the check box, all
//		* of the bits are set.  The hit test flags are LVHT_ONITEM.
//		* This means that a bit test for LVHT_ONITEMSTATEICON is not
//		* the correct way to determine that the user has selected
//		* the check box, equality is needed.
//		*/
//		int index = OS.SendMessage (handle, OS.LVM_HITTEST, 0, pinfo);
//		if (index != -1 && pinfo.flags == OS.LVHT_ONITEMSTATEICON) {
//			TableItem item = _getItem (index);
//			item.setChecked (!item.getChecked (), true);
//			if (!OS.IsWinCE) {
//				OS.NotifyWinEvent (OS.EVENT_OBJECT_FOCUS, handle, OS.OBJID_CLIENT, index + 1);
//			}
//		}	
//	}
//	
//	return result;
//}
//
//LRESULT WM_LBUTTONUP (int wParam, int lParam) {
//	mouseDown = false;
//	return super.WM_LBUTTONUP (wParam, lParam);
//}
//
//LRESULT WM_MOUSEHOVER (int wParam, int lParam) {
//	/*
//	* Feature in Windows.  Despite the fact that hot
//	* tracking is not enabled, the hot tracking code
//	* in WM_MOUSEHOVER is executed causing the item
//	* under the cursor to be selected.  The fix is to
//	* avoid calling the window proc.
//	*/
//	LRESULT result = super.WM_MOUSEHOVER (wParam, lParam);
//	int bits = OS.SendMessage (handle, OS.LVM_GETEXTENDEDLISTVIEWSTYLE, 0, 0);
//	int mask = OS.LVS_EX_ONECLICKACTIVATE | OS.LVS_EX_TRACKSELECT | OS.LVS_EX_TWOCLICKACTIVATE;
//	if ((bits & mask) != 0) return result;
//	return LRESULT.ZERO;
//}
//
//LRESULT WM_PAINT (int wParam, int lParam) {
//	if (!ignoreShrink) {
//		/* Resize the item array to match the item count */
//		int count = OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
//		if (items.length > 4 && items.length - count > 3) {
//			int length = Math.max (4, (count + 3) / 4 * 4);
//			TableItem [] newItems = new TableItem [length];
//			System.arraycopy (items, 0, newItems, 0, count);
//			items = newItems;
//		}
//	}
//	if (fixScrollWidth) setScrollWidth (null, true);
//	return super.WM_PAINT (wParam, lParam);
//}
//
//LRESULT WM_NOTIFY (int wParam, int lParam) {
//	NMHDR hdr = new NMHDR ();
//	OS.MoveMemory (hdr, lParam, NMHDR.sizeof);
//	int hwndHeader = OS.SendMessage (handle, OS.LVM_GETHEADER, 0, 0);
//	if (hdr.hwndFrom == hwndHeader) {
//		/*
//		* Feature in Windows.  On NT, the automatically created
//		* header control is created as a UNICODE window, not an
//		* ANSI window despite the fact that the parent is created
//		* as an ANSI window.  This means that it sends UNICODE
//		* notification messages to the parent window on NT for
//		* no good reason.  The data and size in the NMHEADER and
//		* HDITEM structs is identical between the platforms so no
//		* different message is actually necessary.  Despite this,
//		* Windows sends different messages.  The fix is to look
//		* for both messages, despite the platform.  This works
//		* because only one will be sent on either platform, never
//		* both.
//		*/
//		switch (hdr.code) {
//			case OS.HDN_BEGINTRACKW:
//			case OS.HDN_BEGINTRACKA:
//			case OS.HDN_DIVIDERDBLCLICKW:
//			case OS.HDN_DIVIDERDBLCLICKA: {
//				int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
//				if (count == 1 && columns [0] == null) return LRESULT.ONE;
//				NMHEADER phdn = new NMHEADER ();
//				OS.MoveMemory (phdn, lParam, NMHEADER.sizeof);
//				TableColumn column = columns [phdn.iItem];
//				if (column != null && !column.getResizable ()) {
//					return LRESULT.ONE;
//				}
//				break;
//			}
//			case OS.HDN_BEGINDRAG: {
//				int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
//				if (count == 1 && columns [0] == null) return LRESULT.ONE;
//				NMHEADER phdn = new NMHEADER ();
//				OS.MoveMemory (phdn, lParam, NMHEADER.sizeof);
//				if (phdn.iItem != -1) {
//					TableColumn column = columns [phdn.iItem];
//					if (column != null && !column.getMoveable ()) {
//						OS.ReleaseCapture ();
//						return LRESULT.ONE;
//					}
//				}
//				break;
//			}
//			case OS.HDN_ENDDRAG: {
//				NMHEADER phdn = new NMHEADER ();
//				OS.MoveMemory (phdn, lParam, NMHEADER.sizeof);
//				if (phdn.iItem != -1 && phdn.pitem != 0) {
//					HDITEM pitem = new HDITEM ();
//					OS.MoveMemory (pitem, phdn.pitem, HDITEM.sizeof);
//					if ((pitem.mask & OS.HDI_ORDER) != 0 && pitem.iOrder != -1) {
//						int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
//						if (count == 1 && columns [0] == null) break;
//						int [] order = new int [count];
//						OS.SendMessage (handle, OS.LVM_GETCOLUMNORDERARRAY, count, order);
//						int index = 0;
//						while (index < order.length) {
//						 	if (order [index] == phdn.iItem) break;
//							index++;
//						}
//						if (index == order.length) index = 0;
//						if (index == pitem.iOrder) break;
//						int start = Math.min (index, pitem.iOrder);
//						int end = Math.max (index, pitem.iOrder);
//						for (int i=start; i<=end; i++) {
//							TableColumn column = columns [order [i]];
//							if (!column.isDisposed ()) {
//								column.postEvent (SWT.Move);
//							}
//						}
//					}
//				}
//				break;
//			}
//			case OS.HDN_ITEMCHANGEDW:
//			case OS.HDN_ITEMCHANGEDA: {
//				/*
//				* Bug in Windows.  When a table has the LVS_EX_GRIDLINES extended
//				* style and the user drags any column over the first column in the
//				* table, making the size become zero, when the user drags a column
//				* such that the size of the first column becomes non-zero, the grid
//				* lines are not redrawn.  The fix is to detect the case and force
//				* a redraw of the first column.
//				*/
//				int width = OS.SendMessage (handle, OS.LVM_GETCOLUMNWIDTH, 0, 0);
//				if (lastWidth == 0 && width > 0) {
//					int bits = OS.SendMessage (handle, OS.LVM_GETEXTENDEDLISTVIEWSTYLE, 0, 0);
//					if ((bits & OS.LVS_EX_GRIDLINES) != 0) {
//						RECT rect = new RECT ();
//						OS.GetClientRect (handle, rect);
//						rect.right = rect.left + width;
//						OS.InvalidateRect (handle, rect, true);
//					}
//				}
//				lastWidth = width;
//				if (!ignoreResize) {
//					NMHEADER phdn = new NMHEADER ();
//					OS.MoveMemory (phdn, lParam, NMHEADER.sizeof);
//					if (phdn.pitem != 0) {
//						HDITEM pitem = new HDITEM ();
//						OS.MoveMemory (pitem, phdn.pitem, HDITEM.sizeof);
//						if ((pitem.mask & OS.HDI_WIDTH) != 0) {
//							TableColumn column = columns [phdn.iItem];
//							if (column != null) {
//								column.sendEvent (SWT.Resize);
//								/*
//								* It is possible (but unlikely), that application
//								* code could have disposed the widget in the resize
//								* event.  If this happens, end the processing of the
//								* Windows message by returning zero as the result of
//								* the window proc.
//								*/
//								if (isDisposed ()) return LRESULT.ZERO;	
//								int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
//								if (count == 1 && columns [0] == null) count = 0;
//								/*
//								* It is possible (but unlikely), that application
//								* code could have disposed the column in the move
//								* event.  If this happens, process the move event
//								* for those columns that have not been destroyed.
//								*/
//								TableColumn [] newColumns = new TableColumn [count];
//								System.arraycopy (columns, 0, newColumns, 0, count);
//								int [] order = new int [count];
//								OS.SendMessage (handle, OS.LVM_GETCOLUMNORDERARRAY, count, order);
//								boolean moved = false;
//								for (int i=0; i<count; i++) {
//									TableColumn nextColumn = newColumns [order [i]];
//									if (moved && !nextColumn.isDisposed ()) {
//										nextColumn.sendEvent (SWT.Move);
//									}
//									if (nextColumn == column) moved = true;
//								}
//							}
//						}
//					}
//				}
//				break;
//			}
//			case OS.HDN_ITEMDBLCLICKW:      
//			case OS.HDN_ITEMDBLCLICKA: {
//				NMHEADER phdn = new NMHEADER ();
//				OS.MoveMemory (phdn, lParam, NMHEADER.sizeof);
//				TableColumn column = columns [phdn.iItem];
//				if (column != null) {
//					column.postEvent (SWT.DefaultSelection);
//				}
//				break;
//			}
//		}
//	}
//	LRESULT result = super.WM_NOTIFY (wParam, lParam);
//	if (result != null) return result;
//	switch (hdr.code) {
//		case OS.TTN_GETDISPINFOA:
//		case OS.TTN_GETDISPINFOW: {
//			tipRequested = true;
//			int code = callWindowProc (handle, OS.WM_NOTIFY, wParam, lParam);
//			tipRequested = false;
//			return new LRESULT (code);
//		}
//	}
//	return result;
//}
//
//LRESULT WM_RBUTTONDBLCLK (int wParam, int lParam) {
//	/*
//	* Feature in Windows.  When the user selects outside of
//	* a table item, Windows deselects all the items, even
//	* when the table is multi-select.  While not strictly
//	* wrong, this is unexpected.  The fix is to detect the
//	* case and avoid calling the window proc.
//	*/
//	LVHITTESTINFO pinfo = new LVHITTESTINFO ();
//	pinfo.x = (short) (lParam & 0xFFFF);
//	pinfo.y = (short) (lParam >> 16);
//	OS.SendMessage (handle, OS.LVM_HITTEST, 0, pinfo);
//	sendMouseEvent (SWT.MouseDown, 1, handle, OS.WM_RBUTTONDOWN, wParam, lParam);
//	sendMouseEvent (SWT.MouseDoubleClick, 1, handle, OS.WM_RBUTTONDBLCLK, wParam, lParam);
//	if (pinfo.iItem != -1) callWindowProc (handle, OS.WM_RBUTTONDBLCLK, wParam, lParam);
//	if (OS.GetCapture () != handle) OS.SetCapture (handle);
//	return LRESULT.ZERO;
//}
//
//LRESULT WM_RBUTTONDOWN (int wParam, int lParam) {
//	/*
//	* Feature in Windows.  For some reason, capturing
//	* the mouse after processing the mouse event for the
//	* widget interferes with the normal mouse processing
//	* for the widget.  The fix is to avoid the automatic
//	* mouse capture.
//	*/
//	return sendMouseDownEvent (SWT.MouseDown, 3, OS.WM_RBUTTONDOWN, wParam, lParam);
//}
//
//LRESULT WM_SETFOCUS (int wParam, int lParam) {
//	LRESULT result = super.WM_SETFOCUS (wParam, lParam);
//	/*
//	* Bug in Windows.  For some reason, the table does
//	* not set the default focus rectangle to be the first
//	* item in the table when it gets focus and there is
//	* no selected item.  The fix to make the first item
//	* be the focus item.
//	*/
//	int count = OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
//	if (count == 0) return result;
//	int index = OS.SendMessage (handle, OS.LVM_GETNEXTITEM, -1, OS.LVNI_FOCUSED);
//	if (index == -1) {
//		LVITEM lvItem = new LVITEM ();
//		lvItem.state = OS.LVIS_FOCUSED;
//		lvItem.stateMask = OS.LVIS_FOCUSED;
//		ignoreSelect = true;
//		OS.SendMessage (handle, OS.LVM_SETITEMSTATE, 0, lvItem);
//		ignoreSelect = false;
//	}
//	return result;
//}
//
//LRESULT WM_SIZE (int wParam, int lParam) {
//	/*
//	* Bug in Windows.  If the table column widths are adjusted
//	* in WM_SIZE, blank lines may be inserted at the top of the
//	* widget.  A call to LVM_GETTOPINDEX will return a negative
//	* number (this is an impossible result).  The fix is to do
//	* the WM_SIZE processing in WM_WINDOWPOSCHANGED after the
//	* table has been updated.
//	*/
//	return null;
//}
//
//LRESULT WM_SYSCOLORCHANGE (int wParam, int lParam) {
//	LRESULT result = super.WM_SYSCOLORCHANGE (wParam, lParam);
//	if (result != null) return result;
//	if (background == -1) {
//		int pixel = defaultBackground ();
//		OS.SendMessage (handle, OS.LVM_SETBKCOLOR, 0, pixel);
//		OS.SendMessage (handle, OS.LVM_SETTEXTBKCOLOR, 0, pixel);
//	}
//	if ((style & SWT.CHECK) != 0) setCheckboxImageListColor ();
//	return result;
//}
//
//LRESULT WM_VSCROLL (int wParam, int lParam) {
//	LRESULT result = super.WM_VSCROLL (wParam, lParam);
//	/*
//	* Bug in Windows.  When a table is drawing grid lines and the
//	* user scrolls vertically up or down by a line or a page, the
//	* table does not redraw the grid lines for newly exposed items.
//	* The fix is to invalidate the items.
//	*/
//	int bits = OS.SendMessage (handle, OS.LVM_GETEXTENDEDLISTVIEWSTYLE, 0, 0);
//	if ((bits & OS.LVS_EX_GRIDLINES) != 0) {
//		int code = wParam & 0xFFFF;
//		switch (code) {
//			case OS.SB_THUMBPOSITION:
//			case OS.SB_ENDSCROLL:
//			case OS.SB_THUMBTRACK:
//			case OS.SB_TOP:
//			case OS.SB_BOTTOM:
//				break;
//			case OS.SB_LINEDOWN:
//			case OS.SB_LINEUP:
//				int headerHeight = 0;
//				int hwndHeader = OS.SendMessage (handle, OS.LVM_GETHEADER, 0, 0);
//				if (hwndHeader != 0) {
//					RECT rect = new RECT ();					
//					OS.GetWindowRect (hwndHeader, rect);
//					headerHeight = rect.bottom - rect.top;
//				}
//				RECT rect = new RECT ();
//				OS.GetClientRect (handle, rect);
//				rect.top += headerHeight;
//				int empty = OS.SendMessage (handle, OS.LVM_APPROXIMATEVIEWRECT, 0, 0);
//				int oneItem = OS.SendMessage (handle, OS.LVM_APPROXIMATEVIEWRECT, 1, 0);
//				int itemHeight = (oneItem >> 16) - (empty >> 16);
//				if (code == OS.SB_LINEDOWN) {
//					rect.top = rect.bottom - itemHeight - GRID_WIDTH;
//				} else {
//					rect.bottom = rect.top + itemHeight + GRID_WIDTH;
//				}
//				OS.InvalidateRect (handle, rect, true);
//				break;
//			case OS.SB_PAGEDOWN:
//			case OS.SB_PAGEUP:
//				OS.InvalidateRect (handle, null, true);
//				break;
//		}
//	}
//	return result;
//}
//
//LRESULT WM_WINDOWPOSCHANGED (int wParam, int lParam) {
//	if (ignoreResize) return null;
//	LRESULT result = super.WM_WINDOWPOSCHANGED (wParam, lParam);
//	if (result != null) return result;
//	WINDOWPOS lpwp = new WINDOWPOS ();
//	OS.MoveMemory (lpwp, lParam, WINDOWPOS.sizeof);
//	if ((lpwp.flags & OS.SWP_NOSIZE) == 0) {
//		setResizeChildren (false);
//		int code = callWindowProc (handle, OS.WM_WINDOWPOSCHANGED, wParam, lParam);
//		sendEvent (SWT.Resize);
//		if (isDisposed ()) return new LRESULT (code);
//		if (layout != null) {
//			markLayout (false, false);
//			updateLayout (false, false);
//		}
//		setResizeChildren (true);
//		return new LRESULT (code);
//	}
//	return result;
//}
//	
//LRESULT wmNotifyChild (int wParam, int lParam) {
//	NMHDR hdr = new NMHDR ();
//	OS.MoveMemory (hdr, lParam, NMHDR.sizeof);
//	switch (hdr.code) {
//		case OS.LVN_ODFINDITEMA:
//		case OS.LVN_ODFINDITEMW: {
//			if ((style & SWT.VIRTUAL) != 0) {
//				NMLVFINDITEM pnmfi = new NMLVFINDITEM ();
//				OS.MoveMemory (pnmfi, lParam, NMLVFINDITEM.sizeof);
//				int index = Math.max (0, pnmfi.iStart - 1);
//				return new LRESULT (index);
//			}
//			break;
//		}
//		case OS.LVN_GETDISPINFOA:
//		case OS.LVN_GETDISPINFOW: {
////			if (drawCount != 0 || !OS.IsWindowVisible (handle)) break;
//			NMLVDISPINFO plvfi = new NMLVDISPINFO ();
//			OS.MoveMemory (plvfi, lParam, NMLVDISPINFO.sizeof);
//			lastIndexOf = plvfi.iItem;
//			TableItem item = _getItem (plvfi.iItem);
//			/*
//			* The cached flag is used by both virtual and non-virtual
//			* tables to indicate that Windows has asked at least once
//			* for a table item.
//			*/
//			if (!item.cached) {
//				if ((style & SWT.VIRTUAL) != 0) {
//					if (!checkData (item, false)) break;
//					TableItem newItem = fixScrollWidth ? null : item;
//					if (setScrollWidth (newItem, true)) {
//						OS.InvalidateRect (handle, null, true);
//					}
//				}
//				item.cached = true;
//			}
//			if ((plvfi.mask & OS.LVIF_TEXT) != 0) {
//				String string = null;
//				if (plvfi.iSubItem == 0) {
//					string = item.text;
//				} else {
//					String [] strings  = item.strings;
//					if (strings != null) string = strings [plvfi.iSubItem];
//				}
//				if (string != null) {
//					/*
//					* Bug in Windows.  When pszText points to a zero length
//					* NULL terminated string, Windows correctly draws the
//					* empty string but the cache of the bounds for the item
//					* is not reset.  This means that when the text for the
//					* item is set and then reset to an empty string, the
//					* selection draws using the bounds of the previous text.
//					* The fix is to use a space rather than an empty string
//					* when anything but a tool tip is requested (to avoid
//					* a tool tip that is a single space).
//					* 
//					* NOTE: This is only a problem for items in the first
//					* column.  Assigning NULL to other columns stops Windows
//					* from drawing the selection when LVS_EX_FULLROWSELECT
//					* is set.
//					*/
//					if (!tipRequested && string.length () == 0 && plvfi.iSubItem == 0) {
//						string = " "; //$NON-NLS-1$
//					}
//					TCHAR buffer = new TCHAR (getCodePage (), string, false);
//					int byteCount = Math.min (buffer.length (), plvfi.cchTextMax - 1) * TCHAR.sizeof;
//					OS.MoveMemory (plvfi.pszText, buffer, byteCount);
//					OS.MoveMemory (plvfi.pszText + byteCount, new byte [TCHAR.sizeof], TCHAR.sizeof);
//					plvfi.cchTextMax = Math.min (plvfi.cchTextMax, string.length () + 1);
//				}
//			}
//			if ((plvfi.mask & OS.LVIF_IMAGE) != 0) {
//				Image image = null;
//				if (plvfi.iSubItem == 0) {
//					image = item.image;
//				} else {
//					Image [] images = item.images;
//					if (images != null) image = images [plvfi.iSubItem];
//				}
//				if (image != null) plvfi.iImage = imageIndex (image);
//			}
//			if ((plvfi.mask & OS.LVIF_STATE) != 0) {
//				if (plvfi.iSubItem == 0) {
//					int state = 1;
//					if (item.checked) state++;
//					if (item.grayed) state +=2;
//					plvfi.state = state << 12;
//					plvfi.stateMask = OS.LVIS_STATEIMAGEMASK;
//				}
//			}
//			if ((plvfi.mask & OS.LVIF_INDENT) != 0) {
//				if (plvfi.iSubItem == 0) plvfi.iIndent = item.imageIndent;
//			}
//			OS.MoveMemory (lParam, plvfi, NMLVDISPINFO.sizeof);
//			break;
//		}
//		case OS.NM_CUSTOMDRAW: {
//			if (!customDraw) break;
//			NMLVCUSTOMDRAW nmcd = new NMLVCUSTOMDRAW ();
//			OS.MoveMemory (nmcd, lParam, NMLVCUSTOMDRAW.sizeof);
//			switch (nmcd.dwDrawStage) {
//				case OS.CDDS_PREPAINT: return new LRESULT (OS.CDRF_NOTIFYITEMDRAW);
//				case OS.CDDS_ITEMPREPAINT: return new LRESULT (OS.CDRF_NOTIFYSUBITEMDRAW);
//				case OS.CDDS_ITEMPREPAINT | OS.CDDS_SUBITEM: {
//					TableItem item = _getItem (nmcd.dwItemSpec);
//					int hFont = item.cellFont != null ? item.cellFont [nmcd.iSubItem] : -1;
//					if (hFont == -1) hFont = item.font;
//					int clrText = item.cellForeground != null ? item.cellForeground [nmcd.iSubItem] : -1;
//					if (clrText == -1) clrText = item.foreground;
//					int clrTextBk = item.cellBackground != null ? item.cellBackground [nmcd.iSubItem] : -1;
//					if (clrTextBk == -1) clrTextBk = item.background;
//					/*
//					* Feature in Windows.  When the font is set for one cell in a table,
//					* Windows does not reset the font for the next cell.  As a result,
//					* all subsequent cells are drawn using the new font.  The fix is to
//					* reset the font to the default.
//					* 
//					* NOTE: This does not happen for foreground and background.
//					*/
//					if (hFont == -1 && clrText == -1 && clrTextBk == -1) {
//						if (item.cellForeground == null && item.cellBackground == null && item.cellFont == null) {
//							int hwndHeader = OS.SendMessage (handle, OS.LVM_GETHEADER, 0, 0);
//							int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
//							if (count == 1) break;
//						}
//					}
//					if (hFont == -1) hFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
//					OS.SelectObject (nmcd.hdc, hFont);
//					if (OS.IsWindowEnabled (handle)) {
//						nmcd.clrText = clrText == -1 ? getForegroundPixel () : clrText;
//						nmcd.clrTextBk = clrTextBk == -1 ? getBackgroundPixel () : clrTextBk;
//						OS.MoveMemory (lParam, nmcd, NMLVCUSTOMDRAW.sizeof);
//					}
//					return new LRESULT (OS.CDRF_NEWFONT);
//				}
//			}
//			break;
//		}
//		case OS.LVN_MARQUEEBEGIN: return LRESULT.ONE;
//		case OS.LVN_BEGINDRAG:
//		case OS.LVN_BEGINRDRAG: {
//			dragStarted = true;
//			if (hdr.code == OS.LVN_BEGINDRAG) {
//				int pos = OS.GetMessagePos ();
//				POINT pt = new POINT ();
//				pt.x = (short) (pos & 0xFFFF);
//				pt.y = (short) (pos >> 16); 
//				OS.ScreenToClient (handle, pt);
//				Event event = new Event ();
//				event.x = pt.x;
//				event.y = pt.y;
//				postEvent (SWT.DragDetect, event);
//			}
//			break;
//		}
//		case OS.LVN_COLUMNCLICK: {
//			NMLISTVIEW pnmlv = new NMLISTVIEW ();
//			OS.MoveMemory(pnmlv, lParam, NMLISTVIEW.sizeof);
//			TableColumn column = columns [pnmlv.iSubItem];
//			if (column != null) {
//				column.postEvent (SWT.Selection);
//			}
//			break;
//		}
//		case OS.LVN_ITEMACTIVATE: {
//			if (ignoreActivate) break;
//			NMLISTVIEW pnmlv = new NMLISTVIEW ();
//			OS.MoveMemory(pnmlv, lParam, NMLISTVIEW.sizeof);
//			if (pnmlv.iItem != -1) {
//				Event event = new Event ();
//				event.item = _getItem (pnmlv.iItem);
//				postEvent (SWT.DefaultSelection, event);
//			}
//			break;
//		}
//		case OS.LVN_ITEMCHANGED: {
//			if (!ignoreSelect) {
//				NMLISTVIEW pnmlv = new NMLISTVIEW ();
//				OS.MoveMemory (pnmlv, lParam, NMLISTVIEW.sizeof);
//				if (pnmlv.iItem != -1 && (pnmlv.uChanged & OS.LVIF_STATE) != 0) {
//					boolean isFocus = (pnmlv.uNewState & OS.LVIS_FOCUSED) != 0;
//					int index = OS.SendMessage (handle, OS.LVM_GETNEXTITEM, -1, OS.LVNI_FOCUSED);
//					if ((style & SWT.MULTI) != 0) {
//						if (OS.GetKeyState (OS.VK_CONTROL) < 0) {
//							if (!isFocus) {
//								if (index == pnmlv.iItem) {
//									boolean isSelected = (pnmlv.uNewState & OS.LVIS_SELECTED) != 0;
//									boolean wasSelected = (pnmlv.uOldState & OS.LVIS_SELECTED) != 0;
//									isFocus = isSelected != wasSelected;
//								}
//							} else {
//								isFocus = mouseDown;
//							}
//						}
//					}
//					if (OS.GetKeyState (OS.VK_SPACE) < 0) isFocus = true;
//					if (isFocus) {
//						Event event = new Event();
//						if (index != -1) {
//							/*
//							* This code is intentionally commented.
//							*/
////							OS.SendMessage (handle, OS.LVM_ENSUREVISIBLE, index, 0);
//							event.item = _getItem (index);
//						}
//						postEvent (SWT.Selection, event);
//					}
//				}
//			}
//			break;
//		}
//		case OS.NM_RECOGNIZEGESTURE:
//			/* 
//			* Feature on Pocket PC.  The tree and table controls detect the tap
//			* and hold gesture by default. They send a GN_CONTEXTMENU message to show
//			* the popup menu.  This default behaviour is unwanted on Pocket PC 2002
//			* when no menu has been set, as it still draws a red circle.  The fix
//			* is to disable this default behaviour when no menu is set by returning
//			* TRUE when receiving the Pocket PC 2002 specific NM_RECOGNIZEGESTURE
//			* message.
//			*/
//			if (OS.IsPPC) {
//				boolean hasMenu = menu != null && !menu.isDisposed ();
//				if (!hasMenu && !hooks (SWT.MenuDetect)) return LRESULT.ONE;
//			}
//			break;
//		case OS.GN_CONTEXTMENU:
//			if (OS.IsPPC) {
//				boolean hasMenu = menu != null && !menu.isDisposed ();
//				if (hasMenu || hooks (SWT.MenuDetect)) {
//					NMRGINFO nmrg = new NMRGINFO ();
//					OS.MoveMemory (nmrg, lParam, NMRGINFO.sizeof);
//					showMenu (nmrg.x, nmrg.y);
//					return LRESULT.ONE;
//				}
//			}
//			break;
//	}
//	return super.wmNotifyChild (wParam, lParam);
//}

public void processEvent(EventObject e) {
  if(e instanceof CellPaintEvent) {
    switch(((CellPaintEvent)e).getType()) {
    case CellPaintEvent.ERASE_TYPE: if(!hooks(SWT.EraseItem)) { super.processEvent(e); return; } break;
    case CellPaintEvent.PAINT_TYPE: if(!hooks(SWT.PaintItem)) { super.processEvent(e); return; } break;
    case CellPaintEvent.MEASURE_TYPE: if(!hooks(SWT.MeasureItem)) { super.processEvent(e); return; } break;
    default: super.processEvent(e); return;
    }
  } else if(e instanceof ListSelectionEvent) {
    if(!hooks(SWT.Selection) || isAdjustingSelection) { super.processEvent(e); return; };
  } else {
    super.processEvent(e);
    return;
  }
  UIThreadUtils.startExclusiveSection(getDisplay());
  if(isDisposed()) {
    UIThreadUtils.stopExclusiveSection();
    super.processEvent(e);
    return;
  }
  try {
    if(e instanceof CellPaintEvent) {
      CellPaintEvent cellPaintEvent = (CellPaintEvent)e;
      switch(cellPaintEvent.getType()) {
      case CellPaintEvent.ERASE_TYPE: {
        TableItem tableItem = cellPaintEvent.tableItem.getTableItem();
        Rectangle cellBounds = tableItem.getBounds(cellPaintEvent.column);
        Event event = new Event();
        event.x = cellBounds.x;
        event.y = cellBounds.y;
        event.width = cellBounds.width;
        event.height = cellBounds.height;
        event.item = tableItem;
        event.index = cellPaintEvent.column;
        if(!cellPaintEvent.ignoreDrawForeground) event.detail |= SWT.FOREGROUND;
        if(!cellPaintEvent.ignoreDrawBackground) event.detail |= SWT.BACKGROUND;
        if(!cellPaintEvent.ignoreDrawSelection) event.detail |= SWT.SELECTED;
        if(!cellPaintEvent.ignoreDrawFocused) event.detail |= SWT.FOCUSED;
        event.gc = new GC(this);
        event.gc.handle.setUserClip(((CTable)handle).getCellRect(cellPaintEvent.row, cellPaintEvent.column, false));
//        event.gc.isSwingPainting = true;
        sendEvent(SWT.EraseItem, event);
        if(event.doit) {
          cellPaintEvent.ignoreDrawForeground = (event.detail & SWT.FOREGROUND) == 0;
          cellPaintEvent.ignoreDrawBackground = (event.detail & SWT.BACKGROUND) == 0;
          cellPaintEvent.ignoreDrawSelection = (event.detail & SWT.SELECTED) == 0;
          cellPaintEvent.ignoreDrawFocused = (event.detail & SWT.FOCUSED) == 0;
        } else {
          cellPaintEvent.ignoreDrawForeground = true;
          cellPaintEvent.ignoreDrawBackground = true;
          cellPaintEvent.ignoreDrawSelection = true;
          cellPaintEvent.ignoreDrawFocused = true;
        }
//        event.gc.isSwingPainting = false; 
        break;
      }
      case CellPaintEvent.PAINT_TYPE: {
        TableItem tableItem = cellPaintEvent.tableItem.getTableItem();
        Rectangle cellBounds = tableItem.getBounds(cellPaintEvent.column);
        Event event = new Event();
        event.x = cellBounds.x;
        event.y = cellBounds.y;
        event.width = cellBounds.width;
        event.height = cellBounds.height;
        event.item = tableItem;
        event.index = cellPaintEvent.column;
        if(!cellPaintEvent.ignoreDrawForeground) event.detail |= SWT.FOREGROUND;
        if(!cellPaintEvent.ignoreDrawBackground) event.detail |= SWT.BACKGROUND;
        if(!cellPaintEvent.ignoreDrawSelection) event.detail |= SWT.SELECTED;
        if(!cellPaintEvent.ignoreDrawFocused) event.detail |= SWT.FOCUSED;
        event.gc = new GC(this);
        event.gc.handle.setUserClip(((CTable)handle).getCellRect(cellPaintEvent.row, cellPaintEvent.column, false));
        sendEvent(SWT.PaintItem, event);
        break;
      }
      case CellPaintEvent.MEASURE_TYPE:
        TableItem tableItem = cellPaintEvent.tableItem.getTableItem();
//        Rectangle cellBounds = tableItem.getBounds(cellPaintEvent.column);
        Event event = new Event();
//        event.x = cellBounds.x;
//        event.y = cellBounds.y;
//        event.width = cellBounds.width;
//        event.height = cellBounds.height;
        event.height = cellPaintEvent.rowHeight;
        event.item = tableItem;
        event.index = cellPaintEvent.column;
        event.gc = new GC(this);
//        event.gc.handle.clip(((CTable)handle).getCellRect(cellPaintEvent.row, cellPaintEvent.column, false));
        sendEvent(SWT.MeasureItem, event);
//        cellPaintEvent.rowHeight -= event.height - cellBounds.height;
        cellPaintEvent.rowHeight = event.height;
        break;
      }
    } else if(e instanceof ListSelectionEvent) {
      if(!((ListSelectionEvent)e).getValueIsAdjusting()) {
        Event event = new Event ();
        int selectionIndex = ((CTable)handle).getSelectionModel().getLeadSelectionIndex();
        if(selectionIndex != -1) {
          // TODO: should we send the previous item?
          event.item = _getItem(selectionIndex);
        }
        sendEvent(SWT.Selection, event);
      }
    }
    super.processEvent(e);
  } catch(Throwable t) {
    UIThreadUtils.storeException(t);
  } finally {
    UIThreadUtils.stopExclusiveSection();
  }
}

public void processEvent(AWTEvent e) {
  int id = e.getID();
  switch(id) {
  case KeyEvent.KEY_PRESSED: {
    KeyEvent ke = (KeyEvent)e;
    if(ke.getKeyCode() != KeyEvent.VK_ENTER || ((CTable)handle).getSelectionModel().getLeadSelectionIndex() == -1 || !hooks(SWT.DefaultSelection)) { super.processEvent(e); return; } break;
  }
  case MouseEvent.MOUSE_PRESSED: {
    MouseEvent me = (MouseEvent)e;
    if(me.getID() != MouseEvent.MOUSE_PRESSED || me.getClickCount() != 2 || ((CTable)handle).getSelectionModel().getLeadSelectionIndex() == -1 || !hooks(SWT.DefaultSelection)) {
      super.processEvent(e);
      return;
    };
    break;
  }
  case ItemEvent.ITEM_STATE_CHANGED: if(!hooks(SWT.Selection) || isAdjustingSelection) { super.processEvent(e); return; } break;
  default: { super.processEvent(e); return; }
  }
  if(isDisposed()) {
    super.processEvent(e);
    return;
  }
  UIThreadUtils.startExclusiveSection(getDisplay());
  if(isDisposed()) {
    UIThreadUtils.stopExclusiveSection();
    super.processEvent(e);
    return;
  }
  try {
    switch(id) {
    case KeyEvent.KEY_PRESSED:
      if(((CTable)handle).getSelectionModel().getLeadSelectionIndex() != -1) {
        Event event = new Event ();
        event.item = _getItem(((CTable)handle).getSelectionModel().getLeadSelectionIndex());
        sendEvent(SWT.DefaultSelection, event);
      }
      break;
    case MouseEvent.MOUSE_PRESSED:
      if(((CTable)handle).getSelectionModel().getLeadSelectionIndex() != -1) {
        Event event = new Event ();
        event.item = _getItem(((CTable)handle).getSelectionModel().getLeadSelectionIndex());
        sendEvent(SWT.DefaultSelection, event);
      }
      break;
    case ItemEvent.ITEM_STATE_CHANGED:
      Event event = new Event();
      event.detail = SWT.CHECK;
      event.item = ((CTableItem)((ItemEvent)e).getItem()).getTableItem();
      sendEvent(SWT.Selection, event);
      break;
    }
    super.processEvent(e);
  } catch(Throwable t) {
    UIThreadUtils.storeException(t);
  } finally {
    UIThreadUtils.stopExclusiveSection();
  }
}

}
