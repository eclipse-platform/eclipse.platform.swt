package org.eclipse.swt.examples.skia;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class SnippetTextExtent {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Text Extent Snippet");
		shell.setLayout(new GridLayout(2, true));

		// Create two canvases
		for (int i = 0; i < 2; i++) {
			int skiaStyle = (i == 0) ? SWT.NONE : SWT.SKIA;
			Canvas canvas = new Canvas(shell, SWT.BORDER | skiaStyle);
			canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			canvas.addListener(SWT.Paint, new Listener() {
				@Override
				public void handleEvent(Event event) {
					GC gc = event.gc;
					Rectangle rect = canvas.getClientArea();
					Font font = new Font(display, "Arial", 250, SWT.BOLD);
					gc.setFont(font);
					String text = "a";
					// Use textExtent to get size
					Point extent = gc.textExtent(text);
					int x = rect.x + (rect.width - extent.x) / 2;
					int y = rect.y + (rect.height - extent.y) / 2;

					// Print font metrics for debugging
					org.eclipse.swt.graphics.FontMetrics metrics = gc.getFontMetrics();
					System.out.println((gc.getClass().getName().contains("GCExtension") ? "[SkiaGC]" : "[SWTGC]") +
						" ascent=" + metrics.getAscent() +
						", descent=" + metrics.getDescent() +
						", leading=" + metrics.getLeading() +
						", height=" + metrics.getHeight() +
						", extentY=" + extent.y);


					boolean red = false;
					for (int i = 0; i < extent.y / 5; i = i + 5) {
						if (red) {
							gc.setBackground(display.getSystemColor(SWT.COLOR_RED));
							red = false;
						} else {
							gc.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
							red = true;
						}
						gc.fillRectangle(x, y + i * 5, extent.x, 5);
					}
					gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
					gc.drawRectangle(new Rectangle(x, y, extent.x, extent.y));
					gc.drawString(text, x, y, true);
					// Draw baseline indicator
					int baselineY = y + metrics.getAscent();
					gc.setForeground(display.getSystemColor(SWT.COLOR_BLUE));
					gc.drawLine(x, baselineY, x + extent.x, baselineY);
					font.dispose();
				}
			});
		}

		shell.setSize(600, 300);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}