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
	public static final int BOOLEAN = 2;
	public static final int NUMBER = 3;
	public static final int STRING = 4;  // Value in returnPointer. Needs to be freed on Java side
	public static final int NULL = 5;
	public static final int ARRAY = 6;

	// Note, the following are used but are defined in SWT.java:
	// ERROR_FAILED_EVALUATE = 50;
	// ERROR_INVALID_RETURN_VALUE = 51;

	/** @field cast=(jintLong) */
	public long /*int*/ returnPointer;

	/** @field cast(double) */
	public double returnDouble;

	/** @field cast(jboolean) */
	public boolean returnBoolean;

	/** @field cast=(int) */
	public int returnType;  //Uses types defined above.

	public static final int sizeof = WebKitGTK.SWTJSreturnVal_sizeof();
}
