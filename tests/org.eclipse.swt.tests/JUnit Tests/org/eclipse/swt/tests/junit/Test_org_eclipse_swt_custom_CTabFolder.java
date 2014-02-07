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


import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Text;

/**
 * Automated Test Suite for class org.eclipse.swt.custom.CTabFolder
 *
 * @see org.eclipse.swt.custom.CTabFolder
 */
public class Test_org_eclipse_swt_custom_CTabFolder extends Test_org_eclipse_swt_widgets_Composite {

public Test_org_eclipse_swt_custom_CTabFolder(String name) {
	super(name);
}

@Override
protected void setUp() {
	super.setUp();
	makeCleanEnvironment();
}

@Override
public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
}

@Override
public void test_computeSizeIIZ() {
}

@Override
public void test_computeTrimIIII() {
}

@Override
public void test_getClientArea() {
}

@Override
public void test_setBackgroundLorg_eclipse_swt_graphics_Color() {
}

@Override
public void test_setFontLorg_eclipse_swt_graphics_Font() {
}

/* custom */
protected CTabFolder ctabFolder;

private void makeCleanEnvironment() {
// this method must be private or protected so the auto-gen tool keeps it
	ctabFolder = new CTabFolder(shell, 0);
	setWidget(ctabFolder);
}

private void createTabFolder(Vector<String> events) {
	makeCleanEnvironment();
	for (int i = 0; i < 3; i++) {
		CTabItem item = new CTabItem(ctabFolder, SWT.NONE);
		item.setText("CTabItem &" + i);
		item.setToolTipText("CTabItem ToolTip" + i);
		Text itemText = new Text(ctabFolder, SWT.MULTI | SWT.BORDER);
		itemText.setText("\nText for CTabItem " + i + "\n\n\n");
		item.setControl(itemText);
		hookExpectedEvents(item, getTestName(), events);
		hookExpectedEvents(itemText, getTestName(), events);
	}
	ctabFolder.setSelection(ctabFolder.getItem(0));
}

public void test_consistency_KeySelection() {
    Vector<String> events = new Vector<String>();
    createTabFolder(events);
    consistencyEvent(0, SWT.ARROW_RIGHT, 0, 0, ConsistencyUtility.KEY_PRESS, events, false);
}

public void test_consistency_MouseSelection() {
    Vector<String> events = new Vector<String>();
    createTabFolder(events);
    consistencyPrePackShell();
    consistencyEvent(ctabFolder.getSize().x/2, 5, 1, 0, ConsistencyUtility.MOUSE_CLICK, events);
}

public void test_consistency_PgdwnSelection () {
    Vector<String> events = new Vector<String>();
    createTabFolder(events);
    consistencyEvent(0, SWT.CTRL, 0, SWT.PAGE_DOWN, ConsistencyUtility.DOUBLE_KEY_PRESS, events, false);
}

public void test_consistency_PgupSelection () {
    Vector<String> events = new Vector<String>();
    createTabFolder(events);
    ctabFolder.setSelection(2);
    consistencyEvent(0, SWT.CTRL, 0, SWT.PAGE_UP, ConsistencyUtility.DOUBLE_KEY_PRESS, events, false);
}

public void test_consistency_MenuDetect () {
    Vector<String> events = new Vector<String>();
    createTabFolder(events);
    ctabFolder.setSelection(1);
    consistencyEvent(50, 5, 3, 0, ConsistencyUtility.MOUSE_CLICK, events);
}

public void test_consistency_DragDetect () {
    Vector<String> events = new Vector<String>();
    createTabFolder(events);
    ctabFolder.setSelection(1);
    consistencyEvent(50, 5, 70, 10, ConsistencyUtility.MOUSE_DRAG, events);
}

}
