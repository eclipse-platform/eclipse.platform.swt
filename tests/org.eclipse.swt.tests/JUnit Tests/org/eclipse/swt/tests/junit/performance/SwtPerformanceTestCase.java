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


import java.io.File;
import java.net.URL;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.test.performance.Dimension;
import org.eclipse.test.performance.Performance;
import org.eclipse.test.performance.PerformanceMeter;


public class SwtPerformanceTestCase extends TestCase {
	// used to specify verbose mode, if true unimplemented warning messages will
	// be written to System.out
	public static boolean verbose = false;

	public final static boolean isGTK = SWT.getPlatform().equals("gtk");
	public final static boolean isWindows = SWT.getPlatform().startsWith("win32");

	// allow specific image formats to be tested
	public static String[] imageFormats = new String[] {"bmp", "jpg", "gif", "png"};
	public static String[] imageFilenames = new String[] {"folder", "folderOpen", "target"};
	public static String[] transparentImageFilenames = new String[] {"transparent.png"};


public SwtPerformanceTestCase(String name) {
	super(name);
}

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

protected String getPath(String fileName) {
	String urlPath;

	String pluginPath = System.getProperty("PLUGIN_PATH");
	if (verbose) {
		System.out.println("PLUGIN_PATH <"+pluginPath+">");
	}
	if (pluginPath == null) {
		URL url = getClass().getClassLoader().getResource(fileName);
		if (url == null) {
			fail("URL == null for file " + fileName);
		}
		urlPath = url.getFile();
	} else {
		urlPath = pluginPath + "/data/" + fileName;
	}

	if (File.separatorChar != '/') urlPath = urlPath.replace('/', File.separatorChar);
	if (isWindows && urlPath.indexOf(File.separatorChar) == 0) urlPath = urlPath.substring(1);
	urlPath = urlPath.replaceAll("%20", " ");

	if (verbose) {
		System.out.println("Resolved file name for " + fileName + " = " + urlPath);
	}
	return urlPath;
}

}
