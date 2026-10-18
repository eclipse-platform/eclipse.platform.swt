/*******************************************************************************
 * Copyright (c) 2019 Paul Pazderski and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Paul Pazderski - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.win32;

import static org.eclipse.swt.tests.win32.SwtWin32TestUtil.assertImageDataEqualsIgnoringAlphaInData;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.HTMLTransfer;
import org.eclipse.swt.dnd.ImageTransfer;
import org.eclipse.swt.dnd.RTFTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.URLTransfer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageGcDrawer;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Some simple tests for drag and drop.
 * <p>
 * To test drag and drop the appropriate user input must be simulated. This
 * requires that the test window is visible/active/focused and if it fails may
 * move something or trigger other actions when the input is simulated but the
 * test window not at the expected position. This should be no problem for fully
 * automated tests but may disturb when run locally in background as part of a
 * larger test suite.
 * </p>
 */
public class Test_org_eclipse_swt_dnd_DND {

private Shell shell;

@BeforeEach
public void setUp() {
	shell = new Shell();
	shell.setLayout(new RowLayout());
	shell.setSize(300, 300);
	shell.open();
	try {
		SwtWin32TestUtil.processEvents(shell.getDisplay(), 1000, shell::isVisible);
	} catch (InterruptedException e) {
		fail("Initialization interrupted");
	}
	assertTrue(shell.isVisible(), "Shell not visible.");
}

@AfterEach
public void tearDown() {
	Display display = shell.getDisplay();
	display.dispose();
	assertTrue(display.isDisposed());
}

/**
 * DnD some random bytes using a custom transfer type and the abstract {@link ByteArrayTransfer}.
 */
@Test
public void testByteArrayTransfer() throws InterruptedException {
	final byte[] drag = new byte[1024];
	final byte[] drop;

	new Random(4).nextBytes(drag);
	drop = testTransferRoundtrip(new ByteArrayTransfer() {
		private final String TEST_TYPE = "SWT JUnit Transfer Test";
		private final int TEST_ID = registerType(TEST_TYPE);

		@Override
		protected String[] getTypeNames() {
			return new String[] { TEST_TYPE };
		}

		@Override
		protected int[] getTypeIds() {
			return new int[] { TEST_ID };
		}
	}, drag);
	assertArrayEquals(drag, drop, "Drop received other data as we dragged.");
}

/**
 * DnD some filenames using the {@link FileTransfer}.
 */
@Test
public void testFileTransfer() throws InterruptedException {
	final String[] drag = new String[] { "C:\\temp\\file1", "C:\\temp\\file2" };
	final String[] drop;

	drop = testTransferRoundtrip(FileTransfer.getInstance(), drag);
	assertArrayEquals(drag, drop, "Drop received other data as we dragged.");
}

/**
 * DnD some HTML using the {@link HTMLTransfer}.
 */
@Test
public void testHtmlTransfer() throws InterruptedException {
	final String drag = "<p>This is a paragraph of text.</p>";
	final String drop;

	drop = testTransferRoundtrip(HTMLTransfer.getInstance(), drag);
	assertEquals(drag, drop, "Drop received other data as we dragged.");
}

/**
 * DnD ImageData retrieved from an image (created as DDB) using the {@link ImageTransfer}.
 */
@Test
public void testImageTransfer_fromImage() throws InterruptedException {
	final Image image = createTestImage();
	try {
		final ImageData drag = image.getImageData();
		final ImageData drop = testTransferRoundtrip(ImageTransfer.getInstance(), drag);
		assertImageDataEqualsIgnoringAlphaInData(drag, drop);
	} finally {
		image.dispose();
	}
}

/**
 * DnD ImageData created from image copy using the {@link ImageTransfer}.
 */
@Test
public void testImageTransfer_fromCopiedImage() throws InterruptedException {
	final Image image = createTestImage();
	try {
		final ImageData drag = new Image(shell.getDisplay(), image, SWT.IMAGE_COPY).getImageData();
		final ImageData drop = testTransferRoundtrip(ImageTransfer.getInstance(), drag);
		assertImageDataEqualsIgnoringAlphaInData(drag, drop);
	} finally {
		image.dispose();
	}
}

/**
 * DnD manually created ImageData using the {@link ImageTransfer}.
 */
@Test
public void testImageTransfer_fromImageData() throws InterruptedException {
	final ImageData imageData = new ImageData(16, 16, 32, new PaletteData(0xFF00, 0xFF0000, 0xFF000000));
	for (int i = 0; i < imageData.data.length; i++) {
		imageData.data[i] = (byte) (i % 3 == 0 ? 128 : 0);
	}
	final ImageData drag = imageData;
	final ImageData drop = testTransferRoundtrip(ImageTransfer.getInstance(), drag);
	assertImageDataEqualsIgnoringAlphaInData(drag, drop);
}

/**
 * DnD ImageData created from image data using the {@link ImageTransfer}.
 */
@Test
public void testImageTransfer_fromImageDataFromImage() throws InterruptedException {
	final Image image = createTestImage();
	try {
		Image imageFromImageData = new Image(shell.getDisplay(), image.getImageData());
		try {
			final ImageData drag = imageFromImageData.getImageData();
			final ImageData drop = testTransferRoundtrip(ImageTransfer.getInstance(), drag);
			assertImageDataEqualsIgnoringAlphaInData(drag, drop);
		} finally {
			imageFromImageData.dispose();
		}
	} finally {
		image.dispose();
	}
}

/**
 * DnD some formatted text using the {@link RTFTransfer}.
 */
@Test
public void testRtfTransfer() throws InterruptedException {
	final String drag = "{\\rtf1{\\colortbl;\\red255\\green0\\blue0;}\\uc1\\b\\i Hello World}";
	final String drop;

	drop = testTransferRoundtrip(RTFTransfer.getInstance(), drag);
	assertEquals(drag, drop, "Drop received other data as we dragged.");
}

/**
 * DnD some simple text using the {@link TextTransfer}.
 */
@Test
public void testTextTransfer() throws InterruptedException {
	final String drag = "Hello World";
	final String drop;

	drop = testTransferRoundtrip(TextTransfer.getInstance(), drag);
	assertEquals(drag, drop, "Drop received other data as we dragged.");
}

/**
 * DnD an URL using the {@link URLTransfer}.
 */
@Test
public void testUrlTransfer() throws InterruptedException {
	final String drag = "https://www.eclipse.org";
	final String drop;

	drop = testTransferRoundtrip(URLTransfer.getInstance(), drag);
	assertEquals(drag, drop, "Drop received other data as we dragged.");
}

/**
 * Creates a DDB test image with a uniform color applied to all pixels.
 */
private Image createTestImage() {
	try {
		Color color = shell.getDisplay().getSystemColor(SWT.COLOR_DARK_BLUE);
		final ImageGcDrawer imageGcDrawer = (gc, width, height) -> {
			gc.setBackground(color);
			gc.fillRectangle(0, 0, width, height);
		};
		return new Image(shell.getDisplay(), imageGcDrawer, 16, 16);
	} catch (Exception e) {
		fail("test image could not be initialized: " + e);
	}
	return null;
}

/**
 * Test transfer implementation by dragging and dropping some data onto ourself.
 *
 * @param <T> the data's type
 * @param transfer the transfer type to test
 * @param data the data to drag and drop
 * @return the data received from the drop
 */
private <T> T testTransferRoundtrip(Transfer transfer, T data) throws InterruptedException {
	int maxTries = 3;
	AtomicBoolean dropped = new AtomicBoolean(false);
	AtomicReference<T> droppedData = new AtomicReference<>(null);

	DragSource source = new DragSource(shell, DND.DROP_LINK | DND.DROP_COPY | DND.DROP_MOVE);
	source.setTransfer(transfer);
	source.addListener(DND.DragSetData, event -> event.data = data);

	DropTarget target = new DropTarget(shell, DND.DROP_DEFAULT | DND.DROP_COPY | DND.DROP_LINK | DND.DROP_MOVE);
	target.setTransfer(transfer);
	target.addListener(DND.Drop, event -> {
		try {
			@SuppressWarnings("unchecked")
			T o = (T) event.data;
			droppedData.set(o);
		} catch (ClassCastException ex) {
		}
		dropped.set(true);
	});

	do {
		postDragAndDropEvents();
		SwtWin32TestUtil.processEvents(shell.getDisplay(), 2000, dropped::get);
	} while(!dropped.get() && --maxTries > 0);

	assertTrue(dropped.get(), "No drop received.");
	assertNotNull(droppedData.get(), "No data was dropped.");

	return droppedData.get();
}

/**
 * Posts the events required to do a drag and drop. (i.e. click and hold mouse button and move mouse)
 * <p>
 * The caller is responsible to ensure the generated events are processed by the event loop.
 * </p>
 */
private void postDragAndDropEvents() {
	shell.forceActive();
	assertTrue(shell.forceFocus(), "Test shell requires input focus.");
	Event event = new Event();
	Point pt = shell.toDisplay(50, 50);
	event.x = pt.x;
	event.y = pt.y;
	event.type = SWT.MouseMove;
	shell.getDisplay().post(event);
	event.button = 1;
	event.count = 1;
	event.type = SWT.MouseDown;
	shell.getDisplay().post(event);
	event.x += 30;
	event.type = SWT.MouseMove;
	shell.getDisplay().post(event);
	event.type = SWT.MouseUp;
	shell.getDisplay().post(event);
}
}
