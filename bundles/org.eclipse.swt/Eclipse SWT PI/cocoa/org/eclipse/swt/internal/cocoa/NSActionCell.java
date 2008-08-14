/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSActionCell extends NSCell {

public NSActionCell() {
	super();
}

public NSActionCell(int /*long*/ id) {
	super(id);
}

public NSActionCell(id id) {
	super(id);
}

public void setAlignment(int mode) {
	OS.objc_msgSend(this.id, OS.sel_setAlignment_, mode);
}

public void setImage(NSImage image) {
	OS.objc_msgSend(this.id, OS.sel_setImage_, image != null ? image.id : 0);
}

}
