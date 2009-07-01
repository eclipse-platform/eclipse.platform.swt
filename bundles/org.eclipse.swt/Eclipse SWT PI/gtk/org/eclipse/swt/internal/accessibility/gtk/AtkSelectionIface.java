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


public class AtkSelectionIface {
	/** @field cast=(gboolean (*)()) */
	public int /*long*/ add_selection;
	/** @field cast=(gboolean (*)()) */
	public int /*long*/ clear_selection;
	/** @field cast=(AtkObject *(*)()) */
	public int /*long*/ ref_selection;
	/** @field cast=(gint (*)()) */
	public int /*long*/ get_selection_count;
	/** @field cast=(gboolean (*)()) */
	public int /*long*/ is_child_selected;
	/** @field cast=(gboolean (*)()) */
	public int /*long*/ remove_selection;
	/** @field cast=(gboolean (*)()) */
	public int /*long*/ select_all_selection;
	/** @field cast=(void (*)()) */
	public int /*long*/ selection_changed;
}
