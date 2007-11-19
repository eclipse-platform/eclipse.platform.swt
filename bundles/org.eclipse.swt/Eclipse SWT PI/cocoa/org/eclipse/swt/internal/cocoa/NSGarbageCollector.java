/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSGarbageCollector extends NSObject {

public NSGarbageCollector() {
	super();
}

public NSGarbageCollector(int id) {
	super(id);
}

public void collectExhaustively() {
	OS.objc_msgSend(this.id, OS.sel_collectExhaustively);
}

public void collectIfNeeded() {
	OS.objc_msgSend(this.id, OS.sel_collectIfNeeded);
}

public static id defaultCollector() {
	int result = OS.objc_msgSend(OS.class_NSGarbageCollector, OS.sel_defaultCollector);
	return result != 0 ? new id(result) : null;
}

public void disable() {
	OS.objc_msgSend(this.id, OS.sel_disable);
}

public void disableCollectorForPointer(int ptr) {
	OS.objc_msgSend(this.id, OS.sel_disableCollectorForPointer_1, ptr);
}

public void enable() {
	OS.objc_msgSend(this.id, OS.sel_enable);
}

public void enableCollectorForPointer(int ptr) {
	OS.objc_msgSend(this.id, OS.sel_enableCollectorForPointer_1, ptr);
}

public boolean isCollecting() {
	return OS.objc_msgSend(this.id, OS.sel_isCollecting) != 0;
}

public boolean isEnabled() {
	return OS.objc_msgSend(this.id, OS.sel_isEnabled) != 0;
}

public int zone() {
	return OS.objc_msgSend(this.id, OS.sel_zone);
}

}
