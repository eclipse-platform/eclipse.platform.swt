/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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
import org.eclipse.swt.widgets.FileDialog;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.FileDialog
 *
 * @see org.eclipse.swt.widgets.FileDialog
 */
public class Test_org_eclipse_swt_widgets_FileDialog extends Test_org_eclipse_swt_widgets_Dialog {

@Override
@Before
public void setUp() {
	super.setUp();
	fileDialog = new FileDialog(shell, SWT.NULL);
	setDialog(fileDialog);
}

@Test
public void test_ConstructorLorg_eclipse_swt_widgets_Shell() {
	// Test FileDialog(Shell)
	new FileDialog(shell);
	try {
		new FileDialog(null);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}
}

@Test
public void test_ConstructorLorg_eclipse_swt_widgets_ShellI() {
	// Test FileDialog(Shell, int)
	FileDialog fd;
	fd = new FileDialog(shell, SWT.NULL);
	int style = fd.getStyle();
	style &= ~SWT.LEFT_TO_RIGHT;
	style &= ~SWT.RIGHT_TO_LEFT;
	assertEquals(SWT.APPLICATION_MODAL, style);

	fd = new FileDialog(shell, SWT.APPLICATION_MODAL);
	style = fd.getStyle();
	style &= ~SWT.LEFT_TO_RIGHT;
	style &= ~SWT.RIGHT_TO_LEFT;
	assertEquals(SWT.APPLICATION_MODAL, style);

	fd = new FileDialog(shell, SWT.PRIMARY_MODAL);
	style = fd.getStyle();
	style &= ~SWT.LEFT_TO_RIGHT;
	style &= ~SWT.RIGHT_TO_LEFT;
	assertEquals(SWT.PRIMARY_MODAL, style);

	fd = new FileDialog(shell, SWT.SYSTEM_MODAL);
	style = fd.getStyle();
	style &= ~SWT.LEFT_TO_RIGHT;
	style &= ~SWT.RIGHT_TO_LEFT;
	assertEquals(SWT.SYSTEM_MODAL, style);
}

@Test
public void test_getFileNames() {
	String[] names = fileDialog.getFileNames();
	assertEquals(0, names.length);
}

@Test
public void test_open() {
	if (SwtTestUtil.fTestDialogOpen)
		fileDialog.open();
}

@Test
public void test_setFileNameLjava_lang_String() {
	fileDialog.setFileName("");
	String name = fileDialog.getFileName();
	assertTrue(name.isEmpty());
	fileDialog.setFileName(null);
	name = fileDialog.getFileName();
	assertNull(name);
	fileDialog.setFileName("somefile.test");
	name = fileDialog.getFileName();
	assertEquals("somefile.test", name);
}

@Test
public void test_setFilterExtensions$Ljava_lang_String() {
	fileDialog.setFilterExtensions(new String[] {"txt","java"});
	String filters[] = fileDialog.getFilterExtensions();
	assertEquals(2, filters.length);
	assertEquals("txt", filters[0]);
	assertEquals("java", filters[1]);
	fileDialog.setFilterExtensions(new String[] {""});
	filters = fileDialog.getFilterExtensions();
	assertEquals(1, filters.length);
	fileDialog.setFilterExtensions(null);
	filters = fileDialog.getFilterExtensions();
	assertNull(filters);
}

@Test
public void test_setFilterNames$Ljava_lang_String() {
	fileDialog.setFilterNames(new String[] {"a.txt","b.java"});
	String filters[] = fileDialog.getFilterNames();
	assertEquals(2, filters.length);
	assertEquals("a.txt", filters[0]);
	assertEquals("b.java", filters[1]);
	fileDialog.setFilterNames(new String[] {""});
	filters = fileDialog.getFilterNames();
	assertEquals(1, filters.length);
	fileDialog.setFilterNames(null);
	filters = fileDialog.getFilterNames();
	assertNull(filters);
}

@Test
public void test_setFilterPathLjava_lang_String() {
	assertEquals(":1:", "", fileDialog.getFilterPath());
	String testStr = "./*";
	fileDialog.setFilterPath(testStr);
	assertEquals(":2:", testStr, fileDialog.getFilterPath());
	fileDialog.setFilterPath("");
	assertTrue(":3:", fileDialog.getFilterPath().isEmpty());
	fileDialog.setFilterPath(null);
	assertNull(":4:", fileDialog.getFilterPath());
}
/* custom */
FileDialog fileDialog;
}
