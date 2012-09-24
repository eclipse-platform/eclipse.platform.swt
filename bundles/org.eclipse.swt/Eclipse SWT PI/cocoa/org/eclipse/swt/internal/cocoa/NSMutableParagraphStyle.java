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

public class NSMutableParagraphStyle extends NSParagraphStyle {

public NSMutableParagraphStyle() {
	super();
}

public NSMutableParagraphStyle(long /*int*/ id) {
	super(id);
}

public NSMutableParagraphStyle(id id) {
	super(id);
}

public void addTabStop(NSTextTab anObject) {
	OS.objc_msgSend(this.id, OS.sel_addTabStop_, anObject != null ? anObject.id : 0);
}

public void setAlignment(long /*int*/ alignment) {
	OS.objc_msgSend(this.id, OS.sel_setAlignment_, alignment);
}

public void setBaseWritingDirection(long /*int*/ writingDirection) {
	OS.objc_msgSend(this.id, OS.sel_setBaseWritingDirection_, writingDirection);
}

public void setDefaultTabInterval(double /*float*/ aFloat) {
	OS.objc_msgSend(this.id, OS.sel_setDefaultTabInterval_, aFloat);
}

public void setFirstLineHeadIndent(double /*float*/ aFloat) {
	OS.objc_msgSend(this.id, OS.sel_setFirstLineHeadIndent_, aFloat);
}

public void setHeadIndent(double /*float*/ aFloat) {
	OS.objc_msgSend(this.id, OS.sel_setHeadIndent_, aFloat);
}

public void setLineBreakMode(long /*int*/ mode) {
	OS.objc_msgSend(this.id, OS.sel_setLineBreakMode_, mode);
}

public void setLineSpacing(double /*float*/ aFloat) {
	OS.objc_msgSend(this.id, OS.sel_setLineSpacing_, aFloat);
}

public void setTabStops(NSArray array) {
	OS.objc_msgSend(this.id, OS.sel_setTabStops_, array != null ? array.id : 0);
}

}
