package org.eclipse.swt.graphics;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.*;
import org.eclipse.swt.internal.gtk.*;

class DefaultGtkStyle {
	
	private static DefaultGtkStyle instance = null;
	private GtkStyle style = null;
	private int defaultFont;

	public Color foregroundColorNORMAL() {
		return new Color(null,
			((short)0xFF00 & style.fg0_red)>>8,
			((short)0xFF00 & style.fg0_green)>>8,
			((short)0xFF00 & style.fg0_blue)>>8);
	}
	
	public Color backgroundColorNORMAL() {
		return new Color(null,
			((short)0xFF00 & style.bg0_red)>>8,
			((short)0xFF00 & style.bg0_green)>>8,
			((short)0xFF00 & style.bg0_blue)>>8);
	}
	
	public Color foregroundColorACTIVE() {
		return new Color(null,
			((short)0xFF00 & style.fg1_red)>>8,
			((short)0xFF00 & style.fg1_green)>>8,
			((short)0xFF00 & style.fg1_blue)>>8);
	}
	
	public Color backgroundColorACTIVE() {
		return new Color(null,
			((short)0xFF00 & style.bg1_red)>>8,
			((short)0xFF00 & style.bg1_green)>>8,
			((short)0xFF00 & style.bg1_blue)>>8);
	}
	
	public Color foregroundColorPRELIGHT() {
		return new Color(null,
			((short)0xFF00 & style.fg2_red)>>8,
			((short)0xFF00 & style.fg2_green)>>8,
			((short)0xFF00 & style.fg2_blue)>>8);
	}
	
	public Color backgroundColorPRELIGHT() {
		return new Color(null,
			((short)0xFF00 & style.bg2_red)>>8,
			((short)0xFF00 & style.bg2_green)>>8,
			((short)0xFF00 & style.bg2_blue)>>8);
	}
	
	public Color foregroundColorSELECTED() {
		return new Color(null,
			((short)0xFF00 & style.fg3_red)>>8,
			((short)0xFF00 & style.fg3_green)>>8,
			((short)0xFF00 & style.fg3_blue)>>8);
	}
	
	public Color backgroundColorSELECTED() {
		return new Color(null,
			((short)0xFF00 & style.bg3_red)>>8,
			((short)0xFF00 & style.bg3_green)>>8,
			((short)0xFF00 & style.bg3_blue)>>8);
	}
	
	public Color foregroundColorINSENSITIVE() {
		return new Color(null,
			((short)0xFF00 & style.fg4_red)>>8,
			((short)0xFF00 & style.fg4_green)>>8,
			((short)0xFF00 & style.fg4_blue)>>8);
	}
	
	public Color backgroundColorINSENSITIVE() {
		return new Color(null,
			((short)0xFF00 & style.bg4_red)>>8,
			((short)0xFF00 & style.bg4_green)>>8,
			((short)0xFF00 & style.bg4_blue)>>8);
	}
	
	public int loadDefaultFont() {
		if (defaultFont == 0) {
			int fnames = Font.getFontNameList(style.font);
			int slength = OS.g_slist_length(fnames);
			if (slength < 1) SWT.error(SWT.ERROR_UNSPECIFIED);
			int name1 = OS.g_slist_nth_data(fnames, 0);
			int length = OS.strlen(name1);
			byte [] buffer1 = new byte[length];
			OS.memmove(buffer1, name1, length);
			defaultFont = OS.gdk_font_load(buffer1);
			if (defaultFont==0) SWT.error(SWT.ERROR_UNSPECIFIED);
			GdkFont gdkFont = new GdkFont();
			OS.memmove(gdkFont, defaultFont, GdkFont.sizeof);
			if (gdkFont.type != OS.GDK_FONT_FONT) SWT.error(SWT.ERROR_UNSPECIFIED);
		}
		return defaultFont;
	}
	
	public static DefaultGtkStyle instance() {
		if (instance==null) instance = new DefaultGtkStyle();
		return instance;
	}
	
	private DefaultGtkStyle() {
		style = new GtkStyle();
		OS.memmove(style, OS.gtk_widget_get_default_style(), GtkStyle.sizeof);
	}
	
}

