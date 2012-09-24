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

public class NSBrowserCell extends NSCell {

public NSBrowserCell() {
	super();
}

public NSBrowserCell(long /*int*/ id) {
	super(id);
}

public NSBrowserCell(id id) {
	super(id);
}

public NSColor highlightColorInView(NSView controlView) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_highlightColorInView_, controlView != null ? controlView.id : 0);
	return result != 0 ? new NSColor(result) : null;
}

public void setLeaf(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setLeaf_, flag);
}

}
