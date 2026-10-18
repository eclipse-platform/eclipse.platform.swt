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

public class NSDockTile extends NSObject {

public NSDockTile() {
	super();
}

public NSDockTile(long id) {
	super(id);
}

public NSDockTile(id id) {
	super(id);
}

public NSString badgeLabel() {
	long result = OS.objc_msgSend(this.id, OS.sel_badgeLabel);
	return result != 0 ? new NSString(result) : null;
}

public void setBadgeLabel(NSString badgeLabel) {
	OS.objc_msgSend(this.id, OS.sel_setBadgeLabel_, badgeLabel != null ? badgeLabel.id : 0);
}

}
