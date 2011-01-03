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
package org.eclipse.swt.internal.webkit;


import org.eclipse.swt.internal.ole.win32.*;

public class IWebDataSource extends IUnknown {

public IWebDataSource (int /*long*/ address) {
	super (address);
}

public int representation (int /*long*/[] rep) {
	return COM.VtblCall (5, getAddress (), rep);
}

public int webFrame (int /*long*/[] frame) {
	return COM.VtblCall (6, getAddress (), frame);
}

public int request (int /*long*/[] request) {
	return COM.VtblCall (8, getAddress (), request);
}

public int pageTitle (int /*long*/[] title) {
	return COM.VtblCall (12, getAddress (), title);
}

}
