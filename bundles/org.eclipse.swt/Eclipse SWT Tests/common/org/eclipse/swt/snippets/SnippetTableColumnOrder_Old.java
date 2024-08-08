package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class SnippetTableColumnOrder_Old {

	final static boolean USE_IMAGE = true;

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Snippet 106");
		shell.setLayout(new RowLayout(SWT.VERTICAL));
		final Table_Old table = new Table_Old(shell, SWT.BORDER | SWT.MULTI);
		table.setHeaderVisible(true);
		for (int i = 0; i < 2; i++) {
			TableColumn_Old column = new TableColumn_Old(table, SWT.BORDER);
			column.setText("Column " + i);
			System.out.println(column.getWidth());
		}
		final TableColumn_Old[] columns = table.getColumns();
		for (int i = 0; i < 5; i++) {
			TableItem_Old item = new TableItem_Old(table, SWT.NONE);
			for (int j = 0; j < columns.length; j++) {
				item.setText(j, "Item " + i);
				if (USE_IMAGE)
					item.setImage(j, table.getDisplay().getSystemImage(SWT.ICON_INFORMATION));
			}
		}

		table.setColumnOrder(new int[] { 4, 3, 2, 1, 0 });

		TableItem_Old item = new TableItem_Old(table, SWT.NONE);


		for (TableColumn_Old col : columns) {
			if (table.indexOf(col) == 0) {
				col.setWidth(100);
			} else
				col.setWidth(100);
			System.out.println(col.getWidth());
		}
		Button button = new Button(shell, SWT.PUSH);
		final int index = 1;
		button.setText("Insert Column " + index + "a");
		button.addListener(SWT.Selection, e -> {
			TableColumn_Old column = new TableColumn_Old(table, SWT.NONE, index);
			column.setText("Column " + index + "a");
			TableItem_Old[] items = table.getItems();
			for (int i = 0; i < items.length; i++) {
				items[i].setText(index, "Item " + i + "a");
			}
			column.pack();
		});

		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		table.setSize(100, 50);

//		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
			printTableBounds(table);
		}
		display.dispose();
	}

	static void printTableBounds(Table_Old t) {

		System.out.println("Table: " + t.getBounds());
		System.out.println("TableItemHeight: " + t.getItemHeight());

		System.out.println("\tColumns:");

		for (var c : t.getColumns()) {
			System.out.println("\t\t" + c.getText() + " " + c.getWidth());
		}

		System.out.println("\tItems:");

		for (var i : t.getItems()) {

			System.out.println("\t\tItem: " + i.getText() + " " + i.getBounds());

			for (int r = 0; r < t.getColumnCount(); r++) {

				System.out.println("\t\t\t" + i.getText(r) + " " + i.getBounds(r));
			}

		}

	}

}
