/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSLocale extends NSObject {

public NSLocale() {
	super();
}

public NSLocale(int /*long*/ id) {
	super(id);
}

public NSLocale(id id) {
	super(id);
}

public NSString displayNameForKey(id key, id value) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_displayNameForKey_value_, key != null ? key.id : 0, value != null ? value.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public id initWithLocaleIdentifier(NSString string) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_initWithLocaleIdentifier_, string != null ? string.id : 0);
	return result != 0 ? new id(result) : null;
}

}
