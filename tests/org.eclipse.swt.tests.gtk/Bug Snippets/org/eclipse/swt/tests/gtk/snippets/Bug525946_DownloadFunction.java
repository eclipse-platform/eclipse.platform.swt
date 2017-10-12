package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/*
 * Title: Bug 459462 - [WebKit] Implement Download functionality
 * How to run: launch snippet
 * Bug description: Download functionality doesn't work on WebKit2.
 * Expected results: Download functionality now works on WebKit2.
 * GTK version(s): GTK3.x
 */

public class Bug525946_DownloadFunction {
public static void main(String[] args) {
final Display display = new Display();
final Shell shell = new Shell(display);
shell.setBounds(10, 10, 400, 400);
shell.setLayout(new FillLayout());
final Browser browser = new Browser(shell, SWT.NONE);
browser.setUrl("http://download.eclipse.org/tools/orbit/downloads/drops/R20170516192513/orbit-buildrepo-R20170516192513.zip");
shell.open();
while (!shell.isDisposed()) {
if (!display.readAndDispatch()) display.sleep();
}
shell.dispose();
display.dispose();
}
}