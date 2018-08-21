/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

// clicking on a row item and typing appends characters.
// Gtk2: Works as expected.
// Gtk3: Spam of errors, black second window.
//(SWT:7324): Gdk-WARNING **: gdk_window_new(): parent is destroyed
//(SWT:7324): Gdk-CRITICAL **: gdk_window_set_user_data: assertion 'GDK_IS_WINDOW (window)' failed
//(SWT:7324): Gdk-CRITICAL **: gdk_window_get_scale_factor: assertion 'GDK_IS_WINDOW (window)' failed
//(SWT:7324): GLib-GObject-CRITICAL **: g_object_ref: assertion 'G_IS_OBJECT (object)' failed

public class Bug510803_TabFolder_editable_table_brokenGtk3 {
	public static void main(String[] args) {
		Shell shell = shellSetup();

		final TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("Hello tab");

		Table table = new Table(tabFolder, SWT.NONE);
		TableItem tableItem = new TableItem(table, SWT.None);
		tableItem.setText("Item 1");
		TableItem tableItem2 = new TableItem(table, SWT.None);
		tableItem2.setText("Item 2");

		// Notes:
		// - Seems to occur with any control, not just Text. (Tested with Button also)
		final Text cellEditorText = new Text(table, SWT.SINGLE); // Note,
		cellEditorText.setText("Hello world");

//		 Listeners that make typing into Table edit controls. Useful to test
//		 functionality, but errors occur without the listeners also.
		 table.addMouseListener(new MouseAdapter() {
		 @Override
		public void mouseUp(MouseEvent e) {
		 cellEditorText.setFocus();
		 }
		 });

		 cellEditorText.addKeyListener(new KeyAdapter() {
		 @Override
		public void keyPressed(KeyEvent e) {
		 TableItem selectedItem = table.getSelection()[0];
		 selectedItem.setText(selectedItem.getText() + e.character);
		 }
		 });

		// Location of setControl() method call has an impact.
		// If it's before 'Text' creation, no errors are thrown into the
		// console.
		tabItem.setControl(table);

		mainEventLoop(shell);
	}

	private static Shell shellSetup() {
		final Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		return shell;
	}

	private static void mainEventLoop(Shell shell) {
		Display display = shell.getDisplay();
		shell.open();
		shell.setSize(200, 300);
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
