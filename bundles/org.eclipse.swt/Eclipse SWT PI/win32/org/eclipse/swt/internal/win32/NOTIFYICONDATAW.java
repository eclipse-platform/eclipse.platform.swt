/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.win32;

public class NOTIFYICONDATAW extends NOTIFYICONDATA {
	public char szTip[] = new char [128];		// TCHAR szTip
	public char szInfo[] = new char [512];		// TCHAR szInfo
	public char szInfoTitle[] = new char [128];	// TCHAR szInfoTitle
//	public static final int sizeof = 952; 	
}
