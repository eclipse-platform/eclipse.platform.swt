/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.browser;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.win32.OS;

class BrowserFactory {

WebBrowser createWebBrowser (int style) {
	if (OS.IsWinCE && (style & (SWT.MOZILLA | SWT.WEBKIT)) != 0) {
		throw new SWTError (SWT.ERROR_NO_HANDLES, "Unsupported Browser type"); //$NON-NLS-1$
	}
	if ((style & SWT.MOZILLA) != 0) {
		return new Mozilla ();
	}
	if ((style & SWT.WEBKIT) != 0) {
		return new WebKit ();
	}
	return new IE ();
}

}
