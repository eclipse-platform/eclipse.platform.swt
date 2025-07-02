package org.eclipse.swt.snippets;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class TabExpansionTest {
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("Tab Expansion Test");
		shell.setSize(400, 200);

        shell.addPaintListener(e -> {
			GC gc = Drawing.createGraphicsContext(e.gc, shell);
			Color color = display.getSystemColor(SWT.COLOR_RED);
			gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
			gc.setBackground(color);
			gc.setFont(new Font(display, "Segoe UI", 16, SWT.NONE));
			gc.drawText("a\tb (DRAW_TAB)", 20, 20, SWT.DRAW_TAB);
			gc.drawText("a\tb (true)", 20, 60, true);
			gc.drawText("a\tb (false)", 20, 100, false);
			gc.drawText("ab (true)", 20, 140, true);
			gc.commit();
        });

        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
    }
}