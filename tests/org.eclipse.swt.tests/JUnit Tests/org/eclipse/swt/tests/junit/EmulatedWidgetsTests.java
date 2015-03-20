/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Red Hat Inc. - Adapt to JUnit 4.
 *******************************************************************************/
package org.eclipse.swt.tests.junit;


import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Suite for testing widgets that have been emulated on Motif.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
	Test_org_eclipse_swt_widgets_ColorDialog.class,
	Test_org_eclipse_swt_widgets_FontDialog.class,	
	Test_org_eclipse_swt_widgets_TabFolder.class,
	Test_org_eclipse_swt_widgets_TabItem.class,
	Test_org_eclipse_swt_widgets_Table.class,
	Test_org_eclipse_swt_widgets_TableItem.class,
	Test_org_eclipse_swt_widgets_TableColumn.class,	
	Test_org_eclipse_swt_widgets_Tree.class,
	Test_org_eclipse_swt_widgets_TreeItem.class,
})
public class EmulatedWidgetsTests {

public static void main(String[] args) {
	JUnitCore.main(EmulatedWidgetsTests.class.getName());
}
}
