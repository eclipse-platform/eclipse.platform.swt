/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.ole.win32;

 
import org.eclipse.swt.internal.win32.RECT;
 
public class IOleDocumentView extends IUnknown
{
public IOleDocumentView(int /*long*/ address) {
	super(address);
}
public int SetInPlaceSite(int /*long*/ pIPSite) {
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
