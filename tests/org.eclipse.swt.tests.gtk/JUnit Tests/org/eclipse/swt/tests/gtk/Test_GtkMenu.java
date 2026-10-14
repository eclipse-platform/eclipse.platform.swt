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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.Converter;
import org.eclipse.swt.internal.gtk.GTK;
import org.eclipse.swt.internal.gtk.OS;
import org.eclipse.swt.internal.gtk4.GTK4;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@SuppressWarnings("restriction")
public class Test_GtkMenu {

	private Display display;
	private Shell oldShell;
	private Shell newShell;

	@BeforeEach
	public void setUp() {
		display = Display.getDefault();
		oldShell = new Shell(display);
		newShell = new Shell(display);
	}

	@AfterEach
	public void tearDown() {
		// The moved control and its menu go first: a popover left under oldShell made disposing it spin
		newShell.dispose();
		oldShell.dispose();
	}

	/**
	 * Eclipse 4 creates parts in a hidden shell and moves them into the workbench
	 * window. On GTK4 the items of the moved control's context menu must still
	 * find their actions, or they all show disabled.
	 */
	@Test
	public void test_popUpItemsSensitiveAfterControlMovedToAnotherShell() throws ReflectiveOperationException {
		assumeTrue(GTK.GTK4, "GTK4 menu items take their state from actions found through the widget hierarchy");
		Composite composite = new Composite(oldShell, SWT.NONE);
		Menu menu = new Menu(composite);
		composite.setMenu(menu);
		new MenuItem(menu, SWT.PUSH).setText("Enabled");
		new MenuItem(menu, SWT.PUSH).setText("Disabled");
		menu.getItem(1).setEnabled(false);

		composite.setParent(newShell);
		composite.setSize(100, 100);
		newShell.open();
		processEvents();

		// What a right-click on the control runs; it moves the popover under the control
		Method showMenu = Control.class.getDeclaredMethod("showMenu", int.class, int.class);
		showMenu.setAccessible(true);
		assertTrue((Boolean) showMenu.invoke(composite, 10, 10));
		processEvents();

		List<Long> buttons = new ArrayList<>();
		collectModelButtons(menu.handle, buttons);
		menu.setVisible(false);
		assertEquals(2, buttons.size());
		assertTrue(GTK.gtk_widget_get_sensitive(buttons.get(0)), "enabled item shows disabled");
		assertFalse(GTK.gtk_widget_get_sensitive(buttons.get(1)), "disabled item shows enabled");
	}

	private void collectModelButtons(long widget, List<Long> buttons) {
		String type = Converter.cCharPtrToJavaString(OS.g_type_name(OS.G_OBJECT_TYPE(widget)), false);
		if ("GtkModelButton".equals(type)) buttons.add(widget);
		for (long child = GTK4.gtk_widget_get_first_child(widget); child != 0; child = GTK4.gtk_widget_get_next_sibling(child)) {
			collectModelButtons(child, buttons);
		}
	}

	private void processEvents() {
		while (display.readAndDispatch()) {
		}
	}
}
