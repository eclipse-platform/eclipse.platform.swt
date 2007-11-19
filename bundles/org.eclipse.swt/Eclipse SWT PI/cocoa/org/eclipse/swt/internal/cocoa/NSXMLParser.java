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

public class NSXMLParser extends NSObject {

public NSXMLParser() {
	super();
}

public NSXMLParser(int id) {
	super(id);
}

public void abortParsing() {
	OS.objc_msgSend(this.id, OS.sel_abortParsing);
}

public int columnNumber() {
	return OS.objc_msgSend(this.id, OS.sel_columnNumber);
}

public id delegate() {
	int result = OS.objc_msgSend(this.id, OS.sel_delegate);
	return result != 0 ? new id(result) : null;
}

public id initWithContentsOfURL(NSURL url) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithContentsOfURL_1, url != null ? url.id : 0);
	return result != 0 ? new id(result) : null;
}

public id initWithData(NSData data) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithData_1, data != null ? data.id : 0);
	return result != 0 ? new id(result) : null;
}

public int lineNumber() {
	return OS.objc_msgSend(this.id, OS.sel_lineNumber);
}

public boolean parse() {
	return OS.objc_msgSend(this.id, OS.sel_parse) != 0;
}

public NSError parserError() {
	int result = OS.objc_msgSend(this.id, OS.sel_parserError);
	return result != 0 ? new NSError(result) : null;
}

public NSString publicID() {
	int result = OS.objc_msgSend(this.id, OS.sel_publicID);
	return result != 0 ? new NSString(result) : null;
}

public void setDelegate(id delegate) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_1, delegate != null ? delegate.id : 0);
}

public void setShouldProcessNamespaces(boolean shouldProcessNamespaces) {
	OS.objc_msgSend(this.id, OS.sel_setShouldProcessNamespaces_1, shouldProcessNamespaces);
}

public void setShouldReportNamespacePrefixes(boolean shouldReportNamespacePrefixes) {
	OS.objc_msgSend(this.id, OS.sel_setShouldReportNamespacePrefixes_1, shouldReportNamespacePrefixes);
}

public void setShouldResolveExternalEntities(boolean shouldResolveExternalEntities) {
	OS.objc_msgSend(this.id, OS.sel_setShouldResolveExternalEntities_1, shouldResolveExternalEntities);
}

public boolean shouldProcessNamespaces() {
	return OS.objc_msgSend(this.id, OS.sel_shouldProcessNamespaces) != 0;
}

public boolean shouldReportNamespacePrefixes() {
	return OS.objc_msgSend(this.id, OS.sel_shouldReportNamespacePrefixes) != 0;
}

public boolean shouldResolveExternalEntities() {
	return OS.objc_msgSend(this.id, OS.sel_shouldResolveExternalEntities) != 0;
}

public NSString systemID() {
	int result = OS.objc_msgSend(this.id, OS.sel_systemID);
	return result != 0 ? new NSString(result) : null;
}

}
