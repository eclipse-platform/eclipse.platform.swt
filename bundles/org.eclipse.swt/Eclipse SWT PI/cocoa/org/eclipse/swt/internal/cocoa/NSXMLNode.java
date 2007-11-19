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

public class NSXMLNode extends NSObject {

public NSXMLNode() {
	super();
}

public NSXMLNode(int id) {
	super(id);
}

public static id DTDNodeWithXMLString(NSString string) {
	int result = OS.objc_msgSend(OS.class_NSXMLNode, OS.sel_DTDNodeWithXMLString_1, string != null ? string.id : 0);
	return result != 0 ? new id(result) : null;
}

public NSString URI() {
	int result = OS.objc_msgSend(this.id, OS.sel_URI);
	return result != 0 ? new NSString(result) : null;
}

public NSString XMLString() {
	int result = OS.objc_msgSend(this.id, OS.sel_XMLString);
	return result != 0 ? new NSString(result) : null;
}

public NSString XMLStringWithOptions(int options) {
	int result = OS.objc_msgSend(this.id, OS.sel_XMLStringWithOptions_1, options);
	return result != 0 ? new NSString(result) : null;
}

public NSString XPath() {
	int result = OS.objc_msgSend(this.id, OS.sel_XPath);
	return result != 0 ? new NSString(result) : null;
}

public static id static_attributeWithName_URI_stringValue_(NSString name, NSString URI, NSString stringValue) {
	int result = OS.objc_msgSend(OS.class_NSXMLNode, OS.sel_attributeWithName_1URI_1stringValue_1, name != null ? name.id : 0, URI != null ? URI.id : 0, stringValue != null ? stringValue.id : 0);
	return result != 0 ? new id(result) : null;
}

public static id static_attributeWithName_stringValue_(NSString name, NSString stringValue) {
	int result = OS.objc_msgSend(OS.class_NSXMLNode, OS.sel_attributeWithName_1stringValue_1, name != null ? name.id : 0, stringValue != null ? stringValue.id : 0);
	return result != 0 ? new id(result) : null;
}

public NSString canonicalXMLStringPreservingComments(boolean comments) {
	int result = OS.objc_msgSend(this.id, OS.sel_canonicalXMLStringPreservingComments_1, comments);
	return result != 0 ? new NSString(result) : null;
}

public NSXMLNode childAtIndex(int index) {
	int result = OS.objc_msgSend(this.id, OS.sel_childAtIndex_1, index);
	return result == this.id ? this : (result != 0 ? new NSXMLNode(result) : null);
}

public int childCount() {
	return OS.objc_msgSend(this.id, OS.sel_childCount);
}

public NSArray children() {
	int result = OS.objc_msgSend(this.id, OS.sel_children);
	return result != 0 ? new NSArray(result) : null;
}

public static id commentWithStringValue(NSString stringValue) {
	int result = OS.objc_msgSend(OS.class_NSXMLNode, OS.sel_commentWithStringValue_1, stringValue != null ? stringValue.id : 0);
	return result != 0 ? new id(result) : null;
}

public NSString description() {
	int result = OS.objc_msgSend(this.id, OS.sel_description);
	return result != 0 ? new NSString(result) : null;
}

public void detach() {
	OS.objc_msgSend(this.id, OS.sel_detach);
}

public static id document() {
	int result = OS.objc_msgSend(OS.class_NSXMLNode, OS.sel_document);
	return result != 0 ? new id(result) : null;
}

public static id documentWithRootElement(NSXMLElement element) {
	int result = OS.objc_msgSend(OS.class_NSXMLNode, OS.sel_documentWithRootElement_1, element != null ? element.id : 0);
	return result != 0 ? new id(result) : null;
}

public static id static_elementWithName_(NSString name) {
	int result = OS.objc_msgSend(OS.class_NSXMLNode, OS.sel_elementWithName_1, name != null ? name.id : 0);
	return result != 0 ? new id(result) : null;
}

public static id static_elementWithName_URI_(NSString name, NSString URI) {
	int result = OS.objc_msgSend(OS.class_NSXMLNode, OS.sel_elementWithName_1URI_1, name != null ? name.id : 0, URI != null ? URI.id : 0);
	return result != 0 ? new id(result) : null;
}

public static id static_elementWithName_children_attributes_(NSString name, NSArray children, NSArray attributes) {
	int result = OS.objc_msgSend(OS.class_NSXMLNode, OS.sel_elementWithName_1children_1attributes_1, name != null ? name.id : 0, children != null ? children.id : 0, attributes != null ? attributes.id : 0);
	return result != 0 ? new id(result) : null;
}

public static id static_elementWithName_stringValue_(NSString name, NSString string) {
	int result = OS.objc_msgSend(OS.class_NSXMLNode, OS.sel_elementWithName_1stringValue_1, name != null ? name.id : 0, string != null ? string.id : 0);
	return result != 0 ? new id(result) : null;
}

public int index() {
	return OS.objc_msgSend(this.id, OS.sel_index);
}

public id initWithKind_(int kind) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithKind_1, kind);
	return result != 0 ? new id(result) : null;
}

public id initWithKind_options_(int kind, int options) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithKind_1options_1, kind, options);
	return result != 0 ? new id(result) : null;
}

public int kind() {
	return OS.objc_msgSend(this.id, OS.sel_kind);
}

public int level() {
	return OS.objc_msgSend(this.id, OS.sel_level);
}

public NSString localName() {
	int result = OS.objc_msgSend(this.id, OS.sel_localName);
	return result != 0 ? new NSString(result) : null;
}

public static NSString localNameForName(NSString name) {
	int result = OS.objc_msgSend(OS.class_NSXMLNode, OS.sel_localNameForName_1, name != null ? name.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public NSString name() {
	int result = OS.objc_msgSend(this.id, OS.sel_name);
	return result != 0 ? new NSString(result) : null;
}

public static id namespaceWithName(NSString name, NSString stringValue) {
	int result = OS.objc_msgSend(OS.class_NSXMLNode, OS.sel_namespaceWithName_1stringValue_1, name != null ? name.id : 0, stringValue != null ? stringValue.id : 0);
	return result != 0 ? new id(result) : null;
}

public NSXMLNode nextNode() {
	int result = OS.objc_msgSend(this.id, OS.sel_nextNode);
	return result == this.id ? this : (result != 0 ? new NSXMLNode(result) : null);
}

public NSXMLNode nextSibling() {
	int result = OS.objc_msgSend(this.id, OS.sel_nextSibling);
	return result == this.id ? this : (result != 0 ? new NSXMLNode(result) : null);
}

public NSArray nodesForXPath(NSString xpath, int error) {
	int result = OS.objc_msgSend(this.id, OS.sel_nodesForXPath_1error_1, xpath != null ? xpath.id : 0, error);
	return result != 0 ? new NSArray(result) : null;
}

public id objectValue() {
	int result = OS.objc_msgSend(this.id, OS.sel_objectValue);
	return result != 0 ? new id(result) : null;
}

public NSArray objectsForXQuery_constants_error_(NSString xquery, NSDictionary constants, int error) {
	int result = OS.objc_msgSend(this.id, OS.sel_objectsForXQuery_1constants_1error_1, xquery != null ? xquery.id : 0, constants != null ? constants.id : 0, error);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray objectsForXQuery_error_(NSString xquery, int error) {
	int result = OS.objc_msgSend(this.id, OS.sel_objectsForXQuery_1error_1, xquery != null ? xquery.id : 0, error);
	return result != 0 ? new NSArray(result) : null;
}

public NSXMLNode parent() {
	int result = OS.objc_msgSend(this.id, OS.sel_parent);
	return result == this.id ? this : (result != 0 ? new NSXMLNode(result) : null);
}

public static NSXMLNode predefinedNamespaceForPrefix(NSString name) {
	int result = OS.objc_msgSend(OS.class_NSXMLNode, OS.sel_predefinedNamespaceForPrefix_1, name != null ? name.id : 0);
	return result != 0 ? new NSXMLNode(result) : null;
}

public NSString prefix() {
	int result = OS.objc_msgSend(this.id, OS.sel_prefix);
	return result != 0 ? new NSString(result) : null;
}

public static NSString prefixForName(NSString name) {
	int result = OS.objc_msgSend(OS.class_NSXMLNode, OS.sel_prefixForName_1, name != null ? name.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public NSXMLNode previousNode() {
	int result = OS.objc_msgSend(this.id, OS.sel_previousNode);
	return result == this.id ? this : (result != 0 ? new NSXMLNode(result) : null);
}

public NSXMLNode previousSibling() {
	int result = OS.objc_msgSend(this.id, OS.sel_previousSibling);
	return result == this.id ? this : (result != 0 ? new NSXMLNode(result) : null);
}

public static id processingInstructionWithName(NSString name, NSString stringValue) {
	int result = OS.objc_msgSend(OS.class_NSXMLNode, OS.sel_processingInstructionWithName_1stringValue_1, name != null ? name.id : 0, stringValue != null ? stringValue.id : 0);
	return result != 0 ? new id(result) : null;
}

public NSXMLDocument rootDocument() {
	int result = OS.objc_msgSend(this.id, OS.sel_rootDocument);
	return result != 0 ? new NSXMLDocument(result) : null;
}

public void setName(NSString name) {
	OS.objc_msgSend(this.id, OS.sel_setName_1, name != null ? name.id : 0);
}

public void setObjectValue(id value) {
	OS.objc_msgSend(this.id, OS.sel_setObjectValue_1, value != null ? value.id : 0);
}

public void setStringValue_(NSString string) {
	OS.objc_msgSend(this.id, OS.sel_setStringValue_1, string != null ? string.id : 0);
}

public void setStringValue_resolvingEntities_(NSString string, boolean resolve) {
	OS.objc_msgSend(this.id, OS.sel_setStringValue_1resolvingEntities_1, string != null ? string.id : 0, resolve);
}

public void setURI(NSString URI) {
	OS.objc_msgSend(this.id, OS.sel_setURI_1, URI != null ? URI.id : 0);
}

public NSString stringValue() {
	int result = OS.objc_msgSend(this.id, OS.sel_stringValue);
	return result != 0 ? new NSString(result) : null;
}

public static id textWithStringValue(NSString stringValue) {
	int result = OS.objc_msgSend(OS.class_NSXMLNode, OS.sel_textWithStringValue_1, stringValue != null ? stringValue.id : 0);
	return result != 0 ? new id(result) : null;
}

}
