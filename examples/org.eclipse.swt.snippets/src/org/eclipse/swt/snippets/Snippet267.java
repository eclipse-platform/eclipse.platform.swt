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

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.mozilla.interfaces.*;

/*
 * Browser example snippet: Toggle a Mozilla Browser between Design mode and View mode.
 * 
 * IMPORTANT: For this snippet to work properly all of the requirements
 * for using JavaXPCOM in a stand-alone application must be satisfied
 * (see http://www.eclipse.org/swt/faq.php#howusejavaxpcom).
 * 
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.3
 */
public class Snippet267 {
	static Browser browser;
	public static void main (String [] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(2, true));
		shell.setText("Use Mozilla's Design Mode");
		try {
			browser = new Browser(shell, SWT.MOZILLA);
		} catch (SWTError e) {
			System.out.println("Could not instantiate Browser: " + e.getMessage());
			display.dispose();
			return;
		}
		browser.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 2, 1));

		final Button offButton = new Button(shell, SWT.RADIO);
		offButton.setText("Design Mode Off");
		offButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (!offButton.getSelection()) return;
				setDesignMode("off");
			}
		});
		final Button onButton = new Button(shell, SWT.RADIO);
		onButton.setText("Design Mode On");
		onButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (!onButton.getSelection()) return;
				boolean success = setDesignMode("on");
				if (!success) {
					onButton.setSelection(false);
					offButton.setSelection(true);
				}
			}
		});
		offButton.setSelection(true);

		browser.setUrl("http://www.google.com");
		shell.setSize(400,400);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}
	public static boolean setDesignMode(String value) {
		nsIWebBrowser webBrowser = (nsIWebBrowser)browser.getWebBrowser();
		if (webBrowser == null) {
			System.out.println("Could not get the nsIWebBrowser from the Browser widget");
			return false;
		}
		nsIDOMWindow window = webBrowser.getContentDOMWindow();
		nsIDOMDocument document = window.getDocument();
		nsIDOMNSHTMLDocument nsDocument = (nsIDOMNSHTMLDocument)document.queryInterface(nsIDOMNSHTMLDocument.NS_IDOMNSHTMLDOCUMENT_IID);
		nsDocument.setDesignMode(value);
		return true;
	}
}
