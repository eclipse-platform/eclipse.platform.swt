package org.eclipse.swt.graphics;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.opengl.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Canvas;

import io.github.humbleui.skija.*;

public class SnippetSkijaCanvas {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Snippet 1");

		shell.setLayout(new FillLayout());

		GLData data = new GLData();
		data.doubleBuffer = true;

		Canvas c = new SkiaCanvas(shell, SWT.FILL, data);

		c.setSize(100, 100);

		shell.addListener(SWT.Resize, e -> onResize(e, c));
		c.addListener(SWT.Paint, e -> onPaint(e));
		c.addListener(SWT.Paint, e -> onPaint2(e));


		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}


	static long startTime = System.currentTimeMillis();

	private static void onPaint(Event e) {

		System.out.println("paint...");




		var s = ((SkiaCanvas) e.widget);

		Surface surface = s.surface;

		if (surface == null)
			return;

//		surface.getCanvas().clear(0xFFFFFFFF);

		Point size = s.getSize();

		long currentPosTime = System.currentTimeMillis() - startTime;

		currentPosTime = currentPosTime % 10000;

		double position = (double) currentPosTime / (double) 10000;

		int colorAsRGB = 0xFF42FFF4;
		int colorRed = 0xFFFF0000;
		int colorGreen = 0xFF00FF00;
		int colorBlue = 0xFF0000FF;

		e.gc.setForeground(s.getDisplay().getSystemColor(SWT.COLOR_RED));

		e.gc.drawRectangle((int) (position * size.x),50,100,100);

	}

	private static void onPaint2(Event e) {
		System.out.println("paint...");




		var s = ((SkiaCanvas) e.widget);

		Surface surface = s.surface;

		if (surface == null)
			return;

//		surface.getCanvas().clear(0xFFFFFFFF);

		Point size = s.getSize();

		long currentPosTime = System.currentTimeMillis() - startTime;

		currentPosTime = currentPosTime % 10000;

		double position = (double) currentPosTime / (double) 10000;

		int colorAsRGB = 0xFF42FFF4;
		int colorRed = 0xFFFF0000;
		int colorGreen = 0xFF00FF00;
		int colorBlue = 0xFF0000FF;

		e.gc.setForeground(s.getDisplay().getSystemColor(SWT.COLOR_RED));

		e.gc.drawRectangle((int) (position * size.x),200,100,100);
	}



	private static void onResize(Event e, Canvas c) {

		var ca = c.getShell().getClientArea();

		c.setSize(ca.width, ca.height);

	}

}
