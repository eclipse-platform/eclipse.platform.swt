package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

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
 * for all SWT widget classes should include a comment which
 * describes the style constants which are applicable to the class.
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
public ToolItem (ToolBar parent, int style) {
	this(parent, style, parent.getItemCount ());
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
 * for all SWT widget classes should include a comment which
 * describes the style constants which are applicable to the class.
 * </p>
 *
 * @param parent a composite control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 * @param index the index to store the receiver in its parent
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
public ToolItem (ToolBar parent, int style, int index) {
	super (parent, checkStyle (style));
	this.parent = parent;
	parent.createItem (this, index);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is selected, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * When <code>widgetSelected</code> is called when the mouse is over the arrow portion of a drop-down tool,
 * the event object detail field contains the value <code>SWT.ARROW</code>.
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
	OS.PmMemStart(mc);
	OS.PgSetFillColor(0xFFFFFF);
	OS.PgDrawIRect(0, 0, width, height, OS.Pg_DRAW_FILL);
	OS.PgSetStrokeColor(0x000000);
	OS.PgSetFillColor(0x000000);
	short [] points = {(short)0, (short)1, (short)2, (short)3, (short)4, (short)1};
	OS.PgDrawPolygon(points, points.length / 2,	new PhPoint_t(), OS.Pg_DRAW_FILL | OS.Pg_DRAW_STROKE | OS.Pg_CLOSED);
	OS.PmMemFlush(mc, image);
	OS.PmMemStop(mc);
	OS.PmMemReleaseMC(mc);
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
			OS.Pt_ARG_FLAGS, (style & SWT.NO_FOCUS) != 0 ? 0 : OS.Pt_GETS_FOCUS, OS.Pt_GETS_FOCUS,
			OS.Pt_ARG_BALLOON_POSITION, rightAligned ? OS.Pt_BALLOON_RIGHT : OS.Pt_BALLOON_BOTTOM, 0,
			OS.Pt_ARG_BASIC_FLAGS, 0, OS.Pt_RIGHT_ETCH | OS.Pt_RIGHT_OUTLINE,
		};
		button = OS.PtCreateWidget (OS.PtButton (), handle, args.length / 3, args);
		if (button == 0) error (SWT.ERROR_NO_HANDLES);
		int arrowImage = createArrowImage ();
		args =  new int [] {
			OS.Pt_ARG_FLAGS, (style & SWT.NO_FOCUS) != 0 ? 0 : OS.Pt_GETS_FOCUS, OS.Pt_GETS_FOCUS,
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
			OS.Pt_ARG_FLAGS, (style & SWT.NO_FOCUS) != 0 ? 0 : OS.Pt_GETS_FOCUS, OS.Pt_GETS_FOCUS,
			OS.Pt_ARG_BALLOON_POSITION, rightAligned ? OS.Pt_BALLOON_RIGHT : OS.Pt_BALLOON_BOTTOM, 0,
			OS.Pt_ARG_FLAGS, toggle ? OS.Pt_TOGGLE : 0, OS.Pt_TOGGLE,
		};
		handle = button = OS.PtCreateWidget (OS.PtButton (), parentHandle, args.length / 3, args);
	}
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
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

void deregister () {
	super.deregister ();
	if ((style & SWT.DROP_DOWN) != 0) {
		WidgetTable.remove (button);
		WidgetTable.remove (arrow);
	}
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
	PhArea_t area = new PhArea_t ();
	OS.PtWidgetArea (handle, area);
	return new Rectangle (area.pos_x, area.pos_y, area.size_w, area.size_h);
}

/**
 * Returns the control that is used to fill the bounds of
 * the item when the items is a <code>SEPARATOR</code>.
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

public Display getDisplay () {
	ToolBar parent = this.parent;
	if (parent == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent.getDisplay ();
}

/**
 * Returns <code>true</code> if the receiver is enabled, and
 * <code>false</code> otherwise.
 * <p>
 * A disabled control is typically not selectable from the
 * user interface and draws with an inactive or "grayed" look.
 * </p>
 *
 * @return the receiver's enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
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
 * it is selected when it is checked. When it is of type <code>TOGGLE</code>,
 * it is selected when it is pushed.
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
	int [] args = {OS.Pt_ARG_WIDTH, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	return args [1];
}

void hookEvents () {
	super.hookEvents ();
	if ((style & SWT.SEPARATOR) != 0) return;
	int windowProc = getDisplay ().windowProc;
	OS.PtAddEventHandler (handle, OS.Ph_EV_BOUNDARY, windowProc, SWT.MouseEnter);	
	OS.PtAddCallback (button, OS.Pt_CB_ACTIVATE, windowProc, SWT.Selection);
	if ((style & SWT.DROP_DOWN) != 0) {
		OS.PtAddCallback (arrow, OS.Pt_CB_ACTIVATE, windowProc, SWT.Selection);
	}
}

/**
 * Returns <code>true</code> if the receiver is enabled, and
 * <code>false</code> otherwise.
 * <p>
 * A disabled control is typically not selectable from the
 * user interface and draws with an inactive or "grayed" look.
 * </p>
 *
 * @return the receiver's enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean isEnabled () {
	checkWidget();
	return getEnabled () && parent.isEnabled ();
}

int processEvent (int widget, int data, int info) {
	if (widget == arrow && data == SWT.Selection) {
		Event event = new Event ();
		event.detail = SWT.ARROW;
		postEvent (SWT.Selection, event);
		return OS.Pt_CONTINUE;
	}
	return super.processEvent (widget, data, info);
}

int processMouseEnter (int info) {
	if (info == 0) return OS.Pt_END;
	PtCallbackInfo_t cbinfo = new PtCallbackInfo_t ();
	OS.memmove (cbinfo, info, PtCallbackInfo_t.sizeof);
	if (cbinfo.event == 0) return OS.Pt_END;
	PhEvent_t ev = new PhEvent_t ();
	OS.memmove (ev, cbinfo.event, PhEvent_t.sizeof);
	switch (ev.subtype) {
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

int processSelection (int info) {
	if ((style & SWT.RADIO) != 0) {
		setSelection (true);		
		ToolItem [] items = parent.getItems ();
		int index = 0;
		while (index < items.length && items [index] != this) index++;
		ToolItem item;
		int i = index;
		while (--i >= 0 && ((item = items [i]).style & SWT.RADIO) != 0) {
			item.setSelection (false);
		}
		i = index;
		while (++i < items.length && ((item = items [i]).style & SWT.RADIO) != 0) {
			item.setSelection (false);
		}
	}
	postEvent (SWT.Selection);
	return OS.Pt_CONTINUE;
}

void register () {
	super.register ();
	if ((style & SWT.DROP_DOWN) != 0) {
		WidgetTable.put (button, this);
		WidgetTable.put (arrow, this);
	}
}

void releaseChild () {
	super.releaseChild ();
	parent.destroyItem (this);
}

void releaseHandle () {
	super.releaseHandle ();
	arrow = button = 0;
}

void releaseWidget () {
	super.releaseWidget ();
	if (toolTipHandle != 0) destroyToolTip (toolTipHandle);
	toolTipHandle = 0;
	parent = null;
	control = null;
	hotImage = null;
	disabledImage = null;
	toolTipText = null;
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
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

void setBackgroundPixel (int pixel) {
	OS.PtSetResource (handle, OS.Pt_ARG_FILL_COLOR, pixel, 0);
	if (button != 0 && button != handle) {
		OS.PtSetResource (button, OS.Pt_ARG_FILL_COLOR, pixel, 0);
	}
	if (arrow != 0) {
		OS.PtSetResource (arrow, OS.Pt_ARG_FILL_COLOR, pixel, 0);
	}
}

/**
 * Sets the control that is used to fill the bounds of
 * the item when the items is a <code>SEPARATOR</code>.
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

/**
 * Sets the receiver's disabled image to the argument, which may be
 * null indicating that no disabled image should be displayed.
 * <p>
 * The disbled image is displayed when the receiver is disabled.
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
}

void setFont (int font) {
	int [] args = {
		OS.Pt_ARG_TEXT_FONT, font, 0,
		OS.Pt_ARG_LIST_FONT, font, 0,
		OS.Pt_ARG_TITLE_FONT, font, 0,
	};
	OS.PtSetResources (handle, args.length / 3, args);
	if (button != 0 && button != handle) {
		OS.PtSetResources (button, args.length / 3, args);
	}
}

void setForegroundPixel (int pixel) {
	OS.PtSetResource (handle, OS.Pt_ARG_COLOR, pixel, 0);
	if (button != 0 && button != handle) {
		OS.PtSetResource (button, OS.Pt_ARG_COLOR, pixel, 0);
	}
	if (arrow != 0) {
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

/**
 * Sets the selection state of the receiver.
 * <p>
 * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>,
 * it is selected when it is checked. When it is of type <code>TOGGLE</code>,
 * it is selected when it is pushed.
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

public void setText (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if ((style & SWT.SEPARATOR) != 0) return;
	super.setText (string);
	byte [] buffer = Converter.wcsToMbcs (null, string, true);
	int ptr = OS.malloc (buffer.length);
	OS.memmove (ptr, buffer, buffer.length);
	int type = OS.Pt_Z_STRING;
	if (image != null) type = OS.Pt_TEXT_IMAGE;
	int [] args = {
		OS.Pt_ARG_TEXT_STRING, ptr, 0,
		OS.Pt_ARG_LABEL_TYPE, type, 0,
	};
	OS.PtSetResources (button, args.length / 3, args);
	if (ptr != 0) OS.free (ptr);
	
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
 * may be null indicating that no tool tip text should be shown.
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
	checkWidget();
	if ((style & SWT.SEPARATOR) == 0) return;
	if (width < 0) return;
	OS.PtSetResource (handle, OS.Pt_ARG_WIDTH, width, 0);
	if (control != null && !control.isDisposed ()) {
		control.setBounds (getBounds ());
	}
}
}
