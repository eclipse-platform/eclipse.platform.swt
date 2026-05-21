package org.eclipse.swt.examples.skia;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;

public class SnippetCompareImages {

	record ImageInfo(String name, Image image) {
	}

	private static final String IMAGES_PATH = "images";
	private static final int IMAGE_SPACING = 10;
	private final static ArrayList<ImageInfo> swtImages = new ArrayList<>();

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Compare Images: GC vs Skija");
		shell.setLayout(new GridLayout(2, true));

		Composite leftComposite = new Composite(shell, SWT.BORDER);
		leftComposite.setLayout(new FillLayout());
		leftComposite.setLayoutData(new org.eclipse.swt.layout.GridData(SWT.FILL, SWT.FILL, true, true));
		Canvas gcCanvas = new Canvas(leftComposite, SWT.V_SCROLL | SWT.DOUBLE_BUFFERED);
		gcCanvas.setLayoutData(new org.eclipse.swt.layout.GridData(SWT.FILL, SWT.FILL, true, true));
		ScrollBar gcScrollBar = gcCanvas.getVerticalBar();

		Composite rightComposite = new Composite(shell, SWT.BORDER);
		rightComposite.setLayout(new FillLayout());
		rightComposite.setLayoutData(new org.eclipse.swt.layout.GridData(SWT.FILL, SWT.FILL, true, true));
		Canvas skiaCanvas = new Canvas(rightComposite, SWT.V_SCROLL | SWT.SKIA);
		skiaCanvas.setLayoutData(new org.eclipse.swt.layout.GridData(SWT.FILL, SWT.FILL, true, true));
		ScrollBar skiaScrollBar = skiaCanvas.getVerticalBar();
		// Load images

		swtImages.add(new ImageInfo("Question Icon", Display.getDefault().getSystemImage(SWT.ICON_QUESTION)));
		swtImages.add(new ImageInfo("Error Icon", Display.getDefault().getSystemImage(SWT.ICON_ERROR)));
		swtImages.add(new ImageInfo("Info Icon", Display.getDefault().getSystemImage(SWT.ICON_INFORMATION)));

		List<File> imageFiles = new ArrayList<>();
		// Use working directory reliably
		File imagesDir = new File(System.getProperty("user.dir"), IMAGES_PATH);
		if (imagesDir.exists() && imagesDir.isDirectory()) {
			for (File f : imagesDir.listFiles()) {
				String name = f.getName().toLowerCase();
				if (name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".gif")
						|| name.endsWith(".bmp") || name.endsWith(".ico")) {

					try {
						Image img = new Image(display, f.getAbsolutePath());
						swtImages.add(new ImageInfo(f.getAbsolutePath(), img));
						imageFiles.add(f);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		// Calculate total height
		int totalHeight = IMAGE_SPACING;
		for (ImageInfo img : swtImages) {
			Rectangle bounds = img.image().getBounds();
			totalHeight += bounds.height + IMAGE_SPACING;
		}

		// Scroll logic
		gcScrollBar.setMaximum(totalHeight);
		gcScrollBar.setThumb(600); // Initial thumb size, will resize with shell
		skiaScrollBar.setMaximum(totalHeight);
		skiaScrollBar.setThumb(600); // Initial thumb size, will resize with shell

		PaintListener gcPaintListener = new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				int y = IMAGE_SPACING - gcScrollBar.getSelection();
				drawImages(y, e.gc);
			}
		};
		gcCanvas.addPaintListener(gcPaintListener);

		PaintListener swtPaintListener = new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				int y = IMAGE_SPACING - skiaScrollBar.getSelection();
				drawImages(y, e.gc);
			}
		};

		skiaCanvas.addPaintListener(swtPaintListener);

		gcScrollBar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				gcCanvas.redraw();
			}
		});

		skiaScrollBar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				skiaCanvas.redraw();
			}
		});

		shell.setSize(1200, 700); // Initial size, but will resize
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		for (ImageInfo img : swtImages)
			img.image().dispose();
		display.dispose();
	}

	private static void drawImages(int y, GC gc) {
		gc.setInterpolation(SWT.HIGH);
		for (ImageInfo img : swtImages) {
			Rectangle bounds = img.image().getBounds();
			try {
				gc.drawImage(img.image(), 10, y);
			} catch (Exception ex) {
				System.err.println("  Exception drawing image: " + ex);
			}
			gc.drawText(img.name(), 10 + bounds.width + 30, y);
			y += bounds.height + IMAGE_SPACING;
		}
	}

}