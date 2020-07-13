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
#[cfg(feature = "gen")]
extern crate bindgen;

#[cfg(debug_assertions)]
const CEF_TARGET: &'static str = "Debug"; 
#[cfg(not(debug_assertions))]
const CEF_TARGET: &'static str = "Release";

#[cfg(feature = "gen")]
fn main() {
  link();
  let cef_path = get_cef_path();
  gen_cef(cef_path.display());
  gen_os(cef_path.display());
}

#[cfg(not(any(feature = "gen")))]
fn main() {
  link();
}

fn get_cef_path() -> std::path::PathBuf {
  let cwd = std::env::current_dir().unwrap();
  let mut cef_path = cwd.clone();
  
  if cfg!(target_os = "macos") {
    cef_path.push("cef_macosx");
  } 
  else if cfg!(target_os = "linux") {
    cef_path.push("cef_linux");
  } 
  else if cfg!(target_os = "windows") {
    if std::env::var("CARGO_CFG_TARGET_ARCH").unwrap() == "x86" {
      cef_path.push("cef_win32_x86");
    } else {
    cef_path.push("cef_win32");
  }
  }
  cef_path
}

fn link() {
  let cef_path = get_cef_path();

  if !cef_path.exists() {
    panic!("cargo:warning=Extract and rename cef binary (minimal) distro to {:?}", cef_path);
  }

  if cfg!(target_os = "linux") {
    // println!("cargo:rustc-link-lib=gtk-x11-2.0");
    // println!("cargo:rustc-link-lib=gdk-x11-2.0");
    // println!("cargo:rustc-link-lib=gtk-3.so.0");
    println!("cargo:rustc-link-lib=X11");
  }

  // Tell cargo to tell rustc to link the system shared library.
  let mut cef_bin = cef_path.clone();
  cef_bin.push(CEF_TARGET);
  let lib = if cfg!(target_os = "windows") {
    println!("cargo:rustc-link-search={}", cef_bin.display()); 
    "libcef" 
  } else if cfg!(target_os = "macos") {
    println!("cargo:rustc-link-search=framework={}", cef_bin.display());
    "framework=Chromium Embedded Framework"
  } else { 
    println!("cargo:rustc-link-search={}", cef_bin.display());
    "cef" 
  };
  println!("cargo:rustc-link-lib={}", lib);
}

#[cfg(feature = "gen")]
#[cfg(target_os = "windows")]
fn gen_os(cef_path: std::path::Display) {
  let _ = generator(cef_path)
    .header("cef_win.h")
    .whitelist_type("_cef_main_args_t")
    .whitelist_type("_cef_window_info_t")
    .blacklist_type("wchar_t")
    .blacklist_type("char16")
    .blacklist_type(".*string.*")
    .raw_line("use cef::cef_string_t;")
    .generate().expect("Failed to gencef win")
    .write_to_file(std::path::Path::new("src").join("cef").join("win.rs"));
}

#[cfg(feature = "gen")]
#[cfg(target_os = "linux")]
fn gen_os(cef_path: std::path::Display) {
  let _ = generator(cef_path)
    .header("cef_linux.h")
    .whitelist_type("_cef_main_args_t")
    .whitelist_type("_cef_window_info_t")
    .whitelist_function("cef_get_xdisplay")
    .generate().expect("Failed to gencef linux")
    .write_to_file(std::path::Path::new("src").join("cef").join("linux.rs"));
}

#[cfg(feature = "gen")]
#[cfg(target_os = "macos")]
fn gen_os(cef_path: std::path::Display) {
  let _ = generator(cef_path)
    .header("cef_mac.h")
    .whitelist_type("_cef_main_args_t")
    .whitelist_type("_cef_window_info_t")
    .blacklist_type(".*string.*")
    .blacklist_type("char16")
    .raw_line("use cef::cef_string_t;")
    .generate().expect("Failed to gencef mac")
    .write_to_file(std::path::Path::new("src").join("cef").join("mac.rs"));
}

#[cfg(feature = "gen")]
fn gen_cef(cef_path: std::path::Display) {
  use std::io::Write;
  #[cfg(target_os = "windows")] let gen = generator(cef_path).header("include/internal/cef_types_win.h");
  #[cfg(target_os = "linux")] let gen = generator(cef_path).header("include/internal/cef_types_linux.h");
  #[cfg(target_os = "macos")] let gen = generator(cef_path).header("include/internal/cef_types_mac.h");
  let generated = gen
    .header("cef.h")
    .whitelist_type("cef_string_t")
    .whitelist_type("cef_string_userfree_t")
    .whitelist_type(".*cef_base_t")
    .whitelist_type("_cef_scheme_registrar_t")
    .whitelist_type("_cef_.*_handler_t")
    .whitelist_type("_cef_urlrequest_client_t")
    .whitelist_type("_cef_urlrequest_t")
    .whitelist_type("cef_window_handle_t")
    .whitelist_function("cef_string_.*")
    .whitelist_function("cef_execute_process")
    .whitelist_function("cef_initialize")
    .whitelist_function("cef_run_message_loop")
    .whitelist_function("cef_shutdown")
    .whitelist_function("cef_browser_host_create_browser")
    .whitelist_function("cef_urlrequest_create")
    .whitelist_function("cef_cookie_manager_get_global_manager")
    .whitelist_function("cef_.*")
    .blacklist_type("_cef_main_args_t")
    .blacklist_type("_cef_window_info_t")
    .blacklist_type("(__)?time(64)?_t")
    .blacklist_type("wchar_t")
    .blacklist_type("char16")
    .blacklist_type("u?int64")
    .blacklist_type("DWORD")
    .blacklist_type("HWND.*")
    .blacklist_type("HINSTANCE.*")
    .blacklist_type("HMENU.*")
    .blacklist_type("HICON.*")
    .blacklist_type("HCURSOR.*")
    .blacklist_type("POINT")
    .blacklist_type("MSG")
    .blacklist_type("tagMSG")
    .blacklist_type("tagPOINT")
    .blacklist_type(".*XDisplay")
    .blacklist_type("VisualID")
    .blacklist_type(".*XEvent")
    .raw_line("#[cfg(target_os = \"linux\")] pub mod linux;")
    .raw_line("#[cfg(target_os = \"linux\")] pub use self::linux::_cef_window_info_t;")
    .raw_line("#[cfg(target_os = \"linux\")] pub use self::linux::_cef_main_args_t;")
    .raw_line("#[cfg(target_os = \"linux\")] pub type wchar_t = i32;")
    .raw_line("#[cfg(target_os = \"linux\")] pub type char16 = i32;")
    .raw_line("#[cfg(target_os = \"linux\")] pub type time_t = isize;")
    .raw_line("#[cfg(target_os = \"linux\")] pub type int64 = ::std::os::raw::c_longlong;")
    .raw_line("#[cfg(target_os = \"linux\")] pub type uint64 = ::std::os::raw::c_ulonglong;")
    .raw_line("#[cfg(target_os = \"macos\")] pub mod mac;")
    .raw_line("#[cfg(target_os = \"macos\")] pub use self::mac::_cef_window_info_t;")
    .raw_line("#[cfg(target_os = \"macos\")] pub use self::mac::_cef_main_args_t;")
    .raw_line("#[cfg(target_os = \"macos\")] pub type wchar_t = i32;")
    .raw_line("#[cfg(target_os = \"macos\")] pub type char16 = u16;")
    .raw_line("#[cfg(target_os = \"macos\")] pub type time_t = i64;")
    .raw_line("#[cfg(target_os = \"macos\")] pub type int64 = ::std::os::raw::c_longlong;")
    .raw_line("#[cfg(target_os = \"macos\")] pub type uint64 = ::std::os::raw::c_ulonglong;")
    .raw_line("#[cfg(windows)] pub mod win;")
    .raw_line("#[cfg(windows)] pub use self::win::_cef_window_info_t;")
    .raw_line("#[cfg(windows)] pub use self::win::_cef_main_args_t;")
    .raw_line("#[cfg(windows)] pub type wchar_t = u16;")
    .raw_line("#[cfg(windows)] pub type char16 = u16;")
    .raw_line("#[cfg(windows)] pub type time_t = i64;")
    .raw_line("#[cfg(windows)] pub type int64 = ::std::os::raw::c_longlong;")
    .raw_line("#[cfg(windows)] pub type uint64 = ::std::os::raw::c_ulonglong;")
    .generate().expect("Failed to gencef")
    .to_string();
    // .write_to_file(std::path::Path::new("src").join("cef").join("mod.rs"));
    let new_data = generated.replace("\"stdcall\"", "\"system\"");

    // Recreate the file and dump the processed contents to it
    let mut dst = std::fs::File::create(std::path::Path::new("src").join("cef").join("mod.rs")).expect("Cannot create mod.rs file");
    dst.write(new_data.as_bytes()).expect("Cannot write mod.rs");
}

#[cfg(feature = "gen")]
fn generator(cef_path: std::path::Display) -> bindgen::Builder {
  let mut config = bindgen::CodegenConfig::FUNCTIONS;
  config.insert(bindgen::CodegenConfig::TYPES);
  let gen = bindgen::builder()
    .clang_arg(format!("-I{}", cef_path))
    .clang_arg(format!("-I{}", "C:\\Program Files (x86)\\Microsoft SDKs\\Windows\\v7.1A\\Include"))
    .clang_arg("-fparse-all-comments")
    .clang_arg("-Wno-nonportable-include-path")
    .clang_arg("-Wno-invalid-token-paste")
    // .link("cef")
    //.use_core()
    .with_codegen_config(config)
    .rustified_enum(".*")
    .rustfmt_bindings(true)
    .derive_debug(true)
    .trust_clang_mangling(false)
    .layout_tests(false)
    .raw_line("#![allow(dead_code)]")
    .raw_line("#![allow(non_snake_case)]")
    .raw_line("#![allow(non_camel_case_types)]")
    .raw_line("#![allow(non_upper_case_globals)]");
  gen
} 
