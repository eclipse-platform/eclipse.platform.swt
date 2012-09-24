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

public IWebFrame (long /*int*/ address) {
	super (address);
}

public int loadRequest (long /*int*/ request) {
	return COM.VtblCall (8, getAddress (), request);
}

public int loadHTMLString (long /*int*/ string, long /*int*/ baseURL) {
	return COM.VtblCall (10, getAddress (), string, baseURL);
}

public int dataSource (long /*int*/[] source) {
	return COM.VtblCall (13, getAddress (), source);
}

public long /*int*/ globalContext () {
	return COM.VtblCall (23, getAddress ());
}

}
