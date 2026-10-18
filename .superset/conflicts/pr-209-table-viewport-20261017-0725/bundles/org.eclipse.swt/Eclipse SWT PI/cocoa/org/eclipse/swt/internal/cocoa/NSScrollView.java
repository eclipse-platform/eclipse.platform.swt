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

public class NSScrollView extends NSView {

public NSScrollView() {
	super();
}

public NSScrollView(long id) {
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

public static NSSize contentSizeForFrameSize(NSSize fSize, boolean hFlag, boolean vFlag, long aType) {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, OS.class_NSScrollView, OS.sel_contentSizeForFrameSize_hasHorizontalScroller_hasVerticalScroller_borderType_, fSize, hFlag, vFlag, aType);
	return result;
}

public NSClipView contentView() {
	long result = OS.objc_msgSend(this.id, OS.sel_contentView);
	return result != 0 ? new NSClipView(result) : null;
}

public NSView documentView() {
	long result = OS.objc_msgSend(this.id, OS.sel_documentView);
	return result != 0 ? new NSView(result) : null;
}

public NSRect documentVisibleRect() {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_documentVisibleRect);
	return result;
}

public void flashScrollers() {
	OS.objc_msgSend(this.id, OS.sel_flashScrollers);
}

public static NSSize frameSizeForContentSize(NSSize cSize, boolean hFlag, boolean vFlag, long aType) {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, OS.class_NSScrollView, OS.sel_frameSizeForContentSize_hasHorizontalScroller_hasVerticalScroller_borderType_, cSize, hFlag, vFlag, aType);
	return result;
}

public static NSSize frameSizeForContentSize(NSSize cSize, long horizontalScrollerClass, long verticalScrollerClass, long aType, long controlSize, long scrollerStyle) {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, OS.class_NSScrollView, OS.sel_frameSizeForContentSize_horizontalScrollerClass_verticalScrollerClass_borderType_controlSize_scrollerStyle_, cSize, horizontalScrollerClass, verticalScrollerClass, aType, controlSize, scrollerStyle);
	return result;
}

public void reflectScrolledClipView(NSClipView cView) {
	OS.objc_msgSend(this.id, OS.sel_reflectScrolledClipView_, cView != null ? cView.id : 0);
}

public long scrollerStyle() {
	return OS.objc_msgSend(this.id, OS.sel_scrollerStyle);
}

public void setAutohidesScrollers(boolean autohidesScrollers) {
	OS.objc_msgSend(this.id, OS.sel_setAutohidesScrollers_, autohidesScrollers);
}

public void setBorderType(long borderType) {
	OS.objc_msgSend(this.id, OS.sel_setBorderType_, borderType);
}

public void setDocumentView(id documentView) {
	OS.objc_msgSend(this.id, OS.sel_setDocumentView_, documentView != null ? documentView.id : 0);
}

public void setDrawsBackground(boolean drawsBackground) {
	OS.objc_msgSend(this.id, OS.sel_setDrawsBackground_, drawsBackground);
}

public void setHasHorizontalScroller(boolean hasHorizontalScroller) {
	OS.objc_msgSend(this.id, OS.sel_setHasHorizontalScroller_, hasHorizontalScroller);
}

public void setHasVerticalScroller(boolean hasVerticalScroller) {
	OS.objc_msgSend(this.id, OS.sel_setHasVerticalScroller_, hasVerticalScroller);
}

public void setHorizontalScroller(NSScroller horizontalScroller) {
	OS.objc_msgSend(this.id, OS.sel_setHorizontalScroller_, horizontalScroller != null ? horizontalScroller.id : 0);
}

public void setVerticalScrollElasticity(long verticalScrollElasticity) {
	OS.objc_msgSend(this.id, OS.sel_setVerticalScrollElasticity_, verticalScrollElasticity);
}

public void setVerticalScroller(NSScroller verticalScroller) {
	OS.objc_msgSend(this.id, OS.sel_setVerticalScroller_, verticalScroller != null ? verticalScroller.id : 0);
}

public void tile() {
	OS.objc_msgSend(this.id, OS.sel_tile);
}

}
