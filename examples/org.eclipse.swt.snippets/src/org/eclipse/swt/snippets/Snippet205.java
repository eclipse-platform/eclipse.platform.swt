/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;


public class Snippet205 {
	
public static void main(String[] args) {
	Display display = new Display();
	final Shell shell = new Shell(display, SWT.SHELL_TRIM | SWT.DOUBLE_BUFFERED);
	final Image[] images = {new Image(display, 32, 32), new Image(display, 20, 40), new Image(display, 40, 20)};
	int[] colors  = {SWT.COLOR_BLUE, SWT.COLOR_MAGENTA, SWT.COLOR_GREEN};
	for (int i = 0; i < images.length; i++) {
		GC gc = new GC(images[i]);
		gc.setBackground(display.getSystemColor(colors[i]));
		gc.fillRectangle(images[i].getBounds());
		gc.dispose();
	}
	
	String text = "Embedded Images test, the first image is blue: \uFFFC, the second one if magenta: \uFFFC, and last one green: \uFFFC.";
	final int[] offsets = {47, 77, 100};
	final TextLayout layout = new TextLayout(display);
	layout.setText(text);
	for (int i = 0; i < images.length; i++) {
		Rectangle bounds = images[i].getBounds();
		TextStyle imageStyle = new TextStyle (null, null, null);
		imageStyle.metrics = new GlyphMetrics (bounds.height, 0, bounds.width); 
		layout.setStyle(imageStyle, offsets[i], offsets[i]);
	}

	
	shell.addListener(SWT.Paint, new Listener() {
		public void handleEvent(Event event) {
			GC gc = event.gc;
			Point margin = new Point(10, 10);
			layout.setWidth(shell.getClientArea().width - 2 * margin.x);
			layout.draw(event.gc, margin.x, margin.y);
			for (int i = 0; i < images.length; i++) {
				int offset = offsets[i];
				int lineIndex = layout.getLineIndex(offset);
				FontMetrics lineMetrics = layout.getLineMetrics(lineIndex);
				Point point = layout.getLocation(offset, false);
				GlyphMetrics glyphMetrics = layout.getStyle(offset).metrics;
				gc.drawImage(images[i], point.x + margin.x, point.y + margin.y + lineMetrics.getAscent() - glyphMetrics.ascent);
			}
		}
	});

	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	layout.dispose();
	for (int i = 0; i < images.length; i++) {
		images[i].dispose();
	}
	display.dispose ();
}
}
