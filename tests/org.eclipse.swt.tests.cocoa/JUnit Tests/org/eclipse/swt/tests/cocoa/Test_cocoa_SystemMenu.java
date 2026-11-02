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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.junit.jupiter.api.Test;

/**
 * Checks the accelerators synced from the native items of the application menu.
 */
public class Test_cocoa_SystemMenu {

	@Test
	public void test_getAccelerator() {
		Menu menu = Display.getDefault().getSystemMenu();
		assertEquals(0, item(menu, SWT.ID_ABOUT).getAccelerator());
		assertEquals(0, item(menu, SWT.ID_SHOW_ALL).getAccelerator());
		assertEquals(SWT.COMMAND | 'q', item(menu, SWT.ID_QUIT).getAccelerator());
		assertEquals(SWT.COMMAND | SWT.ALT | 'h', item(menu, SWT.ID_HIDE_OTHERS).getAccelerator());
		for (MenuItem item : menu.getItems()) {
			if ((item.getStyle() & SWT.SEPARATOR) != 0) {
				assertEquals(0, item.getAccelerator());
			}
		}
	}

	private static MenuItem item(Menu menu, int id) {
		for (MenuItem item : menu.getItems()) {
			if (item.getID() == id) {
				return item;
			}
		}
		throw new AssertionError("No menu item with id " + id);
	}
}
