/*******************************************************************************
 * Copyright (c) 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.browser;

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

/**
 * A <code>WindowEvent</code> is sent by a {@link Browser} when
 * a new window needs to be created or when an existing window needs to be
 * closed. This notification occurs when a javascript command such as
 * <code>window.open</code> or <code>window.close</code> gets executed by
 * a <code>Browser</code>.
 *
 * <p>
 * The following example shows how <code>WindowEvent</code>'s are typically
 * handled.
 * 
 * <code><pre>
 *	public static void main(String[] args) {
 *		Display display = new Display();
 *		Shell shell = new Shell(display);
 *		shell.setText("Main Window");
 *		shell.setLayout(new FillLayout());
 *		Browser browser = new Browser(shell, SWT.NONE);
 *		initialize(display, browser);
 *		shell.open();
 *		browser.setUrl("http://www.eclipse.org");
 *		while (!shell.isDisposed()) {
 *			if (!display.readAndDispatch())
 *				display.sleep();
 *		}
 *		display.dispose();
 *	}
 *
 *	static void initialize(final Display display, Browser browser) {
 *		browser.addOpenWindowListener(new OpenWindowListener() {
 *			public void open(WindowEvent event) {
 *				Shell shell = new Shell(display);
 *				shell.setText("New Window");
 *				shell.setLayout(new FillLayout());
 *				Browser browser = new Browser(shell, SWT.NONE);
 *				initialize(display, browser);
 *				event.browser = browser;
 *			}
 *		});
 *		browser.addVisibilityWindowListener(new VisibilityWindowListener() {
 *			public void hide(WindowEvent event) {
 *				Browser browser = (Browser)event.widget;
 *				Shell shell = browser.getShell();
 *				shell.setVisible(false);
 *			}
 *			public void show(WindowEvent event) {
 *				Browser browser = (Browser)event.widget;
 *				Shell shell = browser.getShell();
 *				if (event.location != null) shell.setLocation(event.location);
 *				if (event.size != null) {
 *					Point size = event.size;
 *					shell.setSize(shell.computeSize(size.x, size.y));
 *				}
 *				shell.open();
 *			}
 *		});
 *		browser.addCloseWindowListener(new CloseWindowListener() {
 *			public void close(WindowEvent event) {
 *				Browser browser = (Browser)event.widget;
 *				Shell shell = browser.getShell();
 *				shell.close();
 *			}
 *		});
 *	}
 * </pre></code>
 * 
 * <p>
 * NOTE: The API in the browser package is NOT finalized.
 * Use at your own risk, because it will most certainly change.
 * The only reason this API is being released at this time is so that 
 * other teams can try it out.
 * </p>
 * 
 * @see CloseWindowListener
 * @see OpenWindowListener
 * @see VisibilityWindowListener
 * 
 * @since 3.0
 */
public class WindowEvent extends TypedEvent {
	
	/** 
	 * <code>Browser</code> provided by the application. Default is <code>null</code>
	 * and <code>null</code> will cancel the associated navigation request.
	 */
	public Browser browser;

	/** 
	 * Requested location for the <code>Shell</code> hosting the <code>Browser</code>.
	 * It is <code>null</code> if no location has been requested.
	 */
	public Point location;

	/** 
	 * Requested client size for the <code>Shell</code> hosting the <code>Browser</code>.
	 * It is <code>null</code> if no size has been requested.
	 */
	public Point size;
	
WindowEvent(Widget w) {
	super(w);
}
}
