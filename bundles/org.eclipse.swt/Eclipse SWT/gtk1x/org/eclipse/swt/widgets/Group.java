package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class provide an etched border
 * with an optional title.
 * <p>
 * Shadow styles are hints and may not be honoured
 * by the platform.  To create a group with the
 * default shadow style for the platform, do not
 * specify a shadow style.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SHADOW_ETCHED_IN, SHADOW_ETCHED_OUT, SHADOW_IN, SHADOW_OUT, SHADOW_NONE</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */
public class Group extends Composite {
	int frameHandle;
	String text="";

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
public Group (Composite parent, int style) {
	super (parent, checkStyle (style));
}
static int checkStyle (int style) {
	/*
	* Even though it is legal to create this widget
	* with scroll bars, they serve no useful purpose
	* because they do not automatically scroll the
	* widget's client area.  The fix is to clear
	* the SWT style.
	*/
	return style & ~(SWT.H_SCROLL | SWT.V_SCROLL);
}

void createHandle(int index) {
	state |= HANDLE;
	
	eventBoxHandle = OS.gtk_event_box_new ();
	if (eventBoxHandle == 0) error (SWT.ERROR_NO_HANDLES);
	
	frameHandle = OS.gtk_frame_new(null);
	if (frameHandle == 0) error (SWT.ERROR_NO_HANDLES);
	
	handle = OS.gtk_fixed_new();
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
}

void _setHandleStyle() {
	int shadow = OS.GTK_SHADOW_IN;
	if ((style & SWT.SHADOW_IN) != 0) shadow = OS.GTK_SHADOW_IN;
	if ((style & SWT.SHADOW_OUT) != 0) shadow = OS.GTK_SHADOW_OUT;
	if ((style & SWT.SHADOW_ETCHED_IN) != 0) shadow = OS.GTK_SHADOW_ETCHED_IN;
	if ((style & SWT.SHADOW_ETCHED_OUT) != 0) shadow = OS.GTK_SHADOW_ETCHED_OUT;
	OS.gtk_frame_set_shadow_type(frameHandle, shadow);
}

void configure() {
	_connectParent();
	OS.gtk_container_add(eventBoxHandle, frameHandle);
	OS.gtk_container_add(frameHandle, handle);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	int width = _computeSize(wHint, hHint, changed).x;
	int height = 0;
	Point size;
	if (layout != null) {
		size = layout.computeSize (this, wHint, hHint, changed);
	} else {
		size = minimumSize ();
	}
	if (size.x == 0) size.x = DEFAULT_WIDTH;
	if (size.y == 0) size.y = DEFAULT_HEIGHT;
	if (wHint != SWT.DEFAULT) size.x = wHint;
	if (hHint != SWT.DEFAULT) size.y = hHint;
	width = Math.max (width, size.x);
	height = Math.max (height, size.y);
	Rectangle trim = computeTrim (0, 0, width, height);
	width = trim.width;  height = trim.height;
	return new Point (width, height);
}

void showHandle() {
	OS.gtk_widget_show (eventBoxHandle);
	OS.gtk_widget_show (frameHandle);
	OS.gtk_widget_show (handle);
	OS.gtk_widget_realize (handle);
}

void register () {
	super.register ();
	WidgetTable.put (frameHandle, this);
}

void releaseHandle () {
	super.releaseHandle ();
	frameHandle = 0;
}

void releaseWidget () {
	super.releaseWidget ();
	text = null;
}

void deregister () {
	super.deregister ();
	WidgetTable.remove (frameHandle);
}

int topHandle () { return eventBoxHandle; }
int parentingHandle() { return handle; }

/*
 *   ===  GEOMETRY  ===
 */

public Rectangle _getClientArea () {
	/*
	 * The Group coordinates originate at the client area
	 */
	int width, height;
	Point size = _getSize();
	width = size.x - _getTrim().left - _getTrim().right;
	height = size.y - _getTrim().top - _getTrim().bottom;
	return new Rectangle(0,0, width, height);
}

Trim _getTrim() {
	trim = new Trim();
	
	// set up the test widgets
	int testWindowHandle = OS.gtk_window_new(0);
	int testHandle = OS.gtk_frame_new(string2bytesConvertMnemonic("Test String"));
	OS.gtk_container_add(testWindowHandle, testHandle);
	OS.gtk_widget_realize(testHandle);
	
	// get info
	GtkFrame frame = new GtkFrame();
	OS.memmove (frame, testHandle, GtkFrame.sizeof);
	GtkStyle groupStyle = new GtkStyle();
	OS.memmove (groupStyle, frame.style, GtkStyle.sizeof);
	GtkStyleClass styleClass = new GtkStyleClass();
	OS.memmove (styleClass, groupStyle.klass, GtkStyleClass.sizeof);
	
	// see gtk_frame_size_allocate()
	trim.left = trim.right = frame.border_width + styleClass.xthickness;
	trim.top = frame.border_width + Math.max(frame.label_height, styleClass.ythickness);
	trim.bottom = frame.border_width + styleClass.ythickness;
	
	// clean up
	OS.gtk_widget_destroy(testHandle);
	OS.gtk_widget_destroy(testWindowHandle);
	return trim;
}

boolean _setSize(int width, int height) {
	boolean differentExtent = UtilFuncs.setSize (topHandle(), width,height);
	Point clientSize = UtilFuncs.getSize(frameHandle);
	// WRONG but it's quite safe - the frame clips it
	UtilFuncs.setSize (handle, clientSize.x, clientSize.y);
	return differentExtent;
}
/*   =========  Model Logic  =========   */

String getNameText () {
	return getText ();
}
/**
 * Returns the receiver's text, which is the string that the
 * is used as the <em>title</em>. If the text has not previously
 * been set, returns an empty string.
 *
 * @return the text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getText () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return text;
}


/**
 * Sets the receiver's text, which is the string that will
 * be displayed as the receiver's <em>title</em>, to the argument,
 * which may not be null. 
 *
 * @param text the new text
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setText (String string) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	OS.gtk_frame_set_label (frameHandle, string2bytesConvertMnemonic(string));
	text=string;
}

}
