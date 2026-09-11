package org.eclipse.swt.tests.doubles;

import java.util.Objects;

import io.github.humbleui.skija.Font;

public class ExtractedFontData {

	String name;
	int weight;
	int width;
	int slant;
	int ediging;
	float size;
	float scaleX;
	float skewX;

	public static ExtractedFontData getData(Font f) {

		ExtractedFontData d = new ExtractedFontData();

		var t = f.getTypeface();

		d.name = t.getFamilyName();
		var style = t.getFontStyle();

		d.weight = style.getWeight();
		d.width = style.getWidth();
		d.slant = style.getSlant().ordinal();

		d.ediging = f.getEdging().ordinal();
		d.size = f.getSize();
		d.scaleX = f.getScaleX();
		d.skewX = f.getSkewX();

		return d;
	}

	@Override
	public int hashCode() {
		return Objects.hash(ediging, name, scaleX, size, skewX, slant, weight, width);
	}

	@Override
	public String toString() {
		return "FontData [name=" + name + ", weight=" + weight + ", width=" + width + ", slant=" + slant + ", ediging="
				+ ediging + ", size=" + size + ", scaleX=" + scaleX + ", skewX=" + skewX + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExtractedFontData other = (ExtractedFontData) obj;
		return ediging == other.ediging && Objects.equals(name, other.name)
				&& Float.floatToIntBits(scaleX) == Float.floatToIntBits(other.scaleX)
				&& Float.floatToIntBits(size) == Float.floatToIntBits(other.size)
				&& Float.floatToIntBits(skewX) == Float.floatToIntBits(other.skewX) && slant == other.slant
				&& weight == other.weight && width == other.width;
	}

}
