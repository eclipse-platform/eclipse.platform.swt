/*******************************************************************************
 * Copyright (c) 2026 Vector Informatik and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import org.eclipse.swt.tests.junit.AllNonBrowserTests.NonBrowserTestSuite;
import org.junit.platform.suite.api.AfterSuite;
import org.junit.platform.suite.api.BeforeSuite;

/**
 * Suite for running most SWT test cases (all except for browser tests) with non-default autoscaling settings.
 * It is currently executed in I-Build tests (via test.xml) and may be executed for local testing.
 */
@NonBrowserTestSuite
public class AllNonBrowserTests_AutoscaleOsNonDefaults extends AllNonBrowserTests {

	@BeforeSuite
	static void setNonDefaultAutoscale() {
		System.setProperty("swt.autoScale", "quarter");
		System.setProperty("swt.autoScale.updateOnRuntime", "true");
	}

	@AfterSuite
	static void restoreDefaultAutoscale() {
		System.clearProperty("swt.autoScale");
		System.clearProperty("swt.autoScale.updateOnRuntime");
	}


}
