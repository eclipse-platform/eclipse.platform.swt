/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc. All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 *
 * Contributors:
 *     Red Hat, Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.webkit;

/**
 * Custom SWT Struct for passing return values from Javascript back to java.
 * Java: SWTJSReturnVal.java (class)
 * C:    SWTJSReturnVal (struct)
 * NOTE: If you need to modify fields, modify both the C part which is declared in webkitgtk.h and java part int SWTJSreturnVal.java
 * webkigtgtk_struct.c/h should be updated by auto-tools to add setters/getters.
 */
public class SWTJSreturnVal {
	/**
	 * Fields used to determine type of return value. These are like enums.
	 * Note. SWT Tools doesn't auto generate setters/getters for static fields.
	 */
	public static final int VALUE = 1;    // as in return type is a value
	// Note, the following two types are used but are defined in SWT.java:
	// ERROR_FAILED_EVALUATE = 50;
	// ERROR_INVALID_RETURN_VALUE = 51;
	/** @field cast=(int) */
	public int returnType;  //Uses types defined above.

	/** @field cast=(jintLong) */
	public long /*int*/ errorMsg;

	/** @field cast=(jintLong) */
	public long /*int*/ jsResultPointer;

	/** @field cast=(jintLong) */
	public long /*int*/ context;

	/** @field cast=(jintLong) */
	public long /*int*/ value;

	public static final int sizeof = WebKitGTK.SWTJSreturnVal_sizeof();
}
