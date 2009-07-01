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

public class TEXTMETRICA extends TEXTMETRIC {
	public byte tmFirstChar;
	public byte tmLastChar;
	public byte tmDefaultChar; 
	public byte tmBreakChar;
	public static final int sizeof = OS.TEXTMETRICA_sizeof ();
}
