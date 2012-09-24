/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
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

public class IOleInPlaceActiveObject extends IOleWindow
{
public IOleInPlaceActiveObject(long /*int*/ address) {
	super(address);
}
public int TranslateAccelerator(MSG lpmsg) {
	  //lpmsg - Pointer to message that may need translating
	  return COM.VtblCall(5, address, lpmsg);
}
public void OnFrameWindowActivate(boolean fActivate) {
	COM.VtblCall(6, getAddress(), fActivate);
}
public void OnDocWindowActivate(boolean fActivate) {
	COM.VtblCall(7, getAddress(), fActivate);
}
public int ResizeBorder(RECT prcBorder, long /*int*/ pUIWindow, boolean fFrameWindow) {
	return COM.VtblCall(8, address, prcBorder, pUIWindow, fFrameWindow);
}
}
