/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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

public class IOleLink extends IUnknown
{
public IOleLink(long address) {
	super(address);
}
public int BindIfRunning() {
	return COM.VtblCall(10, address);
}
public int GetSourceMoniker(long[] ppmk) {
	return COM.VtblCall(6, address, ppmk);
}
}
