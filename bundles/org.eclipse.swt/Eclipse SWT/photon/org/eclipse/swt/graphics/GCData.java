package org.eclipse.swt.graphics;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;

public final class GCData {
	public Device device;
	public Image image;
	public int rid;
	public int widget, topWidget;
	public int foreground = -1;
	public int background = -1;
	public byte[] font;
	public boolean xorMode;
	public int lineStyle = SWT.LINE_SOLID;
	public int lineWidth = 1;
	public int clipRectsCount;
	public int clipRects;
}
