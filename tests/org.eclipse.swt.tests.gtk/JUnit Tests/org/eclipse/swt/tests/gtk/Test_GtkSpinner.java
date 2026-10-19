/*******************************************************************************
 * Copyright (c) 2026 Aleksandar Kurtakov and others.
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
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.nio.charset.StandardCharsets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.gtk.GTK;
import org.eclipse.swt.internal.gtk4.GTK4;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@SuppressWarnings("restriction")
public class Test_GtkSpinner {
	private Shell shell;

	@BeforeEach
	public void setUp() {
		shell = new Shell(Display.getDefault());
	}

	@AfterEach
	public void tearDown() {
		shell.dispose();
	}

	/**
	 * On GTK4 typed text and value changes go into the GtkText delegate of the
	 * GtkSpinButton, which does not forward insert-text and delete-text. Insert and
	 * delete text there as typing does, set a value, and check that Verify is sent.
	 */
	@Test
	public void test_verifyForDelegateInput() {
		assumeTrue(GTK.GTK4, "only GTK4 spin buttons type into a GtkText delegate");
		Spinner spinner = new Spinner(shell, SWT.NONE);
		int[] verifyCount = new int[1];
		spinner.addListener(SWT.Verify, e -> {
			verifyCount[0]++;
			if (e.text.contains("9")) e.doit = false;
		});
		long delegate = GTK4.gtk_editable_get_delegate(spinner.handle);
		GTK.gtk_editable_delete_text(delegate, 0, -1);
		insert(delegate, "12", 0);
		insert(delegate, "9", 2);
		GTK.gtk_editable_delete_text(delegate, 0, 1);
		assertEquals("2", spinner.getText());
		assertEquals(4, verifyCount[0]);

		verifyCount[0] = 0;
		spinner.setSelection(7);
		assertEquals("7", spinner.getText());
		assertEquals(2, verifyCount[0]);
	}

	private static void insert(long editable, String string, int position) {
		byte[] buffer = string.getBytes(StandardCharsets.UTF_8);
		GTK.gtk_editable_insert_text(editable, buffer, buffer.length, new int[] { position });
	}
}
