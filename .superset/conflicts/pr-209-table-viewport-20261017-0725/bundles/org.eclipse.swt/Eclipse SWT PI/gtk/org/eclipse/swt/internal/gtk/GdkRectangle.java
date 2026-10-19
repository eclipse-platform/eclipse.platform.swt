/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others. All rights reserved.
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
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.gtk;

import org.eclipse.swt.graphics.Rectangle;

public class GdkRectangle {
	/** @field cast=(gint) */
	public int x;
	/** @field cast=(gint) */
	public int y;
	/** @field cast=(gint) */
	public int width;
	/** @field cast=(gint) */
	public int height;
	public static final int sizeof = GDK.GdkRectangle_sizeof();

	/**
	 * Returns a string containing a concise, human-readable
	 * description of the receiver.
	 *
	 * @return a string representation of the <code>GdkRectangle</code>
	 */
	@Override
	public String toString() {
		return "GdkRectangle {" + x + ", " + y + ", " + width + ", " + height + "}"; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	}

	public Rectangle toRectangle() {
		return new Rectangle(x, y, width, height);
	}
}
