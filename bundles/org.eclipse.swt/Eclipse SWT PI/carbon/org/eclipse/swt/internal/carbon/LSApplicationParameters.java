/**********************************************************************
 * Copyright (c) 2003-2006 IBM Corp.
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

 
public class LSApplicationParameters {
	public int version;
	public int flags;
	public int application;
	public int asyncLaunchRefCon;
	public int environment;
	public int argv;
	public int initialEvent;
	public static final int sizeof = 28;
}
