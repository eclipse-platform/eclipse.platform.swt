/*******************************************************************************
 * Copyright (c) 2025 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

/**
 * Headless implementation of Widget for SWT.
 */
public abstract class Widget {
	public long handle;
	int style, state;
	Display display;
	EventTable eventTable;
	Object data;
	
	/* Global state flags */
	static final int DISPOSED = 1<<0;
	static final int CANVAS = 1<<1;
	static final int KEYED_DATA = 1<<2;
	static final int HANDLE = 1<<3;
	static final int DISABLED = 1<<4;
	static final int MENU = 1<<5;
	static final int OBSCURED = 1<<6;
	static final int MOVED = 1<<7;
	static final int RESIZED = 1<<8;
	static final int ZERO_WIDTH = 1<<9;
	static final int ZERO_HEIGHT = 1<<10;
	static final int HIDDEN = 1<<11;
	static final int FOREGROUND = 1<<12;
	static final int BACKGROUND = 1<<13;
	static final int FONT = 1<<14;
	static final int PARENT_BACKGROUND = 1<<15;
	static final int THEME_BACKGROUND = 1<<16;
	static final int LAYOUT_NEEDED = 1<<17;
	static final int LAYOUT_CHANGED = 1<<18;
	static final int LAYOUT_CHILD = 1<<19;
	static final int RELEASED = 1<<20;
	static final int DISPOSE_SENT = 1<<21;
	static final int FOREIGN_HANDLE = 1<<22;
	static final int DRAG_DETECT = 1<<23;
	static final int SKIN_NEEDED = 1<<24;
	
	/* Default sizes */
	static final int DEFAULT_WIDTH = 64;
	static final int DEFAULT_HEIGHT = 64;

public Widget() {
	// No-op
}

public Widget(Widget parent, int style) {
	checkSubclass();
	checkParent(parent);
	this.style = style;
	display = parent.display;
}

protected void checkSubclass() {
	if (!isValidSubclass()) error(SWT.ERROR_INVALID_SUBCLASS);
}

protected void checkParent(Widget parent) {
	if (parent == null) error(SWT.ERROR_NULL_ARGUMENT);
	if (parent.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	if (parent.display != display) error(SWT.ERROR_INVALID_PARENT);
}

protected void checkWidget() {
	Display display = this.display;
	if (display == null) error(SWT.ERROR_WIDGET_DISPOSED);
	if (display.thread != Thread.currentThread()) error(SWT.ERROR_THREAD_INVALID_ACCESS);
	if ((state & DISPOSED) != 0) error(SWT.ERROR_WIDGET_DISPOSED);
}

static int checkBits(int style, int int0, int int1, int int2, int int3, int int4, int int5) {
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

public void addListener(int eventType, Listener listener) {
	checkWidget();
	if (listener == null) error(SWT.ERROR_NULL_ARGUMENT);
	_addListener(eventType, listener);
}

void _addListener(int eventType, Listener listener) {
	if (eventTable == null) eventTable = new EventTable();
	eventTable.hook(eventType, listener);
}

public void addDisposeListener(DisposeListener listener) {
	checkWidget();
	if (listener == null) error(SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);
	addListener(SWT.Dispose, typedListener);
}

public void dispose() {
	if (isDisposed()) return;
	if (!isValidThread()) error(SWT.ERROR_THREAD_INVALID_ACCESS);
	release(true);
}

void error(int code) {
	SWT.error(code);
}

public Object getData() {
	checkWidget();
	return (state & KEYED_DATA) != 0 ? ((Object[]) data)[0] : data;
}

public Object getData(String key) {
	checkWidget();
	if (key == null) error(SWT.ERROR_NULL_ARGUMENT);
	if ((state & KEYED_DATA) != 0) {
		Object[] table = (Object[]) data;
		for (int i = 1; i < table.length; i += 2) {
			if (key.equals(table[i])) return table[i + 1];
		}
	}
	return null;
}

public Display getDisplay() {
	Display display = this.display;
	if (display == null) error(SWT.ERROR_WIDGET_DISPOSED);
	return display;
}

public int getStyle() {
	checkWidget();
	return style;
}

public boolean isDisposed() {
	return (state & DISPOSED) != 0;
}

public boolean isListening(int eventType) {
	checkWidget();
	return eventTable != null && eventTable.hooks(eventType);
}

boolean isValidSubclass() {
	return Display.isValidClass(getClass());
}

boolean isValidThread() {
	return getDisplay().thread == Thread.currentThread();
}

public void notifyListeners(int eventType, Event event) {
	checkWidget();
	if (event == null) event = new Event();
	sendEvent(eventType, event);
}

void postEvent(int eventType) {
	sendEvent(eventType, null, false);
}

void postEvent(int eventType, Event event) {
	sendEvent(eventType, event, false);
}

void release(boolean destroy) {
	if ((state & DISPOSE_SENT) == 0) {
		state |= DISPOSE_SENT;
		sendEvent(SWT.Dispose);
	}
	if ((state & DISPOSED) == 0) {
		releaseChildren(destroy);
	}
	if ((state & RELEASED) == 0) {
		state |= RELEASED;
		if (destroy) {
			releaseParent();
			releaseWidget();
		}
	}
}

void releaseChildren(boolean destroy) {
	// No-op
}

void releaseHandle() {
	handle = 0;
	state |= DISPOSED;
}

void releaseParent() {
	// No-op
}

void releaseWidget() {
	eventTable = null;
	data = null;
}

public void removeListener(int eventType, Listener listener) {
	checkWidget();
	if (listener == null) error(SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(eventType, listener);
}

public void removeDisposeListener(DisposeListener listener) {
	checkWidget();
	if (listener == null) error(SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Dispose, listener);
}

void sendEvent(Event event) {
	Display display = event.display = this.display;
	if (!display.filterEvent(event)) {
		if (eventTable != null) eventTable.sendEvent(event);
	}
}

void sendEvent(int eventType) {
	sendEvent(eventType, null, true);
}

void sendEvent(int eventType, Event event) {
	sendEvent(eventType, event, true);
}

void sendEvent(int eventType, Event event, boolean send) {
	if (eventTable == null && !display.filters(eventType)) {
		return;
	}
	if (event == null) event = new Event();
	event.type = eventType;
	event.display = display;
	event.widget = this;
	if (event.time == 0) {
		event.time = (int) System.currentTimeMillis();
	}
	if (send) {
		sendEvent(event);
	} else {
		display.postEvent(event);
	}
}

public void setData(Object data) {
	checkWidget();
	if ((state & KEYED_DATA) != 0) {
		((Object[]) this.data)[0] = data;
	} else {
		this.data = data;
	}
}

public void setData(String key, Object value) {
	checkWidget();
	if (key == null) error(SWT.ERROR_NULL_ARGUMENT);
	int index = 1;
	Object[] table = null;
	if ((state & KEYED_DATA) != 0) {
		table = (Object[]) data;
		while (index < table.length) {
			if (key.equals(table[index])) break;
			index += 2;
		}
	}
	if (value != null) {
		if ((state & KEYED_DATA) != 0) {
			if (index == table.length) {
				Object[] newTable = new Object[table.length + 2];
				System.arraycopy(table, 0, newTable, 0, table.length);
				data = table = newTable;
			}
		} else {
			table = new Object[3];
			table[0] = data;
			data = table;
			state |= KEYED_DATA;
		}
		table[index] = key;
		table[index + 1] = value;
	} else {
		if ((state & KEYED_DATA) != 0) {
			if (index != table.length) {
				int length = table.length - 2;
				if (length == 1) {
					data = table[0];
					state &= ~KEYED_DATA;
				} else {
					Object[] newTable = new Object[length];
					System.arraycopy(table, 0, newTable, 0, index);
					System.arraycopy(table, index + 2, newTable, index, length - index);
					data = newTable;
				}
			}
		}
	}
}

@Override
public String toString() {
	String string = "*Disposed*";
	if (!isDisposed()) {
		string = "*Wrong Thread*";
		if (isValidThread()) string = getNameText();
	}
	return getName() + " {" + string + "}";
}

String getName() {
	String string = getClass().getName();
	int index = string.lastIndexOf('.');
	if (index == -1) return string;
	return string.substring(index + 1, string.length());
}

String getNameText() {
	return "";
}

}
