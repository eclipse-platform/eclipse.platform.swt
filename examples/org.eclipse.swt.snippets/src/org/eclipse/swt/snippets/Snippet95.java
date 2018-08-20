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
 * GC example snippet: capture a widget image with a GC
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet95 {

public static void main(String[] args) {
	final Display display = new Display();
	final Shell shell = new Shell(display);
	shell.setText("Widget");

	final Table table = new Table(shell, SWT.MULTI);
	table.setLinesVisible(true);
	table.setBounds(10, 10, 100, 100);
	for (int i = 0; i < 9; i++) {
		new TableItem(table, SWT.NONE).setText("item" + i);
	}

	Button button = new Button(shell, SWT.PUSH);
	button.setText("Capture");
	button.pack();
	button.setLocation(10, 140);
	button.addListener(SWT.Selection, event -> {
		Point tableSize = table.getSize();
		GC gc = new GC(table);
		final Image image =
			new Image(display, tableSize.x, tableSize.y);
		gc.copyArea(image, 0, 0);
		gc.dispose();

		Shell popup = new Shell(shell);
		popup.setText("Image");
		popup.addListener(SWT.Close, e -> image.dispose());

		Canvas canvas = new Canvas(popup, SWT.NONE);
		canvas.setBounds(10, 10, tableSize.x+10, tableSize.y+10);
		canvas.addPaintListener(e -> e.gc.drawImage(image, 0, 0));
		popup.pack();
		popup.open();
	});
	shell.pack();
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	display.dispose();
}
}
