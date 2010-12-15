package org.eclipse.swt.internal.webkit;

import org.eclipse.swt.internal.ole.win32.COM;
import org.eclipse.swt.internal.ole.win32.IUnknown;

public class IWebMutableURLRequestPrivate extends IUnknown {

	public IWebMutableURLRequestPrivate(int /*long*/ address) {
		super(address);
	}
	
	public int cfRequest () {
		return COM.VtblCall (4, getAddress ());
	}
	
	public int setClientCertificate (int /*long*/ cert) {
		return COM.VtblCall (3, getAddress (), cert);
	}
}
