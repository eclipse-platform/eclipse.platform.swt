/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
