/*******************************************************************************
 * Copyright (c) 2020, 2025 Red Hat Inc. and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.internal;

import java.lang.reflect.*;
import java.util.function.*;

import org.eclipse.swt.widgets.*;

/**
 * This class is an internal class based on {@link SyncDialogUtil} for the
 * non-UI and reentrant parts of GTK4, such as the clipboard.
 *
 * TODO should "org.eclipse.swt.internal.gtk.dispatchEvent" be set on display so
 * that (very?) unrelated events aren't processed until we are done here? We
 * need to process some unrelated events because one async operation could be
 * writing to a stream another is reading from and they both need to run
 * "simultaneously" for the operation to succeed.
 */

public class SyncFinishUtil<T> {
	private Callback dialogResponseCallback;
	private Function<Long, T> dialogAsyncFinish;
	private T dialogAsyncValue;
	private boolean dialogAsyncValueSet;

	/**
	 * This method implements the {@code AsyncReadyCallback} mechanism that is used
	 * in GTK4. Most operations within GTK4 are executed asynchronously, where the
	 * user is given the option to respond to the completion of such an operation
	 * via a callback method, in order to e.g. process the result or to apply
	 * additional cleanup tasks.<br>
	 * When calling this method, the asynchronous operation is initiated via the
	 * {code asyncOpen} parameter. Callers have to ensure that the callback address
	 * is used as argument for the {@code AsyncReadyCallback} parameter. From within
	 * the callback routine, the {@code asyncFinish} function is called, receiving
	 * the {@code AsyncResult} of the callback as argument and returning the
	 * {@code long} value of the callback function.<br>
	 * This method blocks until the callback method has been called. It is therefore
	 * essential that callers use the address of the {@link Callback} as address for
	 * the {@code AsyncReadyCallback} object.
	 */
	public T run(Display display, SyncFinishCallback<T> callback) {
		System.out.println("Running SyncFinishUtil " + this.hashCode());
		initializeResponseCallback();

		dialogAsyncFinish = callback::await;
		System.out.println("Running SyncFinishUtil await " + this.hashCode());
		callback.async(dialogResponseCallback.getAddress());

		System.out.println("Running SyncFinishUtil readAndDispatch " + this.hashCode());
		while (!display.isDisposed()) {
			if (dialogAsyncValueSet) {
				break;
			}
			display.readAndDispatch();
		}

		disposeResponseCallback();
		System.out.println("Running SyncFinishUtil FINISHED " + this.hashCode());
		return dialogAsyncValue;
	}

	/**
	 * Initializes the response callback and resets the responseID of the dialog to
	 * the default value. This function should be called before connect the dialog
	 * to the "response" signal, as this sets up the callback.
	 */
	private void initializeResponseCallback() {
		dialogResponseCallback = new Callback(this, "dialogResponseProc", void.class,
				new Type[] { long.class, long.class, long.class });
		dialogAsyncValue = null;
		dialogAsyncValueSet = false;
	}

	private void disposeResponseCallback() {
		dialogResponseCallback.dispose();
		dialogResponseCallback = null;
		dialogAsyncFinish = null;
	}

	/**
	 * Callback function for the "response" signal in GtkDialog widgets.
	 * Responsibility of destroying the dialog is the owner of the dialog handle.
	 *
	 * Note: Native dialogs are platform dialogs that don't use GtkDialog or
	 * GtkWindow.
	 */
	void dialogResponseProc(long dialog, long response, long user_data) {
		System.out.println("Running SyncFinishUtil dialogResponseProc " + this.hashCode());
		if (dialogAsyncFinish != null) {
			System.out.println("Running SyncFinishUtil apply " + this.hashCode());
			dialogAsyncValue = dialogAsyncFinish.apply(response);
			System.out.println("Running SyncFinishUtil dialogAsyncValueSet " + this.hashCode());
			dialogAsyncValueSet = true;
		}
	}
}
