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

public class IWebError extends IUnknown {

public IWebError (int /*long*/ address) {
	super (address);
}

public int code (int[] result) {
	return COM.VtblCall (4, getAddress (), result);
}

//public int domain (int /*long*/[] result) {
//	return COM.VtblCall (5, getAddress (), result);
//}

public int localizedDescription (int /*long*/[] result) {
	return COM.VtblCall (6, getAddress (), result);
}

//public int localizedFailureReason (int /*long*/[] result) {
//	return COM.VtblCall (7, getAddress (), result);
//}

//public int userInfo (int /*long*/[] result) {
//return COM.VtblCall (11, getAddress (), result);
//}

public int failingURL (int /*long*/[] result) {
	return COM.VtblCall (12, getAddress (), result);
}

}
