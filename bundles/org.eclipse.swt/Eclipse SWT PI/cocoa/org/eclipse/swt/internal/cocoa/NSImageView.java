/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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

public NSImageView(int /*long*/ id) {
	super(id);
}

public NSImageView(id id) {
	super(id);
}

public NSImage image() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_image);
	return result != 0 ? new NSImage(result) : null;
}

public void setImage(NSImage newImage) {
	OS.objc_msgSend(this.id, OS.sel_setImage_, newImage != null ? newImage.id : 0);
}

public void setImageAlignment(int /*long*/ newAlign) {
	OS.objc_msgSend(this.id, OS.sel_setImageAlignment_, newAlign);
}

public void setImageScaling(int /*long*/ newScaling) {
	OS.objc_msgSend(this.id, OS.sel_setImageScaling_, newScaling);
}

public static int /*long*/ cellClass() {
	return OS.objc_msgSend(OS.class_NSImageView, OS.sel_cellClass);
}

public static void setCellClass(int /*long*/ factoryId) {
	OS.objc_msgSend(OS.class_NSImageView, OS.sel_setCellClass_, factoryId);
}

}
