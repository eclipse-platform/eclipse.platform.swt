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
import java.util.EventObject;

import javax.swing.event.ChangeEvent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.internal.swing.CControl;
import org.eclipse.swt.internal.swing.CTabFolder;
import org.eclipse.swt.internal.swing.CTabItem;
import org.eclipse.swt.internal.swing.UIThreadUtils;

/**
 * Instances of this class implement the notebook user interface
 * metaphor.  It allows the user to select a notebook page from
 * set of pages.
 * <p>
 * The item children that may be added to instances of this class
 * must be of type <code>TabItem</code>.
 * <code>Control</code> children are created and then set into a
 * tab item using <code>TabItem#setControl</code>.
 * </p><p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to set a layout on it.
 * </p><p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>TOP, BOTTOM</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles TOP and BOTTOM may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */
public class TabFolder extends Composite {

  ArrayList itemList;
	
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
public TabFolder (Composite parent, int style) {
	super (parent, checkStyle (style));
}

Control [] _getChildren () {
  Component[] children = ((CControl)handle).getClientArea().getComponents();
  if(children.length == 0) {
    return new Control[0];
  }
  ArrayList controlsList = new ArrayList(children.length);
  for(int i=0; i<children.length; i++) {
    Component childComponent = children[i];
    if(childComponent instanceof CTabItem) {
      CTabItem cTabItem = ((CTabItem)childComponent);
      childComponent = cTabItem.getContent();
      if(childComponent != null) {
        Control control = display.getControl(childComponent);
        if (control != null && control != this) {
          controlsList.add(control);
        }
      }
    }
  }
  return (Control[])controlsList.toArray(new Control[0]);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver's selection changes, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * When <code>widgetSelected</code> is called, the item field of the event object is valid.
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
public void addSelectionListener(SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);
	addListener(SWT.Selection,typedListener);
	addListener(SWT.DefaultSelection,typedListener);
}

static int checkStyle (int style) {
	style = checkBits (style, SWT.TOP, SWT.BOTTOM, 0, 0, 0, 0);
	return style & ~(SWT.H_SCROLL | SWT.V_SCROLL);
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

boolean autoAddChildren() {
  return false;
}

void createHandleInit() {
  super.createHandleInit();
  state &= ~(CANVAS | THEME_BACKGROUND);
}

protected Container createHandle () {
  return (Container)CTabFolder.Factory.newInstance(this, style);
}

void createItem (TabItem item, int index) {
  int count = getItemCount();
  if (!(0 <= index && index <= count)) error (SWT.ERROR_INVALID_RANGE);
  itemList.add(index, item);
  handle.add(item.handle, index);
  handle.invalidate();
  handle.validate();
	/*
	* Send a selection event when the item that is added becomes
	* the new selection.  This only happens when the first item
	* is added.
	*/
//	if (getItemCount() == 1) {
//		Event event = new Event ();
//		event.item = item;
//		sendEvent (SWT.Selection, event);
//		// the widget could be destroyed at this point
//	}
}

void createWidget () {
	super.createWidget ();
	itemList = new ArrayList();
}

void destroyItem (TabItem item) {
  int index = indexOf(item);
  if(index != -1) {
    handle.remove(item.handle);
    handle.invalidate();
    handle.validate();
    handle.repaint();
  }
  itemList = null;
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
public TabItem getItem (int index) {
	checkWidget ();
	int count = getItemCount();
	if (!(0 <= index && index < count)) error (SWT.ERROR_INVALID_RANGE);
	return (TabItem)itemList.get(index);
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
 * Returns an array of <code>TabItem</code>s which are the items
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
public TabItem [] getItems () {
	checkWidget ();
  return (TabItem[])itemList.toArray(new TabItem [0]);
}

/**
 * Returns an array of <code>TabItem</code>s that are currently
 * selected in the receiver. An empty array indicates that no
 * items are selected.
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
public TabItem [] getSelection () {
	checkWidget ();
	int index = ((CTabFolder)handle).getSelectedIndex();
	if (index == -1) return new TabItem [0];
	return new TabItem [] {(TabItem)itemList.get(index)};
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
	return ((CTabFolder)handle).getSelectedIndex();
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
public int indexOf (TabItem item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	int count = getItemCount();
	for (int i=0; i<count; i++) {
		if (itemList.get(i) == item) return i;
	}
	return -1;
}

//Point minimumSize (int wHint, int hHint, boolean flushCache) {
//	Control [] children = _getChildren ();
//	int width = 0, height = 0;
//	for (int i=0; i<children.length; i++) {
//		Control child = children [i];
//		int index = 0;	
//		int count = OS.SendMessage (handle, OS.TCM_GETITEMCOUNT, 0, 0);
//		while (index < count) {
//			if (items [index].control == child) break;
//			index++;
//		}
//		if (index == count) {
//			Rectangle rect = child.getBounds ();
//			width = Math.max (width, rect.x + rect.width);
//			height = Math.max (height, rect.y + rect.height);
//		} else {
//			Point size = child.computeSize (wHint, hHint, flushCache);
//			width = Math.max (width, size.x);
//			height = Math.max (height, size.y);
//		}
//	}
//	return new Point (width, height);
//}

void releaseChildren (boolean destroy) {
  if(itemList != null) {
    int count = getItemCount();
    for (int i=0; i<count; i++) {
      TabItem item = (TabItem)itemList.get(i);
      if (!item.isDisposed ()) item.release (false);
    }
    itemList = null;
  }
//	if (imageList != null) {
//		OS.SendMessage (handle, OS.TCM_SETIMAGELIST, 0, 0);
//		display.releaseImageList (imageList);
//	}
//	imageList = null;
  super.releaseChildren (destroy);
}

void removeControl (Control control) {
	super.removeControl (control);
	int count = getItemCount();
	for (int i=0; i<count; i++) {
		TabItem item = (TabItem)itemList.get(i);
		if (item.control == control) item.setControl (null);
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
 * @see #addSelectionListener
 */
public void removeSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

/**
 * Sets the receiver's selection to the given item.
 * The current selected is first cleared, then the new item is
 * selected.
 *
 * @param item the item to select
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the item is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.2
 */
public void setSelection (TabItem item) {
  checkWidget ();
  if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
  setSelection (new TabItem [] {item});
}

/**
 * Sets the receiver's selection to be the given array of items.
 * The current selected is first cleared, then the new items are
 * selected.
 *
 * @param items the array of items
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the items array is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (TabItem [] items) {
	checkWidget ();
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (items.length == 0) {
    ((CTabFolder)handle).setSelectedIndex(-1);
	} else {
    ((CTabFolder)handle).setSelectedIndex(indexOf (items [items.length-1]));
	}
}

/**
 * Selects the item at the given zero-relative index in the receiver. 
 * If the item at the index was already selected, it remains selected.
 * The current selection is first cleared, then the new items are
 * selected. Indices that are out of range are ignored.
 *
 * @param index the index of the item to select
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (int index) {
	checkWidget ();
	int count = getItemCount();
	if (!(0 <= index && index < count)) return;
  ((CTabFolder)handle).setSelectedIndex(index);
}

Point minimumSize (int wHint, int hHint, boolean changed) {
  java.awt.Dimension size = handle.getPreferredSize();
  return new Point(size.width, size.height);
}

public void processEvent(EventObject e) {
  if(e instanceof ChangeEvent) {
    if(!hooks(SWT.Selection)) { super.processEvent(e); return; }
  } else { super.processEvent(e); return; }
  UIThreadUtils.startExclusiveSection(getDisplay());
  if(isDisposed()) {
    UIThreadUtils.stopExclusiveSection();
    super.processEvent(e);
    return;
  }
  try {
    if(e instanceof ChangeEvent) {
//      ChangeEvent ce = (ChangeEvent)e;
      int index = ((CTabFolder)handle).getSelectedIndex();
      Event event = new Event();
      if(index >= 0) {
        event.item = (Widget)itemList.get(index);
      }
      sendEvent(SWT.Selection, event);
    }
    super.processEvent(e);
  } catch(Throwable t) {
    UIThreadUtils.storeException(t);
  } finally {
    UIThreadUtils.stopExclusiveSection();
  }
}

}
