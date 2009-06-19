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


import junit.framework.*;
import junit.textui.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Scale
 *
 * @see org.eclipse.swt.widgets.Scale
 */
public class Test_org_eclipse_swt_widgets_Scale extends Test_org_eclipse_swt_widgets_Control {

public Test_org_eclipse_swt_widgets_Scale(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	super.setUp();
	scale = new Scale(shell, 0);
	setWidget(scale);
}

public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
	try {
		scale = new Scale(null, 0);
		fail("No exception occurred"); //should never get here
	}
	catch (IllegalArgumentException e) {
	}

	int[] cases = {0, SWT.HORIZONTAL, SWT.VERTICAL};
	for (int i = 0; i < cases.length; i++)
		scale = new Scale(shell, cases[i]);
}

public void test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	warnUnimpl("Test test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener not written");
}

public void test_computeSizeIIZ() {
	warnUnimpl("Test test_computeSizeIIZ not written");
}

public void test_getIncrement() {
	warnUnimpl("Test test_getIncrement not written");
}

public void test_getMaximum() {
	warnUnimpl("Test test_getMaximum not written");
}

public void test_getMinimum() {
	warnUnimpl("Test test_getMinimum not written");
}

public void test_getPageIncrement() {
	warnUnimpl("Test test_getPageIncrement not written");
}

public void test_getSelection() {
	warnUnimpl("Test test_getSelection not written");
}

public void test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	warnUnimpl("Test test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener not written");
}

public void test_setIncrementI() {
	warnUnimpl("Test test_setIncrementI not written");
}

public void test_setMaximumI() {

	int [][] testValues = getSetMaximumValues();

	for (int i = 0; i < testValues.length; i++) {
		int[] intArray = testValues[i];
		setDefaults();
		scale.setMaximum(intArray[0]);
		report("setMaximum", intArray[0], intArray[1], intArray[2], intArray[3]);
	}
}

public void test_setMinimumI() {


	int [][] testValues = getSetMinimumValues();

	for (int i = 0; i < testValues.length; i++) {
		int[] intArray = testValues[i];
		setDefaults();
		scale.setMinimum(intArray[0]);
		report("setMinimum", intArray[0], intArray[1], intArray[2], intArray[3]);
	}
}

public void test_setPageIncrementI() {
	warnUnimpl("Test test_setPageIncrementI not written");
}

public void test_setSelectionI() {
	int [][] testValues = getSetSelectionValues();
	for (int i = 0; i < testValues.length; i++) {
		int[] intArray = testValues[i];
		setDefaults();
		scale.setSelection(intArray[0]);
		report("setSelection", intArray[0], intArray[1], intArray[2], intArray[3]);
	}
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_widgets_Scale((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_CompositeI");
	methodNames.addElement("test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener");
	methodNames.addElement("test_computeSizeIIZ");
	methodNames.addElement("test_getIncrement");
	methodNames.addElement("test_getMaximum");
	methodNames.addElement("test_getMinimum");
	methodNames.addElement("test_getPageIncrement");
	methodNames.addElement("test_getSelection");
	methodNames.addElement("test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener");
	methodNames.addElement("test_setIncrementI");
	methodNames.addElement("test_setMaximumI");
	methodNames.addElement("test_setMinimumI");
	methodNames.addElement("test_setPageIncrementI");
	methodNames.addElement("test_setSelectionI");
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
	else if (getName().equals("test_getIncrement")) test_getIncrement();
	else if (getName().equals("test_getMaximum")) test_getMaximum();
	else if (getName().equals("test_getMinimum")) test_getMinimum();
	else if (getName().equals("test_getPageIncrement")) test_getPageIncrement();
	else if (getName().equals("test_getSelection")) test_getSelection();
	else if (getName().equals("test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener")) test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener();
	else if (getName().equals("test_setIncrementI")) test_setIncrementI();
	else if (getName().equals("test_setMaximumI")) test_setMaximumI();
	else if (getName().equals("test_setMinimumI")) test_setMinimumI();
	else if (getName().equals("test_setPageIncrementI")) test_setPageIncrementI();
	else if (getName().equals("test_setSelectionI")) test_setSelectionI();
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
	// to set the font of a Scale.
}

Scale scale;

// this method must be private or protected so the auto-gen tool keeps it
private void report(String call, int set, int minExpected, int maxExpected, int selectionExpected) {
	 //Uncomment these lines and comment out call to check() if you want the test to report all errors without
	 //stopping.
	
	//if (trackBar.getMinimum() != minExpected)
		//System.out.println(call + "(" + set + "): Minimum Expected: " + minExpected + "  Actual: " + trackBar.getMinimum());
	//if (trackBar.getMaximum() != maxExpected)
		//System.out.println(call + "(" + set + "): Maximum Expected: " + maxExpected + "  Actual: " + trackBar.getMaximum());
	//if (trackBar.getSelection() != selectionExpected)
		//System.out.println(call + "(" + set + "): Selection Expected: " + selectionExpected + "  Actual: " + trackBar.getSelection());
	check(minExpected, maxExpected, selectionExpected);
}
// this method must be private or protected so the auto-gen tool keeps it
private void check(int minExpected, int maxExpected, int selectionExpected) {
	assertEquals(scale.getMaximum(), maxExpected);
	assertEquals(scale.getMinimum(), minExpected);
	assertEquals(scale.getSelection(), selectionExpected);
}
// this method must be private or protected so the auto-gen tool keeps it
private int[][] getSetMinimumValues() {
return new int[][] {
{-15, 10, 100, 50, },
{-14, 10, 100, 50, },
{-13, 10, 100, 50, },
{-12, 10, 100, 50, },
{-11, 10, 100, 50, },
{-10, 10, 100, 50, },
{-9, 10, 100, 50, },
{-8, 10, 100, 50, },
{-7, 10, 100, 50, },
{-6, 10, 100, 50, },
{-5, 10, 100, 50, },
{-4, 10, 100, 50, },
{-3, 10, 100, 50, },
{-2, 10, 100, 50, },
{-1, 10, 100, 50, },
{0, 0, 100, 50, },
{1, 1, 100, 50, },
{2, 2, 100, 50, },
{3, 3, 100, 50, },
{4, 4, 100, 50, },
{5, 5, 100, 50, },
{6, 6, 100, 50, },
{7, 7, 100, 50, },
{8, 8, 100, 50, },
{9, 9, 100, 50, },
{10, 10, 100, 50, },
{11, 11, 100, 50, },
{12, 12, 100, 50, },
{13, 13, 100, 50, },
{14, 14, 100, 50, },
{15, 15, 100, 50, },
{16, 16, 100, 50, },
{17, 17, 100, 50, },
{18, 18, 100, 50, },
{19, 19, 100, 50, },
{20, 20, 100, 50, },
{21, 21, 100, 50, },
{22, 22, 100, 50, },
{23, 23, 100, 50, },
{24, 24, 100, 50, },
{25, 25, 100, 50, },
{26, 26, 100, 50, },
{27, 27, 100, 50, },
{28, 28, 100, 50, },
{29, 29, 100, 50, },
{30, 30, 100, 50, },
{31, 31, 100, 50, },
{32, 32, 100, 50, },
{33, 33, 100, 50, },
{34, 34, 100, 50, },
{35, 35, 100, 50, },
{36, 36, 100, 50, },
{37, 37, 100, 50, },
{38, 38, 100, 50, },
{39, 39, 100, 50, },
{40, 40, 100, 50, },
{41, 41, 100, 50, },
{42, 42, 100, 50, },
{43, 43, 100, 50, },
{44, 44, 100, 50, },
{45, 45, 100, 50, },
{46, 46, 100, 50, },
{47, 47, 100, 50, },
{48, 48, 100, 50, },
{49, 49, 100, 50, },
{50, 50, 100, 50, },
{51, 51, 100, 51, },
{52, 52, 100, 52, },
{53, 53, 100, 53, },
{54, 54, 100, 54, },
{55, 55, 100, 55, },
{56, 56, 100, 56, },
{57, 57, 100, 57, },
{58, 58, 100, 58, },
{59, 59, 100, 59, },
{60, 60, 100, 60, },
{61, 61, 100, 61, },
{62, 62, 100, 62, },
{63, 63, 100, 63, },
{64, 64, 100, 64, },
{65, 65, 100, 65, },
{66, 66, 100, 66, },
{67, 67, 100, 67, },
{68, 68, 100, 68, },
{69, 69, 100, 69, },
{70, 70, 100, 70, },
{71, 71, 100, 71, },
{72, 72, 100, 72, },
{73, 73, 100, 73, },
{74, 74, 100, 74, },
{75, 75, 100, 75, },
{76, 76, 100, 76, },
{77, 77, 100, 77, },
{78, 78, 100, 78, },
{79, 79, 100, 79, },
{80, 80, 100, 80, },
{81, 81, 100, 81, },
{82, 82, 100, 82, },
{83, 83, 100, 83, },
{84, 84, 100, 84, },
{85, 85, 100, 85, },
{86, 86, 100, 86, },
{87, 87, 100, 87, },
{88, 88, 100, 88, },
{89, 89, 100, 89, },
{90, 90, 100, 90, },
{91, 91, 100, 91, },
{92, 92, 100, 92, },
{93, 93, 100, 93, },
{94, 94, 100, 94, },
{95, 95, 100, 95, },
{96, 96, 100, 96, },
{97, 97, 100, 97, },
{98, 98, 100, 98, },
{99, 99, 100, 99, },
{100, 10, 100, 50, },
{101, 10, 100, 50, },
{102, 10, 100, 50, },
{103, 10, 100, 50, },
{104, 10, 100, 50, },
{105, 10, 100, 50, },
{106, 10, 100, 50, },
{107, 10, 100, 50, },
{108, 10, 100, 50, },
{109, 10, 100, 50, },
{110, 10, 100, 50, },
{111, 10, 100, 50, },
{112, 10, 100, 50, },
{113, 10, 100, 50, },
{114, 10, 100, 50, },
{115, 10, 100, 50, },
{116, 10, 100, 50, },
{117, 10, 100, 50, },
{118, 10, 100, 50, },
{119, 10, 100, 50, },
{120, 10, 100, 50, },
{121, 10, 100, 50, },
{122, 10, 100, 50, },
{123, 10, 100, 50, },
{124, 10, 100, 50, },
};
}
// this method must be private or protected so the auto-gen tool keeps it
private int[][] getSetMaximumValues() {
return new int[][] {
{-15, 10, 100, 50, },
{-14, 10, 100, 50, },
{-13, 10, 100, 50, },
{-12, 10, 100, 50, },
{-11, 10, 100, 50, },
{-10, 10, 100, 50, },
{-9, 10, 100, 50, },
{-8, 10, 100, 50, },
{-7, 10, 100, 50, },
{-6, 10, 100, 50, },
{-5, 10, 100, 50, },
{-4, 10, 100, 50, },
{-3, 10, 100, 50, },
{-2, 10, 100, 50, },
{-1, 10, 100, 50, },
{0, 10, 100, 50, },
{1, 10, 100, 50, },
{2, 10, 100, 50, },
{3, 10, 100, 50, },
{4, 10, 100, 50, },
{5, 10, 100, 50, },
{6, 10, 100, 50, },
{7, 10, 100, 50, },
{8, 10, 100, 50, },
{9, 10, 100, 50, },
{10, 10, 100, 50, },
{11, 10, 11, 11, },
{12, 10, 12, 12, },
{13, 10, 13, 13, },
{14, 10, 14, 14, },
{15, 10, 15, 15, },
{16, 10, 16, 16, },
{17, 10, 17, 17, },
{18, 10, 18, 18, },
{19, 10, 19, 19, },
{20, 10, 20, 20, },
{21, 10, 21, 21, },
{22, 10, 22, 22, },
{23, 10, 23, 23, },
{24, 10, 24, 24, },
{25, 10, 25, 25, },
{26, 10, 26, 26, },
{27, 10, 27, 27, },
{28, 10, 28, 28, },
{29, 10, 29, 29, },
{30, 10, 30, 30, },
{31, 10, 31, 31, },
{32, 10, 32, 32, },
{33, 10, 33, 33, },
{34, 10, 34, 34, },
{35, 10, 35, 35, },
{36, 10, 36, 36, },
{37, 10, 37, 37, },
{38, 10, 38, 38, },
{39, 10, 39, 39, },
{40, 10, 40, 40, },
{41, 10, 41, 41, },
{42, 10, 42, 42, },
{43, 10, 43, 43, },
{44, 10, 44, 44, },
{45, 10, 45, 45, },
{46, 10, 46, 46, },
{47, 10, 47, 47, },
{48, 10, 48, 48, },
{49, 10, 49, 49, },
{50, 10, 50, 50, },
{51, 10, 51, 50, },
{52, 10, 52, 50, },
{53, 10, 53, 50, },
{54, 10, 54, 50, },
{55, 10, 55, 50, },
{56, 10, 56, 50, },
{57, 10, 57, 50, },
{58, 10, 58, 50, },
{59, 10, 59, 50, },
{60, 10, 60, 50, },
{61, 10, 61, 50, },
{62, 10, 62, 50, },
{63, 10, 63, 50, },
{64, 10, 64, 50, },
{65, 10, 65, 50, },
{66, 10, 66, 50, },
{67, 10, 67, 50, },
{68, 10, 68, 50, },
{69, 10, 69, 50, },
{70, 10, 70, 50, },
{71, 10, 71, 50, },
{72, 10, 72, 50, },
{73, 10, 73, 50, },
{74, 10, 74, 50, },
{75, 10, 75, 50, },
{76, 10, 76, 50, },
{77, 10, 77, 50, },
{78, 10, 78, 50, },
{79, 10, 79, 50, },
{80, 10, 80, 50, },
{81, 10, 81, 50, },
{82, 10, 82, 50, },
{83, 10, 83, 50, },
{84, 10, 84, 50, },
{85, 10, 85, 50, },
{86, 10, 86, 50, },
{87, 10, 87, 50, },
{88, 10, 88, 50, },
{89, 10, 89, 50, },
{90, 10, 90, 50, },
{91, 10, 91, 50, },
{92, 10, 92, 50, },
{93, 10, 93, 50, },
{94, 10, 94, 50, },
{95, 10, 95, 50, },
{96, 10, 96, 50, },
{97, 10, 97, 50, },
{98, 10, 98, 50, },
{99, 10, 99, 50, },
{100, 10, 100, 50, },
{101, 10, 101, 50, },
{102, 10, 102, 50, },
{103, 10, 103, 50, },
{104, 10, 104, 50, },
{105, 10, 105, 50, },
{106, 10, 106, 50, },
{107, 10, 107, 50, },
{108, 10, 108, 50, },
{109, 10, 109, 50, },
{110, 10, 110, 50, },
{111, 10, 111, 50, },
{112, 10, 112, 50, },
{113, 10, 113, 50, },
{114, 10, 114, 50, },
{115, 10, 115, 50, },
{116, 10, 116, 50, },
{117, 10, 117, 50, },
{118, 10, 118, 50, },
{119, 10, 119, 50, },
{120, 10, 120, 50, },
{121, 10, 121, 50, },
{122, 10, 122, 50, },
{123, 10, 123, 50, },
{124, 10, 124, 50, },
};
}
// this method must be private or protected so the auto-gen tool keeps it
private void setDefaults() {
	scale.setMaximum(100);
	scale.setMinimum(10);
	scale.setSelection(50);
}
// this method must be private or protected so the auto-gen tool keeps it
private int[][] getSetSelectionValues() {
return new int[][] {
{-15, 10, 100, 10, },
{-14, 10, 100, 10, },
{-13, 10, 100, 10, },
{-12, 10, 100, 10, },
{-11, 10, 100, 10, },
{-10, 10, 100, 10, },
{-9, 10, 100, 10, },
{-8, 10, 100, 10, },
{-7, 10, 100, 10, },
{-6, 10, 100, 10, },
{-5, 10, 100, 10, },
{-4, 10, 100, 10, },
{-3, 10, 100, 10, },
{-2, 10, 100, 10, },
{-1, 10, 100, 10, },
{0, 10, 100, 10, },
{1, 10, 100, 10, },
{2, 10, 100, 10, },
{3, 10, 100, 10, },
{4, 10, 100, 10, },
{5, 10, 100, 10, },
{6, 10, 100, 10, },
{7, 10, 100, 10, },
{8, 10, 100, 10, },
{9, 10, 100, 10, },
{10, 10, 100, 10, },
{11, 10, 100, 11, },
{12, 10, 100, 12, },
{13, 10, 100, 13, },
{14, 10, 100, 14, },
{15, 10, 100, 15, },
{16, 10, 100, 16, },
{17, 10, 100, 17, },
{18, 10, 100, 18, },
{19, 10, 100, 19, },
{20, 10, 100, 20, },
{21, 10, 100, 21, },
{22, 10, 100, 22, },
{23, 10, 100, 23, },
{24, 10, 100, 24, },
{25, 10, 100, 25, },
{26, 10, 100, 26, },
{27, 10, 100, 27, },
{28, 10, 100, 28, },
{29, 10, 100, 29, },
{30, 10, 100, 30, },
{31, 10, 100, 31, },
{32, 10, 100, 32, },
{33, 10, 100, 33, },
{34, 10, 100, 34, },
{35, 10, 100, 35, },
{36, 10, 100, 36, },
{37, 10, 100, 37, },
{38, 10, 100, 38, },
{39, 10, 100, 39, },
{40, 10, 100, 40, },
{41, 10, 100, 41, },
{42, 10, 100, 42, },
{43, 10, 100, 43, },
{44, 10, 100, 44, },
{45, 10, 100, 45, },
{46, 10, 100, 46, },
{47, 10, 100, 47, },
{48, 10, 100, 48, },
{49, 10, 100, 49, },
{50, 10, 100, 50, },
{51, 10, 100, 51, },
{52, 10, 100, 52, },
{53, 10, 100, 53, },
{54, 10, 100, 54, },
{55, 10, 100, 55, },
{56, 10, 100, 56, },
{57, 10, 100, 57, },
{58, 10, 100, 58, },
{59, 10, 100, 59, },
{60, 10, 100, 60, },
{61, 10, 100, 61, },
{62, 10, 100, 62, },
{63, 10, 100, 63, },
{64, 10, 100, 64, },
{65, 10, 100, 65, },
{66, 10, 100, 66, },
{67, 10, 100, 67, },
{68, 10, 100, 68, },
{69, 10, 100, 69, },
{70, 10, 100, 70, },
{71, 10, 100, 71, },
{72, 10, 100, 72, },
{73, 10, 100, 73, },
{74, 10, 100, 74, },
{75, 10, 100, 75, },
{76, 10, 100, 76, },
{77, 10, 100, 77, },
{78, 10, 100, 78, },
{79, 10, 100, 79, },
{80, 10, 100, 80, },
{81, 10, 100, 81, },
{82, 10, 100, 82, },
{83, 10, 100, 83, },
{84, 10, 100, 84, },
{85, 10, 100, 85, },
{86, 10, 100, 86, },
{87, 10, 100, 87, },
{88, 10, 100, 88, },
{89, 10, 100, 89, },
{90, 10, 100, 90, },
{91, 10, 100, 91, },
{92, 10, 100, 92, },
{93, 10, 100, 93, },
{94, 10, 100, 94, },
{95, 10, 100, 95, },
{96, 10, 100, 96, },
{97, 10, 100, 97, },
{98, 10, 100, 98, },
{99, 10, 100, 99, },
{100, 10, 100, 100, },
{101, 10, 100, 100, },
{102, 10, 100, 100, },
{103, 10, 100, 100, },
{104, 10, 100, 100, },
{105, 10, 100, 100, },
{106, 10, 100, 100, },
{107, 10, 100, 100, },
{108, 10, 100, 100, },
{109, 10, 100, 100, },
{110, 10, 100, 100, },
{111, 10, 100, 100, },
{112, 10, 100, 100, },
{113, 10, 100, 100, },
{114, 10, 100, 100, },
{115, 10, 100, 100, },
{116, 10, 100, 100, },
{117, 10, 100, 100, },
{118, 10, 100, 100, },
{119, 10, 100, 100, },
{120, 10, 100, 100, },
{121, 10, 100, 100, },
{122, 10, 100, 100, },
{123, 10, 100, 100, },
{124, 10, 100, 100, },
};
}

public void test_consistency_KeySelection () {
    consistencyEvent(0, SWT.ARROW_RIGHT, 0, 0, ConsistencyUtility.KEY_PRESS);
}

public void test_consistency_ThumbSelection () {
    consistencyEvent(9, 10, 1, 0, ConsistencyUtility.MOUSE_CLICK);
}

public void test_consistency_TroughSelection () {
    consistencyEvent(27, 10, 1, 0, ConsistencyUtility.MOUSE_CLICK);
}

public void test_consistency_MenuDetect () {
    consistencyEvent(27, 5, 3, 0, ConsistencyUtility.MOUSE_CLICK);
}

public void test_consistency_DragDetect () {
    consistencyEvent(9, 5, 30, 10, ConsistencyUtility.MOUSE_DRAG);
}

}
