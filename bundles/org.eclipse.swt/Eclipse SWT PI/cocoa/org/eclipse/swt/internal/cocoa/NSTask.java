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

public class NSTask extends NSObject {

public NSTask() {
	super();
}

public NSTask(int id) {
	super(id);
}

public NSArray arguments() {
	int result = OS.objc_msgSend(this.id, OS.sel_arguments);
	return result != 0 ? new NSArray(result) : null;
}

public NSString currentDirectoryPath() {
	int result = OS.objc_msgSend(this.id, OS.sel_currentDirectoryPath);
	return result != 0 ? new NSString(result) : null;
}

public NSDictionary environment() {
	int result = OS.objc_msgSend(this.id, OS.sel_environment);
	return result != 0 ? new NSDictionary(result) : null;
}

public void interrupt() {
	OS.objc_msgSend(this.id, OS.sel_interrupt);
}

public boolean isRunning() {
	return OS.objc_msgSend(this.id, OS.sel_isRunning) != 0;
}

public void launch() {
	OS.objc_msgSend(this.id, OS.sel_launch);
}

public NSString launchPath() {
	int result = OS.objc_msgSend(this.id, OS.sel_launchPath);
	return result != 0 ? new NSString(result) : null;
}

public static NSTask launchedTaskWithLaunchPath(NSString path, NSArray arguments) {
	int result = OS.objc_msgSend(OS.class_NSTask, OS.sel_launchedTaskWithLaunchPath_1arguments_1, path != null ? path.id : 0, arguments != null ? arguments.id : 0);
	return result != 0 ? new NSTask(result) : null;
}

public int processIdentifier() {
	return OS.objc_msgSend(this.id, OS.sel_processIdentifier);
}

public boolean resume() {
	return OS.objc_msgSend(this.id, OS.sel_resume) != 0;
}

public void setArguments(NSArray arguments) {
	OS.objc_msgSend(this.id, OS.sel_setArguments_1, arguments != null ? arguments.id : 0);
}

public void setCurrentDirectoryPath(NSString path) {
	OS.objc_msgSend(this.id, OS.sel_setCurrentDirectoryPath_1, path != null ? path.id : 0);
}

public void setEnvironment(NSDictionary dict) {
	OS.objc_msgSend(this.id, OS.sel_setEnvironment_1, dict != null ? dict.id : 0);
}

public void setLaunchPath(NSString path) {
	OS.objc_msgSend(this.id, OS.sel_setLaunchPath_1, path != null ? path.id : 0);
}

public void setStandardError(id error) {
	OS.objc_msgSend(this.id, OS.sel_setStandardError_1, error != null ? error.id : 0);
}

public void setStandardInput(id input) {
	OS.objc_msgSend(this.id, OS.sel_setStandardInput_1, input != null ? input.id : 0);
}

public void setStandardOutput(id output) {
	OS.objc_msgSend(this.id, OS.sel_setStandardOutput_1, output != null ? output.id : 0);
}

public id standardError() {
	int result = OS.objc_msgSend(this.id, OS.sel_standardError);
	return result != 0 ? new id(result) : null;
}

public id standardInput() {
	int result = OS.objc_msgSend(this.id, OS.sel_standardInput);
	return result != 0 ? new id(result) : null;
}

public id standardOutput() {
	int result = OS.objc_msgSend(this.id, OS.sel_standardOutput);
	return result != 0 ? new id(result) : null;
}

public boolean suspend() {
	return OS.objc_msgSend(this.id, OS.sel_suspend) != 0;
}

public void terminate() {
	OS.objc_msgSend(this.id, OS.sel_terminate);
}

public int terminationStatus() {
	return OS.objc_msgSend(this.id, OS.sel_terminationStatus);
}

public void waitUntilExit() {
	OS.objc_msgSend(this.id, OS.sel_waitUntilExit);
}

}
