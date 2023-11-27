/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.TreeItem
 *
 * @see org.eclipse.swt.widgets.TreeItem
 */
public class Test_org_eclipse_swt_widgets_TreeItem extends Test_org_eclipse_swt_widgets_Item {

@Override
@Before
public void setUp() {
	super.setUp();
	makeCleanEnvironment();
}

@Test
public void test_ConstructorLorg_eclipse_swt_widgets_TreeI() {
	try {
		new TreeItem((TreeItem)null, SWT.NULL);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}

	for (int i=0; i<10; i++) {
		new TreeItem(tree, SWT.NONE);
	}
	assertEquals(11, tree.getItemCount());
	new TreeItem(tree, SWT.NONE, 5);
	assertEquals(12, tree.getItemCount());
}

@Test
public void test_ConstructorLorg_eclipse_swt_widgets_TreeII() {
	try {
		new TreeItem(tree, SWT.NONE, 5);
		fail("No exception thrown for illegal index argument");
	}
	catch (IllegalArgumentException e) {
	}
}

@Test
public void test_ConstructorLorg_eclipse_swt_widgets_TreeItemI() {
	for (int i = 0; i < 10; i++) {
		new TreeItem(treeItem, SWT.NONE);
	}
	assertEquals(10, treeItem.getItemCount());
	new TreeItem(treeItem, SWT.NONE, 5);
	assertEquals(1, tree.getItemCount());
}

@Test
public void test_ConstructorLorg_eclipse_swt_widgets_TreeItemII() {
	try {
		new TreeItem(treeItem, SWT.NONE, 5);
		fail("No exception thrown for illegal index argument");
	}
	catch (IllegalArgumentException e) {}
	assertEquals(1, tree.getItemCount());
}

@Test
public void test_getBounds() {
	if (SwtTestUtil.isGTK || SwtTestUtil.isCocoa) {
		//TODO Fix GTK and Cocoa failure.
		if (SwtTestUtil.verbose) {
			System.out.println("Excluded test_getBounds(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_TreeItem)");
		}
		return;
	}
	Image image = images[0];
	Rectangle bounds;
	Rectangle bounds2;
	String string = "hello";

	// no columns
	bounds = treeItem.getBounds();
	assertTrue(":1a:", bounds.x > 0 && bounds.height > 0);

	treeItem.setText(string);
	GC gc = new GC(tree);
	Point extent = gc.stringExtent(string);
	gc.dispose();
	bounds = treeItem.getBounds();
	assertTrue(":1b:", bounds.x > 0 && bounds.height > extent.y && bounds.width > extent.x);

	//
	makeCleanEnvironment();

	Rectangle rect = image.getBounds();
	treeItem.setImage(image);
	bounds = treeItem.getBounds();
	assertTrue(":1c:", bounds.x > 0 && bounds.height >= rect.height);
	bounds2 = treeItem.getImageBounds(0);
	assertTrue(":1d:", bounds.x >= bounds2.x + bounds2.width);

	//
	makeCleanEnvironment();

	TreeItem subItem = new TreeItem(treeItem, SWT.NONE);
	bounds = subItem.getBounds();
	assertEquals(":1e:", new Rectangle(0, 0, 0, 0), bounds);
	treeItem.setExpanded(true);
	bounds = subItem.getBounds();
	bounds2 = treeItem.getBounds();
	assertTrue(":1f:", bounds.x > bounds2.x && bounds.y >= bounds2.y + bounds2.height && bounds.height > 0);
	treeItem.setExpanded(false);
	bounds = subItem.getBounds();
	assertEquals(":1g:", new Rectangle(0, 0, 0, 0), bounds);

	treeItem.setExpanded(true);
	subItem.setText(string);
	bounds = subItem.getBounds();
	bounds2 = treeItem.getBounds();
	assertTrue(":1h:", bounds.x > bounds2.x && bounds.y >= bounds2.y + bounds2.height && bounds.height > extent.y && bounds.width > extent.x);

	//
	makeCleanEnvironment();
	subItem = new TreeItem(treeItem, SWT.NONE);
	treeItem.setExpanded(true);
	subItem.setImage(image);
	bounds = subItem.getBounds();
	assertTrue(":1i:", bounds.x > 0 && bounds.height >= rect.height);
	bounds2 = subItem.getImageBounds(0);
	assertTrue(":1j:", bounds.x >= bounds2.x + bounds2.width);

	// TODO no columns and CHECK style
	// TODO with columns
	// TODO with columns and CHECK style
}
void getBoundsIA() {
	// no columns - plain style
	Image image = images[0];
	Rectangle imageBounds = image.getBounds();
	String string = "hello";
	GC gc = new GC(tree);
	Point stringExtent = gc.stringExtent(string);
	gc.dispose();

	Rectangle bounds;
	Rectangle bounds2;

	//
	makeCleanEnvironment();

	bounds = treeItem.getBounds(0);
	assertTrue(":1a:", bounds.x > 0 && bounds.height > 0);
	bounds = treeItem.getBounds(-1);
	assertEquals(":1b:", new Rectangle(0, 0, 0, 0), bounds);
	bounds = treeItem.getBounds(1);
	assertEquals(":1c:", new Rectangle(0, 0, 0, 0), bounds);
	// unexpanded item
	TreeItem subItem = new TreeItem(treeItem, SWT.NONE);
	bounds = subItem.getBounds(0);
	assertEquals(":1d:", new Rectangle(0, 0, 0, 0), bounds);
	treeItem.setExpanded(true);
	bounds = subItem.getBounds(0);
	assertTrue(":1e:", bounds.x > 0 && bounds.height > 0);
	treeItem.setExpanded(false);
	bounds = subItem.getBounds(0);
	assertEquals(":1f:", new Rectangle(0, 0, 0, 0), bounds);
	treeItem.setExpanded(true);
	subItem.setText(string);
	bounds = subItem.getBounds(0);
	bounds2 = treeItem.getBounds(0);
	assertTrue(":1g:", bounds.x > bounds2.x && bounds.y >= bounds2.y + bounds2.height && bounds.height > stringExtent.y && bounds.width > stringExtent.x);

	treeItem.setText(string);
	bounds = treeItem.getBounds(0);
	assertTrue(":1h:", bounds.x > 0 && bounds.height > stringExtent.y && bounds.width > stringExtent.x);
	bounds2 = treeItem.getBounds();
	//assertTrue(":1new:", bounds.equals(bounds2)); // TODO should bounds be equal?
	treeItem.setText("");
	bounds2 = treeItem.getBounds(0);
	assertTrue(":1i:", bounds2.x > 0 && bounds2.height > 0);
	assertTrue(":1j:", bounds2.width < bounds.width);

	//
	makeCleanEnvironment();

	treeItem.setImage(image);
	bounds = treeItem.getBounds(0);
	assertTrue(":1k:", bounds.x > 0 && bounds.height >= imageBounds.height && bounds.width >= imageBounds.width);
	treeItem.setImage((Image)null);
	bounds2 = treeItem.getBounds(0);
	assertTrue(":1l:", bounds2.x > 0 && bounds2.height > 0);
//	assertTrue(":1m:", bounds2.width > bounds.width); // once an image is added the space for it is always there

	//
	makeCleanEnvironment();

	treeItem.setText(string);
	bounds = treeItem.getBounds(0);
	treeItem.setImage(image);
	bounds2 = treeItem.getBounds(0);
	assertTrue(":1n:", bounds2.x > 0 && bounds2.height > 0);
	assertTrue(":1o:", bounds2.width > bounds.width);
	assertTrue(":1p", bounds2.width >= stringExtent.x + imageBounds.width && bounds2.height >= Math.max(stringExtent.y, imageBounds.height));
}
void getBoundsIB() {
	// no columns and CHECK style
	Image image = images[0];
	Rectangle imageBounds = image.getBounds();
	String string = "hello";
	GC gc = new GC(tree);
	Point stringExtent = gc.stringExtent(string);
	gc.dispose();

	Rectangle bounds;
	Rectangle bounds2;

	Tree tree2 = new Tree(shell, SWT.CHECK);
	TreeItem treeItem2 = new TreeItem(tree2, SWT.NONE);

	bounds = treeItem2.getBounds(0);
	assertTrue(":2a:", bounds.x > 0 && bounds.height > 0);
	bounds = treeItem2.getBounds(-1);
	assertEquals(":2b:", new Rectangle(0, 0, 0, 0), bounds);
	bounds = treeItem2.getBounds(1);
	assertEquals(":2c:", new Rectangle(0, 0, 0, 0), bounds);
	// unexpanded item
	TreeItem subItem2 = new TreeItem(treeItem2, SWT.NONE);
	bounds = subItem2.getBounds(0);
	assertEquals(":2d:", new Rectangle(0, 0, 0, 0), bounds);
	treeItem2.setExpanded(true);
	bounds = subItem2.getBounds(0);
	assertTrue(":2e:", bounds.x > 0 && bounds.height > 0);
	treeItem2.setExpanded(false);
	bounds = subItem2.getBounds(0);
	assertEquals(":2f", new Rectangle(0, 0, 0, 0), bounds);
	treeItem2.setExpanded(true);
	subItem2.setText(string);
	bounds = subItem2.getBounds(0);
	bounds2 = treeItem2.getBounds(0);
	assertTrue(":2g:", bounds.x > bounds2.x && bounds.y >= bounds2.y + bounds2.height && bounds.height > stringExtent.y && bounds.width > stringExtent.x);

	treeItem2.setText(string);
	bounds = treeItem2.getBounds(0);
	assertTrue(":2h:", bounds.x > 0 && bounds.height > stringExtent.y && bounds.width > stringExtent.x);
	bounds2 = treeItem2.getBounds();
	//assertTrue(":2new:", bounds.equals(bounds2)); // TODO should bounds be equal?
	treeItem2.setText("");
	bounds2 = treeItem2.getBounds(0);
	assertTrue(":2i:", bounds2.x > 0 && bounds2.height > 0);
	assertTrue(":2j:", bounds2.width < bounds.width);

	tree2.dispose();
	tree2 = new Tree(shell, SWT.CHECK);
	treeItem2 = new TreeItem(tree2, SWT.NONE);

	treeItem2.setImage(image);
	bounds = treeItem2.getBounds(0);
	assertTrue(":2k:", bounds.x > 0 && bounds.height >= imageBounds.height && bounds.width >= imageBounds.width);
	treeItem2.setImage((Image)null);
	bounds2 = treeItem2.getBounds(0);
	assertTrue(":2l:", bounds2.x > 0 && bounds2.height > 0);
	//assertTrue(":2m:", bounds2.width < bounds.width);  // once an image is added the space for it is always there

	tree2.dispose();
	tree2 = new Tree(shell, SWT.CHECK);
	treeItem2 = new TreeItem(tree2, SWT.NONE);

	treeItem2.setText(string);
	bounds = treeItem2.getBounds(0);
	treeItem2.setImage(image);
	bounds2 = treeItem2.getBounds(0);
	assertTrue(":2n:", bounds2.x > 0 && bounds2.height > 0);
	assertTrue(":2o:", bounds2.width > bounds.width);
	assertTrue(":1p", bounds2.width >= stringExtent.x + imageBounds.width && bounds2.height >= Math.max(stringExtent.y, imageBounds.height));
}
void getBoundsIC() {
	// with columns

	Image image = images[0];
	Rectangle imageBounds = image.getBounds();
	String string1 = "hello";
	String string2 = "world";
	GC gc = new GC(tree);
	Point stringExtent1 = gc.stringExtent(string1);
	//Point stringExtent2 = gc.stringExtent(string2);
	gc.dispose();

	Rectangle bounds;
	Rectangle bounds2;

	//
	makeCleanEnvironment();

	TreeColumn column0 = new TreeColumn(tree, SWT.LEFT);
	TreeColumn column1 = new TreeColumn(tree, SWT.CENTER);

	bounds = treeItem.getBounds(0);
	assertTrue(":3a:", bounds.x > 0 && bounds.height > 0 && bounds.width == 0);
	bounds = treeItem.getBounds(1);
	assertTrue(":3b:", /*bounds.x > 0 &&*/ bounds.height > 0 && bounds.width == 0); // TODO bounds.x == 0 Is this right?
	bounds = treeItem.getBounds(-1);
	assertEquals(":3c:", new Rectangle(0, 0, 0, 0), bounds);
	bounds = treeItem.getBounds(2);
	assertEquals(":3d:", new Rectangle(0, 0, 0, 0), bounds);
	// unexpanded item
	TreeItem subItem = new TreeItem(treeItem, SWT.NONE);
	bounds = subItem.getBounds(0);
	assertEquals(":3e:", new Rectangle(0, 0, 0, 0), bounds);
	treeItem.setExpanded(true);
	bounds = subItem.getBounds(0);
	assertTrue(":3f:", bounds.x > 0 && bounds.height > 0);
	treeItem.setExpanded(false);
	bounds = subItem.getBounds(0);
	assertEquals(":3g:", new Rectangle(0, 0, 0, 0), bounds);
	treeItem.setExpanded(true);
	subItem.setText(new String[] {string1, string2});
	bounds = subItem.getBounds(0);
	bounds2 = treeItem.getBounds(0);
	assertTrue(":3h:", bounds.x > bounds2.x && bounds.y >= bounds2.y + bounds2.height && bounds.height > stringExtent1.y && bounds.width == 0);

	column0.setWidth(100);
	bounds = treeItem.getBounds(0);
	assertTrue(":3i:", bounds.x > 0 && bounds.height > 0 && bounds.width > 0 && bounds.width < 100);
	bounds = treeItem.getBounds(1);
	assertTrue(":3j:", bounds.x >= 100 && bounds.height > 0 && bounds.width == 0);
	bounds = subItem.getBounds(0);
	bounds2 = treeItem.getBounds(0);
	assertTrue(":3k:", bounds.x > bounds2.x && bounds.y >= bounds2.y + bounds2.height && bounds.height > stringExtent1.y && bounds.width > 0 && bounds.width < 100);

	column1.setWidth(200);
	bounds = treeItem.getBounds(0);
	assertTrue(":3l:", bounds.x > 0 && bounds.height > 0 && bounds.width > 0 && bounds.width < 100);
	bounds = treeItem.getBounds(1);
	assertTrue(":3m:", bounds.x >= 100 && bounds.height > 0 && bounds.width == 200);

	treeItem.setText(new String[] {string1, string2});
	bounds = treeItem.getBounds(0);
	assertTrue(":3n:", bounds.x > 0 && bounds.height > stringExtent1.y && bounds.width > 0 && bounds.width < 100);
	bounds = treeItem.getBounds(1);
	assertTrue(":3o:", bounds.x >= 100 && bounds.height > stringExtent1.y && bounds.width  == 200);
	treeItem.setText(new String[] {"", ""});
	bounds = treeItem.getBounds(0);
	assertTrue(":3p:", bounds.x > 0 && bounds.height > stringExtent1.y && bounds.width > 0 && bounds.width < 100);
	bounds = treeItem.getBounds(1);
	assertTrue(":3q:", bounds.x >= 100 && bounds.height > stringExtent1.y && bounds.width  == 200);

	//
	makeCleanEnvironment();
	column0 = new TreeColumn(tree, SWT.LEFT);
	column1 = new TreeColumn(tree, SWT.CENTER);
	column0.setWidth(100);
	column1.setWidth(200);

	treeItem.setImage(new Image[] {image, image});
	bounds = treeItem.getBounds(0);
	assertTrue(":3r:", bounds.x > 0 && bounds.height >= imageBounds.height && bounds.width > 0 && bounds.width < 100);
	bounds = treeItem.getBounds(1);
	assertTrue(":3s:", bounds.x >= 100 && bounds.height >= imageBounds.height && bounds.width  == 200);
	treeItem.setImage(new Image[] {null, null});
	bounds = treeItem.getBounds(0);
	assertTrue(":3t:", bounds.x > 0 && bounds.height > 0 && bounds.width > 0 && bounds.width < 100);
	bounds = treeItem.getBounds(1);
	assertTrue(":3u:", bounds.x >= 100 && bounds.height > 0 && bounds.width  == 200);

	//
	makeCleanEnvironment();
	column0 = new TreeColumn(tree, SWT.LEFT);
	column1 = new TreeColumn(tree, SWT.CENTER);
	column0.setWidth(100);
	column1.setWidth(200);

	treeItem.setText(new String[] {string1, string2});
	treeItem.setImage(new Image[] {image, image});
	bounds = treeItem.getBounds(0);
	assertTrue(":3v:", bounds.x > 0 && bounds.height > stringExtent1.y && bounds.height >= imageBounds.height && bounds.width > 0 && bounds.width < 100);
	bounds = treeItem.getBounds(1);
	assertTrue(":3w:", bounds.x > 0 && bounds.height > stringExtent1.y && bounds.height >= imageBounds.height && bounds.width  == 200);

	//
	makeCleanEnvironment();

	treeItem.setText(string1);
	new TreeColumn(tree, SWT.RIGHT);
	bounds = treeItem.getBounds(0);
	assertTrue(":3x:", bounds.x > 0 && bounds.height > stringExtent1.y && bounds.width  == 0);
}

void getBoundsID() {
	// with columns and CHECK style
	Image image = images[0];
	Rectangle imageBounds = image.getBounds();
	String string1 = "hello";
	String string2 = "world";
	GC gc = new GC(tree);
	Point stringExtent1 = gc.stringExtent(string1);
	//Point stringExtent2 = gc.stringExtent(string2);
	gc.dispose();

	Rectangle bounds;
	Rectangle bounds2;

	Tree tree2 = new Tree(shell, SWT.CHECK);
	TreeItem treeItem2 = new TreeItem(tree2, SWT.NONE);
	TreeColumn column0 = new TreeColumn(tree2, SWT.LEFT);
	TreeColumn column1 = new TreeColumn(tree2, SWT.CENTER);

	bounds = treeItem2.getBounds(0);
	assertTrue(":4a:", bounds.x > 0 && bounds.height > 0 && bounds.width == 0);
	bounds = treeItem2.getBounds(1);
	assertTrue(":4b:", /*bounds.x > 0 &&*/ bounds.height > 0 && bounds.width == 0); // TODO bounds.x == 0 Is this right?
	bounds = treeItem2.getBounds(-1);
	assertEquals(":4c:", new Rectangle(0, 0, 0, 0), bounds);
	bounds = treeItem2.getBounds(2);
	assertEquals(":4d:", new Rectangle(0, 0, 0, 0), bounds);
	// unexpanded item
	TreeItem subItem2 = new TreeItem(treeItem2, SWT.NONE);
	bounds = subItem2.getBounds(0);
	assertEquals(":4e:", new Rectangle(0, 0, 0, 0), bounds);
	treeItem2.setExpanded(true);
	bounds = subItem2.getBounds(0);
	assertTrue(":4f:", bounds.x > 0 && bounds.height > 0);
	treeItem2.setExpanded(false);
	bounds = subItem2.getBounds(0);
	assertEquals(":4g:", new Rectangle(0, 0, 0, 0), bounds);
	treeItem2.setExpanded(true);
	subItem2.setText(new String[] {string1, string2});
	bounds = subItem2.getBounds(0);
	bounds2 = treeItem2.getBounds(0);
	assertTrue(":4h:", bounds.x > bounds2.x && bounds.y >= bounds2.y + bounds2.height && bounds.height > stringExtent1.y && bounds.width == 0);

	column0.setWidth(100);
	bounds = treeItem2.getBounds(0);
	assertTrue(":4i:", bounds.x > 0 && bounds.height > 0 && bounds.width > 0 && bounds.width < 100);
	bounds = treeItem2.getBounds(1);
	assertTrue(":4j:", bounds.x >= 100 && bounds.height > 0 && bounds.width == 0);
	bounds = subItem2.getBounds(0);
	bounds2 = treeItem2.getBounds(0);
	assertTrue(":4k:", bounds.x > bounds2.x && bounds.y >= bounds2.y + bounds2.height && bounds.height > stringExtent1.y && bounds.width > 0 && bounds.width < 100);


	column1.setWidth(200);
	bounds = treeItem2.getBounds(0);
	assertTrue(":4l:", bounds.x > 0 && bounds.height > 0 && bounds.width > 0 && bounds.width < 100);
	bounds = treeItem2.getBounds(1);
	assertTrue(":4m:", bounds.x >= 100 && bounds.height > 0 && bounds.width == 200);

	treeItem2.setText(new String[] {string1, string2});
	bounds = treeItem2.getBounds(0);
	assertTrue(":4n:", bounds.x > 0 && bounds.height > stringExtent1.y && bounds.width > 0 && bounds.width < 100);
	bounds = treeItem2.getBounds(1);
	assertTrue(":4o:", bounds.x >= 100 && bounds.height > stringExtent1.y && bounds.width  == 200);
	treeItem2.setText(new String[] {"", ""});
	bounds = treeItem2.getBounds(0);
	assertTrue(":4p:", bounds.x > 0 && bounds.height > stringExtent1.y && bounds.width > 0 && bounds.width < 100);
	bounds = treeItem2.getBounds(1);
	assertTrue(":4q:", bounds.x >= 100 && bounds.height > stringExtent1.y && bounds.width  == 200);

	//
	tree2.dispose();
	tree2 = new Tree(shell, SWT.CHECK);
	treeItem2 = new TreeItem(tree2, SWT.NONE);
	column0 = new TreeColumn(tree2, SWT.LEFT);
	column1 = new TreeColumn(tree2, SWT.CENTER);
	column0.setWidth(100);
	column1.setWidth(200);

	treeItem2.setImage(new Image[] {image, image});
	bounds = treeItem2.getBounds(0);
	assertTrue(":4r:", bounds.x > 0 && bounds.height >= imageBounds.height && bounds.width > 0 && bounds.width < 100);
	bounds = treeItem2.getBounds(1);
	assertTrue(":4s:", bounds.x >= 100 && bounds.height >= imageBounds.height && bounds.width  == 200);
	treeItem2.setImage(new Image[] {null, null});
	bounds = treeItem2.getBounds(0);
	assertTrue(":4t:", bounds.x > 0 && bounds.height > 0 && bounds.width > 0 && bounds.width < 100);
	bounds = treeItem2.getBounds(1);
	assertTrue(":4u:", bounds.x >= 100 && bounds.height > 0 && bounds.width  == 200);

	//
	tree2.dispose();
	tree2 = new Tree(shell, SWT.CHECK);
	treeItem2 = new TreeItem(tree2, SWT.NONE);
	column0 = new TreeColumn(tree2, SWT.LEFT);
	column1 = new TreeColumn(tree2, SWT.CENTER);
	column0.setWidth(100);
	column1.setWidth(200);

	treeItem2.setText(new String[] {string1, string2});
	treeItem2.setImage(new Image[] {image, image});
	bounds = treeItem2.getBounds(0);
	assertTrue(":4v:", bounds.x > 0 && bounds.height > stringExtent1.y && bounds.height >= imageBounds.height && bounds.width > 0 && bounds.width < 100);
	bounds = treeItem2.getBounds(1);
	assertTrue(":4w:", bounds.x >= 100 && bounds.height > stringExtent1.y && bounds.height >= imageBounds.height && bounds.width  == 200);

	//
	tree2.dispose();
	tree2 = new Tree(shell, SWT.CHECK);
	treeItem2 = new TreeItem(tree2, SWT.NONE);

	treeItem2.setText(string1);
	new TreeColumn(tree2, SWT.RIGHT);
	bounds = treeItem2.getBounds(0);
	assertTrue(":4x:", bounds.x > 0 && bounds.height > stringExtent1.y && bounds.width  == 0);
}

@Test
public void test_getBoundsI() {
	if (SwtTestUtil.isGTK || SwtTestUtil.isCocoa) {
		//TODO Fix Gtk and Cocoa failure.
		if (SwtTestUtil.verbose) {
			System.out.println("Excluded test_getBoundsI(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_TreeItem)");
		}
		return;
	}
	getBoundsIA();
	getBoundsIB();
	getBoundsIC();
	getBoundsID();
}

@Test
public void test_getExpanded() {
	assertFalse(treeItem.getExpanded());
	// there must be at least one subitem before you can set the treeitem expanded
	new TreeItem(treeItem, 0);
	treeItem.setExpanded(true);
	assertTrue(treeItem.getExpanded());
	treeItem.setExpanded(false);
	assertFalse(treeItem.getExpanded());
}

@Test
public void test_getImageBoundsI() {
/**
 * Test without item image
 */
	Rectangle bounds;
	Tree tree2 = new Tree(shell, SWT.CHECK);
	TreeItem treeItem2 = new TreeItem(tree2, SWT.NULL);

	assertEquals(new Rectangle(0, 0, 0, 0), treeItem.getImageBounds(-1));

	// TODO - should this width be 0 or a value?
	bounds = treeItem.getImageBounds(0);
	assertEquals(":b:", 0, bounds.width);

	assertEquals(new Rectangle(0, 0, 0, 0), treeItem.getImageBounds(1));

	assertEquals(new Rectangle(0, 0, 0, 0), treeItem2.getImageBounds(-1));

	// TODO - should this width be 0 or a value?
	//bounds = treeItem2.getImageBounds(0);
	//assertTrue(":e:", bounds.width == 0);

	assertEquals(new Rectangle(0, 0, 0, 0), treeItem2.getImageBounds(1));
	//
	makeCleanEnvironment();

	Image image = images[0];
//	int imageWidth = image.getBounds().width;
//	int imageHeight;

	treeItem.setImage(0, image);
//	imageHeight = tree.getItemHeight() - tree.getGridLineWidth();
	assertEquals(new Rectangle(0, 0, 0, 0), treeItem.getImageBounds(-1));

	bounds = treeItem.getImageBounds(0);
//	assertTrue(":b:", bounds.x > 0 && bounds.width == imageWidth && bounds.height == imageHeight);
// 	assertEquals(new Rectangle(0, 0, 0, 0), treeItem.getImageBounds(1));


	//
	makeCleanEnvironment();

	tree2.dispose();
	tree2 = new Tree(shell, SWT.CHECK);
	treeItem2.dispose();
	treeItem2 = new TreeItem(tree2, SWT.NULL);
//	Rectangle imageBounds = image.getBounds();
//	imageWidth = imageBounds.width; 	treeItem2.setImage(0, image);
//	imageHeight = tree2.getItemHeight() - tree2.getGridLineWidth();
	assertEquals(new Rectangle(0, 0, 0, 0), treeItem2.getImageBounds(-1));

	bounds = treeItem2.getImageBounds(0);	// bounds.width should be check box width if they are wider than image
//	assertTrue(":b:", bounds.x > 0 && bounds.width > 0 && bounds.height == imageHeight);
// 	assertEquals(new Rectangle(0, 0, 0, 0), treeItem2.getImageBounds(1));


	//
	makeCleanEnvironment();

	tree2.dispose();
	tree2 = new Tree(shell, SWT.CHECK);
	treeItem2.dispose();
	treeItem2 = new TreeItem(tree2, SWT.NULL);
	image = images[1];
//	imageBounds = image.getBounds();
//	imageWidth = imageBounds.width;
	treeItem2.setImage(0, image);
//	imageHeight = tree2.getItemHeight() - tree2.getGridLineWidth();
	assertEquals(new Rectangle(0, 0, 0, 0), treeItem2.getImageBounds(-1));
	bounds = treeItem2.getImageBounds(0);	// bounds.width should be check box width if check box is wider than image
//	assertTrue(":b:", bounds.x > 0 && bounds.width > 0 && bounds.height == imageHeight);
	assertEquals(new Rectangle(0, 0, 0, 0), treeItem2.getImageBounds(1));
}

@Test
public void test_getItemI() {
	int number = 15;
	TreeItem[] items = new TreeItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TreeItem(treeItem, 0);

	for (int i = 0; i < number; i++)
		assertEquals("i=" + i, items[i], treeItem.getItem(i));
	try {
		treeItem.getItem(number);
		fail("No exception thrown for illegal index argument");
	}
	catch (IllegalArgumentException e) {
	}

	try {
		treeItem.getItem(number+1);
		fail("No exception thrown for illegal index argument");
	}
	catch (IllegalArgumentException e) {
	}

	try {
		treeItem.getItem(-1);
		fail("No exception thrown for illegal index argument");
	}
	catch (IllegalArgumentException e) {
	}
}

@Test
public void test_getItemCount() {
	for (int i = 0; i < 10; i++) {
		assertEquals(i, treeItem.getItemCount());
		new TreeItem(treeItem, 0);
	}
	assertEquals("b: ", 10, treeItem.getItemCount());
}

@Test
public void test_getItems() {
	int[] cases = {0, 10, 100};
	TreeItem [][] items = new TreeItem [cases.length][];
	for (int j = 0; j < cases.length; j++) {
		items [j] = new TreeItem [cases [j]];
	}
	for (int j = 0; j < cases.length; j++) {
		for (int i = 0; i < cases[j]; i++) {
			TreeItem ti = new TreeItem(treeItem, 0);
			items [j][i] = ti;
		}
		assertArrayEquals(items[j], treeItem.getItems());
		for (int i = 0; i < cases[j]; i++) {
			items [j][i].dispose();
		}
		assertEquals(0, treeItem.getItemCount());
	}
}

@Test
public void test_getParent() {
	assertEquals(tree, treeItem.getParent());
}

@Test
public void test_getParentItem() {
	TreeItem tItem = new TreeItem(treeItem, SWT.NULL);
	assertEquals(treeItem, tItem.getParentItem());
}

@Test
public void test_setBackgroundILorg_eclipse_swt_graphics_Color() {
	Display display = treeItem.getDisplay();
	Color red = display.getSystemColor(SWT.COLOR_RED);
	Color blue = display.getSystemColor(SWT.COLOR_BLUE);

	// no columns
	assertEquals(tree.getBackground(), treeItem.getBackground(0));
	assertEquals(treeItem.getBackground(), treeItem.getBackground(0));
	treeItem.setBackground(0, red);
	assertEquals(red, treeItem.getBackground(0));

	// index beyond range - no error
	treeItem.setBackground(10, red);
	assertEquals(treeItem.getBackground(), treeItem.getBackground(10));

	// with columns
	new TreeColumn(tree, SWT.LEFT);
	new TreeColumn(tree, SWT.LEFT);

	// index beyond range - no error
	treeItem.setBackground(10, red);
	assertEquals(treeItem.getBackground(), treeItem.getBackground(10));

	treeItem.setBackground(0, red);
	assertEquals(red, treeItem.getBackground(0));
	treeItem.setBackground(0, null);
	assertEquals(tree.getBackground(),treeItem.getBackground(0));

	treeItem.setBackground(0, blue);
	treeItem.setBackground(red);
	assertEquals(blue, treeItem.getBackground(0));

	treeItem.setBackground(0, null);
	assertEquals(red, treeItem.getBackground(0));

	treeItem.setBackground(null);
	assertEquals(tree.getBackground(),treeItem.getBackground(0));

	try {
		Color color = new Color(255, 0, 0);
		color.dispose();
		treeItem.setBackground(color);
		fail("No exception thrown for color disposed");
	} catch (IllegalArgumentException e) {
	}
}

@Test
public void test_setBackgroundLorg_eclipse_swt_graphics_Color() {
	Color color = new Color(255, 0, 0);
	treeItem.setBackground(color);
	assertEquals(color, treeItem.getBackground());
	treeItem.setBackground(null);
	assertEquals(tree.getBackground(),treeItem.getBackground());
	color.dispose();
	try {
		treeItem.setBackground(color);
		fail("No exception thrown for color disposed");
	} catch (IllegalArgumentException e) {
	}
}

@Test
public void test_setCheckedZ() {
	assertFalse(treeItem.getChecked());

	treeItem.setChecked(true);
	assertFalse(treeItem.getChecked());

	Tree t = new Tree(shell, SWT.CHECK);
	TreeItem ti = new TreeItem(t, SWT.NULL);
	ti.setChecked(true);
	assertTrue(ti.getChecked());

	ti.setChecked(false);
	assertFalse(ti.getChecked());
	t.dispose();
}

@Test
public void test_setExpandedZ() {
	assertFalse(treeItem.getExpanded());

	// there must be at least one subitem before you can set the treeitem expanded
	treeItem.setExpanded(true);
	assertFalse(treeItem.getExpanded());


	new TreeItem(treeItem, SWT.NULL);
	treeItem.setExpanded(true);
	assertTrue(treeItem.getExpanded());
	treeItem.setExpanded(false);
	assertFalse(treeItem.getExpanded());

	TreeItem ti = new TreeItem(treeItem, SWT.NULL);
	ti.setExpanded(true);
	treeItem.setExpanded(false);
	assertFalse(ti.getExpanded());
}

@Test
public void test_setFontLorg_eclipse_swt_graphics_Font() {
	Font font = treeItem.getFont();
	treeItem.setFont(font);
	assertEquals(treeItem.getFont(), font);

	font = new Font(treeItem.getDisplay(), SwtTestUtil.testFontName, 10, SWT.NORMAL);
	treeItem.setFont(font);
	assertEquals(treeItem.getFont(), font);

	treeItem.setFont(null);
	assertEquals(treeItem.getFont(), tree.getFont());

	font.dispose();
	try {
		treeItem.setFont(font);
		treeItem.setFont(null);
		fail("No exception thrown for disposed font");
	} catch (IllegalArgumentException e) {
	}
}

@Test
public void test_setFontILorg_eclipse_swt_graphics_Font() {
	Display display = treeItem.getDisplay();
	Font font = new Font(display, SwtTestUtil.testFontName, 10, SWT.NORMAL);

	// no columns
	assertEquals(treeItem.getFont(0), tree.getFont());
	assertEquals(treeItem.getFont(0), treeItem.getFont());
	treeItem.setFont(0, font);
	assertEquals(treeItem.getFont(0), font);

	// index beyond range - no error
	treeItem.setFont(10, font);
	assertEquals(treeItem.getFont(10), treeItem.getFont());

	// with columns
	new TreeColumn(tree, SWT.LEFT);
	new TreeColumn(tree, SWT.LEFT);

	// index beyond range - no error
	treeItem.setFont(10, font);
	assertEquals(treeItem.getFont(10), treeItem.getFont());

	treeItem.setFont(0, font);
	assertEquals(treeItem.getFont(0), font);
	treeItem.setFont(0, null);
	assertEquals(treeItem.getFont(0), tree.getFont());

	Font font2 = new Font(display, SwtTestUtil.testFontName, 20, SWT.NORMAL);

	treeItem.setFont(0, font);
	treeItem.setFont(font2);
	assertEquals(treeItem.getFont(0), font);

	treeItem.setFont(0, null);
	assertEquals(treeItem.getFont(0), font2);

	treeItem.setFont(null);
	assertEquals(treeItem.getFont(0), tree.getFont());

	font.dispose();
	font2.dispose();

	try {
		treeItem.setFont(0, font);
		treeItem.setFont(0, null);
		fail("No exception thrown for disposed font");
	} catch (IllegalArgumentException e) {
	}
}

@Test
public void test_setForegroundILorg_eclipse_swt_graphics_Color() {
	Display display = treeItem.getDisplay();
	Color red = display.getSystemColor(SWT.COLOR_RED);
	Color blue = display.getSystemColor(SWT.COLOR_BLUE);

	// no columns
	assertEquals(tree.getForeground(), treeItem.getForeground(0));
	assertEquals(treeItem.getForeground(), treeItem.getForeground(0));
	treeItem.setForeground(0, red);
	assertEquals(red, treeItem.getForeground(0));

	// index beyond range - no error
	treeItem.setForeground(10, red);
	assertEquals(treeItem.getForeground(), treeItem.getForeground(10));

	// with columns
	new TreeColumn(tree, SWT.LEFT);
	new TreeColumn(tree, SWT.LEFT);

	// index beyond range - no error
	treeItem.setForeground(10, red);
	assertEquals(treeItem.getForeground(), treeItem.getForeground(10));

	treeItem.setForeground(0, red);
	assertEquals(red, treeItem.getForeground(0));
	treeItem.setForeground(0, null);
	assertEquals(tree.getForeground(),treeItem.getForeground(0));

	treeItem.setForeground(0, blue);
	treeItem.setForeground(red);
	assertEquals(blue, treeItem.getForeground(0));

	treeItem.setForeground(0, null);
	assertEquals(red, treeItem.getForeground(0));

	treeItem.setForeground(null);
	assertEquals(tree.getForeground(),treeItem.getForeground(0));

	try {
		Color color = new Color(255, 0, 0);
		color.dispose();
		treeItem.setForeground(color);
		fail("No exception thrown for color disposed");
	} catch (IllegalArgumentException e) {
	}
}

@Test
public void test_setForegroundLorg_eclipse_swt_graphics_Color() {
	Color color = new Color(255, 0, 0);
	treeItem.setForeground(color);
	assertEquals(color, treeItem.getForeground());
	treeItem.setForeground(null);
	assertEquals(tree.getForeground(),treeItem.getForeground());
	color.dispose();
	try {
		treeItem.setForeground(color);
		fail("No exception thrown for color disposed");
	} catch (IllegalArgumentException e) {
	}
}

@Test
public void test_setGrayedZ() {
	Tree newTree = new Tree(shell, SWT.CHECK);
	TreeItem tItem = new TreeItem(newTree,0);
	assertFalse(tItem.getGrayed());
	tItem.setGrayed(true);
	assertTrue(tItem.getGrayed());
	tItem.setGrayed(false);
	assertFalse(tItem.getGrayed());
	newTree.dispose();
}

@Test
public void test_setImage$Lorg_eclipse_swt_graphics_Image() {
	assertNull(treeItem.getImage(1));
	treeItem.setImage(-1, null);
	assertNull(treeItem.getImage(-1));

	treeItem.setImage(0, images[0]);
	assertEquals(images[0], treeItem.getImage(0));
	String texts[] = new String[images.length];
	for (int i = 0; i < texts.length; i++) {
		texts[i] = String.valueOf(i);
	}

	//tree.setText(texts);				// create enough columns for TreeItem.setImage(Image[]) to work
	int columnCount = tree.getColumnCount();
	if (columnCount < texts.length) {
		for (int i = columnCount; i < texts.length; i++){
			new TreeColumn(tree, SWT.NONE);
		}
	}
	TreeColumn[] columns = tree.getColumns();
	for (int i = 0; i < texts.length; i++) {
		columns[i].setText(texts[i]);
	}
	treeItem.setImage(1, images[1]);
	assertEquals(images[1], treeItem.getImage(1));
	treeItem.setImage(images);
	for (int i = 0; i < images.length; i++) {
		assertEquals(images[i], treeItem.getImage(i));
	}
	try {
		treeItem.setImage((Image []) null);
		fail("No exception thrown for images == null");
	}
	catch (IllegalArgumentException e) {
	}
}

@Test
public void test_setImageILorg_eclipse_swt_graphics_Image() {
	// no columns
	assertEquals(null, treeItem.getImage(0));
	treeItem.setImage(0, images[0]);
	assertEquals(images[0], treeItem.getImage(0));

	// index beyond range - no error
	treeItem.setImage(10, images[0]);
	assertEquals(null, treeItem.getImage(10));

	// with columns
	new TreeColumn(tree, SWT.LEFT);
	new TreeColumn(tree, SWT.LEFT);

	// index beyond range - no error
	treeItem.setImage(10, images[0]);
	assertEquals(null, treeItem.getImage(10));

	treeItem.setImage(0, images[0]);
	assertEquals(images[0], treeItem.getImage(0));
	treeItem.setImage(0, null);
	assertEquals(null, treeItem.getImage(0));

	treeItem.setImage(0, images[0]);
	treeItem.setImage(images[1]);
	assertEquals(images[1], treeItem.getImage(0));

	treeItem.setImage(images[1]);
	treeItem.setImage(0, images[0]);
	assertEquals(images[0], treeItem.getImage(0));

	images[0].dispose();
	try {
		treeItem.setImage(0, images[0]);
		treeItem.setImage(0, null);
		fail("No exception thrown for disposed font");
	} catch (IllegalArgumentException e) {
	}
}

@Test
public void test_setText$Ljava_lang_String() {
	final String TestString = "test";
	final String TestStrings[] = new String[] {TestString, TestString + "1", TestString + "2"};

	try {
		treeItem.setText((String []) null);
		fail("No exception thrown for strings == null");
	}
	catch (IllegalArgumentException e) {
	}

	/*
	* Test the getText/setText API with a Tree that has only
	* the default column.
	*/

	assertEquals(0, treeItem.getText(1).length());

	treeItem.setText(TestStrings);
	assertEquals(TestStrings[0], treeItem.getText(0));
	for (int i = 1; i < TestStrings.length; i++) {
		assertEquals(0, treeItem.getText(i).length());
	}


	/*
	* Test the getText/setText API with a Tree that enough
	* columns to fit all test item texts.
	*/

	int columnCount = tree.getColumnCount();
	if (columnCount < images.length) {
		for (int i = columnCount; i < images.length; i++){
			new TreeColumn(tree, SWT.NONE);
		}
	}
	TreeColumn[] columns = tree.getColumns();
	for (int i = 0; i < TestStrings.length; i++) {
		columns[i].setText(TestStrings[i]);
	}
	assertEquals(0, treeItem.getText(1).length());

}

@Test
public void test_setTextILjava_lang_String(){
	final String TestString = "test";
	final String TestStrings[] = new String[] {TestString, TestString + "1", TestString + "2"};

	/*
	* Test the getText/setText API with a Tree that has only
	* the default column.
	*/

	assertEquals(0, treeItem.getText(1).length());
	treeItem.setText(1, TestString);
	assertEquals(0, treeItem.getText(1).length());
	assertEquals(0, treeItem.getText(0).length());

	treeItem.setText(0, TestString);
	assertEquals(TestString, treeItem.getText(0));
	treeItem.setText(-1, TestStrings[1]);
	assertEquals(0, treeItem.getText(-1).length());

	/*
	* Test the getText/setText API with a Tree that enough
	* columns to fit all test item texts.
	*/

	makeCleanEnvironment();

	//tree.setText(TestStrings);				// create anough columns for TreeItem.setText(String[]) to work
	int columnCount = tree.getColumnCount();
	if (columnCount < images.length) {
		for (int i = columnCount; i < images.length; i++){
			new TreeColumn(tree, SWT.NONE);
		}
	}
	TreeColumn[] columns = tree.getColumns();
	for (int i = 0; i < TestStrings.length; i++) {
		columns[i].setText(TestStrings[i]);
	}
	assertEquals(0, treeItem.getText(1).length());


	treeItem.setText(1, TestString);
	assertEquals(TestString, treeItem.getText(1));
	assertEquals(0, treeItem.getText(0).length());

	treeItem.setText(0, TestString);
	assertEquals(TestString, treeItem.getText(0));


	treeItem.setText(-1, TestStrings[1]);
	assertEquals(0, treeItem.getText(-1).length());


	try {
		treeItem.setText(-1, null);
		fail("No exception thrown for string == null");
	}
	catch (IllegalArgumentException e) {
	}

	try {
		treeItem.setText(0, null);
		fail("No exception thrown for string == null");
	}
	catch (IllegalArgumentException e) {
	}

	/*
	* Test the getText/setText API with a small table that
	* will be extended by a column:
	* First the table has only one column and one row (TableItem) with text.
	* Then it is extended by one more column.
	* After this the existing row gets one more text for the new column.
	*/

	makeCleanEnvironment();

	//create first column and set row text
	new TreeColumn(tree, SWT.NONE);
	treeItem = new TreeItem (tree, SWT.NONE);
	treeItem.setText(0, TestString);
	assertEquals(TestString, treeItem.getText(0));

	//create second column and set a second text in the same row
	new TreeColumn(tree, SWT.NONE);
	treeItem.setText(1, TestString);
	assertEquals(TestString, treeItem.getText(1));

	//create third column and set a third text in the same row
	new TreeColumn(tree, SWT.NONE);
	treeItem.setText(2, TestString);
	assertEquals(TestString, treeItem.getText(2));


}

@Test
public void test_removeAll() {
	TreeItem item = new TreeItem(treeItem, SWT.NONE);
	assertEquals(1, treeItem.getItemCount());
	treeItem.removeAll();
	assertEquals(0, treeItem.getItemCount());
	assertTrue(item.isDisposed());
}

/* custom */
TreeItem treeItem;
Tree tree;

// this method must be private or protected so the auto-gen tool keeps it
private void makeCleanEnvironment() {
	if ( treeItem != null ) treeItem.dispose();
	if ( tree != null ) tree.dispose();
	tree = new Tree(shell, 0);
	treeItem = new TreeItem(tree, 0);
	setWidget(treeItem);
}
}
