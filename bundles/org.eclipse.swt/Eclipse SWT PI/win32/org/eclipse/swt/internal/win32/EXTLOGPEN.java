/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.win32;

public class EXTLOGPEN {
	public int elpPenStyle;
	public int elpWidth;
	public int elpBrushStyle;
	public int elpColor;
	public int elpHatch;
	public int elpNumEntries;
	public int[] elpStyleEntry = new int[1];
	public static final int sizeof = 28;
}
