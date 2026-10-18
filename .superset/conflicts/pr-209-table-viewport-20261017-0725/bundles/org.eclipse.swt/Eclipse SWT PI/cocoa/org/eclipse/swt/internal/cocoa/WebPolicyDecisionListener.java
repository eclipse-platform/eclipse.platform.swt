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

public class WebPolicyDecisionListener extends NSObject {

public WebPolicyDecisionListener() {
	super();
}

public WebPolicyDecisionListener(long id) {
	super(id);
}

public WebPolicyDecisionListener(id id) {
	super(id);
}

public void download() {
	OS.objc_msgSend(id, OS.sel_download);
}

public void use() {
	OS.objc_msgSend(id, OS.sel_use);
}

public void ignore() {
	OS.objc_msgSend(id, OS.sel_ignore);
}
}
