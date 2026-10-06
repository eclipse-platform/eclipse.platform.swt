/*******************************************************************************
 * Copyright (c) 2026 vogella GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.tests.gtk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.internal.Converter;
import org.eclipse.swt.internal.DPIUtil;
import org.eclipse.swt.internal.gtk.GTK;
import org.eclipse.swt.internal.gtk.OS;
import org.eclipse.swt.internal.gtk3.GTK3;
import org.eclipse.swt.internal.gtk4.GTK4;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests that only the scale-factor notification of the shell window changes the device zoom.
 */
@SuppressWarnings("restriction")
public class Test_GtkScaleFactor {

	private static final byte[] SCALE_FACTOR = Converter.javaStringToCString("scale-factor");

	private Display display;
	private Shell shell;
	private int nativeZoom;
	private int testZoom;
	private int scaleZoom;

	@BeforeEach
	public void setUp() {
		display = Display.getDefault();
		shell = new Shell(display);
		nativeZoom = DPIUtil.getNativeDeviceZoom();
		// a zoom that differs from the GTK scale factor makes every notification look like a change
		testZoom = nativeZoom * 2;
		DPIUtil.setDeviceZoom(testZoom);
		scaleZoom = GTK.gtk_widget_get_scale_factor(shell.handle) * 100;
		assumeTrue(testZoom != scaleZoom, "The test zoom matches the GTK scale factor");
	}

	@AfterEach
	public void tearDown() {
		DPIUtil.setDeviceZoom(nativeZoom);
		shell.dispose();
	}

	private static void notifyScaleFactor(long gtkWidget) {
		OS.g_object_notify(gtkWidget, SCALE_FACTOR);
	}

	private static long window(Shell shell) {
		return GTK.GTK4 ? GTK4.gtk_widget_get_root(shell.handle) : GTK3.gtk_widget_get_toplevel(shell.handle);
	}

	@Test
	public void test_onlyShellWindowChangesZoom() {
		List<Event> events = new ArrayList<>();
		shell.addListener(SWT.ZoomChanged, events::add);
		Button child = new Button(shell, SWT.PUSH);
		child.addListener(SWT.ZoomChanged, events::add);

		notifyScaleFactor(child.handle);
		assertEquals(0, events.size(), "A child must not send SWT.ZoomChanged");
		assertEquals(testZoom, DPIUtil.getNativeDeviceZoom(), "A child must not change the device zoom");

		notifyScaleFactor(window(shell));
		assertEquals(1, events.size(), "SWT.ZoomChanged expected");
		assertEquals(scaleZoom, DPIUtil.getNativeDeviceZoom());
		assertEquals(DPIUtil.getZoomForAutoscaleProperty(scaleZoom), events.get(0).detail, "Event.detail must be the new zoom");

		notifyScaleFactor(window(shell));
		assertEquals(1, events.size(), "An unchanged scale factor must not send SWT.ZoomChanged");
	}

	@Test
	public void test_fixedAutoScaleKeepsUnchangedScaleFactor() {
		DPIUtil.setDeviceZoom(scaleZoom);
		List<Event> events = new ArrayList<>();
		shell.addListener(SWT.ZoomChanged, events::add);
		DPIUtil.runWithAutoScaleValue(Integer.toString(scaleZoom * 2), () -> notifyScaleFactor(window(shell)));
		assertEquals(0, events.size(), "A fixed swt.autoScale must not turn an unchanged scale factor into a zoom change");
	}

	static class ScaleNotifyingStyledText extends StyledText {
		boolean constructed;
		boolean notified;

		ScaleNotifyingStyledText(Composite parent) {
			super(parent, SWT.NONE);
			constructed = true;
		}

		// called from Control.checkBackground, before the StyledText constructor body runs
		@Override
		public Shell getShell() {
			if (!notified) {
				notified = true;
				notifyScaleFactor(handle);
			}
			return super.getShell();
		}
	}

	@Test
	public void test_skinNotSentBeforeConstructionCompletes() {
		shell.setLayout(new FillLayout());
		boolean[] skinned = { false };
		boolean[] early = { false };
		Listener skinListener = e -> {
			if (e.widget instanceof ScaleNotifyingStyledText styledText) {
				skinned[0] = true;
				early[0] |= !styledText.constructed;
			}
		};
		display.addListener(SWT.Skin, skinListener);
		try {
			ScaleNotifyingStyledText text = new ScaleNotifyingStyledText(shell);
			assertTrue(text.notified, "The scale factor notification was not sent during construction");
			while (display.readAndDispatch()) {
				// delivers the queued SWT.Skin
			}
			assertTrue(skinned[0], "SWT.Skin event expected");
			assertFalse(early[0], "SWT.Skin was sent before the StyledText was fully constructed");
			assertEquals(testZoom, DPIUtil.getNativeDeviceZoom(), "A child must not change the device zoom");
		} finally {
			display.removeListener(SWT.Skin, skinListener);
		}
	}
}
