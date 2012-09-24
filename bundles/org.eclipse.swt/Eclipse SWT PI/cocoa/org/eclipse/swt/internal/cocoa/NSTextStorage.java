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

public class NSTextStorage extends NSMutableAttributedString {

public NSTextStorage() {
	super();
}

public NSTextStorage(long /*int*/ id) {
	super(id);
}

public NSTextStorage(id id) {
	super(id);
}

public void addLayoutManager(NSLayoutManager obj) {
	OS.objc_msgSend(this.id, OS.sel_addLayoutManager_, obj != null ? obj.id : 0);
}

public NSArray paragraphs() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_paragraphs);
	return result != 0 ? new NSArray(result) : null;
}

public static NSAttributedString attributedStringWithAttachment(NSTextAttachment attachment) {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSTextStorage, OS.sel_attributedStringWithAttachment_, attachment != null ? attachment.id : 0);
	return result != 0 ? new NSAttributedString(result) : null;
}

}
