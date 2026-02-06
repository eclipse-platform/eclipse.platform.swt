/*******************************************************************************
 * Copyright (c) 2026 Eclipse Platform Contributors and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eclipse Platform Contributors - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * Drawing operations test snippet: compare drawing on Display, Printer and PDF
 *
 * This snippet demonstrates drawing operations across different devices (Display, Printer, PDF)
 * to verify that drawing is consistent. It draws a test pattern with borders, crosses, and
 * a reference box of a specific physical size (in centimeters).
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.printing.*;
import org.eclipse.swt.program.*;
import org.eclipse.swt.widgets.*;

public class Snippet391 {

	private static int boxSizeCm = 10;
	private static boolean considerMonitorZoom = false;
	private static boolean useControlPrint = false;

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Drawing Operations Test - Snippet 391");
		shell.setLayout(new GridLayout(2, false));

		// Canvas on the left
		Canvas canvas = new Canvas(shell, SWT.BORDER | SWT.DOUBLE_BUFFERED);
		GridData canvasData = new GridData(SWT.FILL, SWT.FILL, true, true);
		canvasData.minimumWidth = 400;
		canvasData.minimumHeight = 400;
		canvas.setLayoutData(canvasData);
		canvas.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		canvas.addListener(SWT.Paint, e -> {
			Monitor monitor = canvas.getMonitor();
			Rectangle clientArea = canvas.getClientArea();
			drawTestPattern(e.gc, display, clientArea, monitor);
		});

		// Controls on the right
		Composite controlPanel = new Composite(shell, SWT.NONE);
		controlPanel.setLayout(new GridLayout(2, false));
		controlPanel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));

		// Box size spinner
		Label sizeLabel = new Label(controlPanel, SWT.NONE);
		sizeLabel.setText("Box size (cm):");

		Spinner sizeSpinner = new Spinner(controlPanel, SWT.BORDER);
		sizeSpinner.setMinimum(1);
		sizeSpinner.setMaximum(50);
		sizeSpinner.setSelection(boxSizeCm);
		sizeSpinner.addListener(SWT.Selection, e -> {
			boxSizeCm = sizeSpinner.getSelection();
			canvas.redraw();
		});

		// Monitor zoom checkbox
		Button zoomCheckbox = new Button(controlPanel, SWT.CHECK);
		zoomCheckbox.setText("Consider Monitor zoom");
		GridData checkboxData = new GridData();
		checkboxData.horizontalSpan = 2;
		zoomCheckbox.setLayoutData(checkboxData);
		zoomCheckbox.addListener(SWT.Selection, e -> {
			considerMonitorZoom = zoomCheckbox.getSelection();
			canvas.redraw();
		});

		// Paint mode radio buttons
		Group modeGroup = new Group(controlPanel, SWT.NONE);
		modeGroup.setText("Paint Mode");
		modeGroup.setLayout(new GridLayout(1, false));
		GridData modeData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		modeData.horizontalSpan = 2;
		modeGroup.setLayoutData(modeData);

		Button directPaintRadio = new Button(modeGroup, SWT.RADIO);
		directPaintRadio.setText("Direct Paint");
		directPaintRadio.setSelection(true);

		Button controlPrintRadio = new Button(modeGroup, SWT.RADIO);
		controlPrintRadio.setText("Control Print");

		directPaintRadio.addListener(SWT.Selection, e -> {
			useControlPrint = !directPaintRadio.getSelection();
		});
		controlPrintRadio.addListener(SWT.Selection, e -> {
			useControlPrint = controlPrintRadio.getSelection();
		});

		// Separator
		Label separator = new Label(controlPanel, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData sepData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		sepData.horizontalSpan = 2;
		separator.setLayoutData(sepData);

		// Print button
		Button printButton = new Button(controlPanel, SWT.PUSH);
		printButton.setText("Print");
		GridData printData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		printData.horizontalSpan = 2;
		printButton.setLayoutData(printData);
		printButton.addListener(SWT.Selection, e -> printToPrinter(shell, canvas));

		// Create PDF button
		Button pdfButton = new Button(controlPanel, SWT.PUSH);
		pdfButton.setText("Create PDF");
		GridData pdfData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		pdfData.horizontalSpan = 2;
		pdfButton.setLayoutData(pdfData);
		pdfButton.addListener(SWT.Selection, e -> createPdf(shell, canvas));

		shell.setSize(800, 600);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	/**
	 * Draws a test pattern on the given GC.
	 *
	 * @param gc       the graphics context to draw on
	 * @param device the device (Display, Printer, or PDFDocument)
	 * @param rect     the rectangle defining the drawing area
	 * @param monitor  optional monitor for zoom information (can be null)
	 */
	public static void drawTestPattern(GC gc, Device device, Rectangle rect, Monitor monitor) {
		Point deviceDpi = device.getDPI();
		int dpi = deviceDpi.x;
		// Calculate effective DPI considering monitor zoom if enabled
		int effectiveDpi = dpi;
		if (monitor != null && considerMonitorZoom) {
			int zoom = monitor.getZoom();
			effectiveDpi = (dpi * zoom) / 100;
		}

		// Save original settings
		int originalLineWidth = gc.getLineWidth();
		Color originalFg = gc.getForeground();

		// Draw red border at 1px distance from edges
		gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_RED));
		gc.setLineWidth(1);
		gc.drawRectangle(rect.x + 1, rect.y + 1, rect.width - 3, rect.height - 3);

		// Draw blue border at 5px distance from edges
		gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_BLUE));
		gc.drawRectangle(rect.x + 5, rect.y + 5, rect.width - 11, rect.height - 11);

		// Draw DPI info in upper left if drawable is a Device
		gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_BLACK));
		gc.drawString("DPI: " + dpi, rect.x + 10, rect.y + 10, true);

		// Draw monitor zoom in upper right if monitor is given
		if (monitor != null) {
			String zoomText = "Zoom: " + monitor.getZoom() + "%";
			Point textExtent = gc.stringExtent(zoomText);
			gc.drawString(zoomText, rect.x + rect.width - textExtent.x - 10, rect.y + 10, true);
		}

		// Draw black cross with 2px line width over full rectangle
		gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_BLACK));
		gc.setLineWidth(2);
		gc.drawLine(rect.x, rect.y, rect.x + rect.width, rect.y + rect.height);
		gc.drawLine(rect.x + rect.width, rect.y, rect.x, rect.y + rect.height);

		// Calculate box size in pixels from centimeters
		// 1 inch = 2.54 cm, so pixels = (cm / 2.54) * DPI
		int boxWidthPx = (int) Math.round((boxSizeCm / 2.54) * effectiveDpi);
		int boxHeightPx = boxWidthPx;

		// Center the box in the rectangle
		int boxX = rect.x + (rect.width - boxWidthPx) / 2;
		int boxY = rect.y + (rect.height - boxHeightPx) / 2;

		// Fill the box with light gray
		gc.setBackground(gc.getDevice().getSystemColor(SWT.COLOR_GRAY));
		gc.fillRectangle(boxX, boxY, boxWidthPx, boxHeightPx);

		// Draw 1px black border around the box
		gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_BLACK));
		gc.setLineWidth(1);
		gc.drawRectangle(boxX, boxY, boxWidthPx, boxHeightPx);

		// Show the actual computed pixel dimensions on top/right of the box
		String pixelInfo = boxWidthPx + " x " + boxHeightPx + " px";
		Point infoExtent = gc.stringExtent(pixelInfo);
		int infoX = boxX + boxWidthPx - infoExtent.x;
		int infoY = boxY - infoExtent.y - 2;
		if (infoY < rect.y + 30) {
			infoY = rect.y + 30;
		}
		gc.drawString(pixelInfo, infoX, infoY, true);

		// Show the box size in cm below the pixel info
		String cmInfo = boxSizeCm + " cm (effective DPI: " + effectiveDpi + ")";
		gc.drawString(cmInfo, boxX, boxY + boxHeightPx + 5, true);

		// Restore original settings
		gc.setLineWidth(originalLineWidth);
		gc.setForeground(originalFg);
	}

	private static void printToPrinter(Shell shell, Canvas canvas) {
		PrintDialog printDialog = new PrintDialog(shell);
		PrinterData data = printDialog.open();
		if (data == null) {
			return; // User cancelled
		}

		Printer printer = new Printer(data);
		if (printer.startJob("Drawing Test - Snippet 391")) {
			GC gc = new GC(printer);
			if (printer.startPage()) {
				if (useControlPrint) {
					canvas.print(gc);
				} else {
					Rectangle printArea = printer.getClientArea();
					drawTestPattern(gc, printer, printArea, null);
				}
				printer.endPage();
			}
			gc.dispose();
			printer.endJob();
		}
		printer.dispose();

		MessageBox msgBox = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
		msgBox.setText("Print Complete");
		msgBox.setMessage("Document sent to printer.");
		msgBox.open();
	}

	private static void createPdf(Shell shell, Canvas canvas) {
		try {
			String tempDir = System.getProperty("java.io.tmpdir");
			String pdfPath = tempDir +(useControlPrint? "/drawing_test_snippet391_control.pdf":"/drawing_test_snippet391_direct.pdf");

			// Use exact canvas size in points (1 point = 1 pixel at 72 DPI)
			Point canvasSize = canvas.getSize();
			double widthPoints = canvasSize.x;
			double heightPoints = canvasSize.y;

			PDFDocument pdf = new PDFDocument(pdfPath, widthPoints, heightPoints);
			GC gc = new GC(pdf);

			if (useControlPrint) {
				canvas.print(gc);
			} else {
				Rectangle pdfRect = new Rectangle(0, 0, canvasSize.x, canvasSize.y);
				drawTestPattern(gc, pdf, pdfRect, null);
			}

			gc.dispose();
			pdf.dispose();

			System.out.println("PDF exported to: " + pdfPath);
			Program.launch(pdfPath);

		} catch (Throwable ex) {
			MessageBox errorBox = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
			errorBox.setText("Error");
			errorBox.setMessage("Failed to create PDF: " + ex.getMessage());
			errorBox.open();
			ex.printStackTrace();
		}
	}
}
