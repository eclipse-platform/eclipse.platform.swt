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

 
public class XClientMessageEvent extends XAnyEvent {
	/** @field cast=(Atom) */
	public int message_type;
	public int format;
	/** @field accessor=data.l,cast=(long *) */
	public int[] data = new int[5];
	public static final int sizeof = 48;
}
