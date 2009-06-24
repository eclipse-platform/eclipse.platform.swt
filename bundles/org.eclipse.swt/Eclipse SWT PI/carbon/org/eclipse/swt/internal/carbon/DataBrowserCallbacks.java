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

 
public class DataBrowserCallbacks {
	/** @field cast=(UInt32) */
	public int version;
	/** @field accessor=u.v1.itemDataCallback,cast=(DataBrowserItemDataUPP) */
	public int v1_itemDataCallback;
	/** @field accessor=u.v1.itemCompareCallback,cast=(DataBrowserItemCompareUPP) */
	public int v1_itemCompareCallback;
	/** @field accessor=u.v1.itemNotificationCallback,cast=(DataBrowserItemNotificationUPP) */
	public int v1_itemNotificationCallback;
	/** @field accessor=u.v1.addDragItemCallback,cast=(DataBrowserAddDragItemUPP) */
	public int v1_addDragItemCallback;
	/** @field accessor=u.v1.acceptDragCallback,cast=(DataBrowserAcceptDragUPP) */
	public int v1_acceptDragCallback;
	/** @field accessor=u.v1.receiveDragCallback,cast=(DataBrowserReceiveDragUPP) */
	public int v1_receiveDragCallback;
	/** @field accessor=u.v1.postProcessDragCallback,cast=(DataBrowserPostProcessDragUPP) */
	public int v1_postProcessDragCallback;
	/** @field accessor=u.v1.itemHelpContentCallback,cast=(DataBrowserItemHelpContentUPP) */
	public int v1_itemHelpContentCallback;
	/** @field accessor=u.v1.getContextualMenuCallback,cast=(DataBrowserGetContextualMenuUPP) */
	public int v1_getContextualMenuCallback;
	/** @field accessor=u.v1.selectContextualMenuCallback,cast=(DataBrowserSelectContextualMenuUPP) */
	public int v1_selectContextualMenuCallback;
	public static final int sizeof = 44;
}
