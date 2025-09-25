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
 * The run method is non-blocking here!
 */

public class AsyncFinishUtil {
	private Callback dialogResponseCallback;
	private Function<Long, Long> dialogAsyncFinish;
	private Long dialogAsyncValue;

	/**
	 * TODO: Write new javadoc here
	 */
	public void run(Display display, AsyncReadyCallback callback) {
		initializeResponseCallback();

		dialogAsyncFinish = callback::await;
		callback.async(dialogResponseCallback.getAddress());
	}

	/**
	 * Returns null until await (aka finish) method is called.
	 * Once await is finished, will have non-null return value from finish method
	 */
	public Long getDialogAsyncValue() {
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
	}

	private void disposeResponseCallback() {
		dialogResponseCallback.dispose();
		dialogResponseCallback = null;
		dialogAsyncFinish = null;
	}

	void dialogResponseProc(long dialog, long response, long user_data) {
		if (dialogAsyncFinish != null) {
			dialogAsyncValue = dialogAsyncFinish.apply(response);
		}
		disposeResponseCallback();
	}
}
