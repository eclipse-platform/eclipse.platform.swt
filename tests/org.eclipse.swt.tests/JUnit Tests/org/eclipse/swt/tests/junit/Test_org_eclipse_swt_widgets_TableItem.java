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
 * Automated Test Suite for class org.eclipse.swt.widgets.TableItem
 *
 * @see org.eclipse.swt.widgets.TableItem
 */
public class Test_org_eclipse_swt_widgets_TableItem extends Test_org_eclipse_swt_widgets_Item {

Table table;
TableItem tableItem;

public Test_org_eclipse_swt_widgets_TableItem(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	super.setUp();
	makeCleanEnvironment();
}

protected void tearDown() {
	super.tearDown();
}

// this method must be private or protected so the auto-gen tool keeps it
private void makeCleanEnvironment() {
	if ( tableItem != null ) tableItem.dispose();
	if ( table != null ) table.dispose();
	table = new Table(shell, 0);
	tableItem = new TableItem(table, 0);
	setWidget(tableItem);
}

public void test_ConstructorLorg_eclipse_swt_widgets_TableI(){
 	if (fCheckSwtNullExceptions) {
		try {
			TableItem tItem = new TableItem(null, SWT.NULL);
			fail("No exception thrown for parent == null");
		}
		catch (IllegalArgumentException e) {
		}
	}
}

public void test_ConstructorLorg_eclipse_swt_widgets_TableII() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_widgets_TableII not written");
}

public void test_checkSubclass() {
	warnUnimpl("Test test_checkSubclass not written");
}

public void test_getBackground() {
	// tested in test_setBackgroundLorg_eclipse_swt_graphics_Color
}

public void test_getBoundsI(){
	int boundsX;
	Rectangle bounds;
	Table table2 = new Table(shell, SWT.CHECK);
	TableItem tableItem2 = new TableItem(table2, SWT.NULL);
 	bounds = tableItem.getBounds(0);
	assertTrue(":a:", bounds.x > 0 && bounds.width > 0);
	boundsX = bounds.x;
 	bounds = tableItem.getBounds(-1);
	assertTrue(":b:", bounds.equals(new Rectangle(0, 0, 0, 0)));	
 	bounds = tableItem.getBounds(1);
	assertTrue(":c:", bounds.equals(new Rectangle(0, 0, 0, 0)));
 	//table2.setWidths(new int[] {30});
	TableColumn column = new TableColumn(table2, SWT.NONE, 0);
	column.setWidth(30);
	bounds = tableItem2.getBounds(0);
	assertTrue(":d:", bounds.x > boundsX && bounds.width > 0);
 	bounds = tableItem2.getBounds(-1);
	assertEquals(new Rectangle(0, 0, 0, 0), bounds);	
 	bounds = tableItem2.getBounds(1);
	assertEquals(new Rectangle(0, 0, 0, 0), bounds);			

	
	//
	makeCleanEnvironment();

	Image image = images[0];
	table2.dispose();
	table2 = new Table(shell, SWT.CHECK);
	tableItem2.dispose();
	tableItem2 = new TableItem(table2, SWT.NULL);
	column.dispose();

	new TableColumn(table, SWT.NULL);
	new TableColumn(table, SWT.NULL);
	tableItem.setImage(1, image);
	bounds = tableItem.getBounds(0);
	assertTrue(":a:", bounds.x > 0 && bounds.width > 0);
	boundsX = bounds.x;
 	bounds = tableItem.getBounds(-1);
	assertEquals(new Rectangle(0, 0, 0, 0), bounds);	
 	bounds = tableItem.getBounds(1);
	//assert(":c:", bounds.x > 0 && bounds.width > 0);  // ?? setting the image in one column does not affect width of other columns
	assertTrue(":c:", bounds.x > 0 && bounds.height > 0);
 
	column = new TableColumn(table2, SWT.NULL);
	column.setWidth(30);
	new TableColumn(table2, SWT.NULL);	
	tableItem2.setImage(1, image);
	bounds = tableItem2.getBounds(0);
	assertTrue(":d:", bounds.x > boundsX && bounds.width > 0);
 	bounds = tableItem2.getBounds(-1);
	assertEquals(new Rectangle(0, 0, 0, 0), bounds);	
 	bounds = tableItem2.getBounds(1);
	//assert(":f:", bounds.x > 0 && bounds.width > 0); // ?? setting the image in one column does not affect width of other columns
	assertTrue(":f:", bounds.x > 0 && bounds.height > 0);
}

public void test_getChecked() {
	warnUnimpl("Test test_getChecked not written");
}

public void test_getDisplay() {
	assertEquals(table.getDisplay(), tableItem.getDisplay());
}

public void test_getForeground() {
	// tested in test_setForegroundLorg_eclipse_swt_graphics_Color
}

public void test_getGrayed() {
	warnUnimpl("Test test_getGrayed not written");
}

public void test_getImageI() {
	warnUnimpl("Test test_getImageI not written");
}

/**
 * Test without item image
 */
public void test_getImageBoundsI(){
	Rectangle bounds;
	Table table2 = new Table(shell, SWT.CHECK);
	TableItem tableItem2 = new TableItem(table2, SWT.NULL);
	int imageX;
	
	assertEquals(new Rectangle(0, 0, 0, 0), tableItem.getImageBounds(-1));
	
	bounds = tableItem.getImageBounds(0);
	assertTrue(":b:", bounds.x > 0 && bounds.width == 0);
	imageX = bounds.x;
	
	assertEquals(new Rectangle(0, 0, 0, 0), tableItem.getImageBounds(1));
	
	assertEquals(new Rectangle(0, 0, 0, 0), tableItem2.getImageBounds(-1));
	
	bounds = tableItem2.getImageBounds(0);
	assertTrue(":e:", bounds.x > imageX && bounds.width == 0);
	
	assertEquals(new Rectangle(0, 0, 0, 0), tableItem2.getImageBounds(1));
 	//
	makeCleanEnvironment();
	
	Image image = images[0];	
	int imageWidth = image.getBounds().width;
	int imageHeight;
	
	tableItem.setImage(0, image);
	imageHeight = table.getItemHeight() - table.getGridLineWidth();
	assertEquals(new Rectangle(0, 0, 0, 0), tableItem.getImageBounds(-1));
	
	bounds = tableItem.getImageBounds(0);
//	assertTrue(":b:", bounds.x > 0 && bounds.width == imageWidth && bounds.height == imageHeight);	
// 	assertEquals(new Rectangle(0, 0, 0, 0), tableItem.getImageBounds(1));	


	//
	makeCleanEnvironment();	
	
	table2.dispose();
	table2 = new Table(shell, SWT.CHECK);
	tableItem2.dispose();
	tableItem2 = new TableItem(table2, SWT.NULL);
	Rectangle imageBounds = image.getBounds();
	imageWidth = imageBounds.width; 	tableItem2.setImage(0, image);
	imageHeight = table2.getItemHeight() - table2.getGridLineWidth();
	assertEquals(new Rectangle(0, 0, 0, 0), tableItem2.getImageBounds(-1));
	
	bounds = tableItem2.getImageBounds(0);	// bounds.width should be check box width if they are wider than image
//	assertTrue(":b:", bounds.x > 0 && bounds.width > 0 && bounds.height == imageHeight);
// 	assertEquals(new Rectangle(0, 0, 0, 0), tableItem2.getImageBounds(1));	


	//
	makeCleanEnvironment();

	table2.dispose();
	table2 = new Table(shell, SWT.CHECK);
	tableItem2.dispose();
	tableItem2 = new TableItem(table2, SWT.NULL);
	image = images[1];
	imageBounds = image.getBounds();
	imageWidth = imageBounds.width;
 	tableItem2.setImage(0, image);
	imageHeight = table2.getItemHeight() - table2.getGridLineWidth();
	assertEquals(new Rectangle(0, 0, 0, 0), tableItem2.getImageBounds(-1));
 	bounds = tableItem2.getImageBounds(0);	// bounds.width should be check box width if check box is wider than image
//	assertTrue(":b:", bounds.x > 0 && bounds.width > 0 && bounds.height == imageHeight);
 	assertEquals(new Rectangle(0, 0, 0, 0), tableItem2.getImageBounds(1));
}

public void test_getImageIndent() {
	warnUnimpl("Test test_getImageIndent not written");
}

public void test_getParent() {
	assertEquals(table, tableItem.getParent());
}

public void test_getTextI() {
	warnUnimpl("Test test_getTextI not written");
}

public void test_setBackgroundLorg_eclipse_swt_graphics_Color() {
	Color color = new Color(tableItem.getDisplay(), 255, 0, 0);
	tableItem.setBackground(color);
	assertEquals(color, tableItem.getBackground());
	tableItem.setBackground(null);
	assertEquals(table.getBackground(),tableItem.getBackground());
	color.dispose();
	try { 
		tableItem.setBackground(color);
		fail("No exception thrown for color disposed");		
	} catch (IllegalArgumentException e) {
	}
}

public void test_setCheckedZ(){
	assertEquals(false, tableItem.getChecked());
	
	tableItem.setChecked(true);
	assertEquals(false, tableItem.getChecked());
 	Table t = new Table(shell, SWT.CHECK);
	TableItem ti = new TableItem(t, SWT.NULL);
	ti.setChecked(true);
	assertTrue(ti.getChecked());
	
	ti.setChecked(false);
	assertEquals(false, ti.getChecked());
	t.dispose();
}

public void test_setForegroundLorg_eclipse_swt_graphics_Color() {
	Color color = new Color(tableItem.getDisplay(), 255, 0, 0);
	tableItem.setForeground(color);
	assertEquals(color, tableItem.getForeground());
	tableItem.setForeground(null);
	assertEquals(table.getForeground(),tableItem.getForeground());
	color.dispose();
	try { 
		tableItem.setForeground(color);
		fail("No exception thrown for color disposed");
	} catch (IllegalArgumentException e) {
	}
}

public void test_setGrayedZ() {
	warnUnimpl("Test test_setGrayedZ not written");
}

public void test_setImage$Lorg_eclipse_swt_graphics_Image(){
	assertNull(tableItem.getImage(1));	
 	tableItem.setImage(-1, null);		
	assertNull(tableItem.getImage(-1));	
		
	tableItem.setImage(0, images[0]);
	assertEquals(images[0], tableItem.getImage(0));	
 	String texts[] = new String[images.length];
	for (int i = 0; i < texts.length; i++) {
		texts[i] = String.valueOf(i);
	}
	
	//table.setText(texts);				// create enough columns for TableItem.setImage(Image[]) to work
	int columnCount = table.getColumnCount();
	if (columnCount < texts.length) {
		for (int i = columnCount; i < texts.length; i++){
			TableColumn column = new TableColumn(table, SWT.NONE);
		}
	}
	TableColumn[] columns = table.getColumns();
	for (int i = 0; i < texts.length; i++) {
		columns[i].setText(texts[i]);
	}
	tableItem.setImage(1, images[1]);
	assertEquals(images[1], tableItem.getImage(1));	
 	tableItem.setImage(images);
	for (int i = 0; i < images.length; i++) {
		assertEquals(images[i], tableItem.getImage(i));
	}
	try {
		tableItem.setImage((Image []) null);
		fail("No exception thrown for images == null");
	}
	catch (IllegalArgumentException e) {
	}
}

public void test_setImageILorg_eclipse_swt_graphics_Image() {
	warnUnimpl("Test test_setImageILorg_eclipse_swt_graphics_Image not written");
}

public void test_setImageLorg_eclipse_swt_graphics_Image() {
	warnUnimpl("Test test_setImageLorg_eclipse_swt_graphics_Image not written");
}

public void test_setImageIndentI(){
	assertEquals(0, tableItem.getImageIndent());
 	tableItem.setImageIndent(1);
	assertEquals(1, tableItem.getImageIndent());
 	tableItem.setImageIndent(-1);
	assertEquals(1, tableItem.getImageIndent());
}

public void test_setText$Ljava_lang_String(){
	final String TestString = "test";
	final String TestStrings[] = new String[] {TestString, TestString + "1", TestString + "2"};
	
	try {
		tableItem.setText((String []) null);
		fail("No exception thrown for strings == null");
	}
	catch (IllegalArgumentException e) {
	}
	
   /*
 	* Test the getText/setText API with a Table that has only 
 	* the default column.
 	*/
	
	assertEquals(0, tableItem.getText(1).length());
	
	tableItem.setText(TestStrings);
	assertEquals(TestStrings[0], tableItem.getText(0));
	for (int i = 1; i < TestStrings.length; i++) {
		assertEquals(0, tableItem.getText(i).length());
	}
	
	
   /*
 	* Test the getText/setText API with a Table that enough 
 	* columns to fit all test item texts.
 	*/
 		
	int columnCount = table.getColumnCount();
	if (columnCount < images.length) {
		for (int i = columnCount; i < images.length; i++){
			TableColumn column = new TableColumn(table, SWT.NONE);
		}
	}
	TableColumn[] columns = table.getColumns();
	for (int i = 0; i < TestStrings.length; i++) {
		columns[i].setText(TestStrings[i]);
	}
	assertEquals(0, tableItem.getText(1).length());

}

public void test_setTextILjava_lang_String(){
	final String TestString = "test";
	final String TestStrings[] = new String[] {TestString, TestString + "1", TestString + "2"};

   /*
 	* Test the getText/setText API with a Table that has only 
 	* the default column.
 	*/
	
	assertEquals(0, tableItem.getText(1).length());	
 	tableItem.setText(1, TestString);
	assertEquals(0, tableItem.getText(1).length());	
	assertEquals(0, tableItem.getText(0).length());
	
	tableItem.setText(0, TestString);
	assertEquals(TestString, tableItem.getText(0));
 	tableItem.setText(-1, TestStrings[1]);
	assertEquals(0, tableItem.getText(-1).length());	

   /*
 	* Test the getText/setText API with a Table that enough 
 	* columns to fit all test item texts.
 	*/

	makeCleanEnvironment();
	
	//table.setText(TestStrings);				// create anough columns for TableItem.setText(String[]) to work
	int columnCount = table.getColumnCount();
	if (columnCount < images.length) {
		for (int i = columnCount; i < images.length; i++){
			TableColumn column = new TableColumn(table, SWT.NONE);
		}
	}
	TableColumn[] columns = table.getColumns();
	for (int i = 0; i < TestStrings.length; i++) {
		columns[i].setText(TestStrings[i]);
	}
	assertEquals(0, tableItem.getText(1).length());	


	tableItem.setText(1, TestString);
	assertEquals(TestString, tableItem.getText(1));	
	assertEquals(0, tableItem.getText(0).length());
	
	tableItem.setText(0, TestString);
	assertEquals(TestString, tableItem.getText(0));


	tableItem.setText(-1, TestStrings[1]);
	assertEquals(0, tableItem.getText(-1).length());	


	try {
		tableItem.setText(-1, null);		
		fail("No exception thrown for string == null");
	}
	catch (IllegalArgumentException e) {
	}
	
	try {
		tableItem.setText(0, null);		
		fail("No exception thrown for string == null");
	}
	catch (IllegalArgumentException e) {
	} 


}

public void test_setTextLjava_lang_String() {
	warnUnimpl("Test test_setTextLjava_lang_String not written");
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_widgets_TableItem((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_TableI");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_TableII");
	methodNames.addElement("test_checkSubclass");
	methodNames.addElement("test_getBoundsI");
	methodNames.addElement("test_getChecked");
	methodNames.addElement("test_getDisplay");
	methodNames.addElement("test_getGrayed");
	methodNames.addElement("test_getImageI");
	methodNames.addElement("test_getImageBoundsI");
	methodNames.addElement("test_getImageIndent");
	methodNames.addElement("test_getParent");
	methodNames.addElement("test_getTextI");
	methodNames.addElement("test_setCheckedZ");
	methodNames.addElement("test_setGrayedZ");
	methodNames.addElement("test_setBackgroundLorg_eclipse_swt_graphics_Color");	
	methodNames.addElement("test_setForegroundLorg_eclipse_swt_graphics_Color");		
	methodNames.addElement("test_setImage$Lorg_eclipse_swt_graphics_Image");
	methodNames.addElement("test_setImageILorg_eclipse_swt_graphics_Image");
	methodNames.addElement("test_setImageLorg_eclipse_swt_graphics_Image");
	methodNames.addElement("test_setImageIndentI");
	methodNames.addElement("test_setText$Ljava_lang_String");
	methodNames.addElement("test_setTextILjava_lang_String");
	methodNames.addElement("test_setTextLjava_lang_String");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Item.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_TableI")) test_ConstructorLorg_eclipse_swt_widgets_TableI();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_TableII")) test_ConstructorLorg_eclipse_swt_widgets_TableII();
	else if (getName().equals("test_checkSubclass")) test_checkSubclass();
	else if (getName().equals("test_getBoundsI")) test_getBoundsI();
	else if (getName().equals("test_getChecked")) test_getChecked();
	else if (getName().equals("test_getDisplay")) test_getDisplay();
	else if (getName().equals("test_getGrayed")) test_getGrayed();
	else if (getName().equals("test_getImageI")) test_getImageI();
	else if (getName().equals("test_getImageBoundsI")) test_getImageBoundsI();
	else if (getName().equals("test_getImageIndent")) test_getImageIndent();
	else if (getName().equals("test_getParent")) test_getParent();
	else if (getName().equals("test_getTextI")) test_getTextI();
	else if (getName().equals("test_setBackgroundLorg_eclipse_swt_graphics_Color")) test_setBackgroundLorg_eclipse_swt_graphics_Color();
	else if (getName().equals("test_setCheckedZ")) test_setCheckedZ();
	else if (getName().equals("test_setForegroundLorg_eclipse_swt_graphics_Color")) test_setForegroundLorg_eclipse_swt_graphics_Color();
	else if (getName().equals("test_setGrayedZ")) test_setGrayedZ();
	else if (getName().equals("test_setImage$Lorg_eclipse_swt_graphics_Image")) test_setImage$Lorg_eclipse_swt_graphics_Image();
	else if (getName().equals("test_setImageILorg_eclipse_swt_graphics_Image")) test_setImageILorg_eclipse_swt_graphics_Image();
	else if (getName().equals("test_setImageLorg_eclipse_swt_graphics_Image")) test_setImageLorg_eclipse_swt_graphics_Image();
	else if (getName().equals("test_setImageIndentI")) test_setImageIndentI();
	else if (getName().equals("test_setText$Ljava_lang_String")) test_setText$Ljava_lang_String();
	else if (getName().equals("test_setTextILjava_lang_String")) test_setTextILjava_lang_String();
	else if (getName().equals("test_setTextLjava_lang_String")) test_setTextLjava_lang_String();
	else super.runTest();
}
}
