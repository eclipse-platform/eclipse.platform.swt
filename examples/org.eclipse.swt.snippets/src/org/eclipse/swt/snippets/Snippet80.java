/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
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
 * Tree example snippet: limit selection to items that match a pattern
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet80 {
	
public static void main(String[] args) {
	final Display display = new Display();
	final Shell shell = new Shell(display);
	shell.setLayout(new FillLayout());
	final Tree tree = new Tree(shell, SWT.BORDER | SWT.MULTI);
	for (int i = 0; i < 2; i++) {
		TreeItem item = new TreeItem(tree, SWT.NONE);
		item.setText("item " + i);
		for (int j = 0; j < 2; j++) {
			TreeItem subItem = new TreeItem(item, SWT.NONE);
			subItem.setText("item " + j);
			for (int k = 0; k < 2; k++) {
				TreeItem subsubItem = new TreeItem(subItem, SWT.NONE);
				subsubItem.setText("item " + k);
			}
		}
	}
	
	tree.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			TreeItem[] selection = tree.getSelection();
			TreeItem[] revisedSelection = new TreeItem[0];
			for (int i = 0; i < selection.length; i++) {
				String text = selection[i].getText();
				if (text.indexOf("1") > 0) {
					TreeItem[] newSelection = new TreeItem[revisedSelection.length + 1];
					System.arraycopy(revisedSelection, 0, newSelection, 0, revisedSelection.length);
					newSelection[revisedSelection.length] = selection[i];
					revisedSelection = newSelection;
				}
			}
			tree.setSelection(revisedSelection);
		}
	});

	shell.setSize(300, 300);
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch())
			display.sleep();
	}
	display.dispose();
}
}
