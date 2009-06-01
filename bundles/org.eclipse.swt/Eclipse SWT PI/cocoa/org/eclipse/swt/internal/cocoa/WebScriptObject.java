/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class WebScriptObject extends NSObject {

public WebScriptObject() {
	super();
}

public WebScriptObject(int /*long*/ id) {
	super(id);
}

public WebScriptObject(id id) {
	super(id);
}

public id webScriptValueAtIndex(int index) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_webScriptValueAtIndex_, index);
	return result != 0 ? new id(result) : null;
}

}
