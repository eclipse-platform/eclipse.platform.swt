/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * WebKit in a Browser
 *
 * The requirements for using WebKit-based Browsers are described
 * at http://www.eclipse.org/swt/faq.php#howusewebkit
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.7
 */ 
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.layout.*;

public class Snippet351 {

	public static void main(String [] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		shell.setText("WebKit");
		final Browser browser;
		try {
			browser = new Browser(shell, SWT.WEBKIT);
		} catch (SWTError e) {
			System.out.println("Could not instantiate Browser: " + e.getMessage());
			display.dispose();
			return;
		}
		shell.open();
		browser.setUrl("http://webkit.org");
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}
}