package org.eclipse.swt.tests.junit;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import junit.framework.*;
import junit.textui.*;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Label
 *
 * @see org.eclipse.swt.widgets.Label
 */
public class Test_org_eclipse_swt_widgets_Label extends Test_org_eclipse_swt_widgets_Control {

Label label;

public Test_org_eclipse_swt_widgets_Label(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	super.setUp();
	label = new Label(shell, 0);
	setWidget(label);
}

protected void tearDown() {
//	super.tearDown();

	if (label != null) {
		assertEquals(shell.isDisposed(), false);
		assertEquals(label.isDisposed(), false);
	}

	shell.dispose();
	if (label != null) {
		assertTrue(shell.isDisposed());
		assertTrue(label.isDisposed());
	}
}

/**
 * Possible exceptions:
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 */
public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI(){
	try {
		label = new Label(null, 0);
		fail("No exception thrown"); //should never get here
	}
	catch (IllegalArgumentException e) {
	}

	label = new Label(shell, 0);

	int[] cases = {SWT.LEFT, SWT.RIGHT, SWT.CENTER, SWT.SEPARATOR, SWT.HORIZONTAL, SWT.VERTICAL, SWT.SHADOW_IN, SWT.SHADOW_OUT};
	for (int i = 0; i < cases.length; i++)
		label = new Label(shell, cases[i]);
}

public void test_computeSizeIIZ() {
	warnUnimpl("Test test_computeSizeIIZ not written");
}

/**
 * Returns a value which describes the position of the
 * text or image in the receiver. The value will be one of
 * <code>LEFT</code>, <code>RIGHT</code> or <code>CENTER</code>
 * unless the receiver is a <code>SEPARATOR</code> label, in 
 * which case, <code>NONE</code> is returned.
 */
public void test_getAlignment(){
	int[] cases = {SWT.LEFT, SWT.RIGHT, SWT.CENTER};
	for (int i=0; i<cases.length; i++)
	{
 	  label = new Label(shell, cases[i]);
	  assertEquals(label.getAlignment(), cases[i]);
	} 
}

/**
 * Returns the receiver's image if it has one, or null
 * if it does not.
 */
public void test_getImage(){
	Image[] cases = {null, new Image(null, 100, 100)};
	for(int i=0; i<cases.length; i++){
	 label.setImage(cases[i]);
	 assertEquals(label.getImage(), cases[i]);
	 if (cases[i]!=null)
	  cases[i].dispose();
	}
}

/**
 * Returns the receiver's text, which will be an empty
 * string if it has never been set or if the receiver is
 * a <code>SEPARATOR</code> label.
 */
public void test_getText(){
	String[] cases = {"", "some name", "sdasdlkjshcdascecoewcwe"};
	for(int i=0; i<cases.length; i++){
	 label.setText(cases[i]);
	 assertEquals(label.getText(), cases[i]);
	}
}

/**
 * Controls how text and images will be displayed in the receiver.
 * The argument should be one of <code>LEFT</code>, <code>RIGHT</code>
 * or <code>CENTER</code>.  If the receiver is a <code>SEPARATOR</code>
 * label, the argument is ignored and the alignment is not changed.
 */
public void test_setAlignmentI(){
	int[] cases = {SWT.LEFT, SWT.RIGHT, SWT.CENTER};
	for (int i=0; i<cases.length; i++)
	{
 	  label.setAlignment(cases[i]);
	  assertEquals(label.getAlignment(), cases[i]);
	} 
}

public void test_setFocus() {
	warnUnimpl("Test test_setFocus not written");
}

public void test_setImageLorg_eclipse_swt_graphics_Image() {
	warnUnimpl("Test test_setImageLorg_eclipse_swt_graphics_Image not written");
}

/**
 * Sets the receiver's text.
 * <p>
 * This method sets the widget label.  The label may include
 * the mnemonic characters and line delimiters.
 * </p>
 * 
 * @param string the new text
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the text is null</li>
 * </ul>
 */
public void test_setTextLjava_lang_String(){
	try {
		label.setText(null);
		fail("No exception thrown for string == null");
	}
	catch (IllegalArgumentException e) {
	}
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_widgets_Label((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_CompositeI");
	methodNames.addElement("test_computeSizeIIZ");
	methodNames.addElement("test_getAlignment");
	methodNames.addElement("test_getImage");
	methodNames.addElement("test_getText");
	methodNames.addElement("test_setAlignmentI");
	methodNames.addElement("test_setFocus");
	methodNames.addElement("test_setImageLorg_eclipse_swt_graphics_Image");
	methodNames.addElement("test_setTextLjava_lang_String");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Control.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_CompositeI")) test_ConstructorLorg_eclipse_swt_widgets_CompositeI();
	else if (getName().equals("test_computeSizeIIZ")) test_computeSizeIIZ();
	else if (getName().equals("test_getAlignment")) test_getAlignment();
	else if (getName().equals("test_getImage")) test_getImage();
	else if (getName().equals("test_getText")) test_getText();
	else if (getName().equals("test_setAlignmentI")) test_setAlignmentI();
	else if (getName().equals("test_setFocus")) test_setFocus();
	else if (getName().equals("test_setImageLorg_eclipse_swt_graphics_Image")) test_setImageLorg_eclipse_swt_graphics_Image();
	else if (getName().equals("test_setTextLjava_lang_String")) test_setTextLjava_lang_String();
	else super.runTest();
}
}
