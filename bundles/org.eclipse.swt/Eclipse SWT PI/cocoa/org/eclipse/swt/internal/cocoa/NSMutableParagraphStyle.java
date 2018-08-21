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

public void setBaseWritingDirection(long /*int*/ baseWritingDirection) {
	OS.objc_msgSend(this.id, OS.sel_setBaseWritingDirection_, baseWritingDirection);
}

public void setDefaultTabInterval(double /*float*/ defaultTabInterval) {
	OS.objc_msgSend(this.id, OS.sel_setDefaultTabInterval_, defaultTabInterval);
}

public void setFirstLineHeadIndent(double /*float*/ firstLineHeadIndent) {
	OS.objc_msgSend(this.id, OS.sel_setFirstLineHeadIndent_, firstLineHeadIndent);
}

public void setHeadIndent(double /*float*/ headIndent) {
	OS.objc_msgSend(this.id, OS.sel_setHeadIndent_, headIndent);
}

public void setLineBreakMode(long /*int*/ lineBreakMode) {
	OS.objc_msgSend(this.id, OS.sel_setLineBreakMode_, lineBreakMode);
}

public void setLineSpacing(double /*float*/ lineSpacing) {
	OS.objc_msgSend(this.id, OS.sel_setLineSpacing_, lineSpacing);
}

public void setTabStops(NSArray tabStops) {
	OS.objc_msgSend(this.id, OS.sel_setTabStops_, tabStops != null ? tabStops.id : 0);
}

}
