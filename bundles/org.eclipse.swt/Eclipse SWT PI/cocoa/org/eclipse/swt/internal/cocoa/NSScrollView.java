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

public class NSScrollView extends NSView {

public NSScrollView() {
	super();
}

public NSScrollView(int id) {
	super(id);
}

public boolean autohidesScrollers() {
	return OS.objc_msgSend(this.id, OS.sel_autohidesScrollers) != 0;
}

public NSColor backgroundColor() {
	int result = OS.objc_msgSend(this.id, OS.sel_backgroundColor);
	return result != 0 ? new NSColor(result) : null;
}

public int borderType() {
	return OS.objc_msgSend(this.id, OS.sel_borderType);
}

public NSSize contentSize() {
	NSSize result = new NSSize();
	OS.objc_msgSend_struct(result, this.id, OS.sel_contentSize);
	return result;
}

public static NSSize contentSizeForFrameSize(NSSize fSize, boolean hFlag, boolean vFlag, int aType) {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, OS.class_NSScrollView, OS.sel_contentSizeForFrameSize_1hasHorizontalScroller_1hasVerticalScroller_1borderType_1, fSize, hFlag, vFlag, aType);
	return result;
}

public NSClipView contentView() {
	int result = OS.objc_msgSend(this.id, OS.sel_contentView);
	return result != 0 ? new NSClipView(result) : null;
}

public NSCursor documentCursor() {
	int result = OS.objc_msgSend(this.id, OS.sel_documentCursor);
	return result != 0 ? new NSCursor(result) : null;
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

public static NSSize frameSizeForContentSize(NSSize cSize, boolean hFlag, boolean vFlag, int aType) {
	NSSize result = new NSSize();
	OS.objc_msgSend_struct(result, OS.class_NSScrollView, OS.sel_frameSizeForContentSize_1hasHorizontalScroller_1hasVerticalScroller_1borderType_1, cSize, hFlag, vFlag, aType);
	return result;
}

public boolean hasHorizontalRuler() {
	return OS.objc_msgSend(this.id, OS.sel_hasHorizontalRuler) != 0;
}

public boolean hasHorizontalScroller() {
	return OS.objc_msgSend(this.id, OS.sel_hasHorizontalScroller) != 0;
}

public boolean hasVerticalRuler() {
	return OS.objc_msgSend(this.id, OS.sel_hasVerticalRuler) != 0;
}

public boolean hasVerticalScroller() {
	return OS.objc_msgSend(this.id, OS.sel_hasVerticalScroller) != 0;
}

public float horizontalLineScroll() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_horizontalLineScroll);
}

public float horizontalPageScroll() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_horizontalPageScroll);
}

public NSRulerView horizontalRulerView() {
	int result = OS.objc_msgSend(this.id, OS.sel_horizontalRulerView);
	return result != 0 ? new NSRulerView(result) : null;
}

public NSScroller horizontalScroller() {
	int result = OS.objc_msgSend(this.id, OS.sel_horizontalScroller);
	return result != 0 ? new NSScroller(result) : null;
}

public float lineScroll() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_lineScroll);
}

public float pageScroll() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_pageScroll);
}

public void reflectScrolledClipView(NSClipView cView) {
	OS.objc_msgSend(this.id, OS.sel_reflectScrolledClipView_1, cView != null ? cView.id : 0);
}

public static int rulerViewClass() {
	return OS.objc_msgSend(OS.class_NSScrollView, OS.sel_rulerViewClass);
}

public boolean rulersVisible() {
	return OS.objc_msgSend(this.id, OS.sel_rulersVisible) != 0;
}

public void scrollWheel(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_scrollWheel_1, theEvent != null ? theEvent.id : 0);
}

public boolean scrollsDynamically() {
	return OS.objc_msgSend(this.id, OS.sel_scrollsDynamically) != 0;
}

public void setAutohidesScrollers(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAutohidesScrollers_1, flag);
}

public void setBackgroundColor(NSColor color) {
	OS.objc_msgSend(this.id, OS.sel_setBackgroundColor_1, color != null ? color.id : 0);
}

public void setBorderType(int aType) {
	OS.objc_msgSend(this.id, OS.sel_setBorderType_1, aType);
}

public void setContentView(NSClipView contentView) {
	OS.objc_msgSend(this.id, OS.sel_setContentView_1, contentView != null ? contentView.id : 0);
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

public void setHasHorizontalRuler(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setHasHorizontalRuler_1, flag);
}

public void setHasHorizontalScroller(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setHasHorizontalScroller_1, flag);
}

public void setHasVerticalRuler(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setHasVerticalRuler_1, flag);
}

public void setHasVerticalScroller(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setHasVerticalScroller_1, flag);
}

public void setHorizontalLineScroll(float value) {
	OS.objc_msgSend(this.id, OS.sel_setHorizontalLineScroll_1, value);
}

public void setHorizontalPageScroll(float value) {
	OS.objc_msgSend(this.id, OS.sel_setHorizontalPageScroll_1, value);
}

public void setHorizontalRulerView(NSRulerView ruler) {
	OS.objc_msgSend(this.id, OS.sel_setHorizontalRulerView_1, ruler != null ? ruler.id : 0);
}

public void setHorizontalScroller(NSScroller anObject) {
	OS.objc_msgSend(this.id, OS.sel_setHorizontalScroller_1, anObject != null ? anObject.id : 0);
}

public void setLineScroll(float value) {
	OS.objc_msgSend(this.id, OS.sel_setLineScroll_1, value);
}

public void setPageScroll(float value) {
	OS.objc_msgSend(this.id, OS.sel_setPageScroll_1, value);
}

public static void setRulerViewClass(int rulerViewClass) {
	OS.objc_msgSend(OS.class_NSScrollView, OS.sel_setRulerViewClass_1, rulerViewClass);
}

public void setRulersVisible(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setRulersVisible_1, flag);
}

public void setScrollsDynamically(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setScrollsDynamically_1, flag);
}

public void setVerticalLineScroll(float value) {
	OS.objc_msgSend(this.id, OS.sel_setVerticalLineScroll_1, value);
}

public void setVerticalPageScroll(float value) {
	OS.objc_msgSend(this.id, OS.sel_setVerticalPageScroll_1, value);
}

public void setVerticalRulerView(NSRulerView ruler) {
	OS.objc_msgSend(this.id, OS.sel_setVerticalRulerView_1, ruler != null ? ruler.id : 0);
}

public void setVerticalScroller(NSScroller anObject) {
	OS.objc_msgSend(this.id, OS.sel_setVerticalScroller_1, anObject != null ? anObject.id : 0);
}

public void tile() {
	OS.objc_msgSend(this.id, OS.sel_tile);
}

public float verticalLineScroll() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_verticalLineScroll);
}

public float verticalPageScroll() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_verticalPageScroll);
}

public NSRulerView verticalRulerView() {
	int result = OS.objc_msgSend(this.id, OS.sel_verticalRulerView);
	return result != 0 ? new NSRulerView(result) : null;
}

public NSScroller verticalScroller() {
	int result = OS.objc_msgSend(this.id, OS.sel_verticalScroller);
	return result != 0 ? new NSScroller(result) : null;
}

}
