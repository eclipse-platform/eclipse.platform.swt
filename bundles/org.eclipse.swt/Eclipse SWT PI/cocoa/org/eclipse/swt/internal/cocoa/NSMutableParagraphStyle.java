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

public class NSMutableParagraphStyle extends NSParagraphStyle {

public NSMutableParagraphStyle() {
	super();
}

public NSMutableParagraphStyle(int /*long*/ id) {
	super(id);
}

public NSMutableParagraphStyle(id id) {
	super(id);
}

public void setAlignment(int /*long*/ alignment) {
	OS.objc_msgSend(this.id, OS.sel_setAlignment_, alignment);
}

public void setFirstLineHeadIndent(float /*double*/ aFloat) {
	OS.objc_msgSend(this.id, OS.sel_setFirstLineHeadIndent_, aFloat);
}

public void setLineSpacing(float /*double*/ aFloat) {
	OS.objc_msgSend(this.id, OS.sel_setLineSpacing_, aFloat);
}

}
