/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
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


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.CoolItem
 *
 * @see org.eclipse.swt.widgets.CoolItem
 */
public class Test_org_eclipse_swt_widgets_CoolItem extends Test_org_eclipse_swt_widgets_Item {

@Override
@Before
public void setUp() {
	super.setUp();
	CoolBar coolBar = new CoolBar(shell, 0);
	CoolItem coolItem = new CoolItem(coolBar, 0);
	setWidget(coolItem);
}

@Test
public void test_ConstructorLorg_eclipse_swt_widgets_CoolBarI() {
	CoolBar coolBar = new CoolBar(shell, 0);
	new CoolItem(coolBar, 0);

	try {
		new CoolItem(null, 0);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}
}

@Test
public void test_ConstructorLorg_eclipse_swt_widgets_CoolBarII() {
	CoolBar coolBar = new CoolBar(shell, 0);
	CoolItem coolItem = new CoolItem(coolBar, 0, 0);
	try {
		coolItem = new CoolItem(coolBar, 0, -1);
		fail("No exception thrown for index == -1");
	}
	catch (IllegalArgumentException e){
	}
	try {
		coolItem = new CoolItem(coolBar, 0, 2);
		fail("No exception thrown for index == 2");
	}
	catch (IllegalArgumentException e){
	}
	assertEquals(1, coolBar.getItemCount());
	coolItem = new CoolItem(coolBar, 0, 1);
	assertEquals(2, coolBar.getItemCount());
	coolItem = new CoolItem(coolBar, 0, 0);
	assertEquals(3, coolBar.getItemCount());
	assertEquals(coolItem, coolBar.getItem(0));
}

@Test
public void test_computeSizeII() {
	CoolBar coolBar = new CoolBar(shell, 0);
	CoolItem coolItem = new CoolItem(coolBar, 0);
	Button button = new Button(coolBar, SWT.PUSH);
	button.setText("foo");

	Point size = coolItem.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	coolItem.setControl(button);
	Point size2 = coolItem.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	assertTrue(size2.x == size.x);

	size = coolItem.computeSize(50, 25);
	size2 = coolItem.computeSize(100, 25);
	assertEquals(size.x + 50, size2.x);
	assertEquals(size.y, size2.y);

	size = coolItem.computeSize(1,1);
	size2 = coolItem.computeSize(26, 26);
	assertEquals(25, size2.x - size.x);
}

@Test
public void test_getBounds() {
	CoolBar coolBar = new CoolBar(shell, 0);
	CoolItem coolItem = new CoolItem(coolBar, 0);
	Button button = new Button(coolBar, SWT.PUSH);
	button.setText("foo");
	coolItem.setControl(button);

	Rectangle rect = coolItem.getBounds();
	Point size = coolItem.getSize();
	assertEquals(size.x, rect.width);
	assertEquals(size.y, rect.height);

	coolItem.setSize(25, 25);
	rect = coolItem.getBounds();
	coolItem.setSize(100, 25);
	Rectangle newRect = coolItem.getBounds();
	assertEquals(rect.width + 75, newRect.width);
	assertEquals(rect.x, newRect.x);
	assertEquals(rect.y, newRect.y);
}

@Test
public void test_getControl() {
	CoolBar coolBar = new CoolBar(shell, 0);
	CoolItem coolItem = new CoolItem(coolBar, 0);
	assertNull(coolItem.getControl());

	Button button = new Button(coolBar, SWT.PUSH);
	coolItem.setControl(button);
	Control control = coolItem.getControl();
	assertEquals(button, control);

	button = new Button(coolBar, SWT.PUSH);
	coolItem.setControl(button);
	control = coolItem.getControl();
	assertEquals(button, control);
}

@Test
public void test_getParent() {
	CoolBar coolBar = new CoolBar(shell, 0);
	CoolItem coolItem = new CoolItem(coolBar, 0);
	assertEquals(coolBar, coolItem.getParent());
}

@Test
public void test_getPreferredSize() {
	CoolBar coolBar = new CoolBar(shell, 0);
	CoolItem coolItem = new CoolItem(coolBar, 0);
	Button button = new Button(coolBar, SWT.PUSH);
	button.setText("foobar");
	coolItem.setControl(button);

	Point pref = coolItem.getPreferredSize();
	coolItem.setPreferredSize(pref);
	assertEquals(pref, coolItem.getPreferredSize());
}

@Test
public void test_getSize() {
	CoolBar coolBar = new CoolBar(shell, 0);
	CoolItem coolItem = new CoolItem(coolBar, 0);
	Button button = new Button(coolBar, SWT.PUSH);
	button.setText("foo");
	coolItem.setControl(button);

	Point size = coolItem.getSize();
	Rectangle rect = coolItem.getBounds();
	assertEquals(rect.width, size.x);
	assertEquals(rect.height, size.y);
}

@Test
public void test_setControlLorg_eclipse_swt_widgets_Control() {
	CoolBar coolBar = new CoolBar(shell, 0);
	CoolItem coolItem = new CoolItem(coolBar, 0);
	coolItem.setControl(null);

	Button button = new Button(coolBar, SWT.PUSH);
	button.setText("foobar");

	Point size = coolItem.getSize();
	coolItem.setControl(button);
	Point size2 = coolItem.getSize();
	assertTrue(size2.x > size.x);

	if (!MINIMAL_CONFORMANCE) {
		size = button.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		size2 = coolItem.computeSize(size.x, size.y);
		assertEquals(size2, coolItem.getSize());
	}

	button = new Button(coolBar, SWT.PUSH);
	button.dispose();
	try {
		coolItem.setControl(button);
		fail("No exception when control.isDisposed()");
	}
	catch (IllegalArgumentException e) {
	}

	button = new Button(shell, SWT.PUSH);
	try {
		coolItem.setControl(button);
		fail("No exception thrown when control has wrong parent");
	}
	catch (IllegalArgumentException e) {
	}
}

@Test
public void test_setPreferredSizeII() {
	CoolBar coolBar = new CoolBar(shell, 0);
	CoolItem coolItem = new CoolItem(coolBar, 0);
	Button button = new Button(coolBar, SWT.PUSH);
	button.setText("foobar");
	coolItem.setControl(button);

	Point size = coolItem.getSize();
	coolItem.setPreferredSize(size);
	assertEquals(size.x, coolItem.getSize().x);
	coolItem.setSize(coolItem.getPreferredSize());
	assertEquals(size, coolItem.getSize());
}

@Test
public void test_setPreferredSizeLorg_eclipse_swt_graphics_Point() {
	CoolBar coolBar = new CoolBar(shell, 0);
	CoolItem coolItem = new CoolItem(coolBar, 0);
	Button button = new Button(coolBar, SWT.PUSH);
	button.setText("foobar");
	coolItem.setControl(button);

	Point size = new Point(50, 30);
	coolItem.setPreferredSize(size);
	Point size2 = coolItem.getPreferredSize();
	coolItem.setPreferredSize(50, 30);
	assertEquals(size2, coolItem.getPreferredSize());
}

@Test
public void test_setSizeII() {
	CoolBar coolBar = new CoolBar(shell, 0);
	CoolItem coolItem = new CoolItem(coolBar, 0);
	Button button = new Button(coolBar, SWT.PUSH);
	button.setText("foo");
	coolItem.setControl(button);

	coolItem.setSize(50, 50);
	assertEquals(new Point(50, 50), coolItem.getSize());

	coolItem.setSize(0, 0);
	Point smallest = coolItem.getSize();
	coolItem.setSize(1, 1);
	assertEquals(smallest, coolItem.getSize());

	Rectangle rect = coolItem.getBounds();
	Point size = coolItem.getSize();
	coolItem.setSize(rect.width, rect.height);
	assertEquals(size, coolItem.getSize());
}

@Test
public void test_setSizeLorg_eclipse_swt_graphics_Point() {
	CoolBar coolBar = new CoolBar(shell, 0);
	CoolItem coolItem = new CoolItem(coolBar, 0);
	Button button = new Button(coolBar, SWT.PUSH);
	button.setText("foo");
	coolItem.setControl(button);

	Point size = new Point(50, 50);
	coolItem.setSize(size);
	Point size2 = coolItem.getSize();
	coolItem.setSize(50, 50);
	assertEquals(size2, coolItem.getSize());
}

/* custom */
static final boolean MINIMAL_CONFORMANCE = false;

}
