package org.eclipse.swt.internal.carbon;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

public class NavDialogCreationOptions {
	public short version;
	public int optionFlags;
//  Point location;
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
//  char reserved[16];
	public static final int sizeof = 66;
}
