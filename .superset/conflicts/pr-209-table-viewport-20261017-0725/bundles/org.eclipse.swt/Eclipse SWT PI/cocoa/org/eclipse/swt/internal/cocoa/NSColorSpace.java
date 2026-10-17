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

public class NSColorSpace extends NSObject {

public NSColorSpace() {
	super();
}

public NSColorSpace(long id) {
	super(id);
}

public NSColorSpace(id id) {
	super(id);
}

public long colorSpaceModel() {
	return OS.objc_msgSend(this.id, OS.sel_colorSpaceModel);
}

}
