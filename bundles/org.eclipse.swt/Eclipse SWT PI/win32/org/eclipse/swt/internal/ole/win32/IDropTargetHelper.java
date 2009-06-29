/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
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

public class IDropTargetHelper extends IUnknown {
public IDropTargetHelper(int /*long*/ address) {
	super(address);
}
public int DragEnter(int /*long*/ hwndTarget, int /*long*/ pDataObject, POINT ppt, int dwEffect) {
	return COM.VtblCall(3, address, hwndTarget, pDataObject, ppt, dwEffect);
}
public int DragLeave() {
	return COM.VtblCall(4, address);
}
public int DragOver(POINT ppt, int dwEffect) {
	return COM.VtblCall(5, address, ppt, dwEffect);
}
public int Drop(int /*long*/ pDataObject, POINT ppt, int dwEffect) {
	return COM.VtblCall(6, address, pDataObject, ppt, dwEffect);
}
public int Show(boolean fShow) {
	return COM.VtblCall(7, address, fShow);
}
}
