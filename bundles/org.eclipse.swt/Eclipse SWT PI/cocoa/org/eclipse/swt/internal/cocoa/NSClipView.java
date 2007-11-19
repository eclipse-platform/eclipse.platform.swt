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

public class NSClipView extends NSView {

public NSClipView() {
	super();
}

public NSClipView(int id) {
	super(id);
}

public boolean autoscroll(NSEvent theEvent) {
	return OS.objc_msgSend(this.id, OS.sel_autoscroll_1, theEvent != null ? theEvent.id : 0) != 0;
}

public NSColor backgroundColor() {
	int result = OS.objc_msgSend(this.id, OS.sel_backgroundColor);
	return result != 0 ? new NSColor(result) : null;
}

public NSPoint constrainScrollPoint(NSPoint newOrigin) {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_stret(result, this.id, OS.sel_constrainScrollPoint_1, newOrigin);
	return result;
}

public boolean copiesOnScroll() {
	return OS.objc_msgSend(this.id, OS.sel_copiesOnScroll) != 0;
}

public NSCursor documentCursor() {
	int result = OS.objc_msgSend(this.id, OS.sel_documentCursor);
	return result != 0 ? new NSCursor(result) : null;
}

public NSRect documentRect() {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_documentRect);
	return result;
}

public id documentView() {
	int result = OS.objc_msgSend(this.id, OS.sel_documentView);
	return result != 0 ? new id(result) : null;
}

public NSRect documentVisibleRect() {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_documentVisibleRect);
	return result;
}

public boolean drawsBackground() {
	return OS.objc_msgSend(this.id, OS.sel_drawsBackground) != 0;
}

public void scrollToPoint(NSPoint newOrigin) {
	OS.objc_msgSend(this.id, OS.sel_scrollToPoint_1, newOrigin);
}

public void setBackgroundColor(NSColor color) {
	OS.objc_msgSend(this.id, OS.sel_setBackgroundColor_1, color != null ? color.id : 0);
}

public void setCopiesOnScroll(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setCopiesOnScroll_1, flag);
}

public void setDocumentCursor(NSCursor anObj) {
	OS.objc_msgSend(this.id, OS.sel_setDocumentCursor_1, anObj != null ? anObj.id : 0);
}

public void setDocumentView(NSView aView) {
	OS.objc_msgSend(this.id, OS.sel_setDocumentView_1, aView != null ? aView.id : 0);
}

public void setDrawsBackground(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setDrawsBackground_1, flag);
}

public void viewBoundsChanged(NSNotification notification) {
	OS.objc_msgSend(this.id, OS.sel_viewBoundsChanged_1, notification != null ? notification.id : 0);
}

public void viewFrameChanged(NSNotification notification) {
	OS.objc_msgSend(this.id, OS.sel_viewFrameChanged_1, notification != null ? notification.id : 0);
}

}
