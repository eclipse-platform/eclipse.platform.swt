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

public class NSMutableParagraphStyle extends NSParagraphStyle {

public NSMutableParagraphStyle() {
	super();
}

public NSMutableParagraphStyle(int id) {
	super(id);
}

public void addTabStop(NSTextTab anObject) {
	OS.objc_msgSend(this.id, OS.sel_addTabStop_1, anObject != null ? anObject.id : 0);
}

public void removeTabStop(NSTextTab anObject) {
	OS.objc_msgSend(this.id, OS.sel_removeTabStop_1, anObject != null ? anObject.id : 0);
}

public void setAlignment(int alignment) {
	OS.objc_msgSend(this.id, OS.sel_setAlignment_1, alignment);
}

public void setBaseWritingDirection(int writingDirection) {
	OS.objc_msgSend(this.id, OS.sel_setBaseWritingDirection_1, writingDirection);
}

public void setDefaultTabInterval(float aFloat) {
	OS.objc_msgSend(this.id, OS.sel_setDefaultTabInterval_1, aFloat);
}

public void setFirstLineHeadIndent(float aFloat) {
	OS.objc_msgSend(this.id, OS.sel_setFirstLineHeadIndent_1, aFloat);
}

public void setHeadIndent(float aFloat) {
	OS.objc_msgSend(this.id, OS.sel_setHeadIndent_1, aFloat);
}

public void setHeaderLevel(int level) {
	OS.objc_msgSend(this.id, OS.sel_setHeaderLevel_1, level);
}

public void setHyphenationFactor(float aFactor) {
	OS.objc_msgSend(this.id, OS.sel_setHyphenationFactor_1, aFactor);
}

public void setLineBreakMode(int mode) {
	OS.objc_msgSend(this.id, OS.sel_setLineBreakMode_1, mode);
}

public void setLineHeightMultiple(float aFloat) {
	OS.objc_msgSend(this.id, OS.sel_setLineHeightMultiple_1, aFloat);
}

public void setLineSpacing(float aFloat) {
	OS.objc_msgSend(this.id, OS.sel_setLineSpacing_1, aFloat);
}

public void setMaximumLineHeight(float aFloat) {
	OS.objc_msgSend(this.id, OS.sel_setMaximumLineHeight_1, aFloat);
}

public void setMinimumLineHeight(float aFloat) {
	OS.objc_msgSend(this.id, OS.sel_setMinimumLineHeight_1, aFloat);
}

public void setParagraphSpacing(float aFloat) {
	OS.objc_msgSend(this.id, OS.sel_setParagraphSpacing_1, aFloat);
}

public void setParagraphSpacingBefore(float aFloat) {
	OS.objc_msgSend(this.id, OS.sel_setParagraphSpacingBefore_1, aFloat);
}

public void setParagraphStyle(NSParagraphStyle obj) {
	OS.objc_msgSend(this.id, OS.sel_setParagraphStyle_1, obj != null ? obj.id : 0);
}

public void setTabStops(NSArray array) {
	OS.objc_msgSend(this.id, OS.sel_setTabStops_1, array != null ? array.id : 0);
}

public void setTailIndent(float aFloat) {
	OS.objc_msgSend(this.id, OS.sel_setTailIndent_1, aFloat);
}

public void setTextBlocks(NSArray array) {
	OS.objc_msgSend(this.id, OS.sel_setTextBlocks_1, array != null ? array.id : 0);
}

public void setTextLists(NSArray array) {
	OS.objc_msgSend(this.id, OS.sel_setTextLists_1, array != null ? array.id : 0);
}

public void setTighteningFactorForTruncation(float aFactor) {
	OS.objc_msgSend(this.id, OS.sel_setTighteningFactorForTruncation_1, aFactor);
}

}
