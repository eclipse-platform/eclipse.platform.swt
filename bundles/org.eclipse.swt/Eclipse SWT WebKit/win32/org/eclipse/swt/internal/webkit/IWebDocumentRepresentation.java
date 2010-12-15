package org.eclipse.swt.internal.webkit;

import org.eclipse.swt.internal.ole.win32.COM;
import org.eclipse.swt.internal.ole.win32.IUnknown;

public class IWebDocumentRepresentation extends IUnknown {

public IWebDocumentRepresentation(int /*long*/ address) {
	super(address);
}

public int documentSource (int /*long*/[] source) {
	return COM.VtblCall (8, getAddress (), source);
}

}
