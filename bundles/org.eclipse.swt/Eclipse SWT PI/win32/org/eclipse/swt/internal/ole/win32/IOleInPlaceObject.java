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

public class IOleInPlaceObject extends IOleWindow
{
public IOleInPlaceObject(long /*int*/ address) {
	super(address);
}
public int InPlaceDeactivate() {
	return OS.VtblCall(5, address);
}
public int UIDeactivate() {
	return OS.VtblCall(6, address);
}
public int SetObjectRects(RECT lprcPosRect, RECT lprcClipRect) {
	return COM.VtblCall(7, address, lprcPosRect, lprcClipRect);
}
public int ReactivateAndUndo() {
	return OS.VtblCall(8, address);
}
}
