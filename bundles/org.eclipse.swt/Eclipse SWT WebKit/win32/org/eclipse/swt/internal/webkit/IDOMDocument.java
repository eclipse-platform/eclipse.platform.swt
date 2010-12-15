package org.eclipse.swt.internal.webkit;

import org.eclipse.swt.internal.ole.win32.COM;
import org.eclipse.swt.internal.ole.win32.IUnknown;

public class IDOMDocument extends IUnknown {

public IDOMDocument(int /*long*/ address) {
	super(address);
}
 
public int addEventListener (int /*long*/ type, int /*long*/ listener, boolean useCapture) {
	return COM.VtblCall (40, getAddress (), type, listener, useCapture);
}
}
