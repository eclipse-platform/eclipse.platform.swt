package org.eclipse.swt.graphics;

/*
 * (c) Copyright IBM Corp. 2000, 2001, 2002.
 * All Rights Reserved
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
	public int display;
	public int drawable;
	public int foreground = -1;
	public int background = -1;
	public int fontList;
	public String codePage;
	public int colormap;
	public int clipRgn;
	public int lineStyle = SWT.LINE_SOLID;
	public int renderTable;
}
