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

public class NSComboBoxCell extends NSTextFieldCell {

public NSComboBoxCell() {
	super();
}

public NSComboBoxCell(long /*int*/ id) {
	super(id);
}

public NSComboBoxCell(id id) {
	super(id);
}

public NSArray objectValues() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_objectValues);
	return result != 0 ? new NSArray(result) : null;
}

}
