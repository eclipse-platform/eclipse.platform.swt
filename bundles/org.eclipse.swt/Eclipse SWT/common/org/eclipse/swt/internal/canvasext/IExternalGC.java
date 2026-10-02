/*******************************************************************************
 * Copyright (c) 2025 SAP SE and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * Contributors:
 *     SAP SE and others - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.canvasext;

import org.eclipse.swt.graphics.*;

public interface IExternalGC {

	Device getDevice();

	void copyArea(Image image, int i, int j);

	void dispose();

	Point textExtent(String string);

	void setBackground(Color color);

	void setForeground(Color color);

	void fillRectangle(Rectangle rect);

	void drawImage(Image image, int x, int y);

	void drawImage(Image image, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth,
			int destHeight);

	void drawLine(int x1, int y1, int x2, int y2);

	Color getForeground();

	void drawText(String string, int x, int y);

	void drawText(String string, int x, int y, boolean isTransparent);

	void drawText(String text, int x, int y, int flags);

	void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle);

	void drawFocus(int x, int y, int width, int height);

	void drawOval(int x, int y, int width, int height);

	void drawPath(Path path);

	void drawPoint(int x, int y);

	void drawPolygon(int[] pointArray);

	void drawPolyline(int[] pointArray);

	void drawRectangle(int x, int y, int width, int height);

	void drawRectangle(Rectangle rect);

	void drawRoundRectangle(int x, int y, int width, int height, int arcWidth, int arcHeight);

	void drawString(String string, int x, int y);

	void drawString(String string, int x, int y, boolean isTransparent);

	void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle);

	void fillGradientRectangle(int x, int y, int width, int height, boolean vertical);

	void fillOval(int x, int y, int width, int height);

	void fillPath(Path path);

	void fillPolygon(int[] pointArray);

	void fillRectangle(int x, int y, int width, int height);

	Point textExtent(String string, int flags);

	void fillRoundRectangle(int x, int y, int width, int height, int arcWidth, int arcHeight);

	void setFont(Font font);

	Font getFont();

	void setAlpha(int alpha);

	int getAlpha();

	void setLineWidth(int i);

	int getAntialias();

	void setAntialias(int antialias);

	void setAdvanced(boolean enable);

	void setLineStyle(int lineStyle);

	int getLineStyle();

	int getLineWidth();

	LineAttributes getLineAttributes();

	void setClipping(int x, int y, int width, int height);

	void setTransform(Transform transform);

	Point stringExtent(String string);

	int getLineCap();

	Rectangle getClipping();

	void copyArea(int srcX, int srcY, int width, int height, int destX, int destY);

	void copyArea(int srcX, int srcY, int width, int height, int destX, int destY, boolean paint);

	boolean isClipped();

	int getFillRule();

	void getClipping(Region region);

	int getAdvanceWidth(char ch);

	boolean getAdvanced();

	int getCharWidth(char ch);

	Color getBackground();

	Pattern getBackgroundPattern();

	Pattern getForegroundPattern();

	GCData getGCData();

	int getInterpolation();

	int[] getLineDash();

	int getLineJoin();

	int getStyle();

	int getTextAntialias();

	void getTransform(Transform transform);

	boolean getXORMode();

	void setBackgroundPattern(Pattern pattern);

	void setClipping(Path path);

	void setClipping(Rectangle rect);

	void setClipping(Region region);

	void setFillRule(int rule);

	void setInterpolation(int interpolation);

	void setForegroundPattern(Pattern pattern);

	void setLineAttributes(LineAttributes attributes);

	void setLineCap(int cap);

	void setLineDash(int[] dashes);

	void setLineJoin(int join);

	void setXORMode(boolean xor);

	void setTextAntialias(int antialias);

	boolean isDisposed();

	void drawImage(Image image, int destX, int destY, int destWidth, int destHeight);

	FontMetrics getFontMetrics();

	Drawable getDrawable();

	void textLayoutDraw(TextLayout textLayout, GC gc, int xInPoints, int yInPoints, int selectionStart,
			int selectionEnd, Color selectionForeground, Color selectionBackground, int flags);

}
