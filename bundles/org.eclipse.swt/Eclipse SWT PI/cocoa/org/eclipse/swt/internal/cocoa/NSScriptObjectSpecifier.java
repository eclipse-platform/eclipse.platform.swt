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

public class NSScriptObjectSpecifier extends NSObject {

public NSScriptObjectSpecifier() {
	super();
}

public NSScriptObjectSpecifier(int id) {
	super(id);
}

public NSScriptObjectSpecifier childSpecifier() {
	int result = OS.objc_msgSend(this.id, OS.sel_childSpecifier);
	return result == this.id ? this : (result != 0 ? new NSScriptObjectSpecifier(result) : null);
}

public NSScriptClassDescription containerClassDescription() {
	int result = OS.objc_msgSend(this.id, OS.sel_containerClassDescription);
	return result != 0 ? new NSScriptClassDescription(result) : null;
}

public boolean containerIsObjectBeingTested() {
	return OS.objc_msgSend(this.id, OS.sel_containerIsObjectBeingTested) != 0;
}

public boolean containerIsRangeContainerObject() {
	return OS.objc_msgSend(this.id, OS.sel_containerIsRangeContainerObject) != 0;
}

public NSScriptObjectSpecifier containerSpecifier() {
	int result = OS.objc_msgSend(this.id, OS.sel_containerSpecifier);
	return result == this.id ? this : (result != 0 ? new NSScriptObjectSpecifier(result) : null);
}

public NSAppleEventDescriptor descriptor() {
	int result = OS.objc_msgSend(this.id, OS.sel_descriptor);
	return result != 0 ? new NSAppleEventDescriptor(result) : null;
}

public int evaluationErrorNumber() {
	return OS.objc_msgSend(this.id, OS.sel_evaluationErrorNumber);
}

public NSScriptObjectSpecifier evaluationErrorSpecifier() {
	int result = OS.objc_msgSend(this.id, OS.sel_evaluationErrorSpecifier);
	return result == this.id ? this : (result != 0 ? new NSScriptObjectSpecifier(result) : null);
}

public int indicesOfObjectsByEvaluatingWithContainer(id container, int count) {
	return OS.objc_msgSend(this.id, OS.sel_indicesOfObjectsByEvaluatingWithContainer_1count_1, container != null ? container.id : 0, count);
}

public id initWithContainerClassDescription(NSScriptClassDescription classDesc, NSScriptObjectSpecifier container, NSString property) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithContainerClassDescription_1containerSpecifier_1key_1, classDesc != null ? classDesc.id : 0, container != null ? container.id : 0, property != null ? property.id : 0);
	return result != 0 ? new id(result) : null;
}

public id initWithContainerSpecifier(NSScriptObjectSpecifier container, NSString property) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithContainerSpecifier_1key_1, container != null ? container.id : 0, property != null ? property.id : 0);
	return result != 0 ? new id(result) : null;
}

public NSString key() {
	int result = OS.objc_msgSend(this.id, OS.sel_key);
	return result != 0 ? new NSString(result) : null;
}

public NSScriptClassDescription keyClassDescription() {
	int result = OS.objc_msgSend(this.id, OS.sel_keyClassDescription);
	return result != 0 ? new NSScriptClassDescription(result) : null;
}

public static NSScriptObjectSpecifier objectSpecifierWithDescriptor(NSAppleEventDescriptor descriptor) {
	int result = OS.objc_msgSend(OS.class_NSScriptObjectSpecifier, OS.sel_objectSpecifierWithDescriptor_1, descriptor != null ? descriptor.id : 0);
	return result != 0 ? new NSScriptObjectSpecifier(result) : null;
}

public id objectsByEvaluatingSpecifier() {
	int result = OS.objc_msgSend(this.id, OS.sel_objectsByEvaluatingSpecifier);
	return result != 0 ? new id(result) : null;
}

public id objectsByEvaluatingWithContainers(id containers) {
	int result = OS.objc_msgSend(this.id, OS.sel_objectsByEvaluatingWithContainers_1, containers != null ? containers.id : 0);
	return result != 0 ? new id(result) : null;
}

public void setChildSpecifier(NSScriptObjectSpecifier child) {
	OS.objc_msgSend(this.id, OS.sel_setChildSpecifier_1, child != null ? child.id : 0);
}

public void setContainerClassDescription(NSScriptClassDescription classDesc) {
	OS.objc_msgSend(this.id, OS.sel_setContainerClassDescription_1, classDesc != null ? classDesc.id : 0);
}

public void setContainerIsObjectBeingTested(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setContainerIsObjectBeingTested_1, flag);
}

public void setContainerIsRangeContainerObject(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setContainerIsRangeContainerObject_1, flag);
}

public void setContainerSpecifier(NSScriptObjectSpecifier subRef) {
	OS.objc_msgSend(this.id, OS.sel_setContainerSpecifier_1, subRef != null ? subRef.id : 0);
}

public void setEvaluationErrorNumber(int error) {
	OS.objc_msgSend(this.id, OS.sel_setEvaluationErrorNumber_1, error);
}

public void setKey(NSString key) {
	OS.objc_msgSend(this.id, OS.sel_setKey_1, key != null ? key.id : 0);
}

}
