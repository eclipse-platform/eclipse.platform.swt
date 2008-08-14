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

public class NSButtonCell extends NSActionCell {

public NSButtonCell() {
	super();
}

public NSButtonCell(int /*long*/ id) {
	super(id);
}

public NSButtonCell(id id) {
	super(id);
}

public void setBackgroundColor(NSColor color) {
	OS.objc_msgSend(this.id, OS.sel_setBackgroundColor_, color != null ? color.id : 0);
}

public void setButtonType(int aType) {
	OS.objc_msgSend(this.id, OS.sel_setButtonType_, aType);
}

public void setImagePosition(int aPosition) {
	OS.objc_msgSend(this.id, OS.sel_setImagePosition_, aPosition);
}

public NSString title() {
	int result = OS.objc_msgSend(this.id, OS.sel_title);
	return result != 0 ? new NSString(result) : null;
}

}
