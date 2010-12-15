package org.eclipse.swt.internal.webkit;

import org.eclipse.swt.internal.ole.win32.COM;
import org.eclipse.swt.internal.ole.win32.IUnknown;

public class IWebDownload extends IUnknown {

	public IWebDownload(int /*long*/ address) {
		super(address);
	}
	
	public int cancel () {
		return COM.VtblCall(4, getAddress());
	}
	
	public int cancelForResume () {
		return COM.VtblCall(5, getAddress());
	}
	
	public int setDeletesFileUponFailure (boolean deletesFileUponFailure) {
		return COM.VtblCall(12, getAddress(), deletesFileUponFailure);
	}
	
	public int setDestination (int /*long*/ path, boolean allowOverwrite) {
		return COM.VtblCall(13, getAddress(), path, allowOverwrite);
	}
	
	public int start () {
		return COM.VtblCall(6, getAddress());
	}

}
