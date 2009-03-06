/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

/**
 * @jniclass flags=struct
 */
public class objc_super {
	/** @field cast=(id) */
	public int /*long*/ receiver;
	/** @field accessor=swt_super_class,cast=(Class) */
	public int /*long*/ super_class;
	public static final int sizeof = OS.objc_super_sizeof();	
}
