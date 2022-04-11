package org.eclipse.swt.tests.win32.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

class Bug579626_ArabicTest {

	private static final String ARABIC_TEXT = "جارِ التحميل";

	public static void main(String[] args) {
		final Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout());

		// This is rendered correctly
		Text text = new Text(shell, SWT.BORDER);
		text.setText(ARABIC_TEXT);

		// This is not rendered correctly
		StyledText styled = new StyledText(shell, SWT.BORDER | SWT.MULTI);
		styled.setText(ARABIC_TEXT);

		shell.pack();
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}
}
