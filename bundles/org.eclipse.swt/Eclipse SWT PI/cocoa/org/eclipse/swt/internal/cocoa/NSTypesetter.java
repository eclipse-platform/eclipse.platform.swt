/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSTypesetter extends NSObject {

public NSTypesetter() {
	super();
}

public NSTypesetter(int /*long*/ id) {
	super(id);
}

public NSTypesetter(id id) {
	super(id);
}

public float /*double*/ baselineOffsetInLayoutManager(NSLayoutManager layoutMgr, int /*long*/ glyphIndex) {
	return (float /*double*/)OS.objc_msgSend_fpret(this.id, OS.sel_baselineOffsetInLayoutManager_glyphIndex_, layoutMgr != null ? layoutMgr.id : 0, glyphIndex);
}

}
