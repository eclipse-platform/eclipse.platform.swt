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


import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BidiSegmentEvent;
import org.eclipse.swt.custom.BidiSegmentListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.internal.BidiUtil;

/**
 * Automated Test Suite for class org.eclipse.swt.custom.BidiSegmentListener
 *
 * @see org.eclipse.swt.custom.BidiSegmentListener
 */
public class Test_org_eclipse_swt_custom_BidiSegmentListener extends SwtTestCase {
	Shell shell;
	StyledText text;
	boolean listenerCalled;	
	String line = "Line1";
			
public static void main(String[] args) {
	TestRunner.run(suite());
}
public Test_org_eclipse_swt_custom_BidiSegmentListener(String name) {
	super(name);
}
protected void setUp() {
	shell = new Shell();
	text = new StyledText(shell, SWT.NULL);
}
public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_custom_BidiSegmentListener((String)e.nextElement()));
	}
	return suite;
}
protected void tearDown() {
	shell.dispose();
	super.tearDown();
}
private void testListener(final String message, final int[] segments, boolean exceptionExpected) {
	boolean exceptionThrown = false;
	BidiSegmentListener listener = new BidiSegmentListener() {
		public void lineGetSegments(BidiSegmentEvent event) {
			assertEquals(message + " incorrect BidiSegmentEvent", 0, event.lineOffset);
			assertEquals(message + " incorrect BidiSegmentEvent", line, event.lineText);
			
			event.segments = segments;
			listenerCalled = true;	
		}
	};

	listenerCalled = false;		
	try {
		text.addBidiSegmentListener(listener);
		text.getLocationAtOffset(0);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	finally {
		text.removeBidiSegmentListener(listener);
	}
	if (exceptionExpected) {
		assertTrue(message + " expected exception not thrown", exceptionThrown);
	}
	else {
		assertTrue(message + " unexpected exception thrown", exceptionThrown == false);
	}
	if (isBidi()) {
		assertTrue(message + " listener not called", listenerCalled);	
	}
	else {
		assertTrue(message + " listener called when it shouldn't be", listenerCalled == false);
	}
}
private void testStyleRangeSegmenting(final int[] segments, int[] boldRanges) {
	boolean exceptionThrown = false;
	BidiSegmentListener listener = new BidiSegmentListener() {
		public void lineGetSegments(BidiSegmentEvent event) {
			assertEquals(" incorrect BidiSegmentEvent", 0, event.lineOffset);
			assertEquals(" incorrect BidiSegmentEvent", line, event.lineText);
			
			event.segments = segments;
			listenerCalled = true;	
		}
	};

	listenerCalled = false;		
	try {
		text.addBidiSegmentListener(listener);
		text.setStyleRange(null);
		for (int i=0; i<boldRanges.length; i+=2) {
			StyleRange styleRange = new StyleRange(boldRanges[i], boldRanges[i+1], null, null, SWT.BOLD);
			text.setStyleRange(styleRange);
		}
		text.getLocationAtOffset(0);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	finally {
		text.removeBidiSegmentListener(listener);
	}
	assertTrue(" unexpected exception thrown", exceptionThrown == false);
	if (isBidi()) {
		assertTrue(" listener not called", listenerCalled);	
	}
	else {
		assertTrue(" listener called when it shouldn't be", listenerCalled == false);
	}
}

public void test_lineGetSegmentsLorg_eclipse_swt_custom_BidiSegmentEvent() {
	int lineLength = line.length();

	text.setText(line);	
	// should not cause an exception
	testListener(":a:", null, false);
	testListener(":b:", new int[] {0, lineLength / 2, lineLength}, false);
	testListener(":c:", new int[] {0, lineLength / 2}, false);
			
	// should all cause an exception on a bidi platform
	if (isBidi()) {
		testListener(":d:", new int[] {lineLength / 2}, true);
		testListener(":e:", new int[] {0, 1, 1, lineLength / 2}, true);
		testListener(":f:", new int[] {0, 1, 2, lineLength + 1}, true);
		testListener(":g:", new int[] {0, 1, lineLength + 1, lineLength + 1}, true);
		testListener(":h:", new int[] {0, 2, 1}, true);
		testListener(":i:", new int[] {0, -1, 2}, true);
		testListener(":j:", new int[] {0, -1, 2}, true);
	}

	// test bold segmenting
	line = "this is a line with 50 chars - **** **** **** ****";
	text.setText(line);
	// should segment as int[] {0,5,5,2,12,3,20,5}
	testStyleRangeSegmenting(new int[] {0,5,10,15,20,25}, new int[] {0,5,5,2,12,3,20,5});
	// should segment as int[] {0,5,5,1,7,3,10,3,15,5,20,5}
	testStyleRangeSegmenting(new int[] {0,5,10,15,20,25}, new int[] {0,6,7,6,15,10});
	// should segment as int[] {0,5,5,5,10,5}
	testStyleRangeSegmenting(new int[] {0,5,10,15}, new int[] {0,15});
	// should segment as int[] {0,5}
	testStyleRangeSegmenting(new int[] {0,5}, new int[] {0,5});
	// should segment as int[] {2,3}
	testStyleRangeSegmenting(new int[] {0,5}, new int[] {2,3});
	// should segment as int[] {0,2}
	testStyleRangeSegmenting(new int[] {0,5}, new int[] {0,2});
	// should segment as int[] {10,5}
	testStyleRangeSegmenting(new int[] {0,5,10,15}, new int[] {10,5});
	// should segment as int[] {12,3}
	testStyleRangeSegmenting(new int[] {0,5,10,15}, new int[] {12,3});
	// should segment as int[] {3,2,5,5,10,3,21,4,25,3}
	testStyleRangeSegmenting(new int[] {0,5,10,15,20,25,30}, new int[] {3,10,21,7});
	// should segment as int[] {10,2}
	line = "test1test2/r/n";
	text.setText(line);
	testStyleRangeSegmenting(new int[] {0,5}, new int[] {10,2});
}

public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_lineGetSegmentsLorg_eclipse_swt_custom_BidiSegmentEvent");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_lineGetSegmentsLorg_eclipse_swt_custom_BidiSegmentEvent")) test_lineGetSegmentsLorg_eclipse_swt_custom_BidiSegmentEvent();
}
}
