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
import org.eclipse.swt.graphics.*;

public class Browser5 {
	public static boolean passed = false;
	static Point[][] regressionBounds = {
				{new Point(10,200), new Point(300,100)},
				{new Point(10,200), null},
				{null, new Point(300,100)},
				{null, null}};
	static int index = 0;
	static int cntPassed = 0;
	static int cntClosed = 0;
	
	public static boolean test1(String url) {
		System.out.println("javascript window.open with location and size parameters - args: "+url+" Expected Event Sequence: Visibility.open");
		passed = false;
				
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		final Browser browser = new Browser(shell, SWT.NONE);
		browser.addOpenWindowListener(new OpenWindowListener() {
			public void open(WindowEvent event) {
				System.out.println("OpenWindow "+index);
				Shell newShell = new Shell(display);
				newShell.setLayout(new FillLayout());
				Browser browser = new Browser(newShell, SWT.NONE);
				browser.setData("index", new Integer(index));
				browser.addVisibilityWindowListener(new VisibilityWindowListener() {
					public void hide(WindowEvent event) {
					}
					public void show(WindowEvent event) {
						Browser browser = (Browser)event.widget;
						Shell parent = browser.getShell();
						if (event.location != null) parent.setLocation(event.location);
						if (event.size != null) parent.setSize(event.size);
						int index = ((Integer)browser.getData("index")).intValue();
						parent.setText("SWT Browser shell "+index);
						parent.open();
						if (index < 0) {
							/* Certain browsers fire multiple show events for no good reason. Further show events
							 * are considered 'legal' as long as they don't contain size and location information.
							 */
							if (event.location != null || event.size != null) {
								System.out.println("Failure - Browser "+index+" is receiving multiple show events");
								passed = false;
								shell.close();
							} else {
								System.out.println("Unnecessary (but harmless) visibility.show event Browser "+index);
							}
						} else {
							browser.setData("index", new Integer(-100-index));
							System.out.println("Visibility.show browser "+index+" location "+event.location+" size "+event.size);
							if (((event.location == null && regressionBounds[index][0] == null) ||
								(event.location != null && event.location.equals(regressionBounds[index][0]))) &&
								((event.size == null && regressionBounds[index][1] == null) ||
								(event.size != null && event.size.equals(regressionBounds[index][1])))) cntPassed++;
							else {
								System.out.println("Failure - was expecting location "+regressionBounds[index][0]+" size "+regressionBounds[index][1]);
								passed = false;
								shell.close();
								return;
							}
						}
					}
				});
				browser.addCloseWindowListener(new CloseWindowListener() {
					public void close(WindowEvent event) {
						cntClosed++;
						System.out.println("Close");
						Browser browser = (Browser)event.widget;
						browser.getShell().close();
						if (cntPassed == regressionBounds.length) passed = true;
						if (cntClosed == regressionBounds.length) {
							shell.close();
							return;
						}
					}
				});
				event.browser = browser;
				index++;
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
		String url;
		String pluginPath = System.getProperty("PLUGIN_PATH");
		System.out.println("PLUGIN_PATH <"+pluginPath+">");
		if (pluginPath == null) url = Browser5.class.getClassLoader().getResource("browser5.html").toString();
		else url = pluginPath + "/data/browser5.html";
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