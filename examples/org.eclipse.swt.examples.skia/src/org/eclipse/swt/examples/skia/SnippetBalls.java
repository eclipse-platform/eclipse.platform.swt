package org.eclipse.swt.examples.skia;

import java.util.LinkedList;

/*
 * Fill a shape with a predefined pattern
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.1
 */
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class SnippetBalls {

	final static boolean USE_SKIA = true;
	final static int BALL_COUNT = 3;
	
	static int skiaStyle = USE_SKIA ? SWT.SKIA : SWT.NONE;
	final static int WIDTH = 100;
	final static int HEIGHT = 100;
	static final int TIMER = 30;

	static Composite parent;
	static Canvas canvas;
	static boolean animate = true;

	public static void main(String[] args) {

		Display display = new Display();
		bc = new BallCollection[BALL_COUNT];

		Shell shell = new Shell(display);
		shell.setText("SnippetBalls");
		shell.setLayout(new FillLayout());
		parent = shell;
		Canvas c = new Canvas(shell, SWT.DOUBLE_BUFFERED | skiaStyle);
		canvas = c;

		startAnimationTimer();
		c.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
		c.addListener(SWT.Paint, event -> {
			Rectangle r = ((Composite) event.widget).getClientArea();
			GC gc1 = event.gc;

			paint(gc1, r.width, r.height);

		});
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		animate = false;

		display.dispose();
	}

	static BallCollection[] bc;

	/**
	 * This inner class serves as a container for the data needed to display a
	 * collection of balls.
	 */
	static class BallCollection {
		float x, y; // position of ball
		float incX, incY; // values by which to move the ball
		int ball_size; // size (diameter) of the ball
		int capacity; // number of balls in the collection
		LinkedList<Float> prevx, prevy; // collection of previous x and y positions
		// of ball
		Color[] colors; // colors used for this ball collection

		public BallCollection(float x, float y, float incX, float incY, int ball_size, int capacity, Color[] colors) {
			this.x = x;
			this.y = y;
			this.incX = incX;
			this.incY = incY;
			this.ball_size = ball_size;
			this.capacity = capacity;
			prevx = new LinkedList<>();
			prevy = new LinkedList<>();
			this.colors = colors;
		}
	}

	/**
	 * Starts the animation if the animate flag is set.
	 */
	static void startAnimationTimer() {
		final Display display = parent.getDisplay();
		display.timerExec(TIMER, new Runnable() {
			@Override
			public void run() {
				if (canvas.isDisposed())
					return;
				int timeout = TIMER;
				if (animate) {
					Rectangle rect = canvas.getClientArea();
					next(rect.width, rect.height);
					canvas.redraw();
					canvas.update();
					display.timerExec(timeout, this);
				}
			}
		});
	}

	public int getInitialAnimationTime() {
		return 10;
	}

	public static void next(int width, int height) {
		for (int i = 0; i < bc.length; i++) {
			if (bc[i] == null)
				return;
			if (bc[i].prevx.isEmpty()) {
				bc[i].prevx.addLast(Float.valueOf(bc[i].x));
				bc[i].prevy.addLast(Float.valueOf(bc[i].y));
			} else if (bc[i].prevx.size() == bc[i].capacity) {
				bc[i].prevx.removeFirst();
				bc[i].prevy.removeFirst();
			}

			bc[i].x += bc[i].incX;
			bc[i].y += bc[i].incY;

			float random = (float) Math.random();

			// right
			if (bc[i].x + bc[i].ball_size > width) {
				bc[i].x = width - bc[i].ball_size;
				bc[i].incX = random * -width / 16 - 1;
			}
			// left
			if (bc[i].x < 0) {
				bc[i].x = 0;
				bc[i].incX = random * width / 16 + 1;
			}
			// bottom
			if (bc[i].y + bc[i].ball_size > height) {
				bc[i].y = (height - bc[i].ball_size) - 2;
				bc[i].incY = random * -height / 16 - 1;
			}
			// top
			if (bc[i].y < 0) {
				bc[i].y = 0;
				bc[i].incY = random * height / 16 + 1;
			}
			bc[i].prevx.addLast(Float.valueOf(bc[i].x));
			bc[i].prevy.addLast(Float.valueOf(bc[i].y));
		}
	}

	public static void paint(GC gc, int width, int height) {
		Device device = gc.getDevice();
		gc.setBackground(device.getSystemColor(SWT.COLOR_WHITE));
		gc.fillRectangle(0, 0, width, height);

		if (bc[0] == null) {

			for (int i = 0; i < bc.length; i++) {
				bc[i] = new BallCollection((float) (Math.random() * width), (float) (Math.random() * height),
						(float) (Math.random() * 10 - 5), (float) (Math.random() * 10 - 5), (int) (Math.random() * 30 + 20),
						50, new Color[] { device.getSystemColor(SWT.COLOR_GREEN) });
			}
			
//			bc[0] = new BallCollection((float) (Math.random() * width), (float) (Math.random() * height),
//					(float) (Math.random() * 10 - 5), (float) (Math.random() * 10 - 5), (int) (Math.random() * 30 + 20),
//					50, new Color[] { device.getSystemColor(SWT.COLOR_GREEN) });

		}

		for (BallCollection ballCollection : bc) {
			for (int i = 0; i < ballCollection.prevx.size(); i++) {
				Transform transform = new Transform(device);
				transform.translate(ballCollection.prevx.get(ballCollection.prevx.size() - (i + 1)).floatValue(),
						ballCollection.prevy.get(ballCollection.prevy.size() - (i + 1)).floatValue());
				gc.setTransform(transform);
				transform.dispose();

				Path path = new Path(device);
				path.addArc(0, 0, ballCollection.ball_size, ballCollection.ball_size, 0, 360);
				gc.setAlpha(255 - i * (255 / ballCollection.capacity));
				gc.setBackground(ballCollection.colors[0]);
				gc.fillPath(path);
				gc.drawPath(path);
				path.dispose();
			}
		}
	}
}