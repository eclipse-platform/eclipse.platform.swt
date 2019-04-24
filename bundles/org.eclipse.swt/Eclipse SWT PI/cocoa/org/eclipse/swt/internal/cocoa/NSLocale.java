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

public class NSLocale extends NSObject {

public NSLocale() {
	super();
}

public NSLocale(long id) {
	super(id);
}

public NSLocale(id id) {
	super(id);
}

public NSString displayNameForKey(id key, id value) {
	long result = OS.objc_msgSend(this.id, OS.sel_displayNameForKey_value_, key != null ? key.id : 0, value != null ? value.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public NSLocale initWithLocaleIdentifier(NSString string) {
	long result = OS.objc_msgSend(this.id, OS.sel_initWithLocaleIdentifier_, string != null ? string.id : 0);
	return result == this.id ? this : (result != 0 ? new NSLocale(result) : null);
}

}
