package org.eclipse.swt.widgets;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2000  All Rights Reserved
 */

/* Imports */
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;
import java.util.EventListener;

/* Class Definition */
public abstract class Widget {

	public int handle;
	int style, state;
	EventTable eventTable;
	Object data;
	String [] keys;
	Object [] values;
	
	/* Global state flags */
//	static final int AUTOMATIC		= 0x00000001;
//	static final int ACTIVE			= 0x00000002;
//	static final int AUTOGRAB		= 0x00000004;
//	static final int MULTIEXPOSE	= 0x00000008;
//	static final int RESIZEREDRAW	= 0x00000010;
//	static final int WRAP			= 0x00000020;
//	static final int DISABLED		= 0x00000040;
//	static final int HIDDEN			= 0x00000080;
//	static final int FOREGROUND		= 0x00000100;
//	static final int BACKGROUND		= 0x00000200;
	static final int DISPOSED		= 0x00000400;
	static final int HANDLE			= 0x00000800;
	static final int CANVAS			= 0x00001000;
	
	static final int DEFAULT_WIDTH	= 64;
	static final int DEFAULT_HEIGHT	= 64;
	
	/* Global widget variables */
	static final char Mnemonic = '&';
	static final boolean IsAIX, IsSunOS, IsLinux;
	static {
		
		/* Initialize the X/MOTIF flags */
		String osName = System.getProperty ("os.name");
		if (osName.equals("Linux")) {
			IsLinux = true;
			IsAIX = IsSunOS = false;
		} else {
			if (osName.equals("AIX")) {
				IsAIX = true;
				IsLinux = IsSunOS = false;
			} else {
				if (osName.equals("Solaris")) {
					IsSunOS = true;
					IsLinux = IsAIX = false;
				} else {
					IsLinux = IsSunOS = IsAIX = false;
				}
			}
		}
	}
Widget () {
	/* Do nothing */
}
/**
* Creates a widget.
* <p>
*	This method creates a child widget using style bits
* to select a particular look or set of properties. 
*
* @param parent	a composite widget (cannot be null)
* @param style	the bitwise OR'ing of widget styles
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_ERROR_NULL_ARGUMENT)
*	when the parent is null
*/
public Widget (Widget parent, int style) {
	if (parent == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (!parent.isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	this.style = style;
}
/**
* Adds a listener for an event.
* <p>
*	This method adds a listener for an event.  When the
* event occurs in the widget, the listener is notified
* using <code>handleEvent ()</code>.
*
* @param eventType the desired SWT event
* @param handler  the event handler
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when handler is null
*/
public void addListener (int eventType, Listener handler) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (handler == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) eventTable = new EventTable ();
	eventTable.hook (eventType, handler);
}
/**	 
* Adds the listener to receive events.
* <p>
*
* @param listener the listener
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when listener is null
*/
public void addDisposeListener (DisposeListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Dispose, typedListener);
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
}
/**
 * Checks that this class can be subclassed.
 * <p>
 * IMPORTANT: By providing an implementation of this method that
 * allows subclassing in a subclass of this class, you agree to
 * be fully responsible for the fact that any such subclass will
 * likely fail between releases and will be strongly platform
 * specific.  No support is provided for classes which are implemented
 * as subclasses of this class.  The use of subclassing is intended
 * purely to enable those outside of the development team to implement
 * a patched version of the class in an emergency.  Subclassing should
 * not be attempted without an intimate and detailed understanding of
 * the hierarchy.
 *
 * @exception SWTError <ul>
 *		<li>ERROR_ILLEGAL_SUBCLASS when called</li>
 *	</ul>
 */
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}
/**
* Checks access to a widget.
* <p>
* Throws an exception when access to the widget
* is denied.
*
* It is an error to call a method on a widget that
* has been disposed or to call a method on a widget
* from a thread that is different from the thread
* that created the widget.  There may be more or
* less error checks in future or different versions
* of SWT may issue different errors.
*
* This method is intended to be called by widget
* implementors to enforce the standard SWT error
* checking prologue in API methods.
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
protected void checkWidget () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
}
void createHandle (int index) {
	/* Do nothing */
}
void createWidget (int index) {
	createHandle (index);
	hookEvents ();
	register ();
	manageChildren ();
}
void deregister () {
	if (handle == 0) return;
	WidgetTable.remove (handle);
}
void destroyWidget () {
	int topHandle = topHandle ();
	releaseHandle ();
	if (topHandle != 0) {
		OS.XtDestroyWidget (topHandle);
	}
}
/**
* Disposes a widget.
* <p>
* This method destroys the widget and all children
* and releases all platform resources associated with
* the widget tree.
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void dispose () {
	/*
	* Note:  It is valid to attempt to dispose a widget
	* more than once.  If this happens, fail silently.
	*/
	if (!isValidWidget ()) return;
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	releaseChild ();
	releaseWidget ();
	destroyWidget ();
}
void error (int code) {
	SWT.error(code);
}
/**
* Gets the widget data.
* <p>
*	This method gets the application defined data that
* is associated with the widget.
*
* @return the widget data
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public Object getData () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return data;
}

/**
 * Searches for the property with the specified name.
 * If the property is not found, null is returned.
 *
 * @param	key the name of the property to find
 * @return	the named property value
 *
 * @exception SWTError <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *		<li>ERROR_NULL_ARGUMENT when the name is null</li>
 *	</ul>
 */
public Object getData (String key) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (key == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (keys == null) return null;
	for (int i=0; i<keys.length; i++) {
		if (keys [i].equals (key)) return values [i];
	}
	return null;
}

/**
* Gets the Display.
* <p>
*	This method gets the Display that is associated
* with the widget.
*
* @return the widget data
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public abstract Display getDisplay ();
String getName () {
	String string = getClass ().getName ();
	int index = string.lastIndexOf ('.');
	if (index == -1) return string;
	return string.substring (index + 1, string.length ());
}
String getNameText () {
	return "";
}
/**
* Gets the widget style.
* <p>
* This method gets the widget style bits.
*
* @return an integer that is the widget style bits.
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public int getStyle () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return style;
}
void hookEvents () {
	/* Do nothing */
}
boolean hooks (int eventType) {
	if (eventTable == null) return false;
	return eventTable.hooks (eventType);
}
/**
* Gets the disposed state.
* <p>
* This method gets the dispose state for the widget.
* When a widget has been disposed, it is an error to
* to invoke any other method on the widget.
*
* @return true when the widget is disposed
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
*/
public boolean isDisposed () {
	if (handle != 0) return false;
	if ((state & HANDLE) != 0) return true;
	return (state & DISPOSED) != 0;
}
/**
 * Return the listening state.
 * <p>
 * Returns true if there is a listener, listening for the eventType.
 * Otherwise, returns false.
 *
 * @param	eventType the type of event
 * @return	true if the event is hooked
 *
 * @exception SWTError <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *		<li>ERROR_NULL_ARGUMENT when the name is null</li>
 *	</ul>
 */
protected boolean isListening (int eventType) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return hooks (eventType);
}
boolean isValidSubclass () {
	return Display.isValidClass (getClass ());
}
boolean isValidThread () {
	return getDisplay ().isValidThread ();
}
boolean isValidWidget () {
	if (handle != 0) return true;
	if ((state & HANDLE) != 0) return false;
	return (state & DISPOSED) == 0;
}
void manageChildren () {
	/* Do nothing */
}
char mbcsToWcs (char ch) {
	int key = ch & 0xFFFF;
	if (key <= 0x7F) return ch;
	byte [] buffer;
	if (key <= 0xFF) {
		buffer = new byte [1];
		buffer [0] = (byte) key;
	} else {
		buffer = new byte [2];
		buffer [0] = (byte) ((key >> 8) & 0xFF);
		buffer [1] = (byte) (key & 0xFF);
	}
	char [] result = Converter.mbcsToWcs (null, buffer);
	if (result.length == 0) return 0;
	return result [0];
}
/**
* Notify listeners of an event.
* <p>
*	This method notifies all listeners that an event
* has occurred.
*
* @param eventType the desired SWT event
* @param event the event data
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when handler is null
*/
public void notifyListeners (int eventType, Event event) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
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
	Display display = getDisplay ();
	if (event.time == 0) {
		event.time = OS.XtLastTimestampProcessed (display.xDisplay);
	}
	display.postEvent (event);
}
int processArm (int callData) {
	return 0;
}
int processDispose (int callData) {
	return 0;
}
int processDefaultSelection (int callData) {
	return 0;
}
int processEvent (int eventNumber, int callData) {
	switch (eventNumber) {
		case SWT.Arm:			return processArm (callData);
		case SWT.Dispose:		return processDispose (callData);
		case SWT.DefaultSelection:	return processDefaultSelection (callData);
		case SWT.FocusIn:		return processSetFocus (callData);
		case SWT.Help:			return processHelp (callData);
		case SWT.Hide:			return processHide (callData);
		case SWT.KeyDown:		return processKeyDown (callData);
		case SWT.KeyUp:			return processKeyUp (callData);
		case SWT.Modify:		return processModify (callData);
		case SWT.MouseDown:		return processMouseDown (callData);
		case SWT.MouseEnter:		return processMouseEnter (callData);
		case SWT.MouseExit:		return processMouseExit (callData);
		case SWT.MouseHover:		return processMouseHover (callData);
		case SWT.MouseMove:		return processMouseMove (callData);
		case SWT.MouseUp:		return processMouseUp (callData);
		case SWT.Paint:			return processPaint (callData);
		case SWT.Resize:		return processResize (callData);
		case SWT.Show:			return processShow (callData);
		case SWT.Selection:		return processSelection (callData);
		case SWT.Verify:		return processVerify (callData);
		case -1:			return processNonMaskable (callData);
	}
	return 0;
}
int processHelp (int callData) {
	return 0;
}
int processHide (int callData) {
	return 0;
}
int processKeyDown (int callData) {
	return 0;
}
int processKeyUp (int callData) {
	return 0;
}
int processModify (int callData) {
	return 0;
}
int processMouseDown (int callData) {
	return 0;
}
int processMouseEnter (int callData) {
	return 0;
}
int processMouseExit (int callData) {
	return 0;
}
int processMouseHover (int id) {
	return 0;
}
int processMouseMove (int callData) {
	return 0;
}
int processMouseUp (int callData) {
	return 0;
}
int processNonMaskable (int callData) {
	return 0;
}
int processPaint (int callData) {
	return 0;
}
int processResize (int callData) {
	return 0;
}
int processSelection (int callData) {
	return 0;
}
int processSetFocus (int callData) {
	return 0;
}
int processShow (int callData) {
	return 0;
}
int processVerify (int callData) {
	return 0;
}
void register () {
	if (handle == 0) return;
	WidgetTable.put (handle, this);
}
void releaseChild () {
	/* Do nothing */
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
* Removes a listener for an event.
* <p>
*	This method removes a listener for an event.  When the
* event occurs in the widget, the listener is no longer
* notified.
*
* @param eventType the desired SWT event
* @param handler the event handler
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when handler is null
*/
public void removeListener (int eventType, Listener handler) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (handler == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (eventType, handler);
}
/**
* Warning: API under construction.
*/
protected void removeListener (int eventType, EventListener handler) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (handler == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (eventType, handler);
}
/**	 
* Removes the listener.
* <p>
*
* @param listener the listener
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when listener is null
*/
public void removeDisposeListener (DisposeListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Dispose, listener);
}
void sendEvent (int eventType) {
	if (eventTable == null) return;
	sendEvent (eventType, new Event ());
}
void sendEvent (int eventType, Event event) {
	if (eventTable == null) return;
	event.type = eventType;
	event.widget = this;
	if (event.time == 0) {
		Display display = getDisplay ();
		event.time = OS.XtLastTimestampProcessed (display.xDisplay);
	}
	eventTable.sendEvent (event);
}
/**
* Sets the widget data.
* <p>
*	This method sets the application defined data that
* is associated with the widget.
*
* @param the widget data
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void setData (Object data) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	this.data = data;
}

/**
 * Maps the specified key to the specified value.
 * If the key already exists, the old value is replaced.
 * The key cannot be null.
 *
 * @param		key the name of the property
 * @param		value the value of the property
 *
 * @exception SWTError <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *		<li>ERROR_NULL_ARGUMENT when the name is null</li>
 *	</ul>
 */
public void setData (String key, Object value) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
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

/**
* Returns a string representation of the object.
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
char wcsToMbcs (char ch) {
	int key = ch & 0xFFFF;
	if (key <= 0x7F) return ch;
	byte [] buffer = Converter.wcsToMbcs (null, new char [] {ch}, false);
	if (buffer.length == 1) return (char) buffer [0];
	if (buffer.length == 2) {
		return (char) (((buffer [0] & 0xFF) << 8) | (buffer [1] & 0xFF));
	}
	return 0;
}
}
