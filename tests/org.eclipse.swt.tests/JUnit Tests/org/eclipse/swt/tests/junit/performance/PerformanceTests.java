/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
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
package org.eclipse.swt.tests.junit.performance;


import junit.framework.*;
import junit.textui.*;

/**
 * Suite for running SWT performance test cases.
 */
public class PerformanceTests extends TestSuite {

public static void main(String[] args) {
	TestRunner.run(suite());
}
public static Test suite() {
	return new PerformanceTests();
}

public PerformanceTests() {
	super();
	addTest(Test_situational.suite());
}
}
