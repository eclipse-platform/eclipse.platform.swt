package org.eclipse.swt.widgets;

import static org.eclipse.swt.internal.DPIUtil.setMonitorSpecificScaling;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;

@ExtendWith(PlatformSpecificExecutionExtension.class)
@ExtendWith(ResetMonitorSpecificScalingExtension.class)
class DisplayWin32Test {

	@Test
	public void monitorSpecificScaling_activate() {
		setMonitorSpecificScaling(true);
		Display display = Display.getDefault();
		assertTrue(display.isRescalingAtRuntime());

		assertExpectedDpiAwareness(OS.DPI_AWARENESS_CONTEXT_PER_MONITOR_AWARE_V2);
	}

	private static void assertExpectedDpiAwareness(long expected) {
		long currentDpiAwareness = OS.GetThreadDpiAwarenessContext();
		boolean hasExpectedDpiAwareness = OS.AreDpiAwarenessContextsEqual(expected,currentDpiAwareness);
		assertTrue(hasExpectedDpiAwareness, "unexpected DPI awareness: " + currentDpiAwareness);
	}

	@Test
	public void monitorSpecificScaling_deactivate() {
		setMonitorSpecificScaling(false);
		Display display = Display.getDefault();
		assertFalse(display.isRescalingAtRuntime());
		assertExpectedDpiAwareness(OS.DPI_AWARENESS_CONTEXT_SYSTEM_AWARE);
	}

	@Test
	public void monitorSpecificScaling_withCustomDpiAwareness() {
		System.setProperty(Win32DPIUtils.CUSTOM_DPI_AWARENESS_PROPERTY, "System");
		try {
			setMonitorSpecificScaling(true);
			Display display = Display.getDefault();
			assertFalse(display.isRescalingAtRuntime());
			assertExpectedDpiAwareness(OS.DPI_AWARENESS_CONTEXT_SYSTEM_AWARE);
		} finally {
			System.clearProperty(Win32DPIUtils.CUSTOM_DPI_AWARENESS_PROPERTY);
		}
	}

	@Test
	public void customDpiAwareness_System() {
		System.setProperty(Win32DPIUtils.CUSTOM_DPI_AWARENESS_PROPERTY, "System");
		try {
			Display.getDefault();
			assertExpectedDpiAwareness(OS.DPI_AWARENESS_CONTEXT_SYSTEM_AWARE);
		} finally {
			System.clearProperty(Win32DPIUtils.CUSTOM_DPI_AWARENESS_PROPERTY);
		}
	}

	@Test
	public void customDpiAwareness_PerMonitorV2() {
		System.setProperty(Win32DPIUtils.CUSTOM_DPI_AWARENESS_PROPERTY, "PerMonitorV2");
		try {
			Display.getDefault();
			assertExpectedDpiAwareness(OS.DPI_AWARENESS_CONTEXT_PER_MONITOR_AWARE_V2);
		} finally {
			System.clearProperty(Win32DPIUtils.CUSTOM_DPI_AWARENESS_PROPERTY);
		}
	}

	@Test
	@SuppressWarnings("removal")
	public void setRescaleAtRuntime_activate() {
		Display display = Display.getDefault();
		display.setRescalingAtRuntime(true);
		assertTrue(display.isRescalingAtRuntime());
		assertExpectedDpiAwareness(OS.DPI_AWARENESS_CONTEXT_PER_MONITOR_AWARE_V2);
	}

	@Test
	@SuppressWarnings("removal")
	public void setRescaleAtRuntime_deactivate() {
		Display display = Display.getDefault();
		display.setRescalingAtRuntime(false);
		assertFalse(display.isRescalingAtRuntime());
		assertExpectedDpiAwareness(OS.DPI_AWARENESS_CONTEXT_SYSTEM_AWARE);
	}

	@Test
	@SuppressWarnings("removal")
	public void setRescaleAtRuntime_toggling() {
		Display display = Display.getDefault();
		display.setRescalingAtRuntime(false);
		assertFalse(display.isRescalingAtRuntime());
		assertExpectedDpiAwareness(OS.DPI_AWARENESS_CONTEXT_SYSTEM_AWARE);
		display.setRescalingAtRuntime(true);
		assertTrue(display.isRescalingAtRuntime());
		assertExpectedDpiAwareness(OS.DPI_AWARENESS_CONTEXT_PER_MONITOR_AWARE_V2);
		display.setRescalingAtRuntime(false);
		assertFalse(display.isRescalingAtRuntime());
		assertExpectedDpiAwareness(OS.DPI_AWARENESS_CONTEXT_SYSTEM_AWARE);
	}

}
