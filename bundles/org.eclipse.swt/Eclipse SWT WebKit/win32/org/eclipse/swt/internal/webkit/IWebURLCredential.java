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

public class IWebURLCredential extends IUnknown {

public IWebURLCredential (long /*int*/ address) {
	super (address);
}

public int hasPassword (int[] result) {
	return COM.VtblCall (3, getAddress (), result);
}

public int initWithUser (long /*int*/ user, long /*int*/ password, long /*int*/ persistence) {
	return COM.VtblCall (4, getAddress (), user, password, persistence);
}

public int password (long /*int*/[] password) {
	return COM.VtblCall (5, getAddress (), password);
}

public int user (long /*int*/[] result) {
	return COM.VtblCall (7, getAddress (), result);
}

}
