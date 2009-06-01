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

public class NSAutoreleasePool extends NSObject {

public NSAutoreleasePool() {
	super();
}

public NSAutoreleasePool(int /*long*/ id) {
	super(id);
}

public NSAutoreleasePool(id id) {
	super(id);
}

public static void enableFreedObjectCheck(boolean enable) {
	OS.objc_msgSend(OS.class_NSAutoreleasePool, OS.sel_enableFreedObjectCheck_, enable);
}

}
