/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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

public class NSImageView extends NSControl {

public NSImageView() {
	super();
}

public NSImageView(long /*int*/ id) {
	super(id);
}

public NSImageView(id id) {
	super(id);
}

public NSImage image() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_image);
	return result != 0 ? new NSImage(result) : null;
}

public void setImage(NSImage image) {
	OS.objc_msgSend(this.id, OS.sel_setImage_, image != null ? image.id : 0);
}

public void setImageAlignment(long /*int*/ imageAlignment) {
	OS.objc_msgSend(this.id, OS.sel_setImageAlignment_, imageAlignment);
}

public void setImageScaling(long /*int*/ imageScaling) {
	OS.objc_msgSend(this.id, OS.sel_setImageScaling_, imageScaling);
}

public static long /*int*/ cellClass() {
	return OS.objc_msgSend(OS.class_NSImageView, OS.sel_cellClass);
}

public static void setCellClass(long /*int*/ factoryId) {
	OS.objc_msgSend(OS.class_NSImageView, OS.sel_setCellClass_, factoryId);
}

}
