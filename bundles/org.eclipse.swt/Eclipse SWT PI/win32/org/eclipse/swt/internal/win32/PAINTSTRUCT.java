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
package org.eclipse.swt.internal.win32;

public class PAINTSTRUCT {
	public int  hdc; 
	public boolean fErase;
//	public RECT rcPaint;
	public int left, top, right, bottom;
	public boolean fRestore; 
	public boolean fIncUpdate; 
	public byte[] rgbReserved = new byte[32];
	public static final int sizeof = 64;
}
