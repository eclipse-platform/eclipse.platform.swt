/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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

public class NSTabViewItem extends NSObject {

public NSTabViewItem() {
	super();
}

public NSTabViewItem(long id) {
	super(id);
}

public NSTabViewItem(id id) {
	super(id);
}

public void drawLabel(boolean shouldTruncateLabel, NSRect labelRect) {
	OS.objc_msgSend(this.id, OS.sel_drawLabel_inRect_, shouldTruncateLabel, labelRect);
}

public NSTabViewItem initWithIdentifier(id identifier) {
	long result = OS.objc_msgSend(this.id, OS.sel_initWithIdentifier_, identifier != null ? identifier.id : 0);
	return result == this.id ? this : (result != 0 ? new NSTabViewItem(result) : null);
}

public void setLabel(NSString label) {
	OS.objc_msgSend(this.id, OS.sel_setLabel_, label != null ? label.id : 0);
}

public void setView(NSView view) {
	OS.objc_msgSend(this.id, OS.sel_setView_, view != null ? view.id : 0);
}

public NSSize sizeOfLabel(boolean computeMin) {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_sizeOfLabel_, computeMin);
	return result;
}

}
