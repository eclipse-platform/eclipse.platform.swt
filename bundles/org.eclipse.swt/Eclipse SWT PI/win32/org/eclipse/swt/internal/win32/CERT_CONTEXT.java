/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.win32;


public class CERT_CONTEXT {
	public int dwCertEncodingType;
	/** @field cast=(BYTE *) */
	public int /*long*/ pbCertEncoded;
	public int cbCertEncoded;
	/** @field cast=(PCERT_INFO) */
	public int /*long*/ pCertInfo;
	/** @field cast=(HCERTSTORE) */
	public int /*long*/ hCertStore;

	public static final int sizeof = OS.CERT_CONTEXT_sizeof ();
}
