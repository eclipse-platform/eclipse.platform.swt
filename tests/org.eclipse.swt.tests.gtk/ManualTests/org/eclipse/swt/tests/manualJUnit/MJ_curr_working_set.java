/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others. All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 */
package org.eclipse.swt.tests.manualJUnit;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

/**
 * During development/testing, sometimes when you want to run specific set of jUnits, you can move them here temporarily.
 */
@FixMethodOrder(MethodSorters.JVM)  // To make it easier for human to go through the tests. Same order makes tests easier to recognize.
public class MJ_curr_working_set extends MJ_root {


}
