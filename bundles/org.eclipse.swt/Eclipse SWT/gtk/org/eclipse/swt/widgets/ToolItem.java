package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
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
	Image hotImage, disabledImage;
	String toolTipText;

	int boxHandle, arrowHandle, arrowButtonHandle;
	
	int currentpixmap;
	boolean drawHotImage;
	private int tooltipsHandle;

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
	super (parent, checkStyle (style));
	this.parent = parent;
	createWidget (parent.getItemCount ());
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
	int count = parent.getItemCount ();
	if (!(0 <= index && index <= count)) {
		error (SWT.ERROR_ITEM_NOT_ADDED);
	}
	createWidget (index);
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

void createHandle (int index) {
	state |= HANDLE;
	switch (style & (SWT.SEPARATOR | SWT.RADIO | SWT.CHECK | SWT.PUSH | SWT.DROP_DOWN)) {
		case SWT.PUSH:
		case 0:
			handle = OS.gtk_toolbar_insert_element (parent.handle,
			OS.GTK_TOOLBAR_CHILD_BUTTON(),
			0, new byte[1], null, null,
			0, 0, 0,
			index);
			return;
		case SWT.RADIO:
			handle = OS.gtk_toolbar_insert_element (parent.handle,
				OS.GTK_TOOLBAR_CHILD_RADIOBUTTON(),
				0, new byte[1], null, null,
				0, 0, 0,
				index);
			return;
		case SWT.CHECK:
			handle = OS.gtk_toolbar_insert_element (parent.handle,
				OS.GTK_TOOLBAR_CHILD_TOGGLEBUTTON(),
				0, new byte[1], null, null,
				0, 0, 0,
				index);
			return;
		case SWT.SEPARATOR:
			boxHandle = OS.gtk_event_box_new();
			if (boxHandle==0) error(SWT.ERROR_NO_HANDLES);
			boolean isVertical = (parent.getStyle()&SWT.VERTICAL) != 0;
			handle = isVertical? OS.gtk_hseparator_new() : OS.gtk_vseparator_new();
			if (handle==0) error(SWT.ERROR_NO_HANDLES);
			OS.gtk_toolbar_insert_widget (
				parent.handle,
				boxHandle,
				new byte[1], new byte[1],
				index);
			OS.gtk_container_add(boxHandle, handle);
			OS.gtk_widget_show(boxHandle);
			OS.gtk_widget_show(handle);
			return;
		case SWT.DROP_DOWN:
			/* create the box */
			isVertical = (parent.getStyle()&SWT.VERTICAL) != 0;
			boxHandle = isVertical? OS.gtk_vbox_new(false, 0) : OS.gtk_hbox_new(false, 0);
			if (boxHandle==0) error(SWT.ERROR_NO_HANDLES);
			/* create the button */
			handle = OS.gtk_button_new();
			if (handle==0) error(SWT.ERROR_NO_HANDLES);
			OS.gtk_button_set_relief(handle, OS.GTK_RELIEF_NONE);
			/* create the arrow */
			arrowHandle = OS.gtk_arrow_new (OS.GTK_ARROW_DOWN, OS.GTK_SHADOW_NONE);
			if (arrowHandle==0) error(SWT.ERROR_NO_HANDLES);
			arrowButtonHandle = OS.gtk_button_new ();
			if (arrowButtonHandle==0) error(SWT.ERROR_NO_HANDLES);
			OS.gtk_button_set_relief(arrowButtonHandle, OS.GTK_RELIEF_NONE);
			OS.gtk_container_set_border_width(arrowButtonHandle,0);
			int style = OS.gtk_style_copy(OS.gtk_widget_get_style(arrowButtonHandle));
			OS.gtk_style_set_xthickness(style, 0);
			OS.gtk_widget_set_style(arrowButtonHandle, style);
			// when the arrow gets destroyed, it will dereference the clone
			
			OS.gtk_toolbar_insert_widget (
				parent.handle,
				boxHandle,
				new byte[1], new byte[1],
				index);
			OS.gtk_box_pack_start(boxHandle, handle, true,true,0);
			OS.gtk_box_pack_end(boxHandle, arrowButtonHandle, true,true,0);
			OS.gtk_container_add (arrowButtonHandle, arrowHandle);
			OS.gtk_widget_show(handle);
			OS.gtk_widget_show (arrowHandle);
			OS.gtk_widget_show (arrowButtonHandle);
			OS.gtk_widget_show(boxHandle);
			return;
		default:
			/*
			 * Can not specify more than one style
			 */
			error(SWT.ERROR_ITEM_NOT_ADDED);
	}
}


void register() {
	super.register ();
	if (boxHandle != 0) WidgetTable.put(boxHandle, this);
	if (arrowButtonHandle != 0) WidgetTable.put(arrowButtonHandle, this);
	if (arrowHandle != 0) WidgetTable.put(arrowHandle, this);
}
void deregister() {
	super.deregister ();
	if (boxHandle != 0) WidgetTable.remove (boxHandle);
	if (arrowButtonHandle != 0) WidgetTable.remove (arrowButtonHandle);
	if (arrowHandle != 0) WidgetTable.remove (arrowHandle);
}

int topHandle() {
	return boxHandle == 0 ? handle : boxHandle;
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
	int x = OS.GTK_WIDGET_X (handle);
	int y = OS.GTK_WIDGET_Y (handle);
	int width = OS.GTK_WIDGET_WIDTH (handle);
	int height = OS.GTK_WIDGET_HEIGHT (handle);
	return new Rectangle (x, y, width, height);
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
	error(SWT.ERROR_NOT_IMPLEMENTED);
	return null;
}

public Display getDisplay () {
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
	checkWidget();
	return OS.GTK_WIDGET_SENSITIVE(handle);
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
	/* NOT IMPLEMENTED */
	return null;
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
	if (parent == null) error (SWT.ERROR_WIDGET_DISPOSED);
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
	if ((style & (SWT.CHECK | SWT.RADIO)) == 0) return false;
	return OS.gtk_toggle_button_get_active (handle);
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
/* FIXME */
	return 15;
}
void hookEvents () {
	super.hookEvents ();
	if ((style & SWT.SEPARATOR) != 0) return;
	signal_connect(handle, "clicked",   SWT.Selection, 2);
	signal_connect(handle, "enter-notify-event", SWT.MouseEnter, 3);
	signal_connect(handle, "leave-notify-event", SWT.MouseExit,  3);
	if (arrowButtonHandle!=0) signal_connect(arrowButtonHandle, "clicked",   SWT.DefaultSelection, 2);

	/*
	 * Feature in GTK.
	 * Usually, GTK widgets propagate all events to their parent when they
	 * are done their own processing.  However, in contrast to other widgets,
	 * the buttons that make up the tool items, do not propagate the mouse
	 * up/down events.
	 * (It it interesting to note that they DO propagate mouse motion events.)
	 */
	int mask =
		OS.GDK_EXPOSURE_MASK | OS.GDK_POINTER_MOTION_MASK |
		OS.GDK_BUTTON_PRESS_MASK | OS.GDK_BUTTON_RELEASE_MASK | 
		OS.GDK_ENTER_NOTIFY_MASK | OS.GDK_LEAVE_NOTIFY_MASK | 
		OS.GDK_KEY_PRESS_MASK | OS.GDK_KEY_RELEASE_MASK |
		OS.GDK_FOCUS_CHANGE_MASK;
	OS.gtk_widget_add_events (handle, mask);
	signal_connect (handle, "button_press_event", SWT.MouseDown, 3);
	signal_connect (handle, "button_release_event", SWT.MouseUp, 3);
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

int processMouseDown (int callData, int arg1, int int2) {
	parent.processMouseDown (callData, arg1, int2);
	return 0;
}
int processMouseUp (int callData, int arg1, int int2) {
	parent.processMouseUp (callData, arg1, int2);
	return 0;
}
int processMouseEnter (int int0, int int1, int int2) {
	drawHotImage = (parent.style & SWT.FLAT) != 0 && hotImage != null;
	if ( drawHotImage && (currentpixmap != 0) ) { 
		OS.gtk_pixmap_set (currentpixmap, hotImage.pixmap, hotImage.mask);
	}
	return 0;
}

int processMouseExit (int int0, int int1, int int2) {
	if (drawHotImage) {
		drawHotImage = false;
		if (currentpixmap != 0 && image != null){
			OS.gtk_pixmap_set (currentpixmap, image.pixmap, image.mask);
		}	
	}
	return 0;
}
/*
int processPaint (int int0, int int1, int int2) {
	if (ignorePaint) return 0;
	Image currentImage = drawHotImage ? hotImage : image;
	if (!getEnabled()) {
		Display display = getDisplay ();
		currentImage = disabledImage;
		if (currentImage == null) {
			currentImage = new Image (display, image, SWT.IMAGE_DISABLE);
		}
	}	
	if (currentpixmap != 0 && currentImage != null)
		OS.gtk_pixmap_set (currentpixmap, currentImage.pixmap, currentImage.mask);
	return 0;
}
*/
int processSelection  (int int0, int int1, int int2) {
	if ((style & SWT.RADIO) != 0) {
		this.setSelection (true);
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
	Event event = new Event ();
	postEvent (SWT.Selection, event);
	return 0;
}
int processDefaultSelection (int int0, int int1, int int2) {
	Event event = new Event ();
	event.detail = SWT.ARROW;
	postEvent (SWT.Selection, event);
	return 0;
}
void releaseWidget () {
	super.releaseWidget ();
	tooltipsHandle = arrowButtonHandle = arrowHandle = 0;
	parent = null;
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
	checkWidget ();
	if (control != null) {
		if (control.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
		if (control.parent != parent) error (SWT.ERROR_INVALID_PARENT);
	}
	if ((style & SWT.SEPARATOR) == 0) return;
	Control newControl = control;
	Control oldControl = this.control;
	if (oldControl == newControl) return;

	this.control = newControl;
	if (newControl != null) {
		if (handle != boxHandle) {
			WidgetTable.remove (handle);
			OS.gtk_widget_destroy (handle);
			handle = boxHandle;
		}
		OS.gtk_widget_reparent (newControl.topHandle(), boxHandle);
	} else {		
		boolean isVertical = (parent.getStyle () & SWT.VERTICAL) != 0;
		handle = isVertical ? OS.gtk_hseparator_new () : OS.gtk_vseparator_new ();
		if (handle == 0) error(SWT.ERROR_NO_HANDLES);
		OS.gtk_container_add (boxHandle, handle);
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
	checkWidget();
	OS.gtk_widget_set_sensitive (handle, enabled);
}

void setFontDescription (int font) {
	int list = OS.gtk_container_get_children (handle);
	if (list != 0) {
		int fontHandle = OS.g_list_nth_data (list, 0);
		OS.g_list_free (list);
		OS.gtk_widget_modify_font (fontHandle, font);
		return;
	}
	OS.gtk_widget_modify_font (handle, font);
}

void setForegroundColor (GdkColor color) {
	int list = OS.gtk_container_get_children (handle);
	if (list != 0) {
		int colorHandle = OS.g_list_nth_data (list, 0);
		OS.g_list_free (list);
		OS.gtk_widget_modify_fg (colorHandle, 0, color);
		return;
	}
	OS.gtk_widget_modify_fg (handle, 0, color);
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
	if ((style & SWT.SEPARATOR) != 0) return;
	hotImage = image;
}
public void setImage (Image image) {
	checkWidget();
	super.setImage (image);
	if ((style & SWT.SEPARATOR) != 0) return;
	int list = OS.gtk_container_get_children (handle);
	if (list != 0) {
		int widget = OS.g_list_nth_data (list, 0);
		if (widget != 0) OS.gtk_widget_destroy (widget);
		OS.g_list_free (list);
	}
	if (image != null) {
		int pixmap = OS.gtk_pixmap_new (image.pixmap, image.mask);
		OS.gtk_container_add (handle, pixmap);
		OS.gtk_widget_show (pixmap);
		currentpixmap = pixmap;
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
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((style & (SWT.CHECK | SWT.RADIO)) == 0) return;
	OS.gtk_signal_handler_block_by_data (handle, SWT.Selection);
	OS.gtk_toggle_button_set_active (handle, selected);
	OS.gtk_signal_handler_unblock_by_data (handle, SWT.Selection);
}
public void setText (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if ((style & SWT.SEPARATOR) != 0) return;
	super.setText (string);
	int length = string.length ();
	char [] text = new char [length + 1];
	string.getChars (0, length, text, 0);
	for (int i=0; i<length; i++) {
		if (text [i] == '&') text [i] = '_';
	}
	int list = OS.gtk_container_get_children (handle);
	if (list != 0) {
		int widget = OS.g_list_nth_data (list, 0);
		if (widget !=  0) OS.gtk_widget_destroy (widget);
		OS.g_list_free (list);
	}
	byte [] buffer = Converter.wcsToMbcs (null, text);
	int label = OS.gtk_label_new_with_mnemonic (buffer);
	OS.gtk_container_add (handle, label);
	OS.gtk_widget_show (label);
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
	if (tooltipsHandle == 0) tooltipsHandle = OS.gtk_tooltips_new();
	byte [] buffer = null;
	if (string != null) buffer = Converter.wcsToMbcs (null, string, true);
	OS.gtk_tooltips_set_tip (tooltipsHandle, handle, buffer, null);
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
	
	Point size = control.computeSize(width, SWT.DEFAULT);
	control.setSize(size);
}

static int checkStyle (int style) {
	return checkBits (style, SWT.PUSH, SWT.CHECK, SWT.RADIO, SWT.SEPARATOR, SWT.DROP_DOWN, 0);
}
}
