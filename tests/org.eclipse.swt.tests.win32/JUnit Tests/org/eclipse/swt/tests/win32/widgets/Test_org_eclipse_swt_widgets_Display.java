/*******************************************************************************
 * Copyright (c) 2021 Joerg Kubitz
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Joerg Kubitz - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.win32.widgets;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Test_org_eclipse_swt_widgets_Display {

	private Display display;
	private Shell shell;

	@BeforeEach
	public void setup() {
		display = new Display();
		shell = new Shell(display);
	}

	@AfterEach
	public void teardown() {
		shell.dispose();
		display.dispose();
	}

	@Test
	public void test_isXMouseActive() throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		Method method = display.getClass().getDeclaredMethod("isXMouseActive");
		method.setAccessible(true);

		boolean xMouseActive = (boolean) method.invoke(display);
		System.out.println("org.eclipse.swt.widgets.Display.isXMouseActive(): " + xMouseActive);
	}

	@Test
	public void test_mixedLfAndCrfl() {
		//Use text control for testing since Display.withCrLf() is package private
		Text text = new Text(shell, SWT.None);

		text.setText("First Line \n second line \r\n third line");
		assertEquals("First Line \r\n second line \r\n third line", text.getText());

		text.setText("First Line \n second line \r\n third line\n");
		assertEquals("First Line \r\n second line \r\n third line\r\n", text.getText());
	}
}
