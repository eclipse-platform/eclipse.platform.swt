/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.ole.win32;

public final class EXCEPINFO {
	public short wCode;   
	public short wReserved;
	/** @field cast=(BSTR) */
	public int /*long*/ bstrSource; 
	/** @field cast=(BSTR) */
	public int /*long*/ bstrDescription; 
	/** @field cast=(BSTR) */
	public int /*long*/ bstrHelpFile;
	public int dwHelpContext; 
	/** @field cast=(void FAR *) */
	public int /*long*/ pvReserved;
	/** @field cast=(HRESULT (STDAPICALLTYPE FAR* )(struct tagEXCEPINFO FAR*)) */
	public int /*long*/ pfnDeferredFillIn;
	public int scode;
	public static final int sizeof = COM.EXCEPINFO_sizeof ();
}
