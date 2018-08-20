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
 * Text example snippet: implement content assist
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet320 {

public static void main(String [] args) {
	final Display display = new Display();
	final Shell shell = new Shell(display);
	shell.setLayout(new GridLayout());
	final Text text = new Text(shell, SWT.SINGLE | SWT.BORDER);
	text.setLayoutData(new GridData(150, SWT.DEFAULT));
	shell.pack();
	shell.open();

	final Shell popupShell = new Shell(display, SWT.ON_TOP);
	popupShell.setLayout(new FillLayout());
	final Table table = new Table(popupShell, SWT.SINGLE);
	for (int i = 0; i < 5; i++) {
		new TableItem(table, SWT.NONE);
	}

	text.addListener(SWT.KeyDown, event -> {
		switch (event.keyCode) {
			case SWT.ARROW_DOWN:
				int index = (table.getSelectionIndex() + 1) % table.getItemCount();
				table.setSelection(index);
				event.doit = false;
				break;
			case SWT.ARROW_UP:
				index = table.getSelectionIndex() - 1;
				if (index < 0) index = table.getItemCount() - 1;
				table.setSelection(index);
				event.doit = false;
				break;
			case SWT.CR:
				if (popupShell.isVisible() && table.getSelectionIndex() != -1) {
					text.setText(table.getSelection()[0].getText());
					popupShell.setVisible(false);
				}
				break;
			case SWT.ESC:
				popupShell.setVisible(false);
				break;
		}
	});
	text.addListener(SWT.Modify, event -> {
		String string = text.getText();
		if (string.length() == 0) {
			popupShell.setVisible(false);
		} else {
			TableItem[] items = table.getItems();
			for (int i = 0; i < items.length; i++) {
				items[i].setText(string + '-' + i);
			}
			Rectangle textBounds = display.map(shell, null, text.getBounds());
			popupShell.setBounds(textBounds.x, textBounds.y + textBounds.height, textBounds.width, 150);
			popupShell.setVisible(true);
		}
	});

	table.addListener(SWT.DefaultSelection, event -> {
		text.setText(table.getSelection()[0].getText());
		popupShell.setVisible(false);
	});
	table.addListener(SWT.KeyDown, event -> {
		if (event.keyCode == SWT.ESC) {
			popupShell.setVisible(false);
		}
	});

	Listener focusOutListener = event -> display.asyncExec(() -> {
		if (display.isDisposed()) return;
		Control control = display.getFocusControl();
		if (control == null || (control != text && control != table)) {
			popupShell.setVisible(false);
		}
	});
	table.addListener(SWT.FocusOut, focusOutListener);
	text.addListener(SWT.FocusOut, focusOutListener);

	shell.addListener(SWT.Move, event -> popupShell.setVisible(false));

	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	display.dispose();
}

}
