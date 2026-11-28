/*******************************************************************************
 * Copyright (c) 2026 Vector Informatik GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.tests.win32.widgets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Test_org_eclipse_swt_widgets_Shell {

	private static final int WM_GETICON = 0x7F;
	private static final int GCLP_HCURSOR = -12;
	private static final int GCLP_WNDPROC = -24;
	private static final int GCL_STYLE = -26;

	private Display display;

	@BeforeEach
	public void setup() {
		display = new Display();
	}

	@AfterEach
	public void teardown() {
		display.dispose();
	}

	@Test
	public void test_classIconsFollowImage() {
		Shell shell = new Shell(display);
		Image image = createImage(32, SWT.COLOR_RED);
		shell.setImage(image);
		assertClassIconsEqualWindowIcons(shell);

		Image otherImage = createImage(32, SWT.COLOR_BLUE);
		shell.setImage(otherImage);
		assertClassIconsEqualWindowIcons(shell);

		shell.dispose();
		image.dispose();
		otherImage.dispose();
	}

	@Test
	public void test_classIconsFollowImages() {
		Shell shell = new Shell(display);
		Image[] images = { createImage(16, SWT.COLOR_RED), createImage(32, SWT.COLOR_RED), createImage(48, SWT.COLOR_RED) };
		shell.setImages(images);
		assertClassIconsEqualWindowIcons(shell);
		assertNotEquals(getWindowIcon(shell, OS.ICON_SMALL), getWindowIcon(shell, OS.ICON_BIG));

		shell.dispose();
		for (Image image : images) {
			image.dispose();
		}
	}

	@Test
	public void test_classIconsResetWhenImageRemoved() {
		Shell shell = new Shell(display);
		long defaultClassIcon = OS.GetClassLongPtr(shell.handle, OS.GCLP_HICON);
		Image image = createImage(32, SWT.COLOR_RED);
		shell.setImage(image);
		long smallIconOfImage = getWindowIcon(shell, OS.ICON_SMALL);

		shell.setImage(null);
		assertEquals(defaultClassIcon, OS.GetClassLongPtr(shell.handle, OS.GCLP_HICON));
		assertNotEquals(smallIconOfImage, OS.GetClassLongPtr(shell.handle, OS.GCLP_HICONSM));

		shell.dispose();
		image.dispose();
	}

	@Test
	public void test_topLevelShellsHaveSeparateClassIcons() {
		Shell redShell = new Shell(display);
		Shell blueShell = new Shell(display);
		Image red = createImage(32, SWT.COLOR_RED);
		Image blue = createImage(32, SWT.COLOR_BLUE);
		redShell.setImage(red);
		blueShell.setImage(blue);

		assertClassIconsEqualWindowIcons(redShell);
		assertClassIconsEqualWindowIcons(blueShell);
		assertNotEquals(getClassName(redShell), getClassName(blueShell));

		redShell.dispose();
		blueShell.dispose();
		red.dispose();
		blue.dispose();
	}

	@Test
	public void test_childShellKeepsSharedClassIcons() {
		Shell parent = new Shell(display);
		Shell child = new Shell(parent);
		long sharedClassIcon = OS.GetClassLongPtr(child.handle, OS.GCLP_HICON);
		Image image = createImage(32, SWT.COLOR_RED);
		child.setImage(image);

		assertEquals(sharedClassIcon, OS.GetClassLongPtr(child.handle, OS.GCLP_HICON));

		parent.dispose();
		image.dispose();
	}

	@Test
	public void test_toolShellKeepsSharedClassIcons() {
		Shell toolShell = new Shell(display, SWT.TOOL | SWT.TITLE | SWT.CLOSE);
		long sharedClassIcon = OS.GetClassLongPtr(toolShell.handle, OS.GCLP_HICON);
		Image image = createImage(32, SWT.COLOR_RED);
		toolShell.setImage(image);

		assertEquals(sharedClassIcon, OS.GetClassLongPtr(toolShell.handle, OS.GCLP_HICON));

		toolShell.dispose();
		image.dispose();
	}

	@Test
	public void test_ownClassMatchesSharedClass() {
		Shell shell = new Shell(display);
		Canvas canvas = new Canvas(shell, SWT.NONE);

		assertNotEquals(getClassName(canvas), getClassName(shell));
		for (int index : new int[] { GCL_STYLE, GCLP_HCURSOR, GCLP_WNDPROC }) {
			assertEquals(OS.GetClassLongPtr(canvas.handle, index), OS.GetClassLongPtr(shell.handle, index));
		}

		shell.dispose();
	}

	@Test
	public void test_classOfDisposedShellIsReused() {
		Shell firstShell = new Shell(display);
		String firstClassName = getClassName(firstShell);
		firstShell.dispose();

		Shell secondShell = new Shell(display);
		assertEquals(firstClassName, getClassName(secondShell));
		secondShell.dispose();
	}

	private Image createImage(int size, int color) {
		Image image = new Image(display, size, size);
		GC gc = new GC(image);
		gc.setBackground(display.getSystemColor(color));
		gc.fillRectangle(0, 0, size, size);
		gc.dispose();
		return image;
	}

	private static void assertClassIconsEqualWindowIcons(Shell shell) {
		assertNotEquals(0, getWindowIcon(shell, OS.ICON_BIG));
		assertEquals(getWindowIcon(shell, OS.ICON_BIG), OS.GetClassLongPtr(shell.handle, OS.GCLP_HICON));
		assertEquals(getWindowIcon(shell, OS.ICON_SMALL), OS.GetClassLongPtr(shell.handle, OS.GCLP_HICONSM));
	}

	private static long getWindowIcon(Shell shell, int type) {
		return OS.SendMessage(shell.handle, WM_GETICON, type, 0);
	}

	private static String getClassName(Control control) {
		char[] buffer = new char[128];
		int length = OS.GetClassName(control.handle, buffer, buffer.length);
		return new String(buffer, 0, length);
	}
}
