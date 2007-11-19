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

public class NSTextTable extends NSTextBlock {

public NSTextTable() {
	super();
}

public NSTextTable(int id) {
	super(id);
}

public NSRect boundsRectForBlock(NSTextTableBlock block, NSRect contentRect, NSRect rect, NSTextContainer textContainer, NSRange charRange) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_boundsRectForBlock_1contentRect_1inRect_1textContainer_1characterRange_1, block != null ? block.id : 0, contentRect, rect, textContainer != null ? textContainer.id : 0, charRange);
	return result;
}

public boolean collapsesBorders() {
	return OS.objc_msgSend(this.id, OS.sel_collapsesBorders) != 0;
}

public void drawBackgroundForBlock(NSTextTableBlock block, NSRect frameRect, NSView controlView, NSRange charRange, NSLayoutManager layoutManager) {
	OS.objc_msgSend(this.id, OS.sel_drawBackgroundForBlock_1withFrame_1inView_1characterRange_1layoutManager_1, block != null ? block.id : 0, frameRect, controlView != null ? controlView.id : 0, charRange, layoutManager != null ? layoutManager.id : 0);
}

public boolean hidesEmptyCells() {
	return OS.objc_msgSend(this.id, OS.sel_hidesEmptyCells) != 0;
}

public int layoutAlgorithm() {
	return OS.objc_msgSend(this.id, OS.sel_layoutAlgorithm);
}

public int numberOfColumns() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfColumns);
}

public NSRect rectForBlock(NSTextTableBlock block, NSPoint startingPoint, NSRect rect, NSTextContainer textContainer, NSRange charRange) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_rectForBlock_1layoutAtPoint_1inRect_1textContainer_1characterRange_1, block != null ? block.id : 0, startingPoint, rect, textContainer != null ? textContainer.id : 0, charRange);
	return result;
}

public void setCollapsesBorders(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setCollapsesBorders_1, flag);
}

public void setHidesEmptyCells(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setHidesEmptyCells_1, flag);
}

public void setLayoutAlgorithm(int algorithm) {
	OS.objc_msgSend(this.id, OS.sel_setLayoutAlgorithm_1, algorithm);
}

public void setNumberOfColumns(int numCols) {
	OS.objc_msgSend(this.id, OS.sel_setNumberOfColumns_1, numCols);
}

}
