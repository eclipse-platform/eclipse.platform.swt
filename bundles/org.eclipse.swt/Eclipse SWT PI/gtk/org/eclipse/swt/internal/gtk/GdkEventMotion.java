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


public class GdkEventMotion extends GdkEvent {
	/** @field cast=(GdkWindow *) */
	public int /*long*/ window;
	/** @field cast=(gint8) */
	public byte send_event;
	/** @field cast=(guint32) */
	public int time;
	/** @field cast=(gdouble) */
	public double x;
	/** @field cast=(gdouble) */
	public double y;
	/** @field cast=(gdouble *) */
	public int /*long*/ axes;
	/** @field cast=(guint) */
	public int state;
	/** @field cast=(gint16) */
	public short is_hint;
	/** @field cast=(GdkDevice *) */
	public int /*long*/ device;
	/** @field cast=(gdouble) */
	public double x_root;
	/** @field cast=(gdouble) */
	public double y_root;
	public static final int sizeof = OS.GdkEventMotion_sizeof();
}
