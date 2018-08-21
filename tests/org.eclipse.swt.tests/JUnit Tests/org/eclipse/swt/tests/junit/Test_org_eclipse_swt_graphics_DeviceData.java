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


import org.eclipse.swt.graphics.DeviceData;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.graphics.DeviceData
 *
 * @see org.eclipse.swt.graphics.DeviceData
 */
public class Test_org_eclipse_swt_graphics_DeviceData {

@Test
public void test_Constructor() {
	DeviceData data = new DeviceData();
	data.debug = true;
	data.tracking = true;
}

}
