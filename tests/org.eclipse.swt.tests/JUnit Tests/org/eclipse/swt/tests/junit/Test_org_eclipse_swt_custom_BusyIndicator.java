/*******************************************************************************
 * Copyright (c) 2024, 2026 Christoph Läubrich and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Christoph Läubrich - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

public class Test_org_eclipse_swt_custom_BusyIndicator {

	private static final long LOOP_TIMEOUT_SECONDS = 5;
	private static final long WATCHDOG_TIMEOUT_SECONDS = 25;

	private static class TestState {
		final Thread testThread = Thread.currentThread();
		final AtomicBoolean latchWaitEntered = new AtomicBoolean(false);
		final AtomicBoolean latchNestedWaitEntered = new AtomicBoolean(false);
		final AtomicBoolean showWhileStarted = new AtomicBoolean(false);
		final AtomicBoolean showWhileCompleted = new AtomicBoolean(false);
		final AtomicBoolean eventLoopDraining = new AtomicBoolean(false);
		volatile String currentStage = "initialization";

		@Override
		public String toString() {
			return String.format(
					"TestState[stage=%s, testThread=%s, latchWaitEntered=%s, latchNestedWaitEntered=%s, showWhileStarted=%s, showWhileCompleted=%s, eventLoopDraining=%s]",
					currentStage, testThread.getName(), latchWaitEntered.get(), latchNestedWaitEntered.get(),
					showWhileStarted.get(), showWhileCompleted.get(), eventLoopDraining.get());
		}
	}

	private static ScheduledFuture<?> startWatchdog(ScheduledExecutorService watchdogExecutor, TestState state) {
		return watchdogExecutor.schedule(() -> {
			System.err.println("=== WATCHDOG FIRED ===");
			System.err.println("Test state: " + state);
			System.err.println();
			System.err.println("=== THREAD DUMP ===");
			Map<Thread, StackTraceElement[]> allStackTraces = Thread.getAllStackTraces();
			for (Map.Entry<Thread, StackTraceElement[]> entry : allStackTraces.entrySet()) {
				Thread thread = entry.getKey();
				StackTraceElement[] stackTrace = entry.getValue();
				System.err.println("Thread: " + thread.getName() + " (state=" + thread.getState() + ", id="
						+ thread.threadId() + ")");
				for (StackTraceElement element : stackTrace) {
					System.err.println("    at " + element);
				}
				System.err.println();
			}
			System.err.println("=== END THREAD DUMP ===");
			state.testThread.interrupt();
		}, WATCHDOG_TIMEOUT_SECONDS, TimeUnit.SECONDS);
	}

	private static void drainEventQueue(Display display, TestState state) {
		state.eventLoopDraining.set(true);
		long startTime = System.nanoTime();
		long timeoutNanos = TimeUnit.SECONDS.toNanos(LOOP_TIMEOUT_SECONDS);
		while (!display.isDisposed() && display.readAndDispatch()) {
			if (System.nanoTime() - startTime > timeoutNanos) {
				throw new IllegalStateException(
						"Event queue not empty after " + LOOP_TIMEOUT_SECONDS + " seconds. State: " + state);
			}
		}
		state.eventLoopDraining.set(false);
	}

	@Test
	@Timeout(value = 30)
	public void testShowWhile() {
		TestState state = new TestState();
		try (ExecutorService executor = Executors.newFixedThreadPool(2);
				ScheduledExecutorService watchdogExecutor = Executors.newSingleThreadScheduledExecutor()) {
			ScheduledFuture<?> watchdog = startWatchdog(watchdogExecutor, state);
			try {
				state.currentStage = "creating shell and display";
				Shell shell = new Shell();
				Display display = shell.getDisplay();
				Cursor busyCursor = display.getSystemCursor(SWT.CURSOR_WAIT);
				CountDownLatch latch = new CountDownLatch(1);
				state.currentStage = "creating main future";
				CompletableFuture<?> future = CompletableFuture.runAsync(() -> {
					state.latchWaitEntered.set(true);
					try {
						latch.await(10, TimeUnit.SECONDS);
					} catch (InterruptedException e) {
					}
				}, executor);

				CountDownLatch latchNested = new CountDownLatch(1);
				state.currentStage = "creating nested future";
				CompletableFuture<?> futureNested = CompletableFuture.runAsync(() -> {
					state.latchNestedWaitEntered.set(true);
					try {
						latchNested.await(10, TimeUnit.SECONDS);
					} catch (InterruptedException e) {
					}
				}, executor);

				assertNotEquals(busyCursor, shell.getCursor());

				// This it proves that events on the display are executed
				display.asyncExec(() -> {
					// This will happen during the showWhile(future) from below.
					BusyIndicator.showWhile(futureNested);
				});

				Cursor[] cursorInAsync = new Cursor[2];

				// this serves two purpose:
				// 1) it proves that events on the display are executed
				// 2) it checks that the shell has the busy cursor during the nest showWhile.
				display.asyncExec(() -> {
					cursorInAsync[0] = shell.getCursor();
					latchNested.countDown();
				});

				// this serves two purpose:
				// 1) it proves that events on the display are executed
				// 2) it checks that the shell has the busy cursor even after the termination of
				// the nested showWhile.
				display.asyncExec(() -> {
					cursorInAsync[1] = shell.getCursor();
					latch.countDown();
				});

				state.currentStage = "calling showWhile";
				state.showWhileStarted.set(true);
				BusyIndicator.showWhile(future);
				state.showWhileCompleted.set(true);
				assertTrue(future.isDone());
				assertEquals(busyCursor, cursorInAsync[0]);
				assertEquals(busyCursor, cursorInAsync[1]);
				shell.dispose();
				state.currentStage = "draining event queue";
				drainEventQueue(display, state);
				state.currentStage = "completed";
			} finally {
				watchdog.cancel(false);
			}
		}
	}

	@Test
	@Timeout(value = 30)
	public void testShowWhileWithFuture() {
		TestState state = new TestState();
		try (ExecutorService executor = Executors.newSingleThreadExecutor();
				ScheduledExecutorService watchdogExecutor = Executors.newSingleThreadScheduledExecutor()) {
			ScheduledFuture<?> watchdog = startWatchdog(watchdogExecutor, state);
			try {
				state.currentStage = "creating shell and display";
				Shell shell = new Shell();
				Display display = shell.getDisplay();
				Cursor busyCursor = display.getSystemCursor(SWT.CURSOR_WAIT);
				Cursor[] cursorInAsync = new Cursor[1];
				CountDownLatch latch = new CountDownLatch(1);
				state.currentStage = "creating future";
				Future<?> future = executor.submit(() -> {
					state.latchWaitEntered.set(true);
					try {
						latch.await(10, TimeUnit.SECONDS);
					} catch (InterruptedException e) {
					}
				});
				// this serves two purpose:
				// 1) it proves that events on the display are executed
				// 2) it checks that the shell has the busy cursor during the nest showWhile.
				display.asyncExec(() -> {
					cursorInAsync[0] = shell.getCursor();
					latch.countDown();
				});
				// External trigger for minimal latency as advised in the javadoc
				executor.submit(() -> {
					try {
						future.get();
					} catch (Exception e) {
					}
					display.wake();
				});
				state.currentStage = "calling showWhile";
				state.showWhileStarted.set(true);
				BusyIndicator.showWhile(future);
				state.showWhileCompleted.set(true);
				assertTrue(future.isDone());
				assertEquals(busyCursor, cursorInAsync[0]);
				shell.dispose();
				state.currentStage = "draining event queue";
				drainEventQueue(display, state);
				state.currentStage = "completed";
			} finally {
				watchdog.cancel(false);
			}
		}
	}

}
