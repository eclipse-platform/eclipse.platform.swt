/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSAssertionHandler extends NSObject {

public NSAssertionHandler() {
	super();
}

public NSAssertionHandler(long /*int*/ id) {
	super(id);
}

public NSAssertionHandler(id id) {
	super(id);
}

public static NSAssertionHandler currentHandler() {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSAssertionHandler, OS.sel_currentHandler);
	return result != 0 ? new NSAssertionHandler(result) : null;
}

public void handleFailureInFunction(NSString functionName, NSString fileName, long /*int*/ line, NSString format) {
	OS.objc_msgSend(this.id, OS.sel_handleFailureInFunction_file_lineNumber_description_, functionName != null ? functionName.id : 0, fileName != null ? fileName.id : 0, line, format != null ? format.id : 0);
}

public void handleFailureInMethod(long /*int*/ selector, id object, NSString fileName, long /*int*/ line, NSString format) {
	OS.objc_msgSend(this.id, OS.sel_handleFailureInMethod_object_file_lineNumber_description_, selector, object != null ? object.id : 0, fileName != null ? fileName.id : 0, line, format != null ? format.id : 0);
}

}
