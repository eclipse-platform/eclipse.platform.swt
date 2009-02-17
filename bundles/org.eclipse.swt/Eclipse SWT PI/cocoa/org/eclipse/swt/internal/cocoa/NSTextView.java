/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSTextView extends NSText {

public NSTextView() {
	super();
}

public NSTextView(int /*long*/ id) {
	super(id);
}

public NSTextView(id id) {
	super(id);
}

public int /*long*/ characterIndexForInsertionAtPoint(NSPoint point) {
	return OS.objc_msgSend(this.id, OS.sel_characterIndexForInsertionAtPoint_, point);
}

public boolean dragSelectionWithEvent(NSEvent event, NSSize mouseOffset, boolean slideBack) {
	return OS.objc_msgSend_bool(this.id, OS.sel_dragSelectionWithEvent_offset_slideBack_, event != null ? event.id : 0, mouseOffset, slideBack);
}

public NSDictionary markedTextAttributes() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_markedTextAttributes);
	return result != 0 ? new NSDictionary(result) : null;
}

public void setRichText(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setRichText_, flag);
}

public boolean shouldChangeTextInRange(NSRange affectedCharRange, NSString replacementString) {
	return OS.objc_msgSend_bool(this.id, OS.sel_shouldChangeTextInRange_replacementString_, affectedCharRange, replacementString != null ? replacementString.id : 0);
}

public NSTextContainer textContainer() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_textContainer);
	return result != 0 ? new NSTextContainer(result) : null;
}

public NSTextStorage textStorage() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_textStorage);
	return result != 0 ? new NSTextStorage(result) : null;
}

}
