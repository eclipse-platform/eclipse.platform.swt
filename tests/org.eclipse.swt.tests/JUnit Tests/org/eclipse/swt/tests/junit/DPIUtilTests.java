/*******************************************************************************
 * Copyright (c) 2024, 2025 Yatta Solutions and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Yatta Solutions - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.DPIUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;

/**
 * Automated Test Suite for class org.eclipse.swt.internal.DPIUtil
 *
 * @see org.eclipse.swt.internal.DPIUtil
 */
@SuppressWarnings("restriction")
@DisabledOnOs(value = org.junit.jupiter.api.condition.OS.LINUX, disabledReason = "Linux uses Cairo auto scaling, thus DPIUtil scaling is disabled")
public class DPIUtilTests {

	private int deviceZoom;
	private static final int ZOOM_200 = 200;

	@BeforeEach
	public void setup() {
		deviceZoom = DPIUtil.getDeviceZoom();
		DPIUtil.setDeviceZoom(ZOOM_200);
	}

	@AfterEach
	public void tearDown() {
		DPIUtil.setDeviceZoom(deviceZoom);
	}

	@Test
	public void scaleDownPoint() {
		Point valueAt200 = new Point(10, 14);
		Point valueAt150 = new Point(7, 10);
		Point valueAt100 = new Point(5, 7);

		Point scaledValue = DPIUtil.scaleDown(valueAt200, 100);
		assertEquals(valueAt100, scaledValue,
				String.format("Scaling down Point from device zoom (%d) to 100 failed", ZOOM_200));
		scaledValue = DPIUtil.scaleDown((Device) null, valueAt200, 100);
		assertEquals(valueAt100, scaledValue, String
				.format("Scaling down Point from device zoom (%d) to 100 with device failed", ZOOM_200));

		scaledValue = DPIUtil.scaleDown(valueAt200, 200);
		assertEquals(valueAt100, scaledValue, "Scaling down Point from 200 failed");
		scaledValue = DPIUtil.scaleDown((Device) null, valueAt200, 200);
		assertEquals(valueAt100, scaledValue, "Scaling down Point from 200 with device failed");
		scaledValue = DPIUtil.scaleDown(valueAt150, 150);
		assertEquals(valueAt100, scaledValue, "Scaling down Point from 150 failed");
		scaledValue = DPIUtil.scaleDown((Device) null, valueAt150, 150);
		assertEquals(valueAt100, scaledValue, "Scaling down Point from 150 with device failed");
		scaledValue = DPIUtil.scaleDown(valueAt100, 100);
		assertSame(valueAt100, scaledValue, "Scaling down Point without zoom change failed");
		scaledValue = DPIUtil.scaleDown((Device) null, valueAt100, 100);
		assertSame(valueAt100, scaledValue, "Scaling down Point without zoom change with device failed");
	}

	@Test
	public void scaleDownRectangle() {
		Rectangle valueAt200 = new Rectangle(100, 150, 10, 14);
		Rectangle valueAt150 = new Rectangle(75, 113, 7, 10);
		Rectangle valueAt100 = new Rectangle(50, 75, 5, 7);

		Rectangle scaledValue = DPIUtil.scaleDown(valueAt200, 100);
		assertEquals(valueAt100, scaledValue,
				String.format("Scaling down Rectangle from device zoom (%d) to 100 failed", ZOOM_200));
		scaledValue = DPIUtil.scaleDown((Device) null, valueAt200, 100);
		assertEquals(valueAt100, scaledValue, String.format(
				"Scaling down Rectangle from device zoom (%d) to 100 with device failed", ZOOM_200));

		scaledValue = DPIUtil.scaleDown(valueAt200, 200);
		assertEquals(valueAt100, scaledValue, "Scaling down Rectangle from 200 failed");
		scaledValue = DPIUtil.scaleDown((Device) null, valueAt200, 200);
		assertEquals(valueAt100, scaledValue, "Scaling down Rectangle from 200 with device failed");
		scaledValue = DPIUtil.scaleDown(valueAt150, 150);
		assertEquals(valueAt100, scaledValue, "Scaling down Rectangle from 150 failed");
		scaledValue = DPIUtil.scaleDown((Device) null, valueAt150, 150);
		assertEquals(valueAt100, scaledValue, "Scaling down Rectangle from 150 with device failed");
		scaledValue = DPIUtil.scaleDown(valueAt100, 100);
		assertSame(valueAt100, scaledValue, "Scaling down Rectangle without zoom change failed");
		scaledValue = DPIUtil.scaleDown((Device) null, valueAt100, 100);
		assertSame(valueAt100, scaledValue, "Scaling down Rectangle without zoom change with device failed");
	}

	@Test
	public void scaleUpInteger() {
		int valueAt200 = 10;
		int valueAt150 = 8;
		int valueAt100 = 5;

		int scaledValue = DPIUtil.scaleUp(valueAt100, 100);
		assertEquals(valueAt200, scaledValue,
				String.format("Scaling up integer to device zoom (%d) to 100 failed", ZOOM_200));
		scaledValue = DPIUtil.scaleUp((Device) null, valueAt100, 100);
		assertEquals(valueAt200, scaledValue, String
				.format("Scaling up integer to device zoom (%d) to 100 with device failed", ZOOM_200));

		scaledValue = DPIUtil.scaleUp(valueAt100, 200);
		assertEquals(valueAt200, scaledValue, "Scaling up integer to 200 failed");
		scaledValue = DPIUtil.scaleUp((Device) null, valueAt100, 200);
		assertEquals(valueAt200, scaledValue, "Scaling up integer to 200 with device failed");
		scaledValue = DPIUtil.scaleUp(valueAt100, 150);
		assertEquals(valueAt150, scaledValue, "Scaling up integer to 150 failed");
		scaledValue = DPIUtil.scaleUp((Device) null, valueAt100, 150);
		assertEquals(valueAt150, scaledValue, "Scaling up integer to 150 with device failed");
		scaledValue = DPIUtil.scaleUp(valueAt100, 100);
		assertSame(valueAt100, scaledValue, "Scaling up integer without zoom change failed");
		scaledValue = DPIUtil.scaleUp((Device) null, valueAt100, 100);
		assertSame(valueAt100, scaledValue,"Scaling up integer without zoom change with device failed");
	}

	@Test
	public void scaleUpFloat() {
		float valueAt200 = 10;
		float valueAt150 = 7.5f;
		float valueAt100 = 5;

		float scaledValue = DPIUtil.scaleUp(valueAt100, 100);
		assertEquals(valueAt200, scaledValue, 0.001f,
				String.format("Scaling up integer to device zoom (%d) to 100 failed", ZOOM_200));
		scaledValue = DPIUtil.scaleUp((Device) null, valueAt100, 100);
		assertEquals(valueAt200, scaledValue, 0.001f, String
				.format("Scaling up integer to device zoom (%d) to 100 with device failed", ZOOM_200));

		scaledValue = DPIUtil.scaleUp(valueAt100, 200);
		assertEquals(valueAt200, scaledValue, 0.001f, "Scaling up integer to 200 failed");
		scaledValue = DPIUtil.scaleUp((Device) null, valueAt100, 200);
		assertEquals(valueAt200, scaledValue, 0.001f, "Scaling up integer to 200 with device failed");
		scaledValue = DPIUtil.scaleUp(valueAt100, 150);
		assertEquals(valueAt150, scaledValue, 0.001f, "Scaling up integer to 150 failed");
		scaledValue = DPIUtil.scaleUp((Device) null, valueAt100, 150);
		assertEquals(valueAt150, scaledValue, 0.001f, "Scaling up integer to 150 with device failed");
		scaledValue = DPIUtil.scaleUp(valueAt100, 100);
		assertEquals(valueAt100, scaledValue, 0.001f, "Scaling up integer without zoom change failed");
		scaledValue = DPIUtil.scaleUp((Device) null, valueAt100, 100);
		assertEquals(valueAt100, scaledValue, 0.001f, "Scaling up integer without zoom change with device failed");
	}

	@Test
	public void scaleUpPoint() {
		Point valueAt200 = new Point(10, 14);
		Point valueAt150 = new Point(8, 11);
		Point valueAt100 = new Point(5, 7);

		Point scaledValue = DPIUtil.scaleUp(valueAt100, 100);
		assertEquals(valueAt200, scaledValue,
				String.format("Scaling up Point to device zoom (%d) to 100 failed", ZOOM_200));
		scaledValue = DPIUtil.scaleUp((Device) null, valueAt100, 100);
		assertEquals(valueAt200, scaledValue, String
				.format("Scaling up Point to device zoom (%d) to 100 with device failed", ZOOM_200));

		scaledValue = DPIUtil.scaleUp(valueAt100, 200);
		assertEquals(valueAt200, scaledValue, "Scaling up Point to 200 failed");
		scaledValue = DPIUtil.scaleUp((Device) null, valueAt100, 200);
		assertEquals(valueAt200, scaledValue, "Scaling up Point to 200 with device failed");
		scaledValue = DPIUtil.scaleUp(valueAt100, 150);
		assertEquals(valueAt150, scaledValue, "Scaling up Point to 150 failed");
		scaledValue = DPIUtil.scaleUp((Device) null, valueAt100, 150);
		assertEquals(valueAt150, scaledValue, "Scaling up Point to 150 with device failed");
		scaledValue = DPIUtil.scaleUp(valueAt100, 100);
		assertSame(valueAt100, scaledValue, "Scaling up Point without zoom change failed");
		scaledValue = DPIUtil.scaleUp((Device) null, valueAt100, 100);
		assertSame(valueAt100, scaledValue, "Scaling up Point without zoom change with device failed");
	}

	@Test
	public void scaleUpRectangle() {
		Rectangle valueAt200 = new Rectangle(100, 150, 10, 14);
		Rectangle valueAt150 = new Rectangle(75, 113, 8, 10);
		Rectangle valueAt100 = new Rectangle(50, 75, 5, 7);

		Rectangle scaledValue = DPIUtil.scaleUp(valueAt100, 100);
		assertEquals(valueAt200, scaledValue,
				String.format("Scaling up Rectangle to device zoom (%d) to 100 failed", ZOOM_200));
		scaledValue = DPIUtil.scaleUp((Device) null, valueAt100, 100);
		assertEquals(valueAt200, scaledValue, String
				.format("Scaling up Rectangle to device zoom (%d) to 100 with device failed", ZOOM_200));

		scaledValue = DPIUtil.scaleUp(valueAt100, 200);
		assertEquals(valueAt200, scaledValue, "Scaling up Rectangle to 200 failed");
		scaledValue = DPIUtil.scaleUp((Device) null, valueAt100, 200);
		assertEquals(valueAt200, scaledValue, "Scaling up Rectangle to 200 with device failed");
		scaledValue = DPIUtil.scaleUp(valueAt100, 150);
		assertEquals(valueAt150, scaledValue, "Scaling up Rectangle to 150 failed");
		scaledValue = DPIUtil.scaleUp((Device) null, valueAt100, 150);
		assertEquals(valueAt150, scaledValue, "Scaling up Rectangle to 150 with device failed");
		scaledValue = DPIUtil.scaleUp(valueAt100, 100);
		assertSame(valueAt100, scaledValue, "Scaling up Rectangle without zoom change failed");
		scaledValue = DPIUtil.scaleUp((Device) null, valueAt100, 100);
		assertSame(valueAt100, scaledValue, "Scaling up Rectangle without zoom change with device failed");
	}
}
