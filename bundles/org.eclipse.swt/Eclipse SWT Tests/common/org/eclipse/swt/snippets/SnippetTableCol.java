package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class SnippetTableCol {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Snippet 35");
		Table table = new Table(shell,
				SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);

		TableColumn c = new TableColumn(table, 0);
		c.setText("Test");


		for (int i = 0; i < 12; i++) {
			TableItem item = new TableItem(table, 0);
			item.setText("Item " + i);
		}
		Rectangle clientArea = shell.getClientArea();
		table.setBounds(clientArea.x, clientArea.y, 300, 300);
		shell.setSize(600, 600);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

}

