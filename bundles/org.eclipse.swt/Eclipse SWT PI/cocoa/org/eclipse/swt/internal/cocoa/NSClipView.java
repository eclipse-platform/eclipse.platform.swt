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

public class NSClipView extends NSView {

public NSClipView() {
	super();
}

public NSClipView(int /*long*/ id) {
	super(id);
}

public NSClipView(id id) {
	super(id);
}

public boolean copiesOnScroll() {
	return OS.objc_msgSend_bool(this.id, OS.sel_copiesOnScroll);
}

public NSCursor documentCursor() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_documentCursor);
	return result != 0 ? new NSCursor(result) : null;
}

public void scrollToPoint(NSPoint newOrigin) {
	OS.objc_msgSend(this.id, OS.sel_scrollToPoint_, newOrigin);
}

public void setCopiesOnScroll(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setCopiesOnScroll_, flag);
}

public void setDocumentCursor(NSCursor anObj) {
	OS.objc_msgSend(this.id, OS.sel_setDocumentCursor_, anObj != null ? anObj.id : 0);
}

public void setDrawsBackground(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setDrawsBackground_, flag);
}

}
