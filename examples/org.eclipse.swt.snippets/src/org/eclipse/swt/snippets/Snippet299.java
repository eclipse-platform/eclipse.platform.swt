package org.eclipse.swt.snippets;

/*
 * RowLayout: center alignment
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet299 {

	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		RowLayout layout = new RowLayout();
		layout.center = true;
		shell.setLayout(layout);

		Button button1 = new Button(shell, SWT.PUSH);
		button1.setText("Button 1");

		Button button2 = new Button(shell, SWT.PUSH);
		button2.setText("Button 2");
		RowData data = new RowData();
		data.height = 200;
		button2.setLayoutData(data);

		Button button3 = new Button(shell, SWT.PUSH);
		button3.setText("Button 3");

		shell.pack();
		shell.open();
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
} 
