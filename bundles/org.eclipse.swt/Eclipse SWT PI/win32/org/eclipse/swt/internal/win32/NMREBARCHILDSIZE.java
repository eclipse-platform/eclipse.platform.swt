/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.win32;

public class NMREBARCHILDSIZE extends NMHDR {
	public int uBand;
	public int wID;
//  RECT rcChild;
	public int rcChild_left;
	public int rcChild_top;
	public int rcChild_right;
	public int rcChild_bottom;
//  RECT rcBand;
	public int rcBand_left;
	public int rcBand_top;
	public int rcBand_right;
	public int rcBand_bottom;
	public static final int sizeof = 52;
}
