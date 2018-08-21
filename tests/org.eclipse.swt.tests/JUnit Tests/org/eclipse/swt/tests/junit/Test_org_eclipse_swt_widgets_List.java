/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.List;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.List
 *
 * @see org.eclipse.swt.widgets.List
 */
public class Test_org_eclipse_swt_widgets_List extends Test_org_eclipse_swt_widgets_Scrollable {

@Override
@Before
public void setUp() {
	super.setUp();
	list = new List(shell, SWT.MULTI);

	setWidget(list);
}

@Override
@Test
public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
	try {
		list = new List(null, 0);
		fail("No exception thrown"); //should never get here
	} catch (IllegalArgumentException e) {
	}

	int[] cases =
		{
			0,
			SWT.SINGLE,
			SWT.MULTI,
			SWT.MULTI | SWT.V_SCROLL,
			SWT.MULTI | SWT.H_SCROLL,
			SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL };
	for (int i = 0; i < cases.length; i++)
		list = new List(shell, cases[i]);
}

@Test
public void test_addLjava_lang_String() {
	try {
		list.add(null);
		fail("No exception thrown");
	} catch (IllegalArgumentException e) {
	}
	list.add("");
	assertArrayEquals(":a:", new String[] {""}, list.getItems());
	list.add("some \n text");
	assertArrayEquals(":b:", new String[] {"", "some \n text"}, list.getItems());
	list.add("some text");
	assertArrayEquals(":c:", new String[] {"", "some \n text", "some text"}, list.getItems());

	// test single-selection list

	setSingleList();

	try {
		list.add(null);
		fail("No exception thrown");
	} catch (IllegalArgumentException e) {
	}

	list.add("");
	assertArrayEquals(":a:", new String[] {""}, list.getItems());
	list.add("some \n text");
	assertArrayEquals(":b:", new String[] {"", "some \n text"}, list.getItems());
	list.add("some text");
	assertArrayEquals(":c:", new String[] {"", "some \n text", "some text"}, list.getItems());
}

@Test
public void test_addLjava_lang_StringI() {
	try {
		list.add("some text", 2);
		fail("No exception thrown");
	} catch (IllegalArgumentException e) {
	}
	assertEquals(0, list.getItemCount());

	list.add("", 0);
	assertArrayEquals(":a:", new String[] {""}, list.getItems());
	list.add("some \n text", 1);
	assertArrayEquals(":b:", new String[] {"", "some \n text"}, list.getItems());
	list.add("some text", 0);
	assertArrayEquals(":c:", new String[] {"some text", "", "some \n text" }, list.getItems());

	try {
		list.add(null, 0);
		fail("No exception thrown string == null");
	} catch (IllegalArgumentException e) {
	}

	try {
		list.add("string", -1);
		fail("No exception thrown index < 0");
	} catch (IllegalArgumentException e) {
	}

	// test single-selection list

	setSingleList();

	try {
		list.add("some text", 2);
		fail("No exception thrown");
	} catch (IllegalArgumentException e) {
	}

	assertEquals(0, list.getItemCount());

	list.add("", 0);
	assertArrayEquals(":a:", new String[] {""}, list.getItems());
	list.add("some \n text", 1);
	assertArrayEquals(":b:", new String[] {"", "some \n text"}, list.getItems());
	list.add("some text", 0);
	assertArrayEquals(":c:", new String[] {"some text", "", "some \n text" }, list.getItems());

	try {
		list.add(null, 0);
		fail("No exception thrown string == null");
	} catch (IllegalArgumentException e) {
	}

	try {
		list.add("string", -1);
		fail("No exception thrown index < 0");
	} catch (IllegalArgumentException e) {
	}
}

@Test
public void test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	listenerCalled = false;
	boolean exceptionThrown = false;
	SelectionListener listener = new SelectionListener() {
		@Override
		public void widgetSelected(SelectionEvent event) {
			listenerCalled = true;
		}
		@Override
		public void widgetDefaultSelected(SelectionEvent event) {
		}
	};
	try {
		list.addSelectionListener(null);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue("Expected exception not thrown for listener == null", exceptionThrown);

	list.addSelectionListener(listener);
	list.select(0);
	assertFalse(":a:", listenerCalled);
	list.removeSelectionListener(listener);
	exceptionThrown = false;
	try {
		list.removeSelectionListener(null);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue("Expected exception not thrown for listener == null", exceptionThrown);

	// test single-selection list

	setSingleList();

	listenerCalled = false;
	exceptionThrown = false;
	try {
		list.addSelectionListener(null);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue("Expected exception not thrown for listener == null", exceptionThrown);

	list.addSelectionListener(listener);
	list.select(0);
	assertFalse(":a:", listenerCalled);
	list.removeSelectionListener(listener);
	exceptionThrown = false;
	try {
		list.removeSelectionListener(null);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue("Expected exception not thrown for listener == null", exceptionThrown);
}

@Test
public void test_addSelectionListenerWidgetSelectedAdapterLorg_eclipse_swt_events_SelectionListener() {
	listenerCalled = false;
	SelectionListener listener = SelectionListener.widgetSelectedAdapter(e -> listenerCalled = true);

	list.addSelectionListener(listener);
	list.select(0);
	assertFalse(":a:", listenerCalled);
	list.removeSelectionListener(listener);

	// test single-selection list

	setSingleList();

	list.addSelectionListener(listener);
	list.select(0);
	assertFalse(":a:", listenerCalled);
	list.removeSelectionListener(listener);
}

@Override
@Test
public void test_computeSizeIIZ() {
	// super class test is sufficient
}

@Test
public void test_deselect$I() {
	String[] items = { "item0", "item1", "item2", "item3" };
	list.setItems(items);
	list.setSelection(items);
	assertArrayEquals(":a:", list.getSelection(), items);
	try {
		list.deselect(null);
		fail("No exception thrown");
	} catch (IllegalArgumentException e) {
	}
	assertArrayEquals(list.getSelection(), items);
	list.deselect(new int[] {
	});
	assertArrayEquals(list.getSelection(), items);
	list.deselect(new int[] { 0 });
	assertEquals(list.isSelected(0), false);
	assertTrue(list.isSelected(1));
	list.deselect(new int[] { 2, 0, 0 });
	assertArrayEquals(list.getSelectionIndices(), new int[] { 1, 3 });
	/*	assert(":d:", !list.isSelected(0));
	assert(":dd:", !list.isSelected(2));
	assert(":ddd:", list.isSelected(1));
	assert(":ddd:", list.isSelected(1));*/


	setSingleList();
	list.setItems(items);
	list.setSelection(new String[] { "item3" });
	assertArrayEquals(list.getSelection(), new String[] { "item3" });
	try {
		list.deselect(null);
		fail("No exception thrown");
	} catch (IllegalArgumentException e) {
	}

	assertArrayEquals(list.getSelection(), new String[] { "item3" });
	list.deselect(new int[] {});
	assertArrayEquals(list.getSelection(), new String[] { "item3" });
	list.deselect(new int[] { 1 });
	assertArrayEquals(list.getSelectionIndices(), new int[] { 3 });
	list.deselect(new int[] { 0 });
	assertArrayEquals(list.getSelectionIndices(), new int[] { 3 });
	list.deselect(new int[] { 3 });
	assertArrayEquals(list.getSelectionIndices(), new int[] {});
	list.deselect(new int[] { 2, 0, 0 });
	assertArrayEquals(list.getSelectionIndices(), new int[] {});

}

@Test
public void test_deselectAll() {
	String[] items = { "item0", "item1", "item2", "item3" };
	String[] empty = {
	};
	list.setItems(items);
	list.setSelection(items);
	assertArrayEquals(items, list.getSelection());
	list.deselectAll();
	assertArrayEquals(empty, list.getSelection());


	setSingleList();

	list.setItems(items);
	list.setSelection(new String[] { "item3" });
	assertArrayEquals(new String[] { "item3" }, list.getSelection());
	list.deselectAll();
	assertArrayEquals(empty, list.getSelection());

}

@Test
public void test_deselectI() {
	int number = 5;
	String[] items = new String[number];
	for (int i = 0; i < number; i++)
		items[i] = "fred" + i;
	list.setItems(items);

	String[] items2 = { "item0", "item1", "item2", "item3" };
	list.setItems(items2);
	list.setSelection(items2);
	assertArrayEquals(items2, list.getSelection());
	list.deselect(5);
	assertArrayEquals(items2, list.getSelection());
	list.deselect(1);
	assertFalse(list.isSelected(1));
	list.deselect(1);
	assertFalse(list.isSelected(1));


	setSingleList();
	list.setItems(items2);
	list.setSelection(new String[] { "item3" });
	assertArrayEquals(new String[] { "item3" }, list.getSelection());
	list.deselect(5);
	assertArrayEquals(new String[] { "item3" }, list.getSelection());
	list.deselect(2);
	assertFalse(list.isSelected(2));
	list.deselect(1);
	assertFalse(list.isSelected(1));
	list.deselect(1);
	assertFalse(list.isSelected(1));
}

@Test
public void test_deselectII() {
	int number = 5;
	String[] items = new String[number];
	for (int i = 0; i < number; i++)
		items[i] = "fred" + i;
	list.setItems(items);
	list.setSelection(items);

	// tests if deselect(i, j) is the same as for (i=0; i<=j; ++i) deselect(i);
	int[][] cases = { { 3, 1 }, {
			-3, -2 }, {
			-2, -1 }, {
			-1, -1 }, {
			10, 1 }
	};

	for (int i = 0; i < cases.length; ++i) {
		deselectII_helper(items, cases[i][0], cases[i][1], new int[] { 0, 1, 2, 3, 4 });
	}
	if (SwtTestUtil.fCheckSWTPolicy) {
		deselectII_helper(items, -1, 3, new int[] { 4 });
		deselectII_helper(items, -1, 30, new int[] {
		});
	}
	deselectII_helper(items, 1, 3, new int[] { 0, 4 });
	deselectII_helper(items, 1, 1, new int[] { 0, 2, 3, 4 });
	// done

	String[] items2 = { "item0", "item1", "item2", "item3" };
	list.setItems(items2);
	list.setSelection(items2);
	assertArrayEquals(":a:", items2, list.getSelection());
	list.deselect(0, 0);
	assertArrayEquals(":b:", list.getSelectionIndices(), new int[] { 1, 2, 3 });
	list.deselect(0, 0);
	assertArrayEquals(":bbb:", list.getSelectionIndices(), new int[] { 1, 2, 3 });
	list.deselect(2, 3);
	assertArrayEquals(":bb:", list.getSelectionIndices(), new int[] { 1 });

	list.setSelection(items2);
	list.deselect(0, 2);
	assertArrayEquals(":dddd:", list.getSelectionIndices(), new int[] { 3 });

	list.setSelection(items2);
	list.deselect(2, 0);
	assertArrayEquals(
		":ddddd:",
		list.getSelectionIndices(), new int[] { 0, 1, 2, 3 });


	setSingleList();

	list.setItems(items2);
	list.deselectAll();

	list.select(0);

	list.deselect(-3, -2);
	assertArrayEquals(list.getSelectionIndices(), new int[] { 0 });

	list.deselect(-2, -1);
	assertArrayEquals(list.getSelectionIndices(), new int[] { 0 });

	list.deselect(-1, -1);
	assertArrayEquals(":e:", list.getSelectionIndices(), new int[] { 0 });


	list.setSelection(new String[] { "item3" });
	assertArrayEquals(list.getSelection(), new String[] { "item3" });

	list.deselect(1, 1);
	assertArrayEquals(list.getSelection(), new String[] { "item3" });

	list.deselect(0, 0);
	assertArrayEquals(list.getSelection(), new String[] { "item3" });

	list.deselect(3, 3);
	assertArrayEquals(list.getSelection(), new String[] {});

	list.setSelection(new String[] { "item3" });
	list.deselect(1, 2);
	assertArrayEquals(list.getSelection(), new String[] { "item3" });

	list.setSelection(new String[] { "item3" });
	list.deselect(0, 2);
	assertArrayEquals(list.getSelectionIndices(), new int[] { 3 });

	list.setSelection(new String[] { "item3" });
	list.deselect(1, 3);
	assertArrayEquals(list.getSelectionIndices(), new int[] {});
}

@Test
public void test_getFocusIndex() {
	String[] items = { "item0", "item1", "item2"};
	list.setItems(items);
	list.setSelection(0);
	assertEquals(0, list.getFocusIndex());
	list.setSelection(2);
	assertEquals(2, list.getFocusIndex());
}

@Test
public void test_getItemCount() {
	String[] items = { "item0", "item1", "item2", "item3" };

	assertEquals(0, list.getItemCount());
	list.setItems(items);
	assertEquals(4, list.getItemCount());
	list.remove(2);
	assertEquals(3, list.getItemCount());
	list.removeAll();
	assertEquals(0, list.getItemCount());


	setSingleList();
	assertEquals(0, list.getItemCount());
	list.setItems(items);
	assertEquals(4, list.getItemCount());
	list.remove(2);
	assertEquals(3, list.getItemCount());
	list.removeAll();
	assertEquals(0, list.getItemCount());

}

@Test
public void test_getItemHeight() {
	if (SwtTestUtil.isGTK) {
		//TODO Fix GTK failure.
		if (SwtTestUtil.verbose) {
			System.out.println("Excluded test_getItemHeight(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_List)");
		}
		return;
	}
	FontData fontData = list.getFont().getFontData()[0];
	int lineHeight;
	Font font;

	font = new Font(list.getDisplay(), fontData.getName(), 8, fontData.getStyle());
	list.setFont(font);
	lineHeight = list.getItemHeight();
	list.setFont(null);
	font.dispose();
	font = new Font(list.getDisplay(), fontData.getName(), 12, fontData.getStyle());
	list.setFont(font);
	int newLineHeight = list.getItemHeight();
	assertTrue(":a:", newLineHeight > lineHeight);
	list.setFont(null);
	font.dispose();
}

@Test
public void test_getItemI() {
	String[] items = { "item0", "item1", "item2", "item3" };
	list.setItems(items);
	try {
		list.getItem(5);
		fail("No exception thrown");
	} catch (IllegalArgumentException e) {
	}

	try {
		list.getItem(-1);
		fail("No exception thrown for index < 0");
	} catch (IllegalArgumentException e) {
	}

	assertEquals(list.getItem(3), "item3");


	setSingleList();
	list.setItems(items);
	try {
		list.getItem(5);
		fail("No exception thrown");
	} catch (IllegalArgumentException e) {
	}

	try {
		list.getItem(-1);
		fail("No exception thrown for index < 0");
	} catch (IllegalArgumentException e) {
	}

	//assert(":a:", list.getItem(5)==null);
	assertEquals("item3", list.getItem(3));

}

@Test
public void test_getItems() {
	String[][] cases = { {
		}, {
			"" }, {
			"", "" }, {
			"text1", "text2" }
	};
	for (int i = 0; i < cases.length; i++) {
		list.setItems(cases[i]);
		assertArrayEquals("case: " + i, cases[i], list.getItems());
	}
}

@Test
public void test_getSelection() {
	String[][] cases = { {
		}, {
			"" }, {
			"", "" }, {
			"text1", "text2" }
	};
	for (int i = 0; i < cases.length; i++) {
		//		System.out.println("loop:" + i);
		list.setItems(cases[i]);
		list.setSelection(cases[i]);
		//		System.out.println("list:" + list.getSelection());
		//		System.out.println("case:" + i + cases[i]);
		assertArrayEquals("case: " + i, cases[i], list.getSelection());
	}

	for (int i = 1; i < cases.length; i++) {
		list.setItems(cases[i]);
		list.setSelection(0);
		assertArrayEquals(
			"case: " + String.valueOf(i),
			list.getSelection(), new String[] { cases[i][0] });
	}

	String[] items = { "text1", "text2", "text3" };
	list.setItems(items);
	int[] sel = { 0, 2 };
	list.setSelection(sel);
	assertEquals(list.getSelection().length, 2);
	String[] selItems = new String[] {items[0], items[2]};
	assertArrayEquals(list.getSelection(), selItems);

	list.setSelection(0, 1);
	assertEquals(list.getSelection().length, 2);
	selItems = new String[] {items[0], items[1]};
	assertArrayEquals(list.getSelection(), selItems);

	list.setSelection(1, 1);
	assertEquals(list.getSelection().length, 1);
	assertEquals(list.getSelection()[0], items[1]);

	list.setSelection(1, 0);
	String[] empty = {
	};
	assertArrayEquals(empty, list.getSelection());

	String[] bogus_items = { "bogus_text1", "bogus_text2", "bogus_text3" };
	list.setSelection(bogus_items);
	assertArrayEquals(empty, list.getSelection());

	// test single-selection lists

	setSingleList();

	list.setItems(items);
	sel = new int[] { 0 };
	list.setSelection(sel);
	assertEquals(1, list.getSelection().length);
	assertEquals(items[0], list.getSelection()[0]);

	list.setSelection(1, 1);
	assertEquals(1, list.getSelection().length);
	assertEquals(items[1], list.getSelection()[0]);
}

/**
 * Returns the number of selected items contained in the receiver.
 *
 * @return the number of selected items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_COUNT - if the operation fails because of an operating system failure</li>
 * </ul>
 */
@Test
public void test_getSelectionCount() {
	String[] items = { "text1", "text2", "text3" };

	list.setItems(items);
	assertEquals(0, list.getSelectionCount());

	list.setSelection(items);
	assertEquals(3, list.getSelectionCount());

	list.deselectAll();
	try {
		list.setSelection((String[]) null);
		fail("No exception thrown for selection == null");
	} catch (IllegalArgumentException e) {
	}
	assertEquals(list.getSelectionCount(), 0);


	setSingleList();

	list.setItems(items);
	list.setSelection(new String[] { "text2" });
	assertEquals(1, list.getSelectionCount());
}

@Test
public void test_getSelectionIndex() {
	String[] items = { "text1", "text2", "text3" };

	// not properly spec'd for multi-select lists
	list.setItems(items);
	list.setSelection(items);
	assertEquals(0, list.getSelectionIndex());


	setSingleList();

	list.setItems(items);
	list.setSelection(items);
	assertEquals(-1, list.getSelectionIndex());

	list.setSelection(new String[] { "text1" });
	assertEquals(0, list.getSelectionIndex());

	list.setSelection(new String[] { "text2" });
	assertEquals(1, list.getSelectionIndex());

	list.setSelection(new String[] { "text3" });
	assertEquals(2, list.getSelectionIndex());

	list.setSelection(items);
	assertEquals(-1, list.getSelectionIndex());
}

@Test
public void test_getSelectionIndices() {
	String[] items = { "text1", "text2", "text3" };

	list.setItems(items);

	assertArrayEquals(list.getSelectionIndices(), new int[] {});

	list.setSelection(items);
	int[] sel = { 0, 1, 2 };
	assertArrayEquals(sel, list.getSelectionIndices());

	list.setSelection(sel);
	assertArrayEquals(sel, list.getSelectionIndices());

	sel = new int[] { 0 };
	list.setSelection(sel);
	assertArrayEquals(sel, list.getSelectionIndices());

	sel = new int[] { 1 };
	list.setSelection(sel);
	assertArrayEquals(sel, list.getSelectionIndices());

	sel = new int[] { 2 };
	list.setSelection(sel);
	assertArrayEquals(sel, list.getSelectionIndices());

	sel = new int[] { 3 };
	list.setSelection(sel);
	assertArrayEquals(new int[] {}, list.getSelectionIndices());

	sel = new int[] { -1, 0, 1, 2, 3 };
	list.setSelection(sel);
	assertArrayEquals(new int[] { 0, 1, 2 }, list.getSelectionIndices());

	sel = new int[] { 1, 1, 2, 2 };
	list.setSelection(sel);
	assertArrayEquals(new int[] { 1, 2 }, list.getSelectionIndices());


	setSingleList();

	list.setItems(items);

	sel = new int[] { 0 };
	list.setSelection(sel);
	assertArrayEquals(sel, list.getSelectionIndices());

	sel = new int[] { 1 };
	list.setSelection(sel);
	assertArrayEquals(sel, list.getSelectionIndices());

	sel = new int[] { 2 };
	list.setSelection(sel);
	assertArrayEquals(sel, list.getSelectionIndices());

	sel = new int[] { 3 };
	list.setSelection(sel);
	assertArrayEquals(new int[] {}, list.getSelectionIndices());
}

@Test
public void test_getTopIndex() {
	String[] items = new String [10];
	for (int i = 0; i < 10; i++) {
		items[i] = "text" + String.valueOf(i);
	}

	list.setItems(items);
	assertEquals(0, list.getTopIndex());

	list.setTopIndex(5);
	assertEquals(5, list.getTopIndex());

	setSingleList();

	list.setItems(items);
	assertEquals(0, list.getTopIndex());

}

@Test
public void test_indexOfLjava_lang_String() {
	String[] items = { "text1", "text2", "text3" };

	list.setItems(items);
	assertEquals(list.indexOf("text3"), 2);
	assertEquals(list.indexOf("text4"), -1);

	try {
		list.indexOf(null);
		fail("No exception thrown for item == null");
	} catch (IllegalArgumentException e) {
	}

	String[] items2 = { "text1", "text2", "text2" }; //two identical

	list.setItems(items2);
	assertEquals(list.indexOf("text2"), 1);


	setSingleList();

	list.setItems(items);
	assertEquals(-1, list.indexOf("text3", 4));


	assertEquals(2, list.indexOf("text3"));


	assertEquals(-1, list.indexOf("text4"));


	try {
		list.indexOf(null);
		fail("No exception thrown");
	} catch (IllegalArgumentException e) {
	}


	assertEquals(1, list.indexOf("text2"));
}

@Test
public void test_indexOfLjava_lang_StringI() {
	String[] items = { "text1", "text2", "text3" };

	list.setItems(items);
	assertEquals(-1, list.indexOf("text3", 4));
	assertEquals(2, list.indexOf("text3", 2));
	assertEquals(1, list.indexOf("text2", 0));
	assertEquals(1, list.indexOf("text2", 1));
	assertEquals(-1, list.indexOf("text2", 2));

	String[] items2 = { "text1", "text2", "text2" }; //two identical
	list.setItems(items2);
	assertEquals(list.indexOf("text2", 2), 2);

	try {
		 list.indexOf(null, 0);
		fail("No exception thrown for string == null");
	} catch (IllegalArgumentException e) {
	}

	setSingleList();

	list.setItems(items2);
	//	assert("list.indexOf(\"text2\", -1)==1", list.indexOf("text2", -1)==1);
	assertEquals(1, list.indexOf("text2", 0));
	assertEquals(1, list.indexOf("text2", 1));
	assertEquals(2, list.indexOf("text2", 2));

	try {
		list.indexOf(null, 0);
		fail("No exception thrown for string == null");
	} catch (IllegalArgumentException e) {
	}
}

@Test
public void test_isSelectedI() {
	String[] items = { "text1", "text2", "text2" }; //two identical

	list.setItems(items);
	list.setSelection(items);
	assertTrue(list.isSelected(0));
	assertTrue(list.isSelected(1));
	assertTrue(list.isSelected(2));
	assertFalse(list.isSelected(3));


	setSingleList();

	list.setItems(items);
	list.setSelection(items);
	if (SwtTestUtil.fCheckSWTPolicy) {
		assertFalse(list.isSelected(0));
		assertTrue(list.isSelected(1));
		assertFalse(list.isSelected(2));
		assertFalse(list.isSelected(3));
	}

}

@Test
public void test_remove$I() {
	try {
		list.remove((int[]) null);
		fail("No exception thrown for indices == null");
	} catch (IllegalArgumentException e) {
	}

	String[] items = { "text0", "text1", "text2", "text3" };

	list.setItems(items);
	assertEquals(list.getItemCount(), 4);

	list.setItems(items);
	list.remove(new int[] { 1, 0, 1 });
	assertEquals(list.getItemCount(), 2);

	list.setItems(items);

	// index > number of elements in list
	try {
		list.remove(new int[] { 4, 1});
		fail("No exception thrown");
	} catch (IllegalArgumentException e) {
	}
	assertArrayEquals(":a:", list.getItems(), items);

	try {
		list.remove(new int[] { 3, 1, -1 });
		fail("No exception thrown");
	} catch (IllegalArgumentException e) {
	}
	assertArrayEquals(":a:", list.getItems(), items);

	list.setItems(items);
	assertEquals(list.getItemCount(), 4);

	try {
		list.remove(new int[] { -1, -1 });
		fail("No exception thrown");
	} catch (IllegalArgumentException e) {
	}
	assertArrayEquals(":b:", list.getItems(), items);

	try {
		list.remove(new int[] { -2, -1 });
		fail("No exception thrown");
	} catch (IllegalArgumentException e) {
	}
	assertArrayEquals(":c:", list.getItems(), items);

	list.setItems(items);
	assertEquals(list.getItemCount(), 4);

	list.remove(new int[] { 1, 1, 1 });
	assertArrayEquals(
		":d:",
		list.getItems(), new String[] { "text0", "text2", "text3" });

	list.setItems(items);
	assertEquals(list.getItemCount(), 4);

	list.remove(new int[] { 1, 3 });
	assertArrayEquals(":e:", list.getItems(), new String[] { "text0", "text2" });


	setSingleList();

	try {
		int[] indices = null;
		list.remove(indices);
		fail("No exception thrown for indices == null");
	} catch (IllegalArgumentException e) {
	}

	list.setItems(items);
	assertEquals(4, list.getItemCount());

	list.remove(new int[] { 1, 3 });
	assertArrayEquals(":f:", list.getItems(), new String[] { "text0", "text2" });


	list.setItems(items);
	assertEquals(4, list.getItemCount());

	list.remove(new int[] { 3, 1 });
	assertArrayEquals(":g:", list.getItems(), new String[] { "text0", "text2" });


	list.setItems(items);
	assertEquals(4, list.getItemCount());

	// index > number of elements in list
	try {
		list.remove(new int[] { 4, 1});
		fail("No exception thrown");
	} catch (IllegalArgumentException e) {
	}
	assertArrayEquals(":h:", list.getItems(), items);

	try {
		list.remove(new int[] { 3, 1, -1 });
		fail("No exception thrown");
	} catch (IllegalArgumentException e) {
	}
	assertArrayEquals(":h:", list.getItems(), items);


	list.setItems(items);
	assertEquals(4, list.getItemCount());

	try {
		list.remove(new int[] { -1, -1 });
		fail("No exception thrown");
	} catch (IllegalArgumentException e) {
	}

	assertArrayEquals(":i:", items, list.getItems());


	assertEquals(4, list.getItemCount());

	list.remove(new int[] { 1, 1, 1 });
	assertArrayEquals(":j:",
		new String[] { "text0", "text2", "text3" }, list.getItems());

}

@Test
public void test_removeAll() {
	String[] items = { "text1", "text2", "text3", "test2" };

	list.setItems(items);
	assertEquals(list.getItemCount(), 4);

	list.removeAll();
	assertEquals(list.getItemCount(), 0);
	list.removeAll();
	assertEquals(list.getItemCount(), 0);


	setSingleList();
	list.setItems(items);
	assertEquals(4, list.getItemCount());

	list.removeAll();
	assertEquals(0, list.getItemCount());


	setSingleList();
	assertEquals(0, list.getItemCount());
	list.removeAll();
	assertEquals(0, list.getItemCount());

}

@Test
public void test_removeI() {
	String[] items = { "text1", "text2", "text3" };

	list.setItems(items);
	assertEquals(list.getItemCount(), 3);

	try {
		list.remove(3);
		fail("No exception thrown");
	} catch (IllegalArgumentException e) {
	}
	assertEquals(list.getItemCount(), 3);

	try {
		list.remove(-1);
		fail("No exception thrown");
	} catch (IllegalArgumentException e) {
	}
	assertEquals(list.getItemCount(), 3);

	list.remove(1);
	assertEquals(list.getItemCount(), 2);
	assertEquals(list.getItem(1), "text3");

	list.setItems(items);
	assertEquals(list.getItemCount(), 3);

	try {
		list.remove(3, 4);
		fail("No exception thrown");
	} catch (IllegalArgumentException e) {
	}

	assertEquals(list.getItemCount(), 3);

	try {
		list.remove(3, 3);
		fail("No exception thrown");
	} catch (IllegalArgumentException e) {
	}

	assertEquals(list.getItemCount(), 3);

	list.remove(0);
	assertEquals(list.getItemCount(), 2);
	list.remove(0);
	assertEquals(list.getItemCount(), 1);
	assertEquals(list.getItem(0), "text3");
	list.remove(0);
	assertEquals(list.getItemCount(), 0);

	list.setItems(items);
	list.remove(1, 2);
	assertEquals(list.getItemCount(), 1);
	assertEquals(list.getItem(0), "text1");


	setSingleList();
	list.setItems(items);
	assertEquals(3, list.getItemCount());
	try {
		list.remove(3);
		fail("No exception thrown");
	} catch (IllegalArgumentException e) {
	}
	assertEquals(3, list.getItemCount());
	/////////////////////////////////////////////////
	try {
		list.remove(-1);
		fail("No exception thrown");
	} catch (IllegalArgumentException e) {
	}

	assertEquals(3, list.getItemCount());
	////////////////////////////////////////////////
	list.remove(1);
	assertEquals(2, list.getItemCount());
	//////////////////////////////////////////////////////
	assertTrue(list.getItem(1).equals("text3"));

	list.setItems(items);
	assertEquals(list.getItemCount(), 3);

	list.remove(0);
	assertEquals(list.getItemCount(), 2);
	list.remove(0);
	assertEquals(list.getItemCount(), 1);
	assertEquals(list.getItem(0), "text3");
	list.remove(0);
	assertEquals(list.getItemCount(), 0);

}

@Test
public void test_removeII() {
	String[] items = { "text1", "text2", "text3" };

	list.setItems(items);
	assertEquals(3, list.getItemCount());

	try {
		list.remove(3, 4);
		fail("No exception thrown");
	} catch (IllegalArgumentException e) {
	}

	assertEquals(3, list.getItemCount());

	try {
		list.remove(3, 3);
		fail("No exception thrown");
	} catch (IllegalArgumentException e) {
	}
	assertEquals(3, list.getItemCount());

	list.remove(0, 0);
	assertEquals(2, list.getItemCount());
	assertEquals("text3", list.getItem(1));

	list.setItems(items);
	assertEquals(3, list.getItemCount());

	try {
		list.remove(-1, 1);
		fail("No exception thrown for start index < 0");
	} catch (IllegalArgumentException e) {
	}
	assertEquals(3, list.getItemCount());

	try {
		list.remove(3, 4);
		fail("No exception thrown");
	} catch (IllegalArgumentException e) {
	}
	assertEquals(3, list.getItemCount());

	list.remove(0, 2);
	assertEquals(0, list.getItemCount());

	list.setItems(items);
	assertEquals(3, list.getItemCount());

	try {
		list.remove(3, 3);
		fail("No exception thrown");
	} catch (IllegalArgumentException e) {
	}
	assertEquals(3, list.getItemCount());

	list.remove(2, 0);
	assertEquals(3, list.getItemCount());


	setSingleList();

	list.setItems(items);
	assertEquals(3, list.getItemCount());
	//////////////////////////////////////////////////////////////
	try {
		list.remove(3, 4);
		fail("No exception thrown");
	} catch (IllegalArgumentException e) {
	}

	assertEquals(3, list.getItemCount());
	/////////////////////////////////////////////////////////
	try {
		list.remove(3, 3);
		fail("No exception thrown");
	} catch (IllegalArgumentException e) {
	}

	assertEquals(3, list.getItemCount());
	//////////////////////////////////////////////////////////////

	try {
		list.remove(-1, 1);
		fail("No exception thrown for start index < 0");
	} catch (IllegalArgumentException e) {
	}
	assertEquals(3, list.getItemCount());

	list.remove(1, 2);
	assertEquals(1, list.getItemCount());
	assertEquals("text1", list.getItem(0));


	list.setItems(items);
	assertEquals(3, list.getItemCount());

	try {
		list.remove(2, 10);
		fail("No exception thrown");
	} catch (IllegalArgumentException e) {
	}
	assertEquals(3, list.getItemCount());
	assertEquals("text2", list.getItem(1));

	list.remove(2, 0);
	assertEquals(3, list.getItemCount());
}

@Test
public void test_removeLjava_lang_String() {
	String[] items = { "text1", "text2", "text3", "test2" };

	list.setItems(items);
	assertEquals(list.getItemCount(), 4);

	try {
		list.remove((String) null);
		fail("No exception thrown");
	} catch (IllegalArgumentException e) {
	}
	assertEquals(list.getItemCount(), 4);

	try {
		list.remove("items989");
		fail("No exception thrown");
	} catch (IllegalArgumentException e) {
	}
	assertEquals(list.getItemCount(), 4);

	list.setItems(items);
	assertEquals(list.getItemCount(), 4);

	list.remove("text3");
	assertEquals(list.getItemCount(), 3);

	list.remove("text2");
	assertEquals(list.getItemCount(), 2);


	setSingleList();
	list.setItems(items);
	assertEquals(4, list.getItemCount());

	try {
		list.remove((String) null);
		fail("No exception thrown");
	} catch (IllegalArgumentException e) {
	}
	assertEquals(4, list.getItemCount());
	////////////////////////////////////////
	try {
		list.remove("items989");
		fail("No exception thrown");
	} catch (IllegalArgumentException e) {
	}
	assertEquals(4, list.getItemCount());


	assertEquals(4, list.getItemCount());

	list.remove("text3");
	assertEquals(3, list.getItemCount());

	list.remove("text2");
	assertEquals(2, list.getItemCount());

}

@Test
public void test_select$I() {
	try {
		list.select((int[]) null);
		fail("No exception thrown");
	} catch (IllegalArgumentException e) {
	}

	String[] items = { "item0", "item1", "item2", "item3" };
	list.setItems(items);

	list.select(new int[] { 0, 2 });
	assertArrayEquals(list.getSelectionIndices(), new int[] { 0, 2 });

	list.select(new int[] { 1, 3 });
	assertArrayEquals(list.getSelectionIndices(), new int[] { 0, 1, 2, 3 });

	list.select(new int[] { 1, 3 });
	assertArrayEquals(list.getSelectionIndices(), new int[] { 0, 1, 2, 3 });

	list.select(new int[] { 1 });
	assertArrayEquals(list.getSelectionIndices(), new int[] { 0, 1, 2, 3 });

	list.add("item4");

	list.select(new int[] { -1, 0, 1, 2, 3 });
	assertArrayEquals(list.getSelectionIndices(), new int[] { 0, 1, 2, 3 });

	list.deselectAll();
	list.select(new int[] { 1, 2, 3 });
	assertArrayEquals(list.getSelectionIndices(), new int[] { 1, 2, 3 });

	int[] ind = new int[32];
	for (int i = 0; i < ind.length; i++) ind[i] = i;
	list.select(ind);
	assertArrayEquals(list.getSelectionIndices(), new int[] { 0, 1, 2, 3, 4 });

	list.setSelection(new int[] {});
	list.select(new int[] { 1 });
	assertArrayEquals(list.getSelectionIndices(), new int[] { 1 });

	list.setSelection(new int[] {});
	list.select(new int[] { -1 });
	assertArrayEquals(list.getSelectionIndices(), new int[] {});

	list.deselectAll();
	items = list.getItems();

	select$I_helper(0, 3, new int[] { 0, 1, 2, 3 });
	select$I_helper(-1, 3, new int[] { 0, 1, 2, 3 });
	select$I_helper(-1, 30, new int[] { 0, 1, 2, 3, 4 });


	/*--- Single-select ---*/

	setSingleList();
	list.setItems(items);

	try {
		list.select((int[]) null);
		fail("No exception thrown");
	} catch (IllegalArgumentException e) {
	}

	list.select(new int[]{ -1 });
	assertArrayEquals(list.getSelectionIndices(), new int[] {});

	list.select(new int[] { 0 });
	assertArrayEquals(list.getSelectionIndices(), new int[] { 0 });

	list.select(new int[] { 1 });
	assertArrayEquals(list.getSelectionIndices(), new int[] { 1 });

	list.select(new int[] { 2 });
	assertArrayEquals(list.getSelectionIndices(), new int[] { 2 });

	list.select(new int[] { 3 });
	assertArrayEquals(list.getSelectionIndices(), new int[] { 3 });

	list.select(new int[] { 4 });
	assertArrayEquals(list.getSelectionIndices(), new int[] { 4 });

	list.select(new int[] { 5 });
	assertArrayEquals(list.getSelectionIndices(), new int[] { 4 });

	list.deselectAll();
	list.select(new int[]{ 0, 1, 2, 3 });
	assertArrayEquals(list.getSelectionIndices(), new int[] {});

	list.select(new int[]{ -1, 0, 1, 2, 3 });
	assertArrayEquals(list.getSelectionIndices(), new int[] {});

	int[] selection = new int[32];
	for (int i = 0; i < selection.length; i++) selection[i] = i;
	list.select(selection);
	assertArrayEquals(list.getSelectionIndices(), new int[] {});

	list.select(new int[]{ 1, 1, 1 });
	assertArrayEquals(list.getSelectionIndices(), new int[]{});
}

@Test
public void test_selectAll() {
	String[] items = { "text1", "text2", "text3", "test2" };

	list.setItems(items);
	assertEquals(list.getSelectionCount(), 0);
	list.selectAll();
	assertEquals(list.getSelectionCount(), 4);


	setSingleList();

	list.setItems(items);
	assertEquals(0, list.getSelectionCount());
	list.selectAll();
	assertEquals(0, list.getSelectionCount());

}

@Test
public void test_selectI() {
	String[] items = { "item0", "item1", "item2", "item3" };
	list.setItems(items);

	list.select(2);
	assertArrayEquals("select(2):", list.getSelectionIndices(), new int[] { 2 });

	list.select(1);
	assertArrayEquals("select(1):", list.getSelectionIndices(), new int[] { 1, 2 });

	list.select(3);
	assertArrayEquals(
		"select(3):",
		list.getSelectionIndices(), new int[] { 1, 2, 3 });

	list.select(5);
	assertArrayEquals(
		"select(5):",
		list.getSelectionIndices(), new int[] { 1, 2, 3 });

	list.deselectAll();
	list.select(0);
	assertArrayEquals("select(0):", list.getSelectionIndices(), new int[] { 0 });

	list.deselectAll();
	list.select(-1);
	assertArrayEquals("select(-1):", list.getSelectionIndices(), new int[] {});

	list.deselectAll();
	list.select(-2);
	assertArrayEquals("select(-2):", list.getSelectionIndices(), new int[] {});

	list.deselectAll();
	list.select(4);
	assertArrayEquals("select(4):", list.getSelectionIndices(), new int[] {});

	setSingleList();
	list.setItems(items);

	list.select(2);
	assertArrayEquals(list.getSelectionIndices(), new int[] { 2 });

	list.select(1);
	assertArrayEquals(list.getSelectionIndices(), new int[] { 1 });

	list.select(3);
	assertArrayEquals(list.getSelectionIndices(), new int[] { 3 });

	list.select(5);
	assertArrayEquals(list.getSelectionIndices(), new int[] { 3 });

	list.deselectAll();
	list.select(0);
	assertArrayEquals("select(0):", list.getSelectionIndices(), new int[] { 0 });

	list.deselectAll();
	list.select(-1);
	assertArrayEquals("select(-1):", list.getSelectionIndices(), new int[] {});

	list.deselectAll();
	list.select(-2);
	assertArrayEquals("select(-2):", list.getSelectionIndices(), new int[] {});

	list.deselectAll();
	list.select(4);
	assertArrayEquals("select(4):", list.getSelectionIndices(), new int[] {});
}

@Test
public void test_selectII() {
	list.select(0, 0);
	assertArrayEquals("empty list", list.getSelectionIndices(), new int[] {});

	list.select(0, 1);
	assertArrayEquals("empty list", list.getSelectionIndices(), new int[] {});

	list.select(-1, 0);
	assertArrayEquals("empty list", list.getSelectionIndices(), new int[] {});

	int number = 5;

	String[] items = new String[number];
	int[] empty = {};

	for (int i = 0; i < number; i++)
		items[i] = "item" + i;

	selectII_helper(items, 10, 1, empty);
	selectII_helper(items, 3, 1, empty);
	selectII_helper(items, -1, -1, empty);
	selectII_helper(items, 5, 5, empty);
	selectII_helper(items, 4, 5, new int[] { 4 });
	selectII_helper(items, -1, 0, new int[] { 0 });
	selectII_helper(items, 2, 4, new int[] { 2, 3, 4 });
	selectII_helper(items, 0, 3, new int[] { 0, 1, 2, 3 });
	selectII_helper(items, 1, 1, new int[] { 1 });
	selectII_helper(items, -1, 30, new int[] { 0, 1, 2, 3, 4 });
	selectII_helper(items, -1, 3, new int[] { 0, 1, 2, 3 });

	list.select(0);
	assertArrayEquals(list.getSelectionIndices(), new int[] { 0 });

	list.select(-10, -9);
	assertArrayEquals(list.getSelectionIndices(), new int[] { 0 });
	list.deselectAll();

	list.select(1000, 2000);
	assertEquals(list.getSelectionCount(), 0);

	list.deselectAll();
	list.select(1, 2);
	assertArrayEquals(list.getSelectionIndices(), new int[] { 1, 2 });

	list.select(1, 3);
	assertArrayEquals(list.getSelectionIndices(), new int[] { 1, 2, 3 });

	list.select(2, 2);
	assertArrayEquals(list.getSelectionIndices(), new int[] { 1, 2, 3 });

	list.select(3, 5);
	assertArrayEquals(list.getSelectionIndices(), new int[] { 1, 2, 3, 4 });


	setSingleList();
	list.select(0, 0);
	assertArrayEquals("empty list", list.getSelectionIndices(), new int[] {});

	list.select(0, 1);
	assertArrayEquals("empty list", list.getSelectionIndices(), new int[] {});

	list.select(-1, 0);
	assertArrayEquals("empty list", list.getSelectionIndices(), new int[] {});

	list.setItems(items);
	list.select(0);
	assertArrayEquals(list.getSelectionIndices(), new int[] { 0 });
	list.select(-10, -9);
	assertArrayEquals(list.getSelectionIndices(), new int[] { 0 });

	list.deselectAll();
	assertEquals(0, list.getSelectionCount());

	list.select(1000, 2000);
	assertEquals(0, list.getSelectionCount());

	list.deselectAll();
	assertEquals(0, list.getSelectionCount());

	list.select(0, 0);
	assertArrayEquals(list.getSelectionIndices(), new int[] { 0 });

	list.select(1, 1);
	assertArrayEquals(list.getSelectionIndices(), new int[] { 1 });

	list.select(2, 2);
	assertArrayEquals(list.getSelectionIndices(), new int[] { 2 });

	list.select(3, 3);
	assertArrayEquals(list.getSelectionIndices(), new int[] { 3 });

	list.select(4, 4);
	assertArrayEquals(list.getSelectionIndices(), new int[] { 4 });

	list.select(5, 5);
	assertArrayEquals(list.getSelectionIndices(), new int[] { 4 });

	list.deselectAll();
	list.select(5, 5);
	assertArrayEquals(list.getSelectionIndices(), new int[] {});

	list.select(1, 2);
	assertArrayEquals(list.getSelectionIndices(), new int[] {});

	list.select(1, 3);
	assertArrayEquals(list.getSelectionIndices(), new int[] {});

	list.select(3, 5);
	assertArrayEquals(list.getSelectionIndices(), new int[] {});

	selectII_helper(items, 0, 0, new int[]{0});
	selectII_helper(items, 1, 1, new int[]{1});
	selectII_helper(items, 2, 2, new int[]{2});
	selectII_helper(items, 3, 3, new int[]{3});
	selectII_helper(items, 4, 4, new int[]{4});
	selectII_helper(items, 5, 5, new int[]{});
	selectII_helper(items, 10, 1, new int[]{});
	selectII_helper(items, 3, 1, new int[]{});
	selectII_helper(items, -1, -1, new int[]{});
}

@Override
@Test
public void test_setFontLorg_eclipse_swt_graphics_Font() {
	if (SwtTestUtil.isGTK) {
		//TODO Fix GTK failure.
		if (SwtTestUtil.verbose) {
			System.out.println("Excluded test_setFontLorg_eclipse_swt_graphics_Font(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_List)");
		}
		return;
	}
	FontData fontData = list.getFont().getFontData()[0];
	int lineHeight;
	Font font;

	font = new Font(list.getDisplay(), fontData.getName(), 8, fontData.getStyle());
	list.setFont(font);
	lineHeight = list.getItemHeight();
	list.setFont(null);
	font.dispose();
	font = new Font(list.getDisplay(), fontData.getName(), 12, fontData.getStyle());
	list.setFont(font);
	assertEquals(font, list.getFont());
	assertTrue("itemHeight=" + list.getItemHeight() + ", lineHeight=" + lineHeight, list.getItemHeight() > lineHeight);
	list.setFont(null);
	font.dispose();
}

@Test
public void test_setItemILjava_lang_String() {
	String[] items = { "item0", "item1", "item2", "item3" };

	assertEquals(list.getItemCount(), 0);
	int[] cases = { -10, 0, 10 };
	for (int i = 0; i < cases.length; i++) {
		try {
			list.setItem(cases[i], null);
			fail("No exception thrown");
		} catch (IllegalArgumentException e) {
		}
	}
	assertEquals(list.getItemCount(), 0);

	for (int i = 0; i < cases.length; i++) {
		try {
			list.setItem(cases[i], "");
			fail("No exception thrown");
		} catch (IllegalArgumentException e) {
		}
	}
	assertEquals(list.getItemCount(), 0);

	int cases2[] = { 10, 15, 0 };
	for (int i = 0; i < cases2.length; i++) {
		try {
			list.setItem(cases2[i], "fred");
			fail("No exception thrown");
		} catch (IllegalArgumentException e) {
		}
		assertEquals(list.getItemCount(), 0);
	}

	list.setItems(items);
	list.setItem(1, "new1");
	assertArrayEquals(new String[] { "item0", "new1", "item2", "item3" }, list.getItems());

	setSingleList();
	assertEquals(0, list.getItemCount());
	for (int i = 0; i < cases.length; i++) {
		try {
			list.setItem(cases[i], null);
			fail("No exception thrown");
		} catch (IllegalArgumentException e) {
		}

	}


	setSingleList();
	for (int i = 0; i < cases.length; i++) {
		try {
			list.setItem(cases[i], "");
			fail("No exception thrown");
		} catch (IllegalArgumentException e) {
		}
	}

	assertEquals(0, list.getItemCount());


	setSingleList();
	for (int i = 0; i < cases2.length; i++) {
		try {
			list.setItem(cases2[i], "fred");
			fail("No exception thrown");
		} catch (IllegalArgumentException e) {
		}

		assertEquals(0, list.getItemCount());
	}

	list.setItems(items);
	list.setItem(1, "new1");
	assertArrayEquals(new String[] { "item0", "new1", "item2", "item3" }, list.getItems());

}

@Test
public void test_setItems$Ljava_lang_String() {
	try {
		list.setItems((String[])null);
		fail("No exception thrown for items == null");
	} catch (IllegalArgumentException e) {
	}

	// TODO An SWTError should never happen and should not
	// be part of the test case.  List should throw an
	// SWTException.
//	String[][] cases = { { null }, {
//			"dsada", null, "dsdasdasd" }
//	};
//	for (int i = 0; i < cases.length; i++) {
//		try {
//			list.setItems(cases[i]);
//			fail("No exception thrown for items not found");
//		} catch (SWTError e) {
//		}
//	}

	String[][] itemArr = { {
		}, {
			"" }, {
			"sdasd" }, {
			"sdasd", "323434" }
	};
	for (int i = 0; i < itemArr.length; i++) {
		list.setItems(itemArr[i]);
		assertArrayEquals(itemArr[i], list.getItems());
	}

	try {
		list.setItems((String[])null);
		fail("No exception thrown");
	} catch (IllegalArgumentException e) {
	}


	setSingleList();
	for (int i = 0; i < itemArr.length; i++) {
		list.setItems(itemArr[i]);
		assertArrayEquals("case:" + i, itemArr[i], list.getItems());
	}


	try {
		list.setItems((String[])null);
		fail("No exception thrown");
	} catch (IllegalArgumentException e) {
	}
}

@Test
public void test_setSelection$I() {
	int number = 8;
	for (int i = 0; i < number; i++)
		list.add("fred" + i);

	list.setSelection(new int [0]);
	assertArrayEquals("MULTI: setSelection(new int [0])", list.getSelectionIndices(), new int[0]);

	try {
		list.setSelection((int[]) null);
		fail("No exception thrown for MULTI: setSelection((int[]) null)");
	} catch (IllegalArgumentException e) {
	}

	list.setSelection(new int [] {2});
	assertArrayEquals("MULTI: setSelection(new int [] {2})", list.getSelectionIndices(), new int[] {2});
	assertEquals("MULTI: setSelection(new int [] {2}) getFocusIndex()", list.getFocusIndex(), 2);

	list.setSelection(new int [] {number});
	assertArrayEquals("MULTI: setSelection(new int [] {number})", list.getSelectionIndices(), new int[0]);

	list.setSelection(new int [] {3, 1, 5, 2});
	assertArrayEquals("MULTI: setSelection(new int [] {3, 1, 5, 2})", list.getSelectionIndices(), new int[] {1, 2, 3, 5});

	list.setSelection(new int [] {1, 0});
	assertArrayEquals("MULTI: setSelection(new int [] {1, 0})", list.getSelectionIndices(), new int[] {0, 1});

	list.setSelection(new int [] {-1, number});
	assertArrayEquals("MULTI: setSelection(new int [] {-1, number})", list.getSelectionIndices(), new int[0]);

	list.setSelection(new int [] {number - 1, number});
	assertArrayEquals("MULTI: setSelection(new int [] {number - 1, number})", list.getSelectionIndices(), new int[] {number - 1});
	assertEquals("MULTI: setSelection(new int [] {number - 1, number}) getFocusIndex()", list.getFocusIndex(), number - 1);

	list.setSelection(new int [] {-1, 0});
	assertArrayEquals("MULTI: setSelection(new int [] {-1, 0})", list.getSelectionIndices(), new int[] {0});

	list.setSelection(new int [] {0, 1, 2, 3, 5});
	assertArrayEquals("MULTI: setSelection(new int [] {0, 1, 2, 3, 5})", list.getSelectionIndices(), new int [] {0, 1, 2, 3, 5});

	int[] indices = new int [number];
	for (int i = 0; i < number; i++) {
		indices[i] = i;
	}
	list.setSelection(indices);
	assertArrayEquals("MULTI: setSelection(indices)", indices, list.getSelectionIndices());

	list.setSelection(new int [] {number, number});
	assertArrayEquals("MULTI: setSelection(new int [] {number, number})", new int[0], list.getSelectionIndices());

	list.setSelection(new int [] {number - 1, number - 1});
	assertArrayEquals("MULTI: setSelection(new int [] {number - 1, number - 1})", list.getSelectionIndices(), new int[] {number - 1});
	assertEquals("MULTI: setSelection(new int [] {number - 1, number - 1}) getFocusIndex()", list.getFocusIndex(), number - 1);

	list.setSelection(new int [] {0, number, 1});
	assertArrayEquals("MULTI: setSelection(new int [] {0, number, 1})", list.getSelectionIndices(), new int[] {0, 1});

	list.setSelection(new int [] {number - 1, 0, number - 2});
	assertArrayEquals("MULTI: setSelection(new int [] {number - 1, 0, number - 2})", list.getSelectionIndices(), new int[] {0, number - 2, number - 1});

	list.removeAll();

	list.setSelection(new int [0]);
	assertArrayEquals("EMPTY MULTI: setSelection(new int [0])", list.getSelectionIndices(), new int[0]);
	assertEquals("EMPTY MULTI: setSelection(new int [0]) getFocusIndex()", list.getFocusIndex(), -1);

	try {
		list.setSelection((int[]) null);
		fail("No exception thrown for EMPTY MULTI: setSelection((int[]) null)");
	} catch (IllegalArgumentException e) {
	}

	list.setSelection(new int [] {0});
	assertArrayEquals("EMPTY MULTI: setSelection(new int [] {0})", list.getSelectionIndices(), new int[0]);
	assertEquals("EMPTY MULTI: setSelection(new int [] {0}) getFocusIndex()", list.getFocusIndex(), -1);

	list.setSelection(new int [] {-1});
	assertArrayEquals("EMPTY MULTI: setSelection(new int [] {-1})", list.getSelectionIndices(), new int[0]);
	assertEquals("EMPTY MULTI: setSelection(new int [] {-1}) getFocusIndex()", list.getFocusIndex(), -1);

	list.setSelection(new int [] {0, 0});
	assertArrayEquals("EMPTY MULTI: setSelection(new int [] {0, 0})", list.getSelectionIndices(), new int[0]);
	assertEquals("EMPTY MULTI: setSelection(new int [] {0, 0}) getFocusIndex()", list.getFocusIndex(), -1);

	list.setSelection(new int [] {-1, 0});
	assertArrayEquals("EMPTY MULTI: setSelection(new int [] {-1, 0})", list.getSelectionIndices(), new int[0]);
	assertEquals("EMPTY MULTI: setSelection(new int [] {-1, 0}) getFocusIndex()", list.getFocusIndex(), -1);

	list.setSelection(new int [] {0, -1});
	assertArrayEquals("EMPTY MULTI: setSelection(new int [] {0, -1})", list.getSelectionIndices(), new int[0]);
	assertEquals("EMPTY MULTI: setSelection(new int [] {0, -1}) getFocusIndex()", list.getFocusIndex(), -1);


	setSingleList();
	for (int i = 0; i < number; i++)
		list.add("fred" + i);

	list.setSelection(new int [0]);
	assertArrayEquals("SINGLE: setSelection(new int [0])", list.getSelectionIndices(), new int[0]);

	try {
		list.setSelection((int[]) null);
		fail("No exception thrown for SINGLE: setSelection((int[]) null)");
	} catch (IllegalArgumentException e) {
	}

	list.setSelection(new int [] {2});
	assertArrayEquals("SINGLE: setSelection(new int [] {2})", list.getSelectionIndices(), new int[] {2});
	assertEquals("SINGLE: setSelection(new int [] {2}) getFocusIndex()", list.getFocusIndex(), 2);

	list.setSelection(new int [] {number});
	assertArrayEquals("SINGLE: setSelection(new int [] {number})", list.getSelectionIndices(), new int[0]);

	list.setSelection(new int [] {1, 0});
	assertArrayEquals("SINGLE: setSelection(new int [] {1, 0})", list.getSelectionIndices(), new int[] {});

	list.setSelection(new int [] {0, 1, 2, 3, 5});
	assertArrayEquals("SINGLE: setSelection(new int [] {0, 1, 2, 3, 5})", list.getSelectionIndices(), new int [] {});

	list.setSelection(new int [] {-1, number});
	assertArrayEquals("SINGLE: setSelection(new int [] {-1, number})", list.getSelectionIndices(), new int[0]);

	list.setSelection(new int [] {number - 1, number});
	assertArrayEquals("SINGLE: setSelection(new int [] {number - 1, number})", list.getSelectionIndices(), new int[] {});

	list.setSelection(new int [] {-1, 0});
	assertArrayEquals("SINGLE: setSelection(new int [] {-1, 0})", list.getSelectionIndices(), new int[] {});

	indices = new int [number];
	for (int i = 0; i < number; i++) {
		indices[i] = i;
	}
	list.setSelection(indices);
	assertArrayEquals("SINGLE: setSelection(indices)", list.getSelectionIndices(), new int[] {});

	list.setSelection(new int [] {number, number});
	assertArrayEquals("SINGLE: setSelection(new int [] {number, number})", list.getSelectionIndices(), new int[0]);

	list.setSelection(new int [] {number - 1, number - 1});
	assertArrayEquals("SINGLE: setSelection(new int [] {number - 1, number - 1})", list.getSelectionIndices(), new int[] {});

	list.setSelection(new int [] {0, number, 1});
	assertArrayEquals("SINGLE: setSelection(new int [] {0, number, 1})", list.getSelectionIndices(), new int[] {});

	list.setSelection(new int [] {number - 1, 0, number - 2});
	assertArrayEquals("SINGLE: setSelection(new int [] {number - 1, 0, number - 2})", list.getSelectionIndices(), new int[] {});

	list.removeAll();

	list.setSelection(new int [0]);
	assertArrayEquals("EMPTY SINGLE: setSelection(new int [0])", list.getSelectionIndices(), new int[0]);
	assertEquals("EMPTY SINGLE: setSelection(new int [0]) getFocusIndex()", list.getFocusIndex(), -1);

	try {
		list.setSelection((int[]) null);
		fail("No exception thrown for EMPTY SINGLE: setSelection((int[]) null)");
	} catch (IllegalArgumentException e) {
	}

	list.setSelection(new int [] {0});
	assertArrayEquals("EMPTY SINGLE: setSelection(new int [] {0})", list.getSelectionIndices(), new int[0]);
	assertEquals("EMPTY SINGLE: setSelection(new int [] {0}) getFocusIndex()", list.getFocusIndex(), -1);

	list.setSelection(new int [] {-1});
	assertArrayEquals("EMPTY SINGLE: setSelection(new int [] {-1})", list.getSelectionIndices(), new int[0]);
	assertEquals("EMPTY SINGLE: setSelection(new int [] {-1}) getFocusIndex()", list.getFocusIndex(), -1);

	list.setSelection(new int [] {0, 0});
	assertArrayEquals("EMPTY SINGLE: setSelection(new int [] {0, 0})", list.getSelectionIndices(), new int[0]);
	assertEquals("EMPTY SINGLE: setSelection(new int [] {0, 0}) getFocusIndex()", list.getFocusIndex(), -1);

	list.setSelection(new int [] {-1, 0});
	assertArrayEquals("EMPTY SINGLE: setSelection(new int [] {-1, 0})", list.getSelectionIndices(), new int[0]);
	assertEquals("EMPTY SINGLE: setSelection(new int [] {-1, 0}) getFocusIndex()", list.getFocusIndex(), -1);

	list.setSelection(new int [] {0, -1});
	assertArrayEquals("EMPTY SINGLE: setSelection(new int [] {0, -1})", list.getSelectionIndices(), new int[0]);
	assertEquals("EMPTY SINGLE: setSelection(new int [] {0, -1}) getFocusIndex()", list.getFocusIndex(), -1);
}

@Test
public void test_setSelection$Ljava_lang_String() {
	int number = 8;
	for (int i = 0; i < number; i++)
		list.add("fred " + i);

	list.setSelection(new String [0]);
	assertArrayEquals(list.getSelection(), new String[0]);
	if (SwtTestUtil.fCheckSWTPolicy) {
		assertEquals(list.getFocusIndex(), -1);
	}

	try {
		list.setSelection((String[]) null);
		fail("No exception thrown");
	} catch (IllegalArgumentException e) {
	}

	list.setSelection(new String [] {"fred 2"});
	assertArrayEquals(list.getSelection(), new String [] {"fred 2"});
	assertEquals(list.getFocusIndex(), 2);

	list.setSelection(new String [] {"fred " + number});
	assertArrayEquals(list.getSelection(), new String [0]);

	list.setSelection(new String [] {"fred 1", "fred 0"});
	assertArrayEquals(list.getSelection(), new String [] {"fred 0", "fred 1"});

	list.setSelection(new String [] {"fred -1", "fred " + number});
	assertArrayEquals(list.getSelection(), new String [0]);

	list.setSelection(new String [] {"fred " + (number - 1), "fred " + number});
	assertArrayEquals(list.getSelection(), new String [] {"fred " + (number - 1)});

	list.setSelection(new String [] {"fred -1", "fred 0"});
	assertArrayEquals(list.getSelection(), new String [] {"fred 0"});

	String[] items = new String [number];
	for (int i = 0; i < number; i++) {
		items[i] = "fred " + i;
	}
	list.setSelection(items);
	assertArrayEquals(list.getSelection(), items);

	list.setSelection(new String [] {"fred " + number, "fred " + number});
	assertArrayEquals(list.getSelection(), new String [0]);

	list.setSelection(new String [] {"fred " + (number - 1), "fred " + (number - 1)});
	assertArrayEquals(list.getSelection(), new String[] {"fred " + (number - 1)});

	list.setSelection(new String [] {"fred 0", "fred " + number, "fred 1"});
	assertArrayEquals(list.getSelection(), new String[] {"fred 0", "fred 1"});

	list.removeAll();

	list.setSelection(new String [0]);
	assertArrayEquals(list.getSelection(), new String[0]);
	assertEquals(list.getFocusIndex(), -1);

	try {
		list.setSelection((String[]) null);
		fail("No exception thrown");
	} catch (IllegalArgumentException e) {
	}

	list.setSelection(new String [] {"fred 0"});
	assertArrayEquals(list.getSelection(), new String[0]);
	assertEquals(list.getFocusIndex(), -1);

	list.setSelection(new String [] {"fred 0", "fred 0"});
	assertArrayEquals(list.getSelection(), new String[0]);
	assertEquals(list.getFocusIndex(), -1);


	setSingleList();
	for (int i = 0; i < number; i++)
		list.add("fred " + i);

	list.setSelection(new String [0]);
	assertArrayEquals(list.getSelection(), new String[0]);

	try {
		list.setSelection((String[]) null);
		fail("No exception thrown");
	} catch (IllegalArgumentException e) {
	}

	list.setSelection(new String [] {"fred 2"});
	assertArrayEquals(list.getSelection(), new String[] {"fred 2"});
	assertEquals(list.getFocusIndex(), 2);

	list.setSelection(new String [] {"fred " + number});
	assertArrayEquals(list.getSelection(), new String[0]);

	list.setSelection(new String [] {"fred 1", "fred 0"});
	assertArrayEquals(list.getSelection(), new String[] {});

	list.setSelection(new String [] {"fred -1", "fred " + number});
	assertArrayEquals(list.getSelection(), new String[0]);

	list.setSelection(new String [] {"fred " + (number - 1)});
	assertArrayEquals(list.getSelection(), new String[] {"fred " + (number - 1)});

	items = new String[number];
	for (int i = 0; i < number; i++) {
		items[i] = "fred " + i;
	}
	list.setSelection(items);
	assertArrayEquals(list.getSelection(), new String[] {});

	list.setSelection(new String [] {"fred " + number, "fred " + number});
	assertArrayEquals(list.getSelection(), new String[0]);

	list.setSelection(new String [] {"fred " + (number - 1), "fred " + (number - 1)});
	assertArrayEquals(list.getSelection(), new String[] {});

	list.setSelection(new String [] {"fred 0", "fred " + number, "fred 1"});
	assertArrayEquals(list.getSelection(), new String[] {});

	list.removeAll();

	list.setSelection(new String [0]);
	assertArrayEquals(list.getSelection(), new String[0]);
	assertEquals(list.getFocusIndex(), -1);

	try {
		list.setSelection((String[]) null);
		fail("No exception thrown");
	} catch (IllegalArgumentException e) {
	}

	list.setSelection(new String [] {"fred 0"});
	assertArrayEquals(list.getSelection(), new String[0]);
	assertEquals(list.getFocusIndex(), -1);

	list.setSelection(new String [] {"fred 0", "fred 0"});
	assertArrayEquals(list.getSelection(), new String[0]);
	assertEquals(list.getFocusIndex(), -1);
}

@Test
public void test_setSelectionI() {
	int number = 8;
	for (int i = 0; i < number; i++) {
		list.add("fred" + i);
	}

	list.setSelection(2);
	assertArrayEquals(list.getSelectionIndices(), new int[] {2});
	assertEquals(list.getFocusIndex(), 2);

	list.setSelection(-5);
	assertArrayEquals(list.getSelectionIndices(), new int[0]);

	list.setSelection(0);
	assertArrayEquals(list.getSelectionIndices(), new int[] {0});
	assertEquals(list.getFocusIndex(), 0);

	list.setSelection(number);
	assertArrayEquals(list.getSelectionIndices(), new int[0]);

	list.setSelection(number - 1);
	assertArrayEquals(list.getSelectionIndices(), new int[] {number - 1});

	list.setSelection(-1);
	assertArrayEquals(list.getSelectionIndices(), new int[0]);

	list.removeAll();

	list.setSelection(-2);
	assertArrayEquals(list.getSelectionIndices(), new int[0]);
	assertEquals(list.getFocusIndex(), -1);

	list.setSelection(0);
	assertArrayEquals(list.getSelectionIndices(), new int[0]);
	assertEquals(list.getFocusIndex(), -1);

	list.setSelection(-1);
	assertArrayEquals(list.getSelectionIndices(), new int[0]);
	assertEquals(list.getFocusIndex(), -1);

	setSingleList();

	for (int i = 0; i < number; i++) {
		list.add("fred" + i);
	}

	list.setSelection(2);
	assertArrayEquals(list.getSelectionIndices(), new int[] {2});
	assertEquals(list.getFocusIndex(), 2);

	list.setSelection(-5);
	assertArrayEquals(list.getSelectionIndices(), new int[0]);

	list.setSelection(0);
	assertArrayEquals(list.getSelectionIndices(), new int[] {0});
	assertEquals(list.getFocusIndex(), 0);

	list.setSelection(number);
	assertArrayEquals(list.getSelectionIndices(), new int[0]);

	list.setSelection(number - 1);
	assertArrayEquals(list.getSelectionIndices(), new int[] {number - 1});
	assertEquals(list.getFocusIndex(), number - 1);

	list.setSelection(-1);
	assertArrayEquals(list.getSelectionIndices(), new int[0]);

	list.removeAll();

	list.setSelection(0);
	assertArrayEquals(list.getSelectionIndices(), new int[0]);
	assertEquals(list.getFocusIndex(), -1);

	list.setSelection(-1);
	assertArrayEquals(list.getSelectionIndices(), new int[0]);
	assertEquals(list.getFocusIndex(), -1);

	list.setSelection(-2);
	assertArrayEquals(list.getSelectionIndices(), new int[0]);
	assertEquals(list.getFocusIndex(), -1);
}

@Test
public void test_setSelectionII() {
	int number = 8;
	String[] items = new String[number];
	for (int i = 0; i < number; i++)
		items[i] = "fred" + i;

	list.setItems(items);

	list.setSelection(1, 2);
	assertArrayEquals(list.getSelectionIndices(), new int[] {1, 2});

	list.setSelection(-3, -2);
	assertArrayEquals(list.getSelectionIndices(), new int[0]);

	list.setSelection(0, 1);
	assertArrayEquals(list.getSelectionIndices(), new int[] {0, 1});

	list.setSelection(-2, -1);
	assertArrayEquals(list.getSelectionIndices(), new int[0]);

	list.setSelection(number - 2, number - 1);
	assertArrayEquals(list.getSelectionIndices(), new int[] {number - 2, number - 1});

	list.setSelection(number - 1, number);
	assertArrayEquals(list.getSelectionIndices(), new int[] {number - 1});

	list.setSelection(-1, 0);
	assertArrayEquals(list.getSelectionIndices(), new int[] {0});

	list.setSelection(number, number + 1);
	assertArrayEquals(list.getSelectionIndices(), new int[0]);

	list.setSelection(0, 0);
	assertArrayEquals(list.getSelectionIndices(), new int[] {0});
	assertEquals(list.getFocusIndex(), 0);

	list.setSelection(2, 1);
	assertArrayEquals(list.getSelectionIndices(), new int[0]);

	list.setSelection(number - 1, number - 1);
	assertArrayEquals(list.getSelectionIndices(), new int[] {number - 1});
	assertEquals(list.getFocusIndex(), number - 1);

	list.setSelection(-1, -1);
	assertArrayEquals(list.getSelectionIndices(), new int[0]);

	list.removeAll();

	list.setSelection(-2, -1);
	assertArrayEquals(list.getSelectionIndices(), new int[0]);
	assertEquals(list.getFocusIndex(), -1);

	list.setSelection(-1, 0);
	assertArrayEquals(list.getSelectionIndices(), new int[0]);
	assertEquals(list.getFocusIndex(), -1);

	list.setSelection(0, 1);
	assertArrayEquals(list.getSelectionIndices(), new int[0]);
	assertEquals(list.getFocusIndex(), -1);

	list.setSelection(1, 0);
	assertArrayEquals(list.getSelectionIndices(), new int[0]);
	assertEquals(list.getFocusIndex(), -1);

	list.setSelection(0, -1);
	assertArrayEquals(list.getSelectionIndices(), new int[0]);
	assertEquals(list.getFocusIndex(), -1);


	setSingleList();
	list.setItems(items);

	list.setSelection(0, 0);
	assertArrayEquals(list.getSelectionIndices(), new int[] {0});
	assertEquals(list.getFocusIndex(), 0);

	list.setSelection(1, 1);
	assertArrayEquals(list.getSelectionIndices(), new int[] {1});
	assertEquals(list.getFocusIndex(), 1);

	list.setSelection(4, 4);
	assertArrayEquals(list.getSelectionIndices(), new int[] {4});
	assertEquals(list.getFocusIndex(), 4);

	list.setSelection(number - 1, number - 1);
	assertArrayEquals(list.getSelectionIndices(), new int[] {number - 1});
	assertEquals(list.getFocusIndex(), number - 1);

	list.setSelection(number, number);
	assertArrayEquals(list.getSelectionIndices(), new int[] {});

	list.setSelection(-3, -2);
	assertArrayEquals(list.getSelectionIndices(), new int[] {});

	list.setSelection(0, 1);
	assertArrayEquals(list.getSelectionIndices(), new int[] {});

	list.setSelection(-2, -1);
	assertArrayEquals(list.getSelectionIndices(), new int[] {});

	list.setSelection(number - 2, number - 1);
	assertArrayEquals(list.getSelectionIndices(), new int[] {});

	list.setSelection(number - 1, number);
	assertArrayEquals(list.getSelectionIndices(), new int[] {});

	list.setSelection(-1, 0);
	assertArrayEquals(list.getSelectionIndices(), new int[] {});

	list.setSelection(number, number + 1);
	assertArrayEquals(list.getSelectionIndices(), new int[] {});

	list.setSelection(2, 1);
	assertArrayEquals(list.getSelectionIndices(), new int[] {});

	list.setSelection(number - 1, number - 1);
	assertArrayEquals(list.getSelectionIndices(), new int[] {number - 1});
	assertEquals(list.getFocusIndex(), number - 1);

	list.setSelection(-1, -1);
	assertArrayEquals(list.getSelectionIndices(), new int[] {});
}

@Test
public void test_setTopIndexI() {
	if (SwtTestUtil.isGTK) {
		//TODO Fix GTK failure.
		if (SwtTestUtil.verbose) {
			System.out.println("Excluded test_setTopIndexI(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_List)");
		}
		return;
	}
	list.setTopIndex(3);
	assertEquals("MULTI: setTopIndex(3) in empty list", 0, list.getTopIndex());
	String[] items = { "item0", "item1", "item2", "item3" };
	list.setItems(items);
	for (int i = 0; i < items.length; i++) {
		list.setTopIndex(i);
		assertEquals("MULTI: setTopIndex(i=" + i + ")", i, list.getTopIndex());
	}


	setSingleList();
	list.setTopIndex(3);
	assertEquals("SINGLE: setTopIndex(3) in empty list", 0, list.getTopIndex());

	list.setItems(items);
	for (int i = 0; i < items.length; i++) {
		list.setTopIndex(i);
		assertEquals("SINGLE: setTopIndex(i=" + i + ")", i, list.getTopIndex());
	}

}

@Test
public void test_showSelection() {
	String[] items = { "item0", "item1", "item2", "item3" };
	list.setItems(items);
	list.setSelection(items);
	list.showSelection();


	setSingleList();

	list.setItems(items);
	list.setSelection(items);
	list.showSelection();
}

/* custom */
List list;
/**
 * Test if 'deselect(u, v)' is the same as 'for (i=u; i<=v; ++i) deselect(i);'
 */
protected void deselectII_helper(
	String[] items,
	int start,
	int end,
	int[] expectedIndices) {

	list.setItems(items);
	list.setSelection(items);

	list.deselect(start, end);
	assertArrayEquals(
		":(" + start + ", " + end + "):",
		expectedIndices, list.getSelectionIndices());

	list.setSelection(items);
	if ( 0 != (list.getStyle() & SWT.MULTI) ) {
		assertArrayEquals("setSelection(items):", items, list.getSelection());
	}

	for (int i = start; i <= end; ++i) {
		list.deselect(i);
	}
	assertArrayEquals(
		":(" + start + ", " + end + "):",
		expectedIndices, list.getSelectionIndices());

	list.deselectAll();
}
/**
 * Dispose of the main list and create a new, single-selection one.
 */
protected List setSingleList() {
	list.dispose();
	list = new List(shell, SWT.SINGLE);
	setWidget(list);
	return list;
}
/**
 * Similar to deselectII_helper, checks if select(u, v) is the same as
 * for (i=u; i<=v; ++i) select(i)
 */
protected void selectII_helper(
	String[] items,
	int start,
	int end,
	int[] expectedIndices) {
	list.setItems(items);
	list.select(start, end);
	assertArrayEquals(
		":(" + start + ", " + end + "):",
		expectedIndices, list.getSelectionIndices());

	list.deselectAll();
	assertArrayEquals("deselectAll:", list.getSelectionIndices(), new int[] {});

	for (int i = start; i <= end; i++) // <= on purpose
		list.select(i);

	assertArrayEquals(":(" + start + ", " + end + "):",
		expectedIndices, list.getSelectionIndices());

	list.deselectAll();
}
/**
 * Similar to deselectII_helper, checks if select(int []arr) gives the same
 * result as several individual select(int) calls. The int[] used for selection
 * will be filled all integers from start to end inclusive, in order.
 */
protected void select$I_helper(
	int start,
	int end,
	int[] expectedIndices) {
	int[] selection = new int[end - start + 1];
	for (int i = 0; i < selection.length; ++i) {
		selection[i] = i + start;
	}

	list.select(selection);

	assertArrayEquals(
		":(" + start + ", " + end + "):",
		expectedIndices, list.getSelectionIndices());

	list.deselectAll();
	assertArrayEquals("deselectAll:", list.getSelectionIndices(), new int[] {});

	for (int i = start; i <= end; i++) // <= on purpose
		list.select(i);

	assertArrayEquals(
		":(" + start + ", " + end + "):",
		expectedIndices, list.getSelectionIndices());

	list.deselectAll();
}

private void add() {
    list.add("this");
    list.add("is");
    list.add("SWT");
}

@Test
public void test_consistency_MouseSelection () {
    add();
    consistencyEvent(27, 10, 1, 0, ConsistencyUtility.MOUSE_CLICK);
}

@Test
public void test_consistency_KeySelection () {
    add();
    consistencyEvent(0, SWT.ARROW_DOWN, 0, 0, ConsistencyUtility.KEY_PRESS);
}

@Test
public void test_consistency_SpaceSelection () {
    add();
    consistencyEvent(' ', 32, 0, 0, ConsistencyUtility.KEY_PRESS);
}

@Test
public void test_consistency_DoubleClick () {
    add();
    consistencyEvent(27, 10, 1, 0, ConsistencyUtility.MOUSE_DOUBLECLICK);
}

@Test
public void test_consistency_MenuDetect () {
    add();
    consistencyEvent(27, 5, 3, 0, ConsistencyUtility.MOUSE_CLICK);
}
@Test
public void test_consistency_DragDetect () {
    add();
    consistencyEvent(20, 5, 30, 10, ConsistencyUtility.MOUSE_DRAG);
}
}
