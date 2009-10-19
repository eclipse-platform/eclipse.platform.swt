/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit.browser;

import org.eclipse.swt.tests.junit.SwtJunit;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.*;

public class Browser6 {
	public static boolean verbose = false;
	public static boolean passed = false;
	public static boolean isMozilla = SwtJunit.isGTK || SwtJunit.isMotif;
	
	public static boolean test1(String url) {
		if (verbose) System.out.println("URL Loading, verify get title event - args: "+url+" Expected Event Sequence: Title.changed");
		passed = false;
				
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		Browser browser = new Browser(shell, SWT.NONE);
		browser.addTitleListener(new TitleListener() {
			public void changed(TitleEvent event) {
				Browser browser = (Browser)event.widget;
				String url = browser.getUrl();
				if (verbose) System.out.println("Title changed <"+event.title+"> for location <"+url+">");
				passed = true;
				Runnable runnable = new Runnable() {
					public void run() {
						shell.close();
					}
				};
				if (isMozilla) {
					display.asyncExec(runnable);
				} else {
					runnable.run();
				}
			}
		});
		
		shell.open();
		browser.setUrl(url);
		
		boolean timeout = runLoopTimer(display, shell, 600);
		if (timeout) passed = false;
		display.dispose();
		return passed;
	}
	
	public static boolean test2(String url, final String expectedTitle) {
		if (verbose) System.out.println("URL Loading, verify get title event - args: "+url+" Expected Event Sequence: Title.changed");
		passed = false;
		
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		Browser browser = new Browser(shell, SWT.NONE);
		browser.addTitleListener(new TitleListener() {
			public void changed(TitleEvent event) {
				Browser browser = (Browser)event.widget;
				String url = browser.getUrl();
				if (verbose) System.out.println("Title changed <"+event.title+"> for location <"+url+">");
				if (event.title.equals(expectedTitle)) {
					passed = true;
					Runnable runnable = new Runnable() {
						public void run() {
							shell.close();
						}
					};
					if (isMozilla) {
						display.asyncExec(runnable);
					} else {
						runnable.run();
					}
				}
			}
		});
		shell.open();
		browser.setUrl(url);
		
		boolean timeout = runLoopTimer(display, shell, 600);
		if (timeout) passed = false;
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
		
		String[] urls = {"http://www.google.com"};
		for (int i = 0; i < urls.length; i++) {
			if (!isMozilla) {
				boolean result = test1(urls[i]); 
				if (verbose) System.out.print(result ? "." : "E");
				if (!result) fail++;
			}
		}
		
		String pluginPath = System.getProperty("PLUGIN_PATH");
		if (verbose) System.out.println("PLUGIN_PATH <"+pluginPath+">");
		String url;
		if (pluginPath == null) url = Browser6.class.getClassLoader().getResource("browser6.html").toString();
		else url = pluginPath + "/data/browser6.html";
		urls = new String[] {url};
		String[] titles = {"This is a test title that must be carefully checked when that page is loaded"};
		for (int i = 0; i < urls.length; i++) {
			if (!isMozilla) {
				boolean result = test2(urls[i], titles[i]); 
				if (verbose) System.out.print(result ? "." : "E");
				if (!result) fail++;
			}
		}
		
		
		return fail == 0;
	}
	
	public static void main(String[] argv) {
		System.out.println("\r\nTests Finished. SUCCESS: "+test());
	}
}
