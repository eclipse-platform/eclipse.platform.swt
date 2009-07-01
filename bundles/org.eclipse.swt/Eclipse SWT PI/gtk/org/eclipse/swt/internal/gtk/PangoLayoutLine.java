/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others. All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.gtk;

public class PangoLayoutLine {
	/** @field cast=(PangoLayout *) */
	public int /*long*/ layout;
	public int start_index;
	public int length;
	/** @field cast=(GSList *) */
	public int /*long*/ runs;
//	public boolean is_paragraph_start;
//	public byte resolved_dir;
	public static final int sizeof = OS.PangoLayoutLine_sizeof();
}
