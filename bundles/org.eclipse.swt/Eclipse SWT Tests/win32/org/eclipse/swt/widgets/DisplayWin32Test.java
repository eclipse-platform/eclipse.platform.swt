package org.eclipse.swt.widgets;

import static org.junit.jupiter.api.Assertions.*;

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;
import org.junit.jupiter.api.*;

public class DisplayWin32Test {

	private Display display;

	@BeforeAll
	public static void assumeIsFittingPlatform() {
		PlatformSpecificExecution.assumeIsFittingPlatform();
	}

	@BeforeEach
	public void createDisplay() {
		display = new Display();
	}

	@AfterEach
	public void destroyDisplay() {
		display.dispose();
	}

	@Test
	public void setRescaleAtRuntime_activate() {
		display.setRescalingAtRuntime(true);
		assertTrue(display.isRescalingAtRuntime());
		assertEquals(OS.DPI_AWARENESS_CONTEXT_PER_MONITOR_AWARE_V2, OS.GetThreadDpiAwarenessContext());
	}

	@Test
	public void setRescaleAtRuntime_deactivate() {
		display.setRescalingAtRuntime(false);
		assertFalse(display.isRescalingAtRuntime());
		assertEquals(OS.DPI_AWARENESS_CONTEXT_SYSTEM_AWARE, OS.GetThreadDpiAwarenessContext());
	}

	@Test
	public void setRescaleAtRuntime_toggling() {
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
