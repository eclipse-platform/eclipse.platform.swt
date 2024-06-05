/*******************************************************************************
 * Copyright (c) 2012, 2016 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * example snippet: use Java2D to modify an image being displayed in an SWT GUI.
 * Take a screen snapshot to print the image to a printer.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.8
 */
import static org.eclipse.swt.events.SelectionListener.*;

import java.awt.*;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Image;
import java.awt.geom.*;

import org.eclipse.swt.*;
import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.awt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.printing.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;

public class Snippet361 {
	static Composite composite; // SWT
	static Canvas canvas; // AWT
	static Image image = null; // AWT
	static double translateX = 0, translateY = 0;
	static double rotate = 0;

	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setText("Snippet 361");
		shell.setText("Translate and Rotate an AWT Image in an SWT GUI");
		shell.setLayout(new GridLayout(8, false));

		Button fileButton = new Button(shell, SWT.PUSH);
		fileButton.setText("&Open Image File");
		fileButton.addSelectionListener(widgetSelectedAdapter(e -> {
			String filename = new FileDialog(shell).open();
			if (filename != null) {
				image = Toolkit.getDefaultToolkit().getImage(filename);
				canvas.repaint();
			}
		}));

		new Label(shell, SWT.NONE).setText("Translate &X by:");
		final Combo translateXCombo = new Combo(shell, SWT.NONE);
		translateXCombo.setItems("0", "image width", "image height", "100", "200");
		translateXCombo.select(0);
		translateXCombo.addModifyListener(e -> {
			translateX = numericValue(translateXCombo);
			canvas.repaint();
		});

		new Label(shell, SWT.NONE).setText("Translate &Y by:");
		final Combo translateYCombo = new Combo(shell, SWT.NONE);
		translateYCombo.setItems("0", "image width", "image height", "100", "200");
		translateYCombo.select(0);
		translateYCombo.addModifyListener(e -> {
			translateY = numericValue(translateYCombo);
			canvas.repaint();
		});

		new Label(shell, SWT.NONE).setText("&Rotate by:");
		final Combo rotateCombo = new Combo(shell, SWT.NONE);
		rotateCombo.setItems("0", "Pi", "Pi/2", "Pi/4", "Pi/8");
		rotateCombo.select(0);
		rotateCombo.addModifyListener(e -> {
			rotate = numericValue(rotateCombo);
			canvas.repaint();
		});

		Button printButton = new Button(shell, SWT.PUSH);
		printButton.setText("&Print Image");
		printButton.addSelectionListener(widgetSelectedAdapter(e -> {
			performPrintAction(display, shell);
		}));

		composite = new Composite(shell, SWT.EMBEDDED | SWT.BORDER);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true, 8, 1);
		data.widthHint = 640;
		data.heightHint = 480;
		composite.setLayoutData(data);
		Frame frame = SWT_AWT.new_Frame(composite);
		canvas = new Canvas() {
			private static final long serialVersionUID = 1L;

			@Override
			public void paint (Graphics g) {
				if (image != null) {
					g.setColor(Color.WHITE);
					g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

					/* Use Java2D here to modify the image as desired. */
					Graphics2D g2d = (Graphics2D) g;
					AffineTransform t = new AffineTransform();
					t.translate(translateX, translateY);
					t.rotate(rotate);
					g2d.setTransform(t);
					/*------------*/

					g.drawImage(image, 0, 0, this);
				}
			}
		};
		frame.add(canvas);
		composite.getAccessible().addAccessibleListener(new AccessibleAdapter() {
			@Override
			public void getName(AccessibleEvent e) {
				e.result = "Image drawn in AWT Canvas";
			}
		});

		shell.pack();
		shell.open();
		while(!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}

	private static void performPrintAction(final Display display, final Shell shell) {
		Rectangle r = composite.getBounds();
		Point p = shell.toDisplay(r.x, r.y);
		org.eclipse.swt.graphics.Image snapshotImage
			= new org.eclipse.swt.graphics.Image(display, r.width-2, r.height-2);
		GC snapshotGC = new GC(display);
		snapshotGC.copyArea(snapshotImage, p.x+1, p.y+1);
		PrintDialog dialog = new PrintDialog(shell, SWT.NONE);
		PrinterData data = new PrinterData();
		data.orientation = PrinterData.LANDSCAPE;
		dialog.setPrinterData(data);
		data = dialog.open();
		if (data != null) {
			Printer printer = new Printer(data);
			Point screenDPI = display.getDPI();
			Point printerDPI = printer.getDPI();
			int scaleFactor = printerDPI.x / screenDPI.x;
			Rectangle trim = printer.computeTrim(0, 0, 0, 0);
			if (printer.startJob("Print Image")) {
				ImageData imageData = snapshotImage.getImageData();
				org.eclipse.swt.graphics.Image printerImage
					= new org.eclipse.swt.graphics.Image(printer, imageData);
				GC printerGC = new GC(printer);
				if (printer.startPage()) {
					printerGC.drawImage(
						printerImage,
						0,
						0,
						imageData.width,
						imageData.height,
						-trim.x,
						-trim.y,
						scaleFactor * imageData.width,
						scaleFactor * imageData.height);
					printer.endPage();
				}
				printerGC.dispose();
				printer.endJob();
			}
			printer.dispose();
		}
		snapshotImage.dispose();
		snapshotGC.dispose ();
	}

	static double numericValue(Combo combo) {
		String string = combo.getText();
		if (string.equals("image width")) return image.getWidth(canvas);
		if (string.equals("image height")) return image.getHeight(canvas);
		if (string.equals("100")) return 100;
		if (string.equals("200")) return 200;
		if (string.equals("Pi")) return Math.PI;
		if (string.equals("Pi/2")) return Math.PI / 2;
		if (string.equals("Pi/4")) return Math.PI / 4;
		if (string.equals("Pi/8")) return Math.PI / 8;
		/* Allow user-entered numbers. */
		Double d = (double) 0;
		try {
			d = Double.valueOf(string);
		} catch(NumberFormatException ex) {
		}
		return d;
	}

}
