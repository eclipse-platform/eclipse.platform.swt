/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.ole.win32;

public class IPersistStreamInit extends IPersist
{
public IPersistStreamInit(long /*int*/ address) {
	super(address);
}

public int Load(long /*int*/ pStm) {
	return COM.VtblCall(5, address, pStm);
}

public int InitNew() {
	return COM.VtblCall(8, address);
}
}
