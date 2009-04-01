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
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.TableItem
 *
 * @see org.eclipse.swt.widgets.TableItem
 */
public class Test_org_eclipse_swt_widgets_TableItem extends Test_org_eclipse_swt_widgets_Item {

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

public void test_ConstructorLorg_eclipse_swt_widgets_TableI() {
	try {
		new TableItem(null, SWT.NULL);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}
	
	for (int i=0; i<10; i++) {
		new TableItem(table, SWT.NONE);	
	}
	assertEquals(11, table.getItemCount());
	new TableItem(table, SWT.NONE, 5);	
	assertEquals(12, table.getItemCount());
}

public void test_ConstructorLorg_eclipse_swt_widgets_TableII() {
	try {
		new TableItem(table, SWT.NONE, 5);
		fail("No exception thrown for illegal index argument");
	}
	catch (IllegalArgumentException e) {
	}
}

public void test_getBackground() {
	// tested in test_setBackgroundLorg_eclipse_swt_graphics_Color
}

public void test_getBackgroundI() {
	// tested in test_setBackgroundILorg_eclipse_swt_graphics_Color
}

public void test_getBoundsI() {
	Image image = images[0];
	Rectangle bounds;
	Rectangle bounds2;
	
	// no columns
 	bounds = tableItem.getBounds(0);
	assertTrue(":1a:", bounds.x > 0 && bounds.height > 0);
	bounds = tableItem.getBounds(-1);
	assertTrue(":1b:", bounds.equals(new Rectangle(0, 0, 0, 0)));	
 	bounds = tableItem.getBounds(1);
	assertTrue(":1c:", bounds.equals(new Rectangle(0, 0, 0, 0)));
	
	tableItem.setText("hello");
	bounds = tableItem.getBounds(0);
	assertTrue(":1d:", bounds.x > 0 && bounds.height > 0 && bounds.width > 0);
	tableItem.setText("");
	bounds2 = tableItem.getBounds(0);
	assertTrue(":1e:", bounds2.x > 0 && bounds2.height > 0);
	//assertTrue(":1f:", bounds2.width < bounds.width); // TODO doesn't shrink?
	
	//
	makeCleanEnvironment();
	
	tableItem.setImage(image);
	bounds = tableItem.getBounds(0);
	assertTrue(":1g:", bounds.x > 0 && bounds.height > 0 && bounds.width > 0);
	tableItem.setImage((Image)null);
	bounds2 = tableItem.getBounds(0);
	assertTrue(":1h:", bounds2.x > 0 && bounds2.height > 0);
//	assertTrue(":1i:", bounds2.width > bounds.width); // TODO once an image is added the space for it is always there
 	
	//
	makeCleanEnvironment();
	
	tableItem.setText("hello");
	bounds = tableItem.getBounds(0);
	tableItem.setImage(image);
	bounds2 = tableItem.getBounds(0);
	assertTrue(":1j:", bounds2.x > 0 && bounds2.height > 0);
	assertTrue(":1k:", bounds2.width > bounds.width);
	
	// no columns and CHECK style
	Table table2 = new Table(shell, SWT.CHECK);
	TableItem tableItem2 = new TableItem(table2, SWT.NONE);
	
	bounds = tableItem2.getBounds(0);
	assertTrue(":2a:", bounds.x > 0 && bounds.height > 0);
	bounds = tableItem2.getBounds(-1);
	assertTrue(":2b:", bounds.equals(new Rectangle(0, 0, 0, 0)));	
 	bounds = tableItem2.getBounds(1);
	assertTrue(":2c:", bounds.equals(new Rectangle(0, 0, 0, 0)));
	
	tableItem2.setText("hello");
	bounds = tableItem2.getBounds(0);
	assertTrue(":2d:", bounds.x > 0 && bounds.height > 0 && bounds.width > 0);
	tableItem2.setText("");
	bounds2 = tableItem2.getBounds(0);
	assertTrue(":2e:", bounds2.x > 0 && bounds2.height > 0);
	//assertTrue(":2f:", bounds2.width < bounds.width); // TODO doesn't shrink?
	
	table2.dispose();
	table2 = new Table(shell, SWT.CHECK);
	tableItem2 = new TableItem(table2, SWT.NONE);
	
	tableItem2.setImage(image);
	bounds = tableItem2.getBounds(0);
	assertTrue(":2g:", bounds.x > 0 && bounds.height > 0 && bounds.width > 0);
	tableItem2.setImage((Image)null);
	bounds2 = tableItem2.getBounds(0);
	assertTrue(":2h:", bounds2.x > 0 && bounds2.height > 0);
	//assertTrue(":2i:", bounds2.width < bounds.width);  // TODO once an image is added the space for it is always there
	
	table2.dispose();
	table2 = new Table(shell, SWT.CHECK);
	tableItem2 = new TableItem(table2, SWT.NONE);
	
	tableItem2.setText("hello");
	bounds = tableItem2.getBounds(0);
	tableItem2.setImage(image);
	bounds2 = tableItem2.getBounds(0);
	assertTrue(":2j:", bounds2.x > 0 && bounds2.height > 0);
	assertTrue(":2k:", bounds2.width > bounds.width);
	
	//
	makeCleanEnvironment();

	// with columns
	
	TableColumn column0 = new TableColumn(table, SWT.LEFT);
	TableColumn column1 = new TableColumn(table, SWT.CENTER);
	
	bounds = tableItem.getBounds(0);
	assertTrue(":3a:", bounds.x > 0 && bounds.height > 0 && bounds.width == 0);
	bounds = tableItem.getBounds(1);
	assertTrue(":3b:", /*bounds.x > 0 &&*/ bounds.height > 0 && bounds.width == 0); // TODO bounds.x == 0 Is this right?
	bounds = tableItem.getBounds(-1);
	assertTrue(":3c:", bounds.equals(new Rectangle(0, 0, 0, 0)));	
 	bounds = tableItem.getBounds(2);
	assertTrue(":3d:", bounds.equals(new Rectangle(0, 0, 0, 0)));
	
	column0.setWidth(100);
	bounds = tableItem.getBounds(0);
	assertTrue(":3e:", bounds.x > 0 && bounds.height > 0 && bounds.width > 0 && bounds.width < 100);
	bounds = tableItem.getBounds(1);
	assertTrue(":3f:", bounds.x >= 100 && bounds.height > 0 && bounds.width == 0);
	
	column1.setWidth(200);
	bounds = tableItem.getBounds(0);
	assertTrue(":3g:", bounds.x > 0 && bounds.height > 0 && bounds.width > 0 && bounds.width < 100);
	bounds = tableItem.getBounds(1);
	assertTrue(":3h:", bounds.x >= 100 && bounds.height > 0 && bounds.width == 200);
	
	tableItem.setText(new String[] {"hello", "world"});
	bounds = tableItem.getBounds(0);
	assertTrue(":3i:", bounds.x > 0 && bounds.height > 0 && bounds.width > 0 && bounds.width < 100);
	bounds = tableItem.getBounds(1);
	assertTrue(":3j:", bounds.x >= 100 && bounds.height > 0 && bounds.width  == 200);
	tableItem.setText(new String[] {"", ""});
	bounds = tableItem.getBounds(0);
	assertTrue(":3k:", bounds.x > 0 && bounds.height > 0 && bounds.width > 0 && bounds.width < 100);
	bounds = tableItem.getBounds(1);
	assertTrue(":3l:", bounds.x >= 100 && bounds.height > 0 && bounds.width  == 200);
	
	//
	makeCleanEnvironment();
	column0 = new TableColumn(table, SWT.LEFT);
	column1 = new TableColumn(table, SWT.CENTER);
	column0.setWidth(100);
	column1.setWidth(200);
	
	tableItem.setImage(new Image[] {image, image});
	bounds = tableItem.getBounds(0);
	assertTrue(":3m:", bounds.x > 0 && bounds.height > 0 && bounds.width > 0 && bounds.width < 100);
	bounds = tableItem.getBounds(1);
	assertTrue(":3n:", bounds.x >= 100 && bounds.height > 0 && bounds.width  == 200);
 	tableItem.setImage(new Image[] {null, null});
	bounds = tableItem.getBounds(0);
	assertTrue(":3o:", bounds.x > 0 && bounds.height > 0 && bounds.width > 0 && bounds.width < 100);
	bounds = tableItem.getBounds(1);
	assertTrue(":3p:", bounds.x >= 100 && bounds.height > 0 && bounds.width  == 200);
	
	//
	makeCleanEnvironment();
	column0 = new TableColumn(table, SWT.LEFT);
	column1 = new TableColumn(table, SWT.CENTER);
	column0.setWidth(100);
	column1.setWidth(200);
	
	tableItem.setText(new String[] {"hello", "world"});
	tableItem.setImage(new Image[] {null, null});
	bounds = tableItem.getBounds(0);
	assertTrue(":3q:", bounds.x > 0 && bounds.height > 0 && bounds.width > 0 && bounds.width < 100);
	bounds = tableItem.getBounds(1);
	assertTrue(":3r:", bounds.x > 0 && bounds.height > 0 && bounds.width  == 200);
	
	//
	makeCleanEnvironment();
	
	tableItem.setText("hello");
	new TableColumn(table, SWT.RIGHT);
	bounds = tableItem.getBounds(0);
	assertTrue(":3s:", bounds.x > 0 && bounds.height > 0 && bounds.width  == 0);
	
	// with columns and CHECK style
	table2.dispose();
	table2 = new Table(shell, SWT.CHECK);
	tableItem2 = new TableItem(table2, SWT.NONE);
	column0 = new TableColumn(table2, SWT.LEFT);
	column1 = new TableColumn(table2, SWT.CENTER);
	
	bounds = tableItem2.getBounds(0);
	assertTrue(":4a:", bounds.x > 0 && bounds.height > 0 && bounds.width == 0);
	bounds = tableItem2.getBounds(1);
	assertTrue(":4b:", /*bounds.x > 0 &&*/ bounds.height > 0 && bounds.width == 0); // TODO bounds.x == 0 Is this right?
	bounds = tableItem2.getBounds(-1);
	assertTrue(":4c:", bounds.equals(new Rectangle(0, 0, 0, 0)));	
 	bounds = tableItem2.getBounds(2);
	assertTrue(":4d:", bounds.equals(new Rectangle(0, 0, 0, 0)));
	
	column0.setWidth(100);
	bounds = tableItem2.getBounds(0);
	assertTrue(":4e:", bounds.x > 0 && bounds.height > 0 && bounds.width > 0 && bounds.width < 100);
	bounds = tableItem2.getBounds(1);
	assertTrue(":4f:", bounds.x >= 100 && bounds.height > 0 && bounds.width == 0);
	
	column1.setWidth(200);
	bounds = tableItem2.getBounds(0);
	assertTrue(":4g:", bounds.x > 0 && bounds.height > 0 && bounds.width > 0 && bounds.width < 100);
	bounds = tableItem2.getBounds(1);
	assertTrue(":4h:", bounds.x >= 100 && bounds.height > 0 && bounds.width == 200);
	
	tableItem2.setText(new String[] {"hello", "world"});
	bounds = tableItem2.getBounds(0);
	assertTrue(":4i:", bounds.x > 0 && bounds.height > 0 && bounds.width > 0 && bounds.width < 100);
	bounds = tableItem2.getBounds(1);
	assertTrue(":4j:", bounds.x >= 100 && bounds.height > 0 && bounds.width  == 200);
	tableItem2.setText(new String[] {"", ""});
	bounds = tableItem2.getBounds(0);
	assertTrue(":4k:", bounds.x > 0 && bounds.height > 0 && bounds.width > 0 && bounds.width < 100);
	bounds = tableItem2.getBounds(1);
	assertTrue(":4l:", bounds.x >= 100 && bounds.height > 0 && bounds.width  == 200);
	
	//
	table2.dispose();
	table2 = new Table(shell, SWT.CHECK);
	tableItem2 = new TableItem(table2, SWT.NONE);
	column0 = new TableColumn(table2, SWT.LEFT);
	column1 = new TableColumn(table2, SWT.CENTER);
	column0.setWidth(100);
	column1.setWidth(200);
	
	tableItem2.setImage(new Image[] {image, image});
	bounds = tableItem2.getBounds(0);
	assertTrue(":4m:", bounds.x > 0 && bounds.height > 0 && bounds.width > 0 && bounds.width < 100);
	bounds = tableItem2.getBounds(1);
	assertTrue(":4n:", bounds.x >= 100 && bounds.height > 0 && bounds.width  == 200);
 	tableItem2.setImage(new Image[] {null, null});
	bounds = tableItem2.getBounds(0);
	assertTrue(":4o:", bounds.x > 0 && bounds.height > 0 && bounds.width > 0 && bounds.width < 100);
	bounds = tableItem2.getBounds(1);
	assertTrue(":4p:", bounds.x >= 100 && bounds.height > 0 && bounds.width  == 200);
	
	//
	table2.dispose();
	table2 = new Table(shell, SWT.CHECK);
	tableItem2 = new TableItem(table2, SWT.NONE);
	column0 = new TableColumn(table2, SWT.LEFT);
	column1 = new TableColumn(table2, SWT.CENTER);
	column0.setWidth(100);
	column1.setWidth(200);
	
	tableItem2.setText(new String[] {"hello", "world"});
	tableItem2.setImage(new Image[] {null, null});
	bounds = tableItem2.getBounds(0);
	assertTrue(":4q:", bounds.x > 0 && bounds.height > 0 && bounds.width > 0 && bounds.width < 100);
	bounds = tableItem2.getBounds(1);
	assertTrue(":4r:", bounds.x >= 100 && bounds.height > 0 && bounds.width  == 200);
	
	//
	table2.dispose();
	table2 = new Table(shell, SWT.CHECK);
	tableItem2 = new TableItem(table2, SWT.NONE);
	
	tableItem2.setText("hello");
	new TableColumn(table2, SWT.RIGHT);
	bounds = tableItem2.getBounds(0);
	assertTrue(":4s:", bounds.x > 0 && bounds.height > 0 && bounds.width  == 0);
}

public void test_getChecked() {
	// tested in test_setCheckedZ
}

public void test_getFont() {
	// tested in test_setFontLorg_eclipse_swt_graphics_Font
}

public void test_getFontI() {
	// tested in test_setFontILorg_eclipse_swt_graphics_Font
}

public void test_getForeground() {
	// tested in test_setForegroundLorg_eclipse_swt_graphics_Color
}

public void test_getForegroundI() {
	// tested in test_setForegroundILorg_eclipse_swt_graphics_Color
}

public void test_getGrayed() {
	// tested in test_setGrayedZ
}

public void test_getImageBoundsI() {
/**
 * Test without item image
 */
	Rectangle bounds;
	Table table2 = new Table(shell, SWT.CHECK);
	TableItem tableItem2 = new TableItem(table2, SWT.NULL);
	
	assertEquals(new Rectangle(0, 0, 0, 0), tableItem.getImageBounds(-1));
	
	bounds = tableItem.getImageBounds(0);
	assertTrue(":b:", bounds.width == 0);
	
	assertEquals(new Rectangle(0, 0, 0, 0), tableItem.getImageBounds(1));
	
	assertEquals(new Rectangle(0, 0, 0, 0), tableItem2.getImageBounds(-1));
	
	bounds = tableItem2.getImageBounds(0);
	assertTrue(":e:", bounds.width == 0);
	
	assertEquals(new Rectangle(0, 0, 0, 0), tableItem2.getImageBounds(1));
 	//
	makeCleanEnvironment();
	
	Image image = images[0];	
//	int imageWidth = image.getBounds().width;
//	int imageHeight;
	
	tableItem.setImage(0, image);
//	imageHeight = table.getItemHeight() - table.getGridLineWidth();
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
//	Rectangle imageBounds = image.getBounds();
//	imageWidth = imageBounds.width; 	tableItem2.setImage(0, image);
//	imageHeight = table2.getItemHeight() - table2.getGridLineWidth();
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
//	imageBounds = image.getBounds();
//	imageWidth = imageBounds.width;
 	tableItem2.setImage(0, image);
//	imageHeight = table2.getItemHeight() - table2.getGridLineWidth();
	assertEquals(new Rectangle(0, 0, 0, 0), tableItem2.getImageBounds(-1));
 	bounds = tableItem2.getImageBounds(0);	// bounds.width should be check box width if check box is wider than image
//	assertTrue(":b:", bounds.x > 0 && bounds.width > 0 && bounds.height == imageHeight);
 	assertEquals(new Rectangle(0, 0, 0, 0), tableItem2.getImageBounds(1));
}

public void test_getImageI() {
	// tested in test_setImageILorg_eclipse_swt_graphics_Image
}

public void test_getImageIndent() {
	warnUnimpl("Test test_getImageIndent not written");
}

public void test_getParent() {
	assertEquals(table, tableItem.getParent());
}

public void test_getTextI() {
	// tested in test_setTextILJava_lang_String
}

public void test_setBackgroundILorg_eclipse_swt_graphics_Color() {
	Display display = tableItem.getDisplay();
	Color red = display.getSystemColor(SWT.COLOR_RED);
	Color blue = display.getSystemColor(SWT.COLOR_BLUE);
	
	// no columns
	assertEquals(table.getBackground(), tableItem.getBackground(0));
	assertEquals(tableItem.getBackground(), tableItem.getBackground(0));
	tableItem.setBackground(0, red);
	assertEquals(red, tableItem.getBackground(0));
	
	// index beyond range - no error
	tableItem.setBackground(10, red);
	assertEquals(tableItem.getBackground(), tableItem.getBackground(10));
	
	// with columns
	new TableColumn(table, SWT.LEFT);
	new TableColumn(table, SWT.LEFT);
	
	// index beyond range - no error
	tableItem.setBackground(10, red);
	assertEquals(tableItem.getBackground(), tableItem.getBackground(10));
	
	tableItem.setBackground(0, red);
	assertEquals(red, tableItem.getBackground(0));
	tableItem.setBackground(0, null);
	assertEquals(table.getBackground(),tableItem.getBackground(0));

	tableItem.setBackground(0, blue);
	tableItem.setBackground(red);
	assertEquals(blue, tableItem.getBackground(0));
	
	tableItem.setBackground(0, null);
	assertEquals(red, tableItem.getBackground(0));
	
	tableItem.setBackground(null);
	assertEquals(table.getBackground(),tableItem.getBackground(0));
	
	try { 
		Color color = new Color(display, 255, 0, 0);
		color.dispose();
		tableItem.setBackground(color);
		fail("No exception thrown for color disposed");		
	} catch (IllegalArgumentException e) {
	}
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

public void test_setCheckedZ() {
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

public void test_setFontLorg_eclipse_swt_graphics_Font() {
	Display display = tableItem.getDisplay();
	Font font = tableItem.getFont();
	tableItem.setFont(font);
	assertTrue(font.equals(tableItem.getFont()));
	
	font = new Font(display, SwtJunit.testFontName, 10, SWT.NORMAL);
	tableItem.setFont(font);
	assertTrue(font.equals( tableItem.getFont()));

	tableItem.setFont(null);
	assertTrue(table.getFont().equals(tableItem.getFont()));
	
	font.dispose();
	try {
		tableItem.setFont(font);
		tableItem.setFont(null);
		fail("No exception thrown for disposed font");
	} catch (IllegalArgumentException e) {
	}
}

public void test_setFontILorg_eclipse_swt_graphics_Font() {
	Display display = tableItem.getDisplay();
	Font font = new Font(display, SwtJunit.testFontName, 10, SWT.NORMAL);
	
	// no columns
	assertTrue(table.getFont().equals(tableItem.getFont(0)));
	assertTrue(tableItem.getFont().equals(tableItem.getFont(0)));
	tableItem.setFont(0, font);
	assertTrue(font.equals(tableItem.getFont(0)));
	
	// index beyond range - no error
	tableItem.setFont(10, font);
	assertTrue(tableItem.getFont().equals(tableItem.getFont(10)));
	
	// with columns
	new TableColumn(table, SWT.LEFT);
	new TableColumn(table, SWT.LEFT);
	
	// index beyond range - no error
	tableItem.setFont(10, font);
	assertTrue(tableItem.getFont().equals(tableItem.getFont(10)));
	
	tableItem.setFont(0, font);
	assertTrue(font.equals(tableItem.getFont(0)));
	tableItem.setFont(0, null);
	assertTrue(table.getFont().equals(tableItem.getFont(0)));
	
	Font font2 = new Font(display, SwtJunit.testFontName, 20, SWT.NORMAL);
	
	tableItem.setFont(0, font);
	tableItem.setFont(font2);
	assertTrue(font.equals(tableItem.getFont(0)));
	
	tableItem.setFont(0, null);
	assertTrue(font2.equals(tableItem.getFont(0)));
	
	tableItem.setFont(null);
	assertTrue(table.getFont().equals(tableItem.getFont(0)));
	
	font.dispose();
	font2.dispose();
	
	try {
		tableItem.setFont(0, font);
		tableItem.setFont(0, null);
		fail("No exception thrown for disposed font");
	} catch (IllegalArgumentException e) {
	}
}

public void test_setForegroundILorg_eclipse_swt_graphics_Color() {
	Display display = tableItem.getDisplay();
	Color red = display.getSystemColor(SWT.COLOR_RED);
	Color blue = display.getSystemColor(SWT.COLOR_BLUE);
	
	// no columns
	assertEquals(table.getForeground(), tableItem.getForeground(0));
	assertEquals(tableItem.getForeground(), tableItem.getForeground(0));
	tableItem.setForeground(0, red);
	assertEquals(red, tableItem.getForeground(0));
	
	// index beyond range - no error
	tableItem.setForeground(10, red);
	assertEquals(tableItem.getForeground(), tableItem.getForeground(10));
	
	// with columns
	new TableColumn(table, SWT.LEFT);
	new TableColumn(table, SWT.LEFT);
	
	// index beyond range - no error
	tableItem.setForeground(10, red);
	assertEquals(tableItem.getForeground(), tableItem.getForeground(10));
	
	tableItem.setForeground(0, red);
	assertEquals(red, tableItem.getForeground(0));
	tableItem.setForeground(0, null);
	assertEquals(table.getForeground(),tableItem.getForeground(0));

	tableItem.setForeground(0, blue);
	tableItem.setForeground(red);
	assertEquals(blue, tableItem.getForeground(0));
	
	tableItem.setForeground(0, null);
	assertEquals(red, tableItem.getForeground(0));
	
	tableItem.setForeground(null);
	assertEquals(table.getForeground(),tableItem.getForeground(0));
	
	try { 
		Color color = new Color(display, 255, 0, 0);
		color.dispose();
		tableItem.setForeground(color);
		fail("No exception thrown for color disposed");		
	} catch (IllegalArgumentException e) {
	}
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
	Table newTable = new Table(shell, SWT.CHECK);
	TableItem tItem = new TableItem(newTable,0);
	assertEquals(false, tItem.getGrayed());
	tItem.setGrayed(true);
	assertTrue(tItem.getGrayed());
	tItem.setGrayed(false);
	assertEquals(false, tItem.getGrayed());
	newTable.dispose();
}

public void test_setImage$Lorg_eclipse_swt_graphics_Image() {
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
			new TableColumn(table, SWT.NONE);
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
		// no columns
	assertEquals(null, tableItem.getImage(0));
	tableItem.setImage(0, images[0]);
	assertEquals(images[0], tableItem.getImage(0));
	
	// index beyond range - no error
	tableItem.setImage(10, images[0]);
	assertEquals(null, tableItem.getImage(10));
	
	// with columns
	new TableColumn(table, SWT.LEFT);
	new TableColumn(table, SWT.LEFT);
	
	// index beyond range - no error
	tableItem.setImage(10, images[0]);
	assertEquals(null, tableItem.getImage(10));
	
	tableItem.setImage(0, images[0]);
	assertEquals(images[0], tableItem.getImage(0));
	tableItem.setImage(0, null);
	assertEquals(null, tableItem.getImage(0));
	
	tableItem.setImage(0, images[0]);
	tableItem.setImage(images[1]);
	assertEquals(images[1], tableItem.getImage(0));
	
	tableItem.setImage(images[1]);
	tableItem.setImage(0, images[0]);
	assertEquals(images[0], tableItem.getImage(0));
	
	images[0].dispose();
	try {
		tableItem.setImage(0, images[0]);
		tableItem.setImage(0, null);
		fail("No exception thrown for disposed font");
	} catch (IllegalArgumentException e) {
	}
}

public void test_setImageIndentI() {
	if (SwtJunit.isCarbon || SwtJunit.isCocoa || SwtJunit.isGTK) {
		//setImageIndent not implemented on Carbon
		tableItem.setImageIndent(1);
		return; 
	}
	assertEquals(0, tableItem.getImageIndent());
 	tableItem.setImageIndent(1);
	assertEquals(1, tableItem.getImageIndent());
 	tableItem.setImageIndent(-1);
	assertEquals(1, tableItem.getImageIndent());
}

public void test_setText$Ljava_lang_String() {
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
			new TableColumn(table, SWT.NONE);
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
			new TableColumn(table, SWT.NONE);
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
	methodNames.addElement("test_getBackground");
	methodNames.addElement("test_getBackgroundI");
	methodNames.addElement("test_getBoundsI");
	methodNames.addElement("test_getChecked");
	methodNames.addElement("test_getFont");
	methodNames.addElement("test_getFontI");
	methodNames.addElement("test_getForeground");
	methodNames.addElement("test_getForegroundI");
	methodNames.addElement("test_getGrayed");
	methodNames.addElement("test_getImageBoundsI");
	methodNames.addElement("test_getImageI");
	methodNames.addElement("test_getImageIndent");
	methodNames.addElement("test_getParent");
	methodNames.addElement("test_getTextI");
	methodNames.addElement("test_setBackgroundILorg_eclipse_swt_graphics_Color");
	methodNames.addElement("test_setBackgroundLorg_eclipse_swt_graphics_Color");
	methodNames.addElement("test_setCheckedZ");
	methodNames.addElement("test_setFontILorg_eclipse_swt_graphics_Font");
	methodNames.addElement("test_setFontLorg_eclipse_swt_graphics_Font");
	methodNames.addElement("test_setForegroundILorg_eclipse_swt_graphics_Color");
	methodNames.addElement("test_setForegroundLorg_eclipse_swt_graphics_Color");
	methodNames.addElement("test_setGrayedZ");
	methodNames.addElement("test_setImage$Lorg_eclipse_swt_graphics_Image");
	methodNames.addElement("test_setImageILorg_eclipse_swt_graphics_Image");
	methodNames.addElement("test_setImageIndentI");
	methodNames.addElement("test_setImageLorg_eclipse_swt_graphics_Image");
	methodNames.addElement("test_setText$Ljava_lang_String");
	methodNames.addElement("test_setTextILjava_lang_String");
	methodNames.addElement("test_setTextLjava_lang_String");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Item.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_TableI")) test_ConstructorLorg_eclipse_swt_widgets_TableI();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_TableII")) test_ConstructorLorg_eclipse_swt_widgets_TableII();
	else if (getName().equals("test_getBackground")) test_getBackground();
	else if (getName().equals("test_getBackgroundI")) test_getBackgroundI();
	else if (getName().equals("test_getBoundsI")) test_getBoundsI();
	else if (getName().equals("test_getChecked")) test_getChecked();
	else if (getName().equals("test_getFont")) test_getFont();
	else if (getName().equals("test_getFontI")) test_getFontI();
	else if (getName().equals("test_getForeground")) test_getForeground();
	else if (getName().equals("test_getForegroundI")) test_getForegroundI();
	else if (getName().equals("test_getGrayed")) test_getGrayed();
	else if (getName().equals("test_getImageBoundsI")) test_getImageBoundsI();
	else if (getName().equals("test_getImageI")) test_getImageI();
	else if (getName().equals("test_getImageIndent")) test_getImageIndent();
	else if (getName().equals("test_getParent")) test_getParent();
	else if (getName().equals("test_getTextI")) test_getTextI();
	else if (getName().equals("test_setBackgroundILorg_eclipse_swt_graphics_Color")) test_setBackgroundILorg_eclipse_swt_graphics_Color();
	else if (getName().equals("test_setBackgroundLorg_eclipse_swt_graphics_Color")) test_setBackgroundLorg_eclipse_swt_graphics_Color();
	else if (getName().equals("test_setCheckedZ")) test_setCheckedZ();
	else if (getName().equals("test_setFontILorg_eclipse_swt_graphics_Font")) test_setFontILorg_eclipse_swt_graphics_Font();
	else if (getName().equals("test_setFontLorg_eclipse_swt_graphics_Font")) test_setFontLorg_eclipse_swt_graphics_Font();
	else if (getName().equals("test_setForegroundILorg_eclipse_swt_graphics_Color")) test_setForegroundILorg_eclipse_swt_graphics_Color();
	else if (getName().equals("test_setForegroundLorg_eclipse_swt_graphics_Color")) test_setForegroundLorg_eclipse_swt_graphics_Color();
	else if (getName().equals("test_setGrayedZ")) test_setGrayedZ();
	else if (getName().equals("test_setImage$Lorg_eclipse_swt_graphics_Image")) test_setImage$Lorg_eclipse_swt_graphics_Image();
	else if (getName().equals("test_setImageILorg_eclipse_swt_graphics_Image")) test_setImageILorg_eclipse_swt_graphics_Image();
	else if (getName().equals("test_setImageIndentI")) test_setImageIndentI();
	else if (getName().equals("test_setImageLorg_eclipse_swt_graphics_Image")) test_setImageLorg_eclipse_swt_graphics_Image();
	else if (getName().equals("test_setText$Ljava_lang_String")) test_setText$Ljava_lang_String();
	else if (getName().equals("test_setTextILjava_lang_String")) test_setTextILjava_lang_String();
	else if (getName().equals("test_setTextLjava_lang_String")) test_setTextLjava_lang_String();
	else super.runTest();
}

/* custom */
Table table;
TableItem tableItem;

// this method must be private or protected so the auto-gen tool keeps it
private void makeCleanEnvironment() {
	if ( tableItem != null ) tableItem.dispose();
	if ( table != null ) table.dispose();
	table = new Table(shell, 0);
	tableItem = new TableItem(table, 0);
	setWidget(tableItem);
}
}
