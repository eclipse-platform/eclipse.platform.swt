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
use cef;
use utils;
#[cfg(target_os = "linux")]
use gtk;

use std::os::raw::{c_int, c_void};
use std::mem::size_of;
use std::ptr::null_mut;

pub fn create_browser(canvas_hwnd: *mut c_void, url: &str, jclient: &mut cef::_cef_client_t, w: c_int, h: c_int, js: c_int, bg: cef::cef_color_t) -> *mut cef::cef_browser_t {
    //println!("create_browser in {}", canvas_hwnd);

    let window_info = cef_window_info(canvas_hwnd, w, h);
    // Browser settings.
    // It is mandatory to set the "size" member.
    let browser_settings = cef::_cef_browser_settings_t {
        size: size_of::<cef::_cef_browser_settings_t>(),
        windowless_frame_rate: 0,
        standard_font_family: utils::cef_string_empty(),
        fixed_font_family: utils::cef_string_empty(),
        serif_font_family: utils::cef_string_empty(),
        sans_serif_font_family: utils::cef_string_empty(),
        cursive_font_family: utils::cef_string_empty(),
        fantasy_font_family: utils::cef_string_empty(),
        default_font_size: 0,
        default_fixed_font_size: 0,
        minimum_font_size: 0,
        minimum_logical_font_size: 0,
        default_encoding: utils::cef_string_empty(),
        remote_fonts: cef::cef_state_t::STATE_DEFAULT,
        javascript: if js == 0 { cef::cef_state_t::STATE_DISABLED } else { cef::cef_state_t::STATE_DEFAULT },
        javascript_open_windows: cef::cef_state_t::STATE_DEFAULT,
        javascript_close_windows: cef::cef_state_t::STATE_DEFAULT,
        javascript_access_clipboard: cef::cef_state_t::STATE_DEFAULT,
        javascript_dom_paste: cef::cef_state_t::STATE_DEFAULT,
        plugins: cef::cef_state_t::STATE_DEFAULT,
        universal_access_from_file_urls: cef::cef_state_t::STATE_ENABLED,
        file_access_from_file_urls: cef::cef_state_t::STATE_ENABLED,
        web_security: cef::cef_state_t::STATE_DEFAULT,
        image_loading: cef::cef_state_t::STATE_DEFAULT,
        image_shrink_standalone_to_fit: cef::cef_state_t::STATE_DEFAULT,
        text_area_resize: cef::cef_state_t::STATE_DEFAULT,
        tab_to_links: cef::cef_state_t::STATE_DEFAULT,
        local_storage: cef::cef_state_t::STATE_DEFAULT,
        databases: cef::cef_state_t::STATE_DEFAULT,
        application_cache: cef::cef_state_t::STATE_DEFAULT,
        webgl: cef::cef_state_t::STATE_DEFAULT,
        background_color: bg,
        accept_language_list: utils::cef_string_empty()
    };

    let url_cef = utils::cef_string(url);

    // Create browser.
    // println!("Calling cef_browser_host_create_browser");
    //if unsafe { cef::cef_browser_host_create_browser(&window_info, client, &url_cef, &browser_settings, null_mut()) } != 1 {
        //println!("Failed calling browserHostCreateBrowser");
    //}
    let browser: *mut cef::cef_browser_t = unsafe { cef::cef_browser_host_create_browser_sync(&window_info, jclient, &url_cef, &browser_settings, null_mut()) };
    // println!("after Calling cef_browser_host_create_browser {:?}", browser);
    
    assert_eq!(unsafe{(*browser).base.size}, size_of::<cef::_cef_browser_t>());
    // println!("browser size ok");
    browser
}

#[cfg(target_os = "linux")]
fn override_system_visual(visual: *mut c_void) {
    unsafe {
        let xvisual = gtk::gdk_x11_visual_get_xvisual(visual);
        //println!("xvisualid: {:?}", (*xvisual).visualid);
        cef_override_system_visual((*xvisual).visualid);
    }
}

#[cfg(target_os = "linux")]
extern "C" {
    pub fn cef_override_system_visual(visual_id: std::os::raw::c_ulong);
}

#[cfg(target_os = "linux")]
fn cef_window_info(hwnd: *mut c_void, w: c_int, h: c_int) -> cef::_cef_window_info_t {
    use std::os::raw::c_uint;
    let window_info = unsafe {
        let visual = gtk::gtk_widget_get_visual(hwnd);
        override_system_visual(visual);
        let parent_win = gtk::gdk_x11_window_get_xid(gtk::gtk_widget_get_window(hwnd as *mut c_void));
        let window_info = cef::_cef_window_info_t {
            x: 0,
            y: 0,
            width: w as c_uint,
            height: h as c_uint,
            parent_window: parent_win,
            windowless_rendering_enabled: 0,
            window: 0
        };
        window_info
    };
    //println!("parent {}", window_info.parent_window);
    window_info
}

#[cfg(target_os = "linux")]
pub fn set_window_parent(window_info: *mut cef::_cef_window_info_t, hwnd: *mut c_void, x: c_int, y: c_int, w: c_int, h: c_int) {
    use std::os::raw::c_uint;
    //unsafe {println!("orig window_info {} {:?}", hwnd, (*window_info)); };
    unsafe { 
        (*window_info).x = x as c_uint;
        (*window_info).y = y as c_uint;
        (*window_info).width = w as c_uint;
        (*window_info).height = h as c_uint;
        (*window_info).parent_window = if hwnd.is_null() { 0 } else {
            let visual = gtk::gtk_widget_get_visual(hwnd);
            override_system_visual(visual);

            gtk::gdk_x11_window_get_xid(gtk::gtk_widget_get_window(hwnd)) 
        };
        (*window_info).windowless_rendering_enabled = 0;
        (*window_info).window = 0;
    }
    //unsafe { println!("new window_info {:?}", (*window_info)); };
}

#[cfg(target_os = "macos")]
fn cef_window_info(hwnd: *mut c_void, w: c_int, h: c_int) -> cef::_cef_window_info_t {
    use std::os::raw::c_void;
    let window_info = cef::_cef_window_info_t {
        x: 0,
        y: 0,
        width: w,
        height: h,
        parent_view: hwnd,
        windowless_rendering_enabled: 0,
        view: 0 as *mut c_void,
        hidden: 0,
        window_name: cef::cef_string_t { str: null_mut(),  length: 0,  dtor: Option::None }
    };
    //println!("parent {:?}", window_info.parent_view);
    window_info
}

#[cfg(target_os = "macos")]
pub fn set_window_parent(window_info: *mut cef::_cef_window_info_t, hwnd: *mut c_void, x: c_int, y: c_int, w: c_int, h: c_int) {
    use std::os::raw::c_void;
    //unsafe { println!("orig window_info {} {:?}", hwnd, (*window_info)); };
    unsafe { 
        (*window_info).x = x;
        (*window_info).y = y;
        (*window_info).width = w;
        (*window_info).height = h;
        (*window_info).parent_view = hwnd;
        (*window_info).windowless_rendering_enabled = 0;
        (*window_info).view = 0 as *mut c_void;
        (*window_info).hidden = 0;
        (*window_info).window_name = cef::cef_string_t { str: null_mut(),  length: 0,  dtor: Option::None };
    };
    //unsafe { println!("new window_info {:?}", (*window_info)); };
}

#[cfg(windows)]
fn cef_window_info(hwnd: *mut c_void, w: c_int, h: c_int) -> cef::_cef_window_info_t {
    extern crate winapi;

    let window_info = cef::_cef_window_info_t {
        x: 0,
        y: 0,
        width: w,
        height: h,
        parent_window: hwnd as cef::win::HWND,
        windowless_rendering_enabled: 0,
        window: 0 as cef::win::HWND,
        ex_style: 0,
        window_name: cef::cef_string_t { str: null_mut(),  length: 0,  dtor: Option::None },
        style: winapi::um::winuser::WS_CHILDWINDOW | winapi::um::winuser::WS_CLIPCHILDREN
            | winapi::um::winuser::WS_CLIPSIBLINGS | winapi::um::winuser::WS_VISIBLE | winapi::um::winuser::WS_TABSTOP,
        menu: 0 as cef::win::HMENU
    };
    //println!("parent {:?}", window_info.parent_window);
    window_info
}

#[cfg(windows)]
pub fn set_window_parent(window_info: *mut cef::_cef_window_info_t, hwnd: *mut c_void, x: c_int, y: c_int, w: c_int, h: c_int) {
    extern crate winapi;
    unsafe {
        //println!("orig window_info {} {:?}", hwnd, (*window_info));
        if x != 0 {
            (*window_info).x = x;
        }
        if y != 0 {
            (*window_info).y = y;
        }
        if w != 0 {
            (*window_info).width = w;
        }
        if h != 0 {
            (*window_info).height = h;
        }
        (*window_info).parent_window = hwnd as cef::win::HWND;
        (*window_info).windowless_rendering_enabled = 0;
        (*window_info).window = 0 as cef::win::HWND;
        (*window_info).ex_style = 0;
        (*window_info).window_name = cef::cef_string_t { str: null_mut(),  length: 0,  dtor: Option::None };
        if !hwnd.is_null() {
            (*window_info).style = winapi::um::winuser::WS_CHILDWINDOW | winapi::um::winuser::WS_CLIPCHILDREN
                | winapi::um::winuser::WS_CLIPSIBLINGS | winapi::um::winuser::WS_VISIBLE | winapi::um::winuser::WS_TABSTOP;
        }
        (*window_info).menu = 0 as cef::win::HMENU;
        //println!("new window_info {:?}", (*window_info)); 
    };
}
