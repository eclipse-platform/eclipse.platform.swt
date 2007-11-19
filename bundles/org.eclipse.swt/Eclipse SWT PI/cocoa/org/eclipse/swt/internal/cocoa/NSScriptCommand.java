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

public class NSScriptCommand extends NSObject {

public NSScriptCommand() {
	super();
}

public NSScriptCommand(int id) {
	super(id);
}

public NSAppleEventDescriptor appleEvent() {
	int result = OS.objc_msgSend(this.id, OS.sel_appleEvent);
	return result != 0 ? new NSAppleEventDescriptor(result) : null;
}

public NSDictionary arguments() {
	int result = OS.objc_msgSend(this.id, OS.sel_arguments);
	return result != 0 ? new NSDictionary(result) : null;
}

public NSScriptCommandDescription commandDescription() {
	int result = OS.objc_msgSend(this.id, OS.sel_commandDescription);
	return result != 0 ? new NSScriptCommandDescription(result) : null;
}

public static NSScriptCommand currentCommand() {
	int result = OS.objc_msgSend(OS.class_NSScriptCommand, OS.sel_currentCommand);
	return result != 0 ? new NSScriptCommand(result) : null;
}

public id directParameter() {
	int result = OS.objc_msgSend(this.id, OS.sel_directParameter);
	return result != 0 ? new id(result) : null;
}

public NSDictionary evaluatedArguments() {
	int result = OS.objc_msgSend(this.id, OS.sel_evaluatedArguments);
	return result != 0 ? new NSDictionary(result) : null;
}

public id evaluatedReceivers() {
	int result = OS.objc_msgSend(this.id, OS.sel_evaluatedReceivers);
	return result != 0 ? new id(result) : null;
}

public id executeCommand() {
	int result = OS.objc_msgSend(this.id, OS.sel_executeCommand);
	return result != 0 ? new id(result) : null;
}

public id initWithCommandDescription(NSScriptCommandDescription commandDef) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithCommandDescription_1, commandDef != null ? commandDef.id : 0);
	return result != 0 ? new id(result) : null;
}

public boolean isWellFormed() {
	return OS.objc_msgSend(this.id, OS.sel_isWellFormed) != 0;
}

public id performDefaultImplementation() {
	int result = OS.objc_msgSend(this.id, OS.sel_performDefaultImplementation);
	return result != 0 ? new id(result) : null;
}

public NSScriptObjectSpecifier receiversSpecifier() {
	int result = OS.objc_msgSend(this.id, OS.sel_receiversSpecifier);
	return result != 0 ? new NSScriptObjectSpecifier(result) : null;
}

public void resumeExecutionWithResult(id result) {
	OS.objc_msgSend(this.id, OS.sel_resumeExecutionWithResult_1, result != null ? result.id : 0);
}

public NSAppleEventDescriptor scriptErrorExpectedTypeDescriptor() {
	int result = OS.objc_msgSend(this.id, OS.sel_scriptErrorExpectedTypeDescriptor);
	return result != 0 ? new NSAppleEventDescriptor(result) : null;
}

public int scriptErrorNumber() {
	return OS.objc_msgSend(this.id, OS.sel_scriptErrorNumber);
}

public NSAppleEventDescriptor scriptErrorOffendingObjectDescriptor() {
	int result = OS.objc_msgSend(this.id, OS.sel_scriptErrorOffendingObjectDescriptor);
	return result != 0 ? new NSAppleEventDescriptor(result) : null;
}

public NSString scriptErrorString() {
	int result = OS.objc_msgSend(this.id, OS.sel_scriptErrorString);
	return result != 0 ? new NSString(result) : null;
}

public void setArguments(NSDictionary args) {
	OS.objc_msgSend(this.id, OS.sel_setArguments_1, args != null ? args.id : 0);
}

public void setDirectParameter(id directParameter) {
	OS.objc_msgSend(this.id, OS.sel_setDirectParameter_1, directParameter != null ? directParameter.id : 0);
}

public void setReceiversSpecifier(NSScriptObjectSpecifier receiversRef) {
	OS.objc_msgSend(this.id, OS.sel_setReceiversSpecifier_1, receiversRef != null ? receiversRef.id : 0);
}

public void setScriptErrorExpectedTypeDescriptor(NSAppleEventDescriptor errorExpectedTypeDescriptor) {
	OS.objc_msgSend(this.id, OS.sel_setScriptErrorExpectedTypeDescriptor_1, errorExpectedTypeDescriptor != null ? errorExpectedTypeDescriptor.id : 0);
}

public void setScriptErrorNumber(int errorNumber) {
	OS.objc_msgSend(this.id, OS.sel_setScriptErrorNumber_1, errorNumber);
}

public void setScriptErrorOffendingObjectDescriptor(NSAppleEventDescriptor errorOffendingObjectDescriptor) {
	OS.objc_msgSend(this.id, OS.sel_setScriptErrorOffendingObjectDescriptor_1, errorOffendingObjectDescriptor != null ? errorOffendingObjectDescriptor.id : 0);
}

public void setScriptErrorString(NSString errorString) {
	OS.objc_msgSend(this.id, OS.sel_setScriptErrorString_1, errorString != null ? errorString.id : 0);
}

public void suspendExecution() {
	OS.objc_msgSend(this.id, OS.sel_suspendExecution);
}

}
