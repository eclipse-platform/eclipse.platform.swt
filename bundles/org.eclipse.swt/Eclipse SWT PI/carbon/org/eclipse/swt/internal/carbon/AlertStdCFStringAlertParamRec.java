/**********************************************************************
 * Copyright (c) 2003, 2008 IBM Corp.
 * Portions Copyright (c) 1983-2002, Apple Computer, Inc.
 *
 * All rights reserved.  This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 **********************************************************************/
package org.eclipse.swt.internal.carbon;

 
public class AlertStdCFStringAlertParamRec {
	public int version;
	public boolean movable;
	public boolean helpButton;
	/** @field cast=(CFStringRef) */
	public int defaultText;
	/** @field cast=(CFStringRef) */
	public int cancelText;
	/** @field cast=(CFStringRef) */
	public int otherText;
	public short defaultButton;
	public short cancelButton;
	public short position;
	public int flags;
	
	public static final int sizeof = 28;
}
