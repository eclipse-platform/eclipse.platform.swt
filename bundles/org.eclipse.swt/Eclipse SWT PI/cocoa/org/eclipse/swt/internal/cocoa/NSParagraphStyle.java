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

public class NSParagraphStyle extends NSObject {

public NSParagraphStyle() {
	super();
}

public NSParagraphStyle(long id) {
	super(id);
}

public NSParagraphStyle(id id) {
	super(id);
}

public long alignment() {
	return OS.objc_msgSend(this.id, OS.sel_alignment);
}

public NSArray tabStops() {
	long result = OS.objc_msgSend(this.id, OS.sel_tabStops);
	return result != 0 ? new NSArray(result) : null;
}

}
