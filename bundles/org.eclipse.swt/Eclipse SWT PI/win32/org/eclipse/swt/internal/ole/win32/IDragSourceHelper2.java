/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.ole.win32;

public class IDragSourceHelper2 extends IDragSourceHelper {
public IDragSourceHelper2(long /*int*/ address) {
	super(address);
}
public int SetFlags(int dwFlags) {
	return COM.VtblCall(5, address, dwFlags);
}
}
