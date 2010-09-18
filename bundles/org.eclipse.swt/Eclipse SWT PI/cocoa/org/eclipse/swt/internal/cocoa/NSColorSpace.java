/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSColorSpace extends NSObject {

public NSColorSpace() {
	super();
}

public NSColorSpace(int /*long*/ id) {
	super(id);
}

public NSColorSpace(id id) {
	super(id);
}

public static NSColorSpace genericRGBColorSpace() {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSColorSpace, OS.sel_genericRGBColorSpace);
	return result != 0 ? new NSColorSpace(result) : null;
}

}
