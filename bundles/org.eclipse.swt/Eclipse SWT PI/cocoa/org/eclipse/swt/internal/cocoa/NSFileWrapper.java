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

public class NSFileWrapper extends NSObject {

public NSFileWrapper() {
	super();
}

public NSFileWrapper(int /*long*/ id) {
	super(id);
}

public NSFileWrapper(id id) {
	super(id);
}

public void setIcon(NSImage icon) {
	OS.objc_msgSend(this.id, OS.sel_setIcon_, icon != null ? icon.id : 0);
}

}
