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

public class NSXMLDocument extends NSXMLNode {

public NSXMLDocument() {
	super();
}

public NSXMLDocument(int id) {
	super(id);
}

public NSXMLDTD DTD() {
	int result = OS.objc_msgSend(this.id, OS.sel_DTD);
	return result != 0 ? new NSXMLDTD(result) : null;
}

public NSString MIMEType() {
	int result = OS.objc_msgSend(this.id, OS.sel_MIMEType);
	return result != 0 ? new NSString(result) : null;
}

public NSData XMLData() {
	int result = OS.objc_msgSend(this.id, OS.sel_XMLData);
	return result != 0 ? new NSData(result) : null;
}

public NSData XMLDataWithOptions(int options) {
	int result = OS.objc_msgSend(this.id, OS.sel_XMLDataWithOptions_1, options);
	return result != 0 ? new NSData(result) : null;
}

public void addChild(NSXMLNode child) {
	OS.objc_msgSend(this.id, OS.sel_addChild_1, child != null ? child.id : 0);
}

public NSString characterEncoding() {
	int result = OS.objc_msgSend(this.id, OS.sel_characterEncoding);
	return result != 0 ? new NSString(result) : null;
}

public int documentContentKind() {
	return OS.objc_msgSend(this.id, OS.sel_documentContentKind);
}

public id initWithContentsOfURL(NSURL url, int mask, int error) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithContentsOfURL_1options_1error_1, url != null ? url.id : 0, mask, error);
	return result != 0 ? new id(result) : null;
}

public id initWithData(NSData data, int mask, int error) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithData_1options_1error_1, data != null ? data.id : 0, mask, error);
	return result != 0 ? new id(result) : null;
}

public id initWithRootElement(NSXMLElement element) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithRootElement_1, element != null ? element.id : 0);
	return result != 0 ? new id(result) : null;
}

public id initWithXMLString(NSString string, int mask, int error) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithXMLString_1options_1error_1, string != null ? string.id : 0, mask, error);
	return result != 0 ? new id(result) : null;
}

public void insertChild(NSXMLNode child, int index) {
	OS.objc_msgSend(this.id, OS.sel_insertChild_1atIndex_1, child != null ? child.id : 0, index);
}

public void insertChildren(NSArray children, int index) {
	OS.objc_msgSend(this.id, OS.sel_insertChildren_1atIndex_1, children != null ? children.id : 0, index);
}

public boolean isStandalone() {
	return OS.objc_msgSend(this.id, OS.sel_isStandalone) != 0;
}

public id objectByApplyingXSLT(NSData xslt, NSDictionary arguments, int error) {
	int result = OS.objc_msgSend(this.id, OS.sel_objectByApplyingXSLT_1arguments_1error_1, xslt != null ? xslt.id : 0, arguments != null ? arguments.id : 0, error);
	return result != 0 ? new id(result) : null;
}

public id objectByApplyingXSLTAtURL(NSURL xsltURL, NSDictionary argument, int error) {
	int result = OS.objc_msgSend(this.id, OS.sel_objectByApplyingXSLTAtURL_1arguments_1error_1, xsltURL != null ? xsltURL.id : 0, argument != null ? argument.id : 0, error);
	return result != 0 ? new id(result) : null;
}

public id objectByApplyingXSLTString(NSString xslt, NSDictionary arguments, int error) {
	int result = OS.objc_msgSend(this.id, OS.sel_objectByApplyingXSLTString_1arguments_1error_1, xslt != null ? xslt.id : 0, arguments != null ? arguments.id : 0, error);
	return result != 0 ? new id(result) : null;
}

public void removeChildAtIndex(int index) {
	OS.objc_msgSend(this.id, OS.sel_removeChildAtIndex_1, index);
}

public void replaceChildAtIndex(int index, NSXMLNode node) {
	OS.objc_msgSend(this.id, OS.sel_replaceChildAtIndex_1withNode_1, index, node != null ? node.id : 0);
}

public static int replacementClassForClass(int cls) {
	return OS.objc_msgSend(OS.class_NSXMLDocument, OS.sel_replacementClassForClass_1, cls);
}

public NSXMLElement rootElement() {
	int result = OS.objc_msgSend(this.id, OS.sel_rootElement);
	return result != 0 ? new NSXMLElement(result) : null;
}

public void setCharacterEncoding(NSString encoding) {
	OS.objc_msgSend(this.id, OS.sel_setCharacterEncoding_1, encoding != null ? encoding.id : 0);
}

public void setChildren(NSArray children) {
	OS.objc_msgSend(this.id, OS.sel_setChildren_1, children != null ? children.id : 0);
}

public void setDTD(NSXMLDTD documentTypeDeclaration) {
	OS.objc_msgSend(this.id, OS.sel_setDTD_1, documentTypeDeclaration != null ? documentTypeDeclaration.id : 0);
}

public void setDocumentContentKind(int kind) {
	OS.objc_msgSend(this.id, OS.sel_setDocumentContentKind_1, kind);
}

public void setMIMEType(NSString MIMEType) {
	OS.objc_msgSend(this.id, OS.sel_setMIMEType_1, MIMEType != null ? MIMEType.id : 0);
}

public void setRootElement(NSXMLNode root) {
	OS.objc_msgSend(this.id, OS.sel_setRootElement_1, root != null ? root.id : 0);
}

public void setStandalone(boolean standalone) {
	OS.objc_msgSend(this.id, OS.sel_setStandalone_1, standalone);
}

public void setVersion(NSString version) {
	OS.objc_msgSend(this.id, OS.sel_setVersion_1, version != null ? version.id : 0);
}

public boolean validateAndReturnError(int error) {
	return OS.objc_msgSend(this.id, OS.sel_validateAndReturnError_1, error) != 0;
}

//public NSString version() {
//	int result = OS.objc_msgSend(this.id, OS.sel_version);
//	return result != 0 ? new NSString(result) : null;
//}

}
