/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

