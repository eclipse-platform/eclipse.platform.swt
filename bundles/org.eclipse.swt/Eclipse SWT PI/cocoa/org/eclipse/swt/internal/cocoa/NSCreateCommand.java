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

public class NSCreateCommand extends NSScriptCommand {

public NSCreateCommand() {
	super();
}

public NSCreateCommand(int id) {
	super(id);
}

public NSScriptClassDescription createClassDescription() {
	int result = OS.objc_msgSend(this.id, OS.sel_createClassDescription);
	return result != 0 ? new NSScriptClassDescription(result) : null;
}

public NSDictionary resolvedKeyDictionary() {
	int result = OS.objc_msgSend(this.id, OS.sel_resolvedKeyDictionary);
	return result != 0 ? new NSDictionary(result) : null;
}

}
