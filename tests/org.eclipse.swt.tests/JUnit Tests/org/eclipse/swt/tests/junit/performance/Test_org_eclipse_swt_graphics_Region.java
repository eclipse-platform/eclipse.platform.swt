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
	final int COUNT = 2000000;
	
	PerformanceMeter meter = createMeter("Region constr.()");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		/*
		* This test is not really valid since it's measuring both creation and
		* disposal of the Regions.  This is necessary because attempting to defer
		* the region disposal until the timer has been stopped causes a No More
		* Handles error.
		*/
		new Region().dispose();
	}
	meter.stop();

	disposeMeter(meter);
}

public void test_ConstructorLorg_eclipse_swt_graphics_Device() {
	final int COUNT = 2000000;
	
	PerformanceMeter meter = createMeter("Region constr.(Device)");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		/*
		* This test is not really valid since it's measuring both creation and
		* disposal of the Regions.  This is necessary because attempting to defer
		* the region disposal until the timer has been stopped causes a No More
		* Handles error.
		*/
		new Region(display).dispose();
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_add$I() {
	final int COUNT = 250000;
	
	Region region = new Region(display);
	int[][] toAdd = new int[COUNT][];
	for (int i = 0; i < COUNT; i++) {
		toAdd[i] = new int[] {i,i, i,i, i+1,i+1, i+1,i+1};
	}
	
	PerformanceMeter meter = createMeter("Region add($I)");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		region.add(toAdd[i]);
	}
	meter.stop();
	
	region.dispose();
	
	disposeMeter(meter);
}

public void test_addLorg_eclipse_swt_graphics_Rectangle() {
	final int COUNT = 4000;
	
	Region region = new Region(display);
	Rectangle[] toAdd = new Rectangle[COUNT];
	for (int i = 0; i < COUNT; i++) {
		toAdd[i] = new Rectangle (i, i, i+1, i+1);
	}
	
	PerformanceMeter meter = createMeter("Region add(Rectangle)");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		region.add(toAdd[i]);
	}
	meter.stop();
	
	region.dispose();
	
	disposeMeter(meter);
}

public void test_addLorg_eclipse_swt_graphics_Region() {
	final int COUNT = 4000;
	
	Region region = new Region(display);
	Region[] regions = new Region[COUNT];
	for (int i = 0; i < COUNT; i++) {
		Region newRegion = new Region (display);
		newRegion.add(new Rectangle (i, i, i+1, i+1));
		regions[i] = newRegion;
	}
	
	PerformanceMeter meter = createMeter("Region add(Region)");
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
	final int COUNT = 25000000;
	
	Region region = new Region (display);
	region.add(new Rectangle (30,30,30,30));
	
	PerformanceMeter meter = createMeter("Region contains(II) - yes");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		region.contains(50, 50);	// contained
	}
	meter.stop();
	
	region.dispose();

	disposeMeter(meter);
	
	region = new Region (display);
	region.add(new Rectangle (30,30,30,30));

	meter = createMeter("Region contains(II) - no");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		region.contains(20, 20);	// not contained
	}
	meter.stop();
	
	region.dispose();

	disposeMeter(meter);
}

public void test_containsLorg_eclipse_swt_graphics_Point() {
	final int COUNT = 20000000;
	
	Region region = new Region (display);
	Point point = new Point (20,20);
	region.add(new Rectangle (30,30,30,30));
	
	PerformanceMeter meter = createMeter("Region contains(Point) - yes");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		region.contains(point);	// contained
	}
	meter.stop();
	
	region.dispose();

	disposeMeter(meter);
	
	region = new Region (display);
	region.add(new Rectangle (30,30,30,30));

	meter = createMeter("Region contains(Point) - no");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		region.contains(point);	// not contained
	}
	meter.stop();
	
	region.dispose();

	disposeMeter(meter);
}

public void test_dispose() {
	final int COUNT = 50000000;

	/*
	* The tests for the constructors cover the base dispose case since
	* they have to dispose of created Regions within their timer blocks.  
	*/
    
	Region region = new Region(display);
	region.dispose();
	
	PerformanceMeter meter = createMeter("Region dispose - disposed");
	
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		region.dispose();	// dispose disposed
	}
	meter.stop();
	
    disposeMeter(meter);
}

public void test_equalsLjava_lang_Object() {
	final int COUNT = 50000000;
	
	//	Currently, Regions are only "equal" if they have the same handle.
	//	This is so that identical objects are properly hashed.
	//	We are considering adding a new method that will compare Regions for the same area.

	Rectangle rect = new Rectangle(25, 100, 200, 780);
	Region region1 = new Region(display);
	region1.add(rect);
	
	PerformanceMeter meter = createMeter("Region equals - yes");
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
	
	meter = createMeter("Region equals - no");
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
	final int COUNT = 4500000;
	
	Region region = new Region(display);
	region.add(new Rectangle(10,10,20,20));

	PerformanceMeter meter = createMeter("Region getBounds");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		region.getBounds();
	}
	meter.stop();
	
	region.dispose();
	
	disposeMeter(meter);
}

public void test_hashCode() {
	final int COUNT = 700000000;
	
	Region region = new Region(display);
	region.add(new Rectangle(10,10,20,20));

	PerformanceMeter meter = createMeter("Region hashCode");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		region.hashCode();
	}
	meter.stop();
	
	region.dispose();
	
	disposeMeter(meter);
}

public void test_intersectLorg_eclipse_swt_graphics_Rectangle() {
	final int COUNT = 2000000;
	
	Region region = new Region(display);
	region.add(new Rectangle(10,10,20,20));
	Rectangle rect = new Rectangle(0,0,5,5);
	
	PerformanceMeter meter = createMeter("Region intersect(Rectangle) - disjoint");
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
	
	meter = createMeter("Region intersect(Rectangle) - contained");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		region.intersect(rect);	// intersects
	}
	meter.stop();
	
	region.dispose();
	
	disposeMeter(meter);
}

public void test_intersectLorg_eclipse_swt_graphics_Region() {
	final int COUNT = 12000000;
	
	Region region1 = new Region(display);
	region1.add(new Rectangle(10,10,20,20));
	Region region2 = new Region(display);
	region2.add(new Rectangle(0,0,5,5));
	
	PerformanceMeter meter = createMeter("Region intersect(Region) - disjoint");
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
	
	meter = createMeter("Region intersect(Region) - contained");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		region1.intersect(region2);	// intersects
	}
	meter.stop();
	
	region1.dispose();
	
	disposeMeter(meter);
}

public void test_intersectsIIII() {
	final int COUNT = 2500000;
	
	Region region = new Region(display);
	region.add(new Rectangle(10,10,20,20));
	
	PerformanceMeter meter = createMeter("Region intersects(IIII) - no");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		region.intersects(0,0,5,5);		// disjoint
	}
	meter.stop();
	
	region.dispose();
	
	disposeMeter(meter);

	region = new Region(display);
	region.add(new Rectangle(10,10,20,20));
	
	meter = createMeter("Region intersects(IIII) - yes");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		region.intersects(20,20,5,5);	// intersects
	}
	meter.stop();
	
	region.dispose();
	
	disposeMeter(meter);
}

public void test_intersectsLorg_eclipse_swt_graphics_Rectangle() {
	final int COUNT = 2500000;
	
	Region region = new Region(display);
	region.add(new Rectangle(10,10,20,20));
	Rectangle rect = new Rectangle (0,0,5,5);
	
	PerformanceMeter meter = createMeter("Region intersects(Rectangle) - no");
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
	
	meter = createMeter("Region intersects(Rectangle) - yes");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		region.intersects(rect);	// intersects
	}
	meter.stop();
	
	region.dispose();
	
	disposeMeter(meter);
}

public void test_isDisposed() {
	final int COUNT = 500000000;
	
	Region region = new Region(display);
	region.add(new Rectangle(10,10,10,10));
	
	PerformanceMeter meter = createMeter("Region isDisposed - no");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		region.isDisposed();	// not disposed
	}
	meter.stop();
	
	region.dispose();
	
	disposeMeter(meter);
	
	meter = createMeter("Region isDisposed - yes");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		region.isDisposed();	// disposed
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_isEmpty() {
	final int COUNT = 5000000;
	
	Region region = new Region (display);
	
	PerformanceMeter meter = createMeter("Region isEmpty - yes");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		region.isEmpty();		// empty
	}
	meter.stop();
	
	disposeMeter(meter);

	region.dispose();
	region = new Region (display);
	region.add(new Rectangle(10,10,10,10));
	
	meter = createMeter("Region isEmpty - no");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		region.isEmpty();		// not empty
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_subtract$I() {
	final int COUNT = 250000;
	
	Region region = new Region(display);
	region.add(new Rectangle(0,0,COUNT * 2, COUNT * 2));
	int[][] toSubtract = new int[COUNT][];
	for (int i = 0; i < COUNT; i++) {
		toSubtract[i] = new int[] {i,i, i,i, i+1,i+1, i+1,i+1};
	}
	
	PerformanceMeter meter = createMeter("Region subtract($I)");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		region.subtract(toSubtract[i]);
	}
	meter.stop();
	
	region.dispose();
	
	disposeMeter(meter);
}

public void test_subtractLorg_eclipse_swt_graphics_Rectangle() {
	final int COUNT = 3000;
	
	Region region = new Region(display);
	region.add(new Rectangle(0,0,COUNT * 2, COUNT * 2));
	Rectangle[] toSubtract = new Rectangle[COUNT];
	for (int i = 0; i < COUNT; i++) {
		toSubtract[i] = new Rectangle (i, i, i+1, i+1);
	}
	
	PerformanceMeter meter = createMeter("Region subtract(Rectangle)");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		region.subtract(toSubtract[i]);
	}
	meter.stop();
	
	region.dispose();
	
	disposeMeter(meter);
}

public void test_subtractLorg_eclipse_swt_graphics_Region() {
	final int COUNT = 3000;
	
	Region region = new Region(display);
	region.add(new Rectangle(0, 0, COUNT*2, COUNT*2));
	Region[] regions = new Region[COUNT];
	for (int i = 0; i < COUNT; i++) {
		Region newRegion = new Region (display);
		newRegion.add(new Rectangle (i, i, i+1, i+1));
		regions[i] = newRegion;
	}
	
	PerformanceMeter meter = createMeter("Region subtract(Region)");
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
}

/* custom */
	Display display;
}
