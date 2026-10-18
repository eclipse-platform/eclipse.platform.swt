/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
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

public class NSSecureTextField extends NSTextField {

public NSSecureTextField() {
	super();
}

public NSSecureTextField(long id) {
	super(id);
}

public NSSecureTextField(id id) {
	super(id);
}

public static long cellClass() {
	return OS.objc_msgSend(OS.class_NSSecureTextField, OS.sel_cellClass);
}

public static void setCellClass(long factoryId) {
	OS.objc_msgSend(OS.class_NSSecureTextField, OS.sel_setCellClass_, factoryId);
}

}
