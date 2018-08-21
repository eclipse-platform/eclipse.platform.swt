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


public class CRYPT_ALGORITHM_IDENTIFIER {
	/** @field cast=(LPSTR) */
	public long /*int*/ pszObjId;
	public CRYPT_OBJID_BLOB Parameters = new CRYPT_OBJID_BLOB ();

	static final public int sizeof = OS.CRYPT_ALGORITHM_IDENTIFIER_sizeof ();
}
