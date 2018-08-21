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
 *******************************************************************************/
package org.eclipse.swt.tests.junit;


import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Suite for running all SWT test cases.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
	AllNonBrowserTests.class,
	AllBrowserTests.class
})
public class AllTests {

public static void main(String[] args) {
	JUnitCore.main(AllTests.class.getName());
}
}
