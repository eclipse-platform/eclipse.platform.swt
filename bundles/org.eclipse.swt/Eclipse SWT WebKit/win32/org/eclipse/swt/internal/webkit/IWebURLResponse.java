package org.eclipse.swt.internal.webkit;

import org.eclipse.swt.internal.ole.win32.COM;
import org.eclipse.swt.internal.ole.win32.IUnknown;

public class IWebURLResponse extends IUnknown {

	public IWebURLResponse (int /*long*/ address) {
		super(address);
	}
	
	public int expectedContentLength (long [] result) {
		return COM.VtblCall (3, getAddress (), result);
	}
	
	public int suggestedFilename (int /*long*/ []result) {
		return COM.VtblCall (8, getAddress (), result);
	}
	
	public int URL (int /*long*/ []result) {
		return COM.VtblCall (8, getAddress (), result);
	}
	
}
