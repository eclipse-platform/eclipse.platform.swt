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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

public class Test_org_eclipse_swt_custom_BusyIndicator {

	@Test
	@Timeout(value = 30)
	public void testShowWhile() {
		// Executors.newSingleThreadExecutor() hangs on some Linux configurations
		try (ExecutorService executor = Executors.newFixedThreadPool(2)){
			Shell shell = new Shell();
			Display display = shell.getDisplay();
			Cursor busyCursor = display.getSystemCursor(SWT.CURSOR_WAIT);
			CountDownLatch latch = new CountDownLatch(1);
			CompletableFuture<?> future = CompletableFuture.runAsync(() -> {
				try {
					latch.await(10, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
				}
			}, executor);

			CountDownLatch latchNested = new CountDownLatch(1);
			CompletableFuture<?> futureNested = CompletableFuture.runAsync(() -> {
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

			AtomicBoolean timedOut = new AtomicBoolean(false);
			AtomicInteger wakeCount = new AtomicInteger(0);
			AtomicReference<Timer> wakeTimerRef = new AtomicReference<>();
			Timer watchdog = new Timer("BusyIndicator-watchdog", true);
			watchdog.schedule(new TimerTask() {
				@Override
				public void run() {
					timedOut.set(true);
					System.out.println("[BusyIndicator testShowWhile] Watchdog fired after 20s! future.isDone()=" + future.isDone());
					Timer wakeTimer = new Timer("BusyIndicator-waker", true);
					wakeTimerRef.set(wakeTimer);
					wakeTimer.scheduleAtFixedRate(new TimerTask() {
						@Override
						public void run() {
							int count = wakeCount.incrementAndGet();
							System.out.println("[BusyIndicator testShowWhile] Calling display.wake() #" + count + " future.isDone()=" + future.isDone());
							if (!display.isDisposed()) {
								try {
									display.wake();
								} catch (Exception e) {
									System.out.println("[BusyIndicator testShowWhile] display.wake() threw: " + e);
								}
							}
						}
					}, 0, 1000);
				}
			}, 20_000);
			try {
				BusyIndicator.showWhile(future);
			} finally {
				watchdog.cancel();
				Timer wakeTimer = wakeTimerRef.get();
				if (wakeTimer != null) {
					wakeTimer.cancel();
				}
			}
			assertFalse(timedOut.get(), "showWhile() did not complete within 20s, had to call display.wake() " + wakeCount.get() + " time(s)");
			assertTrue(future.isDone());
			assertEquals(busyCursor, cursorInAsync[0]);
			assertEquals(busyCursor, cursorInAsync[1]);
			shell.dispose();
			while (!display.isDisposed() && display.readAndDispatch()) {
			}
		}
	}

	@Test
	@Timeout(value = 30)
	public void testShowWhileWithFuture() {
		try (ExecutorService executor = Executors.newSingleThreadExecutor()){
			Shell shell = new Shell();
			Display display = shell.getDisplay();
			Cursor busyCursor = display.getSystemCursor(SWT.CURSOR_WAIT);
			Cursor[] cursorInAsync = new Cursor[1];
			CountDownLatch latch = new CountDownLatch(1);
			Future<?> future = executor.submit(() -> {
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
			//External trigger for minimal latency as advised in the javadoc
			executor.submit(()->{
				try {
					future.get();
				} catch (Exception e) {
				}
				display.wake();
			});

			AtomicBoolean timedOut = new AtomicBoolean(false);
			AtomicInteger wakeCount = new AtomicInteger(0);
			AtomicReference<Timer> wakeTimerRef = new AtomicReference<>();
			Timer watchdog = new Timer("BusyIndicator-watchdog", true);
			watchdog.schedule(new TimerTask() {
				@Override
				public void run() {
					timedOut.set(true);
					System.out.println("[BusyIndicator testShowWhileWithFuture] Watchdog fired after 20s! future.isDone()=" + future.isDone());
					Timer wakeTimer = new Timer("BusyIndicator-waker", true);
					wakeTimerRef.set(wakeTimer);
					wakeTimer.scheduleAtFixedRate(new TimerTask() {
						@Override
						public void run() {
							int count = wakeCount.incrementAndGet();
							System.out.println("[BusyIndicator testShowWhileWithFuture] Calling display.wake() #" + count + " future.isDone()=" + future.isDone());
							if (!display.isDisposed()) {
								try {
									display.wake();
								} catch (Exception e) {
									System.out.println("[BusyIndicator testShowWhileWithFuture] display.wake() threw: " + e);
								}
							}
						}
					}, 0, 1000);
				}
			}, 20_000);
			try {
				BusyIndicator.showWhile(future);
			} finally {
				watchdog.cancel();
				Timer wakeTimer = wakeTimerRef.get();
				if (wakeTimer != null) {
					wakeTimer.cancel();
				}
			}
			assertFalse(timedOut.get(), "showWhile() did not complete within 20s, had to call display.wake() " + wakeCount.get() + " time(s)");
			assertTrue(future.isDone());
			assertEquals(busyCursor, cursorInAsync[0]);
			shell.dispose();
			while (!display.isDisposed() && display.readAndDispatch()) {
			}
		}
	}

}
