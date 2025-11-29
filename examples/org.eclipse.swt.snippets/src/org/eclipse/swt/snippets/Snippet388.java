/*******************************************************************************
 * Copyright (c) 2025 Eclipse Platform Contributors and others.
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
 * PDFDocument example snippet: create a shell with graphics and export to PDF
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

public class Snippet388 {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("PDF Export Demo");
		shell.setLayout(new BorderLayout());

		Label titleLabel = new Label(shell, SWT.CENTER);
		titleLabel.setText("SWT Graphics Demo");
		titleLabel.setLayoutData(new BorderData(SWT.TOP));

		Canvas canvas = new Canvas(shell, SWT.BORDER);
		canvas.setLayoutData(new BorderData(SWT.CENTER));
		canvas.addListener(SWT.Paint, e -> {
			GC gc = e.gc;
			Color red = display.getSystemColor(SWT.COLOR_RED);
			Color blue = display.getSystemColor(SWT.COLOR_BLUE);
			Color green = display.getSystemColor(SWT.COLOR_GREEN);
			Color yellow = display.getSystemColor(SWT.COLOR_YELLOW);
			Color black = display.getSystemColor(SWT.COLOR_BLACK);
			Color darkGray = display.getSystemColor(SWT.COLOR_DARK_GRAY);

			int shapeSpacing = 200;
			int shapeY = 20;
			int shapeWidth = 100;
			int shapeHeight = 80;
			int textY = shapeY + shapeHeight + 5;

			int x1 = 50;
			gc.setBackground(red);
			gc.fillRectangle(x1, shapeY, shapeWidth, shapeHeight);

			int x2 = x1 + shapeSpacing;
			gc.setForeground(blue);
			gc.setLineWidth(3);
			gc.drawRectangle(x2, shapeY, shapeWidth, shapeHeight);

			int x3 = x2 + shapeSpacing;
			gc.setBackground(green);
			gc.fillOval(x3, shapeY, shapeWidth, shapeHeight);

			int x4 = x3 + shapeSpacing;
			gc.setForeground(yellow);
			gc.setLineWidth(2);
			gc.drawOval(x4, shapeY, shapeWidth, shapeHeight);

			gc.setForeground(black);
			gc.setLineWidth(4);
			gc.drawLine(20, 150, x4 + shapeWidth, 150);

			gc.setBackground(blue);
			gc.fillPolygon(new int[] { 50, 170, 100, 220, 150, 170 });

			gc.setForeground(red);
			gc.setLineWidth(2);
			gc.drawPolygon(new int[] { 250, 170, 300, 220, 350, 170, 300, 200 });

			gc.setForeground(darkGray);
			String[] labels = { "Filled Rectangle", "Outlined Rectangle", "Filled Oval", "Outlined Oval" };
			int[] shapeXPositions = { x1, x2, x3, x4 };
			for (int i = 0; i < labels.length; i++) {
				Point textExtent = gc.stringExtent(labels[i]);
				int centeredX = shapeXPositions[i] + (shapeWidth - textExtent.x) / 2;
				gc.drawString(labels[i], centeredX, textY, true);
			}

			int row2Y = 240;

			Path path = new Path(display);
			try {
				path.moveTo(x1, row2Y + 40);
				path.lineTo(x1 + 30, row2Y);
				path.quadTo(x1 + 50, row2Y + 20, x1 + 70, row2Y);
				path.cubicTo(x1 + 90, row2Y, x1 + 100, row2Y + 60, x1 + 50, row2Y + 70);
				path.close();
				gc.setBackground(display.getSystemColor(SWT.COLOR_CYAN));
				gc.fillPath(path);
				gc.setForeground(black);
				gc.setLineWidth(2);
				gc.drawPath(path);
			} finally {
				path.dispose();
			}

			Pattern gradient1 = new Pattern(display, x2, row2Y, x2 + shapeWidth, row2Y + shapeHeight, 
					display.getSystemColor(SWT.COLOR_MAGENTA), display.getSystemColor(SWT.COLOR_WHITE));
			try {
				gc.setBackgroundPattern(gradient1);
				gc.fillRoundRectangle(x2, row2Y, shapeWidth, shapeHeight, 20, 20);
			} finally {
				gradient1.dispose();
			}

			Pattern gradient2 = new Pattern(display, x3 + shapeWidth / 2, row2Y + shapeHeight / 2, 
					x3 + shapeWidth / 2, row2Y + shapeHeight / 2, red, 0, yellow, 50);
			try {
				gc.setBackgroundPattern(gradient2);
				gc.fillOval(x3, row2Y, shapeWidth, shapeHeight);
			} finally {
				gradient2.dispose();
			}

			Transform transform = new Transform(display);
			try {
				transform.translate(x4 + shapeWidth / 2, row2Y + shapeHeight / 2);
				transform.rotate(45);
				gc.setTransform(transform);
				gc.setBackground(green);
				gc.fillRectangle(-40, -40, 80, 80);
				gc.setForeground(black);
				gc.setLineWidth(2);
				gc.drawRectangle(-40, -40, 80, 80);
				gc.setTransform(null);
			} finally {
				transform.dispose();
			}

			gc.setForeground(darkGray);
			String[] row2Labels = { "Path with Curves", "Linear Gradient", "Radial Gradient", "45° Rotation" };
			int row2TextY = row2Y + shapeHeight + 5;
			for (int i = 0; i < row2Labels.length; i++) {
				Point textExtent = gc.stringExtent(row2Labels[i]);
				int centeredX = shapeXPositions[i] + (shapeWidth - textExtent.x) / 2;
				gc.drawString(row2Labels[i], centeredX, row2TextY, true);
			}

			int row3Y = 360;

			gc.setAlpha(128);
			gc.setBackground(blue);
			gc.fillOval(x1, row3Y, 60, 60);
			gc.setBackground(red);
			gc.fillOval(x1 + 40, row3Y + 20, 60, 60);
			gc.setAlpha(255);

			gc.setLineStyle(SWT.LINE_DASH);
			gc.setForeground(blue);
			gc.setLineWidth(3);
			gc.drawRoundRectangle(x2, row3Y, shapeWidth, shapeHeight, 15, 15);
			gc.setLineStyle(SWT.LINE_DOT);
			gc.setForeground(red);
			gc.drawRectangle(x2 + 10, row3Y + 10, shapeWidth - 20, shapeHeight - 20);
			gc.setLineStyle(SWT.LINE_SOLID);

			gc.setAntialias(SWT.ON);
			gc.setForeground(green);
			gc.setLineWidth(3);
			for (int i = 0; i < 5; i++) {
				int offset = i * 20;
				gc.drawLine(x3 + offset, row3Y, x3 + shapeWidth, row3Y + shapeHeight - offset);
			}
			gc.setAntialias(SWT.OFF);

			Font largeFont = new Font(display, "Arial", 24, SWT.BOLD);
			try {
				gc.setFont(largeFont);
				gc.setForeground(display.getSystemColor(SWT.COLOR_DARK_BLUE));
				String text = "ABC";
				Point extent = gc.stringExtent(text);
				gc.drawString(text, x4 + (shapeWidth - extent.x) / 2, row3Y + (shapeHeight - extent.y) / 2, true);
			} finally {
				largeFont.dispose();
			}

			gc.setForeground(darkGray);
			String[] row3Labels = { "Alpha Blending", "Line Styles", "Antialiasing", "Custom Font" };
			int row3TextY = row3Y + shapeHeight + 5;
			for (int i = 0; i < row3Labels.length; i++) {
				Point textExtent = gc.stringExtent(row3Labels[i]);
				int centeredX = shapeXPositions[i] + (shapeWidth - textExtent.x) / 2;
				gc.drawString(row3Labels[i], centeredX, row3TextY, true);
			}
		});

		Button exportButton = new Button(shell, SWT.PUSH);
		exportButton.setText("Export to PDF");
		exportButton.setLayoutData(new BorderData(SWT.BOTTOM));
		exportButton.addListener(SWT.Selection, e -> {
			try {
				String tempDir = System.getProperty("java.io.tmpdir");
				String pdfPath = tempDir + "/swt_graphics_demo.pdf";

				Rectangle clientArea = shell.getClientArea();
				PDFDocument pdf = new PDFDocument(pdfPath, clientArea.width, clientArea.height);
				GC gc = new GC(pdf);
				shell.print(gc);
				gc.drawString("Exported to PDF...", 0, 0, true);
				gc.dispose();
				pdf.dispose();
				System.out.println("PDF has been exported to:\n" + pdfPath + "\n\nOpening...");
				Program.launch(pdfPath);

			} catch (Throwable ex) {
				MessageBox errorBox = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
				errorBox.setText("Error");
				errorBox.setMessage("Failed to export PDF: " + ex.getMessage());
				errorBox.open();
				ex.printStackTrace();
			}
		});

		shell.setSize(800, 600);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
