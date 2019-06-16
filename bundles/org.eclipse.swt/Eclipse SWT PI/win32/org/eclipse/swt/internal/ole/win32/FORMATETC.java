/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
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
package org.eclipse.swt.internal.ole.win32;

public final class FORMATETC {
	/** @field cast=(CLIPFORMAT) */
	public int cfFormat;
	/** @field cast=(DVTARGETDEVICE *) */
	public long ptd;
	public int dwAspect;
	public int lindex;
	public int tymed;
	public static final int sizeof = COM.FORMATETC_sizeof ();
}
