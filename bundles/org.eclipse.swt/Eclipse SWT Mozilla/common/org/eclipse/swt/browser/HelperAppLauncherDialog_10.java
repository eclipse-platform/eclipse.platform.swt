/*******************************************************************************
 * Copyright (c) 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.browser;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.C;
import org.eclipse.swt.internal.mozilla.*;
import org.eclipse.swt.widgets.*;

/**
 * This class implements the nsIHelperAppLauncherDialog interface for mozilla
 * versions >= 4.  For mozilla versions >= 1.9, < 4 this interface is
 * implemented by class HelperAppLauncherDialog_1_9.  HelperAppLauncherDialogFactory
 * determines at runtime which of these classes to instantiate. 
 */
class HelperAppLauncherDialog_10 extends HelperAppLauncherDialog_1_9 {
	XPCOMObject supports;
	XPCOMObject helperAppLauncherDialog;
	int refCount = 0;

HelperAppLauncherDialog_10 () {
	createCOMInterfaces ();
}

/* nsIHelperAppLauncherDialog */

int Show (long /*int*/ aLauncher, long /*int*/ aContext, int aReason) {
	nsIHelperAppLauncher_8 helperAppLauncher = new nsIHelperAppLauncher_8 (aLauncher);
	return helperAppLauncher.SaveToDisk (0, 0);
}

int PromptForSaveToFile (long /*int*/ aLauncher, long /*int*/ aWindowContext, long /*int*/ aDefaultFileName, long /*int*/ aSuggestedFileExtension, int aForcePrompt, long /*int*/ _retval) {
	int length = XPCOM.strlen_PRUnichar (aDefaultFileName);
	char[] dest = new char[length];
	XPCOM.memmove (dest, aDefaultFileName, length * 2);
	String defaultFile = new String (dest);

	length = XPCOM.strlen_PRUnichar (aSuggestedFileExtension);
	dest = new char[length];
	XPCOM.memmove (dest, aSuggestedFileExtension, length * 2);
	String suggestedFileExtension = new String (dest);

	Shell shell = new Shell ();
	FileDialog fileDialog = new FileDialog (shell, SWT.SAVE);
	fileDialog.setFileName (defaultFile);
	fileDialog.setFilterExtensions (new String[] {suggestedFileExtension});
	String name = fileDialog.open ();
	shell.close ();
	if (name == null) {
		nsIHelperAppLauncher_8 launcher = new nsIHelperAppLauncher_8 (aLauncher);
		int rc = launcher.Cancel (XPCOM.NS_BINDING_ABORTED);
		if (rc != XPCOM.NS_OK) Mozilla.error (rc);
		return XPCOM.NS_ERROR_FAILURE;
	}
	nsEmbedString path = new nsEmbedString (name);
	long /*int*/[] result = new long /*int*/[1];
	int rc = XPCOM.NS_NewLocalFile (path.getAddress (), 1, result);
	path.dispose ();
	if (rc != XPCOM.NS_OK) Mozilla.error (rc);
	if (result[0] == 0) Mozilla.error (XPCOM.NS_ERROR_NULL_POINTER);
	/* Our own nsIDownload has been registered during the Browser initialization. It will be invoked by Mozilla. */
	XPCOM.memmove (_retval, result, C.PTR_SIZEOF);	
	return XPCOM.NS_OK;
}
}
