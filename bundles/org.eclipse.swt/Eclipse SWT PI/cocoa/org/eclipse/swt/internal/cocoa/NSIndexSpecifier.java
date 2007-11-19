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

public class NSIndexSpecifier extends NSScriptObjectSpecifier {

public NSIndexSpecifier() {
	super();
}

public NSIndexSpecifier(int id) {
	super(id);
}

public int index() {
	return OS.objc_msgSend(this.id, OS.sel_index);
}

public id initWithContainerClassDescription(NSScriptClassDescription classDesc, NSScriptObjectSpecifier container, NSString property, int index) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithContainerClassDescription_1containerSpecifier_1key_1index_1, classDesc != null ? classDesc.id : 0, container != null ? container.id : 0, property != null ? property.id : 0, index);
	return result != 0 ? new id(result) : null;
}

public void setIndex(int index) {
	OS.objc_msgSend(this.id, OS.sel_setIndex_1, index);
}

}
