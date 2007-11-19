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

public class NSTextBlock extends NSObject {

public NSTextBlock() {
	super();
}

public NSTextBlock(int id) {
	super(id);
}

public NSColor backgroundColor() {
	int result = OS.objc_msgSend(this.id, OS.sel_backgroundColor);
	return result != 0 ? new NSColor(result) : null;
}

public NSColor borderColorForEdge(int edge) {
	int result = OS.objc_msgSend(this.id, OS.sel_borderColorForEdge_1, edge);
	return result != 0 ? new NSColor(result) : null;
}

public NSRect boundsRectForContentRect(NSRect contentRect, NSRect rect, NSTextContainer textContainer, NSRange charRange) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_boundsRectForContentRect_1inRect_1textContainer_1characterRange_1, contentRect, rect, textContainer != null ? textContainer.id : 0, charRange);
	return result;
}

public float contentWidth() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_contentWidth);
}

public int contentWidthValueType() {
	return OS.objc_msgSend(this.id, OS.sel_contentWidthValueType);
}

public void drawBackgroundWithFrame(NSRect frameRect, NSView controlView, NSRange charRange, NSLayoutManager layoutManager) {
	OS.objc_msgSend(this.id, OS.sel_drawBackgroundWithFrame_1inView_1characterRange_1layoutManager_1, frameRect, controlView != null ? controlView.id : 0, charRange, layoutManager != null ? layoutManager.id : 0);
}

public NSRect rectForLayoutAtPoint(NSPoint startingPoint, NSRect rect, NSTextContainer textContainer, NSRange charRange) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_rectForLayoutAtPoint_1inRect_1textContainer_1characterRange_1, startingPoint, rect, textContainer != null ? textContainer.id : 0, charRange);
	return result;
}

public void setBackgroundColor(NSColor color) {
	OS.objc_msgSend(this.id, OS.sel_setBackgroundColor_1, color != null ? color.id : 0);
}

public void setBorderColor_(NSColor color) {
	OS.objc_msgSend(this.id, OS.sel_setBorderColor_1, color != null ? color.id : 0);
}

public void setBorderColor_forEdge_(NSColor color, int edge) {
	OS.objc_msgSend(this.id, OS.sel_setBorderColor_1forEdge_1, color != null ? color.id : 0, edge);
}

public void setContentWidth(float val, int type) {
	OS.objc_msgSend(this.id, OS.sel_setContentWidth_1type_1, val, type);
}

public void setValue(float val, int type, int dimension) {
	OS.objc_msgSend(this.id, OS.sel_setValue_1type_1forDimension_1, val, type, dimension);
}

public void setVerticalAlignment(int alignment) {
	OS.objc_msgSend(this.id, OS.sel_setVerticalAlignment_1, alignment);
}

public void setWidth_type_forLayer_(float val, int type, int layer) {
	OS.objc_msgSend(this.id, OS.sel_setWidth_1type_1forLayer_1, val, type, layer);
}

public void setWidth_type_forLayer_edge_(float val, int type, int layer, int edge) {
	OS.objc_msgSend(this.id, OS.sel_setWidth_1type_1forLayer_1edge_1, val, type, layer, edge);
}

public float valueForDimension(int dimension) {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_valueForDimension_1, dimension);
}

public int valueTypeForDimension(int dimension) {
	return OS.objc_msgSend(this.id, OS.sel_valueTypeForDimension_1, dimension);
}

public int verticalAlignment() {
	return OS.objc_msgSend(this.id, OS.sel_verticalAlignment);
}

public float widthForLayer(int layer, int edge) {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_widthForLayer_1edge_1, layer, edge);
}

public int widthValueTypeForLayer(int layer, int edge) {
	return OS.objc_msgSend(this.id, OS.sel_widthValueTypeForLayer_1edge_1, layer, edge);
}

}
