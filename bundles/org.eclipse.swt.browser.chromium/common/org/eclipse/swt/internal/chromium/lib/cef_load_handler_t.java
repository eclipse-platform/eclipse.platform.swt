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
/// Implement this structure to handle events related to browser load status. The
/// functions of this structure will be called on the browser process UI thread
/// or render process main thread (TID_RENDERER).
///
public class cef_load_handler_t {
	///
	/// Base structure.
	///
	public cef_base_ref_counted_t base;
	///
	/// Called when the loading state has changed. This callback will be executed
	/// twice -- once when loading is initiated either programmatically or by user
	/// action, and once when loading is terminated due to completion, cancellation
	/// of failure. It will be called before any calls to OnLoadStart and after all
	/// calls to OnLoadError and/or OnLoadEnd.
	///
	/** @field cast=(void*) */
	public long on_loading_state_change;
	///
	/// Called after a navigation has been committed and before the browser begins
	/// loading contents in the frame. The |frame| value will never be NULL -- call
	/// the is_main() function to check if this frame is the main frame.
	/// |transition_type| provides information about the source of the navigation
	/// and an accurate value is only available in the browser process. Multiple
	/// frames may be loading at the same time. Sub-frames may start or continue
	/// loading after the main frame load has ended. This function will not be
	/// called for same page navigations (fragments, history state, etc.) or for
	/// navigations that fail or are canceled before commit. For notification of
	/// overall browser load status use OnLoadingStateChange instead.
	///
	/** @field cast=(void*) */
	public long on_load_start;
	///
	/// Called when the browser is done loading a frame. The |frame| value will
	/// never be NULL -- call the is_main() function to check if this frame is the
	/// main frame. Multiple frames may be loading at the same time. Sub-frames may
	/// start or continue loading after the main frame load has ended. This
	/// function will not be called for same page navigations (fragments, history
	/// state, etc.) or for navigations that fail or are canceled before commit.
	/// For notification of overall browser load status use OnLoadingStateChange
	/// instead.
	///
	/** @field cast=(void*) */
	public long on_load_end;
	///
	/// Called when a navigation fails or is canceled. This function may be called
	/// by itself if before commit or in combination with OnLoadStart/OnLoadEnd if
	/// after commit. |errorCode| is the error code number, |errorText| is the
	/// error text and |failedUrl| is the URL that failed to load. See
	/// net\base\net_error_list.h for complete descriptions of the error codes.
	///
	/** @field cast=(void*) */
	public long on_load_error;

	/** @field flags=no_gen */
	public long ptr;
	/** @field flags=no_gen */
	public Callback on_loading_state_change_cb;

	public static final int sizeof = ChromiumLib.cef_load_handler_t_sizeof();

}