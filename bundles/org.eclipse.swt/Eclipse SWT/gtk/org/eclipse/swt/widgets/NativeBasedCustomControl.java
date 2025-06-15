/*******************************************************************************
 * Copyright (c) 2025 Vector Informatik GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.internal.gtk3.*;
import org.eclipse.swt.internal.gtk4.*;

abstract class NativeBasedCustomControl extends Control {

	long scrolledHandle;
	long imHandle, socketHandle;
	static final String NO_INPUT_METHOD = "org.eclipse.swt.internal.gtk.noInputMethod"; //$NON-NLS-1$

	protected NativeBasedCustomControl(Composite parent, int style) {
		super(parent, style);
	}

	boolean hasBorder() {
		return (style & SWT.BORDER) != 0;
	}

	@Override
	void createHandle(int index) {
		state |= HANDLE | CANVAS | CHECK_SUBWINDOW;
		boolean scrolled = isScrolled();
		if (!scrolled)
			state |= THEME_BACKGROUND;
		createCompositeHandle(index, true, scrolled || (style & SWT.BORDER) != 0);
	}

	protected boolean isScrolled() {
		return (style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0;
	}

	private void createCompositeHandle(int index, boolean fixed, boolean scrolled) {
		if (scrolled) {
			if (fixed) {
				fixedHandle = OS.g_object_new(display.gtk_fixed_get_type(), 0);
				if (fixedHandle == 0)
					error(SWT.ERROR_NO_HANDLES);
				if (!GTK.GTK4)
					GTK3.gtk_widget_set_has_window(fixedHandle, true);
			}

			long vadj = GTK.gtk_adjustment_new(0, 0, 100, 1, 10, 10);
			if (vadj == 0)
				error(SWT.ERROR_NO_HANDLES);
			long hadj = GTK.gtk_adjustment_new(0, 0, 100, 1, 10, 10);
			if (hadj == 0)
				error(SWT.ERROR_NO_HANDLES);

			if (GTK.GTK4) {
				scrolledHandle = GTK4.gtk_scrolled_window_new();
				GTK.gtk_scrolled_window_set_hadjustment(scrolledHandle, hadj);
				GTK.gtk_scrolled_window_set_vadjustment(scrolledHandle, vadj);
				GTK.gtk_widget_set_hexpand(scrolledHandle, true);
				GTK.gtk_widget_set_vexpand(scrolledHandle, true);
			} else {
				scrolledHandle = GTK3.gtk_scrolled_window_new(hadj, vadj);
			}
			if (scrolledHandle == 0)
				error(SWT.ERROR_NO_HANDLES);
		}

		handle = OS.g_object_new(display.gtk_fixed_get_type(), 0);
		if (handle == 0)
			error(SWT.ERROR_NO_HANDLES);

		if (GTK.GTK4) {
			GTK4.gtk_widget_set_focusable(handle, true);
		} else {
			GTK3.gtk_widget_set_has_window(handle, true);
		}
		GTK.gtk_widget_set_can_focus(handle, true);

		if ((style & SWT.EMBEDDED) == 0) {
			if ((state & CANVAS) != 0) {
				/* Prevent an input method context from being created for the Browser widget */
				if (display.getData(NO_INPUT_METHOD) == null) {
					imHandle = GTK.gtk_im_multicontext_new();
					if (imHandle == 0)
						error(SWT.ERROR_NO_HANDLES);
				}
			}
		}
		if (scrolled) {
			if (fixed) {
				if (GTK.GTK4) {
					OS.swt_fixed_add(fixedHandle, scrolledHandle);
				} else {
					GTK3.gtk_container_add(fixedHandle, scrolledHandle);
				}
			}
			if (GTK.GTK4) {
				GTK4.gtk_scrolled_window_set_child(scrolledHandle, handle);
			} else {
				/*
				 * Force the scrolledWindow to have a single child that is not scrolled
				 * automatically. Calling gtk_container_add() seems to add the child correctly
				 * but cause a warning.
				 */
				boolean warnings = display.getWarnings();
				display.setWarnings(false);
				GTK3.gtk_container_add(scrolledHandle, handle);
				display.setWarnings(warnings);
			}

			int hsp = (style & SWT.H_SCROLL) != 0 ? GTK.GTK_POLICY_ALWAYS : GTK.GTK_POLICY_NEVER;
			int vsp = (style & SWT.V_SCROLL) != 0 ? GTK.GTK_POLICY_ALWAYS : GTK.GTK_POLICY_NEVER;
			GTK.gtk_scrolled_window_set_policy(scrolledHandle, hsp, vsp);
			if (hasBorder()) {
				if (GTK.GTK4) {
					GTK4.gtk_scrolled_window_set_has_frame(scrolledHandle, true);
				} else {
					GTK3.gtk_scrolled_window_set_shadow_type(scrolledHandle, GTK.GTK_SHADOW_ETCHED_IN);
				}
			}
		}
		if ((style & SWT.EMBEDDED) != 0) {
			if (OS.isWayland()) {
				if (Device.DEBUG) {
					new SWTError(SWT.ERROR_INVALID_ARGUMENT,
							"SWT.EMBEDDED is currently not yet supported in Wayland. \nPlease "
									+ "refer to https://bugs.eclipse.org/bugs/show_bug.cgi?id=514487 for development status.")
							.printStackTrace();
				}
			} else {
				if (GTK.GTK4) {
					// From Emmanuele Bassi:
					// "[Embedding external windows is] not possible any more. Socket/plug were
					// available only on X11,
					// and foreign windowing system surfaces were a massive complication in the
					// backend code."
					if (Device.DEBUG) {
						new SWTError(SWT.ERROR_INVALID_ARGUMENT, "SWT.EMBEDDED is not supported for GTK >= 4.")
								.printStackTrace();
					}
				} else {
					socketHandle = GTK.gtk_socket_new();
					if (socketHandle == 0)
						error(SWT.ERROR_NO_HANDLES);
					GTK3.gtk_container_add(handle, socketHandle);
				}
			}
		}

		if (!GTK.GTK4) {
			if ((style & SWT.NO_REDRAW_RESIZE) != 0 && (style & SWT.RIGHT_TO_LEFT) == 0) {
				GTK3.gtk_widget_set_redraw_on_allocate(handle, false);
			}
			/*
			 * Bug in GTK. When a widget is double buffered and the back pixmap is null, the
			 * double buffer pixmap is filled with the background of the widget rather than
			 * the current contents of the screen. If nothing is drawn during an expose
			 * event, the pixels are altered. The fix is to clear double buffering when
			 * NO_BACKGROUND is set and DOUBLE_BUFFERED is not explicitly set.
			 */
			if ((style & SWT.DOUBLE_BUFFERED) == 0 && (style & SWT.NO_BACKGROUND) != 0) {
				GTK3.gtk_widget_set_double_buffered(handle, false);
			}
		}
	}

}
