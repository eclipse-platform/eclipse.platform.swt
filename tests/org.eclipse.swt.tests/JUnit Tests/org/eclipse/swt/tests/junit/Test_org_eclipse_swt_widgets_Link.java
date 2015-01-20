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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Link;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Link
 *
 * @see org.eclipse.swt.widgets.Link
 */
public class Test_org_eclipse_swt_widgets_Link extends Test_org_eclipse_swt_widgets_Control {

	Link link;

public Test_org_eclipse_swt_widgets_Link(String name) {
	super(name);
}

@Override
protected void setUp() {
	super.setUp();
	link = new Link(shell, SWT.NONE);
	setWidget(link);
}

@Override
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

public void test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	listenerCalled = false;
	SelectionListener listener = new SelectionListener() {
		public void widgetSelected(SelectionEvent e) {
			listenerCalled = true;
		}
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

@Override
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
	for (int i=0; i<cases.length; i++){
		link.setText(cases[i]);
		assertEquals(link.getText() , cases[i]);
	}
	try {
		link.setText(null);
		fail("No exception thrown for text == null");
	} catch (IllegalArgumentException e) {
	}
}
}
