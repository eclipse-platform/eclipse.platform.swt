package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class SnippetTableTree {
	public static void main(String[] args) {

		Shell shell = new Shell();

		Image image = new Image(null, 20, 20);

		Color red = new Color(null, 255, 0, 0);

		GC gc = new GC(image);

		gc.setBackground(red);

		gc.fillRectangle(image.getBounds());

		gc.dispose();

		red.dispose();

		TableTree tableTree = new TableTree(shell, SWT.BORDER);

		tableTree.setSize(320, 200);

		Table table = tableTree.getTable();

		table.setHeaderVisible(true);

		table.setLinesVisible(true);

		for (int col = 0; col < 3; col++) {

			TableColumn column = new TableColumn(table, SWT.NONE, col);

			column.setText("Column " + col);

			column.setWidth(100);

		}

		for (int iRoot = 0; iRoot < 8; iRoot++) {

			TableTreeItem root = new TableTreeItem(tableTree, SWT.NONE);

			root.setText("Root " + iRoot);

			for (int iBranch = 0; iBranch < 4; iBranch++) {

				TableTreeItem branch = new TableTreeItem(root, SWT.NONE);

				branch.setText("Branch " + iBranch);

				for (int col = 1; col < 3; col++) {

					branch.setImage(col, image);

					branch.setText(col, "R" + iRoot + "B" + iBranch + "C" + col);

				}

				for (int iLeaf = 0; iLeaf < 4; iLeaf++) {

					TableTreeItem leaf = new TableTreeItem(branch, SWT.NONE);

					leaf.setText("Leaf " + iLeaf);

					for (int col = 1; col < 3; col++) {

						leaf.setImage(col, image);

						leaf.setText(col, "R" + iRoot + "B" + iBranch + "L" + iLeaf + "C" + col);

					}

				}

			}

		}

		shell.pack();
		shell.open();

		Display display = shell.getDisplay();

		while (!shell.isDisposed()) {

			if (!display.readAndDispatch())
				display.sleep();

		}

	}
}
