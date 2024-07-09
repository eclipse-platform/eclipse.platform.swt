/*******************************************************************************
 * Copyright (c) 2024 Yatta Solutions
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.widgets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.layout.*;
import org.junit.jupiter.api.*;

class WidgetWin32Tests extends Win32AutoscaleTestBase {

	@Test
	public void testWidgetZoomShouldChangeOnZoomLevelChange() {
		int zoom = DPIUtil.getDeviceZoom();
		int scaledZoom = zoom * 2;

		Button button = new Button(shell, SWT.PUSH);
		button.setBounds(0, 0, 200, 50);
		button.setText("Widget Test");
		button.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_CYAN));
		shell.open();
		assertEquals("The initial zoom is wrong", zoom, button.getZoom()); // pre-condition
		changeDPIZoom(scaledZoom);
		assertEquals("The Zoom Level should be updated for button on zoom change event on its shell", scaledZoom,
				button.getZoom());
	}

	@Test
	public void testButtonPointsAfterZooming() throws NoSuchMethodException, IllegalAccessException {
		int zoom = DPIUtil.getDeviceZoom();
		int scaledZoom = zoom * 2;

		shell = new Shell(display);
		shell.setBounds(0, 0, 100, 160);
		shell.setLayout(new FillLayout());
		shell.pack();

		Button button = new Button(shell, SWT.PUSH);
		button.setText("Button");
		button.setBounds(0, 0, 100, 200);
		Point sizeBeforeEvent = button.getSize();
		Point p1 = button.computeSizeInPixels(sizeBeforeEvent.x, sizeBeforeEvent.y, false);
		changeDPIZoom(scaledZoom);
		Point sizeAfterEvent = button.getSize();
		Point p2 = button.computeSizeInPixels(sizeAfterEvent.x, sizeAfterEvent.y, false);

		assertEquals("Width should be half in points after zooming to 200", p1.x / 2 , p2.x);
		assertEquals("Height should be half in points after zooming to 200", p1.y / 2, p2.y);
	}

	@Test
	public void testImagePixelsWithDoubleZoomLevel() {
		int zoom = DPIUtil.getDeviceZoom();
		int scaledZoom = zoom * 2;

		InputStream inputStream = WidgetWin32Tests.class.getResourceAsStream("folder.png");
		Image image = new Image(display, inputStream);

		Point buttonImageSizeBeforeEvent = getImageDimension(image, zoom);
		Point buttonImageSizeAfterEvent = getImageDimension(image, scaledZoom);

		assertEquals("Width of a button image should be doubled after zooming to 200",
				buttonImageSizeBeforeEvent.x * 2, buttonImageSizeAfterEvent.x);
		assertEquals("Height of a button image should be doubled after zooming to 200",
				buttonImageSizeBeforeEvent.y * 2, buttonImageSizeAfterEvent.y);
	}

	private Point getImageDimension(Image image, Integer zoomLevel) {
		BITMAP bm = new BITMAP();
		OS.GetObject(Image.win32_getHandle(image, zoomLevel), BITMAP.sizeof, bm);
		int imgWidth = bm.bmWidth;
		int imgHeight = bm.bmHeight;
		return new Point(imgWidth, imgHeight);
	}

	@Test
	public void testButtonFontAfterZooming() {
		int zoom = DPIUtil.getDeviceZoom();
		int scaledZoom = zoom * 2;

		shell = new Shell(display);
		shell.setBounds(0, 0, 100, 160);
		shell.setLayout(new FillLayout());
		shell.pack();

		Button button = new Button(shell, SWT.PUSH);
		button.setText("Button");
		button.setBounds(0, 0, 100, 200);
		Font font = new Font(display, "Arial", 12, SWT.BOLD);
		button.setFont(font);

		int heightBeforeZoom = button.getFont().getFontData()[0].data.lfHeight;
		changeDPIZoom(scaledZoom);
		int heightAfterZoom = button.getFont().getFontData()[0].data.lfHeight;

		assertEquals("Height of a font of the button should be doubled after zooming to 200",
				heightBeforeZoom * 2, heightAfterZoom);
	}

	@Test
	public void testCoolItemAfterZooming() {
		int zoom = DPIUtil.getDeviceZoom();
		int scaledZoom = zoom * 2;

		shell = new Shell(display);
		shell.setBounds(0, 0, 100, 160);
		shell.setLayout(new FillLayout());
		shell.pack();

		CoolBar coolBar = new CoolBar(shell, SWT.NONE);
		CoolItem item1 = new CoolItem(coolBar, SWT.NONE);
		Label label1 = new Label(coolBar, SWT.NONE);
		label1.setText("Label 1");
		label1.setSize(new Point(10, 20));
		item1.setControl(label1);
		item1.setPreferredSize(label1.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		item1.setSize(item1.getPreferredSize());

		var preferredControlSize = item1.getPreferredSizeInPixels();
		int xBeforeZoom = preferredControlSize.x;
		int yBeforeZoom = preferredControlSize.y;
		changeDPIZoom(scaledZoom);
		var preferredControlSize2 = item1.getPreferredSizeInPixels();
		int xAfterZoom = preferredControlSize2.x;
		int yAfterZoom = preferredControlSize2.y;

		// Adding tolerance of +- 5 because OS doesn't scale coolitem up exactly twice
		int tolerance = 5;
		int yExpectedValue = yAfterZoom / 2;
		int lowerBound = yExpectedValue - tolerance;
		int upperBound = yExpectedValue + tolerance;

		assertEquals("Width of a Item should be twice after zooming to 200", xBeforeZoom * 2, xAfterZoom);
		assertTrue("Height of a Item should be twice (+/- 5) after zooming to 200",
				yBeforeZoom >= lowerBound && yBeforeZoom <= upperBound);
	}

	@Test
	public void testExpandItemAfterZooming() {
		int zoom = DPIUtil.getDeviceZoom();
		int scaledZoom = zoom * 2;

		shell = new Shell(display);
		shell.setBounds(0, 0, 100, 160);
		shell.setLayout(new FillLayout());
		shell.pack();

		ExpandBar coolBar = new ExpandBar(shell, SWT.NONE);
		ExpandItem item1 = new ExpandItem(coolBar, SWT.NONE);
		Label label1 = new Label(coolBar, SWT.NONE);
		label1.setText("Label 1");
		item1.setControl(label1);
		item1.setHeight(10);
		item1.setExpanded(true);

		var heightBeforeZoom = item1.getHeightInPixels();
		changeDPIZoom(scaledZoom);
		var heightAfterZoom = item1.getHeightInPixels();

		assertEquals("Height of a font of the button should be doubled after zooming to 200",
				heightBeforeZoom * 2, heightAfterZoom);
	}

	@Test
	public void testTabFolderSizeAfterZooming() {
		int zoom = DPIUtil.getDeviceZoom();
		int scaledZoom = zoom * 2;

		shell = new Shell(display);
		shell.setBounds(0, 0, 100, 160);
		shell.setLayout(new FillLayout());
		shell.pack();

		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setBounds(20, 20, 360, 240);

		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("Tab 1");
		Label label = new Label(tabFolder, SWT.NONE);
		label.setSize(200, 200);
		tabItem.setControl(label);

		Point tabItemSizeBeforeEvent = tabItem.getControl().getSize();
		changeDPIZoom(scaledZoom);
		Point tabItemSizeAfterEvent = tabItem.getControl().getSize();

		assertEquals("Width of a tab folder item should be halved in points after zooming to 200",
				tabItemSizeBeforeEvent.x / 2, tabItemSizeAfterEvent.x);
		assertEquals("Height of a tab folder item should be halved in points after zooming to 200",
				tabItemSizeBeforeEvent.y / 2, tabItemSizeAfterEvent.y);
	}

	@Test
	public void testTableAfterZooming() {
		int zoom = DPIUtil.getDeviceZoom();
		int scaledZoom = zoom * 2;

		shell = new Shell(display);
		shell.setBounds(0, 0, 100, 160);
		shell.setLayout(new FillLayout());
		shell.pack();

		Table table = new Table(shell, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		table.setBounds(20, 20, 360, 240);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn column = new TableColumn(table, SWT.NONE);
		column.setText("Column 1");
		column.setWidth(200);

		Font font = new Font(display, "Arial", 12, SWT.BOLD);

		TableItem item1 = new TableItem(table, SWT.NONE);
		item1.setText("Item 1");
		item1.setFont(font);

		for (TableColumn col : table.getColumns()) {
			col.pack();
		}

		int fontHeightBefore = item1.getFont().getFontData()[0].data.lfHeight;
		changeDPIZoom(scaledZoom);
		int fontHeightAfter = item1.getFont().getFontData()[0].data.lfHeight;

		assertEquals("Height of a font for table item should be doubled after zooming to 200",
				fontHeightBefore * 2, fontHeightAfter);
	}

	@Test
	public void testTreeAfterZooming() {
		int zoom = DPIUtil.getDeviceZoom();
		int scaledZoom = zoom * 2;

		shell = new Shell(display);
		shell.setBounds(0, 0, 100, 160);
		shell.setLayout(new FillLayout());
		shell.pack();

		Tree tree = new Tree(shell, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		tree.setBounds(20, 20, 360, 240);
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);

		TreeColumn column = new TreeColumn(tree, SWT.NONE);
		column.setText("Column 1");
		column.setWidth(200);

		Font font = new Font(display, "Arial", 12, SWT.BOLD);
		TreeItem item1 = new TreeItem(tree, SWT.NONE);
		item1.setText("Item 1");
		item1.setFont(font);
		for (TreeColumn col : tree.getColumns()) {
			col.pack();
		}

		int fontHeightBefore = item1.getFont().getFontData()[0].data.lfHeight;
		changeDPIZoom(scaledZoom);
		int fontHeightAfter = item1.getFont().getFontData()[0].data.lfHeight;

		assertEquals("Height of a font for tree item should be doubled after zooming to 200",
				fontHeightBefore * 2, fontHeightAfter);
	}

	@Test
	public void testCaretInStyledTextAfterZooming() {
		int zoom = DPIUtil.getDeviceZoom();
		int scaledZoom = zoom * 2;

		shell = new Shell(display);
		shell.setBounds(0, 0, 100, 160);
		shell.setLayout(new FillLayout());
		shell.pack();

		// Create the StyledText widget
		StyledText styledText = new StyledText(shell, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		styledText.setBounds(10, 10, 360, 200);

		// Set some text with different styles
		styledText.setText("This is an example of styled text.");

		// Apply bold style to a range of text
		StyleRange styleRange1 = new StyleRange();
		styleRange1.start = 10;
		styleRange1.length = 7;
		styleRange1.fontStyle = SWT.BOLD;
		styledText.setStyleRange(styleRange1);

		// Apply italic style to another range of text
		StyleRange styleRange2 = new StyleRange();
		styleRange2.start = 18;
		styleRange2.length = 6;
		styleRange2.fontStyle = SWT.ITALIC;
		styledText.setStyleRange(styleRange2);

		// Get the caret size
		Point caretSize = styledText.getCaret().getSizeInPixels();
		changeDPIZoom(scaledZoom);
		Point caretSize2 = styledText.getCaret().getSizeInPixels();

		assertEquals("Height of a Caret for Styled Text should be doubled after zooming to 200", caretSize.y * 2,
				caretSize2.y);

	}

}