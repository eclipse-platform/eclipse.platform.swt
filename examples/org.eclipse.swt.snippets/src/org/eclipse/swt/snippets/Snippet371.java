/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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
 * CTabFolder example snippet: demonstration of a multi line CTabFolder
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 4.8
 */
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet371 {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		CTabFolder folder = new CTabFolder(shell, SWT.BORDER);

		CTabItem item1 = new CTabItem(folder, SWT.CLOSE);
		item1.setText("Item on one line");
		Text text1 = new Text(folder, SWT.MULTI);
		text1.setText("Content for Item 1");
		item1.setControl(text1);

		CTabItem item2 = new CTabItem(folder, SWT.CLOSE);
		item2.setText("Item on \ntwo lines");
		Text text2 = new Text(folder, SWT.MULTI);
		text2.setText("Content for Item 2");
		item2.setControl(text2);


		shell.setSize(new Point(300, 200));
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}

