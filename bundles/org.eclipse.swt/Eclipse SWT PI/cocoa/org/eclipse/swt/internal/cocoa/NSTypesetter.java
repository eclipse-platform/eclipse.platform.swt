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

public class NSTypesetter extends NSObject {

public NSTypesetter() {
	super();
}

public NSTypesetter(long id) {
	super(id);
}

public NSTypesetter(id id) {
	super(id);
}

public double baselineOffsetInLayoutManager(NSLayoutManager layoutMgr, long glyphIndex) {
	return OS.objc_msgSend_fpret(this.id, OS.sel_baselineOffsetInLayoutManager_glyphIndex_, layoutMgr != null ? layoutMgr.id : 0, glyphIndex);
}

}
