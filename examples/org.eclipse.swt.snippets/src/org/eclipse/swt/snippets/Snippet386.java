package org.eclipse.swt.snippets;

import java.io.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.DPIUtil.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet386 {

    public static void main(String[] args) {
        Display display = new Display();

        Image[] images = new Image[] {
            new Image(display, createImageFileNameProvider("resources/Snippet386/collapseall.png")),
            new Image(display, createImageFileNameProvider("resources/Snippet386/collapseall.svg")),
            new Image(display, createImageDataProviderWithImageLoader()),
            new Image(display, createImageDataProvider()),
            new Image(display, createImageDataAtSizeProvider())
        };

        String[] descriptions = new String[] {
            "ImageFileNameProvider with PNGs scaled destructively",
            "ImageFileNameProvider with SVGs scaled by SVG rasterization",
            "ImageDataAtSizeProvider loading svgs with NativeImageLoader",
            "ImageDataProvider with fixed font size for a given zoom",
            "ImageDataAtSizeProvider which scales font size based on target height and width"
        };

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
        return zoom -> fileName;
    }

    private static ImageDataProvider createImageDataProviderWithImageLoader() {
		return new ImageDataAtSizeProvider() {
			@SuppressWarnings("restriction")
			@Override
			public ImageData getImageData(int targetWidth, int targetHeight) {
				try (InputStream stream = new FileInputStream("resources/Snippet386/collapseall.svg")) {
					return NativeImageLoader.load(stream, new ImageLoader(), targetWidth, targetHeight);
				} catch (IOException e) {
					SWT.error(SWT.ERROR_IO, e);
				}
				return null;
			}

			@SuppressWarnings("restriction")
			@Override
			public ImageData getImageData(int zoom) {
				try (InputStream stream = new FileInputStream("resources/Snippet386/collapseall.svg")) {
					return NativeImageLoader.load(new ElementAtZoom<>(stream, 100), new ImageLoader(), zoom).get(0).element();
				} catch (IOException e) {
					SWT.error(SWT.ERROR_IO, e);
				}
				return null;
			}
		};
	}

    private static ImageDataProvider createImageDataProvider() {
        return new ImageDataProvider() {
            @Override
            public ImageData getImageData(int zoomLevel) {
                int scaleFactor = zoomLevel / 100;
                return createScaledTextImageData(100 * scaleFactor, 100 * scaleFactor);
            }
        };
    }

    private static ImageDataProvider createImageDataAtSizeProvider() {
        return new ImageDataAtSizeProvider() {
            @Override
            public ImageData getImageData(int zoomLevel) {
                int scaleFactor = zoomLevel / 100;
                return createScaledTextImageData(100 * scaleFactor, 100 * scaleFactor);
            }

            @Override
            public ImageData getImageData(int width, int height) {
                return createScaledTextImageData(width, height);
            }
        };
    }

    private static ImageData createScaledTextImageData(int width, int height) {
        Display display = Display.getDefault();
        String text = "abcd";

        int fontSize = Math.max(1, height / 100);
        Font font = new Font(display, "Arial", fontSize, SWT.NORMAL);

        Image tmp = new Image(display, 1, 1);
        GC measureGC = new GC(tmp);
        measureGC.setFont(font);
        Point textExtent = measureGC.textExtent(text);
        measureGC.dispose();
        tmp.dispose();

        double scale = Math.min((double) width / textExtent.x, (double) height / textExtent.y);
        font.dispose();
        font = new Font(display, "Arial", Math.max(1, (int) (fontSize * scale)), SWT.NORMAL);

        Image image = new Image(display, width, height);
        GC gc = new GC(image);
        gc.setFont(font);
        gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
        gc.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
        gc.fillRectangle(image.getBounds());

        gc.setLineWidth(Math.max(1, width / 20));
        gc.drawLine(0, 0, width / 2, height);

        Point newTextExtent = gc.textExtent(text);
        gc.drawText(text, (width - newTextExtent.x) / 2, (height - newTextExtent.y) / 2, true);

        gc.dispose();
        ImageData data = image.getImageData();

        image.dispose();
        font.dispose();

        return data;
    }

    static class Slice {
        String name;
        double xFrac, yFrac, wFrac, hFrac;
        Slice(String name, double x, double y, double w, double h) {
            this.name = name;
            this.xFrac = x;
            this.yFrac = y;
            this.wFrac = w;
            this.hFrac = h;
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

        int boxW = 200, boxH = 200, gap = 20;
        int titleHeight = 20, descHeight = 40, sliceHeaderHeight = 30;

        int rows = images.length;
        int cols = slices.length;

        int canvasWidth = (boxW + gap) * cols + gap;
        int canvasHeight = (boxH + titleHeight + gap) * rows + descHeight + sliceHeaderHeight;
        canvas.setSize(canvasWidth, canvasHeight);
        scrolledComposite.setMinSize(canvasWidth, canvasHeight);

        canvas.addListener(SWT.Paint, e -> {
            // Column headers
            for (int col = 0; col < cols; col++) {
                int x = col * (boxW + gap) + gap;
                Font font = new Font(display, "Arial", 18, SWT.NORMAL);
                e.gc.setFont(font);
                e.gc.drawText(slices[col].name, x, 0, true);
                font.dispose();

            }

            // Rows
            for (int row = 0; row < rows; row++) {
                Image image = images[row];
                Rectangle rect = image.getBounds();
                int y = row * (boxH + titleHeight + gap) + descHeight + sliceHeaderHeight;

                Font font = new Font(display, "Arial", 18, SWT.NORMAL);
                e.gc.setFont(font);
                e.gc.drawText(descriptions[row], 0, y - 10, true);
                font.dispose();

                for (int col = 0; col < cols; col++) {
                    Slice s = slices[col];
                    int x = col * (boxW + gap) + gap;

                    int srcX = (int) (rect.width * s.xFrac);
                    int srcY = (int) (rect.height * s.yFrac);
                    int srcW = (int) (rect.width * s.wFrac);
                    int srcH = (int) (rect.height * s.hFrac);

                    int boxTop = y + titleHeight;
                    e.gc.drawRectangle(x, boxTop, boxW, boxH);


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
