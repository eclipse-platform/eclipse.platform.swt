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


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BidiSegmentListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

@BeforeEach
public void setUp() {
	shell = new Shell();
	text = new StyledText(shell, SWT.NULL);
}
@AfterEach
public void tearDown() {
	shell.dispose();
}
private void testListener(final int[] segments, boolean exceptionExpected) {
	BidiSegmentListener listener = event -> {
		assertEquals(0, event.lineOffset, " incorrect BidiSegmentEvent");
		assertEquals(line, event.lineText, " incorrect BidiSegmentEvent");

		event.segments = segments;
		listenerCalled = true;
	};

	listenerCalled = false;
	text.addBidiSegmentListener(listener);
	if (exceptionExpected) {
		assertThrows(IllegalArgumentException.class, () -> text.getLocationAtOffset(0), " expected exception not thrown");
	} else {
		text.getLocationAtOffset(0);
	}
	text.removeBidiSegmentListener(listener);
	if (SwtTestUtil.isBidi()) {
		assertTrue(listenerCalled, " listener not called");
	} else {
		assertFalse(listenerCalled, " listener called when it shouldn't be");
	}
}
private void testStyleRangeSegmenting(final int[] segments, int[] boldRanges) {
	BidiSegmentListener listener = event -> {
		assertEquals(0, event.lineOffset, " incorrect BidiSegmentEvent");
		assertEquals(line, event.lineText, " incorrect BidiSegmentEvent");

		event.segments = segments;
		listenerCalled = true;
	};

	listenerCalled = false;
	text.addBidiSegmentListener(listener);
	try {
		text.setStyleRange(null);
		for (int i = 0; i < boldRanges.length; i += 2) {
			StyleRange styleRange = new StyleRange(boldRanges[i], boldRanges[i + 1], null, null, SWT.BOLD);
			text.setStyleRange(styleRange);
		}
		text.getLocationAtOffset(0);
	} finally {
		text.removeBidiSegmentListener(listener);
	}
	if (SwtTestUtil.isBidi()) {
		assertTrue(listenerCalled, " listener not called");
	} else {
		assertFalse(listenerCalled, " listener called when it shouldn't be");
	}
}

@Test
public void test_lineGetSegmentsLorg_eclipse_swt_custom_BidiSegmentEvent() {
	int lineLength = line.length();

	text.setText(line);
	// should not cause an exception
	testListener(null, false);
	testListener(new int[] {0, lineLength / 2, lineLength}, false);
	testListener(new int[] {0, lineLength / 2}, false);

	// should all cause an exception on a bidi platform
	if (SwtTestUtil.isBidi()) {
		testListener(new int[] {0, 1, 1, lineLength / 2}, true);
		testListener(new int[] {0, 1, 2, lineLength + 1}, true);
		testListener(new int[] {0, 1, lineLength + 1, lineLength + 1}, true);
		testListener(new int[] {0, 2, 1}, true);
		testListener(new int[] {0, -1, 2}, true);
		testListener(new int[] {0, -1, 2}, true);
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
