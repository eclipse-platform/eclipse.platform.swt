package org.eclipse.swt.examples.skia;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;

public class SnippetSkijaCanvas {
	final static boolean useSkia = true;

	static final int RECTANGLES_PER_LINE = 200;
	

	static record RecDraw(int xPos, int yPos, Color c) {
	}

	static int style = SWT.FILL | SWT.DOUBLE_BUFFERED | (useSkia ? SWT.SKIA : SWT.NONE);
	private static final RecDraw[][] recDraws = new RecDraw[RECTANGLES_PER_LINE][RECTANGLES_PER_LINE];
	private static long minFrameRate = Long.MAX_VALUE;
	private static long maxFrameRate = 0;
	private static long sum = 0;
	private static double count = 0.0;
	private static double average = 0;

	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setText("Snippet 1");

		shell.setLayout(new FillLayout());
		final Canvas c = new Canvas(shell,style);


		for (int x = 0; x < RECTANGLES_PER_LINE; x++) {
			for (int y = 0; y < RECTANGLES_PER_LINE; y++) {
				recDraws[x][y] = new RecDraw(x * 2, y * 2, Display.getDefault().getSystemColor((x + y) % 16));
			}
		}

		c.setSize(100, 100);

		shell.addListener(SWT.Resize, e -> onResize(e, c));
		c.addListener(SWT.Paint, SnippetSkijaCanvas::onPaint);
		c.addListener(SWT.Paint, SnippetSkijaCanvas::onPaint2);

		shell.setSize(1000, RECTANGLES_PER_LINE * 3 + 80);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	static long startTime = System.currentTimeMillis();
	private static boolean printFrameRate = true;
	private static int frames;
	private static long lastFrame;
	private static long framesToDraw;

	private static void onPaint(Event e) {
		final var s = ((Canvas) e.widget);

		final Point size = s.getSize();
		long currentPosTime = System.currentTimeMillis() - startTime;
		currentPosTime = currentPosTime % 10000;

		final double position = (double) currentPosTime / (double) 10000;
		final int shift = (int) (position * size.x);
		final int shiftDown = 20;

		for (int x = 0; x < RECTANGLES_PER_LINE; x++) {
			for (int y = 0; y < RECTANGLES_PER_LINE; y++) {
				final var rec = recDraws[x][y];
				e.gc.setForeground(rec.c);
				e.gc.drawRectangle(shift + rec.xPos, shiftDown + rec.yPos, 2, 2);

			}
		}
	}

	private static void onPaint2(Event e) {
		final var s = ((Canvas) e.widget);

		if (printFrameRate) {
			if (System.currentTimeMillis() - lastFrame > 1000) {
				framesToDraw = frames;
				frames = 0;
				lastFrame = System.currentTimeMillis();
				if(framesToDraw != 0) {
					minFrameRate = Math.min(minFrameRate, framesToDraw);
				}
				maxFrameRate = Math.max(maxFrameRate, framesToDraw);
				sum += framesToDraw;
				count++;
				average = sum/count;
			}
			frames++;
			e.gc.drawText("Frames: min: " + minFrameRate + ", max: " + maxFrameRate
					+ " cur: " + framesToDraw
					+ " avg: " + String.format("%.1f", average) , 10, 10);
		}
		s.redraw();

		// Mac need an additional redraw call. Else the animation stops and it reacts on user input.
		e.display.timerExec(10, () -> {
			if(!s.isDisposed()) {
				s.redraw();
			}
		});
	}

	private static void onResize(Event e, Canvas c) {
		final var ca = c.getShell().getClientArea();
		c.setSize(ca.width, ca.height);
	}

}
