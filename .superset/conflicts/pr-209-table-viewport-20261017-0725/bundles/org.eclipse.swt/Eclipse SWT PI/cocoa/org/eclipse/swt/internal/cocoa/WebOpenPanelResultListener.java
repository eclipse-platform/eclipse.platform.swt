/*******************************************************************************
 * Copyright (c) 2007, 2012 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class WebOpenPanelResultListener extends NSObject {

public WebOpenPanelResultListener() {
	super();
}

public WebOpenPanelResultListener(long id) {
	super(id);
}

public WebOpenPanelResultListener(id id) {
	super(id);
}

public void cancel() {
	OS.objc_msgSend(id, OS.sel_cancel);
}

public void chooseFilename(NSString string) {
	OS.objc_msgSend(id, OS.sel_chooseFilename_, string != null ? string.id : 0);
}
}
