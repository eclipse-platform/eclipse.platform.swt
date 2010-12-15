package org.eclipse.swt.internal.webkit;

import org.eclipse.swt.internal.ole.win32.COM;
import org.eclipse.swt.internal.ole.win32.IUnknown;

public class IWebURLAuthenticationChallengeSender extends IUnknown {

public IWebURLAuthenticationChallengeSender(int /*long*/ address) {
	super(address);
}

public int cancelAuthenticationChallenge (int /*long*/ challenge) {
	return COM.VtblCall (3, getAddress (), challenge);
}

public int useCredential (int /*long*/ credential, int /*long*/ challenge) {
	return COM.VtblCall (5, getAddress (), credential, challenge);
}

}
