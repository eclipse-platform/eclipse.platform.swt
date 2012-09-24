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

public class NSCharacterSet extends NSObject {

public NSCharacterSet() {
	super();
}

public NSCharacterSet(long /*int*/ id) {
	super(id);
}

public NSCharacterSet(id id) {
	super(id);
}

public boolean characterIsMember(short aCharacter) {
	return OS.objc_msgSend_bool(this.id, OS.sel_characterIsMember_, aCharacter);
}

public static id decimalDigitCharacterSet() {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSCharacterSet, OS.sel_decimalDigitCharacterSet);
	return result != 0 ? new id(result) : null;
}

}
