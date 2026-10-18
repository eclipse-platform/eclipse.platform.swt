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

public class NSTextAttachment extends NSObject {

public NSTextAttachment() {
	super();
}

public NSTextAttachment(long id) {
	super(id);
}

public NSTextAttachment(id id) {
	super(id);
}

public NSTextAttachment initWithFileWrapper(NSFileWrapper fileWrapper) {
	long result = OS.objc_msgSend(this.id, OS.sel_initWithFileWrapper_, fileWrapper != null ? fileWrapper.id : 0);
	return result == this.id ? this : (result != 0 ? new NSTextAttachment(result) : null);
}

public void setAttachmentCell(id attachmentCell) {
	OS.objc_msgSend(this.id, OS.sel_setAttachmentCell_, attachmentCell != null ? attachmentCell.id : 0);
}

}
