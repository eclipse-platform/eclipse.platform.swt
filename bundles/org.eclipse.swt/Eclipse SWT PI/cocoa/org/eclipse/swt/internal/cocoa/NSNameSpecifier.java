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

public class NSNameSpecifier extends NSScriptObjectSpecifier {

public NSNameSpecifier() {
	super();
}

public NSNameSpecifier(int id) {
	super(id);
}

public id initWithContainerClassDescription(NSScriptClassDescription classDesc, NSScriptObjectSpecifier container, NSString property, NSString name) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithContainerClassDescription_1containerSpecifier_1key_1name_1, classDesc != null ? classDesc.id : 0, container != null ? container.id : 0, property != null ? property.id : 0, name != null ? name.id : 0);
	return result != 0 ? new id(result) : null;
}

public NSString name() {
	int result = OS.objc_msgSend(this.id, OS.sel_name);
	return result != 0 ? new NSString(result) : null;
}

public void setName(NSString name) {
	OS.objc_msgSend(this.id, OS.sel_setName_1, name != null ? name.id : 0);
}

}
