package org.eclipse.swt.examples.skia;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;

public class SnippetCanvasCompare {

	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setText("Snippet Canvas Compare");


		configureCanvas(shell, new Canvas(shell, SWT.DOUBLE_BUFFERED| SWT.H_SCROLL | SWT.BORDER), 1, "SWT Canvas");
		configureCanvas(shell, new Canvas(shell, SWT.DOUBLE_BUFFERED | SWT.H_SCROLL| SWT.SKIA | SWT.BORDER), 2, "SkiaGlCanvas");

		shell.setSize(1500, 1000);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	private static void configureCanvas(final Shell shell, final Canvas canvas, int index, String title) {
		canvas.setSize(100, 100);
//		canvas.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_YELLOW));
		shell.addListener(SWT.Resize, e -> onResize(canvas, index));
		canvas.addListener(SWT.Paint, e -> onPaint(e, title));
	}

	private static void onPaint(Event e, String title) {
		final Display d = e.widget.getDisplay();
		final GC gc = e.gc;

		var bg = gc.getBackground();

		gc.setBackground(d.getSystemColor(SWT.COLOR_YELLOW));
		gc.fillRectangle(new Rectangle(-100, -100, 3000, 3000));

		gc.setBackground(bg);


		gc.setForeground(d.getSystemColor(SWT.COLOR_RED));
		gc.drawRectangle(new Rectangle(0, 0, 100, 100));

		gc.setForeground(d.getSystemColor(SWT.COLOR_BLUE));
		gc.drawRectangle(new Rectangle(100, 0, 100, 100));

		gc.setForeground(d.getSystemColor(SWT.COLOR_GREEN));
		gc.drawRectangle(new Rectangle(0, 100, 100, 100));

		final var img = d.getSystemImage(SWT.ICON_QUESTION);
		gc.drawImage(img, 100, 100);

		gc.setForeground(d.getSystemColor(SWT.COLOR_RED));
		gc.drawLine(200, 100, 100, 200);

		gc.setForeground(d.getSystemColor(SWT.COLOR_CYAN));
		gc.drawFocus(200, 0, 100, 100);

		gc.setForeground(d.getSystemColor(SWT.COLOR_RED));
		gc.drawArc(200, 100, 100, 100, 90, 200);

		gc.setForeground(d.getSystemColor(SWT.COLOR_BLACK));
		gc.drawText(title, 100, 250);

		gc.setForeground(d.getSystemColor(SWT.COLOR_DARK_CYAN));
		gc.drawOval(0, 300, 100, 50);

		final Path p = new Path(d);
		p.addRectangle(0, 400, 100, 100);
		gc.setForeground(d.getSystemColor(SWT.COLOR_DARK_GREEN));
		gc.drawPath(p);

		gc.setForeground(d.getSystemColor(SWT.COLOR_GRAY));
		gc.drawRoundRectangle(0, 500, 100, 100, 50, 50);

		gc.setForeground(d.getSystemColor(SWT.COLOR_DARK_RED));
		gc.drawPoint(5, 600);

		final var bo = img.getBounds();
		gc.drawImage(img, 0, 0, bo.width /2 , bo.height /2 ,    20 , 600, bo.width * 2, bo.height *2 );

		gc.setForeground(d.getSystemColor(SWT.COLOR_DARK_MAGENTA));
		gc.drawText("Test\nTest2", 300, 0);
		gc.drawText("Test2\nTest3", 300, 100, false);
		gc.drawText("Transparent Text", 300,200, true);


		gc.setForeground(d.getSystemColor(SWT.COLOR_DARK_GRAY));
		gc.drawPolygon(new int[] {
				400,2, //
				500,100, //
				400,100, //
				500,2 //
		});


		gc.setForeground(d.getSystemColor(SWT.COLOR_DARK_YELLOW));
		gc.drawPolyline(new int[] {
				400,50, //
				500,100, //
				600,200

		});

		gc.setBackground(d.getSystemColor(SWT.COLOR_DARK_YELLOW));
		gc.fillArc(100, 300, 100, 100, 90, 200);

		gc.setBackground(d.getSystemColor(SWT.COLOR_DARK_GREEN));
		gc.fillGradientRectangle(100, 400, 100, 100, false);

		gc.fillOval(100, 500, 100, 50);


		final Path p2 = new Path(d);
		p2.addRectangle(100, 600, 100, 100);
		gc.fillPath(p2);

		gc.fillPolygon(new int[] {
				100,700, //
				200,800, //
				100,800, //
				200,700 //
		});

		gc.fillRectangle(new Rectangle(100, 800, 100, 100));

		gc.fillRoundRectangle(200, 800, 100, 100, 20, 20);
	}

	private static void onResize(Canvas c,  int index) {
		final var ca = c.getShell().getClientArea();
		switch(index) {
		case 1 -> c.setBounds(new Rectangle(0, 0, ca.width / 2, ca.height));
		case 2 -> c.setBounds(new Rectangle(ca.width / 2, 0, ca.width / 2, ca.height));
		default -> { /* nothing to do */ }
		}
	}

}
