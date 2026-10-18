package org.eclipse.swt.tests.win32.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * @author Thomas Singer
 */
public class Bug536020_TableColorTest  {

	public static void main(String[] args) {
		final Display display = new Display();

		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());

		final Table table = new Table(shell, SWT.MULTI | SWT.FULL_SELECTION);
		createColumn(table, "Column 1");
		final TableColumn column = createColumn(table, "Column 2");
		table.setHeaderVisible(true);

		table.setSortColumn(column);
		table.setSortDirection(SWT.UP);

		table.setHeaderBackground(display.getSystemColor(SWT.COLOR_WHITE));

		for (int i = 0; i < 10; i++) {
			final TableItem item = new TableItem(table, 0);
			item.setText(0, "Item 1+" + i);
			item.setText(1, "Item 2+" + i);
		}

		table.setSelection(0);

		shell.setSize(400, 200);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}

	private static TableColumn createColumn(Table table, String text) {
		final TableColumn column = new TableColumn(table, SWT.LEFT);
		column.setText(text);
		column.setWidth(100);
		return column;
	}
}
