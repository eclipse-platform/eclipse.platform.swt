/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.ole.win32;

public final class CONTROLINFO {
	public int cb;
	/** @field cast=(HACCEL) */
	public long /*int*/ hAccel;
	public short cAccel;
	public int dwFlags;
	public static final int sizeof = COM.CONTROLINFO_sizeof ();
}
