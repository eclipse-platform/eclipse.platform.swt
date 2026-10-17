/*******************************************************************************
 * Copyright (c) 2009, 2012 IBM Corporation and others.
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
package org.eclipse.swt.internal.ole.win32;

public class IServiceProvider extends IUnknown
{
public IServiceProvider(long address) {
	super(address);
}
public int QueryService(GUID iid1, GUID iid2, long ppvObject[]) {
	return COM.VtblCall(3, address, iid1, iid2, ppvObject);
}
}
