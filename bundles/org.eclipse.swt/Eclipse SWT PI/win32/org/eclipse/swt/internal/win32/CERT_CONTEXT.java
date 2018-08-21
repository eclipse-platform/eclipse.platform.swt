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


public class CERT_CONTEXT {
	public int dwCertEncodingType;
	/** @field cast=(BYTE *) */
	public long /*int*/ pbCertEncoded;
	public int cbCertEncoded;
	/** @field cast=(PCERT_INFO) */
	public long /*int*/ pCertInfo;
	/** @field cast=(HCERTSTORE) */
	public long /*int*/ hCertStore;

	public static final int sizeof = OS.CERT_CONTEXT_sizeof ();
}
