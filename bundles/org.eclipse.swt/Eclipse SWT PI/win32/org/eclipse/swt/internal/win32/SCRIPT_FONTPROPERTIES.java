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

public class SCRIPT_FONTPROPERTIES {
	public int cBytes;
	public short wgBlank;
	public short wgDefault;
	public short wgInvalid;
	public short wgKashida; 
	public int iKashidaWidth;
	public static final int sizeof = OS.SCRIPT_FONTPROPERTIES_sizeof ();
}
