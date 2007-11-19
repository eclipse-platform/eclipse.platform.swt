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

public class NSRelativeSpecifier extends NSScriptObjectSpecifier {

public NSRelativeSpecifier() {
	super();
}

public NSRelativeSpecifier(int id) {
	super(id);
}

public NSScriptObjectSpecifier baseSpecifier() {
	int result = OS.objc_msgSend(this.id, OS.sel_baseSpecifier);
	return result != 0 ? new NSScriptObjectSpecifier(result) : null;
}

public id initWithContainerClassDescription(NSScriptClassDescription classDesc, NSScriptObjectSpecifier container, NSString property, int relPos, NSScriptObjectSpecifier baseSpecifier) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithContainerClassDescription_1containerSpecifier_1key_1relativePosition_1baseSpecifier_1, classDesc != null ? classDesc.id : 0, container != null ? container.id : 0, property != null ? property.id : 0, relPos, baseSpecifier != null ? baseSpecifier.id : 0);
	return result != 0 ? new id(result) : null;
}

public int relativePosition() {
	return OS.objc_msgSend(this.id, OS.sel_relativePosition);
}

public void setBaseSpecifier(NSScriptObjectSpecifier baseSpecifier) {
	OS.objc_msgSend(this.id, OS.sel_setBaseSpecifier_1, baseSpecifier != null ? baseSpecifier.id : 0);
}

public void setRelativePosition(int relPos) {
	OS.objc_msgSend(this.id, OS.sel_setRelativePosition_1, relPos);
}

}
