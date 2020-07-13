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
/// Implement this structure to handle events related to JavaScript dialogs. The
/// functions of this structure will be called on the UI thread.
///
public class cef_jsdialog_handler_t {
	///
	/// Base structure.
	///
	public cef_base_ref_counted_t base;
	///
	/// Called to run a JavaScript dialog. If |origin_url| is non-NULL it can be
	/// passed to the CefFormatUrlForSecurityDisplay function to retrieve a secure
	/// and user-friendly display string. The |default_prompt_text| value will be
	/// specified for prompt dialogs only. Set |suppress_message| to true (1) and
	/// return false (0) to suppress the message (suppressing messages is
	/// preferable to immediately executing the callback as this is used to detect
	/// presumably malicious behavior like spamming alert messages in
	/// onbeforeunload). Set |suppress_message| to false (0) and return false (0)
	/// to use the default implementation (the default implementation will show one
	/// modal dialog at a time and suppress any additional dialog requests until
	/// the displayed dialog is dismissed). Return true (1) if the application will
	/// use a custom dialog or if the callback has been executed immediately.
	/// Custom dialogs may be either modal or modeless. If a custom dialog is used
	/// the application must execute |callback| once the custom dialog is
	/// dismissed.
	///
	/** @field cast=(void*) */
	public long on_jsdialog;
	///
	/// Called to run a dialog asking the user if they want to leave a page. Return
	/// false (0) to use the default dialog implementation. Return true (1) if the
	/// application will use a custom dialog or if the callback has been executed
	/// immediately. Custom dialogs may be either modal or modeless. If a custom
	/// dialog is used the application must execute |callback| once the custom
	/// dialog is dismissed.
	///
	/** @field cast=(void*) */
	public long on_before_unload_dialog;
	///
	/// Called to cancel any pending dialogs and reset any saved dialog state. Will
	/// be called due to events like page navigation irregardless of whether any
	/// dialogs are currently pending.
	///
	/** @field cast=(void*) */
	public long on_reset_dialog_state;
	///
	/// Called when the default implementation dialog is closed.
	///
	/** @field cast=(void*) */
	public long on_dialog_closed;

	/** @field flags=no_gen */
	public long ptr;
	/** @field flags=no_gen */
	public Callback on_jsdialog_cb;
	/** @field flags=no_gen */
	public Callback on_dialog_closed_cb;
	/** @field flags=no_gen */
	public Callback on_before_unload_dialog_cb;

	public static final int sizeof = ChromiumLib.cef_jsdialog_handler_t_sizeof();

	public static final int JSDIALOGTYPE_ALERT = 0;
	public static final int JSDIALOGTYPE_CONFIRM = 1;
	public static final int JSDIALOGTYPE_PROMPT = 2;

}