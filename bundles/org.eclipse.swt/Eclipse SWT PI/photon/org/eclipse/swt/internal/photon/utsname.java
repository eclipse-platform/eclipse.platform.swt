/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.photon;

public class utsname {
	public byte[] sysname = new byte[_SYSNAME_SIZE];
	public byte[] nodename = new byte[_SYSNAME_SIZE];
	public byte[] release = new byte[_SYSNAME_SIZE];
	public byte[] version = new byte[_SYSNAME_SIZE];
	public byte[] machine = new byte[_SYSNAME_SIZE];
	public static final int _SYSNAME_SIZE = 256 + 1;
	public static final int sizeof = _SYSNAME_SIZE * 5;
}
