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
package org.eclipse.swt.internal.gtk;


public class GtkWidgetClass extends GObjectClass {
	public int activate_signal;
	public int set_scroll_adjustments_signal;	
	public int /*long*/ dispatch_child_properties_changed;
	public int /*long*/ show;
	public int /*long*/ show_all;
	public int /*long*/ hide;
	public int /*long*/ hide_all;
	public int /*long*/ map;
	public int /*long*/ unmap;
	public int /*long*/ realize;
	public int /*long*/ unrealize;
	public int /*long*/ size_request;
	public int /*long*/ size_allocate;
	public int /*long*/ state_changed; 
	public int /*long*/ parent_set;
	public int /*long*/ hierarchy_changed;
	public int /*long*/ style_set;
	public int /*long*/ direction_changed;
	public int /*long*/ grab_notify;
	public int /*long*/ child_notify;	
	public int /*long*/ mnemonic_activate;
	public int /*long*/ grab_focus;
	public int /*long*/ focus;
	public int /*long*/ event;
	public int /*long*/ button_press_event;
	public int /*long*/ button_release_event;
	public int /*long*/ scroll_event;
	public int /*long*/ motion_notify_event;
	public int /*long*/ delete_event;
	public int /*long*/ destroy_event;
	public int /*long*/ expose_event;
	public int /*long*/ key_press_event;
	public int /*long*/ key_release_event;
	public int /*long*/ enter_notify_event;
	public int /*long*/ leave_notify_event;
	public int /*long*/ configure_event;
	public int /*long*/ focus_in_event;
	public int /*long*/ focus_out_event;
	public int /*long*/ map_event;
	public int /*long*/ unmap_event;
	public int /*long*/ property_notify_event;
	public int /*long*/ selection_clear_event;
	public int /*long*/ selection_request_event;
	public int /*long*/ selection_notify_event;
	public int /*long*/ proximity_in_event;
	public int /*long*/ proximity_out_event;
	public int /*long*/ visibility_notify_event;
	public int /*long*/ client_event;
	public int /*long*/ no_expose_event;
	public int /*long*/ window_state_event;
	public int /*long*/ selection_get;
	public int /*long*/ selection_received;
	public int /*long*/ drag_begin;
	public int /*long*/ drag_end;
	public int /*long*/ drag_data_get;
	public int /*long*/ drag_data_delete;
	public int /*long*/ drag_leave;
	public int /*long*/ drag_motion;
	public int /*long*/ drag_drop;
	public int /*long*/ drag_data_received;
	public int /*long*/ popup_menu;
	public int /*long*/ show_help;
	public int /*long*/ get_accessible;
	public int /*long*/ screen_changed;
}
