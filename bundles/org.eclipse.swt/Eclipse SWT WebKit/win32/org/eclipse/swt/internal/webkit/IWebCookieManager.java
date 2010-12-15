package org.eclipse.swt.internal.webkit;

import org.eclipse.swt.internal.ole.win32.COM;
import org.eclipse.swt.internal.ole.win32.IUnknown;

public class IWebCookieManager extends IUnknown {

public IWebCookieManager(int /*long*/ address) {
	super(address);
}

public int cookieStorage (int /*long*/[] storage) {
	return COM.VtblCall (3, getAddress (), storage);
}

}
