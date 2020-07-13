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
/// Implement this structure to handle context menu events. The functions of this
/// structure will be called on the UI thread.
///
public class cef_context_menu_handler_t {
	///
	/// Base structure.
	///
	public cef_base_ref_counted_t base;
	///
	/// Called before a context menu is displayed. |params| provides information
	/// about the context menu state. |model| initially contains the default
	/// context menu. The |model| can be cleared to show no context menu or
	/// modified to show a custom menu. Do not keep references to |params| or
	/// |model| outside of this callback.
	///
	/** @field cast=(void*) */
	public long on_before_context_menu;
	///
	/// Called to allow custom display of the context menu. |params| provides
	/// information about the context menu state. |model| contains the context menu
	/// model resulting from OnBeforeContextMenu. For custom display return true
	/// (1) and execute |callback| either synchronously or asynchronously with the
	/// selected command ID. For default display return false (0). Do not keep
	/// references to |params| or |model| outside of this callback.
	///
	/** @field cast=(void*) */
	public long run_context_menu;
	///
	/// Called to execute a command selected from the context menu. Return true (1)
	/// if the command was handled or false (0) for the default implementation. See
	/// cef_menu_id_t for the command ids that have default implementations. All
	/// user-defined command ids should be between MENU_ID_USER_FIRST and
	/// MENU_ID_USER_LAST. |params| will have the same values as what was passed to
	/// on_before_context_menu(). Do not keep a reference to |params| outside of
	/// this callback.
	///
	/** @field cast=(void*) */
	public long on_context_menu_command;
	///
	/// Called when the context menu is dismissed irregardless of whether the menu
	/// was NULL or a command was selected.
	///
	/** @field cast=(void*) */
	public long on_context_menu_dismissed;

	/** @field flags=no_gen */
	public long ptr;
	/** @field flags=no_gen */
	public Callback run_context_menu_cb;

	public static final int sizeof = ChromiumLib.cef_context_menu_handler_t_sizeof();

}