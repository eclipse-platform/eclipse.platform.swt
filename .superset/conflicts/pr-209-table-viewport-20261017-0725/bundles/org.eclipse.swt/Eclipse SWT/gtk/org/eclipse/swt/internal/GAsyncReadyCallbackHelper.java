/*******************************************************************************
 * Copyright (c) 2025 Kichwa Coders Canada, Inc.
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
import java.util.*;

/**
 * Wrapper for use with GTK methods that use the async -> callback -> finish
 * pattern such as g_output_stream_write_all_async -> GAsyncReadyCallback ->
 * g_output_stream_write_all_finish
 *
 * This implementation allows reentrant calls to {@link #run(Async)}
 */
public class GAsyncReadyCallbackHelper {

	public static interface Async {
		/**
		 * The method that will make the async call to GTK, passing the GTK method
		 * callback in the parameter expecting a callback of type GAsyncReadyCallback
		 */
		void async(long callback);

		/**
		 * The method that will be called by the GAsyncReadyCallback and the result is
		 * passed to the finish method.
		 */
		void callback(long result);
	}

	private Callback gAsyncReadyCallback;
	private Async async;

	private GAsyncReadyCallbackHelper() {
	}

	public static void run(Async async) {
		Objects.requireNonNull(async, "async");
		GAsyncReadyCallbackHelper helper = new GAsyncReadyCallbackHelper();
		helper.async = async;
		helper.gAsyncReadyCallback = new Callback(helper, "GAsyncReadyCallback", void.class,
				new Type[] { long.class, long.class, long.class });
		async.async(helper.gAsyncReadyCallback.getAddress());
	}

	void GAsyncReadyCallback( //
			long /* (GObject*) */ source_object, //
			long /* (GAsyncResult*) */ result, //
			long /* (gpointer) */ data) {
		async.callback(result);
		gAsyncReadyCallback.dispose();
		gAsyncReadyCallback = null;
	}
}
