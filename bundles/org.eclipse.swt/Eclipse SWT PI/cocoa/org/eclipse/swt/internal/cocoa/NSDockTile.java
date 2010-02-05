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

public class NSDockTile extends NSObject {

public NSDockTile() {
	super();
}

public NSDockTile(int /*long*/ id) {
	super(id);
}

public NSDockTile(id id) {
	super(id);
}

public NSString badgeLabel() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_badgeLabel);
	return result != 0 ? new NSString(result) : null;
}

public void setBadgeLabel(NSString string) {
	OS.objc_msgSend(this.id, OS.sel_setBadgeLabel_, string != null ? string.id : 0);
}

}
