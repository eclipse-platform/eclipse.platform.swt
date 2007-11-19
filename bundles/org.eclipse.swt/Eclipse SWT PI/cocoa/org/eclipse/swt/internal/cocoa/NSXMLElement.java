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

public class NSXMLElement extends NSXMLNode {

public NSXMLElement() {
	super();
}

public NSXMLElement(int id) {
	super(id);
}

public void addAttribute(NSXMLNode attribute) {
	OS.objc_msgSend(this.id, OS.sel_addAttribute_1, attribute != null ? attribute.id : 0);
}

public void addChild(NSXMLNode child) {
	OS.objc_msgSend(this.id, OS.sel_addChild_1, child != null ? child.id : 0);
}

public void addNamespace(NSXMLNode aNamespace) {
	OS.objc_msgSend(this.id, OS.sel_addNamespace_1, aNamespace != null ? aNamespace.id : 0);
}

public NSXMLNode attributeForLocalName(NSString localName, NSString URI) {
	int result = OS.objc_msgSend(this.id, OS.sel_attributeForLocalName_1URI_1, localName != null ? localName.id : 0, URI != null ? URI.id : 0);
	return result != 0 ? new NSXMLNode(result) : null;
}

public NSXMLNode attributeForName(NSString name) {
	int result = OS.objc_msgSend(this.id, OS.sel_attributeForName_1, name != null ? name.id : 0);
	return result != 0 ? new NSXMLNode(result) : null;
}

public NSArray attributes() {
	int result = OS.objc_msgSend(this.id, OS.sel_attributes);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray elementsForLocalName(NSString localName, NSString URI) {
	int result = OS.objc_msgSend(this.id, OS.sel_elementsForLocalName_1URI_1, localName != null ? localName.id : 0, URI != null ? URI.id : 0);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray elementsForName(NSString name) {
	int result = OS.objc_msgSend(this.id, OS.sel_elementsForName_1, name != null ? name.id : 0);
	return result != 0 ? new NSArray(result) : null;
}

public id initWithName_(NSString name) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithName_1, name != null ? name.id : 0);
	return result != 0 ? new id(result) : null;
}

public id initWithName_URI_(NSString name, NSString URI) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithName_1URI_1, name != null ? name.id : 0, URI != null ? URI.id : 0);
	return result != 0 ? new id(result) : null;
}

public id initWithName_stringValue_(NSString name, NSString string) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithName_1stringValue_1, name != null ? name.id : 0, string != null ? string.id : 0);
	return result != 0 ? new id(result) : null;
}

public id initWithXMLString(NSString string, int error) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithXMLString_1error_1, string != null ? string.id : 0, error);
	return result != 0 ? new id(result) : null;
}

public void insertChild(NSXMLNode child, int index) {
	OS.objc_msgSend(this.id, OS.sel_insertChild_1atIndex_1, child != null ? child.id : 0, index);
}

public void insertChildren(NSArray children, int index) {
	OS.objc_msgSend(this.id, OS.sel_insertChildren_1atIndex_1, children != null ? children.id : 0, index);
}

public NSXMLNode namespaceForPrefix(NSString name) {
	int result = OS.objc_msgSend(this.id, OS.sel_namespaceForPrefix_1, name != null ? name.id : 0);
	return result != 0 ? new NSXMLNode(result) : null;
}

public NSArray namespaces() {
	int result = OS.objc_msgSend(this.id, OS.sel_namespaces);
	return result != 0 ? new NSArray(result) : null;
}

public void normalizeAdjacentTextNodesPreservingCDATA(boolean preserve) {
	OS.objc_msgSend(this.id, OS.sel_normalizeAdjacentTextNodesPreservingCDATA_1, preserve);
}

public void removeAttributeForName(NSString name) {
	OS.objc_msgSend(this.id, OS.sel_removeAttributeForName_1, name != null ? name.id : 0);
}

public void removeChildAtIndex(int index) {
	OS.objc_msgSend(this.id, OS.sel_removeChildAtIndex_1, index);
}

public void removeNamespaceForPrefix(NSString name) {
	OS.objc_msgSend(this.id, OS.sel_removeNamespaceForPrefix_1, name != null ? name.id : 0);
}

public void replaceChildAtIndex(int index, NSXMLNode node) {
	OS.objc_msgSend(this.id, OS.sel_replaceChildAtIndex_1withNode_1, index, node != null ? node.id : 0);
}

public NSXMLNode resolveNamespaceForName(NSString name) {
	int result = OS.objc_msgSend(this.id, OS.sel_resolveNamespaceForName_1, name != null ? name.id : 0);
	return result != 0 ? new NSXMLNode(result) : null;
}

public NSString resolvePrefixForNamespaceURI(NSString namespaceURI) {
	int result = OS.objc_msgSend(this.id, OS.sel_resolvePrefixForNamespaceURI_1, namespaceURI != null ? namespaceURI.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public void setAttributes(NSArray attributes) {
	OS.objc_msgSend(this.id, OS.sel_setAttributes_1, attributes != null ? attributes.id : 0);
}

public void setAttributesAsDictionary(NSDictionary attributes) {
	OS.objc_msgSend(this.id, OS.sel_setAttributesAsDictionary_1, attributes != null ? attributes.id : 0);
}

public void setChildren(NSArray children) {
	OS.objc_msgSend(this.id, OS.sel_setChildren_1, children != null ? children.id : 0);
}

public void setNamespaces(NSArray namespaces) {
	OS.objc_msgSend(this.id, OS.sel_setNamespaces_1, namespaces != null ? namespaces.id : 0);
}

}
