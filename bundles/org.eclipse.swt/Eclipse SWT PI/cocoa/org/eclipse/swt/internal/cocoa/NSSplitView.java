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

public class NSSplitView extends NSView {

public NSSplitView() {
	super();
}

public NSSplitView(int id) {
	super(id);
}

public void adjustSubviews() {
	OS.objc_msgSend(this.id, OS.sel_adjustSubviews);
}

public NSString autosaveName() {
	int result = OS.objc_msgSend(this.id, OS.sel_autosaveName);
	return result != 0 ? new NSString(result) : null;
}

public id delegate() {
	int result = OS.objc_msgSend(this.id, OS.sel_delegate);
	return result != 0 ? new id(result) : null;
}

public NSColor dividerColor() {
	int result = OS.objc_msgSend(this.id, OS.sel_dividerColor);
	return result != 0 ? new NSColor(result) : null;
}

public int dividerStyle() {
	return OS.objc_msgSend(this.id, OS.sel_dividerStyle);
}

public float dividerThickness() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_dividerThickness);
}

public void drawDividerInRect(NSRect rect) {
	OS.objc_msgSend(this.id, OS.sel_drawDividerInRect_1, rect);
}

public boolean isPaneSplitter() {
	return OS.objc_msgSend(this.id, OS.sel_isPaneSplitter) != 0;
}

public boolean isSubviewCollapsed(NSView subview) {
	return OS.objc_msgSend(this.id, OS.sel_isSubviewCollapsed_1, subview != null ? subview.id : 0) != 0;
}

public boolean isVertical() {
	return OS.objc_msgSend(this.id, OS.sel_isVertical) != 0;
}

public float maxPossiblePositionOfDividerAtIndex(int dividerIndex) {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_maxPossiblePositionOfDividerAtIndex_1, dividerIndex);
}

public float minPossiblePositionOfDividerAtIndex(int dividerIndex) {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_minPossiblePositionOfDividerAtIndex_1, dividerIndex);
}

public void setAutosaveName(NSString autosaveName) {
	OS.objc_msgSend(this.id, OS.sel_setAutosaveName_1, autosaveName != null ? autosaveName.id : 0);
}

public void setDelegate(id delegate) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_1, delegate != null ? delegate.id : 0);
}

public void setDividerStyle(int dividerStyle) {
	OS.objc_msgSend(this.id, OS.sel_setDividerStyle_1, dividerStyle);
}

public void setIsPaneSplitter(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setIsPaneSplitter_1, flag);
}

public void setPosition(float position, int dividerIndex) {
	OS.objc_msgSend(this.id, OS.sel_setPosition_1ofDividerAtIndex_1, position, dividerIndex);
}

public void setVertical(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setVertical_1, flag);
}

}
