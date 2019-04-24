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

public class NSKeyedUnarchiver extends NSCoder {

public NSKeyedUnarchiver() {
	super();
}

public NSKeyedUnarchiver(long id) {
	super(id);
}

public NSKeyedUnarchiver(id id) {
	super(id);
}

public static id unarchiveObjectWithData(NSData data) {
	long result = OS.objc_msgSend(OS.class_NSKeyedUnarchiver, OS.sel_unarchiveObjectWithData_, data != null ? data.id : 0);
	return result != 0 ? new id(result) : null;
}

}
