/*******************************************************************************
 * Copyright (c) 2000, 2022 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Chrsitoph Läubrich - add methods to work with {@link CompletableFuture}s
 *******************************************************************************/
package org.eclipse.swt.custom;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * Support for showing a Busy Cursor during a long running process.
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#busyindicator">BusyIndicator snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public class BusyIndicator {

	private static final AtomicInteger nextBusyId = new AtomicInteger();
	static final String BUSYID_NAME = "SWT BusyIndicator"; //$NON-NLS-1$
	static final String BUSY_CURSOR = "SWT BusyIndicator Cursor"; //$NON-NLS-1$

	/**
	 * Runs the given <code>Runnable</code> while providing
	 * busy feedback using this busy indicator.
	 *
	 * @param display the display on which the busy feedback should be
	 *        displayed.  If the display is null, the Display for the current
	 *        thread will be used.  If there is no Display for the current thread,
	 *        the runnable code will be executed and no busy feedback will be displayed.
	 * @param runnable the runnable for which busy feedback is to be shown.
	 *        Must not be null.
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the runnable is null</li>
	 * </ul>
	 */

	public static void showWhile(Display display, Runnable runnable) {
		if (runnable == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (display == null) {
			display = Display.getCurrent();
			if (display == null) {
				runnable.run();
				return;
			}
		}

		Integer busyId = setBusyCursor(display);

		try {
			runnable.run();
		} finally {
			clearBusyCursor(display, busyId);
		}
	}

	/**
	 * If called from a {@link Display} thread use the given {@link SwtRunnable} to
	 * produces a {@link CompletableFuture} providing busy feedback using the busy
	 * indicator while execution is running. If called from a non {@link Display}
	 * the execution is performed in place and the result returned as a
	 * {@link CompletableFuture}. It is therefore safe to call this method from any
	 * thread and the {@link SwtCallable} is always evaluated outside the UI. The
	 * {@link ForkJoinPool#commonPool()} is used to execute the computation in case
	 * this is called from a {@link Display} thread
	 *
	 * @param action the action that should be executed and produces a result of the
	 *               {@link CompletableFuture}
	 *
	 * @since 3.122
	 */
	public static <E extends Exception> CompletableFuture<?> execute(SwtRunnable<E> action) {
		return execute(action, ForkJoinPool.commonPool());
	}

	/**
	 * If called from a {@link Display} thread use the given {@link SwtRunnable} to
	 * produces a {@link CompletableFuture} providing busy feedback using the busy
	 * indicator while execution is running. If called from a non {@link Display}
	 * the execution is performed in place and the result returned as a
	 * {@link CompletableFuture}. It is therefore safe to call this method from any
	 * thread and the {@link SwtCallable} is always evaluated outside the UI.
	 *
	 * @param action   the action that should be executed and produces a result of
	 *                 the {@link CompletableFuture}
	 * @param executor the Executor to perform the computation in case this is
	 *                 called from a {@link Display} thread, passing a
	 *                 {@link Display} will throw an
	 *                 {@link IllegalArgumentException} as this will lead to a
	 *                 blocking UI and violates the contract of this method
	 * @since 3.122
	 */
	public static <E extends Exception> CompletableFuture<?> execute(SwtRunnable<E> action, Executor executor) {
		return compute(() -> {
			action.run();
			return null;
		}, executor);
	}

	/**
	 * If called from a {@link Display} thread use the given {@link SwtCallable} to
	 * produces a {@link CompletableFuture} providing busy feedback using the busy
	 * indicator while computation is running. If called from a non {@link Display}
	 * the computation is performed in place and the result returned as a
	 * {@link CompletableFuture}. It is therefore safe to call this method from any
	 * thread and the {@link SwtCallable} is always evaluated outside the UI. The
	 * {@link ForkJoinPool#commonPool()} is used to execute the computation in case
	 * this is called from a {@link Display} thread
	 *
	 * @param action the action that should be executed and produces a result of the
	 *               {@link CompletableFuture}
	 *
	 * @since 3.122
	 */
	public static <V, E extends Exception> CompletableFuture<V> compute(SwtCallable<V, E> action) {
		return compute(action, ForkJoinPool.commonPool());
	}

	/**
	 * If called from a {@link Display} thread use the given {@link SwtCallable} to
	 * compute a {@link CompletableFuture} providing busy feedback using the busy
	 * indicator while computation is running. If called from a non {@link Display}
	 * the computation is performed in place and the result returned as a
	 * {@link CompletableFuture}. It is therefore safe to call this method from any
	 * thread and the {@link SwtCallable} is always evaluated outside the UI.
	 *
	 * @param action   the action that should be executed and produces a result of
	 *                 the {@link CompletableFuture}
	 * @param executor the Executor to perform the computation in case this is
	 *                 called from a {@link Display} thread, passing a
	 *                 {@link Display} will throw an
	 *                 {@link IllegalArgumentException} as this will lead to a
	 *                 blocking UI and violates the contract of this method
	 *
	 * @since 3.122
	 */
	public static <V, E extends Exception> CompletableFuture<V> compute(SwtCallable<V, E> action, Executor executor) {
		Objects.requireNonNull(action);
		Objects.requireNonNull(executor);
		if (executor instanceof Display) {
			throw new IllegalArgumentException("passing a Display as an executor is not allowed!");
		}
		Display display = Display.findDisplay(Thread.currentThread());
		if (display == null) {
			try {
				V inplaceResult = action.call();
				return CompletableFuture.completedFuture(inplaceResult);
			} catch (Exception e) {
				return CompletableFuture.failedFuture(e);
			}
		}
		Integer busyId = setBusyCursor(display);
		CompletableFuture<V> future = new CompletableFuture<>();
		executor.execute(() -> {
			try {
				if (future.isCancelled()) {
					return;
				}
				V asyncResult = action.call();
				future.complete(asyncResult);
			} catch (Exception e) {
				future.completeExceptionally(e);
			} finally {
				display.asyncExec(() -> clearBusyCursor(display, busyId));
			}
		});
		return future;
	}

	private static void clearBusyCursor(Display display, Integer busyId) {
		Shell[] shells = display.getShells();
		for (Shell shell : shells) {
			Integer id = (Integer) shell.getData(BUSYID_NAME);
			if (Objects.equals(id, busyId)) {
				setCursorAndId(shell, null, null);
			}
		}
	}

	private static Integer setBusyCursor(Display display) {
		Integer busyId = nextBusyId.getAndIncrement();
		Cursor cursor = display.getSystemCursor(SWT.CURSOR_WAIT);
		Shell[] shells = display.getShells();
		for (Shell shell : shells) {
			Integer id = (Integer) shell.getData(BUSYID_NAME);
			if (id == null) {
				setCursorAndId(shell, cursor, busyId);
			}
		}
		return busyId;
	}

	/**
	 * Paranoia code to make sure we don't break UI because of one shell disposed,
	 * see bug 532632 comment 20
	 */
	private static void setCursorAndId(Shell shell, Cursor cursor, Integer busyId) {
		if (!shell.isDisposed()) {
			shell.setCursor(cursor);
		}
		if (!shell.isDisposed()) {
			shell.setData(BUSYID_NAME, busyId);
		}
	}
}
