/*******************************************************************************
 * Copyright (c) 2000, 2019 IBM Corporation and others.
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

import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Composite
 *
 * @see org.eclipse.swt.widgets.Composite
 */
public class Test_org_eclipse_swt_widgets_Composite extends Test_org_eclipse_swt_widgets_Scrollable {

Composite composite;

@Override
@Before
public void setUp() {
	super.setUp();
	composite = new Composite(shell, 0);
	super.setWidget(composite);
}

@Override
@Test
public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
	try {
		composite = new Composite(null, 0);
		fail("No exception thrown");
	}
	catch (IllegalArgumentException e) {
	}

	int[] cases = {SWT.H_SCROLL, SWT.V_SCROLL, SWT.H_SCROLL | SWT.V_SCROLL};
	for (int style : cases)
		composite = new Composite(shell, style);
}

@Test
public void test_getChildren() {
	assertArrayEquals(":a:", new Control[]{}, composite.getChildren());
	Composite c1 = new Composite(composite, 0);
	assertArrayEquals(":b:", new Control[]{c1}, composite.getChildren());

	List c2 = new List(composite, 0);
	assertArrayEquals(":c:", new Control[]{c1, c2}, composite.getChildren());

	Button c3 = new Button(composite, 0);
	assertArrayEquals(":d:", new Control[]{c1, c2, c3}, composite.getChildren());

	c2.dispose();
	assertArrayEquals(":e:", new Control[]{c1, c3}, composite.getChildren());

	Control[] children = composite.getChildren();
	for (Control element : children)
		element.dispose();

	assertArrayEquals(":f:", new Control[]{}, composite.getChildren());
}


@Test
public void test_setVisibility_and_sizing() {

	// Note: This test needs it's own shell/composite because original bug is only
	// reproduced if composite has style SWT.BORDER. (the setup() creates one without border).
	Shell visibilityShell = new Shell();
	visibilityShell.setSize(500, 500);
	Composite visibilityComposite = new Composite(visibilityShell, SWT.BORDER);

	visibilityShell.setLayout(new FillLayout());
	visibilityComposite.setLayout(new FillLayout());

	Button button = new Button(visibilityComposite, SWT.PUSH);
	button.setText("Test Button");

	visibilityComposite.setVisible(false);
	visibilityComposite.setVisible(true);

	visibilityShell.layout();

// 	  // Useful for troubleshooting:											   // should be like:
//    System.out.println("Shell size : " + visibilityShell.getSize().toString());  // >> 500,500
//    System.out.println("SComp size : " + visibilityComposite.getSize().toString()); // >> 500, 463
//    System.out.println("Button size: " + button.getSize().toString());			  // >> 500,463

	Point compSize = visibilityComposite.getSize();
	assertTrue("Composite should be aprox 500 by 463 px, but instead it is: " + compSize.toString(),
			compSize.x > 100 && compSize.y > 100); // If this is 1x1 or 0x0 then there was some fault in layout.
	visibilityShell.dispose();
}

@Test
public void test_setFocus_toChild_afterOpen() throws InterruptedException {
	if (SwtTestUtil.isCocoa) {
		//TODO Fix Cocoa failure.
		if (SwtTestUtil.verbose) {
			System.out.println("Excluded test_setFocus_toChild_afterOpen(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Composite)");
		}
		return;
	}
	Text focusChild = new Text(composite, SWT.NONE);
	shell.open();
	// Wait for the shell to become active
	processEvents(500, () -> shell.getDisplay().getActiveShell() == shell);
	assertEquals(shell, shell.getDisplay().getActiveShell());
	composite.setFocus();
	assertTrue("First child widget should have focus", focusChild.isFocusControl());
}

@Test
public void test_setFocus_toChild_beforeOpen() throws InterruptedException {
	if (SwtTestUtil.isCocoa) {
		//TODO Fix Cocoa failure.
		if (SwtTestUtil.verbose) {
			System.out.println("Excluded test_setFocus_toChild_beforeOpen(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Composite)");
		}
		return;
	}
	Text focusChild = new Text(composite, SWT.NONE);
	composite.setFocus();
	shell.open();
	// Wait for the shell to become active
	processEvents(500, () -> shell.getDisplay().getActiveShell() == shell);
	assertEquals(shell, shell.getDisplay().getActiveShell());
	assertTrue("First child widget should have focus", focusChild.isFocusControl());
}

@Test
public void test_setFocus_withInvisibleChild() throws InterruptedException {
	final AtomicReference<Boolean> wasSetFocusCalledOnInvisibleChildWidget = new AtomicReference<>(false);
	Composite invisibleChildWidget = new Composite(composite, SWT.NONE) {
		@Override
		public boolean setFocus() {
			wasSetFocusCalledOnInvisibleChildWidget.set(true);
			return super.setFocus();
		}
	};
	invisibleChildWidget.setVisible(false);
	shell.open();
	// Wait for the shell to become active
	processEvents(500, () -> shell.getDisplay().getActiveShell() == shell);
	assertEquals(shell, shell.getDisplay().getActiveShell());

	composite.setFocus();
	assertFalse("Composite should not try to set focus on invisible child", wasSetFocusCalledOnInvisibleChildWidget.get());
}

@Test
public void test_setFocus_withVisibleAndInvisibleChild() throws InterruptedException {
	final AtomicReference<Boolean> wasSetFocusCalledOnInvisibleChildWidget = new AtomicReference<>(false);
	Composite invisibleChildWidget = new Composite(composite, SWT.NONE) {
		@Override
		public boolean setFocus() {
			wasSetFocusCalledOnInvisibleChildWidget.set(true);
			return super.setFocus();
		}
	};
	invisibleChildWidget.setVisible(false);
	Composite visibleChildWidget = new Composite(composite, SWT.NONE);
	shell.open();
	// Wait for the shell to become active
	processEvents(500, () -> shell.getDisplay().getActiveShell() == shell);
	assertEquals(shell, shell.getDisplay().getActiveShell());

	composite.setFocus();
	assertFalse("Composite should not try to set focus on invisible child", wasSetFocusCalledOnInvisibleChildWidget.get());
	assertTrue("Visible child widget should have focus", getElementExpectedToHaveFocusAfterSetFocusOnParent(visibleChildWidget).isFocusControl());
}

@Test
public void test_setTabList$Lorg_eclipse_swt_widgets_Control() {
	Button button1 = new Button(composite, SWT.PUSH);
	Button button2 = new Button(composite, SWT.PUSH);
	Control[] tablist = new Control[] {button1, button2};
	composite.setTabList(tablist);
	assertArrayEquals(tablist, composite.getTabList());
	button1.dispose();
	button2.dispose();
}

protected Composite getElementExpectedToHaveFocusAfterSetFocusOnParent(Composite visibleChild) {
	return visibleChild;
}

/* custom */
@Override
protected void setWidget(Widget w) {
	if (composite != null)
		composite.dispose();
	composite = (Composite)w;
	super.setWidget(w);
}
}
