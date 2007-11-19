/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSTextTab extends NSObject {

public NSTextTab() {
	super();
}

public NSTextTab(int id) {
	super(id);
}

public int alignment() {
	return OS.objc_msgSend(this.id, OS.sel_alignment);
}

public NSTextTab initWithTextAlignment(int alignment, float loc, NSDictionary options) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithTextAlignment_1location_1options_1, alignment, loc, options != null ? options.id : 0);
	return result != 0 ? this : null;
}

public NSTextTab initWithType(int type, float loc) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithType_1location_1, type, loc);
	return result != 0 ? this : null;
}

public float location() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_location);
}

public NSDictionary options() {
	int result = OS.objc_msgSend(this.id, OS.sel_options);
	return result != 0 ? new NSDictionary(result) : null;
}

public int tabStopType() {
	return OS.objc_msgSend(this.id, OS.sel_tabStopType);
}

}
