package org.eclipse.swt.graphics;

/*
 * (c) Copyright IBM Corp. 2000, 2001, 2002.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.gtk.*;
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
	public int drawable;
	public GdkColor foreground;
	public GdkColor background;
	public int font;
	public int context;
	public int layout;
	public int clipRgn;
	public int lineStyle = SWT.LINE_SOLID;
}
