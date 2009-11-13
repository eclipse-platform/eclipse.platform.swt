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
 * Browser example snippet: Examine request and response headers in a Mozilla Browser
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;
import org.mozilla.interfaces.*;
import org.mozilla.xpcom.Mozilla;

public class Snippet321 {

public static void main(String [] args) {
	Display display = new Display();
	Shell shell = new Shell(display);
	shell.setBounds(10,10,200,200);
	shell.setLayout(new FillLayout());
	Browser browser;
	try {
		browser = new Browser(shell, SWT.MOZILLA);
	} catch (SWTError e) {
		System.out.println("Could not instantiate Browser: " + e.getMessage());
		display.dispose();
		return;
	}

	nsIObserver observer = new nsIObserver() {
		public nsISupports queryInterface(String aIID) {
			if (aIID.equals(nsIObserver.NS_IOBSERVER_IID) || aIID.equals(nsIObserver.NS_ISUPPORTS_IID)) {
				return this;
			}
			return null;
		}
		public void observe(nsISupports subject, String topic, String data) {
			nsIHttpChannel channel = (nsIHttpChannel)subject.queryInterface(nsIHttpChannel.NS_IHTTPCHANNEL_IID);
			nsIRequest request = (nsIRequest)subject.queryInterface(nsIRequest.NS_IREQUEST_IID);
			if (topic.equals("http-on-modify-request")) {
				System.out.println("---------------------\nSome Request Header Values for " + request.getName() + ':');
				printRequestHeader(channel, "accept");
				printRequestHeader(channel, "accept-language");
				printRequestHeader(channel, "host");
				printRequestHeader(channel, "user-agent");
			} else {
				/* http-on-examine-response */
				System.out.println("---------------------\n\tSome Response Header Values for " + request.getName() + ':');
				printResponseHeader(channel, "content-length");
				printResponseHeader(channel, "content-type");
				printResponseHeader(channel, "expires");
				printResponseHeader(channel, "server");
			}
		}
		void printRequestHeader(nsIHttpChannel channel, String header) {
			try {
				System.out.println(header + '=' + channel.getRequestHeader(header));
			} catch (Exception e) {
				// the header did not exist, just continue
			}
		}
		void printResponseHeader(nsIHttpChannel channel, String header) {
			try {
				System.out.println('\t' + header + '=' + channel.getResponseHeader(header));
			} catch (Exception e) {
				// the header did not exist, just continue
			}
		}
	};
	nsIObserverService observerService = (nsIObserverService)Mozilla.getInstance().getServiceManager().getServiceByContractID(
		"@mozilla.org/observer-service;1", nsIObserverService.NS_IOBSERVERSERVICE_IID);
	observerService.addObserver(observer, "http-on-modify-request", false);
	observerService.addObserver(observer, "http-on-examine-response", false);

	browser.setUrl("http://www.eclipse.org");
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	display.dispose();
}

}
