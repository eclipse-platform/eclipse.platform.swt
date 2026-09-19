package org.eclipse.swt.internal.skia;

public interface ISkiaResources {
	void setBackground(org.eclipse.swt.graphics.Color color);
	void setForeground(org.eclipse.swt.graphics.Color color);
	org.eclipse.swt.graphics.Color getForeground();
	org.eclipse.swt.graphics.Color getBackground();
	void setFont(org.eclipse.swt.graphics.Font font);
	io.github.humbleui.skija.Font getSkiaFont();
	org.eclipse.swt.internal.canvasext.IDpiScaler getScaler();
	org.eclipse.swt.graphics.Font getFont();
	org.eclipse.swt.graphics.FontData getFontData();
	void resetBaseColors();
	void cacheTextImage(String text, org.eclipse.swt.internal.canvasext.FontProperties fontProperties, boolean transparent, int background, int foreground, boolean antiAlias, ISkImage image);
	ISkImage getTextImage(String text, org.eclipse.swt.internal.canvasext.FontProperties fontProperties, boolean transparent, int background, int foreground, boolean antialias);
	String[] getTextSplits(String inputText, int flags);
	org.eclipse.swt.graphics.Point textExtent(String text, int flags);
	org.eclipse.swt.graphics.Point textExtent(String string);
}