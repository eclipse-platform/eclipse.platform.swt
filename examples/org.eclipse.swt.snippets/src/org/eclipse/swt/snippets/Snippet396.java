/*******************************************************************************
 * Copyright (c) 2026 vogella GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Lars Vogel - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * CTabFolder example snippet: give single tabs their own background colors,
 * for example to mark the editors of one connection or environment.
 *
 * For a list of all SWT example snippets see
 * https://eclipse.dev/eclipse/swt/snippets/
 *
 * @since 3.136
 */
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet396 {

public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new Shell(display);
	shell.setText("Snippet 396");
	shell.setLayout(new FillLayout());

	CTabFolder folder = new CTabFolder(shell, SWT.BORDER);
	String[] names = { "Default", "Production", "Test", "Development", "Default" };
	RGB[] backgrounds = { null, new RGB(255, 205, 205), new RGB(255, 240, 190), new RGB(205, 240, 205), null };
	RGB[] selectionBackgrounds = { null, new RGB(240, 130, 130), new RGB(245, 210, 100), new RGB(130, 210, 130), null };
	for (int i = 0; i < names.length; i++) {
		CTabItem item = new CTabItem(folder, SWT.CLOSE);
		item.setText(names[i]);
		if (backgrounds[i] != null) {
			item.setBackground(new Color(backgrounds[i]));
			item.setSelectionBackground(new Color(selectionBackgrounds[i]));
		}
		Text text = new Text(folder, SWT.MULTI);
		text.setText("Content of " + names[i]);
		item.setControl(text);
	}
	folder.setSelection(1);

	shell.setSize(500, 200);
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	display.dispose();
}
}
