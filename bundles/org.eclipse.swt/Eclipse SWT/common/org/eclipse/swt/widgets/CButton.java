package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

public class CButton extends Canvas {

	Image image;

	String text;

	public CButton(Composite parent, int style) {
		super(parent, style);

		addMouseListener(MouseListener.mouseUpAdapter(this::onMouseUp));
		addKeyListener(KeyListener.keyReleasedAdapter(this::onKeyReleased));
		addDisposeListener(this::onDispose);
		addPaintListener(this::onPaint);
	}

	private void onDispose(DisposeEvent e) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	private void onPaint(PaintEvent e) {
		GC gc = e.gc;
		int x = 1;

		if (image != null) {
			gc.drawImage(image, x, 1);
			x = image.getBounds().width + 5;
		}

		if (text != null) {
			gc.drawString(text, x, 1);
		}
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
		redraw();
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		redraw();
	}

	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		int width = 0, height = 0;

		if (image != null) {
			Rectangle bounds = image.getBounds();
			width = bounds.width + 5;
			height = bounds.height;
		}

		if (text != null) {
			GC gc = new GC(this);
			Point extent = gc.stringExtent(text);
			gc.dispose();
			width += extent.x;
			height = Math.max(height, extent.y);
		}

		// TODO (visjee) honor the hints?
//		if (wHint != SWT.DEFAULT)
//			width = wHint;
//		if (hHint != SWT.DEFAULT)
//			height = hHint;

		// add a 1-pixel margin to each side
		return new Point(width + 2, height + 2);
	}

	public void addSelectionListener(SelectionListener listener) {
		addListener(SWT.Selection, new TypedListener(listener));
	}

	public void removeSelectionListener(SelectionListener listener) {
		removeListener(SWT.Selection, listener);
	}

	private void onMouseUp(MouseEvent e) {
		notifyListeners(SWT.Selection, new Event());
	}

	private void onKeyReleased(KeyEvent e) {
		System.out.println("Key: " + e.keyCode + " - " + e.character);

		int oneOf = e.keyCode & SWT.CR & SWT.LF & SWT.DEL & SWT.ESC & SWT.TAB;
		System.out.println("Is one of: " + oneOf);

		notifyListeners(SWT.Selection, new Event());
	}
}
