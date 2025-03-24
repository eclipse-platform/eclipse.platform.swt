/*******************************************************************************
 * Copyright (c) 2000, 2013 IBM Corporation and others.
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
package org.eclipse.swt.tests.junit.performance;

import org.eclipse.swt.SWT;
import org.eclipse.test.performance.Dimension;
import org.eclipse.test.performance.Performance;
import org.eclipse.test.performance.PerformanceMeter;

public class SwtPerformanceTestCase {

	public final static boolean isGTK = SWT.getPlatform().equals("gtk");
	public final static boolean isWindows = SWT.getPlatform().startsWith("win32");

protected PerformanceMeter createMeter(String id) {
	Performance performance = Performance.getDefault();
	String scenarioId = "org.eclipse.swt.test." + id;
	PerformanceMeter meter = performance.createPerformanceMeter(scenarioId);
	performance.tagAsSummary(meter, id, Dimension.ELAPSED_PROCESS);
	return meter;
}

protected PerformanceMeter createMeterWithoutSummary(String id) {
	Performance performance = Performance.getDefault();
	String scenarioId = "org.eclipse.swt.test." + id;
	PerformanceMeter meter = performance.createPerformanceMeter(scenarioId);
	return meter;
}

protected void disposeMeter(PerformanceMeter meter) {
	try {
		meter.commit();
		Performance.getDefault().assertPerformance(meter);
	} finally {
		meter.dispose();
	}
}

}
