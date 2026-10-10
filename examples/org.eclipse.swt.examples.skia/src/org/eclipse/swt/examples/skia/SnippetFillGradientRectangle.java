package org.eclipse.swt.examples.skia;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class SnippetFillGradientRectangle {
	
	final static boolean USE_SKIA = true;
	
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("Skia FillGradientRectangle Example");
        shell.setSize(400, 300);

        Canvas canvas = new Canvas(shell, USE_SKIA ? SWT.SKIA : SWT.NONE);
        canvas.setBounds(20, 20, 360, 240);

        canvas.addListener(SWT.Paint, event -> {
            GC gc = event.gc;
            Color color1 = display.getSystemColor(SWT.COLOR_RED);
            Color color2 = display.getSystemColor(SWT.COLOR_YELLOW);
            gc.setForeground(color1);
            gc.setBackground(color2);
            gc.fillGradientRectangle(40, 40, 200, 100, true); // vertical gradient
            gc.setForeground(display.getSystemColor(SWT.COLOR_BLUE));
            gc.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
            gc.fillGradientRectangle(40, 160, 200, 40, false); // horizontal gradient
        });

        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }
}