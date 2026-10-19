/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
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

public class NSFileWrapper extends NSObject {

public NSFileWrapper() {
	super();
}

public NSFileWrapper(long id) {
	super(id);
}

public NSFileWrapper(id id) {
	super(id);
}

public void setIcon(NSImage icon) {
	OS.objc_msgSend(this.id, OS.sel_setIcon_, icon != null ? icon.id : 0);
}

}
