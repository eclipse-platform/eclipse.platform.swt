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

public class NSURLDownload extends NSObject {

public NSURLDownload() {
	super();
}

public NSURLDownload(int id) {
	super(id);
}

public static boolean canResumeDownloadDecodedWithEncodingMIMEType(NSString MIMEType) {
	return OS.objc_msgSend(OS.class_NSURLDownload, OS.sel_canResumeDownloadDecodedWithEncodingMIMEType_1, MIMEType != null ? MIMEType.id : 0) != 0;
}

public void cancel() {
	OS.objc_msgSend(this.id, OS.sel_cancel);
}

public boolean deletesFileUponFailure() {
	return OS.objc_msgSend(this.id, OS.sel_deletesFileUponFailure) != 0;
}

public id initWithRequest(NSURLRequest request, id delegate) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithRequest_1delegate_1, request != null ? request.id : 0, delegate != null ? delegate.id : 0);
	return result != 0 ? new id(result) : null;
}

public id initWithResumeData(NSData resumeData, id delegate, NSString path) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithResumeData_1delegate_1path_1, resumeData != null ? resumeData.id : 0, delegate != null ? delegate.id : 0, path != null ? path.id : 0);
	return result != 0 ? new id(result) : null;
}

public NSURLRequest request() {
	int result = OS.objc_msgSend(this.id, OS.sel_request);
	return result != 0 ? new NSURLRequest(result) : null;
}

public NSData resumeData() {
	int result = OS.objc_msgSend(this.id, OS.sel_resumeData);
	return result != 0 ? new NSData(result) : null;
}

public void setDeletesFileUponFailure(boolean deletesFileUponFailure) {
	OS.objc_msgSend(this.id, OS.sel_setDeletesFileUponFailure_1, deletesFileUponFailure);
}

public void setDestination(NSString path, boolean allowOverwrite) {
	OS.objc_msgSend(this.id, OS.sel_setDestination_1allowOverwrite_1, path != null ? path.id : 0, allowOverwrite);
}

}
