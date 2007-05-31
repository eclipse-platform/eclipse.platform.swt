/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.win32;

public class NMREBARCHEVRON extends NMHDR {
	public int uBand;
	public int wID;
	public int /*long*/ lParam;
//	RECT rc;
	public int left, top, right, bottom;
	public int /*long*/ lParamNM;
	public static int sizeof = OS.NMREBARCHEVRON_sizeof ();
}
