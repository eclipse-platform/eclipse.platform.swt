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

public class IWebURLProtectionSpace extends IUnknown {

public IWebURLProtectionSpace(int /*long*/ address) {
	super(address);
}

public int host (int /*long*/[] result) {
	return COM.VtblCall (4, getAddress (), result);
}

public int port (int[] result) {
	return COM.VtblCall (8, getAddress (), result);
}

public int realm (int /*long*/[] result) {
	return COM.VtblCall (11, getAddress (), result);
}

}
