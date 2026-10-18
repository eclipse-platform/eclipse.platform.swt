package org.eclipse.swt.tests.win32.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

/**
 * @author Niraj Modi
 */
public class Bug570468_TreeColorTest {

	public static void main(String[] args) {
		final Display display = new Display();

		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());

		final Tree tree = new Tree(shell, SWT.MULTI | SWT.FULL_SELECTION);
		createColumn(tree, "Column 1");
		final TreeColumn column = createColumn(tree, "Column 2");
		tree.setHeaderVisible(true);

		tree.setSortColumn(column);
		tree.setSortDirection(SWT.UP);

		tree.setHeaderBackground(display.getSystemColor(SWT.COLOR_WHITE));

		for (int i = 0; i < 10; i++) {
			final TreeItem item = new TreeItem(tree, 0);
			item.setText(0, "Item 1+" + i);
			item.setText(1, "Item 2+" + i);
		}

//		tree.setSelection(0);

		shell.setSize(400, 200);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}

	private static TreeColumn createColumn(Tree tree, String text) {
		final TreeColumn column = new TreeColumn(tree, SWT.LEFT);
		column.setText(text);
		column.setWidth(100);
		return column;
	}
}
