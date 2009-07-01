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

public class IOleLink extends IUnknown
{
public IOleLink(int /*long*/ address) {
	super(address);
}
public int BindIfRunning() {
	return COM.VtblCall(10, address);
}
public int GetSourceMoniker(int /*long*/[] ppmk) {
	return COM.VtblCall(6, address, ppmk);
}
}
