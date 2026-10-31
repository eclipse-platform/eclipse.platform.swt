package org.eclipse.swt.examples.skia;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class SnippetDrawImageThenChange {

	private static final boolean USE_SKIA = true;

	public static void main(String[] args) {

		int canvasStyle = SWT.BORDER | (USE_SKIA ? SWT.SKIA : SWT.NONE);

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Image Cache Test");
		shell.setLayout(new RowLayout(SWT.HORIZONTAL));

		int width = 120, height = 80;
		// Erzeuge das Image
		Image image = new Image(display, width, height);
		GC gc = new GC(image);
		gc.setBackground(display.getSystemColor(SWT.COLOR_YELLOW));
		gc.fillRectangle(0, 0, width, height);
		gc.setForeground(display.getSystemColor(SWT.COLOR_BLUE));
		gc.drawText("Original", 10, 30);
		gc.dispose();

		Button b = new Button(shell, SWT.PUSH);
		b.setText("Change Image");
		Canvas canvas1 = new Canvas(shell, canvasStyle);
		canvas1.setSize(width, height);
		canvas1.addPaintListener(e -> {
			e.gc.drawImage(image, 0, 0);
		});

		Canvas canvas2 = new Canvas(shell, canvasStyle);
		canvas2.setSize(width, height);
		canvas2.addPaintListener(e -> {
			e.gc.drawImage(image, 0, 0);
		});

		b.addListener(SWT.Selection, e -> {

			// Ändere das Image mit neuem GC
			GC gc2 = new GC(image);
			gc2.setForeground(display.getSystemColor(SWT.COLOR_RED));
			gc2.setLineWidth(4);
			gc2.drawLine(0, 0, width, height);
			gc2.drawText("Geändert", 10, 50);
			gc2.dispose();
			canvas1.redraw();
			canvas2.redraw();

		});

		// Image-Größe

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		image.dispose();
		display.dispose();
	}
}