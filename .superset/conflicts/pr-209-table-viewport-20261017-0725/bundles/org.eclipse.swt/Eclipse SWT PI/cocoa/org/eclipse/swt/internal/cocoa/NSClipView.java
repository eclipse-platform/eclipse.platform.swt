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

public class NSClipView extends NSView {

public NSClipView() {
	super();
}

public NSClipView(long id) {
	super(id);
}

public NSClipView(id id) {
	super(id);
}

public boolean copiesOnScroll() {
	return OS.objc_msgSend_bool(this.id, OS.sel_copiesOnScroll);
}

public void setCopiesOnScroll(boolean copiesOnScroll) {
	OS.objc_msgSend(this.id, OS.sel_setCopiesOnScroll_, copiesOnScroll);
}

public void setDocumentCursor(NSCursor documentCursor) {
	OS.objc_msgSend(this.id, OS.sel_setDocumentCursor_, documentCursor != null ? documentCursor.id : 0);
}

public void setDrawsBackground(boolean drawsBackground) {
	OS.objc_msgSend(this.id, OS.sel_setDrawsBackground_, drawsBackground);
}

}
