/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * Lazy tab content example snippet: a TabFolder and a CTabFolder created with
 * SWT.VIRTUAL send SWT.SetData the first time a tab is selected while the folder
 * is showing; the listener creates the tab's content and calls setControl. At
 * startup only the selected tab's content exists.
 *
 * For a list of all SWT example snippets see
 * https://eclipse.dev/eclipse/swt/snippets/
 *
 * @since 3.137
 */
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet396 {

static Composite content (Composite parent, String name) {
	Composite composite = new Composite (parent, SWT.NONE);
	composite.setLayout (new GridLayout (2, false));
	for (int i = 0; i < 40; i++) {
		new Label (composite, SWT.NONE).setText (name + " field " + i);
		new Text (composite, SWT.BORDER).setLayoutData (new GridData (SWT.FILL, SWT.CENTER, true, false));
	}
	return composite;
}

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	shell.setText ("Snippet 396");
	shell.setLayout (new FillLayout (SWT.VERTICAL));

	TabFolder folder = new TabFolder (shell, SWT.VIRTUAL);
	folder.addListener (SWT.SetData, event -> {
		TabItem item = (TabItem) event.item;
		item.setControl (content (folder, item.getText ()));
		System.out.println ("TabFolder created the content of tab " + event.index);
	});
	for (int i = 0; i < 50; i++) {
		new TabItem (folder, SWT.NONE).setText ("Tab " + i);
	}

	CTabFolder cfolder = new CTabFolder (shell, SWT.VIRTUAL | SWT.BORDER);
	cfolder.addListener (SWT.SetData, event -> {
		CTabItem item = (CTabItem) event.item;
		item.setControl (content (cfolder, item.getText ()));
		System.out.println ("CTabFolder created the content of tab " + event.index);
	});
	for (int i = 0; i < 50; i++) {
		new CTabItem (cfolder, SWT.NONE).setText ("CTab " + i);
	}
	cfolder.setSelection (0);

	shell.setSize (700, 700);
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}
