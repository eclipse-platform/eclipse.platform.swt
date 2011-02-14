/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * Table example snippet: update table item text
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;

public class Snippet103 {

static char content = 'a';
public static void main(String[] args) {
	final Display display = new Display();
	Shell shell = new Shell(display);
	shell.setBounds(10, 10, 200, 240);
	Table table = new Table(shell, SWT.NONE);
	Rectangle clientArea = shell.getClientArea ();
	table.setBounds (clientArea.x + 10, clientArea.y + 10, 160, 160);

	final TableItem[] items = new TableItem[4];
	for (int i = 0; i < 4; i++) {
		new TableColumn(table, SWT.NONE).setWidth(40);
	}
	for (int i = 0; i < 4; i++) {
		items[i] = new TableItem(table, SWT.NONE);
		populateItem(items[i]);
	}

	Button button = new Button(shell, SWT.PUSH);
	button.setText("Change");
	button.pack();
	button.setLocation(10, 180);
	button.addListener(SWT.Selection, new Listener() {
		public void handleEvent(Event event) {
			for (int i = 0; i < 4; i++) {
				populateItem(items[i]);
			}
		}
	});

	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	display.dispose();
}

static void populateItem(TableItem item) {
	String stringContent = String.valueOf(content);
	item.setText(
		new String[] {
			stringContent,
			stringContent,
			stringContent,
			stringContent });
	content++;
	if (content > 'z') content = 'a';
}

}
