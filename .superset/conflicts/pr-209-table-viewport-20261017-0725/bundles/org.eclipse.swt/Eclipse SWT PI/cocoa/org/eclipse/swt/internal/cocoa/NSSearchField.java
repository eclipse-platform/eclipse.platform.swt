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

public class NSSearchField extends NSTextField {

public NSSearchField() {
	super();
}

public NSSearchField(long id) {
	super(id);
}

public NSSearchField(id id) {
	super(id);
}

public NSArray recentSearches() {
	long result = OS.objc_msgSend(this.id, OS.sel_recentSearches);
	return result != 0 ? new NSArray(result) : null;
}

public static long cellClass() {
	return OS.objc_msgSend(OS.class_NSSearchField, OS.sel_cellClass);
}

public static void setCellClass(long factoryId) {
	OS.objc_msgSend(OS.class_NSSearchField, OS.sel_setCellClass_, factoryId);
}

}
