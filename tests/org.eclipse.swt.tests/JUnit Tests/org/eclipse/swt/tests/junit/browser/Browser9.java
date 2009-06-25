/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
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

public class Browser9 {
	public static boolean verbose = false;
	public static boolean passed = false;	
	
	static String html[] = {"browser9.html"};
	static String script[] = {
		"changeStatus('new title');"};
	static String status[] = {"new title"};
	
	public static boolean test(String url, final String script, final String status) {
		if (verbose) System.out.println("Javascript - verify execute("+script+") works on a static HTML file "+url);
		passed = false;
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		final Browser browser = new Browser(shell, SWT.NONE);
		browser.addStatusTextListener(new StatusTextListener() {
			public void changed(StatusTextEvent event) {
				browser.setData("query", event.text);
		}});
		browser.addProgressListener(new ProgressListener() {
			public void changed(ProgressEvent event) {
			}
			public void completed(ProgressEvent event) {
				boolean result = browser.execute(script);
				if (!result) {
					if (verbose) System.out.println("execute failed for "+script);
					passed = false;
					return;
				}
				/* Script may additionally set the Status value */
				String value = (String)browser.getData("query");
				if (verbose) System.out.println("window.status after script: "+value);
				if (status != null) {
					passed = status.equals(value);
				} else {
					if (verbose) System.out.println("Failure - expected "+script+", not "+value);
				}
			}
		});
		shell.open();
		browser.setUrl(url);
		
		runLoopTimer(display, shell, 10);
		display.dispose();
		return passed;
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
		for (int i = 0; i < html.length; i++) {
			if (pluginPath == null) url = Browser9.class.getClassLoader().getResource(html[i]).toString();
			else url = pluginPath + "/data/"+html[i];
			boolean result = test(url, script[i], status[i]); 
			if (verbose) System.out.print(result ? "." : "E");
			if (!result) fail++; 
		}
		return fail == 0;
	}
	
	public static void main(String[] argv) {
		System.out.println("\r\nTests Finished. SUCCESS: "+test());
	}
}
