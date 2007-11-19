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

public class WebOpenPanelResultListener extends NSObject {

public WebOpenPanelResultListener(int id) {
	super(id);
}

public void cancel() {
	OS.objc_msgSend(id, OS.sel_cancel);
}

public void chooseFilename(NSString string) {
	OS.objc_msgSend(id, OS.sel_chooseFilename_1, string != null ? string.id : 0);
}
}
