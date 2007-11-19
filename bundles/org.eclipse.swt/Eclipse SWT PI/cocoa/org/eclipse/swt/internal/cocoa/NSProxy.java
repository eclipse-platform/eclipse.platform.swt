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

public class NSProxy extends NSObject {

public NSProxy() {
	super();
}

public NSProxy(int id) {
	super(id);
}

//public static id alloc() {
//	int result = OS.objc_msgSend(OS.class_NSProxy, OS.sel_alloc);
//	return result != 0 ? new id(result) : null;
//}

public static id allocWithZone(int zone) {
	int result = OS.objc_msgSend(OS.class_NSProxy, OS.sel_allocWithZone_1, zone);
	return result != 0 ? new id(result) : null;
}

//public static int class() {
//	return OS.objc_msgSend(OS.class_NSProxy, OS.sel_class);
//}

public void dealloc() {
	OS.objc_msgSend(this.id, OS.sel_dealloc);
}

public NSString description() {
	int result = OS.objc_msgSend(this.id, OS.sel_description);
	return result != 0 ? new NSString(result) : null;
}

public void finalize() {
	OS.objc_msgSend(this.id, OS.sel_finalize);
}

public void forwardInvocation(NSInvocation invocation) {
	OS.objc_msgSend(this.id, OS.sel_forwardInvocation_1, invocation != null ? invocation.id : 0);
}

public NSMethodSignature methodSignatureForSelector(int sel) {
	int result = OS.objc_msgSend(this.id, OS.sel_methodSignatureForSelector_1, sel);
	return result != 0 ? new NSMethodSignature(result) : null;
}

//public static boolean respondsToSelector(int aSelector) {
//	return OS.objc_msgSend(OS.class_NSProxy, OS.sel_respondsToSelector_1, aSelector) != 0;
//}

}
