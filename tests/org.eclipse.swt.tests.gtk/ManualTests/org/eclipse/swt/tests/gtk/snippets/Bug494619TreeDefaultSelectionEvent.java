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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class Bug494619TreeDefaultSelectionEvent {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		final Tree tree = new Tree(shell, SWT.BORDER);
		for (int i = 0; i < 4; i++) {
			TreeItem iItem = new TreeItem(tree, 0);
			iItem.setText("TreeItem (0) -" + i);
			for (int j = 0; j < 4; j++) {
				TreeItem jItem = new TreeItem(iItem, 0);
				jItem.setText("TreeItem (1) -" + j);
				for (int k = 0; k < 4; k++) {
					TreeItem kItem = new TreeItem(jItem, 0);
					kItem.setText("TreeItem (2) -" + k);
					for (int l = 0; l < 4; l++) {
						TreeItem lItem = new TreeItem(kItem, 0);
						lItem.setText("TreeItem (3) -" + l);
					}
				}
			}
		}

		tree.addListener(SWT.KeyDown, new Listener() {
			@Override
			public void handleEvent(Event event) {
				//32 in hex = 0x20 - space
				//13 in hex = 0xD - enter.
				if (event.keyCode == 0x20 && (event.stateMask & SWT.MODIFIER_MASK) == 0) {
					System.out.println("KeyDown event. Handle space");
					event.doit = true;
				} else if (event.keyCode == 0xD && (event.stateMask & SWT.MODIFIER_MASK) == 0) {
					System.out.println("KeyDown event. Handle enter");
				}
			}
		});
		tree.addListener(SWT.DefaultSelection, new Listener() {
			@Override
			public void handleEvent(Event event) {

				System.out.println(event.keyCode);
				System.out.println("Default Selection event. Handle enter or double-click");
			}
		});

		shell.setSize(200, 200);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}
