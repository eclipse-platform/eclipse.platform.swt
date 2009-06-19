/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Slider
 *
 * @see org.eclipse.swt.widgets.Slider
 */
public class Test_org_eclipse_swt_widgets_Slider extends Test_org_eclipse_swt_widgets_Control {

public Test_org_eclipse_swt_widgets_Slider(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	super.setUp();
	slider = new Slider(shell, 0);
	setWidget(slider);
}

protected String valueString(int[] intArray) {
	return " ("+intArray[1]+","+intArray[2]+","+intArray[3]+","+intArray[4]+")";
}

public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
	try {
		slider = new Slider(null, 0);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}

	int[] cases = {0, SWT.HORIZONTAL, SWT.VERTICAL};
	for (int i = 0; i < cases.length; i++)
		slider = new Slider(shell, cases[i]);
}

public void test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	listenerCalled = false;
	boolean exceptionThrown = false;
	SelectionListener listener = new SelectionListener() {
		public void widgetSelected(SelectionEvent event) {
			listenerCalled = true;
		}
		public void widgetDefaultSelected(SelectionEvent event) {
		}
	};
	try {
		slider.addSelectionListener(null);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue("Expected exception not thrown", exceptionThrown);
	exceptionThrown = false;
	slider.addSelectionListener(listener);
	slider.setSelection(0);
	assertTrue(":a:", listenerCalled == false);
	slider.removeSelectionListener(listener);
	try {
		slider.removeSelectionListener(null);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue("Expected exception not thrown", exceptionThrown);
}

public void test_computeSizeIIZ() {
	// super class method sufficient test
}

public void test_getEnabled() {
	// tested in setEnabled method
}

public void test_getIncrement() {
	int[] cases = {1, 10, 10000};
	for (int i=0; i<cases.length; i++)
	{
	  slider.setIncrement(cases[i]);
	  assertTrue("case: " + String.valueOf(i), slider.getIncrement()==cases[i]);
	} 
}

public void test_getMaximum() {
	slider.setMaximum(2000);
	assertTrue(":a:", slider.getMaximum() == 2000);
	slider.setMaximum(20);
	assertTrue(":b:", slider.getMaximum() == 20);
	slider.setMaximum(-1);
	assertTrue(":c:", slider.getMaximum() == 20);
	slider.setMaximum(0);
	assertTrue(":d:", slider.getMaximum() == 20);
	slider.setMaximum(10);
	assertTrue(":d:", slider.getMaximum() == 10);
}

public void test_getMinimum() {
	slider.setMinimum(5);
	assertTrue(":a:", slider.getMinimum() == 5);
	slider.setMinimum(20);
	assertTrue(":b:", slider.getMinimum() == 20);
	slider.setMinimum(-1);
	assertTrue(":c:", slider.getMinimum() == 20);
	slider.setMinimum(0);
	assertTrue(":d:", slider.getMinimum() == 0);
	slider.setMinimum(10);
	assertTrue(":d:", slider.getMinimum() == 10);
}

public void test_getPageIncrement() {
	int[] cases = {1, 10, 10000};
	for (int i=0; i<cases.length; i++)
	{
	  slider.setPageIncrement(cases[i]);
	  assertTrue("case: " + String.valueOf(i), slider.getPageIncrement()==cases[i]);
	} 
}

public void test_getSelection() {
	slider.setSelection(10);
	assertTrue(":a:", slider.getSelection()== 10);
	
}

public void test_getThumb() {
	// tested in test_setThumb() method
}

public void test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	// tested in addSelectionListener method
}

public void test_setEnabledZ() {
	slider.setEnabled(true);
	assertTrue(slider.getEnabled());
	slider.setEnabled(false);
	assertEquals(slider.getEnabled(), false);
}

public void test_setIncrementI() {
	// tested in getIncrement method
}

public void test_setMaximumI() {

	int [][] testValues = getSetMaximumValues();

	for (int i = 0; i < testValues.length; i++) {
		int[] intArray = testValues[i];
		setDefaults();
		slider.setMaximum(intArray[0]);
		String valueString = valueString(intArray);
		report("setMax "+intArray[0]+ valueString, intArray[0], intArray[1], intArray[2], intArray[3], intArray[4]);
	}
}

public void test_setMinimumI() {

	int [][] testValues = getSetMinimumValues();

	for (int i = 0; i < testValues.length; i++) {
		int[] intArray = testValues[i];
		setDefaults();
		slider.setMinimum(intArray[0]);
		String valueString = valueString(intArray);
		report("setMin "+intArray[0]+valueString, intArray[0], intArray[1], intArray[2], intArray[3], intArray[4]);
	}
}

public void test_setPageIncrementI() {
	 slider.setPageIncrement(3);
	 assertTrue(":a:", slider.getPageIncrement()== 3);
}

public void test_setSelectionI() {
	int [][] testValues = getSetSelectionValues();
	for (int i = 0; i < testValues.length; i++) {
		int[] intArray = testValues[i];
		setDefaults();
		slider.setSelection(intArray[0]);
		String valueString = valueString(intArray);
		report("setSel "+intArray[0]+valueString,intArray[0], intArray[1], intArray[2], intArray[3], intArray[4]);
	}
}

public void test_setThumbI() {

	int [][] testValues = getSetThumbValues();

	for (int i = 0; i < testValues.length; i++) {
		int[] intArray = testValues[i];
		setDefaults();
		slider.setThumb(intArray[0]);
		String valueString = valueString(intArray);
		report("setThmb "+intArray[0]+valueString,intArray[0], intArray[1], intArray[2], intArray[3], intArray[4]);
	}
}

public void test_setValuesIIIIII() {
	slider.setValues(10, 10, 50, 2, 5, 10);
	assertTrue(":a:", slider.getSelection() == 10);
	assertTrue(":b:", slider.getMinimum() == 10);
	assertTrue(":c:", slider.getMaximum() == 50);
	assertTrue(":d:", slider.getThumb() == 2);
	assertTrue(":e:", slider.getIncrement() == 5);
	assertTrue(":f:", slider.getPageIncrement() == 10);
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_widgets_Slider((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_CompositeI");
	methodNames.addElement("test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener");
	methodNames.addElement("test_computeSizeIIZ");
	methodNames.addElement("test_getEnabled");
	methodNames.addElement("test_getIncrement");
	methodNames.addElement("test_getMaximum");
	methodNames.addElement("test_getMinimum");
	methodNames.addElement("test_getPageIncrement");
	methodNames.addElement("test_getSelection");
	methodNames.addElement("test_getThumb");
	methodNames.addElement("test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener");
	methodNames.addElement("test_setEnabledZ");
	methodNames.addElement("test_setIncrementI");
	methodNames.addElement("test_setMaximumI");
	methodNames.addElement("test_setMinimumI");
	methodNames.addElement("test_setPageIncrementI");
	methodNames.addElement("test_setSelectionI");
	methodNames.addElement("test_setThumbI");
	methodNames.addElement("test_setValuesIIIIII");
	methodNames.addElement("test_consistency_ArrowSelection");
	methodNames.addElement("test_consistency_KeySelection");
	methodNames.addElement("test_consistency_TroughSelection");
	methodNames.addElement("test_consistency_ThumbSelection");
	methodNames.addElement("test_consistency_MenuDetect");
	methodNames.addElement("test_consistency_DragDetect");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Control.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_CompositeI")) test_ConstructorLorg_eclipse_swt_widgets_CompositeI();
	else if (getName().equals("test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener")) test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener();
	else if (getName().equals("test_computeSizeIIZ")) test_computeSizeIIZ();
	else if (getName().equals("test_getEnabled")) test_getEnabled();
	else if (getName().equals("test_getIncrement")) test_getIncrement();
	else if (getName().equals("test_getMaximum")) test_getMaximum();
	else if (getName().equals("test_getMinimum")) test_getMinimum();
	else if (getName().equals("test_getPageIncrement")) test_getPageIncrement();
	else if (getName().equals("test_getSelection")) test_getSelection();
	else if (getName().equals("test_getThumb")) test_getThumb();
	else if (getName().equals("test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener")) test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener();
	else if (getName().equals("test_setEnabledZ")) test_setEnabledZ();
	else if (getName().equals("test_setIncrementI")) test_setIncrementI();
	else if (getName().equals("test_setMaximumI")) test_setMaximumI();
	else if (getName().equals("test_setMinimumI")) test_setMinimumI();
	else if (getName().equals("test_setPageIncrementI")) test_setPageIncrementI();
	else if (getName().equals("test_setSelectionI")) test_setSelectionI();
	else if (getName().equals("test_setThumbI")) test_setThumbI();
	else if (getName().equals("test_setValuesIIIIII")) test_setValuesIIIIII();
	else if (getName().equals("test_consistency_ArrowSelection")) test_consistency_ArrowSelection();
	else if (getName().equals("test_consistency_KeySelection")) test_consistency_KeySelection();
	else if (getName().equals("test_consistency_TroughSelection")) test_consistency_TroughSelection();
	else if (getName().equals("test_consistency_ThumbSelection")) test_consistency_ThumbSelection();
	else if (getName().equals("test_consistency_MenuDetect")) test_consistency_MenuDetect();
	else if (getName().equals("test_consistency_DragDetect")) test_consistency_DragDetect();
	else super.runTest();
}

/* custom */
public void test_setFontLorg_eclipse_swt_graphics_Font() {
	// overridden from Control because it does not make sense
	// to set the font of a Slider.
}

Slider slider;

// this method must be private or protected so the auto-gen tool keeps it
private void report(String call, int set, int minExpected, int maxExpected, int selectionExpected, int thumbExpected) {
	// Uncomment these lines and comment out call to check() if you want the test to report all errors without
	// stopping.
//	if (slider.getMinimum() != minExpected) {
//		System.out.println(call + " : Minimum Expected: " + minExpected + "  Actual: " + slider.getMinimum());
//	}
//	if (slider.getMaximum() != maxExpected){
//		System.out.println(call + " : Maximum Expected: " + maxExpected + "  Actual: " + slider.getMaximum());
//	}
//	if (slider.getSelection() != selectionExpected) {
//		System.out.println(call + " : Selection Expected: " + selectionExpected + "  Actual: " + slider.getSelection());
//	}
//	if (slider.getThumb() != thumbExpected) {
//		System.out.println(call + " : Thumb Expected: " + thumbExpected + "  Actual: " + slider.getThumb());
//	}
	check(call, minExpected, maxExpected, selectionExpected, thumbExpected);
}
// this method must be private or protected so the auto-gen tool keeps it
private void check(String call, int minExpected, int maxExpected, int selectionExpected, int thumbExpected) {
	assertEquals(call+" max ", maxExpected, slider.getMaximum());
	assertEquals(call+" min ", minExpected, slider.getMinimum());
	assertEquals(call+" sel ", selectionExpected, slider.getSelection());
	assertEquals(call+" thmb ", thumbExpected, slider.getThumb());
}
// this method must be private or protected so the auto-gen tool keeps it
private int[][] getSetThumbValues() {
return new int[][] {
{-15, 10, 100, 50, 10},
{-14, 10, 100, 50, 10},
{-13, 10, 100, 50, 10},
{-12, 10, 100, 50, 10},
{-11, 10, 100, 50, 10},
{-10, 10, 100, 50, 10},
{-9, 10, 100, 50, 10},
{-8, 10, 100, 50, 10},
{-7, 10, 100, 50, 10},
{-6, 10, 100, 50, 10},
{-5, 10, 100, 50, 10},
{-4, 10, 100, 50, 10},
{-3, 10, 100, 50, 10},
{-2, 10, 100, 50, 10},
{-1, 10, 100, 50, 10},
{0, 10, 100, 50, 10},
{1, 10, 100, 50, 1},
{2, 10, 100, 50, 2},
{3, 10, 100, 50, 3},
{4, 10, 100, 50, 4},
{5, 10, 100, 50, 5},
{6, 10, 100, 50, 6},
{7, 10, 100, 50, 7},
{8, 10, 100, 50, 8},
{9, 10, 100, 50, 9},
{10, 10, 100, 50, 10},
{11, 10, 100, 50, 11},
{12, 10, 100, 50, 12},
{13, 10, 100, 50, 13},
{14, 10, 100, 50, 14},
{15, 10, 100, 50, 15},
{16, 10, 100, 50, 16},
{17, 10, 100, 50, 17},
{18, 10, 100, 50, 18},
{19, 10, 100, 50, 19},
{20, 10, 100, 50, 20},
{21, 10, 100, 50, 21},
{22, 10, 100, 50, 22},
{23, 10, 100, 50, 23},
{24, 10, 100, 50, 24},
{25, 10, 100, 50, 25},
{26, 10, 100, 50, 26},
{27, 10, 100, 50, 27},
{28, 10, 100, 50, 28},
{29, 10, 100, 50, 29},
{30, 10, 100, 50, 30},
{31, 10, 100, 50, 31},
{32, 10, 100, 50, 32},
{33, 10, 100, 50, 33},
{34, 10, 100, 50, 34},
{35, 10, 100, 50, 35},
{36, 10, 100, 50, 36},
{37, 10, 100, 50, 37},
{38, 10, 100, 50, 38},
{39, 10, 100, 50, 39},
{40, 10, 100, 50, 40},
{41, 10, 100, 50, 41},
{42, 10, 100, 50, 42},
{43, 10, 100, 50, 43},
{44, 10, 100, 50, 44},
{45, 10, 100, 50, 45},
{46, 10, 100, 50, 46},
{47, 10, 100, 50, 47},
{48, 10, 100, 50, 48},
{49, 10, 100, 50, 49},
{50, 10, 100, 50, 50},
{51, 10, 100, 49, 51},
{52, 10, 100, 48, 52},
{53, 10, 100, 47, 53},
{54, 10, 100, 46, 54},
{55, 10, 100, 45, 55},
{56, 10, 100, 44, 56},
{57, 10, 100, 43, 57},
{58, 10, 100, 42, 58},
{59, 10, 100, 41, 59},
{60, 10, 100, 40, 60},
{61, 10, 100, 39, 61},
{62, 10, 100, 38, 62},
{63, 10, 100, 37, 63},
{64, 10, 100, 36, 64},
{65, 10, 100, 35, 65},
{66, 10, 100, 34, 66},
{67, 10, 100, 33, 67},
{68, 10, 100, 32, 68},
{69, 10, 100, 31, 69},
{70, 10, 100, 30, 70},
{71, 10, 100, 29, 71},
{72, 10, 100, 28, 72},
{73, 10, 100, 27, 73},
{74, 10, 100, 26, 74},
{75, 10, 100, 25, 75},
{76, 10, 100, 24, 76},
{77, 10, 100, 23, 77},
{78, 10, 100, 22, 78},
{79, 10, 100, 21, 79},
{80, 10, 100, 20, 80},
{81, 10, 100, 19, 81},
{82, 10, 100, 18, 82},
{83, 10, 100, 17, 83},
{84, 10, 100, 16, 84},
{85, 10, 100, 15, 85},
{86, 10, 100, 14, 86},
{87, 10, 100, 13, 87},
{88, 10, 100, 12, 88},
{89, 10, 100, 11, 89},
{90, 10, 100, 10, 90},
{91, 10, 100, 10, 90},
{92, 10, 100, 10, 90},
{93, 10, 100, 10, 90},
{94, 10, 100, 10, 90},
{95, 10, 100, 10, 90},
{96, 10, 100, 10, 90},
{97, 10, 100, 10, 90},
{98, 10, 100, 10, 90},
{99, 10, 100, 10, 90},
{100, 10, 100, 10, 90},
{101, 10, 100, 10, 90},
{102, 10, 100, 10, 90},
{103, 10, 100, 10, 90},
{104, 10, 100, 10, 90},
{105, 10, 100, 10, 90},
{106, 10, 100, 10, 90},
{107, 10, 100, 10, 90},
{108, 10, 100, 10, 90},
{109, 10, 100, 10, 90},
{110, 10, 100, 10, 90},
{111, 10, 100, 10, 90},
{112, 10, 100, 10, 90},
{113, 10, 100, 10, 90},
{114, 10, 100, 10, 90},
{115, 10, 100, 10, 90},
{116, 10, 100, 10, 90},
{117, 10, 100, 10, 90},
{118, 10, 100, 10, 90},
{119, 10, 100, 10, 90},
{120, 10, 100, 10, 90},
{121, 10, 100, 10, 90},
{122, 10, 100, 10, 90},
{123, 10, 100, 10, 90},
{124, 10, 100, 10, 90},
};
}
// this method must be private or protected so the auto-gen tool keeps it
private int[][] getSetMinimumValues() {
return new int[][] {
{-15, 10, 100, 50, 10},
{-14, 10, 100, 50, 10},
{-13, 10, 100, 50, 10},
{-12, 10, 100, 50, 10},
{-11, 10, 100, 50, 10},
{-10, 10, 100, 50, 10},
{-9, 10, 100, 50, 10},
{-8, 10, 100, 50, 10},
{-7, 10, 100, 50, 10},
{-6, 10, 100, 50, 10},
{-5, 10, 100, 50, 10},
{-4, 10, 100, 50, 10},
{-3, 10, 100, 50, 10},
{-2, 10, 100, 50, 10},
{-1, 10, 100, 50, 10},
{0, 0, 100, 50, 10},
{1, 1, 100, 50, 10},
{2, 2, 100, 50, 10},
{3, 3, 100, 50, 10},
{4, 4, 100, 50, 10},
{5, 5, 100, 50, 10},
{6, 6, 100, 50, 10},
{7, 7, 100, 50, 10},
{8, 8, 100, 50, 10},
{9, 9, 100, 50, 10},
{10, 10, 100, 50, 10},
{11, 11, 100, 50, 10},
{12, 12, 100, 50, 10},
{13, 13, 100, 50, 10},
{14, 14, 100, 50, 10},
{15, 15, 100, 50, 10},
{16, 16, 100, 50, 10},
{17, 17, 100, 50, 10},
{18, 18, 100, 50, 10},
{19, 19, 100, 50, 10},
{20, 20, 100, 50, 10},
{21, 21, 100, 50, 10},
{22, 22, 100, 50, 10},
{23, 23, 100, 50, 10},
{24, 24, 100, 50, 10},
{25, 25, 100, 50, 10},
{26, 26, 100, 50, 10},
{27, 27, 100, 50, 10},
{28, 28, 100, 50, 10},
{29, 29, 100, 50, 10},
{30, 30, 100, 50, 10},
{31, 31, 100, 50, 10},
{32, 32, 100, 50, 10},
{33, 33, 100, 50, 10},
{34, 34, 100, 50, 10},
{35, 35, 100, 50, 10},
{36, 36, 100, 50, 10},
{37, 37, 100, 50, 10},
{38, 38, 100, 50, 10},
{39, 39, 100, 50, 10},
{40, 40, 100, 50, 10},
{41, 41, 100, 50, 10},
{42, 42, 100, 50, 10},
{43, 43, 100, 50, 10},
{44, 44, 100, 50, 10},
{45, 45, 100, 50, 10},
{46, 46, 100, 50, 10},
{47, 47, 100, 50, 10},
{48, 48, 100, 50, 10},
{49, 49, 100, 50, 10},
{50, 50, 100, 50, 10},
{51, 51, 100, 51, 10},
{52, 52, 100, 52, 10},
{53, 53, 100, 53, 10},
{54, 54, 100, 54, 10},
{55, 55, 100, 55, 10},
{56, 56, 100, 56, 10},
{57, 57, 100, 57, 10},
{58, 58, 100, 58, 10},
{59, 59, 100, 59, 10},
{60, 60, 100, 60, 10},
{61, 61, 100, 61, 10},
{62, 62, 100, 62, 10},
{63, 63, 100, 63, 10},
{64, 64, 100, 64, 10},
{65, 65, 100, 65, 10},
{66, 66, 100, 66, 10},
{67, 67, 100, 67, 10},
{68, 68, 100, 68, 10},
{69, 69, 100, 69, 10},
{70, 70, 100, 70, 10},
{71, 71, 100, 71, 10},
{72, 72, 100, 72, 10},
{73, 73, 100, 73, 10},
{74, 74, 100, 74, 10},
{75, 75, 100, 75, 10},
{76, 76, 100, 76, 10},
{77, 77, 100, 77, 10},
{78, 78, 100, 78, 10},
{79, 79, 100, 79, 10},
{80, 80, 100, 80, 10},
{81, 81, 100, 81, 10},
{82, 82, 100, 82, 10},
{83, 83, 100, 83, 10},
{84, 84, 100, 84, 10},
{85, 85, 100, 85, 10},
{86, 86, 100, 86, 10},
{87, 87, 100, 87, 10},
{88, 88, 100, 88, 10},
{89, 89, 100, 89, 10},
{90, 90, 100, 90, 10},
{91, 91, 100, 91, 9},
{92, 92, 100, 92, 8},
{93, 93, 100, 93, 7},
{94, 94, 100, 94, 6},
{95, 95, 100, 95, 5},
{96, 96, 100, 96, 4},
{97, 97, 100, 97, 3},
{98, 98, 100, 98, 2},
{99, 99, 100, 99, 1},
{100, 10, 100, 50, 10}, 
{101, 10, 100, 50, 10},
{102, 10, 100, 50, 10},
{103, 10, 100, 50, 10},
{104, 10, 100, 50, 10},
{105, 10, 100, 50, 10},
{106, 10, 100, 50, 10},
{107, 10, 100, 50, 10},
{108, 10, 100, 50, 10},
{109, 10, 100, 50, 10},
{110, 10, 100, 50, 10},
{111, 10, 100, 50, 10},
{112, 10, 100, 50, 10},
{113, 10, 100, 50, 10},
{114, 10, 100, 50, 10},
{115, 10, 100, 50, 10},
{116, 10, 100, 50, 10},
{117, 10, 100, 50, 10},
{118, 10, 100, 50, 10},
{119, 10, 100, 50, 10},
{120, 10, 100, 50, 10},
{121, 10, 100, 50, 10},
{122, 10, 100, 50, 10},
{123, 10, 100, 50, 10},
{124, 10, 100, 50, 10},
};
}
// this method must be private or protected so the auto-gen tool keeps it
private int[][] getSetMaximumValues() {
return new int[][] {
{-15, 10, 100, 50, 10},
{-14, 10, 100, 50, 10},
{-13, 10, 100, 50, 10},
{-12, 10, 100, 50, 10},
{-11, 10, 100, 50, 10},
{-10, 10, 100, 50, 10},
{-9, 10, 100, 50, 10},
{-8, 10, 100, 50, 10},
{-7, 10, 100, 50, 10},
{-6, 10, 100, 50, 10},
{-5, 10, 100, 50, 10},
{-4, 10, 100, 50, 10},
{-3, 10, 100, 50, 10},
{-2, 10, 100, 50, 10},
{-1, 10, 100, 50, 10},
{0, 10, 100, 50, 10},
{1, 10, 100, 50, 10},
{2, 10, 100, 50, 10},
{3, 10, 100, 50, 10},
{4, 10, 100, 50, 10},
{5, 10, 100, 50, 10},
{6, 10, 100, 50, 10},
{7, 10, 100, 50, 10},
{8, 10, 100, 50, 10},
{9, 10, 100, 50, 10},
{10, 10, 100, 50, 10},
{11, 10, 11, 10, 1},
{12, 10, 12, 10, 2},
{13, 10, 13, 10, 3},
{14, 10, 14, 10, 4},
{15, 10, 15, 10, 5},
{16, 10, 16, 10, 6},
{17, 10, 17, 10, 7},
{18, 10, 18, 10, 8},
{19, 10, 19, 10, 9},
{20, 10, 20, 10, 10},
{21, 10, 21, 11, 10},
{22, 10, 22, 12, 10},
{23, 10, 23, 13, 10},
{24, 10, 24, 14, 10},
{25, 10, 25, 15, 10},
{26, 10, 26, 16, 10},
{27, 10, 27, 17, 10},
{28, 10, 28, 18, 10},
{29, 10, 29, 19, 10},
{30, 10, 30, 20, 10},
{31, 10, 31, 21, 10},
{32, 10, 32, 22, 10},
{33, 10, 33, 23, 10},
{34, 10, 34, 24, 10},
{35, 10, 35, 25, 10},
{36, 10, 36, 26, 10},
{37, 10, 37, 27, 10},
{38, 10, 38, 28, 10},
{39, 10, 39, 29, 10},
{40, 10, 40, 30, 10},
{41, 10, 41, 31, 10},
{42, 10, 42, 32, 10},
{43, 10, 43, 33, 10},
{44, 10, 44, 34, 10},
{45, 10, 45, 35, 10},
{46, 10, 46, 36, 10},
{47, 10, 47, 37, 10},
{48, 10, 48, 38, 10},
{49, 10, 49, 39, 10},
{50, 10, 50, 40, 10},
{51, 10, 51, 41, 10},
{52, 10, 52, 42, 10},
{53, 10, 53, 43, 10},
{54, 10, 54, 44, 10},
{55, 10, 55, 45, 10},
{56, 10, 56, 46, 10},
{57, 10, 57, 47, 10},
{58, 10, 58, 48, 10},
{59, 10, 59, 49, 10},
{60, 10, 60, 50, 10},
{61, 10, 61, 50, 10},
{62, 10, 62, 50, 10},
{63, 10, 63, 50, 10},
{64, 10, 64, 50, 10},
{65, 10, 65, 50, 10},
{66, 10, 66, 50, 10},
{67, 10, 67, 50, 10},
{68, 10, 68, 50, 10},
{69, 10, 69, 50, 10},
{70, 10, 70, 50, 10},
{71, 10, 71, 50, 10},
{72, 10, 72, 50, 10},
{73, 10, 73, 50, 10},
{74, 10, 74, 50, 10},
{75, 10, 75, 50, 10},
{76, 10, 76, 50, 10},
{77, 10, 77, 50, 10},
{78, 10, 78, 50, 10},
{79, 10, 79, 50, 10},
{80, 10, 80, 50, 10},
{81, 10, 81, 50, 10},
{82, 10, 82, 50, 10},
{83, 10, 83, 50, 10},
{84, 10, 84, 50, 10},
{85, 10, 85, 50, 10},
{86, 10, 86, 50, 10},
{87, 10, 87, 50, 10},
{88, 10, 88, 50, 10},
{89, 10, 89, 50, 10},
{90, 10, 90, 50, 10},
{91, 10, 91, 50, 10},
{92, 10, 92, 50, 10},
{93, 10, 93, 50, 10},
{94, 10, 94, 50, 10},
{95, 10, 95, 50, 10},
{96, 10, 96, 50, 10},
{97, 10, 97, 50, 10},
{98, 10, 98, 50, 10},
{99, 10, 99, 50, 10},
{100, 10, 100, 50, 10},
{101, 10, 101, 50, 10},
{102, 10, 102, 50, 10},
{103, 10, 103, 50, 10},
{104, 10, 104, 50, 10},
{105, 10, 105, 50, 10},
{106, 10, 106, 50, 10},
{107, 10, 107, 50, 10},
{108, 10, 108, 50, 10},
{109, 10, 109, 50, 10},
{110, 10, 110, 50, 10},
{111, 10, 111, 50, 10},
{112, 10, 112, 50, 10},
{113, 10, 113, 50, 10},
{114, 10, 114, 50, 10},
{115, 10, 115, 50, 10},
{116, 10, 116, 50, 10},
{117, 10, 117, 50, 10},
{118, 10, 118, 50, 10},
{119, 10, 119, 50, 10},
{120, 10, 120, 50, 10},
{121, 10, 121, 50, 10},
{122, 10, 122, 50, 10},
{123, 10, 123, 50, 10},
{124, 10, 124, 50, 10},
};
}
// this method must be private or protected so the auto-gen tool keeps it
private void setDefaults() {

	slider.setMaximum(100);
	slider.setMinimum(10);
	slider.setThumb(10);
	slider.setSelection(50);

}
// this method must be private or protected so the auto-gen tool keeps it
private int[][] getSetSelectionValues() {
return new int[][] {
{-15, 10, 100, 10, 10},
{-14, 10, 100, 10, 10},
{-13, 10, 100, 10, 10},
{-12, 10, 100, 10, 10},
{-11, 10, 100, 10, 10},
{-10, 10, 100, 10, 10},
{-9, 10, 100, 10, 10},
{-8, 10, 100, 10, 10},
{-7, 10, 100, 10, 10},
{-6, 10, 100, 10, 10},
{-5, 10, 100, 10, 10},
{-4, 10, 100, 10, 10},
{-3, 10, 100, 10, 10},
{-2, 10, 100, 10, 10},
{-1, 10, 100, 10, 10},
{0, 10, 100, 10, 10},
{1, 10, 100, 10, 10},
{2, 10, 100, 10, 10},
{3, 10, 100, 10, 10},
{4, 10, 100, 10, 10},
{5, 10, 100, 10, 10},
{6, 10, 100, 10, 10},
{7, 10, 100, 10, 10},
{8, 10, 100, 10, 10},
{9, 10, 100, 10, 10},
{10, 10, 100, 10, 10},
{11, 10, 100, 11, 10},
{12, 10, 100, 12, 10},
{13, 10, 100, 13, 10},
{14, 10, 100, 14, 10},
{15, 10, 100, 15, 10},
{16, 10, 100, 16, 10},
{17, 10, 100, 17, 10},
{18, 10, 100, 18, 10},
{19, 10, 100, 19, 10},
{20, 10, 100, 20, 10},
{21, 10, 100, 21, 10},
{22, 10, 100, 22, 10},
{23, 10, 100, 23, 10},
{24, 10, 100, 24, 10},
{25, 10, 100, 25, 10},
{26, 10, 100, 26, 10},
{27, 10, 100, 27, 10},
{28, 10, 100, 28, 10},
{29, 10, 100, 29, 10},
{30, 10, 100, 30, 10},
{31, 10, 100, 31, 10},
{32, 10, 100, 32, 10},
{33, 10, 100, 33, 10},
{34, 10, 100, 34, 10},
{35, 10, 100, 35, 10},
{36, 10, 100, 36, 10},
{37, 10, 100, 37, 10},
{38, 10, 100, 38, 10},
{39, 10, 100, 39, 10},
{40, 10, 100, 40, 10},
{41, 10, 100, 41, 10},
{42, 10, 100, 42, 10},
{43, 10, 100, 43, 10},
{44, 10, 100, 44, 10},
{45, 10, 100, 45, 10},
{46, 10, 100, 46, 10},
{47, 10, 100, 47, 10},
{48, 10, 100, 48, 10},
{49, 10, 100, 49, 10},
{50, 10, 100, 50, 10},
{51, 10, 100, 51, 10},
{52, 10, 100, 52, 10},
{53, 10, 100, 53, 10},
{54, 10, 100, 54, 10},
{55, 10, 100, 55, 10},
{56, 10, 100, 56, 10},
{57, 10, 100, 57, 10},
{58, 10, 100, 58, 10},
{59, 10, 100, 59, 10},
{60, 10, 100, 60, 10},
{61, 10, 100, 61, 10},
{62, 10, 100, 62, 10},
{63, 10, 100, 63, 10},
{64, 10, 100, 64, 10},
{65, 10, 100, 65, 10},
{66, 10, 100, 66, 10},
{67, 10, 100, 67, 10},
{68, 10, 100, 68, 10},
{69, 10, 100, 69, 10},
{70, 10, 100, 70, 10},
{71, 10, 100, 71, 10},
{72, 10, 100, 72, 10},
{73, 10, 100, 73, 10},
{74, 10, 100, 74, 10},
{75, 10, 100, 75, 10},
{76, 10, 100, 76, 10},
{77, 10, 100, 77, 10},
{78, 10, 100, 78, 10},
{79, 10, 100, 79, 10},
{80, 10, 100, 80, 10},
{81, 10, 100, 81, 10},
{82, 10, 100, 82, 10},
{83, 10, 100, 83, 10},
{84, 10, 100, 84, 10},
{85, 10, 100, 85, 10},
{86, 10, 100, 86, 10},
{87, 10, 100, 87, 10},
{88, 10, 100, 88, 10},
{89, 10, 100, 89, 10},
{90, 10, 100, 90, 10},
{91, 10, 100, 90, 10},
{92, 10, 100, 90, 10},
{93, 10, 100, 90, 10},
{94, 10, 100, 90, 10},
{95, 10, 100, 90, 10},
{96, 10, 100, 90, 10},
{97, 10, 100, 90, 10},
{98, 10, 100, 90, 10},
{99, 10, 100, 90, 10},
{100, 10, 100, 90, 10},
{101, 10, 100, 90, 10},
{102, 10, 100, 90, 10},
{103, 10, 100, 90, 10},
{104, 10, 100, 90, 10},
{105, 10, 100, 90, 10},
{106, 10, 100, 90, 10},
{107, 10, 100, 90, 10},
{108, 10, 100, 90, 10},
{109, 10, 100, 90, 10},
{110, 10, 100, 90, 10},
{111, 10, 100, 90, 10},
{112, 10, 100, 90, 10},
{113, 10, 100, 90, 10},
{114, 10, 100, 90, 10},
{115, 10, 100, 90, 10},
{116, 10, 100, 90, 10},
{117, 10, 100, 90, 10},
{118, 10, 100, 90, 10},
{119, 10, 100, 90, 10},
{120, 10, 100, 90, 10},
{121, 10, 100, 90, 10},
{122, 10, 100, 90, 10},
{123, 10, 100, 90, 10},
{124, 10, 100, 90, 10},
};
}

public void test_consistency_ArrowSelection() {
    consistencyPrePackShell();
    consistencyEvent(slider.getSize().x-10, 5, 1, 0, ConsistencyUtility.MOUSE_CLICK);
}

public void test_consistency_KeySelection () {
    consistencyEvent(0, SWT.ARROW_RIGHT, 0, 0, ConsistencyUtility.KEY_PRESS);
}

public void test_consistency_ThumbSelection () {
    consistencyEvent(25, 10, 1, 0, ConsistencyUtility.MOUSE_CLICK);
}

public void test_consistency_TroughSelection () {
    consistencyEvent(45, 10, 1, 0, ConsistencyUtility.MOUSE_CLICK);
}

public void test_consistency_MenuDetect () {
    consistencyEvent(27, 5, 3, 0, ConsistencyUtility.MOUSE_CLICK);
}

public void test_consistency_DragDetect () {
    consistencyEvent(9, 5, 30, 10, ConsistencyUtility.MOUSE_DRAG);
}
}
