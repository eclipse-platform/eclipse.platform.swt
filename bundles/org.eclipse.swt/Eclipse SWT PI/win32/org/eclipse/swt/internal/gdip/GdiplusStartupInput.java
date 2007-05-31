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
package org.eclipse.swt.internal.gdip;

public class GdiplusStartupInput {
	public int GdiplusVersion;
	public int /*long*/ DebugEventCallback;
	public boolean SuppressBackgroundThread;
	public boolean SuppressExternalCodecs;
	public static final int sizeof = Gdip.GdiplusStartupInput_sizeof ();
}
