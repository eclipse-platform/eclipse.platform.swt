package org.eclipse.swt.examples.skia;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class SnippetCanvasAdvancedCompare {

	// working variable for the snippet
	private static boolean skiaCurrentlyActive;

	public static void main(String[] args) {
		final Display display = new Display();
		final Shell s = new Shell(display);
		s.setLayout(new FillLayout());

		final ScrolledComposite sc = new ScrolledComposite(s, SWT.V_SCROLL | SWT.H_SCROLL);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);

		final Composite shell = new Composite(sc, SWT.NONE);
		sc.setContent(shell);

		shell.setLayout(new GridLayout(2, true));

		text(shell, "Zeichne Text mit verschiedenen Farben");

		coloredTextCanvas(shell);

		resetCanvasConfiguration();

		activateSkiaRaster();
		coloredTextCanvas(shell);
		resetCanvasConfiguration();

		text(shell, "Zeichne Text AntiAlias-Modi");

		coloredTextNoAACanvas(shell);

		activateSkiaRaster();
		coloredTextNoAACanvas(shell);
		resetCanvasConfiguration();

		text(shell, "Zeichne rechteckige Linien mit verschiedenen Strichstärken");

		lineAttributesCanvas(shell);
		activateSkiaRaster();
		lineAttributesCanvas(shell);
		resetCanvasConfiguration();

		text(shell, " Zeichne mit einem Path");

		pathCanvas(shell);

		activateSkiaRaster();
		pathCanvas(shell);
		resetCanvasConfiguration();

		text(shell, " XOR-Mode");

		xorMode(shell);
		activateSkiaRaster();
		xorMode(shell);
		resetCanvasConfiguration();

		text(shell, " Geänderte Transformationsmatrix");

		transformCanvas(shell);
		activateSkiaRaster();
		transformCanvas(shell);
		resetCanvasConfiguration();

		text(shell, " Benutze Region");

		regionCanvas(shell);
		activateSkiaRaster();
		regionCanvas(shell);
		resetCanvasConfiguration();

		text(shell, " Zeichne mit einem Muster");

		patternCanvas(shell);

		activateSkiaRaster();
		patternCanvas(shell);
		resetCanvasConfiguration();

		text(shell, " Anti-Aliasing für Grafik");

		antiAliasCanvas(shell);
		activateSkiaRaster();
		antiAliasCanvas(shell);
		resetCanvasConfiguration();

		text(shell, " Zeichne Bilder mit verschiedenen Alpha-Werten");

		alphaCanvas(shell);
		activateSkiaRaster();
		alphaCanvas(shell);
		resetCanvasConfiguration();

		text(shell, "Clipping Rectangle");

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

		final var size = shell.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		sc.setMinSize(size);

		s.open();
		while (!s.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}

	private static void resetCanvasConfiguration() {
		skiaCurrentlyActive = false;
	}

	private static void activateSkiaRaster() {
		skiaCurrentlyActive = true;
	}

	private static void coloredTextNoAACanvas(Composite shell) {
		final Canvas coloredTextCanvas = createCanvas(shell);
		coloredTextCanvas.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_YELLOW));
		coloredTextCanvas.addPaintListener(e -> {
			final GC gc = e.gc;

			final boolean advanced = e.gc.getAdvanced();
			final int textAA = e.gc.getTextAntialias();

			final var f = shell.getDisplay().getSystemFont();

			final var fd = f.getFontData()[0];

			fd.height = 20;

			final Font nf = new Font(shell.getDisplay(), new FontData[] { fd });

			e.gc.setFont(nf);

			e.gc.setAdvanced(true);
			e.gc.setTextAntialias(SWT.OFF);

			final Color blue = shell.getDisplay().getSystemColor(SWT.COLOR_BLUE);
			gc.setForeground(blue);
			gc.drawText("No AntiAlias Text", 10, 10);

			final Color red = shell.getDisplay().getSystemColor(SWT.COLOR_RED);
			gc.setForeground(red);
			e.gc.setTextAntialias(SWT.ON);
			gc.drawText("AntiAlias Text", 10, 70);

			final Color black = shell.getDisplay().getSystemColor(SWT.COLOR_BLACK);
			gc.setForeground(black);
			e.gc.setTextAntialias(SWT.DEFAULT);
			gc.drawText("Default Text", 10, 130);

			gc.setAdvanced(advanced);
			gc.setTextAntialias(textAA);

		});

		setGridData(coloredTextCanvas, 200);

	}

	private static void clipCanvasPath(Composite shell) {
		final Canvas c = createCanvas(shell);
		final var display = shell.getDisplay();

		c.addPaintListener(e -> {
			final var p = c.getSize();
			final int width = p.x;
			final int height = p.y;

			final Path pa = new Path(display);
			pa.addArc(0, 0, 100, 100, 90, 200);

			e.gc.setClipping(pa);
			e.gc.setBackground(display.getSystemColor(SWT.COLOR_CYAN));

			e.gc.fillRectangle(0, 0, width, height);
		});

		setGridData(c, 200);

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
			e.gc.setClipping(20, 20, width - 40, height - 40);
			e.gc.setBackground(display.getSystemColor(SWT.COLOR_CYAN));

			e.gc.fillPolygon(new int[] { 0, 0, width, 0, width / 2, height });
		});

		setGridData(c, 200);

	}

	private static void alphaCanvas(Composite shell) {
		final Canvas alphaCanvas = createCanvas(shell);
		final var display = shell.getDisplay();
		final Image image = new Image(display, 100, 50);
		final GC gcImage = new GC(image);
		gcImage.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
		gcImage.fillRectangle(0, 0, 100, 50);
		gcImage.dispose();

		alphaCanvas.addPaintListener(e -> {
			final GC gc = e.gc;
			gc.setAlpha(128);
			gc.drawImage(image, 10, 10);
		});

		setGridData(alphaCanvas);

	}

	private static void antiAliasCanvas(Composite shell) {
		final Canvas antiAliasCanvas = createCanvas(shell);
		antiAliasCanvas.addPaintListener(e -> {
			final GC gc = e.gc;
			gc.setAntialias(SWT.ON);
			gc.drawOval(10, 10, 100, 50);
		});

		setGridData(antiAliasCanvas);

	}

	private static void patternCanvas(Composite shell) {

		final Canvas patternCanvas = createCanvas(shell);
		patternCanvas.addPaintListener(e -> {
			final GC gc = e.gc;

			final Pattern pattern = new Pattern(shell.getDisplay(), 10, 10, 100, 50, new Color(new RGB(255, 0, 0)), 100,
					new Color(new RGB(0, 0, 255)), 255);
			gc.setBackgroundPattern(pattern);
			gc.fillRectangle(10, 10, 100, 50);
			pattern.dispose();
		});

		setGridData(patternCanvas);

	}

	private static void regionCanvas(Composite shell) {
		final Canvas regionCanvas = createCanvas(shell);
		regionCanvas.addPaintListener(e -> {
			final GC gc = e.gc;
			final Region region = new Region();
			region.add(new Rectangle(10, 10, 100, 50));
			region.add(new Rectangle(60, 30, 100, 50));
			gc.setClipping(region);
			gc.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_GREEN));
			gc.fillRectangle(0, 0, 200, 100);
			region.dispose();
		});

		setGridData(regionCanvas);

	}

	private static void transformCanvas(Composite shell) {
		final Canvas transformCanvas = createCanvas(shell);
		transformCanvas.addPaintListener(e -> {
			final GC gc = e.gc;
			final Transform transform = new Transform(shell.getDisplay());
			transform.translate(50, 50);
			transform.rotate(45);
			gc.setTransform(transform);
			gc.drawRectangle(-25, -25, 100, 50);
			transform.dispose();
		});

		setGridData(transformCanvas);

	}

	private static void xorMode(Composite shell) {

		final var dis = shell.getDisplay();

		final Canvas xorModeCanvas = createCanvas(shell);
		xorModeCanvas.setBackground(dis.getSystemColor(SWT.COLOR_BLACK));
		xorModeCanvas.addPaintListener(e -> {
			final GC gc = e.gc;
			gc.setXORMode(true);
			gc.setForeground(shell.getDisplay().getSystemColor(SWT.COLOR_RED));
			gc.drawRectangle(10, 10, 100, 50);
			gc.drawRectangle(30, 30, 60, 30);
			gc.setXORMode(false);
		});

		setGridData(xorModeCanvas);

	}

	private static Canvas createCanvas(Composite shell) {
		int style = skiaCurrentlyActive ? SWT.SKIA : SWT.NONE;
		return new Canvas(shell, SWT.BORDER | style);
	}

	private static void pathCanvas(Composite shell) {
		final Canvas pathCanvas = createCanvas(shell);
		pathCanvas.addPaintListener(e -> {
			final GC gc = e.gc;
			final Path path = new Path(shell.getDisplay());
			path.moveTo(10, 10);
			path.lineTo(110, 60);
			path.lineTo(60, 110);
			path.close();
			gc.drawPath(path);
			path.dispose();
		});

		setGridData(pathCanvas);

	}

	private static void setGridData(Control c) {
		setGridData(c, 150);
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

	private static void lineAttributesCanvas(Composite shell) {
		final Canvas lineAttributesCanvas = createCanvas(shell);
		lineAttributesCanvas.addPaintListener(e -> {
			final GC gc = e.gc;
			gc.setLineWidth(5);
			gc.setLineJoin(SWT.JOIN_ROUND);
			gc.setLineCap(SWT.CAP_ROUND);
			gc.drawRectangle(10, 10, 100, 50);
		});

		setGridData(lineAttributesCanvas);

	}

	private static void coloredTextCanvas(Composite shell) {
		final Canvas coloredTextCanvas = createCanvas(shell);
		coloredTextCanvas.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_YELLOW));
		coloredTextCanvas.addPaintListener(e -> {
			final GC gc = e.gc;
			final Color blue = shell.getDisplay().getSystemColor(SWT.COLOR_BLUE);
			gc.setForeground(blue);
			gc.drawText("Blauer Text", 10, 10);
		});

		setGridData(coloredTextCanvas);

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