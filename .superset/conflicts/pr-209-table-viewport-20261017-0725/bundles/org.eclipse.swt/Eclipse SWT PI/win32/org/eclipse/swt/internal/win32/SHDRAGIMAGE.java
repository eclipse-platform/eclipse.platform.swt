/*******************************************************************************
 * Copyright (c) 2008, 2012 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.win32;

public final class SHDRAGIMAGE {
	public SIZE sizeDragImage = new SIZE ();
	public POINT ptOffset = new POINT ();
	/** @field cast=(HBITMAP) */
	public long hbmpDragImage;
	public int crColorKey;
	public static final int sizeof = OS.SHDRAGIMAGE_sizeof ();
}
