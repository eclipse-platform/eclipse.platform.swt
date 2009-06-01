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

public class NSScrollView extends NSView {

public NSScrollView() {
	super();
}

public NSScrollView(int /*long*/ id) {
	super(id);
}

public NSScrollView(id id) {
	super(id);
}

public NSSize contentSize() {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_contentSize);
	return result;
}

public static NSSize contentSizeForFrameSize(NSSize fSize, boolean hFlag, boolean vFlag, int /*long*/ aType) {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, OS.class_NSScrollView, OS.sel_contentSizeForFrameSize_hasHorizontalScroller_hasVerticalScroller_borderType_, fSize, hFlag, vFlag, aType);
	return result;
}

public NSClipView contentView() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_contentView);
	return result != 0 ? new NSClipView(result) : null;
}

public NSView documentView() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_documentView);
	return result != 0 ? new NSView(result) : null;
}

public NSRect documentVisibleRect() {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_documentVisibleRect);
	return result;
}

public static NSSize frameSizeForContentSize(NSSize cSize, boolean hFlag, boolean vFlag, int /*long*/ aType) {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, OS.class_NSScrollView, OS.sel_frameSizeForContentSize_hasHorizontalScroller_hasVerticalScroller_borderType_, cSize, hFlag, vFlag, aType);
	return result;
}

public void reflectScrolledClipView(NSClipView cView) {
	OS.objc_msgSend(this.id, OS.sel_reflectScrolledClipView_, cView != null ? cView.id : 0);
}

public void setAutohidesScrollers(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAutohidesScrollers_, flag);
}

public void setBorderType(int /*long*/ aType) {
	OS.objc_msgSend(this.id, OS.sel_setBorderType_, aType);
}

public void setDocumentView(NSView aView) {
	OS.objc_msgSend(this.id, OS.sel_setDocumentView_, aView != null ? aView.id : 0);
}

public void setDrawsBackground(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setDrawsBackground_, flag);
}

public void setHasHorizontalScroller(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setHasHorizontalScroller_, flag);
}

public void setHasVerticalScroller(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setHasVerticalScroller_, flag);
}

public void setHorizontalScroller(NSScroller anObject) {
	OS.objc_msgSend(this.id, OS.sel_setHorizontalScroller_, anObject != null ? anObject.id : 0);
}

public void setVerticalScroller(NSScroller anObject) {
	OS.objc_msgSend(this.id, OS.sel_setVerticalScroller_, anObject != null ? anObject.id : 0);
}

}
