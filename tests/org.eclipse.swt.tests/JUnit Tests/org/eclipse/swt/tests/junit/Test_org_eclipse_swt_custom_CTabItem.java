/*******************************************************************************
 * Copyright (c) 2000, 2026 IBM Corporation and others.
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
package org.eclipse.swt.tests.junit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.custom.CTabItem
 *
 * @see org.eclipse.swt.custom.CTabItem
 */
public class Test_org_eclipse_swt_custom_CTabItem extends Test_org_eclipse_swt_widgets_Item {

	CTabFolder cTabFolder;
	CTabItem cTabItem;

@Override
@BeforeEach
public void setUp() {
	super.setUp();
	cTabFolder = new CTabFolder(shell, SWT.NONE);
	cTabItem = new CTabItem(cTabFolder, SWT.NONE);
	setWidget(cTabItem);
}

@Override
@Test
public void test_setImageLorg_eclipse_swt_graphics_Image() {
}

@Override
@Test
public void test_setTextLjava_lang_String() {
}

@Test
public void test_setForegroundLorg_eclipse_swt_graphics_Color() {
	Color red = cTabFolder.getDisplay().getSystemColor(SWT.COLOR_RED);
	cTabFolder.setForeground(red);

	Color blue = cTabFolder.getDisplay().getSystemColor(SWT.COLOR_BLUE);
	cTabItem.setForeground(blue);
	assertEquals(blue, cTabItem.getForeground());

	cTabItem.setForeground(null);
	assertEquals(red, cTabItem.getForeground());
}

@Test
public void test_setSelectionForegroundLorg_eclipse_swt_graphics_Color() {
	Color red = cTabFolder.getDisplay().getSystemColor(SWT.COLOR_RED);
	cTabFolder.setSelectionForeground(red);

	Color blue = cTabFolder.getDisplay().getSystemColor(SWT.COLOR_BLUE);
	cTabItem.setSelectionForeground(blue);
	assertEquals(blue, cTabItem.getSelectionForeground());

	cTabItem.setSelectionForeground(null);
	assertEquals(red, cTabItem.getSelectionForeground());
}

@Test
public void test_setBackgroundLorg_eclipse_swt_graphics_Color() {
	assertBackgroundSetter(CTabItem::setBackground, CTabItem::getBackground);
}

@Test
public void test_setSelectionBackgroundLorg_eclipse_swt_graphics_Color() {
	assertBackgroundSetter(CTabItem::setSelectionBackground, CTabItem::getSelectionBackground);
}

private void assertBackgroundSetter(BiConsumer<CTabItem, Color> setter, Function<CTabItem, Color> getter) {
	cTabFolder.setBackground(cTabFolder.getDisplay().getSystemColor(SWT.COLOR_RED));
	cTabFolder.setSelectionBackground(cTabFolder.getDisplay().getSystemColor(SWT.COLOR_RED));
	assertNull(getter.apply(cTabItem));

	Color blue = cTabFolder.getDisplay().getSystemColor(SWT.COLOR_BLUE);
	setter.accept(cTabItem, blue);
	assertEquals(blue, getter.apply(cTabItem));

	setter.accept(cTabItem, null);
	assertNull(getter.apply(cTabItem));

	Color disposed = new Color(1, 2, 3);
	disposed.dispose();
	IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> setter.accept(cTabItem, disposed));
	assertEquals("Argument not valid", e.getMessage());
	assertNull(getter.apply(cTabItem));

	CTabItem item = new CTabItem(cTabFolder, SWT.NONE);
	item.dispose();
	SWTException disposedItem = assertThrows(SWTException.class, () -> setter.accept(item, blue));
	assertEquals(SWT.ERROR_WIDGET_DISPOSED, disposedItem.code);
	assertThrows(SWTException.class, () -> getter.apply(item));
}

@Test
public void test_setBackgroundSameValueDoesNotRedraw() throws InterruptedException {
	cTabFolder.setSize(300, 100);
	shell.open();
	Color blue = cTabFolder.getDisplay().getSystemColor(SWT.COLOR_BLUE);
	cTabItem.setBackground(blue);
	cTabItem.setSelectionBackground(blue);
	AtomicInteger paints = new AtomicInteger();
	cTabFolder.addListener(SWT.Paint, e -> paints.incrementAndGet());
	waitUntilNoPaints(paints);

	paints.set(0);
	cTabItem.setBackground(blue);
	cTabItem.setSelectionBackground(blue);
	runPendingTabUpdate();
	cTabFolder.update();
	assertEquals(0, paints.get());

	cTabItem.setBackground(cTabFolder.getDisplay().getSystemColor(SWT.COLOR_GREEN));
	runPendingTabUpdate();
	cTabFolder.update();
	SwtTestUtil.processEvents(2000, () -> paints.get() > 0);
	assertTrue(paints.get() > 0);
}

/** Waits until the folder has not painted for a while, so paints from opening the shell are over. */
private void waitUntilNoPaints(AtomicInteger paints) throws InterruptedException {
	long deadline = System.currentTimeMillis() + 5000;
	int last;
	do {
		last = paints.get();
		cTabFolder.update();
		SwtTestUtil.processEvents(300, null);
	} while (paints.get() != last && System.currentTimeMillis() < deadline);
}

/** Runs the tab update the folder posts with asyncExec, by waiting for a runnable posted after it. */
private void runPendingTabUpdate() throws InterruptedException {
	AtomicBoolean done = new AtomicBoolean();
	cTabFolder.getDisplay().asyncExec(() -> done.set(true));
	SwtTestUtil.processEvents(5000, done::get);
	assertTrue(done.get());
}

@Test
public void test_itemBackgroundIsPainted() {
	Color folderBackground = new Color(255, 255, 255);
	Color red = new Color(255, 0, 0);
	Color green = new Color(0, 255, 0);
	cTabFolder.setBackground(folderBackground);
	cTabItem.setText("First");
	CTabItem colored = new CTabItem(cTabFolder, SWT.NONE);
	colored.setText("Colored");
	CTabItem third = new CTabItem(cTabFolder, SWT.NONE);
	third.setText("Third");
	colored.setBackground(red);
	colored.setSelectionBackground(green);
	cTabFolder.setSize(400, 100);
	cTabFolder.setSelection(0);
	SwtTestUtil.processEvents();

	ImageData unselected = paintFolder();
	assertEquals(red.getRGB(), pixelInTab(unselected, colored));
	assertEquals(folderBackground.getRGB(), pixelInTab(unselected, third));

	cTabFolder.setSelection(colored);
	SwtTestUtil.processEvents();
	ImageData selected = paintFolder();
	assertEquals(green.getRGB(), pixelInTab(selected, colored));
	assertEquals(folderBackground.getRGB(), pixelInTab(selected, cTabItem));
	assertEquals(folderBackground.getRGB(), pixelInTab(selected, third));
}

private ImageData paintFolder() {
	Point size = cTabFolder.getSize();
	Image image = new Image(cTabFolder.getDisplay(), size.x, size.y);
	GC gc = new GC(image);
	try {
		// the paint event alone does not erase the folder background, the OS does
		gc.setBackground(cTabFolder.getBackground());
		gc.fillRectangle(0, 0, size.x, size.y);
		Event event = new Event();
		event.gc = gc;
		event.setBounds(new Rectangle(0, 0, size.x, size.y));
		cTabFolder.notifyListeners(SWT.Paint, event);
	} finally {
		gc.dispose();
	}
	ImageData data = image.getImageData();
	image.dispose();
	return data;
}

/** Samples the left margin at mid-height, clear of the tab borders, the selection bar and the text. */
private static RGB pixelInTab(ImageData data, CTabItem item) {
	Rectangle bounds = item.getBounds();
	return data.palette.getRGB(data.getPixel(bounds.x + 2, bounds.y + bounds.height / 2));
}

@Test
public void test_setShowDirty() {
	assertFalse(cTabItem.getShowDirty());
	cTabItem.setShowDirty(true);
	assertTrue(cTabItem.getShowDirty());
	cTabItem.setShowDirty(false);
	assertFalse(cTabItem.getShowDirty());
}

@Test
public void test_dirtyIndicatorCloseStyle() {
	assertFalse(cTabFolder.getDirtyIndicatorStyle());
	cTabFolder.setDirtyIndicatorStyle(true);
	assertTrue(cTabFolder.getDirtyIndicatorStyle());
	cTabFolder.setDirtyIndicatorStyle(false);
	assertFalse(cTabFolder.getDirtyIndicatorStyle());
}
}
