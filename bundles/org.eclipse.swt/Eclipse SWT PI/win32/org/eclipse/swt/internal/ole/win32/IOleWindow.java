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

public class IOleWindow extends IUnknown {
public IOleWindow(int /*long*/ address) {
	super(address);
}
public int GetWindow(int /*long*/[] phwnd) {
	return COM.VtblCall(3, address, phwnd);
}
}
