package org.eclipse.swt.internal.ole.win32;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
import org.eclipse.swt.internal.win32.*;

public class IOleInPlaceActiveObject extends IOleWindow
{
public IOleInPlaceActiveObject(int address) {
	super(address);
}
public int TranslateAccelerator(MSG lpmsg) {
	  //lpmsg - Pointer to message that may need translating
	  return COM.VtblCall(5, address, lpmsg);
}
public int ResizeBorder(RECT prcBorder, int pUIWindow, boolean fFrameWindow) {
	return COM.VtblCall(8, address, prcBorder, pUIWindow, fFrameWindow);
}
}
