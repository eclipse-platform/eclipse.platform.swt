/*******************************************************************************
 * Copyright (c) 2022 Joerg Kubitz
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Joerg Kubitz - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit.performance;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.eclipse.swt.widgets.Display;

/**
 * Tests SWT Event scheduling performance
 */
public class BenchmarkSwtMultithreading {
	private static final int BATCH_SIZE = 1_000_000;
	static AtomicInteger countdown = new AtomicInteger();

	/**
	 * manual performance test
	 * see https://github.com/eclipse-platform/eclipse.platform.swt/issues/74
	 *
	 * @param args ignored
	 */
	public static void main(String[] args) throws InterruptedException {
		final Display display = new Display();
		try {
			for (int runs = 0; runs < 100; runs++) {
				countdown.set(BATCH_SIZE);
				AtomicLong schedulingNanos = new AtomicLong();
				Thread thread = new Thread(() -> {
					// scheduling:
					schedulingNanos.set(new BenchmarkSwtMultithreading().scheduleAsyncEvents());
				}, "test");
				thread.start();
				thread.join();

				long nanoTime = System.nanoTime();
				while (countdown.get() > 0) {
					// handling;
					if (!display.readAndDispatch())
						display.sleep();
				}
				long nanoTime2 = System.nanoTime();
				long durationNanos = nanoTime2 - nanoTime;

				System.out.println("Duration for scheduling: " + String.format("%,15d", schedulingNanos.get())
						+ " ns  handling: " + String.format("%,15d", durationNanos) + " ns");
			}
		} finally {
			display.dispose();
		}
	}

	/**
	 * @return nanoTime
	 */
	public long scheduleAsyncEvents() {
		long nanoTime = System.nanoTime();
		Display display = Display.getDefault();
		for (int i = 0; i < BATCH_SIZE; i++) {
			display.asyncExec(() -> {
				Display.getCurrent();
				countdown.decrementAndGet();
			});
		}
		long nanoTime2 = System.nanoTime();
		long durationNanos = nanoTime2 - nanoTime;
		return durationNanos;
	}
}
