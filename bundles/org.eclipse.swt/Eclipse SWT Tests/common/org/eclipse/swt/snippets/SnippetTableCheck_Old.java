package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class SnippetTableCheck_Old {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Snippet 113");
		Table_Old table = new Table_Old(shell, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);

		var c1 = new TableColumn_Old(table, 0);
		c1.setWidth(200);

		var c2 = new TableColumn_Old(table, 0);
		c2.setWidth(200);

		for (int i = 0; i < 12; i++) {
			TableItem_Old item = new TableItem_Old(table, SWT.NONE);
			item.setText(0, "Item " + i);
			item.setText(1, "Item " + i);
		}
		Rectangle clientArea = shell.getClientArea();
		table.setBounds(clientArea.x, clientArea.y, 100, 100);
		table.addListener(SWT.Selection, event -> {
			String string = event.detail == SWT.CHECK ? "Checked" : "Selected";
			System.out.println(event.item + " " + string);
		});

		table.pack();
		shell.pack();
		shell.open();
		printTableBounds(table);
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}


		display.dispose();
	}

	static void printTableBounds(Table_Old t) {

		System.out.println("Table: " + t.getBounds());

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
