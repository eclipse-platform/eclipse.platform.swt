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

public class SCRIPT_CONTROL {
	public int uDefaultLanguage; 
	public boolean fContextDigits; 
	public boolean fInvertPreBoundDir; 
	public boolean fInvertPostBoundDir; 
	public boolean fLinkStringBefore; 
	public boolean fLinkStringAfter;
	public boolean fNeutralOverride;
	public boolean fNumericOverride;
	public boolean fLegacyBidiClass;
	public int fReserved;
	public static final int sizeof = OS.SCRIPT_CONTROL_sizeof ();
}
