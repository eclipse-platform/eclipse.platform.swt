/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
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

public class SCRIPT_STATE {
	public short uBidiLevel;
	public boolean fOverrideDirection;
	public boolean fInhibitSymSwap;
	public boolean fCharShape;
	public boolean fDigitSubstitute;
	public boolean fInhibitLigate;
	public boolean fDisplayZWG;
	public boolean fArabicNumContext;
	public boolean fGcpClusters;
	public boolean fReserved;
	public short fEngineReserved;
	public static final int sizeof = OS.SCRIPT_STATE_sizeof ();
}
