package org.eclipse.swt.examples.skia;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class SnippetClippingRectangle {

	
	private static boolean skiaEnabled = true;
	public static void main(String[] args) {
		final Display display = new Display();
		final Shell s = new Shell(display);
		s.setLayout(new FillLayout());

		final Composite shell = new Composite(s, SWT.FILL | SWT.V_SCROLL);

		shell.setSize(1000, 2000); // Erhöhte Höhe für mehr Canvas
		shell.setLayout(new GridLayout(2, true));

		text(shell, "Clipping Rectangle");

		resetCanvasConfiguration();
		clipRectangleCanvas(shell);
		activateSkiaRaster();
		clipRectangleCanvas(shell);
		resetCanvasConfiguration();

		text(shell, "Clipping Region");

		clipCanvasRegion(shell);
		activateSkiaRaster();
		clipCanvasRegion(shell);
		resetCanvasConfiguration();
		
		text(shell, "Clipping Path");
		
		clipCanvasPath(shell);
		activateSkiaRaster();
		clipCanvasPath(shell);
		resetCanvasConfiguration();
		
		
		s.open();
		while (!s.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}

	private static void clipCanvasPath(Composite shell) {
		final Canvas c = createCanvas(shell);
		final var display = shell.getDisplay();

		c.addPaintListener(e -> {
			final var p = c.getSize();
			final int width = p.x;
			final int height = p.y;

			final Path r = new Path(display);
			
			r.addArc(width / 4, height / 4, width / 2, height / 2, 0, 360);
			
			e.gc.setClipping(r);
			e.gc.setBackground(display.getSystemColor(SWT.COLOR_CYAN));

			e.gc.fillRectangle(0, 0, width, height);
		});

		setGridData(c, 200);
	}

	private static void resetCanvasConfiguration() {
		skiaEnabled = false;
	}

	private static void activateSkiaRaster() {
		skiaEnabled = true;
	}

	private static void clipCanvasRegion(Composite shell) {
		final Canvas c = createCanvas(shell);
		final var display = shell.getDisplay();

		c.addPaintListener(e -> {
			final var p = c.getSize();
			final int width = p.x;
			final int height = p.y;

			final Region r = new Region(display);
			r.add(new int[] { width / 2, 0, 0, height / 2, width / 2, height, width, height / 2 });
			e.gc.setClipping(r);
			e.gc.setBackground(display.getSystemColor(SWT.COLOR_CYAN));

			e.gc.fillPolygon(new int[] { 0, 0, width, 0, width / 2, height });
		});

		setGridData(c, 200);
	}

	private static void clipRectangleCanvas(Composite shell) {
		final Canvas c = createCanvas(shell);
		final var display = shell.getDisplay();

		c.addPaintListener(e -> {
			final var p = c.getSize();
			final int width = p.x;
			final int height = p.y;
			e.gc.setClipping(20, 20, width /2, height /2 );
			e.gc.setBackground(display.getSystemColor(SWT.COLOR_CYAN));

			e.gc.fillPolygon(new int[] { 0, 0, width, 0, width / 2, height });
		});

		setGridData(c, 200);
	}

	private static Canvas createCanvas(Composite shell) {
		int style = skiaEnabled ? SWT.SKIA : SWT.NONE;
		return new Canvas(shell, SWT.BORDER | style);
	}


	private static void setGridData(Control c, int minHeight) {
		final var g = new GridData();
		g.grabExcessHorizontalSpace = true;
		g.grabExcessVerticalSpace = true;
		g.horizontalAlignment = GridData.FILL;
		g.verticalAlignment = GridData.FILL;
		g.minimumHeight = minHeight;
		c.setLayoutData(g);
	}

	private static void text(Composite shell, String string) {
		final var l = new Label(shell, SWT.BORDER);
		l.setText(string);
		final var g = new GridData();
		g.horizontalSpan = 2;
		g.grabExcessHorizontalSpace = true;
		g.horizontalAlignment = GridData.FILL;
		l.setLayoutData(g);
	}
}
