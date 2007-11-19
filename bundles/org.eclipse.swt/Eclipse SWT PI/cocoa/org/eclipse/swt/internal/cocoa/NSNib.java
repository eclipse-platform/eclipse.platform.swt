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

public class NSNib extends NSObject {

public NSNib() {
	super();
}

public NSNib(int id) {
	super(id);
}

public id initWithContentsOfURL(NSURL nibFileURL) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithContentsOfURL_1, nibFileURL != null ? nibFileURL.id : 0);
	return result != 0 ? new id(result) : null;
}

public id initWithNibNamed(NSString nibName, NSBundle bundle) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithNibNamed_1bundle_1, nibName != null ? nibName.id : 0, bundle != null ? bundle.id : 0);
	return result != 0 ? new id(result) : null;
}

public boolean instantiateNibWithExternalNameTable(NSDictionary externalNameTable) {
	return OS.objc_msgSend(this.id, OS.sel_instantiateNibWithExternalNameTable_1, externalNameTable != null ? externalNameTable.id : 0) != 0;
}

public boolean instantiateNibWithOwner(id owner, int topLevelObjects) {
	return OS.objc_msgSend(this.id, OS.sel_instantiateNibWithOwner_1topLevelObjects_1, owner != null ? owner.id : 0, topLevelObjects) != 0;
}

}
