/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.carbon;

 
public class AlertStdCFStringAlertParamRec {
	public int version;
	public boolean movable;
	public boolean helpButton;
	public int defaultText;
	public int cancelText;
	public int otherText;
	public short defaultButton;
	public short cancelButton;
	public short position;
	public int flags;
	
	public static final int sizeof = 28;
}
