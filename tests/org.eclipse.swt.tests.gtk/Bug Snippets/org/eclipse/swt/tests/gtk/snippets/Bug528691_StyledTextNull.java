package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/*
 * Title: Bug 528691 - [GTK] StyledText ignores text after \u0000 character
 * How to run: launch snippet and observe StyledText widget
 * Bug description: Only "hello" is displayed
 * Expected results: "helloworld" should be displayed
 * GTK Version(s): GTK2, GTK3
 */
public class Bug528691_StyledTextNull {

	public static void main(String[] args) {
		final Display display = new Display();

		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());

		final StyledText styledText = new StyledText(shell, SWT.BORDER);
		styledText.setText("hello\u0000world");

		shell.setSize(500, 400);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}
}
