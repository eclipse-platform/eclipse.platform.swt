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

public class NSTextContainer extends NSObject {

public NSTextContainer() {
	super();
}

public NSTextContainer(int /*long*/ id) {
	super(id);
}

public NSTextContainer(id id) {
	super(id);
}

public NSSize containerSize() {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_containerSize);
	return result;
}

public NSTextContainer initWithContainerSize(NSSize size) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_initWithContainerSize_, size);
	return result == this.id ? this : (result != 0 ? new NSTextContainer(result) : null);
}

public void setContainerSize(NSSize size) {
	OS.objc_msgSend(this.id, OS.sel_setContainerSize_, size);
}

public void setLineFragmentPadding(float /*double*/ pad) {
	OS.objc_msgSend(this.id, OS.sel_setLineFragmentPadding_, pad);
}

public void setWidthTracksTextView(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setWidthTracksTextView_, flag);
}

}
