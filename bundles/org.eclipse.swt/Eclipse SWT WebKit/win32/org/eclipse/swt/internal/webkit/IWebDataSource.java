package org.eclipse.swt.internal.webkit;

import org.eclipse.swt.internal.ole.win32.COM;
import org.eclipse.swt.internal.ole.win32.IUnknown;

public class IWebDataSource extends IUnknown {

public IWebDataSource (int /*long*/ address) {
	super(address);
}

public int pageTitle (int /*long*/[] title) {
	return COM.VtblCall (12, getAddress (), title);
}

public int representation (int /*long*/[] rep) {
	return COM.VtblCall (5, getAddress (), rep);
}

public int request (int /*long*/[] request) {
	return COM.VtblCall (8, getAddress (), request);
}

public int webFrame (int /*long*/[] frame) {
	return COM.VtblCall (6, getAddress (), frame);
}
}
