/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
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
 * SWT Table snippet: scroll a Table one "page" at a time.
 * 
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet332 {

public static void main(String [] args) {
	Display display = new Display();
	Shell shell = new Shell(display);
	shell.setBounds(10,10,300,300);
	shell.setLayout(new GridLayout(2,true));
	final Table table = new Table(shell, SWT.NONE);
	GridData data = new GridData(GridData.FILL_BOTH);
	data.horizontalSpan = 2;
	table.setLayoutData(data);
	for (int i = 0; i < 99; i++) {
		new TableItem(table, SWT.NONE).setText("item " + i);
	}
	Button upButton = new Button(shell, SWT.PUSH);
	upButton.setText("Scroll up one page");
	upButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
	upButton.addListener(SWT.Selection, new Listener() {
		public void handleEvent(Event event) {
			int height = table.getClientArea().height;
			int visibleItemCount = height / table.getItemHeight();
			int topIndex = table.getTopIndex();
			int newTopIndex = Math.max(0, topIndex - visibleItemCount);
			if (topIndex != newTopIndex) {
				table.setTopIndex(newTopIndex);
			}
		}
	});
	Button downButton = new Button(shell, SWT.PUSH);
	downButton.setText("Scroll down one page");
	downButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
	downButton.addListener(SWT.Selection, new Listener() {
		public void handleEvent(Event event) {
			int height = table.getClientArea().height;
			int visibleItemCount = height / table.getItemHeight();
			int topIndex = table.getTopIndex();
			int newTopIndex = Math.min(table.getItemCount(), topIndex + visibleItemCount);
			if (topIndex != newTopIndex) {
				table.setTopIndex(newTopIndex);
			}
		}
	});
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	display.dispose();
}

}
