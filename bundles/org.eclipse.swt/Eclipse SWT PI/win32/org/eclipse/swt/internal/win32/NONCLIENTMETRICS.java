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

public abstract class NONCLIENTMETRICS {
	public int cbSize; 
	public int iBorderWidth; 
	public int iScrollWidth; 
	public int iScrollHeight; 
	public int iCaptionWidth; 
	public int iCaptionHeight; 
	public int iSmCaptionWidth; 
	public int iSmCaptionHeight;
	public int iMenuWidth; 
	public int iMenuHeight;
	public static final int sizeof = OS.IsUnicode ? OS.NONCLIENTMETRICSW_sizeof () : OS.NONCLIENTMETRICSA_sizeof ();
}

