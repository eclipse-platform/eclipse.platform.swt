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

public class NSMetadataQueryAttributeValueTuple extends NSObject {

public NSMetadataQueryAttributeValueTuple() {
	super();
}

public NSMetadataQueryAttributeValueTuple(int id) {
	super(id);
}

public NSString attribute() {
	int result = OS.objc_msgSend(this.id, OS.sel_attribute);
	return result != 0 ? new NSString(result) : null;
}

public int count() {
	return OS.objc_msgSend(this.id, OS.sel_count);
}

public id value() {
	int result = OS.objc_msgSend(this.id, OS.sel_value);
	return result != 0 ? new id(result) : null;
}

}
