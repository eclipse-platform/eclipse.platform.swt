package org.eclipse.swt.snippets;

import java.io.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet386 {

    public static void main(String[] args) {
        Display display = new Display();

        // Load all images
        Image[] images = new Image[] {
            new Image(display, createImageFileNameProvider("resources/Snippet386/collapseall.png")),
            new Image(display, createImageDataProvider("resources/Snippet386/collapseall.svg")),
            new Image(display, createImageFileNameProvider("resources/Snippet386/collapseall.svg")),
            new Image(display, createImageDataProvider("resources/Snippet386/collapseall.png"))
        };

        // Descriptions for each image (rows)
        String[] descriptions = new String[] {
            "ImageFileNameProvider with SVGs scaled by SVG rasterization",
            "ImageDataProvider with SVGs scaled by SVG rasterization",
            "ImageFileNameProvider with PNGs scaled destructively",
            "ImageDataProvider with PNGs scaled destructively"
        };

        // Slice names (columns)
        Slice[] slices = {
            new Slice("Full", 0.0, 0.0, 1.0, 1.0),
            new Slice("Top Half", 0.0, 0.0, 1.0, 0.5),
            new Slice("Bottom Half", 0.0, 0.5, 1.0, 0.5),
            new Slice("Left Half", 0.0, 0.0, 0.5, 1.0),
            new Slice("Right Half", 0.5, 0.0, 0.5, 1.0),
            new Slice("Top-Left Quarter", 0.0, 0.0, 0.5, 0.5)
        };

        createShellWithImages(display, images, descriptions, slices, "Snippet 386 - Flipped Layout");
    }



    private static ImageFileNameProvider createImageFileNameProvider(String fileName) {
        return new ImageFileNameProvider() {

            @Override
            public String getImagePath(int zoom) {
                return fileName;
            }
        };
    }

    private static ImageDataProvider createImageDataProvider(String fileName) {
        return new ImageDataProvider() {
            @Override
            public ImageData getImageData(int zoom) {
                try (InputStream stream = new FileInputStream(fileName)) {
                	if(zoom==100)
                	return new ImageData (fileName);
                	else
                		return new ImageData ("resources/Snippet384/images/civil-icon.png");
                    //return NativeImageLoader.load(new ElementAtZoom<>(stream, 100), new ImageLoader(), 100).get(0).element();
                } catch (IOException e) {
                    SWT.error(SWT.ERROR_IO, e);
                }
                return null;
            }
        };
    }

    static class Slice {
        String name;
        double xFrac, yFrac, wFrac, hFrac;
        Slice(String name, double x, double y, double w, double h) {
            this.name = name;
            this.xFrac = x; this.yFrac = y;
            this.wFrac = w; this.hFrac = h;
        }
    }

    private static void createShellWithImages(Display display, Image[] images, String[] descriptions, Slice[] slices, String title) {
        Shell shell = new Shell(display, SWT.SHELL_TRIM | SWT.MAX | SWT.RESIZE);
        shell.setText(title);
        shell.setLayout(new FillLayout());

        ScrolledComposite scrolledComposite = new ScrolledComposite(shell, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
        Canvas canvas = new Canvas(scrolledComposite, SWT.DOUBLE_BUFFERED);
        scrolledComposite.setContent(canvas);
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);

        int boxW = 400;
        int boxH = 500;
        int gap = 20;
        int titleHeight = 20;
        int descHeight = 40; // description row height
        int sliceHeaderHeight = 30; // column header height

        int rows = images.length; // one row per image
        int cols = slices.length;  // one column per slice

        int canvasWidth = (boxW + gap) * cols + gap; // extra gap for row headers
        int canvasHeight = (boxH + titleHeight + gap) * rows + descHeight + sliceHeaderHeight;
        canvas.setSize(canvasWidth, canvasHeight);
        scrolledComposite.setMinSize(canvasWidth, canvasHeight);

        canvas.addListener(SWT.Paint, e -> {

            // Draw column headers (slice names)
            for (int col = 0; col < cols; col++) {
                int x = col * (boxW + gap) + gap; // leave gap for row header
                e.gc.drawText(slices[col].name, x, 0, true);
            }

            // Draw each row (image type)
            for (int row = 0; row < rows; row++) {
                Image image = images[row];
                Rectangle rect = image.getBounds();

                int y = row * (boxH + titleHeight + gap) + descHeight + sliceHeaderHeight;

                // Draw row description
                e.gc.drawText(descriptions[row], 0, y, true);

                for (int col = 0; col < cols; col++) {
                    Slice s = slices[col];

                    int x = col * (boxW + gap) + gap;

                    int srcX = (int) (rect.width * s.xFrac);
                    int srcY = (int) (rect.height * s.yFrac);
                    int srcW = (int) (rect.width * s.wFrac);
                    int srcH = (int) (rect.height * s.hFrac);

                    // Draw box
                    int boxTop = y + titleHeight;
                    e.gc.drawRectangle(x, boxTop, boxW, boxH);

                    // Draw image slice
                    e.gc.drawImage(image, srcX, srcY, srcW, srcH, x, boxTop, boxW, boxH);
                }
            }
        });

        shell.setMaximized(true);
        shell.open();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }

        for (Image img : images) img.dispose();
        display.dispose();
    }
}
