package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.SWTEventListener;


/**
 * Instances of this class implement a simple
 * look up mechanism that maps an event type
 * to a listener.  Muliple listeners for the
 * same event type are supported.
 */

class EventTable {
	int [] types;
	Listener [] listeners;
	int level;
	
public void hook (int eventType, Listener listener) {
	if (types == null) types = new int [4];
	if (listeners == null) listeners = new Listener [4];
	int length = types.length, index = length - 1;
	while (index >= 0) {
		if (types [index] != 0) break;
		--index;
	}
	index++;
	if (index == length) {
		if (level == 0) {
			index = 0;
			for (int i=0; i<types.length; i++) {
				if (types [i] != 0) {
					types [index] = types [i];
					listeners [index] = listeners [i];
					index++;
				}
			}
			for (int i=index; i<types.length; i++) {
				types [i] = 0;
				listeners [index] = null;
			}
		}
		if (index == length) {
			int [] newTypes = new int [length + 4];
			System.arraycopy (types, 0, newTypes, 0, length);
			types = newTypes;
			Listener [] newListeners = new Listener [length + 4];
			System.arraycopy (listeners, 0, newListeners, 0, length);
			listeners = newListeners;
		}
	}
	types [index] = eventType;
	listeners [index] = listener;
}

public boolean hooks (int eventType) {
	if (types == null) return false;
	for (int i=0; i<types.length; i++) {
		if (types [i] == eventType) return true;
	}
	return false;
}

public void sendEvent (Event event) {
	if (types == null) return;
	level++;
	for (int i=0; i<types.length; i++) {
		if (types [i] == event.type) {
			Listener listener = listeners [i];
			if (listener != null) listener.handleEvent (event);
		}
	}
	--level;
}

void remove (int index) {
	if (level == 0) {
		int end = types.length - 1;
		System.arraycopy (types, index + 1, types, index, end - index);
		System.arraycopy (listeners, index + 1, listeners, index, end - index);
		index = end;
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

public void unhook (int eventType, SWTEventListener handler) {
	if (types == null) return;
	for (int i=0; i<types.length; i++) {
		if (types [i] == eventType) {
			if (listeners [i] instanceof TypedListener) {
				TypedListener listener = (TypedListener) listeners [i];
				if (listener.getEventListener () == handler) {
					remove (i);
					return;
				}
			}
		}
	}
}

}
