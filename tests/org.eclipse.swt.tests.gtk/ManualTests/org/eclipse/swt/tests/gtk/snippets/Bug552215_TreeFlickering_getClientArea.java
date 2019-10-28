/*******************************************************************************
 * Copyright (c) 2019 Syntevo and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Syntevo - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Bug552215_TreeFlickering_getClientArea {
	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout());

		Label lblHint = new Label(shell, 0);
		lblHint.setText("Bug: Move mouse over Tree items => flickering");

		Tree tree = new Tree(shell, SWT.NONE);
		tree.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));

		// Countermeasures for Bug 550606
		tree.setHeaderVisible(true);

		// This is what causes flickering
		tree.addListener(SWT.EraseItem, event -> {
			tree.getClientArea();
		});

		// First paint must be with no items to trigger 'Tree.gtk_draw' code from Bug 541427
		display.timerExec(1000, () -> {
			for (int i = 0; i < 20; i++) {
				final TreeItem rootNode = new TreeItem(tree, SWT.NONE);
				rootNode.setText("node " + i);
			}
		});

		shell.setSize(400, 600);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}
}
