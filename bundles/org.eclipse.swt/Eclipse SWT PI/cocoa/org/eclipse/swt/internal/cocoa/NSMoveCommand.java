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

public class NSMoveCommand extends NSScriptCommand {

public NSMoveCommand() {
	super();
}

public NSMoveCommand(int id) {
	super(id);
}

public NSScriptObjectSpecifier keySpecifier() {
	int result = OS.objc_msgSend(this.id, OS.sel_keySpecifier);
	return result != 0 ? new NSScriptObjectSpecifier(result) : null;
}

public void setReceiversSpecifier(NSScriptObjectSpecifier receiversRef) {
	OS.objc_msgSend(this.id, OS.sel_setReceiversSpecifier_1, receiversRef != null ? receiversRef.id : 0);
}

}
