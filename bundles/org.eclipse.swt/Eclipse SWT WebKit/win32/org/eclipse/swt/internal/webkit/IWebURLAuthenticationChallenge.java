package org.eclipse.swt.internal.webkit;

import org.eclipse.swt.internal.ole.win32.COM;
import org.eclipse.swt.internal.ole.win32.IUnknown;

public class IWebURLAuthenticationChallenge extends IUnknown {

public IWebURLAuthenticationChallenge(int /*long*/ address) {
	super(address);
}

public int previousFailureCount (int /*long*/[] result) {
	return COM.VtblCall (7, getAddress (), result);
}

public int proposedCredential (int /*long*/[] result) {
	return COM.VtblCall (8, getAddress (), result);
}

public int protectionSpace (int /*long*/[] result) {
	return COM.VtblCall (9, getAddress (), result);
}

public int sender (int /*long*/[] sender) {
	return COM.VtblCall (10, getAddress (), sender);
}

}
