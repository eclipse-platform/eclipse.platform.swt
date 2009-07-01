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


public class GtkFileSelection {
	/** @field cast=(GtkWidget *) */
	public int /*long*/ dir_list;
	/** @field cast=(GtkWidget *) */
	public int /*long*/ file_list;
	/** @field cast=(GtkWidget *) */
	public int /*long*/ selection_entry;
	/** @field cast=(GtkWidget *) */
	public int /*long*/ selection_text;
	/** @field cast=(GtkWidget *) */
	public int /*long*/ main_vbox;
	/** @field cast=(GtkWidget *) */
	public int /*long*/ ok_button;
	/** @field cast=(GtkWidget *) */
	public int /*long*/ cancel_button;
	/** @field cast=(GtkWidget *) */
	public int /*long*/ help_button;
	/** @field cast=(GtkWidget *) */
	public int /*long*/ history_pulldown;
	/** @field cast=(GtkWidget *) */
	public int /*long*/ history_menu;
	/** @field cast=(GList *) */
	public int /*long*/ history_list;
	/** @field cast=(GtkWidget *) */
	public int /*long*/ fileop_dialog;
	/** @field cast=(GtkWidget *) */
	public int /*long*/ fileop_entry;
	/** @field cast=(gchar *) */
	public int /*long*/ fileop_file;
	/** @field cast=(gpointer) */
	public int /*long*/ cmpl_state;			// gpointer
	/** @field cast=(GtkWidget *) */
	public int /*long*/ fileop_c_dir;
	/** @field cast=(GtkWidget *) */
	public int /*long*/ fileop_del_file;
	/** @field cast=(GtkWidget *) */
	public int /*long*/ fileop_ren_file;
	/** @field cast=(GtkWidget *) */
	public int /*long*/ button_area;
	/** @field cast=(GtkWidget *) */
	public int /*long*/ action_area;
}
