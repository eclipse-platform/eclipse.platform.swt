/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;


import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Bug510183_javadocHang {
	
	public static int count = 0;

	public static void main(String[] args) {
		Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		Button button = new Button(shell, SWT.PUSH);
		final Browser browser = new Browser(shell, SWT.NONE);
		button.setText("Click to increase count.");
		button.addMouseListener(new MouseListener() {
			@Override
			public void mouseUp(MouseEvent e) {}
			@Override
			public void mouseDown(MouseEvent e) {
				shell.setText("Count: " + count++);
			}
			@Override
			public void mouseDoubleClick(MouseEvent e) {}
		});
		
		browser.setText("<html><title>Snippet</title><body><a href=\"https://eclipse.org/\">Eclipse.org  Disposing on link change causes hang</a></body></html>");
		browser.addProgressListener(new ProgressListener() {
			@Override
			public void completed(ProgressEvent event) {
				browser.addLocationListener(new LocationListener() {
					@Override
					public void changing(LocationEvent event) {
						browser.dispose();
//						System.out.println("KILLING OF THE BROWSER");
//						
//						String value = (String)widget.browser.evaluate("return 'hello'");
//						System.out.println("Returned: " + value);
//						
//						String script = "document.body.style.backgroundColor = 'red'";
//						widget.browser.execute(script);
					}
					@Override
					public void changed(LocationEvent event) {
					}
				});
			}
			@Override
			public void changed(ProgressEvent event) {}
		});
		
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();

	}

}
