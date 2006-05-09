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


public class AtkTextIface {
	public int /*long*/ get_text;
	public int /*long*/ get_text_after_offset;
	public int /*long*/ get_text_at_offset;
	public int /*long*/ get_character_at_offset;
	public int /*long*/ get_text_before_offset;
	public int /*long*/ get_caret_offset;
	public int /*long*/ get_run_attributes;
	public int /*long*/ get_default_attributes;
	public int /*long*/ get_character_extents;
	public int /*long*/ get_character_count;
	public int /*long*/ get_offset_at_point;
	public int /*long*/ get_n_selections;
	public int /*long*/ get_selection;
	public int /*long*/ add_selection;
	public int /*long*/ remove_selection;
	public int /*long*/ set_selection;
	public int /*long*/ set_caret_offset;
	public int /*long*/ text_changed;
	public int /*long*/ text_caret_moved;
	public int /*long*/ text_selection_changed;
}
