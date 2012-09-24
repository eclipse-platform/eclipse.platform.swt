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

public class IWebDataSource extends IUnknown {

public IWebDataSource (long /*int*/ address) {
	super (address);
}

public int representation (long /*int*/[] rep) {
	return COM.VtblCall (5, getAddress (), rep);
}

public int webFrame (long /*int*/[] frame) {
	return COM.VtblCall (6, getAddress (), frame);
}

public int request (long /*int*/[] request) {
	return COM.VtblCall (8, getAddress (), request);
}

public int pageTitle (long /*int*/[] title) {
	return COM.VtblCall (12, getAddress (), title);
}

}
