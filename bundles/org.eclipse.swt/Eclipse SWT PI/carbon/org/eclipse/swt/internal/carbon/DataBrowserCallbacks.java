/**********************************************************************
 * Copyright (c) 2003-2004 IBM Corp.
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
	public int version;
	public int v1_itemDataCallback;
	public int v1_itemCompareCallback;
	public int v1_itemNotificationCallback;
	public int v1_addDragItemCallback;
	public int v1_acceptDragCallback;
	public int v1_receiveDragCallback;
	public int v1_postProcessDragCallback;
	public int v1_itemHelpContentCallback;
	public int v1_getContextualMenuCallback;
	public int v1_selectContextualMenuCallback;
	public static final int sizeof = 44;
}
