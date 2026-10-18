/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
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

public class TF_DISPLAYATTRIBUTE {
	public TF_DA_COLOR crText = new TF_DA_COLOR();
	public TF_DA_COLOR crBk = new TF_DA_COLOR();
	public int lsStyle;
	public boolean fBoldLine;
	public TF_DA_COLOR crLine = new TF_DA_COLOR();
	public int bAttr;
	public static final int sizeof = OS.TF_DISPLAYATTRIBUTE_sizeof ();
}
