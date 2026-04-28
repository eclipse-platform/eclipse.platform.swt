package org.eclipse.swt.examples.skia;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class SnippetText {

    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("SWT vs Skija drawText Example");
        shell.setSize(1100, 700);
        shell.setLayout(new FillLayout(SWT.HORIZONTAL));

        // Composite for two canvases side by side
        org.eclipse.swt.widgets.Composite composite = shell;

        // Left: Classic SWT Canvas with scrollbars
        ScrolledComposite scrolledClassic = new ScrolledComposite(composite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        Canvas canvasClassic = new Canvas(scrolledClassic, SWT.NONE);
        scrolledClassic.setContent(canvasClassic);
        scrolledClassic.setExpandHorizontal(true);
        scrolledClassic.setExpandVertical(true);
        canvasClassic.setSize(500, 1200);
        scrolledClassic.setMinSize(canvasClassic.getSize());

        // Right: Skija Canvas with scrollbars
        ScrolledComposite scrolledSkija = new ScrolledComposite(composite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        Canvas canvasSkija = new Canvas(scrolledSkija, SWT.SKIA);
        scrolledSkija.setContent(canvasSkija);
        scrolledSkija.setExpandHorizontal(true);
        scrolledSkija.setExpandVertical(true);
        canvasSkija.setSize(500, 1200);
        scrolledSkija.setMinSize(canvasSkija.getSize());

        String multiLine = "Short\nA much &longer \ttab line &of text\rCarriage &return\nMid length\nTiny\nThe last line is the longest of all lines in this example.\nAmpersand: &File\nBackspace: A\bB\nForm feed: A\fB";
        Font font = new Font(display, "Arial", 15, SWT.NORMAL);
        String displayText = multiLine.replace("\f", "\u240C");

        // Paint logic for both canvases
        java.util.function.Consumer<org.eclipse.swt.graphics.GC> paintLogic = gc -> {
            gc.setFont(font);
            gc.setBackground(display.getSystemColor(SWT.COLOR_YELLOW));
            int y = 5;
            gc.drawText("draw\ttab", 20, y , SWT.DRAW_TAB);
            y +=30;
            gc.drawText("draw\rdeleimiter\r\ncarriagereturn", 20, y , SWT.DRAW_DELIMITER);
            y +=70;
            gc.drawText("draw\rtab and draw deleimiter\r\ncarriagereturn", 20, y , SWT.DRAW_DELIMITER | SWT.DRAW_TAB);
            y +=80;
            gc.drawText("Not draw\rtab", 20, y , SWT.NONE);
            y +=30;
            gc.drawText(displayText, 20, y);
            y +=300;
            gc.drawText(displayText, 20, y, true);
            y +=300;
            // windows always draws win delimiter mode, even without flag...
            gc.drawText("SWT.DRAW_MNEMONIC: "+displayText, 20, y, SWT.DRAW_MNEMONIC);
            y+=300;
        };

        canvasClassic.addPaintListener(e -> paintLogic.accept(e.gc));
        canvasSkija.addPaintListener(e -> paintLogic.accept(e.gc));

        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        font.dispose();
        display.dispose();
    }
}