package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class SnippetTable_Old {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Snippet 35");
		Table_Old table = new Table_Old(shell,
				SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		for (int i = 0; i < 12; i++) {
			TableItem_Old item = new TableItem_Old(table, 0);
			item.setText("Item " + i);
			item.addListener(SWT.Paint, e -> {

				System.out.println(
						"TableItem.doPaint: event bounds:" + e.getBounds());

				Rectangle bounds = table.getBounds();
				System.out.println("TableItem.doPaint: table bounds:" + bounds);

				System.out.println("Event x/y: " + e.x + " " + e.y);

				System.out.println("ClientArea: " + table.getClientArea());

			});

			item.setImage(table.getDisplay().getSystemImage(SWT.ICON_INFORMATION));

		}

		table.addListener(SWT.Paint, e -> {

			System.out.println("Table.doPaint: event bounds:" + e.getBounds());

			Rectangle bounds = table.getBounds();
			System.out.println("Table.doPaint: table bounds:" + bounds);

			System.out.println("Event x/y: " + e.x + " " + e.y);

			System.out.println("ClientArea: " + table.getClientArea());

			ScrollBar vBar = table.getVerticalBar();

			int vs = vBar.getSelection();
			int vmin = vBar.getMinimum();
			int vmax = vBar.getMaximum();

			System.out.println("sel: " + vmin + " " + vs + " " + vmax);

		});

		Rectangle clientArea = shell.getClientArea();
		table.setBounds(clientArea.x, clientArea.y, 300, 300);
		shell.setSize(200, 200);
		shell.pack(true);
		shell.open();
		while (!shell.isDisposed()) {

			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

}
