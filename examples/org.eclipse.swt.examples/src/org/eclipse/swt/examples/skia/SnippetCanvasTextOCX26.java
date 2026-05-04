package org.eclipse.swt.examples.skia;

import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;

public class SnippetCanvasTextOCX26 {
	final static boolean useSkia = true;

	final static int style = SWT.DOUBLE_BUFFERED | (useSkia ? SWT.SKIA : SWT.NONE);
	final static int LETTERS_PER_LINE = 2000;
	final static int LINES = 60;
	final static int SHIFT_LEFT = 2000;

	final static int LEFT_START = 50;
	static String[] text = new String[LINES];

	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setText("Snippet Canvas");
		// here you can switch between Canvas SkiaRasterCanvas and SkiaCanvas
		final Canvas c = new Canvas(shell, style );
		c.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
		c.setForeground(display.getSystemColor(SWT.COLOR_WHITE));

		for (int j = 0; j < LINES; j++) {
			text[j] = generateText(LETTERS_PER_LINE);
		}

		c.setSize(100, 100);

		shell.addListener(SWT.Resize, e -> onResize(e, c));
		c.addListener(SWT.Paint, SnippetCanvasTextOCX26::onPaint);
		c.addListener(SWT.Paint, SnippetCanvasTextOCX26::onPaint2);

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

		Font f = e.display.getSystemFont();

		var fd = f.getFontData()[0];

		fd.setHeight(250);

		e.gc.setAlpha(200);
		var fontBefore = e.gc.getFont();
		var newFont = new Font(e.display, fd);
		e.gc.setFont(newFont);
		e.gc.setForeground(e.display.getSystemColor(SWT.COLOR_GREEN));
		e.gc.drawText("OCX 2026" ,LEFT_START,50,true  );

		var ocxSize = e.gc.textExtent("OCX 2026");

		newFont.dispose();

		fd.setHeight(50);
		newFont = new Font(e.display, fd);
		e.gc.setFont(newFont);

		if(useSkia) {
			e.gc.drawText("GPU Accelerated Drawing With Skia SWT" ,LEFT_START,50 + ocxSize.y + 5,true  );
		} else {
			e.gc.drawText("CPU Drawing With Classical SWT" ,LEFT_START,50 + ocxSize.y + 5,true  );
		}
		newFont.dispose();

		e.gc.setFont(fontBefore);
		e.gc.setAlpha(255);
		e.gc.setForeground(e.display.getSystemColor(SWT.COLOR_WHITE));

		final var size = s.getSize();

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

			var fontBefore = e.display.getSystemFont();
			var fd = fontBefore.getFontData()[0];
			fd.setHeight(50);
			var newFont = new Font(e.display, fd);
			e.gc.setFont(newFont);

			e.gc.drawText("Frames: " + framesToDraw, 10, 10,true);

			newFont.dispose();

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
