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
package org.eclipse.swt.ole.win32;


/**
* The OleEventTable class implements a simple
* look up mechanism that maps an event type
* to a listener.  Multiple listeners for the
* same event type are supported.
*/

class OleEventTable {
	int [] types;
	OleListener [] handlers;
void hook (int eventType, OleListener handler) {
	if (types == null) types = new int [4];
	if (handlers == null) handlers = new OleListener [4];
	for (int i=0; i<types.length; i++) {
		if (types [i] == 0) {
			types [i] = eventType;
			handlers [i] = handler;
			return;
		}
	}
	int size = types.length;
	int [] newTypes = new int [size + 4];
	OleListener [] newHandlers = new OleListener [size + 4];
	System.arraycopy (types, 0, newTypes, 0, size);
	System.arraycopy (handlers, 0, newHandlers, 0, size);
	types = newTypes;  handlers = newHandlers;
	types [size] = eventType;  handlers [size] = handler;
}
boolean hooks (int eventType) {
	if (handlers == null) return false;
	for (int type : types) {
		if (type == eventType) return true;
	}
	return false;
}
void sendEvent (OleEvent event) {
	if (handlers == null) return;
	for (int i=0; i<types.length; i++) {
		if (types [i] == event.type) {
			OleListener listener = handlers [i];
			if (listener != null) listener.handleEvent (event);
		}
	}
}
void unhook (int eventType, OleListener handler) {
	if (handlers == null) return;
	for (int i=0; i<types.length; i++) {
		if ((types [i] == eventType) && (handlers [i] == handler)) {
			types [i] = 0;
			handlers [i] = null;
			return;
		}
	}
}
boolean hasEntries() {
	for (int type : types) {
		if (type != 0) return true;
	}
	return false;
}
}
