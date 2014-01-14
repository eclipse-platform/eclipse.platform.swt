/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
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

import org.eclipse.swt.graphics.DeviceData;

/**
 * Automated Test Suite for class org.eclipse.swt.graphics.DeviceData
 *
 * @see org.eclipse.swt.graphics.DeviceData
 */
public class Test_org_eclipse_swt_graphics_DeviceData extends TestCase {

public void test_Constructor() {
	DeviceData data = new DeviceData();
	data.debug = true;
	data.tracking = true;
}

}
