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
 * This class is the abstract superclass of all user interface objects.  
 * Widgets are created, disposed and issue notification to listeners
 * when events occur which affect them.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Dispose</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation. However, it has not been marked
 * final to allow those outside of the SWT development team to implement
 * patched versions of the class in order to get around specific
 * limitations in advance of when those limitations can be addressed
 * by the team.  Any class built using subclassing to access the internals
 * of this class will likely fail to compile or run between releases and
 * may be strongly platform specific. Subclassing should not be attempted
 * without an intimate and detailed understanding of the workings of the
 * hierarchy. No support is provided for user-written classes which are
 * implemented as subclasses of this class.
 * </p>
 *
 * @see #checkSubclass
 */
public abstract class Widget {
	public int handle;
	int style, state;
	EventTable eventTable;
	Object data;
	String [] keys;
	Object [] values;
	
	static final int DISPOSED		= 0x00000001;
	static final int HANDLE			= 0x00000002;
	static final int CANVAS			= 0x00000004;
	
	static final int DEFAULT_WIDTH	= 64;
	static final int DEFAULT_HEIGHT = 64;

	static final char Mnemonic = '&';

Widget () {
	/* Do nothing */
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
 * for all SWT widget classes should include a comment which
 * describes the style constants which are applicable to the class.
 * </p>
 *
 * @param parent a widget which will be the parent of the new instance (cannot be null)
 * @param style the style of widget to construct
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
 * @see #checkSubclass
 * @see #getStyle
 */
public Widget (Widget parent, int style) {
	checkSubclass ();
	checkParent (parent);
	this.style = style;
}

static int checkBits (int style, int int0, int int1, int int2, int int3, int int4, int int5) {
	int mask = int0 | int1 | int2 | int3 | int4 | int5;
	if ((style & mask) == 0) style |= int0;
	if ((style & int0) != 0) style = (style & ~mask) | int0;
	if ((style & int1) != 0) style = (style & ~mask) | int1;
	if ((style & int2) != 0) style = (style & ~mask) | int2;
	if ((style & int3) != 0) style = (style & ~mask) | int3;
	if ((style & int4) != 0) style = (style & ~mask) | int4;
	if ((style & int5) != 0) style = (style & ~mask) | int5;
	return style;
}

void checkParent (Widget parent) {
	if (parent == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (!parent.isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (parent.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

protected void checkWidget () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (isDisposed ()) error (SWT.ERROR_WIDGET_DISPOSED);
}

int copyPhImage(int image) {
	if (image == 0) return 0;
	int newImage = OS.PiDuplicateImage (image, 0);

	/*
	* Bug in Photon.  The image returned by PiDuplicateImage might
	* have the same mask_bm/alpha as the original image.  The fix
	* is to detect this case and copy mask_bm/alpha if necessary.
	*/
	PhImage_t phImage = new PhImage_t();
	OS.memmove (phImage, image, PhImage_t.sizeof);
	PhImage_t newPhImage = new PhImage_t();
	OS.memmove(newPhImage, newImage, PhImage_t.sizeof);
	if (newPhImage.mask_bm != 0 && phImage.mask_bm == newPhImage.mask_bm) {
		int length = newPhImage.mask_bpl * newPhImage.size_h;
		int ptr = OS.malloc(length);
		OS.memmove(ptr, newPhImage.mask_bm, length);
		newPhImage.mask_bm = ptr;
	}
	if (newPhImage.alpha != 0 && phImage.alpha == newPhImage.alpha) {
		PgAlpha_t alpha = new PgAlpha_t();
		OS.memmove(alpha, phImage.alpha, PgAlpha_t.sizeof);
		if (alpha.src_alpha_map_map != 0) {
			int length = alpha.src_alpha_map_bpl * alpha.src_alpha_map_dim_h;
			int ptr = OS.malloc(length);
			OS.memmove(ptr, alpha.src_alpha_map_map, length);
			alpha.src_alpha_map_map = ptr;
		}
		int ptr = OS.malloc(PgAlpha_t.sizeof);
		OS.memmove(ptr, alpha, PgAlpha_t.sizeof);
		newPhImage.alpha = ptr;
	}
	OS.memmove(newImage, newPhImage, PhImage_t.sizeof);
	return newImage;
}

/**
 * Adds the listener to the collection of listeners who will
 * be notifed when an event of the given type occurs. When the
 * event does occur in the widget, the listener is notified by
 * sending it the <code>handleEvent()</code> message.
 *
 * @param eventType the type of event to listen for
 * @param listener the listener which should be notified when the event occurs
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Listener
 * @see #removeListener
 */
public void addListener (int eventType, Listener handler) {
	checkWidget();
	if (handler == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) eventTable = new EventTable ();
	eventTable.hook (eventType, handler);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notifed when the widget is disposed. When the widget is
 * disposed, the listener is notified by sending it the
 * <code>widgetDisposed()</code> message.
 *
 * @param listener the listener which should be notified when the receiver is disposed
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see DisposeListener
 * @see #removeDisposeListener
 */
public void addDisposeListener (DisposeListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Dispose, typedListener);
}

void createHandle (int index) {
	/* Do nothing */
}

int createToolTip (String string, int handle, byte [] font) {
	if (string == null || string.length () == 0 || handle == 0) {
		return 0;
	}

	int shellHandle = OS.PtFindDisjoint (handle);
	byte [] buffer = Converter.wcsToMbcs (null, string, true);
	Display display = getDisplay ();
	int fill = display.INFO_BACKGROUND;
	int text_color = display.INFO_FOREGROUND;
	int toolTipHandle = OS.PtInflateBalloon (shellHandle, handle, OS.Pt_BALLOON_RIGHT, buffer, font, fill, text_color);

	/*
	* Feature in Photon. The position of the inflated balloon
	* is relative to the widget position and not to the cursor
	* position. The fix is to re-position the balloon.
	*/
	int ig = OS.PhInputGroup (0);
	PhCursorInfo_t info = new PhCursorInfo_t ();
	OS.PhQueryCursor ((short)ig, info);
	short [] absX = new short [1], absY = new short [1];
	OS.PtGetAbsPosition (shellHandle, absX, absY);
	int x = info.pos_x - absX [0] + 16;
	int y = info.pos_y - absY [0] + 16;
	PhArea_t shellArea = new PhArea_t ();
	OS.PtWidgetArea (shellHandle, shellArea);
	PhArea_t toolTipArea = new PhArea_t ();
	OS.PtWidgetArea (toolTipHandle, toolTipArea);
	x = Math.max (0, Math.min (x, shellArea.size_w - toolTipArea.size_w));
	y = Math.max (0, Math.min (y, shellArea.size_h - toolTipArea.size_h));
	PhPoint_t pt = new PhPoint_t ();
	pt.x = (short) x;
	pt.y = (short) y;
	int ptr = OS.malloc (PhPoint_t.sizeof);
	OS.memmove (ptr, pt, PhPoint_t.sizeof);
	int [] args = {OS.Pt_ARG_POS, ptr, 0};
	OS.PtSetResources (toolTipHandle, args.length / 3, args);
	OS.free (ptr);

	return toolTipHandle;
}

void createWidget (int index) {
	createHandle (index);
	hookEvents ();
	register ();
}

void deregister () {
	if (handle == 0) return;
	WidgetTable.remove (handle);
}

void destroyToolTip (int toolTipHandle) {
	if (toolTipHandle != 0) OS.PtDestroyWidget (toolTipHandle);
}

void destroyWidget () {
	int topHandle = topHandle ();
	releaseHandle ();
	if (topHandle != 0) {
		OS.PtDestroyWidget (topHandle);
	}
}

/**
 * Disposes of the operating system resources associated with
 * the receiver and all its descendents. After this method has
 * been invoked, the receiver and all descendents will answer
 * <code>true</code> when sent the message <code>isDisposed()</code>.
 * Any internal connections between the widgets in the tree will
 * have been removed to facilitate garbage collection.
 * <p>
 * NOTE: This method is not called recursively on the descendents
 * of the receiver. This means that, widget implementers can not
 * detect when a widget is being disposed of by re-implementing
 * this method, but should instead listen for the <code>Dispose</code>
 * event.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #addDisposeListener
 * @see #removeDisposeListener
 * @see #checkWidget
 */
public void dispose () {
	/*
	* Note:  It is valid to attempt to dispose a widget
	* more than once.  If this happens, fail silently.
	*/
	if (isDisposed()) return;
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	releaseChild ();
	releaseWidget ();
	destroyWidget ();
}

static void error (int code) {
	SWT.error(code);
}

/**
 * Returns the application defined widget data associated
 * with the receiver, or null if it has not been set. The
 * <em>widget data</em> is a single, unnamed field that is
 * stored with every widget. 
 * <p>
 * Applications may put arbitrary objects in this field. If
 * the object stored in the widget data needs to be notified
 * when the widget is disposed of, it is the application's
 * responsibility to hook the Dispose event on the widget and
 * do so.
 * </p>
 *
 * @return the widget data
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - when the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - when called from the wrong thread</li>
 * </ul>
 *
 * @see #setData
 */
public Object getData () {
	checkWidget();
	return data;
}

/**
 * Returns the application defined property of the receiver
 * with the specified name, or null if it has not been set.
 * <p>
 * Applications may have associated arbitrary objects with the
 * receiver in this fashion. If the objects stored in the
 * properties need to be notified when the widget is disposed
 * of, it is the application's responsibility to hook the
 * Dispose event on the widget and do so.
 * </p>
 *
 * @param	key the name of the property
 * @return the value of the property or null if it has not been set
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the key is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #setData
 */
public Object getData (String key) {
	checkWidget();
	if (key == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (keys == null) return null;
	for (int i=0; i<keys.length; i++) {
		if (keys [i].equals (key)) return values [i];
	}
	return null;
}

/**
 * Returns the <code>Display</code> that is associated with
 * the receiver.
 * <p>
 * A widget's display is either provided when it is created
 * (for example, top level <code>Shell</code>s) or is the
 * same as its parent's display.
 * </p>
 *
 * @return the receiver's display
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public abstract Display getDisplay ();

String getName () {
	String string = getClass ().getName ();
	int index = string.length ();
	while (--index > 0 && string.charAt (index) != '.');
	return string.substring (index + 1, string.length ());
}

String getNameText () {
	return "";
}

/**
 * Returns the receiver's style information.
 * <p>
 * Note that the value which is returned by this method <em>may
 * not match</em> the value which was provided to the constructor
 * when the receiver was created. This can occur when the underlying
 * operating system does not support a particular combination of
 * requested styles. For example, if the platform widget used to
 * implement a particular SWT widget always has scroll bars, the
 * result of calling this method would always have the
 * <code>SWT.H_SCROLL</code> and <code>SWT.V_SCROLL</code> bits set.
 * </p>
 *
 * @return the style bits
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getStyle () {
	checkWidget();
	return style;
}

boolean hooks (int eventType) {
	if (eventTable == null) return false;
	return eventTable.hooks (eventType);
}

void hookEvents () {
	/* Do nothing */
}

/**
 * Returns <code>true</code> if the widget has been disposed,
 * and <code>false</code> otherwise.
 * <p>
 * This method gets the dispose state for the widget.
 * When a widget has been disposed, it is an error to
 * invoke any other method using the widget.
 * </p>
 *
 * @return <code>true</code> when the widget is disposed and <code>false</code> otherwise
 */
public boolean isDisposed () {
	if (handle != 0) return false;
	if ((state & HANDLE) != 0) return true;
	return (state & DISPOSED) != 0;
}

boolean isValidSubclass () {
	return Display.isValidClass (getClass ());
}

protected boolean isListening (int eventType) {
	checkWidget();
	return hooks (eventType);
}

boolean isValidThread () {
	return getDisplay ().isValidThread ();
}

/**
 * Notifies all of the receiver's listeners for events
 * of the given type that one such event has occurred by
 * invoking their <code>handleEvent()</code> method.
 *
 * @param eventType the type of event which has occurred
 * @param event the event data
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the event is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void notifyListeners (int eventType, Event event) {
	checkWidget();
	if (event == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	event.type = eventType;
	event.widget = this;
	eventTable.sendEvent (event);
}

void postEvent (int eventType) {
	if (eventTable == null) return;
	postEvent (eventType, new Event ());
}

void postEvent (int eventType, Event event) {
	if (eventTable == null) return;
	event.type = eventType;
	event.widget = this;
	if (event.time == 0) {
		event.time = (int) System.currentTimeMillis ();
	}
	getDisplay ().postEvent (event);
}

int processActivate (int info) {
	return OS.Pt_CONTINUE;
}

int processArm (int info) {
	return OS.Pt_CONTINUE;
}

int processDefaultSelection (int info) {
	return OS.Pt_CONTINUE;
}

int processFocusIn (int info) {
	return OS.Pt_CONTINUE;
}

int processFocusOut (int info) {
	return OS.Pt_CONTINUE;
}

int processHide (int info) {
	return OS.Pt_CONTINUE;
}

int processHotkey (int data, int info) {
	return OS.Pt_CONTINUE;
}

int processKey (int info) {
	return OS.Pt_CONTINUE;
}

int processEvent (int widget, int data, int info) {
	switch (data) {
		case SWT.Activate:			return processActivate (info);
		case SWT.Arm:				return processArm (info);
//		case SWT.Dispose:			return processDispose (info);
		case SWT.DefaultSelection:	return processDefaultSelection (info);
		case SWT.FocusIn:			return processFocusIn (info);
		case SWT.FocusOut:			return processFocusOut (info);
//		case SWT.Help:				return processHelp (info);
		case SWT.Hide:				return processHide (info);
		case SWT.KeyDown:			
		case SWT.KeyUp:				return processKey (info);
		case SWT.Modify:			return processModify (info);
		case SWT.MouseDown:			
		case SWT.MouseMove:
		case SWT.MouseUp:			return processMouse (info);
		case SWT.MouseEnter:		return processMouseEnter (info);
//		case SWT.MouseExit:			return processMouseOut (info);
		case SWT.Move:				return processMove (info);
//		case SWT.Paint:				return processPaint (info);
		case SWT.Resize:			return processResize (info);
		case SWT.Show:				return processShow (info);
		case SWT.Selection:			return processSelection (info);
		case SWT.Verify:			return processVerify (info);
	}
	return OS.Pt_CONTINUE;
}

int processModify (int info) {
	return OS.Pt_CONTINUE;
}

int processMouse (int info) {
	return OS.Pt_CONTINUE;
}

int processMouseEnter (int info) {
	return OS.Pt_CONTINUE;
}

int processMove (int info) {
	return OS.Pt_CONTINUE;
}

int processPaint (int damage) {
	return OS.Pt_CONTINUE;
}

int processResize (int info) {
	return OS.Pt_CONTINUE;
}

int processShow (int info) {
	return OS.Pt_CONTINUE;
}

int processSelection (int info) {
	return OS.Pt_CONTINUE;
}

int processVerify (int info) {
	return OS.Pt_CONTINUE;
}

void releaseChild () {
	/* Do nothing */
}

void register () {
	if (handle == 0) return;
	WidgetTable.put (handle, this);
}

void releaseHandle () {
	handle = 0;
	state |= DISPOSED;
}

void releaseWidget () {
	sendEvent (SWT.Dispose);
	deregister ();
	eventTable = null;
	data = null;
	keys = null;
	values = null;
}

/**
 * Removes the listener from the collection of listeners who will
 * be notifed when an event of the given type occurs.
 *
 * @param eventType the type of event to listen for
 * @param listener the listener which should no longer be notified when the event occurs
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Listener
 * @see #addListener
 */
public void removeListener (int eventType, Listener handler) {
	checkWidget();
	if (handler == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (eventType, handler);
}

protected void removeListener (int eventType, SWTEventListener handler) {
	checkWidget();
	if (handler == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (eventType, handler);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notifed when the widget is disposed.
 *
 * @param listener the listener which should no longer be notified when the receiver is disposed
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see DisposeListener
 * @see #removeDisposeListener
 */
public void removeDisposeListener (DisposeListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Dispose, listener);
}

void replaceMnemonic (int mnemonic, boolean normal, boolean alt) {
	Display display = getDisplay ();
	int [] args = {OS.Pt_ARG_ACCEL_KEY, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	if (args [1] != 0) {
		int length = OS.strlen (args [1]);
		if (length > 0) {
			byte [] buffer = new byte [length];
			OS.memmove (buffer, args [1], length);
			char [] accelText = Converter.mbcsToWcs (null, buffer);
			if (accelText.length > 0) {
				char key = Character.toLowerCase (accelText [0]);
				if (normal) {
					OS.PtRemoveHotkeyHandler (handle, key, 0, (short)0, SWT.Activate, display.windowProc);
				}
				if (alt) {
					OS.PtRemoveHotkeyHandler (handle, key, OS.Pk_KM_Alt, (short)0, SWT.Activate, display.windowProc);
				}
			}
		}
	}
	if (mnemonic == 0) return;
	char key = Character.toLowerCase ((char)mnemonic);
	if (normal) {
		OS.PtAddHotkeyHandler (handle, key, 0, (short)0, SWT.Activate, display.windowProc);
	}
	if (alt) {
		OS.PtAddHotkeyHandler (handle, key, OS.Pk_KM_Alt, (short)0, SWT.Activate, display.windowProc);
	}
}

void sendEvent (int eventType) {
	if (eventTable == null) return;
	sendEvent (eventType, new Event ());
}

void sendEvent (int eventType, Event event) {
	if (eventTable == null) return;
	event.widget = this;
	event.type = eventType;
	if (event.time == 0) {
		event.time = (int) System.currentTimeMillis ();
	}
	eventTable.sendEvent (event);
}

/**
 * Sets the application defined widget data associated
 * with the receiver to be the argument. The <em>widget
 * data</em> is a single, unnamed field that is stored
 * with every widget. 
 * <p>
 * Applications may put arbitrary objects in this field. If
 * the object stored in the widget data needs to be notified
 * when the widget is disposed of, it is the application's
 * responsibility to hook the Dispose event on the widget and
 * do so.
 * </p>
 *
 * @param data the widget data
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - when the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - when called from the wrong thread</li>
 * </ul>
 */
public void setData (Object data) {
	checkWidget();
	this.data = data;
}

/**
 * Sets the application defined property of the receiver
 * with the specified name to the given value.
 * <p>
 * Applications may associate arbitrary objects with the
 * receiver in this fashion. If the objects stored in the
 * properties need to be notified when the widget is disposed
 * of, it is the application's responsibility to hook the
 * Dispose event on the widget and do so.
 * </p>
 *
 * @param key the name of the property
 * @param value the new value for the property
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the key is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #getData
 */
public void setData (String key, Object value) {
	checkWidget();
	if (key == null) error (SWT.ERROR_NULL_ARGUMENT);
	
	/* Remove the key/value pair */
	if (value == null) {
		if (keys == null) return;
		int index = 0;
		while (index < keys.length && !keys [index].equals (key)) index++;
		if (index == keys.length) return;
		if (keys.length == 1) {
			keys = null;
			values = null;
		} else {
			String [] newKeys = new String [keys.length - 1];
			Object [] newValues = new Object [values.length - 1];
			System.arraycopy (keys, 0, newKeys, 0, index);
			System.arraycopy (keys, index + 1, newKeys, index, newKeys.length - index);
			System.arraycopy (values, 0, newValues, 0, index);
			System.arraycopy (values, index + 1, newValues, index, newValues.length - index);
			keys = newKeys;
			values = newValues;
		}
		return;
	}
	
	/* Add the key/value pair */
	if (keys == null) {
		keys = new String [] {key};
		values = new Object [] {value};
		return;
	}
	for (int i=0; i<keys.length; i++) {
		if (keys [i].equals (key)) {
			values [i] = value;
			return;
		}
	}
	String [] newKeys = new String [keys.length + 1];
	Object [] newValues = new Object [values.length + 1];
	System.arraycopy (keys, 0, newKeys, 0, keys.length);
	System.arraycopy (values, 0, newValues, 0, values.length);
	newKeys [keys.length] = key;
	newValues [values.length] = value;
	keys = newKeys;
	values = newValues;
}

void setKeyState(Event event, PhKeyEvent_t ke) {
	int key_mods = ke.key_mods;
	int button_state = ke.button_state;
	if ((key_mods & OS.Pk_KM_Alt) != 0) event.stateMask |= SWT.ALT;
	if ((key_mods & OS.Pk_KM_Shift) != 0) event.stateMask |= SWT.SHIFT;
	if ((key_mods & OS.Pk_KM_Ctrl) != 0) event.stateMask |= SWT.CONTROL;
	if ((button_state & OS.Ph_BUTTON_SELECT) != 0) event.stateMask |= SWT.BUTTON1;
	if ((button_state & OS.Ph_BUTTON_ADJUST) != 0) event.stateMask |= SWT.BUTTON2;
	if ((button_state & OS.Ph_BUTTON_MENU) != 0) event.stateMask |= SWT.BUTTON3;
	switch (event.type) {
		case SWT.KeyDown:
		case SWT.Traverse:
			if (event.keyCode == SWT.ALT) event.stateMask &= ~SWT.ALT;
			if (event.keyCode == SWT.SHIFT) event.stateMask &= ~SWT.SHIFT;
			if (event.keyCode == SWT.CONTROL) event.stateMask &= ~SWT.CONTROL;
			break;
		case SWT.KeyUp:
			if (event.keyCode == SWT.ALT) event.stateMask |= SWT.ALT;
			if (event.keyCode == SWT.SHIFT) event.stateMask |= SWT.SHIFT;
			if (event.keyCode == SWT.CONTROL) event.stateMask |= SWT.CONTROL;
			break;
	}
}

void setMouseState(int type, Event event, PhPointerEvent_t pe) {
	int key_mods = pe.key_mods;
	int buttons = pe.buttons;
	int button_state = pe.button_state;
	if ((key_mods & OS.Pk_KM_Alt) != 0) event.stateMask |= SWT.ALT;
	if ((key_mods & OS.Pk_KM_Shift) != 0) event.stateMask |= SWT.SHIFT;
	if ((key_mods & OS.Pk_KM_Ctrl) != 0) event.stateMask |= SWT.CONTROL;
	if ((button_state & OS.Ph_BUTTON_SELECT) != 0) event.stateMask |= SWT.BUTTON1;
	if ((button_state & OS.Ph_BUTTON_ADJUST) != 0) event.stateMask |= SWT.BUTTON2;
	if ((button_state & OS.Ph_BUTTON_MENU) != 0) event.stateMask |= SWT.BUTTON3;
	switch (type) {
		case OS.Ph_EV_BUT_PRESS:
			if (buttons == OS.Ph_BUTTON_SELECT) event.stateMask &= ~SWT.BUTTON1;
			if (buttons == OS.Ph_BUTTON_ADJUST) event.stateMask &= ~SWT.BUTTON2;
			if (buttons == OS.Ph_BUTTON_MENU) event.stateMask &= ~SWT.BUTTON3;
			break;
		case OS.Ph_EV_BUT_RELEASE:
		case OS.Ph_EV_DRAG:
			if (buttons == OS.Ph_BUTTON_SELECT) event.stateMask |= SWT.BUTTON1;
			if (buttons == OS.Ph_BUTTON_ADJUST) event.stateMask |= SWT.BUTTON2;
			if (buttons == OS.Ph_BUTTON_MENU) event.stateMask |= SWT.BUTTON3;
			break;
	}
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the receiver
 */
public String toString () {
	String string = "*Disposed*";
	if (!isDisposed ()) {
		string = "*Wrong Thread*";
		if (isValidThread ()) string = getNameText ();
	}
	return getName () + " {" + string + "}";
}

int topHandle () {
	return handle;
}

}
