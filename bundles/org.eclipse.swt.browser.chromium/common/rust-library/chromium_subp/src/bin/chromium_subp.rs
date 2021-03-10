extern crate chromium;

use chromium::utils;
use chromium::cef;
use chromium::app;

use std::os::raw::c_int;
use std::ptr::null_mut;

fn subp() {
    //println!("IN SUBP");
    let main_args = utils::prepare_args();
    let mut app = app::App::new();
    //println!("Calling cef_execute_process");
    #[cfg(target_os = "windows")]
    unsafe {cef::cef_enable_highdpi_support()};
    let exit_code: c_int = unsafe { cef::cef_execute_process(&main_args, app.as_ptr(), null_mut()) };
    //println!("exiting subp with {}", exit_code);
    std::process::exit(exit_code);
}

fn main() {
    subp();
}
