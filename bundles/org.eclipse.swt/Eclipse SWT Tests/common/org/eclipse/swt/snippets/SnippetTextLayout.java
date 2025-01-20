package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class SnippetTextLayout {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Snippet 145 Win");

		Font font1 = new Font(display, "Tahoma", 14, SWT.BOLD);
		Font font2 = new Font(display, "MS Mincho", 20, SWT.ITALIC);
		Font font3 = new Font(display, "Arabic Transparent", 18, SWT.NORMAL);

		Color blue = display.getSystemColor(SWT.COLOR_BLUE);
		Color green = display.getSystemColor(SWT.COLOR_GREEN);
		Color yellow = display.getSystemColor(SWT.COLOR_YELLOW);
		Color gray = display.getSystemColor(SWT.COLOR_GRAY);

		final TextLayout_Old layout = new TextLayout_Old(display);
		TextStyle style1 = new TextStyle(font1, yellow, blue);
		TextStyle style2 = new TextStyle(font2, green, null);
		TextStyle style3 = new TextStyle(font3, blue, gray);

		layout.setText(
				"English \u65E5\u672C\u8A9E  \u0627\u0644\u0639\u0631\u0628\u064A\u0651\u0629");
		layout.setStyle(style1, 0, 6);
		layout.setStyle(style2, 8, 10);
		layout.setStyle(style3, 13, 21);

		shell.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		shell.addListener(SWT.Paint, event -> layout.draw(event.gc, 10, 10));
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		font1.dispose();
		font2.dispose();
		font3.dispose();
		layout.dispose();
		display.dispose();
	}

}
