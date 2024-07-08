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
import java.awt.Point;
import java.awt.event.MouseEvent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.swing.CSash;
import org.eclipse.swt.internal.swing.UIThreadUtils;

/**
 * Instances of the receiver represent a selectable user interface object
 * that allows the user to drag a rubber banded outline of the sash within
 * the parent control.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>HORIZONTAL, VERTICAL, SMOOTH</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles HORIZONTAL and VERTICAL may be specified.
 * </p><p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 */
public class Sash extends Control {
//	final static int INCREMENT = 1;
//	final static int PAGE_INCREMENT = 9;

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
 * @see SWT#HORIZONTAL
 * @see SWT#VERTICAL
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Sash (Composite parent, int style) {
	super (parent, checkStyle (style));
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is selected, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * When <code>widgetSelected</code> is called, the x, y, width, and height fields of the event object are valid.
 * If the receiver is being dragged, the event object detail field contains the value <code>SWT.DRAG</code>.
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
	return checkBits (style, SWT.HORIZONTAL, SWT.VERTICAL, 0, 0, 0, 0);
}

void createHandleInit() {
  super.createHandleInit();
  state |= THEME_BACKGROUND;
}

Container createHandle () {
  return (Container)CSash.Factory.newInstance(this, style);
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

Point origin;
Point targetPoint;

public void processEvent(AWTEvent e) {
  int id = e.getID();
  switch(id) {
  case MouseEvent.MOUSE_PRESSED: if(!hooks(SWT.Selection)) { super.processEvent(e); return; } break;
  case MouseEvent.MOUSE_DRAGGED: if(!hooks(SWT.Selection)) { super.processEvent(e); return; } break;
  case MouseEvent.MOUSE_RELEASED: if(!hooks(SWT.Selection)) { super.processEvent(e); return; } break;
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
    case MouseEvent.MOUSE_PRESSED: {
      origin = ((MouseEvent)e).getPoint();
      Event event = new Event();
      Rectangle bounds = getBounds();
      event.x = bounds.x;
      event.y = bounds.y;
      event.width = bounds.width;
      event.height = bounds.height;
      targetPoint = new Point(bounds.x, bounds.y);
      sendEvent(SWT.Selection, event);
      break;
    }
    case MouseEvent.MOUSE_RELEASED: {
      Event event = new Event();
      Rectangle bounds = getBounds();
      event.x = targetPoint.x;
      event.y = targetPoint.y;
      event.width = bounds.width;
      event.height = bounds.height;
//        if ((style & SWT.SMOOTH) != 0) {
//          event.detail = SWT.DRAG;
//        }
      sendEvent(SWT.Selection, event);
      origin = null;
      targetPoint = null;
      break;
    }
    case MouseEvent.MOUSE_DRAGGED:
      Event event = new Event();
      MouseEvent me = (MouseEvent)e;
      java.awt.Dimension size = handle.getParent().getSize();
      Rectangle bounds = getBounds();
      event.x = bounds.x;
      event.y = bounds.y;
      if((style & SWT.VERTICAL) != 0) {
        event.x += me.getX() - origin.x;
        if(event.x < 0) {
          event.x = 0;
        } else if(event.x >= size.width - bounds.width) {
          event.x = size.width - bounds.width;
        }
      } else {
        event.y += me.getY() - origin.y;
        if(event.y < 0) {
          event.y = 0;
        } else if(event.y >= size.height - bounds.height) {
          event.y = size.height - bounds.height;
        }
      }
      event.width = bounds.width;
      event.height = bounds.height;
      if ((style & SWT.SMOOTH) == 0) {
        event.detail = SWT.DRAG;
      }
      sendEvent(SWT.Selection, event);
      if (event.doit) {
        targetPoint.x = event.x;
        targetPoint.y = event.y;
        ((CSash)handle).setDragLocation((style & SWT.VERTICAL) != 0? targetPoint.x: targetPoint.y);
//          if ((style & SWT.SMOOTH) != 0) {
//            setLocation(targetPoint.x, targetPoint.y);
//          }
      }
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
