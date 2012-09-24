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

public class NSKeyedArchiver extends NSCoder {

public NSKeyedArchiver() {
	super();
}

public NSKeyedArchiver(long /*int*/ id) {
	super(id);
}

public NSKeyedArchiver(id id) {
	super(id);
}

public static NSData archivedDataWithRootObject(id rootObject) {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSKeyedArchiver, OS.sel_archivedDataWithRootObject_, rootObject != null ? rootObject.id : 0);
	return result != 0 ? new NSData(result) : null;
}

}
