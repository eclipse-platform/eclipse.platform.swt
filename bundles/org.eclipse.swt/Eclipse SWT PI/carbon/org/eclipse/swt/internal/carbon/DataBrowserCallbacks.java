package org.eclipse.swt.internal.carbon;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
 
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
