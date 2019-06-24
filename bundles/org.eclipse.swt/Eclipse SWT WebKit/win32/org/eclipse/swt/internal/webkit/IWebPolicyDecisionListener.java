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

public class IWebPolicyDecisionListener extends IUnknown{

public IWebPolicyDecisionListener (long address) {
	super (address);
}

public int use () {
	return COM.VtblCall (3, getAddress ());
}

public int download () {
	return COM.VtblCall (4, getAddress ());
}

public int ignore () {
	return COM.VtblCall (5, getAddress ());
}

}
