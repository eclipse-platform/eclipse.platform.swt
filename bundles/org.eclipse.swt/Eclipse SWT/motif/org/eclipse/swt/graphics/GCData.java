package org.eclipse.swt.graphics;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2000  All Rights Reserved
 */

import org.eclipse.swt.*;

public final class GCData {
	public Device device;
	public Image image;
	public int display;
	public int drawable;
	public int foreground = -1;
	public int background = -1;
	public int fontList;
	public int colormap;
	public int clipRgn;
	public int lineStyle = SWT.LINE_SOLID;
	public int renderTable;
}