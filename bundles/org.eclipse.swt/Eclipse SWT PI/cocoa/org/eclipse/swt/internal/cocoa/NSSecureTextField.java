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

public class NSSecureTextField extends NSTextField {

public NSSecureTextField() {
	super();
}

public NSSecureTextField(int /*long*/ id) {
	super(id);
}

public NSSecureTextField(id id) {
	super(id);
}

public static int /*long*/ cellClass() {
	return OS.objc_msgSend(OS.class_NSSecureTextField, OS.sel_cellClass);
}

public static void setCellClass(int /*long*/ factoryId) {
	OS.objc_msgSend(OS.class_NSSecureTextField, OS.sel_setCellClass_, factoryId);
}

}
