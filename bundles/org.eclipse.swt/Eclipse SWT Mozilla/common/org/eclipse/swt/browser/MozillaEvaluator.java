/*******************************************************************************
 * Copyright (c) 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Matthew Painter <matthew.painter@import.io>
 *******************************************************************************/
package org.eclipse.swt.browser;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.mozilla.*;

/**
 * Helper class to evaluate javascript in XULRunner >=24
 * @since 3.104
 */
public class MozillaEvaluator {
	
	/**	 
	 * Allows evaluation of javascript in XULRunner >=24 in a chrome security context.
	 * Execution in XULRunner <= 24 is in this security context by default.
	 *
	 * @param browser the browser which should be the value of the "window" variable in the script execution context
	 * @param script the javascript to be evaluated.
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the browser or javascript is null</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if the version of XULRunner < 24</li>
	 * </ul>
	 *@since 3.104
	 */
	public static Object evaluateAsChrome (Browser browser, String script) {
		
		if (browser == null || script == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		if ((browser.getStyle() & SWT.MOZILLA) == 0) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		if (!MozillaVersion.CheckVersion (MozillaVersion.VERSION_XR24, false)) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		
		Mozilla mozillaWebBrowser = (Mozilla) browser.webBrowser;
		nsIWebBrowser webBrowser = mozillaWebBrowser.webBrowser;
		
		long /*int*/[] result = new long /*int*/[1];
		int rc = webBrowser.QueryInterface(IIDStore.GetIID (nsIInterfaceRequestor.class), result);
		if (rc != XPCOM.NS_OK) Mozilla.error (rc);
		if (result[0] == 0) Mozilla.error (XPCOM.NS_NOINTERFACE);
		nsIInterfaceRequestor interfaceRequestor = new nsIInterfaceRequestor (result[0]);
		result[0] = 0;
		rc = XPCOM.NS_GetServiceManager (result);
		if (rc != XPCOM.NS_OK) Mozilla.error (rc);
		if (result[0] == 0) Mozilla.error (XPCOM.NS_NOINTERFACE);
	
		nsIServiceManager serviceManager = new nsIServiceManager (result[0]);
		result[0] = 0;
		rc = interfaceRequestor.GetInterface (IIDStore.GetIID (nsIDOMWindow.class), result);
		interfaceRequestor.Release ();
		if (rc != XPCOM.NS_OK) Mozilla.error (rc);
		if (result[0] == 0) Mozilla.error (XPCOM.NS_NOINTERFACE);
	
		nsIDOMWindow window = new nsIDOMWindow (result[0]);
		result[0] = 0;
		byte[] aContractID = MozillaDelegate.wcsToMbcs (null, XPCOM.EXECUTE_CONTRACTID, true);
		rc = serviceManager.GetServiceByContractID (aContractID, IIDStore.GetIID (Execute.class), result);
		if (rc != XPCOM.NS_OK) Mozilla.error (rc);
		if (result[0] == 0) Mozilla.error (XPCOM.NS_NOINTERFACE);
	
		Execute execute = new Execute (result[0]);
		result[0] = 0;
		nsEmbedString data = new nsEmbedString ("(function(){" + script + "}())");
		execute.EvalAsChrome(window, data, result);
		data.dispose ();
		execute.Release ();
		if (result[0] == 0) return null;
	
		nsIVariant variant = new nsIVariant (result[0]);
		Object retval = External.convertToJava( variant);
		variant.Release ();
		return retval;
	}
}
