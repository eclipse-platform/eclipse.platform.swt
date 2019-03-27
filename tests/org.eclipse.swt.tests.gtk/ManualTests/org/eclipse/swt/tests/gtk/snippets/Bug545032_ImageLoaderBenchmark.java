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
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import java.io.File;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;

public class Bug545032_ImageLoaderBenchmark {
	// Absolute path to a directory containing benchmark images
	static final String BENCHMARK_DIR = new String("/home/xiyan/Pictures/benchmarks/");

	public static void main(String[] args) {
		final File folder = new File(BENCHMARK_DIR);

		ImageLoader loader = new ImageLoader();

		for (File fileEntry : folder.listFiles()) {
			String filePath = fileEntry.getAbsolutePath();
			long startTime = System.nanoTime();
			ImageData [] imgData = loader.load(filePath);
			long endTime = System.nanoTime();
			long duration = (endTime - startTime) / 1000000;
			if (imgData != null && imgData.length > 0) {
				ImageData img = imgData[0];
				System.out.println("Loading " + fileEntry.getName() + " (" + img.width + "x" + img.height + ") takes " + duration + "ms");
			}
		}
	}
}
