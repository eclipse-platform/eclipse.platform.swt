package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import java.util.EventListener;

/**
 * Instances of this class implement a simple
 * look up mechanism that maps an event type
 * to a listener.  Muliple listeners for the
 * same event type are supported.
 */

class EventTable {
	int [] types;
	Listener [] handlers;
	
public void hook (int eventType, Listener handler) {
	if (types == null) types = new int [4];
	if (handlers == null) handlers = new Listener [4];
	for (int i=0; i<types.length; i++) {
		if (types [i] == 0) {
			types [i] = eventType;
			handlers [i] = handler;
			return;
		}
	}
	int size = types.length;
	int [] newTypes = new int [size + 4];
	Listener [] newHandlers = new Listener [size + 4];
	System.arraycopy (types, 0, newTypes, 0, size);
	System.arraycopy (handlers, 0, newHandlers, 0, size);
	types = newTypes;  handlers = newHandlers;
	types [size] = eventType;  handlers [size] = handler;
}

public boolean hooks (int eventType) {
	if (handlers == null) return false;
	for (int i=0; i<types.length; i++) {
		if (types [i] == eventType) return true;
	}
	return false;
}

public void sendEvent (Event event) {
	if (handlers == null) return;
	for (int i=0; i<types.length; i++) {
		if (types [i] == event.type) {
			Listener listener = handlers [i];
			if (listener != null) listener.handleEvent (event);
		}
	}
}

public void unhook (int eventType, Listener handler) {
	if (handlers == null) return;
	for (int i=0; i<types.length; i++) {
		if ((types [i] == eventType) && (handlers [i] == handler)) {
			types [i] = 0;
			handlers [i] = null;
			return;
		}
	}
}

public void unhook (int eventType, EventListener handler) {
	if (handlers == null) return;
	for (int i=0; i<types.length; i++) {
		if (types [i] == eventType) {
			if (handlers [i] instanceof TypedListener) {
				TypedListener listener = (TypedListener) handlers [i];
				if (listener.getEventListener () == handler) {
					types [i] = 0;
					handlers [i] = null;
					return;
				}
			}
		}
	}
}

}
