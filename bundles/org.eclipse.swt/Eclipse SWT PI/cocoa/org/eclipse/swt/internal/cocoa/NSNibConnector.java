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

public class NSNibConnector extends NSObject {

public NSNibConnector() {
	super();
}

public NSNibConnector(int id) {
	super(id);
}

public id destination() {
	int result = OS.objc_msgSend(this.id, OS.sel_destination);
	return result != 0 ? new id(result) : null;
}

public void establishConnection() {
	OS.objc_msgSend(this.id, OS.sel_establishConnection);
}

public NSString label() {
	int result = OS.objc_msgSend(this.id, OS.sel_label);
	return result != 0 ? new NSString(result) : null;
}

public void replaceObject(id oldObject, id newObject) {
	OS.objc_msgSend(this.id, OS.sel_replaceObject_1withObject_1, oldObject != null ? oldObject.id : 0, newObject != null ? newObject.id : 0);
}

public void setDestination(id destination) {
	OS.objc_msgSend(this.id, OS.sel_setDestination_1, destination != null ? destination.id : 0);
}

public void setLabel(NSString label) {
	OS.objc_msgSend(this.id, OS.sel_setLabel_1, label != null ? label.id : 0);
}

public void setSource(id source) {
	OS.objc_msgSend(this.id, OS.sel_setSource_1, source != null ? source.id : 0);
}

public id source() {
	int result = OS.objc_msgSend(this.id, OS.sel_source);
	return result != 0 ? new id(result) : null;
}

}
