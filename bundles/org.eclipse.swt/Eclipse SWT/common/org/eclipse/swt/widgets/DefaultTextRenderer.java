package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

public class DefaultTextRenderer extends TextRenderer {

	private static final Color DISABLED_COLOR = new Color(160, 160, 160);
	private static final Color TEXT_COLOR = new Color(0, 0, 0);
	private static final Color BACKGROUND_COLOR = new Color(255, 255, 255);
	private static final Color BORDER_COLOR = new Color(128, 128, 128);
	private static final Color READONLY_BACKGROUND_COLOR = new Color(227, 227, 227);
	private static final Color SELECTION_BACKGROUND_COLOR = new Color(0, 120, 215);
	private static final Color SELECTION_TEXT_COLOR = new Color(255, 255, 255);

	public DefaultTextRenderer(Text text, TextModel model) {
		super(text, model);
	}

	@Override
	protected void paint(GC gc, int width, int height) {
		final boolean enabled = text.isEnabled();
		if (!enabled) {
			gc.setForeground(DISABLED_COLOR);
		}

		final int style = text.getStyle();
		final boolean editable = text.getEditable();
		if (!enabled || ((style & SWT.BORDER) == 1 && !editable)) {
			gc.setBackground(READONLY_BACKGROUND_COLOR);
		}

		final Rectangle visibleArea = getVisibleArea();
		final Rectangle clientArea = text.getClientArea();

		drawBackground(gc, clientArea, style, editable && enabled);
		drawText(visibleArea, clientArea, style, gc);
		if (text.isFocusControl()) {
			drawSelection(gc, visibleArea);
			drawCaret(gc, visibleArea);
		}
	}

	@Override
	public Color getDefaultBackground() {
		return BACKGROUND_COLOR;
	}

	@Override
	public Color getDefaultForeground() {
		return TEXT_COLOR;
	}

	@Override
	protected Rectangle getVisibleArea() {
		Rectangle clientArea = text.getClientArea();

		ScrollBar horizontalBar = text.getHorizontalBar();
		ScrollBar verticalBar = text.getVerticalBar();

		int hOffset = (horizontalBar != null) ? horizontalBar.getSelection() : 0;
		int vOffset = (verticalBar != null) ? verticalBar.getSelection() : 0;

		clientArea.x += hOffset;
		clientArea.y += vOffset;

		return clientArea;
	}

	@Override
	protected Point getLocationByTextLocation(TextLocation textLocation, GC gc) {
		String completeText = model.getLines()[textLocation.line];
		String beforeSelection = completeText.substring(0, textLocation.column);
		gc.setFont(text.getFont());
		Point completeTextExtent = gc.textExtent(completeText);
		Point textExtent = gc.textExtent(beforeSelection);
		int x;
		Rectangle clientArea = text.getClientArea();
		final int style = text.getStyle();
		if ((style & SWT.CENTER) != 0) {
			x = (clientArea.width - completeTextExtent.x) / 2;
		} else if ((style & SWT.RIGHT) != 0) {
			x = clientArea.width - completeTextExtent.x;
		} else { // ((style & SWT.LEFT) != 0)
			x = 0;
		}
		x += textExtent.x;
		int y = textLocation.line * textExtent.y;
		if ((style & SWT.BORDER) != 0) {
			final int borderWidth = text.getBorderWidth();
			x += borderWidth;
			y += borderWidth;
		}
		return new Point(x, y);
	}

	@Override
	protected int getLineHeight(GC gc) {
		String str = model.getLines()[0];
		return gc.textExtent(str).y;
	}

	private void drawBackground(GC gc, Rectangle clientArea, int style, boolean editable) {
		int height = clientArea.height;
		final boolean drawLine = (style & SWT.BORDER) != 0 && editable;
		if (drawLine) {
			height--;
		}
		gc.fillRectangle(clientArea.x, clientArea.y, clientArea.width, height);
		if (drawLine) {
			Color prevBackground = gc.getBackground();
			gc.setBackground(text.isFocusControl() ? SELECTION_BACKGROUND_COLOR : BORDER_COLOR);
			gc.fillRectangle(clientArea.x, clientArea.y + height, clientArea.width, 1);
			gc.setBackground(prevBackground);
		}
	}

	private void drawText(Rectangle visibleArea, Rectangle clientArea, int style, GC gc) {
		String[] lines = model.getLines();
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			drawTextLine(line, i, visibleArea, clientArea, style, gc);
		}
	}

	private void drawTextLine(String text, int lineNumber, Rectangle visibleArea,
							  Rectangle clientArea, int style, GC gc) {
		Point completeTextExtent = gc.textExtent(text);
		int _x = 0;
		if ((style & SWT.CENTER) != 0) {
			_x = (clientArea.width - completeTextExtent.x) / 2;
		} else if ((style & SWT.RIGHT) != 0) {
			_x = clientArea.width - completeTextExtent.x;
		}
		_x -= visibleArea.x;
		int _y = lineNumber * completeTextExtent.y - visibleArea.y;
		if ((style & SWT.BORDER) != 0) {
			final int borderWidth = this.text.getBorderWidth();
			_x += borderWidth;
			_y += borderWidth;
		}
		gc.drawText(text, _x, _y, true);
	}

	private void drawSelection(GC gc, Rectangle visibleArea) {
		int textLength = model.getText().length();
		int start = Math.min(Math.max(model.getSelectionStart(), 0), textLength);
		int end = Math.min(Math.max(model.getSelectionEnd(), 0), textLength);

		if (model.getSelectionStart() >= 0) {
			TextLocation startLocation = model.getLocation(start);
			TextLocation endLocation = model.getLocation(end);
			String[] textLines = model.getLines();

			Color oldForeground = gc.getForeground();
			Color oldBackground = gc.getBackground();
			gc.setForeground(SELECTION_TEXT_COLOR);
			gc.setBackground(SELECTION_BACKGROUND_COLOR);
			for (int i = startLocation.line; i <= endLocation.line; i++) {
				TextLocation location = new TextLocation(i, 0);
				String text = textLines[i];
				if (i == endLocation.line) {
					text = text.substring(0, endLocation.column);
				}
				if (i == startLocation.line) {
					location.column = startLocation.column;
					text = text.substring(startLocation.column);
				}
				Point locationPixel = getLocationByTextLocation(location, gc);
				gc.drawText(text, locationPixel.x - visibleArea.x, locationPixel.y - visibleArea.y, false);
			}
			gc.setForeground(oldForeground);
			gc.setBackground(oldBackground);
		}
	}

	private void drawCaret(GC gc, Rectangle visibleArea) {
		int caretOffset = model.getCaretOffset();
		final TextCaret caret = text.getCaret();
		if (caretOffset >= 0) {
			int offset = model.getCaretOffset();
			TextLocation textLocation = model.getLocation(offset);
			Point caretLocation = getLocationByTextLocation(textLocation, gc);
			int x = caretLocation.x - visibleArea.x;
			int y = caretLocation.y - visibleArea.y;
			caret.setBounds(x, y, 1, getLineHeight(gc));
		}

		caret.paint(gc);
	}
}
