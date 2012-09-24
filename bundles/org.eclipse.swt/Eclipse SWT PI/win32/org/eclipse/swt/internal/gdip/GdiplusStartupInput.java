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

/** @jniclass flags=cpp */
public class GdiplusStartupInput {
	public int GdiplusVersion;
	/** @field cast=(DebugEventProc) */
	public long /*int*/ DebugEventCallback;
	/** @field cast=(BOOL) */
	public boolean SuppressBackgroundThread;
	/** @field cast=(BOOL) */
	public boolean SuppressExternalCodecs;
	public static final int sizeof = Gdip.GdiplusStartupInput_sizeof ();
}
