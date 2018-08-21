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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Bug509615_auto_executeCloseListener {

	static int count = 0;
	public static void main(String[] args) {
		Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		final Browser browser = new Browser(shell, SWT.NONE);
		browser.setText("Hello world");
		
//		widget.browser.addLocationListener(new LocationListener() {
//			@Override
//			public void changing(LocationEvent event) {}
//			
//			@Override
//			public void changed(LocationEvent event) {
//				if (count == 0) {
//					widget.browser.execute("window.close()");
//					count++;
//				}
//				
//				System.out.println("Location has changed.");
//			}
//		});
		
		browser.addCloseWindowListener(e -> {
			System.out.println("window is closing");
		});
		browser.execute("window.close()");
		

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

}
