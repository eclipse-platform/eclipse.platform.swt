/**********************************************************************
 * Copyright (c) 2003-2006 IBM Corp.
 *
 * All rights reserved.  This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 **********************************************************************/
package org.eclipse.swt.internal.cocoa;


public class objc_class {
	public int isa;
	public int super_class;
	public int name;
	public int version;
	public int info;
	public int instance_size;
	public int ivars;
	public int methodLists;
	public int cache;
	public int protocols;
	public static final int sizeof = 40;
}
