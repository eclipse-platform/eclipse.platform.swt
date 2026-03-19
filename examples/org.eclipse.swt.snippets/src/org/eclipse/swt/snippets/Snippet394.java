/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
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
 * CTabFolder example: dirty indicator using bullet dot on close button
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet394 {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout());
		shell.setText("CTabFolder Dirty Indicator");

		CTabFolder folder = new CTabFolder(shell, SWT.CLOSE | SWT.BORDER);
		folder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		folder.setDirtyIndicatorCloseStyle(true);

		for (int i = 0; i < 4; i++) {
			CTabItem item = new CTabItem(folder, SWT.NONE);
			item.setText("Tab " + i);
			Text text = new Text(folder, SWT.MULTI | SWT.WRAP);
			text.setText("Content for tab " + i);
			item.setControl(text);
		}

		// Mark tabs 0 and 2 as dirty
		folder.getItem(0).setShowDirty(true);
		folder.getItem(2).setShowDirty(true);
		folder.setSelection(0);

		Button toggleButton = new Button(shell, SWT.PUSH);
		toggleButton.setText("Toggle dirty on selected tab");
		toggleButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		toggleButton.addListener(SWT.Selection, e -> {
			CTabItem selected = folder.getSelection();
			if (selected != null) {
				selected.setShowDirty(!selected.getShowDirty());
			}
		});

		shell.setSize(400, 300);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
