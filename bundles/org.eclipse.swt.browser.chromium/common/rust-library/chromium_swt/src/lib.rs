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
extern crate chromium;

#[cfg(target_os = "linux")]
extern crate x11;
#[cfg(unix)]
extern crate nix;
#[cfg(target_os = "macos")]
#[macro_use]
extern crate objc;

use chromium::cef;
use chromium::utils;
use chromium::socket;

mod app;
#[cfg(target_os = "linux")]
mod gtk;

use std::os::raw::{c_char, c_int, c_void};
#[cfg(unix)]
use std::collections::HashMap;

#[cfg(target_os = "linux")]
unsafe extern fn xerror_handler_impl(_: *mut x11::xlib::Display, _event: *mut x11::xlib::XErrorEvent) -> c_int {
    //print!("X error received: ");
    //println!("type {}, serial {}, error_code {}, request_code {}, minor_code {}",
    //    (*event).type_, (*event).serial, (*event).error_code, (*event).request_code, (*event).minor_code);
    0
}
#[cfg(target_os = "linux")]
unsafe extern fn xioerror_handler_impl(_: *mut x11::xlib::Display) -> c_int {
    //println!("XUI error received");
    0
}

#[no_mangle]
pub extern fn cefswt_init(japp: *mut cef::cef_app_t, cefrust_path: *const c_char, cef_path: *const c_char, version: *const c_char, debug_port: c_int) {
    //println!("DLL init");
    assert_eq!(unsafe{(*japp).base.size}, std::mem::size_of::<cef::_cef_app_t>());
    //println!("app {:?}", japp);

    let cefrust_path = utils::str_from_c(cefrust_path);
    let cef_path = utils::str_from_c(cef_path);
    let version = utils::str_from_c(version);

    let main_args = utils::prepare_args();

    let cefrust_dir = std::path::Path::new(&cefrust_path);
    let cef_dir = std::path::Path::new(&cef_path);

    let subp = utils::subp_path(cefrust_dir, version);
    let subp_cef = utils::cef_string(&subp);

    if cef_path != cefrust_path {
        if cfg!(target_family = "windows") {
            set_env_var(cef_path, "PATH", ";");
        }
        if cfg!(target_os = "linux") {
            set_env_var(cef_path, "LD_LIBRARY_PATH", ":");
        }
        if cfg!(target_os = "macos") {
            set_env_var(cef_path, "DYLD_LIBRARY_PATH", ":")
        }
    }

    let resources_cef = if cfg!(target_os = "macos") {
        utils::cef_string(cef_dir.join("Chromium Embedded Framework.framework").join("Resources").to_str().unwrap())
    } else {
        utils::cef_string(cef_dir.to_str().unwrap())
    };
    let locales_cef = if cfg!(target_os = "macos") {
        utils::cef_string(cef_dir.join("Chromium Embedded Framework.framework").join("Resources").to_str().unwrap())
    } else {
        utils::cef_string(cef_dir.join("locales").to_str().unwrap())
    };
    let framework_dir_cef = if cfg!(target_os = "macos") {
        utils::cef_string(cef_dir.join("Chromium Embedded Framework.framework").to_str().unwrap())
    } else {
        utils::cef_string_empty()
    };

    let cache_dir_cef = utils::cef_string(cef_dir.parent().unwrap().parent().unwrap().join("cef_cache").to_str().unwrap());

    let logfile_cef = utils::cef_string(cef_dir.join("lib.log").to_str().unwrap());

    let settings = cef::_cef_settings_t {
        size: std::mem::size_of::<cef::_cef_settings_t>(),
        single_process: 0,
        no_sandbox: 1,
        browser_subprocess_path: subp_cef,
        framework_dir_path: framework_dir_cef,
        multi_threaded_message_loop: 0,
        external_message_pump: 1,
        windowless_rendering_enabled: 0,
        command_line_args_disabled: 0,
        cache_path: cache_dir_cef,
        user_data_path: utils::cef_string_empty(),
        persist_session_cookies: 1,
        persist_user_preferences: 1,
        user_agent: utils::cef_string_empty(),
        product_version: utils::cef_string_empty(),
        locale: utils::cef_string_empty(),
        log_file: logfile_cef,
        log_severity: cef::cef_log_severity_t::LOGSEVERITY_INFO,
        // log_severity: cef::cef_log_severity_t::LOGSEVERITY_VERBOSE,
        javascript_flags: utils::cef_string_empty(),
        resources_dir_path: resources_cef,
        locales_dir_path: locales_cef,
        pack_loading_disabled: 0,
        remote_debugging_port: debug_port,
        uncaught_exception_stack_size: 0,
        ignore_certificate_errors: 0,
        enable_net_security_expiration: 0,
        background_color: 0,
        accept_language_list: utils::cef_string_empty()
    };

    //println!("Calling cef_initialize");
    do_initialize(main_args, settings, japp);
    // println!("After Calling cef_initialize");
}

fn set_env_var(cef_path: &str, var: &str, sep: &str) {
    match std::env::var(var) {
        Ok(paths) => {
            let paths = format!("{}{}{}", cef_path, sep, paths);
            println!("{}: {}", var, paths);
            std::env::set_var(var, paths);
        },
        Err(e) => {
            println!("Couldn't read {} ({})", var, e);
            std::env::set_var(var, cef_path);
        },
    };
}

#[cfg(target_os = "linux")]
fn do_initialize(main_args: cef::_cef_main_args_t, settings: cef::_cef_settings_t, app_raw: *mut cef::_cef_app_t) {
    unsafe { x11::xlib::XSetErrorHandler(Option::Some(xerror_handler_impl)) };
    unsafe { x11::xlib::XSetIOErrorHandler(Option::Some(xioerror_handler_impl)) };

    let mut signal_handlers: HashMap<c_int, nix::sys::signal::SigAction> = HashMap::new();
    backup_signal_handlers(&mut signal_handlers);

    unsafe { cef::cef_initialize(&main_args, &settings, app_raw, std::ptr::null_mut()) };

    restore_signal_handlers(signal_handlers);
}

#[cfg(target_os = "macos")]
static EVENT_KEY: char = 'k';

#[cfg(target_os = "macos")]
fn do_initialize(main_args: cef::_cef_main_args_t, settings: cef::_cef_settings_t, app_raw: *mut cef::_cef_app_t) {
    let mut signal_handlers: HashMap<c_int, nix::sys::signal::SigAction> = HashMap::new();
    backup_signal_handlers(&mut signal_handlers);

    swizzle_send_event();

    unsafe { cef::cef_initialize(&main_args, &settings, &mut (*app_raw), std::ptr::null_mut()) };

    restore_signal_handlers(signal_handlers);
}

#[cfg(target_os = "macos")]
fn swizzle_send_event() {
    use std::ffi::CString;
    use objc::runtime::{BOOL, Class, Method, NO, YES, Object, Sel, self};
    use objc::{Encode, EncodeArguments, Encoding};
    use nix::libc::intptr_t;

    fn count_args(sel: Sel) -> usize {
        sel.name().chars().filter(|&c| c == ':').count()
    }

    fn method_type_encoding(ret: &Encoding, args: &[Encoding]) -> CString {
        let mut types = ret.as_str().to_owned();
        // First two arguments are always self and the selector
        types.push_str(<*mut Object>::encode().as_str());
        types.push_str(Sel::encode().as_str());
        types.extend(args.iter().map(|e| e.as_str()));
        CString::new(types).unwrap()
    }

    pub unsafe fn add_method<F>(cls: *mut Class, sel: Sel, func: F)
            where F: objc::declare::MethodImplementation<Callee=Object> {
        let encs = F::Args::encodings();
        let encs = encs.as_ref();
        let sel_args = count_args(sel);
        assert!(sel_args == encs.len(),
            "Selector accepts {} arguments, but function accepts {}",
            sel_args, encs.len(),
        );

        let types = method_type_encoding(&F::Ret::encode(), encs);
        let success = runtime::class_addMethod(cls, sel, func.imp(),
            types.as_ptr());
        assert!(success != NO, "Failed to add method {:?}", sel);
    }

    pub type Id = *mut runtime::Object;
    pub type AssociationPolicy = intptr_t;
    extern {
        pub fn objc_getAssociatedObject(object: Id, key: *const c_void) -> BOOL;
        pub fn objc_setAssociatedObject(object: Id,
                                    key: *const c_void,
                                    value: BOOL,
                                    policy: AssociationPolicy);
    }

    let cls_nm = CString::new("NSApplication").unwrap();
    let cls = unsafe { runtime::objc_getClass(cls_nm.as_ptr()) as *mut Class };
    assert!(!cls.is_null(), "null class");

    extern fn is_handling_sendevent(this: &mut Object, _cmd: Sel) -> BOOL {
        //println!("isHandlingSendEvent {:?}", this);
        let kp = &EVENT_KEY as *const _ as *const c_void;
        let is = unsafe { objc_getAssociatedObject(this, kp) };
        //println!("AssociatedObject: {:?}", is);
        is
    }
    unsafe { add_method(cls, sel!(isHandlingSendEvent), is_handling_sendevent as extern fn(&mut Object, Sel) -> BOOL) };

    extern fn set_handling_sendevent(this: &mut Object, _cmd: Sel, handling_sendevent: BOOL) {
        //println!("setHandlingSendEvent {:?} {:?}", this, handling_sendevent);
        let kp = &EVENT_KEY as *const _ as *const c_void;
        let policy_assign = 0;
        unsafe { objc_setAssociatedObject(this, kp, handling_sendevent, policy_assign) };
    }
    unsafe { add_method(cls, sel!(setHandlingSendEvent:), set_handling_sendevent as extern fn(&mut Object, Sel, BOOL)) };

    extern fn swizzled_sendevent(this: &mut Object, _cmd: Sel, event: Id) {
        //println!("swizzled_sendevent {:?}", this);
        unsafe {
            let handling: BOOL = msg_send![this, isHandlingSendEvent];
            msg_send![this, setHandlingSendEvent:YES];
            msg_send![this, _swizzled_sendEvent:event];
            msg_send![this, setHandlingSendEvent:handling];
        }
    }
    let sel_swizzled_sendevent = sel!(_swizzled_sendEvent:);
    unsafe { add_method(cls, sel_swizzled_sendevent, swizzled_sendevent as extern fn(&mut Object, Sel, Id)) };

    unsafe {
        let original = runtime::class_getInstanceMethod(cls, sel!(sendEvent:)) as *mut Method;
        let swizzled = runtime::class_getInstanceMethod(cls, sel_swizzled_sendevent) as *mut Method;
        runtime::method_exchangeImplementations(original, swizzled);
    }
}

#[cfg(target_os = "windows")]
fn do_initialize(main_args: cef::_cef_main_args_t, settings: cef::_cef_settings_t, app_raw: *mut cef::_cef_app_t) {
    unsafe { cef::cef_enable_highdpi_support() };
    unsafe { cef::cef_initialize(&main_args, &settings, &mut (*app_raw), std::ptr::null_mut()) };
}

#[cfg(unix)]
fn backup_signal_handlers(signal_handlers: &mut HashMap<c_int, nix::sys::signal::SigAction>) {
    use nix::sys::signal;
    let signals_to_restore = [signal::SIGHUP, signal::SIGINT, signal::SIGQUIT, signal::SIGILL,
        signal::SIGABRT, signal::SIGFPE, signal::SIGSEGV, signal::SIGALRM, signal::SIGTERM,
        signal::SIGCHLD, signal::SIGBUS, signal::SIGTRAP, signal::SIGPIPE];

    for signal in &signals_to_restore {
        let sig_action = signal::SigAction::new(signal::SigHandler::SigDfl,
                                          signal::SaFlags::empty(),
                                          signal::SigSet::empty());
        let oldsigact = unsafe { signal::sigaction(*signal, &sig_action) };
        //println!("backup signal {:?}:{:?}", signal, "oldsigact.ok()");
        signal_handlers.insert(*signal as c_int, oldsigact.unwrap());
    }
}

#[cfg(unix)]
fn restore_signal_handlers(signal_handlers: HashMap<c_int, nix::sys::signal::SigAction>) {
    use nix::sys::signal;
    for (signal, sigact) in signal_handlers {
        //println!("restore signal {:?}:{:?}", signal, "sigact");
        unsafe { signal::sigaction(std::mem::transmute(signal), &sigact).unwrap() };
    }
}

#[no_mangle]
pub extern fn cefswt_create_browser(hwnd: *mut c_void, url: *const c_char, client: &mut cef::_cef_client_t, w: c_int, h: c_int, js: c_int, bg: cef::cef_color_t) -> *const cef::cef_browser_t {
    assert_eq!((*client).base.size, std::mem::size_of::<cef::_cef_client_t>());

    // println!("hwnd: {:?}", hwnd);
    // (*client).on_process_message_received = Option::Some(on_process_message_received);

    let url = utils::str_from_c(url);
    // println!("url: {:?}", url);

    let browser = app::create_browser(hwnd, url, client, w, h, js, bg);
    // println!("after create browser");

    // let browser_host = get_browser_host(browser);
    // unsafe {
    //     (*browser_host).show_dev_tools.expect("no dev_tools")(browser_host, std::ptr::null_mut(), std::ptr::null_mut(), std::ptr::null_mut(), std::ptr::null_mut());
    // }
    browser
}

#[no_mangle]
pub extern fn cefswt_set_window_info_parent(window_info: *mut cef::_cef_window_info_t, client: *mut *mut cef::_cef_client_t, jclient: &mut cef::_cef_client_t, hwnd: *mut c_void, x: c_int, y: c_int, w: c_int, h: c_int) {
    unsafe {
        //println!("cefswt_set_window_info_parent {:?} {}", *window_info, hwnd);
        (*client) = jclient;
        app::set_window_parent(window_info, hwnd, x, y, w, h);
        //println!("after cefswt_set_window_info_parent {:?} {}", *window_info, hwnd);
    };
}

#[no_mangle]
pub extern fn cefswt_do_message_loop_work() -> c_int {
    let result = std::panic::catch_unwind(|| {
        unsafe { cef::cef_do_message_loop_work() };
    });
    match result {
        Ok(_) => 1,
        Err(_) => 0
    }
}

#[no_mangle]
pub extern fn cefswt_free(obj: *mut cef::cef_browser_t) {
    //println!("freeing {:?}", obj);
    unsafe {
        assert_eq!((*obj).base.size, std::mem::size_of::<cef::_cef_browser_t>());

        // let rls_fn = (*obj).base.release.expect("null release");
        // println!("call rls");
        // let refs = rls_fn(obj as *mut cef::_cef_base_ref_counted_t);
        // assert_eq!(refs, 1);
    }

    //println!("freed");
}

#[no_mangle]
pub extern fn cefswt_get_id(browser: *mut cef::cef_browser_t) -> c_int {
    unsafe {
        let get_id = (*browser).get_identifier.unwrap();
        get_id(browser)
    }
}

#[no_mangle]
pub extern fn cefswt_resized(browser: *mut cef::cef_browser_t, width: i32, height: i32) {
    //println!("Calling resized {}:{}", width, height);

    let browser_host = get_browser_host(browser);
    let get_window_handle_fn = unsafe { (*browser_host).get_window_handle.expect("no get_window_handle") };
    let win_handle = unsafe { get_window_handle_fn(browser_host) };
    do_resize(win_handle, width, height);
}

#[cfg(target_os = "linux")]
fn do_resize(win_handle: *mut c_void, width: i32, height: i32) {
    use x11::xlib;

    let xwindow = win_handle;
    let xdisplay = unsafe { cef::linux::cef_get_xdisplay() };
    let mut changes = xlib::XWindowChanges {
        x: 0,
        y: 0,
        width,
        height,
        border_width: 0,
        sibling: 0,
        stack_mode: 0
    };
    unsafe { xlib::XConfigureWindow(std::mem::transmute(xdisplay), xwindow as u64,
        (xlib::CWX | xlib::CWY | xlib::CWHeight | xlib::CWWidth) as u32, &mut changes) };
}

#[cfg(target_os = "macos")]
fn do_resize(_win_handle: *mut c_void, _: i32, _: i32) {
    // handled by cocoa
}

#[cfg(target_family = "windows")]
fn do_resize(win_handle: *mut c_void, width: i32, height: i32) {
    extern crate winapi;

    let x = 0;
    let y = 0;
    unsafe { winapi::um::winuser::SetWindowPos(win_handle as winapi::shared::windef::HWND,
        std::ptr::null_mut(), x, y, width, height, winapi::um::winuser::SWP_NOZORDER) };
}

#[no_mangle]
pub extern fn cefswt_close_browser(browser: *mut cef::cef_browser_t, force: c_int) {
    let browser_host = get_browser_host(browser);
    let close_fn = unsafe { (*browser_host).close_browser.expect("null close_browser") };
    unsafe { close_fn(browser_host, force) };
}

#[no_mangle]
pub extern fn cefswt_load_url(browser: *mut cef::cef_browser_t, url: *const c_char, post_bytes: *const c_void, post_size: usize, headers: *const c_char, headers_size: usize) {
    let url = utils::str_from_c(url);
    let url_cef = utils::cef_string(url);
    //println!("url: {:?}", url);
    unsafe {
        let get_frame = (*browser).get_main_frame.expect("null get_main_frame");
        let main_frame = get_frame(browser);
        if post_bytes.is_null() && headers.is_null() {
            (*main_frame).load_url.unwrap()(main_frame, &url_cef);
        } else {
            let request = cef::cef_request_create();
            (*request).set_url.unwrap()(request, &url_cef);
            if !post_bytes.is_null() {
                let post_data = cef::cef_post_data_create();
                let post_element = cef::cef_post_data_element_create();
                (*post_element).set_to_bytes.unwrap()(post_element, post_size, post_bytes);
                (*post_data).add_element.unwrap()(post_data, post_element);

                (*request).set_post_data.unwrap()(request, post_data);
            }

            if !headers.is_null() {
                let map = cef::cef_string_multimap_alloc();

                let headers = utils::str_from_c(headers);
                let headers: Vec<&str> = headers.splitn(headers_size, "::").collect();
                for i in 0..headers_size {
                    // let header = headers.wrapping_add(i);
                    // let ptr = header.read();

                    // let header_str = utils::str_from_c(ptr);
                    let header_str = headers[i];
                    let header: Vec<&str> = header_str.splitn(2, ':').collect();
                    let key = header[0].trim();
                    let value = header[1].trim();
                    let key = utils::cef_string(key);
                    let value = utils::cef_string(value);

                    cef::cef_string_multimap_append(map, &key, &value);
                }
                (*request).set_header_map.unwrap()(request, map);
            }

            (*main_frame).load_request.unwrap()(main_frame, request);
        }
    }
}

#[no_mangle]
pub extern fn cefswt_get_url(browser: *mut cef::cef_browser_t) -> *mut c_char {
    let get_frame = unsafe { (*browser).get_main_frame.expect("null get_main_frame") };
    let main_frame = unsafe { get_frame(browser) };
    assert!(!main_frame.is_null());
    let get_url = unsafe { (*main_frame).get_url.expect("null get_url") };
    let url = unsafe { get_url(main_frame) };
    utils::cstr_from_cef(url)
}

#[no_mangle]
pub extern fn cefswt_cefstring_to_java(cefstring: *mut cef::cef_string_t) -> *const c_char {
    let r = utils::cstr_from_cef(cefstring);
    r
}

#[no_mangle]
pub extern fn cefswt_request_to_java(request: *mut cef::cef_request_t) -> *mut c_char {
    let url = unsafe { (*request).get_url.expect("null get_url")(request) };
    let cstr = utils::cstr_from_cef(url);
    unsafe { cef::cef_string_userfree_utf16_free(url) };
    cstr
}

#[no_mangle]
pub extern fn cefswt_cookie_to_java(cookie: *mut cef::_cef_cookie_t) -> *mut c_char {
    let name = unsafe { (*cookie).name };
    let cstr = utils::cstr_from_cef(&name);
    cstr
}

#[no_mangle]
pub extern fn cefswt_load_text(browser: *mut cef::cef_browser_t, text: *const c_char) {
    let text = utils::str_from_c(text);
    let text_cef = utils::cef_string(text);
    let url_cef = utils::cef_string("http://text/");
    // println!("text: {:?}", text);
    let get_frame = unsafe { (*browser).get_main_frame.expect("null get_main_frame") };
    let main_frame = unsafe { get_frame(browser) };
    let load_string = unsafe { (*main_frame).load_string.expect("null load_string") };
    unsafe { load_string(main_frame, &text_cef, &url_cef) };
}

#[no_mangle]
pub extern fn cefswt_stop(browser: *mut cef::cef_browser_t) {
    unsafe { (*browser).stop_load.expect("null stop_load")(browser); };
}

#[no_mangle]
pub extern fn cefswt_reload(browser: *mut cef::cef_browser_t) {
    unsafe { (*browser).reload.expect("null reload")(browser); };
}

#[no_mangle]
pub extern fn cefswt_get_text(browser: *mut cef::cef_browser_t, visitor: *mut cef::_cef_string_visitor_t) {
    assert_eq!(unsafe{(*visitor).base.size}, std::mem::size_of::<cef::_cef_string_visitor_t>());
    let get_frame = unsafe { (*browser).get_main_frame.expect("null get_main_frame") };
    let main_frame = unsafe { get_frame(browser) };
    assert!(!main_frame.is_null());
    let get_text = unsafe { (*main_frame).get_source.expect("null get_text") };
    // println!("before get_text");
    unsafe { get_text(main_frame, visitor) };
    // println!("after get_text");
}

#[no_mangle]
pub extern fn cefswt_execute(browser: *mut cef::cef_browser_t, text: *const c_char) {
    let text = utils::str_from_c(text);
    let text_cef = utils::cef_string(text);
    let url_cef = utils::cef_string_empty();
    let get_frame = unsafe { (*browser).get_main_frame.expect("null get_main_frame") };
    let main_frame = unsafe { get_frame(browser) };
    let execute = unsafe { (*main_frame).execute_java_script.expect("null execute_java_script") };
    unsafe { execute(main_frame, &text_cef, &url_cef, 0) };
}

#[no_mangle]
pub extern fn cefswt_eval(browser: *mut cef::cef_browser_t, text: *const c_char, id: i32,
        callback: unsafe extern "system" fn(work: c_int, kind: c_int, value: *const c_char)) -> c_int {
    let text_cef = utils::cef_string_from_c(text);
    let name = utils::cef_string("eval");
    unsafe {
        let msg = cef::cef_process_message_create(&name);
        let args = (*msg).get_argument_list.unwrap()(msg);
        let s = (*args).set_int.unwrap()(args, 1, id);
        assert_eq!(s, 1);
        let s = (*args).set_string.unwrap()(args, 2, &text_cef);
        assert_eq!(s, 1);
        match socket::wait_response(browser, msg, args, cef::cef_process_id_t::PID_RENDERER, Some(callback)) {
            Ok(r) => {
                callback(0, r.kind as i32, r.str_value.as_ptr());
                1
            },
            Err(_e) => {
                //println!("Failed in socket server {:?}", e);
                0
            }
        }
    }
}

#[no_mangle]
pub extern fn cefswt_function(browser: *mut cef::cef_browser_t, name: *mut c_char, id: i32) -> c_int {
    let name_cef = utils::cef_string_from_c(name);
    let msg_name = utils::cef_string("function");
    unsafe {
        let msg = cef::cef_process_message_create(&msg_name);
        let args = (*msg).get_argument_list.unwrap()(msg);
        let s = (*args).set_int.unwrap()(args, 0, id);
        assert_eq!(s, 1);
        let s = (*args).set_string.unwrap()(args, 1, &name_cef);
        assert_eq!(s, 1);
        let sent = (*browser).send_process_message.unwrap()(browser, cef::cef_process_id_t::PID_RENDERER, msg);
        assert_eq!(sent, 1);
        sent
    }
}

#[repr(C)]
#[derive(Debug)]
pub struct FunctionSt {
    pub id: i32,
    pub port: i32,
    pub args: usize,
}

#[no_mangle]
pub unsafe extern fn cefswt_function_id(message: *mut cef::cef_process_message_t, st: *mut FunctionSt) {
    let valid = (*message).is_valid.unwrap()(message);
    let name = (*message).get_name.unwrap()(message);
    // let mut st = FunctionSt {id: -1, args: 0, port: 0};
    (*st).id = -1;
    (*st).args = 0;
    (*st).port = 0;
    if valid == 1 && cef::cef_string_utf16_cmp(&utils::cef_string("function_call"), name) == 0 {
        let args = (*message).get_argument_list.unwrap()(message);
        let args_len = (*args).get_size.unwrap()(args);
        let port = (*args).get_int.unwrap()(args, 0);
        // st = FunctionSt {
        //     id: (*args).get_int.unwrap()(args, 1),
        //     args: (args_len-1) / 2,
        //     port
        // };
        (*st).id = (*args).get_int.unwrap()(args, 1);
        (*st).args = (args_len-1) / 2;
        (*st).port = port; 
    }
    // let r = Box::new(st);
    // let r = Box::into_raw(r);
    // return r;
}

#[no_mangle]
pub unsafe extern fn cefswt_function_arg(message: *mut cef::cef_process_message_t, index: i32, callback: unsafe extern "system" fn(work: c_int, kind: c_int, value: *const c_char)) -> c_int {
    let args = (*message).get_argument_list.unwrap()(message);
    let kind = (*args).get_int.unwrap()(args, (1+index*2+1) as usize);
    let arg = (*args).get_string.unwrap()(args, (1+index*2+2) as usize);
    let cstr = utils::cstr_from_cef(arg);
    let kind = socket::ReturnType::from(kind);
    callback(0, kind as i32, cstr);
    1
}

#[no_mangle]
pub extern fn cefswt_function_return(_browser: *mut cef::cef_browser_t, _id: i32, port: i32, kind: socket::ReturnType, ret: *const c_char) -> c_int {
    let cstr = unsafe { std::ffi::CStr::from_ptr(ret) };
    let s = socket::socket_client(port as u16, cstr.to_owned(), kind);
    s
}

#[no_mangle]
pub extern fn cefswt_set_focus(browser: *mut cef::cef_browser_t, set: bool, parent: *mut c_void) {
    let browser_host = get_browser_host(browser);
    let focus_fn = unsafe { (*browser_host).set_focus.expect("null set_focus") };
    let focus = if set {
        1
    } else {
        0
    };
    //println!("<<<<<<<< set_focus {}", focus);
    unsafe { focus_fn(browser_host, focus) };
    if !set && !parent.is_null() {
        do_set_focus(parent, focus);
    }
}

#[cfg(target_os = "linux")]
fn do_set_focus(parent: *mut c_void, _focus: i32) {
    let root = unsafe { gtk::gtk_widget_get_toplevel(parent) };
    //println!("<<<<<<<< set_focus {} {:?} {:?}", focus, parent, root);
    // workaround to actually remove focus from cef inputs
    unsafe { gtk::gtk_window_present(root) };
}

#[cfg(target_family = "windows")]
fn do_set_focus(_parent: *mut c_void, _focus: i32) {
    // TODO
}

#[cfg(target_os = "macos")]
fn do_set_focus(_parent: *mut c_void, _focus: i32) {
    // handled by cocoa
}

#[no_mangle]
pub extern fn cefswt_is_same(browser: *mut cef::cef_browser_t, that: *mut cef::cef_browser_t) -> c_int {
    unsafe { (*browser).is_same.unwrap()(browser, that) }
}

#[no_mangle]
pub extern fn cefswt_dialog_close(callback: *mut cef::_cef_jsdialog_callback_t, success: c_int, prompt: *mut cef::cef_string_t) {
    unsafe { (*callback).cont.unwrap()(callback, success, prompt) };
}

#[no_mangle]
pub extern fn cefswt_context_menu_cancel(callback: *mut cef::_cef_run_context_menu_callback_t) {
    unsafe { (*callback).cancel.unwrap()(callback) };
}

#[no_mangle]
pub extern fn cefswt_auth_callback(callback: *mut cef::_cef_auth_callback_t, juser: *const c_char, jpass: *const c_char, cont: c_int) {
    unsafe {
        if cont == 1 {
            let user = utils::cef_string_from_c(juser);
            let pass = utils::cef_string_from_c(jpass);
            (*callback).cont.unwrap()(callback, &user, &pass)
        } else {
            (*callback).cancel.unwrap()(callback) 
        }
    };
}

#[no_mangle]
pub extern fn cefswt_set_cookie(jurl: *const c_char, jname: *const c_char, jvalue: *const c_char, jdomain: *const c_char, jpath: *const c_char, secure: i32, httponly: i32, max_age: f64) -> c_int {
    let manager = unsafe { cef::cef_cookie_manager_get_global_manager(std::ptr::null_mut()) };
    let url = utils::cef_string_from_c(jurl);
    let domain = utils::cef_string_from_c(jdomain);
    let path = utils::cef_string_from_c(jpath);
    let name = utils::cef_string_from_c(jname);
    let value = utils::cef_string_from_c(jvalue);
    let has_expires = if max_age == -1.0 {
        0
    } else {
        1
    };
    let mut expires = cef::cef_time_t { year: 0, month: 0, day_of_week: 0, day_of_month: 0, hour: 0, minute: 0, second: 0, millisecond: 0 };

    if max_age == -1.0 {
        unsafe { cef::cef_time_from_doublet(max_age, &mut expires) };
    }

    let cookie = cef::_cef_cookie_t {
        name,
        value,
        domain,
        path,
        secure,
        httponly,
        has_expires,
        expires,
        creation: expires,
        last_access: expires
    };
    unsafe { (*manager).set_cookie.expect("null set_cookie")(manager, &url, &cookie, std::ptr::null_mut()) }
}

#[no_mangle]
pub extern fn cefswt_get_cookie(jurl: *const c_char, jvisitor: *mut cef::_cef_cookie_visitor_t) -> c_int {
    let manager = unsafe { cef::cef_cookie_manager_get_global_manager(std::ptr::null_mut()) };
    let url = utils::cef_string_from_c(jurl);

    unsafe { (*manager).visit_url_cookies.expect("null visit_url_cookies")(manager, &url, 1, jvisitor) }
}

#[no_mangle]
pub extern fn cefswt_cookie_value(cookie: *mut cef::_cef_cookie_t) -> *mut c_char {
    unsafe { utils::cstr_from_cef(&mut (*cookie).value) }
}

#[no_mangle]
pub extern fn cefswt_delete_cookies() {
    let manager = unsafe { cef::cef_cookie_manager_get_global_manager(std::ptr::null_mut()) };
    unsafe { (*manager).delete_cookies.expect("null delete_cookies")(manager, std::ptr::null_mut(), std::ptr::null_mut(), std::ptr::null_mut()) };
}

#[no_mangle]
pub extern fn cefswt_shutdown() {
    //println!("r: Calling cef_shutdown");
    // Shut down CEF.
    unsafe { cef::cef_shutdown() };
    // println!("r: After Calling cef_shutdown");
}

fn get_browser_host(browser: *mut cef::cef_browser_t) -> *mut cef::_cef_browser_host_t {
    let get_host_fn = unsafe { (*browser).get_host.expect("null get_host") };
    let browser_host = unsafe { get_host_fn(browser) };
    browser_host
}

#[no_mangle]
pub extern fn cefswt_is_main_frame(frame: *mut cef::_cef_frame_t) -> i32 {
    unsafe { (*frame).is_main.expect("null is_main")(frame) }
}

#[no_mangle]
pub extern fn cefswt_go_forward(browser: *mut cef::_cef_browser_t) {
    unsafe { (*browser).go_forward.expect("null go_forward")(browser) };
}

#[no_mangle]
pub extern fn cefswt_go_back(browser: *mut cef::_cef_browser_t) {
    unsafe { (*browser).go_back.expect("null go_back")(browser) };
}