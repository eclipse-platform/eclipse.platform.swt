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
	
public void hook (int type, Listener listener) {
	if (types == null) types = new int [4];
	if (listeners == null) listeners = new Listener [4];
	for (int i=0; i<types.length; i++) {
		if (types [i] == 0) {
			types [i] = type;
			listeners [i] = listener;
			return;
		}
	}
	int length = types.length;
	int [] newTypes = new int [length + 4];
	Listener [] newListeners = new Listener [length + 4];
	System.arraycopy (types, 0, newTypes, 0, length);
	System.arraycopy (listeners, 0, newListeners, 0, length);
	types = newTypes;  listeners = newListeners;
	types [length] = type;  listeners [length] = listener;
}

public boolean hooks (int type) {
	if (listeners == null) return false;
	for (int i=0; i<types.length; i++) {
		if (types [i] == type) return true;
	}
	return false;
}

public void sendEvent (Event event) {
	if (listeners == null) return;
	for (int i=0; i<types.length; i++) {
		if (types [i] == event.type) {
			Listener listener = listeners [i];
			if (listener != null) listener.handleEvent (event);
		}
	}
}

public void unhook (int type, Listener listener) {
	if (listeners == null) return;
	for (int i=0; i<types.length; i++) {
		if (types [i] == type && listeners [i] == listener) {
			int end = types.length - 1;
			System.arraycopy (types, i + 1, types, i, end - i);
			System.arraycopy (listeners, i + 1, listeners, i, end - i);
			types [end] = 0;  listeners [end] = null;
			return;
		}
	}
}

public void unhook (int type, SWTEventListener listener) {
	if (listeners == null) return;
	for (int i=0; i<types.length; i++) {
		if (types [i] == type) {
			if (listeners [i] instanceof TypedListener) {
				TypedListener typedListener = (TypedListener) listeners [i];
				if (typedListener.getEventListener () == listener) {
					int end = types.length - 1;
					System.arraycopy (types, i + 1, types, i, end - i);
					System.arraycopy (listeners, i + 1, listeners, i, end - i);
					types [end] = 0;  listeners [end] = null;
					return;
				}
			}
		}
	}
}

}
