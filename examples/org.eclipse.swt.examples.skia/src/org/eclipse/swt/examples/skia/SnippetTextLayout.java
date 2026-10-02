package org.eclipse.swt.examples.skia;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class SnippetTextLayout {

	private static final boolean useSkia = false; // Set to false to use default TextLayout

    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("TextLayout Example");
        shell.setLayout(new FillLayout());

        int style = useSkia ? SWT.DOUBLE_BUFFERED | SWT.SKIA : SWT.DOUBLE_BUFFERED;

        Canvas canvas = new Canvas(shell, style);
        canvas.addPaintListener(new PaintListener() {
            @Override
            public void paintControl(PaintEvent e) {
                TextLayout layout = new TextLayout(display);
                layout.setText("Hello, Skia TextLayout!\nMultiline and styled text.");
                layout.setStyle(null, 0, 4); // Default style for 'Hello'
                layout.setStyle(new org.eclipse.swt.graphics.TextStyle(
                    display.getSystemFont(), new Color(display, 0, 128, 255), null), 7, 15); // Style 'Skia Text'

                e.gc.setBackground(display.getSystemColor(SWT.COLOR_GREEN));

                e.gc.fillRectangle(e.x, e.y, e.width, e.height);

                layout.draw(e.gc, 20, 20);
                layout.dispose();
            }
        });

        shell.setSize(400, 200);
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }
}