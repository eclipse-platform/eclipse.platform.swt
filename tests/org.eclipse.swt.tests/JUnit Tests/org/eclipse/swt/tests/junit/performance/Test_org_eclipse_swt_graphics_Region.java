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

import junit.framework.*;
import junit.textui.*;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * Automated Performance Test Suite for class org.eclipse.swt.graphics.Region
 *
 * @see org.eclipse.swt.graphics.Region
 */
public class Test_org_eclipse_swt_graphics_Region extends SwtPerformanceTestCase {
	static final int COUNT = 1000;

public Test_org_eclipse_swt_graphics_Region(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() throws Exception {
	super.setUp();
	display = Display.getDefault();
}

public void test_Constructor() {
	Region[] regions = new Region [COUNT];
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		regions[i] = new Region ();
	}
	stopMeasuring();
	for (int i = 0; i < COUNT; i++) {
		regions[i].dispose();
	}
	
	commitMeasurements();
	assertPerformance();
}

public void test_ConstructorLorg_eclipse_swt_graphics_Device() {
	Region[] regions = new Region [COUNT];
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		regions[i] = new Region (display);
	}
	stopMeasuring();
	for (int i = 0; i < COUNT; i++) {
		regions[i].dispose();
	}
	
	commitMeasurements();
	assertPerformance();
}

public void test_add$I() {
	Region region = new Region(display);
	int[][] toAdd = new int[COUNT][];
	for (int i = 0; i < COUNT; i++) {
		toAdd[i] = new int[] {i,i, i,i, i+1,i+1, i+1,i+1};
	}
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		region.add(toAdd[i]);
	}
	stopMeasuring();
	
	region.dispose();
	
	commitMeasurements();
	assertPerformance();
}

public void test_addLorg_eclipse_swt_graphics_Rectangle() {
	Region region = new Region(display);
	Rectangle[] toAdd = new Rectangle[COUNT];
	for (int i = 0; i < COUNT; i++) {
		toAdd[i] = new Rectangle (i, i, i+1, i+1);
	}
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		region.add(toAdd[i]);
	}
	stopMeasuring();
	
	region.dispose();
	
	commitMeasurements();
	assertPerformance();
}

public void test_addLorg_eclipse_swt_graphics_Region() {
	Region region = new Region(display);
	Region[] regions = new Region[COUNT];
	for (int i = 0; i < COUNT; i++) {
		Region newRegion = new Region (display);
		newRegion.add(new Rectangle (i, i, i+1, i+1));
		regions[i] = newRegion;
	}
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		region.add(regions[i]);
	}
	stopMeasuring();
	
	region.dispose();
	for (int i = 0; i < COUNT; i++) {
		regions[i].dispose();
	}
	
	commitMeasurements();
	assertPerformance();
}

public void test_containsII() {
	Region region = new Region (display);
	region.add(new Rectangle (30,30,30,30));
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		region.contains(50, 50);	// contained
	}
	stopMeasuring();
	
	region.dispose();

	commitMeasurements();
	assertPerformance();
	
	region = new Region (display);
	region.add(new Rectangle (30,30,30,30));

	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		region.contains(20, 20);	// not contained
	}
	stopMeasuring();
	
	region.dispose();

	commitMeasurements();
	assertPerformance();
}

public void test_containsLorg_eclipse_swt_graphics_Point() {
	Region region = new Region (display);
	Point point = new Point (20,20);
	region.add(new Rectangle (30,30,30,30));
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		region.contains(point);	// contained
	}
	stopMeasuring();
	
	region.dispose();

	commitMeasurements();
	assertPerformance();
	
	region = new Region (display);
	region.add(new Rectangle (30,30,30,30));

	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		region.contains(point);	// not contained
	}
	stopMeasuring();
	
	region.dispose();

	commitMeasurements();
	assertPerformance();
}

public void test_dispose() {
	Region[] regions = new Region [COUNT];
	for (int i = 0; i < COUNT; i++) {
		regions[i] = new Region(display);
		regions[i].add(new Rectangle(i, i, i+5, i+5));
	}
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		regions[i].dispose();	// dispose
	}
	stopMeasuring();
	
    commitMeasurements();
    assertPerformance();
    
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		regions[i].dispose();	// dispose disposed
	}
	stopMeasuring();
	
    commitMeasurements();
    assertPerformance();
}

public void test_equalsLjava_lang_Object() {
	//	Currently, Regions are only "equal" if they have the same handle.
	//	This is so that identical objects are properly hashed.
	//	We are considering adding a new method that will compare Regions for the same area.

	Rectangle rect = new Rectangle(25, 100, 200, 780);
	Region region1 = new Region(display);
	region1.add(rect);
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		region1.equals(region1);	// identical
	}
	stopMeasuring();

	region1.dispose();
	
	commitMeasurements();
	assertPerformance();
	
	region1 = new Region(display);
	region1.add(rect);
	Region region2 = new Region(display);
	region2.add(rect);
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		region1.equals(region2);	// unique
	}
	stopMeasuring();

	region1.dispose();
	region2.dispose();
	
	commitMeasurements();
	assertPerformance();
}

public void test_getBounds() {
	Region region = new Region(display);
	region.add(new Rectangle(10,10,20,20));

	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		region.getBounds();
	}
	stopMeasuring();
	
	region.dispose();
	
	commitMeasurements();
	assertPerformance();
}

public void test_hashCode() {
	Region region = new Region(display);
	region.add(new Rectangle(10,10,20,20));

	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		region.hashCode();
	}
	stopMeasuring();
	
	region.dispose();
	
	commitMeasurements();
	assertPerformance();
}

public void test_intersectLorg_eclipse_swt_graphics_Rectangle() {
	Region region = new Region(display);
	region.add(new Rectangle(10,10,20,20));
	Rectangle rect = new Rectangle(0,0,5,5);
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		region.intersect(rect);	// disjoint
	}
	stopMeasuring();
	
	region.dispose();
	
	commitMeasurements();
	assertPerformance();

	region = new Region(display);
	region.add(new Rectangle(10,10,20,20));
	rect = new Rectangle(20,20,5,5);
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		region.intersect(rect);	// intersects
	}
	stopMeasuring();
	
	region.dispose();
	
	commitMeasurements();
	assertPerformance();
}

public void test_intersectLorg_eclipse_swt_graphics_Region() {
	Region region1 = new Region(display);
	region1.add(new Rectangle(10,10,20,20));
	Region region2 = new Region(display);
	region2.add(new Rectangle(0,0,5,5));
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		region1.intersect(region2);	// disjoint
	}
	stopMeasuring();
	
	region1.dispose();
	region2.dispose();
	
	commitMeasurements();
	assertPerformance();

	region1 = new Region(display);
	region1.add(new Rectangle(10,10,20,20));
	region2 = new Region(display);
	region2.add(new Rectangle(20,20,5,5));
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		region1.intersect(region2);	// intersects
	}
	stopMeasuring();
	
	region1.dispose();
	
	commitMeasurements();
	assertPerformance();
}

public void test_intersectsIIII() {
	Region region = new Region(display);
	region.add(new Rectangle(10,10,20,20));
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		region.intersects(0,0,5,5);		// disjoint
	}
	stopMeasuring();
	
	region.dispose();
	
	commitMeasurements();
	assertPerformance();

	region = new Region(display);
	region.add(new Rectangle(10,10,20,20));
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		region.intersects(20,20,5,5);	// intersects
	}
	stopMeasuring();
	
	region.dispose();
	
	commitMeasurements();
	assertPerformance();
}

public void test_intersectsLorg_eclipse_swt_graphics_Rectangle() {
	Region region = new Region(display);
	region.add(new Rectangle(10,10,20,20));
	Rectangle rect = new Rectangle (0,0,5,5);
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		region.intersects(rect);	// disjoint
	}
	stopMeasuring();
	
	region.dispose();
	
	commitMeasurements();
	assertPerformance();

	region = new Region(display);
	region.add(new Rectangle(10,10,20,20));
	rect = new Rectangle (20,20,5,5);
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		region.intersects(rect);	// intersects
	}
	stopMeasuring();
	
	region.dispose();
	
	commitMeasurements();
	assertPerformance();
}

public void test_isDisposed() {
	Region region = new Region(display);
	region.add(new Rectangle(10,10,10,10));
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		region.isDisposed();	// not disposed
	}
	stopMeasuring();
	
	region.dispose();
	
	commitMeasurements();
	assertPerformance();
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		region.isDisposed();	// disposed
	}
	stopMeasuring();
	
	commitMeasurements();
	assertPerformance();
}

public void test_isEmpty() {
	Region region = new Region (display);
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		region.isEmpty();		// empty
	}
	stopMeasuring();
	
	commitMeasurements();
	assertPerformance();

	region.dispose();
	region = new Region (display);
	region.add(new Rectangle(10,10,10,10));
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		region.isEmpty();		// not empty
	}
	stopMeasuring();
	
	commitMeasurements();
	assertPerformance();
}

public void test_subtract$I() {
	Region region = new Region(display);
	region.add(new Rectangle(0,0,COUNT * 2, COUNT * 2));
	int[][] toSubtract = new int[COUNT][];
	for (int i = 0; i < COUNT; i++) {
		toSubtract[i] = new int[] {i,i, i,i, i+1,i+1, i+1,i+1};
	}
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		region.subtract(toSubtract[i]);
	}
	stopMeasuring();
	
	region.dispose();
	
	commitMeasurements();
	assertPerformance();
}

public void test_subtractLorg_eclipse_swt_graphics_Rectangle() {
	Region region = new Region(display);
	region.add(new Rectangle(0,0,COUNT * 2, COUNT * 2));
	Rectangle[] toSubtract = new Rectangle[COUNT];
	for (int i = 0; i < COUNT; i++) {
		toSubtract[i] = new Rectangle (i, i, i+1, i+1);
	}
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		region.subtract(toSubtract[i]);
	}
	stopMeasuring();
	
	region.dispose();
	
	commitMeasurements();
	assertPerformance();
}

public void test_subtractLorg_eclipse_swt_graphics_Region() {
	Region region = new Region(display);
	region.add(new Rectangle(0, 0, COUNT*2, COUNT*2));
	Region[] regions = new Region[COUNT];
	for (int i = 0; i < COUNT; i++) {
		Region newRegion = new Region (display);
		newRegion.add(new Rectangle (i, i, i+1, i+1));
		regions[i] = newRegion;
	}
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		region.subtract(regions[i]);
	}
	stopMeasuring();
	
	region.dispose();
	for (int i = 0; i < COUNT; i++) {
		regions[i].dispose();
	}
	
	commitMeasurements();
	assertPerformance();
}

public void test_win32_newLorg_eclipse_swt_graphics_DeviceI() {
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_graphics_Region((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_Constructor");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_graphics_Device");
	methodNames.addElement("test_add$I");
	methodNames.addElement("test_addLorg_eclipse_swt_graphics_Rectangle");
	methodNames.addElement("test_addLorg_eclipse_swt_graphics_Region");
	methodNames.addElement("test_containsII");
	methodNames.addElement("test_containsLorg_eclipse_swt_graphics_Point");
	methodNames.addElement("test_dispose");
	methodNames.addElement("test_equalsLjava_lang_Object");
	methodNames.addElement("test_getBounds");
	methodNames.addElement("test_hashCode");
	methodNames.addElement("test_intersectLorg_eclipse_swt_graphics_Rectangle");
	methodNames.addElement("test_intersectLorg_eclipse_swt_graphics_Region");
	methodNames.addElement("test_intersectsIIII");
	methodNames.addElement("test_intersectsLorg_eclipse_swt_graphics_Rectangle");
	methodNames.addElement("test_isDisposed");
	methodNames.addElement("test_isEmpty");
	methodNames.addElement("test_subtract$I");
	methodNames.addElement("test_subtractLorg_eclipse_swt_graphics_Rectangle");
	methodNames.addElement("test_subtractLorg_eclipse_swt_graphics_Region");
	methodNames.addElement("test_win32_newLorg_eclipse_swt_graphics_DeviceI");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_Constructor")) test_Constructor();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_graphics_Device")) test_ConstructorLorg_eclipse_swt_graphics_Device();
	else if (getName().equals("test_add$I")) test_add$I();
	else if (getName().equals("test_addLorg_eclipse_swt_graphics_Rectangle")) test_addLorg_eclipse_swt_graphics_Rectangle();
	else if (getName().equals("test_addLorg_eclipse_swt_graphics_Region")) test_addLorg_eclipse_swt_graphics_Region();
	else if (getName().equals("test_containsII")) test_containsII();
	else if (getName().equals("test_containsLorg_eclipse_swt_graphics_Point")) test_containsLorg_eclipse_swt_graphics_Point();
	else if (getName().equals("test_dispose")) test_dispose();
	else if (getName().equals("test_equalsLjava_lang_Object")) test_equalsLjava_lang_Object();
	else if (getName().equals("test_getBounds")) test_getBounds();
	else if (getName().equals("test_hashCode")) test_hashCode();
	else if (getName().equals("test_intersectLorg_eclipse_swt_graphics_Rectangle")) test_intersectLorg_eclipse_swt_graphics_Rectangle();
	else if (getName().equals("test_intersectLorg_eclipse_swt_graphics_Region")) test_intersectLorg_eclipse_swt_graphics_Region();
	else if (getName().equals("test_intersectsIIII")) test_intersectsIIII();
	else if (getName().equals("test_intersectsLorg_eclipse_swt_graphics_Rectangle")) test_intersectsLorg_eclipse_swt_graphics_Rectangle();
	else if (getName().equals("test_isDisposed")) test_isDisposed();
	else if (getName().equals("test_isEmpty")) test_isEmpty();
	else if (getName().equals("test_subtract$I")) test_subtract$I();
	else if (getName().equals("test_subtractLorg_eclipse_swt_graphics_Rectangle")) test_subtractLorg_eclipse_swt_graphics_Rectangle();
	else if (getName().equals("test_subtractLorg_eclipse_swt_graphics_Region")) test_subtractLorg_eclipse_swt_graphics_Region();
	else if (getName().equals("test_win32_newLorg_eclipse_swt_graphics_DeviceI")) test_win32_newLorg_eclipse_swt_graphics_DeviceI();
}

/* custom */
	Display display;
}
