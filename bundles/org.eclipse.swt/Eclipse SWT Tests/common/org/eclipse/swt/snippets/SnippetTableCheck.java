package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class SnippetTableCheck {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Snippet 113");
		Table table = new Table(shell, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		for (int i = 0; i < 12; i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText("Item " + i);
		}
		Rectangle clientArea = shell.getClientArea();
		table.pack();
		table.setBounds(clientArea.x, clientArea.y, 200, 200);
		table.addListener(SWT.Selection, event -> {
			String string = event.detail == SWT.CHECK ? "Checked" : "Selected";
			System.out.println(event.item + " " + string);
		});
		shell.setSize(200, 200);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
			printTableBounds(table);
		}


		display.dispose();
	}

	static void printTableBounds(Table t) {

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
