/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.ole.win32;

public class IStream extends IUnknown {
public IStream(int address) {
	super(address);
}
public int Clone(
	int[] ppstm  //Pointer to location for pointer to the new stream object
){
	return COM.VtblCall(13, address, ppstm);
}
public int Commit( int grfCommitFlags  //Specifies how changes are committed
){
	return COM.VtblCall(8, address, grfCommitFlags);
}
public int Read(int pv, int cb, int[] pcbWritten) {
	return COM.VtblCall(3, address, pv, cb, pcbWritten);
}
public int Revert(){
	return COM.VtblCall(9, address);
}
public int Write(int pv, int cb, int[] pcbWritten) {
	return COM.VtblCall(4, address, pv, cb, pcbWritten);
}
}
