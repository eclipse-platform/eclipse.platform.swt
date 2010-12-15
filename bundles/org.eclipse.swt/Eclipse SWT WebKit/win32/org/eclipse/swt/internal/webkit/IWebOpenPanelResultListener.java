package org.eclipse.swt.internal.webkit;

import org.eclipse.swt.internal.ole.win32.COM;
import org.eclipse.swt.internal.ole.win32.IUnknown;

public class IWebOpenPanelResultListener extends IUnknown {

	public IWebOpenPanelResultListener(int /*long*/ address) {
		super(address);
	}
	
	public int cancel () {
		return COM.VtblCall (4, getAddress ());
	}
	
	public int chooseFilename (int /*long*/ fileName) {
		return COM.VtblCall (3, getAddress (), fileName);
	}

}
