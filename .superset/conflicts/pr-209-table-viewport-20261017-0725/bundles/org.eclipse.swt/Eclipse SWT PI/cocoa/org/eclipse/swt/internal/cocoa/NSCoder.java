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

public class NSCoder extends NSObject {

public NSCoder() {
	super();
}

public NSCoder(long id) {
	super(id);
}

public NSCoder(id id) {
	super(id);
}

public int systemVersion() {
	return (int)OS.objc_msgSend(this.id, OS.sel_systemVersion);
}

}
