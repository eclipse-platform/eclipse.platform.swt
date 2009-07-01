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


public class NavDialogCreationOptions {
	public short version;
	/** @field cast=(NavDialogOptionFlags) */
	public int optionFlags;
//	Point location;
	/** @field accessor=location.h */
	public short location_h;
	/** @field accessor=location.v */
	public short location_v;
	/** @field cast=(CFStringRef) */
	public int clientName;
	/** @field cast=(CFStringRef) */
	public int windowTitle;
	/** @field cast=(CFStringRef) */
	public int actionButtonLabel;
	/** @field cast=(CFStringRef) */
	public int cancelButtonLabel;
	/** @field cast=(CFStringRef) */
	public int saveFileName;
	/** @field cast=(CFStringRef) */
	public int message;
	public int preferenceKey;
	/** @field cast=(CFArrayRef) */
	public int popupExtension;
	/** @field cast=(WindowModality) */
	public int modality;
	/** @field cast=(WindowRef) */
	public int parentWindow;
//	char reserved[16];
	public static final int sizeof = 66;
}
