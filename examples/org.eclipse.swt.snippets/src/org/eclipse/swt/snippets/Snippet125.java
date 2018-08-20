/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * Tool Tips example snippet: create fake tool tips for items in a table
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet125 {

public static void main (String[] args) {
	final Display display = new Display ();
	final Shell shell = new Shell (display);
	shell.setLayout (new FillLayout ());
	final Table table = new Table (shell, SWT.BORDER);
	for (int i = 0; i < 20; i++) {
		TableItem item = new TableItem (table, SWT.NONE);
		item.setText ("item " + i);
	}
	// Disable native tooltip
	table.setToolTipText ("");

	// Implement a "fake" tooltip
	final Listener labelListener = event -> {
		Label label = (Label)event.widget;
		Shell shell1 = label.getShell ();
		switch (event.type) {
			case SWT.MouseDown:
				Event e = new Event ();
				e.item = (TableItem) label.getData ("_TABLEITEM");
				// Assuming table is single select, set the selection as if
				// the mouse down event went through to the table
				table.setSelection (new TableItem [] {(TableItem) e.item});
				table.notifyListeners (SWT.Selection, e);
				shell1.dispose ();
				table.setFocus();
				break;
			case SWT.MouseExit:
				shell1.dispose ();
				break;
		}
	};

	Listener tableListener = new Listener () {
		Shell tip = null;
		Label label = null;
		@Override
		public void handleEvent (Event event) {
			switch (event.type) {
				case SWT.Dispose:
				case SWT.KeyDown:
				case SWT.MouseMove: {
					if (tip == null) break;
					tip.dispose ();
					tip = null;
					label = null;
					break;
				}
				case SWT.MouseHover: {
					TableItem item = table.getItem (new Point (event.x, event.y));
					if (item != null) {
						if (tip != null  && !tip.isDisposed ()) tip.dispose ();
						tip = new Shell (shell, SWT.ON_TOP | SWT.NO_FOCUS | SWT.TOOL);
						tip.setBackground (display.getSystemColor (SWT.COLOR_INFO_BACKGROUND));
						FillLayout layout = new FillLayout ();
						layout.marginWidth = 2;
						tip.setLayout (layout);
						label = new Label (tip, SWT.NONE);
						label.setForeground (display.getSystemColor (SWT.COLOR_INFO_FOREGROUND));
						label.setBackground (display.getSystemColor (SWT.COLOR_INFO_BACKGROUND));
						label.setData ("_TABLEITEM", item);
						label.setText (item.getText ());
						label.addListener (SWT.MouseExit, labelListener);
						label.addListener (SWT.MouseDown, labelListener);
						Point size = tip.computeSize (SWT.DEFAULT, SWT.DEFAULT);
						Rectangle rect = item.getBounds (0);
						Point pt = table.toDisplay (rect.x, rect.y);
						tip.setBounds (pt.x, pt.y, size.x, size.y);
						tip.setVisible (true);
					}
				}
			}
		}
	};
	table.addListener (SWT.Dispose, tableListener);
	table.addListener (SWT.KeyDown, tableListener);
	table.addListener (SWT.MouseMove, tableListener);
	table.addListener (SWT.MouseHover, tableListener);
	shell.pack ();
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch())
			display.sleep();
	}
	display.dispose();
}
}
