/*******************************************************************************
 * Copyright (c) 2000, 2019 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSImageRep extends NSObject {

public NSImageRep() {
	super();
}

public NSImageRep(long id) {
	super(id);
}

public NSImageRep(id id) {
	super(id);
}

public long bitsPerSample() {
	return OS.objc_msgSend(this.id, OS.sel_bitsPerSample);
}

public boolean drawInRect(NSRect rect) {
	return OS.objc_msgSend_bool(this.id, OS.sel_drawInRect_, rect);
}

public boolean hasAlpha() {
	return OS.objc_msgSend_bool(this.id, OS.sel_hasAlpha);
}

public static NSImageRep imageRepWithContentsOfFile(NSString filename) {
	long result = OS.objc_msgSend(OS.class_NSImageRep, OS.sel_imageRepWithContentsOfFile_, filename != null ? filename.id : 0);
	return result != 0 ? new NSImageRep(result) : null;
}

public long pixelsHigh() {
	return OS.objc_msgSend(this.id, OS.sel_pixelsHigh);
}

public long pixelsWide() {
	return OS.objc_msgSend(this.id, OS.sel_pixelsWide);
}

public void setAlpha(boolean alpha) {
	OS.objc_msgSend(this.id, OS.sel_setAlpha_, alpha);
}

public void setSize(NSSize size) {
	OS.objc_msgSend(this.id, OS.sel_setSize_, size);
}

}
