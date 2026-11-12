/*******************************************************************************
 * Copyright (c) 2026 Vector Informatik GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Vector Informatik GmbH - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.win32.widgets;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the win32-specific handling of {@link SWT#TRANSPARENT} composites,
 * which requires their parent window to use composited drawing.
 */
public class Test_org_eclipse_swt_widgets_Composite {

	private static Display display;
	private Shell shell;

	@BeforeAll
	public static void setUpClass() {
		display = Display.getDefault();
	}

	@AfterAll
	public static void tearDownClass() {
		display.dispose();
	}

	@BeforeEach
	public void setUp() {
		shell = new Shell(display);
	}

	@AfterEach
	public void tearDown() {
		shell.dispose();
	}

	@Test
	public void test_parentStaysCompositedWhileTransparentChildrenExist() {
		Composite first = new Composite(shell, SWT.TRANSPARENT);
		Composite second = new Composite(shell, SWT.TRANSPARENT);
		assertTrue(isComposited(shell), "parent must be composited for its transparent children");

		first.dispose();
		assertTrue(isComposited(shell), "parent must stay composited for the remaining transparent child");

		second.dispose();
		assertFalse(isComposited(shell), "parent must not stay composited without transparent children");
	}

	@Test
	public void test_parentStopsBeingCompositedWithOpaqueChildrenOnly() {
		Composite transparent = new Composite(shell, SWT.TRANSPARENT);
		new Composite(shell, SWT.NONE);
		assertTrue(isComposited(shell), "parent must be composited for its transparent child");

		transparent.dispose();
		assertFalse(isComposited(shell), "an opaque child must not keep the parent composited");
	}

	private static boolean isComposited(Control control) {
		return (OS.GetWindowLong(control.handle, OS.GWL_EXSTYLE) & OS.WS_EX_COMPOSITED) != 0;
	}
}
