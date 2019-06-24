/*******************************************************************************
 * Copyright (c) 2010, 2017 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.webkit;


import org.eclipse.swt.internal.ole.win32.*;

public class IWebURLAuthenticationChallenge extends IUnknown {

public IWebURLAuthenticationChallenge (long address) {
	super (address);
}

public int previousFailureCount (int[] result) {
	return COM.VtblCall (7, getAddress (), result);
}

public int proposedCredential (long[] result) {
	return COM.VtblCall (8, getAddress (), result);
}

public int protectionSpace (long[] result) {
	return COM.VtblCall (9, getAddress (), result);
}

public int sender (long[] sender) {
	return COM.VtblCall (10, getAddress (), sender);
}

}
