/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Mozilla Communicator client code, released March 31, 1998.
 *
 * The Initial Developer of the Original Code is
 * Netscape Communications Corporation.
 * Portions created by Netscape are Copyright (C) 1998-1999
 * Netscape Communications Corporation.  All Rights Reserved.
 *
 * Contributor(s):
 *
 * IBM
 * -  Binding to permit interfacing between Mozilla and SWT
 * -  Copyright (C) 2003, 2013 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;


public class nsIWebBrowser extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + ((IsXULRunner10() || IsXULRunner24() || IsXULRunner31()) ? 9 : 7);

	static final String NS_IWEBBROWSER_IID_STR = "69e5df00-7b8b-11d3-af61-00a024ffc08c";
	static final String NS_IWEBBROWSER_10_IID_STR = "33e9d001-caab-4ba9-8961-54902f197202";

	static {
		IIDStore.RegisterIID(nsIWebBrowser.class, MozillaVersion.VERSION_BASE, new nsID(NS_IWEBBROWSER_IID_STR));
		IIDStore.RegisterIID(nsIWebBrowser.class, MozillaVersion.VERSION_XR10, new nsID(NS_IWEBBROWSER_10_IID_STR));
	}

	public nsIWebBrowser(long /*int*/ address) {
		super(address);
	}

	public int AddWebBrowserListener(long /*int*/ aListener, nsID aIID) {
		return XPCOM.VtblCall(this.getMethodIndex("addWebBrowserListener"), getAddress(), aListener, aIID);
	}

	public int RemoveWebBrowserListener(long /*int*/ aListener, nsID aIID) {
		return XPCOM.VtblCall(this.getMethodIndex("removeWebBrowserListener"), getAddress(), aListener, aIID);
	}

	public int SetContainerWindow(long /*int*/ aContainerWindow) {
		return XPCOM.VtblCall(this.getSetterIndex("containerWindow"), getAddress(), aContainerWindow);
	}

	public int SetParentURIContentListener(long /*int*/ aParentURIContentListener) {
		return XPCOM.VtblCall(this.getSetterIndex("parentURIContentListener"), getAddress(), aParentURIContentListener);
	}

	public int GetContentDOMWindow(long /*int*/[] aContentDOMWindow) {
		return XPCOM.VtblCall(this.getGetterIndex("contentDOMWindow"), getAddress(), aContentDOMWindow);
	}
}
