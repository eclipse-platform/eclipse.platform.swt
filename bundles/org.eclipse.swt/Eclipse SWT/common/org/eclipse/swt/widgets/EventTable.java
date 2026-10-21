/*******************************************************************************
 * Copyright (c) 2000, 2024 IBM Corporation and others.
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


import java.util.*;

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
	/**
	 * M3 physical range: one past the last nonzero event type, independent of
	 * the two shared dynamic-array capacities. Tombstones inside this range
	 * remain until the original outermost dispatch finishes compaction.
	 */
	int highWater;
	static final int GROW_SIZE = 4;

public Listener [] getListeners (int eventType) {
	if (types == null) return new Listener [0];
	// SWT.None is the historical zero/tombstone value: its private query
	// still sees the entire allocated backing lane, including empty cells.
	int limit = eventType == SWT.None ? types.length : highWater;
	int count = 0;
	for (int i=0; i<limit; i++) if (types [i] == eventType) count++;
	if (count == 0) return new Listener [0];
	Listener [] result = new Listener [count];
	for (int i=0, write=0; i<limit; i++) {
		if (types [i] == eventType) result [write++] = listeners [i];
	}
	return result;
}

public void hook (int eventType, Listener listener) {
	if (types == null) types = new int [GROW_SIZE];
	if (listeners == null) listeners = new Listener [GROW_SIZE];
	int index = highWater;
	if (index == types.length) {
		int length = types.length + GROW_SIZE;
		types = Arrays.copyOf (types, length);
		listeners = Arrays.copyOf (listeners, length);
	}
	types [index] = eventType;
	listeners [index] = listener;
	// Zero is not a live event registration. The next hook may replace it,
	// exactly as the original backwards scan for the final nonzero slot.
	if (eventType != SWT.None) highWater = index + 1;
}

public boolean hooks (int eventType) {
	if (types == null) return false;
	int limit = eventType == SWT.None ? types.length : highWater;
	for (int i=0; i<limit; i++) if (types [i] == eventType) return true;
	return false;
}

public void sendEvent (Event event) {
    if (types == null) {
        return;
    }
	level += level >= 0 ? 1 : -1;
	try (ExceptionStash exceptions = new ExceptionStash ()) {
		for (int i=0; i<highWater; i++) {
            if (event.type == SWT.None) {
                return;
            }
			if (types [i] == event.type) {
				Listener listener = listeners [i];
				if (listener != null) {
					try {
						listener.handleEvent (event);
					} catch (Error | RuntimeException ex) {
						exceptions.stash (ex);
					}
				}
			}
		}
	} finally {
		boolean compact = level < 0;
		level -= level >= 0 ? 1 : -1;
		if (compact && level == 0) {
			int index = 0;
			for (int i=0; i<highWater; i++) {
				if (types [i] != 0) {
					types [index] = types [i];
					listeners [index] = listeners [i];
					index++;
				}
			}
			// Clear trailing references, including an SWT.None registration
			// temporarily written beyond the last live event type.
			Arrays.fill (types, index, types.length, 0);
			Arrays.fill (listeners, index, listeners.length, null);
			highWater = index;
		}
	}
}

public int size () {
	if (types == null) return 0;
	int count = 0;
	for (int i=0; i<highWater; i++) if (types [i] != 0) count++;
	return count;
}

void remove (int index) {
	if (level == 0) {
		int end = types.length - 1;
		// Preserve the original full-capacity shifts even for SWT.None
		// queries made by same-package callers.
		System.arraycopy (types, index + 1, types, index, end - index);
		System.arraycopy (listeners, index + 1, listeners, index, end - index);
		if (index < highWater) highWater--;
		index = end;
	} else {
		if (level > 0) level = -level;
	}
	types [index] = 0;
	listeners [index] = null;
	if (level != 0) {
		// A removed tail must be reusable by reentrant hooks; interior
		// tombstones stay in place until the outermost dispatch completes.
		while (highWater > 0 && types [highWater - 1] == 0) highWater--;
	}
}

public void unhook (int eventType, Listener listener) {
    if (types == null) {
        return;
    }
	int limit = eventType == SWT.None ? types.length : highWater;
	for (int i=0; i<limit; i++) {
		if (types [i] == eventType && listeners [i] == listener) {
			remove (i);
			return;
		}
	}
}

@SuppressWarnings("removal")
public void unhook (int eventType, EventListener listener) {
    if (types == null) {
        return;
    }
	int limit = eventType == SWT.None ? types.length : highWater;
	for (int i=0; i<limit; i++) {
		if (types [i] == eventType) {
			if (listeners [i] instanceof TypedListener typedListener) {
				if (typedListener.eventListener == listener) {
					remove (i);
					return;
				}
			}
		}
	}
}

}
