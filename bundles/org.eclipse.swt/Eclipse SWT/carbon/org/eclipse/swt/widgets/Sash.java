package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.internal.carbon.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

/**
 * Instances of the receiver represent a selectable user interface object
 * that allows the user to drag a rubber banded outline of the sash within
 * the parent control.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>HORIZONTAL, VERTICAL</dd>
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
public /*final*/ class Sash extends Control {
	boolean dragging;
	int startX, startY, lastX, lastY;

	private static int H_ARROW;
	private static int V_ARROW;
	
	static {
		short[] h= new short[] {
				(short) 0x0300,
			 	(short) 0x0300,
				(short) 0x0300,
			 	(short) 0x0300,
			 	(short) 0x0300,
			 	(short) 0x2310,
			 	(short) 0x6318,
			 	(short) 0xFB7C,
			 	(short) 0x6318,
			 	(short) 0x2310,
			 	(short) 0x0300,
			 	(short) 0x0300,
			 	(short) 0x0300,
			 	(short) 0x0300,
			 	(short) 0x0300,
			 	(short) 0x0000
			};
		H_ARROW= OS.NewCursor((short)6, (short)7, h, h);
		
		h= new short[] {
				(short) 0x0100,
			 	(short) 0x0380,
				(short) 0x07C0,
			 	(short) 0x0100,
			 	(short) 0x0100,
			 	(short) 0x0000,
			 	(short) 0xFFFE,
			 	(short) 0xFFFE,
			 	(short) 0x0000,
			 	(short) 0x0100,
			 	(short) 0x0100,
			 	(short) 0x07C0,
			 	(short) 0x0380,
			 	(short) 0x0100,
			 	(short) 0x0000,
			 	(short) 0x0000
			};
		V_ARROW= OS.NewCursor((short)7, (short)6, h, h);
	}

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
 * If the reciever is being dragged, the event object detail field contains the value <code>SWT.DRAG</code>.
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
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);
	addListener(SWT.Selection,typedListener);
	addListener(SWT.DefaultSelection,typedListener);
}
static int checkStyle (int style) {
	return checkBits (style, SWT.HORIZONTAL, SWT.VERTICAL, 0, 0, 0, 0);
}
public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	int border = getBorderWidth ();
	int width = border * 2, height = border * 2;
	if ((style & SWT.HORIZONTAL) != 0) {
		width += DEFAULT_WIDTH;  height += 3;
	} else {
		width += 3; height += DEFAULT_HEIGHT;
	}
	if (wHint != SWT.DEFAULT) width = wHint + (border * 2);
	if (hHint != SWT.DEFAULT) height = hHint + (border * 2);
	return new Point (width, height);
}
void createHandle (int index) {
	state |= HANDLE;
	int border = (style & SWT.BORDER) != 0 ? 1 : 0;
    /* AW
	int [] argList = {
		OS.XmNborderWidth, border,
		OS.XmNmarginWidth, 0,
		OS.XmNmarginHeight, 0,
		OS.XmNresizePolicy, OS.XmRESIZE_NONE,
		OS.XmNancestorSensitive, 1,
	};
	*/
	handle = MacUtil.createDrawingArea(parent.handle, -1, true, 0, 0, border);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
}
int defaultBackground () {
	return getDisplay ().labelBackground;
}
void drawBand (int x, int y, int width, int height) {

	MacRect bounds= new MacRect();
	OS.GetControlBounds(parent.handle, bounds.getData());
	x+= bounds.getX();
	y+= bounds.getY();

	int port= OS.GetPort();
	OS.SetPortWindowPort(OS.GetControlOwner(handle));
	OS.InvertRect((short)x, (short)y, (short)width, (short)height);
	OS.SetPort(port);
}
void hookEvents () {
	super.hookEvents ();
	Display display= getDisplay();		
	OS.SetControlData(handle, OS.kControlEntireControl, OS.kControlUserPaneDrawProcTag, display.fUserPaneDrawProc);
	OS.SetControlData(handle, OS.kControlEntireControl, OS.kControlUserPaneHitTestProcTag, display.fUserPaneHitTestProc);
}

int processMouseDown (Object callData) {
	super.processMouseDown (callData);

	MacEvent mEvent= (MacEvent) callData;
	Point mp= MacUtil.toControl(parent.handle, mEvent.getWhere2());
	startX = mp.x;  startY = mp.y;

	MacRect bounds= new MacRect();
	OS.GetControlBounds(handle, bounds.getData());
	int width = bounds.getWidth(), height = bounds.getHeight();
	
	MacRect parentBounds= new MacRect();
	OS.GetControlBounds(parent.handle, parentBounds.getData());
	
	lastX = bounds.getX()-parentBounds.getX();
	lastY = bounds.getY()-parentBounds.getY();
	
	Event event = new Event ();
	event.detail = SWT.DRAG;
	//event.time = xEvent.time;
	event.x = lastX;  event.y = lastY;
	event.width = width;  event.height = height;
	sendEvent (SWT.Selection, event);
	if (event.doit) {
		dragging = true;
		drawBand (lastX = event.x, lastY = event.y, width, height);
	}
	return 0;
}
int processMouseMove (Object callData) {
	super.processMouseMove (callData);
	
	getDisplay().setCursor((style & SWT.VERTICAL) != 0 ? H_ARROW : V_ARROW);

	MacEvent mEvent= (MacEvent) callData;

	if (!dragging || (mEvent.getButton() != 1)) return 0;
	Point mp= MacUtil.toControl(parent.handle, mEvent.getWhere2());

	MacRect bounds= new MacRect();
	OS.GetControlBounds(handle, bounds.getData());
	int width = bounds.getWidth(), height = bounds.getHeight();
	
	MacRect parentBounds= new MacRect();
	OS.GetControlBounds(parent.handle, parentBounds.getData());

	int x = bounds.getX()-parentBounds.getX(), y = bounds.getY()-parentBounds.getY();

	int newX = lastX, newY = lastY;
	if ((style & SWT.VERTICAL) != 0) {
		newX = Math.min (Math.max (0, x + (mp.x - startX)), parentBounds.getWidth() - width);
	} else {
		newY = Math.min (Math.max (0, y + (mp.y - startY)), parentBounds.getHeight() - height);
	}
	if (newX == lastX && newY == lastY) return 0;
	drawBand (lastX, lastY, width, height);
	Event event = new Event ();
	event.detail = SWT.DRAG;
	//event.time = xEvent.time;
	event.x = newX;  event.y = newY;
	event.width = width;  event.height = height;
	sendEvent (SWT.Selection, event);
	if (event.doit) {
		lastX = event.x;  lastY = event.y;
		drawBand (lastX, lastY, width, height);
	}
	return 0;
}
int processMouseUp (Object callData) {
	super.processMouseUp (callData);

	MacEvent mEvent= (MacEvent) callData;

	if (mEvent.getButton() != 1) return 0;
	if (!dragging) return 0;
	dragging = false;

	MacRect bounds= new MacRect();
	OS.GetControlBounds(handle, bounds.getData());
	int width = bounds.getWidth(), height = bounds.getHeight();

	Event event = new Event ();
	//event.time = xEvent.time;
	event.x = lastX;  event.y = lastY;
	event.width = width;  event.height = height;
	drawBand (lastX, lastY, width, height);
	sendEvent (SWT.Selection, event);
	return 0;
}
int processPaint (Object callData) {
	
	GC gc= new GC(this);
	MacControlEvent me= (MacControlEvent) callData;
	Rectangle r= gc.carbon_focus(me.getDamageRegionHandle());
	
	if (! r.isEmpty()) {
		Point e= getSize();
		
		// erase background
		gc.fillRectangle(0, 0, e.x, e.y);
		
		gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_GRAY));
		if (e.x < e.y) {	// vertical
			gc.fillRectangle ((e.x-1)/2, (e.y-20)/2, 1, 20);
		} else {			// horizontal
			gc.fillRectangle ((e.x-20)/2, (e.y-1)/2, 20, 1);
		}
	}
	
	gc.carbon_unfocus();
	gc.dispose();
	
	return 0;
}
/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is selected.
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
 * @see #addSelectionListener
 */
public void removeSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Selection, listener);
	eventTable.unhook(SWT.DefaultSelection,listener);
}
}
