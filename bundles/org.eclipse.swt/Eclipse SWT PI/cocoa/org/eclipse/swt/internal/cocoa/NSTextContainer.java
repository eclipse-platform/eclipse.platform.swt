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

public class NSTextContainer extends NSObject {

public NSTextContainer() {
	super();
}

public NSTextContainer(int id) {
	super(id);
}

public NSSize containerSize() {
	NSSize result = new NSSize();
	OS.objc_msgSend_struct(result, this.id, OS.sel_containerSize);
	return result;
}

public boolean containsPoint(NSPoint point) {
	return OS.objc_msgSend(this.id, OS.sel_containsPoint_1, point) != 0;
}

public boolean heightTracksTextView() {
	return OS.objc_msgSend(this.id, OS.sel_heightTracksTextView) != 0;
}

public id initWithContainerSize(NSSize size) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithContainerSize_1, size);
	return result != 0 ? new id(result) : null;
}

public boolean isSimpleRectangularTextContainer() {
	return OS.objc_msgSend(this.id, OS.sel_isSimpleRectangularTextContainer) != 0;
}

public NSLayoutManager layoutManager() {
	int result = OS.objc_msgSend(this.id, OS.sel_layoutManager);
	return result != 0 ? new NSLayoutManager(result) : null;
}

public float lineFragmentPadding() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_lineFragmentPadding);
}

public NSRect lineFragmentRectForProposedRect(NSRect proposedRect, int sweepDirection, int movementDirection, int remainingRect) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_lineFragmentRectForProposedRect_1sweepDirection_1movementDirection_1remainingRect_1, proposedRect, sweepDirection, movementDirection, remainingRect);
	return result;
}

public void replaceLayoutManager(NSLayoutManager newLayoutManager) {
	OS.objc_msgSend(this.id, OS.sel_replaceLayoutManager_1, newLayoutManager != null ? newLayoutManager.id : 0);
}

public void setContainerSize(NSSize size) {
	OS.objc_msgSend(this.id, OS.sel_setContainerSize_1, size);
}

public void setHeightTracksTextView(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setHeightTracksTextView_1, flag);
}

public void setLayoutManager(NSLayoutManager layoutManager) {
	OS.objc_msgSend(this.id, OS.sel_setLayoutManager_1, layoutManager != null ? layoutManager.id : 0);
}

public void setLineFragmentPadding(float pad) {
	OS.objc_msgSend(this.id, OS.sel_setLineFragmentPadding_1, pad);
}

public void setTextView(NSTextView textView) {
	OS.objc_msgSend(this.id, OS.sel_setTextView_1, textView != null ? textView.id : 0);
}

public void setWidthTracksTextView(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setWidthTracksTextView_1, flag);
}

public NSTextView textView() {
	int result = OS.objc_msgSend(this.id, OS.sel_textView);
	return result != 0 ? new NSTextView(result) : null;
}

public boolean widthTracksTextView() {
	return OS.objc_msgSend(this.id, OS.sel_widthTracksTextView) != 0;
}

}
