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

public class CIColor extends NSObject {

public CIColor() {
	super();
}

public CIColor(int id) {
	super(id);
}

public CIColor initWithColor(NSColor color) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithColor_1, color != null ? color.id : 0);
	return result != 0 ? this : null;
}

}
