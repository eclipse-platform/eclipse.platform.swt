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


import org.eclipse.swt.internal.win32.*;

public class IOleDocumentView extends IUnknown
{
public IOleDocumentView(long address) {
	super(address);
}
public int SetInPlaceSite(long pIPSite) {
	return COM.VtblCall(3, address, pIPSite);
}
public int SetRect(RECT prcView) {
	return COM.VtblCall(6, address, prcView);
}
public int Show(int fShow) {
	return COM.VtblCall(9, address, fShow);
}
public int UIActivate(int fUIActivate) {
	return COM.VtblCall(10, address, fUIActivate);
}
}
