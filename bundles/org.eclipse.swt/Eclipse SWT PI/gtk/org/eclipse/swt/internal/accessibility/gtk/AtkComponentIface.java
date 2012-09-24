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
package org.eclipse.swt.internal.accessibility.gtk;


public class AtkComponentIface {
	/** @field cast=(guint (*)()) */
	public long /*int*/ add_focus_handler;
	/** @field cast=(gboolean (*)()) */
	public long /*int*/ contains;
	/** @field cast=(AtkObject *(*)()) */
	public long /*int*/ ref_accessible_at_point;
	/** @field cast=(void (*)()) */
	public long /*int*/ get_extents;
	/** @field cast=(void (*)()) */
	public long /*int*/ get_position;
	/** @field cast=(void (*)()) */
	public long /*int*/ get_size;
	/** @field cast=(gboolean (*)()) */
	public long /*int*/ grab_focus;
	/** @field cast=(void (*)()) */
	public long /*int*/ remove_focus_handler;
	/** @field cast=(gboolean (*)()) */
	public long /*int*/ set_extents;
	/** @field cast=(gboolean (*)()) */
	public long /*int*/ set_position;
	/** @field cast=(gboolean (*)()) */
	public long /*int*/ set_size;
	/** @field cast=(AtkLayer (*)()) */
	public long /*int*/ get_layer;
	/** @field cast=(gint (*)()) */
	public long /*int*/ get_mdi_zorder;
}
