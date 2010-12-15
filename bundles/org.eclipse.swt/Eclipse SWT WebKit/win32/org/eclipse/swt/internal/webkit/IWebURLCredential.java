package org.eclipse.swt.internal.webkit;

import org.eclipse.swt.internal.ole.win32.COM;
import org.eclipse.swt.internal.ole.win32.IUnknown;

public class IWebURLCredential extends IUnknown {

public IWebURLCredential(int /*long*/ address) {
	super(address);
}

public int hasPassword (int /*long*/[] result) {
	return COM.VtblCall (3, getAddress (), result);
}

public int initWithUser (int /*long*/ user, int /*long*/ password, int /*long*/ persistence) {
	return COM.VtblCall (4, getAddress (), user, password, persistence);
}

public int password (int /*long*/[] password) {
	return COM.VtblCall (5, getAddress (), password);
}

public int user (int /*long*/[] result) {
	return COM.VtblCall (7, getAddress (), result);
}

}
