/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
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
 * Browser example snippet: Show an HTML element-specific context menu
 * in a Mozilla Browser.
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
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.browser.*;
import org.mozilla.interfaces.*;

public class Snippet306 {

public static void main (String [] args) {
	Display display = new Display ();
	final Shell shell = new Shell (display);
	shell.setLayout (new FillLayout ());
	final Browser browser;
	try {
		browser = new Browser (shell, SWT.MOZILLA);
	} catch (SWTError e) {
		System.out.println ("Could not instantiate Browser: " + e.getMessage ());
		display.dispose();
		return;
	}
	browser.addProgressListener (new ProgressAdapter () {
		public void completed (ProgressEvent event) {
			nsIWebBrowser webBrowser = (nsIWebBrowser)browser.getWebBrowser ();
			nsIDOMWindow domWindow = webBrowser.getContentDOMWindow ();
			nsIDOMEventTarget target = (nsIDOMEventTarget)domWindow.queryInterface (nsIDOMEventTarget.NS_IDOMEVENTTARGET_IID);
			nsIDOMEventListener listener = new nsIDOMEventListener () {
				public nsISupports queryInterface (String uuid) {
					if (uuid.equals (nsIDOMEventListener.NS_IDOMEVENTLISTENER_IID) ||
						uuid.equals (nsIDOMEventListener.NS_ISUPPORTS_IID)) {
							return this;
					}
					return null;
				}
				public void handleEvent (nsIDOMEvent event) {
					nsIDOMElement element = (nsIDOMElement)event.getTarget ().queryInterface (nsIDOMElement.NS_IDOMELEMENT_IID);
					Menu menu = new Menu (browser);
					MenuItem item = new MenuItem (menu, SWT.NONE);
					item.setText ("custom menu for element with tag: " + element.getTagName ());
					nsIDOMMouseEvent mouseEvent = (nsIDOMMouseEvent)event.queryInterface (nsIDOMMouseEvent.NS_IDOMMOUSEEVENT_IID);
					menu.setLocation (mouseEvent.getScreenX (), mouseEvent.getScreenY ());
					menu.setVisible (true);
				}
			};
			target.addEventListener ("contextmenu", listener, false);
		}
	});
	shell.open ();
	browser.setUrl ("http://www.eclipse.org");
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}

}
