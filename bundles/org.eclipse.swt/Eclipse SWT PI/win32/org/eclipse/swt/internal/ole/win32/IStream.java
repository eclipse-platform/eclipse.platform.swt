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

public class IStream extends IUnknown {
public IStream(long /*int*/ address) {
	super(address);
}
public int Clone(
	long /*int*/[] ppstm  //Pointer to location for pointer to the new stream object
){
	return OS.VtblCall(13, address, ppstm);
}
public int Commit( int grfCommitFlags  //Specifies how changes are committed
){
	return OS.VtblCall(8, address, grfCommitFlags);
}
public int Read(long /*int*/ pv, int cb, int[] pcbWritten) {
	return COM.VtblCall(3, address, pv, cb, pcbWritten);
}
public int Revert(){
	return OS.VtblCall(9, address);
}
public int Write(long /*int*/ pv, int cb, int[] pcbWritten) {
	return COM.VtblCall(4, address, pv, cb, pcbWritten);
}
}
