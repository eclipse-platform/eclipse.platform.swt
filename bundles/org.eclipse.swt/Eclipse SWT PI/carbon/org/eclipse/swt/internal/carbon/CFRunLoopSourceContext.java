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


public class CFRunLoopSourceContext {
	/** @field cast=(CFIndex) */
	public int version;
	/** @field cast=(void *) */
	public int info;
	/** @field cast=(void *) */
	public int retain;
	/** @field cast=(void *) */
	public int release;
	/** @field cast=(void *) */
	public int copyDescription;
	/** @field cast=(void *) */
	public int equal;
	/** @field cast=(void *) */
	public int hash;
	/** @field cast=(void *) */
	public int schedule;
	/** @field cast=(void *) */
	public int cancel;
	/** @field cast=(void *) */
	public int perform;
	public static final int sizeof = 40;
}
