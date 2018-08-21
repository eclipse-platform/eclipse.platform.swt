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

import org.eclipse.swt.internal.win32.*;

public class IPersistStreamInit extends IPersist
{
public IPersistStreamInit(long /*int*/ address) {
	super(address);
}

public int Load(long /*int*/ pStm) {
	return OS.VtblCall(5, address, pStm);
}

public int InitNew() {
	return OS.VtblCall(8, address);
}
}
