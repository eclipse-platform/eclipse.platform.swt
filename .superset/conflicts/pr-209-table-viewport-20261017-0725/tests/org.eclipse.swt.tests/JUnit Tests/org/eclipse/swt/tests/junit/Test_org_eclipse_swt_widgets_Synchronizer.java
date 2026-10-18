/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Project
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.swt.widgets.Display;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Synchronizer
 *
 * @see org.eclipse.swt.widgets.Synchronizer
 */
public class Test_org_eclipse_swt_widgets_Synchronizer {

	private static final int TIMEOUT_MS = 20_000;

	/**
	 * Regression test for
	 * https://github.com/eclipse-platform/eclipse.platform.swt/issues/3151
	 *
	 * <p>
	 * Verifies that every task posted via {@code asyncExec()} from a non-UI thread
	 * is eventually executed, even when posted concurrently while the UI thread is
	 * draining the message queue.
	 *
	 * <p>
	 * The old {@code Synchronizer.addLast()} implementation had this race:
	 * <ol>
	 * <li>Producer thread: {@code messages.isEmpty()} → {@code false} (queue has
	 * items), so {@code wake = false}</li>
	 * <li>UI thread: drains all remaining items; calls {@code sleep()} →
	 * {@code OS.WaitMessage()} / blocks</li>
	 * <li>Producer thread: {@code messages.add(lock)}</li>
	 * <li>Producer thread: {@code if (wake)} → {@code false} → {@code wakeThread()}
	 * never called → Display sleeps forever despite a pending message</li>
	 * </ol>
	 *
	 * <p>
	 * Using multiple concurrent producers maximises the probability of hitting the
	 * race window: one producer's task appears in the queue when another producer
	 * evaluates {@code isEmpty()}, then that task gets consumed and the UI sleeps
	 * before the second producer's {@code add()} completes.
	 *
	 * <p>
	 * A {@code timerExec} sentinel bounds the test duration so a hung Display
	 * produces a test failure rather than an infinite hang.
	 */
	@Test
	@Timeout(TIMEOUT_MS)
	public void test_asyncExec_allTasksComplete_noMissedWakeup() throws InterruptedException {
		final int PRODUCERS = 4;
		final int TASKS_PER_PRODUCER = 1_000;
		final int TOTAL_TASKS = PRODUCERS * TASKS_PER_PRODUCER;
		final int TIMEOUT_MS = 15_000;

		Display display = Display.getDefault();
		try {
			AtomicInteger completedCount = new AtomicInteger();
			AtomicBoolean timedOut = new AtomicBoolean(false);

			// Safety net: if the Display gets stuck due to a missed wakeThread() call,
			// the timer fires on the UI thread and lets the event loop exit cleanly so
			// the assertion below produces a clear failure instead of hanging CI.
			display.timerExec(TIMEOUT_MS * 3/4, () -> timedOut.set(true));

			// Multiple concurrent producers increase the chance that one producer's
			// isEmpty() check sees another's task, which is then drained before the
			// first producer's add() completes – the exact sequence that triggers the bug.
			Thread[] producers = new Thread[PRODUCERS];
			for (int p = 0; p < PRODUCERS; p++) {
				producers[p] = new Thread(() -> {
					for (int i = 0; i < TASKS_PER_PRODUCER; i++) {
						if (timedOut.get()) {
							return;
						}
						display.asyncExec(completedCount::incrementAndGet);
					}
				}, "asyncExec-producer-" + p);
				producers[p].start();
			}

			while (completedCount.get() < TOTAL_TASKS && !timedOut.get()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}

			for (Thread producer : producers) {
				producer.join(1_000);
			}

			assertFalse(timedOut.get(),
					"Display did not process all asyncExec tasks within " + TIMEOUT_MS + " ms " + "(completed "
							+ completedCount.get() + "/" + TOTAL_TASKS + "). "
							+ "Likely a missed wakeThread() call in Synchronizer.addLast() "
							+ "– regression of https://github.com/eclipse-platform/eclipse.platform.swt/issues/3151");
			assertEquals(TOTAL_TASKS, completedCount.get(), "Not all asyncExec tasks were executed.");
		} finally {
			display.dispose();
		}
	}

}
