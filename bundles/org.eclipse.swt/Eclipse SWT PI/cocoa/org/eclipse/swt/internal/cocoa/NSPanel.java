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

public class NSPanel extends NSWindow {

public NSPanel() {
	super();
}

public NSPanel(int /*long*/ id) {
	super(id);
}

public NSPanel(id id) {
	super(id);
}

public void setWorksWhenModal(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setWorksWhenModal_, flag);
}

public boolean worksWhenModal() {
	return OS.objc_msgSend_bool(this.id, OS.sel_worksWhenModal);
}

}
