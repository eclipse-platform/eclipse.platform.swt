package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class SnippetTable {

	final static boolean USE_IMAGES = true;

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Snippet 35");
		Table table = new Table(shell,
				SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL );
		for (int i = 0; i < 500; i++) {
			TableItem item = new TableItem(table, 0);
			item.setText("Item " + i);
			if (USE_IMAGES)
				item.setImage(table.getDisplay().getSystemImage(SWT.ICON_INFORMATION));
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
