/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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

public class NSBrowserCell extends NSCell {

public NSBrowserCell() {
	super();
}

public NSBrowserCell(long id) {
	super(id);
}

public NSBrowserCell(id id) {
	super(id);
}

public NSColor highlightColorInView(NSView controlView) {
	long result = OS.objc_msgSend(this.id, OS.sel_highlightColorInView_, controlView != null ? controlView.id : 0);
	return result != 0 ? new NSColor(result) : null;
}

public void setLeaf(boolean leaf) {
	OS.objc_msgSend(this.id, OS.sel_setLeaf_, leaf);
}

}
