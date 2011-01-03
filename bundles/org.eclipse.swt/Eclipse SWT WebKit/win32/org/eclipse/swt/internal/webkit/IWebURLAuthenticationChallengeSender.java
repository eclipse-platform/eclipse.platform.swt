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

public class IWebURLAuthenticationChallengeSender extends IUnknown {

public IWebURLAuthenticationChallengeSender (int /*long*/ address) {
	super (address);
}

public int cancelAuthenticationChallenge (int /*long*/ challenge) {
	return COM.VtblCall (3, getAddress (), challenge);
}

public int useCredential (int /*long*/ credential, int /*long*/ challenge) {
	return COM.VtblCall (5, getAddress (), credential, challenge);
}

}
