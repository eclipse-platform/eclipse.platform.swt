/*******************************************************************************
 * Copyright (c) 2000, 2025 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * Suite for testing all of the graphics test cases.
 */
@Suite
@SelectClasses({ //
		// Sorted list of tests
		Test_org_eclipse_swt_graphics_Color.class, //
		Test_org_eclipse_swt_graphics_Cursor.class, //
		Test_org_eclipse_swt_graphics_DeviceData.class, //
		Test_org_eclipse_swt_graphics_Font.class, //
		Test_org_eclipse_swt_graphics_FontData.class, //
		Test_org_eclipse_swt_graphics_FontMetrics.class, //
		Test_org_eclipse_swt_graphics_GC.class, //
		Test_org_eclipse_swt_graphics_Image.class, //
		Test_org_eclipse_swt_graphics_ImageData.class, //
		Test_org_eclipse_swt_graphics_ImageLoader.class, //
		Test_org_eclipse_swt_graphics_ImageLoaderEvent.class, //
		Test_org_eclipse_swt_graphics_PaletteData.class, //
		Test_org_eclipse_swt_graphics_Path.class, //
		Test_org_eclipse_swt_graphics_Point.class, //
		Test_org_eclipse_swt_graphics_RGB.class, //
		Test_org_eclipse_swt_graphics_RGBA.class, //
		Test_org_eclipse_swt_graphics_Rectangle.class, //
		Test_org_eclipse_swt_graphics_Region.class, //
		Test_org_eclipse_swt_graphics_TextLayout.class, //
		Test_org_eclipse_swt_graphics_Transform.class, //
})
public class AllGraphicsTests {
}
