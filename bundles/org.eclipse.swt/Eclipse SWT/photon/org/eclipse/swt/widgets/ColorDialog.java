package org.eclipse.swt.widgets;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2001  All Rights Reserved
 */

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public /*final*/ class ColorDialog extends Dialog {
	RGB rgb;

public ColorDialog (Shell parent) {
	this (parent, SWT.PRIMARY_MODAL);
}

public ColorDialog (Shell parent, int style) {
	super (parent, style);
}

public RGB getRGB () {
	return rgb;
}

public RGB open () {
	int parentHandle = 0;
	if (parent != null) parentHandle = parent.shellHandle;
	byte[] title = null;
	if (this.title != null) title = Converter.wcsToMbcs (null, this.title, true);
	PtColorSelectInfo_t info = new PtColorSelectInfo_t();
	info.flags = OS.Pt_COLORSELECT_MODAL;
	if (rgb != null) info.rgb = (rgb. blue & 0xFF) | ((rgb.green & 0xFF) << 8) | ((rgb.red & 0xFF) << 16);
	rgb = null;

	OS.PtColorSelect(parentHandle, title, info);
	
	if ((info.flags & OS.Pt_COLORSELECT_ACCEPT) != 0) {
		int color = info.rgb;
		rgb = new RGB ((color & 0xFF0000) >> 16, (color & 0xFF00) >> 8, color & 0xFF);
	}	
	return rgb;
}

public void setRGB (RGB rgb) {
	this.rgb = rgb;
}

}