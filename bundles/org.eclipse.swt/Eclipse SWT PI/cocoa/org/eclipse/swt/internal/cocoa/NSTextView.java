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

public class NSTextView extends NSText {

public NSTextView() {
	super();
}

public NSTextView(long id) {
	super(id);
}

public NSTextView(id id) {
	super(id);
}

public long characterIndexForInsertionAtPoint(NSPoint point) {
	return OS.objc_msgSend(this.id, OS.sel_characterIndexForInsertionAtPoint_, point);
}

public NSParagraphStyle defaultParagraphStyle() {
	long result = OS.objc_msgSend(this.id, OS.sel_defaultParagraphStyle);
	return result != 0 ? new NSParagraphStyle(result) : null;
}

public boolean dragSelectionWithEvent(NSEvent event, NSSize mouseOffset, boolean slideBack) {
	return OS.objc_msgSend_bool(this.id, OS.sel_dragSelectionWithEvent_offset_slideBack_, event != null ? event.id : 0, mouseOffset, slideBack);
}

public void drawViewBackgroundInRect(NSRect rect) {
	OS.objc_msgSend(this.id, OS.sel_drawViewBackgroundInRect_, rect);
}

public NSLayoutManager layoutManager() {
	long result = OS.objc_msgSend(this.id, OS.sel_layoutManager);
	return result != 0 ? new NSLayoutManager(result) : null;
}

public NSDictionary linkTextAttributes() {
	long result = OS.objc_msgSend(this.id, OS.sel_linkTextAttributes);
	return result != 0 ? new NSDictionary(result) : null;
}

public NSDictionary markedTextAttributes() {
	long result = OS.objc_msgSend(this.id, OS.sel_markedTextAttributes);
	return result != 0 ? new NSDictionary(result) : null;
}

public NSDictionary selectedTextAttributes() {
	long result = OS.objc_msgSend(this.id, OS.sel_selectedTextAttributes);
	return result != 0 ? new NSDictionary(result) : null;
}

public void setAllowsUndo(boolean allowsUndo) {
	OS.objc_msgSend(this.id, OS.sel_setAllowsUndo_, allowsUndo);
}

public void setDefaultParagraphStyle(NSParagraphStyle defaultParagraphStyle) {
	OS.objc_msgSend(this.id, OS.sel_setDefaultParagraphStyle_, defaultParagraphStyle != null ? defaultParagraphStyle.id : 0);
}

public void setDisplaysLinkToolTips(boolean displaysLinkToolTips) {
	OS.objc_msgSend(this.id, OS.sel_setDisplaysLinkToolTips_, displaysLinkToolTips);
}

public void setLinkTextAttributes(NSDictionary linkTextAttributes) {
	OS.objc_msgSend(this.id, OS.sel_setLinkTextAttributes_, linkTextAttributes != null ? linkTextAttributes.id : 0);
}

public void setRichText(boolean richText) {
	OS.objc_msgSend(this.id, OS.sel_setRichText_, richText);
}

public void setSelectedTextAttributes(NSDictionary selectedTextAttributes) {
	OS.objc_msgSend(this.id, OS.sel_setSelectedTextAttributes_, selectedTextAttributes != null ? selectedTextAttributes.id : 0);
}

public void setUsesFontPanel(boolean usesFontPanel) {
	OS.objc_msgSend(this.id, OS.sel_setUsesFontPanel_, usesFontPanel);
}

public boolean shouldChangeTextInRange(NSRange affectedCharRange, NSString replacementString) {
	return OS.objc_msgSend_bool(this.id, OS.sel_shouldChangeTextInRange_replacementString_, affectedCharRange, replacementString != null ? replacementString.id : 0);
}

public boolean shouldDrawInsertionPoint() {
	return OS.objc_msgSend_bool(this.id, OS.sel_shouldDrawInsertionPoint);
}

public NSTextContainer textContainer() {
	long result = OS.objc_msgSend(this.id, OS.sel_textContainer);
	return result != 0 ? new NSTextContainer(result) : null;
}

public NSTextStorage textStorage() {
	long result = OS.objc_msgSend(this.id, OS.sel_textStorage);
	return result != 0 ? new NSTextStorage(result) : null;
}

}
