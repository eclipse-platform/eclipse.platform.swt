/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit.browser;

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.*;

public class Browser3 {
	public static boolean passed = false;	
	public static boolean newWindow, locationChanging, locationChanged, visibilityShow, progressCompleted;
	
	public static boolean test1(String url) {
		System.out.println("javascript window.open - args: "+url+" Expected Event Sequence: Browser1:NewWindow.newWindow > Browser2:Location.changing > Browser2:Visibility.show > Browser2:Location.changed > Browser2:Progress.completed");
		passed = false;
		locationChanging = locationChanged = progressCompleted = false;
				
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		final Browser browser1 = new Browser(shell, SWT.NONE);
		final Shell shell2 = new Shell(display);
		shell2.setLayout(new FillLayout());
		final Browser browser2 = new Browser(shell2, SWT.NONE);
		browser1.addNewWindowListener(new NewWindowListener() {
			public void newWindow(NewWindowEvent event) {
				newWindow = true;
				Browser src = (Browser)event.widget;
				if (src != browser1) {
					System.out.println("Failure - expected "+browser1+", got "+src);
					passed = false;
					shell.close();
					return;
				}
				if (event.browser != null) {
					System.out.println("Failure - expected null, got "+event.browser);
					passed = false;
					shell.close();
					return;
				}
				event.browser = browser2;
			}
		});
		browser2.addLocationListener(new LocationListener() {
			public void changed(LocationEvent event) {
				if (!newWindow || !locationChanging) {
					System.out.println("Failure - LocationEvent.changing received at wrong time");
					passed = false;
					shell.close();
					return;
				}
				locationChanged = true;
			}
			public void changing(LocationEvent event) {
				if (!newWindow || locationChanging || locationChanged) {
					System.out.println("Failure - LocationEvent.changing received at wrong time");
					passed = false;
					shell.close();
					return;
				}
				locationChanging = true;
			}
		});
		browser2.addVisibilityListener(new VisibilityListener() {
			public void hide(VisibilityEvent event) {
				System.out.println("Failure - did not expect VisibilityEvent.hide");
				passed = false;
				shell.close();
			}
			public void show(VisibilityEvent event) {
				if (!newWindow || !locationChanging || locationChanged) {
					System.out.println("Failure - VisibilityEvent.show received at wrong time");
					passed = false;
					shell.close();
					return;					
				}
				shell2.open();
			}
		});
		browser2.addProgressListener(new ProgressListener() {
			public void changed(ProgressEvent event) {
			}

			public void completed(ProgressEvent event) {
				new Thread() {
					public void run() {
						System.out.println("timer start");
						try { sleep(2000); } catch (Exception e) {};
						passed = true;
						if (!display.isDisposed())
							display.asyncExec(new Runnable(){
								public void run() {
									System.out.println("timer asyncexec shell.close");
									if (!shell.isDisposed()) shell.close();							
								}
							});
						System.out.println("timer over");
					};
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
		String url = Browser3.class.getResource("browser3.html").toString();
		String[] urls = {url};
		for (int i = 0; i < urls.length; i++) {
			boolean result = test1(urls[i]); 
			System.out.print(result ? "." : "E");
			if (!result) fail++; 
		}
		return fail == 0;
	}
	
	public static void main(String[] argv) {		
		System.out.println("\r\nTests Finished. SUCCESS: "+test());
	}
}