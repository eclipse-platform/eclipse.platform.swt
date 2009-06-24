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

 
public class DataBrowserCustomCallbacks {
	public int version;
	/** @field accessor=u.v1.drawItemCallback,cast=(DataBrowserDrawItemUPP) */
	public int v1_drawItemCallback;
	/** @field accessor=u.v1.editTextCallback,cast=(DataBrowserEditItemUPP) */
	public int v1_editTextCallback;
	/** @field accessor=u.v1.hitTestCallback,cast=(DataBrowserHitTestUPP) */
	public int v1_hitTestCallback;
	/** @field accessor=u.v1.trackingCallback,cast=(DataBrowserTrackingUPP) */
	public int v1_trackingCallback;
	/** @field accessor=u.v1.dragRegionCallback,cast=(DataBrowserItemDragRgnUPP) */
	public int v1_dragRegionCallback;
	/** @field accessor=u.v1.acceptDragCallback,cast=(DataBrowserItemAcceptDragUPP) */
	public int v1_acceptDragCallback;
	/** @field accessor=u.v1.receiveDragCallback,cast=(DataBrowserItemReceiveDragUPP) */
	public int v1_receiveDragCallback;
	public static final int sizeof = 32;
}
