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

import org.eclipse.swt.tests.junit.SwtJunit;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.*;

public class Browser8 {
	public static boolean verbose = false;
	public static boolean passed = false;
	public static boolean isMozilla = SwtJunit.isGTK || SwtJunit.isMotif;
	
	static String html[] = {"<html><title>Snippet</title><body><p id='myid'>Best Friends</p><p id='myid2'>Cat and Dog</p></body></html>"};
	static String script[] = {
		"var newNode = document.createElement('P'); \r\n"+
		"var text = document.createTextNode('At least when I am around');\r\n"+
		"newNode.appendChild(text);\r\n"+
		"document.getElementById('myid').appendChild(newNode);\r\n"+
		"\r\n"+
		"document.bgColor='yellow';"};
	
	public static boolean test(final int index) {
		if (verbose) System.out.println("Javascript - verify execute() works on HTML rendered from memory with getText - script index "+index);
		passed = false;
		
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		final Browser browser = new Browser(shell, SWT.NONE);
		browser.addProgressListener(new ProgressListener() {
			public void changed(ProgressEvent event) {
			}
			public void completed(ProgressEvent event) {
				passed = browser.execute(script[index]);
			}
		});
		shell.open();
		browser.setText(html[index]);
		
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
		if (pluginPath == null) url = Browser8.class.getClassLoader().getResource("browser7.html").toString();
		else url = pluginPath + "/data/browser7.html";
		String[] urls = new String[] {url};
		for (int i = 0; i < urls.length; i++) {
			// TEST TEMPORARILY NOT RUN FOR MOZILLA
			if (!isMozilla) {
				boolean result = test(i); 
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
