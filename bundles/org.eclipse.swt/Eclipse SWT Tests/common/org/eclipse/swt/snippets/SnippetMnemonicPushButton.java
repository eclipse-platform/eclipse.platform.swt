package org.eclipse.swt.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;

public class SnippetMnemonicPushButton {
	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display, SWT.SHELL_TRIM);
		shell.setLayout(new RowLayout());
		{

			Button b1 = new Button(shell, SWT.RIGHT);

			b1.setText("&text on the button");

			b1.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					System.out.println(b1.getText() + " widgetSelected");
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					System.out.println("widgetDefaultSelected");
				}

			});
		}
		{
			Button b1 = new Button(shell, SWT.RIGHT);

			b1.setText("&second text on the button");

			b1.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					System.out.println(b1.getText() + " widgetSelected");
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					System.out.println("widgetDefaultSelected");
				}

			});
		}

		shell.open();
		// Set up the event loop.
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				// If no more entries in event queue
				display.sleep();
			}
		}
		display.dispose();
	}

}