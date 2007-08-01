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

import org.eclipse.swt.*;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.widgets.*;
import org.mozilla.interfaces.*;
import org.mozilla.xpcom.Mozilla;

/*
 * Browser example snippet: Implement a custom download handler for a Mozilla Browser.
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
public class Snippet277 {
	static Shell shell;
	static Label statusLabel;
	static Button cancelButton;
	static nsICancelable nsICancelable;

	public static void main (String [] args) {
		Display display = new Display ();
		shell = new Shell (display);
		shell.setText ("Custom Download Handler");

		Browser browser;
		try {
			browser = new Browser (shell, SWT.MOZILLA);
		} catch (SWTError e) {
			System.out.println ("Could not instantiate Browser: " + e.getMessage ());
			return;
		}

		statusLabel = new Label (shell, SWT.WRAP | SWT.BORDER);
		cancelButton = new Button (shell, SWT.PUSH);
		cancelButton.setText ("Cancel");
		cancelButton.setEnabled (false);
		cancelButton.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event event) {
				if (nsICancelable != null) {
					nsICancelable.cancel (Mozilla.NS_ERROR_ABORT);
					nsICancelable = null;
				}
				cancelButton.setEnabled (false);
				statusLabel.setData ("");
				statusLabel.setText ("");
			}
		});

		nsIComponentRegistrar registrar = Mozilla.getInstance ().getComponentRegistrar ();
		String NS_DOWNLOAD_CID = "e3fa9D0a-1dd1-11b2-bdef-8c720b597445";
		String NS_TRANSFER_CONTRACTID = "@mozilla.org/transfer;1";
		final nsITransfer transfer = createTransfer ();
		registrar.registerFactory (NS_DOWNLOAD_CID, "Transfer", NS_TRANSFER_CONTRACTID, new nsIFactory () {
			public nsISupports queryInterface (String uuid) {
				if (uuid.equals (nsIFactory.NS_IFACTORY_IID) ||
					uuid.equals (nsIFactory.NS_ISUPPORTS_IID)) return this;
				return null;
			}
			public nsISupports createInstance (nsISupports outer, String iid) {
				return transfer;
			}
			public void lockFactory (boolean lock) {}
		});

		browser.setUrl ("http://download.eclipse.org/downloads");
		shell.setBounds (10, 10, 800, 600);
		browser.setBounds (10, 10, 780, 450);
		statusLabel.setBounds (10, 470, 680, 100);
		cancelButton.setBounds (710, 470, 80, 50);
		shell.open ();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}

	static nsITransfer createTransfer () {
		/* nsITransfer is documented at http://www.xulplanet.com/references/xpcomref/ifaces/nsITransfer.html */
		return new nsITransfer () {
			public nsISupports queryInterface (String uuid) {
				if (uuid.equals (nsITransfer.NS_ITRANSFER_IID) ||
					uuid.equals (nsITransfer.NS_IWEBPROGRESSLISTENER2_IID) ||
					uuid.equals (nsITransfer.NS_IWEBPROGRESSLISTENER_IID) ||
					uuid.equals (nsITransfer.NS_ISUPPORTS_IID)) return this;
				return null;
			}
			public void onStateChange (nsIWebProgress webProgress, nsIRequest request, long stateFlags, long status) {
				if ((stateFlags & nsIWebProgressListener.STATE_STOP) != 0) {
					nsICancelable = null;
					cancelButton.setEnabled (false);
					statusLabel.setData ("");
					statusLabel.setText ("");
				}
			}
			public void onProgressChange64 (nsIWebProgress webProgress, nsIRequest request, long curSelfProgress, long maxSelfProgress, long curTotalProgress, long maxTotalProgress) {
				long currentKBytes = curTotalProgress / 1024;
				long totalKBytes = maxTotalProgress / 1024;
				String string = (String)statusLabel.getData () + " (" + currentKBytes + "/" + totalKBytes + ")";
				statusLabel.setText (string);
			}
			public void init (nsIURI source, nsIURI target, String displayName, nsIMIMEInfo MIMEInfo, double startTime, nsILocalFile tempFile, nsICancelable cancelable) {
				nsICancelable = cancelable;
				cancelButton.setEnabled (true);
				String string = "Downloading " + source.getSpec () + " to " + target.getSpec ();
				statusLabel.setData (string);
				statusLabel.setText (string);
			}
			public void onStatusChange (nsIWebProgress webProgress, nsIRequest request, long status, String message) {}
			public void onSecurityChange (nsIWebProgress webProgress, nsIRequest request, long state) {}
			public void onProgressChange (nsIWebProgress webProgress, nsIRequest request, int curSelfProgress, int maxSelfProgress, int curTotalProgress, int maxTotalProgress) {}
			public void onLocationChange (nsIWebProgress webProgress, nsIRequest request, nsIURI location) {}
		};
	}
}
