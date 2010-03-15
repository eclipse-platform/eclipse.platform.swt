/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSTextAttachment extends NSObject {

public NSTextAttachment() {
	super();
}

public NSTextAttachment(int /*long*/ id) {
	super(id);
}

public NSTextAttachment(id id) {
	super(id);
}

public id initWithFileWrapper(NSFileWrapper fileWrapper) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_initWithFileWrapper_, fileWrapper != null ? fileWrapper.id : 0);
	return result != 0 ? new id(result) : null;
}

public void setAttachmentCell(id cell) {
	OS.objc_msgSend(this.id, OS.sel_setAttachmentCell_, cell != null ? cell.id : 0);
}

}
