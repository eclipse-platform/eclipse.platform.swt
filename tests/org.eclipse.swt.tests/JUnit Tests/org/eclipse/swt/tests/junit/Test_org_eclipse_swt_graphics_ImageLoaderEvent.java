/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;


import junit.framework.TestCase;

import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.ImageLoaderEvent;

/**
 * Automated Test Suite for class org.eclipse.swt.graphics.ImageLoaderEvent
 *
 * @see org.eclipse.swt.graphics.ImageLoaderEvent
 */
public class Test_org_eclipse_swt_graphics_ImageLoaderEvent extends TestCase {

public void test_ConstructorLorg_eclipse_swt_graphics_ImageLoaderLorg_eclipse_swt_graphics_ImageDataIZ() {
	try {
		new ImageLoaderEvent(null, null, 0, true);
		fail("No exception thrown for ImageLoader source == null");
	} catch (IllegalArgumentException e) {
	}
	
	new ImageLoaderEvent(new ImageLoader(), null, 0, true);
}

public void test_toString() {
	ImageLoaderEvent event = new ImageLoaderEvent(new ImageLoader(), null, 0, true);
	assertNotNull(event.toString());
	assertTrue(event.toString().length() > 0);
}

}
