package org.eclipse.swt.widgets;

import static org.junit.Assert.assertEquals;

import java.io.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.layout.*;
import org.junit.*;

public class WidgetWin32Tests extends Win32AutoscaleTestBase {

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
		shell = new Shell(display);
		shell.setBounds(0, 0, 100, 160);
		shell.setLayout(new FillLayout());
		shell.pack();

		Button button = new Button(shell, SWT.PUSH);
		button.setText("Button");
		button.setBounds(0, 0, 100, 200);
		Point sizeBeforeEvent = button.getSize();
		Point p1 = button.computeSizeInPixels(sizeBeforeEvent.x, sizeBeforeEvent.y, false);
		changeDPIZoom(200);
		Point sizeAfterEvent = button.getSize();
		Point p2 = button.computeSizeInPixels(sizeAfterEvent.x, sizeAfterEvent.y, false);

		Assert.assertEquals("Width should be half in points after zooming to 200", p1.x, p2.x * 2);
		Assert.assertEquals("Height should be half in points after zooming to 200", p1.y, p2.y * 2);
	}

	@Test
	public void testImagePixelsWithDoubleZoomLevel() {
		InputStream inputStream = WidgetWin32Tests.class.getResourceAsStream("folder.png");
		Image image = new Image(display, inputStream);

		Point buttonImageSizeBeforeEvent = getImageDimension(image, 100);
		Point buttonImageSizeAfterEvent = getImageDimension(image, 200);

		Assert.assertEquals("Width of a button image should be doubled after zooming to 200",
				buttonImageSizeBeforeEvent.x * 2, buttonImageSizeAfterEvent.x);
		Assert.assertEquals("Height of a button image should be doubled after zooming to 200",
				buttonImageSizeBeforeEvent.y * 2, buttonImageSizeAfterEvent.y);
	}

	public Point getImageDimension(Image image, Integer zoomLevel) {
		BITMAP bm = new BITMAP();
		OS.GetObject(Image.win32_getHandle(image, zoomLevel), BITMAP.sizeof, bm);
		int imgWidth = bm.bmWidth;
		int imgHeight = bm.bmHeight;
		return new Point(imgWidth, imgHeight);
	}

	@Test
	public void testButtonFontAfterZooming() {
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
		changeDPIZoom(200);
		int heightAfterZoom = button.getFont().getFontData()[0].data.lfHeight;

		Assert.assertEquals("Height of a font of the button should be doubled after zooming to 200",
				heightBeforeZoom * 2, heightAfterZoom);
	}

	@Test
	public void testCoolItemAfterZooming() {
		shell = new Shell(display);
		shell.setBounds(0, 0, 100, 160);
		shell.setLayout(new FillLayout());
		shell.pack();

		CoolBar coolBar = new CoolBar(shell, SWT.NONE);
		CoolItem item1 = new CoolItem(coolBar, SWT.NONE);
		Label label1 = new Label(coolBar, SWT.NONE);
		label1.setText("Label 1");
		item1.setControl(label1);
		item1.setPreferredSize(label1.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		item1.setSize(item1.getPreferredSize());

		var preferredControlSize = item1.getPreferredSizeInPixels();
		int xBeforeZoom = preferredControlSize.x;
		int yBeforeZoom = preferredControlSize.y;
		changeDPIZoom(200);
		var preferredControlSize2 = item1.getPreferredSizeInPixels();
		int xAfterZoom = preferredControlSize2.x;
		int yAfterZoom = preferredControlSize2.y;

		Assert.assertTrue("Height of a Item should be greater after zooming to 200", xBeforeZoom < xAfterZoom);
		Assert.assertTrue("Width of a Item should be greater after zooming to 200", yBeforeZoom < yAfterZoom);
	}

	@Test
	public void testExpandItemAfterZooming() {
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
		changeDPIZoom(200);
		var heightAfterZoom = item1.getHeightInPixels();

		Assert.assertEquals("Height of a font of the button should be doubled after zooming to 200",
				heightBeforeZoom * 2, heightAfterZoom);
	}

	@Test
	public void testTabFolderSizeAfterZooming() {
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
		changeDPIZoom(200);
		Point tabItemSizeAfterEvent = tabItem.getControl().getSize();

		Assert.assertEquals("Width of a tab folder item should be halved in points after zooming to 200",
				tabItemSizeBeforeEvent.x, tabItemSizeAfterEvent.x * 2);
		Assert.assertEquals("Height of a tab folder item should be halved in points after zooming to 200",
				tabItemSizeBeforeEvent.y, tabItemSizeAfterEvent.y * 2);
	}

	@Test
	public void testTableAfterZooming() {
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
		changeDPIZoom(200);
		int fontHeightAfter = item1.getFont().getFontData()[0].data.lfHeight;

		Assert.assertEquals("Height of a font for table item should be doubled after zooming to 200",
				fontHeightBefore * 2, fontHeightAfter);
	}

	@Test
	public void testTreeAfterZooming() {
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
		changeDPIZoom(200);
		int fontHeightAfter = item1.getFont().getFontData()[0].data.lfHeight;

		Assert.assertEquals("Height of a font for tree item should be doubled after zooming to 200",
				fontHeightBefore * 2, fontHeightAfter);
	}

}
