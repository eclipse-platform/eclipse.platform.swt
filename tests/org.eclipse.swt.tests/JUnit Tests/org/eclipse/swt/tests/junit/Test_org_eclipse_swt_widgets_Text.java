/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;


import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import junit.framework.*;
import junit.textui.*;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Text
 *
 * @see org.eclipse.swt.widgets.Text
 */
public class Test_org_eclipse_swt_widgets_Text extends Test_org_eclipse_swt_widgets_Scrollable {

Text text;
String delimiterString;

public Test_org_eclipse_swt_widgets_Text(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	super.setUp();
	makeCleanEnvironment(false); // use multi-line by default	
}

protected void tearDown() {
	super.tearDown();
}

/**
 * Clean up the environment for a new test.
 * 
 * @param single true if the new text widget should be single-line.
 */
private void makeCleanEnvironment(boolean single) {
// this method must be private or protected so the auto-gen tool keeps it
	if ( text != null ) text.dispose();

	if ( single == true )
		text = new Text(shell, SWT.SINGLE);	
	else
		text = new Text(shell, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
	setWidget(text);
	delimiterString = Text.DELIMITER;
}
protected void setWidget(Widget w) {
	text = (Text)w;
	super.setWidget(w);
}

public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI(){
	try {
		text = new Text(null, 0);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}

	int[] cases = {0, SWT.SINGLE, SWT.MULTI, SWT.MULTI | SWT.V_SCROLL, SWT.MULTI | SWT.H_SCROLL, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL, 
					SWT.WRAP};
	for (int i = 0; i < cases.length; i++)
		text = new Text(shell, cases[i]);
}

public void test_addModifyListenerLorg_eclipse_swt_events_ModifyListener() {
	warnUnimpl("Test test_addModifyListenerLorg_eclipse_swt_events_ModifyListener not written");
}

public void test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	warnUnimpl("Test test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener not written");
}

public void test_addVerifyListenerLorg_eclipse_swt_events_VerifyListener() {
	warnUnimpl("Test test_addVerifyListenerLorg_eclipse_swt_events_VerifyListener not written");
}

public void test_appendLjava_lang_String(){
	try {
		text.append(null);
		fail("No exception thrown for string == null");
	}
	catch (IllegalArgumentException e) {
	}

	text.setText("01");
	text.append("23");
	assertEquals("0123", text.getText());
	text.append("45");
	assertEquals("012345", text.getText());
	text.setSelection(0);
	text.append("67");
	assertEquals("01234567", text.getText());

	text.setText("01");
	text.append("23");
	assertEquals("0123", text.getText());
	text.append("4" + delimiterString+ "5");
	assertEquals("01234" + delimiterString +"5", text.getText());
	text.setSelection(0);
	text.append("67");
	assertEquals("01234" + delimiterString+"567", text.getText());

	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	try {
		text.append(null);
		fail("No exception thrown on string == null");
	}
	catch (IllegalArgumentException e) {
	}

	// tests a SINGLE line text editor
	makeCleanEnvironment(true);

	text.setText("01");
	text.append("23");
	assertEquals("0123", text.getText());
	text.append("45");
	assertEquals("012345", text.getText());
	text.setSelection(0);
	text.append("67");
	assertEquals("01234567", text.getText());

	// tests a SINGLE line text editor
	makeCleanEnvironment(true);

	text.setText("01");
	text.append("23");
	assertEquals("0123", text.getText());
	text.append("4" + delimiterString+"5");
	assertEquals("01234" + delimiterString+"5", text.getText());
	text.setSelection(0);
	text.append("67");
	assertEquals("01234" + delimiterString+"567", text.getText());
}

public void test_clearSelection(){
	text.setText("01234567890");
	assertEquals("", text.getSelectionText());
	text.selectAll();
	assertEquals("01234567890", text.getSelectionText());
	text.clearSelection();
	assertEquals("", text.getSelectionText());

	text.setText("01234567890");
	assertEquals("", text.getSelectionText());
	text.selectAll();
	assertEquals("01234567890", text.getSelectionText());
	text.clearSelection();
	assertEquals("", text.getSelectionText());
	
	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	text.setText("01234567890");
	assertEquals("", text.getSelectionText());
	text.selectAll();
	assertEquals("01234567890", text.getSelectionText());
	text.clearSelection();
	assertEquals("", text.getSelectionText());
}

public void test_computeSizeIIZ() {
	warnUnimpl("Test test_computeSizeIIZ not written");
}

public void test_copy(){
	text.copy();

	text.selectAll();
	text.copy();
	assertEquals("", text.getSelectionText());

	text.setText("00000");
	text.selectAll();
	text.copy();
	text.setSelection(2);
	assertEquals("", text.getSelectionText());

	text.setText("");
	text.paste();
	assertEquals("00000", text.getText());
	
	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	text.copy();

	text.selectAll();
	text.copy();
	assertEquals("", text.getSelectionText());

	text.setText("00000");
	text.selectAll();
	text.copy();
	text.setSelection(2);
	assertEquals("", text.getSelectionText());

	text.setText("");
	text.paste();
	assertEquals("00000", text.getText());
}

public void test_cut(){
	text.cut();
	text.setText("01234567890");
	text.setSelection(2, 5);
	text.cut();
	assertEquals("01567890", text.getText());

	text.selectAll();
	text.cut();
	assertEquals("", text.getText());
	
	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	text.cut();

	text.setText("01234567890");
	text.setSelection(2, 5);
	text.cut();
	assertEquals("01567890", text.getText());

	text.selectAll();
	text.cut();
	assertEquals("", text.getText());
}

public void test_getCaretLineNumber() {
	warnUnimpl("Test test_getCaretLineNumber not written");
}

public void test_getCaretLocation() {
	warnUnimpl("Test test_getCaretLocation not written");
}

public void test_getCaretPosition() {
	warnUnimpl("Test test_getCaretPosition not written");
}

public void test_getCharCount(){
	assertEquals(0, text.getCharCount());
	text.setText("");
	assertEquals(0, text.getCharCount());
	text.setText("01234567890");
	assertEquals(11, text.getCharCount());

	text.setText("012345" + delimiterString+"67890");
	assertEquals(11  + delimiterString.length(), text.getCharCount()); //might be bogus on UNIX

	text.setText("");
	assertEquals(0, text.getCharCount());
	
	text.setText("01234\t567890");
	assertEquals(12, text.getCharCount());

	//
	text.setText("");
	assertEquals(0, text.getCharCount());
	text.setText("01234567890");
	assertEquals(11, text.getCharCount());

	text.setText("012345" + delimiterString+"67890");
	assertEquals(11  + delimiterString.length(), text.getCharCount()); //might be bogus on UNIX

	text.setText("");
	assertEquals(0, text.getCharCount());
	
	text.setText("01234\t567890");
	assertEquals(12, text.getCharCount());
	
	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	assertEquals(0, text.getCharCount());
	text.setText("");
	assertEquals(0, text.getCharCount());
	text.setText("01234567890");
	assertEquals(11, text.getCharCount());
	if (fCheckBogusTestCases) {
		text.setText("012345"+ delimiterString + "67890");
		assertEquals(11  + delimiterString.length(), text.getCharCount()); //might be bogus on UNIX
	}
	text.setText("");
	assertEquals(0, text.getCharCount());
	if (!SwtJunit.isAIX) {	
		text.setText("01234\t567890");
		assertEquals(12, text.getCharCount());
	}
}

public void test_getDoubleClickEnabled(){
	text.setDoubleClickEnabled(true);
	assertTrue(text.getDoubleClickEnabled());
	
	text.setDoubleClickEnabled(false);
	assertEquals(false, text.getDoubleClickEnabled());
	
	// this method tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	text.setDoubleClickEnabled(true);
	assertTrue(text.getDoubleClickEnabled());
	
	text.setDoubleClickEnabled(false);
	assertEquals(false, text.getDoubleClickEnabled());
}

public void test_getEchoChar(){
	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	text.setEchoChar('a');
	assertEquals('a', text.getEchoChar());
}

public void test_getEditable() {
	warnUnimpl("Test test_getEditable not written");
}

public void test_getLineCount(){
	assertEquals(1, text.getLineCount());
	text.append("dddasd" + delimiterString);
	assertEquals(2, text.getLineCount());
	text.append("ddasdasdasdasd" + delimiterString);
	assertEquals(3, text.getLineCount());
	
	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	assertEquals(1, text.getLineCount());
	text.append("dddasd" + delimiterString);
	assertEquals(1, text.getLineCount());
	text.append("ddasdasdasdasd" + delimiterString);
	assertEquals(1, text.getLineCount());
}

public void test_getLineDelimiter() {
	warnUnimpl("Test test_getLineDelimiter not written");
}

public void test_getLineHeight() {
	warnUnimpl("Test test_getLineHeight not written");
}

public void test_getSelection() {
	warnUnimpl("Test test_getSelection not written");
}

public void test_getSelectionCount() {
	warnUnimpl("Test test_getSelectionCount not written");
}

public void test_getSelectionText(){
	text.setText("01234567890");
	assertEquals("", text.getSelectionText());
	text.setSelection(3, 7);
	assertEquals("3456", text.getSelectionText());

	text.selectAll();
	assertEquals("01234567890", text.getSelectionText());
}

public void test_getTabs() {
	warnUnimpl("Test test_getTabs not written");
}

public void test_getText(){
	assertEquals("", text.getText());
	text.setText("01234567890");
	assertEquals("01234567890", text.getText());
	text.setText("");
	assertEquals("", text.getText());
	String string = "012345" + delimiterString + "67890";
	text.setText(string);
	assertEquals(string, text.getText());
}

public void test_getTextII(){
	assertEquals("", text.getText());
	text.setText("01234567890");
	assertEquals("345", text.getText(3, 5));
	// these tests should not cause a null pointer exception, checks not implemented yet	
	if (fCheckOutOfRangeBehaviour) {
		text.setText("");
		assertEquals("", text.getText(3, 5));
		text.setText("01234567890");
		assertEquals(":d:", text.getText(3, 100));
		text.setText("01234567890");
		assertEquals("34", text.getText(5, 3));
	}
}

public void test_getTextLimit() {
	warnUnimpl("Test test_getTextLimit not written");
}

public void test_getTopIndex(){
	assertEquals(0, text.getTopIndex());
	text.setText("01234567890");
	assertEquals(0, text.getTopIndex());
	text.append(delimiterString +"01234567890");
	assertEquals(0, text.getTopIndex());
	text.setTopIndex(1);
	assertEquals(1, text.getTopIndex());
	text.setTopIndex(17);
	assertEquals(1, text.getTopIndex());
}

public void test_getTopPixel() {
	warnUnimpl("Test test_getTopPixel not written");
}

public void test_insertLjava_lang_String(){
	try {
		text.insert(null);
		fail("No exception thrown for string == null");
	}
	catch (IllegalArgumentException e) {
	}

	assertEquals("", text.getText());
	text.insert("");
	assertEquals("", text.getText());
	text.insert("fred");
	assertEquals("fred", text.getText());
	text.setSelection(2);
	text.insert("helmut");
	assertEquals("frhelmuted", text.getText());

	text.setText("01234567890");
	text.setSelection(4);
	assertEquals(1, text.getLineCount());
	text.insert(delimiterString);
	assertEquals(2, text.getLineCount());

	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	try {
		text.insert(null);
		fail("No exception thrown on string == null");
	}
	catch (IllegalArgumentException e) {
	}

	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	assertEquals("", text.getText());
	text.insert("");
	assertEquals("", text.getText());
	text.insert("fred");
	assertEquals("fred", text.getText());
	text.setSelection(2);
	text.insert("helmut");
	assertEquals("frhelmuted", text.getText());

	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	text.setText("01234567890");
	text.setSelection(4);
	assertEquals(1, text.getLineCount());
	text.insert(Text.DELIMITER);
	assertEquals(1, text.getLineCount());

	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	try {
		text.insert(null);
		fail("No exception thrown on string == null");
	}
	catch (IllegalArgumentException e) {
	}
}

public void test_paste(){
	text.setText("01234567890");
	text.setSelection(2, 4);
	assertEquals("01234567890", text.getText());
	text.copy();
	text.setSelection(0);
	text.paste();
	assertEquals("2301234567890", text.getText());
	text.copy();
	text.setSelection(3);
	text.paste();
	assertEquals("230231234567890", text.getText());

	text.setText("0" + delimiterString + "1");
	text.selectAll();
	text.copy();
	text.setSelection(0);
	text.paste();
	assertEquals("0" + delimiterString + "1" + "0" + delimiterString + "1", text.getText());
	
	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	text.setText("01234567890");
	text.setSelection(2, 4);
	assertEquals("01234567890", text.getText());
	text.copy();
	text.setSelection(0);
	text.paste();
	assertEquals("2301234567890", text.getText());
	text.copy();
	text.setSelection(3);
	text.paste();
	assertEquals("230231234567890", text.getText());

	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	text.setText("0" + delimiterString + "1");
	text.selectAll();
	text.copy();
	text.setSelection(0);
	text.paste();

	if (fCheckSWTPolicy)
		assertEquals("0" + delimiterString + "1" + "0" + delimiterString + "1", text.getText());
}

public void test_removeModifyListenerLorg_eclipse_swt_events_ModifyListener() {
	warnUnimpl("Test test_removeModifyListenerLorg_eclipse_swt_events_ModifyListener not written");
}

public void test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	warnUnimpl("Test test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener not written");
}

public void test_removeVerifyListenerLorg_eclipse_swt_events_VerifyListener() {
	warnUnimpl("Test test_removeVerifyListenerLorg_eclipse_swt_events_VerifyListener not written");
}

public void test_selectAll(){
	text.setText("01234567890");
	assertEquals("01234567890", text.getText());
	text.selectAll();
	assertEquals("01234567890", text.getSelectionText());
	text.cut();
	assertEquals("", text.getText());

	text.setText("01234" + delimiterString+"567890");
	assertEquals("01234" + delimiterString+"567890", text.getText());
	text.selectAll();
	assertEquals("01234" + delimiterString+"567890", text.getSelectionText());
	text.cut();
	assertEquals("", text.getText());
		
	// tests a SINGLE line text editor
	makeCleanEnvironment(true);

	text.setText("01234567890");
	assertEquals("01234567890", text.getText());
	text.selectAll();
	assertEquals("01234567890", text.getSelectionText());
	text.cut();
	assertEquals("", text.getText());

	// tests a SINGLE line text editor
	if (fCheckBogusTestCases) {
		text.setText("01234" + delimiterString+"567890");
		assertEquals("01234" + delimiterString+"567890", text.getText());
		text.selectAll();
		assertEquals("01234" + delimiterString+"567890", text.getSelectionText());
		text.cut();
		assertEquals("", text.getText());
	}
}

public void test_setDoubleClickEnabledZ(){
	text.setDoubleClickEnabled(true);
	assertTrue(text.getDoubleClickEnabled());
	
	text.setDoubleClickEnabled(false);
	assertEquals(false, text.getDoubleClickEnabled());
	
	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	text.setDoubleClickEnabled(true);
	assertTrue(text.getDoubleClickEnabled());
	
	text.setDoubleClickEnabled(false);
	assertEquals(false, text.getDoubleClickEnabled());
}

public void test_setEchoCharC(){
	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	for (int i=0; i<128; i++){
		text.setEchoChar((char) i);
		assertEquals((char)i, text.getEchoChar());
	}
	
	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	text.setEchoChar('a');
	assertEquals('a', text.getEchoChar());

	text.setEchoChar((char) 0);
	assertEquals((char)0, text.getEchoChar());

	text.setEchoChar('\n');
	assertEquals('\n', text.getEchoChar());

	for (int i=0; i<128; i++){
		text.setEchoChar((char) i);
		assertEquals((char)i, text.getEchoChar());
	}
}

public void test_setEditableZ() {
	warnUnimpl("Test test_setEditableZ not written");
}

public void test_setFontLorg_eclipse_swt_graphics_Font() {
	warnUnimpl("Test test_setFontLorg_eclipse_swt_graphics_Font not written");
}

public void test_setSelectionI() {
	warnUnimpl("Test test_setSelectionI not written");
}

public void test_setSelectionII(){
	text.setText("01234567890");
	assertEquals(0, text.getSelectionCount());
	text.setSelection(2, 4);
	assertEquals(2, text.getSelectionCount());
	text.setSelection(2, 100);
	assertEquals(9, text.getSelectionCount());

	text.setText("0123" + delimiterString +"4567890");
	assertEquals(0, text.getSelectionCount());
	text.setSelection(2, 4);
	assertEquals(2, text.getSelectionCount());
	text.setSelection(2, 100);
	assertEquals(9 + delimiterString.length(), text.getSelectionCount());
	
	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	text.setText("01234567890");
	assertEquals(0, text.getSelectionCount());
	text.setSelection(2, 4);
	assertEquals(2, text.getSelectionCount());
	text.setSelection(2, 100);
	assertEquals(9, text.getSelectionCount());

	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	text.setText("0123"+ delimiterString+"4567890");
	assertEquals(0, text.getSelectionCount());
	text.setSelection(2, 4);
	assertEquals(2, text.getSelectionCount());
	if (fCheckBogusTestCases) {
		text.setSelection(2, 100);
		assertEquals(9 +delimiterString.length(), text.getSelectionCount());
	}
}

public void test_setRedrawZ(){
	text.setRedraw(false);
	text.setRedraw(true);
}

public void test_setSelectionLorg_eclipse_swt_graphics_Point(){
	text.setText("dsdsdasdslaasdas");
	try {
		text.setSelection((Point) null);
		fail("No exception thrown for selection == null");
	}
	catch (IllegalArgumentException e) {
	}

	text.setText("01234567890");
	text.setSelection(new Point(2, 2));
	assertEquals(new Point(2, 2), text.getSelection());

	text.setSelection(new Point(3, 2));
	assertEquals(new Point(2, 3), text.getSelection());

	text.setSelection(new Point(3, 100));
	assertEquals(new Point(3, 11), text.getSelection());

	text.setText("01234567890");
	text.setSelection(4);
	assertEquals(new Point(4, 4), text.getSelection());

	text.setSelection(100);
	assertEquals(new Point(11, 11), text.getSelection());
	
	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	text.setText("dsdsdasdslaasdas");
	try {
		text.setSelection((Point) null);
		fail("No exception thrown for selection == null");
	}
	catch (IllegalArgumentException e) {
	}

	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	text.setText("01234567890");

	text.setSelection(new Point(2, 2));
	assertEquals(new Point(2, 2), text.getSelection());

	text.setSelection(new Point(3, 2));
	assertEquals(new Point(2, 3), text.getSelection());

	text.setSelection(new Point(3, 100));
	assertEquals(new Point(3, 11), text.getSelection());

	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	text.setText("01234567890");
	text.setSelection(4);
	assertEquals(new Point(4, 4), text.getSelection());

	text.setSelection(100);
	assertEquals(new Point(11, 11), text.getSelection());
}

public void test_setTabsI(){
	if (SwtJunit.isMotif) {
		for (int i = 0; i < 200; i++) {
			text.setTabs(i);
			assertEquals(8, text.getTabs());
		}
	} else {
		for (int i = 0; i < 200; i++) {
			text.setTabs(i);
			assertEquals(i, text.getTabs());
		}
	}
}

public void test_setTextLjava_lang_String(){
	try {
		text.setText(null);
		fail("No exception thrown for string == null");
	}
	catch (IllegalArgumentException e) {
	}

	text.setText("");
	
	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	assertEquals("", text.getText());
	text.setText("01234567890");
	assertEquals("01234567890", text.getText());
	text.setText("");
	assertEquals("", text.getText());
	if (fCheckBogusTestCases) {
		text.setText("012345" + delimiterString+ "67890");
		assertEquals("012345" + delimiterString +"67890", text.getText());
	}

	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	assertEquals("", text.getText());
	text.setText("01234567890");
	assertEquals("345", text.getText(3, 5));
	// these tests should not cause a null pointer exception, checks not implemented yet	
	if (fCheckOutOfRangeBehaviour) {
		text.setText("");
		assertEquals("", text.getText(3, 5));
		text.setText("01234567890");
		assertEquals("34567890", text.getText(3, 100));
		text.setText("01234567890");
		assertEquals("34", text.getText(5, 3));
	}
}

public void test_setTextLimitI() {
	warnUnimpl("Test test_setTextLimitI not written");
}

public void test_setTopIndexI(){
	int number = 100;
	for (int i = 0; i < number; i++) {
		text.append("01234\n");
	}
	for (int i = 1; i <= number; i++) {
		text.setTopIndex(i);
		assertEquals(i, text.getTopIndex());
	}
	
	text.setTopIndex(number+5);
	assertEquals(number, text.getTopIndex());
	
	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	assertEquals(0, text.getTopIndex());
	text.setText("01234567890");
	assertEquals(0, text.getTopIndex());
	text.append(Text.DELIMITER +"01234567890");
	assertEquals(0, text.getTopIndex());
	text.setTopIndex(1);
	assertEquals(0, text.getTopIndex());
	text.setTopIndex(17);
	assertEquals(0, text.getTopIndex());

	text.setText("");	
	for (int i = 0; i < number; i++) {
		text.append("01234" + Text.DELIMITER);
	}
	for (int i = 0; i < number; i++) {
		text.setTopIndex(i);
		assertEquals(0, text.getTopIndex());
	}
}

public void test_showSelection(){
	text.showSelection();

	text.selectAll();
	text.showSelection();

	text.setText("00000");
	text.selectAll();
	text.showSelection();
	text.clearSelection();
	text.showSelection();
	
	// this method tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	text.showSelection();

	text.selectAll();
	text.showSelection();


	text.setText("00000");
	text.selectAll();
	text.showSelection();

	text.clearSelection();
	text.showSelection();
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_widgets_Text((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_CompositeI");
	methodNames.addElement("test_addModifyListenerLorg_eclipse_swt_events_ModifyListener");
	methodNames.addElement("test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener");
	methodNames.addElement("test_addVerifyListenerLorg_eclipse_swt_events_VerifyListener");
	methodNames.addElement("test_appendLjava_lang_String");
	methodNames.addElement("test_clearSelection");
	methodNames.addElement("test_computeSizeIIZ");
	methodNames.addElement("test_copy");
	methodNames.addElement("test_cut");
	methodNames.addElement("test_getCaretLineNumber");
	methodNames.addElement("test_getCaretLocation");
	methodNames.addElement("test_getCaretPosition");
	methodNames.addElement("test_getCharCount");
	methodNames.addElement("test_getDoubleClickEnabled");
	methodNames.addElement("test_getEchoChar");
	methodNames.addElement("test_getEditable");
	methodNames.addElement("test_getLineCount");
	methodNames.addElement("test_getLineDelimiter");
	methodNames.addElement("test_getLineHeight");
	methodNames.addElement("test_getSelection");
	methodNames.addElement("test_getSelectionCount");
	methodNames.addElement("test_getSelectionText");
	methodNames.addElement("test_getTabs");
	methodNames.addElement("test_getText");
	methodNames.addElement("test_getTextII");
	methodNames.addElement("test_getTextLimit");
	methodNames.addElement("test_getTopIndex");
	methodNames.addElement("test_getTopPixel");
	methodNames.addElement("test_insertLjava_lang_String");
	methodNames.addElement("test_paste");
	methodNames.addElement("test_removeModifyListenerLorg_eclipse_swt_events_ModifyListener");
	methodNames.addElement("test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener");
	methodNames.addElement("test_removeVerifyListenerLorg_eclipse_swt_events_VerifyListener");
	methodNames.addElement("test_selectAll");
	methodNames.addElement("test_setDoubleClickEnabledZ");
	methodNames.addElement("test_setEchoCharC");
	methodNames.addElement("test_setEditableZ");
	methodNames.addElement("test_setFontLorg_eclipse_swt_graphics_Font");
	methodNames.addElement("test_setSelectionI");
	methodNames.addElement("test_setSelectionII");
	methodNames.addElement("test_setRedrawZ");
	methodNames.addElement("test_setSelectionLorg_eclipse_swt_graphics_Point");
	methodNames.addElement("test_setTabsI");
	methodNames.addElement("test_setTextLjava_lang_String");
	methodNames.addElement("test_setTextLimitI");
	methodNames.addElement("test_setTopIndexI");
	methodNames.addElement("test_showSelection");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Scrollable.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_CompositeI")) test_ConstructorLorg_eclipse_swt_widgets_CompositeI();
	else if (getName().equals("test_addModifyListenerLorg_eclipse_swt_events_ModifyListener")) test_addModifyListenerLorg_eclipse_swt_events_ModifyListener();
	else if (getName().equals("test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener")) test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener();
	else if (getName().equals("test_addVerifyListenerLorg_eclipse_swt_events_VerifyListener")) test_addVerifyListenerLorg_eclipse_swt_events_VerifyListener();
	else if (getName().equals("test_appendLjava_lang_String")) test_appendLjava_lang_String();
	else if (getName().equals("test_clearSelection")) test_clearSelection();
	else if (getName().equals("test_computeSizeIIZ")) test_computeSizeIIZ();
	else if (getName().equals("test_copy")) test_copy();
	else if (getName().equals("test_cut")) test_cut();
	else if (getName().equals("test_getCaretLineNumber")) test_getCaretLineNumber();
	else if (getName().equals("test_getCaretLocation")) test_getCaretLocation();
	else if (getName().equals("test_getCaretPosition")) test_getCaretPosition();
	else if (getName().equals("test_getCharCount")) test_getCharCount();
	else if (getName().equals("test_getDoubleClickEnabled")) test_getDoubleClickEnabled();
	else if (getName().equals("test_getEchoChar")) test_getEchoChar();
	else if (getName().equals("test_getEditable")) test_getEditable();
	else if (getName().equals("test_getLineCount")) test_getLineCount();
	else if (getName().equals("test_getLineDelimiter")) test_getLineDelimiter();
	else if (getName().equals("test_getLineHeight")) test_getLineHeight();
	else if (getName().equals("test_getSelection")) test_getSelection();
	else if (getName().equals("test_getSelectionCount")) test_getSelectionCount();
	else if (getName().equals("test_getSelectionText")) test_getSelectionText();
	else if (getName().equals("test_getTabs")) test_getTabs();
	else if (getName().equals("test_getText")) test_getText();
	else if (getName().equals("test_getTextII")) test_getTextII();
	else if (getName().equals("test_getTextLimit")) test_getTextLimit();
	else if (getName().equals("test_getTopIndex")) test_getTopIndex();
	else if (getName().equals("test_getTopPixel")) test_getTopPixel();
	else if (getName().equals("test_insertLjava_lang_String")) test_insertLjava_lang_String();
	else if (getName().equals("test_paste")) test_paste();
	else if (getName().equals("test_removeModifyListenerLorg_eclipse_swt_events_ModifyListener")) test_removeModifyListenerLorg_eclipse_swt_events_ModifyListener();
	else if (getName().equals("test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener")) test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener();
	else if (getName().equals("test_removeVerifyListenerLorg_eclipse_swt_events_VerifyListener")) test_removeVerifyListenerLorg_eclipse_swt_events_VerifyListener();
	else if (getName().equals("test_selectAll")) test_selectAll();
	else if (getName().equals("test_setDoubleClickEnabledZ")) test_setDoubleClickEnabledZ();
	else if (getName().equals("test_setEchoCharC")) test_setEchoCharC();
	else if (getName().equals("test_setEditableZ")) test_setEditableZ();
	else if (getName().equals("test_setFontLorg_eclipse_swt_graphics_Font")) test_setFontLorg_eclipse_swt_graphics_Font();
	else if (getName().equals("test_setSelectionI")) test_setSelectionI();
	else if (getName().equals("test_setSelectionII")) test_setSelectionII();
	else if (getName().equals("test_setRedrawZ")) test_setRedrawZ();
	else if (getName().equals("test_setSelectionLorg_eclipse_swt_graphics_Point")) test_setSelectionLorg_eclipse_swt_graphics_Point();
	else if (getName().equals("test_setTabsI")) test_setTabsI();
	else if (getName().equals("test_setTextLjava_lang_String")) test_setTextLjava_lang_String();
	else if (getName().equals("test_setTextLimitI")) test_setTextLimitI();
	else if (getName().equals("test_setTopIndexI")) test_setTopIndexI();
	else if (getName().equals("test_showSelection")) test_showSelection();
	else super.runTest();
}
}
