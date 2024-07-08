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

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.ExpandAdapter;
import org.eclipse.swt.events.ExpandEvent;
import org.eclipse.swt.events.ExpandListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.internal.swing.CControl;
import org.eclipse.swt.internal.swing.CExpandBar;
import org.eclipse.swt.internal.swing.CExpandItem;
import org.eclipse.swt.internal.swing.JExpandPane.JExpandPaneItem;

/**
 * Instances of this class support the layout of selectable
 * expand bar items.
 * <p>
 * The item children that may be added to instances of this class
 * must be of type <code>ExpandItem</code>.
 * </p><p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>V_SCROLL</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Expand, Collapse</dd>
 * </dl>
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * 
 * @see ExpandItem
 * @see ExpandEvent
 * @see ExpandListener
 * @see ExpandAdapter
 * 
 * @since 3.2
 */
public class ExpandBar extends Composite {
  
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
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public ExpandBar (Composite parent, int style) {
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
    if(childComponent instanceof JExpandPaneItem) {
      childComponent = ((JExpandPaneItem)childComponent).getComponent();
      if(childComponent != null && childComponent instanceof CExpandItem) {
        childComponent = ((CExpandItem)childComponent).getContent();
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
 * be notified when an item in the receiver is expanded or collapsed
 * by sending it one of the messages defined in the <code>ExpandListener</code>
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
 * @see ExpandListener
 * @see #removeExpandListener
 */
public void addExpandListener (ExpandListener listener) {
  checkWidget ();
  if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
  TypedListener typedListener = new TypedListener (listener);
  addListener (SWT.Expand, typedListener);
  addListener (SWT.Collapse, typedListener);
}

protected void checkSubclass () {
  if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

static int checkStyle (int style) {
  style &= ~SWT.H_SCROLL;
  return style | SWT.NO_BACKGROUND;
}

boolean autoAddChildren() {
  return false;
}

void createHandleInit() {
  super.createHandleInit();
  state &= ~CANVAS;
}

protected Container createHandle () {
  return (Container)CExpandBar.Factory.newInstance(this, style);
}

void createItem (ExpandItem item, int style, int index) {
  int count = getItemCount();
  if (!(0 <= index && index <= count)) error (SWT.ERROR_INVALID_RANGE);
  itemList.add(index, item);
  ((CExpandBar)handle).insertExpandPaneItem(null, null, item.handle, index);
  handle.invalidate();
  handle.validate();
}

void createWidget () {
  super.createWidget ();
  itemList = new ArrayList();
}

void destroyItem (ExpandItem item) {
  int index = indexOf(item);
  if(index != -1) {
    ((CExpandBar)handle).removeExpandPaneItem(item.handle);
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
public ExpandItem getItem (int index) {
  checkWidget ();
  int count = getItemCount();
  if (!(0 <= index && index < count)) error (SWT.ERROR_INVALID_RANGE);
  return (ExpandItem)itemList.get(index);
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
 * Returns an array of <code>ExpandItem</code>s which are the items
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
public ExpandItem [] getItems () {
  checkWidget ();
  return (ExpandItem[])itemList.toArray(new ExpandItem [0]);
}

/**
 * Returns the receiver's spacing.
 *
 * @return the spacing
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getSpacing () {
  checkWidget ();
  return ((CExpandBar)handle).getSpacing();
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
 *    <li>ERROR_NULL_ARGUMENT - if the item is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the item has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int indexOf (ExpandItem item) {
  checkWidget ();
  if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
  int count = getItemCount();
  for (int i=0; i<count; i++) {
    if (itemList.get(i) == item) return i;
  }
  return -1;
}

Point minimumSize (int wHint, int hHint, boolean changed) {
  java.awt.Dimension size = handle.getPreferredSize();
  int width = ((CExpandBar)handle).getVerticalScrollBar().getPreferredSize().width;
  return new Point(size.width + width, size.height);
}

void releaseChildren (boolean destroy) {
  if(itemList != null) {
    int count = getItemCount();
    for (int i=0; i<count; i++) {
      ExpandItem item = (ExpandItem)itemList.get(i);
      if (!item.isDisposed ()) item.release (false);
    }
    itemList = null;
  }
  super.releaseChildren (destroy);
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
 * @see ExpandListener
 * @see #addExpandListener
 */
public void removeExpandListener (ExpandListener listener) {
  checkWidget ();
  if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
  if (eventTable == null) return;
  eventTable.unhook (SWT.Expand, listener);
  eventTable.unhook (SWT.Collapse, listener); 
}

/**
 * Sets the receiver's spacing. Spacing specifies the number of pixels allocated around 
 * each item.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSpacing (int spacing) {
  checkWidget ();
  if (spacing < 0) return;
  ((CExpandBar)handle).setSpacing(spacing);
}

}