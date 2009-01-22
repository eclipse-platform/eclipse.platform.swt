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

public class NSSearchFieldCell extends NSTextFieldCell {

public NSSearchFieldCell() {
	super();
}

public NSSearchFieldCell(int /*long*/ id) {
	super(id);
}

public NSSearchFieldCell(id id) {
	super(id);
}

public NSButtonCell cancelButtonCell() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_cancelButtonCell);
	return result != 0 ? new NSButtonCell(result) : null;
}

public NSButtonCell searchButtonCell() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_searchButtonCell);
	return result != 0 ? new NSButtonCell(result) : null;
}

public void setCancelButtonCell(NSButtonCell cell) {
	OS.objc_msgSend(this.id, OS.sel_setCancelButtonCell_, cell != null ? cell.id : 0);
}

public void setSearchButtonCell(NSButtonCell cell) {
	OS.objc_msgSend(this.id, OS.sel_setSearchButtonCell_, cell != null ? cell.id : 0);
}

}
