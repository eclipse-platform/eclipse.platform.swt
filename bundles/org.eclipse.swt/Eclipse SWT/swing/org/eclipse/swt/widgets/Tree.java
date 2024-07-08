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
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventObject;

import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.swing.CTree;
import org.eclipse.swt.internal.swing.CTreeItem;
import org.eclipse.swt.internal.swing.DefaultMutableTreeTableNode;
import org.eclipse.swt.internal.swing.UIThreadUtils;
import org.eclipse.swt.internal.swing.Utils;
import org.eclipse.swt.internal.swing.CTree.CellPaintEvent;

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
 * Here is an example of using a <code>Tree</code> with style <code>VIRTUAL</code>:
 * <code><pre>
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
 * </p><p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to add <code>Control</code> children to it,
 * or set a layout on it.
 * </p><p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SINGLE, MULTI, CHECK, FULL_SELECTION, VIRTUAL</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection, DefaultSelection, Collapse, Expand, SetData, MeasureItem, EraseItem, PaintItem</dd>
 * </dl>
 * </p><p>
 * Note: Only one of the styles SINGLE and MULTI may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */
public class Tree extends Composite {
  ArrayList itemList;
  ArrayList columnList;
  TreeItem currentItem;
//	TreeColumn [] columns;
//	int hwndParent, hwndHeader, hAnchor;
//	ImageList imageList;
//	boolean dragStarted, gestureCompleted;
//	boolean ignoreSelect, ignoreExpand, ignoreDeselect, ignoreResize;
//	boolean lockSelection, oldSelected, newSelected;
//	boolean linesVisible, customDraw, printClient;
//	static final int INSET = 3;
	static final int GRID_WIDTH = 1;
//	static final int HEADER_MARGIN = 10;
//	static final char [] BUTTON = new char [] {'B', 'U', 'T', 'T', 'O', 'N', 0};

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
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Tree (Composite parent, int style) {
	super (parent, checkStyle (style));
}

static int checkStyle (int style) {
//	/*
//	* Feature in Windows.  It is not possible to create
//	* a tree that scrolls and does not have scroll bars.
//	* The TVS_NOSCROLL style will remove the scroll bars
//	* but the tree will never scroll.  Therefore, no matter
//	* what style bits are specified, set the H_SCROLL and
//	* V_SCROLL bits so that the SWT style will match the
//	* widget that Windows creates.
//	*/
	style |= SWT.H_SCROLL | SWT.V_SCROLL;
	return checkBits (style, SWT.SINGLE, SWT.MULTI, 0, 0, 0, 0);
}

void adjustColumnWidth() {
  // Could be more efficient. Should post, with coalescing?
  if(getColumnCount() == 0) {
    CTree cTree = (CTree)handle;
    cTree.getColumnModel().getColumn(0).setPreferredWidth(cTree.getPreferredColumnWidth(0));
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
public void addSelectionListener(SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection, typedListener);
	addListener (SWT.DefaultSelection, typedListener);
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
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Expand, typedListener);
	addListener (SWT.Collapse, typedListener);
} 

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

boolean checkData (TreeItem item, boolean redraw) {
  if ((style & SWT.VIRTUAL) == 0) return true;
  TreeItem parentItem = item.getParentItem ();
  return checkData (item, parentItem == null ? indexOf (item) : parentItem.indexOf (item), redraw);
}

boolean checkData (TreeItem item, int index, boolean redraw) {
  if ((style & SWT.VIRTUAL) == 0) return true;
  if (!item.cached) {
    item.cached = true;
    Event event = new Event ();
    event.item = item;
    event.index = index;
    TreeItem oldItem = currentItem;
    currentItem = item;
    sendEvent (SWT.SetData, event);
    //widget could be disposed at this point
    currentItem = oldItem;
    if (isDisposed () || item.isDisposed ()) return false;
//    if (redraw) item.redraw ();
  }
  return true;
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
public void clear (int index, boolean all) {
  checkWidget ();
  getItem(index).clearAll(all);
//  int hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_ROOT, 0);
//  if (hItem == 0) error (SWT.ERROR_INVALID_RANGE);
//  hItem = findItem (hItem, index);
//  if (hItem == 0) error (SWT.ERROR_INVALID_RANGE);
//  TVITEM tvItem = new TVITEM ();
//  tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_PARAM;
//  clear (hItem, tvItem);
//  if (all) {
//    hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CHILD, hItem);
//    clearAll (hItem, tvItem, all);
//  }
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
  for(int i=getItemCount()-1; i>=0; i--) {
    getItem(i).clearAll(all);
  }
//  int hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_ROOT, 0);
//  if (hItem == 0) return;
//  TVITEM tvItem = new TVITEM ();
//  tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_PARAM;
//  clearAll (hItem, tvItem, all);
}

public Rectangle computeTrim(int x, int y, int width, int height) {
  CTree cTree = (CTree)handle;
  width += cTree.getVerticalScrollBar().getPreferredSize().width;
  height += cTree.getHorizontalScrollBar().getPreferredSize().height;
  return super.computeTrim(x, y, width, height);
}

void createHandleInit() {
  super.createHandleInit();
  state &= ~(CANVAS | THEME_BACKGROUND);
}

protected Container createHandle () {
  return (Container)CTree.Factory.newInstance(this, style);
}

//void createHandle () {
//	super.createHandle ();
//	state &= ~CANVAS;
//	
//	/*
//	* Feature in Windows.  In version 5.8 of COMCTL32.DLL,
//	* if the font is changed for an item, the bounds for the
//	* item are not updated, causing the text to be clipped.
//	* The fix is to detect the version of COMCTL32.DLL, and
//	* if it is one of the versions with the problem, then
//	* use version 5.00 of the control (a version that does
//	* not have the problem).  This is the recomended work
//	* around from the MSDN.
//	*/
//	if (!OS.IsWinCE) {
//		if (OS.COMCTL32_MAJOR < 6) {
//			OS.SendMessage (handle, OS.CCM_SETVERSION, 5, 0);
//		}
//	}
//		
//	/* Set the checkbox image list */
//	if ((style & SWT.CHECK) != 0) setCheckboxImageList ();
//	
//	/*
//	* Feature in Windows.  When the control is created,
//	* it does not use the default system font.  A new HFONT
//	* is created and destroyed when the control is destroyed.
//	* This means that a program that queries the font from
//	* this control, uses the font in another control and then
//	* destroys this control will have the font unexpectedly
//	* destroyed in the other control.  The fix is to assign
//	* the font ourselves each time the control is created.
//	* The control will not destroy a font that it did not
//	* create.
//	*/
//	int hFont = OS.GetStockObject (OS.SYSTEM_FONT);
//	OS.SendMessage (handle, OS.WM_SETFONT, hFont, 0);
//}

void createItem (TreeColumn column, int index) {
//	int columnCount = getColumnCount();
	for (int i=0; i<itemList.size(); i++) {
		TreeItem item = (TreeItem)itemList.get(i);
    item.handle.insertColumn(index);
		if (index == 0) {
      item.text = "";
      item.image = null;
		}
	}
  TableColumnModel columnModel = ((CTree)handle).getColumnModel();
  TableColumn tableColumn = (TableColumn)column.handle;
  if(columnList.isEmpty()) {
    // TODO: remove a first bogus column? (3)
    columnModel.removeColumn(columnModel.getColumn(0));
  }
  columnModel.addColumn(tableColumn);
  tableColumn.setModelIndex(index);
  columnList.add(index, column);
  // TODO: check it is enough
  handle.repaint();
}

void createItem (TreeItem item, int index) {
  itemList.add(index, item);
  ((CTree)handle).getRoot().insert((MutableTreeNode)item.handle, index);
  // TODO: check how to notify addition and if it is needed, because this line causes snippet8 not to work
//  ((CTree)handle).getModel().nodesWereInserted(((CTree)handle).getRoot(), new int[] {index});
}

void createItem (TreeItem item, TreeItem parentItem, int index) {
  if(parentItem.itemList == null) {
    parentItem.itemList = new ArrayList();
  }
  parentItem.itemList.add(index, item);
  ((MutableTreeNode)parentItem.handle).insert((MutableTreeNode)item.handle, index);
  ((CTree)handle).getModel().nodesWereInserted((MutableTreeNode)parentItem.handle, new int[] {index});
}

//void createParent () {
//	forceResize ();
//	RECT rect = new RECT ();
//	OS.GetWindowRect (handle, rect);
//	OS.MapWindowPoints (0, parent.handle, rect, 2);
//	int oldStyle = OS.GetWindowLong (handle, OS.GWL_STYLE);
//	int newStyle = super.widgetStyle () & ~OS.WS_VISIBLE;
//	if ((oldStyle & OS.WS_DISABLED) != 0) newStyle |= OS.WS_DISABLED;
////	if ((oldStyle & OS.WS_VISIBLE) != 0) newStyle |= OS.WS_VISIBLE;
//	hwndParent = OS.CreateWindowEx (
//		super.widgetExtStyle (),
//		super.windowClass (),
//		null,
//		newStyle,
//		rect.left, 
//		rect.top, 
//		rect.right - rect.left, 
//		rect.bottom - rect.top,
//		parent.handle,
//		0,
//		OS.GetModuleHandle (null),
//		null);
//	if (hwndParent == 0) error (SWT.ERROR_NO_HANDLES);
//	OS.SetWindowLong (hwndParent, OS.GWL_ID, hwndParent);
//	hwndHeader = OS.CreateWindowEx (
//		0,
//		new TCHAR (0, OS.WC_HEADER, true),
//		null,
//		OS.HDS_BUTTONS | OS.HDS_FULLDRAG | OS.HDS_HIDDEN | OS.WS_CHILD | OS.WS_CLIPSIBLINGS,
//		0, 0, 0, 0,
//		hwndParent,
//		0,
//		OS.GetModuleHandle (null),
//		null);
//	if (hwndHeader == 0) error (SWT.ERROR_NO_HANDLES);
//	OS.SetWindowLong (hwndHeader, OS.GWL_ID, hwndHeader);
//	if (OS.IsDBLocale) {
//		int hIMC = OS.ImmGetContext (handle);
//		OS.ImmAssociateContext (hwndParent, hIMC);
//		OS.ImmAssociateContext (hwndHeader, hIMC);		
//		OS.ImmReleaseContext (handle, hIMC);
//	}
//	if (!OS.IsPPC) {
//		if ((style & SWT.BORDER) != 0) {
//			int oldExStyle = OS.GetWindowLong (handle, OS.GWL_EXSTYLE);
//			oldExStyle &= ~OS.WS_EX_CLIENTEDGE;
//			OS.SetWindowLong (handle, OS.GWL_EXSTYLE, oldExStyle);
//		}
//	}
//	int hFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
//	if (hFont != 0) OS.SendMessage (hwndHeader, OS.WM_SETFONT, hFont, 0);
//	int hImageList = OS.SendMessage (handle, OS.TVM_GETIMAGELIST, OS.TVSIL_NORMAL, 0);
//	if (hImageList != 0) {
//		OS.SendMessage (hwndHeader, OS.HDM_SETIMAGELIST, 0, hImageList);
//	}
//	int hwndInsertAfter = OS.GetWindow (handle, OS.GW_HWNDPREV);
//	int flags = OS.SWP_NOSIZE | OS.SWP_NOMOVE | OS.SWP_NOACTIVATE;
//	SetWindowPos (hwndParent, hwndInsertAfter, 0, 0, 0, 0, flags);
//	SCROLLINFO info = new SCROLLINFO ();
//	info.cbSize = SCROLLINFO.sizeof;
//	info.fMask = OS.SIF_RANGE | OS.SIF_PAGE;
//	OS.GetScrollInfo (hwndParent, OS.SB_HORZ, info);
//	info.nPage = info.nMax + 1;
//	OS.SetScrollInfo (hwndParent, OS.SB_HORZ, info, true);
//	OS.GetScrollInfo (hwndParent, OS.SB_VERT, info);
//	info.nPage = info.nMax + 1;
//	OS.SetScrollInfo (hwndParent, OS.SB_VERT, info, true);
//	customDraw = true;
//	deregister ();
//	if ((oldStyle & OS.WS_VISIBLE) != 0) {
//		OS.ShowWindow (hwndParent, OS.SW_SHOW);
//	}
//	int hwndFocus = OS.GetFocus ();
//	if (hwndFocus == handle) OS.SetFocus (hwndParent);
//	OS.SetParent (handle, hwndParent);
//	if (hwndFocus == handle) OS.SetFocus (handle);
//	register ();
//}

void createWidget () {
	super.createWidget ();
  itemList = new ArrayList();
  columnList = new ArrayList();
}

//void deregister () {
//	super.deregister ();
//	if (hwndParent != 0) display.removeControl (hwndParent);
//}

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
  ((CTree)handle).clearSelection();
}

void destroyItem (TreeColumn column) {
  int index = columnList.indexOf(column);
  for (int i=0; i<itemList.size(); i++) {
    TreeItem item = (TreeItem)itemList.get(i);
    item.handle.removeColumn(index);
  }
  columnList.remove(index);
  if(columnList.isEmpty()) {
    // TODO: add a first bogus column? (2)
    TableColumnModel columnModel = ((CTree)handle).getColumnModel();
    javax.swing.table.TableColumn tableColumn = new javax.swing.table.TableColumn(0);
    columnModel.addColumn(tableColumn);
  }
  if(sortColumn == column) {
    sortColumn = null;
    sortDirection = SWT.NONE;
  }
  handle.repaint();
}

void destroyItem (TreeItem item) {
//  TreeItem parentItem = item.getParentItem();
//  if(parentItem == null) {
//    ((CTree)handle).getRoot().remove((MutableTreeNode)item.handle);
//    itemList.remove(item);
//  } else {
//    ((DefaultMutableTreeTableNode)parentItem.handle).remove((MutableTreeNode)item.handle);
//    parentItem.itemList.remove(item);
//  }
}

//void enableWidget (boolean enabled) {
//	super.enableWidget (enabled);
//	/*
//	* Feature in Windows.  When a tree is given a background color
//	* using TVM_SETBKCOLOR and the tree is disabled, Windows draws
//	* the tree using the background color rather than the disabled
//	* colors.  This is different from the table which draws grayed.
//	* The fix is to set the default background color while the tree
//	* is disabled and restore it when enabled.
//	*/
//	if (background != -1) {
//		_setBackgroundPixel (enabled ? background : -1);
//	}
//	if (hwndParent != 0) OS.EnableWindow (hwndParent, enabled);
//}

//Widget findItem (int id) {
//	TVITEM tvItem = new TVITEM ();
//	tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_PARAM;
//	tvItem.hItem = id;
//	if (OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem) != 0) {
//		int lParam = tvItem.lParam;
//		if (0 <= lParam && lParam < items.length) return items [lParam];
//	}
//	return null;
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
 * 
 * @since 3.1
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
 * @since 3.1 
 */
public int getHeaderHeight () {
	checkWidget ();
  JTableHeader tableHeader = ((CTree)handle).getTableHeader();
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
 * 
 * @since 3.1
 */
public boolean getHeaderVisible () {
	checkWidget ();
  return ((CTree)handle).getTableHeader().isVisible();
}

//Point getImageSize () {
//	if (imageList != null) return imageList.getImageSize ();
//	return new Point (0, getItemHeight ());
//}

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
	checkWidget ();
	if (columnList == null) error (SWT.ERROR_INVALID_RANGE);
	int count = getColumnCount();
	if (!(0 <= index && index < count)) error (SWT.ERROR_INVALID_RANGE);
	return (TreeColumn)columnList.get(index);
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
	checkWidget ();
	if (columnList == null) return 0;
	return columnList.size();
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
public int[] getColumnOrder () {
  checkWidget ();
  return ((CTree)handle).getColumnOrder();
//  if (hwndHeader == 0) return new int [0];
//  int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
//  int [] order = new int [count];
//  OS.SendMessage (hwndHeader, OS.HDM_GETORDERARRAY, count, order);
//  return order;
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
	checkWidget ();
	if (columnList == null) return new TreeColumn [0];
  return (TreeColumn[])columnList.toArray(new TreeColumn[0]);
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
  checkWidget ();
  if (index < 0) error (SWT.ERROR_INVALID_RANGE);
  if (index >= itemList.size()) error (SWT.ERROR_INVALID_RANGE);
  return (TreeItem)itemList.get(index);
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
	checkWidget ();
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
  TreePath treePath = ((CTree)handle).getPathForLocation(point.x, point.y);
  if(treePath == null) {
    return null;
  }
  CTreeItem.TreeItemObject treeItemObject = ((CTreeItem.TreeItemObject)((DefaultMutableTreeNode)treePath.getLastPathComponent()).getUserObject());
  return treeItemObject.getTreeItem().getTreeItem();
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
  return itemList == null? 0: itemList.size();
}

//int getItemCount (int hItem) {
//	int count = 0;
//	while (hItem != 0) {
//		hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXT, hItem);
//		count++;
//	}
//	return count;
//}

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
  return ((CTree)handle).getRowHeight();
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
	checkWidget ();
  return (TreeItem[])itemList.toArray(new TreeItem[0]);
}

//TreeItem [] getItems (int hTreeItem) {
//	int count = 0, hItem = hTreeItem;
//	while (hItem != 0) {
//		hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXT, hItem);
//		count++;
//	}
//	int index = 0;
//	TreeItem [] result = new TreeItem [count];
//	TVITEM tvItem = new TVITEM ();
//	tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_PARAM;
//	tvItem.hItem = hTreeItem;
//	/*
//	* Feature in Windows.  In some cases an expand or collapse message
//	* can occurs from within TVM_DELETEITEM.  When this happens, the item
//	* being destroyed has been removed from the list of items but has not
//	* been deleted from the tree.  The fix is to check for null items and
//	* remove them from the list.
//	*/
//	while (tvItem.hItem != 0) {
//		OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
//		TreeItem item = items [tvItem.lParam];
//		if (item != null) result [index++] = item;
//		tvItem.hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXT, tvItem.hItem);
//	}
//	if (index != count) {
//		TreeItem [] newResult = new TreeItem [index];
//		System.arraycopy (result, 0, newResult, 0, index);
//		result = newResult;
//	}
//	return result;
//}

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
 * 
 * @since 3.1
 */
public boolean getLinesVisible () {
	checkWidget ();
  return ((CTree)handle).isGridVisible();
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
public TreeItem [] getSelection () {
	checkWidget ();
  TreePath[] paths = ((CTree)handle).getSelectionModel().getSelectionPaths();
  if(paths == null || paths.length == 0) return new TreeItem[0];
  TreeItem[] items = new TreeItem[paths.length];
  for(int i=0; i<items.length; i++) {
    items[i] = ((CTreeItem.TreeItemObject)((DefaultMutableTreeNode)paths[i].getLastPathComponent()).getUserObject()).getTreeItem().getTreeItem();
  }
  return items;
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
  return ((CTree)handle).getSelectionModel().getSelectionCount();
}

TreeColumn sortColumn;
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
  int index = ((CTree)handle).getTopIndex();
  if(index < 0) return null;
  return ((CTreeItem)((CTree)handle).getPathForRow(index).getLastPathComponent()).getTreeItem();
}

//int imageIndex (Image image) {
//	if (image == null) return OS.I_IMAGENONE;
//	if (imageList == null) {
//		int hOldList = OS.SendMessage (handle, OS.TVM_GETIMAGELIST, OS.TVSIL_NORMAL, 0);
//		if (hOldList != 0) OS.ImageList_Destroy (hOldList);
//		Rectangle bounds = image.getBounds ();
//		imageList = display.getImageList (new Point (bounds.width, bounds.height));
//		int index = imageList.indexOf (image);
//		if (index == -1) index = imageList.add (image);
//		int hImageList = imageList.getHandle ();
//		OS.SendMessage (handle, OS.TVM_SETIMAGELIST, OS.TVSIL_NORMAL, hImageList);
//		if (hwndHeader != 0) {
//			OS.SendMessage (hwndHeader, OS.HDM_SETIMAGELIST, 0, hImageList);
//		}
//		updateScrollBar ();
//		return index;
//	}
//	int index = imageList.indexOf (image);
//	if (index != -1) return index;
//	return imageList.add (image);
//}

//int indexOf (int hItem, int hChild) {
//	int index = 0;
//	while (hItem != 0 && hItem != hChild) {
//		hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXT, hItem);
//		index++;
//	}
//	return hItem == hChild ? index : -1;
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
 * 
 * @since 3.1
 */
public int indexOf (TreeColumn column) {
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
 *    <li>ERROR_NULL_ARGUMENT - if the tool item is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the tool item has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.1
 */
public int indexOf (TreeItem item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
  return itemList.indexOf(item);
}

//void register () {
//	super.register ();
//	if (hwndParent != 0) display.addControl (hwndParent, this);
//}
//
//boolean releaseItem (TreeItem item, TVITEM tvItem) {
//	int hItem = item.handle;
//	if (hItem == hAnchor) hAnchor = 0;
//	if (item.isDisposed ()) return false;
//	tvItem.hItem = hItem;
//	OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
//	items [tvItem.lParam] = null;
//	return true;
//}
//
//void releaseItems (TreeItem [] nodes, TVITEM tvItem) {
//	for (int i=0; i<nodes.length; i++) {
//		TreeItem item = nodes [i];
//		TreeItem [] sons = item.getItems ();
//		if (sons.length != 0) {
//			releaseItems (sons, tvItem);
//		}
//		if (releaseItem (item, tvItem)) {
//			item.releaseResources ();
//		}
//	}
//}
//
//void releaseHandle () {
//	super.releaseHandle ();
//	hwndParent = hwndHeader = 0;
//}

void releaseChildren (boolean destroy) {
  if(itemList != null) {
    for (int i=0; i<itemList.size(); i++) {
      TreeItem item = (TreeItem)itemList.get(i);
      if (item != null && !item.isDisposed ()) item.release(false);
    }
    itemList = null;
  }
  if(columnList != null) {
    for(int i=0; i<columnList.size(); i++) {
      TreeColumn column = (TreeColumn)columnList.get(i);
      if (column != null && !column.isDisposed ()) column.release(false);
    }
    columnList = null;
  }
//	/*
//	* Feature in Windows.  For some reason, when TVM_GETIMAGELIST
//	* or TVM_SETIMAGELIST is sent, the tree issues NM_CUSTOMDRAW
//	* messages.  This behavior is unwanted when the tree is being
//	* disposed.  The fix is to ingore NM_CUSTOMDRAW messages by
//	* clearing the custom draw flag.
//	* 
//	* NOTE: This only happens on Windows XP.
//	*/
//	customDraw = false;
//	if (imageList != null) {
//		OS.SendMessage (handle, OS.TVM_SETIMAGELIST, OS.TVSIL_NORMAL, 0);
//		OS.SendMessage (hwndHeader, OS.HDM_SETIMAGELIST, 0, 0);
//		display.releaseImageList (imageList);
//	} else {
//		int hOldList = OS.SendMessage (handle, OS.TVM_GETIMAGELIST, OS.TVSIL_NORMAL, 0);
//		OS.SendMessage (handle, OS.TVM_SETIMAGELIST, OS.TVSIL_NORMAL, 0);
//		OS.SendMessage (hwndHeader, OS.HDM_SETIMAGELIST, 0, 0);
//		if (hOldList != 0) OS.ImageList_Destroy (hOldList);
//	}
//	imageList = null;
//	int hOldList = OS.SendMessage (handle, OS.TVM_GETIMAGELIST, OS.TVSIL_STATE, 0);
//	OS.SendMessage (handle, OS.TVM_SETIMAGELIST, OS.TVSIL_STATE, 0);
//	if (hOldList != 0) OS.ImageList_Destroy (hOldList);
  super.releaseChildren (destroy);
}

void releaseItem (TreeItem treeItem, boolean release) {
  TreeItem parentItem = treeItem.getParentItem();
  if(parentItem == null) {
    ((CTree)handle).getRoot().remove((MutableTreeNode)treeItem.handle);
    int index = itemList.indexOf(treeItem);
    itemList.remove(index);
    ((CTree)handle).getModel().nodesWereRemoved(((CTree)handle).getRoot(), new int[] {index}, new Object[] {treeItem.handle});
  } else {
    ((DefaultMutableTreeTableNode)parentItem.handle).remove((MutableTreeNode)treeItem.handle);
    int index = parentItem.itemList.indexOf(treeItem);
    parentItem.itemList.remove(index);
    ((CTree)handle).getModel().nodesWereRemoved((DefaultMutableTreeTableNode)parentItem.handle, new int[] {index}, new Object[] {treeItem.handle});
  }
  handle.repaint();
  if(release) {
    treeItem.release (false);
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
  for (int i=itemList.size()-1; i>=0; i--) {
    TreeItem item = (TreeItem)itemList.get(i);
    if (item != null && !item.isDisposed ()) {
      item.dispose();
//      item.release (false);
    } else {
      itemList.remove(i);
    }
  }
//  ((CTree)handle).getRoot().removeAllChildren();
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
 * @see #addSelectionListener
 */
public void removeSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
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
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Expand, listener);
	eventTable.unhook (SWT.Collapse, listener);
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
  Utils.notImplemented();
//	int hItem = 0;
//	if (item != null) {
//		if (item.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
//		hItem = item.handle;
//	}
//	OS.SendMessage (handle, OS.TVM_SETINSERTMARK, (before) ? 0 : 1, hItem);
}

/**
 * Sets the number of root-level items contained in the receiver.
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
  setItemCount (null, itemList, count);

//  itemList.add(index, item);
//  ((CTree)handle).getRoot().insert((MutableTreeNode)item.handle, index);
  
//  Utils.notImplemented();
//  int hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_ROOT, 0);
//  setItemCount (count, OS.TVGN_ROOT, hItem);
}

void setItemCount (TreeItem treeItem, int count) {
  if(treeItem.itemList == null) {
    treeItem.itemList = new ArrayList();
  }
  setItemCount (treeItem, treeItem.itemList, count);
}

void setItemCount (TreeItem parentItem, ArrayList itemList, int count) {
  count = Math.max (0, count);
  for(int i=itemList.size()-1; i>= count; i--) {
    TreeItem item = (TreeItem)itemList.get(i);
    if (item != null && !item.isDisposed ()) {
      item.release (false);
    }
    if(parentItem == null) {
      ((CTree)handle).getRoot().remove(i);
    } else {
      ((DefaultMutableTreeTableNode)parentItem.handle).remove(i);
    }
  }
  int itemCount = count - itemList.size();
  if(itemCount <= 0) {
    return;
  }
  itemList.ensureCapacity(count);
  for(int i=0; i<itemCount; i++) {
//    childIndices[i] = itemCount;
    if(parentItem != null) {
      new TreeItem(parentItem, SWT.NONE);
    } else {
      new TreeItem(this, SWT.NONE);
    }
  }
//  if(isVirtual) {
//    if(parentItem != null) {
//      ((CTree)handle).getModel().nodeStructureChanged(((DefaultMutableTreeTableNode)parentItem.handle));
////      ((CTree)handle).getModel().nodesWereInserted(((DefaultMutableTreeTableNode)parentItem.handle), childIndices);
////    } else {
////      ((CTree)handle).getModel().nodeStructureChanged(((CTree)handle).getRoot());
//////      ((CTree)handle).getModel().nodesWereInserted(((CTree)handle).getRoot(), childIndices);
//    }
//  }
  
//  if(parentItem.itemList == null) {
//    parentItem.itemList = new ArrayList();
//  }
//  parentItem.itemList.add(index, item);
//  ((MutableTreeNode)parentItem.handle).insert((MutableTreeNode)item.handle, index);
//  ((CTree)handle).getModel().nodesWereInserted((MutableTreeNode)parentItem.handle, new int[] {index});

//  boolean redraw = false;
//  if (OS.SendMessage (handle, OS.TVM_GETCOUNT, 0, 0) == 0) {
//    redraw = drawCount == 0 && OS.IsWindowVisible (handle);
//    if (redraw) OS.SendMessage (handle, OS.WM_SETREDRAW, 0, 0);
//  }
//  int itemCount = 0;
//  while (hItem != 0 && itemCount < count) {
//    hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXT, hItem);
//    itemCount++;
//  }
//  TVITEM tvItem = new TVITEM ();
//  tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_PARAM;
//  while (hItem != 0) {
//    tvItem.hItem = hItem;
//    OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
//    hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXT, hItem);
//    TreeItem item = tvItem.lParam != -1 ? items [tvItem.lParam] : null;
//    if (item != null && !item.isDisposed ()) {
//      item.dispose ();
//    } else {
//      releaseItem (tvItem.hItem, tvItem, false);
//      destroyItem (null, tvItem.hItem);
//    }
//  }
//  if ((style & SWT.VIRTUAL) != 0) {
//    for (int i=itemCount; i<count; i++) {
//      createItem (null, hParent, OS.TVI_LAST, 0);
//    }
//  } else {
//    shrink = true;
//    int extra = Math.max (4, (count + 3) / 4 * 4);
//    TreeItem [] newItems = new TreeItem [items.length + extra];
//    System.arraycopy (items, 0, newItems, 0, items.length);
//    items = newItems;
//    for (int i=itemCount; i<count; i++) {
//      new TreeItem (this, SWT.NONE, hParent, OS.TVI_LAST, 0);
//    }
//  }
//  if (redraw) {
//    OS.SendMessage (handle, OS.WM_SETREDRAW, 1, 0);
//    OS.InvalidateRect (handle, null, true);
//  }
}

/**
 * Sets the height of the area which would be used to
 * display <em>one</em> of the items in the tree.
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
  if (itemHeight < -1) error (SWT.ERROR_INVALID_ARGUMENT);
  ((CTree)handle).setRowHeight(itemHeight);
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
 * 
 * @since 3.1
 */
public void setLinesVisible (boolean show) {
	checkWidget ();
  ((CTree)handle).setGridVisible(show);
}

//int scrolledHandle () {
//	if (hwndHeader == 0) return handle;
//	int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
//	return count == 0 ? handle : hwndParent;
//}

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
  ((CTree)handle).selectAll();
}

//void setBounds (int x, int y, int width, int height, int flags) {
//	/*
//	* Ensure that the selection is visible when the tree is resized
//	* from a zero size to a size that can show the selection.
//	*/
//	boolean fixSelection = false;
//	if ((flags & OS.SWP_NOSIZE) == 0 && (width != 0 || height != 0)) {
//		if (OS.SendMessage (handle, OS.TVM_GETVISIBLECOUNT, 0, 0) == 0) {
//			fixSelection = true;
//		}
//	}
//	super.setBounds (x, y, width, height, flags);
//	if (fixSelection) {
//		int hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
//		if (hItem != 0) showItem (hItem);
//	}
//}

//void setCursor () {
//	/*
//	* Bug in Windows.  Under certain circumstances, when WM_SETCURSOR
//	* is sent from SendMessage(), Windows GP's in the window proc for
//	* the tree.  The fix is to avoid calling the tree window proc and
//	* set the cursor for the tree outside of WM_SETCURSOR.
//	* 
//	* NOTE:  This code assumes that the default cursor for the tree
//	* is IDC_ARROW.
//	*/
//	Cursor cursor = findCursor ();
//	int hCursor = cursor == null ? OS.LoadCursor (0, OS.IDC_ARROW) : cursor.handle;
//	OS.SetCursor (hCursor);
//}

//void setCheckboxImageList () {
//	if ((style & SWT.CHECK) == 0) return;
//	int count = 5;
//	int height = OS.SendMessage (handle, OS.TVM_GETITEMHEIGHT, 0, 0), width = height;
//	int flags = ImageList.COLOR_FLAGS;
//	if ((style & SWT.RIGHT_TO_LEFT) != 0) flags |= OS.ILC_MIRROR;
//	int hImageList = OS.ImageList_Create (width, height, flags, count, count);
//	int hDC = OS.GetDC (handle);
//	int memDC = OS.CreateCompatibleDC (hDC);
//	int hBitmap = OS.CreateCompatibleBitmap (hDC, width * count, height);
//	int hOldBitmap = OS.SelectObject (memDC, hBitmap);
//	RECT rect = new RECT ();
//	OS.SetRect (rect, 0, 0, width * count, height);
//	int hBrush = OS.CreateSolidBrush (_getBackgroundPixel ());
//	OS.FillRect (memDC, rect, hBrush);
//	OS.DeleteObject (hBrush);
//	int oldFont = OS.SelectObject (hDC, defaultFont ());
//	TEXTMETRIC tm = OS.IsUnicode ? (TEXTMETRIC) new TEXTMETRICW () : new TEXTMETRICA ();
//	OS.GetTextMetrics (hDC, tm);
//	OS.SelectObject (hDC, oldFont);
//	int itemWidth = Math.min (tm.tmHeight, width);
//	int itemHeight = Math.min (tm.tmHeight, height);
//	int left = (width - itemWidth) / 2, top = (height - itemHeight) / 2 + 1;
//	OS.SetRect (rect, left + width, top, left + width + itemWidth, top + itemHeight);
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
//	int hOldList = OS.SendMessage (handle, OS.TVM_GETIMAGELIST, OS.TVSIL_STATE, 0);
//	OS.SendMessage (handle, OS.TVM_SETIMAGELIST, OS.TVSIL_STATE, hImageList);
//	if (hOldList != 0) OS.ImageList_Destroy (hOldList);
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
 * @see Tree#getColumnOrder()
 * @see TreeColumn#getMoveable()
 * @see TreeColumn#setMoveable(boolean)
 * @see SWT#Move
 * 
 * @since 3.2
 */
public void setColumnOrder (int [] order) {
  checkWidget ();
  if (order == null) error (SWT.ERROR_NULL_ARGUMENT);
  int columnCount = getColumnCount();
  if (order.length != columnCount) error (SWT.ERROR_INVALID_ARGUMENT);
  ((CTree)handle).setColumnOrder(order);
//  int count = 0;
//  if (hwndHeader != 0) {
//    count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
//  }
//  if (count == 0) {
//    if (order.length != 0) error (SWT.ERROR_INVALID_ARGUMENT);
//    return;
//  }
//  if (order.length != count) error (SWT.ERROR_INVALID_ARGUMENT);
//  int [] oldOrder = new int [count];
//  OS.SendMessage (hwndHeader, OS.HDM_GETORDERARRAY, count, oldOrder);
//  boolean reorder = false;
//  boolean [] seen = new boolean [count];
//  for (int i=0; i<order.length; i++) {
//    int index = order [i];
//    if (index < 0 || index >= count) error (SWT.ERROR_INVALID_RANGE);
//    if (seen [index]) error (SWT.ERROR_INVALID_ARGUMENT);
//    seen [index] = true;
//    if (index != oldOrder [i]) reorder = true;
//  }
//  if (reorder) {
//    RECT [] oldRects = new RECT [count];
//    for (int i=0; i<count; i++) {
//      oldRects [i] = new RECT ();
//      OS.SendMessage (hwndHeader, OS.HDM_GETITEMRECT, i, oldRects [i]);
//    }
//    OS.SendMessage (hwndHeader, OS.HDM_SETORDERARRAY, order.length, order);
//    OS.InvalidateRect (handle, null, true);
//    updateImageList ();
//    TreeColumn [] newColumns = new TreeColumn [count];
//    System.arraycopy (columns, 0, newColumns, 0, count);
//    RECT newRect = new RECT ();
//    for (int i=0; i<count; i++) {
//      TreeColumn column = newColumns [i];
//      if (!column.isDisposed ()) {
//        OS.SendMessage (hwndHeader, OS.HDM_GETITEMRECT, i, newRect);
//        if (newRect.left != oldRects [i].left) {
//          column.updateToolTip (i);
//          column.sendEvent (SWT.Move);
//        }
//      }
//    }
//  }
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
  ((CTree)handle).setHeaderVisible(show);
}

//void setScrollWidth () {
//	if (hwndHeader == 0 || hwndParent == 0) return;
//	int width = 0;
//	HDITEM hdItem = new HDITEM ();
//	int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
//	for (int i=0; i<count; i++) {
//		hdItem.mask = OS.HDI_WIDTH;
//		OS.SendMessage (hwndHeader, OS.HDM_GETITEM, i, hdItem);
//		width += hdItem.cxy;
//	}
//	int left = 0;
//	RECT rect = new RECT ();
//	SCROLLINFO info = new SCROLLINFO ();
//	info.cbSize = SCROLLINFO.sizeof;
//	info.fMask = OS.SIF_RANGE | OS.SIF_PAGE;
//	if (count == 0) {
//		OS.GetScrollInfo (hwndParent, OS.SB_HORZ, info);
//		info.nPage = info.nMax + 1;
//		OS.SetScrollInfo (hwndParent, OS.SB_HORZ, info, true);
//		OS.GetScrollInfo (hwndParent, OS.SB_VERT, info);
//		info.nPage = info.nMax + 1;
//		OS.SetScrollInfo (hwndParent, OS.SB_VERT, info, true);
//	} else {
//		OS.GetClientRect (hwndParent, rect);
//		OS.GetScrollInfo (hwndParent, OS.SB_HORZ, info);
//		info.nMax = width;
//		info.nPage = rect.right - rect.left;
//		OS.SetScrollInfo (hwndParent, OS.SB_HORZ, info, true);
//		info.fMask = OS.SIF_POS;
//		OS.GetScrollInfo (hwndParent, OS.SB_HORZ, info);
//		left = info.nPos;
//	}
//	OS.GetClientRect (hwndParent, rect);
//	int hHeap = OS.GetProcessHeap ();
//	HDLAYOUT playout = new HDLAYOUT ();
//	playout.prc = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, RECT.sizeof);
//	playout.pwpos = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, WINDOWPOS.sizeof);
//	OS.MoveMemory (playout.prc, rect, RECT.sizeof);
//	OS.SendMessage (hwndHeader, OS.HDM_LAYOUT, 0, playout);
//	WINDOWPOS pos = new WINDOWPOS ();
//	OS.MoveMemory (pos, playout.pwpos, WINDOWPOS.sizeof);
//	if (playout.prc != 0) OS.HeapFree (hHeap, 0, playout.prc);
//	if (playout.pwpos != 0) OS.HeapFree (hHeap, 0, playout.pwpos);
//	SetWindowPos (hwndHeader, OS.HWND_TOP, pos.x - left, pos.y, pos.cx + left, pos.cy, OS.SWP_NOACTIVATE);
//	int w = pos.cx + (count == 0 ? 0 : OS.GetSystemMetrics (OS.SM_CXVSCROLL));
//	int h = rect.bottom - rect.top - pos.cy;
//	boolean oldIgnore = ignoreResize;
//	ignoreResize = true;
//	SetWindowPos (handle, 0, pos.x - left, pos.y + pos.cy, w + left, h, OS.SWP_NOACTIVATE | OS.SWP_NOZORDER);
//	ignoreResize = oldIgnore;
//}

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
public void setSelection (TreeItem item) {
  checkWidget ();
  if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
  setSelection (new TreeItem [] {item});
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
 * @see Tree#deselectAll()
 */
public void setSelection (TreeItem [] items) {
	checkWidget ();
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	int length = items.length;
	if (length == 0 || ((style & SWT.SINGLE) != 0 && length > 1)) {
		deselectAll();
		return;
	}
  TreePath[] paths = new TreePath[items.length];
  for (int i = 0; i < items.length; i++) {
    paths[i] = new TreePath(items[i].handle.getPath());
  }
  ((CTree)handle).getSelectionModel().setSelectionPaths(paths);
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
	if (item == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed ()) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
  int row = ((CTree)handle).getRowForPath(new TreePath(item.handle.getPath()));
  if(row < 0) {
    return;
  }
  ((CTree)handle).setTopIndex(row);
}

//void showItem (int hItem) {
//	/*
//	* Bug in Windows.  When TVM_ENSUREVISIBLE is used to ensure
//	* that an item is visible and the client area of the tree is
//	* smaller that the size of one item, TVM_ENSUREVISIBLE makes
//	* the next item in the tree visible by making it the top item
//	* instead of making the desired item visible.  The fix is to
//	* detect the case when the client area is too small and make
//	* the desired visible item be the top item in the tree.
//	*/
//	if (OS.SendMessage (handle, OS.TVM_GETVISIBLECOUNT, 0, 0) == 0) {
//		boolean fixScroll = checkScroll (hItem);
//		if (fixScroll) {
//			OS.SendMessage (handle, OS.WM_SETREDRAW, 1, 0);
//			OS.DefWindowProc (handle, OS.WM_SETREDRAW, 0, 0);
//		}
//		OS.SendMessage (handle, OS.TVM_SELECTITEM, OS.TVGN_FIRSTVISIBLE, hItem);
//		OS.SendMessage (handle, OS.WM_HSCROLL, OS.SB_TOP, 0);
//		if (fixScroll) {
//			OS.DefWindowProc (handle, OS.WM_SETREDRAW, 1, 0);
//			OS.SendMessage (handle, OS.WM_SETREDRAW, 0, 0);
//		}
//	} else {
//		boolean scroll = true;
//		RECT itemRect = new RECT ();
//		itemRect.left = hItem;
//		if (OS.SendMessage (handle, OS.TVM_GETITEMRECT, 1, itemRect) != 0) {
//			forceResize ();
//			RECT rect = new RECT ();
//			OS.GetClientRect (handle, rect);
//			POINT pt = new POINT ();
//			pt.x = itemRect.left;
//			pt.y = itemRect.top;
//			if (OS.PtInRect (rect, pt)) {
//				pt.y = itemRect.bottom;
//				if (OS.PtInRect (rect, pt)) scroll = false;
//			}
//		}
//		if (scroll) {
//			boolean fixScroll = checkScroll (hItem);
//			if (fixScroll) {
//				OS.SendMessage (handle, OS.WM_SETREDRAW, 1, 0);
//				OS.DefWindowProc (handle, OS.WM_SETREDRAW, 0, 0);
//			}
//			OS.SendMessage (handle, OS.TVM_ENSUREVISIBLE, 0, hItem);
//			if (fixScroll) {
//				OS.DefWindowProc (handle, OS.WM_SETREDRAW, 1, 0);
//				OS.SendMessage (handle, OS.WM_SETREDRAW, 0, 0);
//			}
//		}
//	}
//	if (hwndParent != 0) {
//		RECT itemRect = new RECT ();
//		itemRect.left = hItem;
//		if (OS.SendMessage (handle, OS.TVM_GETITEMRECT, 1, itemRect) != 0) {
//			forceResize ();
//			RECT rect = new RECT ();
//			OS.GetClientRect (hwndParent, rect);
//			OS.MapWindowPoints (hwndParent, handle, rect, 2);
//			POINT pt = new POINT ();
//			pt.x = itemRect.left;
//			pt.y = itemRect.top;
//			if (!OS.PtInRect (rect, pt)) {
//				pt.y = itemRect.bottom;
//				if (!OS.PtInRect (rect, pt)) {
//					SCROLLINFO info = new SCROLLINFO ();
//					info.cbSize = SCROLLINFO.sizeof;
//					info.fMask = OS.SIF_POS;
//					info.nPos = Math.max (0, pt.x - Tree.INSET / 2);
//					OS.SetScrollInfo (hwndParent, OS.SB_HORZ, info, true);
//					setScrollWidth ();
//				}
//			}
//		}
//	}
//	updateScrollBar ();
//}

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
	if (column == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (column.isDisposed ()) error(SWT.ERROR_INVALID_ARGUMENT);
	if (column.parent != this) return;
	int index = indexOf (column);
	if (index == -1) return;
  ((CTree)handle).ensureColumnVisible(index);
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
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed ()) error(SWT.ERROR_INVALID_ARGUMENT);
  int row = ((CTree)handle).getRowForPath(new TreePath(item.handle.getPath()));
  if(row < 0) {
    return;
  }
  ((CTree)handle).ensureRowVisible(row);
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
	checkWidget ();
  int selectionRow = ((CTree)handle).getSelectionModel().getLeadSelectionRow();
  if(selectionRow == -1) {
    return;
  }
  ((CTree)handle).ensureRowVisible(selectionRow);
}

//void showWidget (boolean visible) {
//	super.showWidget (visible);
//	if (hwndParent != 0) {
//		OS.ShowWindow (hwndParent, visible ? OS.SW_SHOW : OS.SW_HIDE);
//	}
//}
//
//void updateScrollBar () {
//	if (hwndParent != 0) {
//		int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
//		if (count != 0) {
//			SCROLLINFO info = new SCROLLINFO ();
//			info.cbSize = SCROLLINFO.sizeof;
//			info.fMask = OS.SIF_ALL;
//			OS.GetScrollInfo (handle, OS.SB_VERT, info);
//			OS.SetScrollInfo (hwndParent, OS.SB_VERT, info, true);
//		}
//	}
//}
//
//TCHAR windowClass () {
//	return TreeClass;
//}
//
//int windowProc () {
//	return TreeProc;
//}
//
//int windowProc (int hwnd, int msg, int wParam, int lParam) {
//	if (hwndParent != 0 && hwnd == hwndParent) {
//		switch (msg) {
//			case OS.WM_MOVE: {
//				sendEvent (SWT.Move);
//				return 0;
//			}
//			case OS.WM_SIZE: {
//				setScrollWidth ();
//				if (ignoreResize) return 0;
//				setResizeChildren (false);
//				int code = callWindowProc (hwnd, OS.WM_SIZE, wParam, lParam);
//				sendEvent (SWT.Resize);
//				if (isDisposed ()) return 0;
//				if (layout != null) {
//					markLayout (false, false);
//					updateLayout (false, false);
//				}
//				setResizeChildren (true);
//				return code;
//			}
//			case OS.WM_COMMAND:
//			case OS.WM_NOTIFY:
//			case OS.WM_SYSCOLORCHANGE: {
//				return OS.SendMessage (handle, msg, wParam, lParam);
//			}
//			case OS.WM_VSCROLL: {
//				SCROLLINFO info = new SCROLLINFO ();
//				info.cbSize = SCROLLINFO.sizeof;
//				info.fMask = OS.SIF_ALL;
//				OS.GetScrollInfo (hwndParent, OS.SB_VERT, info);
//				OS.SetScrollInfo (handle, OS.SB_VERT, info, true);
//				int code = OS.SendMessage (handle, OS.WM_VSCROLL, wParam, lParam);
//				OS.GetScrollInfo (handle, OS.SB_VERT, info);
//				OS.SetScrollInfo (hwndParent, OS.SB_VERT, info, true);
//				return code;
//			}
//			case OS.WM_HSCROLL: {
//				/*
//				* Bug on WinCE.  lParam should be NULL when the message is not sent
//				* by a scroll bar control, but it contains the handle to the window.
//				* When the message is sent by a scroll bar control, it correctly
//				* contains the handle to the scroll bar.  The fix is to check for
//				* both.
//				*/
//				if (horizontalBar != null && (lParam == 0 || lParam == hwndParent)) {
//					wmScroll (horizontalBar, true, hwndParent, OS.WM_HSCROLL, wParam, lParam);
//				}
//				setScrollWidth ();
//				break;
//			}
//		}
//		return callWindowProc (hwnd, msg, wParam, lParam);
//	}
//	int code = super.windowProc (handle, msg, wParam, lParam);
//	switch (msg) {
//		/* Keyboard messages */
//		case OS.WM_CHAR:
//		case OS.WM_IME_CHAR:
//		case OS.WM_KEYDOWN:
//		case OS.WM_KEYUP:
//		case OS.WM_SYSCHAR:
//		case OS.WM_SYSKEYDOWN:
//		case OS.WM_SYSKEYUP:
//			
//		/* Mouse messages */
//		case OS.WM_LBUTTONDBLCLK:
//		case OS.WM_LBUTTONDOWN:
//		case OS.WM_LBUTTONUP:
//		case OS.WM_MBUTTONDBLCLK:
//		case OS.WM_MBUTTONDOWN:
//		case OS.WM_MBUTTONUP:
//		case OS.WM_MOUSEHOVER:
//		case OS.WM_MOUSELEAVE:
//		case OS.WM_MOUSEMOVE:
//		case OS.WM_MOUSEWHEEL:
//		case OS.WM_RBUTTONDBLCLK:
//		case OS.WM_RBUTTONDOWN:
//		case OS.WM_RBUTTONUP:
//		case OS.WM_XBUTTONDBLCLK:
//		case OS.WM_XBUTTONDOWN:
//		case OS.WM_XBUTTONUP:
//			
//		/* Other messages */
//		case OS.WM_SIZE:
//		case OS.WM_SETFONT:
//		case OS.WM_TIMER: {
//			updateScrollBar ();
//		}
//	}
//	return code;
//}
//
//LRESULT WM_CHAR (int wParam, int lParam) {
//	LRESULT result = super.WM_CHAR (wParam, lParam);
//	if (result != null) return result;
//	/*
//	* Feature in Windows.  The tree control beeps
//	* in WM_CHAR when the search for the item that
//	* matches the key stroke fails.  This is the
//	* standard tree behavior but is unexpected when
//	* the key that was typed was ESC, CR or SPACE.
//	* The fix is to avoid calling the tree window
//	* proc in these cases.
//	*/
//	switch (wParam) {
//		case SWT.CR:
//			/*
//			* Feature in Windows.  Windows sends NM_RETURN from WM_KEYDOWN
//			* instead of using WM_CHAR.  This means that application code
//			* that expects to consume the key press and therefore avoid a
//			* SWT.DefaultSelection event from WM_CHAR will fail.  The fix
//			* is to implement SWT.DefaultSelection in WM_CHAR instead of
//			* using NM_RETURN.
//			*/
//			Event event = new Event ();
//			int hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
//			if (hItem != 0) {
//				TVITEM tvItem = new TVITEM ();
//				tvItem.hItem = hItem;
//				tvItem.mask = OS.TVIF_PARAM;
//				OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
//				event.item = items [tvItem.lParam];
//			}
//			postEvent (SWT.DefaultSelection, event);
//			//FALL THROUGH
//		case SWT.ESC:
//		case ' ':
//			return LRESULT.ZERO;
//	}
//	return result;
//}
//
//LRESULT WM_GETOBJECT (int wParam, int lParam) {
//	/*
//	* Ensure that there is an accessible object created for this
//	* control because support for checked item and tree column
//	* accessibility is temporarily implemented in the accessibility
//	* package.
//	*/
//	if ((style & SWT.CHECK) != 0 || hwndParent != 0) {
//		if (accessible == null) accessible = new_Accessible (this);
//	}
//	return super.WM_GETOBJECT (wParam, lParam);
//}
//
//LRESULT WM_KEYDOWN (int wParam, int lParam) {
//	LRESULT result = super.WM_KEYDOWN (wParam, lParam);
//	if (result != null) return result;
//	switch (wParam) {
//		case OS.VK_SPACE: {
//			int hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
//			if (hItem != 0) {
//				hAnchor = hItem;
//				OS.SendMessage (handle, OS.TVM_ENSUREVISIBLE, 0, hItem);
//				TVITEM tvItem = new TVITEM ();
//				tvItem.mask = OS.TVIF_STATE | OS.TVIF_PARAM;
//				tvItem.hItem = hItem;
//				if ((style & SWT.CHECK) != 0) {
//					tvItem.stateMask = OS.TVIS_STATEIMAGEMASK;
//					OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
//					int state = tvItem.state >> 12;
//					if ((state & 0x1) != 0) {
//						state++;
//					} else  {
//						--state;
//					}
//					tvItem.state = state << 12;
//					OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
//					if (!OS.IsWinCE) {
//						int id = hItem;
//						if (OS.COMCTL32_MAJOR >= 6) {
//							id = OS.SendMessage (handle, OS.TVM_MAPHTREEITEMTOACCID, hItem, 0);
//						}
//						OS.NotifyWinEvent (OS.EVENT_OBJECT_FOCUS, handle, OS.OBJID_CLIENT, id);	
//					}
//				}
//				tvItem.stateMask = OS.TVIS_SELECTED;
//				OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
//				if ((style & SWT.MULTI) != 0 && OS.GetKeyState (OS.VK_CONTROL) < 0) {
//					if ((tvItem.state & OS.TVIS_SELECTED) != 0) {
//						tvItem.state &= ~OS.TVIS_SELECTED;
//					} else {
//						tvItem.state |= OS.TVIS_SELECTED;
//					}
//				} else {
//					tvItem.state |= OS.TVIS_SELECTED;
//				}
//				OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
//				Event event = new Event ();
//				event.item = items [tvItem.lParam];
//				postEvent (SWT.Selection, event);
//				if ((style & SWT.CHECK) != 0) {
//					event = new Event ();
//					event.item = items [tvItem.lParam];
//					event.detail = SWT.CHECK;
//					postEvent (SWT.Selection, event);
//				}
//				return LRESULT.ZERO;
//			}
//			break;
//		}
//		case OS.VK_UP:
//		case OS.VK_DOWN:
//		case OS.VK_PRIOR:
//		case OS.VK_NEXT:
//		case OS.VK_HOME:
//		case OS.VK_END: {
//			if ((style & SWT.SINGLE) != 0) break;
//			OS.SendMessage (handle, OS.WM_CHANGEUISTATE, OS.UIS_INITIALIZE, 0);
//			if (OS.GetKeyState (OS.VK_SHIFT) < 0) {
//				int hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
//				if (hItem != 0) {
//					if (hAnchor == 0) hAnchor = hItem;
//					ignoreSelect = ignoreDeselect = true;
//					int code = callWindowProc (handle, OS.WM_KEYDOWN, wParam, lParam);
//					ignoreSelect = ignoreDeselect = false;
//					int hNewItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
//					TVITEM tvItem = new TVITEM ();
//					tvItem.mask = OS.TVIF_STATE;
//					tvItem.stateMask = OS.TVIS_SELECTED;
//					int hDeselectItem = hItem;					
//					RECT rect1 = new RECT ();
//					rect1.left = hAnchor;
//					OS.SendMessage (handle, OS.TVM_GETITEMRECT, 1, rect1);
//					RECT rect2 = rect2 = new RECT ();
//					rect2.left = hDeselectItem;
//					OS.SendMessage (handle, OS.TVM_GETITEMRECT, 1, rect2);
//					int flags = rect1.top < rect2.top ? OS.TVGN_PREVIOUSVISIBLE : OS.TVGN_NEXTVISIBLE;
//					while (hDeselectItem != hAnchor) {
//						tvItem.hItem = hDeselectItem;
//						OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
//						hDeselectItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, flags, hDeselectItem);
//					}
//					int hSelectItem = hAnchor;
//					rect1.left = hNewItem;
//					OS.SendMessage (handle, OS.TVM_GETITEMRECT, 1, rect1);
//					rect2.left = hSelectItem;
//					OS.SendMessage (handle, OS.TVM_GETITEMRECT, 1, rect2);
//					tvItem.state = OS.TVIS_SELECTED;
//					flags = rect1.top < rect2.top ? OS.TVGN_PREVIOUSVISIBLE : OS.TVGN_NEXTVISIBLE;
//					while (hSelectItem != hNewItem) {
//						tvItem.hItem = hSelectItem;
//						OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
//						hSelectItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, flags, hSelectItem);
//					}
//					tvItem.hItem = hNewItem;
//					OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
//					tvItem.mask = OS.TVIF_PARAM;
//					tvItem.hItem = hNewItem;
//					OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
//					Event event = new Event ();
//					event.item = items [tvItem.lParam];
//					postEvent (SWT.Selection, event);
//					return new LRESULT (code);
//				}
//			}
//			if (OS.GetKeyState (OS.VK_CONTROL) < 0) {
//				int hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
//				if (hItem != 0) {
//					TVITEM tvItem = new TVITEM ();
//					tvItem.mask = OS.TVIF_STATE;
//					tvItem.stateMask = OS.TVIS_SELECTED;
//					tvItem.hItem = hItem;
//					OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
//					boolean oldSelected = (tvItem.state & OS.TVIS_SELECTED) != 0;
//					int hNewItem = 0;
//					switch (wParam) {
//						case OS.VK_UP:
//							hNewItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_PREVIOUSVISIBLE, hItem);
//							break;
//						case OS.VK_DOWN:
//							hNewItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXTVISIBLE, hItem);
//							break;
//						case OS.VK_HOME:
//							hNewItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_ROOT, 0);
//							break;
//						case OS.VK_PRIOR:
//							hNewItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_FIRSTVISIBLE, 0);
//							if (hNewItem == hItem) {
//								OS.SendMessage (handle, OS.WM_VSCROLL, OS.SB_PAGEUP, 0);
//								hNewItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_FIRSTVISIBLE, 0);
//							}
//							break;
//						case OS.VK_NEXT:			
//							RECT rect = new RECT (), clientRect = new RECT ();
//							OS.GetClientRect (handle, clientRect);
//							hNewItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_FIRSTVISIBLE, 0);
//							do {
//								int hVisible = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXTVISIBLE, hNewItem);
//								if (hVisible == 0) break;
//								rect.left = hVisible;
//								OS.SendMessage (handle, OS.TVM_GETITEMRECT, 1, rect);
//								if (rect.bottom > clientRect.bottom) break;
//								if ((hNewItem = hVisible) == hItem) {
//									OS.SendMessage (handle, OS.WM_VSCROLL, OS.SB_PAGEDOWN, 0);
//								}
//							} while (hNewItem != 0);
//							break;
//						case OS.VK_END:
//							hNewItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_LASTVISIBLE, 0);
//							break;
//					}
//					if (hNewItem != 0) {
//						OS.SendMessage (handle, OS.TVM_ENSUREVISIBLE, 0, hNewItem);
//						tvItem.hItem = hNewItem;
//						OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
//						boolean newSelected = (tvItem.state & OS.TVIS_SELECTED) != 0;
//						if (!newSelected && drawCount == 0) {
//							OS.UpdateWindow (handle);
//							OS.DefWindowProc (handle, OS.WM_SETREDRAW, 0, 0);
//							/*
//							* This code is intentionally commented.
//							*/
////							OS.SendMessage (handle, OS.WM_SETREDRAW, 0, 0);
//						}
//						ignoreSelect = true;
//						OS.SendMessage (handle, OS.TVM_SELECTITEM, OS.TVGN_CARET, hNewItem);
//						ignoreSelect = false;
//						if (oldSelected) {
//							tvItem.state = OS.TVIS_SELECTED;
//							tvItem.hItem = hItem;
//							OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
//						}
//						if (!newSelected) {
//							tvItem.state = 0;
//							tvItem.hItem = hNewItem;
//							OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
//						}
//						if (!newSelected && drawCount == 0) {
//							RECT rect1 = new RECT (), rect2 = new RECT ();
//							rect1.left = hItem;  rect2.left = hNewItem;
//							int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
//							int fItemRect = (bits & OS.TVS_FULLROWSELECT) != 0 ? 0 : 1;
//							OS.SendMessage (handle, OS.TVM_GETITEMRECT, fItemRect, rect1);
//							OS.SendMessage (handle, OS.TVM_GETITEMRECT, fItemRect, rect2);
//							/*
//							* This code is intentionally commented.
//							*/
////							OS.SendMessage (handle, OS.WM_SETREDRAW, 1, 0);
//							OS.DefWindowProc (handle, OS.WM_SETREDRAW, 1, 0);
//							if (OS.IsWinCE) {
//								OS.InvalidateRect (handle, rect1, false);
//								OS.InvalidateRect (handle, rect2, false);
//								OS.UpdateWindow (handle);
//							} else {
//								int flags = OS.RDW_UPDATENOW | OS.RDW_INVALIDATE;
//								OS.RedrawWindow (handle, rect1, 0, flags);
//								OS.RedrawWindow (handle, rect2, 0, flags);
//							}
//						}
//						return LRESULT.ZERO;
//					}
//				}
//			}
//			int code = callWindowProc (handle, OS.WM_KEYDOWN, wParam, lParam);
//			hAnchor = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
//			return new LRESULT (code);
//		}
//	}
//	return result;
//}
//
//LRESULT WM_KILLFOCUS (int wParam, int lParam) {
//	LRESULT result = super.WM_KILLFOCUS (wParam, lParam);
//	if ((style & SWT.SINGLE) != 0) return result;
//	/*
//	* Feature in Windows.  When multiple item have
//	* the TVIS_SELECTED state, Windows redraws only
//	* the focused item in the color used to show the
//	* selection when the tree loses or gains focus.
//	* The fix is to force Windows to redraw all the
//	* visible items when focus is gained or lost.
//	*/
//	OS.InvalidateRect (handle, null, false);
//	return result;
//}
//
//LRESULT WM_LBUTTONDBLCLK (int wParam, int lParam) {
//	if ((style & SWT.CHECK) != 0) {
//		TVHITTESTINFO lpht = new TVHITTESTINFO ();
//		lpht.x = (short) (lParam & 0xFFFF);
//		lpht.y = (short) (lParam >> 16);
//		OS.SendMessage (handle, OS.TVM_HITTEST, 0, lpht);
//		if ((lpht.flags & OS.TVHT_ONITEMSTATEICON) != 0) {
//			sendMouseEvent (SWT.MouseDown, 1, handle, OS.WM_LBUTTONDOWN, wParam, lParam);
//			sendMouseEvent (SWT.MouseDoubleClick, 1, handle, OS.WM_LBUTTONDBLCLK, wParam, lParam);
//			if (OS.GetCapture () != handle) OS.SetCapture (handle);
//			TVITEM tvItem = new TVITEM ();
//			tvItem.hItem = lpht.hItem;
//			tvItem.mask = OS.TVIF_PARAM | OS.TVIF_STATE;	
//			tvItem.stateMask = OS.TVIS_STATEIMAGEMASK;
//			OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
//			int state = tvItem.state >> 12;
//			if ((state & 0x1) != 0) {
//				state++;
//			} else  {
//				--state;
//			}
//			tvItem.state = state << 12;
//			OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
//			if (!OS.IsWinCE) {	
//				int id = tvItem.hItem;
//				if (OS.COMCTL32_MAJOR >= 6) {
//					id = OS.SendMessage (handle, OS.TVM_MAPHTREEITEMTOACCID, tvItem.hItem, 0);
//				}
//				OS.NotifyWinEvent (OS.EVENT_OBJECT_FOCUS, handle, OS.OBJID_CLIENT, id);	
//			}
//			Event event = new Event ();
//			event.item = items [tvItem.lParam];
//			event.detail = SWT.CHECK;
//			postEvent (SWT.Selection, event);
//			return LRESULT.ZERO;
//		}
//	}
//	return super.WM_LBUTTONDBLCLK (wParam, lParam);
//}
//
//LRESULT WM_LBUTTONDOWN (int wParam, int lParam) {
//	/*
//	* In a multi-select tree, if the user is collapsing a subtree that
//	* contains selected items, clear the selection from these items and
//	* issue a selection event.  Only items that are selected and visible
//	* are cleared.  This code also runs in the case when no item is selected.
//	*/
//	TVHITTESTINFO lpht = new TVHITTESTINFO ();
//	lpht.x = (short) (lParam & 0xFFFF);
//	lpht.y = (short) (lParam >> 16);
//	OS.SendMessage (handle, OS.TVM_HITTEST, 0, lpht);
//	if (lpht.hItem == 0 || (lpht.flags & OS.TVHT_ONITEMBUTTON) != 0) {
//		sendMouseEvent (SWT.MouseDown, 1, handle, OS.WM_LBUTTONDOWN, wParam, lParam);
//		boolean fixSelection = false, deselected = false;
//		if (lpht.hItem != 0 && (style & SWT.MULTI) != 0) {
//			int hSelection = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
//			if (hSelection != 0) {
//				TVITEM tvItem = new TVITEM ();
//				tvItem.mask = OS.TVIF_STATE | OS.TVIF_PARAM;
//				tvItem.hItem = lpht.hItem;
//				OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
//				if ((tvItem.state & OS.TVIS_EXPANDED) != 0) {
//					fixSelection = true;
//					tvItem.stateMask = OS.TVIS_SELECTED;
//					int hParent = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_PARENT, lpht.hItem);
//					int hLast = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_LASTVISIBLE, lpht.hItem);
//					int hNext = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXTVISIBLE, lpht.hItem);
//					while (hNext != 0 && hNext != hLast) {
//						tvItem.hItem = hNext;
//						OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
//						if ((tvItem.state & OS.TVIS_SELECTED) != 0) deselected = true;
//						tvItem.state = 0;
//						OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
//						if ((hNext = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXTVISIBLE, hNext)) == 0) break;
//						if (hParent == OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_PARENT, hNext)) break;
//					}
//				}
//			}
//		}
//		dragStarted = gestureCompleted = false;
//		if (fixSelection) ignoreDeselect = ignoreSelect = lockSelection = true;
//		int code = callWindowProc (handle, OS.WM_LBUTTONDOWN, wParam, lParam);
//		if (fixSelection) ignoreDeselect = ignoreSelect = lockSelection = false;
//		if (dragStarted && OS.GetCapture () != handle) OS.SetCapture (handle);
//		if (deselected) {
//			TVITEM tvItem = new TVITEM ();
//			tvItem.mask = OS.TVIF_PARAM;
//			tvItem.hItem = lpht.hItem;
//			OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
//			Event event = new Event ();
//			event.item = items [tvItem.lParam];
//			postEvent (SWT.Selection, event);
//		}
//		return new LRESULT (code);
//	}
//	
//	/* Look for check/uncheck */
//	if ((style & SWT.CHECK) != 0) {
//		if ((lpht.flags & OS.TVHT_ONITEMSTATEICON) != 0) {
//			sendMouseEvent (SWT.MouseDown, 1, handle, OS.WM_LBUTTONDOWN, wParam, lParam);
//			if (OS.GetCapture () != handle) OS.SetCapture (handle);
//			TVITEM tvItem = new TVITEM ();
//			tvItem.hItem = lpht.hItem;
//			tvItem.mask = OS.TVIF_PARAM | OS.TVIF_STATE;	
//			tvItem.stateMask = OS.TVIS_STATEIMAGEMASK;
//			OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
//			int state = tvItem.state >> 12;
//			if ((state & 0x1) != 0) {
//				state++;
//			} else  {
//				--state;
//			}
//			tvItem.state = state << 12;
//			OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
//			if (!OS.IsWinCE) {	
//				int id = tvItem.hItem;
//				if (OS.COMCTL32_MAJOR >= 6) {
//					id = OS.SendMessage (handle, OS.TVM_MAPHTREEITEMTOACCID, tvItem.hItem, 0);
//				}
//				OS.NotifyWinEvent (OS.EVENT_OBJECT_FOCUS, handle, OS.OBJID_CLIENT, id);	
//			}
//			Event event = new Event ();
//			event.item = items [tvItem.lParam];
//			event.detail = SWT.CHECK;
//			postEvent (SWT.Selection, event);
//			return LRESULT.ZERO;
//		}
//	}
//
//	/* Get the selected state of the item under the mouse */
//	TVITEM tvItem = new TVITEM ();
//	tvItem.mask = OS.TVIF_STATE;
//	tvItem.stateMask = OS.TVIS_SELECTED;
//	boolean hittestSelected = false;
//	if ((style & SWT.MULTI) != 0) {
//		tvItem.hItem = lpht.hItem;
//		OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
//		hittestSelected = (tvItem.state & OS.TVIS_SELECTED) != 0;
//	}
//	
//	/* Get the selected state of the last selected item */
//	int hOldItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
//	if ((style & SWT.MULTI) != 0) {
//		tvItem.hItem = hOldItem;
//		OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
//
//		/* Check for CONTROL or drag selection */
//		if (hittestSelected || (wParam & OS.MK_CONTROL) != 0) {
//			if (drawCount == 0) {
//				OS.UpdateWindow (handle);
//				OS.DefWindowProc (handle, OS.WM_SETREDRAW, 0, 0);
//				/*
//				* This code is intentionally commented.
//				*/
////				OS.SendMessage (handle, OS.WM_SETREDRAW, 0, 0);
//			}
//		} else {
//			deselectAll ();
//		}
//	}
//
//	/* Do the selection */
//	sendMouseEvent (SWT.MouseDown, 1, handle, OS.WM_LBUTTONDOWN, wParam, lParam);
//	dragStarted = gestureCompleted = false;
//	ignoreDeselect = ignoreSelect = true;
//	int code = callWindowProc (handle, OS.WM_LBUTTONDOWN, wParam, lParam);
//	ignoreDeselect = ignoreSelect = false;
//	if (dragStarted && OS.GetCapture () != handle) OS.SetCapture (handle);
//	int hNewItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
//
//	/*
//	* Feature in Windows.  When the old and new focused item
//	* are the same, Windows does not check to make sure that
//	* the item is actually selected, not just focused.  The
//	* fix is to force the item to draw selected by setting
//	* the state mask.  This is only necessary when the tree
//	* is single select.
//	*/
//	if ((style & SWT.SINGLE) != 0) {
//		if (hOldItem == hNewItem) {
//			tvItem.mask = OS.TVIF_STATE;
//			tvItem.state = OS.TVIS_SELECTED;
//			tvItem.stateMask = OS.TVIS_SELECTED;
//			tvItem.hItem = hNewItem;
//			OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
//		}
//	}
//	
//	/* Reselect the last item that was unselected */
//	if ((style & SWT.MULTI) != 0) {
//		
//		/* Check for CONTROL and reselect the last item */
//		if (hittestSelected || (wParam & OS.MK_CONTROL) != 0) {
//			if (hOldItem == hNewItem && hOldItem == lpht.hItem) {
//				if ((wParam & OS.MK_CONTROL) != 0) {
//					tvItem.state ^= OS.TVIS_SELECTED;
//					if (dragStarted) tvItem.state = OS.TVIS_SELECTED;
//					OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
//				}
//			} else {
//				if ((tvItem.state & OS.TVIS_SELECTED) != 0) {
//					tvItem.state = OS.TVIS_SELECTED;
//					OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
//				}
//				if ((wParam & OS.MK_CONTROL) != 0 && !dragStarted) {
//					if (hittestSelected) {
//						tvItem.state = 0;
//						tvItem.hItem = lpht.hItem;
//						OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
//					}
//				}
//			}
//			if (drawCount == 0) {
//				RECT rect1 = new RECT (), rect2 = new RECT ();
//				rect1.left = hOldItem;  rect2.left = hNewItem;
//				int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
//				int fItemRect = (bits & OS.TVS_FULLROWSELECT) != 0 ? 0 : 1;
//				OS.SendMessage (handle, OS.TVM_GETITEMRECT, fItemRect, rect1);
//				OS.SendMessage (handle, OS.TVM_GETITEMRECT, fItemRect, rect2);
//				/*
//				* This code is intentionally commented.
//				*/
////				OS.SendMessage (handle, OS.WM_SETREDRAW, 1, 0);
//				OS.DefWindowProc (handle, OS.WM_SETREDRAW, 1, 0);
//				if (OS.IsWinCE) {
//					OS.InvalidateRect (handle, rect1, false);
//					OS.InvalidateRect (handle, rect2, false);
//					OS.UpdateWindow (handle);
//				} else {
//					int flags = OS.RDW_UPDATENOW | OS.RDW_INVALIDATE;
//					OS.RedrawWindow (handle, rect1, 0, flags);
//					OS.RedrawWindow (handle, rect2, 0, flags);
//				} 
//			}
//		}
//
//		/* Check for SHIFT or normal select and delect/reselect items */
//		if ((wParam & OS.MK_CONTROL) == 0) {
//			if (!hittestSelected || !dragStarted) {
//				tvItem.state = 0;
//				int oldProc = OS.GetWindowLong (handle, OS.GWL_WNDPROC);
//				OS.SetWindowLong (handle, OS.GWL_WNDPROC, TreeProc);	
//				for (int i=0; i<items.length; i++) {
//					TreeItem item = items [i];
//					if (item != null && item.handle != hNewItem) {
//						tvItem.hItem = item.handle;
//						OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
//					}
//				}
//				tvItem.hItem = hNewItem;
//				tvItem.state = OS.TVIS_SELECTED;
//				OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
//				OS.SetWindowLong (handle, OS.GWL_WNDPROC, oldProc);
//				if ((wParam & OS.MK_SHIFT) != 0) {
//					RECT rect1 = new RECT ();
//					if (hAnchor == 0) hAnchor = hNewItem;
//					rect1.left = hAnchor;
//					if (OS.SendMessage (handle, OS.TVM_GETITEMRECT, 1, rect1) != 0) {
//						RECT rect2 = rect2 = new RECT ();
//						rect2.left = hNewItem;
//						OS.SendMessage (handle, OS.TVM_GETITEMRECT, 1, rect2);
//						int flags = rect1.top < rect2.top ? OS.TVGN_NEXTVISIBLE : OS.TVGN_PREVIOUSVISIBLE;			
//						tvItem.state = OS.TVIS_SELECTED;
//						int hItem = tvItem.hItem = hAnchor;
//						OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
//						while (hItem != hNewItem) {
//							tvItem.hItem = hItem;
//							OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
//							hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, flags, hItem);
//						}
//					}
//				}
//			}
//		}
//	}
//	if ((wParam & OS.MK_SHIFT) == 0) hAnchor = hNewItem;
//			
//	/* Issue notification */
//	if (!gestureCompleted) {
//		tvItem.hItem = hNewItem;
//		tvItem.mask = OS.TVIF_PARAM;
//		OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
//		Event event = new Event ();
//		event.item = items [tvItem.lParam];
//		postEvent (SWT.Selection, event);
//	}
//	gestureCompleted = false;
//	
//	/*
//	* Feature in Windows.  Inside WM_LBUTTONDOWN and WM_RBUTTONDOWN,
//	* the widget starts a modal loop to determine if the user wants
//	* to begin a drag/drop operation or marque select.  Unfortunately,
//	* this modal loop eats the corresponding mouse up.  The fix is to
//	* detect the cases when the modal loop has eaten the mouse up and
//	* issue a fake mouse up.
//	*/
//	if (dragStarted) {
//		Event event = new Event ();
//		event.x = (short) (lParam & 0xFFFF);
//		event.y = (short) (lParam >> 16);
//		postEvent (SWT.DragDetect, event);
//	} else {
//		sendMouseEvent (SWT.MouseUp, 1, handle, OS.WM_LBUTTONUP, wParam, lParam);
//	}
//	dragStarted = false;
//	return new LRESULT (code);
//}
//
//LRESULT WM_MOVE (int wParam, int lParam) {
//	if (ignoreResize) return null;
//	return super.WM_MOVE (wParam, lParam);
//}
//
//LRESULT WM_NOTIFY (int wParam, int lParam) {
//	NMHDR hdr = new NMHDR ();
//	OS.MoveMemory (hdr, lParam, NMHDR.sizeof);
//	if (hwndHeader != 0 && hdr.hwndFrom == hwndHeader) {
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
//				NMHEADER phdn = new NMHEADER ();
//				OS.MoveMemory (phdn, lParam, NMHEADER.sizeof);
//				TreeColumn column = columns [phdn.iItem];
//				if (column != null && !column.getResizable ()) {
//					return LRESULT.ONE;
//				}
//				break;
//			}
//			case OS.HDN_ITEMCHANGINGW:
//			case OS.HDN_ITEMCHANGINGA: {
//				NMHEADER phdn = new NMHEADER ();
//				OS.MoveMemory (phdn, lParam, NMHEADER.sizeof);
//				if (phdn.pitem != 0) {
//					HDITEM newItem = new HDITEM ();
//					OS.MoveMemory (newItem, phdn.pitem, HDITEM.sizeof);
//					if ((newItem.mask & OS.HDI_WIDTH) != 0) {
//						HDITEM oldItem = new HDITEM ();
//						oldItem.mask = OS.HDI_WIDTH;
//						OS.SendMessage (hwndHeader, OS.HDM_GETITEM, phdn.iItem, oldItem);
//						int deltaX = newItem.cxy - oldItem.cxy;
//						RECT rect = new RECT (), itemRect = new RECT ();
//						OS.GetClientRect (handle, rect);
//						OS.SendMessage (hwndHeader, OS.HDM_GETITEMRECT, phdn.iItem, itemRect);
//						int gridWidth = getLinesVisible () ? GRID_WIDTH : 0;
//						rect.left = itemRect.right - gridWidth;
//						int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
//						if (phdn.iItem < count - 1) {
//							for (int i=phdn.iItem; i<count; i++) {
//								OS.SendMessage (hwndHeader, OS.HDM_GETITEMRECT, i, itemRect);
//							}
//							rect.right = itemRect.right;
//						}
//						int flags = OS.SW_INVALIDATE | OS.SW_ERASE;
//						OS.ScrollWindowEx (handle, deltaX, 0, rect, null, 0, null, flags);
//						//TODO - column flashes when resized
//						if (phdn.iItem != 0) {
//							OS.SendMessage (hwndHeader, OS.HDM_GETITEMRECT, phdn.iItem, itemRect);
//							rect.left = itemRect.left;
//							rect.right = itemRect.right;
//							OS.InvalidateRect (handle, rect, true);
//						}
//						setScrollWidth ();
//					}
//				}
//				break;
//			}
//			case OS.HDN_ITEMCHANGEDW:
//			case OS.HDN_ITEMCHANGEDA: {
//				NMHEADER phdn = new NMHEADER ();
//				OS.MoveMemory (phdn, lParam, NMHEADER.sizeof);
//				if (phdn.pitem != 0) {
//					HDITEM pitem = new HDITEM ();
//					OS.MoveMemory (pitem, phdn.pitem, HDITEM.sizeof);
//					if ((pitem.mask & OS.HDI_WIDTH) != 0) {
//						TreeColumn column = columns [phdn.iItem];
//						if (column != null) {
//							column.sendEvent (SWT.Resize);
//							if (isDisposed ()) return LRESULT.ZERO;	
//							int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
//							TreeColumn [] newColumns = new TreeColumn [count];
//							System.arraycopy (columns, 0, newColumns, 0, count);
//							for (int i=phdn.iItem+1; i<count; i++) {
//								if (!newColumns [i].isDisposed ()) {
//									newColumns [i].sendEvent (SWT.Move);
//								}
//							}
//						}
//					}
//					setScrollWidth ();
//				}
//				break;
//			}
//			case OS.HDN_ITEMCLICKW:
//			case OS.HDN_ITEMCLICKA: {
//				NMHEADER phdn = new NMHEADER ();
//				OS.MoveMemory (phdn, lParam, NMHEADER.sizeof);
//				TreeColumn column = columns [phdn.iItem];
//				if (column != null) {
//					column.postEvent (SWT.Selection);
//				}
//				break;
//			}
//			case OS.HDN_ITEMDBLCLICKW:      
//			case OS.HDN_ITEMDBLCLICKA: {
//				NMHEADER phdn = new NMHEADER ();
//				OS.MoveMemory (phdn, lParam, NMHEADER.sizeof);
//				TreeColumn column = columns [phdn.iItem];
//				if (column != null) {
//					column.postEvent (SWT.DefaultSelection);
//				}
//				break;
//			}
//		}
//	}
//	return super.WM_NOTIFY (wParam, lParam);
//}
//
//LRESULT WM_RBUTTONDOWN (int wParam, int lParam) {
//	/*
//	* Feature in Windows.  The receiver uses WM_RBUTTONDOWN
//	* to initiate a drag/drop operation depending on how the
//	* user moves the mouse.  If the user clicks the right button,
//	* without moving the mouse, the tree consumes the corresponding
//	* WM_RBUTTONUP.  The fix is to avoid calling the window proc for
//	* the tree.
//	*/
//	sendMouseEvent (SWT.MouseDown, 3, handle, OS.WM_RBUTTONDOWN, wParam, lParam);
//	/*
//	* This code is intentionally commented.
//	*/
////	if (OS.GetCapture () != handle) OS.SetCapture (handle);
//	setFocus ();
//	
//	/*
//	* Feature in Windows.  When the user selects a tree item
//	* with the right mouse button, the item remains selected
//	* only as long as the user does not release or move the
//	* mouse.  As soon as this happens, the selection snaps
//	* back to the previous selection.  This behavior can be
//	* observed in the Explorer but is not instantly apparent
//	* because the Explorer explicity sets the selection when
//	* the user chooses a menu item.  If the user cancels the
//	* menu, the selection snaps back.  The fix is to avoid
//	* calling the window proc and do the selection ourselves.
//	* This behavior is consistent with the table.
//	*/
//	TVHITTESTINFO lpht = new TVHITTESTINFO ();
//	lpht.x = (short) (lParam & 0xFFFF);
//	lpht.y = (short) (lParam >> 16);
//	OS.SendMessage (handle, OS.TVM_HITTEST, 0, lpht);
//	if (lpht.hItem != 0 && (lpht.flags & (OS.TVHT_ONITEMICON | OS.TVHT_ONITEMLABEL)) != 0) {
//		if ((wParam & (OS.MK_CONTROL | OS.MK_SHIFT)) == 0) {
//			TVITEM tvItem = new TVITEM ();
//			tvItem.mask = OS.TVIF_STATE;
//			tvItem.stateMask = OS.TVIS_SELECTED;
//			tvItem.hItem = lpht.hItem;
//			OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
//			if ((tvItem.state & OS.TVIS_SELECTED) == 0) {
//				ignoreSelect = true;
//				OS.SendMessage (handle, OS.TVM_SELECTITEM, OS.TVGN_CARET, 0);
//				ignoreSelect = false;
//				OS.SendMessage (handle, OS.TVM_SELECTITEM, OS.TVGN_CARET, lpht.hItem);
//			}
//		}
//	}
//	return LRESULT.ZERO;
//}
//
//LRESULT WM_PRINTCLIENT (int wParam, int lParam) {
//	LRESULT result = super.WM_PRINTCLIENT (wParam, lParam);
//	if (result != null) return result;
//	/*
//	* Feature in Windows.  For some reason, when WM_PRINT is used
//	* to capture an image of a hierarchy that contains a tree with
//	* columns, the clipping that is used to stop the first column
//	* from drawing on top of subsequent columns stops the first
//	* column and the tree lines from drawing.  This does not happen
//	* during WM_PAINT.  The fix is to draw without clipping and
//	* then draw the rest of the columns on top.  Since the drawing
//	* is happening in WM_PRINTCLIENT, the redrawing is not visible.
//	*/
//	printClient = true;
//	int code = callWindowProc (handle, OS.WM_PRINTCLIENT, wParam, lParam);
//	printClient = false;
//	return new LRESULT (code);
//}
//
//LRESULT WM_SETFOCUS (int wParam, int lParam) {
//	LRESULT result = super.WM_SETFOCUS (wParam, lParam);
//	if ((style & SWT.SINGLE) != 0) return result;
//	/*
//	* Feature in Windows.  When multiple item have
//	* the TVIS_SELECTED state, Windows redraws only
//	* the focused item in the color used to show the
//	* selection when the tree loses or gains focus.
//	* The fix is to force Windows to redraw all the
//	* visible items when focus is gained or lost.
//	*/
//	OS.InvalidateRect (handle, null, false);
//	return result;
//}
//
//LRESULT WM_SETFONT (int wParam, int lParam) {
//	LRESULT result = super.WM_SETFONT (wParam, lParam);
//	if (result != null) return result;
//	if (hwndHeader != 0) OS.SendMessage (hwndHeader, OS.WM_SETFONT, wParam, lParam);
//	return result;
//}
//
//LRESULT WM_SIZE (int wParam, int lParam) {
//	if (ignoreResize) return null;
//	return super.WM_SIZE (wParam, lParam);
//}
//
//LRESULT WM_SYSCOLORCHANGE (int wParam, int lParam) {
//	LRESULT result = super.WM_SYSCOLORCHANGE (wParam, lParam);
//	if (result != null) return result;
//	if ((style & SWT.CHECK) != 0) setCheckboxImageList ();
//	return result;
//}
//
//LRESULT wmColorChild (int wParam, int lParam) {
//	/*
//	* Feature in Windows.  Tree controls send WM_CTLCOLOREDIT
//	* to allow application code to change the default colors.
//	* This is undocumented and conflicts with TVM_SETTEXTCOLOR
//	* and TVM_SETBKCOLOR, the documented way to do this.  The
//	* fix is to ignore WM_CTLCOLOREDIT messages from trees.
//	*/
//	return null;
//}
//
//LRESULT wmNotifyChild (int wParam, int lParam) {
//	NMHDR hdr = new NMHDR ();
//	OS.MoveMemory (hdr, lParam, NMHDR.sizeof);
//	switch (hdr.code) {
//		case OS.TVN_GETDISPINFOA:
//		case OS.TVN_GETDISPINFOW: {
//			NMTVDISPINFO lptvdi = new NMTVDISPINFO ();
//			OS.MoveMemory (lptvdi, lParam, NMTVDISPINFO.sizeof);
//			/*
//			* Feature in Windows.  When a new tree item is inserted
//			* using TVM_INSERTITEM, a TVN_GETDISPINFO is sent before
//			* TVM_INSERTITEM returns and before the item is added to
//			* the items array.  The fix is to check for null.
//			* 
//			* NOTE: This only happens on XP with the version 6.00 of
//			* COMCTL32.DLL,
//			*/
//			if (items == null) break;
//			TreeItem item = items [lptvdi.lParam];
//			if (item == null) break;
//			if ((lptvdi.mask & OS.TVIF_TEXT) != 0) {
//				String string = item.text;
//				TCHAR buffer = new TCHAR (getCodePage (), string, false);
//				int byteCount = Math.min (buffer.length (), lptvdi.cchTextMax - 1) * TCHAR.sizeof;
//				OS.MoveMemory (lptvdi.pszText, buffer, byteCount);
//				OS.MoveMemory (lptvdi.pszText + byteCount, new byte [TCHAR.sizeof], TCHAR.sizeof);
//				lptvdi.cchTextMax = Math.min (lptvdi.cchTextMax, string.length () + 1);
//			}
//			if ((lptvdi.mask & (OS.TVIF_IMAGE | OS.TVIF_SELECTEDIMAGE)) != 0) {
//				Image image = item.image;
//				lptvdi.iImage = OS.I_IMAGENONE;
//				if (image != null) {
//					lptvdi.iImage = lptvdi.iSelectedImage = imageIndex (image);
//				}
//			}
//			OS.MoveMemory (lParam, lptvdi, NMTVDISPINFO.sizeof);
//			break;
//		}
//		case OS.NM_CUSTOMDRAW: {
//			if (!customDraw) break;
//			NMTVCUSTOMDRAW nmcd = new NMTVCUSTOMDRAW ();
//			OS.MoveMemory (nmcd, lParam, NMTVCUSTOMDRAW.sizeof);		
//			switch (nmcd.dwDrawStage) {
//				case OS.CDDS_PREPAINT: {
//					return new LRESULT (OS.CDRF_NOTIFYITEMDRAW | OS.CDRF_NOTIFYPOSTPAINT);
//				}
//				case OS.CDDS_POSTPAINT: {
//					if (linesVisible) {
//						int hDC = nmcd.hdc;
//						if (hwndHeader != 0) {
//							int x = 0;
//							RECT rect = new RECT ();
//							HDITEM hdItem = new HDITEM ();
//							hdItem.mask = OS.HDI_WIDTH;
//							int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
//							for (int i=0; i<count; i++) {
//								OS.SendMessage (hwndHeader, OS.HDM_GETITEM, i, hdItem);
//								OS.SetRect (rect, x, nmcd.top, x + hdItem.cxy, nmcd.bottom);
//								OS.DrawEdge (hDC, rect, OS.BDR_SUNKENINNER, OS.BF_RIGHT);
//								x += hdItem.cxy;
//							}
//						}
//						RECT rect = new RECT ();
//						int hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_LASTVISIBLE, 0);
//						rect.left = hItem;
//						if (OS.SendMessage (handle, OS.TVM_GETITEMRECT, 0, rect) != 0) {
//							int height = rect.bottom - rect.top;
//							while (rect.bottom < nmcd.bottom) {
//								int top = rect.top + height;
//								OS.SetRect (rect, rect.left, top, rect.right, top + height);
//								OS.DrawEdge (hDC, rect, OS.BDR_SUNKENINNER, OS.BF_BOTTOM);
//							}
//						}
//					}
//					return new LRESULT (OS.CDRF_DODEFAULT);
//				}
//				case OS.CDDS_ITEMPREPAINT: {
//					/*
//					* Feature in Windows.  When a new tree item is inserted
//					* using TVM_INSERTITEM and the tree is using custom draw,
//					* a NM_CUSTOMDRAW is sent before TVM_INSERTITEM returns
//					* and before the item is added to the items array.  The
//					* fix is to check for null.
//					* 
//					* NOTE: This only happens on XP with the version 6.00 of
//					* COMCTL32.DLL,
//					*/
//					TreeItem item = items [nmcd.lItemlParam];
//					if (item == null) break;
//					if (nmcd.left >= nmcd.right || nmcd.top >= nmcd.bottom) {
//						break;
//					}
//					int hDC = nmcd.hdc;
//					OS.SaveDC (hDC);
//					if (linesVisible) {
//						RECT rect = new RECT ();
//						OS.SetRect (rect, nmcd.left, nmcd.top, nmcd.right, nmcd.bottom);
//						OS.DrawEdge (hDC, rect, OS.BDR_SUNKENINNER, OS.BF_BOTTOM);
//					}
//					if (!printClient && (style & SWT.FULL_SELECTION) == 0) {
//						if (hwndHeader != 0) {
//							int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
//							if (count != 0) {
//								HDITEM hdItem = new HDITEM ();
//								hdItem.mask = OS.HDI_WIDTH;
//								OS.SendMessage (hwndHeader, OS.HDM_GETITEM, 0, hdItem);
//								int hRgn = OS.CreateRectRgn (nmcd.left, nmcd.top, nmcd.left + hdItem.cxy, nmcd.bottom);
//								OS.SelectClipRgn (hDC, hRgn);
//								OS.DeleteObject (hRgn);
//							}
//						}
//					}
//					if (item.font == -1 && item.foreground == -1 && item.background == -1) {
//						if (item.cellForeground == null && item.cellBackground == null && item.cellFont == null) {
//							return new LRESULT (OS.CDRF_DODEFAULT | OS.CDRF_NOTIFYPOSTPAINT);
//						}
//					}
//					TVITEM tvItem = new TVITEM ();
//					tvItem.mask = OS.TVIF_STATE;
//					tvItem.hItem = item.handle;
//					OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
//					int hFont = item.cellFont != null ? item.cellFont [0] : item.font;
//					if (hFont != -1) OS.SelectObject (hDC, hFont);
//					if ((tvItem.state & (OS.TVIS_SELECTED | OS.TVIS_DROPHILITED)) == 0) {
//						if (OS.IsWindowEnabled (handle)) {
//							int clrText = item.cellForeground != null ? item.cellForeground [0] : item.foreground;
//							nmcd.clrText = clrText == -1 ? getForegroundPixel () : clrText;
//							int clrTextBk = item.cellBackground != null ? item.cellBackground [0] : item.background;
//							nmcd.clrTextBk = clrTextBk == -1 ? getBackgroundPixel () : clrTextBk;
//							OS.MoveMemory (lParam, nmcd, NMTVCUSTOMDRAW.sizeof);
//						}
//					}
//					return new LRESULT (OS.CDRF_NEWFONT | OS.CDRF_NOTIFYPOSTPAINT);
//				}
//				case OS.CDDS_ITEMPOSTPAINT: {
//					TreeItem item = items [nmcd.lItemlParam];
//					if (item == null) break;
//					/*
//					* Feature in Windows.  Under certain circumstances, Windows
//					* sends CDDS_ITEMPOSTPAINT for an empty rectangle.  This is
//					* not a problem providing that graphics do not occur outside
//					* the rectangle.  The fix is to test for the rectangle and
//					* draw nothing.
//					* 
//					* NOTE:  This seems to happen when both I_IMAGECALLBACK
//					* and LPSTR_TEXTCALLBACK are used at the same time with
//					* TVM_SETITEM.
//					*/
//					if (nmcd.left >= nmcd.right || nmcd.top >= nmcd.bottom) {
//						break;
//					}
//					int hDC = nmcd.hdc;
//					OS.RestoreDC (hDC, -1);
//					OS.SetBkMode (hDC, OS.TRANSPARENT);
//					boolean useColor = OS.IsWindowEnabled (handle);
//					if (useColor) {
//						if ((style & SWT.FULL_SELECTION) != 0) {
//							TVITEM tvItem = new TVITEM ();
//							tvItem.mask = OS.TVIF_STATE;
//							tvItem.hItem = item.handle;
//							OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
//							if ((tvItem.state & OS.TVIS_SELECTED) != 0) {
//								useColor = false;
//							} else {
//								/*
//								* Feature in Windows.  When the mouse is pressed and the
//								* selection is first drawn for a tree, the item is drawn
//								* selected, but the TVIS_SELECTED bits for the item are
//								* not set.  When the user moves the mouse slightly and
//								* a drag and drop operation is not started, the item is
//								* drawn again and this time TVIS_SELECTED is set.  This
//								* means that an item that is in a tree that has the style
//								* TVS_FULLROWSELECT and that also contains colored cells
//								* will not draw the entire row selected until the user
//								* moves the mouse.  The fix is to test for the selection
//								* colors and guess that the item is selected.
//								* 
//								* NOTE: This code doesn't work when the foreground and
//								* background of the tree are set to the selection colors
//								* but this does not happen in a regular application.
//								*/
//								int clrForeground = OS.GetTextColor (hDC);
//								if (clrForeground == OS.GetSysColor (OS.COLOR_HIGHLIGHTTEXT)) {
//									int clrBackground = OS.GetBkColor (hDC);
//									if (clrBackground == OS.GetSysColor (OS.COLOR_HIGHLIGHT)) {
//										useColor = false;
//									}
//								}
//							}
//						} else {
//							OS.SetTextColor (hDC, getForegroundPixel ());
//						}
//					}
//					if (hwndHeader != 0) {
//						GCData data = new GCData();
//						data.device = display;
//						GC gc = GC.win32_new (hDC, data);
//						int x = 0;
//						Point size = null;
//						RECT rect = new RECT ();
//						HDITEM hdItem = new HDITEM ();
//						hdItem.mask = OS.HDI_WIDTH;
//						int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
//						for (int i=0; i<count; i++) {
//							OS.SendMessage (hwndHeader, OS.HDM_GETITEM, i, hdItem);
//							if (i > 0) {
//								OS.SetRect (rect, x, nmcd.top, x + hdItem.cxy, nmcd.bottom - GRID_WIDTH);
//								if (printClient || (style & SWT.FULL_SELECTION) != 0) {
//									drawBackground (hDC, OS.GetBkColor (hDC), rect);
//								}
//								if (useColor) {
//									int clrTextBk = item.cellBackground != null ? item.cellBackground [i] : item.background;
//									if (clrTextBk != -1) drawBackground (hDC, clrTextBk, rect);
//								}
//								Image image = item.images != null ? item.images [i] : null;
//								if (image != null) {
//									Rectangle bounds = image.getBounds ();
//									if (size == null) size = getImageSize ();
//									gc.drawImage (image, 0, 0, bounds.width, bounds.height, rect.left, rect.top, size.x, size.y);
//									OS.SetRect (rect, rect.left + size.x + INSET, rect.top, rect.right - INSET, rect.bottom);
//								} else {
//									OS.SetRect (rect, rect.left + INSET, rect.top, rect.right - INSET, rect.bottom);
//								}
//								/*
//								* Bug in Windows.  When DrawText() is used with DT_VCENTER
//								* and DT_ENDELLIPSIS, the ellipsis can draw outside of the
//								* rectangle when the rectangle is empty.  The fix is avoid
//								* all text drawing for empty rectangles.
//								*/
//								if (rect.left < rect.right) {
//									if (item.strings != null && item.strings [i] != null) {
//										int hFont = item.cellFont != null ? item.cellFont [i] : item.font;
//										hFont = hFont != -1 ? OS.SelectObject (hDC, hFont) : -1;
//										int clrText = -1;
//										if (useColor) {
//											clrText = item.cellForeground != null ? item.cellForeground [i] : item.foreground;
//											clrText = clrText != -1? OS.SetTextColor (hDC, clrText) : -1;
//										}
//										int flags = OS.DT_NOPREFIX | OS.DT_SINGLELINE | OS.DT_VCENTER | OS.DT_ENDELLIPSIS;
//										TreeColumn column = columns [i];
//										if ((column.style & SWT.LEFT) != 0) flags |= OS.DT_LEFT;
//										if ((column.style & SWT.CENTER) != 0) flags |= OS.DT_CENTER;
//										if ((column.style & SWT.RIGHT) != 0) flags |= OS.DT_RIGHT;
//										TCHAR buffer = new TCHAR (getCodePage (), item.strings [i], false);
//										OS.DrawText (hDC, buffer, buffer.length (), rect, flags);
//										if (hFont != -1) OS.SelectObject (hDC, hFont);
//										if (clrText != -1) OS.SetTextColor (hDC, clrText);
//									}
//								}
//							}
//							x += hdItem.cxy;
//						}
//						gc.dispose ();
//					}
//					if (linesVisible) {
//						if (printClient && (style & SWT.FULL_SELECTION) == 0) {
//							if (hwndHeader != 0) {
//								int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
//								if (count != 0 && printClient) {
//									HDITEM hdItem = new HDITEM ();
//									hdItem.mask = OS.HDI_WIDTH;
//									OS.SendMessage (hwndHeader, OS.HDM_GETITEM, 0, hdItem);
//									RECT rect = new RECT ();
//									OS.SetRect (rect, nmcd.left + hdItem.cxy, nmcd.top, nmcd.right, nmcd.bottom);
//									OS.DrawEdge (hDC, rect, OS.BDR_SUNKENINNER, OS.BF_BOTTOM);
//								}
//							}
//						}
//						RECT rect = new RECT ();
//						if (OS.COMCTL32_MAJOR < 6 || (style & SWT.FULL_SELECTION) != 0) {
//							OS.SetRect (rect, nmcd.left, nmcd.top, nmcd.right, nmcd.bottom);
//						} else {
//							rect.left = item.handle;
//							if (OS.SendMessage (handle, OS.TVM_GETITEMRECT, 1, rect) != 0) {
//								int hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
//								if (hItem == item.handle) {
//									OS.SetRect (rect, rect.right, nmcd.top, nmcd.right, nmcd.bottom);
//								} else {
//									TVITEM tvItem = new TVITEM ();
//									tvItem.mask = OS.TVIF_STATE;
//									tvItem.hItem = item.handle;
//									OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
//									if ((tvItem.state & OS.TVIS_SELECTED) != 0) {
//										OS.SetRect (rect, rect.right, nmcd.top, nmcd.right, nmcd.bottom);
//									} else {
//										OS.SetRect (rect, rect.left, nmcd.top, nmcd.right, nmcd.bottom);
//									}
//								}
//							} else {
//								rect.left = 0;
//							}
//						}
//						OS.DrawEdge (hDC, rect, OS.BDR_SUNKENINNER, OS.BF_BOTTOM);
//					}
//					return new LRESULT (OS.CDRF_DODEFAULT);
//				}
//			}
//			break;
//		}
//		case OS.NM_DBLCLK: {
//			if (!ignoreSelect) {
//				int pos = OS.GetMessagePos ();
//				POINT pt = new POINT ();
//				pt.x = (short) (pos & 0xFFFF);
//				pt.y = (short) (pos >> 16);
//				OS.ScreenToClient (handle, pt);
//				TVHITTESTINFO lpht = new TVHITTESTINFO ();
//				lpht.x = pt.x;
//				lpht.y = pt.y;
//				OS.SendMessage (handle, OS.TVM_HITTEST, 0, lpht);
//				if ((lpht.flags & OS.TVHT_ONITEM) == 0) break;
//				Event event = new Event ();
//				int hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
//				if (hItem != 0) {
//					TVITEM tvItem = new TVITEM ();
//					tvItem.hItem = hItem;
//					tvItem.mask = OS.TVIF_PARAM;
//					OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
//					event.item = items [tvItem.lParam];
//				}
//				postEvent (SWT.DefaultSelection, event);
//			}
//			if (hooks (SWT.DefaultSelection)) return LRESULT.ONE;
//			break;
//		}
//		case OS.TVN_SELCHANGEDA:
//		case OS.TVN_SELCHANGEDW: {
//			if ((style & SWT.MULTI) != 0) {
//				if (lockSelection) {
//					/* Restore the old selection state of both items */
//					if (oldSelected) {
//						TVITEM tvItem = new TVITEM ();
//						int offset = NMHDR.sizeof + 4;
//						OS.MoveMemory (tvItem, lParam + offset, TVITEM.sizeof);
//						tvItem.mask = OS.TVIF_STATE;
//						tvItem.stateMask = OS.TVIS_SELECTED;
//						tvItem.state = OS.TVIS_SELECTED;
//						OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
//					}
//					if (!newSelected && ignoreSelect) {
//						TVITEM tvItem = new TVITEM ();
//						int offset = NMHDR.sizeof + 4 + TVITEM.sizeof;
//						OS.MoveMemory (tvItem, lParam + offset, TVITEM.sizeof);
//						tvItem.mask = OS.TVIF_STATE;
//						tvItem.stateMask = OS.TVIS_SELECTED;
//						tvItem.state = 0;
//						OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
//					}
//				}
//			}
//			if (!ignoreSelect) {
//				TVITEM tvItem = new TVITEM ();
//				int offset = NMHDR.sizeof + 4 + TVITEM.sizeof;
//				OS.MoveMemory (tvItem, lParam + offset, TVITEM.sizeof);
//				hAnchor = tvItem.hItem;
//				Event event = new Event ();
//				event.item = items [tvItem.lParam];
//				postEvent (SWT.Selection, event);
//			}
//			updateScrollBar ();
//			break;
//		}
//		case OS.TVN_SELCHANGINGA:
//		case OS.TVN_SELCHANGINGW: {
//			if ((style & SWT.MULTI) != 0) {
//				if (lockSelection) {
//					/* Save the old selection state for both items */
//					TVITEM tvItem = new TVITEM ();
//					int offset1 = NMHDR.sizeof + 4;
//					OS.MoveMemory (tvItem, lParam + offset1, TVITEM.sizeof);
//					oldSelected = (tvItem.state & OS.TVIS_SELECTED) != 0;
//					int offset2 = NMHDR.sizeof + 4 + TVITEM.sizeof;
//					OS.MoveMemory (tvItem, lParam + offset2, TVITEM.sizeof);
//					newSelected = (tvItem.state & OS.TVIS_SELECTED) != 0;
//				}
//			}
//			if (!ignoreSelect && !ignoreDeselect) {
//				hAnchor = 0;
//				if ((style & SWT.MULTI) != 0) deselectAll ();
//			}
//			break;
//		}
//		case OS.TVN_ITEMEXPANDEDA:
//		case OS.TVN_ITEMEXPANDEDW: {
//			updateScrollBar ();
//			break;
//		}
//		case OS.TVN_ITEMEXPANDINGA:
//		case OS.TVN_ITEMEXPANDINGW: {
//			if (!ignoreExpand) {
//				TVITEM tvItem = new TVITEM ();
//				int offset = NMHDR.sizeof + 4 + TVITEM.sizeof;
//				OS.MoveMemory (tvItem, lParam + offset, TVITEM.sizeof);
//				/*
//				* Feature in Windows.  In some cases, TVM_ITEMEXPANDING
//				* is sent from within TVM_DELETEITEM for the tree item
//				* being destroyed.  By the time the message is sent,
//				* the item has already been removed from the list of
//				* items.  The fix is to check for null. 
//				*/
//				if (items == null) break;
//				TreeItem item = items [tvItem.lParam];
//				if (item == null) break;
//				Event event = new Event ();
//				event.item = item;
//				int [] action = new int [1];
//				OS.MoveMemory (action, lParam + NMHDR.sizeof, 4);
//				switch (action [0]) {
//					case OS.TVE_EXPAND:
//						/*
//						* Bug in Windows.  When the numeric keypad asterisk
//						* key is used to expand every item in the tree, Windows
//						* sends TVN_ITEMEXPANDING to items in the tree that
//						* have already been expanded.  The fix is to detect
//						* that the item is already expanded and ignore the
//						* notification.
//						*/
//						if ((tvItem.state & OS.TVIS_EXPANDED) == 0) {
//							sendEvent (SWT.Expand, event);
//							if (isDisposed ()) return LRESULT.ZERO;
//						}
//						break;
//					case OS.TVE_COLLAPSE:
//						sendEvent (SWT.Collapse, event);
//						if (isDisposed ()) return LRESULT.ZERO;
//						break;
//				}
//			}
//			break;
//		}
//		case OS.TVN_BEGINDRAGA:
//		case OS.TVN_BEGINDRAGW:
//		case OS.TVN_BEGINRDRAGA:
//		case OS.TVN_BEGINRDRAGW: {
//			TVITEM tvItem = new TVITEM ();
//			int offset = NMHDR.sizeof + 4 + TVITEM.sizeof;
//			OS.MoveMemory (tvItem, lParam + offset, TVITEM.sizeof);
//			if (tvItem.hItem != 0 && (tvItem.state & OS.TVIS_SELECTED) == 0) {
//				ignoreSelect = ignoreDeselect = true;
//				OS.SendMessage (handle, OS.TVM_SELECTITEM, OS.TVGN_CARET, tvItem.hItem);
//				ignoreSelect = ignoreDeselect = false;
//			}
//			dragStarted = true;
//			break;
//		}
//		case OS.NM_RECOGNIZEGESTURE: {
//			/* 
//			* Feature in Pocket PC.  The tree and table controls detect the tap
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
//		}
//		case OS.GN_CONTEXTMENU: {
//			if (OS.IsPPC) {
//				boolean hasMenu = menu != null && !menu.isDisposed ();
//				if (hasMenu || hooks (SWT.MenuDetect)) {
//					NMRGINFO nmrg = new NMRGINFO ();
//					OS.MoveMemory (nmrg, lParam, NMRGINFO.sizeof);
//					showMenu (nmrg.x, nmrg.y);
//					gestureCompleted = true;
//					return LRESULT.ONE;
//				}
//			}
//			break;
//		}
//	}
//	return super.wmNotifyChild (wParam, lParam);
//}

Point minimumSize (int wHint, int hHint, boolean changed) {
  java.awt.Dimension size = handle.getPreferredSize();
  return new Point(size.width, size.height);
}

public void processEvent(AWTEvent e) {
  int id = e.getID();
  switch(id) {
  case MouseEvent.MOUSE_CLICKED: if(!hooks(SWT.DefaultSelection) || ((MouseEvent)e).getClickCount() != 2) { super.processEvent(e); return; } break;
  case ItemEvent.ITEM_STATE_CHANGED: if(!hooks(SWT.Selection)) { super.processEvent(e); return; } break;
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
    case MouseEvent.MOUSE_CLICKED:
      MouseEvent me = (MouseEvent)e;
      if(me.getClickCount() == 2) {
        java.awt.Point location = me.getPoint();
        TreePath path = ((CTree)handle).getPathForLocation(location.x, location.y);
        if(path != null) {
          Event event = new Event();
          event.item = ((CTreeItem)path.getLastPathComponent()).getTreeItem();
          sendEvent(SWT.DefaultSelection, event);
        }
      }
      break;
    case ItemEvent.ITEM_STATE_CHANGED:
      Event event = new Event();
      event.detail = SWT.CHECK;
      event.item = ((CTreeItem)((ItemEvent)e).getItem()).getTreeItem();
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

public void processEvent(EventObject e) {
  if(e instanceof TreeExpansionEvent) {
    TreePath path = ((TreeExpansionEvent)e).getPath();
    if(!(path.getLastPathComponent() instanceof CTreeItem)) {
      super.processEvent(e);
      return;
    }
    boolean isExpanded = ((CTree)handle).isExpanded(path);
    if(isExpanded) {
      if(!hooks(SWT.Expand)) { super.processEvent(e); return; }
    } else {
      if(!hooks(SWT.Collapse)) { super.processEvent(e); return; }
    }
  } else if(e instanceof TreeSelectionEvent) {
    TreePath path = ((TreeSelectionEvent)e).getPath();
    if(!(path.getLastPathComponent() instanceof CTreeItem)) {
      super.processEvent(e);
      return;
    }
    if(!hooks(SWT.Selection)) { super.processEvent(e); return; }
  } else if(e instanceof CellPaintEvent) {
      switch(((CellPaintEvent)e).getType()) {
      case CellPaintEvent.ERASE_TYPE: if(!hooks(SWT.EraseItem)) { super.processEvent(e); return; } break;
      case CellPaintEvent.PAINT_TYPE: if(!hooks(SWT.PaintItem)) { super.processEvent(e); return; } break;
      case CellPaintEvent.MEASURE_TYPE: if(!hooks(SWT.MeasureItem)) { super.processEvent(e); return; } break;
      default: super.processEvent(e); return;
      }
  } else {
    super.processEvent(e);
    return;
  }
  if(isDisposed()) {
    super.processEvent(e);
    return;
  }
  try {
    UIThreadUtils.startExclusiveSection(getDisplay());
    if(isDisposed()) {
      UIThreadUtils.stopExclusiveSection();
      super.processEvent(e);
      return;
    }
    if(e instanceof TreeExpansionEvent) {
      TreePath path = ((TreeExpansionEvent)e).getPath();
      boolean isExpanded = ((CTree)handle).isExpanded(path);
      Event event = new Event();
      event.item = ((CTreeItem)path.getLastPathComponent()).getTreeItem();
      if(isExpanded) {
        sendEvent(SWT.Expand, event);
        ((CTree)handle).expandPath(path);
      } else {
        sendEvent(SWT.Collapse, event);
        ((CTree)handle).collapsePath(path);
      }
    } else if(e instanceof TreeSelectionEvent) {
      TreePath path = ((TreeSelectionEvent)e).getPath();
      Event event = new Event();
      event.item = ((CTreeItem)path.getLastPathComponent()).getTreeItem();
      sendEvent(SWT.Selection, event);
    } else   if(e instanceof CellPaintEvent) {
      CellPaintEvent cellPaintEvent = (CellPaintEvent)e;
      switch(cellPaintEvent.getType()) {
      case CellPaintEvent.ERASE_TYPE: {
        TreeItem treeItem = cellPaintEvent.treeItem.getTreeItem();
        Rectangle cellBounds = treeItem.getBounds(cellPaintEvent.column);
        Event event = new Event();
        event.x = cellBounds.x;
        event.y = cellBounds.y;
        event.width = cellBounds.width;
        event.height = cellBounds.height;
        event.item = treeItem;
        event.index = cellPaintEvent.column;
        if(!cellPaintEvent.ignoreDrawForeground) event.detail |= SWT.FOREGROUND;
        if(!cellPaintEvent.ignoreDrawBackground) event.detail |= SWT.BACKGROUND;
        if(!cellPaintEvent.ignoreDrawSelection) event.detail |= SWT.SELECTED;
        if(!cellPaintEvent.ignoreDrawFocused) event.detail |= SWT.FOCUSED;
        event.gc = new GC(this);
        event.gc.handle.setUserClip(((CTree)handle).getCellRect(cellPaintEvent.row, cellPaintEvent.column, false));
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
        TreeItem treeItem = cellPaintEvent.treeItem.getTreeItem();
        Rectangle cellBounds = treeItem.getBounds(cellPaintEvent.column);
        Event event = new Event();
        event.x = cellBounds.x;
        event.y = cellBounds.y;
        event.width = cellBounds.width;
        event.height = cellBounds.height;
        event.item = treeItem;
        event.index = cellPaintEvent.column;
        if(!cellPaintEvent.ignoreDrawForeground) event.detail |= SWT.FOREGROUND;
        if(!cellPaintEvent.ignoreDrawBackground) event.detail |= SWT.BACKGROUND;
        if(!cellPaintEvent.ignoreDrawSelection) event.detail |= SWT.SELECTED;
        if(!cellPaintEvent.ignoreDrawFocused) event.detail |= SWT.FOCUSED;
        event.gc = new GC(this);
        event.gc.handle.setUserClip(((CTree)handle).getCellRect(cellPaintEvent.row, cellPaintEvent.column, false));
        sendEvent(SWT.PaintItem, event);
        break;
      }
      case CellPaintEvent.MEASURE_TYPE:
        TreeItem treeItem = cellPaintEvent.treeItem.getTreeItem();
//        Rectangle cellBounds = tableItem.getBounds(cellPaintEvent.column);
        Event event = new Event();
//        event.x = cellBounds.x;
//        event.y = cellBounds.y;
//        event.width = cellBounds.width;
//        event.height = cellBounds.height;
        event.height = cellPaintEvent.rowHeight;
        event.item = treeItem;
        event.index = cellPaintEvent.column;
        event.gc = new GC(this);
//        event.gc.handle.clip(((CTree)handle).getCellRect(cellPaintEvent.row, cellPaintEvent.column, false));
        sendEvent(SWT.MeasureItem, event);
//        cellPaintEvent.rowHeight -= event.height - cellBounds.height;
        cellPaintEvent.rowHeight = event.height;
        break;
      }
    }
    super.processEvent(e);
  } catch(Throwable t) {
    UIThreadUtils.storeException(t);
  } finally {
    UIThreadUtils.stopExclusiveSection();
  }
}

}
