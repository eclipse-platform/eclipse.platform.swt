/**********************************************************************
 * Copyright (c) 2003, 2008 IBM Corp.
 * Portions Copyright (c) 1983-2002, Apple Computer, Inc.
 *
 * All rights reserved.  This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 **********************************************************************/
package org.eclipse.swt.internal.carbon;

 
public class NavCBRec {
	public short version;
	/** @field cast=(NavDialogRef) */
	public int context;
	/** @field cast=(WindowRef) */
	public int window;
	public Rect customRect = new Rect ();
	public Rect previewRect = new Rect ();
	public NavEventData eventData = new NavEventData ();
	/** @field cast=(NavUserAction) */
	public int userAction;
	public byte[] reserved = new byte[218];
	public static final int sizeof = 254;
}
