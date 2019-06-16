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

public class IOleInPlaceObject extends IOleWindow
{
public IOleInPlaceObject(long address) {
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
}
