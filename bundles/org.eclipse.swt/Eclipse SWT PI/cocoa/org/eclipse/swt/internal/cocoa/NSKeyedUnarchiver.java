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

public class NSKeyedUnarchiver extends NSCoder {

public NSKeyedUnarchiver() {
	super();
}

public NSKeyedUnarchiver(long /*int*/ id) {
	super(id);
}

public NSKeyedUnarchiver(id id) {
	super(id);
}

public static id unarchiveObjectWithData(NSData data) {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSKeyedUnarchiver, OS.sel_unarchiveObjectWithData_, data != null ? data.id : 0);
	return result != 0 ? new id(result) : null;
}

}
