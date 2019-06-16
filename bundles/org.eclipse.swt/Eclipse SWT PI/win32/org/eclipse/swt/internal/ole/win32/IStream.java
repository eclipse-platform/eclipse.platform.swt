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

public class IStream extends IUnknown {
public IStream(long address) {
	super(address);
}
public int Commit( int grfCommitFlags  //Specifies how changes are committed
){
	return COM.VtblCall(8, address, grfCommitFlags);
}
public int Read(long pv, int cb, int[] pcbWritten) {
	return COM.VtblCall(3, address, pv, cb, pcbWritten);
}
public int Write(long pv, int cb, int[] pcbWritten) {
	return COM.VtblCall(4, address, pv, cb, pcbWritten);
}
}
