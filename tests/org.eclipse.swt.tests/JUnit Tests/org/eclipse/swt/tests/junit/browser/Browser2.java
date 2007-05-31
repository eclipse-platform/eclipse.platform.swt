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

public class Browser2 {
	public static boolean verbose = false;
	public static boolean passed = false;	
	public static boolean locationChanging = false;
	public static boolean locationChanged = false;
	public static boolean progressCompleted = false;
	
	public static boolean test1(String html) {
		if (verbose) System.out.println("setText - args: "+html+" Expected Event Sequence: Location.changing > Location.changed > Progress.completed");
		passed = false;
		locationChanging = locationChanged = progressCompleted = false;
				
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		Browser browser = new Browser(shell, SWT.NONE);
		browser.addLocationListener(new LocationListener() {
			public void changing(LocationEvent event) {
				if (verbose) System.out.println("changing "+event.location);
				/* certain browsers do send multiple changing events. Safari does this. */
				passed = !locationChanged && !progressCompleted;
				locationChanging = true;
				if (!passed) shell.close();
			}
			public void changed(LocationEvent event) {
				if (verbose) System.out.println("changed "+event.location);
				passed = locationChanging && !locationChanged && !progressCompleted;
				locationChanged = true;
				if (!passed) shell.close();
			}
		});
		browser.addProgressListener(new ProgressListener() {
			public void changed(ProgressEvent event) {
			}
			public void completed(ProgressEvent event) {
				if (verbose) System.out.println("completed");
				passed = locationChanging && locationChanged && !progressCompleted;
				progressCompleted = true;
				if (!passed) shell.close();
				if (passed) {
					/* wait a little bit more before declaring it a success,
					 * in case bogus events follow this one.
					 */
					new Thread() {
						public void run() {
							if (verbose) System.out.println("timer start");
							try { sleep(2000); } catch (Exception e) {}
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
			}
		});
		
		shell.open();
		browser.setText(html);
		
		boolean timeout = runLoopTimer(display, shell, 600);
		if (timeout) passed = false;
		display.dispose();
		return passed;
	}
	
	public static boolean test2(String html) {
		if (verbose) System.out.println("setText URL Loading Filtering - args: "+html+" Expected Event Sequence: Location.changing cancel true > no Location.changed, no Progress.completed");
		locationChanging = locationChanged = progressCompleted = false;
		passed = false;
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		final Browser browser = new Browser(shell, SWT.NONE);
		browser.addLocationListener(new LocationListener() {
			public void changing(LocationEvent event) {
				if (verbose) System.out.println("changing "+event.location);
				/*
				* Feature on Internet Explorer.  When pending requests are stopped, IE
				* emits a Location.changing with res://C:\WINDOWS\System32\shdoclc.dll/navcancl.htm.
				* Pending requests are stopped before going to the blank page to set HTML in memory
				* with setText.
				* The test considers it is OK to get multiple Location.changing events at the condition
				* that no locationChanged and progressCompleted are reported.
				*/
				passed = !locationChanged && !progressCompleted;
				locationChanging = true;
				if (!passed) {
					shell.close();
					return;
				}
				event.doit = false;
				new Thread() {
					public void run() {
						if (verbose) System.out.println("timer start");
						try { sleep(2000); } catch (Exception e) {}
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
			public void changed(LocationEvent event) {
				/*
				 * Feature on Internet Explorer. If there is no current location, IE still fires a DocumentComplete
				 * following the BeforeNavigate2 cancel event. This DocumentComplete event contains an empty URL
				 * since the URL in BeforeNavigate2 was correctly cancelled.
				 * The test considers it is OK to send a Location.changed and a Progress.completed events after
				 * a Location.changing cancel true - at the condition that the current location is empty,
				 * otherwise it is considered that the location was not successfully cancelled. 
				 */
				passed = event.location.length() == 0;
				if (verbose) System.out.println("changed "+event.location+" "+passed);
				/* ignore LocationChanged that are empty */
				locationChanged = !passed;
			}
		});
		browser.addProgressListener(new ProgressListener() {
			public void changed(ProgressEvent event) {
			}
			public void completed(ProgressEvent event) {
				/*
				 * Feature on Internet Explorer. If there is no current location, IE still fires a DocumentComplete
				 * following the BeforeNavigate2 cancel event. This DocumentComplete event contains an empty URL
				 * since the URL in BeforeNavigate2 was correctly cancelled.
				 * The test considers it is OK to send a Location.changed and a Progress.completed events after
				 * a Location.changing cancel true - at the condition that the current location is empty,
				 * otherwise it is considered that the location was not successfully cancelled. 
				 */
				String location = browser.getUrl();
				passed = location.length() == 0;
				if (verbose) System.out.println("completed "+passed);
				progressCompleted = true;
			}
		});
		shell.open();
		browser.setText(html);
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
		String[] html = {file1};
		for (int i = 0; i < html.length; i++) {
			boolean result = test1(html[i]); 
			if (verbose) System.out.print(result ? "." : "E");
			if (!result) fail++; 
		}
		for (int i = 0; i < html.length; i++) {
			boolean result = test2(html[i]); 
			if (verbose) System.out.print(result ? "." : "E");
			if (!result) fail++; 
		}
		return fail == 0;
	}
	
	public static void main(String[] argv) {
		System.out.println("\r\nTests Finished. SUCCESS: "+test());
	}

	public static String file1 = "<HTML><HEAD>"+
		"<META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=iso-8859-1\">"+
		"<TITLE>Test with 2 frames</TITLE>"+
		"</HEAD><BODY>some simple test case here</BODY></HTML>";

}
