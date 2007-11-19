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

public class NSPanel extends NSWindow {

public NSPanel() {
	super();
}

public NSPanel(int id) {
	super(id);
}

public boolean becomesKeyOnlyIfNeeded() {
	return OS.objc_msgSend(this.id, OS.sel_becomesKeyOnlyIfNeeded) != 0;
}

public boolean isFloatingPanel() {
	return OS.objc_msgSend(this.id, OS.sel_isFloatingPanel) != 0;
}

public void setBecomesKeyOnlyIfNeeded(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setBecomesKeyOnlyIfNeeded_1, flag);
}

public void setFloatingPanel(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setFloatingPanel_1, flag);
}

public void setWorksWhenModal(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setWorksWhenModal_1, flag);
}

public boolean worksWhenModal() {
	return OS.objc_msgSend(this.id, OS.sel_worksWhenModal) != 0;
}

}
