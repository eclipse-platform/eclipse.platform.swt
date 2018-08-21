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
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class Bug508245_TableSelectionListener {
	public static void main (String [] args) {
		Display display = new Display ();
		Shell shell = new Shell (display);
//		List listTableTree = new List (widget.shell, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
//		Table listTableTree = new Table (widget.shell, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		Tree listTableTree = new Tree (shell, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		for (int i=0; i<12; i++) {
//			widget.list.add("Item " + i);
//			
//			TableItem item = new TableItem (listTableTree, 0);
//			item.setText ("Item " + i);
			
			TreeItem item = new TreeItem (listTableTree, 0);
			item.setText ("Item " + i);
		}
		Rectangle clientArea = shell.getClientArea ();
		
		
		listTableTree.setBounds (clientArea.x, clientArea.y, 400, 400);
		shell.setSize (400, 400);
		shell.open ();

		listTableTree.addSelectionListener(new SelectionListener() {

			/**
			 * Selection event occurs when clicking or using arrow keys.
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				if ((e.stateMask & SWT.MOD1) != 0 | ((e.stateMask & SWT.MOD4) != 0)) {
					System.out.println("Selection event" + "\n"
							+ "		Cocoa: Ctrl+Click (Mod4) or Cmd+Click (Mod1) \n"
							+ "		Gtk:   Ctrl+Click (mod1) \n"
							+ "		Win32: Ctrl+Click (mod1)");
				} else if ((e.stateMask & SWT.MOD2) != 0) {
					System.out.println("Selection event" + "\n"
							+ "		Cocoa/Gtk/Win32: Shift+Click (Mod2) \n");
				} else if ((e.stateMask & SWT.MOD3) != 0) {
					System.out.println("Selection event" + "\n"
							+ "		Cocoa/Gtk/Win32: Alt+Click (Mod3)");     
				} else {  // Do not use (e.stateMask == 0), as on Win32/Cocoa StateMask is not 0 (=524288?) when no modifiers are pressed.
					System.out.println("Selection event" + "\n"
							+ "		Cocoa/Gtk/Win32: Arrow keys and click (can be first click in a double click");
				}
			}

			/**
			 * Default selection triggered upon "double click" or "enter" button.
			 */
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				if ((e.stateMask & SWT.MOD1) != 0 | ((e.stateMask & SWT.MOD4) != 0)) {
					System.out.println("Default selection event" + "\n"
							+ "		Cocoa: Ctrl+Enter (Mod4) or Cmd+Enter/Cmd+DoubleClick (Mod1) \n"
							+ "		Gtk:   Ctrl+Enter or Ctrl+Doubleclick (mod1) \n"
							+ "		Win32: Ctrl+Enter does NOT trigger default selection. Ctrl+DoubleClick does\n");
				} else if ((e.stateMask & SWT.MOD2) != 0) {
					System.out.println("Default selection event" + "\n"
							+ "		Cocoa/Gtk/Win32: Shift+Enter or Shift+DoubleClick(Mod2) \n");
				} else if ((e.stateMask & SWT.MOD3) != 0) {
					System.out.println("Default selection event" + "\n"
							+ "		Cocoa/Gtk/Win32: Alt+Enter or Alt+DoubleClick (Mod3)");     
				} else {
					System.out.println("Default selection event \n"
							+ "		Cocoa/Gtk/Win32: Enter or double click (no mod)");
				}
			}
		});

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}
}
