/* Copyright (c) 2000, 2015 IBM Corporation and others.
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.ToolItem
 *
 * @see org.eclipse.swt.widgets.ToolItem
 */
public class Test_org_eclipse_swt_widgets_ExpandItem extends Test_org_eclipse_swt_widgets_Item {

@Override
@Before
public void setUp() {
	super.setUp();
	expandBar = new ExpandBar(shell, 0);
	expandItem = new ExpandItem(expandBar, 0);
	setWidget(expandItem);
}

@Test
public void test_ConstructorLorg_eclipse_swt_widgets_ExpandItemI() {
	try {
		new ExpandItem(null, SWT.NULL);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}
}

@Test
public void test_ConstructorLorg_eclipse_swt_widgets_ExpandItemII() {
	ExpandItem item = new ExpandItem(expandBar, SWT.NULL, 0); //create an expand item at index 0
	assertNotNull(item);
	assertTrue(expandBar.getItem(0).equals(item));
	item = new ExpandItem(expandBar, SWT.NULL, 1);
	assertNotNull(item);
	assertTrue(expandBar.getItem(1).equals(item));
}

@Test
public void test_getControl() {
	assertNull(expandItem.getControl());

	Button button = new Button(expandBar, SWT.PUSH);
	expandItem.setControl(button);
	Control control = expandItem.getControl();
	assertEquals(button, control);

	button = new Button(expandBar, SWT.PUSH);
	expandItem.setControl(button);
	control = expandItem.getControl();
	assertEquals(button, control);
}

@Test
public void test_getParent() {
	assertEquals(expandItem.getParent(), expandBar);
}

@Test
public void test_setControlLorg_eclipse_swt_widgets_Control() {
	expandItem.setControl(null);
	Button button = new Button(expandBar, SWT.PUSH);
	expandItem.setControl(button);

	button = new Button(expandBar, SWT.PUSH);
	button.dispose();
	try {
		expandItem.setControl(button);
		fail("No exception when control.isDisposed()");
	}
	catch (IllegalArgumentException e) {
	}

	button = new Button(shell, SWT.PUSH);
	try {
		expandItem.setControl(button);
		fail("No exception thrown when control has wrong parent");
	}
	catch (IllegalArgumentException e) {
	}
}

@Test
public void test_setExpandedZ() {
	expandItem.setExpanded(true);
	assertTrue(expandItem.getExpanded());
	expandItem.setExpanded(false);
	assertEquals(expandItem.getExpanded(), false);
}

@Test
public void test_setHeightI() {
	expandItem.setHeight(30);
	assertEquals(expandItem.getHeight(), 30);
	expandItem.setHeight(-8);
	assertEquals(expandItem.getHeight(), 30);
}

@Override
@Test
public void test_setImageLorg_eclipse_swt_graphics_Image() {
	assertNull(expandItem.getImage());
	expandItem.setImage(images[0]);
	assertEquals(images[0], expandItem.getImage());
	assertTrue(expandItem.getImage() != images[1]);
	expandItem.setImage(null);
	assertNull(expandItem.getImage());
}

@Override
@Test
public void test_setTextLjava_lang_String() {
	expandItem.setText("ABCDEFG");
	assertTrue(expandItem.getText().equals("ABCDEFG"));
	try {
		expandItem.setText(null);
		fail("No exception thrown for addArmListener with null argument");
	} catch (IllegalArgumentException e) {
	}
	expandItem.setText("ABCDEFG");
	assertTrue(expandItem.getText().startsWith("ABCDEFG"));
}

/* custom */
ExpandBar expandBar;
ExpandItem expandItem;
}

