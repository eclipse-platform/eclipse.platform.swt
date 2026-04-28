package org.eclipse.swt.examples.skia;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class SnippetSkiaFonts {

	final static int stepSize = 60;

	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display, SWT.V_SCROLL |  SWT.SHELL_TRIM);
		shell.setText("Available Fonts");
		shell.setSize(800, 600);
		shell.setLayout(new GridLayout(2,true));

		final Canvas canvas = new Canvas(shell,  SWT.BORDER);
		final Canvas skiaCanvas = new Canvas(shell, SWT.BORDER | SWT.SKIA);

		setGridData(canvas, 300);
		setGridData(skiaCanvas, 300);


		final var pl = (PaintListener) event -> {
			final GC gc = event.gc;
			final FontData[] fontDataArrayScalable = display.getFontList(null, true);
			final FontData[] fontDataArrayNotScalable = display.getFontList(null, false);
			int y = 10 - shell.getVerticalBar().getSelection()   ;
			
			// Courier is a bitmap font on windows, skia does not support it...
			final Font font1 = new Font(display, "Courier", 20, SWT.NONE);
			y = drawFont(gc,font1,"Courier",y);
			
			
			for (final FontData fd : fontDataArrayScalable) {

				fd.setHeight(20);
				if( y < -stepSize || y > canvas.getSize().y ) {
					y += stepSize;
					continue;
				}

				final Font font = new Font(display, fd);
				y = drawFont(gc,font,"'" + fd.getName() +"'",y);
				
			}

			for (final FontData fd : fontDataArrayNotScalable) {

				if( y < -stepSize || y > canvas.getSize().y ) {
					y += stepSize;
					continue;
				}

				fd.setHeight(20);
				final Font font = new Font(display, fd);
				y = drawFont(gc,font,"'" + fd.getName() +"'",y);
			}

			shell.getVerticalBar().setMaximum(y + shell.getVerticalBar().getSelection());
		};

		canvas.addPaintListener(pl );
		skiaCanvas.addPaintListener(pl);


		shell.getVerticalBar().addListener(SWT.Selection, e -> {

			canvas.redraw();
			skiaCanvas.redraw();

		});



		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	private static int drawFont(GC gc, Font font1, String fontName1, int y) {
		
		gc.setFont(font1);
		gc.drawText(fontName1, 20, y);
		font1.dispose();  // Frees system resources
		y += stepSize;
		
		return y;
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