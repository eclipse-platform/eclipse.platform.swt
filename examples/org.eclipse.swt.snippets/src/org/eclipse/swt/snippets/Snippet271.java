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
 * Table snippet: specify custom content dimensions in a table with no columns
 *
 * For a detailed explanation of this snippet see
 * http://www.eclipse.org/articles/Article-CustomDrawingTableAndTreeItems/customDraw.htm#_example1
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.2
 */
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

public class Snippet271 {

public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new Shell(display);
	shell.setBounds(10,10,200,250);
	final Table table = new Table(shell, SWT.NONE);
	table.setBounds(10,10,150,200);
	table.setLinesVisible(true);
	for (int i = 0; i < 5; i++) {
		new TableItem(table, SWT.NONE).setText("item " + i);
	}

	/*
	 * NOTE: MeasureItem is called repeatedly.  Therefore it is critical
	 * for performance that this method be as efficient as possible.
	 */
	table.addListener(SWT.MeasureItem, event -> {
		int clientWidth = table.getClientArea().width;
		event.height = event.gc.getFontMetrics().getHeight() * 2;
		event.width = clientWidth * 2;
	});

	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	display.dispose();
}
}
