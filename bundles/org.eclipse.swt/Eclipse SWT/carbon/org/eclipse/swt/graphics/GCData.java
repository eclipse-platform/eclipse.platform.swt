package org.eclipse.swt.graphics;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.*;

/**
 * Instances of this class are descriptions of GCs in terms
 * of unallocated platform-specific data fields.
 * <p>
 * <b>IMPORTANT:</b> This class is <em>not</em> part of the public
 * API for SWT. It is marked public only so that it can be shared
 * within the packages provided by SWT. It is not available on all
 * platforms, and should never be called from application code.
 * </p>
 *
 * @private
 */
public final class GCData {
	public Device device;
	public Image image;
	public int foreground = -1;
	public int background = -1;
	public MacFont font;
	public int clipRgn;
	public int lineStyle = SWT.LINE_SOLID;
	
	// AW
	public int controlHandle;
	public boolean clipAgainstChildren= true;
	public int[] savePort= new int[1];
	public int[] saveGWorld= new int[1];
	public int saveClip;
	public boolean isFocused= false;
	public int lineWidth= 1;
	public boolean xorMode= false;
	public int damageRgn;
	public boolean pendingClip;
	public int[] context= new int[1];
	public boolean isCGContextCreated;
	// AW
}
