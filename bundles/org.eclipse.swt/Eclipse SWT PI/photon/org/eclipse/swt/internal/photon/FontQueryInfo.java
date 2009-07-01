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
package org.eclipse.swt.internal.photon;

 
public class FontQueryInfo {
	/** @field cast=(FontName) */
	public byte[] font = new byte[OS.MAX_FONT_TAG];
	/** @field cast=(FontDescription) */
	public byte[] desc = new byte[OS.MAX_DESC_LENGTH];
	public short size;
	public short style;
	public short ascender;
	public short descender;
	public short width;
	public int lochar;
	public int hichar;
	public static final int sizeof = 140;
}
