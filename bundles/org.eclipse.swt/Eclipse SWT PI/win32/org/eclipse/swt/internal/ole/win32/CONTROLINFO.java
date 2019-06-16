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

public final class CONTROLINFO {
	public int cb;
	/** @field cast=(HACCEL) */
	public long hAccel;
	public short cAccel;
	public int dwFlags;
	public static final int sizeof = COM.CONTROLINFO_sizeof ();
}
