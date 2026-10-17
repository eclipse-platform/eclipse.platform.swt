/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
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

public class NMLISTVIEW extends NMHDR {
	public int iItem;
	public int iSubItem;
	public int uNewState;
	public int uOldState;
	public int uChanged;
//	POINT ptAction;
	/** @field accessor=ptAction.x */
	public int x;
	/** @field accessor=ptAction.y */
	public int y;
	public long lParam;
	public static int sizeof = OS.NMLISTVIEW_sizeof ();
}
