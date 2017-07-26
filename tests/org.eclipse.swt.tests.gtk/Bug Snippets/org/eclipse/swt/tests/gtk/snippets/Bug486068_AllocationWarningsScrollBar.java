package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/*
 * Title: Bug 486068: [GTK3.20+] Allocation warnings printed in error console
 * How to run: launch snippet and check console for warnings
 * Bug description: GTK assertion warnings all over the place for GtkScrollbar
 * Expected results: clean console output, no warnings
 * GTK Version(s): GTK3.20+
 */
public class Bug486068_AllocationWarningsScrollBar {

	public static void main (String [] args) {
			Display display = new Display ();
			Shell shell = new Shell (display);
			Text text = new Text (shell, SWT.V_SCROLL);
			text.setBounds(0, 0, 200, 30);
			shell.pack ();
			shell.open ();
			while (!shell.isDisposed ()) {
				if (!display.readAndDispatch ()) display.sleep ();
			}
			display.dispose ();
	}
}