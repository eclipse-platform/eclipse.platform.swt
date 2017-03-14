/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc. All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 *******************************************************************************/
package org.eclipse.swt.internal.webkit;


public class GdkRectangle {
	/** @field cast=(gint) */
	public int x;
	/** @field cast=(gint) */
	public int y;
	/** @field cast=(gint) */
	public int width;
	/** @field cast=(gint) */
	public int height;
	public static final int sizeof = WebKitGTK.GdkRectangle_sizeof();
}
