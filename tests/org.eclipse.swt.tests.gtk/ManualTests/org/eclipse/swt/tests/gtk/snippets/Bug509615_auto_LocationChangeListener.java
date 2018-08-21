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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Bug509615_auto_LocationChangeListener {

	static int count = 0;
	public static void main(String[] args) {
		Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		final Browser browser = new Browser(shell, SWT.NONE);
		
		browser.addLocationListener(new LocationListener() {
			@Override 
			public void changing(LocationEvent event) {
				System.out.println("Changing ....");
				browser.evaluate("return 123");
			}
			
			@Override 
			public void changed(LocationEvent event) {
				System.out.println("Changed !!!!");
				browser.evaluate("return 123");
			}
		});
		browser.addProgressListener(new ProgressListener() {
			
			@Override
			public void completed(ProgressEvent event) {
				System.out.println("Progress Completed !!!! ");
			}
			
			@Override
			public void changed(ProgressEvent event) {
				System.out.println("Progress Changing ....");
			}
		});
		
		browser.setText("Hello world");
		
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

}
