package org.eclipse.swt.widgets;

import static org.junit.Assert.assertEquals;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
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
		assertEquals("The initial zoom is wrong", zoom, button.getZoom()); //pre-condition
		changeDPIZoom(scaledZoom);
		assertEquals("The Zoom Level should be updated for button on zoom change event on its shell", scaledZoom, button.getZoom());
	}
}
