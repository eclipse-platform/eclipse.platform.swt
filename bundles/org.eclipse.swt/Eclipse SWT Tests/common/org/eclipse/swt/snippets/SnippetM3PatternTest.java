package org.eclipse.swt.snippets;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class SnippetM3PatternTest {
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("SWT Pattern Test");
        shell.setSize(400, 300);

        shell.addListener(SWT.Paint, e -> {

            GC gc=	Drawing.createGraphicsContext(e.gc, shell);
            // Create images for patterns using ImageData directly (no GC on Image)
            Image bgImage = createPatternImage(display, 16, 16, SWT.COLOR_YELLOW, SWT.COLOR_RED, true);
            Image fgImage = createPatternImage(display, 8, 8, SWT.COLOR_GREEN, SWT.COLOR_BLUE, false);

            // Create patterns
            Pattern backgroundPattern = new Pattern(display, bgImage);
            Pattern foregroundPattern = new Pattern(display, fgImage);

            // Set background pattern and fill a rectangle
            gc.setBackgroundPattern(backgroundPattern);
            gc.fillRectangle(20, 20, 150, 100);

            // Set foreground pattern and draw a rectangle border
            gc.setForegroundPattern(foregroundPattern);
            gc.setLineWidth(5);
            gc.drawRectangle(20, 20, 150, 100);
            gc.commit();
            // Clean up
            backgroundPattern.dispose();
            foregroundPattern.dispose();
            bgImage.dispose();
            fgImage.dispose();
        });

        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }

    private static Image createPatternImage(Display display, int width, int height, int bgColor, int fgColor, boolean diagonal) {
        Image image = new Image(display, width, height);
        ImageData data = image.getImageData();
        Color bg = display.getSystemColor(bgColor);
        Color fg = display.getSystemColor(fgColor);
        RGB bgRGB = bg.getRGB();
        RGB fgRGB = fg.getRGB();
        // Fill background
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                data.setPixel(x, y, data.palette.getPixel(bgRGB));
            }
        }
        // Draw pattern
        if (diagonal) {
            for (int i = 0; i < Math.min(width, height); i++) {
                data.setPixel(i, i, data.palette.getPixel(fgRGB));
            }
        } else {
            for (int i = 0; i < width; i++) {
                int y = height - 1 - i * height / width;
                if (y >= 0 && y < height) {
                    data.setPixel(i, y, data.palette.getPixel(fgRGB));
                }
            }
        }
        image.dispose();
        return new Image(display, data);
    }
}