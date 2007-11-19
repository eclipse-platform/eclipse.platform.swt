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

public class NSDistantObject extends NSProxy {

public NSDistantObject() {
	super();
}

public NSDistantObject(int id) {
	super(id);
}

public NSConnection connectionForProxy() {
	int result = OS.objc_msgSend(this.id, OS.sel_connectionForProxy);
	return result != 0 ? new NSConnection(result) : null;
}

public id initWithLocal(id target, NSConnection connection) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithLocal_1connection_1, target != null ? target.id : 0, connection != null ? connection.id : 0);
	return result != 0 ? new id(result) : null;
}

public id initWithTarget(id target, NSConnection connection) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithTarget_1connection_1, target != null ? target.id : 0, connection != null ? connection.id : 0);
	return result != 0 ? new id(result) : null;
}

public static NSDistantObject proxyWithLocal(id target, NSConnection connection) {
	int result = OS.objc_msgSend(OS.class_NSDistantObject, OS.sel_proxyWithLocal_1connection_1, target != null ? target.id : 0, connection != null ? connection.id : 0);
	return result != 0 ? new NSDistantObject(result) : null;
}

public static NSDistantObject proxyWithTarget(id target, NSConnection connection) {
	int result = OS.objc_msgSend(OS.class_NSDistantObject, OS.sel_proxyWithTarget_1connection_1, target != null ? target.id : 0, connection != null ? connection.id : 0);
	return result != 0 ? new NSDistantObject(result) : null;
}

//PUBLIC VOID SETPROTOCOLFORPROXY(PROTOCOL PROTO) {
//	OS.OBJC_MSGSEND(THIS.ID, OS.SEL_SETPROTOCOLFORPROXY_1, PROTO != NULL ? PROTO.ID : 0);
//}

}
