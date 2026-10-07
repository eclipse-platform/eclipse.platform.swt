package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.AuthenticationEvent;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Issue3680_BasicAuthenticationTest {
	public static void main(String[] args) {

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Basic Authentication Test");
		shell.setSize(800, 600);
		shell.setLayout(new FillLayout());
		Browser browser = new Browser(shell, SWT.EDGE);
		browser.addAuthenticationListener((AuthenticationEvent event) -> {
			System.out.println("AUTHENTICATION LISTENER CALLED");
			System.out.println("Location: " + event.location);
			// Supply the credentials expected by httpbin
			event.user = "foo";
			event.password = "bar";
		});
		browser.setUrl("https://httpbin.org/basic-auth/foo/bar");
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}
}
