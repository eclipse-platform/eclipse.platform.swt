/**********************************************************************
 * Copyright (c) 2003, 2007 IBM Corp.
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

 
public class HILayoutInfo {
	public int version;
	public HIBinding binding = new HIBinding();
	public HIScaling scale = new HIScaling();
	public HIPositioning position = new HIPositioning();	
	public static final int sizeof = 84;
}
