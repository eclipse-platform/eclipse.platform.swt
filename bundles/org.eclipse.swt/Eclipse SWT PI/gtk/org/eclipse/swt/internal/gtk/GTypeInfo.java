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


public class GTypeInfo {
	/** @field cast=(guint16) */
	public short class_size;
	/** @field cast=(GBaseInitFunc) */
	public int /*long*/ base_init;
	/** @field cast=(GBaseFinalizeFunc) */
	public int /*long*/ base_finalize;
	/** @field cast=(GClassInitFunc) */
	public int /*long*/ class_init;
	/** @field cast=(GClassFinalizeFunc) */
	public int /*long*/ class_finalize;
	/** @field cast=(gconstpointer) */
	public int /*long*/ class_data;
	/** @field cast=(guint16) */
	public short instance_size;
	/** @field cast=(guint16) */
	public short n_preallocs;
	/** @field cast=(GInstanceInitFunc) */
	public int /*long*/ instance_init;
	/** @field cast=(GTypeValueTable *) */
	public int /*long*/ value_table;
	public static final int sizeof = OS.GTypeInfo_sizeof();	
}
