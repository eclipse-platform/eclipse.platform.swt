package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.carbon.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;

public abstract class Widget {
	int style, state;
	EventTable eventTable;
	Object data;
	String [] keys;
	Object [] values;

	/* Global state flags */
//	static final int AUTOMATIC		= 1 << 0;
//	static final int ACTIVE			= 1 << 1;
//	static final int AUTOGRAB		= 1 << 2;
//	static final int MULTIEXPOSE	= 1 << 3;
//	static final int RESIZEREDRAW	= 1 << 4;
//	static final int WRAP			= 1 << 5;
//	static final int DISABLED		= 1 << 6;
//	static final int HIDDEN		= 1 << 7;
//	static final int FOREGROUND		= 1 << 8;
//	static final int BACKGROUND		= 1 << 9;
	static final int DISPOSED	= 1 << 10;
//	static final int HANDLE		= 1 << 11;
	static final int CANVAS		= 1 << 12;

	static final int DEFAULT_WIDTH	= 64;
	static final int DEFAULT_HEIGHT	= 64;
	static final char Mnemonic = '&';
	
	static final boolean EMULATE_RIGHT_BUTTON = true;

Widget () {
	/* Do nothing */
}

public Widget (Widget parent, int style) {
	checkSubclass ();
	checkParent (parent);
	this.style = style;
}

public void addListener (int eventType, Listener handler) {
	checkWidget();
	if (handler == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) eventTable = new EventTable ();
	eventTable.hook (eventType, handler);
}

public void addDisposeListener (DisposeListener listener) {
	checkWidget();
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
	if (parent.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

protected void checkWidget () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (isDisposed ()) error (SWT.ERROR_WIDGET_DISPOSED);
}

void destroyWidget () {
	releaseHandle ();
}

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

void error (int code) {
	SWT.error(code);
}

boolean filters (int eventType) {
	Display display = getDisplay ();
	return display.filters (eventType);
}

public Object getData () {
	checkWidget();
	return data;
}

public Object getData (String key) {
	checkWidget();
	if (key == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (keys == null) return null;
	for (int i=0; i<keys.length; i++) {
		if (keys [i].equals (key)) return values [i];
	}
	return null;
}

public abstract Display getDisplay ();

String getName () {
	String string = getClass ().getName ();
	int index = string.lastIndexOf(".");
	if (index == -1) return string;
	return string.substring(index + 1, string.length ());
}

String getNameText () {
	return "";
}

public int getStyle () {
	checkWidget();
	return style;
}

boolean hooks (int eventType) {
	if (eventTable == null) return false;
	return eventTable.hooks (eventType);
}

public boolean isDisposed () {
	return (state & DISPOSED) != 0;
}

protected boolean isListening (int eventType) {
	checkWidget();
	return hooks (eventType);
}

boolean isValidSubclass () {
	return Display.isValidClass (getClass ());
}

boolean isValidThread () {
	return getDisplay ().isValidThread ();
}

public void notifyListeners (int eventType, Event event) {
	checkWidget();
	if (event == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	event.type = eventType;
	event.widget = this;
	eventTable.sendEvent (event);
}

void postEvent (int eventType) {
	sendEvent (eventType, null, false);
}

void postEvent (int eventType, Event event) {
	sendEvent (eventType, event, false);
}

void releaseChild () {
	/* Do nothing */
}

void releaseHandle () {
	state |= DISPOSED;
}

void releaseResources () {
	releaseWidget ();
	releaseHandle ();
}

void releaseWidget () {
	sendEvent (SWT.Dispose);
	eventTable = null;
	data = null;
	keys = null;
	values = null;
}

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

public void removeDisposeListener (DisposeListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Dispose, listener);
}

void sendEvent (Event event) {
	Display display = event.display;
	if (!display.filterEvent (event)) {
		if (eventTable != null) eventTable.sendEvent (event);
	}
}

void sendEvent (int eventType) {
	sendEvent (eventType, null, true);
}

void sendEvent (int eventType, Event event) {
	sendEvent (eventType, event, true);
}

void sendEvent (int eventType, Event event, boolean send) {
	Display display = getDisplay ();
	if (eventTable == null && !display.filters (eventType)) {
		return;
	}
	if (event == null) event = new Event ();
	event.type = eventType;
	event.display = display;
	event.widget = this;
	if (event.time == 0) {
		event.time = display.getLastEventTime ();
	}
	if (send) {
		sendEvent (event);
	} else {
		display.postEvent (event);
	}
}

public void setData (Object data) {
	checkWidget();
	this.data = data;
}

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

void setInputState (Event event, int theEvent) {
	int [] modifiers = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamKeyModifiers, OS.typeUInt32, null, modifiers.length * 4, null, modifiers);
	if ((modifiers [0] & OS.optionKey) != 0) event.stateMask |= SWT.ALT;
	if ((modifiers [0] & OS.shiftKey) != 0) event.stateMask |= SWT.SHIFT;
	if ((modifiers [0] & OS.controlKey) != 0) event.stateMask |= SWT.CONTROL;
	if ((modifiers [0] & OS.cmdKey) != 0) event.stateMask |= SWT.COMMAND;
	short[] button = new short[1];
	OS.GetEventParameter (theEvent, OS.kEventParamMouseButton, OS.typeMouseButton, null, button.length * 2, null, button);
	switch (button [0]) {
		case OS.kEventMouseButtonPrimary: event.stateMask |= SWT.BUTTON1; break;
		case OS.kEventMouseButtonSecondary: event.stateMask |= SWT.BUTTON3; break;
		case OS.kEventMouseButtonTertiary:	event.stateMask |= SWT.BUTTON2; break;
	}
	switch (event.type) {
		case SWT.MouseDown:
		case SWT.MouseDoubleClick:
			if (event.button == 1) event.stateMask &= ~SWT.BUTTON1;
			if (event.button == 2) event.stateMask &= ~SWT.BUTTON2;
			if (event.button == 3)  event.stateMask &= ~SWT.BUTTON3;
			break;
		case SWT.MouseUp:
			if (event.button == 1) event.stateMask |= SWT.BUTTON1;
			if (event.button == 2) event.stateMask |= SWT.BUTTON2;
			if (event.button == 3) {
				event.stateMask |= SWT.BUTTON3;	
				if (EMULATE_RIGHT_BUTTON) event.stateMask &= ~SWT.CONTROL;
			}
			break;
		case SWT.KeyDown:
		case SWT.Traverse: {
			if (event.keyCode != 0 || event.character != 0) return;
			Display display = getDisplay ();
			int lastModifiers = display.lastModifiers;
			if ((modifiers [0] & OS.shiftKey) != 0 && (lastModifiers & OS.shiftKey) == 0) {
				event.stateMask &= ~SWT.SHIFT;
				event.keyCode = SWT.SHIFT;
				return;
			}
			if ((modifiers [0] & OS.controlKey) != 0 && (lastModifiers & OS.controlKey) == 0) {
				event.stateMask &= ~SWT.CONTROL;
				event.keyCode = SWT.CONTROL;
				return;
			}
			if ((modifiers [0] & OS.cmdKey) != 0 && (lastModifiers & OS.cmdKey) == 0) {
				event.stateMask &= ~SWT.COMMAND;
				event.keyCode = SWT.COMMAND;
				return;
			}	
			if ((modifiers [0] & OS.optionKey) != 0 && (lastModifiers & OS.optionKey) == 0) {
				event.stateMask &= ~SWT.ALT;
				event.keyCode = SWT.ALT;
				return;
			}
			break;
		}
		case SWT.KeyUp: {
			if (event.keyCode != 0 || event.character != 0) return;
			Display display = getDisplay ();
			int lastModifiers = display.lastModifiers;
			if ((modifiers [0] & OS.shiftKey) == 0 && (lastModifiers & OS.shiftKey) != 0) {
				event.stateMask |= SWT.SHIFT;
				event.keyCode = SWT.SHIFT;
				return;
			}
			if ((modifiers [0] & OS.controlKey) == 0 && (lastModifiers & OS.controlKey) != 0) {
				event.stateMask |= SWT.CONTROL;
				event.keyCode = SWT.CONTROL;
				return;
			}
			if ((modifiers [0] & OS.cmdKey) == 0 && (lastModifiers & OS.cmdKey) != 0) {
				event.stateMask |= SWT.COMMAND;
				event.keyCode = SWT.COMMAND;
				return;
			}	
			if ((modifiers [0] & OS.optionKey) != 0 && (lastModifiers & OS.optionKey) == 0) {
				event.stateMask |= SWT.ALT;
				event.keyCode = SWT.ALT;
				return;
			}
			break;
		}
	}
}

void setKeyState (Event event, int theEvent) {
	int [] keyCode = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamKeyCode, OS.typeUInt32, null, keyCode.length * 4, null, keyCode);
	event.keyCode = Display.translateKey (keyCode [0]);
	switch (event.keyCode) {
		case 0:
		case SWT.BS:
		case SWT.CR:
		case SWT.DEL:
		case SWT.ESC:
		case SWT.TAB: {
			byte [] charCode = new byte [1];
			OS.GetEventParameter (theEvent, OS.kEventParamKeyMacCharCodes, OS.typeChar, null, charCode.length, null, charCode);
			event.character = (char) charCode [0];
			break;
		}
		case SWT.LF:
			event.character = '\n';
			break;
	}
	setInputState (event, theEvent);
}

public String toString () {
	String string = "*Disposed*";
	if (!isDisposed ()) {
		string = "*Wrong Thread*";
		if (isValidThread ()) string = getNameText ();
	}
	return getName () + " {" + string + "}";
}
}
