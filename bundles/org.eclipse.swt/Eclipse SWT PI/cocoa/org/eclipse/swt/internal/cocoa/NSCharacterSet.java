/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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

public class NSCharacterSet extends NSObject {

public NSCharacterSet() {
	super();
}

public NSCharacterSet(long id) {
	super(id);
}

public NSCharacterSet(id id) {
	super(id);
}

public boolean characterIsMember(short aCharacter) {
	return OS.objc_msgSend_bool(this.id, OS.sel_characterIsMember_, aCharacter);
}

public static NSCharacterSet decimalDigitCharacterSet() {
	long result = OS.objc_msgSend(OS.class_NSCharacterSet, OS.sel_decimalDigitCharacterSet);
	return result != 0 ? new NSCharacterSet(result) : null;
}

}
