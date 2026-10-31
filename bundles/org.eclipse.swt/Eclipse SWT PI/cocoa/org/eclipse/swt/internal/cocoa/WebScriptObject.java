/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class WebScriptObject extends NSObject {

public WebScriptObject() {
	super();
}

public WebScriptObject(long id) {
	super(id);
}

public WebScriptObject(id id) {
	super(id);
}

public id callWebScriptMethod(NSString name, NSArray arguments) {
	long result = OS.objc_msgSend(this.id, OS.sel_callWebScriptMethod_withArguments_, name != null ? name.id : 0, arguments != null ? arguments.id : 0);
	return result != 0 ? new id(result) : null;
}

public id evaluateWebScript(NSString script) {
	long result = OS.objc_msgSend(this.id, OS.sel_evaluateWebScript_, script != null ? script.id : 0);
	return result != 0 ? new id(result) : null;
}

public id webScriptValueAtIndex(int index) {
	long result = OS.objc_msgSend(this.id, OS.sel_webScriptValueAtIndex_, index);
	return result != 0 ? new id(result) : null;
}

}
