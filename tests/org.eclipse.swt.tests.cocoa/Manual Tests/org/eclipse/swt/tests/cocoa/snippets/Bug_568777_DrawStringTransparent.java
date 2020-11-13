package org.eclipse.swt.tests.cocoa.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Bug_568777_DrawStringTransparent {

    public static void main(String[] args) {
        Display display = new Display();
        runSnippet(display);
        display.dispose();
    }

    public static void runSnippet(Display display) {
        final Shell shell = new Shell(display);
        shell.setText("Snippet 245");
        Painter painter = new Painter(display);
        shell.addPaintListener(painter);
        Rectangle clientArea = shell.getClientArea();
        shell.setBounds(clientArea.x + 10, clientArea.y + 10, 300, 400);
        shell.open();
        try {
            while (!shell.isDisposed()) {
                if (!display.readAndDispatch())
                    display.sleep();
            }
        } finally {
            painter.dispose();
        }
    }

    private static class Painter implements PaintListener {
        private Font mySmall;
        private Font myBig;

        public Painter(Display display) {
            mySmall = new Font(display, "Verdana", 10, SWT.BOLD);
            myBig = new Font(display, "Menlo", 22, SWT.ITALIC);
        }

        @Override
        public void paintControl(PaintEvent event) {
            doPaint(event.gc, 100, true);
            doPaint(event.gc, 200, false);
        }

        private void doPaint(GC gc, int x, boolean transparent) {
            gc.setFont(mySmall);
            gc.drawText("3", x, 50, transparent);

            gc.setFont(myBig);
            gc.drawText("3", x, 100, transparent);
            gc.drawText("6", x, 150, transparent);
        }

        public void dispose() {
            myBig.dispose();
            mySmall.dispose();
        }
    }

}
