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

public class NSXMLDTD extends NSXMLNode {

public NSXMLDTD() {
	super();
}

public NSXMLDTD(int id) {
	super(id);
}

public void addChild(NSXMLNode child) {
	OS.objc_msgSend(this.id, OS.sel_addChild_1, child != null ? child.id : 0);
}

public NSXMLDTDNode attributeDeclarationForName(NSString name, NSString elementName) {
	int result = OS.objc_msgSend(this.id, OS.sel_attributeDeclarationForName_1elementName_1, name != null ? name.id : 0, elementName != null ? elementName.id : 0);
	return result != 0 ? new NSXMLDTDNode(result) : null;
}

public NSXMLDTDNode elementDeclarationForName(NSString name) {
	int result = OS.objc_msgSend(this.id, OS.sel_elementDeclarationForName_1, name != null ? name.id : 0);
	return result != 0 ? new NSXMLDTDNode(result) : null;
}

public NSXMLDTDNode entityDeclarationForName(NSString name) {
	int result = OS.objc_msgSend(this.id, OS.sel_entityDeclarationForName_1, name != null ? name.id : 0);
	return result != 0 ? new NSXMLDTDNode(result) : null;
}

public id initWithContentsOfURL(NSURL url, int mask, int error) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithContentsOfURL_1options_1error_1, url != null ? url.id : 0, mask, error);
	return result != 0 ? new id(result) : null;
}

public id initWithData(NSData data, int mask, int error) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithData_1options_1error_1, data != null ? data.id : 0, mask, error);
	return result != 0 ? new id(result) : null;
}

public void insertChild(NSXMLNode child, int index) {
	OS.objc_msgSend(this.id, OS.sel_insertChild_1atIndex_1, child != null ? child.id : 0, index);
}

public void insertChildren(NSArray children, int index) {
	OS.objc_msgSend(this.id, OS.sel_insertChildren_1atIndex_1, children != null ? children.id : 0, index);
}

public NSXMLDTDNode notationDeclarationForName(NSString name) {
	int result = OS.objc_msgSend(this.id, OS.sel_notationDeclarationForName_1, name != null ? name.id : 0);
	return result != 0 ? new NSXMLDTDNode(result) : null;
}

public static NSXMLDTDNode predefinedEntityDeclarationForName(NSString name) {
	int result = OS.objc_msgSend(OS.class_NSXMLDTD, OS.sel_predefinedEntityDeclarationForName_1, name != null ? name.id : 0);
	return result != 0 ? new NSXMLDTDNode(result) : null;
}

public NSString publicID() {
	int result = OS.objc_msgSend(this.id, OS.sel_publicID);
	return result != 0 ? new NSString(result) : null;
}

public void removeChildAtIndex(int index) {
	OS.objc_msgSend(this.id, OS.sel_removeChildAtIndex_1, index);
}

public void replaceChildAtIndex(int index, NSXMLNode node) {
	OS.objc_msgSend(this.id, OS.sel_replaceChildAtIndex_1withNode_1, index, node != null ? node.id : 0);
}

public void setChildren(NSArray children) {
	OS.objc_msgSend(this.id, OS.sel_setChildren_1, children != null ? children.id : 0);
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
