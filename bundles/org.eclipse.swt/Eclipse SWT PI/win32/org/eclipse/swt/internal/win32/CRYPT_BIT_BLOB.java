/*******************************************************************************
 * Copyright (c) 2010, 2012 IBM Corporation and others.
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
package org.eclipse.swt.internal.win32;


public class CRYPT_BIT_BLOB {
	public int cbData;
	/** @field cast=(BYTE *) */
	public long /*int*/ pbData;
	public int cUnusedBits;

	static final public int sizeof = OS.CRYPT_BIT_BLOB_sizeof ();
}
