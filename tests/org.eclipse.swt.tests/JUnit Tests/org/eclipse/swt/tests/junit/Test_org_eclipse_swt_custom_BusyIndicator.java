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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Synchronizer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

public class Test_org_eclipse_swt_custom_BusyIndicator {

	private static final long LOOP_TIMEOUT_SECONDS = 5;
	private static final long WATCHDOG_TIMEOUT_SECONDS = 25;

	/**
	 * Debug tracker for SWT internal state. Uses reflection to access private
	 * fields for diagnostic purposes only.
	 */
	private static class SwtInternalStateTracker {
		private final Display display;
		private Field wakeField;
		private Field synchronizerField;

// Event log for tracking what happened
		final CopyOnWriteArrayList<String> eventLog = new CopyOnWriteArrayList<>();
		final AtomicInteger wakeCallCount = new AtomicInteger(0);
		final AtomicInteger readAndDispatchCallCount = new AtomicInteger(0);
		final AtomicInteger readAndDispatchTrueCount = new AtomicInteger(0);
		final AtomicInteger sleepCallCount = new AtomicInteger(0);
		final AtomicInteger asyncExecScheduledCount = new AtomicInteger(0);
		final AtomicInteger asyncExecRunCount = new AtomicInteger(0);
		volatile long lastWakeCallTime = 0;
		volatile String lastWakeCallThread = null;
		volatile long lastSleepEnterTime = 0;
		volatile long lastSleepExitTime = 0;

		SwtInternalStateTracker(Display display) {
			this.display = display;
			initReflection();
			logEvent("SwtInternalStateTracker initialized");
		}

		private void initReflection() {
			try {
// Try to access Display.wake field (GTK-specific)
				wakeField = Display.class.getDeclaredField("wake");
				wakeField.setAccessible(true);
				logEvent("Found Display.wake field (GTK platform)");
			} catch (NoSuchFieldException e) {
				logEvent("WARN: Could not find Display.wake field (may not be GTK platform)");
			}
			try {
				synchronizerField = Display.class.getDeclaredField("synchronizer");
				synchronizerField.setAccessible(true);
				logEvent("Found Display.synchronizer field");
			} catch (NoSuchFieldException e) {
				logEvent("WARN: Could not find Display.synchronizer field");
			}
		}

		void logEvent(String event) {
			long ts = System.currentTimeMillis() % 100000;
			String threadName = Thread.currentThread().getName();
			eventLog.add(String.format("[%05d] [%-20s] %s", ts, threadName, event));
		}

		void recordWakeCall() {
			wakeCallCount.incrementAndGet();
			lastWakeCallTime = System.currentTimeMillis();
			lastWakeCallThread = Thread.currentThread().getName();
			Boolean wakeFlagBefore = getWakeFlag();
			logEvent("display.wake() called (wake flag before=" + wakeFlagBefore + ")");
		}

		void recordAsyncExecScheduled(String description) {
			asyncExecScheduledCount.incrementAndGet();
			Boolean syncEmpty = isSynchronizerEmpty();
			logEvent("asyncExec SCHEDULED: " + description + " (syncEmpty before=" + syncEmpty + ")");
		}

		void recordAsyncExecRun(String description) {
			asyncExecRunCount.incrementAndGet();
			logEvent("asyncExec RUNNING: " + description);
		}

		void recordReadAndDispatch(boolean result) {
			readAndDispatchCallCount.incrementAndGet();
			if (result) {
				readAndDispatchTrueCount.incrementAndGet();
			}
			Boolean wakeFlagNow = getWakeFlag();
			Boolean syncEmpty = isSynchronizerEmpty();
			logEvent("readAndDispatch() returned " + result + " (wake=" + wakeFlagNow + ", syncEmpty=" + syncEmpty
					+ ")");
		}

		void recordSleepEnter() {
			sleepCallCount.incrementAndGet();
			lastSleepEnterTime = System.currentTimeMillis();
			Boolean wakeFlagNow = getWakeFlag();
			Boolean syncEmpty = isSynchronizerEmpty();
			logEvent("Display.sleep() ENTER (wake=" + wakeFlagNow + ", syncEmpty=" + syncEmpty + ")");
		}

		void recordSleepExit() {
			lastSleepExitTime = System.currentTimeMillis();
			long duration = lastSleepExitTime - lastSleepEnterTime;
			Boolean wakeFlagNow = getWakeFlag();
			Boolean syncEmpty = isSynchronizerEmpty();
			logEvent("Display.sleep() EXIT after " + duration + "ms (wake=" + wakeFlagNow + ", syncEmpty=" + syncEmpty
					+ ")");
		}

		Boolean getWakeFlag() {
			if (wakeField == null)
				return null;
			try {
				return wakeField.getBoolean(display);
			} catch (Exception e) {
				return null;
			}
		}

		Boolean isSynchronizerEmpty() {
			if (synchronizerField == null)
				return null;
			try {
				Synchronizer sync = (Synchronizer) synchronizerField.get(display);
				Method isEmptyMethod = Synchronizer.class.getDeclaredMethod("isMessagesEmpty");
				isEmptyMethod.setAccessible(true);
				return (Boolean) isEmptyMethod.invoke(sync);
			} catch (Exception e) {
				return null;
			}
		}

		String getInternalStateSummary() {
			StringBuilder sb = new StringBuilder();
			sb.append("=== SWT INTERNAL STATE ===\n");
			sb.append("  wake flag (current): ").append(getWakeFlag()).append("\n");
			sb.append("  synchronizer empty (current): ").append(isSynchronizerEmpty()).append("\n");
			sb.append("  display.wake() call count: ").append(wakeCallCount.get()).append("\n");
			sb.append("  last wake() call time: ").append(lastWakeCallTime > 0 ? (lastWakeCallTime % 100000) : "never")
					.append("\n");
			sb.append("  last wake() call thread: ").append(lastWakeCallThread != null ? lastWakeCallThread : "none")
					.append("\n");
			sb.append("  readAndDispatch() total calls: ").append(readAndDispatchCallCount.get()).append("\n");
			sb.append("  readAndDispatch() returned true: ").append(readAndDispatchTrueCount.get()).append("\n");
			sb.append("  Display.sleep() call count: ").append(sleepCallCount.get()).append("\n");
			sb.append("  asyncExec scheduled count: ").append(asyncExecScheduledCount.get()).append("\n");
			sb.append("  asyncExec actually ran count: ").append(asyncExecRunCount.get()).append("\n");
			sb.append("=== END SWT INTERNAL STATE ===");
			return sb.toString();
		}

		void printEventLog() {
			System.err.println("=== SWT EVENT LOG (" + eventLog.size() + " entries) ===");
			for (String event : eventLog) {
				System.err.println(event);
			}
			System.err.println("=== END SWT EVENT LOG ===");
		}
	}

	private static class TestState {
		final Thread testThread = Thread.currentThread();
		final AtomicBoolean latchWaitEntered = new AtomicBoolean(false);
		final AtomicBoolean latchNestedWaitEntered = new AtomicBoolean(false);
		final AtomicBoolean showWhileStarted = new AtomicBoolean(false);
		final AtomicBoolean showWhileCompleted = new AtomicBoolean(false);
		final AtomicBoolean eventLoopDraining = new AtomicBoolean(false);
		final AtomicBoolean futureCompleted = new AtomicBoolean(false);
		final AtomicBoolean futureNestedCompleted = new AtomicBoolean(false);
		volatile String currentStage = "initialization";
		volatile SwtInternalStateTracker swtTracker = null;

// Track asyncExec callback execution
		final AtomicBoolean asyncExec1Ran = new AtomicBoolean(false);
		final AtomicBoolean asyncExec2Ran = new AtomicBoolean(false);
		final AtomicBoolean asyncExec3Ran = new AtomicBoolean(false);

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("TestState[\n");
			sb.append("  stage=").append(currentStage).append("\n");
			sb.append("  testThread=").append(testThread.getName()).append("\n");
			sb.append("  latchWaitEntered=").append(latchWaitEntered.get()).append("\n");
			sb.append("  latchNestedWaitEntered=").append(latchNestedWaitEntered.get()).append("\n");
			sb.append("  showWhileStarted=").append(showWhileStarted.get()).append("\n");
			sb.append("  showWhileCompleted=").append(showWhileCompleted.get()).append("\n");
			sb.append("  eventLoopDraining=").append(eventLoopDraining.get()).append("\n");
			sb.append("  futureCompleted=").append(futureCompleted.get()).append("\n");
			sb.append("  futureNestedCompleted=").append(futureNestedCompleted.get()).append("\n");
			sb.append("  asyncExec1Ran=").append(asyncExec1Ran.get()).append("\n");
			sb.append("  asyncExec2Ran=").append(asyncExec2Ran.get()).append("\n");
			sb.append("  asyncExec3Ran=").append(asyncExec3Ran.get()).append("\n");
			sb.append("]");
			return sb.toString();
		}
	}

	private static ScheduledFuture<?> startWatchdog(ScheduledExecutorService watchdogExecutor, TestState state) {
		return watchdogExecutor.schedule(() -> {
			System.err.println("=== WATCHDOG FIRED ===");
			System.err.println("Test state: " + state);
			System.err.println();

// Print SWT internal state
			if (state.swtTracker != null) {
				System.err.println(state.swtTracker.getInternalStateSummary());
				System.err.println();
				state.swtTracker.printEventLog();
				System.err.println();
			}

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
			Runtime.getRuntime().exit(-1000);
		}, WATCHDOG_TIMEOUT_SECONDS, TimeUnit.SECONDS);
	}

	private static void drainEventQueue(Display display, TestState state) {
		state.eventLoopDraining.set(true);
		if (state.swtTracker != null) {
			state.swtTracker.logEvent("drainEventQueue() started");
		}
		long startTime = System.nanoTime();
		long timeoutNanos = TimeUnit.SECONDS.toNanos(LOOP_TIMEOUT_SECONDS);
		while (!display.isDisposed() && display.readAndDispatch()) {
			if (System.nanoTime() - startTime > timeoutNanos) {
				throw new IllegalStateException(
						"Event queue not empty after " + LOOP_TIMEOUT_SECONDS + " seconds. State: " + state);
			}
		}
		if (state.swtTracker != null) {
			state.swtTracker.logEvent("drainEventQueue() completed");
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

// Initialize SWT internal state tracker
				SwtInternalStateTracker tracker = new SwtInternalStateTracker(display);
				state.swtTracker = tracker;

				Cursor busyCursor = display.getSystemCursor(SWT.CURSOR_WAIT);
				CountDownLatch latch = new CountDownLatch(1);

				state.currentStage = "creating main future";
				tracker.logEvent("Creating main future");
				CompletableFuture<?> future = CompletableFuture.runAsync(() -> {
					tracker.logEvent("Main future task STARTED, entering latch.await()");
					state.latchWaitEntered.set(true);
					try {
						boolean completed = latch.await(10, TimeUnit.SECONDS);
						tracker.logEvent("Main future latch.await() returned: " + completed);
					} catch (InterruptedException e) {
						tracker.logEvent("Main future latch.await() INTERRUPTED");
					}
					state.futureCompleted.set(true);
					tracker.logEvent("Main future task COMPLETED");
				}, executor);

				CountDownLatch latchNested = new CountDownLatch(1);
				state.currentStage = "creating nested future";
				tracker.logEvent("Creating nested future");
				CompletableFuture<?> futureNested = CompletableFuture.runAsync(() -> {
					tracker.logEvent("Nested future task STARTED, entering latch.await()");
					state.latchNestedWaitEntered.set(true);
					try {
						boolean completed = latchNested.await(10, TimeUnit.SECONDS);
						tracker.logEvent("Nested future latch.await() returned: " + completed);
					} catch (InterruptedException e) {
						tracker.logEvent("Nested future latch.await() INTERRUPTED");
					}
					state.futureNestedCompleted.set(true);
					tracker.logEvent("Nested future task COMPLETED");
				}, executor);

				assertNotEquals(busyCursor, shell.getCursor());

// asyncExec #1: This will call nested showWhile
				tracker.recordAsyncExecScheduled("#1: BusyIndicator.showWhile(futureNested)");
				display.asyncExec(() -> {
					tracker.recordAsyncExecRun("#1: BusyIndicator.showWhile(futureNested)");
					state.asyncExec1Ran.set(true);
					BusyIndicator.showWhile(futureNested);
					tracker.logEvent("asyncExec #1: showWhile(futureNested) returned");
				});

				Cursor[] cursorInAsync = new Cursor[2];

// asyncExec #2: Check cursor and release nested latch
				tracker.recordAsyncExecScheduled("#2: check cursor + latchNested.countDown()");
				display.asyncExec(() -> {
					tracker.recordAsyncExecRun("#2: check cursor + latchNested.countDown()");
					state.asyncExec2Ran.set(true);
					cursorInAsync[0] = shell.getCursor();
					tracker.logEvent("asyncExec #2: cursor=" + cursorInAsync[0] + ", calling latchNested.countDown()");
					latchNested.countDown();
				});

// asyncExec #3: Check cursor and release main latch
				tracker.recordAsyncExecScheduled("#3: check cursor + latch.countDown()");
				display.asyncExec(() -> {
					tracker.recordAsyncExecRun("#3: check cursor + latch.countDown()");
					state.asyncExec3Ran.set(true);
					cursorInAsync[1] = shell.getCursor();
					tracker.logEvent("asyncExec #3: cursor=" + cursorInAsync[1] + ", calling latch.countDown()");
					latch.countDown();
				});

				state.currentStage = "calling showWhile";
				state.showWhileStarted.set(true);
				tracker.logEvent("About to call BusyIndicator.showWhile(future)");
				tracker.logEvent("State before showWhile: wake=" + tracker.getWakeFlag() + ", syncEmpty="
						+ tracker.isSynchronizerEmpty());

				BusyIndicator.showWhile(future);

				tracker.logEvent("BusyIndicator.showWhile(future) returned");
				state.showWhileCompleted.set(true);
				assertTrue(future.isDone());
				assertEquals(busyCursor, cursorInAsync[0]);
				assertEquals(busyCursor, cursorInAsync[1]);
				shell.dispose();
				state.currentStage = "draining event queue";
				drainEventQueue(display, state);
				state.currentStage = "completed";
				tracker.logEvent("Test completed successfully");
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

// Initialize SWT internal state tracker
				SwtInternalStateTracker tracker = new SwtInternalStateTracker(display);
				state.swtTracker = tracker;

				Cursor busyCursor = display.getSystemCursor(SWT.CURSOR_WAIT);
				Cursor[] cursorInAsync = new Cursor[1];
				CountDownLatch latch = new CountDownLatch(1);

				state.currentStage = "creating future";
				tracker.logEvent("Creating future");
				Future<?> future = executor.submit(() -> {
					tracker.logEvent("Future task STARTED, entering latch.await()");
					state.latchWaitEntered.set(true);
					try {
						boolean completed = latch.await(10, TimeUnit.SECONDS);
						tracker.logEvent("Future latch.await() returned: " + completed);
					} catch (InterruptedException e) {
						tracker.logEvent("Future latch.await() INTERRUPTED");
					}
					state.futureCompleted.set(true);
					tracker.logEvent("Future task COMPLETED");
				});

// asyncExec: Check cursor and release latch
				tracker.recordAsyncExecScheduled("#1: check cursor + latch.countDown()");
				display.asyncExec(() -> {
					tracker.recordAsyncExecRun("#1: check cursor + latch.countDown()");
					state.asyncExec1Ran.set(true);
					cursorInAsync[0] = shell.getCursor();
					tracker.logEvent("asyncExec #1: cursor=" + cursorInAsync[0] + ", calling latch.countDown()");
					latch.countDown();
				});

// External trigger for minimal latency as advised in the javadoc
				executor.submit(() -> {
					tracker.logEvent("Wake trigger task: waiting for future.get()");
					try {
						future.get();
						tracker.logEvent("Wake trigger task: future.get() returned, calling display.wake()");
					} catch (Exception e) {
						tracker.logEvent("Wake trigger task: future.get() threw " + e.getClass().getSimpleName());
					}
					tracker.recordWakeCall();
					display.wake();
				});

				state.currentStage = "calling showWhile";
				state.showWhileStarted.set(true);
				tracker.logEvent("About to call BusyIndicator.showWhile(future)");
				tracker.logEvent("State before showWhile: wake=" + tracker.getWakeFlag() + ", syncEmpty="
						+ tracker.isSynchronizerEmpty());

				BusyIndicator.showWhile(future);

				tracker.logEvent("BusyIndicator.showWhile(future) returned");
				state.showWhileCompleted.set(true);
				assertTrue(future.isDone());
				assertEquals(busyCursor, cursorInAsync[0]);
				shell.dispose();
				state.currentStage = "draining event queue";
				drainEventQueue(display, state);
				state.currentStage = "completed";
				tracker.logEvent("Test completed successfully");
			} finally {
				watchdog.cancel(false);
			}
		}
	}

}
