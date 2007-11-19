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

public class NSToolbarItemGroup extends NSToolbarItem {

public NSToolbarItemGroup() {
	super();
}

public NSToolbarItemGroup(int id) {
	super(id);
}

public void setSubitems(NSArray subitems) {
	OS.objc_msgSend(this.id, OS.sel_setSubitems_1, subitems != null ? subitems.id : 0);
}

public NSArray subitems() {
	int result = OS.objc_msgSend(this.id, OS.sel_subitems);
	return result != 0 ? new NSArray(result) : null;
}

}
