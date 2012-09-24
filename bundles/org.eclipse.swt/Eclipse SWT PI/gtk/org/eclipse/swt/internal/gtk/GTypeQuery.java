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


public class GTypeQuery {
	/** @field cast=(GType) */
	public int type;
	/** @field cast=(const gchar *) */
	public long /*int*/ type_name;
	/** @field cast=(guint) */
	public int class_size;
	/** @field cast=(guint) */
	public int instance_size;
	public static final int sizeof = OS.GTypeQuery_sizeof();
}
