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

public class NSTextTab extends NSObject {

public NSTextTab() {
	super();
}

public NSTextTab(int /*long*/ id) {
	super(id);
}

public NSTextTab(id id) {
	super(id);
}

public NSTextTab initWithType(int /*long*/ type, float /*double*/ loc) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_initWithType_location_, type, loc);
	return result == this.id ? this : (result != 0 ? new NSTextTab(result) : null);
}

public float /*double*/ location() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_location);
}

public int /*long*/ tabStopType() {
	return OS.objc_msgSend(this.id, OS.sel_tabStopType);
}

}
