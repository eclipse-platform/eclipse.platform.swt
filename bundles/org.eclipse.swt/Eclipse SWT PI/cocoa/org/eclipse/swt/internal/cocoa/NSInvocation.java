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

public class NSInvocation extends NSObject {

public NSInvocation() {
	super();
}

public NSInvocation(int id) {
	super(id);
}

public boolean argumentsRetained() {
	return OS.objc_msgSend(this.id, OS.sel_argumentsRetained) != 0;
}

public void getArgument(int argumentLocation, int idx) {
	OS.objc_msgSend(this.id, OS.sel_getArgument_1atIndex_1, argumentLocation, idx);
}

public void getReturnValue(int retLoc) {
	OS.objc_msgSend(this.id, OS.sel_getReturnValue_1, retLoc);
}

public static NSInvocation invocationWithMethodSignature(NSMethodSignature sig) {
	int result = OS.objc_msgSend(OS.class_NSInvocation, OS.sel_invocationWithMethodSignature_1, sig != null ? sig.id : 0);
	return result != 0 ? new NSInvocation(result) : null;
}

public void invoke() {
	OS.objc_msgSend(this.id, OS.sel_invoke);
}

public void invokeWithTarget(id target) {
	OS.objc_msgSend(this.id, OS.sel_invokeWithTarget_1, target != null ? target.id : 0);
}

public NSMethodSignature methodSignature() {
	int result = OS.objc_msgSend(this.id, OS.sel_methodSignature);
	return result != 0 ? new NSMethodSignature(result) : null;
}

public void retainArguments() {
	OS.objc_msgSend(this.id, OS.sel_retainArguments);
}

public int selector() {
	return OS.objc_msgSend(this.id, OS.sel_selector);
}

public void setArgument(int argumentLocation, int idx) {
	OS.objc_msgSend(this.id, OS.sel_setArgument_1atIndex_1, argumentLocation, idx);
}

public void setReturnValue(int retLoc) {
	OS.objc_msgSend(this.id, OS.sel_setReturnValue_1, retLoc);
}

public void setSelector(int selector) {
	OS.objc_msgSend(this.id, OS.sel_setSelector_1, selector);
}

public void setTarget(id target) {
	OS.objc_msgSend(this.id, OS.sel_setTarget_1, target != null ? target.id : 0);
}

public id target() {
	int result = OS.objc_msgSend(this.id, OS.sel_target);
	return result != 0 ? new id(result) : null;
}

}
