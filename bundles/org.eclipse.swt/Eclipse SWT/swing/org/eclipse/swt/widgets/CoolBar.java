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
import java.util.ArrayList;

import javax.swing.JComponent;

import org.eclipse.swt.internal.swing.CCoolBar;
import org.eclipse.swt.internal.swing.CCoolItem;
import org.eclipse.swt.internal.swing.Compatibility;
import org.eclipse.swt.internal.swing.JCoolBarItem;
import org.eclipse.swt.internal.swing.Utils;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class provide an area for dynamically
 * positioning the items they contain.
 * <p>
 * The item children that may be added to instances of this class
 * must be of type <code>CoolItem</code>.
 * </p><p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to add <code>Control</code> children to it,
 * or set a layout on it.
 * </p><p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>FLAT, HORIZONTAL, VERTICAL</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * </p><p>
 * Note: Only one of the styles HORIZONTAL and VERTICAL may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */

public class CoolBar extends Composite {
	ArrayList itemList;
  ArrayList originalItemList;
//	CoolItem [] originalItems;
//	boolean locked;
//	boolean ignoreResize;
//	static final int SEPARATOR_WIDTH = 2;
//	static final int MAX_WIDTH = 0x7FFF;

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
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public CoolBar (Composite parent, int style) {
	super (parent, checkStyle (style));
}

static int checkStyle (int style) {
	style |= SWT.NO_FOCUS;
	/*
	* Even though it is legal to create this widget
	* with scroll bars, they serve no useful purpose
	* because they do not automatically scroll the
	* widget's client area.  The fix is to clear
	* the SWT style.
	*/
	return style & ~(SWT.H_SCROLL | SWT.V_SCROLL);
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

//public Point computeSize (int wHint, int hHint, boolean changed) {
//	checkWidget ();
//	int width = 0, height = 0;
//	int border = getBorderWidth ();
//	int newWidth = wHint == SWT.DEFAULT ? 0x3FFF : wHint + (border * 2);
//	int newHeight = hHint == SWT.DEFAULT ? 0x3FFF : hHint + (border * 2);
//	int count = OS.SendMessage (handle, OS.RB_GETBANDCOUNT, 0, 0);
//	if (count != 0) {
//		ignoreResize = true;
//		boolean redraw = false;
//		if (OS.IsWindowVisible (handle)) {
//			if (OS.COMCTL32_MAJOR >= 6) {
//				redraw = true;
//				OS.UpdateWindow (handle);
//				OS.DefWindowProc (handle, OS.WM_SETREDRAW, 0, 0);
//			} else {
//				redraw = drawCount == 0;
//				if (redraw) {
//					OS.UpdateWindow (handle);
//					OS.SendMessage (handle, OS.WM_SETREDRAW, 0, 0);
//				}
//			}
//		}	
//		RECT oldRect = new RECT ();
//		OS.GetWindowRect (handle, oldRect);
//		int oldWidth = oldRect.right - oldRect.left;
//		int oldHeight = oldRect.bottom - oldRect.top;
//		int flags = OS.SWP_NOACTIVATE | OS.SWP_NOMOVE | OS.SWP_NOREDRAW | OS.SWP_NOZORDER;	
//		SetWindowPos (handle, 0, 0, 0, newWidth, newHeight, flags);
//		RECT rect = new RECT ();
//		OS.SendMessage (handle, OS.RB_GETRECT, count - 1, rect);
//		height = Math.max (height, rect.bottom);
//		SetWindowPos (handle, 0, 0, 0, oldWidth, oldHeight, flags);
//		REBARBANDINFO rbBand = new REBARBANDINFO ();
//		rbBand.cbSize = REBARBANDINFO.sizeof;
//		rbBand.fMask = OS.RBBIM_IDEALSIZE | OS.RBBIM_STYLE;
//		int rowWidth = 0;
//		int separator = (style & SWT.FLAT) == 0 ? SEPARATOR_WIDTH : 0;
//		for (int i = 0; i < count; i++) {
//			OS.SendMessage(handle, OS.RB_GETBANDINFO, i, rbBand);
//			if ((rbBand.fStyle & OS.RBBS_BREAK) != 0) {
//				width = Math.max(width, rowWidth - separator);
//				rowWidth = 0;
//			}
//			rowWidth += rbBand.cxIdeal + getMargin (i) + separator;
//		}
//		width = Math.max(width, rowWidth - separator);
//		if (redraw) {
//			if (OS.COMCTL32_MAJOR >= 6) {
//				OS.DefWindowProc (handle, OS.WM_SETREDRAW, 1, 0);
//			} else {
//				OS.SendMessage (handle, OS.WM_SETREDRAW, 1, 0);
//			}
//		}
//		ignoreResize = false;
//	}
//	if (width == 0) width = DEFAULT_WIDTH;
//	if (height == 0) height = DEFAULT_HEIGHT;
//	if (wHint != SWT.DEFAULT) width = wHint;
//	if (hHint != SWT.DEFAULT) height = hHint;
//	height += border * 2;
//	width += border * 2;	
//	return new Point (width, height);
//}

boolean autoAddChildren() {
  return false;
}

void createHandleInit() {
  super.createHandleInit();
  state &= ~(CANVAS | THEME_BACKGROUND);
}

protected Container createHandle () {
  return (Container)CCoolBar.Factory.newInstance(this, style);
}

//void createHandle () {
//	super.createHandle ();
//	state &= ~CANVAS;
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

void createItem (CoolItem item, int index) {
	int count = getItemCount();
	if (!(0 <= index && index <= count)) error (SWT.ERROR_INVALID_RANGE);
  itemList.add(index, item);
//	if (index == 0 && count > 0) {
//		getItem (0).setWrap (false); 
//	}
  handle.add(item.handle, index);
  originalItemList.add(index, item);
//	/* Insert the item */
//	if (OS.SendMessage (handle, OS.RB_INSERTBAND, index, rbBand) == 0) {
//		error (SWT.ERROR_ITEM_NOT_ADDED);
//	}
//	
//	/* Resize the next to last item to the ideal size */
//	if (fixLast) {  	
//		resizeToPreferredWidth (lastIndex);
//	}
//	
//	OS.HeapFree (hHeap, 0, lpText);
//	items [item.id = id] = item;
//	int length = originalItems.length;
//	CoolItem [] newOriginals = new CoolItem [length + 1];
//	System.arraycopy (originalItems, 0, newOriginals, 0, index);
//	System.arraycopy (originalItems, index, newOriginals, index + 1, length - index);
//	newOriginals [index] = item;
//	originalItems = newOriginals;
}

void createWidget () {
	super.createWidget ();
	itemList = new ArrayList(4);
  originalItemList = new ArrayList(4);
//	originalItems = new CoolItem [0];
}

void destroyItem (CoolItem item) {
  if(!itemList.remove(item)) {
    return;
  }
  Component[] components = handle.getComponents();
  for(int i=0; i<components.length; i++) {
    if(components[i] == item.handle) {
      if(i + 1 < components.length && ((CCoolItem)item.handle).isWrapped()) {
        ((CCoolItem)components[i+1]).setWrapped(true);
      }
      handle.remove(i);
      ((JComponent)handle).revalidate();
      handle.repaint();
      break;
    }
  }
  originalItemList.remove(item);
  if(itemList.isEmpty()) {
    itemList = new ArrayList(4);
    originalItemList = new ArrayList(4);
  }
//  int index = OS.SendMessage (handle, OS.RB_IDTOINDEX, item.id, 0);
//	int count = OS.SendMessage (handle, OS.RB_GETBANDCOUNT, 0, 0);
//	if (count != 0) {
//		int lastIndex = getLastIndexOfRow (index);
//		if (index == lastIndex) {
//			/*
//			* Feature in Windows.  If the last item in a row is
//			* given its ideal size, it will be placed at the far
//			* right hand edge of the coolbar.  It is preferred
//			* that the last item appear next to the second last
//			* item.  The fix is to size the last item of each row 
//			* so that it occupies all the available space to the
//			* right in the row.
//			*/
//			resizeToMaximumWidth (lastIndex - 1);
//		}						
//	}	
//		
//	/*
//	* Feature in Windows.  When Windows removed a rebar
//	* band, it makes the band child invisible.  The fix
//	* is to show the child.
//	*/		
//	Control control = item.control;
//	boolean wasVisible = control != null && !control.isDisposed() && control.getVisible ();
//
//	/*
//	* When a wrapped item is being deleted, make the next
//	* item in the row wrapped in order to preserve the row.
//	* In order to avoid an unnecessary layout, temporarily
//	* ignore WM_SIZE.  If the next item is wrapped then a
//	* row will be deleted and the WM_SIZE is necessary.
//	*/
//	CoolItem nextItem = null;
//	if (item.getWrap ()) {
//		if (index + 1 < count) {
//			nextItem = getItem (index + 1);
//			ignoreResize = !nextItem.getWrap ();
//		}
//	}
//	if (OS.SendMessage (handle, OS.RB_DELETEBAND, index, 0) == 0) {
//		error (SWT.ERROR_ITEM_NOT_REMOVED);
//	}
//	items [item.id] = null;
//	item.id = -1;
//	if (ignoreResize) {
//		nextItem.setWrap (true);
//		ignoreResize = false;
//	}
//	
//	/* Restore the visible state tof the control */
//	if (wasVisible) control.setVisible (true);
//	
//	index = 0;
//	while (index < originalItems.length) {
//		if (originalItems [index] == item) break;
//		index++;
//	}
//	int length = originalItems.length - 1;
//	CoolItem [] newOriginals = new CoolItem [length];
//	System.arraycopy (originalItems, 0, newOriginals, 0, index);
//	System.arraycopy (originalItems, index + 1, newOriginals, index, length - index);
//	originalItems = newOriginals;
}

//int getMargin (int index) {
//	int margin = 0;
//	if (OS.COMCTL32_MAJOR >= 6) {
//		MARGINS margins = new MARGINS ();
//		OS.SendMessage (handle, OS.RB_GETBANDMARGINS, 0, margins);
//		margin += margins.cxLeftWidth + margins.cxRightWidth;
//	}
//	RECT rect = new RECT ();
//	OS.SendMessage (handle, OS.RB_GETBANDBORDERS, index, rect);
//	if ((style & SWT.FLAT) != 0) {
//		/*
//		* Bug in Windows.  When the style bit  RBS_BANDBORDERS is not set
//		* the rectangle returned by RBS_BANDBORDERS is four pixels too small.
//		* The fix is to add four pixels to the result.
//		*/	
//		margin += rect.left + 4;
//	} else {
//		margin += rect.left + rect.right; 
//	}
//	return margin;
//}

//Control findThemeControl () {
//	return null;
//}

/**
 * Returns the item that is currently displayed at the given,
 * zero-relative index. Throws an exception if the index is
 * out of range.
 *
 * @param index the visual index of the item to return
 * @return the item at the given visual index
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public CoolItem getItem (int index) {
	checkWidget ();
	int count = getItemCount();
	if (!(0 <= index && index < count)) error (SWT.ERROR_INVALID_RANGE);
  Component component = handle.getComponent(index);
  for(int i=itemList.size()-1; i>=0; i--) {
    CoolItem coolItem = (CoolItem)itemList.get(i);
    if(coolItem.handle == component) {
      return coolItem;
    }
  }
  // Should not happen
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
	return ((CCoolBar)handle).getItemCount();
}

/**
 * Returns an array of zero-relative ints that map
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
 */
public int [] getItemOrder () {
	checkWidget ();
  Utils.notImplemented(); return null;
//	int count = OS.SendMessage (handle, OS.RB_GETBANDCOUNT, 0, 0);
//	int [] indices = new int [count];
//	REBARBANDINFO rbBand = new REBARBANDINFO ();
//	rbBand.cbSize = REBARBANDINFO.sizeof;
//	rbBand.fMask = OS.RBBIM_ID;
//	for (int i=0; i<count; i++) {
//		OS.SendMessage (handle, OS.RB_GETBANDINFO, i, rbBand);
//		CoolItem item = items [rbBand.wID];
//		int index = 0;
//		while (index<originalItems.length) {
//			if (originalItems [index] == item) break;
//			index++;
//		}
//		if (index == originalItems.length) error (SWT.ERROR_CANNOT_GET_ITEM);
//		indices [i] = index;
//	}
//	return indices;
}

/**
 * Returns an array of <code>CoolItem</code>s in the order
 * in which they are currently being displayed.
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of items, so modifying the array will
 * not affect the receiver. 
 * </p>
 *
 * @return the receiver's items in their current visual order
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public CoolItem [] getItems () {
	checkWidget ();
	int count = getItemCount();
  CoolItem[] coolItems = new CoolItem[count];
  ArrayList itemList = (ArrayList)this.itemList.clone();
  Component[] components = handle.getComponents();
  for(int i=0; i<components.length; i++) {
    Component component = components[i];
    for(int j=itemList.size()-1; j>=0; j--) {
      CoolItem coolItem = (CoolItem)itemList.get(j);
      if(coolItem.handle == component) {
        itemList.remove(j);
        coolItems[i] = coolItem;
        break;
      }
    }
  }
  return coolItems;
}

/**
 * Returns an array of points whose x and y coordinates describe
 * the widths and heights (respectively) of the items in the receiver
 * in the order in which they are currently being displayed.
 *
 * @return the receiver's item sizes in their current visual order
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Point [] getItemSizes () {
	checkWidget ();	
  Component[] components = handle.getComponents();
	int count = components.length;
  Point [] sizes = new Point [count];
  for(int i=0; i<count; i++) {
    java.awt.Dimension size = components[i].getSize();
    sizes[i] = new Point(size.width, size.height);
  }
  return sizes;
}

//int getLastIndexOfRow (int index) {
//	int count = OS.SendMessage (handle, OS.RB_GETBANDCOUNT, 0, 0);
//	if (count == 0) return -1;
//	REBARBANDINFO rbBand = new REBARBANDINFO ();	
//	rbBand.cbSize = REBARBANDINFO.sizeof;
//	rbBand.fMask = OS.RBBIM_STYLE;
//	for (int i=index + 1; i<count; i++) {
//		OS.SendMessage (handle, OS.RB_GETBANDINFO, i, rbBand);
//		if ((rbBand.fStyle & OS.RBBS_BREAK) != 0) {
//			return i - 1;
//		}
//	}
//	return count - 1;
//}

//boolean isLastItemOfRow (int index) {
//	int count = OS.SendMessage (handle, OS.RB_GETBANDCOUNT, 0, 0);
//	if (index + 1 == count) return true;
//	REBARBANDINFO rbBand = new REBARBANDINFO ();
//	rbBand.cbSize = REBARBANDINFO.sizeof;
//	rbBand.fMask = OS.RBBIM_STYLE;
//	OS.SendMessage (handle, OS.RB_GETBANDINFO, index + 1, rbBand);
//	return (rbBand.fStyle & OS.RBBS_BREAK) != 0;
//}

/**
 * Returns whether or not the receiver is 'locked'. When a coolbar
 * is locked, its items cannot be repositioned.
 *
 * @return true if the coolbar is locked, false otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.0
 */
public boolean getLocked () {
	checkWidget ();
	return ((CCoolBar)handle).isLocked();
}

/**
 * Returns an array of ints that describe the zero-relative
 * indices of any item(s) in the receiver that will begin on
 * a new row. The 0th visible item always begins the first row,
 * therefore it does not count as a wrap index.
 *
 * @return an array containing the receiver's wrap indices, or an empty array if all items are in one row
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int [] getWrapIndices () {
	checkWidget ();
  Component[] components = handle.getComponents();
  ArrayList wrapIndiceList = new ArrayList();
  for(int i=1; i<components.length; i++) {
    if(((JCoolBarItem)components[i]).isWrapped()) {
      if(Compatibility.IS_JAVA_5_OR_GREATER) {
        wrapIndiceList.add(Integer.valueOf(i));
      } else {
        wrapIndiceList.add(new Integer(i));
      }
    }
  }
  int[] indices = new int[wrapIndiceList.size()];
  for(int i=0; i<indices.length; i++) {
    indices[i] = ((Integer)wrapIndiceList.get(i)).intValue();
  }
  return indices;
}

/**
 * Searches the receiver's items in the order they are currently
 * being displayed, starting at the first item (index 0), until
 * an item is found that is equal to the argument, and returns
 * the index of that item. If no item is found, returns -1.
 *
 * @param item the search item
 * @return the visual order index of the search item, or -1 if the item is not found
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the item is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the item is disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int indexOf (CoolItem item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
  Component[] components = handle.getComponents();
  for(int i=0; i<components.length; i++) {
    if(components[i] == item.handle) {
      return i;
    }
  }
  return -1;
}

//void resizeToPreferredWidth (int index) {
//	/*
//	* Bug in Windows.  When RB_GETBANDBORDERS is sent
//	* with an index out of range, Windows GP's.  The
//	* fix is to ensure the index is in range.
//	*/
//	int count = OS.SendMessage(handle, OS.RB_GETBANDCOUNT, 0, 0);
//	if (0 <= index && index < count) {
//		REBARBANDINFO rbBand = new REBARBANDINFO();
//		rbBand.cbSize = REBARBANDINFO.sizeof;
//		rbBand.fMask = OS.RBBIM_IDEALSIZE;
//		OS.SendMessage (handle, OS.RB_GETBANDINFO, index, rbBand);
//		RECT rect = new RECT ();
//		OS.SendMessage (handle, OS.RB_GETBANDBORDERS, index, rect);
//		rbBand.cx = rbBand.cxIdeal + rect.left;
//		if ((style & SWT.FLAT) == 0) rbBand.cx += rect.right;
//		rbBand.fMask = OS.RBBIM_SIZE;
//		OS.SendMessage (handle, OS.RB_SETBANDINFO, index, rbBand);
//	}
//}
//
//void resizeToMaximumWidth (int index) {
//	REBARBANDINFO rbBand = new REBARBANDINFO();
//	rbBand.cbSize = REBARBANDINFO.sizeof;
//	rbBand.fMask = OS.RBBIM_SIZE;
//	rbBand.cx = MAX_WIDTH;
//	OS.SendMessage (handle, OS.RB_SETBANDINFO, index, rbBand);
//}	

Point minimumSize (int wHint, int hHint, boolean changed) {
  java.awt.Dimension size = handle.getPreferredSize();
  return new Point(size.width, size.height);
}

void releaseChildren (boolean destroy) {
  if(itemList != null) {
    for (int i=itemList.size()-1; i>=0; i--) {
      CoolItem item = (CoolItem)itemList.get(i);
      if (item != null && !item.isDisposed ()) {
        item.release (false);
      }
    }
    itemList = null;
    originalItemList = null;
  }
  super.releaseChildren (destroy);
}

void removeControl (Control control) {
  if(isDisposed()) return;
	super.removeControl (control);
  int count = itemList.size();
  for (int i=0; i<count; i++) {
		CoolItem item = (CoolItem)itemList.get(i);
		if (item != null && item.control == control) {
			item.setControl (null);
		}
	}
}

//void setBackgroundPixel (int pixel) {
//	if (background == pixel) return;
//	background = pixel;
//	if (pixel == -1) pixel = defaultBackground ();
//	OS.SendMessage (handle, OS.RB_SETBKCOLOR, 0, pixel);
//	setItemColors (OS.SendMessage (handle, OS.RB_GETTEXTCOLOR, 0, 0), pixel);
//	/*
//	* Feature in Windows.  For some reason, Windows
//	* does not fully erase the coolbar area and coolbar
//	* items when you set the background.  The fix is
//	* to invalidate the coolbar area.
//	*/
//	if (!OS.IsWindowVisible (handle)) return;
//	if (OS.IsWinCE) {
//		OS.InvalidateRect (handle, null, true);
//	} else {
//		int flags = OS.RDW_ERASE | OS.RDW_FRAME | OS.RDW_INVALIDATE | OS.RDW_ALLCHILDREN;
//		OS.RedrawWindow (handle, null, 0, flags);
//	}
//}
//
//void setForegroundPixel (int pixel) {
//	if (foreground == pixel) return;
//	foreground = pixel;
//	if (pixel == -1) pixel = defaultForeground ();
//	OS.SendMessage (handle, OS.RB_SETTEXTCOLOR, 0, pixel);
//	setItemColors (pixel, OS.SendMessage (handle, OS.RB_GETBKCOLOR, 0, 0));
//}
//
//void setItemColors (int foreColor, int backColor) {
//	int count = OS.SendMessage (handle, OS.RB_GETBANDCOUNT, 0, 0);
//	REBARBANDINFO rbBand = new REBARBANDINFO ();
//	rbBand.cbSize = REBARBANDINFO.sizeof;
//	rbBand.fMask = OS.RBBIM_COLORS;
//	rbBand.clrFore = foreColor;
//	rbBand.clrBack = backColor;
//	for (int i=0; i<count; i++) {
//		OS.SendMessage (handle, OS.RB_SETBANDINFO, i, rbBand);
//	}
//}

/**
 * Sets the receiver's item order, wrap indices, and item sizes
 * all at once. This method is typically used to restore the
 * displayed state of the receiver to a previously stored state.
 * <p>
 * The item order is the order in which the items in the receiver
 * should be displayed, given in terms of the zero-relative ordering
 * of when the items were added.
 * </p><p>
 * The wrap indices are the indices of all item(s) in the receiver
 * that will begin on a new row. The indices are given in the order
 * specified by the item order. The 0th item always begins the first
 * row, therefore it does not count as a wrap index. If wrap indices
 * is null or empty, the items will be placed on one line.
 * </p><p>
 * The sizes are specified in an array of points whose x and y
 * coordinates describe the new widths and heights (respectively)
 * of the receiver's items in the order specified by the item order.
 * </p>
 *
 * @param itemOrder an array of indices that describe the new order to display the items in
 * @param wrapIndices an array of wrap indices, or null
 * @param sizes an array containing the new sizes for each of the receiver's items in visual order
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if item order or sizes is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if item order or sizes is not the same length as the number of items</li>
 * </ul>
 */
public void setItemLayout (int [] itemOrder, int [] wrapIndices, Point [] sizes) {
	checkWidget ();
	setRedraw (false);
  Utils.notImplemented();
//	setItemOrder (itemOrder);
//	setWrapIndices (wrapIndices);
//	setItemSizes (sizes);
//	setRedraw (true);
}

///*
// * Sets the order that the items in the receiver should 
// * be displayed in to the given argument which is described
// * in terms of the zero-relative ordering of when the items
// * were added.
// *
// * @param itemOrder the new order to display the items in
// *
// * @exception SWTException <ul>
// *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
// *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
// * </ul>
// * @exception IllegalArgumentException <ul>
// *    <li>ERROR_NULL_ARGUMENT - if the item order is null</li>
// *    <li>ERROR_INVALID_ARGUMENT - if the item order is not the same length as the number of items</li>
// * </ul>
// */
//void setItemOrder (int [] itemOrder) {
//	if (itemOrder == null) error (SWT.ERROR_NULL_ARGUMENT);
//	int itemCount = OS.SendMessage (handle, OS.RB_GETBANDCOUNT, 0, 0);
//	if (itemOrder.length != itemCount) error (SWT.ERROR_INVALID_ARGUMENT);
//	
//	/* Ensure that itemOrder does not contain any duplicates. */
//	boolean [] set = new boolean [itemCount];
//	for (int i=0; i<itemOrder.length; i++) {
//		int index = itemOrder [i];
//		if (index < 0 || index >= itemCount) error (SWT.ERROR_INVALID_RANGE);
//		if (set [index]) error (SWT.ERROR_INVALID_ARGUMENT);
//		set [index] = true;
//	}
//	
//	REBARBANDINFO rbBand = new REBARBANDINFO ();
//	rbBand.cbSize = REBARBANDINFO.sizeof;
//	for (int i=0; i<itemOrder.length; i++) {
//		int id = originalItems [itemOrder [i]].id;
//		int index = OS.SendMessage (handle, OS.RB_IDTOINDEX, id, 0);
//		if (index != i) {
//			int lastItemSrcRow = getLastIndexOfRow (index);
//			int lastItemDstRow = getLastIndexOfRow (i);									
//			if (index == lastItemSrcRow) {
//				resizeToPreferredWidth (index);
//			} 
//			if (i == lastItemDstRow) {
//				resizeToPreferredWidth (i);
//			}	
//			
//			/* Move the item */
//			OS.SendMessage (handle, OS.RB_MOVEBAND, index, i);
//
//			if (index == lastItemSrcRow && index - 1 >= 0) {
//				resizeToMaximumWidth (index - 1);
//			}
//			if (i == lastItemDstRow) {
//				resizeToMaximumWidth (i);
//			}	
//		}	
//	}
//}
//
///*
// * Sets the width and height of the receiver's items to the ones
// * specified by the argument, which is an array of points whose x
// * and y coordinates describe the widths and heights (respectively)
// * in the order in which the items are currently being displayed.
// *
// * @param sizes an array containing the new sizes for each of the receiver's items in visual order
// *
// * @exception IllegalArgumentException <ul>
// *    <li>ERROR_NULL_ARGUMENT - if the array of sizes is null</li>
// *    <li>ERROR_INVALID_ARGUMENT - if the array of sizes is not the same length as the number of items</li>
// * </ul>
// * @exception SWTException <ul>
// *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
// *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
// * </ul>
// */
//void setItemSizes (Point [] sizes) {
//	if (sizes == null) error (SWT.ERROR_NULL_ARGUMENT);
//	int count = OS.SendMessage (handle, OS.RB_GETBANDCOUNT, 0, 0);
//	if (sizes.length != count) error (SWT.ERROR_INVALID_ARGUMENT);
//	REBARBANDINFO rbBand = new REBARBANDINFO ();
//	rbBand.cbSize = REBARBANDINFO.sizeof;
//	rbBand.fMask = OS.RBBIM_ID;
//	for (int i=0; i<count; i++) {
//		OS.SendMessage (handle, OS.RB_GETBANDINFO, i, rbBand);
//		items [rbBand.wID].setSize (sizes [i].x, sizes [i].y);
//	}
//}

/**
 * Sets whether or not the receiver is 'locked'. When a coolbar
 * is locked, its items cannot be repositioned.
 *
 * @param locked lock the coolbar if true, otherwise unlock the coolbar
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.0
 */
public void setLocked (boolean locked) {
	checkWidget ();
  ((CCoolBar)handle).setLocked(locked);
}

/**
 * Sets the indices of all item(s) in the receiver that will
 * begin on a new row. The indices are given in the order in
 * which they are currently being displayed. The 0th item
 * always begins the first row, therefore it does not count
 * as a wrap index. If indices is null or empty, the items
 * will be placed on one line.
 *
 * @param indices an array of wrap indices, or null
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setWrapIndices (int [] indices) {
	checkWidget ();
	if (indices == null) indices = new int [0];
	int count = getItemCount ();
	for (int i=0; i<indices.length; i++) {
		if (indices [i] < 0 || indices [i] >= count) {
			error (SWT.ERROR_INVALID_RANGE);
		}	
	}
  setRedraw (false);
  ((CCoolBar)handle).setWrappedIndices(indices);
	setRedraw (true);
}

//int widgetStyle () {
//	int bits = super.widgetStyle () | OS.CCS_NODIVIDER | OS.CCS_NORESIZE;
//	bits |= OS.RBS_VARHEIGHT | OS.RBS_DBLCLKTOGGLE;
//	if ((style & SWT.FLAT) == 0) bits |= OS.RBS_BANDBORDERS; 
//	return bits;
//}
//
//TCHAR windowClass () {
//	return ReBarClass;
//}
//
//int windowProc () {
//	return ReBarProc;
//}
//
//LRESULT WM_COMMAND (int wParam, int lParam) {
//	/*
//	* Feature in Windows.  When the coolbar window
//	* proc processes WM_COMMAND, it forwards this
//	* message to its parent.  This is done so that
//	* children of this control that send this message 
//	* type to their parent will notify not only
//	* this control but also the parent of this control,
//	* which is typically the application window and
//	* the window that is looking for the message.
//	* If the control did not forward the message, 
//	* applications would have to subclass the control 
//	* window to see the message. Because the control
//	* window is subclassed by SWT, the message
//	* is delivered twice, once by SWT and once when
//	* the message is forwarded by the window proc.
//	* The fix is to avoid calling the window proc 
//	* for this control.
//	*/
//	LRESULT result = super.WM_COMMAND (wParam, lParam);
//	if (result != null) return result;
//	return LRESULT.ZERO;
//}
//
//LRESULT WM_ERASEBKGND (int wParam, int lParam) {
//	LRESULT result = super.WM_ERASEBKGND (wParam, lParam);
//	if (result != null) return result;
//		
//	/*
//	* Feature in Windows.  For some reason, Windows
//	* does not fully erase the area that the cool bar
//	* occupies when the size of the cool bar is larger
//	* than the space occupied by the cool bar items.
//	* The fix is to erase the cool bar background.
//	* 
//	* NOTE: On versions of Windows prior to XP, for
//	* some reason, the cool bar draws separators in
//	* WM_ERASEBKGND.  Therefore it is essential to run
//	* the cool bar window proc after the background has
//	* been erased.
//	* 
//	* On XP, this work around is unnecessary because
//	* the background is drawn using NM_CUSTOMDRAW.
//	*/
//	if (OS.COMCTL32_MAJOR < 6) drawBackground (wParam);
//	return null;
//}
//
//LRESULT WM_NOTIFY (int wParam, int lParam) {
//	/*
//	* Feature in Windows.  When the cool bar window
//	* proc processes WM_NOTIFY, it forwards this
//	* message to its parent.  This is done so that
//	* children of this control that send this message 
//	* type to their parent will notify not only
//	* this control but also the parent of this control,
//	* which is typically the application window and
//	* the window that is looking for the message.
//	* If the control did not forward the message, 
//	* applications would have to subclass the control 
//	* window to see the message. Because the control
//	* window is subclassed by SWT, the message
//	* is delivered twice, once by SWT and once when
//	* the message is forwarded by the window proc.
//	* The fix is to avoid calling the window proc 
//	* for this control.
//	*/
//	LRESULT result = super.WM_NOTIFY (wParam, lParam);
//	if (result != null) return result;
//	return LRESULT.ZERO;
//}
//
//LRESULT WM_SETREDRAW (int wParam, int lParam) {
//	LRESULT result = super.WM_SETREDRAW (wParam, lParam);
//	if (result != null) return result;
//	/*
//	* Feature in Windows.  When redraw is turned off, the rebar
//	* control does not call the default window proc.  This means
//	* that the rebar will redraw and children of the rebar will
//	* also redraw.  The fix is to call both the rebar window proc
//	* and the default window proc.
//	*
//	* NOTE: The rebar control can resize itself in WM_SETREDRAW.
//	* When redraw is turned off by the default window proc, this
//	* can leave pixel corruption in the parent.  The fix is to
//	* detect the size change and damage the previous area in the
//	* parent.
//	* 
//	* NOTE:  In version 6.00 of COMCTL32.DLL, when WM_SETREDRAW
//	* is off, we cannot detect that the size has changed causing
//	* pixel corruption.  The fix is to disallow WM_SETREDRAW by
//	* not running the default window proc or the rebar window
//	* proc.
//	*/
//	if (OS.COMCTL32_MAJOR >= 6) return LRESULT.ZERO;
//	Rectangle rect = getBounds ();		
//	int code = callWindowProc (handle, OS.WM_SETREDRAW, wParam, lParam);
//	OS.DefWindowProc (handle, OS.WM_SETREDRAW, wParam, lParam);
//	if (!rect.equals (getBounds ())) {
//		parent.redraw (rect.x, rect.y, rect.width, rect.height, true);
//	}
//	return new LRESULT (code);
//}
//
//LRESULT WM_SIZE (int wParam, int lParam) {
//	if (ignoreResize) {
//		int code = callWindowProc (handle, OS.WM_SIZE, wParam, lParam);
//		if (code == 0) return LRESULT.ZERO;
//		return new LRESULT (code);
//	}
//	return super.WM_SIZE (wParam, lParam);
//}
//
//LRESULT wmNotifyChild (int wParam, int lParam) {
//	NMHDR hdr = new NMHDR ();
//	OS.MoveMemory (hdr, lParam, NMHDR.sizeof);
//	switch (hdr.code) {
//		case OS.RBN_CHILDSIZE:
//			/*
//			* Bug in Windows.  When Windows sets the size of the rebar band
//			* child and the child is a combo box, the size of the drop down
//			* portion of the combo box is resized to zero.  The fix is to set
//			* the size of the control to the current size after the rebar has
//			* already resized it.  If the control is not a combo, this does
//			* nothing.  If the control is a combo, the drop down portion is
//			* recalculated.
//			*/
//			NMREBARCHILDSIZE lprbcs  = new NMREBARCHILDSIZE ();
//			OS.MoveMemory (lprbcs, lParam, NMREBARCHILDSIZE.sizeof);
//			if (lprbcs.uBand != -1) {
//				CoolItem item = items [lprbcs.wID];
//				Control control = item.control;
//				if (control != null) {
//					int width = lprbcs.rcChild_right - lprbcs.rcChild_left;
//					int height = lprbcs.rcChild_bottom - lprbcs.rcChild_top;
//					control.setBounds (lprbcs.rcChild_left, lprbcs.rcChild_top, width, height);
//				}
//			}
//			break;
//		case OS.RBN_HEIGHTCHANGE:
//			if (!ignoreResize) {
//				Point size = getSize ();
//				int border = getBorderWidth ();
//				int height = OS.SendMessage (handle, OS.RB_GETBARHEIGHT, 0, 0);
//				setSize (size.x, height + (border * 2));
//			}
//			break;
//		case OS.RBN_CHEVRONPUSHED:
//			NMREBARCHEVRON lpnm = new NMREBARCHEVRON ();
//			OS.MoveMemory (lpnm, lParam, NMREBARCHEVRON.sizeof);
//			CoolItem item = items [lpnm.wID];
//			if (item != null) {
//				Event event = new Event();
//				event.detail = SWT.ARROW;
//				event.x = lpnm.left;
//				event.y = lpnm.bottom;
//				item.postEvent (SWT.Selection, event);
//			}
//			break;
//		case OS.NM_CUSTOMDRAW:
//			/*
//			* Bug in Windows.  On versions of Windows prior to XP,
//			* drawing the background color in NM_CUSTOMDRAW erases
//			* the separators.  The fix is to draw the background
//			* in WM_ERASEBKGND.
//			*/
//			if (OS.COMCTL32_MAJOR < 6) break;
//			if (background != -1 || (style & SWT.FLAT) != 0) {
//				NMCUSTOMDRAW nmcd = new NMCUSTOMDRAW ();
//				OS.MoveMemory (nmcd, lParam, NMCUSTOMDRAW.sizeof);
//				switch (nmcd.dwDrawStage) {
//					case OS.CDDS_PREERASE:
//						return new LRESULT (OS.CDRF_NOTIFYPOSTERASE);
//					case OS.CDDS_POSTERASE:
//						drawBackground (nmcd.hdc);
//						break;
//				}
//			}
//			break;
//	}
//	return super.wmNotifyChild (wParam, lParam);
//}
}
