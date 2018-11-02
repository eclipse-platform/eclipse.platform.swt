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

public class NONCLIENTMETRICSA extends NONCLIENTMETRICS {
	public LOGFONTA lfCaptionFont = new LOGFONTA ();
	public LOGFONTA lfSmCaptionFont = new LOGFONTA ();
	public LOGFONTA lfMenuFont = new LOGFONTA ();
	public LOGFONTA lfStatusFont = new LOGFONTA ();
	public LOGFONTA lfMessageFont = new LOGFONTA ();
	public static final int sizeof = OS.NONCLIENTMETRICSA_sizeof ();
}

