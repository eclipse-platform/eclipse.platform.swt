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


public class LongDateRec {
	/** @field accessor=ld.era */
	public short era;
	/** @field accessor=ld.year */
	public short year;
	/** @field accessor=ld.month */
	public short month;
	/** @field accessor=ld.day */
	public short day;
	/** @field accessor=ld.hour */
	public short hour;
	/** @field accessor=ld.minute */
	public short minute;
	/** @field accessor=ld.second */
	public short second;
	/** @field accessor=ld.dayOfWeek */
	public short dayOfWeek;
	/** @field accessor=ld.dayOfYear */
	public short dayOfYear;
	/** @field accessor=ld.weekOfYear */
	public short weekOfYear;
	/** @field accessor=ld.pm */
	public short pm;
	/** @field accessor=ld.res1 */
	public short res1;
	/** @field accessor=ld.res2 */
	public short res2;
	/** @field accessor=ld.res3 */
	public short res3;
	public static final int sizeof = 28;
}
