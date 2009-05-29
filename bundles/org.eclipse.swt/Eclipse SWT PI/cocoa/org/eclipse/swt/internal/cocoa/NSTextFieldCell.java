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

public class NSTextFieldCell extends NSActionCell {

public NSTextFieldCell() {
	super();
}

public NSTextFieldCell(int /*long*/ id) {
	super(id);
}

public NSTextFieldCell(id id) {
	super(id);
}

public void setPlaceholderString(NSString string) {
	OS.objc_msgSend(this.id, OS.sel_setPlaceholderString_, string != null ? string.id : 0);
}

public void setTextColor(NSColor color) {
	OS.objc_msgSend(this.id, OS.sel_setTextColor_, color != null ? color.id : 0);
}

public NSColor textColor() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_textColor);
	return result != 0 ? new NSColor(result) : null;
}

}
