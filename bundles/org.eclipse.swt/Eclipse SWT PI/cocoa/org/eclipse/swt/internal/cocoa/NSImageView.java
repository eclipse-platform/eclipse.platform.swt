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

public void setImage(NSImage newImage) {
	OS.objc_msgSend(this.id, OS.sel_setImage_, newImage != null ? newImage.id : 0);
}

public void setImageAlignment(long /*int*/ newAlign) {
	OS.objc_msgSend(this.id, OS.sel_setImageAlignment_, newAlign);
}

public void setImageScaling(long /*int*/ newScaling) {
	OS.objc_msgSend(this.id, OS.sel_setImageScaling_, newScaling);
}

public static long /*int*/ cellClass() {
	return OS.objc_msgSend(OS.class_NSImageView, OS.sel_cellClass);
}

public static void setCellClass(long /*int*/ factoryId) {
	OS.objc_msgSend(OS.class_NSImageView, OS.sel_setCellClass_, factoryId);
}

}
