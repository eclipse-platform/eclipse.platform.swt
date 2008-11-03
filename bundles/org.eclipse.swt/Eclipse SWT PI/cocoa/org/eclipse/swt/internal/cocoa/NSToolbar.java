/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSToolbar extends NSObject {

public NSToolbar() {
	super();
}

public NSToolbar(int /*long*/ id) {
	super(id);
}

public NSToolbar(id id) {
	super(id);
}

public id initWithIdentifier(NSString identifier) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_initWithIdentifier_, identifier != null ? identifier.id : 0);
	return result != 0 ? new id(result) : null;
}

public void setVisible(boolean shown) {
	OS.objc_msgSend(this.id, OS.sel_setVisible_, shown);
}

}
