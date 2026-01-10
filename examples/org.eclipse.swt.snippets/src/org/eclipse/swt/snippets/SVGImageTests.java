package org.eclipse.swt.snippets;
import java.nio.file.Path;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class SVGImageTests {

    public static void main(String[] args) throws Exception {
        final Display display = new Display();

        final Shell shell = new Shell(display);
        shell.setText("SVG Image Test");
        shell.setLayout(new FillLayout());
        shell.setBounds(250, 50, 1000, 400);
        shell.setBackground(new Color(255, 255, 255));

        String imgPath = "eclipse16.svg";
        Path fullPath = Path.of(SVGImageTests.class.getResource(imgPath).toURI());

        // Image loaded in stream
        Image image1 = new Image(display, SVGImageTests.class.getResourceAsStream(imgPath));

        // Image loaded in ImageFileNameProvider
        Image image3 = new Image(display, (ImageFileNameProvider) zoom -> {
            return zoom == 100 ? fullPath.toString() : null;
        });

        shell.addPaintListener(e -> {
            e.gc.drawImage(image1, 20, 10, 300, 300);
            e.gc.drawText("Image(Display, InputStream)", 80, 300);

            e.gc.drawImage(image3, 340, 10, 300, 300);
            e.gc.drawText("Image(Display, String)", 420, 300);
        });

        shell.open();

        while(!shell.isDisposed()) {
            if(!display.readAndDispatch()) {
                display.sleep();
            }
        }

        display.dispose();
    }
}