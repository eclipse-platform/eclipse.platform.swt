/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
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

public class NMTREEVIEW {
	public NMHDR hdr = new NMHDR ();
	public int action;
	public TVITEM itemOld = new TVITEM ();
	public TVITEM itemNew = new TVITEM ();
	public POINT ptDrag = new POINT ();
	public static final int sizeof = OS.NMTREEVIEW_sizeof ();
}
