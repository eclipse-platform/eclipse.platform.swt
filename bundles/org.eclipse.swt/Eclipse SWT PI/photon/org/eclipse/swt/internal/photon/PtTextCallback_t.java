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


public class PtTextCallback_t {
	public int start_pos;
	public int end_pos;
	public int cur_insert;
	public int new_insert;
	public int length;
	public short reserved;
	/** @field cast=(char *) */
	public int text;
	public int doit;
	public static final int sizeof = 32;
}
