/*******************************************************************************
 * Copyright (c) 2026 Eclipse Foundation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.tests.cocoa;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.BooleanSupplier;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Paints a GLCanvas, which in a layer-backed view hierarchy is drawn by its
 * OpenGL layer without a current NSGraphicsContext.
 * <p>
 * See https://github.com/eclipse-platform/eclipse.platform.swt/issues/237
 */
public class Test_cocoa_GLCanvas {

	private Display display;
	private Shell shell;
	private GLCanvas canvas;
	private int paintCount;

	@BeforeEach
	public void setUp() {
		display = Display.getDefault();
		shell = new Shell(display);
		shell.setLayout(new FillLayout());
		GLData data = new GLData();
		data.doubleBuffer = true;
		canvas = new GLCanvas(shell, SWT.NONE, data);
		canvas.addListener(SWT.Paint, e -> {
			canvas.setCurrent();
			canvas.swapBuffers();
			paintCount++;
		});
		shell.setSize(200, 200);
		shell.open();
	}

	@AfterEach
	public void tearDown() {
		shell.dispose();
	}

	@Test
	public void test_paint() {
		assertTrue(processEventsUntil(() -> paintCount > 0), "initial paint");
	}

	@Test
	public void test_paintAfterRedraw() {
		assertTrue(processEventsUntil(() -> paintCount > 0), "initial paint");
		int count = paintCount;
		canvas.redraw();
		assertTrue(processEventsUntil(() -> paintCount > count), "paint after redraw");
	}

	@Test
	public void test_paintAfterResize() {
		assertTrue(processEventsUntil(() -> paintCount > 0), "initial paint");
		int count = paintCount;
		shell.setSize(300, 250);
		assertTrue(processEventsUntil(() -> paintCount > count), "paint after resize");
	}

	private boolean processEventsUntil(BooleanSupplier condition) {
		long end = System.currentTimeMillis() + 5000;
		while (!condition.getAsBoolean() && System.currentTimeMillis() < end) {
			if (!display.readAndDispatch()) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					return condition.getAsBoolean();
				}
			}
		}
		return condition.getAsBoolean();
	}
}
