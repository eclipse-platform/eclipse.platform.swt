package org.eclipse.swt.internal.canvasext;

import org.eclipse.swt.graphics.*;

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

	public static FontProperties getFontProperties(Font font) {
		return null;

	}
}
