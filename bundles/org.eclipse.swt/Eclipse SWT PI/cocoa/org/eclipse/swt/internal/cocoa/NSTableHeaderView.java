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

public class NSTableHeaderView extends NSView {

public NSTableHeaderView() {
	super();
}

public NSTableHeaderView(int /*long*/ id) {
	super(id);
}

public NSTableHeaderView(id id) {
	super(id);
}

public int /*long*/ columnAtPoint(NSPoint point) {
	return OS.objc_msgSend(this.id, OS.sel_columnAtPoint_, point);
}

public NSRect headerRectOfColumn(int /*long*/ column) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_headerRectOfColumn_, column);
	return result;
}

}
