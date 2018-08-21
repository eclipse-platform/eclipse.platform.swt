/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
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
 *     Red Hat Inc. - Bug 462631
 *******************************************************************************/
package org.eclipse.swt.tests.junit;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.ImageLoaderEvent;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.graphics.ImageLoaderEvent
 *
 * @see org.eclipse.swt.graphics.ImageLoaderEvent
 */
public class Test_org_eclipse_swt_graphics_ImageLoaderEvent {

@Test
public void test_ConstructorLorg_eclipse_swt_graphics_ImageLoaderLorg_eclipse_swt_graphics_ImageDataIZ() {
	try {
		new ImageLoaderEvent(null, null, 0, true);
		fail("No exception thrown for ImageLoader source == null");
	} catch (IllegalArgumentException e) {
	}

	new ImageLoaderEvent(new ImageLoader(), null, 0, true);
}

@Test
public void test_toString() {
	ImageLoaderEvent event = new ImageLoaderEvent(new ImageLoader(), null, 0, true);
	assertNotNull(event.toString());
	assertTrue(event.toString().length() > 0);
}

}
