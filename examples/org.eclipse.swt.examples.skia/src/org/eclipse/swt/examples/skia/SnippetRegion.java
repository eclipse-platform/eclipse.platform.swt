package org.eclipse.swt.examples.skia;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;

public class SnippetRegion {

    final static Region r = new Region();

    public static void main(String[] args) {

	for (int re = 0; re < 30; re++) {
	    r.add(new Rectangle(1 + 3 * re, 2 + 3 * re, 40, 40));
	}
	r.subtract(15, 15, 20, 20);

	final Region inters = new Region();
	inters.add(new Rectangle(20, 20, 60, 60));
	r.intersect(inters);


	r.translate(15, 15);

	r.add(toArray(generateCirclePoints(10, 30, 0)));

	final Display display = Display.getDefault();
	final Shell shell = new Shell(display);
	shell.setText("Snippet Canvas");
	// here you can switch between Canvas SkiaRasterCanvas and SkiaCanvas

	setupCanvas(shell, SWT.DOUBLE_BUFFERED | SWT.SKIA, new Point(0, 0));
	setupCanvas(shell, SWT.DOUBLE_BUFFERED, new Point(100, 100));

	shell.setSize(1000, 1000);

	shell.open();
	while (!shell.isDisposed()) {
	    display.readAndDispatch();
	}
	display.dispose();

    }

    public static int[] toArray(int[][] points) {
	final int l = points.length;
	final int[] ret = new int[l *2];
	for( int i = 0 ; i < l ; i++) {
	    ret[2*i] = points[i][0];
	    ret[2*i+1] = points[i][1];
	}

	return ret;

    }


    public static int[][] generateCirclePoints(int numPoints, int radius, int translate) {
	final int[][] points = new int[numPoints][2];
	final double angleIncrement = 2 * Math.PI / numPoints;

	for (int i = 0; i < numPoints; i++) {
	    final double angle = i * angleIncrement;
	    final int x = (int) (radius * Math.cos(angle)) + translate;
	    final int y = (int) (radius * Math.sin(angle)) + translate;
	    points[i][0] = x;
	    points[i][1] = y;
	}

	return points;
    }

    private static void setupCanvas(Shell shell, int style, Point loc) {

	final Canvas c1 = new Canvas(shell, style);
	c1.setSize(100, 100);
	c1.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_CYAN));
	c1.addListener(SWT.Paint, SnippetRegion::onPaint);
	c1.setLocation(loc);

    }

    private static void onPaint(Event e) {

	// surface.getCanvas().clear(0xFFFFFFFF);

	e.gc.setClipping(r);

	e.gc.setBackground(e.display.getSystemColor(SWT.COLOR_RED));
	e.gc.fillRectangle(0, 0, 100, 100);

	// int colorAsRGB = 0xFF42FFF4;
	// int colorRed = 0xFFFF0000;
	// int colorGreen = 0xFF00FF00;
	// int colorBlue = 0xFF0000FF;
	//
	// e.gc.setForeground(s.getDisplay().getSystemColor(SWT.COLOR_RED));

    }

}
