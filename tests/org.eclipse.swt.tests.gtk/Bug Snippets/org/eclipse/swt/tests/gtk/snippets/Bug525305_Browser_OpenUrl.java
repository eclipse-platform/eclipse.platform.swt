package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/*
 * Title: Handle files or URLs from eclipse launcher or gdbus.
 * How to run: 
    - Open snippet with Launch Configuration VM Argument: -Dswt.dbus.init
    - Run launcher with file or url
      eclipse /myFile  htpp://www.google.com 
 * Bug description:
 * Expected results: Browser opens URLs, filenames printed.
 * GTK Version(s): 3.22/2.24
 */
public class Bug525305_Browser_OpenUrl {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setSize(500, 600);
		shell.setLayout(new FillLayout());

		final Browser browser = new Browser(shell, SWT.NONE);
		browser.setText("hello <b>world!</b>");

		display.addListener(SWT.OpenUrl, e -> {
			System.out.println("OpenUrl with:" + e.text);
			browser.setUrl(e.text);
		});

		display.addListener(SWT.OpenDocument, e -> {
			System.out.println("OpenDocument with: " + e.text);
			browser.setText("OpenDocument was called with arg: " + e.text);
		});

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
