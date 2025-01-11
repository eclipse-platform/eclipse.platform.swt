package org.eclipse.swt.widgets;

import static org.junit.jupiter.api.Assertions.*;

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;

@ExtendWith(PlatformSpecificExecutionExtension.class)
@ExtendWith(ResetMonitorSpecificScalingExtension.class)
public class DisplayWin32Test {

	@Test
	public void monitorSpecificScaling_activate() {
		DPIUtil.setMonitorSpecificScaling(true);
		Display display = Display.getDefault();
		assertTrue(display.isRescalingAtRuntime());
		assertEquals(OS.DPI_AWARENESS_CONTEXT_PER_MONITOR_AWARE_V2, OS.GetThreadDpiAwarenessContext());
	}

	@Test
	public void monitorSpecificScaling_deactivate() {
		DPIUtil.setMonitorSpecificScaling(false);
		Display display = Display.getDefault();
		assertFalse(display.isRescalingAtRuntime());
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
