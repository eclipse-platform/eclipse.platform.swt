/*******************************************************************************
 * Copyright (c) 2010, 2011 IBM Corporation and others.
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


public class CERT_PUBLIC_KEY_INFO {
	 public CRYPT_ALGORITHM_IDENTIFIER Algorithm = new CRYPT_ALGORITHM_IDENTIFIER ();
	 public CRYPT_BIT_BLOB PublicKey = new CRYPT_BIT_BLOB ();

	 static final public int sizeof = OS.CERT_PUBLIC_KEY_INFO_sizeof ();
}
