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

public class IWebURLRequest extends IUnknown {

public IWebURLRequest (long address) {
	super (address);
}

//public int HTTPBody (long[] result) {
//	return COM.VtblCall (6, getAddress (), result);
//}

public int HTTPMethod (long[] result) {
	return COM.VtblCall (8, getAddress (), result);
}

public int URL (long[] result) {
	return COM.VtblCall (13, getAddress (), result);
}

public int mutableCopy (long[] result) {
	return COM.VtblCall (16, getAddress (), result);
}

}
