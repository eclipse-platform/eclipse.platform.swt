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

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;

import junit.framework.*;
import junit.textui.*;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Button
 *
 * @see org.eclipse.swt.widgets.Button
 */
public class Test_org_eclipse_swt_widgets_Button extends Test_org_eclipse_swt_widgets_Control {

Button button;

public Test_org_eclipse_swt_widgets_Button(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	super.setUp();
	button = new Button(shell, SWT.PUSH);
	setWidget(button);
}

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

public void test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	listenerCalled = false;
	SelectionListener listener = new SelectionListener() {
		public void widgetSelected(SelectionEvent e) {
			listenerCalled = true;
		}
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

public void test_getAlignment() {
	// tested in test_setAlignmentI()
}

public void test_getImage() {
	// tested in test_setImageLorg_eclipse_swt_graphics_Image
}

public void test_getSelection() {
	// tested in test_setSelectionZ
}

public void test_getText() {
	// tested in  test_setTextLjava_lang_String()
}

public void test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	// tested in test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener()
}

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

public void test_setFocus() {
	Button btn = new Button(shell, SWT.ARROW);
	btn.setFocus();
}

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

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_widgets_Button((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_CompositeI");
	methodNames.addElement("test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener");
	methodNames.addElement("test_computeSizeIIZ");
	methodNames.addElement("test_getAlignment");
	methodNames.addElement("test_getImage");
	methodNames.addElement("test_getSelection");
	methodNames.addElement("test_getText");
	methodNames.addElement("test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener");
	methodNames.addElement("test_setAlignmentI");
	methodNames.addElement("test_setFocus");
	methodNames.addElement("test_setImageLorg_eclipse_swt_graphics_Image");
	methodNames.addElement("test_setSelectionZ");
	methodNames.addElement("test_setTextLjava_lang_String");
	methodNames.addElement("test_consistency_MenuDetect");
	methodNames.addElement("test_consistency_DragDetect");
	methodNames.addElement("test_consistency_EnterSelection");
	methodNames.addElement("test_consistency_SpaceSelection");
	methodNames.addElement("test_consistency_MouseSelection");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Control.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_CompositeI")) test_ConstructorLorg_eclipse_swt_widgets_CompositeI();
	else if (getName().equals("test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener")) test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener();
	else if (getName().equals("test_computeSizeIIZ")) test_computeSizeIIZ();
	else if (getName().equals("test_getAlignment")) test_getAlignment();
	else if (getName().equals("test_getImage")) test_getImage();
	else if (getName().equals("test_getSelection")) test_getSelection();
	else if (getName().equals("test_getText")) test_getText();
	else if (getName().equals("test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener")) test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener();
	else if (getName().equals("test_setAlignmentI")) test_setAlignmentI();
	else if (getName().equals("test_setFocus")) test_setFocus();
	else if (getName().equals("test_setImageLorg_eclipse_swt_graphics_Image")) test_setImageLorg_eclipse_swt_graphics_Image();
	else if (getName().equals("test_setSelectionZ")) test_setSelectionZ();
	else if (getName().equals("test_setTextLjava_lang_String")) test_setTextLjava_lang_String();
	else if (getName().equals("test_consistency_MenuDetect")) test_consistency_MenuDetect();
	else if (getName().equals("test_consistency_DragDetect")) test_consistency_DragDetect();
	else if (getName().equals("test_consistency_MouseSelection")) test_consistency_MouseSelection();
	else if (getName().equals("test_consistency_EnterSelection")) test_consistency_EnterSelection();
	else if (getName().equals("test_consistency_SpaceSelection")) test_consistency_SpaceSelection();
	else super.runTest();
}

//custom

protected void setUp(int style) {
    super.setUp();
    button = new Button(shell, style);
    setWidget(button);
}

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
