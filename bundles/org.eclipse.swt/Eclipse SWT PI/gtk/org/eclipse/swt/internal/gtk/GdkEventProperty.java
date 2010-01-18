/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others. All rights reserved.
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

public class GdkEventProperty extends GdkEvent {
	/** @field cast=(GdkWindow *) */
	public int /*long*/ window;
	/** @field cast=(gint8) */
	public byte send_event;
	/** @field cast=(GdkAtom) */
	public int /*long*/ atom;
	/** @field cast=(guint32) */
	public int time;
	/** @field cast=(guint) */
	public int state;
	public static final int sizeof = OS.GdkEventProperty_sizeof();
}
