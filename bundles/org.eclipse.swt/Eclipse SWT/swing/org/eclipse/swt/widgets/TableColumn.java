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
import java.beans.PropertyChangeEvent;
import java.util.EventObject;

import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.event.TableColumnModelEvent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.internal.swing.CTable;
import org.eclipse.swt.internal.swing.CTableColumn;
import org.eclipse.swt.internal.swing.UIThreadUtils;

/**
 * Instances of this class represent a column in a table widget.
 * <p><dl>
 * <dt><b>Styles:</b></dt>
 * <dd>LEFT, RIGHT, CENTER</dd>
 * <dt><b>Events:</b></dt>
 * <dd> Move, Resize, Selection</dd>
 * </dl>
 * </p><p>
 * Note: Only one of the styles LEFT, RIGHT and CENTER may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */
public class TableColumn extends Item {
	Table parent;
  CTableColumn handle;

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Table</code>) and a style value
 * describing its behavior and appearance. The item is added
 * to the end of the items maintained by its parent.
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
 * @see SWT#LEFT
 * @see SWT#RIGHT
 * @see SWT#CENTER
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public TableColumn (Table parent, int style) {
	super (parent, checkStyle (style));
  handle = createHandle();
	this.parent = parent;
	parent.createItem (this, parent.getColumnCount ());
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Table</code>), a style value
 * describing its behavior and appearance, and the index
 * at which to place it in the items maintained by its parent.
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
 * @param index the zero-relative index to store the receiver in its parent
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the parent (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#LEFT
 * @see SWT#RIGHT
 * @see SWT#CENTER
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public TableColumn (Table parent, int style, int index) {
	super (parent, checkStyle (style));
  handle = createHandle();
	this.parent = parent;
	parent.createItem (this, index);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is moved or resized, by sending
 * it one of the messages defined in the <code>ControlListener</code>
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
 * @see ControlListener
 * @see #removeControlListener
 */
public void addControlListener(ControlListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Resize,typedListener);
	addListener (SWT.Move,typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is selected, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is called when the column header is selected.
 * <code>widgetDefaultSelected</code> is not called.
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

static int checkStyle (int style) {
	return checkBits (style, SWT.LEFT, SWT.CENTER, SWT.RIGHT, 0, 0, 0);
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

CTableColumn createHandle () {
  return CTableColumn.Factory.newInstance(this, style);
}

void destroyWidget () {
  parent.destroyItem (this);
  releaseHandle ();
}

/**
 * Returns a value which describes the position of the
 * text or image in the receiver. The value will be one of
 * <code>LEFT</code>, <code>RIGHT</code> or <code>CENTER</code>.
 *
 * @return the alignment 
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getAlignment () {
	checkWidget ();
	if ((style & SWT.LEFT) != 0) return SWT.LEFT;
	if ((style & SWT.CENTER) != 0) return SWT.CENTER;
	if ((style & SWT.RIGHT) != 0) return SWT.RIGHT;
	return SWT.LEFT;
}

String getNameText () {
	return getText ();
}

/**
 * Returns the receiver's parent, which must be a <code>Table</code>.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Table getParent () {
	checkWidget ();
	return parent;
}

boolean moveable;

/**
 * Gets the moveable attribute. A column that is
 * not moveable cannot be reordered by the user 
 * by dragging the header but may be reordered 
 * by the programmer.
 *
 * @return the moveable attribute
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see Table#getColumnOrder()
 * @see Table#setColumnOrder(int[])
 * @see TableColumn#setMoveable(boolean)
 * @see SWT#Move
 * 
 * @since 3.1
 */
public boolean getMoveable () {
	checkWidget ();
  return moveable;
}

/**
 * Gets the resizable attribute. A column that is
 * not resizable cannot be dragged by the user but
 * may be resized by the programmer.
 *
 * @return the resizable attribute
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getResizable () {
	checkWidget ();
  return ((javax.swing.table.TableColumn)handle).getResizable();
}

/**
 * Returns the receiver's tool tip text, or null if it has
 * not been set.
 *
 * @return the receiver's tool tip text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.2
 */
public String getToolTipText () {
  checkWidget();
  return handle.getToolTipText();
}

/**
 * Gets the width of the receiver.
 *
 * @return the width
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getWidth () {
	checkWidget ();
	int index = parent.indexOf (this);
	if (index == -1) return 0;
  return ((javax.swing.table.TableColumn)handle).getWidth();
}

/**
 * Causes the receiver to be resized to its preferred size.
 * For a composite, this involves computing the preferred size
 * from its layout, if there is one.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 */
public void pack () {
	checkWidget ();
	int index = parent.indexOf (this);
	if (index == -1) return;
  int oldWidth = getWidth();
  CTable cTable = (CTable)parent.handle;
  int newWidth = cTable.getPreferredColumnWidth(index);
  // TODO: check why in the old SWTSwing, +2 is added.
  cTable.getColumnModel().getColumn(index).setPreferredWidth(newWidth + 2);

//	int hwnd = parent.handle;
//	int oldWidth = OS.SendMessage (hwnd, OS.LVM_GETCOLUMNWIDTH, index, 0);
//	TCHAR buffer = new TCHAR (parent.getCodePage (), text, true);
//	int headerWidth = OS.SendMessage (hwnd, OS.LVM_GETSTRINGWIDTH, 0, buffer) + Table.HEADER_MARGIN;
//	if (image != null) {
//		int margin = 0;
//		if (OS.COMCTL32_VERSION >= OS.VERSION (5, 80)) {
//			int hwndHeader = OS.SendMessage (hwnd, OS.LVM_GETHEADER, 0, 0);
//			margin = OS.SendMessage (hwndHeader, OS.HDM_GETBITMAPMARGIN, 0, 0);
//		} else {
//			margin = OS.GetSystemMetrics (OS.SM_CXEDGE) * 3;
//		}
//		Rectangle rect = image.getBounds ();
//		headerWidth += rect.width + margin * 2;
//	}
//	boolean oldIgnoreRezize = parent.ignoreResize;
//	parent.ignoreResize = true;
//	OS.SendMessage (hwnd, OS.LVM_SETCOLUMNWIDTH, index, OS.LVSCW_AUTOSIZE);
//	int columnWidth = OS.SendMessage (hwnd, OS.LVM_GETCOLUMNWIDTH, index, 0);
//	if (index == 0) {
//		/*
//		* Bug in Windows.  When LVM_SETCOLUMNWIDTH is used with LVSCW_AUTOSIZE
//		* where each item has I_IMAGECALLBACK but there are no images in the
//		* table, the size computed by LVM_SETCOLUMNWIDTH is too small for the
//		* first column, causing long items to be clipped with '...'.  The fix
//		* is to increase the column width by a small amount.
//		*/
//		if (parent.imageList == null) columnWidth += 2;
//		/*
//		* Bug in Windows.  When LVM_SETCOLUMNWIDTH is used with LVSCW_AUTOSIZE
//		* for a table with a state image list, the column is width does not
//		* include space for the state icon.  The fix is to increase the column
//		* width by the width of the image list.
//		*/
//		if ((parent.style & SWT.CHECK) != 0) {
//			int hStateList = OS.SendMessage (hwnd, OS.LVM_GETIMAGELIST, OS.LVSIL_STATE, 0);
//			if (hStateList != 0) {
//				int [] cx = new int [1], cy = new int [1];
//				OS.ImageList_GetIconSize (hStateList, cx, cy);
//				columnWidth += cx [0];
//			}
//		}
//	}
//	if (headerWidth > columnWidth) {
//		if (image == null) {
//			/*
//			* Feature in Windows.  When LVSCW_AUTOSIZE_USEHEADER is used
//			* with LVM_SETCOLUMNWIDTH to resize the last column, the last
//			* column is expanded to fill the client area.  The fix is to
//			* resize the table to be small, set the column width and then
//			* restore the table to its original size.
//			*/
//			RECT rect = null;
//			boolean fixWidth = index == parent.getColumnCount () - 1;
//			if (fixWidth) {
//				rect = new RECT ();
//				OS.GetWindowRect (hwnd, rect);
//				OS.UpdateWindow (hwnd);
//				int flags = OS.SWP_NOACTIVATE | OS.SWP_NOMOVE | OS.SWP_NOREDRAW | OS.SWP_NOZORDER;
//				SetWindowPos (hwnd, 0, 0, 0, 0, rect.bottom - rect.top, flags);
//			}
//			OS.SendMessage (hwnd, OS.LVM_SETCOLUMNWIDTH, index, OS.LVSCW_AUTOSIZE_USEHEADER);
//			if (fixWidth) {
//				int flags = OS.SWP_NOACTIVATE | OS.SWP_NOMOVE | OS.SWP_NOZORDER;
//				SetWindowPos (hwnd, 0, 0, 0, rect.right - rect.left, rect.bottom - rect.top, flags);
//			}
//		} else {
//			OS.SendMessage (hwnd, OS.LVM_SETCOLUMNWIDTH, index, headerWidth);
//		}
//	} else {
//		if (index == 0) {
//			OS.SendMessage (hwnd, OS.LVM_SETCOLUMNWIDTH, index, columnWidth);
//		}
//	}
//	parent.ignoreResize = oldIgnoreRezize;
//	int newWidth = OS.SendMessage (hwnd, OS.LVM_GETCOLUMNWIDTH, index, 0);
	if (oldWidth != newWidth) {
		sendEvent (SWT.Resize);
//		if (isDisposed ()) return;
//		boolean moved = false;
//		int [] order = parent.getColumnOrder ();
//		TableColumn [] columns = parent.getColumns ();
//		for (int i=0; i<order.length; i++) {
//			TableColumn column = columns [order [i]];
//			if (moved && !column.isDisposed ()) {
//				column.sendEvent (SWT.Move);
//			}
//			if (column == this) moved = true;
//		}
	}
}

void releaseHandle () {
  super.releaseHandle ();
  parent = null;
}

void releaseParent () {
  super.releaseParent ();
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is moved or resized.
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
 * @see ControlListener
 * @see #addControlListener
 */
public void removeControlListener (ControlListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Move, listener);
	eventTable.unhook (SWT.Resize, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is selected.
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
public void removeSelectionListener(SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

/**
 * Controls how text and images will be displayed in the receiver.
 * The argument should be one of <code>LEFT</code>, <code>RIGHT</code>
 * or <code>CENTER</code>.
 *
 * @param alignment the new alignment 
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setAlignment (int alignment) {
	checkWidget ();
	if ((alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER)) == 0) return;
	int index = parent.indexOf (this);
	if (index == -1 || index == 0) return;
	style &= ~(SWT.LEFT | SWT.RIGHT | SWT.CENTER);
	style |= alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER);
  if ((style & SWT.LEFT) == SWT.LEFT) handle.setAlignment(SwingConstants.LEFT);
  if ((style & SWT.CENTER) == SWT.CENTER) handle.setAlignment(SwingConstants.CENTER);
  if ((style & SWT.RIGHT) == SWT.RIGHT) handle.setAlignment(SwingConstants.RIGHT);
  // TODO: notify change
}

public void setImage (Image image) {
	checkWidget();
	if (image != null && image.isDisposed ()) {
		error (SWT.ERROR_INVALID_ARGUMENT);
	}
	int index = parent.indexOf (this);
	if (index == -1) return;
	super.setImage (image);
  handle.setIcon(image == null? null: new ImageIcon(image.handle));
}

/**
 * Sets the moveable attribute.  A column that is
 * moveable can be reordered by the user by dragging
 * the header. A column that is not moveable cannot be 
 * dragged by the user but may be reordered 
 * by the programmer.
 *
 * @param moveable the moveable attribute
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see Table#setColumnOrder(int[])
 * @see Table#getColumnOrder()
 * @see TableColumn#getMoveable()
 * @see SWT#Move
 * 
 * @since 3.1
 */
public void setMoveable (boolean moveable) {
	checkWidget ();
	this.moveable = moveable;
}

/**
 * Sets the resizable attribute.  A column that is
 * resizable can be resized by the user dragging the
 * edge of the header.  A column that is not resizable 
 * cannot be dragged by the user but may be resized 
 * by the programmer.
 *
 * @param resizable the resize attribute
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setResizable (boolean resizable) {
	checkWidget ();
  ((javax.swing.table.TableColumn)handle).setResizable(resizable);
}

public void setText (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
  if (string.equals (text)) return;
	int index = parent.indexOf (this);
	if (index == -1) return;
  super.setText (string);
  // TODO: check what happens with mnemonics
  ((javax.swing.table.TableColumn)handle).setHeaderValue(string);
}

/**
 * Sets the receiver's tool tip text to the argument, which
 * may be null indicating that no tool tip text should be shown.
 *
 * @param string the new tool tip text (or null)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.2
 */
public void setToolTipText (String string) {
  checkWidget();
  handle.setToolTipText(string);
}

/**
 * Sets the width of the receiver.
 *
 * @param width the new width
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setWidth (int width) {
	checkWidget ();
	int index = parent.indexOf (this);
	if (index == -1) return;
  ((javax.swing.table.TableColumn)handle).setPreferredWidth(width);
}

public void processEvent(AWTEvent e) {
  int id = e.getID();
  switch(id) {
  case java.awt.event.MouseEvent.MOUSE_CLICKED: if(!hooks(SWT.Selection)) return; break;
  }
  if(isDisposed()) {
    return;
  }
  UIThreadUtils.startExclusiveSection(getDisplay());
  if(isDisposed()) {
    UIThreadUtils.stopExclusiveSection();
    return;
  }
  try {
    switch(id) {
    case java.awt.event.MouseEvent.MOUSE_CLICKED: {
      sendEvent(SWT.Selection);
      break;
    }
    }
  } catch(Throwable t) {
    UIThreadUtils.storeException(t);
  } finally {
    UIThreadUtils.stopExclusiveSection();
  }
}

public void processEvent(EventObject e) {
  if(e instanceof PropertyChangeEvent) {
    if(!hooks(SWT.Resize) && !hooks(SWT.Move) || !"width".equals(((PropertyChangeEvent)e).getPropertyName())) { return; }
  } else if(e instanceof TableColumnModelEvent) {
    if(!hooks(SWT.Move)) { return; }
  } else {
    return;
  }
  if(isDisposed()) {
    return;
  }
  UIThreadUtils.startExclusiveSection(getDisplay());
  if(isDisposed()) {
    UIThreadUtils.stopExclusiveSection();
    return;
  }
  try {
    if(e instanceof PropertyChangeEvent) {
      String propertyName = ((PropertyChangeEvent)e).getPropertyName();
      if("width".equals(propertyName)) {
        sendEvent(SWT.Resize);
        int columnCount = parent.getColumnCount();
        for(int i=parent.indexOf(this) + 1; i<columnCount; i++) {
          parent.getColumn(i).sendEvent(SWT.Move);
        }
      }
    } else if(e instanceof TableColumnModelEvent) {
      sendEvent(SWT.Move);
    }
  } catch(Throwable t) {
    UIThreadUtils.storeException(t);
  } finally {
    UIThreadUtils.stopExclusiveSection();
  }
}

}
