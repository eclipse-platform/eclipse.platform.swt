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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Button
 *
 * @see org.eclipse.swt.widgets.Button
 */
public class Test_org_eclipse_swt_widgets_Button extends Test_org_eclipse_swt_widgets_Control {

Button button;

@Override
@Before
public void setUp() {
	super.setUp();
	button = new Button(shell, SWT.PUSH);
	setWidget(button);
}

@Override
@Test
public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
	// Test Button(Composite parent, int style)
	new Button(shell, SWT.NULL);

	new Button(shell, SWT.PUSH);

	new Button(shell, SWT.CHECK);

	new Button(shell, SWT.TOGGLE);

	new Button(shell, SWT.ARROW);

	new Button(shell, SWT.PUSH | SWT.CHECK);

	try {
		new Button(null, 0);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}
}

@Test
public void test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	listenerCalled = false;
	SelectionListener listener = new SelectionListener() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			listenerCalled = true;
		}
		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
		}
	};

	try {
		button.addSelectionListener(null);
		fail("No exception thrown for addSelectionListener with null argument");
	} catch (IllegalArgumentException e) {
	}

	button.addSelectionListener(listener);
	button.notifyListeners(SWT.Selection, new Event());
	assertTrue(listenerCalled);

	try {
		button.removeSelectionListener(null);
		fail("No exception thrown for removeSelectionListener with null argument");
	} catch (IllegalArgumentException e) {
	}
	listenerCalled = false;
	button.removeSelectionListener(listener);
	button.notifyListeners(SWT.Selection, new Event());
	assertFalse(listenerCalled);
}

@Test
public void test_addSelectionListenerWidgetSelectedAdapterLorg_eclipse_swt_events_SelectionListener() {
	listenerCalled = false;
	SelectionListener listener = SelectionListener.widgetSelectedAdapter(e-> listenerCalled = true);

	button.addSelectionListener(listener);
	button.notifyListeners(SWT.Selection, new Event());
	assertTrue(listenerCalled);

	listenerCalled = false;
	button.removeSelectionListener(listener);
	button.notifyListeners(SWT.Selection, new Event());
	assertFalse(listenerCalled);
}

@Override
@Test
public void test_computeSizeIIZ() {
	button.computeSize(0, 0);

	button.computeSize(0, 0, false);

	button.computeSize(-10, -10);

	button.computeSize(-10, -10, false);

	button.computeSize(10, 10);

	button.computeSize(10, 10, false);

	button.computeSize(10000, 10000);

	button.computeSize(10000, 10000, false);
}

@Test
public void test_setAlignmentI() {
	button.setAlignment(SWT.LEFT);
	assertEquals(SWT.LEFT, button.getAlignment());

	button.setAlignment(SWT.RIGHT);
	assertEquals(SWT.RIGHT, button.getAlignment());

	button.setAlignment(SWT.CENTER);
	assertEquals(SWT.CENTER, button.getAlignment());

	button.setAlignment(SWT.UP); // bad value for push button
	assertTrue(SWT.UP != button.getAlignment());

	Button arrowButton = new Button(shell, SWT.ARROW);
	arrowButton.setAlignment(SWT.LEFT);
	assertEquals(SWT.LEFT, arrowButton.getAlignment());

	arrowButton.setAlignment(SWT.RIGHT);
	assertEquals(SWT.RIGHT, arrowButton.getAlignment());

	arrowButton.setAlignment(SWT.UP);
	assertEquals(SWT.UP, arrowButton.getAlignment());

	arrowButton.setAlignment(SWT.DOWN);
	assertEquals(SWT.DOWN, arrowButton.getAlignment());

	arrowButton.setAlignment(SWT.CENTER); // bad value for arrow button
	assertTrue(SWT.CENTER != arrowButton.getAlignment());
	arrowButton.dispose();

	int alignment = 55; // some bogus number
	button.setAlignment(alignment);
	assertTrue(alignment != button.getAlignment());
}

@Test
public void test_setBackgroundCheckButton() {
	Button checkButton = new Button(shell, SWT.CHECK);
	Color color = new Color(255, 0, 0);
	checkButton.setBackground(color);
	assertEquals("getBackground not equal after setBackground for SWT.CHECK Button",
			color, checkButton.getBackground());
	checkButton.setBackground(null);
	assertTrue("getBackground unchanged after setBackground(null) for SWT.CHECK Button",
			!checkButton.getBackground().equals(color));
	color.dispose();
	color = new Color(255, 0, 0, 0);
	checkButton.setBackground(color);
	assertEquals("getBackground not equal after setBackground with 0 alpha for SWT.CHECK Button",
			color, checkButton.getBackground());
	checkButton.setBackground(null);
	assertTrue("getBackground unchanged after setBackground(null) with 0 alpha for SWT.CHECK Button",
			!checkButton.getBackground().equals(color));
	if ("gtk".equals(SWT.getPlatform ())) {
		Color fg = new Color(0, 255, 0);
		checkButton.setBackground(color);
		checkButton.setForeground(fg);
		assertEquals("Setting a foreground disrupted the background color for SWT.CHECK Button",
				color, checkButton.getBackground());
		assertEquals("Setting a foreground onto an SWT.CHECK Button with a background failed",
				fg, checkButton.getForeground());
		fg.dispose();
	}
	color.dispose();
	checkButton.dispose();
}

@Test
public void test_setBackgroundAlphaCheckButton() {
	Button checkButton = new Button(shell, SWT.CHECK);
	Color color = new Color (255, 0, 0, 0);
	checkButton.setBackground(color);
	assertEquals(color, checkButton.getBackground());
	Color fg = new Color(0, 255, 0, 0);
	checkButton.setForeground(fg);
	assertEquals(color, checkButton.getBackground());
	color.dispose();
	fg.dispose();
	checkButton.dispose();
}

@Test
public void test_setBackgroundRadioButton() {
	Button radioButton = new Button(shell, SWT.RADIO);
	Color color = new Color(255, 0, 0);
	radioButton.setBackground(color);
	assertEquals("getBackground not equal after setBackground for SWT.RADIO Button",
			color, radioButton.getBackground());
	radioButton.setBackground(null);
	assertTrue("getBackground unchanged after setBackground(null) for SWT.RADIO Button",
			!radioButton.getBackground().equals(color));
	color.dispose();
	color = new Color(255, 0, 0, 0);
	radioButton.setBackground(color);
	assertEquals("getBackground not equal after setBackground with 0 alpha for SWT.RADIO Button",
			color, radioButton.getBackground());
	radioButton.setBackground(null);
	assertTrue("getBackground unchanged after setBackground(null) with 0 alpha for SWT.RADIO Button",
			!radioButton.getBackground().equals(color));
	if ("gtk".equals(SWT.getPlatform ())) {
		Color fg = new Color(0, 255, 0);
		radioButton.setBackground(color);
		radioButton.setForeground(fg);
		assertEquals("Setting a foreground disrupted the background color for SWT.RADIO Button",
				color, radioButton.getBackground());
		assertEquals("Setting a foreground onto an SWT.RADIO Button with a background failed",
				fg, radioButton.getForeground());
		fg.dispose();
	}
	color.dispose();
	radioButton.dispose();
}

@Test
public void test_setBackgroundAlphaRadioButton() {
	Button radioButton = new Button(shell, SWT.RADIO);
	Color color = new Color (255, 0, 0, 0);
	radioButton.setBackground(color);
	assertEquals(color, radioButton.getBackground());
	Color fg = new Color(0, 255, 0, 0);
	radioButton.setForeground(fg);
	assertEquals(color, radioButton.getBackground());
	color.dispose();
	fg.dispose();
	radioButton.dispose();
}

@Override
@Test
public void test_setFocus() {
	Button btn = new Button(shell, SWT.ARROW);
	btn.setFocus();
}

@Test
public void test_setForegroundCheckButton() {
	Button checkButton = new Button(shell, SWT.CHECK);
	Color color = new Color(255, 0, 0);
	checkButton.setForeground(color);
	assertEquals(color, checkButton.getForeground());
	checkButton.setForeground(null);
	assertFalse(checkButton.getForeground().equals(color));
	if ("gtk".equals(SWT.getPlatform ())) {
		Color bg = new Color(0, 255, 0);
		checkButton.setForeground(color);
		checkButton.setBackground(bg);
		assertEquals("Setting a background disrupted the foreground color for SWT.CHECK Button",
				color, checkButton.getForeground());
		assertEquals("Setting a background onto an SWT.CHECK Button with a foreground failed",
				bg, checkButton.getBackground());
		bg.dispose();
	}
	color.dispose();
	checkButton.dispose();
}

@Test
public void test_setForegroundAlphaCheckButton() {
	Button checkButton = new Button(shell, SWT.CHECK);
	assumeTrue("Alpha support for foreground colors does not exist on GTK2 or Win32",
			SwtTestUtil.isCocoa || SwtTestUtil.isGTK);
	Color color = new Color (255, 0, 0, 0);
	checkButton.setForeground(color);
	assertEquals(color, checkButton.getForeground());
	Color bg = new Color(0, 255, 0, 0);
	checkButton.setBackground(bg);
	assertEquals(color, checkButton.getForeground());
	color.dispose();
	bg.dispose();
	checkButton.dispose();
}

@Test
public void test_setForegroundRadioButton() {
	Button radioButton = new Button(shell, SWT.RADIO);
	Color color = new Color(255, 0, 0);
	radioButton.setForeground(color);
	assertEquals(color, radioButton.getForeground());
	radioButton.setForeground(null);
	assertFalse(radioButton.getForeground().equals(color));
	if ("gtk".equals(SWT.getPlatform ())) {
		Color bg = new Color(0, 255, 0);
		radioButton.setForeground(color);
		radioButton.setBackground(bg);
		assertEquals("Setting a background disrupted the foreground color for SWT.RADIO Button",
				color, radioButton.getForeground());
		assertEquals("Setting a background onto an SWT.RADIO Button with a foreground failed",
				bg, radioButton.getBackground());
		bg.dispose();
	}
	color.dispose();
	radioButton.dispose();
}

@Test
public void test_setForegroundAlphaRadiokButton() {
	Button radioButton = new Button(shell, SWT.RADIO);
	assumeTrue("Alpha support for foreground colors does not exist on GTK2 or Win32",
			SwtTestUtil.isCocoa || SwtTestUtil.isGTK);
	Color color = new Color (255, 0, 0, 0);
	radioButton.setForeground(color);
	assertEquals(color, radioButton.getForeground());
	Color bg = new Color(0, 255, 0, 0);
	radioButton.setBackground(bg);
	assertEquals(color, radioButton.getForeground());
	color.dispose();
	bg.dispose();
	radioButton.dispose();
}

@Test
public void test_setImageLorg_eclipse_swt_graphics_Image() {
	Image image = button.getImage();
	button.setImage(image);
	assertEquals(image, button.getImage());

	button.setImage(null);
	assertNull(button.getImage());

	image = new Image(shell.getDisplay(), 10, 10);
	button.setImage(image);
	assertEquals(image, button.getImage());

	button.setImage(null);
	image.dispose();
	try {
		button.setImage(image);
		button.setImage(null);
		fail("No exception thrown for disposed image");
	} catch (IllegalArgumentException e) {
	}
}

@Test
public void test_setSelectionZ() {
	// test setSelection for check box
	button = new Button(shell, SWT.CHECK);
	button.setSelection(true);
	assertTrue(button.getSelection());
	button.setSelection(false);
	assertTrue(!button.getSelection());

	// test setSelection for arrow button
	Button newButton = new Button(shell, SWT.ARROW);
	newButton.setSelection(true);
	assertTrue(!newButton.getSelection());
	newButton.setSelection(false);
	assertTrue(!newButton.getSelection());
	newButton.dispose();

	// test setSelection for push button
	newButton = new Button(shell, SWT.PUSH);
	newButton.setSelection(true);
	assertTrue(!newButton.getSelection());
	newButton.setSelection(false);
	assertTrue(!newButton.getSelection());
	newButton.dispose();

	// test setSelection for radio button
	newButton = new Button(shell, SWT.RADIO);
	newButton.setSelection(true);
	assertTrue(newButton.getSelection());
	newButton.setSelection(false);
	assertTrue(!newButton.getSelection());
	newButton.dispose();

	// test setSelection for toggle button
	newButton = new Button(shell, SWT.TOGGLE);
	newButton.setSelection(true);
	assertTrue(newButton.getSelection());
	newButton.setSelection(false);
	assertTrue(!newButton.getSelection());
	newButton.dispose();
}

@Test
public void test_setTextLjava_lang_String() {
	String[] cases = {"", "some text", "ldkashdoehufweovcnhslvhregojebckreavbkuhxbiufvcyhbifuyewvbiureyd.,cmnesljliewjfchvbwoifivbeworixuieurvbiuvbohflksjeahfcliureafgyciabelitvyrwtlicuyrtliureybcliuyreuceyvbliureybct", "\n \n \b \t ", "\0"};
	int goodCases = 4;
	for (int i=0; i<goodCases; i++){
		button.setText(cases[i]);
		assertTrue("good case: " + String.valueOf(i), button.getText().equals(cases[i]));
	}

	try {
		button.setText(null);
		fail("No exception thrown for text == null");
	}
	catch (IllegalArgumentException e) {
	}

	button.setText("");
}

//custom

protected void setUp(int style) {
	super.setUp();
	button = new Button(shell, style);
	setWidget(button);
}

@Test
public void test_consistency_MenuDetect () {
	consistencyEvent(10, 10, 3, 0, ConsistencyUtility.MOUSE_CLICK);
	tearDown();
	setUp(SWT.CHECK);
	consistencyEvent(5, 5, 3, 0, ConsistencyUtility.MOUSE_CLICK);
	tearDown();
	setUp(SWT.RADIO);
	consistencyEvent(5, 5, 3, 0, ConsistencyUtility.MOUSE_CLICK);
	tearDown();
	setUp(SWT.TOGGLE);
	consistencyEvent(5, 5, 3, 0, ConsistencyUtility.MOUSE_CLICK);
	tearDown();
	setUp(SWT.ARROW);
	consistencyEvent(5, 5, 3, 0, ConsistencyUtility.MOUSE_CLICK);

}

@Test
public void test_consistency_MouseSelection () {
	consistencyEvent(10, 10, 1, 0, ConsistencyUtility.MOUSE_CLICK);
	tearDown();
	setUp(SWT.CHECK);
	consistencyEvent(5, 5, 1, 0, ConsistencyUtility.MOUSE_CLICK);
	tearDown();
	setUp(SWT.RADIO);
	button.setSelection(true);
	consistencyEvent(5, 5, 1, 0, ConsistencyUtility.MOUSE_CLICK);
	tearDown();
	setUp(SWT.TOGGLE);
	consistencyEvent(5, 5, 1, 0, ConsistencyUtility.MOUSE_CLICK);
	tearDown();
	setUp(SWT.ARROW);
	consistencyEvent(5, 5, 1, 0, ConsistencyUtility.MOUSE_CLICK);
}

@Test
public void test_consistency_EnterSelection () {
//    differences between push and the rest of the buttons
//	  different across platforms
//    consistencyEvent(10, 13, 0, 0, ConsistencyUtility.KEY_PRESS);
	tearDown();
	setUp(SWT.CHECK);
	consistencyEvent(10, 13, 0, 0, ConsistencyUtility.KEY_PRESS);
	tearDown();
	setUp(SWT.RADIO);
	consistencyEvent(10, 13, 0, 0, ConsistencyUtility.KEY_PRESS);
	tearDown();
	setUp(SWT.TOGGLE);
	consistencyEvent(10, 13, 0, 0, ConsistencyUtility.KEY_PRESS);
	tearDown();
	setUp(SWT.ARROW);
	consistencyEvent(10, 13, 0, 0, ConsistencyUtility.KEY_PRESS);
}

@Test
public void test_consistency_SpaceSelection () {
	consistencyEvent(' ', 32, 0, 0, ConsistencyUtility.KEY_PRESS);
	tearDown();
	setUp(SWT.CHECK);
	consistencyEvent(' ', 32, 0, 0, ConsistencyUtility.KEY_PRESS);
	tearDown();
	setUp(SWT.RADIO);
	button.setSelection(true);
	consistencyEvent(' ', 32, 0, 0, ConsistencyUtility.KEY_PRESS);
	tearDown();
	setUp(SWT.TOGGLE);
	consistencyEvent(' ', 32, 0, 0, ConsistencyUtility.KEY_PRESS);
	tearDown();
	//arrow does not produce a traverse mnemonic on xp
	setUp(SWT.ARROW);
	consistencyEvent(' ', 32, 0, 0, ConsistencyUtility.KEY_PRESS);
}

@Test
public void test_consistency_DragDetect () {
	consistencyEvent(10, 10, 20, 20, ConsistencyUtility.MOUSE_DRAG);
	tearDown();
	setUp(SWT.CHECK);
	consistencyEvent(5, 5, 15, 15, ConsistencyUtility.MOUSE_DRAG);
	tearDown();
	setUp(SWT.RADIO);
	consistencyEvent(5, 5, 15, 15, ConsistencyUtility.MOUSE_DRAG);
	tearDown();
	setUp(SWT.TOGGLE);
	consistencyEvent(5, 5, 15, 15, ConsistencyUtility.MOUSE_DRAG);
	tearDown();
	setUp(SWT.ARROW);
	consistencyEvent(5, 5, 15, 15, ConsistencyUtility.MOUSE_DRAG);
}


}
