/*******************************************************************************
 * Copyright (c) 2020, 2024 Red Hat Inc. and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc. - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal;

import java.lang.reflect.*;
import java.util.function.*;

import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.widgets.*;

/**
 * This class is an internal use utilities class introduced during the port
 * from GTK3 to GTK4. This class transforms a non-blocking show dialog call
 * to a blocking call.  See bug 567371 for more information and where this
 * is applied.
 */

public class SyncDialogUtil {
	static int responseID;
	static Callback dialogResponseCallback;
	static Function<Long, Long> dialogAsyncFinish;
	static Long dialogAsyncValue;

	/**
	 * This method implements the {@code AsyncReadyCallback} mechanism that is
	 * used in GTK4. Most operations within GTK4 are executed asynchronously,
	 * where the user is given the option to respond to the completion of such
	 * an operation via a callback method, in order to e.g. process the result
	 * or to apply additional cleanup tasks.<br>
	 * When calling this method, the asynchronous operation is initiated via the
	 * {code asyncOpen} parameter. Callers have to ensure that the callback
	 * address is used as argument for the {@code AsyncReadyCallback} parameter.
	 * From within the callback routine, the {@code asyncFinish} function is
	 * called, receiving the {@code AsyncResult} of the callback as argument and
	 * returning the {@code long} value of the callback function.<br>
	 * This method blocks until the callback method has been called. It is
	 * therefore essential that callers use the address of the {@link Callback}
	 * as address for the {@code AsyncReadyCallback} object.
	 */
	static public long run(Display display, Consumer<Long> asyncOpen, Function<Long, Long> asyncFinish) {
		initializeResponseCallback();

		dialogAsyncFinish = asyncFinish;
		asyncOpen.accept(dialogResponseCallback.getAddress());

		while (!display.isDisposed()) {
			if (dialogAsyncValue != null) {
				break;
			}
			display.readAndDispatch();
		}

		disposeResponseCallback();
		return dialogAsyncValue;
	}

	/**
	 * A blocking call that waits for the handling of the signal before returning
	 *
	 * @return the response_id from the dialog presented to the user
	 */
	static public int run(Display display, long handle, boolean isNativeDialog) {
		initializeResponseCallback();
		OS.g_signal_connect(handle, OS.response, dialogResponseCallback.getAddress(), 0);
		if (isNativeDialog) {
			GTK.gtk_native_dialog_show(handle);
		} else {
			GTK.gtk_widget_show(handle);
		}

		while (!display.isDisposed()) {
			boolean eventsDispatched = OS.g_main_context_iteration(0, false);
			if (responseID != -1) {
				break;
			} else if (!eventsDispatched) {
				display.sleep();
			}
		}

		disposeResponseCallback();
		return (int) responseID;
	}

	/**
	 * Initializes the response callback and resets the responseID of the dialog to the default value.
	 * This function should be called before connect the dialog to the "response" signal, as this sets up the callback.
	 */
	static void initializeResponseCallback() {
		dialogResponseCallback = new Callback(SyncDialogUtil.class, "dialogResponseProc", void.class, new Type[] {long.class, long.class, long.class});
		dialogAsyncValue = null;
		responseID = -1;
	}

	static void disposeResponseCallback() {
		dialogResponseCallback.dispose();
		dialogResponseCallback = null;
		dialogAsyncFinish = null;
	}

	/**
	 * Callback function for the "response" signal in GtkDialog widgets.
	 * Responsibility of destroying the dialog is the owner of the dialog
	 * handle.
	 *
	 * Note: Native dialogs are platform dialogs that don't use GtkDialog or GtkWindow.
	 */
	static void dialogResponseProc(long dialog, long response_id, long user_data) {
		if (dialogAsyncFinish != null) {
			dialogAsyncValue = dialogAsyncFinish.apply(response_id);
		}
		responseID = (int) response_id;
	}
}
