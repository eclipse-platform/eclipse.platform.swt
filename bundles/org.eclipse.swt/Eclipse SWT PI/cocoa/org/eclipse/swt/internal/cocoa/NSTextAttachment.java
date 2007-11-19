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

public class NSTextAttachment extends NSObject {

public NSTextAttachment() {
	super();
}

public NSTextAttachment(int id) {
	super(id);
}

public id  attachmentCell() {
	int result = OS.objc_msgSend(this.id, OS.sel_attachmentCell);
	return result != 0 ? new id (result) : null;
}

public NSFileWrapper fileWrapper() {
	int result = OS.objc_msgSend(this.id, OS.sel_fileWrapper);
	return result != 0 ? new NSFileWrapper(result) : null;
}

public id initWithFileWrapper(NSFileWrapper fileWrapper) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithFileWrapper_1, fileWrapper != null ? fileWrapper.id : 0);
	return result != 0 ? new id(result) : null;
}

public void setAttachmentCell(id  cell) {
	OS.objc_msgSend(this.id, OS.sel_setAttachmentCell_1, cell != null ? cell.id : 0);
}

public void setFileWrapper(NSFileWrapper fileWrapper) {
	OS.objc_msgSend(this.id, OS.sel_setFileWrapper_1, fileWrapper != null ? fileWrapper.id : 0);
}

}
