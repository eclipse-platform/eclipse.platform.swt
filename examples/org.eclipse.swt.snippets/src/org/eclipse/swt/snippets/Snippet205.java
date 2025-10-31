/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * TextLayout example snippet: using the GlyphMetrics to embedded images in
 * a TextLayout.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.2
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet205 {

public static void main(String[] args) {

	Display display = new Display();
	final Shell shell = new Shell(display, SWT.SHELL_TRIM | SWT.DOUBLE_BUFFERED);
	shell.setText("Embedding objects in text");
	final Image[] images = new Image[3];
	final Rectangle[] rects = { new Rectangle(0, 0, 32, 32), new Rectangle(0, 0, 20, 40),
			new Rectangle(0, 0, 40, 20) };
	int[] colors  = {SWT.COLOR_BLUE, SWT.COLOR_MAGENTA, SWT.COLOR_GREEN};
	for (int i = 0; i < images.length; i++) {
		final int index = i;

		ImageGcDrawer imageGcDrawer = (gc, imageWidth, imageHeight) -> {
			gc.setBackground(display.getSystemColor(colors[index]));
			gc.fillRectangle(rects[index]);
		};
		images[i] = new Image(display, imageGcDrawer, rects[i].width, rects[i].height);
	}

	final Button button = new Button(shell, SWT.PUSH);
	button.setText("Button");
	button.pack();
	String text = "Here is some text with a blue image \uFFFC, a magenta image \uFFFC, a green image \uFFFC, and a button: \uFFFC.";
	final int[] imageOffsets = {36, 55, 72};
	final TextLayout layout = new TextLayout(display);
	layout.setText(text);
	for (int i = 0; i < images.length; i++) {
		TextStyle imageStyle = new TextStyle(null, null, null);
		imageStyle.metrics = new GlyphMetrics(rects[i].height, 0, rects[i].width);
		layout.setStyle(imageStyle, imageOffsets[i], imageOffsets[i]);
	}
	Rectangle bounds = button.getBounds();
	TextStyle buttonStyle = new TextStyle(null, null, null);
	buttonStyle.metrics = new GlyphMetrics(bounds.height, 0, bounds.width);
	final int buttonOffset = text.length() - 2;
	layout.setStyle(buttonStyle, buttonOffset, buttonOffset);

	shell.addListener(SWT.Paint, event -> {
		GC gc = event.gc;
		Point margin = new Point(10, 10);
		layout.setWidth(shell.getClientArea().width - 2 * margin.x);
		layout.draw(event.gc, margin.x, margin.y);
		for (int i = 0; i < images.length; i++) {
			int offset = imageOffsets[i];
			int lineIndex1 = layout.getLineIndex(offset);
			FontMetrics lineMetrics1 = layout.getLineMetrics(lineIndex1);
			Point point1 = layout.getLocation(offset, false);
			GlyphMetrics glyphMetrics1 = layout.getStyle(offset).metrics;
			gc.drawImage(images[i], point1.x + margin.x, point1.y + margin.y + lineMetrics1.getAscent() - glyphMetrics1.ascent);
		}
		int lineIndex2 = layout.getLineIndex(buttonOffset);
		FontMetrics lineMetrics2 = layout.getLineMetrics(lineIndex2);
		Point point2 = layout.getLocation(buttonOffset, false);
		GlyphMetrics glyphMetrics2 = layout.getStyle(buttonOffset).metrics;
		button.setLocation(point2.x + margin.x, point2.y + margin.y + lineMetrics2.getAscent() - glyphMetrics2.ascent);
	});

	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	layout.dispose();
	for (Image image : images) {
		image.dispose();
	}
	display.dispose();
}
}