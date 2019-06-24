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

public class IPersistStreamInit extends IPersist
{
public IPersistStreamInit(long address) {
	super(address);
}

public int Load(long pStm) {
	return COM.VtblCall(5, address, pStm);
}

public int InitNew() {
	return COM.VtblCall(8, address);
}
}
