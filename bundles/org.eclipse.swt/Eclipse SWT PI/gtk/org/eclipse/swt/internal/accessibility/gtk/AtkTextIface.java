/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.accessibility.gtk;


public class AtkTextIface {
	public int get_text;
	public int get_text_after_offset;
	public int get_text_at_offset;
	public int get_character_at_offset;
	public int get_text_before_offset;
	public int get_caret_offset;
	public int get_run_attributes;
	public int get_default_attributes;
	public int get_character_extents;
	public int get_character_count;
	public int get_offset_at_point;
	public int get_n_selections;
	public int get_selection;
	public int add_selection;
	public int remove_selection;
	public int set_selection;
	public int set_caret_offset;
	public int text_changed;
	public int text_caret_moved;
	public int text_selection_changed;
}
