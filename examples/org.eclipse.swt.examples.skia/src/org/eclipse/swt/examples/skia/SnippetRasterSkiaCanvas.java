package org.eclipse.swt.examples.skia;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;

public class SnippetRasterSkiaCanvas {

	final static boolean useSkia = true;

	final static int RECTANGLES_PER_LINE = 200;

	static class RecDraw{

		public RecDraw(int xPos, int yPos, Color c) {
			super();
			this.xPos = xPos;
			this.yPos = yPos;
			this.c = c;
		}
		int xPos ,yPos;
		Color c;

	}

	static RecDraw[][] recDraws = new RecDraw[RECTANGLES_PER_LINE][RECTANGLES_PER_LINE];
	private static int style = useSkia ? SWT.SKIA : SWT.NONE;
	int width = 2;

	public static void main(String[] args) {

		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setText("Snippet Canvas");
		final Canvas c = new Canvas(shell,  SWT.DOUBLE_BUFFERED | style);

		for( int x = 0 ; x < RECTANGLES_PER_LINE ; x++ ) {
			for(int y = 0 ; y < RECTANGLES_PER_LINE ; y++) {

				recDraws[x][y] = new RecDraw( x*2,y*2,Display.getDefault().getSystemColor((x+y )% 16 ));

			}
		}

		c.setSize(100, 100);

		shell.addListener(SWT.Resize, e -> onResize(e, c));
		c.addListener(SWT.Paint, SnippetRasterSkiaCanvas::onPaint);
		c.addListener(SWT.Paint, SnippetRasterSkiaCanvas::onPaint2);

		shell.setSize(1000, RECTANGLES_PER_LINE*3+80);


		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				c.redraw();
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

		//		surface.getCanvas().clear(0xFFFFFFFF);

		final Point size = s.getSize();

		long currentPosTime = System.currentTimeMillis() - startTime;

		currentPosTime = currentPosTime % 10000;

		final double position = (double) currentPosTime / (double) 10000;

		final int shift = (int) (position * size.x);
		final int shiftDown = 20;

		for( int x = 0 ; x < RECTANGLES_PER_LINE ; x++ ) {
			for(int y = 0 ; y < RECTANGLES_PER_LINE ; y++) {

				final var rec = recDraws[x][y];
				e.gc.setForeground(rec.c);
				e.gc.drawRectangle(shift + rec.xPos ,shiftDown + rec.yPos,  2,2 );

			}
		}


		//		int colorAsRGB = 0xFF42FFF4;
		//		int colorRed = 0xFFFF0000;
		//		int colorGreen = 0xFF00FF00;
		//		int colorBlue = 0xFF0000FF;
		//
		//		e.gc.setForeground(s.getDisplay().getSystemColor(SWT.COLOR_RED));


	}

	private static void onPaint2(Event e) {

		if (printFrameRate) {

			if (System.currentTimeMillis() - lastFrame > 1000) {
				//				System.out.println("Frames: " + frames);
				framesToDraw = frames;


				frames = 0;
				lastFrame = System.currentTimeMillis();
			}
			frames++;

			e.gc.drawText("Frames: " + framesToDraw, 10,10);

		}


	}



	private static void onResize(Event e, Canvas c) {

		final var ca = c.getShell().getClientArea();

		c.setSize(ca.width, ca.height);

	}

}
