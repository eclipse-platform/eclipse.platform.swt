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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.DefaultLinkRenderer;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.LinkRenderer;
import org.eclipse.swt.widgets.LinkRenderer.TextSegment;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Link
 *
 * @see org.eclipse.swt.widgets.Link
 */
public class Test_org_eclipse_swt_widgets_Link extends Test_org_eclipse_swt_widgets_Control {

	Link link;

@Override
@Before
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
	}

	link.addSelectionListener(listener);
	link.notifyListeners(SWT.Selection, new Event());
	assertTrue(listenerCalled);

	try {
		link.removeSelectionListener(null);
		fail("No exception thrown for removeSelectionListener with null argument");
	} catch (IllegalArgumentException e) {
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
public void testparseLinkText() {
	String linkText = "Start  <a  href=\"targetLink\">click here</a>\n\n\n\ntest <a href=\"targetLink1\">targetline1\n\ntargetline2</a> same as line anchor\n\nLast line";
	String expDisplayText = "Start  click here\n"
			+ "\n"
			+ "\n"
			+ "\n"
			+ "test targetline1\n"
			+ "\n"
			+ "targetline2 same as line anchor\n"
			+ "\n"
			+ "Last line";
	LinkRenderer defLinkRendrer = new DefaultLinkRenderer(null);
	defLinkRendrer.parseLinkText(linkText);
	List<List<TextSegment>> parsedText = defLinkRendrer.getParsedSegments();
	String actual = defLinkRendrer.getLinkDisplayText();
	assertEquals("Incorrect display message: ", expDisplayText, actual);

	int numberOfLines = expDisplayText.split("\n", -1).length;
	assertEquals("Incorrect number of segments: ", numberOfLines, parsedText.size());

	assertTrue("First line second segment must be a link: ", parsedText.get(0).get(1).isLink());
	assertEquals("First line incorrect link display text: ", "click here", parsedText.get(0).get(1).text);
	assertEquals("First line incorrect link data: ", "targetLink", parsedText.get(0).get(1).linkData);

	assertTrue("Fifth line second segment must be a link: ", parsedText.get(4).get(1).isLink());
	assertEquals("Fifth line incorrect link display text: ", "targetline1", parsedText.get(4).get(1).text);
	assertEquals("Fifth line incorrect link data: ", "targetLink1", parsedText.get(4).get(1).linkData);

	assertTrue("Seventh line first segment must be a link: ", parsedText.get(6).get(0).isLink());
	assertEquals("Seventh line incorrect link display text: ", "targetline2", parsedText.get(6).get(0).text);
	assertEquals("Seventh line incorrect link data: ", "targetLink1", parsedText.get(6).get(0).linkData);
}
}
