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

public class NSTabViewItem extends NSObject {

public NSTabViewItem() {
	super();
}

public NSTabViewItem(int /*long*/ id) {
	super(id);
}

public NSTabViewItem(id id) {
	super(id);
}

public id initWithIdentifier(id identifier) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_initWithIdentifier_, identifier != null ? identifier.id : 0);
	return result != 0 ? new id(result) : null;
}

public void setLabel(NSString label) {
	OS.objc_msgSend(this.id, OS.sel_setLabel_, label != null ? label.id : 0);
}

public void setView(NSView view) {
	OS.objc_msgSend(this.id, OS.sel_setView_, view != null ? view.id : 0);
}

}
