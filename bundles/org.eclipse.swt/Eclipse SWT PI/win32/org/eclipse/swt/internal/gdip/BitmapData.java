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
package org.eclipse.swt.internal.gdip;

public class BitmapData {
	public int Width;
	public int Height;
	public int Stride;
	/** @field cast=(PixelFormat) */
	public int PixelFormat;
	/** @field cast=(void*) */
	public long /*int*/ Scan0;
	/** @field cast=(UINT_PTR) */
	public long /*int*/ Reserved;
}
