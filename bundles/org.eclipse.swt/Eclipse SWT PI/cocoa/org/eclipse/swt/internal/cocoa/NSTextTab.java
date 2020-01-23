/*******************************************************************************
 * Copyright (c) 2000, 2019 IBM Corporation and others.
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

public class NSTextTab extends NSObject {

public NSTextTab() {
	super();
}

public NSTextTab(long id) {
	super(id);
}

public NSTextTab(id id) {
	super(id);
}

public NSTextTab initWithType(long type, double loc) {
	long result = OS.objc_msgSend(this.id, OS.sel_initWithType_location_, type, loc);
	return result == this.id ? this : (result != 0 ? new NSTextTab(result) : null);
}

}
