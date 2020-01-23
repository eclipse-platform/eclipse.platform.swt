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

public class NSTextStorage extends NSMutableAttributedString {

public NSTextStorage() {
	super();
}

public NSTextStorage(long id) {
	super(id);
}

public NSTextStorage(id id) {
	super(id);
}

public void addLayoutManager(NSLayoutManager aLayoutManager) {
	OS.objc_msgSend(this.id, OS.sel_addLayoutManager_, aLayoutManager != null ? aLayoutManager.id : 0);
}

public NSArray paragraphs() {
	long result = OS.objc_msgSend(this.id, OS.sel_paragraphs);
	return result != 0 ? new NSArray(result) : null;
}

}
