package org.eclipse.swt.internal.webkit;

import org.eclipse.swt.internal.ole.win32.COM;
import org.eclipse.swt.internal.ole.win32.IUnknown;

public class IWebURLProtectionSpace extends IUnknown {

	public IWebURLProtectionSpace(int /*long*/ address) {
		super(address);
	}
	
	public int host (int /*long*/[] result) {
		return COM.VtblCall (4, getAddress (), result);
	}
	
	public int port (int /*long*/[] result) {
		return COM.VtblCall (8, getAddress (), result);
	}
	
	public int realm (int /*long*/[] result) {
		return COM.VtblCall (11, getAddress (), result);
	}
	
	public int isProxy (int /*long*/[] result) {
		return COM.VtblCall (7, getAddress (), result);
	}
	

}
