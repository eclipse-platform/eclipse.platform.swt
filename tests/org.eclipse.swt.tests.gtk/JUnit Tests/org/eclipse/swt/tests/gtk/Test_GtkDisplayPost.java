/*******************************************************************************
 * Copyright (c) 2026 vogella GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.tests.gtk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Test_GtkDisplayPost {

	private Display display;
	private Shell shell;

	@BeforeEach
	public void setUp() {
		display = Display.getDefault();
		shell = new Shell(display);
	}

	@AfterEach
	public void tearDown() {
		shell.dispose();
	}

	@Test
	public void test_postMouseDownAndUp_reportPointerPosition() {
		shell.setLayout(new GridLayout());
		Canvas canvas = new Canvas(shell, SWT.NONE);
		canvas.setLayoutData(new GridData(200, 100));
		Event[] down = new Event[1], up = new Event[1];
		canvas.addListener(SWT.MouseDown, e -> down[0] = e);
		canvas.addListener(SWT.MouseUp, e -> up[0] = e);
		shell.setLocation(50, 50);
		shell.pack();
		shell.open();
		shell.forceActive();
		processEvents(500);

		Point target = canvas.toDisplay(60, 30);
		Event move = new Event();
		move.type = SWT.MouseMove;
		move.x = target.x;
		move.y = target.y;
		assumeTrue(display.post(move), "Display.post is not supported here");
		processEvents(300);
		assumeTrue(display.getCursorControl() == canvas, "The pointer did not arrive on the canvas");

		Event press = new Event();
		press.type = SWT.MouseDown;
		press.button = 1;
		assumeTrue(display.post(press), "Display.post of a button is not supported here");
		processEvents(200);
		Event release = new Event();
		release.type = SWT.MouseUp;
		release.button = 1;
		display.post(release);
		processEvents(300);

		assertNotNull(down[0], "No MouseDown");
		assertNotNull(up[0], "No MouseUp");
		assertEquals(new Point(60, 30), new Point(down[0].x, down[0].y), "MouseDown position");
		assertEquals(new Point(60, 30), new Point(up[0].x, up[0].y), "MouseUp position");
	}

	private void processEvents(int millis) {
		long end = System.currentTimeMillis() + millis;
		while (System.currentTimeMillis() < end) {
			if (!display.readAndDispatch()) {
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					return;
				}
			}
		}
	}
}
