/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSImageRep extends NSObject {

public NSImageRep() {
	super();
}

public NSImageRep(long /*int*/ id) {
	super(id);
}

public NSImageRep(id id) {
	super(id);
}

public long /*int*/ bitsPerSample() {
	return OS.objc_msgSend(this.id, OS.sel_bitsPerSample);
}

public NSString colorSpaceName() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_colorSpaceName);
	return result != 0 ? new NSString(result) : null;
}

public boolean drawInRect(NSRect rect) {
	return OS.objc_msgSend_bool(this.id, OS.sel_drawInRect_, rect);
}

public boolean hasAlpha() {
	return OS.objc_msgSend_bool(this.id, OS.sel_hasAlpha);
}

public long /*int*/ pixelsHigh() {
	return OS.objc_msgSend(this.id, OS.sel_pixelsHigh);
}

public long /*int*/ pixelsWide() {
	return OS.objc_msgSend(this.id, OS.sel_pixelsWide);
}

public void setAlpha(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAlpha_, flag);
}

}
