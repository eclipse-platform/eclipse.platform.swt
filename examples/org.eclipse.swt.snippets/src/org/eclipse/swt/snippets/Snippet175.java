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

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/*
 * Exclude a widget from a GridLayout
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.1
 */

public class Snippet175 {

public static void main(String[] args) {

	Display display = new Display();
	final Shell shell = new Shell(display);
	shell.setLayout(new GridLayout(3, false));

	Button b = new Button(shell, SWT.PUSH);
	b.setText("Button 0");

	final Button bHidden = new Button(shell, SWT.PUSH);
	bHidden.setText("Button 1");
	GridData data = new GridData();
	data.exclude = true;
	data.horizontalSpan = 2;
	data.horizontalAlignment = SWT.FILL;
	bHidden.setLayoutData(data);

	b = new Button(shell, SWT.PUSH);
	b.setText("Button 2");
	b = new Button(shell, SWT.PUSH);
	b.setText("Button 3");
	b = new Button(shell, SWT.PUSH);
	b.setText("Button 4");

	b = new Button(shell, SWT.CHECK);
	b.setText("hide");
	b.setSelection(true);
	b.addListener(SWT.Selection, e -> {
		Button b1 = (Button) e.widget;
		GridData data1 = (GridData) bHidden.getLayoutData();
		data1.exclude = b1.getSelection();
		bHidden.setVisible(!data1.exclude);
		shell.layout(false);
	});
	shell.setSize(400, 400);
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch())
			display.sleep();
	}
	display.dispose();
}
}
