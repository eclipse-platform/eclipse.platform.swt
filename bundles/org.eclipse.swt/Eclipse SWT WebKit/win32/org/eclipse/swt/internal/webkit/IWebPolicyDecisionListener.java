package org.eclipse.swt.internal.webkit;

import org.eclipse.swt.internal.ole.win32.COM;
import org.eclipse.swt.internal.ole.win32.IUnknown;

public class IWebPolicyDecisionListener extends IUnknown{

public IWebPolicyDecisionListener(int /*long*/ address) {
	super(address);
}

public int download () {
	return COM.VtblCall (4, getAddress ());
}

public int ignore () {
	return COM.VtblCall (5, getAddress ());
}

public int use () {
	return COM.VtblCall (3, getAddress ());
}

}
