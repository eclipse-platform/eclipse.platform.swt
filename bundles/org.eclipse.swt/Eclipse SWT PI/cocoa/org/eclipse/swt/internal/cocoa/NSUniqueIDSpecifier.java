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

public class NSUniqueIDSpecifier extends NSScriptObjectSpecifier {

public NSUniqueIDSpecifier() {
	super();
}

public NSUniqueIDSpecifier(int id) {
	super(id);
}

public id initWithContainerClassDescription(NSScriptClassDescription classDesc, NSScriptObjectSpecifier container, NSString property, id uniqueID) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithContainerClassDescription_1containerSpecifier_1key_1uniqueID_1, classDesc != null ? classDesc.id : 0, container != null ? container.id : 0, property != null ? property.id : 0, uniqueID != null ? uniqueID.id : 0);
	return result != 0 ? new id(result) : null;
}

public void setUniqueID(id uniqueID) {
	OS.objc_msgSend(this.id, OS.sel_setUniqueID_1, uniqueID != null ? uniqueID.id : 0);
}

public id uniqueID() {
	int result = OS.objc_msgSend(this.id, OS.sel_uniqueID);
	return result != 0 ? new id(result) : null;
}

}
