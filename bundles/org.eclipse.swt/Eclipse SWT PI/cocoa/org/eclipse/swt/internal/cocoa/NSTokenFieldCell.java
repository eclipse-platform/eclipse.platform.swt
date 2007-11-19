/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSTokenFieldCell extends NSTextFieldCell {

public NSTokenFieldCell() {
	super();
}

public NSTokenFieldCell(int id) {
	super(id);
}

public double completionDelay() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_completionDelay);
}

public static double defaultCompletionDelay() {
	return OS.objc_msgSend_fpret(OS.class_NSTokenFieldCell, OS.sel_defaultCompletionDelay);
}

public static NSCharacterSet defaultTokenizingCharacterSet() {
	int result = OS.objc_msgSend(OS.class_NSTokenFieldCell, OS.sel_defaultTokenizingCharacterSet);
	return result != 0 ? new NSCharacterSet(result) : null;
}

public id delegate() {
	int result = OS.objc_msgSend(this.id, OS.sel_delegate);
	return result != 0 ? new id(result) : null;
}

public void setCompletionDelay(double delay) {
	OS.objc_msgSend(this.id, OS.sel_setCompletionDelay_1, delay);
}

public void setDelegate(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_1, anObject != null ? anObject.id : 0);
}

public void setTokenStyle(int style) {
	OS.objc_msgSend(this.id, OS.sel_setTokenStyle_1, style);
}

public void setTokenizingCharacterSet(NSCharacterSet characterSet) {
	OS.objc_msgSend(this.id, OS.sel_setTokenizingCharacterSet_1, characterSet != null ? characterSet.id : 0);
}

public int tokenStyle() {
	return OS.objc_msgSend(this.id, OS.sel_tokenStyle);
}

public NSCharacterSet tokenizingCharacterSet() {
	int result = OS.objc_msgSend(this.id, OS.sel_tokenizingCharacterSet);
	return result != 0 ? new NSCharacterSet(result) : null;
}

}
