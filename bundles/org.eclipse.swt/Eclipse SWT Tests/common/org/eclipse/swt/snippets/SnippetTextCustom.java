package org.eclipse.swt.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class SnippetTextCustom {
	public static void main(String[] args) {
		Display display = new Display();

		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout());

		Text text1 = new Text(shell, SWT.BORDER | SWT.SINGLE | SWT.READ_ONLY);
		text1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		text1.setText("readonly");

		Text text2 = new Text(shell, SWT.BORDER | SWT.SINGLE);
		text2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		text2.setMessage("message");

		Text text3 = new Text(shell, SWT.BORDER | SWT.MULTI);
		text3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		text3.setText("multi\nline");

		shell.setSize(400, 300);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}
}
