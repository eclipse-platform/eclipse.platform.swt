/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
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
 * Browser example snippet: Zoom HTML page content in a Mozilla Browser
 * (note that XULRunner 1.9 or newer is required).
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
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.layout.*;
import org.mozilla.interfaces.*;

public class Snippet329 {
	static int zoom = 10;
	static Browser browser;

public static void main(String [] args) {
	Display display = new Display();
	Shell shell = new Shell(display);
	shell.setLayout(new GridLayout(2, true));

	try {
		browser = new Browser(shell, SWT.MOZILLA);
	} catch (SWTError e) {
		System.out.println("Could not instantiate Browser: " + e.getMessage());
		display.dispose();
		return;
	}
	GridData data = new GridData();
	data.heightHint = data.widthHint = 400;
	data.horizontalSpan = 2;
	browser.setLayoutData(data);

	Button zoomIn = new Button(shell, SWT.PUSH);
	zoomIn.setText("Zoom In");
	zoomIn.addListener(SWT.Selection, new Listener() {
		public void handleEvent(Event event) {
			setZoom(++zoom);
		}
	});
	Button zoomOut = new Button(shell, SWT.PUSH);
	zoomOut.setText("Zoom Out");
	zoomOut.addListener(SWT.Selection, new Listener() {
		public void handleEvent(Event event) {
			if (zoom > 1) {
				setZoom(--zoom);
			}
		}
	});

	shell.pack();
	shell.open();
	browser.setUrl("http://www.eclipse.org");
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	display.dispose();
}
static void setZoom(int zoom) {
	nsIWebBrowser webBrowser = (nsIWebBrowser)browser.getWebBrowser();
	nsIInterfaceRequestor req = (nsIInterfaceRequestor)webBrowser.queryInterface(nsIInterfaceRequestor.NS_IINTERFACEREQUESTOR_IID);
	nsIDocShell docShell = (nsIDocShell)req.getInterface(nsIDocShell.NS_IDOCSHELL_IID);
	nsIContentViewer contentView = docShell.getContentViewer();
	nsIMarkupDocumentViewer docView = (nsIMarkupDocumentViewer)contentView.queryInterface(nsIMarkupDocumentViewer.NS_IMARKUPDOCUMENTVIEWER_IID);
	float value = zoom / 10f;
	System.out.println("zoom: " + value);
	docView.setFullZoom(value);
}

}