/*******************************************************************************
 * Copyright (c) 2019 Red Hat Inc. and others. All rights reserved.
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
 *     Red Hat Inc. - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.gtk;

/**
 * This class contains Graphene specific native functions. Please note this library is only relevant
 * for GTK4 and above.
 *
 * In contrast to OS.java, dynamic functions are automatically linked, no need to add os_custom.h entries.
 */
public class Graphene extends OS {
	/* [GTK4 only, if-def'd in os.h] */

	public static final native long graphene_rect_alloc();
	/**
	 * @param rectangle cast=(graphene_rect_t *)
	 */
	/* [GTK4 only, if-def'd in os.h] */
	public static final native void graphene_rect_free(long rectangle);
	/**
	 * @param rectangle cast=(graphene_rect_t *)
	 */
	/* [GTK4 only, if-def'd in os.h] */
	public static final native long graphene_rect_init(long rectangle, float x, float y, float width, float height);
}
