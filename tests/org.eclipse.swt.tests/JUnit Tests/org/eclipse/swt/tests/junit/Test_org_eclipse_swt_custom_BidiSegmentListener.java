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
 *     Red Hat Inc. - Bug 462631
 *******************************************************************************/
package org.eclipse.swt.tests.junit;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BidiSegmentListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.custom.BidiSegmentListener
 *
 * @see org.eclipse.swt.custom.BidiSegmentListener
 */
public class Test_org_eclipse_swt_custom_BidiSegmentListener {
	Shell shell;
	StyledText text;
	boolean listenerCalled;
	String line = "Line1";

@Before
public void setUp() {
	shell = new Shell();
	text = new StyledText(shell, SWT.NULL);
}
@After
public void tearDown() {
	shell.dispose();
}
private void testListener(final String message, final int[] segments, boolean exceptionExpected) {
	boolean exceptionThrown = false;
	BidiSegmentListener listener = event -> {
		assertEquals(message + " incorrect BidiSegmentEvent", 0, event.lineOffset);
		assertEquals(message + " incorrect BidiSegmentEvent", line, event.lineText);

		event.segments = segments;
		listenerCalled = true;
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
		assertFalse(message + " unexpected exception thrown", exceptionThrown);
	}
	if (SwtTestUtil.isBidi()) {
		assertTrue(message + " listener not called", listenerCalled);
	}
	else {
		assertFalse(message + " listener called when it shouldn't be", listenerCalled);
	}
}
private void testStyleRangeSegmenting(final int[] segments, int[] boldRanges) {
	boolean exceptionThrown = false;
	BidiSegmentListener listener = event -> {
		assertEquals(" incorrect BidiSegmentEvent", 0, event.lineOffset);
		assertEquals(" incorrect BidiSegmentEvent", line, event.lineText);

		event.segments = segments;
		listenerCalled = true;
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
	assertFalse(" unexpected exception thrown", exceptionThrown);
	if (SwtTestUtil.isBidi()) {
		assertTrue(" listener not called", listenerCalled);
	}
	else {
		assertFalse(" listener called when it shouldn't be", listenerCalled);
	}
}

@Test
public void test_lineGetSegmentsLorg_eclipse_swt_custom_BidiSegmentEvent() {
	int lineLength = line.length();

	text.setText(line);
	// should not cause an exception
	testListener(":a:", null, false);
	testListener(":b:", new int[] {0, lineLength / 2, lineLength}, false);
	testListener(":c:", new int[] {0, lineLength / 2}, false);

	// should all cause an exception on a bidi platform
	if (SwtTestUtil.isBidi()) {
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

}
