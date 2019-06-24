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

public class IWebDataSource extends IUnknown {

public IWebDataSource (long address) {
	super (address);
}

public int representation (long[] rep) {
	return COM.VtblCall (5, getAddress (), rep);
}

public int webFrame (long[] frame) {
	return COM.VtblCall (6, getAddress (), frame);
}

public int request (long[] request) {
	return COM.VtblCall (8, getAddress (), request);
}

public int pageTitle (long[] title) {
	return COM.VtblCall (12, getAddress (), title);
}

}
