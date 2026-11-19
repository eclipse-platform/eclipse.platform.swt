package org.eclipse.swt.examples.skia;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.graphics.RGBA;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class SnippetPattern2 {
	

	public static void main(String[] args) {
		final Display display = new Display();
		final Shell s = new Shell(display);
		s.setLayout(new FillLayout());

		final Composite shell = new Composite(s, SWT.FILL | SWT.V_SCROLL);
		final GridLayout gridLayout = new GridLayout(2, false);
		shell.setLayout(gridLayout);

		patternCanvas(shell);

		activateSkiaRaster();
		patternCanvas(shell);
		resetCanvasConfiguration();

		s.open();
		while (!s.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}

	private static boolean skiaEnabled = false;
	private static void resetCanvasConfiguration() {
		skiaEnabled = false;
	}

	private static void activateSkiaRaster() {
		skiaEnabled = true;
	}

	private static void patternCanvas(Composite shell) {

		final Canvas patternCanvas = createCanvas(shell);
		patternCanvas.addPaintListener(e -> {
			final GC gc = e.gc;
// in the RGBA color for the pattern the alpha value will be ignored, the alpha values passed to the Pattern constructor will be used
			final Pattern pattern = new Pattern(shell.getDisplay(), 10, 10, 100, 50,
					new Color(new RGBA(255, 0, 0, 50)), 100, new Color(new RGBA(0, 0, 255, 100)), 200);
			gc.setBackgroundPattern(pattern);
			gc.fillRectangle(10, 10, 100, 50);
			pattern.dispose();
		});

		setGridData(patternCanvas);

	}

	private static Canvas createCanvas(Composite shell) {
		int style = skiaEnabled ? SWT.SKIA : SWT.NONE;
		return new Canvas(shell, SWT.BORDER | style);
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
}
