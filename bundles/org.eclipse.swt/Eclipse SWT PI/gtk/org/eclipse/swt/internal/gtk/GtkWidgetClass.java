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


public class GtkWidgetClass extends GObjectClass {
	public int activate_signal;
	public int set_scroll_adjustments_signal;	
	/** @field cast=(void(*)()) */
	public int /*long*/ dispatch_child_properties_changed;
	/** @field cast=(void(*)()) */
	public int /*long*/ show;
	/** @field cast=(void(*)()) */
	public int /*long*/ show_all;
	/** @field cast=(void(*)()) */
	public int /*long*/ hide;
	/** @field cast=(void(*)()) */
	public int /*long*/ hide_all;
	/** @field cast=(void(*)()) */
	public int /*long*/ map;
	/** @field cast=(void(*)()) */
	public int /*long*/ unmap;
	/** @field cast=(void(*)()) */
	public int /*long*/ realize;
	/** @field cast=(void(*)()) */
	public int /*long*/ unrealize;
	/** @field cast=(void(*)()) */
	public int /*long*/ size_request;
	/** @field cast=(void(*)()) */
	public int /*long*/ size_allocate;
	/** @field cast=(void(*)()) */
	public int /*long*/ state_changed; 
	/** @field cast=(void(*)()) */
	public int /*long*/ parent_set;
	/** @field cast=(void(*)()) */
	public int /*long*/ hierarchy_changed;
	/** @field cast=(void(*)()) */
	public int /*long*/ style_set;
	/** @field cast=(void(*)()) */
	public int /*long*/ direction_changed;
	/** @field cast=(void(*)()) */
	public int /*long*/ grab_notify;
	/** @field cast=(void(*)()) */
	public int /*long*/ child_notify;	
	/** @field cast=(gboolean(*)()) */
	public int /*long*/ mnemonic_activate;
	/** @field cast=(void(*)()) */
	public int /*long*/ grab_focus;
	/** @field cast=(gboolean(*)()) */
	public int /*long*/ focus;
	/** @field cast=(gboolean(*)()) */
	public int /*long*/ event;
	/** @field cast=(gboolean(*)()) */
	public int /*long*/ button_press_event;
	/** @field cast=(gboolean(*)()) */
	public int /*long*/ button_release_event;
	/** @field cast=(gboolean(*)()) */
	public int /*long*/ scroll_event;
	/** @field cast=(gboolean(*)()) */
	public int /*long*/ motion_notify_event;
	/** @field cast=(gboolean(*)()) */
	public int /*long*/ delete_event;
	/** @field cast=(gboolean(*)()) */
	public int /*long*/ destroy_event;
	/** @field cast=(gboolean(*)()) */
	public int /*long*/ expose_event;
	/** @field cast=(gboolean(*)()) */
	public int /*long*/ key_press_event;
	/** @field cast=(gboolean(*)()) */
	public int /*long*/ key_release_event;
	/** @field cast=(gboolean(*)()) */
	public int /*long*/ enter_notify_event;
	/** @field cast=(gboolean(*)()) */
	public int /*long*/ leave_notify_event;
	/** @field cast=(gboolean(*)()) */
	public int /*long*/ configure_event;
	/** @field cast=(gboolean(*)()) */
	public int /*long*/ focus_in_event;
	/** @field cast=(gboolean(*)()) */
	public int /*long*/ focus_out_event;
	/** @field cast=(gboolean(*)()) */
	public int /*long*/ map_event;
	/** @field cast=(gboolean(*)()) */
	public int /*long*/ unmap_event;
	/** @field cast=(gboolean(*)()) */
	public int /*long*/ property_notify_event;
	/** @field cast=(gboolean(*)()) */
	public int /*long*/ selection_clear_event;
	/** @field cast=(gboolean(*)()) */
	public int /*long*/ selection_request_event;
	/** @field cast=(gboolean(*)()) */
	public int /*long*/ selection_notify_event;
	/** @field cast=(gboolean(*)()) */
	public int /*long*/ proximity_in_event;
	/** @field cast=(gboolean(*)()) */
	public int /*long*/ proximity_out_event;
	/** @field cast=(gboolean(*)()) */
	public int /*long*/ visibility_notify_event;
	/** @field cast=(gboolean(*)()) */
	public int /*long*/ client_event;
	/** @field cast=(gboolean(*)()) */
	public int /*long*/ no_expose_event;
	/** @field cast=(gboolean(*)()) */
	public int /*long*/ window_state_event;
	/** @field cast=(void(*)()) */
	public int /*long*/ selection_get;
	/** @field cast=(void(*)()) */
	public int /*long*/ selection_received;
	/** @field cast=(void(*)()) */
	public int /*long*/ drag_begin;
	/** @field cast=(void(*)()) */
	public int /*long*/ drag_end;
	/** @field cast=(void(*)()) */
	public int /*long*/ drag_data_get;
	/** @field cast=(void(*)()) */
	public int /*long*/ drag_data_delete;
	/** @field cast=(void(*)()) */
	public int /*long*/ drag_leave;
	/** @field cast=(gboolean(*)()) */
	public int /*long*/ drag_motion;
	/** @field cast=(gboolean(*)()) */
	public int /*long*/ drag_drop;
	/** @field cast=(void(*)()) */
	public int /*long*/ drag_data_received;
	/** @field cast=(gboolean(*)()) */
	public int /*long*/ popup_menu;
	/** @field cast=(gboolean(*)()) */
	public int /*long*/ show_help;
	/** @field cast=(AtkObject*(*)()) */
	public int /*long*/ get_accessible;
	/** @field cast=(void(*)()) */
	public int /*long*/ screen_changed;
}
