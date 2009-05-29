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

 
public class DataBrowserAccessibilityItemInfo {
	/** @field cast=(UInt32) */
	public int version;
	/** @field accessor=u.v0.container,cast=(DataBrowserItemID) */
	public int v0_container;
	/** @field accessor=u.v0.item,cast=(DataBrowserItemID) */
	public int v0_item;
	/** @field accessor=u.v0.columnProperty,cast=(DataBrowserPropertyID) */
	public int v0_columnProperty;
	/** @field accessor=u.v0.propertyPart,cast=(DataBrowserPropertyPart) */
	public int v0_propertyPart;
	
	public static final int sizeof = 20;
}
