/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Widget;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Item
 *
 * @see org.eclipse.swt.widgets.Item
 */
public class Test_org_eclipse_swt_widgets_Item extends Test_org_eclipse_swt_widgets_Widget {

public Test_org_eclipse_swt_widgets_Item(String name) {
	super(name);
}

@Override
protected void setUp() {
	super.setUp();
	loadImages();
}

@Override
protected void tearDown() {
	super.tearDown();
	freeImages();
}

public void test_setImageLorg_eclipse_swt_graphics_Image() {
	assertNull(item.getImage());
	item.setImage(images[0]);
	assertEquals(images[0], item.getImage());
	assertTrue(item.getImage() != images[1]);
	item.setImage(null);
	assertNull(item.getImage());
}

public void test_setTextLjava_lang_String() {
	String testStr = "test string";
	item.setText(testStr);
	assertTrue("a", item.getText().equals(testStr));
	item.setText("");
	assertTrue("b", item.getText().equals(""));
	try {
		item.setText(null);
		fail("No exception thrown for string == null");
	}
	catch (IllegalArgumentException e) {
	}
}

/* custom */
Item item;
Image[] images = new Image [SwtTestUtil.imageFormats.length*SwtTestUtil.imageFilenames.length];

private void loadImages() {
	int numFormats = SwtTestUtil.imageFormats.length;
	int numFiles = SwtTestUtil.imageFilenames.length;
	for (int i=0; i<numFormats; i++) {
		String format = SwtTestUtil.imageFormats[i];
		int index = i*numFiles;
		for (int j=0; j<numFiles; j++){
			String fileName = SwtTestUtil.imageFilenames[j];
			InputStream  resource = this.getClass().getResourceAsStream(fileName + "." + format);
			images [index+j] = new Image (shell.getDisplay(), resource);
			try {
				resource.close();
			} catch (IOException e) {
				// continue;
			}
		}
	}
}
@Override
protected void setWidget(Widget widget) {
	item = (Item) widget;
	super.setWidget(widget);
}

private void freeImages() {
	for (int i=0; i<images.length; i++) {
		if (images[i] != null)
			images[i].dispose();
	}
}
}
