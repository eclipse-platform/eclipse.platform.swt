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

public class NSStatusBar extends NSObject {

public NSStatusBar() {
	super();
}

public NSStatusBar(int id) {
	super(id);
}

public boolean isVertical() {
	return OS.objc_msgSend(this.id, OS.sel_isVertical) != 0;
}

public void removeStatusItem(NSStatusItem item) {
	OS.objc_msgSend(this.id, OS.sel_removeStatusItem_1, item != null ? item.id : 0);
}

public NSStatusItem statusItemWithLength(float length) {
	int result = OS.objc_msgSend(this.id, OS.sel_statusItemWithLength_1, length);
	return result != 0 ? new NSStatusItem(result) : null;
}

public static NSStatusBar systemStatusBar() {
	int result = OS.objc_msgSend(OS.class_NSStatusBar, OS.sel_systemStatusBar);
	return result != 0 ? new NSStatusBar(result) : null;
}

public float thickness() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_thickness);
}

}
