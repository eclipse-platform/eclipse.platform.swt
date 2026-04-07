package org.eclipse.swt.internal.canvasext;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.gtk.*;

public class FontProperties {

	public int lfHeight;
	public int lfWidth = -1;
	public int lfEscapement;
	public int lfOrientation;
	public int lfWeight = -1;
	public byte lfItalic;
	public byte lfUnderline;
	public byte lfStrikeOut;
	public String name;

	private FontProperties() {

	}

	@Override
	public int hashCode() {
		return Objects.hash(lfEscapement, lfHeight, lfItalic, lfOrientation, lfStrikeOut, lfUnderline, lfWeight,
				lfWidth, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FontProperties other = (FontProperties) obj;
		return lfEscapement == other.lfEscapement && lfHeight == other.lfHeight && lfItalic == other.lfItalic
				&& lfOrientation == other.lfOrientation && lfStrikeOut == other.lfStrikeOut
				&& lfUnderline == other.lfUnderline && lfWeight == other.lfWeight && lfWidth == other.lfWidth
				&& Objects.equals(name, other.name);
	}

	public static FontProperties getFontProperties(Font font) {
		var fd = font.getFontData()[0];

//		float height = (float)OS.pango_font_description_get_size(font.handle) / OS.PANGO_SCALE;
		var stretch = OS.pango_font_description_get_stretch(font.handle);
//		var variant = OS.pango_font_description_get_variant(font.handle);
//		var style = OS.pango_font_description_get_style(font.handle);
		var weight = OS.pango_font_description_get_weight(font.handle);

		var fp = new FontProperties();

		fp.name = fd.getName();
		fp.lfHeight = fd.getHeight();

		if((fd.getStyle() & SWT.ITALIC) != 0 )
			fp.lfItalic = 1;
		fp.lfWeight = weight;

		fp.lfWidth = stretch + 1;



		return fp;

	}



}
