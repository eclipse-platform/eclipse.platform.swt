/*******************************************************************************
 * Copyright (c) 2025 SAP SE and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/

package org.eclipse.swt.graphics;

import java.util.*;

import org.eclipse.swt.internal.canvasext.*;

/**
 * @noreference This class is not intended to be referenced by clients.
 */
public final class GCExtension extends GC {

	private final IExternalGC e;

	/**
	 * Blocks access to the static factory methods from GC.
	 *
	 * @noreference This class is not intended to be referenced by clients.
	 */
	public static GCExtension gtk_new(long handle, GCData data) {
		throw new IllegalStateException("GCExtension does not support gtk_new");
	}

	/**
	 * Blocks access to the static factory methods from GC.
	 *
	 * @noreference This class is not intended to be referenced by clients.
	 */
	public static GCExtension gtk_new(Drawable drawable, GCData data) {
		throw new IllegalStateException("GCExtension does not support gtk_new");
	}

	/**
	 * Blocks access to the GC constructor.
	 */
	GCExtension() {
		throw new IllegalStateException("GCExtension does not supported protected constructor");
	}

	/**
	 * Blocks access to the GC constructor.
	 *
	 * @noreference This class is not intended to be referenced by clients.
	 */
	public GCExtension(Drawable drawable) {
		throw new IllegalStateException("Invalid Constructor call");
	}

	/**
	 * Blocks access to the GC constructor.
	 *
	 * @noreference This class is not intended to be referenced by clients.
	 */
	public GCExtension(Drawable drawable, int style) {
		throw new IllegalStateException("Invalid Constructor call");
	}

	/**
	 * Blocks access to the GC constructor.
	 *
	 * @noreference This class is not intended to be referenced by clients.
	 */
	public GCExtension(final IExternalGC ext) {
		this.e = ext;
	}

	@Override
	public FontMetrics getFontMetrics() {
		return e.getFontMetrics();
	}

	@Override
	public void dispose() {
		e.dispose();
	}

	@Override
	public void drawImage(Image image, int destX, int destY, int destWidth, int destHeight) {
		e.drawImage(image, destX, destY, destWidth, destHeight);
	}

	@Override
	public void copyArea(Image image, int x, int y) {
		e.copyArea(image, x, y);
	}

	@Override
	public void copyArea(int srcX, int srcY, int width, int height, int destX, int destY) {
		e.copyArea(srcX, srcY, width, height, destX, destY);
	}

	@Override
	public int getAdvanceWidth(char ch) {
		return e.getAdvanceWidth(ch);
	}

	@Override
	public boolean getAdvanced() {
		return e.getAdvanced();
	}

	@Override
	public int getAlpha() {
		return e.getAlpha();
	}

	@Override
	public int getAntialias() {
		return e.getAntialias();
	}

	@Override
	public Color getBackground() {
		return e.getBackground();
	}

	@Override
	public Pattern getBackgroundPattern() {
		return e.getBackgroundPattern();
	}

	@Override
	public int getCharWidth(char ch) {
		return e.getCharWidth(ch);
	}

	@Override
	public Rectangle getClipping() {
		return e.getClipping();
	}

	@Override
	public void getClipping(Region region) {
		e.getClipping(region);
	}

	@Override
	public int getFillRule() {
		return e.getFillRule();
	}

	@Override
	public Font getFont() {
		return e.getFont();
	}

	@Override
	public Color getForeground() {
		return e.getForeground();
	}

	@Override
	public Pattern getForegroundPattern() {
		return e.getForegroundPattern();
	}


	/**
	 * @noreference This method is not intended to be referenced by clients.
	 */
	@Override
	public GCData getGCData() {
		return e.getGCData();
	}

	@Override
	public int getInterpolation() {
		return e.getInterpolation();
	}

	@Override
	public LineAttributes getLineAttributes() {
		return e.getLineAttributes();
	}

	@Override
	public int getLineCap() {
		return e.getLineCap();
	}

	@Override
	public int[] getLineDash() {
		return e.getLineDash();
	}

	@Override
	public int getLineJoin() {
		return e.getLineJoin();
	}

	@Override
	public int getLineStyle() {
		return e.getLineStyle();
	}

	@Override
	public int getLineWidth() {
		return e.getLineWidth();
	}

	@Override
	public int getStyle() {
		return e.getStyle();
	}

	@Override
	public int getTextAntialias() {
		return e.getTextAntialias();
	}

	@Override
	public void getTransform(Transform transform) {
		e.getTransform(transform);
	}

	@Override
	public boolean getXORMode() {
		return e.getXORMode();
	}

	@Override
	public boolean isClipped() {
		return e.isClipped();
	}

	@Override
	public boolean isDisposed() {
		return e.isDisposed();
	}

	@Override
	public void setAdvanced(boolean advanced) {
		e.setAdvanced(advanced);
	}

	@Override
	public void setAlpha(int alpha) {
		e.setAlpha(alpha);
	}

	@Override
	public void setAntialias(int antialias) {
		e.setAntialias(antialias);
	}

	@Override
	public void setBackground(Color color) {
		e.setBackground(color);
	}

	@Override
	public void setBackgroundPattern(Pattern pattern) {
		e.setBackgroundPattern(pattern);
	}

	@Override
	public void setClipping(int x, int y, int width, int height) {
		e.setClipping(x, y, width, height);
	}

	@Override
	public void setClipping(Path path) {
		e.setClipping(path);
	}

	@Override
	public void setClipping(Rectangle rect) {
		e.setClipping(rect);
	}

	@Override
	public void setClipping(Region region) {
		e.setClipping(region);
	}

	@Override
	public void setFont(Font font) {
		e.setFont(font);
	}

	@Override
	public void setFillRule(int rule) {
		e.setFillRule(rule);
	}

	@Override
	public void setForeground(Color color) {
		e.setForeground(color);
	}

	@Override
	public void setForegroundPattern(Pattern pattern) {
		e.setForegroundPattern(pattern);
	}

	@Override
	public void setInterpolation(int interpolation) {
		e.setInterpolation(interpolation);
	}

	@Override
	public void setLineAttributes(LineAttributes attributes) {
		e.setLineAttributes(attributes);
	}

	@Override
	public void setLineCap(int cap) {
		e.setLineCap(cap);
	}

	@Override
	public void setLineDash(int[] dashes) {
		e.setLineDash(dashes);
	}

	@Override
	public void setLineJoin(int join) {
		e.setLineJoin(join);
	}

	@Override
	public void setLineStyle(int lineStyle) {
		e.setLineStyle(lineStyle);
	}

	@Override
	public void setLineWidth(int lineWidth) {
		e.setLineWidth(lineWidth);
	}

	@Override
	public void setTextAntialias(int antialias) {
		e.setTextAntialias(antialias);
	}

	@Override
	public void setTransform(Transform transform) {
		e.setTransform(transform);
	}

	@Override
	public void setXORMode(boolean xor) {
		e.setXORMode(xor);
	}

	@Override
	public Point stringExtent(String string) {
		return e.stringExtent(string);
	}

	@Override
	public Point textExtent(String string) {
		return e.textExtent(string);
	}

	@Override
	public Point textExtent(String string, int flags) {
		return e.textExtent(string, flags);
	}

	@Override
	public void copyArea(int srcX, int srcY, int width, int height, int destX, int destY, boolean paint) {
		e.copyArea(srcX, srcY, width, height, destX, destY, paint);
	}

	@Override
	public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
		e.drawArc(x, y, width, height, startAngle, arcAngle);
	}

	@Override
	public void drawFocus(int x, int y, int width, int height) {
		e.drawFocus(x, y, width, height);
	}

	@Override
	public void drawImage(Image image, int x, int y) {
		e.drawImage(image, x, y);
	}

	@Override
	public void drawImage(Image image, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY,
			int destWidth, int destHeight) {
		e.drawImage(image, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight);
	}

	@Override
	public void drawLine(int x1, int y1, int x2, int y2) {
		e.drawLine(x1, y1, x2, y2);
	}

	@Override
	public void drawOval(int x, int y, int width, int height) {
		e.drawOval(x, y, width, height);
	}

	@Override
	public void drawPath(Path path) {
		e.drawPath(path);
	}

	@Override
	public void drawPoint(int x, int y) {
		e.drawPoint(x, y);
	}

	@Override
	public void drawPolygon(int[] pointArray) {
		e.drawPolygon(pointArray);
	}

	@Override
	public void drawPolyline(int[] pointArray) {
		e.drawPolyline(pointArray);
	}

	@Override
	public void drawRectangle(int x, int y, int width, int height) {
		e.drawRectangle(x, y, width, height);
	}

	@Override
	public void drawRectangle(Rectangle rect) {
		e.drawRectangle(rect);
	}

	@Override
	public void drawRoundRectangle(int x, int y, int width, int height, int arcWidth, int arcHeight) {
		e.drawRoundRectangle(x, y, width, height, arcWidth, arcHeight);
	}

	@Override
	public void drawString(String string, int x, int y) {
		e.drawString(string, x, y);
	}

	@Override
	public void drawString(String string, int x, int y, boolean isTransparent) {
		e.drawString(string, x, y, isTransparent);
	}

	@Override
	public void drawText(String string, int x, int y) {
		e.drawText(string, x, y);
	}

	@Override
	public void drawText(String string, int x, int y, boolean isTransparent) {
		e.drawText(string, x, y, isTransparent);
	}

	@Override
	public void drawText(String string, int x, int y, int flags) {
		e.drawText(string, x, y, flags);
	}

	@Override
	public boolean equals(Object object) {
		return Objects.equals(object, this);
	}

	@Override
	public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
		e.fillArc(x, y, width, height, startAngle, arcAngle);
	}

	@Override
	public void fillGradientRectangle(int x, int y, int width, int height, boolean vertical) {
		e.fillGradientRectangle(x, y, width, height, vertical);
	}

	@Override
	public void fillOval(int x, int y, int width, int height) {
		e.fillOval(x, y, width, height);
	}

	@Override
	public void fillPath(Path path) {
		e.fillPath(path);
	}

	@Override
	public void fillPolygon(int[] pointArray) {
		e.fillPolygon(pointArray);
	}

	@Override
	public void fillRectangle(int x, int y, int width, int height) {
		e.fillRectangle(x, y, width, height);
	}

	@Override
	public void fillRectangle(Rectangle rect) {
		e.fillRectangle(rect);
	}

	@Override
	public void fillRoundRectangle(int x, int y, int width, int height, int arcWidth, int arcHeight) {
		e.fillRoundRectangle(x, y, width, height, arcWidth, arcHeight);
	}

	@Override
	public int hashCode() {
		return e.hashCode();
	}

	@Override
	public String toString() {
		if (isDisposed())
			return "GCExtension {*DISPOSED*}";
		return "GCExtension {" + e + "}";
	}

	@Override
	public Device getDevice() {
		return e.getDevice();
	}

	public void textLayoutDraw(TextLayout textLayout, GC gc, int xInPoints, int yInPoints, int selectionStart,
			int selectionEnd, Color selectionForeground, Color selectionBackground, int flags) {
		e.textLayoutDraw(textLayout,gc,xInPoints,yInPoints,selectionStart, selectionEnd, selectionForeground, selectionBackground, flags);
	}

}