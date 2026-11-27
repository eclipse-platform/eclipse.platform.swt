/*******************************************************************************
 * Copyright (c) 2026 Eclipse Foundation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSTextInputContext extends NSObject {

public NSTextInputContext() {
	super();
}

public NSTextInputContext(long id) {
	super(id);
}

public NSTextInputContext(id id) {
	super(id);
}

public void discardMarkedText() {
	OS.objc_msgSend(this.id, OS.sel_discardMarkedText);
}

}
