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

import junit.framework.*;
import junit.textui.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;


/**
 * Automated Test Suite for class org.eclipse.swt.widgets.CoolBar
 *
 * @see org.eclipse.swt.widgets.CoolBar
 */
public class Test_org_eclipse_swt_widgets_CoolBar extends Test_org_eclipse_swt_widgets_Composite {

public Test_org_eclipse_swt_widgets_CoolBar(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	super.setUp();
	coolBar = new CoolBar(shell, 0);
	setWidget(coolBar);
}

public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_widgets_CompositeI not written");
}

public void test_computeSizeIIZ() {
	warnUnimpl( "Test test_computeSizeIIZ not written");
}

public void test_getItemCount() {
	warnUnimpl("Test test_getItemCount not written");
}

public void test_getItemI() {
	warnUnimpl("Test test_getItemI not written");
}

public void test_getItemOrder() {
	warnUnimpl("Test test_getItemOrder not written");
}

public void test_getItemSizes() {
	warnUnimpl("Test test_getItemSizes not written");
}

public void test_getItems() {
	warnUnimpl("Test test_getItems not written");
}

public void test_getLocked() {
	warnUnimpl("Test test_getLocked not written");
}

public void test_getWrapIndices() {
	warnUnimpl("Test test_getWrapIndices not written");
}

public void test_indexOfLorg_eclipse_swt_widgets_CoolItem() {
	warnUnimpl("Test test_indexOfLorg_eclipse_swt_widgets_CoolItem not written");
}

public void test_setItemLayout$I$I$Lorg_eclipse_swt_graphics_Point() {
	warnUnimpl("Test test_setItemLayout$I$I$Lorg_eclipse_swt_graphics_Point not written");
}

public void test_setLockedZ() {
	warnUnimpl("Test test_setLockedZ not written");
}

public void test_setWrapIndices$I() {
	warnUnimpl("Test test_setWrapIndices$I not written");
}


public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_widgets_CoolBar((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_CompositeI");
	methodNames.addElement("test_computeSizeIIZ");
	methodNames.addElement("test_getItemCount");
	methodNames.addElement("test_getItemI");
	methodNames.addElement("test_getItemOrder");
	methodNames.addElement("test_getItemSizes");
	methodNames.addElement("test_getItems");
	methodNames.addElement("test_getLocked");
	methodNames.addElement("test_getWrapIndices");
	methodNames.addElement("test_indexOfLorg_eclipse_swt_widgets_CoolItem");
	methodNames.addElement("test_setItemLayout$I$I$Lorg_eclipse_swt_graphics_Point");
	methodNames.addElement("test_setLockedZ");
	methodNames.addElement("test_setWrapIndices$I");
	methodNames.addElement("test_consistency_ChevronDragDetect");
	methodNames.addElement("test_consistency_ChevronMenuDetect");
	methodNames.addElement("test_consistency_ChevronMouseSelection");
	methodNames.addElement("test_consistency_DragDetect");
	methodNames.addElement("test_consistency_MenuDetect");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Composite.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_CompositeI")) test_ConstructorLorg_eclipse_swt_widgets_CompositeI();
	else if (getName().equals("test_computeSizeIIZ")) test_computeSizeIIZ();
	else if (getName().equals("test_getItemCount")) test_getItemCount();
	else if (getName().equals("test_getItemI")) test_getItemI();
	else if (getName().equals("test_getItemOrder")) test_getItemOrder();
	else if (getName().equals("test_getItemSizes")) test_getItemSizes();
	else if (getName().equals("test_getItems")) test_getItems();
	else if (getName().equals("test_getLocked")) test_getLocked();
	else if (getName().equals("test_getWrapIndices")) test_getWrapIndices();
	else if (getName().equals("test_indexOfLorg_eclipse_swt_widgets_CoolItem")) test_indexOfLorg_eclipse_swt_widgets_CoolItem();
	else if (getName().equals("test_setItemLayout$I$I$Lorg_eclipse_swt_graphics_Point")) test_setItemLayout$I$I$Lorg_eclipse_swt_graphics_Point();
	else if (getName().equals("test_setLockedZ")) test_setLockedZ();
	else if (getName().equals("test_setWrapIndices$I")) test_setWrapIndices$I();
	else if (getName().equals("test_consistency_DragDetect")) test_consistency_DragDetect();
	else if (getName().equals("test_consistency_MenuDetect")) test_consistency_MenuDetect();
	else if (getName().equals("test_consistency_ChevronDragDetect")) test_consistency_ChevronDragDetect();
	else if (getName().equals("test_consistency_ChevronMenuDetect")) test_consistency_ChevronMenuDetect();
	else if (getName().equals("test_consistency_ChevronMouseSelection")) test_consistency_ChevronMouseSelection();
	else super.runTest();
}

/* Custom */

CoolBar coolBar;


private void createCoolBar(Vector events) {
    tearDown();
    super.setUp();
    String test = getTestName();
    coolBar = new CoolBar(shell, SWT.FLAT);
	ToolBar[] coolItemToolBar = new ToolBar[2];
	for (int i = 0; i < 2; i++) {
		CoolItem coolItem = new CoolItem(coolBar, SWT.DROP_DOWN);
		coolItemToolBar[i] = new ToolBar(coolBar, SWT.FLAT);
		hookExpectedEvents(coolItem, test, events);
		hookExpectedEvents(coolItemToolBar[i], test, events);
		int toolItemWidth = 0;
		for (int j = 0; j < 2; j++) {
			ToolItem item = new ToolItem(coolItemToolBar[i], SWT.CHECK);
			item.setText("CB" + ((i*2) + j));
			item.setToolTipText("ToolItem ToolTip" + i + j);
			if (item.getWidth() > toolItemWidth)
			    toolItemWidth = item.getWidth();
			hookExpectedEvents(item, test, events);
		}
        coolItem.setControl(coolItemToolBar[i]);
        Point size;
        if(i == 1)
            size = coolItemToolBar[i].computeSize(20, SWT.DEFAULT);
        else
            size = coolItemToolBar[i].computeSize(SWT.DEFAULT, SWT.DEFAULT);
        Point coolSize = coolItem.computeSize (size.x, size.y);
        coolItem.setMinimumSize(toolItemWidth/3, coolSize.y);
        coolItem.setPreferredSize(coolSize);
        coolItem.setSize(coolSize.x/3, coolSize.y);
        coolItem.addSelectionListener(new CoolItemSelectionListener());
	}
	setWidget(coolBar);
}

public void test_consistency_ChevronMenuDetect () {
    Vector events = new Vector();
    createCoolBar(events);
    consistencyPrePackShell();
    Point[] points = coolBar.getItemSizes();
    //chevron
    consistencyEvent(points[0].x-12, 0, 3, ConsistencyUtility.ESCAPE_MENU, ConsistencyUtility.MOUSE_CLICK, events);
}

public void test_consistency_MenuDetect () {
    Vector events = new Vector();
    createCoolBar(events);
    consistencyPrePackShell();
    Point[] points = coolBar.getItemSizes();
    consistencyEvent(points[0].x, 2, 3, 0, ConsistencyUtility.MOUSE_CLICK, events);
}

public void test_consistency_ChevronDragDetect () {
    Vector events = new Vector();
    createCoolBar(events);
    consistencyPrePackShell();
    Point[] points = coolBar.getItemSizes();
    consistencyEvent(points[0].x-12, 0, points[0].x-12, 5, ConsistencyUtility.MOUSE_DRAG, events);
}

public void test_consistency_DragDetect () {
    Vector events = new Vector();
    createCoolBar(events);
    consistencyPrePackShell();
    Point[] points = coolBar.getItemSizes();
    consistencyEvent(points[0].x, 0, points[0].x, 5, ConsistencyUtility.MOUSE_DRAG, events);
}

public void test_consistency_ChevronMouseSelection() {
    Vector events = new Vector();
    createCoolBar(events);
    consistencyPrePackShell();
    Point[] points = coolBar.getItemSizes();
    consistencyEvent(points[0].x-12, 0, points[0].x-8, 30, ConsistencyUtility.SELECTION, events);
}

class CoolItemSelectionListener extends SelectionAdapter {
	private Menu menu = null;
	
	public void widgetSelected(SelectionEvent event) {
		/**
		 * A selection event will be fired when the cool item
		 * is selected by its gripper or if the drop down arrow
		 * (or 'chevron') is selected. Examine the event detail
		 * to determine where the widget was selected.
		 */
		if (event.detail == SWT.ARROW) {
			/* If the popup menu is already up (i.e. user pressed arrow twice),
			 * then dispose it.
			 */
			if (menu != null) {
				menu.dispose();
				menu = null;
				return;
			}
			
			/* Get the cool item and convert its bounds to display coordinates. */
			CoolItem coolItem = (CoolItem) event.widget;
			Rectangle itemBounds = coolItem.getBounds ();
			itemBounds.width = event.x - itemBounds.x;
			Point pt = coolBar.toDisplay(new Point (itemBounds.x, itemBounds.y));
			itemBounds.x = pt.x;
			itemBounds.y = pt.y;
			
			/* Get the toolbar from the cool item. */
			ToolBar toolBar = (ToolBar) coolItem.getControl ();
			ToolItem[] tools = toolBar.getItems ();
			int toolCount = tools.length;
							
			/* Convert the bounds of each tool item to display coordinates,
			 * and determine which ones are past the bounds of the cool item.
			 */
			int i = 0;
			while (i < toolCount) {
				Rectangle toolBounds = tools[i].getBounds ();
				pt = toolBar.toDisplay(new Point(toolBounds.x, toolBounds.y));
				toolBounds.x = pt.x;
				toolBounds.y = pt.y;
		  		Rectangle intersection = itemBounds.intersection (toolBounds);
		  		if (!intersection.equals (toolBounds)) break;
		  		i++;
			}
			
			/* Create a pop-up menu with items for each of the hidden buttons. */
			menu = new Menu (coolBar);
			for (int j = i; j < toolCount; j++) {
				ToolItem tool = tools[j];
				String text = tool.getText();
				MenuItem menuItem = new MenuItem (menu, SWT.NONE);
				menuItem.setText(text);
			}
			
			/* Display the pop-up menu at the lower left corner of the arrow button.
			 * Dispose the menu when the user is done with it.
			 */
			pt = coolBar.toDisplay(new Point(event.x, event.y));
			menu.setLocation (pt.x, pt.y);
			menu.setVisible (true);
			Display display = coolBar.getDisplay ();
			while (menu != null && !menu.isDisposed() && menu.isVisible ()) {
				if (!display.readAndDispatch ()) display.sleep ();
			}
			if (menu != null) {
				menu.dispose ();
				menu = null;
			}
		}
	}
}


}
