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

public class NMCUSTOMDRAW extends NMHDR {
	public int dwDrawStage;
	/** @field cast=(HDC) */
	public long hdc;
//	RECT rc;
	/** @field accessor=rc.left */
	public int left;
	/** @field accessor=rc.top */
	public int top;
	/** @field accessor=rc.right */
	public int right;
	/** @field accessor=rc.bottom */
	public int bottom;
	public long dwItemSpec;
	public int uItemState;
	public long lItemlParam;
	public static final int sizeof = OS.NMCUSTOMDRAW_sizeof ();
}
