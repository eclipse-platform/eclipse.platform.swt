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

public class NSPathControl extends NSControl {

public NSPathControl() {
	super();
}

public NSPathControl(int id) {
	super(id);
}

public NSURL URL() {
	int result = OS.objc_msgSend(this.id, OS.sel_URL);
	return result != 0 ? new NSURL(result) : null;
}

public NSColor backgroundColor() {
	int result = OS.objc_msgSend(this.id, OS.sel_backgroundColor);
	return result != 0 ? new NSColor(result) : null;
}

public NSPathComponentCell clickedPathComponentCell() {
	int result = OS.objc_msgSend(this.id, OS.sel_clickedPathComponentCell);
	return result != 0 ? new NSPathComponentCell(result) : null;
}

public id delegate() {
	int result = OS.objc_msgSend(this.id, OS.sel_delegate);
	return result != 0 ? new id(result) : null;
}

public int doubleAction() {
	return OS.objc_msgSend(this.id, OS.sel_doubleAction);
}

public NSArray pathComponentCells() {
	int result = OS.objc_msgSend(this.id, OS.sel_pathComponentCells);
	return result != 0 ? new NSArray(result) : null;
}

public int pathStyle() {
	return OS.objc_msgSend(this.id, OS.sel_pathStyle);
}

public void setBackgroundColor(NSColor color) {
	OS.objc_msgSend(this.id, OS.sel_setBackgroundColor_1, color != null ? color.id : 0);
}

public void setDelegate(id delegate) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_1, delegate != null ? delegate.id : 0);
}

public void setDoubleAction(int action) {
	OS.objc_msgSend(this.id, OS.sel_setDoubleAction_1, action);
}

public void setDraggingSourceOperationMask(int mask, boolean isLocal) {
	OS.objc_msgSend(this.id, OS.sel_setDraggingSourceOperationMask_1forLocal_1, mask, isLocal);
}

public void setPathComponentCells(NSArray cells) {
	OS.objc_msgSend(this.id, OS.sel_setPathComponentCells_1, cells != null ? cells.id : 0);
}

public void setPathStyle(int style) {
	OS.objc_msgSend(this.id, OS.sel_setPathStyle_1, style);
}

public void setURL(NSURL url) {
	OS.objc_msgSend(this.id, OS.sel_setURL_1, url != null ? url.id : 0);
}

}
