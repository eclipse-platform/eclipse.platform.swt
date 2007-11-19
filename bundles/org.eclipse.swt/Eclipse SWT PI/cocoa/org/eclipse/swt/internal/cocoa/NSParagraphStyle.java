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

public class NSParagraphStyle extends NSObject {

public NSParagraphStyle() {
	super();
}

public NSParagraphStyle(int id) {
	super(id);
}

public int alignment() {
	return OS.objc_msgSend(this.id, OS.sel_alignment);
}

public int baseWritingDirection() {
	return OS.objc_msgSend(this.id, OS.sel_baseWritingDirection);
}

public static NSParagraphStyle defaultParagraphStyle() {
	int result = OS.objc_msgSend(OS.class_NSParagraphStyle, OS.sel_defaultParagraphStyle);
	return result != 0 ? new NSParagraphStyle(result) : null;
}

public float defaultTabInterval() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_defaultTabInterval);
}

public static int defaultWritingDirectionForLanguage(NSString languageName) {
	return OS.objc_msgSend(OS.class_NSParagraphStyle, OS.sel_defaultWritingDirectionForLanguage_1, languageName != null ? languageName.id : 0);
}

public float firstLineHeadIndent() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_firstLineHeadIndent);
}

public float headIndent() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_headIndent);
}

public int headerLevel() {
	return OS.objc_msgSend(this.id, OS.sel_headerLevel);
}

public float hyphenationFactor() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_hyphenationFactor);
}

public int lineBreakMode() {
	return OS.objc_msgSend(this.id, OS.sel_lineBreakMode);
}

public float lineHeightMultiple() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_lineHeightMultiple);
}

public float lineSpacing() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_lineSpacing);
}

public float maximumLineHeight() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_maximumLineHeight);
}

public float minimumLineHeight() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_minimumLineHeight);
}

public float paragraphSpacing() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_paragraphSpacing);
}

public float paragraphSpacingBefore() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_paragraphSpacingBefore);
}

public NSArray tabStops() {
	int result = OS.objc_msgSend(this.id, OS.sel_tabStops);
	return result != 0 ? new NSArray(result) : null;
}

public float tailIndent() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_tailIndent);
}

public NSArray textBlocks() {
	int result = OS.objc_msgSend(this.id, OS.sel_textBlocks);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray textLists() {
	int result = OS.objc_msgSend(this.id, OS.sel_textLists);
	return result != 0 ? new NSArray(result) : null;
}

public float tighteningFactorForTruncation() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_tighteningFactorForTruncation);
}

}
