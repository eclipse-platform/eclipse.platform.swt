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

public class NSEPSImageRep extends NSImageRep {

public NSEPSImageRep() {
	super();
}

public NSEPSImageRep(int id) {
	super(id);
}

public NSData EPSRepresentation() {
	int result = OS.objc_msgSend(this.id, OS.sel_EPSRepresentation);
	return result != 0 ? new NSData(result) : null;
}

public NSRect boundingBox() {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_boundingBox);
	return result;
}

public static id imageRepWithData(NSData epsData) {
	int result = OS.objc_msgSend(OS.class_NSEPSImageRep, OS.sel_imageRepWithData_1, epsData != null ? epsData.id : 0);
	return result != 0 ? new id(result) : null;
}

public id initWithData(NSData epsData) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithData_1, epsData != null ? epsData.id : 0);
	return result != 0 ? new id(result) : null;
}

public void prepareGState() {
	OS.objc_msgSend(this.id, OS.sel_prepareGState);
}

}
