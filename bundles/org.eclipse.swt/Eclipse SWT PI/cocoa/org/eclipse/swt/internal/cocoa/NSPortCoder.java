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

public class NSPortCoder extends NSCoder {

public NSPortCoder() {
	super();
}

public NSPortCoder(int id) {
	super(id);
}

public NSConnection connection() {
	int result = OS.objc_msgSend(this.id, OS.sel_connection);
	return result != 0 ? new NSConnection(result) : null;
}

public NSPort decodePortObject() {
	int result = OS.objc_msgSend(this.id, OS.sel_decodePortObject);
	return result != 0 ? new NSPort(result) : null;
}

public void dispatch() {
	OS.objc_msgSend(this.id, OS.sel_dispatch);
}

public void encodePortObject(NSPort aport) {
	OS.objc_msgSend(this.id, OS.sel_encodePortObject_1, aport != null ? aport.id : 0);
}

public NSPortCoder initWithReceivePort(NSPort rcvPort, NSPort sndPort, NSArray comps) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithReceivePort_1sendPort_1components_1, rcvPort != null ? rcvPort.id : 0, sndPort != null ? sndPort.id : 0, comps != null ? comps.id : 0);
	return result != 0 ? this : null;
}

public boolean isBycopy() {
	return OS.objc_msgSend(this.id, OS.sel_isBycopy) != 0;
}

public boolean isByref() {
	return OS.objc_msgSend(this.id, OS.sel_isByref) != 0;
}

public static id portCoderWithReceivePort(NSPort rcvPort, NSPort sndPort, NSArray comps) {
	int result = OS.objc_msgSend(OS.class_NSPortCoder, OS.sel_portCoderWithReceivePort_1sendPort_1components_1, rcvPort != null ? rcvPort.id : 0, sndPort != null ? sndPort.id : 0, comps != null ? comps.id : 0);
	return result != 0 ? new id(result) : null;
}

}
