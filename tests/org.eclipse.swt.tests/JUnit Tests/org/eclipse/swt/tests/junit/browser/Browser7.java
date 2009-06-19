/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit.browser;

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public class Browser7 {
	public static boolean verbose = false;
	public static boolean passed = false;	
	
	static int cntOpen = 0;
	static int cntShow = 0;
	
	public static boolean test(String url) {
		if (verbose) System.out.println("window.open, verify get Window.open and Window.show events - args: "+url+" Expected Event Sequence: Window.open, Window.show multiple times");
		passed = false;
		
		cntOpen = 0;
		cntShow = 0;
		
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		Browser browser = new Browser(shell, SWT.NONE);
		initialize(display, browser);
		
		shell.open();
		browser.setUrl(url);
		
		boolean timeout = runLoopTimer(display, shell, 10);
		if (verbose) System.out.println("Window opened: "+cntOpen+" Window shown: "+cntShow);
		/*
		 * Bug in Mozilla. Multiple show events are fired by Mozilla.
		 */
		if (timeout) passed = cntOpen == 4 && cntShow >= 4;
		display.dispose();
		return passed;
	}

	static void initialize(final Display display, Browser browser) {
		browser.addOpenWindowListener(new OpenWindowListener() {
			public void open(WindowEvent event) {
				if (verbose) System.out.println("VisibilityWindowListener.open");
				Shell shell = new Shell(display);
				shell.setText("New Window");
				shell.setLayout(new FillLayout());
				Browser browser = new Browser(shell, SWT.NONE);
				initialize(display, browser);
				event.browser = browser;
				cntOpen++;
			}
		});
		browser.addVisibilityWindowListener(new VisibilityWindowListener() {
			public void hide(WindowEvent event) {
				Browser browser = (Browser)event.widget;
				Shell shell = browser.getShell();
				shell.setVisible(false);
			}
			public void show(WindowEvent event) {
				if (verbose) System.out.println("VisibilityWindowListener.show location="+event.location+" size="+event.size+" addressBar="+event.addressBar+" menuBar="+event.menuBar+" statusBar="+event.statusBar+" toolBar="+event.toolBar);
				Browser browser = (Browser)event.widget;
				Shell shell = browser.getShell();
				if (event.location != null) shell.setLocation(event.location);
				if (event.size != null) {
					Point size = event.size;
					shell.setSize(shell.computeSize(size.x, size.y));
				}
				cntShow++;
				shell.open();
			}
		});
		browser.addCloseWindowListener(new CloseWindowListener() {
			public void close(WindowEvent event) {
				Browser browser = (Browser)event.widget;
				Shell shell = browser.getShell();
				shell.close();
			}
		});
}
	 
	static boolean runLoopTimer(final Display display, final Shell shell, final int seconds) {
		final boolean[] timeout = {false};
		new Thread() {
			public void run() {
				try {
					for (int i = 0; i < seconds; i++) {
						Thread.sleep(1000);
						if (display.isDisposed() || shell.isDisposed()) return;
					}
				}
				catch (Exception e) {} 
				timeout[0] = true;
				/* wake up the event loop */
				if (!display.isDisposed()) {
					display.asyncExec(new Runnable() {
						public void run() {
							if (!shell.isDisposed()) shell.redraw();						
						}
					});
				}
			}
		}.start();
		while (!timeout[0] && !shell.isDisposed()) if (!display.readAndDispatch()) display.sleep();
		return timeout[0];
	}
	
	public static boolean test() {
		int fail = 0;
				
		String pluginPath = System.getProperty("PLUGIN_PATH");
		if (verbose) System.out.println("PLUGIN_PATH <"+pluginPath+">");
		String url;
		if (pluginPath == null) url = Browser7.class.getClassLoader().getResource("browser7.html").toString();
		else url = pluginPath + "/data/browser7.html";
		String[] urls = new String[] {url};
		for (int i = 0; i < urls.length; i++) {
			boolean result = test(urls[i]); 
			if (verbose) System.out.print(result ? "." : "E");
			if (!result) fail++; 
		}
		return fail == 0;
	}
	
	public static void main(String[] argv) {
		System.out.println("\r\nTests Finished. SUCCESS: "+test());
	}
}
