package org.eclipse.swt.internal.webkit;

import org.eclipse.swt.internal.ole.win32.COM;
import org.eclipse.swt.internal.ole.win32.IUnknown;

public class IWebIBActions extends IUnknown {

public IWebIBActions (int /*long*/ address) {
	super (address);
}

public int canGoBack (int /*long*/ sender, int /*long*/[] result) {
	return COM.VtblCall (6, getAddress (), sender, result);
}

public int canGoForward (int /*long*/ sender, int /*long*/[] result) {
	return COM.VtblCall (8, getAddress (), sender, result);
}

public int reload (int /*long*/ sender) {
	return COM.VtblCall (5, getAddress (), sender);
}

public int stopLoading (int /*long*/ sender) {
	return COM.VtblCall (4, getAddress (), sender);
}

}
