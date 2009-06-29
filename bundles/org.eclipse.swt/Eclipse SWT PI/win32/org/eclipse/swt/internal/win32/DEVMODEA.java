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

public class DEVMODEA extends DEVMODE {
	public byte[] dmDeviceName = new byte[OS.CCHDEVICENAME];
	public byte[] dmFormName = new byte[OS.CCHFORMNAME];
	public static final int sizeof = OS.DEVMODEA_sizeof ();
}
