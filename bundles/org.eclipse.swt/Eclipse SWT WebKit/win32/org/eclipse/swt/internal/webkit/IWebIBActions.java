/*******************************************************************************
 * Copyright (c) 2010, 2017 IBM Corporation and others.
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
package org.eclipse.swt.internal.webkit;


import org.eclipse.swt.internal.ole.win32.*;

public class IWebIBActions extends IUnknown {

public IWebIBActions (long address) {
	super (address);
}

public int stopLoading (long sender) {
	return COM.VtblCall (4, getAddress (), sender);
}

public int reload (long sender) {
	return COM.VtblCall (5, getAddress (), sender);
}

public int canGoBack (long sender, int[] result) {
	return COM.VtblCall (6, getAddress (), sender, result);
}

public int canGoForward (long sender, int[] result) {
	return COM.VtblCall (8, getAddress (), sender, result);
}

}
