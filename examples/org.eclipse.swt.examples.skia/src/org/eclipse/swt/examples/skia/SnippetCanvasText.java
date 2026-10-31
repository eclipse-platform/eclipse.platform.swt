package org.eclipse.swt.examples.skia;

import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;

public class SnippetCanvasText {
	final static boolean useSkia = true;


	final static int style = SWT.DOUBLE_BUFFERED | (useSkia ? SWT.SKIA : SWT.NONE);
	final static int LETTERS_PER_LINE = 2000;
	final static int LINES = 60;
	final static int SHIFT_LEFT = 2000;

	static String[] text = new String[LINES];

	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setText("Snippet Canvas");
		// here you can switch between Canvas SkiaRasterCanvas and SkiaCanvas
		final Canvas c = new Canvas(shell, style );

		for (int j = 0; j < LINES; j++) {
			text[j] = generateText(LETTERS_PER_LINE);
		}

		c.setSize(100, 100);

		shell.addListener(SWT.Resize, e -> onResize(e, c));
		c.addListener(SWT.Paint, SnippetCanvasText::onPaint);
		c.addListener(SWT.Paint, SnippetCanvasText::onPaint2);

		shell.setSize(1000, LETTERS_PER_LINE * 3 + 80);

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

		final int shift = (int) (position * size.x) - SHIFT_LEFT;
		final int shiftDown = 20;


		for(int j = 0 ; j < LINES; j++) {
			e.gc.drawText(text[j], shift, shiftDown + 20 * j,true);
		}
	}

	private static void onPaint2(Event e) {

		final var s = ((Canvas) e.widget);

		if (printFrameRate) {

			if (System.currentTimeMillis() - lastFrame > 1000) {
				framesToDraw = frames;

				frames = 0;
				lastFrame = System.currentTimeMillis();
			}
			frames++;

			e.gc.drawText("Frames: " + framesToDraw, 10, 10,true);

		}

		s.redraw();

	}

	private static void onResize(Event e, Canvas c) {

		final var ca = c.getShell().getClientArea();

		c.setSize(ca.width, ca.height);

	}
	public static String generateText(int textLength) {
		final int leftLimit = 97; // letter 'a'
		final int rightLimit = 122; // letter 'z'
		final Random random = new Random();

		final String generatedString = random.ints(leftLimit, rightLimit + 1)
				.limit(textLength)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
				.toString();


		return generatedString;
	}


}
