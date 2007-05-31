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

public class SCRIPT_ANALYSIS {
	public short eScript;
	public boolean fRTL;
	public boolean fLayoutRTL;
	public boolean fLinkBefore; 
	public boolean fLinkAfter;
	public boolean fLogicalOrder; 
	public boolean fNoGlyphIndex;
	public SCRIPT_STATE s = new SCRIPT_STATE(); 
	public static final int sizeof = OS.SCRIPT_ANALYSIS_sizeof ();
}
