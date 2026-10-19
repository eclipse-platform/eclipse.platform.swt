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
package org.eclipse.swt.tests.cocoa;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Cocoa specific tests for the cached default fonts of controls.
 */
public class Test_cocoa_DefaultFont {

	private Shell shell;

	@BeforeEach
	public void setUp() {
		shell = new Shell(Display.getDefault());
	}

	@AfterEach
	public void tearDown() {
		shell.dispose();
	}

	@Test
	public void test_getFont_sharedAcrossControls() {
		Button first = new Button(shell, SWT.PUSH);
		Button second = new Button(shell, SWT.PUSH);
		assertSame(first.getFont(), second.getFont());
		assertSame(new ToolBar(shell, SWT.NONE).getFont(), new ToolBar(shell, SWT.NONE).getFont());
	}

	@Test
	public void test_getFont_afterApplicationDisposedIt() {
		Button button = new Button(shell, SWT.PUSH);
		button.getFont().dispose();
		Font font = button.getFont();
		assertFalse(font.isDisposed());
		assertSame(font, new Button(shell, SWT.PUSH).getFont());
	}
}
