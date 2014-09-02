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


import junit.framework.*;
import junit.textui.*;

/**
 * Suite for running all SWT test cases.
 */
public class AllTests extends TestSuite {

public static void main(String[] args) {
	TestRunner.run(suite());
}
public static Test suite() {
	return new AllTests();
}

public AllTests() {
	super(AllTests.class.getName());
	
	addTest(AllNonBrowserTests.suite());
	addTest(AllBrowserTests.suite());
}
}
