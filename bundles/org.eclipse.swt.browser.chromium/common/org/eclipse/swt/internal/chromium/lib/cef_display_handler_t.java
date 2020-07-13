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
/// Implement this structure to handle events related to browser display state.
/// The functions of this structure will be called on the UI thread.
///
public class cef_display_handler_t {
	///
	/// Base structure.
	///
	public cef_base_ref_counted_t base;
	///
	/// Called when a frame's address has changed.
	///
	/** @field cast=(void*) */
	public long on_address_change;
	///
	/// Called when the page title changes.
	///
	/** @field cast=(void*) */
	public long on_title_change;
	///
	/// Called when the page icon changes.
	///
	/** @field cast=(void*) */
	public long on_favicon_urlchange;
	///
	/// Called when web content in the page has toggled fullscreen mode. If
	/// |fullscreen| is true (1) the content will automatically be sized to fill
	/// the browser content area. If |fullscreen| is false (0) the content will
	/// automatically return to its original size and position. The client is
	/// responsible for resizing the browser if desired.
	///
	/** @field cast=(void*) */
	public long on_fullscreen_mode_change;
	///
	/// Called when the browser is about to display a tooltip. |text| contains the
	/// text that will be displayed in the tooltip. To handle the display of the
	/// tooltip yourself return true (1). Otherwise, you can optionally modify
	/// |text| and then return false (0) to allow the browser to display the
	/// tooltip. When window rendering is disabled the application is responsible
	/// for drawing tooltips and the return value is ignored.
	///
	/** @field cast=(void*) */
	public long on_tooltip;
	///
	/// Called when the browser receives a status message. |value| contains the
	/// text that will be displayed in the status message.
	///
	/** @field cast=(void*) */
	public long on_status_message;
	///
	/// Called to display a console message. Return true (1) to stop the message
	/// from being output to the console.
	///
	/** @field cast=(void*) */
	public long on_console_message;

	/** @field flags=no_gen */
	public long ptr;
	/** @field flags=no_gen */
	public Callback on_title_change_cb;
	/** @field flags=no_gen */
	public Callback on_address_change_cb;
	/** @field flags=no_gen */
	public Callback on_status_message_cb;

	public static final int sizeof = ChromiumLib.cef_display_handler_t_sizeof();
}
