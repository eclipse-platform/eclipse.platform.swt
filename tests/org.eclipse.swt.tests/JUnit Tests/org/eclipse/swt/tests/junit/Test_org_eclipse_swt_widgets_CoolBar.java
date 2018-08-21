/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.junit.Before;
import org.junit.Test;


/**
 * Automated Test Suite for class org.eclipse.swt.widgets.CoolBar
 *
 * @see org.eclipse.swt.widgets.CoolBar
 */
public class Test_org_eclipse_swt_widgets_CoolBar extends Test_org_eclipse_swt_widgets_Composite {

@Override
@Before
public void setUp() {
	super.setUp();
	coolBar = new CoolBar(shell, 0);
	setWidget(coolBar);
}

@Override
@Test
public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
}

@Override
@Test
public void test_computeSizeIIZ() {
}

/* Custom */
CoolBar coolBar;

private void createCoolBar(List<String> events) {
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

@Test
public void test_consistency_ChevronMenuDetect () {
    List<String> events = new ArrayList<>();
    createCoolBar(events);
    consistencyPrePackShell();
    Point[] points = coolBar.getItemSizes();
    //chevron
    consistencyEvent(points[0].x-12, 0, 3, ConsistencyUtility.ESCAPE_MENU, ConsistencyUtility.MOUSE_CLICK, events);
}

@Test
public void test_consistency_MenuDetect () {
    List<String> events = new ArrayList<>();
    createCoolBar(events);
    consistencyPrePackShell();
    Point[] points = coolBar.getItemSizes();
    consistencyEvent(points[0].x, 2, 3, 0, ConsistencyUtility.MOUSE_CLICK, events);
}

@Test
public void test_consistency_ChevronDragDetect () {
    List<String> events = new ArrayList<>();
    createCoolBar(events);
    consistencyPrePackShell();
    Point[] points = coolBar.getItemSizes();
    consistencyEvent(points[0].x-12, 0, points[0].x-12, 5, ConsistencyUtility.MOUSE_DRAG, events);
}

@Test
public void test_consistency_DragDetect () {
    List<String> events = new ArrayList<>();
    createCoolBar(events);
    consistencyPrePackShell();
    Point[] points = coolBar.getItemSizes();
    consistencyEvent(points[0].x, 0, points[0].x, 5, ConsistencyUtility.MOUSE_DRAG, events);
}

@Test
public void test_consistency_ChevronMouseSelection() {
    List<String> events = new ArrayList<>();
    createCoolBar(events);
    consistencyPrePackShell();
    Point[] points = coolBar.getItemSizes();
    consistencyEvent(points[0].x-12, 0, points[0].x-8, 30, ConsistencyUtility.SELECTION, events);
}

class CoolItemSelectionListener extends SelectionAdapter {
	private Menu menu = null;

	@Override
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
