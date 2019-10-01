/*******************************************************************************
 * Copyright (c) 2019 Peter Severin and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Peter Severin - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class Bug551588_TextLayoutExample {
	final static String longString;

	static {
		String loremIpsum = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";

		StringBuilder b = new StringBuilder();
		for (int i = 0; i < 20; i++) {
			b.append(loremIpsum);
		}
		longString = b.toString();
	}

	public static void main(String[] args) {
		Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setText("Bug551588");
		shell.setLayout(new FillLayout());

		final ScrolledComposite sc = new ScrolledComposite(shell, SWT.V_SCROLL);

		final TextLayoutControl textLayout = new TextLayoutControl(sc, SWT.NONE);
		textLayout.setText(longString);

		sc.setContent(textLayout);
		sc.addListener(SWT.Resize, event -> {
			int availableWidth = sc.getClientArea().width;
			textLayout.setSize(textLayout.computeSize(availableWidth, SWT.DEFAULT));
			int availableWidthPrime = sc.getClientArea().width;
			if (availableWidth != availableWidthPrime) {
				textLayout.setSize(textLayout.computeSize(availableWidthPrime, SWT.DEFAULT));
			}
		});
		shell.setSize(300, 300);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

}

class TextLayoutControl extends Canvas {
	private final TextLayout textLayout;

	private Listener listener = event -> TextLayoutControl.this.handleEvent(event);

	public TextLayoutControl(Composite parent, int style) {
		super(parent, checkStyle(style));

		textLayout = new TextLayout(parent.getDisplay());

		final int[] events = new int[] { SWT.Paint, SWT.Resize, SWT.Dispose };

		for (int i = 0; i < events.length; i++) {
			addListener(events[i], listener);
		}
	}

	public void setText(String string) {
		textLayout.setText(string);
		redraw();
	}

	private static int checkStyle(int style) {
		style &= ~SWT.H_SCROLL;
		style &= ~SWT.V_SCROLL;
		style |= SWT.DOUBLE_BUFFERED;

		return style;
	}

	private void handleEvent(Event event) {
		switch (event.type) {
		case SWT.Paint:
			onPaint(event.gc);
			break;
		case SWT.Resize:
			onResize();
			redraw();
			break;
		case SWT.Dispose:
			onDispose();
			break;
		}
	}

	private void onDispose() {
		textLayout.dispose();
	}

	private void onPaint(GC gc) {
		Rectangle clientArea = getClientArea();
		gc.fillRectangle(clientArea);

		int x = clientArea.x;
		int y = clientArea.y;

		textLayout.draw(gc, x, y);
	}

	private void onResize() {
		final Rectangle bounds = getBounds();
		textLayout.setWidth(bounds.width);
	}

	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		int oldWidth = textLayout.getWidth();
		Rectangle size;

		try {
			textLayout.setWidth(wHint);
			size = textLayout.getBounds();
		} finally {
			textLayout.setWidth(oldWidth);
		}

		return new Point(size.width, size.height);
	}
}