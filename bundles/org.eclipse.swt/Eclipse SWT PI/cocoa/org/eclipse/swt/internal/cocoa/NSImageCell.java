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

public class NSImageCell extends NSCell {

public NSImageCell() {
	super();
}

public NSImageCell(int id) {
	super(id);
}

public int imageAlignment() {
	return OS.objc_msgSend(this.id, OS.sel_imageAlignment);
}

public int imageFrameStyle() {
	return OS.objc_msgSend(this.id, OS.sel_imageFrameStyle);
}

public int imageScaling() {
	return OS.objc_msgSend(this.id, OS.sel_imageScaling);
}

public void setImageAlignment(int newAlign) {
	OS.objc_msgSend(this.id, OS.sel_setImageAlignment_1, newAlign);
}

public void setImageFrameStyle(int newStyle) {
	OS.objc_msgSend(this.id, OS.sel_setImageFrameStyle_1, newStyle);
}

public void setImageScaling(int newScaling) {
	OS.objc_msgSend(this.id, OS.sel_setImageScaling_1, newScaling);
}

}
