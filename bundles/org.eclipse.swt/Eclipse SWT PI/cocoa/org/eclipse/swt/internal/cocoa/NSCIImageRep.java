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

public class NSCIImageRep extends NSImageRep {

public NSCIImageRep() {
	super();
}

public NSCIImageRep(int id) {
	super(id);
}

public CIImage CIImage() {
	int result = OS.objc_msgSend(this.id, OS.sel_CIImage);
	return result != 0 ? new CIImage(result) : null;
}

public static id imageRepWithCIImage(CIImage image) {
	int result = OS.objc_msgSend(OS.class_NSCIImageRep, OS.sel_imageRepWithCIImage_1, image != null ? image.id : 0);
	return result != 0 ? new id(result) : null;
}

public NSCIImageRep initWithCIImage(CIImage image) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithCIImage_1, image != null ? image.id : 0);
	return result != 0 ? this : null;
}

}
