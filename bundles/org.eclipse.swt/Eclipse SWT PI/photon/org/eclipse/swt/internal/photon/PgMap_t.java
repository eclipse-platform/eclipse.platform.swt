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
package org.eclipse.swt.internal.photon;


public class PgMap_t {
	/** @field accessor=dim.w */
	public short dim_w;
	/** @field accessor=dim.h */
	public short dim_h;
	public short bpl;
	public short bpp;
	/** @field cast=(char *) */
	public int map;
	public static final int sizeof = 12;
}
