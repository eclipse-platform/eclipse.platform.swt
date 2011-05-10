/*******************************************************************************
 * Copyright (c) 2010, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.win32;

public class GESTUREINFO {
	 public int cbSize;
	 public int dwFlags;
	 public int dwID;
	 /** @field cast=(HWND) */
	 public int /*long*/ hwndTarget;
	 //	POINTS ptsLocation
	 /** @field accessor=ptsLocation.x */
	 public short x;
	 /** @field accessor=ptsLocation.y */
	 public short y;
	 public int dwInstanceID;
	 public int dwSequenceID;
	 public long ullArguments;
	 public int cbExtraArgs;
	 public static final int sizeof = OS.GESTUREINFO_sizeof ();
}
