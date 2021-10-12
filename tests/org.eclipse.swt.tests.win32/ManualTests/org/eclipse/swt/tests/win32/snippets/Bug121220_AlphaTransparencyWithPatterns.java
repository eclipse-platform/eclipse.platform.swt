package org.eclipse.swt.tests.win32.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Bug121220_AlphaTransparencyWithPatterns {

	public static void main(String[] args) {

		Display disp = new Display();
		Shell shell = new Shell(disp);
		shell.setSize(400, 300);
		shell.setText("Pattern with alpha");
		Image image = new Image(disp, 50, 50);
		{
			GC gc = new GC(image);
			gc.setForeground(new Color(null, 255, 0, 0));
			gc.drawLine(0, 0, 49, 49);
			gc.drawLine(0, 49, 49, 0);
			gc.setForeground(new Color(null, 0, 0, 200));
			gc.drawString("Pat", 5, 5);
			gc.dispose();
		}
		final Pattern pat = new Pattern(disp, image);
		shell.addPaintListener(e -> {

			e.gc.setBackground(new Color(null, 200, 200, 200));
			e.gc.fillRectangle(0, 0, shell.getBounds().width, shell.getBounds().height);
			e.gc.setBackground(new Color(null, 255, 0, 0));
			e.gc.fillRectangle(0, 0, 100, 100);
			e.gc.fillRectangle(100, 100, 100, 100);
			e.gc.setAlpha(100);
			e.gc.setBackgroundPattern(pat);
			e.gc.fillRectangle(50, 10, 100, 100);
			e.gc.setLineWidth(16);
			e.gc.setAlpha(150);
			e.gc.setLineCap(SWT.CAP_ROUND);
			e.gc.setForegroundPattern(pat);
			e.gc.drawLine(10, 170, 170, 170);
			e.gc.setAlpha(50);
			e.gc.setLineCap(SWT.CAP_FLAT);
			e.gc.setForegroundPattern(pat);
			e.gc.drawLine(10, 120, 170, 120);
			e.gc.setAlpha(255);
			e.gc.setLineCap(SWT.CAP_FLAT);
			e.gc.setForegroundPattern(pat);
			e.gc.drawLine(150, 120, 170, 20);
		});

		shell.open();
		while (!shell.isDisposed()) {
			if (!disp.readAndDispatch())
				disp.sleep();
		}
		image.dispose();
		pat.dispose();
		disp.dispose();
	}
}
