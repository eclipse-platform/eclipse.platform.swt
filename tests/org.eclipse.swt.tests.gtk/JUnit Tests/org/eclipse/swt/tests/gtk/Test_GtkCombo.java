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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@SuppressWarnings("restriction")
public class Test_GtkCombo {
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
	 * On GTK4 typed text goes into the GtkText delegate of the entry of an editable
	 * Combo, which does not forward insert-text and delete-text to the entry. Insert
	 * and delete text there as typing does and check that Verify is sent for both.
	 */
	@Test
	public void test_verifyForDelegateInput() {
		assumeTrue(GTK.GTK4, "only GTK4 entries type into a GtkText delegate");
		Combo combo = new Combo(shell, SWT.DROP_DOWN);
		int[] verifyCount = new int[1];
		combo.addListener(SWT.Verify, e -> {
			verifyCount[0]++;
			if (e.text.contains("x")) {
				e.doit = false;
			} else {
				e.text = e.text.toUpperCase();
			}
		});
		long delegate = GTK4.gtk_editable_get_delegate(GTK4.gtk_combo_box_get_child(combo.handle));
		insert(delegate, "ab", 0);
		insert(delegate, "x", 2);
		GTK.gtk_editable_delete_text(delegate, 0, 1);
		assertEquals("B", combo.getText());
		assertEquals(3, verifyCount[0]);
	}

	private static void insert(long editable, String string, int position) {
		byte[] buffer = string.getBytes(StandardCharsets.UTF_8);
		GTK.gtk_editable_insert_text(editable, buffer, buffer.length, new int[] { position });
	}
}
