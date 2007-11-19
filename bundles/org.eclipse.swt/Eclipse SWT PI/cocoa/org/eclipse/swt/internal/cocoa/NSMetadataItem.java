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

public class NSMetadataItem extends NSObject {

public NSMetadataItem() {
	super();
}

public NSMetadataItem(int id) {
	super(id);
}

public NSArray attributes() {
	int result = OS.objc_msgSend(this.id, OS.sel_attributes);
	return result != 0 ? new NSArray(result) : null;
}

public id valueForAttribute(NSString key) {
	int result = OS.objc_msgSend(this.id, OS.sel_valueForAttribute_1, key != null ? key.id : 0);
	return result != 0 ? new id(result) : null;
}

public NSDictionary valuesForAttributes(NSArray keys) {
	int result = OS.objc_msgSend(this.id, OS.sel_valuesForAttributes_1, keys != null ? keys.id : 0);
	return result != 0 ? new NSDictionary(result) : null;
}

}
