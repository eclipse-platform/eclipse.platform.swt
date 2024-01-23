/*******************************************************************************
 * Copyright (c) 2010, 2012 IBM Corporation and others.
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
package org.eclipse.swt.browser;

import org.eclipse.swt.*;

class BrowserFactory {

WebBrowser createWebBrowser (int style) {
	// This function can't throw, otherwise the Browser will be left in inconsistent state.
	if ((style & SWT.EDGE) != 0) {
		try {
			return new Edge();
		} catch (SWTError e) {
			System.err.println(e);
		}
	}
	return new IE ();
}
}
