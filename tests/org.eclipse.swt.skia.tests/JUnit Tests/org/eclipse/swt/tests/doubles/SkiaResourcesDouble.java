package org.eclipse.swt.tests.doubles;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.internal.canvasext.FontProperties;
import org.eclipse.swt.internal.canvasext.IDpiScaler;
import org.eclipse.swt.internal.skia.ISkImage;
import org.eclipse.swt.internal.skia.ISkiaResources;

public class SkiaResourcesDouble implements ISkiaResources {

	public Color background;
	public Color foreground;
	public Font font;
	public io.github.humbleui.skija.Font skiaFont;
	public Image cachedImage;
	public ISkImage cachedSkijaImage;
	public ISkImage cachedTextImage;
	public String[] textSplits;
	public Point textExtent;
	public FontData fontData;
	public IDpiScaler scaler;

	public SkiaResourcesDouble(int zoom) {
		this.scaler = () -> zoom;
	}

	public SkiaResourcesDouble() {
		this(100);
	}

	@Override
	public void setBackground(Color color) {
		this.background = color;
	}

	@Override
	public void setForeground(Color color) {
		this.foreground = color;
	}

	@Override
	public Color getForeground() {
		return foreground;
	}

	@Override
	public Color getBackground() {
		return background;
	}

	@Override
	public void setFont(Font font) {
		this.font = font;
	}

	@Override
	public io.github.humbleui.skija.Font getSkiaFont() {
		return skiaFont;
	}

	@Override
	public IDpiScaler getScaler() {
		return scaler;
	}

	@Override
	public Font getFont() {
		return font;
	}

	@Override
	public void resetBaseColors() {
		// TODO Auto-generated method stub

	}

	@Override
	public void cacheTextImage(String text, FontProperties fontProperties, boolean transparent, int background,
			int foreground, boolean antiAlias, ISkImage skijaImage) {
		// TODO Auto-generated method stub

	}

	@Override
	public ISkImage getTextImage(String text, FontProperties fontProperties, boolean transparent, int background,
			int foreground, boolean antialias) {
		return cachedTextImage;
	}

	@Override
	public String[] getTextSplits(String inputText, int flags) {
		return TextSplitUtil.getTextSplits(inputText, flags, null, false);
	}

	@Override
	public Point textExtent(String text, int flags) {
		return textExtent;
	}

	@Override
	public Point textExtent(String string) {
		return textExtent;
	}

	@Override
	public FontData getFontData() {
		return this.fontData;
	}

}