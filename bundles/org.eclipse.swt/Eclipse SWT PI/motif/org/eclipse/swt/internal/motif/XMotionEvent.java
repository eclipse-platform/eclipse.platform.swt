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
package org.eclipse.swt.internal.motif;

 
public class XMotionEvent extends XInputEvent {
	public int is_hint;		/* detail */
	public int same_screen;	/* same screen flag */
	public int pad0, pad1, pad2, pad3, pad4, pad5, pad6, pad7, pad8, pad9;
}
