/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
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
import org.eclipse.swt.internal.*;

/**
 * Instances of this class implement a simple
 * look up mechanism that maps an event type
 * to a listener.  Multiple listeners for the
 * same event type are supported.
 */

class EventTable {
	int [] types;
	Listener [] listeners;
	int level;
	static final int GROW_SIZE = 4;

public Listener [] getListeners (int eventType) {
	if (types == null) return new Listener [0];
	int count = 0;
	for (int type : types) {
		if (type == eventType) count++;
	}
	if (count == 0) return new Listener [0];
	Listener [] result = new Listener [count];
	count = 0;
	for (int i=0; i<types.length; i++) {
		if (types [i] == eventType) {
			result [count++] = listeners [i];
		}
	}
	return result;
}

public void hook (int eventType, Listener listener) {
	if (types == null) types = new int [GROW_SIZE];
	if (listeners == null) listeners = new Listener [GROW_SIZE];
	int length = types.length, index = length - 1;
	while (index >= 0) {
		if (types [index] != 0) break;
		--index;
	}
	index++;
	if (index == length) {
		int [] newTypes = new int [length + GROW_SIZE];
		System.arraycopy (types, 0, newTypes, 0, length);
		types = newTypes;
		Listener [] newListeners = new Listener [length + GROW_SIZE];
		System.arraycopy (listeners, 0, newListeners, 0, length);
		listeners = newListeners;
	}
	types [index] = eventType;
	listeners [index] = listener;
}

public boolean hooks (int eventType) {
	if (types == null) return false;
	for (int type : types) {
		if (type == eventType) return true;
	}
	return false;
}

public void sendEvent (Event event) {
	if (types == null) return;
	level += level >= 0 ? 1 : -1;
	try {
		for (int i=0; i<types.length; i++) {
			if (event.type == SWT.None) return;
			if (types [i] == event.type) {
				Listener listener = listeners [i];
				if (listener != null) {
					try {
						listener.handleEvent (event);
					} catch (RuntimeException runtimeException) {
						Display display = Display.getCurrent ();

						if (display == null) {
							throw runtimeException;
						}

						display.getRuntimeExceptionHandler ().accept (runtimeException);
					} catch (Error error) {
						Display display = Display.getCurrent ();

						if (display == null) {
							throw error;
						}

						display.getErrorHandler ().accept (error);
					}
				}
			}
		}
	} finally {
		boolean compact = level < 0;
		level -= level >= 0 ? 1 : -1;
		if (compact && level == 0) {
			int index = 0;
			for (int i=0; i<types.length; i++) {
				if (types [i] != 0) {
					types [index] = types [i];
					listeners [index] = listeners [i];
					index++;
				}
			}
			for (int i=index; i<types.length; i++) {
				types [i] = 0;
				listeners [i] = null;
			}
		}
	}
}

public int size () {
	if (types == null) return 0;
	int count = 0;
	for (int type : types) {
		if (type != 0) count++;
	}
	return count;
}

void remove (int index) {
	if (level == 0) {
		int end = types.length - 1;
		System.arraycopy (types, index + 1, types, index, end - index);
		System.arraycopy (listeners, index + 1, listeners, index, end - index);
		index = end;
	} else {
		if (level > 0) level = -level;
	}
	types [index] = 0;
	listeners [index] = null;
}

public void unhook (int eventType, Listener listener) {
	if (types == null) return;
	for (int i=0; i<types.length; i++) {
		if (types [i] == eventType && listeners [i] == listener) {
			remove (i);
			return;
		}
	}
}

public void unhook (int eventType, SWTEventListener listener) {
	if (types == null) return;
	for (int i=0; i<types.length; i++) {
		if (types [i] == eventType) {
			if (listeners [i] instanceof TypedListener) {
				TypedListener typedListener = (TypedListener) listeners [i];
				if (typedListener.getEventListener () == listener) {
					remove (i);
					return;
				}
			}
		}
	}
}

}
