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

import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Widget;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Scrollable
 *
 * @see org.eclipse.swt.widgets.Scrollable
 */
public class Test_org_eclipse_swt_widgets_Scrollable extends Test_org_eclipse_swt_widgets_Control {
	
public Test_org_eclipse_swt_widgets_Scrollable(String name) {
	super(name);
}

@Override
public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
	// abstract class
}

public void test_computeTrimIIII() {
	scrollable.computeTrim(0, 0, 0, 0);
}

public void test_getClientArea() {
	scrollable.getClientArea();
}

public void test_getHorizontalBar() {
	scrollable.getHorizontalBar();
}

public void test_getVerticalBar() {
	scrollable.getVerticalBar();
}

/* custom */
Scrollable scrollable;

@Override
protected void setWidget(Widget w) {
	scrollable = (Scrollable)w;
	super.setWidget(w);
}
}
