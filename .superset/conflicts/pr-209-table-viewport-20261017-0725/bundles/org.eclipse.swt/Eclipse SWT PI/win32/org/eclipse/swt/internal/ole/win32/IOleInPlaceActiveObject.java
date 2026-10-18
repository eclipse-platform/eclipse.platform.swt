/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
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

public class IOleInPlaceActiveObject extends IOleWindow
{
public IOleInPlaceActiveObject(long address) {
	super(address);
}
public int TranslateAccelerator(MSG lpmsg) {
	//lpmsg - Pointer to message that may need translating
	return COM.VtblCall(5, address, lpmsg);
}
public void OnFrameWindowActivate(boolean fActivate) {
	COM.VtblCall(6, address, fActivate ? 1 : 0);
}
public int ResizeBorder(RECT prcBorder, long pUIWindow, boolean fFrameWindow) {
	return COM.VtblCall(8, address, prcBorder, pUIWindow, fFrameWindow ? 1 : 0);
}
}
