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

public class NSScriptExecutionContext extends NSObject {

public NSScriptExecutionContext() {
	super();
}

public NSScriptExecutionContext(int id) {
	super(id);
}

public id objectBeingTested() {
	int result = OS.objc_msgSend(this.id, OS.sel_objectBeingTested);
	return result != 0 ? new id(result) : null;
}

public id rangeContainerObject() {
	int result = OS.objc_msgSend(this.id, OS.sel_rangeContainerObject);
	return result != 0 ? new id(result) : null;
}

public void setObjectBeingTested(id obj) {
	OS.objc_msgSend(this.id, OS.sel_setObjectBeingTested_1, obj != null ? obj.id : 0);
}

public void setRangeContainerObject(id obj) {
	OS.objc_msgSend(this.id, OS.sel_setRangeContainerObject_1, obj != null ? obj.id : 0);
}

public void setTopLevelObject(id obj) {
	OS.objc_msgSend(this.id, OS.sel_setTopLevelObject_1, obj != null ? obj.id : 0);
}

public static NSScriptExecutionContext sharedScriptExecutionContext() {
	int result = OS.objc_msgSend(OS.class_NSScriptExecutionContext, OS.sel_sharedScriptExecutionContext);
	return result != 0 ? new NSScriptExecutionContext(result) : null;
}

public id topLevelObject() {
	int result = OS.objc_msgSend(this.id, OS.sel_topLevelObject);
	return result != 0 ? new id(result) : null;
}

}
