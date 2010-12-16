/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.webkit;

import org.eclipse.swt.internal.ole.win32.*;

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
