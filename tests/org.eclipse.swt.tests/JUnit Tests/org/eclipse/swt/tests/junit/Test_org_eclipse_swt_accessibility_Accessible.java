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
 *     Red Hat Inc. - Adapt to JUnit 4.
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.swt.accessibility.Accessible;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleControlListener;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.accessibility.AccessibleListener;
import org.eclipse.swt.accessibility.AccessibleTextEvent;
import org.eclipse.swt.accessibility.AccessibleTextListener;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.accessibility.Accessible
 *
 * @see org.eclipse.swt.accessibility.Accessible
 */
public class Test_org_eclipse_swt_accessibility_Accessible {

@Before
public void setUp() {
	shell = new Shell();
	accessible = shell.getAccessible();
}

@After
public void tearDown() {
	assertFalse(shell.isDisposed());
	shell.dispose();
	assertTrue(shell.isDisposed());
}

@Test
public void test_addAccessibleControlListenerLorg_eclipse_swt_accessibility_AccessibleControlListener() {
	AccessibleControlListener listener = new AccessibleControlListener() {
		@Override
		public void getValue(AccessibleControlEvent e) {
		}
		@Override
		public void getChild(AccessibleControlEvent e) {
		}
		@Override
		public void getChildAtPoint(AccessibleControlEvent e) {
		}
		@Override
		public void getChildCount(AccessibleControlEvent e) {
		}
		@Override
		public void getChildren(AccessibleControlEvent e) {
		}
		@Override
		public void getDefaultAction(AccessibleControlEvent e) {
		}
		@Override
		public void getFocus(AccessibleControlEvent e) {
		}
		@Override
		public void getLocation(AccessibleControlEvent e) {
		}
		@Override
		public void getRole(AccessibleControlEvent e) {
		}
		@Override
		public void getSelection(AccessibleControlEvent e) {
		}
		@Override
		public void getState(AccessibleControlEvent e) {
		}
	};
	accessible.addAccessibleControlListener(listener);
	accessible.removeAccessibleControlListener(listener);
}

@Test
public void test_addAccessibleListenerLorg_eclipse_swt_accessibility_AccessibleListener() {
	AccessibleListener listener = new AccessibleListener() {
		@Override
		public void getName(AccessibleEvent e) {
		}
		@Override
		public void getDescription(AccessibleEvent e) {
		}
		@Override
		public void getHelp(AccessibleEvent e) {
		}
		@Override
		public void getKeyboardShortcut(AccessibleEvent e) {
		}
	};
	accessible.addAccessibleListener(listener);
	accessible.removeAccessibleListener(listener);
}

@Test
public void test_addAccessibleTextListenerLorg_eclipse_swt_accessibility_AccessibleTextListener() {
	AccessibleTextListener listener = new AccessibleTextListener() {
		@Override
		public void getSelectionRange(AccessibleTextEvent e) {
		}
		@Override
		public void getCaretOffset(AccessibleTextEvent e) {
		}
	};
	accessible.addAccessibleTextListener(listener);
	accessible.removeAccessibleTextListener(listener);
}

@Test
public void test_getControl() {
	assertEquals(shell, accessible.getControl());
}

/* custom */
private Shell shell;
private Accessible accessible;
}
