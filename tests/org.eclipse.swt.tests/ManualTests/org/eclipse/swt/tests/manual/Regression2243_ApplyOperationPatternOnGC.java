package org.eclipse.swt.tests.manual;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.GCData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Introduced by https://github.com/eclipse-platform/eclipse.platform.swt/pull/2243
 */
public class Regression2243_ApplyOperationPatternOnGC {
	public static void main (String [] args) {
		Display display = new Display ();
		Shell shell = new Shell (display);
		shell.setText("GC Redraw");
		shell.setLayout(new FillLayout());
		int width = 150, height = 200;
		Image image = new Image (display, width, height);
		Image imageToDraw = new Image(display, 1, 1);

		GCData data = new GCData();
		GC gc = GC.win32_new(image, data);
		gc.setBackground(display.getSystemColor(SWT.COLOR_RED));
		gc.fillRectangle (0, 0, width, height);
		gc.drawLine (0, 0, width, height);
		gc.drawLine (0, height, width, 0);
		gc.drawText ("Default Image", 10, 10);
		gc.drawImage(imageToDraw, 0, 0);
		imageToDraw.dispose();
		final Point origin = new Point (0, 0);
		final Canvas canvas = new Canvas (shell, SWT.NONE);
		canvas.addListener (SWT.Paint, e -> {
			GC gc2 = e.gc;
			gc2.getGCData().nativeZoom = 150;
			gc2.drawImage (image, origin.x, origin.y);
			gc2.getGCData().nativeZoom = 200;
			gc2.drawImage (image, origin.x + 300, origin.y);
		});
		shell.setSize (1000, 1000);
		shell.open ();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		gc.dispose ();
		image.dispose();
		display.dispose ();
	}
}

