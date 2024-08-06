package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

public class CSimpleText extends Canvas {

	public static final String DELIMITER = "\n";

	private static final int LINE_HEIGTH = 16;

	private TextContent content;
	private String message;

	private int selectionStart, selectionEnd;
	private int caretOffset;
//	Caret caret;

	public CSimpleText(Composite parent, int style) {
		super(parent, style);
		content = new TextContent();
		message = "";

		caret = new Caret(this, SWT.NONE);

		addDisposeListener(e -> CSimpleText.this.widgetDisposed(e));
		addPaintListener(e -> CSimpleText.this.paintControl(e));
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				CSimpleText.this.keyPressed(e);
			}
		});

		ScrollBar horizontalBar = getHorizontalBar();
		if (horizontalBar != null) {
			horizontalBar.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					CSimpleText.this.scrollBarSelectionChanged(e);
				}
			});
		}
		ScrollBar verticalBar = getVerticalBar();
		if (verticalBar != null) {
			verticalBar.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					CSimpleText.this.scrollBarSelectionChanged(e);
				}
			});
		}
	}

	private void keyPressed(KeyEvent e) {
		switch (e.keyCode) {
        case SWT.ARROW_LEFT:
            System.out.println("Left arrow key pressed");
            caretOffset--;
            break;
        case SWT.ARROW_RIGHT:
            System.out.println("Right arrow key pressed");
            caretOffset++;
            break;
        default:
    		StringBuilder sb = new StringBuilder(content.getText());
    		sb.insert(caretOffset, e.character);
    		content.setText(sb.toString());
    		caretOffset++;
    		selectionStart = selectionEnd = -1;
		}
	}

	protected void widgetDisposed(DisposeEvent e) {
		// TODO Auto-generated method stub
	}

	private void paintControl(PaintEvent e) {
		drawBackground(e);
		Rectangle visibleArea = getVisibleArea();
		drawText(e, visibleArea);
		drawSelection(e, visibleArea);
		drawFrame(e);
		drawCaret(e, visibleArea);
	}

	private void drawSelection(PaintEvent e, Rectangle visibleArea) {
		GC gc = e.gc;

		if (selectionStart >= 0) {
			Point startLocationPixel = getLocationByOffset(selectionStart, gc);
			Point startLocation = content.getLocation(selectionStart);
			Point endLocation = content.getLocation(selectionEnd);
			String[] textLines = content.getLines();

			Color oldBackground = gc.getBackground();
			gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_GRAY));

			int y = startLocationPixel.y;
			for (int i = startLocation.y; i <= endLocation.y; i++) {
				int x = 0;
				String text = textLines[i];
				if (i == endLocation.y) {
					text = text.substring(0, endLocation.x);
				}
				if (i == startLocation.y) {
					x = startLocationPixel.x;
					text = text.substring(startLocation.x);
				}
				gc.drawText(text, x - visibleArea.x, y - visibleArea.y, false);
				y += LINE_HEIGTH;
			}

			gc.setBackground(oldBackground);

		}
	}

	private void drawCaret(PaintEvent e, Rectangle visibleArea) {
		GC gc = e.gc;

		if (caretOffset >= 0) {
			Point caretLocation = getLocationByOffset(caretOffset, gc);
			int x = e.x + caretLocation.x - visibleArea.x;
			int y = e.y + caretLocation.y - visibleArea.y;
			caret.setBounds(x, y, 1, LINE_HEIGTH);
		}

	}

	private void drawFrame(PaintEvent e) {
		GC gc = e.gc;
		gc.setForeground(getForeground());
		gc.drawRectangle(e.x, e.y, e.width - 1, e.height - 1);
	}

	private void drawText(PaintEvent e, Rectangle visibleArea) {
		GC gc = e.gc;
		gc.drawText(content.getText(), e.x - visibleArea.x, e.y - visibleArea.y, true);
	}

	private void drawBackground(PaintEvent e) {
		GC gc = e.gc;
		gc.setBackground(new Color(255, 255, 255, 128));
		gc.fillRectangle(e.x, e.y, e.width - 1, e.height - 1);
	}

	private Rectangle getVisibleArea() {
		Rectangle clientArea = getClientArea();

		ScrollBar horizontalBar = getHorizontalBar();
		ScrollBar verticalBar = getVerticalBar();

		int hOffset = (horizontalBar != null) ? horizontalBar.getSelection() : 0;
		int vOffset = (verticalBar != null) ? verticalBar.getSelection() : 0;

		clientArea.x += hOffset;
		clientArea.y += vOffset;

		return clientArea;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
		redraw();
	}

	public String getText() {
		return content.getText();
	}

	public void setText(String text) {
		content.setText(text);
		redraw();
	}

	public void append(String string) {
		content.append(string);
		redraw();
	}

	public void setSelection(int start) {
		selectionStart = start;
		redraw();
	}
	public void setSelection (int start, int end) {
		int textLength = content.getText().length();
		selectionStart = Math.min (Math.max (Math.min (start, end), 0), textLength);
		selectionEnd = Math.min (Math.max (Math.max (start, end), 0), textLength);
		caretOffset = end;
		redraw();
	}

	public Point getSelection() {
		return new Point(0, 0);
	}

	public String getSelectionText () {
		return content.getText().substring(selectionStart, selectionEnd);
	}

	public int getTopIndex () {
		if ((style & SWT.SINGLE) != 0) return 0;
		Rectangle visibleArea = getVisibleArea();
		return visibleArea.x / LINE_HEIGTH;
	}

	public void setTopIndex (int index) {
		checkWidget ();
		if ((style & SWT.SINGLE) != 0) return;
		Rectangle visibleArea = getVisibleArea();

		int y = index * LINE_HEIGTH;
		scroll(0, 0, visibleArea.x, y, visibleArea.width, visibleArea.height, true);
	}

	/**
	 * Returns a point describing the location of the caret relative to the
	 * receiver.
	 *
	 * @return a point, the location of the caret
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public Point getCaretLocation() {
		return getLocationByOffset(caretOffset, new GC(this));
	}

	public Point getLocationByOffset(int offset, GC gc) {
		Point selectionLocation = content.getLocation(offset);

		String beforeSelection = content.getLines()[selectionLocation.y].substring(0, selectionLocation.x);
		int x = gc.textExtent(beforeSelection).x;
		int y = selectionLocation.y * LINE_HEIGTH;
		return new Point(x, y);
	}

	/**
	 * Returns the character position of the caret.
	 * <p>
	 * Indexing is zero based.
	 * </p>
	 *
	 * @return the position of the caret
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public int getCaretPosition() {
		return selectionStart;
	}

	class TextContent {
		private String text = "";

		public String getText() {
			return text;
		}

		public void setText(String text) {
			if (text == null)
				error(SWT.ERROR_NULL_ARGUMENT);
			this.text = text;
		}

		public void append(String string) {
			if (string == null)
				error(SWT.ERROR_NULL_ARGUMENT);
			text = text + string;
		}

		public String[] getLines() {
			return getLinesOf(text);
		}

		public int getLineCount() {
			return getLines().length;
		}

		public Point getLocation(int offset) {
			String beforeOffset = text.substring(0, offset);
			String[] lines = getLinesOf(beforeOffset);
			return lines.length > 0 ? new Point(lines[lines.length - 1].length(), (lines.length - 1)) : new Point(0, 0);
		}

		private String[] getLinesOf(String string) {
			return string.split(DELIMITER);
		}
	}

	private void scrollBarSelectionChanged(SelectionEvent e) {
		redraw();
	}
}
