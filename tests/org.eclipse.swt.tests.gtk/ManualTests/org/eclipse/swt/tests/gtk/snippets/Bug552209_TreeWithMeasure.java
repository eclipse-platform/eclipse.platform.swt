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

public final class Bug552209_TreeWithMeasure {
	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout());
		shell.setSize(800, 150);

		Label lblHint = new Label(shell, 0);
		lblHint.setText("Bug: 'SWTError: No more handles' exception as soon as you start the snippet");

		Tree tree = new Tree(shell, SWT.NONE);
		tree.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
		tree.setHeaderVisible(true);

		// Listeners are required to trigger the bug
		tree.addListener(SWT.MeasureItem, arg -> {
		});

		tree.addListener(SWT.PaintItem, arg -> {
		});

		// First paint must be with no items to trigger 'Tree.gtk_draw' code from Bug 541427
		display.timerExec(200, () -> {
			TreeItem item = new TreeItem(tree, 0);
			item.setText("Tree item");
		});

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}