package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

/**
 * Instances of this class represent a selectable user interface object that
 * issues notification when pressed and released. 
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>ARROW, CHECK, PUSH, RADIO, TOGGLE, FLAT</dd>
 * <dd>LEFT, RIGHT, CENTER</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 */
public class Button extends Control {
	int boxHandle;
	Image image;
	String text;

/**
 * Constructs a new instance of this class given its parent
 * and a style value describing its behavior and appearance.
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
public Button (Composite parent, int style) {
	super (parent, checkStyle (style));
}

static int checkStyle (int style) {
	style = checkBits (style, SWT.PUSH, SWT.ARROW, SWT.CHECK, SWT.RADIO, SWT.TOGGLE, 0);
	if ((style & SWT.PUSH) != 0) {
		return checkBits (style, SWT.CENTER, SWT.LEFT, SWT.RIGHT, 0, 0, 0);
	}
	if ((style & (SWT.CHECK | SWT.RADIO | SWT.TOGGLE)) != 0) {
		return checkBits (style, SWT.LEFT, SWT.RIGHT, SWT.CENTER, 0, 0, 0);
	}
	if ((style & SWT.ARROW) != 0) {
		return checkBits (style, SWT.UP, SWT.DOWN, SWT.LEFT, SWT.RIGHT, 0, 0);
	}
	return style;
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is selected, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is called when the control is selected.
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

void createHandle (int index) {
	state |= HANDLE;
	int bits = SWT.ARROW | SWT.TOGGLE | SWT.CHECK | SWT.RADIO | SWT.PUSH;
	
	boxHandle = OS.gtk_event_box_new ();
	if (boxHandle == 0) error (SWT.ERROR_NO_HANDLES);
	switch (style & bits) {
		case SWT.ARROW:
			handle = OS.gtk_button_new ();
			int arrow = OS.gtk_arrow_new (OS.GTK_ARROW_UP, OS.GTK_SHADOW_OUT);
			OS.gtk_container_add (handle, arrow);
			OS.gtk_widget_show (arrow);
			break;
		case SWT.TOGGLE:
			handle = OS.gtk_toggle_button_new ();
			break;
		case SWT.CHECK:
			handle = OS.gtk_check_button_new ();
			break;
		case SWT.RADIO:
			handle = OS.gtk_radio_button_new (parent.radioGroup());
			break;
		case SWT.PUSH:
		default:
			handle = OS.gtk_button_new ();
			break;
	}
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
}

void setHandleStyle() {}

void configure() {
	_connectParent();
	OS.gtk_container_add (boxHandle, handle);
}

void showHandle() {
	OS.gtk_widget_show (boxHandle);
	OS.gtk_widget_show (handle);
	OS.gtk_widget_realize (handle);
}

void hookEvents () {
	super.hookEvents();
	/*
	* Feature in GTK.  For some reason, when the widget
	* is a check or radio button, mouse move and key
	* release events are not signaled.  The fix is to
	* look for them on the parent.
	*/
	if ((style & (SWT.CHECK | SWT.RADIO)) != 0) {
		int mask = OS.GDK_POINTER_MOTION_MASK | OS.GDK_KEY_RELEASE_MASK;
		OS.gtk_widget_add_events (boxHandle, mask);
		signal_connect_after (boxHandle, "motion_notify_event", SWT.MouseMove, 3);
		signal_connect_after (boxHandle, "key_release_event", SWT.KeyUp, 3);
	}
	signal_connect (handle, "clicked", SWT.Selection, 2);
}

void register () {
	super.register ();
	WidgetTable.put (boxHandle, this);	
}

void createWidget (int index) {
	super.createWidget (index);
	text = "";
}

int topHandle ()  { return boxHandle; }

/**
 * Returns a value which describes the position of the
 * text or image in the receiver. The value will be one of
 * <code>LEFT</code>, <code>RIGHT</code> or <code>CENTER</code>
 * unless the receiver is an <code>ARROW</code> button, in 
 * which case, the alignment will indicate the direction of
 * the arrow (one of <code>LEFT</code>, <code>RIGHT</code>, 
 * <code>UP</code> or <code>DOWN</code>.
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
	if ((style & SWT.ARROW) != 0) {
		if ((style & SWT.UP) != 0) return SWT.UP;
		if ((style & SWT.DOWN) != 0) return SWT.DOWN;
		if ((style & SWT.LEFT) != 0) return SWT.LEFT;
		if ((style & SWT.RIGHT) != 0) return SWT.RIGHT;
		return SWT.UP;
	}
	if ((style & SWT.LEFT) != 0) return SWT.LEFT;
	if ((style & SWT.CENTER) != 0) return SWT.CENTER;
	if ((style & SWT.RIGHT) != 0) return SWT.RIGHT;
	return SWT.LEFT;
}

/**
 * Returns the receiver's image if it has one, or null
 * if it does not.
 *
 * @return the receiver's image
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Image getImage () {
	checkWidget ();
	return image;
}

String getNameText () {
	return getText ();
}

/**
 * Returns <code>true</code> if the receiver is selected,
 * and false otherwise.
 * <p>
 * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>,
 * it is selected when it is checked. When it is of type <code>TOGGLE</code>,
 * it is selected when it is pushed. If the receiver is of any other type,
 * this method returns false.
 *
 * @return the selection state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getSelection () {
	checkWidget ();
	if ((style & (SWT.CHECK | SWT.RADIO | SWT.TOGGLE)) == 0) return false;
	return OS.gtk_toggle_button_get_active (handle);
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
public void removeSelectionListener (SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

/**
 * Controls how text, images and arrows will be displayed
 * in the receiver. The argument should be one of
 * <code>LEFT</code>, <code>RIGHT</code> or <code>CENTER</code>
 * unless the receiver is an <code>ARROW</code> button, in 
 * which case, the argument indicates the direction of
 * the arrow (one of <code>LEFT</code>, <code>RIGHT</code>, 
 * <code>UP</code> or <code>DOWN</code>.
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
	if ((style & SWT.ARROW) != 0) {
		if ((style & (SWT.UP | SWT.DOWN | SWT.LEFT | SWT.RIGHT)) == 0) return; 
		style &= ~(SWT.UP | SWT.DOWN | SWT.LEFT | SWT.RIGHT);
		style |= alignment & (SWT.UP | SWT.DOWN | SWT.LEFT | SWT.RIGHT);
		int arrow_type = OS.GTK_ARROW_UP;
		switch (alignment) {
			case SWT.UP:
				arrow_type = OS.GTK_ARROW_UP;
				break;
			case SWT.DOWN:
				arrow_type = OS.GTK_ARROW_DOWN;
				break;
			case SWT.LEFT:
				arrow_type = OS.GTK_ARROW_LEFT;
				break;
			case SWT.RIGHT:
				arrow_type = OS.GTK_ARROW_RIGHT;
				break;
		}
		int list = OS.gtk_container_children (handle);
		int arrow = OS.g_list_nth_data (list, 0);
		OS.gtk_arrow_set (arrow, arrow_type, OS.GTK_SHADOW_OUT);
		return;
	}
	if ((alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER)) == 0) return;
	style &= ~(SWT.LEFT | SWT.RIGHT | SWT.CENTER);
	style |= alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER);
	int list = OS.gtk_container_children (handle);
	int label = OS.g_list_nth_data (list, 0);
	if (label == 0) return;
	boolean isText = OS.GTK_WIDGET_TYPE (handle) == OS.gtk_label_get_type ();
	if ((style & SWT.LEFT) != 0) {
		OS.gtk_misc_set_alignment (label, 0.0f, 0.5f);
		if (isText) OS.gtk_label_set_justify (label, OS.GTK_JUSTIFY_LEFT);
		return;
	}
	if ((style & SWT.CENTER) != 0) {
		OS.gtk_misc_set_alignment (label, 0.5f, 0.5f);
		if (isText) OS.gtk_label_set_justify (label, OS.GTK_JUSTIFY_CENTER);
		return;
	}
	if ((style & SWT.RIGHT) != 0) {
		OS.gtk_misc_set_alignment (label, 1.0f, 0.5f);
		if (isText) OS.gtk_label_set_justify (label, OS.GTK_JUSTIFY_RIGHT);
		return;
	}
}

/**
 * Sets the receiver's image to the argument, which may be
 * null indicating that no image should be displayed.
 *
 * @param image the image to display on the receiver (may be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li>
 * </ul> 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setImage (Image image) {
	checkWidget ();
	this.image = image;
	if ((style & SWT.ARROW) != 0) return;
	int list = OS.gtk_container_children (handle);
	if (list != 0) {
		int widget = OS.g_list_nth_data (list, 0);
		if (widget != 0) OS.gtk_widget_destroy (widget);
	}
	if (image != null) {
		int pixmap = OS.gtk_pixmap_new (image.pixmap, image.mask);
		OS.gtk_container_add (handle, pixmap);
		OS.gtk_widget_show (pixmap);
	}
}

/**
 * Returns the receiver's text, which will be an empty
 * string if it has never been set.
 *
 * @return the receiver's text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getText () {
	checkWidget();
	return text;
}

/**
 * Sets the selection state of the receiver, if it is of type <code>CHECK</code>, 
 * <code>RADIO</code>, or <code>TOGGLE</code>.
 *
 * <p>
 * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>,
 * it is selected when it is checked. When it is of type <code>TOGGLE</code>,
 * it is selected when it is pushed.
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
	OS.gtk_signal_handler_block_by_data (handle, SWT.Selection);
	OS.gtk_toggle_button_set_active (handle, selected);
	OS.gtk_signal_handler_unblock_by_data (handle, SWT.Selection);
}

/**
 * Sets the receiver's text.
 * <p>
 * This method sets the button label.  The label may include
 * the mnemonic character but must not contain line delimiters.
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
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	text = string;
	if ((style & SWT.ARROW) != 0) return;
	int length = string.length ();
	char [] text = new char [length + 1];
	char [] pattern = new char [length + 1];
	string.getChars (0, length, text, 0);
	int i = 0, j = 0;
	while (i < length) {
		pattern [j] = ' ';
		if (text [i] == '&') {
			i++;
			if (i < length && text [i] != '&') {
				pattern [j] = '_';
			}
		}
		text [j++] = text [i++];
	}
	while (j < i) {
		text [j] = pattern [j] = '\0';
		j++;
	}
	int list = OS.gtk_container_children (handle);
	if (list != 0) {
		int widget = OS.g_list_nth_data (list, 0);
		if (widget != 0) OS.gtk_widget_destroy (widget);
	}
	byte [] buffer1 = Converter.wcsToMbcs (null, text);
	int label = OS.gtk_label_new (buffer1);
	byte [] buffer2 = Converter.wcsToMbcs (null, pattern);
	OS.gtk_label_set_pattern (label, buffer2);	
	OS.gtk_container_add (handle, label);
	OS.gtk_widget_show (label);	
}

int processSelection (int int0, int int1, int int2) {
	postEvent(SWT.Selection);
	return 0;
}

void deregister () {
	super.deregister ();
	WidgetTable.remove (boxHandle);
}

void releaseWidget () {
	super.releaseWidget ();
	image = null;
	text = null;
}

void releaseHandle () {
	super.releaseHandle ();
	boxHandle = 0;
}

}