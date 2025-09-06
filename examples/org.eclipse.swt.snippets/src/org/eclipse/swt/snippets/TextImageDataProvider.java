package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class TextImageDataProvider implements ImageDataAtSizeProvider {
    private final String text;



    public TextImageDataProvider(String text, int baseFontSize, double zoom) {
        this.text = text;
    }

    @Override
    public ImageData getImageData(int zoomLevel) {
    	int scaleFactor=zoomLevel/100;
    	int baseWidth =100;
    	int baseHeight=100;
    	return getImageData(baseWidth*scaleFactor,baseHeight*scaleFactor);
    }

   @Override
public ImageData getImageData(int width, int height) {
    Display display = Display.getDefault();

    // Step 1: Determine font size based on height and zoom
    int fontSize = (int) Math.round( height / 100.0);
    if (fontSize <= 0) fontSize = 1;
    Font font = new Font(display, "Arial", fontSize, SWT.NORMAL);

    // Step 2: Measure text size
    Image tmp = new Image(display, 1, 1);
    GC measureGC = new GC(tmp);
    measureGC.setFont(font);
    String text="abcd";
    Point textExtent = measureGC.textExtent(text);
    measureGC.dispose();
    tmp.dispose();

    // Step 3: Scale font to fit requested width/height
    double scaleX = (double) width / textExtent.x;
    double scaleY = (double) height / textExtent.y;
    double scale = Math.min(scaleX, scaleY);

    fontSize = Math.max(1, (int) (fontSize * scale));
    font.dispose();
    font = new Font(display, "Arial", fontSize, SWT.NORMAL);

    // Step 4: Create image of requested size
    Image image = new Image(display, width, height);
    GC gc = new GC(image);
    gc.setFont(font);
    gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
    gc.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
    gc.fillRectangle(image.getBounds());

    // Step 5: Draw line and text
    gc.setLineWidth(Math.max(1, width / 20)); // thickness relative to image height
    gc.drawLine(0, 0, width/2, height); // example diagonal line
    Point newTextExtent = gc.textExtent(text);
    int x = (width - newTextExtent.x) / 2; // center horizontally
    int y = (height - newTextExtent.y) / 2; // center vertically
    gc.drawText(text, x, y, true);

    gc.dispose();

    ImageData data =image.getImageData();

    image.dispose();
    font.dispose();

    return data;
}



    // Demo: paint to a canvas
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("Text Image Provider Demo");
        shell.setSize(4000, 4000);

        TextImageDataProvider provider = new TextImageDataProvider("abcd", 12, 1);

        Canvas canvas = new Canvas(shell, SWT.NONE);
        canvas.setBounds(0, 0, 16000, 16000);

        canvas.addPaintListener(e -> {
            // create image from provider
            Image image = new Image(display, provider);
            System.out.println(image.getBounds().width+"ssssss"+image.getBounds().height);
            e.gc.drawImage(image, 0, 0);
            e.gc.drawImage(image, 100, 0);
            e.gc.drawImage(image, 0, 0, image.getBounds().width, image.getBounds().height, 0,200, 200, 200);
            e.gc.drawImage(image, 0, 0, image.getBounds().width, image.getBounds().height, 0,400, 100, 100);
            e.gc.drawImage(image, 0, 0, image.getBounds().width, image.getBounds().height, 100,400, 500, 500);
            image.dispose();
        });

        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }
}
