/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
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
 * RowLayout snippet: align widgets in a row
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.1
 */
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet176 {

	public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new Shell(display);
	RowLayout layout = new RowLayout(SWT.HORIZONTAL);
	layout.wrap = true;
	layout.fill = false;
	layout.justify = true;
	shell.setLayout(layout);

	Button b = new Button(shell, SWT.PUSH);
	b.setText("Button 1");
	b = new Button(shell, SWT.PUSH);

	b.setText("Button 2");

	b = new Button(shell, SWT.PUSH);
	b.setText("Button 3");

	b = new Button(shell, SWT.PUSH);
	b.setText("Not shown");
	b.setVisible(false);
	RowData data = new RowData();
	data.exclude = true;
	b.setLayoutData(data);

	b = new Button(shell, SWT.PUSH);
	b.setText("Button 200 high");
	data = new RowData();
	data.height = 200;
	b.setLayoutData(data);

	b = new Button(shell, SWT.PUSH);
	b.setText("Button 200 wide");
	data = new RowData();
	data.width = 200;
	b.setLayoutData(data);

	shell.pack();
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch())
			display.sleep();
	}
	display.dispose();
}
}
