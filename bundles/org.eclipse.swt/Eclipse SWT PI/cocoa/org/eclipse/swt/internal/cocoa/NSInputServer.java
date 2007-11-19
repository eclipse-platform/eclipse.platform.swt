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

public class NSInputServer extends NSObject {

public NSInputServer() {
	super();
}

public NSInputServer(int id) {
	super(id);
}

public id initWithDelegate(id aDelegate, NSString name) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithDelegate_1name_1, aDelegate != null ? aDelegate.id : 0, name != null ? name.id : 0);
	return result != 0 ? new id(result) : null;
}

}
