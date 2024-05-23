/*******************************************************************************
 * Copyright (c) 2024 Yatta Solutions
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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.DPIUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.internal.DPIUtil
 *
 * @see org.eclipse.swt.internal.DPIUtil
 */
@SuppressWarnings("restriction")
public class DPIUtilTests {

	private int deviceZoom;
	private boolean useCairoAutoScale;

	@Before
	public void setup() {
		deviceZoom = DPIUtil.getDeviceZoom();
		useCairoAutoScale = DPIUtil.useCairoAutoScale();
		DPIUtil.setDeviceZoom(200);
		DPIUtil.setUseCairoAutoScale(false);
	}

	@After
	public void tearDown() {
		DPIUtil.setUseCairoAutoScale(useCairoAutoScale);
		DPIUtil.setDeviceZoom(deviceZoom);
	}

	@Test
	public void scaleDownFloatArray() {
		float[] valueAt200 = new float[] { 2, 3, 4 };
		float[] valueAt150 = new float[] { 1.5f, 2.25f, 3 };
		float[] valueAt100 = new float[] { 1, 1.5f, 2 };

		float[] scaledValue = DPIUtil.autoScaleDown(valueAt200);
		assertArrayEquals(String.format("Scaling down float array from device zoom (%d) to 100 failed",
				DPIUtil.getDeviceZoom()), valueAt100, scaledValue, .001f);
		scaledValue = DPIUtil.autoScaleDown((Device) null, valueAt200);
		assertArrayEquals(String.format("Scaling down float array from device zoom (%d) to 100 with device failed",
				DPIUtil.getDeviceZoom()), valueAt100, scaledValue, .001f);

		scaledValue = DPIUtil.scaleDown(valueAt200, 200);
		assertArrayEquals("Scaling down float array from 200 failed", valueAt100, scaledValue, .001f);
		scaledValue = DPIUtil.scaleDown((Device) null, valueAt200, 200);
		assertArrayEquals("Scaling down float array from 200 with device failed", valueAt100, scaledValue, .001f);
		scaledValue = DPIUtil.scaleDown(valueAt150, 150);
		assertArrayEquals("Scaling down float array from 150 failed", valueAt100, scaledValue, .001f);
		scaledValue = DPIUtil.scaleDown((Device) null, valueAt150, 150);
		assertArrayEquals("Scaling down float array from 150 with device failed", valueAt100, scaledValue, .001f);
		scaledValue = DPIUtil.scaleDown(valueAt100, 100);
		assertSame("Scaling down float array without zoom change failed", valueAt100, scaledValue);
		scaledValue = DPIUtil.scaleDown((Device) null, valueAt100, 100);
		assertSame("Scaling down float array without zoom change with device failed", valueAt100, scaledValue);
	}

	@Test
	public void scaleDownInteger() {
		int valueAt200 = 10;
		int valueAt150 = 7;
		int valueAt100 = 5;

		int scaledValue = DPIUtil.autoScaleDown(valueAt200);
		assertEquals(
				String.format("Scaling down integer from device zoom (%d) to 100 failed", DPIUtil.getDeviceZoom()),
				valueAt100, scaledValue);
		scaledValue = DPIUtil.autoScaleDown((Device) null, valueAt200);
		assertEquals(String.format("Scaling down integer from device zoom (%d) to 100 with device failed",
				DPIUtil.getDeviceZoom()), valueAt100, scaledValue);

		scaledValue = DPIUtil.scaleDown(valueAt200, 200);
		assertEquals("Scaling down integer from 200 failed", valueAt100, scaledValue);
		scaledValue = DPIUtil.scaleDown((Device) null, valueAt200, 200);
		assertEquals("Scaling down integer from 200 with device failed", valueAt100, scaledValue);
		scaledValue = DPIUtil.scaleDown(valueAt150, 150);
		assertEquals("Scaling down integer from 150 failed", valueAt100, scaledValue);
		scaledValue = DPIUtil.scaleDown((Device) null, valueAt150, 150);
		assertEquals("Scaling down integer from 150 with device failed", valueAt100, scaledValue);
		scaledValue = DPIUtil.scaleDown(valueAt100, 100);
		assertSame("Scaling down integer without zoom change failed", valueAt100, scaledValue);
		scaledValue = DPIUtil.scaleDown((Device) null, valueAt100, 100);
		assertSame("Scaling down integer without zoom change with device failed", valueAt100, scaledValue);
	}

	@Test
	public void scaleDownFloat() {
		float valueAt200 = 10f;
		float valueAt150 = 7.5f;
		float valueAt100 = 5f;

		float scaledValue = DPIUtil.autoScaleDown(valueAt200);
		assertEquals(
				String.format("Scaling down float from device zoom (%d) to 100 failed", DPIUtil.getDeviceZoom()),
				valueAt100, scaledValue, .001f);
		scaledValue = DPIUtil.autoScaleDown((Device) null, valueAt200);
		assertEquals(String.format("Scaling down float from device zoom (%d) to 100 with device failed",
				DPIUtil.getDeviceZoom()), valueAt100, scaledValue, .001f);

		scaledValue = DPIUtil.scaleDown(valueAt200, 200);
		assertEquals("Scaling down float from 200 failed", valueAt100, scaledValue, .001f);
		scaledValue = DPIUtil.scaleDown((Device) null, valueAt200, 200);
		assertEquals("Scaling down float from 200 with device failed", valueAt100, scaledValue, .001f);
		scaledValue = DPIUtil.scaleDown(valueAt150, 150);
		assertEquals("Scaling down float from 150 failed", valueAt100, scaledValue, .001f);
		scaledValue = DPIUtil.scaleDown((Device) null, valueAt150, 150);
		assertEquals("Scaling down float from 150 with device failed", valueAt100, scaledValue, .001f);
		scaledValue = DPIUtil.scaleDown(valueAt100, 100);
		assertEquals("Scaling down float without zoom change failed", valueAt100, scaledValue, .001f);
		scaledValue = DPIUtil.scaleDown((Device) null, valueAt100, 100);
		assertEquals("Scaling down float without zoom change with device failed", valueAt100, scaledValue, .001f);
	}

	@Test
	public void scaleDownPoint() {
		Point valueAt200 = new Point(10, 14);
		Point valueAt150 = new Point(7, 10);
		Point valueAt100 = new Point(5, 7);

		Point scaledValue = DPIUtil.autoScaleDown(valueAt200);
		assertEquals(
				String.format("Scaling down Point from device zoom (%d) to 100 failed", DPIUtil.getDeviceZoom()),
				valueAt100, scaledValue);
		scaledValue = DPIUtil.autoScaleDown((Device) null, valueAt200);
		assertEquals(String.format("Scaling down Point from device zoom (%d) to 100 with device failed",
				DPIUtil.getDeviceZoom()), valueAt100, scaledValue);

		scaledValue = DPIUtil.scaleDown(valueAt200, 200);
		assertEquals("Scaling down Point from 200 failed", valueAt100, scaledValue);
		scaledValue = DPIUtil.scaleDown((Device) null, valueAt200, 200);
		assertEquals("Scaling down Point from 200 with device failed", valueAt100, scaledValue);
		scaledValue = DPIUtil.scaleDown(valueAt150, 150);
		assertEquals("Scaling down Point from 150 failed", valueAt100, scaledValue);
		scaledValue = DPIUtil.scaleDown((Device) null, valueAt150, 150);
		assertEquals("Scaling down Point from 150 with device failed", valueAt100, scaledValue);
		scaledValue = DPIUtil.scaleDown(valueAt100, 100);
		assertSame("Scaling down Point without zoom change failed", valueAt100, scaledValue);
		scaledValue = DPIUtil.scaleDown((Device) null, valueAt100, 100);
		assertSame("Scaling down Point without zoom change with device failed", valueAt100, scaledValue);
	}

	@Test
	public void scaleDownRectangle() {
		Rectangle valueAt200 = new Rectangle(100, 150, 10, 14);
		Rectangle valueAt150 = new Rectangle(75, 113, 7, 10);
		Rectangle valueAt100 = new Rectangle(50, 75, 5, 7);

		Rectangle scaledValue = DPIUtil.autoScaleDown(valueAt200);
		assertEquals(String.format("Scaling down Rectangle from device zoom (%d) to 100 failed",
				DPIUtil.getDeviceZoom()), valueAt100, scaledValue);
		scaledValue = DPIUtil.autoScaleDown((Device) null, valueAt200);
		assertEquals(String.format("Scaling down Rectangle from device zoom (%d) to 100 with device failed",
				DPIUtil.getDeviceZoom()), valueAt100, scaledValue);

		scaledValue = DPIUtil.scaleDown(valueAt200, 200);
		assertEquals("Scaling down Rectangle from 200 failed", valueAt100, scaledValue);
		scaledValue = DPIUtil.scaleDown((Device) null, valueAt200, 200);
		assertEquals("Scaling down Rectangle from 200 with device failed", valueAt100, scaledValue);
		scaledValue = DPIUtil.scaleDown(valueAt150, 150);
		assertEquals("Scaling down Rectangle from 150 failed", valueAt100, scaledValue);
		scaledValue = DPIUtil.scaleDown((Device) null, valueAt150, 150);
		assertEquals("Scaling down Rectangle from 150 with device failed", valueAt100, scaledValue);
		scaledValue = DPIUtil.scaleDown(valueAt100, 100);
		assertSame("Scaling down Rectangle without zoom change failed", valueAt100, scaledValue);
		scaledValue = DPIUtil.scaleDown((Device) null, valueAt100, 100);
		assertSame("Scaling down Rectangle without zoom change with device failed", valueAt100, scaledValue);
	}

	@Test
	public void scaleUpIntArray() {
		int[] valueAt200 = new int[] { 10, 12, 14 };
		int[] valueAt150 = new int[] { 8, 9, 11 };
		int[] valueAt100 = new int[] { 5, 6, 7 };

		int[] scaledValue = DPIUtil.autoScaleUp(valueAt100);
		assertArrayEquals(
				String.format("Scaling up int array to device zoom (%d) to 100 failed", DPIUtil.getDeviceZoom()),
				valueAt200, scaledValue);
		scaledValue = DPIUtil.autoScaleUp((Device) null, valueAt100);
		assertArrayEquals(String.format("Scaling up int array to device zoom (%d) to 100 with device failed",
				DPIUtil.getDeviceZoom()), valueAt200, scaledValue);

		scaledValue = DPIUtil.autoScaleUp(valueAt100, 200);
		assertArrayEquals("Scaling up int array to 200 failed", valueAt200, scaledValue);
		scaledValue = DPIUtil.autoScaleUp((Device) null, valueAt100, 200);
		assertArrayEquals("Scaling up int array to 200 with device failed", valueAt200, scaledValue);
		scaledValue = DPIUtil.autoScaleUp(valueAt100, 150);
		assertArrayEquals("Scaling up int array to 150 failed", valueAt150, scaledValue);
		scaledValue = DPIUtil.autoScaleUp((Device) null, valueAt100, 150);
		assertArrayEquals("Scaling up int array to 150 with device failed", valueAt150, scaledValue);
		scaledValue = DPIUtil.autoScaleUp(valueAt100, 100);
		assertSame("Scaling up int array without zoom change failed", valueAt100, scaledValue);
		scaledValue = DPIUtil.autoScaleUp((Device) null, valueAt100, 100);
		assertSame("Scaling up int array without zoom change with device failed", valueAt100, scaledValue);
	}

	@Test
	public void scaleUpInteger() {
		int valueAt200 = 10;
		int valueAt150 = 8;
		int valueAt100 = 5;

		int scaledValue = DPIUtil.autoScaleUp(valueAt100);
		assertEquals(String.format("Scaling up integer to device zoom (%d) to 100 failed", DPIUtil.getDeviceZoom()),
				valueAt200, scaledValue);
		scaledValue = DPIUtil.autoScaleUp((Device) null, valueAt100);
		assertEquals(String.format("Scaling up integer to device zoom (%d) to 100 with device failed",
				DPIUtil.getDeviceZoom()), valueAt200, scaledValue);

		scaledValue = DPIUtil.autoScaleUp(valueAt100, 200);
		assertEquals("Scaling up integer to 200 failed", valueAt200, scaledValue);
		scaledValue = DPIUtil.autoScaleUp((Device) null, valueAt100, 200);
		assertEquals("Scaling up integer to 200 with device failed", valueAt200, scaledValue);
		scaledValue = DPIUtil.autoScaleUp(valueAt100, 150);
		assertEquals("Scaling up integer to 150 failed", valueAt150, scaledValue);
		scaledValue = DPIUtil.autoScaleUp((Device) null, valueAt100, 150);
		assertEquals("Scaling up integer to 150 with device failed", valueAt150, scaledValue);
		scaledValue = DPIUtil.autoScaleUp(valueAt100, 100);
		assertSame("Scaling up integer without zoom change failed", valueAt100, scaledValue);
		scaledValue = DPIUtil.autoScaleUp((Device) null, valueAt100, 100);
		assertSame("Scaling up integer without zoom change with device failed", valueAt100, scaledValue);
	}

	@Test
	public void scaleUpFloat() {
		float valueAt200 = 10;
		float valueAt150 = 7.5f;
		float valueAt100 = 5;

		float scaledValue = DPIUtil.autoScaleUp(valueAt100);
		assertEquals(String.format("Scaling up integer to device zoom (%d) to 100 failed", DPIUtil.getDeviceZoom()),
				valueAt200, scaledValue, 0.001f);
		scaledValue = DPIUtil.autoScaleUp((Device) null, valueAt100);
		assertEquals(String.format("Scaling up integer to device zoom (%d) to 100 with device failed",
				DPIUtil.getDeviceZoom()), valueAt200, scaledValue, 0.001f);

		scaledValue = DPIUtil.autoScaleUp(valueAt100, 200);
		assertEquals("Scaling up integer to 200 failed", valueAt200, scaledValue, 0.001f);
		scaledValue = DPIUtil.autoScaleUp((Device) null, valueAt100, 200);
		assertEquals("Scaling up integer to 200 with device failed", valueAt200, scaledValue, 0.001f);
		scaledValue = DPIUtil.autoScaleUp(valueAt100, 150);
		assertEquals("Scaling up integer to 150 failed", valueAt150, scaledValue, 0.001f);
		scaledValue = DPIUtil.autoScaleUp((Device) null, valueAt100, 150);
		assertEquals("Scaling up integer to 150 with device failed", valueAt150, scaledValue, 0.001f);
		scaledValue = DPIUtil.autoScaleUp(valueAt100, 100);
		assertEquals("Scaling up integer without zoom change failed", valueAt100, scaledValue, 0.001f);
		scaledValue = DPIUtil.autoScaleUp((Device) null, valueAt100, 100);
		assertEquals("Scaling up integer without zoom change with device failed", valueAt100, scaledValue, 0.001f);
	}

	@Test
	public void scaleUpPoint() {
		Point valueAt200 = new Point(10, 14);
		Point valueAt150 = new Point(8, 11);
		Point valueAt100 = new Point(5, 7);

		Point scaledValue = DPIUtil.autoScaleUp(valueAt100);
		assertEquals(String.format("Scaling up Point to device zoom (%d) to 100 failed", DPIUtil.getDeviceZoom()),
				valueAt200, scaledValue);
		scaledValue = DPIUtil.autoScaleUp((Device) null, valueAt100);
		assertEquals(String.format("Scaling up Point to device zoom (%d) to 100 with device failed",
				DPIUtil.getDeviceZoom()), valueAt200, scaledValue);

		scaledValue = DPIUtil.autoScaleUp(valueAt100, 200);
		assertEquals("Scaling up Point to 200 failed", valueAt200, scaledValue);
		scaledValue = DPIUtil.autoScaleUp((Device) null, valueAt100, 200);
		assertEquals("Scaling up Point to 200 with device failed", valueAt200, scaledValue);
		scaledValue = DPIUtil.autoScaleUp(valueAt100, 150);
		assertEquals("Scaling up Point to 150 failed", valueAt150, scaledValue);
		scaledValue = DPIUtil.autoScaleUp((Device) null, valueAt100, 150);
		assertEquals("Scaling up Point to 150 with device failed", valueAt150, scaledValue);
		scaledValue = DPIUtil.autoScaleUp(valueAt100, 100);
		assertSame("Scaling up Point without zoom change failed", valueAt100, scaledValue);
		scaledValue = DPIUtil.autoScaleUp((Device) null, valueAt100, 100);
		assertSame("Scaling up Point without zoom change with device failed", valueAt100, scaledValue);
	}

	@Test
	public void scaleUpRectangle() {
		Rectangle valueAt200 = new Rectangle(100, 150, 10, 14);
		Rectangle valueAt150 = new Rectangle(75, 113, 8, 10);
		Rectangle valueAt100 = new Rectangle(50, 75, 5, 7);

		Rectangle scaledValue = DPIUtil.autoScaleUp(valueAt100);
		assertEquals(
				String.format("Scaling up Rectangle to device zoom (%d) to 100 failed", DPIUtil.getDeviceZoom()),
				valueAt200, scaledValue);
		scaledValue = DPIUtil.autoScaleUp((Device) null, valueAt100);
		assertEquals(String.format("Scaling up Rectangle to device zoom (%d) to 100 with device failed",
				DPIUtil.getDeviceZoom()), valueAt200, scaledValue);

		scaledValue = DPIUtil.autoScaleUp(valueAt100, 200);
		assertEquals("Scaling up Rectangle to 200 failed", valueAt200, scaledValue);
		scaledValue = DPIUtil.autoScaleUp((Device) null, valueAt100, 200);
		assertEquals("Scaling up Rectangle to 200 with device failed", valueAt200, scaledValue);
		scaledValue = DPIUtil.autoScaleUp(valueAt100, 150);
		assertEquals("Scaling up Rectangle to 150 failed", valueAt150, scaledValue);
		scaledValue = DPIUtil.autoScaleUp((Device) null, valueAt100, 150);
		assertEquals("Scaling up Rectangle to 150 with device failed", valueAt150, scaledValue);
		scaledValue = DPIUtil.autoScaleUp(valueAt100, 100);
		assertSame("Scaling up Rectangle without zoom change failed", valueAt100, scaledValue);
		scaledValue = DPIUtil.autoScaleUp((Device) null, valueAt100, 100);
		assertSame("Scaling up Rectangle without zoom change with device failed", valueAt100, scaledValue);
	}
}
