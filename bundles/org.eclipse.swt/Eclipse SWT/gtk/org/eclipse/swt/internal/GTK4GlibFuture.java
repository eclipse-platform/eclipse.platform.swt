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

import java.time.*;
import java.util.*;
import java.util.concurrent.*;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.widgets.*;

/**
 * Utility functions to allow interoperability with {@link CompletableFuture}
 * and GTK4's GLib Event Loop.
 */
public final class GTK4GlibFuture {
	private GTK4GlibFuture() {
	}

	/**
	 * Run the GTK4's GLib Event loop while waiting for future.get() to complete.
	 *
	 * @param <T>
	 * @param display Display to readAndDispatch events on
	 * @param future  to get data from
	 * @param timeout for how long to run if non-<code>null</code>, if
	 *                <code>null</code> this method does not timeout.
	 * @return The value obtained from future.get()
	 * @throws CancellationException if this future was cancelled
	 * @throws ExecutionException    if this future completed exceptionally
	 * @throws InterruptedException  if the current thread was interrupted while
	 *                               waiting
	 * @throws TimeoutException      if the wait timed out
	 */
	public static <T> T get(Display display, CompletableFuture<T> future, Duration timeout)
			throws TimeoutException, ExecutionException, InterruptedException {
		if (!GTK.GTK4) {
			throw new UnsupportedOperationException("This method is only for use in GTK4");
		}
		Objects.requireNonNull(display, "display");
		Objects.requireNonNull(future, "future");
		if (display.isDisposed()) {
			SWT.error(SWT.ERROR_WIDGET_DISPOSED);
		}
		if (display.getThread() != Thread.currentThread()) {
		    SWT.error(SWT.ERROR_THREAD_INVALID_ACCESS);
		}
		if (timeout != null && timeout.isNegative()) {
			throw new IllegalArgumentException("timeout must be >= 0");
		}

		if (future.isDone()) {
			return future.get();
		}

		long startTime = System.nanoTime();

		/*
		 * Run main loop until the future completes or we hit the deadline.
		 */
		while (true) {
			if (Thread.interrupted()) {
				throw new InterruptedException();
			}
			if (future.isDone()) {
				return future.get();
			}

			if (timeout != null && System.nanoTime() - startTime >= timeout.toNanos()) {
				throw new TimeoutException("Timeout waiting for future");
			}

			while (OS.g_main_context_iteration (0, false)) {
				if (Thread.interrupted()) {
					throw new InterruptedException();
				}
				if (future.isDone()) {
					return future.get();
				}
				if (timeout != null && System.nanoTime() - startTime >= timeout.toNanos()) {
					throw new TimeoutException("Timeout waiting for future");
				}
			}
			if (future.isDone()) {
				return future.get();
			}

			if (timeout != null) {
				display.timerExec(50, () -> {
					/* no-op - this exists to prevent sleep from sleeping forever */
				});
			}
			display.sleep();
		}
	}
}
