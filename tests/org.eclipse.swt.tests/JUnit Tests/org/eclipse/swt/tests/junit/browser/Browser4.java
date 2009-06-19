/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
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

public class Browser4 {
	public static boolean verbose = false;
	public static boolean passed = false;	
	public static boolean openWindow, locationChanging, locationChanged, visibilityShow, progressCompleted, closeWindow;
	
	public static boolean test1(String url) {
		if (verbose) System.out.println("javascript window.open - args: "+url+" Expected Event Sequence: Browser1:OpenWindow.open > { Browser2:Location.changing, Browser2:Visibility.show, Browser2:Location.changed } > Browser2:Progress.completed > Browser2.CloseWindow.close");
		passed = false;
		locationChanging = locationChanged = progressCompleted = false;
				
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		final Browser browser1 = new Browser(shell, SWT.NONE);
		final Shell shell2 = new Shell(display);
		shell2.setLayout(new FillLayout());
		final Browser browser2 = new Browser(shell2, SWT.NONE);
		browser1.addOpenWindowListener(new OpenWindowListener() {
			public void open(WindowEvent event) {
				openWindow = true;
				Browser src = (Browser)event.widget;
				if (src != browser1) {
					if (verbose) System.out.println("Failure - expected "+browser1+", got "+src);
					passed = false;
					shell.close();
					return;
				}
				if (event.browser != null) {
					if (verbose) System.out.println("Failure - expected null, got "+event.browser);
					passed = false;
					shell.close();
					return;
				}
				event.browser = browser2;
			}
		});
		browser2.addLocationListener(new LocationListener() {
			public void changed(LocationEvent event) {
				if (!openWindow || !locationChanging) {
					if (verbose) System.out.println("Failure - Location.changing received at wrong time");
					passed = false;
					shell.close();
					return;
				}
				locationChanged = true;
			}
			public void changing(LocationEvent event) {
				if (!openWindow) {
					if (verbose) System.out.println("Failure - Location.changing received at wrong time");
					passed = false;
					shell.close();
					return;
				}
				locationChanging = true;
			}
		});
		browser2.addVisibilityWindowListener(new VisibilityWindowListener() {
			public void hide(WindowEvent event) {
				if (verbose) System.out.println("Failure - did not expect VisibilityEvent.hide");
				passed = false;
				shell.close();
			}
			public void show(WindowEvent event) {
				if (!openWindow) {
					if (verbose) System.out.println("Failure - Visibility.show received at wrong time");
					passed = false;
					shell.close();
					return;					
				}
				shell2.open();
				visibilityShow = true;
			}
		});
		browser2.addProgressListener(new ProgressListener() {
			public void changed(ProgressEvent event) {
			}

			public void completed(ProgressEvent event) {
				if (!locationChanging || !locationChanged || !visibilityShow) {
					if (verbose) System.out.println("Failure - Progress.completed received at wrong time");
					passed = false;
					shell.close();
					return;
				}
				progressCompleted = true;
			}
		});
		browser2.addCloseWindowListener(new CloseWindowListener() {
			public void close(WindowEvent event) {
				if (!progressCompleted) {
					if (verbose) System.out.println("Failure - CloseWindow.close received at wrong time");
					passed = false;
					shell.close();
					return;
				}
				closeWindow = true;
				final Browser browser = (Browser)event.widget;
				Shell parent = browser.getShell();
				parent.close();
				
				new Thread() {
					public void run() {
						if (verbose) System.out.println("timer start");
						try { sleep(2000); } catch (Exception e) {}
						passed = true;
						if (!display.isDisposed())
							display.asyncExec(new Runnable(){
								public void run() {
									if (verbose) System.out.println("timer asyncexec shell.close");
									if (!shell.isDisposed()) shell.close();							
								}
							});
						if (verbose) System.out.println("timer over");
					}
				}.start();
			}
		});
		
		shell.open();
		browser1.setUrl(url);
		
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
		String url;
		String pluginPath = System.getProperty("PLUGIN_PATH");
		if (verbose) System.out.println("PLUGIN_PATH <"+pluginPath+">");
		if (pluginPath == null) url = Browser4.class.getClassLoader().getResource("browser4.html").toString();
		else url = pluginPath + "/data/browser4.html";
		String[] urls = {url};
		for (int i = 0; i < urls.length; i++) {
			boolean result = test1(urls[i]); 
			if (verbose) System.out.print(result ? "." : "E");
			if (!result) fail++; 
		}
		return fail == 0;
	}
	
	public static void main(String[] argv) {		
		System.out.println("\r\nTests Finished. SUCCESS: "+test());
	}
}
