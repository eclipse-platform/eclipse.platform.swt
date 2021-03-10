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
use std::os::raw::c_char;
use std::ffi::CStr;
#[cfg(windows)]
extern crate winapi;

pub fn subp_path(cwd: &::std::path::Path, version: &str) -> String {
    let subp_path = if cfg!(target_os = "windows") { 
        cwd.join(format!("chromium_subp-{}.exe", version))
    } else if cfg!(target_os = "macos") {
        cwd.join(format!("chromium_subp-{}.app/Contents/MacOS/chromium_subp", version))
    } else { 
        cwd.join(format!("chromium_subp-{}", version)) 
    };
    let subp = subp_path.to_str().unwrap();
    //println!("subp: {:?}", subp);
    String::from(subp)
}

#[cfg(unix)]
pub fn prepare_args() -> cef::_cef_main_args_t {
    use std::ffi::CString;
    let mut args: Vec<*mut c_char> = ::std::env::args().map(|arg| {
        // println!("arg: {:?}", arg);
        let carg_rslt = CString::new(arg);
        let carg = carg_rslt.expect("cant create arg");
        carg.into_raw()
    }).collect();
    if cfg!(target_os = "macos") {
        let carg = CString::new("--disable-gpu-compositing").expect("cant create arg");
        let mp = carg.into_raw();
        args.push(mp);
        let carg = CString::new("--disable-accelerated-2d-canvas").expect("cant create arg");
        let mp = carg.into_raw();
        args.push(mp);
        // println!("Force --disable-gpu-compositing");
    } else {
        let carg = CString::new("--enable-webgl-draft-extensions").expect("cant create arg");
        let mp = carg.into_raw();
        args.push(mp);
    }

    let args_size = args.len() as i32;
    let args_ptr = args.as_mut_ptr();
    ::std::mem::forget(args);

    let main_args = cef::_cef_main_args_t {
        argc : args_size,
        argv : args_ptr
    };
    // println!("Hello CEF, ARGS: {:?}", main_args.argc);

    main_args
}

#[cfg(windows)]
pub fn prepare_args() -> cef::_cef_main_args_t {
    let h_instance = unsafe { winapi::um::libloaderapi::GetModuleHandleA(0 as winapi::um::winnt::LPCSTR) };
    let main_args = cef::_cef_main_args_t {
        instance: unsafe { ::std::mem::transmute(h_instance) }
        //instance: unsafe { std::mem::transmute(0 as i64) }
    };
    // println!("Hello CEF, hinstance: {:?}", main_args.instance);
    main_args
}

pub fn cef_string(value: &str) -> cef::cef_string_t {
    let mut str_cef = cef::cef_string_t {str: ::std::ptr::null_mut(), length: 0, dtor: Option::Some(dtr)};
    //unsafe { cef::cef_string_utf16_set(value.as_ptr() as *mut cef::char16, value.len(), &mut str_cef, 1) };
    unsafe {cef::cef_string_utf8_to_utf16(value.as_ptr() as *mut c_char, value.len(), &mut str_cef);}
    str_cef
}

pub fn cef_string_from_c(cstr: *const c_char) -> cef::cef_string_t {
    if cstr.is_null() {
        cef_string_empty()
    } else {
        cef_string(str_from_c(cstr))
    }
}

pub fn cef_string_empty() -> cef::cef_string_t {
    let mut empty_str = cef::cef_string_t {
        str: ::std::ptr::null_mut(), 
        length: 0, 
        dtor: Option::Some(dtr)
    };
    
    let emp = "";
    //unsafe { cef::cef_string_utf16_set(emp.as_ptr() as *mut cef::char16, 0, &mut empty_str, 1) };
    unsafe { cef::cef_string_utf8_to_utf16(emp.as_ptr() as *mut c_char, 0, &mut empty_str);}

    empty_str
}

unsafe extern "C" fn dtr(_: *mut cef::char16) {
    // println!("DESTROY CEF_STRING");
}

pub fn str_from_c(cstr: *const c_char) -> &'static str {
    if cstr.is_null() {
        ""
    } else {
        let slice = unsafe { CStr::from_ptr(cstr) };
        ::std::str::from_utf8(slice.to_bytes()).expect("failed from_utf8")
    }
}

pub fn cstr_from_cef(cefstring: *const cef::cef_string_t) -> *mut c_char {
    if cefstring.is_null() {
        return ::std::ptr::null_mut();
    }
    let utf8 = unsafe { cef::cef_string_userfree_utf8_alloc() };
    unsafe { cef::cef_string_utf16_to_utf8((*cefstring).str, (*cefstring).length, utf8) };
    unsafe {(*utf8).str}
}