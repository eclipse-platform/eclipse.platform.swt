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
package org.eclipse.swt.internal.motif;

 
public class XmTextVerifyCallbackStruct extends XmAnyCallbackStruct {
	public byte doit;
	public int currInsert;
	public int newInsert;
	public int startPos;
	public int endPos;
	/** @field cast=(XmTextBlock) */
	public int text;
	public static final int sizeof = 32;
}
