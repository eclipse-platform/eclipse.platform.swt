/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.win32;

public class NMLINK extends NMHDR {
//	LITEM item;
	public int mask;
	public int iLink;
	public int state;
	public int stateMask;
	public char[] szID = new char[OS.MAX_LINKID_TEXT];
	public char[] szUrl = new char[OS.L_MAX_URL_LENGTH];
	public static final int sizeof = 4292;
}
