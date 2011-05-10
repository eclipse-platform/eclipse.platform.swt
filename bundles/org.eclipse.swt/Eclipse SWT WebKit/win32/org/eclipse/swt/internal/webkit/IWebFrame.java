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

public class IWebFrame extends IUnknown {

public IWebFrame (int /*long*/ address) {
	super (address);
}

public int loadRequest (int /*long*/ request) {
	return COM.VtblCall (8, getAddress (), request);
}

public int loadHTMLString (int /*long*/ string, int /*long*/ baseURL) {
	return COM.VtblCall (10, getAddress (), string, baseURL);
}

public int dataSource (int /*long*/[] source) {
	return COM.VtblCall (13, getAddress (), source);
}

public int /*long*/ globalContext () {
	return COM.VtblCall (23, getAddress ());
}

}
