/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.ole.win32;

import org.eclipse.swt.internal.win32.*;

public class IConnectionPoint extends IUnknown
{
public IConnectionPoint(long /*int*/ address) {
	super(address);
}
public int Advise(long /*int*/ pUnk, int[] pdwCookie) {
	return OS.VtblCall(5, address, pUnk, pdwCookie);
}
public int Unadvise(int dwCookie) {
	return OS.VtblCall(6, address, dwCookie);
}
}
