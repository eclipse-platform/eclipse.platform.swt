/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSMutableCharacterSet extends NSCharacterSet {

public NSMutableCharacterSet() {
	super();
}

public NSMutableCharacterSet(int id) {
	super(id);
}

public void addCharactersInRange(NSRange aRange) {
	OS.objc_msgSend(this.id, OS.sel_addCharactersInRange_1, aRange);
}

public void addCharactersInString(NSString aString) {
	OS.objc_msgSend(this.id, OS.sel_addCharactersInString_1, aString != null ? aString.id : 0);
}

public void formIntersectionWithCharacterSet(NSCharacterSet otherSet) {
	OS.objc_msgSend(this.id, OS.sel_formIntersectionWithCharacterSet_1, otherSet != null ? otherSet.id : 0);
}

public void formUnionWithCharacterSet(NSCharacterSet otherSet) {
	OS.objc_msgSend(this.id, OS.sel_formUnionWithCharacterSet_1, otherSet != null ? otherSet.id : 0);
}

public void invert() {
	OS.objc_msgSend(this.id, OS.sel_invert);
}

public void removeCharactersInRange(NSRange aRange) {
	OS.objc_msgSend(this.id, OS.sel_removeCharactersInRange_1, aRange);
}

public void removeCharactersInString(NSString aString) {
	OS.objc_msgSend(this.id, OS.sel_removeCharactersInString_1, aString != null ? aString.id : 0);
}

}
