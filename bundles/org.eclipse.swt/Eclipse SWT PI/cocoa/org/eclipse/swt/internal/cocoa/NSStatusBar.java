/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSStatusBar extends NSObject {

public NSStatusBar() {
	super();
}

public NSStatusBar(long /*int*/ id) {
	super(id);
}

public NSStatusBar(id id) {
	super(id);
}

public void removeStatusItem(NSStatusItem item) {
	OS.objc_msgSend(this.id, OS.sel_removeStatusItem_, item != null ? item.id : 0);
}

public NSStatusItem statusItemWithLength(double /*float*/ length) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_statusItemWithLength_, length);
	return result != 0 ? new NSStatusItem(result) : null;
}

public static NSStatusBar systemStatusBar() {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSStatusBar, OS.sel_systemStatusBar);
	return result != 0 ? new NSStatusBar(result) : null;
}

public double /*float*/ thickness() {
	return (double /*float*/)OS.objc_msgSend_fpret(this.id, OS.sel_thickness);
}

}
