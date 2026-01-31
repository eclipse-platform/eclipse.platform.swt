/*******************************************************************************
 * Copyright (c) 2026 Patrick Ziegler and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Patrick Ziegler - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.win32;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.AutoscalingMode;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Win32AutoScaleTests {
	private Shell shell;

	@BeforeEach
	public void setUp() {
		shell = new Shell();
	}

	@AfterEach
	public void tearDown() {
		shell.dispose();
	}

	@Test
	public void testAutoScaleDisabledProperty() {
		shell.setData("AUTOSCALE_DISABLED", true);
		assertEquals(AutoscalingMode.DISABLED_INHERITED, SwtWin32TestUtil.getAutoscalingMode(shell));
		Composite child = new Composite(shell, SWT.NONE);
		assertEquals(AutoscalingMode.DISABLED_INHERITED, SwtWin32TestUtil.getAutoscalingMode(child));
	}

	@Test
	public void testAutoScaleDisabled() {
		shell.setAutoscalingMode(AutoscalingMode.DISABLED);
		assertEquals(AutoscalingMode.DISABLED, SwtWin32TestUtil.getAutoscalingMode(shell));
		Composite child = new Composite(shell, SWT.NONE);
		assertEquals(AutoscalingMode.ENABLED, SwtWin32TestUtil.getAutoscalingMode(child));
	}

	@Test
	public void testPropagateAutoScaleDisabledProperty() {
		shell.setData("AUTOSCALE_DISABLED", true);
		shell.setData("PROPOGATE_AUTOSCALE_DISABLED", true);
		assertEquals(AutoscalingMode.DISABLED_INHERITED, SwtWin32TestUtil.getAutoscalingMode(shell));
		Composite child = new Composite(shell, SWT.NONE);
		assertEquals(AutoscalingMode.DISABLED_INHERITED, SwtWin32TestUtil.getAutoscalingMode(child));
	}

	@Test
	public void testPropagateAutoScaleDisabled() {
		shell.setAutoscalingMode(AutoscalingMode.DISABLED_INHERITED);
		assertEquals(AutoscalingMode.DISABLED_INHERITED, SwtWin32TestUtil.getAutoscalingMode(shell));
		Composite child = new Composite(shell, SWT.NONE);
		assertEquals(AutoscalingMode.DISABLED_INHERITED, SwtWin32TestUtil.getAutoscalingMode(child));
	}
}
