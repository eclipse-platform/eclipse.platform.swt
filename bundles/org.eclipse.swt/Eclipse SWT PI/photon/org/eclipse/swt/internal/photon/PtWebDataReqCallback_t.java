/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.photon;

public class PtWebDataReqCallback_t {
	public int type;
	public int length;
	public byte[] url = new byte[OS.MAX_URL_LENGTH];
	public static final int sizeof = OS.MAX_URL_LENGTH + 8;
}
