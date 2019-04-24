/*******************************************************************************
 * Copyright (c) 2007, 2012 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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
	public long receiver;
	/** @field accessor=swt_super_class,cast=(Class) */
	public long super_class;
	public static final int sizeof = OS.objc_super_sizeof();
}
