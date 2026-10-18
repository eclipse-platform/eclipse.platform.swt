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

public class NSTextContainer extends NSObject {

public NSTextContainer() {
	super();
}

public NSTextContainer(long id) {
	super(id);
}

public NSTextContainer(id id) {
	super(id);
}

public NSTextContainer initWithContainerSize(NSSize aContainerSize) {
	long result = OS.objc_msgSend(this.id, OS.sel_initWithContainerSize_, aContainerSize);
	return result == this.id ? this : (result != 0 ? new NSTextContainer(result) : null);
}

public void setContainerSize(NSSize containerSize) {
	OS.objc_msgSend(this.id, OS.sel_setContainerSize_, containerSize);
}

public void setLineFragmentPadding(double lineFragmentPadding) {
	OS.objc_msgSend(this.id, OS.sel_setLineFragmentPadding_, lineFragmentPadding);
}

public void setWidthTracksTextView(boolean widthTracksTextView) {
	OS.objc_msgSend(this.id, OS.sel_setWidthTracksTextView_, widthTracksTextView);
}

}
