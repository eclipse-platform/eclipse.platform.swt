/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.VisibilityWindowListener;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Automated Test Suite for class org.eclipse.swt.browser.VisibilityWindowListener
 *
 * @see org.eclipse.swt.browser.VisibilityWindowListener
 */
public class Test_org_eclipse_swt_browser_VisibilityWindowListener extends TestCase {

public void test_hideLorg_eclipse_swt_browser_WindowEvent() {
	Display display = Display.getCurrent();
	Shell shell = new Shell(display);
	Browser browser = new Browser(shell, SWT.NONE);
	browser.addVisibilityWindowListener(new VisibilityWindowListener() {
		public void hide(WindowEvent event) {
		}
		public void show(WindowEvent event) {
		}
	});
	shell.close();
}
}
