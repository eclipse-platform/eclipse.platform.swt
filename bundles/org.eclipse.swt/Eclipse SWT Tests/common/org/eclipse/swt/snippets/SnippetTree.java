/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
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
 * Tree example snippet: create a tree
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class SnippetTree {

	final static boolean WITH_IMAGE = false;

	public static void main(String[] args) {
		Display display = new Display();
		var img = display.getSystemImage(SWT.ICON_INFORMATION);
		Shell shell = new Shell(display);
		shell.setText("SnippetTree");
		shell.setLayout(new FillLayout());
		final Tree tree = new Tree(shell, SWT.BORDER | SWT.CHECK | SWT.MULTI);


		for (int i = 0; i < 2; i++) {
			TreeItem iItem = new TreeItem(tree, 0);
			setImage(iItem, img);

			iItem.setText("TreeItem (0) -" + i);
			iItem.setExpanded(true);
			System.out.println("\t" + iItem.getText());
			for (int j = 0; j < 2; j++) {
				TreeItem jItem = new TreeItem(iItem, 0);
				jItem.setText("TreeItem (1) -" + j);
				setImage(jItem, img);
				System.out.println("\t\t" + jItem.getText());
				jItem.setExpanded(true);
				for (int k = 0; k < 1; k++) {
					TreeItem kItem = new TreeItem(jItem, 0);
					kItem.setText("TreeItem (2) -" + k);
					setImage(kItem, img);
					System.out.println("\t\t\t" + kItem.getText());
					kItem.setExpanded(true);
					for (int l = 0; l < 1; l++) {
						TreeItem lItem = new TreeItem(kItem, 0);
						lItem.setText("TreeItem (3) -" + l);
						System.out.println("\t\t\t\t" + lItem.getText());
						lItem.setExpanded(true);
					}
				}
			}
		}

		for (var i : tree.getItems()) {

			printIndexOf(tree, i);

		}

		shell.setSize(200, 200);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	private static void setImage(TreeItem iItem, Image img) {

		if (WITH_IMAGE)
			iItem.setImage(img);

	}

	private static void printIndexOf(Tree tree, TreeItem i) {
		System.out.println(i.getText() + "  " + tree.indexOf(i));

		if (i.getExpanded() && i.getItems() != null) {
			for (var it : i.getItems())
				printIndexOf(tree, it);
		}

	}
}
