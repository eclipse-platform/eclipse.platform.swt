/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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

public class EMREXTCREATEFONTINDIRECTW {
	public EMR emr = new EMR();
	public int ihFont;
	public EXTLOGFONTW elfw = new EXTLOGFONTW();
	public static final int sizeof = OS.EMREXTCREATEFONTINDIRECTW_sizeof ();
}
