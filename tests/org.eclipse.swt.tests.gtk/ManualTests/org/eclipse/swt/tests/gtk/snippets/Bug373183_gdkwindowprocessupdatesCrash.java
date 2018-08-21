/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;


import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

public class Bug373183_gdkwindowprocessupdatesCrash {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("SWT cause jvm crash when handle Setdata event");
		shell.setLayout(new FillLayout());
		final Tree tree = new Tree(shell, SWT.VIRTUAL | SWT.BORDER
				| SWT.FULL_SELECTION);

		for (int i = 0; i < 2; i++) {
			TreeColumn column = new TreeColumn(tree, SWT.NONE);
			column.setText("Column " + i);
			column.setWidth(100);
		}

		String[] rowTexts = new String[] { "aa", "bb", "cc", "aa", "bb", "cc"};

//		String[] rowTexts = new String[] { "aa", "bb", "cc", "aa", "bb", "cc",
//				"aa", "bb", "cc", "aa", "bb", "cc", "aa", "bb", "cc"};

		tree.setData(rowTexts);
		tree.addListener(SWT.SetData, new Listener() {
			@Override
			public void handleEvent(Event event) {
				TreeItem item = (TreeItem) event.item;
				String rowText = null;
				String[] rowTexts = (String[]) tree.getData();
				
				rowText = rowTexts[event.index];
				item.setText(rowText.toString());
				item.setText(1, rowText.toString());

				final TreeEditor editor = new TreeEditor(tree);
				editor.horizontalAlignment = SWT.LEFT;
				editor.grabHorizontal = true;
				editor.minimumWidth = 50;
				

				CCombo newEditor = new CCombo(tree, SWT.NONE);
				newEditor.setText(item.getText(EDITABLECOLUMN));

				newEditor.setFocus();
				editor.setEditor(newEditor, item, EDITABLECOLUMN);
			}
		});
		tree.setItemCount(rowTexts.length);

		shell.setSize(300, 300);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
	
	final static int EDITABLECOLUMN = 1;
}