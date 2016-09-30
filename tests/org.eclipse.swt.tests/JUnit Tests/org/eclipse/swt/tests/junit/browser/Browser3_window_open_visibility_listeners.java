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

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.VisibilityWindowListener;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.test.Screenshots;

/**
 * Tests a range of listeners
 * - open window listener (Notification when new window needs to be opened)
 * - location listener (change of url)
 * - window visibility (when window needs to be shown or hidden)
 * - progress listener
 *
 */
public class Browser3_window_open_visibility_listeners {
	public static boolean verbose = false;
	public static boolean passed = false;
	public static boolean openWindow, locationChanging, locationChanged, visibilityShow, progressCompleted;

	public static boolean test1(String url) {
		if (verbose) System.out.println("javascript window.open - args: "+url+" Expected Event Sequence: Browser1:OpenWindow.open > { Browser2:Location.changing, Browser2:Visibility.show, Browser2:Location.changed } > Browser2:Progress.completed");
		passed = false;
		locationChanging = locationChanged = progressCompleted = false;

		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		final Browser browser1 = new Browser(shell, SWT.NONE);
		final Shell shell2 = new Shell(display);
		shell2.setLayout(new FillLayout());
		final Browser browser2 = new Browser(shell2, SWT.NONE);
		browser1.addOpenWindowListener(event -> {
			if (verbose) System.out.println("Browser1:OpenWindow.open");
			openWindow = true;
			if (verbose) Screenshots.takeScreenshot(Browser3_window_open_visibility_listeners.class, "openWindow");
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
		});
		browser2.addLocationListener(new LocationListener() {
			private int changed = 1;
			@Override
			public void changed(LocationEvent event) {
				if (verbose) System.out.println("Browser2:Location.changed to " + event.location);
				if (verbose) Screenshots.takeScreenshot(Browser3_window_open_visibility_listeners.class, "changed" + changed++);
				if (!openWindow || !locationChanging) {
					if (verbose) System.out.println("Failure - LocationEvent.changing received at wrong time");
					passed = false;
					shell.close();
					return;
				}
				locationChanged = true;
			}
			@Override
			public void changing(LocationEvent event) {
				if (verbose) System.out.println("Browser2:Location.changing to " + event.location);
				if (!openWindow) {
					if (verbose) System.out.println("Failure - LocationEvent.changing received at wrong time");
					passed = false;
					shell.close();
					return;
				}
				locationChanging = true;
			}
		});
		browser2.addVisibilityWindowListener(new VisibilityWindowListener() {
			@Override
			public void hide(WindowEvent event) {
				if (verbose) System.out.println("Browser2:Visibility.hide");
				if (verbose) System.out.println("Failure - did not expect VisibilityEvent.hide");
				passed = false;
				shell.close();
			}
			@Override
			public void show(WindowEvent event) {
				if (verbose) System.out.println("Browser2:Visibility.show");
				if (verbose) Screenshots.takeScreenshot(Browser3_window_open_visibility_listeners.class, "show");
				if (!openWindow) {
					if (verbose) System.out.println("Failure - VisibilityEvent.show received at wrong time");
					passed = false;
					shell.close();
					return;
				}
				shell2.open();
			}
		});
		browser2.addProgressListener(new ProgressListener() {
			@Override
			public void changed(ProgressEvent event) {
//				if (verbose) System.out.println("Browser2:Progress.changed");
			}

			@Override
			public void completed(ProgressEvent event) {
				if (verbose) System.out.println("Browser2:Progress.completed");
				new Thread() {
					@Override
					public void run() {
						if (verbose) System.out.println("timer start");
						try { sleep(2000); } catch (Exception e) {}
						passed = true;
						if (verbose) Screenshots.takeScreenshot(Browser3_window_open_visibility_listeners.class, "completed");
						if (!display.isDisposed())
							display.asyncExec(() -> {
								if (verbose) System.out.println("timer asyncexec shell.close");
								if (!shell.isDisposed()) shell.close();
							});
						if (verbose) System.out.println("timer over");
					}
				}.start();
			}
		});

		shell.open();
		browser1.setUrl(url);

		boolean timeout = runLoopTimer(display, shell, 600);
		if (timeout) {
			if (verbose) System.out.println("timed out");
			passed = false;
		}
		if (verbose) Screenshots.takeScreenshot(Browser3_window_open_visibility_listeners.class, "end");
		display.dispose();
		return passed;
	}

	static boolean runLoopTimer(final Display display, final Shell shell, final int seconds) {
		final boolean[] timeout = {false};
		new Thread() {
			@Override
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
		String url;
		String pluginPath = System.getProperty("PLUGIN_PATH");
		if (verbose) System.out.println("PLUGIN_PATH <"+pluginPath+">");
		if (pluginPath == null) url = Browser3_window_open_visibility_listeners.class.getClassLoader().getResource("browser3.html").toString();
		else url = pluginPath + "/data/browser3.html";
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
