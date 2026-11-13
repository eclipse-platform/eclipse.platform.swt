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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class Test_cocoa_ReleasedChildren {

	private Shell shell;

	@AfterEach
	public void tearDown() {
		if (shell != null && !shell.isDisposed()) shell.dispose();
	}

	@Test
	public void childCreatedByDisposeListenerDoesNotOutliveParent() {
		shell = new Shell(Display.getDefault());
		Composite composite = new Composite(shell, SWT.NONE);
		Button first = new Button(composite, SWT.PUSH);
		Button[] late = new Button[1];
		first.addListener(SWT.Dispose, e -> late[0] = new Button(composite, SWT.PUSH));
		shell.open();

		assertDoesNotThrow(shell::dispose);
		// a child created by a dispose listener must be released with its parent
		assertNotNull(late[0]);
		assertTrue(late[0].isDisposed());
	}
}
