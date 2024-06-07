/*******************************************************************************
 * Copyright (c) 2024 Christoph Läubrich and others.
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Test;

public class Test_org_eclipse_swt_custom_BusyIndicator {

	@Test
	public void testShowWhile() {
		Shell shell = new Shell();
		Display display = shell.getDisplay();
		Cursor busyCursor = display.getSystemCursor(SWT.CURSOR_WAIT);
		CountDownLatch latch = new CountDownLatch(1);
		CompletableFuture<?> future = CompletableFuture.runAsync(() -> {
			try {
				latch.await(10, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
			}
		});

		CountDownLatch latchNested = new CountDownLatch(1);
		CompletableFuture<?> futureNested = CompletableFuture.runAsync(() -> {
			try {
				latchNested.await(10, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
			}
		});

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

		BusyIndicator.showWhile(future);
		assertTrue(future.isDone());
		assertEquals(busyCursor, cursorInAsync[0]);
		assertEquals(busyCursor, cursorInAsync[1]);
		shell.dispose();
		while (!display.isDisposed() && display.readAndDispatch()) {
		}
	}

	@Test
	public void testShowWhileWithFuture() {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		try {
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
			BusyIndicator.showWhile(future);
			assertTrue(future.isDone());
			assertEquals(busyCursor, cursorInAsync[0]);
			shell.dispose();
			while (!display.isDisposed() && display.readAndDispatch()) {
			}
		} finally {
			executor.shutdownNow();
		}
	}

}
