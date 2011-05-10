/*******************************************************************************
 * Copyright (c) 2010, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.webkit;


import org.eclipse.swt.internal.ole.win32.*;

public class IWebIBActions extends IUnknown {

public IWebIBActions (int /*long*/ address) {
	super (address);
}

public int stopLoading (int /*long*/ sender) {
	return COM.VtblCall (4, getAddress (), sender);
}

public int reload (int /*long*/ sender) {
	return COM.VtblCall (5, getAddress (), sender);
}

public int canGoBack (int /*long*/ sender, int[] result) {
	return COM.VtblCall (6, getAddress (), sender, result);
}

public int canGoForward (int /*long*/ sender, int[] result) {
	return COM.VtblCall (8, getAddress (), sender, result);
}

}
