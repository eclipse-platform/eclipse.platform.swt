/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others. All rights reserved.
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
	public int /*long*/ add_focus_handler;
	public int /*long*/ contains;
	public int /*long*/ ref_accessible_at_point;
	public int /*long*/ get_extents;
	public int /*long*/ get_position;
	public int /*long*/ get_size;
	public int /*long*/ grab_focus;
	public int /*long*/ remove_focus_handler;
	public int /*long*/ set_extents;
	public int /*long*/ set_position;
	public int /*long*/ set_size;
	public int /*long*/ get_layer;
	public int /*long*/ get_mdi_zorder;
}
