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

public class GCP_RESULTS {
	public int lStructSize;
	public int /*long*/ lpOutString;
	public int /*long*/ lpOrder;
	public int /*long*/ lpDx;
	public int /*long*/ lpCaretPos;
	public int /*long*/ lpClass;
	public int /*long*/ lpGlyphs;
	public int nGlyphs;
	public int nMaxFit;
	public static final int sizeof = OS.GCP_RESULTS_sizeof ();
}

