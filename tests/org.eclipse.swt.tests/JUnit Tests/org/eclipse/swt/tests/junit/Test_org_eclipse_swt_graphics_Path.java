/*******************************************************************************
 * Copyright (c) 2025 Yatta and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Yatta - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeFalse;

import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.PathData;
import org.eclipse.swt.widgets.Display;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.graphics.Path
 *
 * @see org.eclipse.swt.graphics.Path
 */
public class Test_org_eclipse_swt_graphics_Path {

	private Display display;

	@Before
	public void setUp() {
		display = Display.getDefault();
	}

	@Test
	public void createTransform() {
		Path path = new Path(display);
		if (path.isDisposed()) {
			fail("Constructor for Path didn't initialize");
		}
		path.dispose();
	}

	@Test
	public void testClonePath() {
		assumeFalse("Not currently working on macOS, cloning the path leads to not equal pathData.", SwtTestUtil.isCocoa);

		Display display = Display.getDefault();

		Path path = new Path(display);
		path.addArc(0, 0, 10, 10, 0, 90);
		path.addRectangle(10, 10, 50, 50);
		path.quadTo(30, 30, 20, 40);

		Path path2 = new Path(display);
		path2.addArc(0, 0, 30, 30, 0, 270);
		path.addPath(path2);

		PathData pathData = path.getPathData();

		// set flatness to null, so only cloning the path is tested
		Path clonedPath = new Path(display, path, 0);
		PathData clonedPathData = clonedPath.getPathData();

		assertArrayEquals(pathData.points, clonedPathData.points, 0.001f);
		path.dispose();
		path2.dispose();
		clonedPath.dispose();
	}

	@Test
	public void disposePath() {
		Path path = new Path(display);
		path.addArc(0, 0, 10, 10, 0, 90);
		if (path.isDisposed()) {
			fail("Path should not be in the disposed state");
		}

		// dispose twice as this is allowed
		for (int i = 0; i < 2; i++) {
			path.dispose();
			if (!path.isDisposed()) {
				fail("Path should be in the disposed state");
			}
		}
	}

	@Test
	public void testToString() {
		Path path = new Path(display);
		String s = path.toString();

		if (s == null || s.length() == 0) {
			fail("toString returns null or empty string");
		}

		path.addArc(0, 0, 10, 10, 0, 90);
		s = path.toString();

		if (s == null || s.length() == 0) {
			fail("toString returns null or empty string");
		}

		path.dispose();
		s = path.toString();

		if (s == null || s.length() == 0) {
			fail("toString returns null or empty string");
		}
	}
}
