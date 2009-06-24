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

public class EventRecord {
	/** @field cast=(EventKind) */
	public short what;
	public int message;
	public int when;
	//Point where;
	/** @field accessor=where.v */
	public short where_v;
	/** @field accessor=where.h */
	public short where_h;
	/** @field cast=(EventModifiers) */
	public short modifiers;
	public static final int sizeof = 16;
}
