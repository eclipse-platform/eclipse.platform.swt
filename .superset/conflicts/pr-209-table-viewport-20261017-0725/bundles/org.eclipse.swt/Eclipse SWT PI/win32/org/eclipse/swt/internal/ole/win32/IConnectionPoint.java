/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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
package org.eclipse.swt.internal.ole.win32;

public class IConnectionPoint extends IUnknown
{
public IConnectionPoint(long address) {
	super(address);
}
public int Advise(long pUnk, int[] pdwCookie) {
	return COM.VtblCall(5, address, pUnk, pdwCookie);
}
public int Unadvise(int dwCookie) {
	return COM.VtblCall(6, address, dwCookie);
}
}
