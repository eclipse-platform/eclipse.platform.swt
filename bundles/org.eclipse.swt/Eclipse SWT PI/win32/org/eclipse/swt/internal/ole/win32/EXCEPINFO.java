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

public final class EXCEPINFO {
	public short wCode;
	public short wReserved;
	/** @field cast=(BSTR) */
	public long bstrSource;
	/** @field cast=(BSTR) */
	public long bstrDescription;
	/** @field cast=(BSTR) */
	public long bstrHelpFile;
	public int dwHelpContext;
	/** @field cast=(void FAR *) */
	public long pvReserved;
	/** @field cast=(HRESULT (STDAPICALLTYPE FAR* )(struct tagEXCEPINFO FAR*)) */
	public long pfnDeferredFillIn;
	public int scode;
	public static final int sizeof = COM.EXCEPINFO_sizeof ();
}
