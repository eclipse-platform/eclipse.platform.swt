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

public class NSTextList extends NSObject {

public NSTextList() {
	super();
}

public NSTextList(int id) {
	super(id);
}

public NSTextList initWithMarkerFormat(NSString format, int mask) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithMarkerFormat_1options_1, format != null ? format.id : 0, mask);
	return result != 0 ? this : null;
}

public int listOptions() {
	return OS.objc_msgSend(this.id, OS.sel_listOptions);
}

public NSString markerForItemNumber(int itemNum) {
	int result = OS.objc_msgSend(this.id, OS.sel_markerForItemNumber_1, itemNum);
	return result != 0 ? new NSString(result) : null;
}

public NSString markerFormat() {
	int result = OS.objc_msgSend(this.id, OS.sel_markerFormat);
	return result != 0 ? new NSString(result) : null;
}

}
