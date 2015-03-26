/*******************************************************************************
 * Copyright (c) 2000, 2014 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
