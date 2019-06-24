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

public class IWebURLProtectionSpace extends IUnknown {

public IWebURLProtectionSpace(long address) {
	super(address);
}

public int host (long[] result) {
	return COM.VtblCall (4, getAddress (), result);
}

public int port (int[] result) {
	return COM.VtblCall (8, getAddress (), result);
}

public int realm (long[] result) {
	return COM.VtblCall (11, getAddress (), result);
}

}
