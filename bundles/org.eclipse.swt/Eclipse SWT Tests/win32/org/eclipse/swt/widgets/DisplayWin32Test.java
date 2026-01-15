package org.eclipse.swt.widgets;

import static org.junit.jupiter.api.Assertions.*;

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;

@ExtendWith(PlatformSpecificExecutionExtension.class)
@ExtendWith(ResetMonitorSpecificScalingExtension.class)
class DisplayWin32Test {

	@Test
	public void monitorSpecificScaling_activate() {
		Win32DPIUtils.setMonitorSpecificScaling(true);
		Display display = Display.getDefault();
		assertTrue(display.isRescalingAtRuntime());
		assertEquals(OS.DPI_AWARENESS_CONTEXT_PER_MONITOR_AWARE_V2, OS.GetThreadDpiAwarenessContext());
	}

	@Test
	public void monitorSpecificScaling_deactivate() {
		Win32DPIUtils.setMonitorSpecificScaling(false);
		Display display = Display.getDefault();
		assertFalse(display.isRescalingAtRuntime());
	}

	@Test
	public void monitorSpecificScaling_withCustomDpiAwareness() {
		System.setProperty(Win32DPIUtils.CUSTOM_DPI_AWARENESS_PROPERTY, "System");
		try {
			Win32DPIUtils.setMonitorSpecificScaling(true);
			Display display = Display.getDefault();
			assertTrue(display.isRescalingAtRuntime());
			assertEquals(OS.DPI_AWARENESS_CONTEXT_SYSTEM_AWARE, OS.GetThreadDpiAwarenessContext());
		} finally {
			System.clearProperty(Win32DPIUtils.CUSTOM_DPI_AWARENESS_PROPERTY);
		}
	}

	@Test
	public void customDpiAwareness_System() {
		System.setProperty(Win32DPIUtils.CUSTOM_DPI_AWARENESS_PROPERTY, "System");
		try {
			Display.getDefault();
			assertEquals(OS.DPI_AWARENESS_CONTEXT_SYSTEM_AWARE, OS.GetThreadDpiAwarenessContext());
		} finally {
			System.clearProperty(Win32DPIUtils.CUSTOM_DPI_AWARENESS_PROPERTY);
		}
	}

	@Test
	public void customDpiAwareness_PerMonitorV2() {
		System.setProperty(Win32DPIUtils.CUSTOM_DPI_AWARENESS_PROPERTY, "PerMonitorV2");
		try {
			Display.getDefault();
			assertEquals(OS.DPI_AWARENESS_CONTEXT_PER_MONITOR_AWARE_V2, OS.GetThreadDpiAwarenessContext());
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
		assertEquals(OS.DPI_AWARENESS_CONTEXT_PER_MONITOR_AWARE_V2, OS.GetThreadDpiAwarenessContext());
	}

	@Test
	@SuppressWarnings("removal")
	public void setRescaleAtRuntime_deactivate() {
		Display display = Display.getDefault();
		display.setRescalingAtRuntime(false);
		assertFalse(display.isRescalingAtRuntime());
		assertEquals(OS.DPI_AWARENESS_CONTEXT_SYSTEM_AWARE, OS.GetThreadDpiAwarenessContext());
	}

	@Test
	@SuppressWarnings("removal")
	public void setRescaleAtRuntime_toggling() {
		Display display = Display.getDefault();
		display.setRescalingAtRuntime(false);
		assertFalse(display.isRescalingAtRuntime());
		assertEquals(OS.DPI_AWARENESS_CONTEXT_SYSTEM_AWARE, OS.GetThreadDpiAwarenessContext());
		display.setRescalingAtRuntime(true);
		assertTrue(display.isRescalingAtRuntime());
		assertEquals(OS.DPI_AWARENESS_CONTEXT_PER_MONITOR_AWARE_V2, OS.GetThreadDpiAwarenessContext());
		display.setRescalingAtRuntime(false);
		assertFalse(display.isRescalingAtRuntime());
		assertEquals(OS.DPI_AWARENESS_CONTEXT_SYSTEM_AWARE, OS.GetThreadDpiAwarenessContext());
	}

}
