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
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * Automated Test Suite for class org.eclipse.swt.graphics.Region
 *
 * @see org.eclipse.swt.graphics.Region
 */
public class Test_org_eclipse_swt_graphics_Region extends SwtTestCase {

public Test_org_eclipse_swt_graphics_Region(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	super.setUp();
	display = Display.getDefault();
}

public void test_Constructor() {
	// test Region()
	Region reg = new Region();
	if (reg.isDisposed()) {
		fail("Constructor for Region didn't initialize");
	}
	reg.dispose();
}

public void test_ConstructorLorg_eclipse_swt_graphics_Device() {
	Region reg = new Region(display);
	if (reg.isDisposed()) {
		fail("Constructor for Region didn't initialize");
	}
	reg.dispose();
}

public void test_add$I() {
	Region reg = new Region(display);
	try {
		reg.add((int[])null);
		reg.dispose();
		fail("no exception thrown for adding a null rectangle");
	}
	catch (IllegalArgumentException e) {
	}	
	reg.dispose();
	try {
		reg.add(new int[]{});
		reg.dispose();
		fail("no exception thrown on disposed region");
	}
	catch (SWTException e) {
	}
	reg.dispose();
	
	reg = new Region(display);
	reg.add(new int[] {0,0, 50,0, 50,25, 0,25});
	Rectangle box = reg.getBounds();
	reg.dispose();
	Rectangle expected = new Rectangle (0,0,50,25);
	if (!box.equals(expected)) {
		fail("add 1 failed - expected: "+expected+" actual: "+box);
	}
	
	reg = new Region(display);
	reg.add(new int[] {0,0, 50,0, 50,25, 0,25});
	reg.add(new int[] {0,25, 50,25, 50,40, 0,40});
	box = reg.getBounds();
	reg.dispose();
	expected = new Rectangle (0,0,50,40);
	if (!box.equals(expected)) {
		fail("add 2 failed - expected: "+expected+" actual: "+box);
	}
	
//	reg = new Region(display);
//	reg.add(new int[] {10,10, 40,30, 20,60, 5,55});
//	box = reg.getBounds();
//	reg.dispose();
//	expected = new Rectangle (5,10,35,50);
//	if (!box.equals(expected)) {
//		fail("add 3 failed - expected: "+expected+" actual: "+box);
//	}
}

public void test_addLorg_eclipse_swt_graphics_Rectangle() {
	Region reg = new Region(display);
	// add a rectangle
	reg.add(new Rectangle(0, 0, 100, 50));
	// add a second rectangle
	reg.add(new Rectangle(200, 200, 10,10));	

	try {
		reg.add((Rectangle)null);
		fail("no exception thrown for adding a null rectangle");
	}
	catch (IllegalArgumentException e) {
	}
	
	reg.dispose();
	
	try {
		reg.add(new Rectangle(20,30,10,5));
		fail("no exception thrown for adding a rectangle after Region got disposed");
	}
	catch (SWTException e) {
	}
}

public void test_addLorg_eclipse_swt_graphics_Region() {
	Region reg1 = new Region(display);
	// make a second region and add it to the first one
	Region reg2 = new Region(display);
	reg2.add(new Rectangle(40, 50, 10, 20));
	reg1.add(reg2);

	try {
		reg1.add((Region)null);
		fail("no exception thrown for adding a null region");
	}
	catch (IllegalArgumentException e) {
	}

	try {
		reg2 = new Region(display);
		reg2.add(new Rectangle(1,1,100,200));
		reg2.dispose();
		reg1.add(reg2);
		fail("no exception thrown for adding to a Region a Region which got disposed");
	}
	catch (IllegalArgumentException e) {
	}
	
	reg1.dispose();
	
	try {
		reg2 = new Region(display);
		reg2.add(new Rectangle(1,1,100,200));
		reg1.add(reg2);
		fail("no exception thrown for adding a Region to a Region which got disposed");
	}
	catch (SWTException e) {
	}
}

public void test_containsII() {
	Rectangle rect1 = new Rectangle(10,10,200,100);
	Point pointInRect1 = new Point(10,10);
	Rectangle rect2 = new Rectangle(50,50,1000,1000);
	Point pointInRect2 = new Point(1049,1009);
	Point pointNotInRect12 = new Point(49,110);
	
	Region reg = new Region(display);
	reg.dispose();
	try {
		reg.contains(pointInRect1.x, pointInRect1.y);
		fail("no exception thrown on disposed region");
	}
	catch (Exception e) {
	}
	
	reg = new Region(display);
	if (reg.contains(pointInRect1.x, pointInRect1.y)) {
		reg.dispose();
		fail("Empty region should not contain point");
	}
	reg.add(rect1);
	if (!reg.contains(pointInRect1.x, pointInRect1.y)) {
		reg.dispose();
		fail("Region should contain point");
	}
	reg.add(rect2);
	if (!reg.contains(pointInRect1.x, pointInRect1.y) ||
		!reg.contains(pointInRect2.x, pointInRect2.y)) {
		reg.dispose();
		fail("Region should contain point");
	}
	if (reg.contains(pointNotInRect12.x, pointNotInRect12.y)) {
		reg.dispose();
		fail("Region should not contain point");
	}	
	reg.dispose();
}

public void test_containsLorg_eclipse_swt_graphics_Point() {
	Rectangle rect1 = new Rectangle(10,10,200,100);
	Point pointInRect1 = new Point(10,10);
	Rectangle rect2 = new Rectangle(50,50,1000,1000);
	Point pointInRect2 = new Point(1049,1009);
	Point pointNotInRect12 = new Point(49,110);
	
	Region reg = new Region(display);
	reg.dispose();
	try {
		reg.contains(pointInRect1);
		fail("no exception thrown on disposed region");
	}
	catch (Exception e) {
	}
	
	reg = new Region(display);
	if (reg.contains(pointInRect1)) {
		reg.dispose();
		fail("Empty region should not contain point");
	}
	reg.add(rect1);
	if (!reg.contains(pointInRect1)) {
		reg.dispose();
		fail("Region should contain point");
	}
	reg.add(rect2);
	if (!reg.contains(pointInRect1) ||
		!reg.contains(pointInRect2)) {
		reg.dispose();
		fail("Region should contain point");
	}
	if (reg.contains(pointNotInRect12)) {
		reg.dispose();
		fail("Region should not contain point");
	}
	reg.dispose();
}

public void test_dispose() {
	Region reg = new Region(display);
	reg.add(new Rectangle(1,1,50,100));
	if (reg.isDisposed()) {
		fail("Region should not be in the disposed state");
	}
	
	// dispose twice as this is allowed
	for (int i = 0; i < 2; i++) {
		reg.dispose();
		if (!reg.isDisposed()) {
			fail("Region should be in the disposed state");
		}
	}
}

public void test_equalsLjava_lang_Object() {
	Rectangle rect1 = new Rectangle(25, 100, 200, 780);
	Rectangle rect2 = new Rectangle(30, 105, 205, 785);
	
	Region reg1 = new Region(display);
	reg1.add(rect1);

	Region reg2 = reg1;
	
	if (!reg1.equals(reg2)) {
		reg1.dispose();
		reg2.dispose();
		fail("references to the same instance of Region should be considered equal");
	}
	
	reg2 = new Region(display);
	reg2.add(rect1);
	
// Currently, Regions are only "equal" if they have the same handle.
// This is so that identical objects are properly hashed.
// We are considering adding a new method that will compare Regions for the same area.
//	if (!reg1.equals(reg2)) {
//		reg1.dispose();
//		reg2.dispose();
//		fail("two instances of Region representing the same area should be considered equal");
//	}		
	
	reg2.dispose();
	reg2 = new Region(display);
	if (reg1.equals(reg2)) {
		reg1.dispose();
		reg2.dispose();
		fail("Non empty region considered equal to empty one");
	}
	
	reg2.add(rect2);
	if (reg1.equals(reg2)) {
		reg1.dispose();
		reg2.dispose();
		fail("two different regions considered equal");
	}
	
	reg1.dispose();
	reg2.dispose();
}

public void test_getBounds() {
	Region reg = new Region(display);
	reg.dispose();
	
	try {
		reg.getBounds();
		fail("Region disposed should throw Exception");
	}
	catch (Exception e) {
	}

	Rectangle rect1 = new Rectangle(10,10,50,30);
	Rectangle rect2 = new Rectangle(100,100,10,10);
	// the rectangle enclosing the two preceding rectangles
	Rectangle rect12Bounds = new Rectangle(10,10,100,100);
	
	reg = new Region(display);
	reg.add(rect1);
	Rectangle rect1Bis = reg.getBounds();
	if (rect1Bis.x != rect1.x || rect1Bis.y != rect1.y || 
		rect1Bis.height != rect1.height || rect1Bis.width != rect1.width) {
		reg.dispose();
		fail("getBounds doesn't return the borders of the rectangle area which was set up");
	}
	
	reg.add(rect2);
	Rectangle rect12 = reg.getBounds();
	if (rect12.x != rect12Bounds.x || rect12.y != rect12Bounds.y || 
		rect12.height != rect12Bounds.height || rect12.width != rect12Bounds.width) {
		reg.dispose();
		fail("getBounds doesn't return the borders of the resulting rectangle area which was set up");
	}
	
	reg.dispose();
}

public void test_hashCode() {
	Region reg1 = new Region(display);
	Region reg2 = new Region(display);
		
	Rectangle rect1 = new Rectangle(25, 100, 200, 780);
	Rectangle rect2 = new Rectangle(30, 105, 205, 785);
	
	reg1.add(rect1);
	reg2.add(rect2);
	
	if (reg1.hashCode() == reg2.hashCode()) {
		reg1.dispose();
		reg2.dispose();
		fail("two different regions should have different hashCode");
	}
	reg2.dispose();	

	reg2 = new Region(display);
	reg2.add(rect1);


// BUG: this should pass	
//	if (reg1.hashCode() != reg2.hashCode()) {
//		reg1.dispose();
//		reg2.dispose();
//		fail("two regions representing same area should have same hashCode");
//	}
	
	reg1.dispose();
	reg2.dispose();
}

public void test_intersectLorg_eclipse_swt_graphics_Rectangle() {
	Rectangle rect1 = new Rectangle(0,0,50,25);
	Rectangle rect2 = new Rectangle(0,0,50,25);
	Rectangle rect3 = new Rectangle(10,10,10,10);
	Rectangle rect4 = new Rectangle(50,25,10,10);
	Rectangle rect5 = new Rectangle(25,20,25,10);

	Region reg = new Region(display);
	reg.dispose();
	try {
		reg.intersect(rect1);
		fail("no exception thrown on disposed region");
	}
	catch (SWTException e) {
	}
	
	reg = new Region(display);
	reg.intersect(rect1);
	if (!reg.isEmpty()) {
		reg.dispose();
		fail("intersect failed for empty region");
	}
	reg.add(rect1);
	reg.intersect(rect2);
	Rectangle box = reg.getBounds();
	if (!box.equals(rect1)) {
		reg.dispose();
		fail("intersect failed 1");
	}
	reg.intersect(rect3);
	box = reg.getBounds();
	if (!box.equals(rect3)) {
		reg.dispose();
		fail("intersect failed 2");
	}
	reg.intersect(rect4);
	if (!reg.isEmpty()) {
		reg.dispose();
		fail("intersect failed 3");
	}
	reg.add(rect1.union(rect4));
	reg.intersect(rect5);
	box = reg.getBounds();
	if (!box.equals(rect5)) {
		reg.dispose();
		fail("intersect failed 4");
	}
	reg.dispose();

	reg = new Region(display);
	reg.add(rect1);
	reg.add(rect4);
	reg.intersect(rect5);
	box = reg.getBounds();
	if (!box.equals(new Rectangle(25,20,25,5))) {
		reg.dispose();
		fail("intersect failed 5");
	}
	reg.dispose();
}

public void test_intersectLorg_eclipse_swt_graphics_Region() {
	Region reg = new Region(display);
	Region reg1 = new Region(display);
	reg1.add(new Rectangle(0,0,50,25));

	reg.dispose();
	try {
		reg.intersect(reg1);
		reg1.dispose();
		fail("no exception thrown on disposed region");
	}
	catch (SWTException e) {
	}
	
	reg = new Region(display);
	reg.intersect(reg1);
	if (!reg.isEmpty()) {
		reg.dispose();
		reg1.dispose();
		fail("intersect failed for empty region");
	}
	
	Region reg2 = new Region(display);
	reg2.add(new Rectangle(0,0,50,25));

	reg.add(reg1);
	reg.intersect(reg2);
	Rectangle box = reg.getBounds();
	if (!box.equals(reg1.getBounds())) {
		reg.dispose();
		reg1.dispose();
		reg2.dispose();
		fail("intersect failed 1");
	}
	reg2.dispose();
	
	Region reg3 = new Region(display);
	reg3.add(new Rectangle(10,10,10,10));
	
	reg.intersect(reg3);
	box = reg.getBounds();
	if (!box.equals(reg3.getBounds())) {
		reg.dispose();
		reg1.dispose();
		reg3.dispose();
		fail("intersect failed 2");
	}
	reg3.dispose();
	
	Region reg4 = new Region(display);
	reg4.add(new Rectangle(50,25,10,10));

	reg.intersect(reg4);
	if (!reg.isEmpty()) {
		reg.dispose();
		reg1.dispose();
		reg4.dispose();
		fail("intersect failed 3");
	}
	
	Region reg5 = new Region(display);
	reg5.add(new Rectangle(25,20,25,10));
	
	reg.add(reg1.getBounds().union(reg4.getBounds()));
	reg.intersect(reg5);
	box = reg.getBounds();
	if (!box.equals(reg5.getBounds())) {
		reg.dispose();
		reg1.dispose();
		reg4.dispose();
		fail("intersect failed 4");
	}
	reg.dispose();

	reg = new Region(display);
	reg.add(reg1);
	reg.add(reg4);
	reg.intersect(reg5);
	box = reg.getBounds();
	if (!box.equals(new Rectangle(25,20,25,5))) {
		reg.dispose();
		reg1.dispose();
		reg4.dispose();
		fail("intersect failed 5");
	}
	reg.dispose();
	reg1.dispose();
	reg4.dispose();
}

public void test_intersectsIIII() {
	Rectangle rect1 = new Rectangle(10,20,50,30);
	Rectangle rectInter1 = new Rectangle(59,49,10,20);
	Rectangle rectNotInter1 = new Rectangle(0,5,10,15);
	
	Rectangle rect2 = new Rectangle(30,40,10,100);
	Rectangle rectInter2 = new Rectangle(39,139,1,5);
	Rectangle rectNotInter12 = new Rectangle(40,50,5,15);
	
	
	Region reg = new Region(display);
	reg.dispose();
	try {
		reg.intersects(rectInter1.x, rectInter1.y, rectInter1.width, rectInter1.height);
		fail("no exception thrown on disposed region");
	}
	catch (Exception e) {
	}
	
	reg = new Region(display);
	if (reg.intersects(rect1.x, rect1.y, rect1.width, rect1.height)) {
		reg.dispose();
		fail("intersects can't return true on empty region");
	}
	reg.add(rect1);
	if (!reg.intersects(rect1.x, rect1.y, rect1.width, rect1.height)) {
		reg.dispose();
		fail("intersects didn't return true");
	}
	if (!reg.intersects(rectInter1.x, rectInter1.y, rectInter1.width, rectInter1.height)) {
		reg.dispose();
		fail("intersects didn't return true ");
	}
	if (reg.intersects(rectNotInter1.x, rectNotInter1.y, rectNotInter1.width, rectNotInter1.height)) {
		reg.dispose();
		fail("intersects return true on rectangle not intersecting with region");
	}
	
	reg.add(rect2);
	if (!reg.intersects(rect2.x, rect2.y, rect2.width, rect2.height)) {
		reg.dispose();
		fail("intersects didn't return true");
	}
	if (!reg.intersects(rectInter2.x, rectInter2.y, rectInter2.width, rectInter2.height)) {
		reg.dispose();
		fail("intersects didn't return true ");
	}
	if (reg.intersects(rectNotInter12.x, rectNotInter12.y, rectNotInter12.width, rectNotInter12.height)) {
		reg.dispose();
		fail("intersects return true on rectangle not intersecting with region");
	}
	reg.dispose();
}

public void test_intersectsLorg_eclipse_swt_graphics_Rectangle() {
	Rectangle rect1 = new Rectangle(10,20,50,30);
	Rectangle rectInter1 = new Rectangle(59,49,10,20);
	Rectangle rectNotInter1 = new Rectangle(0,5,10,15);
	
	Rectangle rect2 = new Rectangle(30,40,10,100);
	Rectangle rectInter2 = new Rectangle(39,139,1,5);
	Rectangle rectNotInter12 = new Rectangle(40,50,5,15);
	
	
	Region reg = new Region(display);
	reg.dispose();
	try {
		reg.intersects(rectInter1);
		fail("no exception thrown on disposed region");
	}
	catch (Exception e) {
	}
	
	reg = new Region(display);
	if (reg.intersects(rect1)) {
		reg.dispose();
		fail("intersects can't return true on empty region");
	}
	reg.add(rect1);
	if (!reg.intersects(rect1)) {
		reg.dispose();
		fail("intersects didn't return true");
	}
	if (!reg.intersects(rectInter1)) {
		reg.dispose();
		fail("intersects didn't return true ");
	}
	if (reg.intersects(rectNotInter1)) {
		reg.dispose();
		fail("intersects return true on rectangle not intersecting with region");
	}
	
	reg.add(rect2);
	if (!reg.intersects(rect2)) {
		reg.dispose();
		fail("intersects didn't return true");
	}
	if (!reg.intersects(rectInter2)) {
		reg.dispose();
		fail("intersects didn't return true ");
	}
	if (reg.intersects(rectNotInter12)) {
		reg.dispose();
		fail("intersects return true on rectangle not intersecting with region");
	}
	reg.dispose();
}

public void test_isDisposed() {
	// test in dispose()
}

public void test_isEmpty() {
	Region reg = new Region(display);
	Rectangle emptyRect1 = new Rectangle(10,20,0,200);
	Rectangle emptyRect2 = new Rectangle(10,20,10,0);
	Rectangle rect = new Rectangle(10,20,50,100);
	
	if (!reg.isEmpty()) {
		reg.dispose();
		fail("isEmpty didn't return true on empty region");
	}
	
	reg.add(emptyRect1);
	if (!reg.isEmpty()) {
		reg.dispose();
		fail("isEmpty didn't return true on empty region");
	}

	reg.add(emptyRect2);
	if (!reg.isEmpty()) {
		reg.dispose();
		fail("isEmpty didn't return true on empty region");
	}

	reg.add(rect);
	if (reg.isEmpty()) {
		reg.dispose();
		fail("isEmpty returned true on non empty region");
	}
	
	reg.dispose();	
}

public void test_subtract$I() {
	Region reg = new Region(display);
	try {
		reg.subtract((int[])null);
		reg.dispose();
		fail("no exception thrown for subtract a null array");
	}
	catch (IllegalArgumentException e) {
	}	
	reg.dispose();
	try {
		reg.subtract(new int[]{});
		reg.dispose();
		fail("no exception thrown on disposed region");
	}
	catch (SWTException e) {
	}
	reg.dispose();
	
	reg = new Region(display);
	reg.add(new int[] {0,0, 50,0, 50,25, 0,25});
	reg.subtract(new int[] {0,0, 50,0, 50,20, 0,20});
	Rectangle box = reg.getBounds();
	reg.dispose();
	Rectangle expected = new Rectangle (0,20,50,5);
	if (!box.equals(expected)) {
		fail("subtract 1 failed - expected: "+expected+" actual: "+box);
	}
	
	
	reg = new Region(display);
	reg.add(new int[] {0,0, 50,0, 50,25, 0,25});
	reg.add(new int[] {0,25, 50,25, 50,40, 0,40});
	reg.subtract(new int[] {0,25, 50,25, 50,40, 0,40});
	box = reg.getBounds();
	reg.dispose();
	expected = new Rectangle (0,0,50,25);
	if (!box.equals(expected)) {
		fail("subtract 2 failed - expected: "+expected+" actual: "+box);
	}
}

public void test_subtractLorg_eclipse_swt_graphics_Rectangle() {
	Rectangle rect1 = new Rectangle(0,0,50,25);
	Rectangle rect2 = new Rectangle(0,0,50,25);
	Rectangle rect3 = new Rectangle(10,10,10,10);
	Rectangle rect4 = new Rectangle(50,25,10,10);
	Rectangle rect5 = new Rectangle(0,0,60,20);

	Region reg = new Region(display);
	reg.dispose();
	try {
		reg.subtract(rect1);
		fail("no exception thrown on disposed region");
	}
	catch (SWTException e) {
	}
	
	reg = new Region(display);
	reg.subtract(rect1);
	if (!reg.isEmpty()) {
		reg.dispose();
		fail("subtract failed for empty region");
	}
	reg.add(rect1);
	reg.subtract(rect2);
	if (!reg.isEmpty()) {
		reg.dispose();
		fail("subtract failed 1");
	}
	reg.add(rect1);
	reg.subtract(rect3);
	Rectangle box = reg.getBounds();
	if (!box.equals(rect1)) {
		reg.dispose();
		fail("subtract failed 2");
	}
	reg.subtract(rect4);
	box = reg.getBounds();
	if (!box.equals(rect1)) {
		reg.dispose();
		fail("subtract failed 3");
	}
	reg.add(rect1.union(rect4));
	reg.subtract(rect5);
	box = reg.getBounds();
	if (!box.equals(new Rectangle(0,20,60,15))) {
		reg.dispose();
		fail("subtract failed 4");
	}
	reg.dispose();

	reg = new Region(display);
	reg.add(rect1);
	reg.add(rect4);
	reg.subtract(rect5);
	box = reg.getBounds();
	if (!box.equals(new Rectangle(0,20,60,15))) {
		reg.dispose();
		fail("subtract failed 5");
	}
	reg.dispose();
}

public void test_subtractLorg_eclipse_swt_graphics_Region() {
	Region reg1 = new Region(display);
	reg1.add(new Rectangle(0,0,50,25));

	Region reg = new Region(display);
	reg.dispose();
	try {
		reg.subtract(reg1);
		reg1.dispose();
		fail("no exception thrown on disposed region");
	}
	catch (SWTException e) {
	}
	
	reg = new Region(display);
	reg.subtract(reg1);
	if (!reg.isEmpty()) {
		reg.dispose();
		reg1.dispose();
		fail("subtract failed for empty region");
	}
	
	Region reg2 = new Region(display);
	reg2.add(new Rectangle(0,0,50,25));
	
	reg.add(reg1);
	reg.subtract(reg2);
	if (!reg.isEmpty()) {
		reg.dispose();
		reg1.dispose();
		reg2.dispose();
		fail("subtract failed 1");
	}
	reg2.dispose();
	
	Region reg3 = new Region(display);
	reg3.add(new Rectangle(10,10,10,10));
	
	reg.add(reg1);
	reg.subtract(reg3);
	Rectangle box = reg.getBounds();
	if (!box.equals(reg1.getBounds())) {
		reg.dispose();
		reg1.dispose();
		reg3.dispose();
		fail("subtract failed 2");
	}
	reg3.dispose();
	
	Region reg4 = new Region(display);
	reg4.add(new Rectangle(50,25,10,10));
	
	reg.subtract(reg4);
	box = reg.getBounds();
	if (!box.equals(reg1.getBounds())) {
		reg.dispose();
		reg1.dispose();
		reg4.dispose();
		fail("subtract failed 3");
	}

	Region reg5 = new Region(display);
	reg5.add(new Rectangle(0,0,60,20));
	
	reg.add(reg1.getBounds().union(reg4.getBounds()));
	reg.subtract(reg5);
	box = reg.getBounds();
	if (!box.equals(new Rectangle(0,20,60,15))) {
		reg.dispose();
		reg1.dispose();
		reg4.dispose();
		reg5.dispose();
		fail("subtract failed 4");
	}
	reg.dispose();

	reg = new Region(display);
	reg.add(reg1);
	reg.add(reg4);
	reg.subtract(reg5);
	box = reg.getBounds();
	if (!box.equals(new Rectangle(0,20,60,15))) {
		reg.dispose();
		reg1.dispose();
		reg4.dispose();
		reg5.dispose();
		fail("subtract failed 5");
	}
	reg.dispose();
	reg1.dispose();
	reg4.dispose();
	reg5.dispose();
}

public void test_toString() {
	Region reg = new Region(display);
	
	String s = reg.toString();
	
	if (s == null || s.length() == 0) {
		fail("toString returns null or empty string");
	}
	
	reg.add(new Rectangle(1,1,10,20));
	s = reg.toString();
	
	if (s == null || s.length() == 0) {
		fail("toString returns null or empty string");
	}

	reg.dispose();
	s = reg.toString();
	
	if (s == null || s.length() == 0) {
		fail("toString returns null or empty string");
	}				
}

public void test_win32_newLorg_eclipse_swt_graphics_DeviceI() {
	if (SWT.getPlatform().equals("win32")) {
		warnUnimpl("Test test_win32_newLorg_eclipse_swt_graphics_DeviceI not written");
	}
}

public void test_add_intArray() {
	Region reg = new Region(display);
	int[] points = new int[] {1};
	reg.add(points);
	reg.dispose();
	
	Region reg2 = new Region(display);
	points = new int[] {1,2};
	reg2.add(points);
	reg2.dispose();
	
	Region reg3 = new Region(display);
	points = new int[] {1,2,3};
	reg3.add(points);
	reg3.dispose();
	
	Region reg4 = new Region(display);
	points = new int[] {1,2,3,4};
	reg4.add(points);
	reg4.dispose();
	
	Region reg5 = new Region(display);
	points = new int[] {1,2,3,4,5};
	reg5.add(points);
	reg5.dispose();
	
	Region reg6 = new Region(display);
	points = new int[] {1,2,3,4,5,6};
	reg6.add(points);
	reg6.dispose();
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
	methodNames.addElement("test_toString");
	methodNames.addElement("test_win32_newLorg_eclipse_swt_graphics_DeviceI");
	methodNames.addElement("test_add_intArray");
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
	else if (getName().equals("test_toString")) test_toString();
	else if (getName().equals("test_win32_newLorg_eclipse_swt_graphics_DeviceI")) test_win32_newLorg_eclipse_swt_graphics_DeviceI();
	else if (getName().equals("test_add_intArray")) test_add_intArray();
}

/* custom */
	Display display;
}
