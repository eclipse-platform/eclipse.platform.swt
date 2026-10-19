/*******************************************************************************
 * Copyright (c) 2010, 2011 IBM Corporation and others.
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
package org.eclipse.swt.internal.win32;

public class GESTURECONFIG {
	public int dwID;                     // gesture ID
	public int dwWant;                   // settings related to gesture ID that are to be turned on
	public int dwBlock;                  // settings related to gesture ID that are to be turned off
	public static final int sizeof = OS.GESTURECONFIG_sizeof ();
}
