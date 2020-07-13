/********************************************************************************
 * Copyright (c) 2020 Equo
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Guillermo Zunino, Equo - initial implementation
 ********************************************************************************/
#![allow(non_camel_case_types)]

use std::os::raw::{c_void, c_ulong, c_int};

pub type XID = c_ulong;

extern "C" {
    pub fn gtk_widget_get_window(widget: *mut c_void) -> *mut c_void;
    pub fn gdk_x11_window_get_xid(window: *mut c_void) -> XID;
    pub fn gtk_window_present(widget: *mut c_void);
    pub fn gtk_widget_get_toplevel(widget: *mut c_void) -> *mut c_void;
    pub fn gtk_widget_get_visual(window: *mut c_void) -> *mut c_void;
    pub fn gdk_x11_visual_get_xvisual(visual: *mut c_void) -> *mut Visual;
}

#[derive(Debug, Clone, Copy, PartialEq)]
#[repr(C)]
pub struct Visual {
  pub ext_data: *mut c_void,
  pub visualid: XID,
  pub class: c_int,
  pub red_mask: c_ulong,
  pub green_mask: c_ulong,
  pub blue_mask: c_ulong,
  pub bits_per_rgb: c_int,
  pub map_entries: c_int,
}