/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
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

public class NSStatusItem extends NSObject {

public NSStatusItem() {
	super();
}

public NSStatusItem(long id) {
	super(id);
}

public NSStatusItem(id id) {
	super(id);
}

public void drawStatusBarBackgroundInRect(NSRect rect, boolean highlight) {
	OS.objc_msgSend(this.id, OS.sel_drawStatusBarBackgroundInRect_withHighlight_, rect, highlight);
}

public void popUpStatusItemMenu(NSMenu menu) {
	OS.objc_msgSend(this.id, OS.sel_popUpStatusItemMenu_, menu != null ? menu.id : 0);
}

public void setHighlightMode(boolean highlightMode) {
	OS.objc_msgSend(this.id, OS.sel_setHighlightMode_, highlightMode);
}

public void setLength(double length) {
	OS.objc_msgSend(this.id, OS.sel_setLength_, length);
}

public void setView(NSView view) {
	OS.objc_msgSend(this.id, OS.sel_setView_, view != null ? view.id : 0);
}

}
