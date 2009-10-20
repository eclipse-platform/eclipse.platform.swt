/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.win32;

public class DEVMODEW extends DEVMODE {
	public char[] dmDeviceName = new char[OS.CCHDEVICENAME];
	public char[] dmFormName = new char[OS.CCHFORMNAME];
	public static final int sizeof = OS.DEVMODEW_sizeof ();
}
