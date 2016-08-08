/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit.browser;

import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * This test case tests if 'execute()' works on HTML rendered from memory with getText.
 * If execute works, the background of the browser should turn yellow and 'at least when I'm not around'
 * should be appended in between the lines.
 *
 */
public class Browser8 {
	public static boolean verbose = false;  //Set to true to visually inspect change.

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


		AtomicBoolean passed = new AtomicBoolean(); // false on creation.

		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		final Browser browser = new Browser(shell, SWT.NONE);

		final AtomicBoolean finished = new AtomicBoolean(); // initially false.

		browser.addProgressListener(new ProgressListener() {
			@Override
			public void changed(ProgressEvent event) {
			}
			@Override
			public void completed(ProgressEvent event) {
				if (verbose) runLoopTimer(display, shell, 1000); // slow down execution for visual inspection.

				passed.set(browser.execute(script[index]));

				if (verbose) runLoopTimer(display, shell, 1000); // slow down execution for visual inspection.
				finished.set(true);
			}
		});
		shell.open();
		browser.setText(html[index]);

	    while (!passed.get() && !finished.get()) {
	    	runLoopTimer(display, shell, 1000);
	    	if (!display.isDisposed())
	    		display.readAndDispatch ();
	    }
		display.dispose();
		return passed.get();
	}

	static boolean runLoopTimer(final Display display, final Shell shell, final int milliseconds) {
		final boolean[] timeout = {false};
		new Thread() {
			@Override
			public void run() {
				try {
					for (int i = 0; i < Math.max(milliseconds / 1000, 1); i++) {
						Thread.sleep(milliseconds);
						if (display.isDisposed() || shell.isDisposed()) return;
					}
				}
				catch (Exception e) {}
				timeout[0] = true;
				/* wake up the event loop */
				if (!display.isDisposed()) {
					display.asyncExec(() -> {
						if (!shell.isDisposed()) shell.redraw();
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
			boolean result = test(i);
			if (verbose) System.out.print(result ? "." : "E");
			if (!result) fail++;
		}
		return fail == 0;
	}

	public static void main(String[] argv) {
		System.out.println("\r\nTests Finished. SUCCESS: "+test());
	}
}
