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

public class NSAppearance extends NSObject {

public NSAppearance() {
	super();
}

public NSAppearance(long id) {
	super(id);
}

public NSAppearance(id id) {
	super(id);
}

public static NSAppearance appearanceNamed(NSString name) {
	long result = OS.objc_msgSend(OS.class_NSAppearance, OS.sel_appearanceNamed_, name != null ? name.id : 0);
	return result != 0 ? new NSAppearance(result) : null;
}

public static NSAppearance currentAppearance() {
	long result = OS.objc_msgSend(OS.class_NSAppearance, OS.sel_currentAppearance);
	return result != 0 ? new NSAppearance(result) : null;
}

public NSString name() {
	long result = OS.objc_msgSend(this.id, OS.sel_name);
	return result != 0 ? new NSString(result) : null;
}

}
