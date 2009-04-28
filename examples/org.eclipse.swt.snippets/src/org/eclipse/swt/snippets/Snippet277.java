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
import org.eclipse.swt.*;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.mozilla.interfaces.*;
import org.mozilla.xpcom.Mozilla;

public class Snippet277 {
	static Shell shell;
	static Table table;

	public static void main (String [] args) {
		Display display = new Display ();
		shell = new Shell (display);
		shell.setLayout(new GridLayout ());
		shell.setText ("Custom Download Handler");

		Browser browser;
		try {
			browser = new Browser (shell, SWT.MOZILLA);
		} catch (SWTError e) {
			System.out.println ("Could not instantiate Browser: " + e.getMessage ());
			display.dispose();
			return;
		}
		GridData data = new GridData (GridData.FILL_BOTH);
		data.minimumHeight = 800;
		data.minimumWidth = 800;
		browser.setLayoutData (data);

		table = new Table (shell, SWT.NONE);
		table.setForeground (display.getSystemColor (SWT.COLOR_RED));
		data = new GridData (GridData.FILL_HORIZONTAL);
		data.exclude = true;
		table.setLayoutData (data);
		new TableColumn (table, SWT.NONE);
		new TableColumn (table, SWT.NONE);

		nsIComponentRegistrar registrar = Mozilla.getInstance ().getComponentRegistrar ();
		String NS_DOWNLOAD_CID = "e3fa9D0a-1dd1-11b2-bdef-8c720b597445";
		String NS_TRANSFER_CONTRACTID = "@mozilla.org/transfer;1";
		registrar.registerFactory (NS_DOWNLOAD_CID, "Transfer", NS_TRANSFER_CONTRACTID, new nsIFactory () {
			public nsISupports queryInterface (String uuid) {
				if (uuid.equals (nsIFactory.NS_IFACTORY_IID) ||
					uuid.equals (nsIFactory.NS_ISUPPORTS_IID)) return this;
				return null;
			}
			public nsISupports createInstance (nsISupports outer, String iid) {
				return createTransfer ();
			}
			public void lockFactory (boolean lock) {}
		});

		browser.setUrl ("http://www.eclipse.org/downloads");
		shell.pack ();
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
					removeFromTable ();
				}
			}
			public void onProgressChange64 (nsIWebProgress webProgress, nsIRequest request, long curSelfProgress, long maxSelfProgress, long curTotalProgress, long maxTotalProgress) {
				long currentKBytes = curTotalProgress / 1024;
				long totalKBytes = maxTotalProgress / 1024;
				tableItem.setText (1, baseString + " (" + currentKBytes + "/" + totalKBytes + ")");
				table.getColumn (1).pack ();
			}
			public void init (nsIURI source, nsIURI target, String displayName, nsIMIMEInfo MIMEInfo, double startTime, nsILocalFile tempFile, final nsICancelable cancelable) {
				tableItem = new TableItem (table, SWT.NONE);
				button = new Button (table, SWT.PUSH);
				button.setText ("Cancel");
				button.pack ();
				button.addListener (SWT.Selection, new Listener () {
					public void handleEvent (Event event) {
						cancelable.cancel (Mozilla.NS_ERROR_ABORT);
						removeFromTable ();
					}
				});
				TableEditor editor = new TableEditor (table);
				editor.setEditor (button, tableItem, 0);
				editor.minimumWidth = button.computeSize (SWT.DEFAULT, SWT.DEFAULT).x;
				baseString = "Downloading to " + target.getPath ();
				tableItem.setText (1, baseString);
				if (table.getItemCount () == 1) {
					((GridData)table.getLayoutData ()).exclude = false;	/* show the table */
					table.getColumn (0).setWidth (editor.minimumWidth);
				}
				table.getColumn (1).pack ();
				table.getShell ().layout ();
			}
			public void onStatusChange (nsIWebProgress webProgress, nsIRequest request, long status, String message) {}
			public void onSecurityChange (nsIWebProgress webProgress, nsIRequest request, long state) {}
			public void onProgressChange (nsIWebProgress webProgress, nsIRequest request, int curSelfProgress, int maxSelfProgress, int curTotalProgress, int maxTotalProgress) {}
			public void onLocationChange (nsIWebProgress webProgress, nsIRequest request, nsIURI location) {}

			/* the following are not part of the nsITransfer interface but are here for the snippet's convenience */
			Button button;
			TableItem tableItem;
			String baseString;

			void removeFromTable () {
				tableItem.dispose ();
				button.dispose ();
				if (table.getItemCount () == 0) {
					((GridData)table.getLayoutData ()).exclude = true;	/* hide the table */
				}
				table.getColumn (1).pack ();
				table.getShell ().layout ();
			}
		};
	}
}
