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

	text.addListener(SWT.KeyDown, new Listener() {
		public void handleEvent(Event event) {
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
		}
	});
	text.addListener(SWT.Modify, new Listener() {
		public void handleEvent(Event event) {
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
		}
	});

	table.addListener(SWT.DefaultSelection, new Listener() {
		public void handleEvent(Event event) {
			text.setText(table.getSelection()[0].getText());
			popupShell.setVisible(false);
		}
	});
	table.addListener(SWT.KeyDown, new Listener() {
		public void handleEvent(Event event) {
			if (event.keyCode == SWT.ESC) {
				popupShell.setVisible(false);
			}
		}
	});

	Listener focusOutListener = new Listener() {
		public void handleEvent(Event event) {
			display.asyncExec(new Runnable() {
				public void run() {
					if (display.isDisposed()) return;
					Control control = display.getFocusControl();
					if (control == null || (control != text && control != table)) {
						popupShell.setVisible(false);
					}
				}
			});
		}
	};
	table.addListener(SWT.FocusOut, focusOutListener);
	text.addListener(SWT.FocusOut, focusOutListener);

	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	display.dispose();
}

}
