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
package org.eclipse.swt.internal.chromium.lib;

import org.eclipse.swt.internal.*;

///
/// Structure used to implement browser process callbacks. The functions of this
/// structure will be called on the browser process main thread unless otherwise
/// indicated.
///
public class cef_browser_process_handler_t {
	///
	/// Base structure.
	///
	public cef_base_ref_counted_t base;
	///
	/// Called on the browser process UI thread immediately after the CEF context
	/// has been initialized.
	///
	/** @field cast=(void*) */
	public long on_context_initialized;
	///
	/// Called before a child process is launched. Will be called on the browser
	/// process UI thread when launching a render process and on the browser
	/// process IO thread when launching a GPU or plugin process. Provides an
	/// opportunity to modify the child process command line. Do not keep a
	/// reference to |command_line| outside of this function.
	///
	/** @field cast=(void*) */
	public long on_before_child_process_launch;
	///
	/// Called on the browser process IO thread after the main thread has been
	/// created for a new render process. Provides an opportunity to specify extra
	/// information that will be passed to
	/// cef_render_process_handler_t::on_render_thread_created() in the render
	/// process. Do not keep a reference to |extra_info| outside of this function.
	///
	/** @field cast=(void*) */
	public long on_render_process_thread_created;
	///
	/// Return the handler for printing on Linux. If a print handler is not
	/// provided then printing will not be supported on the Linux platform.
	///
	/** @field cast=(void*) */
	public long get_print_handler;
	///
	/// Called from any thread when work has been scheduled for the browser process
	/// main (UI) thread. This callback is used in combination with CefSettings.
	/// external_message_pump and cef_do_message_loop_work() in cases where the CEF
	/// message loop must be integrated into an existing application message loop
	/// (see additional comments and warnings on CefDoMessageLoopWork). This
	/// callback should schedule a cef_do_message_loop_work() call to happen on the
	/// main (UI) thread. |delay_ms| is the requested delay in milliseconds. If
	/// |delay_ms| is <= 0 then the call should happen reasonably soon. If
	/// |delay_ms| is > 0 then the call should be scheduled to happen after the
	/// specified delay and any currently pending scheduled call should be
	/// cancelled.
	///
	/** @field cast=(void*) */
	public long on_schedule_message_pump_work;

	/** @field flags=no_gen */
	public long ptr;
	/** @field flags=no_gen */
	public Callback on_schedule_message_pump_work_cb;

	public static final int sizeof = ChromiumLib.cef_browser_process_handler_t_sizeof();

}
