package org.eclipse.swt.internal.ole.win32;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
import org.eclipse.swt.internal.win32.*;

public class IOleInPlaceObject extends IOleWindow
{	
public IOleInPlaceObject(int address) {
	super(address);
}
public int InPlaceDeactivate() {
	return COM.VtblCall(5, address);
}
public int UIDeactivate() {
	return COM.VtblCall(6, address);
}
public int SetObjectRects(RECT lprcPosRect, RECT lprcClipRect) {
	return COM.VtblCall(7, address, lprcPosRect, lprcClipRect);
}
public int ReactivateAndUndo() {
	return COM.VtblCall(8, address);
}
}
