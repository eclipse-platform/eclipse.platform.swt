/*******************************************************************************
* Copyright (c) 2026 Vector Informatik GmbH and others.
*
* This program and the accompanying materials
* are made available under the terms of the Eclipse Public License 2.0
* which accompanies this distribution, and is available at
* https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
*
* Contributors:
*     Vector Informatik GmbH  - initial API and implementation
*******************************************************************************/
package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * Snippet to demonstrate SWT bug 3075: TreeItem.setImage() does not work for
 * right-to-left orientation. <br/>
 * <br/>
 *
 * See <a href="https://github.com/eclipse-platform/eclipse.platform.swt/issues/3075">SWT bug 3075</a>.
 *
 */
public class Snippet392 {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Snippet 392");
		shell.setLayout(new FillLayout());

		Tree tree = new Tree(shell, SWT.BORDER | SWT.FULL_SELECTION);
		tree.setLinesVisible(true);
		TreeColumn column1 = new TreeColumn(tree, SWT.NONE);
		column1.setWidth(200);
		TreeColumn column2 = new TreeColumn(tree, SWT.NONE);
		column2.setWidth(200);

		Image image = new Image(display, Snippet392.class.getResourceAsStream("eclipse.svg"));

		TreeItem item = new TreeItem(tree, SWT.None);
		item.setText(0, "Hello");
		item.setText(1, "World");
		item.setImage(1, image);
		new TreeItem(item, SWT.None);

		tree.setOrientation(SWT.LEFT_TO_RIGHT);
		shell.setSize(600, 150);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}
}
