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

import org.eclipse.swt.tests.junit.SwtJunit;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public class Browser5 {
	public static boolean verbose = false;
	public static boolean passed = false;
	static Point[][] regressionBounds = {
				{new Point(10,200), new Point(300,100)},
				{new Point(10,200), null},
				{null, new Point(300,100)},
				{null, null}};
	static int index = 0;
	static int cntPassed = 0;
	static int cntClosed = 0;
	public static boolean isMozilla = SwtJunit.isGTK || SwtJunit.isMotif;
	
	public static boolean test1(String url) {
		if (verbose) System.out.println("javascript window.open with location and size parameters - args: "+url+"\n  Expected Event Sequence: Visibility.show");
		passed = false;
				
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		final Browser browser = new Browser(shell, SWT.NONE);
		browser.addOpenWindowListener(new OpenWindowListener() {
			public void open(WindowEvent event) {
				if (verbose) System.out.println("OpenWindow "+index);
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
						Point location = event.location;
						Point size = event.size;
						if (location != null) parent.setLocation(location);
						if (size != null) parent.setSize(size);
						int index = ((Integer)browser.getData("index")).intValue();
						parent.setText("SWT Browser shell "+index);
						parent.open();
						if (index < 0) {
							/* Certain browsers fire multiple show events for no good reason. Further show events
							 * are considered 'legal' as long as they don't contain size and location information.
							 */
							if (location != null || size != null) {
								if (verbose) System.out.println("Failure - Browser "+index+" is receiving multiple show events");
								passed = false;
								shell.close();
							} else {
								if (verbose) System.out.println("Unnecessary (but harmless) visibility.show event Browser "+index);
							}
						} else {
							if (verbose) System.out.println("Visibility.show browser "+index+" location "+location+" size "+size);
							browser.setData("index", new Integer(-100-index));

							/* Certain browsers include decorations in addition to the expected size.
							 * Accept sizes that are greater than or equal to the expected size.
							 * Certain browsers invent size or location when some parameters are missing.
							 * If we expect null for one of size or location, also accept non null answers.
							 */
							Point expectedLocation = regressionBounds[index][0];
							Point expectedSize = regressionBounds[index][1];
							if (verbose) System.out.println("Expected location "+expectedLocation+" size "+expectedSize);
							boolean checkLocation = (location == null && expectedLocation == null) ||
								(location != null && location.equals(expectedLocation) ||
								(location != null && expectedLocation == null));
							boolean checkSize  = (size == null && expectedSize == null) || 
								(size != null && size.equals(expectedSize)) ||
								(size != null && expectedSize == null) ||
								(size != null && size.x >= expectedSize.x && size.y >= expectedSize.y);
							if (!checkSize || !checkLocation) {
								if (verbose) System.out.println("	Failure ");
								passed = false;
								shell.close();
								return;
							} else cntPassed++;
						}
					}
				});
				browser.addCloseWindowListener(new CloseWindowListener() {
					public void close(WindowEvent event) {
						cntClosed++;
						if (verbose) System.out.println("Close");
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
		if (verbose) System.out.println("PLUGIN_PATH <"+pluginPath+">");
		if (pluginPath == null) url = Browser5.class.getClassLoader().getResource("browser5.html").toString();
		else url = pluginPath + "/data/browser5.html";
		String[] urls = {url};
		for (int i = 0; i < urls.length; i++) {
			// TEST1 TEMPORARILY NOT RUN FOR MOZILLA
			if (!isMozilla) {
				boolean result = test1(urls[i]); 
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
