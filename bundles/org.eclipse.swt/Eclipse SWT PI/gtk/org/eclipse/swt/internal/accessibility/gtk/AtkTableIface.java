/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others. All rights reserved.
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


public class AtkTableIface {
	/** @field cast=(AtkObject* (*)()) */
	public long /*int*/ ref_at;
	/** @field cast=(gint (*)()) */
	public long /*int*/ get_index_at;
	/** @field cast=(gint (*)()) */
	public long /*int*/ get_column_at_index;
	/** @field cast=(gint (*)()) */
	public long /*int*/ get_row_at_index;
	/** @field cast=(gint (*)()) */
	public long /*int*/ get_n_columns;
	/** @field cast=(gint (*)()) */
	public long /*int*/ get_n_rows;
	/** @field cast=(gint (*)()) */
	public long /*int*/ get_column_extent_at;
	/** @field cast=(gint (*)()) */
	public long /*int*/ get_row_extent_at;
	/** @field cast=(AtkObject* (*)()) */
	public long /*int*/ get_caption;
	/** @field cast=(const gchar* (*)()) */
	public long /*int*/ get_column_description;
	/** @field cast=(AtkObject* (*)()) */
	public long /*int*/ get_column_header;
	/** @field cast=(const gchar* (*)()) */
	public long /*int*/ get_row_description;
	/** @field cast=(AtkObject* (*)()) */
	public long /*int*/ get_row_header;
	/** @field cast=(AtkObject* (*)()) */
	public long /*int*/ get_summary;
	/** @field cast=(void (*)()) */
	public long /*int*/ set_caption;
	/** @field cast=(void (*)()) */
	public long /*int*/ set_column_description;
	/** @field cast=(void (*)()) */
	public long /*int*/ set_column_header;
	/** @field cast=(void (*)()) */
	public long /*int*/ set_row_description;
	/** @field cast=(void (*)()) */
	public long /*int*/ set_row_header;
	/** @field cast=(void (*)()) */
	public long /*int*/ set_summary;
	/** @field cast=(gint (*)()) */
	public long /*int*/ get_selected_columns;
	/** @field cast=(gint (*)()) */
	public long /*int*/ get_selected_rows;
	/** @field cast=(gboolean (*)()) */
	public long /*int*/ is_column_selected;
	/** @field cast=(gboolean (*)()) */
	public long /*int*/ is_row_selected;
	/** @field cast=(gboolean (*)()) */
	public long /*int*/ is_selected;
	/** @field cast=(gboolean (*)()) */
	public long /*int*/ add_row_selection;
	/** @field cast=(gboolean (*)()) */
	public long /*int*/ remove_row_selection;
	/** @field cast=(gboolean (*)()) */
	public long /*int*/ add_column_selection;
	/** @field cast=(gboolean (*)()) */
	public long /*int*/ remove_column_selection;
	/** @field cast=(void (*)()) */
	public long /*int*/ row_inserted;
	/** @field cast=(void (*)()) */
	public long /*int*/ column_inserted;
	/** @field cast=(void (*)()) */
	public long /*int*/ row_deleted;
	/** @field cast=(void (*)()) */
	public long /*int*/ column_deleted;
	/** @field cast=(void (*)()) */
	public long /*int*/ row_reordered;
	/** @field cast=(void (*)()) */
	public long /*int*/ column_reordered;
	/** @field cast=(void (*)()) */
	public long /*int*/ model_changed;
}
