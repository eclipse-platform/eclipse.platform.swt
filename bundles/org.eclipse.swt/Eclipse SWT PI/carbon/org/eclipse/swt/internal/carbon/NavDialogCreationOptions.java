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


public class NavDialogCreationOptions {
	public short version;
	public int optionFlags;
//	Point location;
	public short location_h;
	public short location_v;
	public int clientName;
	public int windowTitle;
	public int actionButtonLabel;
	public int cancelButtonLabel;
	public int saveFileName;
	public int message;
	public int preferenceKey;
	public int popupExtension;
	public int modality;
	public int parentWindow;
//	char reserved[16];
	public static final int sizeof = 66;
}
