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


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.custom.CTabFolder
 *
 * @see org.eclipse.swt.custom.CTabFolder
 */
public class Test_org_eclipse_swt_custom_CTabFolder extends Test_org_eclipse_swt_widgets_Composite {

@Override
@Before
public void setUp() {
	super.setUp();
	makeCleanEnvironment();
}

@Override
@Test
public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
}

@Override
@Test
public void test_computeSizeIIZ() {
}

@Override
@Test
public void test_computeTrimIIII() {
}

@Override
@Test
public void test_getClientArea() {
}

@Override
@Test
public void test_setBackgroundLorg_eclipse_swt_graphics_Color() {
}

@Override
@Test
public void test_setFontLorg_eclipse_swt_graphics_Font() {
}

/* custom */
protected CTabFolder ctabFolder;

private void makeCleanEnvironment() {
// this method must be private or protected so the auto-gen tool keeps it
	ctabFolder = new CTabFolder(shell, 0);
	setWidget(ctabFolder);
}

private void createTabFolder(List<String> events) {
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

@Test
public void test_consistency_KeySelection() {
    List<String> events = new ArrayList<>();
    createTabFolder(events);
    consistencyEvent(0, SWT.ARROW_RIGHT, 0, 0, ConsistencyUtility.KEY_PRESS, events, false);
}

@Test
public void test_consistency_MouseSelection() {
    List<String> events = new ArrayList<>();
    createTabFolder(events);
    consistencyPrePackShell();
    consistencyEvent(ctabFolder.getSize().x/2, 5, 1, 0, ConsistencyUtility.MOUSE_CLICK, events);
}

@Test
public void test_consistency_PgdwnSelection () {
    List<String> events = new ArrayList<>();
    createTabFolder(events);
    consistencyEvent(0, SWT.CTRL, 0, SWT.PAGE_DOWN, ConsistencyUtility.DOUBLE_KEY_PRESS, events, false);
}

@Test
public void test_consistency_PgupSelection () {
    List<String> events = new ArrayList<>();
    createTabFolder(events);
    ctabFolder.setSelection(2);
    consistencyEvent(0, SWT.CTRL, 0, SWT.PAGE_UP, ConsistencyUtility.DOUBLE_KEY_PRESS, events, false);
}

@Test
public void test_consistency_MenuDetect () {
    List<String> events = new ArrayList<>();
    createTabFolder(events);
    ctabFolder.setSelection(1);
    consistencyEvent(50, 5, 3, 0, ConsistencyUtility.MOUSE_CLICK, events);
}

@Test
public void test_consistency_DragDetect () {
    List<String> events = new ArrayList<>();
    createTabFolder(events);
    ctabFolder.setSelection(1);
    consistencyEvent(50, 5, 70, 10, ConsistencyUtility.MOUSE_DRAG, events);
}

@Test
public void test_setHighlightEnabled () {
	assertTrue("By default, highlighting should be enabled", ctabFolder.getHighlightEnabled());
	ctabFolder.setHighlightEnabled(false);
	assertFalse(ctabFolder.getHighlightEnabled());
	ctabFolder.setHighlightEnabled(true);
	assertTrue(ctabFolder.getHighlightEnabled());
}

@Ignore("Currently failing due to Bug 507611. E.g: Height is 50 instead of being at least 59")
@Test
public void test_checkSize() {
	shell.setLayout(new GridLayout(1, false));
	Text text2 = new Text(shell, SWT.SINGLE | SWT.LEAD | SWT.BORDER);
	text2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
	text2.setText("This control takes the initial focus.");

	Color red = shell.getDisplay().getSystemColor(SWT.COLOR_RED);
	Image systemImage = shell.getDisplay().getSystemImage(SWT.ICON_INFORMATION);

	CTabFolder folder = new CTabFolder(shell, SWT.BORDER);
	folder.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
	folder.setSelectionBackground(red);
	for (int i = 0; i < 3; i++) {
		CTabItem item = new CTabItem(folder, SWT.CLOSE);
		item.setText("Item " + i);
		Text text = new Text(folder, SWT.MULTI);
		text.setText("Content for Item " + i);
		item.setImage(systemImage);
		item.setControl(text);
	}
	folder.setSelection(0);
	shell.pack();
	shell.open();
	int folderY = folder.getSize().y;
	int expectedminHeight = systemImage.getImageData().height + text2.getFont().getFontData()[0].getHeight();
	assertTrue("\nBug 507611 - CTabFolder is too thin for its actual content. \nCtabFolder height:"+folderY+"\nExpected min:"+expectedminHeight,  folderY > expectedminHeight);
}

/**
 * Test for bug 528251.
 *
 * We define two {@link CTabFolder tab folders}, of which one has a nested tab folder.
 * We validate that selecting the nested tab does not break selection highlight for the top-level tabs.
 *
 * @see Bug528251_CTabFolder_nested_highlighting
 */
@Test
public void test_nestedTabHighlighting () {
	CTabFolder partStackTabFolder = new CTabFolder(shell, SWT.NONE);

	CTabItem consoleViewTab = new CTabItem(partStackTabFolder, SWT.NONE);
	consoleViewTab.setText("Console View");

	SashForm anotherView = new SashForm(partStackTabFolder, SWT.NONE);
	CTabItem anotherViewTab = new CTabItem(partStackTabFolder, SWT.NONE);
	anotherViewTab.setText("Other View");
	anotherViewTab.setControl(anotherView);
	CTabFolder anotherViewNestedTabFolder = new CTabFolder(anotherView, SWT.NONE);
	CTabItem anotherViewNestedTab = new CTabItem(anotherViewNestedTabFolder, SWT.NONE);
	anotherViewNestedTab.setText("nested tab");

	shell.pack();
	shell.open();

	processEvents();

	// nothing is selected, expect no highlight
	boolean shouldHighlightConsoleViewTab = reflection_shouldHighlight(partStackTabFolder);
	assertFalse("expected CTabFolder to not need highlighting without any selection",
			shouldHighlightConsoleViewTab);

	// "click" on the Console View tab
	partStackTabFolder.notifyListeners(SWT.Activate, new Event());
	partStackTabFolder.setSelection(consoleViewTab);
	// "click" on the Other View tab, per default the first sub-tab is also highlighted
	partStackTabFolder.setSelection(anotherViewTab);
	anotherViewNestedTabFolder.notifyListeners(SWT.Activate, new Event());
	// "click" on the nested tab
	anotherViewNestedTabFolder.setSelection(anotherViewNestedTab);
	partStackTabFolder.setSelection(consoleViewTab);
	// "click" on the Console View tab, this hides and deactivates the nested CTabFolder
	anotherViewNestedTabFolder.notifyListeners(SWT.Deactivate, new Event());
	processEvents();

	// the Console View tab is selected, so it should still be highlighted
	shouldHighlightConsoleViewTab = reflection_shouldHighlight(partStackTabFolder);
	assertTrue("Bug 528251 - View tab not highlighted due to another view with a CTabFolder",
			shouldHighlightConsoleViewTab);
}

private void processEvents() {
	Display display = shell.getDisplay();

	while (display.readAndDispatch()) {
		//
	}
}

private static boolean reflection_shouldHighlight(CTabFolder partStackTabs) {
	String shouldHighlightMethodName = "shouldHighlight";
	Class<?> cTabFolderClass = CTabFolder.class;

	boolean shouldHighlightConsoleViewTab = false;
	try {
		Method method = cTabFolderClass.getDeclaredMethod(shouldHighlightMethodName);
		method.setAccessible(true);
		Object result = method.invoke(partStackTabs);
		Boolean shouldHighlight = (Boolean) result;
		shouldHighlightConsoleViewTab = shouldHighlight.booleanValue();
	} catch (Throwable t) {
		String message = "reflection call to " + cTabFolderClass.getName() + "." + shouldHighlightMethodName + "() failed";
		throw new AssertionError(message, t);
	}

	return shouldHighlightConsoleViewTab;
}

}
