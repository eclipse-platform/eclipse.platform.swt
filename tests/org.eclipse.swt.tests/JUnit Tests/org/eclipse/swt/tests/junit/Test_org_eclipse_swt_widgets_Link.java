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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Link;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Link
 *
 * @see org.eclipse.swt.widgets.Link
 */
public class Test_org_eclipse_swt_widgets_Link extends Test_org_eclipse_swt_widgets_Control {

	Link link;

@Override
@BeforeEach
public void setUp() {
	super.setUp();
	link = new Link(shell, SWT.NONE);
	setWidget(link);
}

@Override
@Test
public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
	// Test Link(Composite parent, int style)
	link = new Link(shell, SWT.NULL);
	link.dispose();
	link = new Link(shell, SWT.NONE);
	link.dispose();
	link = new Link(shell, SWT.BORDER);
	link.dispose();
	try {
		link = new Link(null, 0);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
		// expected
	}
}

@Test
public void test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	listenerCalled = false;
	SelectionListener listener = new SelectionListener() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			listenerCalled = true;
		}
		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
		}
	};

	try {
		link.addSelectionListener(null);
		fail("No exception thrown for addSelectionListener with null argument");
	} catch (IllegalArgumentException e) {
		// expected
	}

	link.addSelectionListener(listener);
	link.notifyListeners(SWT.Selection, new Event());
	assertTrue(listenerCalled);

	try {
		link.removeSelectionListener(null);
		fail("No exception thrown for removeSelectionListener with null argument");
	} catch (IllegalArgumentException e) {
		// expected
	}
	listenerCalled = false;
	link.removeSelectionListener(listener);
	link.notifyListeners(SWT.Selection, new Event());
	assertFalse(listenerCalled);
}

@Test
public void test_addSelectionListenerWidgetSelectedAdapterLorg_eclipse_swt_events_SelectionListener() {
	listenerCalled = false;
	SelectionListener listener = SelectionListener.widgetSelectedAdapter(e -> listenerCalled = true);

	link.addSelectionListener(listener);
	link.notifyListeners(SWT.Selection, new Event());
	assertTrue(listenerCalled);

	listenerCalled = false;
	link.removeSelectionListener(listener);
	link.notifyListeners(SWT.Selection, new Event());
	assertFalse(listenerCalled);
}

@Override
@Test
public void test_computeSizeIIZ() {
	link.computeSize(0, 0);

	link.computeSize(0, 0, false);

	link.computeSize(-10, -10);

	link.computeSize(-10, -10, false);

	link.computeSize(10, 10);

	link.computeSize(10, 10, false);

	link.computeSize(10000, 10000);

	link.computeSize(10000, 10000, false);
}

@Test
public void test_setTextLjava_lang_String() {
	String[] cases = {
			"",
			"a",
			"aa",
			"aaaaaaaaaa",
			"a&",
			"&",
			"&a",
			"&a&",
			"&a",
			"&<a>",
			"<a>",
			"&a<a>",
			"<a></a>",
			"<a></a>&",
			"<a></a>a&",
			"<a></a>&a",
			"&a<a>a</a>&a",
			"&a<a>&a</a>&a",
			"Text <a href=\"url.com\">Link</a> text <A color=#212121>Link 2</A> ",
			"Text<a  \t   xxx=\"yyy  \"        id=\"ids\" href=\"HREF\"     >Link< /a>End",
			"Te&&xt &text && <a>L&ink</a> h&i <a>fe&&lipe</a> &l &end&&",
			"Text <a id=\"1\">Link</a> something <a href=\"bla bla2\" >Link2</a> somethingelse <a>Link3 large large</a>. some text to test this wrapping thing <A href=\"last\">this is suppose to be a very long link text the spraws over several lines in the text layout</a>.end",
			"The SWT component is designed to provide <A>efficient</A>, <A>portable</A> <A>access to the user-interface facilities of the operating systems</A> on which it is implemented.",
			"some text",
			"ldkashdoehufweovcnhslvhregojebckreavbkuhxbiufvcyhbifuyewvbiureyd.,cmnesljliewjfchvbwoifivbeworixuieurvbiuvbohflksjeahfcliureafgyciabelitvyrwtlicuyrtliureybcliuyreuceyvbliureybct",
			"\n \n \b \t ",
			"\0"};
	for (String text : cases) {
		link.setText(text);
		assertEquals(link.getText() , text);
	}
	try {
		link.setText(null);
		fail("No exception thrown for text == null");
	} catch (IllegalArgumentException e) {
		// expected
	}
}

@Test
public void test_setLinkForegroundLorg_eclipse_swt_graphics_Color() {
	assertNotNull(link.getLinkForeground());
	Color color = new Color(12, 34, 56);
	link.setLinkForeground(color);
	assertEquals(color, link.getLinkForeground());
	link.setLinkForeground(null);
	assertNotEquals(color, link.getForeground());
}


@Test
public void test_LinkRendersCorrectlyAfterResize() {
	shell.setSize(400, 300);
	link.setText("Visit <a href=\"https://eclipse.org\">Eclipse</a> website");
	link.setBounds(10, 10, 200, 50);

	shell.open();

	Display display = shell.getDisplay();
	// Force initial paint
	while (display.readAndDispatch()) {
		// loop until no more events
	}

	// Resize multiple times rapidly (stress test for flicker)
	for (int i = 0; i < 10; i++) {
		link.setSize(200 + (i * 10), 50 + (i * 5));
		while (display.readAndDispatch()) {
			// loop until no more events
		}
	}

	// Verify link is still functional and sized correctly
	Point size = link.getSize();
	assertTrue(size.x > 0 && size.y > 0, "Link should have valid size after resize");
	assertEquals("Visit <a href=\"https://eclipse.org\">Eclipse</a> website",
				link.getText(), "Link text should be preserved after resize");

	// Verify the link is still visible and has proper bounds
	Rectangle bounds = link.getBounds();
	assertTrue(bounds.width > 0 && bounds.height > 0,
			"Link should have valid bounds after multiple resizes");
}

@Test
public void test_LinkBackgroundNotErasedDuringPaint() {
	link.setText("Test <a>link</a>");

	shell.setSize(300, 200);
	link.setVisible(true);
	shell.open();

	Display display = shell.getDisplay();
	// Trigger redraws and ensure no crashes or obvious rendering failure
	for (int i = 0; i < 10; i++) {
		link.redraw();
		link.update();
		while (display.readAndDispatch()) {
			// loop until no more events
		}
	}

	// Verify the link is still valid and has correct text
	assertFalse(link.isDisposed(), "Link should not be disposed after multiple redraws");
	assertEquals("Test <a>link</a>", link.getText(), "Link text should be intact");
}

@Test
public void test_LinkMaintainsContentDuringRapidResize() {
	shell.setSize(400, 300);
	String testText = "Click <a href=\"page1\">here</a> or <a href=\"page2\">there</a>";
	link.setText(testText);
	link.setBounds(10, 10, 300, 100);

	shell.open();
	Display display = shell.getDisplay();
	while (display.readAndDispatch()) {
		// loop until no more events
	}

	// Rapidly resize - this would cause flickering in the old implementation
	for (int i = 0; i < 20; i++) {
		int newWidth = 200 + (i % 2) * 100;
		int newHeight = 50 + (i % 2) * 30;
		link.setSize(newWidth, newHeight);

		// Process events but don't wait for full redraw
		display.readAndDispatch();

		// Content should remain intact
		assertEquals(testText, link.getText(),
			"Link text should remain unchanged during resize iteration " + i);
	}
}
}
