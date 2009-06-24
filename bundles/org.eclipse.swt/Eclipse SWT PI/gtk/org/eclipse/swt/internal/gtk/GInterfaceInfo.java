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


public class GInterfaceInfo {
	/** @field cast=(GInterfaceInitFunc) */
	public int /*long*/ interface_init;
	/** @field cast=(GInterfaceFinalizeFunc) */
	public int /*long*/ interface_finalize;
	/** @field cast=(gpointer) */
	public int /*long*/ interface_data;
	public static final int sizeof = OS.GInterfaceInfo_sizeof();
}
