/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit.performance;

import java.util.*;
import junit.framework.*;
import junit.textui.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.tests.junit.SwtJunit;

/**
 * Automated Performance Test Suite for class org.eclipse.swt.graphics.FontData
 *
 * @see org.eclipse.swt.graphics.FontData
 */
public class Test_org_eclipse_swt_graphics_FontData extends SwtPerformanceTestCase {
	static final int COUNT = 10000;

public Test_org_eclipse_swt_graphics_FontData(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

public void test_Constructor() {
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		new FontData();
	}
	stopMeasuring();
	commitMeasurements();
	assertPerformance();
}

public void test_ConstructorLjava_lang_String() {
	FontData fd = new FontData(SwtJunit.testFontName, 10, SWT.NORMAL);
	String fdString = fd.toString();
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		new FontData(fdString);
	}
	stopMeasuring();
	commitMeasurements();
	assertPerformance();
}

public void test_ConstructorLjava_lang_StringII() {
	FontData fd = new FontData(SwtJunit.testFontName, 10, SWT.NORMAL);
	String fdString = fd.toString();
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		new FontData(SwtJunit.testFontName, 10, SWT.NORMAL);
	}
	stopMeasuring();
	commitMeasurements();
	assertPerformance();

}

public void test_equalsLjava_lang_Object() {
	FontData fd1 = new FontData(SwtJunit.testFontName, 10, SWT.NORMAL);
	FontData fd2 = new FontData(SwtJunit.testFontName, 10, SWT.NORMAL);
	FontData fd3 = new FontData(SwtJunit.testFontName, 12, SWT.NORMAL);
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		fd1.equals(fd2);	// equal	
	}
	stopMeasuring();
	commitMeasurements();
	assertPerformance();
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		fd1.equals(fd3);	// not equal
	}
	stopMeasuring();
	commitMeasurements();
	assertPerformance();
}

public void test_getHeight() {
	FontData data = new FontData(SwtJunit.testFontName, 10, SWT.NORMAL);
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		data.getHeight();
	}
	stopMeasuring();
	
	commitMeasurements();
	assertPerformance();
}

public void test_getLocale() {
	FontData data = new FontData(SwtJunit.testFontName, 10, SWT.NORMAL);
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		data.getLocale();
	}
	stopMeasuring();
	
	commitMeasurements();
	assertPerformance();
}

public void test_getName() {
	FontData data = new FontData(SwtJunit.testFontName, 10, SWT.NORMAL);
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		data.getName();
	}
	stopMeasuring();
	
	commitMeasurements();
	assertPerformance();
}

public void test_getStyle() {
	FontData data = new FontData(SwtJunit.testFontName, 10, SWT.NORMAL);
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		data.getStyle();
	}
	stopMeasuring();
	
	commitMeasurements();
	assertPerformance();
}

public void test_hashCode() {
	FontData data = new FontData(SwtJunit.testFontName, 10, SWT.NORMAL);
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		data.hashCode();
	}
	stopMeasuring();
	
	commitMeasurements();
	assertPerformance();

}

public void test_setHeightI() {
	FontData data = new FontData(SwtJunit.testFontName, 10, SWT.NORMAL);
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		data.setHeight(12);
	}
	stopMeasuring();
	
	commitMeasurements();
	assertPerformance();
}

public void test_setLocaleLjava_lang_String() {
	FontData data = new FontData(SwtJunit.testFontName, 10, SWT.NORMAL);
	String localeString = Locale.ENGLISH.toString();
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		data.setLocale(localeString);
	}
	stopMeasuring();
	
	commitMeasurements();
	assertPerformance();
}

public void test_setNameLjava_lang_String() {
	FontData data = new FontData(SwtJunit.testFontName, 10, SWT.NORMAL);
	String name = "name";
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		data.setName(name);
	}
	stopMeasuring();
	
	commitMeasurements();
	assertPerformance();
}

public void test_setStyleI() {
	FontData data = new FontData(SwtJunit.testFontName, 10, SWT.NORMAL);
	int style = SWT.ITALIC | SWT.BOLD;
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		data.setStyle(style);
	}
	stopMeasuring();
	
	commitMeasurements();
	assertPerformance();
}


public void test_win32_newLorg_eclipse_swt_internal_win32_LOGFONTI() {
	// do not test - Windows only
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_graphics_FontData((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_Constructor");
	methodNames.addElement("test_ConstructorLjava_lang_String");
	methodNames.addElement("test_ConstructorLjava_lang_StringII");
	methodNames.addElement("test_equalsLjava_lang_Object");
	methodNames.addElement("test_getHeight");
	methodNames.addElement("test_getLocale");
	methodNames.addElement("test_getName");
	methodNames.addElement("test_getStyle");
	methodNames.addElement("test_hashCode");
	methodNames.addElement("test_setHeightI");
	methodNames.addElement("test_setLocaleLjava_lang_String");
	methodNames.addElement("test_setNameLjava_lang_String");
	methodNames.addElement("test_setStyleI");
	methodNames.addElement("test_win32_newLorg_eclipse_swt_internal_win32_LOGFONTI");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_Constructor")) test_Constructor();
	else if (getName().equals("test_ConstructorLjava_lang_String")) test_ConstructorLjava_lang_String();
	else if (getName().equals("test_ConstructorLjava_lang_StringII")) test_ConstructorLjava_lang_StringII();
	else if (getName().equals("test_equalsLjava_lang_Object")) test_equalsLjava_lang_Object();
	else if (getName().equals("test_getHeight")) test_getHeight();
	else if (getName().equals("test_getLocale")) test_getLocale();
	else if (getName().equals("test_getName")) test_getName();
	else if (getName().equals("test_getStyle")) test_getStyle();
	else if (getName().equals("test_hashCode")) test_hashCode();
	else if (getName().equals("test_setHeightI")) test_setHeightI();
	else if (getName().equals("test_setLocaleLjava_lang_String")) test_setLocaleLjava_lang_String();
	else if (getName().equals("test_setNameLjava_lang_String")) test_setNameLjava_lang_String();
	else if (getName().equals("test_setStyleI")) test_setStyleI();
	else if (getName().equals("test_win32_newLorg_eclipse_swt_internal_win32_LOGFONTI")) test_win32_newLorg_eclipse_swt_internal_win32_LOGFONTI();
}
}
