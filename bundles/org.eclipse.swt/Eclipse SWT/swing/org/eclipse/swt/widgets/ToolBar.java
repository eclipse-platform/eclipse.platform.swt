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


import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.swing.CControl;
import org.eclipse.swt.internal.swing.CToolBar;
import org.eclipse.swt.internal.swing.Utils;

/**
 * Instances of this class support the layout of selectable
 * tool bar items.
 * <p>
 * The item children that may be added to instances of this class
 * must be of type <code>ToolItem</code>.
 * </p><p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to add <code>Control</code> children to it,
 * or set a layout on it.
 * </p><p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>FLAT, WRAP, RIGHT, HORIZONTAL, VERTICAL, SHADOW_OUT</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles HORIZONTAL and VERTICAL may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */
public class ToolBar extends Composite {
//	int lastFocusId;
  ArrayList itemList = new ArrayList();
//	ToolItem [] items;
//	boolean ignoreResize, ignoreMouse;
//	ImageList imageList, disabledImageList, hotImageList;
	
//	static final int DEFAULT_WIDTH = 24;
//	static final int DEFAULT_HEIGHT = 22;

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
 * @see SWT#FLAT
 * @see SWT#WRAP
 * @see SWT#RIGHT
 * @see SWT#HORIZONTAL
 * @see SWT#SHADOW_OUT
 * @see SWT#VERTICAL
 * @see Widget#checkSubclass()
 * @see Widget#getStyle()
 */
public ToolBar (Composite parent, int style) {
	super (parent, checkStyle (style));
}

ScrollBar createScrollBar (int type) {
  return null;
}

static int checkStyle (int style) {
	/*
	* A vertical tool bar cannot wrap.
	*/
	if ((style & SWT.VERTICAL) != 0) style &= ~SWT.WRAP;
	return style;
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
  checkWidget ();
  Point cSize = super.computeSize (wHint, hHint, changed);
  if((style & SWT.WRAP) != 0 && wHint != SWT.DEFAULT) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        isAdjustingSize = true;
      }
    });
    java.awt.Dimension size = handle.getSize();
    handle.setSize(wHint, Integer.MAX_VALUE);
    handle.validate();
    Component[] components = handle.getComponents();
    int maxHeight = 0;
    for(int i=0; i<components.length; i++) {
      java.awt.Rectangle bounds = components[i].getBounds();
      maxHeight = Math.max(bounds.y + bounds.height, maxHeight);
    }
    handle.setSize(size);
    handle.validate();
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        isAdjustingSize = false;
      }
    });
    return new Point(cSize.x, hHint != SWT.DEFAULT? hHint: maxHeight);
    // TODO: walk through all the components and compute the size for a fixed width.
  }
  Dimension preferredSize = handle.getPreferredSize();
  if(wHint == SWT.DEFAULT) {
    return new Point(preferredSize.width, preferredSize.height);
  }
  return new Point(cSize.x, preferredSize.height);
//  return cSize;
}

Control [] _getChildren () {
  Component[] children = ((CControl)handle).getClientArea().getComponents();
  if(children.length == 0) {
    return new Control[0];
  }
  ArrayList controlsList = new ArrayList(children.length);
  if(itemList != null) {
    for(int i=0; i<itemList.size(); i++) {
      Control control = ((ToolItem)itemList.get(i)).getControl();
      if(control != null) {
        controlsList.add(control);
      }
    }
  }
  return (Control[])controlsList.toArray(new Control[0]);
}

boolean autoAddChildren() {
  return false;
}

void createHandleInit() {
  super.createHandleInit();
  state &= ~CANVAS;
}

protected Container createHandle () {
  return (Container)CToolBar.Factory.newInstance(this, style);
}

void createItem (ToolItem item, int index) {
	int count = getItemCount();
	if (!(0 <= index && index <= count)) error (SWT.ERROR_INVALID_RANGE);
//	int id = 0;
//	while (id < items.length && items [id] != null) id++;
//	if (id == items.length) {
//		ToolItem [] newItems = new ToolItem [items.length + 4];
//		System.arraycopy (items, 0, newItems, 0, items.length);
//		items = newItems;
//	}
//	items [item.id = id] = item;
  itemList.add(index, item);
  handle.add(item.handle, index);
  ((JComponent)handle).revalidate();
  handle.repaint();
//	if ((style & SWT.VERTICAL) != 0) setRows (count + 1);
//	layoutItems ();
}

void createWidget () {
	super.createWidget ();
  itemList = new ArrayList();
//	items = new ToolItem [4];
//	lastFocusId = -1;
}

void destroyItem (ToolItem item) {
  itemList.remove(item);
  handle.remove(item.handle);
  ((JComponent)handle).revalidate();
  handle.repaint();
//	TBBUTTONINFO info = new TBBUTTONINFO ();
//	info.cbSize = TBBUTTONINFO.sizeof;
//	info.dwMask = OS.TBIF_IMAGE | OS.TBIF_STYLE;
//	int index = OS.SendMessage (handle, OS.TB_GETBUTTONINFO, item.id, info);
	/*
	* Feature in Windows.  For some reason, a tool item that has
	* the style BTNS_SEP does not return I_IMAGENONE when queried
	* for an image index, despite the fact that no attempt has been
	* made to assign an image to the item.  As a result, operations
	* on an image list that use the wrong index cause random results.	
	* The fix is to ensure that the tool item is not a separator
	* before using the image index.  Since separators cannot have
	* an image and one is never assigned, this is not a problem.
	*/
//	if ((info.fsStyle & OS.BTNS_SEP) == 0 && info.iImage != OS.I_IMAGENONE) {
//		if (imageList != null) imageList.put (info.iImage, null);
//		if (hotImageList != null) hotImageList.put (info.iImage, null);
//		if (disabledImageList != null) disabledImageList.put (info.iImage, null);
//	}
//	OS.SendMessage (handle, OS.TB_DELETEBUTTON, index, 0);
//	if (item.id == lastFocusId) lastFocusId = -1;
//	items [item.id] = null;
//	item.id = -1;
//	int count = getItemCount();
//	if (count == 0) {
//		if (imageList != null) {
//			OS.SendMessage (handle, OS.TB_SETIMAGELIST, 0, 0);
//			display.releaseToolImageList (imageList);
//		}
//		if (hotImageList != null) {
//			OS.SendMessage (handle, OS.TB_SETHOTIMAGELIST, 0, 0);
//			display.releaseToolHotImageList (hotImageList);
//		}
//		if (disabledImageList != null) {
//			OS.SendMessage (handle, OS.TB_SETDISABLEDIMAGELIST, 0, 0);
//			display.releaseToolDisabledImageList (disabledImageList);
//		}
//		imageList = hotImageList = disabledImageList = null;
//		items = new ToolItem [4];
//	}
//	if ((style & SWT.VERTICAL) != 0) setRows (count - 1);
  if(itemList.isEmpty()) {
    itemList = new ArrayList();
  }
//	layoutItems ();
}

//ImageList getDisabledImageList () {
//	return disabledImageList;
//}
//
//ImageList getHotImageList () {
//	return hotImageList;
//}
//
//ImageList getImageList () {
//	return imageList;
//}

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
public ToolItem getItem (int index) {
	checkWidget ();
	int count = getItemCount();
	if (!(0 <= index && index < count)) error (SWT.ERROR_INVALID_RANGE);
  return (ToolItem)itemList.get(index);
//	return items [handle.getclpButton.idCommand];
}

/**
 * Returns the item at the given point in the receiver
 * or null if no such item exists. The point is in the
 * coordinate system of the receiver.
 *
 * @param point the point used to locate the item
 * @return the item at the given point
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public ToolItem getItem (Point point) {
	checkWidget ();
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
	ToolItem [] items = getItems ();
	for (int i=0; i<items.length; i++) {
		Rectangle rect = items [i].getBounds ();
		if (rect.contains (point)) return items [i];
	}
	return null;
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
	return itemList.size();
}

/**
 * Returns an array of <code>ToolItem</code>s which are the items
 * in the receiver. 
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
public ToolItem [] getItems () {
	checkWidget ();
  return (ToolItem [])itemList.toArray(new ToolItem [0]);
//	int count = OS.SendMessage (handle, OS.TB_BUTTONCOUNT, 0, 0);
//	TBBUTTON lpButton = new TBBUTTON ();
//	ToolItem [] result = new ToolItem [count];
//	for (int i=0; i<count; i++) {
//		OS.SendMessage (handle, OS.TB_GETBUTTON, i, lpButton);
//		result [i] = items [lpButton.idCommand];
//	}
//	return result;
}

/**
 * Returns the number of rows in the receiver. When
 * the receiver has the <code>WRAP</code> style, the
 * number of rows can be greater than one.  Otherwise,
 * the number of rows is always one.
 *
 * @return the number of items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getRowCount () {
	checkWidget ();
  Utils.notImplemented(); return 1;
//	if ((style & SWT.VERTICAL) != 0) {
//		return OS.SendMessage (handle, OS.TB_BUTTONCOUNT, 0, 0);
//	}
//	return OS.SendMessage (handle, OS.TB_GETROWS, 0, 0);
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
 */
public int indexOf (ToolItem item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
  return itemList.indexOf(item);
//	return OS.SendMessage (handle, OS.TB_COMMANDTOINDEX, item.id, 0);
}

//void layoutItems () {
////	if ((style & SWT.WRAP) != 0) {
////		OS.SendMessage(handle, OS.TB_AUTOSIZE, 0, 0);
////	}
//  int count = itemList.size();
//	for (int i=0; i<count; i++) {
//		((ToolItem)itemList.get(i)).resizeControl ();
////		if (item != null) item.resizeControl ();
//	}
//}

Point minimumSize (int wHint, int hHint, boolean changed) {
  java.awt.Dimension size = handle.getPreferredSize();
  return new Point(size.width, size.height);
}

//boolean mnemonicHit (char ch) {
//	int key = Display.wcsToMbcs (ch);
//	int [] id = new int [1];
//	if (OS.SendMessage (handle, OS.TB_MAPACCELERATOR, key, id) == 0) {
//		return false;
//	}
//	if ((style & SWT.FLAT) != 0 && !setTabGroupFocus ()) return false;
//	int index = OS.SendMessage (handle, OS.TB_COMMANDTOINDEX, id [0], 0);
//	if (index == -1) return false;
//	OS.SendMessage (handle, OS.TB_SETHOTITEM, index, 0);
//	items [id [0]].click (false);
//	return true;
//}
//
//boolean mnemonicMatch (char ch) {
//	int key = Display.wcsToMbcs (ch);
//	int [] id = new int [1];
//	if (OS.SendMessage (handle, OS.TB_MAPACCELERATOR, key, id) == 0) {
//		return false;
//	}
//	/*
//	* Feature in Windows.  TB_MAPACCELERATOR matches either the mnemonic
//	* character or the first character in a tool item.  This behavior is
//	* undocumented and unwanted.  The fix is to ensure that the tool item
//	* contains a mnemonic when TB_MAPACCELERATOR returns true.
//	*/
//	int index = OS.SendMessage (handle, OS.TB_COMMANDTOINDEX, id [0], 0);
//	if (index == -1) return false;
//	return findMnemonic (items [id [0]].text) != '\0';
//}

void releaseChildren (boolean destroy) {
  if (itemList != null) {
    int count = itemList.size();
  	for (int i=0; i<count; i++) {
  		ToolItem item = (ToolItem)itemList.get(i);
  		if (item != null && !item.isDisposed ()) {
//  			item.releaseImages ();
  			item.release (false);
  		}
  	}
    itemList = null;
  }
//	if (imageList != null) {
//		OS.SendMessage (handle, OS.TB_SETIMAGELIST, 0, 0);
//		display.releaseToolImageList (imageList);
//	}
//	if (hotImageList != null) {
//		OS.SendMessage (handle, OS.TB_SETHOTIMAGELIST, 0, 0);
//		display.releaseToolHotImageList (hotImageList);
//	}
//	if (disabledImageList != null) {
//		OS.SendMessage (handle, OS.TB_SETDISABLEDIMAGELIST, 0, 0);
//		display.releaseToolDisabledImageList (disabledImageList);
//	}
//	imageList = hotImageList = disabledImageList = null;
  super.releaseChildren (destroy);
}

void removeControl (Control control) {
	super.removeControl (control);
  int count = itemList.size();
	for (int i=0; i<count; i++) {
		ToolItem item = (ToolItem)itemList.get(i);
		if (item != null && item.control == control) {
			item.setControl (null);
		}
	}
}

//void setBounds (int x, int y, int width, int height, int flags) {
//	/*
//	* Feature in Windows.  For some reason, when a tool bar is
//	* repositioned more than once using DeferWindowPos () into
//	* the same HDWP, the toolbar redraws more than once, defeating
//	* the puropse of DeferWindowPos ().  The fix is to end the
//	* defered positioning before the next tool bar is added,
//	* ensuring that only one tool bar position is deferred at
//	* any given time.
//	*/
//	if (parent.lpwp != null) {
//		if (drawCount == 0 && OS.IsWindowVisible (handle)) {
//			parent.setResizeChildren (false);
//			parent.setResizeChildren (true);
//		}
//	}
//	super.setBounds (x, y, width, height, flags);
//}
//
//void setDefaultFont () {
//	super.setDefaultFont ();
//	OS.SendMessage (handle, OS.TB_SETBITMAPSIZE, 0, 0);
//	OS.SendMessage (handle, OS.TB_SETBUTTONSIZE, 0, 0);
//}
//
//void setDisabledImageList (ImageList imageList) {
//	if (disabledImageList == imageList) return;
//	int hImageList = 0;
//	if ((disabledImageList = imageList) != null) {
//		hImageList = disabledImageList.getHandle ();
//	}
//	OS.SendMessage (handle, OS.TB_SETDISABLEDIMAGELIST, 0, hImageList);
//}

//void setHotImageList (ImageList imageList) {
//	if (hotImageList == imageList) return;
//	int hImageList = 0;
//	if ((hotImageList = imageList) != null) {
//		hImageList = hotImageList.getHandle ();
//	}
//	OS.SendMessage (handle, OS.TB_SETHOTIMAGELIST, 0, hImageList);
//}
//
//void setImageList (ImageList imageList) {
//	if (this.imageList == imageList) return;
//	int hImageList = 0;
//	if ((this.imageList = imageList) != null) {
//		hImageList = imageList.getHandle ();
//	}
//	OS.SendMessage (handle, OS.TB_SETIMAGELIST, 0, hImageList);
//}

//public boolean setParent (Composite parent) {
//	checkWidget ();
//	if (!super.setParent (parent)) return false;
//	// TODO: how to add, if the new parent is a coolbar?
//  Utils.notImplemented(); return false;
////	OS.SendMessage (handle, OS.TB_SETPARENT, parent.handle, 0);
////	return true;
//}

boolean setTabItemFocus () {
	int index = 0;
  int count = itemList.size();
	while (index < count) {
		ToolItem item = (ToolItem)itemList.get(index);
		if (item != null && (item.style & SWT.SEPARATOR) == 0) {
			if (item.getEnabled ()) break;
		}
		index++;
	}
	if (index == count) return false;
	return super.setTabItemFocus ();
}

}
