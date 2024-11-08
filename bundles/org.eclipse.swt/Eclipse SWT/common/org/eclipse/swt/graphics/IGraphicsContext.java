package org.eclipse.swt.graphics;

public interface IGraphicsContext {
	default void commit() {};
    void dispose();
    void drawLine(int x1, int y1, int x2, int y2);
    void drawRectangle(int x, int y, int width, int height);
    void fillRectangle(int x, int y, int width, int height);
    void drawText(String string, int x, int y);
    void drawText(String string, int x, int y, boolean isTransparent);
    void drawImage(Image image, int x, int y);
    void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle);
    void drawOval(int x, int y, int width, int height);
    void fillOval(int x, int y, int width, int height);
    void drawPolyline(int[] pointArray);
    void drawPolygon(int[] pointArray);
    void fillPolygon(int[] pointArray);
    void drawPoint(int x, int y);
    void drawRoundRectangle(int x, int y, int width, int height, int arcWidth, int arcHeight);
    void fillRoundRectangle(int x, int y, int width, int height, int arcWidth, int arcHeight);

    void setBackground(Color color);
    Color getBackground();
    void setForeground(Color color);
    Color getForeground();
    void setFont(Font font);
    Font getFont();
    Rectangle getClipping();
    void setClipping(int x, int y, int width, int height);
    void setTransform(Transform transform);
    void setAlpha(int alpha);
    int getAlpha();
	Point textExtent(String string, int dRAW_FLAGS);
	void fillRectangle(Rectangle rect);
	void fillGradientRectangle(int i, int pos, int width, int gradientHeight,
			boolean b);
	void setLineWidth(int i);
	IFontMetrics getFontMetrics();
	void drawImage(Image img, int i, int j, int width, int imageHeight, int x,
			int imageY, int width2, int imageHeight2);
	void drawText(String line, int lineX, int lineY, int dRAW_FLAGS);
	void drawRectangle(Rectangle rectangle);

	void setAntialias(int antialias);

	int getAntialias();

	void setAdvanced(boolean enable);

	void setLineStyle(int lineStyle);
	void drawFocus (int x, int y, int width, int height);
	int getLineStyle();
	int getLineWidth();
	LineAttributes getLineAttributes();

}