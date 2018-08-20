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
 * Composite example snippet: create and dispose children of a composite
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet98 {

static int pageNum = 0;
static Composite pageComposite;

public static void main(String args[]) {
	Display display = new Display();
	final Shell shell = new Shell(display);
	shell.setLayout(new GridLayout());
	Button button = new Button(shell, SWT.PUSH);
	button.setText("Push");
	pageComposite = new Composite(shell, SWT.NONE);
	pageComposite.setLayout(new GridLayout());
	pageComposite.setLayoutData(new GridData());

	button.addListener(SWT.Selection, event -> {
		if ((pageComposite != null) && (!pageComposite.isDisposed())) {
			pageComposite.dispose();
		}
		pageComposite = new Composite(shell, SWT.NONE);
		pageComposite.setLayout(new GridLayout());
		pageComposite.setLayoutData(new GridData());
		if (pageNum++ % 2 == 0) {
			Table table = new Table(pageComposite, SWT.BORDER);
			table.setLayoutData(new GridData());
			for (int i = 0; i < 5; i++) {
				new TableItem(table, SWT.NONE).setText("table item " + i);
			}
		} else {
			new Button(pageComposite, SWT.RADIO).setText("radio");
		}
		shell.layout(true);
	});

	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch())
			display.sleep();
	}
	display.dispose();
}
}
