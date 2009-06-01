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

public class NSFormatter extends NSObject {

public NSFormatter() {
	super();
}

public NSFormatter(int /*long*/ id) {
	super(id);
}

public NSFormatter(id id) {
	super(id);
}

public NSString stringForObjectValue(id obj) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_stringForObjectValue_, obj != null ? obj.id : 0);
	return result != 0 ? new NSString(result) : null;
}

}
