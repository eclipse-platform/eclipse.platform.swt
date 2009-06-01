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

public class NSParagraphStyle extends NSObject {

public NSParagraphStyle() {
	super();
}

public NSParagraphStyle(int /*long*/ id) {
	super(id);
}

public NSParagraphStyle(id id) {
	super(id);
}

public int /*long*/ alignment() {
	return OS.objc_msgSend(this.id, OS.sel_alignment);
}

public NSArray tabStops() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_tabStops);
	return result != 0 ? new NSArray(result) : null;
}

}
