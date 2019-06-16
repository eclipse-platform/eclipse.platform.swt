/*******************************************************************************
 * Copyright (c) 2008, 2017 IBM Corporation and others.
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

public class IDropTargetHelper extends IUnknown {
public IDropTargetHelper(long address) {
	super(address);
}
public int DragEnter(long hwndTarget, long pDataObject, POINT ppt, int dwEffect) {
	return COM.VtblCall(3, address, hwndTarget, pDataObject, ppt, dwEffect);
}
public int DragLeave() {
	return OS.VtblCall(4, address);
}
public int DragOver(POINT ppt, int dwEffect) {
	return COM.VtblCall(5, address, ppt, dwEffect);
}
public int Drop(long pDataObject, POINT ppt, int dwEffect) {
	return COM.VtblCall(6, address, pDataObject, ppt, dwEffect);
}
public int Show(boolean fShow) {
	return COM.VtblCall(7, address, fShow);
}
}
