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
import org.eclipse.test.performance.PerformanceMeter;

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
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		regions[i] = new Region ();
	}
	meter.stop();
	
	for (int i = 0; i < COUNT; i++) {
		regions[i].dispose();
	}
	
	disposeMeter(meter);
}

public void test_ConstructorLorg_eclipse_swt_graphics_Device() {
	Region[] regions = new Region [COUNT];
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		regions[i] = new Region (display);
	}
	meter.stop();
	
	for (int i = 0; i < COUNT; i++) {
		regions[i].dispose();
	}
	
	disposeMeter(meter);
}

public void test_add$I() {
	Region region = new Region(display);
	int[][] toAdd = new int[COUNT][];
	for (int i = 0; i < COUNT; i++) {
		toAdd[i] = new int[] {i,i, i,i, i+1,i+1, i+1,i+1};
	}
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		region.add(toAdd[i]);
	}
	meter.stop();
	
	region.dispose();
	
	disposeMeter(meter);
}

public void test_addLorg_eclipse_swt_graphics_Rectangle() {
	Region region = new Region(display);
	Rectangle[] toAdd = new Rectangle[COUNT];
	for (int i = 0; i < COUNT; i++) {
		toAdd[i] = new Rectangle (i, i, i+1, i+1);
	}
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		region.add(toAdd[i]);
	}
	meter.stop();
	
	region.dispose();
	
	disposeMeter(meter);
}

public void test_addLorg_eclipse_swt_graphics_Region() {
	Region region = new Region(display);
	Region[] regions = new Region[COUNT];
	for (int i = 0; i < COUNT; i++) {
		Region newRegion = new Region (display);
		newRegion.add(new Rectangle (i, i, i+1, i+1));
		regions[i] = newRegion;
	}
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		region.add(regions[i]);
	}
	meter.stop();
	
	region.dispose();
	for (int i = 0; i < COUNT; i++) {
		regions[i].dispose();
	}
	
	disposeMeter(meter);
}

public void test_containsII() {
	Region region = new Region (display);
	region.add(new Rectangle (30,30,30,30));
	
	PerformanceMeter meter = createMeter("contained");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		region.contains(50, 50);	// contained
	}
	meter.stop();
	
	region.dispose();

	disposeMeter(meter);
	
	region = new Region (display);
	region.add(new Rectangle (30,30,30,30));

	meter = createMeter("disjoint");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		region.contains(20, 20);	// not contained
	}
	meter.stop();
	
	region.dispose();

	disposeMeter(meter);
}

public void test_containsLorg_eclipse_swt_graphics_Point() {
	Region region = new Region (display);
	Point point = new Point (20,20);
	region.add(new Rectangle (30,30,30,30));
	
	PerformanceMeter meter = createMeter("contained");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		region.contains(point);	// contained
	}
	meter.stop();
	
	region.dispose();

	disposeMeter(meter);
	
	region = new Region (display);
	region.add(new Rectangle (30,30,30,30));

	meter = createMeter("disjoint");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		region.contains(point);	// not contained
	}
	meter.stop();
	
	region.dispose();

	disposeMeter(meter);
}

public void test_dispose() {
	Region[] regions = new Region [COUNT];
	for (int i = 0; i < COUNT; i++) {
		regions[i] = new Region(display);
		regions[i].add(new Rectangle(i, i, i+5, i+5));
	}
	
	PerformanceMeter meter = createMeter("not disposed");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		regions[i].dispose();	// dispose
	}
	meter.stop();
	
    disposeMeter(meter);
    
	meter = createMeter("disposed");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		regions[i].dispose();	// dispose disposed
	}
	meter.stop();
	
    disposeMeter(meter);
}

public void test_equalsLjava_lang_Object() {
	//	Currently, Regions are only "equal" if they have the same handle.
	//	This is so that identical objects are properly hashed.
	//	We are considering adding a new method that will compare Regions for the same area.

	Rectangle rect = new Rectangle(25, 100, 200, 780);
	Region region1 = new Region(display);
	region1.add(rect);
	
	PerformanceMeter meter = createMeter("equal");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		region1.equals(region1);	// identical
	}
	meter.stop();

	region1.dispose();
	
	disposeMeter(meter);
	
	region1 = new Region(display);
	region1.add(rect);
	Region region2 = new Region(display);
	region2.add(rect);
	
	meter = createMeter("not equal");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		region1.equals(region2);	// unique
	}
	meter.stop();

	region1.dispose();
	region2.dispose();
	
	disposeMeter(meter);
}

public void test_getBounds() {
	Region region = new Region(display);
	region.add(new Rectangle(10,10,20,20));

	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		region.getBounds();
	}
	meter.stop();
	
	region.dispose();
	
	disposeMeter(meter);
}

public void test_hashCode() {
	Region region = new Region(display);
	region.add(new Rectangle(10,10,20,20));

	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		region.hashCode();
	}
	meter.stop();
	
	region.dispose();
	
	disposeMeter(meter);
}

public void test_intersectLorg_eclipse_swt_graphics_Rectangle() {
	Region region = new Region(display);
	region.add(new Rectangle(10,10,20,20));
	Rectangle rect = new Rectangle(0,0,5,5);
	
	PerformanceMeter meter = createMeter("disjoint");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		region.intersect(rect);	// disjoint
	}
	meter.stop();
	
	region.dispose();
	
	disposeMeter(meter);

	region = new Region(display);
	region.add(new Rectangle(10,10,20,20));
	rect = new Rectangle(20,20,5,5);
	
	meter = createMeter("intersect");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		region.intersect(rect);	// intersects
	}
	meter.stop();
	
	region.dispose();
	
	disposeMeter(meter);
}

public void test_intersectLorg_eclipse_swt_graphics_Region() {
	Region region1 = new Region(display);
	region1.add(new Rectangle(10,10,20,20));
	Region region2 = new Region(display);
	region2.add(new Rectangle(0,0,5,5));
	
	PerformanceMeter meter = createMeter("disjoint");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		region1.intersect(region2);	// disjoint
	}
	meter.stop();
	
	region1.dispose();
	region2.dispose();
	
	disposeMeter(meter);

	region1 = new Region(display);
	region1.add(new Rectangle(10,10,20,20));
	region2 = new Region(display);
	region2.add(new Rectangle(20,20,5,5));
	
	meter = createMeter("intersect");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		region1.intersect(region2);	// intersects
	}
	meter.stop();
	
	region1.dispose();
	
	disposeMeter(meter);
}

public void test_intersectsIIII() {
	Region region = new Region(display);
	region.add(new Rectangle(10,10,20,20));
	
	PerformanceMeter meter = createMeter("disjoint");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		region.intersects(0,0,5,5);		// disjoint
	}
	meter.stop();
	
	region.dispose();
	
	disposeMeter(meter);

	region = new Region(display);
	region.add(new Rectangle(10,10,20,20));
	
	meter = createMeter("intersect");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		region.intersects(20,20,5,5);	// intersects
	}
	meter.stop();
	
	region.dispose();
	
	disposeMeter(meter);
}

public void test_intersectsLorg_eclipse_swt_graphics_Rectangle() {
	Region region = new Region(display);
	region.add(new Rectangle(10,10,20,20));
	Rectangle rect = new Rectangle (0,0,5,5);
	
	PerformanceMeter meter = createMeter("disjoint");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		region.intersects(rect);	// disjoint
	}
	meter.stop();
	
	region.dispose();
	
	disposeMeter(meter);

	region = new Region(display);
	region.add(new Rectangle(10,10,20,20));
	rect = new Rectangle (20,20,5,5);
	
	meter = createMeter("intersect");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		region.intersects(rect);	// intersects
	}
	meter.stop();
	
	region.dispose();
	
	disposeMeter(meter);
}

public void test_isDisposed() {
	Region region = new Region(display);
	region.add(new Rectangle(10,10,10,10));
	
	PerformanceMeter meter = createMeter("not disposed");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		region.isDisposed();	// not disposed
	}
	meter.stop();
	
	region.dispose();
	
	disposeMeter(meter);
	
	meter = createMeter("disposed");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		region.isDisposed();	// disposed
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_isEmpty() {
	Region region = new Region (display);
	
	PerformanceMeter meter = createMeter("empty");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		region.isEmpty();		// empty
	}
	meter.stop();
	
	disposeMeter(meter);

	region.dispose();
	region = new Region (display);
	region.add(new Rectangle(10,10,10,10));
	
	meter = createMeter("not empty");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		region.isEmpty();		// not empty
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_subtract$I() {
	Region region = new Region(display);
	region.add(new Rectangle(0,0,COUNT * 2, COUNT * 2));
	int[][] toSubtract = new int[COUNT][];
	for (int i = 0; i < COUNT; i++) {
		toSubtract[i] = new int[] {i,i, i,i, i+1,i+1, i+1,i+1};
	}
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		region.subtract(toSubtract[i]);
	}
	meter.stop();
	
	region.dispose();
	
	disposeMeter(meter);
}

public void test_subtractLorg_eclipse_swt_graphics_Rectangle() {
	Region region = new Region(display);
	region.add(new Rectangle(0,0,COUNT * 2, COUNT * 2));
	Rectangle[] toSubtract = new Rectangle[COUNT];
	for (int i = 0; i < COUNT; i++) {
		toSubtract[i] = new Rectangle (i, i, i+1, i+1);
	}
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		region.subtract(toSubtract[i]);
	}
	meter.stop();
	
	region.dispose();
	
	disposeMeter(meter);
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
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		region.subtract(regions[i]);
	}
	meter.stop();
	
	region.dispose();
	for (int i = 0; i < COUNT; i++) {
		regions[i].dispose();
	}
	
	disposeMeter(meter);
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
