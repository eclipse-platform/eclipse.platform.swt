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

import org.eclipse.swt.internal.DPIUtil;
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

	public void scaleDownInteger() {
		int valueAt200 = 10;
		int valueAt150 = 7;
		int valueAt100 = 5;
		int scaledValue = DPIUtil.scaleDown(valueAt200, 200);
		assertEquals(valueAt100, scaledValue, "Scaling down integer from 200 failed");
		scaledValue = DPIUtil.scaleDown(valueAt150, 150);
		assertEquals(valueAt100, scaledValue, "Scaling down integer from 150 failed");
		scaledValue = DPIUtil.scaleDown(valueAt100, 100);
		assertSame(valueAt100, scaledValue, "Scaling down integer without zoom change failed");
	}

	@Test
	public void scaleDownFloat() {
		float valueAt200 = 10f;
		float valueAt150 = 7.5f;
		float valueAt100 = 5f;
		float scaledValue = DPIUtil.scaleDown(valueAt200, 200);
		assertEquals(valueAt100, scaledValue, .001f, "Scaling down float from 200 failed");
		scaledValue = DPIUtil.scaleDown(valueAt150, 150);
		assertEquals(valueAt100, scaledValue, .001f, "Scaling down float from 150 failed");
		scaledValue = DPIUtil.scaleDown(valueAt100, 100);
		assertEquals(valueAt100, scaledValue, .001f, "Scaling down float without zoom change failed");
	}

}
