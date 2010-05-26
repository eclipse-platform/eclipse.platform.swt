/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

/**
 * Instances of this class represent a selectable user interface object
 * that represents a button in a tool bar.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>PUSH, CHECK, RADIO, SEPARATOR, DROP_DOWN</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles CHECK, PUSH, RADIO, SEPARATOR and DROP_DOWN 
 * may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#toolbar">ToolBar, ToolItem snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class ToolItem extends Item {
	ToolBar parent;
	Control control;
	String toolTipText;
	int toolTipHandle;
	Image hotImage, disabledImage;
	int button, arrow;

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>ToolBar</code>) and a style value
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
 * @see SWT#PUSH
 * @see SWT#CHECK
 * @see SWT#RADIO
 * @see SWT#SEPARATOR
 * @see SWT#DROP_DOWN
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public ToolItem (ToolBar parent, int style) {
	super (parent, checkStyle (style));
	this.parent = parent;
	parent.createItem (this, parent.getItemCount ());
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>ToolBar</code>), a style value
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
 * @see SWT#PUSH
 * @see SWT#CHECK
 * @see SWT#RADIO
 * @see SWT#SEPARATOR
 * @see SWT#DROP_DOWN
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public ToolItem (ToolBar parent, int style, int index) {
	super (parent, checkStyle (style));
	this.parent = parent;
	parent.createItem (this, index);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is selected by the user, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * When <code>widgetSelected</code> is called when the mouse is over the arrow portion of a drop-down tool,
 * the event object detail field contains the value <code>SWT.ARROW</code>.
 * <code>widgetDefaultSelected</code> is not called.
 * </p>
 * <p>
 * When the <code>SWT.RADIO</code> style bit is set, the <code>widgetSelected</code> method is
 * also called when the receiver loses selection because another item in the same radio group 
 * was selected by the user. During <code>widgetSelected</code> the application can use
 * <code>getSelection()</code> to determine the current selected state of the receiver.
 * </p>
 *
 * @param listener the listener which should be notified when the control is selected by the user,
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
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

static int checkStyle (int style) {
	return checkBits (style, SWT.PUSH, SWT.CHECK, SWT.RADIO, SWT.SEPARATOR, SWT.DROP_DOWN, 0);
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

void click () {
	click (handle);
}

int createArrowImage () {
	short width = 5;
	short height = 4;
	int image = OS.PhCreateImage(null, width, height, OS.Pg_IMAGE_DIRECT_888, 0, 0, 0);
	if (image == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	PhDim_t dim = new PhDim_t();
	dim.w = width;
	dim.h = height;
	int mc = OS.PmMemCreateMC(image, dim, new PhPoint_t());
	if (mc == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	int prevContext = OS.PmMemStart(mc);
	OS.PgSetFillColor(0xFFFFFF);
	OS.PgDrawIRect(0, 0, width, height, OS.Pg_DRAW_FILL);
	OS.PgSetStrokeColor(0x000000);
	OS.PgSetFillColor(0x000000);
	short [] points = {(short)0, (short)1, (short)2, (short)3, (short)4, (short)1};
	OS.PgDrawPolygon(points, points.length / 2,	new PhPoint_t(), OS.Pg_DRAW_FILL | OS.Pg_DRAW_STROKE | OS.Pg_CLOSED);
	OS.PmMemFlush(mc, image);
	OS.PmMemStop(mc);
	OS.PmMemReleaseMC(mc);
	OS.PhDCSetCurrent(prevContext);
	OS.PhMakeTransBitmap(image, 0xFFFFFF);
	return image;
}

void createHandle (int index) {
	state |= HANDLE;
	int count = parent.getItemCount();
	if (!(0 <= index && index <= count)) error (SWT.ERROR_INVALID_RANGE);
	int parentHandle = parent.handle;
	
	if ((style & SWT.SEPARATOR) != 0) {
		int [] args = {
//			OS.Pt_ARG_SEP_FLAGS, OS.Pt_SEP_VERTICAL, OS.Pt_SEP_VERTICAL | OS.Pt_SEP_HORIZONTAL,
//			OS.Pt_ARG_SEP_TYPE, OS.Pt_NOLINE, 0,
			OS.Pt_ARG_WIDTH, 2, 0,
			OS.Pt_ARG_RESIZE_FLAGS, OS.Pt_RESIZE_Y_ALWAYS, OS.Pt_RESIZE_XY_BITS,
		};		
		handle = OS.PtCreateWidget (OS.PtContainer (), parentHandle, args.length / 3, args);
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	} else if ((style & SWT.DROP_DOWN) != 0) {
		int [] args =  {
			OS.Pt_ARG_GROUP_ORIENTATION, OS.Pt_GROUP_HORIZONTAL, 0,
			OS.Pt_ARG_GROUP_FLAGS, OS.Pt_GROUP_EQUAL_SIZE_VERTICAL, OS.Pt_GROUP_EQUAL_SIZE_VERTICAL,
		};
		handle = OS.PtCreateWidget (OS.PtGroup (), parentHandle, args.length / 3, args);
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		boolean rightAligned = (parent.style & SWT.RIGHT) != 0;
		args =  new int [] {
			OS.Pt_ARG_LABEL_TYPE, 0, 0,
			OS.Pt_ARG_FLAGS, 0, OS.Pt_GETS_FOCUS,
			OS.Pt_ARG_BALLOON_POSITION, rightAligned ? OS.Pt_BALLOON_RIGHT : OS.Pt_BALLOON_BOTTOM, 0,
			OS.Pt_ARG_BASIC_FLAGS, 0, OS.Pt_RIGHT_ETCH | OS.Pt_RIGHT_OUTLINE,
		};
		button = OS.PtCreateWidget (OS.PtButton (), handle, args.length / 3, args);
		if (button == 0) error (SWT.ERROR_NO_HANDLES);
		int arrowImage = createArrowImage ();
		args =  new int [] {
			OS.Pt_ARG_FLAGS, 0, OS.Pt_GETS_FOCUS,
			OS.Pt_ARG_LABEL_IMAGE, arrowImage, 0,
			OS.Pt_ARG_LABEL_TYPE, OS.Pt_IMAGE, 0,
			OS.Pt_ARG_MARGIN_WIDTH, 1, 0,
			OS.Pt_ARG_BASIC_FLAGS, 0, OS.Pt_LEFT_ETCH | OS.Pt_LEFT_OUTLINE,
		};
		arrow = OS.PtCreateWidget (OS.PtButton (), handle, args.length / 3, args);
		OS.free (arrowImage);
		if (arrow == 0) error (SWT.ERROR_NO_HANDLES);
	} else {
		boolean rightAligned = (parent.style & SWT.RIGHT) != 0;
		boolean toggle = (style & (SWT.CHECK | SWT.RADIO)) != 0;
		int [] args = {
			OS.Pt_ARG_LABEL_TYPE, 0, 0,
			OS.Pt_ARG_FLAGS, 0, OS.Pt_GETS_FOCUS,
			OS.Pt_ARG_BALLOON_POSITION, rightAligned ? OS.Pt_BALLOON_RIGHT : OS.Pt_BALLOON_BOTTOM, 0,
			OS.Pt_ARG_FLAGS, toggle ? OS.Pt_TOGGLE : 0, OS.Pt_TOGGLE,
		};
		handle = button = OS.PtCreateWidget (OS.PtButton (), parentHandle, args.length / 3, args);
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	}
	if (index != count) {
		int i = 0;
		int child = OS.PtWidgetChildBack (parentHandle);
		/*
		* Feature in Photon.  Tool bars have an extra widget which
		* is the parent of all tool items. PtValidParent() can not be
		* used, since it does not return that widget.
		*/
		if (child != 0) child = OS.PtWidgetChildBack (child);
		while (i != index && child != 0) {
			child = OS.PtWidgetBrotherInFront (child);
			i++;
		}
		OS.PtWidgetInsert (topHandle (), child, 1);
	}
	if (OS.PtWidgetIsRealized (parentHandle)) {
		OS.PtRealizeWidget (topHandle ());
	}
}

void createWidget (int index) {
	super.createWidget (index);
	setDefaultFont ();
}

void deregister () {
	super.deregister ();
	if ((style & SWT.DROP_DOWN) != 0) {
		WidgetTable.remove (button);
		WidgetTable.remove (arrow);
	}
}

void destroyWidget () {
	parent.destroyItem (this);
	super.destroyWidget ();
}

/**
 * Returns a rectangle describing the receiver's size and location
 * relative to its parent.
 *
 * @return the receiver's bounding rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Rectangle getBounds () {
	checkWidget();
	int topHandle = topHandle ();
	PhArea_t area = new PhArea_t ();
	OS.PtWidgetArea (topHandle, area);
	int x = area.pos_x, y = area.pos_y;
	int width = area.size_w, height = area.size_h;
	/* Check if the item is scrolled */
	int child = OS.PtWidgetChildBack (parent.handle);
	if (child != 0) {
		OS.PtWidgetArea (child, area);
		x += area.pos_x;
		y += area.pos_y;
	}
	return new Rectangle (x, y, width, height);
}

/**
 * Returns the control that is used to fill the bounds of
 * the item when the item is a <code>SEPARATOR</code>.
 *
 * @return the control
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Control getControl () {
	checkWidget();
	return control;
}

/**
 * Returns the receiver's disabled image if it has one, or null
 * if it does not.
 * <p>
 * The disabled image is displayed when the receiver is disabled.
 * </p>
 *
 * @return the receiver's disabled image
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Image getDisabledImage () {
	checkWidget();
	return disabledImage;
}

/**
 * Returns <code>true</code> if the receiver is enabled, and
 * <code>false</code> otherwise. A disabled control is typically
 * not selectable from the user interface and draws with an
 * inactive or "grayed" look.
 *
 * @return the receiver's enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #isEnabled
 */
public boolean getEnabled () {
	checkWidget ();
	int topHandle = topHandle ();
	return (OS.PtWidgetFlags (topHandle) & OS.Pt_BLOCKED) == 0;
}

/**
 * Returns the receiver's hot image if it has one, or null
 * if it does not.
 * <p>
 * The hot image is displayed when the mouse enters the receiver.
 * </p>
 *
 * @return the receiver's hot image
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Image getHotImage () {
	checkWidget();
	return hotImage;
}

/**
 * Returns the receiver's parent, which must be a <code>ToolBar</code>.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public ToolBar getParent () {
	checkWidget();
	return parent;
}

/**
 * Returns <code>true</code> if the receiver is selected,
 * and false otherwise.
 * <p>
 * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>,
 * it is selected when it is checked (which some platforms draw as a
 * pushed in button). If the receiver is of any other type, this method
 * returns false.
 * </p>
 *
 * @return the selection state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getSelection () {
	checkWidget();
	if ((style & (SWT.CHECK | SWT.RADIO | SWT.TOGGLE)) == 0) return false;
	return (OS.PtWidgetFlags (handle) & OS.Pt_SET) != 0;
}

/**
 * Returns the receiver's tool tip text, or null if it has not been set.
 *
 * @return the receiver's tool tip text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getToolTipText () {
	checkWidget();
	return toolTipText;
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
	checkWidget();
	int topHandle = topHandle ();
	int [] args = {OS.Pt_ARG_WIDTH, 0, 0};
	OS.PtGetResources (topHandle, args.length / 3, args);
	return args [1];
}

boolean hasFocus () {
	return OS.PtIsFocused (handle) != 0;
}

void hookEvents () {
	super.hookEvents ();
	if ((style & SWT.SEPARATOR) != 0) return;
	int windowProc = display.windowProc;
	OS.PtAddEventHandler (handle, OS.Ph_EV_BOUNDARY, windowProc, OS.Ph_EV_BOUNDARY);	
	OS.PtAddCallback (button, OS.Pt_CB_ACTIVATE, windowProc, OS.Pt_CB_ACTIVATE);
	if ((style & SWT.DROP_DOWN) != 0) {
		OS.PtAddCallback (arrow, OS.Pt_CB_ACTIVATE, windowProc, OS.Pt_CB_ACTIVATE);
	}
	OS.PtAddCallback (handle, OS.Pt_CB_LOST_FOCUS, windowProc,  OS.Pt_CB_LOST_FOCUS);
}

int hotkeyProc (int widget, int data, int info) {
	if (setFocus ()) click ();
	return OS.Pt_CONTINUE;
}

/**
 * Returns <code>true</code> if the receiver is enabled and all
 * of the receiver's ancestors are enabled, and <code>false</code>
 * otherwise. A disabled control is typically not selectable from the
 * user interface and draws with an inactive or "grayed" look.
 *
 * @return the receiver's enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #getEnabled
 */
public boolean isEnabled () {
	checkWidget();
	return getEnabled () && parent.isEnabled ();
}

int Ph_EV_BOUNDARY (int widget, int info) {
	if (info == 0) return OS.Pt_END;
	PtCallbackInfo_t cbinfo = new PtCallbackInfo_t ();
	OS.memmove (cbinfo, info, PtCallbackInfo_t.sizeof);
	if (cbinfo.event == 0) return OS.Pt_END;
	PhEvent_t ev = new PhEvent_t ();
	OS.memmove (ev, cbinfo.event, PhEvent_t.sizeof);
	switch ((int) ev.subtype) {
		case OS.Ph_EV_PTR_STEADY:
			int [] args = {OS.Pt_ARG_TEXT_FONT, 0, 0};
			OS.PtGetResources (button, args.length / 3, args);
			int length = OS.strlen (args [1]);
			byte [] font = new byte [length + 1];
			OS.memmove (font, args [1], length);
			destroyToolTip (toolTipHandle);
			toolTipHandle = createToolTip (toolTipText, button, font);
			break;
		case OS.Ph_EV_PTR_UNSTEADY:
			destroyToolTip (toolTipHandle);
			toolTipHandle = 0;
			break;		
	}
	return OS.Pt_END;
}

int Pt_CB_ACTIVATE (int widget, int info) {
	Event event = new Event ();
	if (widget == arrow) {
		event.detail = SWT.ARROW;
		int topHandle = topHandle ();
		PhArea_t area = new PhArea_t ();
		OS.PtWidgetArea (topHandle, area);
		event.x = area.pos_x;
		event.y = area.pos_y + area.size_h;
	} else {
		if ((style & SWT.RADIO) != 0) {
			if ((parent.getStyle () & SWT.NO_RADIO_GROUP) == 0) {
				selectRadio ();
			}
		}
	}
	postEvent (SWT.Selection, event);
	return OS.Pt_CONTINUE;
}

int Pt_CB_LOST_FOCUS (int widget, int info) {
	parent.lastFocus = this;
	return OS.Pt_CONTINUE;
}

void register () {
	super.register ();
	if ((style & SWT.DROP_DOWN) != 0) {
		WidgetTable.put (button, this);
		WidgetTable.put (arrow, this);
	}
}

void releaseHandle () {
	super.releaseHandle ();
	arrow = button = 0;
	parent = null;
}

void releaseWidget () {
	// reparent the control back to the toolbar
	if (control != null) setControl (null);
	super.releaseWidget ();
	if (toolTipHandle != 0) destroyToolTip (toolTipHandle);
	if (parent.lastFocus == this) parent.lastFocus = null;
	toolTipHandle = 0;
	control = null;
	hotImage = null;
	disabledImage = null;
	toolTipText = null;
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is selected by the user.
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
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

void selectRadio () {
	int index = 0;
	ToolItem [] items = parent.getItems ();
	while (index < items.length && items [index] != this) index++;
	int i = index - 1;
	while (i >= 0 && items [i].setRadioSelection (false)) --i;
	int j = index + 1;
	while (j < items.length && items [j].setRadioSelection (false)) j++;
	setSelection (true);
}

void setBackgroundPixel (int pixel) {
	OS.PtSetResource (handle, OS.Pt_ARG_FILL_COLOR, pixel, 0);
	if ((style & SWT.DROP_DOWN) != 0) {
		OS.PtSetResource (button, OS.Pt_ARG_FILL_COLOR, pixel, 0);
		OS.PtSetResource (arrow, OS.Pt_ARG_FILL_COLOR, pixel, 0);
	}
}

/**
 * Sets the control that is used to fill the bounds of
 * the item when the item is a <code>SEPARATOR</code>.
 *
 * @param control the new control
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the control has been disposed</li> 
 *    <li>ERROR_INVALID_PARENT - if the control is not in the same widget tree</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setControl (Control control) {
	checkWidget();
	if (control != null) {
		if (control.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
		if (control.parent != parent) error (SWT.ERROR_INVALID_PARENT);
	}
	if ((style & SWT.SEPARATOR) == 0) return;
	Control oldControl = this.control;
	this.control = control;
	if (oldControl != null) {
		OS.PtReParentWidget(oldControl.handle, parent.parentingHandle ());
	}
	if (control != null && !control.isDisposed ()) {
		OS.PtReParentWidget(control.handle, handle);
		control.setBounds (getBounds ());
	}
}

void setDefaultFont () {
	if (display.defaultFont != null) setFont (parent.defaultFont ());
}

/**
 * Sets the receiver's disabled image to the argument, which may be
 * null indicating that no disabled image should be displayed.
 * <p>
 * The disabled image is displayed when the receiver is disabled.
 * </p>
 *
 * @param image the disabled image to display on the receiver (may be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setDisabledImage (Image image) {
	checkWidget();
	if (image != null && image.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	if ((style & SWT.SEPARATOR) != 0) return;
	disabledImage = image;
}

/**
 * Enables the receiver if the argument is <code>true</code>,
 * and disables it otherwise.
 * <p>
 * A disabled control is typically
 * not selectable from the user interface and draws with an
 * inactive or "grayed" look.
 * </p>
 *
 * @param enabled the new enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setEnabled (boolean enabled) {
	checkWidget ();
	int topHandle = topHandle ();
	int flags = enabled ? 0 : OS.Pt_BLOCKED | OS.Pt_GHOST;
	OS.PtSetResource (topHandle, OS.Pt_ARG_FLAGS, flags, OS.Pt_BLOCKED | OS.Pt_GHOST);
	if ((style & SWT.DROP_DOWN) != 0) {
		OS.PtSetResource (button, OS.Pt_ARG_FLAGS, flags, OS.Pt_BLOCKED | OS.Pt_GHOST);
		OS.PtSetResource (arrow, OS.Pt_ARG_FLAGS, flags, OS.Pt_BLOCKED | OS.Pt_GHOST);
	}
}

boolean setFocus () {
	if ((style & SWT.SEPARATOR) != 0) return false;
	int focusHandle = (style & SWT.DROP_DOWN) != 0 ? button : handle;
	/*
	* Bug in Photon. Photon will stop sending key
	* events, if a menu is up and focus is given to
	* a widget by calling PtContainerGiveFocus(). The
	* fix is to detect when a menu is up and avoid
	* calling this function.
	*/
	Shell shell = parent.getShell ();
	if (shell.activeMenu != null) return false;
	OS.PtContainerGiveFocus (focusHandle, null);
	return OS.PtIsFocused(focusHandle) != 0;
}

void setFont (byte [] font) {
	int ptr = OS.malloc (font.length);
	OS.memmove (ptr, font, font.length);
	setFont (ptr);
	OS.free (ptr);
}

void setFont (int font) {
	int [] args = {
		OS.Pt_ARG_TEXT_FONT, font, 0,
		OS.Pt_ARG_LIST_FONT, font, 0,
		OS.Pt_ARG_TITLE_FONT, font, 0,
		OS.Pt_ARG_GAUGE_FONT, font, 0,
	};
	OS.PtSetResources (handle, args.length / 3, args);
	if ((style & SWT.DROP_DOWN) != 0) {
		OS.PtSetResources (button, args.length / 3, args);
		OS.PtSetResources (arrow, args.length / 3, args);
	}
}

void setForegroundPixel (int pixel) {
	OS.PtSetResource (handle, OS.Pt_ARG_COLOR, pixel, 0);
	if ((style & SWT.DROP_DOWN) != 0) {
		OS.PtSetResource (button, OS.Pt_ARG_COLOR, pixel, 0);
		OS.PtSetResource (arrow, OS.Pt_ARG_COLOR, pixel, 0);
	}
}

/**
 * Sets the receiver's hot image to the argument, which may be
 * null indicating that no hot image should be displayed.
 * <p>
 * The hot image is displayed when the mouse enters the receiver.
 * </p>
 *
 * @param image the hot image to display on the receiver (may be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setHotImage (Image image) {
	checkWidget();
	if (image != null && image.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	if ((style & SWT.SEPARATOR) != 0) return;

	/* TEMPORARY CODE: remove when when FLAT tool bars are implemented */
	if ((parent.style & SWT.FLAT) != 0) setImage (image);

	hotImage = image;
}

public void setImage (Image image) {
	checkWidget();
	if (image != null && image.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	if ((style & SWT.SEPARATOR) != 0) return;	
	super.setImage (image);

	/* TEMPORARY CODE: remove when when FLAT tool bars are implemented */
	if ((parent.style & SWT.FLAT) != 0 && hotImage != null) return;

	int imageHandle = 0;
	int type = OS.Pt_Z_STRING;
	if (image != null) {
		imageHandle = copyPhImage (image.handle);
		if(text.length() != 0) type = OS.Pt_TEXT_IMAGE;
		else type = OS.Pt_IMAGE;
	}	
	int [] args = {
		OS.Pt_ARG_LABEL_IMAGE, imageHandle, 0,
		OS.Pt_ARG_LABEL_TYPE, type, 0
	};
	OS.PtSetResources (button, args.length / 3, args);
	if (imageHandle != 0) OS.free (imageHandle);
	
	/*
	* Bug on Photon.  When a the text/image is set on a
	* DROP_DOWN item that is realized, the item does not resize
	* to show the new text/image.  The fix is to force the item
	* to recalculate the size.
	*/
	if ((style & SWT.DROP_DOWN) != 0) {
		if (OS.PtWidgetIsRealized (handle)) {
			OS.PtExtentWidget (handle);
		}
	}
}

boolean setRadioSelection (boolean value) {
	if ((style & SWT.RADIO) == 0) return false;
	if (getSelection () != value) {
		setSelection (value);
		postEvent (SWT.Selection);
	}
	return true;
}

/**
 * Sets the selection state of the receiver.
 * <p>
 * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>,
 * it is selected when it is checked (which some platforms draw as a
 * pushed in button).
 * </p>
 *
 * @param selected the new selection state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (boolean selected) {
	checkWidget();
	if ((style & (SWT.CHECK | SWT.RADIO | SWT.TOGGLE)) == 0) return;
	OS.PtSetResource (handle, OS.Pt_ARG_FLAGS, selected ? OS.Pt_SET : 0, OS.Pt_SET);
}

/**
 * Sets the receiver's text. The string may include
 * the mnemonic character.
 * </p>
 * <p>
 * Mnemonics are indicated by an '&amp;' that causes the next
 * character to be the mnemonic.  When the user presses a
 * key sequence that matches the mnemonic, a selection
 * event occurs. On most platforms, the mnemonic appears
 * underlined but may be emphasised in a platform specific
 * manner.  The mnemonic indicator character '&amp;' can be
 * escaped by doubling it in the string, causing a single
 * '&amp;' to be displayed.
 * </p>
 * 
 * @param string the new text
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the text is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setText (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if ((style & SWT.SEPARATOR) != 0) return;
	super.setText (string);
	char [] text = new char [string.length ()];
	string.getChars (0, text.length, text, 0);
	char mnemonic = fixMnemonic (text);
	byte [] buffer = Converter.wcsToMbcs (null, text, true);
	int ptr1 = OS.malloc (buffer.length);
	OS.memmove (ptr1, buffer, buffer.length);
	int ptr2 = 0;
	if (mnemonic != 0) {
		byte [] buffer2 = Converter.wcsToMbcs (null, new char []{mnemonic}, true);
		ptr2 = OS.malloc (buffer2.length);
		OS.memmove (ptr2, buffer2, buffer2.length);
	}
	replaceMnemonic (mnemonic, true, true);
	int type = OS.Pt_Z_STRING;
	if (image != null) type = OS.Pt_TEXT_IMAGE;
	int [] args = {
		OS.Pt_ARG_TEXT_STRING, ptr1, 0,
		OS.Pt_ARG_LABEL_TYPE, type, 0,
		OS.Pt_ARG_ACCEL_KEY, ptr2, 0,
	};
	OS.PtSetResources (button, args.length / 3, args);
	OS.free (ptr1);
	OS.free (ptr2);
	
	/*
	* Bug on Photon.  When a the text/image is set on a
	* DROP_DOWN item that is realized, the item does not resize
	* to show the new text/image.  The fix is to force the item
	* to recalculate the size.
	*/
	if ((style & SWT.DROP_DOWN) != 0) {
		if (OS.PtWidgetIsRealized (handle)) {
			OS.PtExtentWidget (handle);
		}
	}
}

/**
 * Sets the receiver's tool tip text to the argument, which
 * may be null indicating that the default tool tip for the 
 * control will be shown. For a control that has a default
 * tool tip, such as the Tree control on Windows, setting
 * the tool tip text to an empty string replaces the default,
 * causing no tool tip text to be shown.
 * <p>
 * The mnemonic indicator (character '&amp;') is not displayed in a tool tip.
 * To display a single '&amp;' in the tool tip, the character '&amp;' can be 
 * escaped by doubling it in the string.
 * </p>
 * 
 * @param string the new tool tip text (or null)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setToolTipText (String string) {
	checkWidget();
	toolTipText = string;
}

/**
 * Sets the width of the receiver, for <code>SEPARATOR</code> ToolItems.
 *
 * @param width the new width
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setWidth (int width) {
	checkWidget();
	if ((style & SWT.SEPARATOR) == 0) return;
	if (width < 0) return;
	int topHandle = topHandle ();
	OS.PtSetResource (topHandle, OS.Pt_ARG_WIDTH, width, 0);
	if (control != null && !control.isDisposed ()) {
		control.setBounds (getBounds ());
	}
}
}
