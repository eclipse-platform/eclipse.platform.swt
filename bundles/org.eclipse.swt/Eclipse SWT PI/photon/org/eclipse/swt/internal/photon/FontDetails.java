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

 
public class FontDetails {
	/** @field cast=(FontDescription) */
	public byte[] desc = new byte[OS.MAX_DESC_LENGTH];
	/** @field cast=(FontName) */
	public byte[] stem = new byte[OS.MAX_FONT_TAG];
	public short losize;
	public short hisize;
	public short flags;
	public static final int sizeof = 128;
}
