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

public class NSXMLDTDNode extends NSXMLNode {

public NSXMLDTDNode() {
	super();
}

public NSXMLDTDNode(int id) {
	super(id);
}

public int DTDKind() {
	return OS.objc_msgSend(this.id, OS.sel_DTDKind);
}

public id initWithXMLString(NSString string) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithXMLString_1, string != null ? string.id : 0);
	return result != 0 ? new id(result) : null;
}

public boolean isExternal() {
	return OS.objc_msgSend(this.id, OS.sel_isExternal) != 0;
}

public NSString notationName() {
	int result = OS.objc_msgSend(this.id, OS.sel_notationName);
	return result != 0 ? new NSString(result) : null;
}

public NSString publicID() {
	int result = OS.objc_msgSend(this.id, OS.sel_publicID);
	return result != 0 ? new NSString(result) : null;
}

public void setDTDKind(int kind) {
	OS.objc_msgSend(this.id, OS.sel_setDTDKind_1, kind);
}

public void setNotationName(NSString notationName) {
	OS.objc_msgSend(this.id, OS.sel_setNotationName_1, notationName != null ? notationName.id : 0);
}

public void setPublicID(NSString publicID) {
	OS.objc_msgSend(this.id, OS.sel_setPublicID_1, publicID != null ? publicID.id : 0);
}

public void setSystemID(NSString systemID) {
	OS.objc_msgSend(this.id, OS.sel_setSystemID_1, systemID != null ? systemID.id : 0);
}

public NSString systemID() {
	int result = OS.objc_msgSend(this.id, OS.sel_systemID);
	return result != 0 ? new NSString(result) : null;
}

}
