/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others. All rights reserved.
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


public class AtkTextIface {
	/** @field cast=(gchar *(*)()) */
	public long /*int*/ get_text;
	/** @field cast=(gchar *(*)()) */
	public long /*int*/ get_text_after_offset;
	/** @field cast=(gchar *(*)()) */
	public long /*int*/ get_text_at_offset;
	/** @field cast=(gunichar (*)()) */
	public long /*int*/ get_character_at_offset;
	/** @field cast=(gchar *(*)()) */
	public long /*int*/ get_text_before_offset;
	/** @field cast=(gint (*)()) */
	public long /*int*/ get_caret_offset;
	/** @field cast=(AtkAttributeSet *(*)()) */
	public long /*int*/ get_run_attributes;
	/** @field cast=(AtkAttributeSet *(*)()) */
	public long /*int*/ get_default_attributes;
	/** @field cast=(void (*)()) */
	public long /*int*/ get_character_extents;
	/** @field cast=(gint (*)()) */
	public long /*int*/ get_character_count;
	/** @field cast=(gint (*)()) */
	public long /*int*/ get_offset_at_point;
	/** @field cast=(gint (*)()) */
	public long /*int*/ get_n_selections;
	/** @field cast=(gchar *(*)()) */
	public long /*int*/ get_selection;
	/** @field cast=(gboolean (*)()) */
	public long /*int*/ add_selection;
	/** @field cast=(gboolean (*)()) */
	public long /*int*/ remove_selection;
	/** @field cast=(gboolean (*)()) */
	public long /*int*/ set_selection;
	/** @field cast=(gboolean (*)()) */
	public long /*int*/ set_caret_offset;
	/** @field cast=(void (*)()) */
	public long /*int*/ text_changed;
	/** @field cast=(void (*)()) */
	public long /*int*/ text_caret_moved;
	/** @field cast=(void (*)()) */
	public long /*int*/ text_selection_changed;
	/** @field cast=(void (*)()) */
	public long /*int*/ get_range_extents;
	/** @field cast=(AtkTextRange** (*)()) */
	public long /*int*/ get_bounded_ranges;
}
