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
import org.eclipse.swt.internal.win32.*;

public class IWebError extends IUnknown {

public IWebError (long /*int*/ address) {
	super (address);
}

public int code (int[] result) {
	return OS.VtblCall (4, getAddress (), result);
}

//public int domain (long /*int*/[] result) {
//	return COM.VtblCall (5, getAddress (), result);
//}

public int localizedDescription (long /*int*/[] result) {
	return OS.VtblCall (6, getAddress (), result);
}

//public int localizedFailureReason (long /*int*/[] result) {
//	return COM.VtblCall (7, getAddress (), result);
//}

//public int userInfo (long /*int*/[] result) {
//return COM.VtblCall (11, getAddress (), result);
//}

public int failingURL (long /*int*/[] result) {
	return OS.VtblCall (12, getAddress (), result);
}

}
